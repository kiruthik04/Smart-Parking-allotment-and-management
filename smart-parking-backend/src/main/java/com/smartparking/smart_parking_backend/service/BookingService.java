package com.smartparking.smart_parking_backend.service;

import com.smartparking.smart_parking_backend.dto.BookingRequestDTO;
import com.smartparking.smart_parking_backend.dto.BookingResponseDTO;
import com.smartparking.smart_parking_backend.exception.ResourceNotFoundException;
import com.smartparking.smart_parking_backend.exception.SlotAlreadyBookedException;
import com.smartparking.smart_parking_backend.model.*;
import com.smartparking.smart_parking_backend.model.PaymentStatus; // Explicit import
import com.smartparking.smart_parking_backend.repository.BookingRepository;
import com.smartparking.smart_parking_backend.repository.ParkingSlotRepository;
import com.smartparking.smart_parking_backend.repository.VehicleRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import com.smartparking.smart_parking_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

        private final BookingRepository bookingRepository;
        private final ParkingSlotRepository slotRepository;
        private final VehicleRepository vehicleRepository;
        private final UserRepository userRepository;

        public BookingService(
                        BookingRepository bookingRepository,
                        ParkingSlotRepository slotRepository,
                        VehicleRepository vehicleRepository,
                        UserRepository userRepository) {
                this.bookingRepository = bookingRepository;
                this.slotRepository = slotRepository;
                this.vehicleRepository = vehicleRepository;
                this.userRepository = userRepository;
        }

        // =====================================================
        // BOOK SLOT (AUTOMATED – NO OWNER / ADMIN APPROVAL)
        // =====================================================
        @Transactional
        public BookingResponseDTO bookSlot(BookingRequestDTO dto) {
                // 🔐 Authenticated user (fetch email from principal)
                String email = SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName();
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                // 🔒 Lock slot row (prevents race condition)
                ParkingSlot slot = slotRepository.findSlotForUpdate(dto.getSlotId())
                                .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

                // 🚫 Slot disabled (maintenance / reserved)
                if (!slot.isEnabled()) {
                        throw new RuntimeException("Slot temporarily unavailable");
                }

                // 🚗 Vehicle Creation (Inline)
                Vehicle vehicle = new Vehicle();
                vehicle.setVehicleNumber(dto.getVehicleNumber());
                vehicle.setVehicleModel(dto.getVehicleModel());
                vehicle.setVehicleType(dto.getVehicleType());
                vehicle.setOwnerName(dto.getOwnerName()); // Optional
                vehicle.setUser(user);

                vehicleRepository.save(vehicle);

                VehicleType vehicleType = vehicle.getVehicleType();

                // 📊 Count active bookings (AUTOMATED AVAILABILITY)
                long activeCount = bookingRepository.countByParkingSlotAndVehicle_VehicleTypeAndActiveTrue(
                                slot,
                                vehicleType);

                int capacity = 0;
                if (vehicleType == VehicleType.CAR) {
                        capacity = slot.getCarCapacity();
                } else if (vehicleType == VehicleType.BIKE) {
                        capacity = slot.getBikeCapacity();
                } else if (vehicleType == VehicleType.TRUCK) {
                        capacity = slot.getTruckCapacity();
                }

                if (activeCount >= capacity) {
                        throw new SlotAlreadyBookedException(
                                        vehicleType + " slots are fully occupied at this location");
                }

                // 🆕 Create booking (ACTIVE immediately)
                Booking booking = new Booking();
                booking.setUser(user);
                booking.setVehicle(vehicle);
                booking.setParkingSlot(slot);
                booking.setStartTime(LocalDateTime.now());
                booking.setActive(true);

                Booking saved = bookingRepository.save(booking);

                return new BookingResponseDTO(
                                saved.getId(),
                                slot.getId(),
                                slot.getLocation(),
                                vehicle.getVehicleNumber(),
                                vehicle.getVehicleModel(),
                                vehicleType,
                                user.getId(),
                                user.getName(),
                                user.getPhone(),
                                slot.getUpiId(), // Added slotUpiId
                                saved.getStartTime(),
                                saved.getEndTime(),
                                saved.isActive(),
                                0.0, // Initial price
                                PaymentStatus.PENDING // Initial payment status
                );
        }

        // =====================================================
        // END BOOKING (USER)
        // =====================================================
        @Transactional
        public BookingResponseDTO endBooking(Long bookingId) {

                Booking booking = bookingRepository.findById(bookingId)
                                .orElseThrow(() -> new RuntimeException("Booking not found"));

                if (!booking.isActive()) {
                        throw new RuntimeException("Booking already ended");
                }

                booking.setEndTime(LocalDateTime.now());
                booking.setActive(false);

                // ⏱ Calculate duration in minutes for precise billing
                // We use Duration.between() to get the exact time difference
                long minutes = java.time.Duration
                                .between(booking.getStartTime(), booking.getEndTime())
                                .toMinutes();

                // Minimum 1 minute charge to avoid 0.00 for immediate close
                // Even if booked for 1 second, we charge for 1 minute
                if (minutes < 1) {
                        minutes = 1;
                }

                ParkingSlot slot = booking.getParkingSlot();
                VehicleType type = booking.getVehicle().getVehicleType();

                // Select the correct price based on vehicle type (CAR, BIKE, TRUCK)
                double pricePerHour = switch (type) {
                        case CAR -> slot.getCarPricePerHour();
                        case BIKE -> slot.getBikePricePerHour();
                        case TRUCK -> slot.getTruckPricePerHour();
                };

                // 🧮 Calculate price: (minutes / 60.0) gives us hours in decimal (e.g., 1.5
                // hours)
                // We multiply that by pricePerHour to get the exact amount
                double totalPrice = (minutes / 60.0) * pricePerHour;
                booking.setTotalPrice(totalPrice);
                // Ensure payment status is PENDING if not already paid (future proofing)
                if (booking.getPaymentStatus() == null) {
                        booking.setPaymentStatus(PaymentStatus.PENDING);
                }

                // Save the updated booking to the database
                Booking saved = bookingRepository.save(booking);
                return mapToDTO(saved);
        }

        // =====================================================
        // PROCESS PAYMENT (NEW)
        // =====================================================
        @Transactional
        public BookingResponseDTO processPayment(Long bookingId, String paymentRef) {
                Booking booking = bookingRepository.findById(bookingId)
                                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

                if (booking.isActive()) {
                        throw new RuntimeException("Cannot pay for an active booking. End it first.");
                }

                if (booking.getPaymentStatus() == PaymentStatus.COMPLETED) {
                        return mapToDTO(booking);
                }

                // Save Manual Payment Reference (UTR)
                if (paymentRef != null && !paymentRef.trim().isEmpty()) {
                        booking.setPaymentReference(paymentRef);
                }

                // Mark COMPLETED (In a real app, this would be PENDING until admin
                // verification)
                booking.setPaymentStatus(PaymentStatus.COMPLETED);
                Booking saved = bookingRepository.save(booking);

                return mapToDTO(saved);
        }

        private BookingResponseDTO mapToDTO(Booking booking) {
                return new BookingResponseDTO(
                                booking.getId(),
                                booking.getParkingSlot().getId(),
                                booking.getParkingSlot().getLocation(),
                                booking.getVehicle().getVehicleNumber(),
                                booking.getVehicle().getVehicleModel(),
                                booking.getVehicle().getVehicleType(),
                                booking.getUser().getId(),
                                booking.getUser().getName(),
                                booking.getUser().getPhone(), // Added phone number
                                booking.getParkingSlot().getUpiId(), // Added slotUpiId
                                booking.getStartTime(),
                                booking.getEndTime(),
                                booking.isActive(),
                                booking.getTotalPrice(),
                                booking.getPaymentStatus());
        }

        // =====================================================
        // GET BOOKINGS OF LOGGED-IN USER
        // =====================================================
        @Transactional(readOnly = true)
        public List<BookingResponseDTO> getMyBookings() {
                String email = SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName();
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                return bookingRepository.findByUser(user)
                                .stream()
                                .map(this::mapToDTO)
                                .toList();
        }

        // =====================================================
        // GET BOOKINGS FOR OWNER (INCOMING BOOKINGS)
        // =====================================================
        @Transactional(readOnly = true)
        public List<BookingResponseDTO> getOwnerBookings() {
                String email = SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName();

                // We don't necessarily need the User object if we trust the Principal email,
                // but checking existence is good practice.
                if (!userRepository.existsByEmail(email)) {
                        throw new ResourceNotFoundException("User not found");
                }

                return bookingRepository.findByParkingSlot_Owner_Email(email)
                                .stream()
                                .map(this::mapToDTO)
                                .toList();
        }
}
