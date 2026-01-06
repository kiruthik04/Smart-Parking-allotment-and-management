package com.smartparking.smart_parking_backend.service;

import com.smartparking.smart_parking_backend.dto.ParkingSlotRequestDTO;
import com.smartparking.smart_parking_backend.dto.ParkingSlotResponseDTO;
import com.smartparking.smart_parking_backend.dto.SlotAvailabilityResponseDTO;
import com.smartparking.smart_parking_backend.model.User;
import com.smartparking.smart_parking_backend.model.Role;
import com.smartparking.smart_parking_backend.model.ParkingSlot;
import com.smartparking.smart_parking_backend.model.VehicleType;
import com.smartparking.smart_parking_backend.repository.BookingRepository;
import com.smartparking.smart_parking_backend.repository.ParkingSlotRepository;
import com.smartparking.smart_parking_backend.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import com.smartparking.smart_parking_backend.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Service
public class ParkingSlotService {

        private final ParkingSlotRepository slotRepository;
        private final BookingRepository bookingRepository;
        private final UserRepository userRepository;

        public ParkingSlotService(
                        ParkingSlotRepository slotRepository,
                        BookingRepository bookingRepository,
                        UserRepository userRepository) {
                this.slotRepository = slotRepository;
                this.bookingRepository = bookingRepository;
                this.userRepository = userRepository;
        }

        // -------------------------------
        // CREATE PARKING SLOT
        // -------------------------------
        // to guarantee location isnt blank, priceperhour isnt negative. Input parameter
        @Transactional
        public ParkingSlotResponseDTO createSlot(ParkingSlotRequestDTO dto) {
                // 🔐 Get logged-in user from JWT
                String email = SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName();
                User owner = userRepository.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                // 🔒 Only OWNER can create slots
                if (owner.getRole() != Role.OWNER) {
                        throw new RuntimeException("Only owners can create parking slots");
                }

                // blank instance of entity class
                ParkingSlot slot = new ParkingSlot();
                slot.setLocation(dto.getLocation());
                slot.setLatitude(dto.getLatitude());
                slot.setLongitude(dto.getLongitude());
                slot.setCarCapacity(dto.getCarCapacity());
                slot.setBikeCapacity(dto.getBikeCapacity());
                slot.setTruckCapacity(dto.getTruckCapacity());
                slot.setCarPricePerHour(dto.getCarPricePerHour());
                slot.setBikePricePerHour(dto.getBikePricePerHour());
                slot.setTruckPricePerHour(dto.getTruckPricePerHour());
                // New fields
                slot.setImageUrl(dto.getImageUrl());
                slot.setAddress(dto.getAddress());
                slot.setCity(dto.getCity());
                slot.setReviews(dto.getReviews());
                slot.setUpiId(dto.getUpiId());
                slot.setStatus("PENDING"); // Explicitly set to pending
                slot.setOwner(owner);
                ParkingSlot saved = slotRepository.save(slot);
                return new ParkingSlotResponseDTO(
                                saved.getId(),
                                saved.getLocation(),
                                saved.getLatitude(),
                                saved.getLongitude(),
                                saved.getCarCapacity(),
                                saved.getBikeCapacity(),
                                saved.getTruckCapacity(),
                                saved.getImageUrl(),
                                saved.getAddress(),
                                saved.getCity(),
                                saved.getReviews(),
                                saved.getUpiId(),
                                saved.isEnabled(),
                                saved.getCarPricePerHour(),
                                saved.getBikePricePerHour(),
                                saved.getTruckPricePerHour());
        }

        // -----------------------------------
        // GET ALL PARKING SLOTS (DTO)
        // -----------------------------------
        @Transactional(readOnly = true)
        public List<ParkingSlotResponseDTO> getAllSlots() {
                return slotRepository.findAll()
                                .stream()
                                .map(slot -> new ParkingSlotResponseDTO(
                                                slot.getId(),
                                                slot.getLocation(),
                                                slot.getLatitude(),
                                                slot.getLongitude(),
                                                slot.getCarCapacity(),
                                                slot.getBikeCapacity(),
                                                slot.getTruckCapacity(),
                                                slot.getImageUrl(),
                                                slot.getAddress(),
                                                slot.getCity(),
                                                slot.getReviews(),
                                                slot.getUpiId(),
                                                slot.isEnabled(),
                                                slot.getCarPricePerHour(),
                                                slot.getBikePricePerHour(),
                                                slot.getTruckPricePerHour()))
                                .toList();
        }

        // MAP: Get all parking slots (ENTITY)
        @Transactional(readOnly = true)
        public List<ParkingSlot> getAllSlotsForMap() {
                return slotRepository.findAll();
        }

        @Transactional
        public ParkingSlotResponseDTO setSlotEnabled(Long slotId, boolean enabled) {

                String email = SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName();

                ParkingSlot slot = slotRepository.findById(slotId)
                                .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

                // 🔐 Ownership check
                if (!slot.getOwner().getEmail().equals(email)) {
                        throw new RuntimeException("You are not the owner of this parking slot");
                }

                slot.setEnabled(enabled);
                ParkingSlot saved = slotRepository.save(slot);

                return new ParkingSlotResponseDTO(
                                saved.getId(),
                                saved.getLocation(),
                                saved.getLatitude(),
                                saved.getLongitude(),
                                saved.getCarCapacity(),
                                saved.getBikeCapacity(),
                                saved.getTruckCapacity(),
                                saved.getImageUrl(),
                                saved.getAddress(),
                                saved.getCity(),
                                saved.getReviews(),
                                saved.getUpiId(),
                                saved.isEnabled(),
                                saved.getCarPricePerHour(),
                                saved.getBikePricePerHour(),
                                saved.getTruckPricePerHour());
        }

        @Transactional
        public ParkingSlotResponseDTO updateCapacity(
                        Long slotId,
                        int carCapacity,
                        int bikeCapacity,
                        int truckCapacity) {

                String email = SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName();

                ParkingSlot slot = slotRepository.findById(slotId)
                                .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

                if (!slot.getOwner().getEmail().equals(email)) {
                        throw new RuntimeException("You are not the owner of this parking slot");
                }

                slot.setCarCapacity(carCapacity);
                slot.setBikeCapacity(bikeCapacity);
                slot.setTruckCapacity(truckCapacity);

                ParkingSlot saved = slotRepository.save(slot);

                return new ParkingSlotResponseDTO(
                                saved.getId(),
                                saved.getLocation(),
                                saved.getLatitude(),
                                saved.getLongitude(),
                                saved.getCarCapacity(),
                                saved.getBikeCapacity(),
                                saved.getTruckCapacity(),
                                saved.getImageUrl(),
                                saved.getAddress(),
                                saved.getCity(),
                                saved.getReviews(),
                                saved.getUpiId(),
                                saved.isEnabled(),
                                saved.getCarPricePerHour(),
                                saved.getBikePricePerHour(),
                                saved.getTruckPricePerHour());
        }

        // -----------------------------------
        // SLOT AVAILABILITY PREVIEW
        // -----------------------------------
        @Transactional(readOnly = true)
        public SlotAvailabilityResponseDTO getSlotAvailability(Long slotId) {

                ParkingSlot slot = slotRepository.findById(slotId)
                                .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

                long carOccupied = bookingRepository
                                .countByParkingSlot_IdAndVehicle_VehicleTypeAndActiveTrue(
                                                slotId, VehicleType.CAR);

                long bikeOccupied = bookingRepository
                                .countByParkingSlot_IdAndVehicle_VehicleTypeAndActiveTrue(
                                                slotId, VehicleType.BIKE);

                long truckOccupied = bookingRepository
                                .countByParkingSlot_IdAndVehicle_VehicleTypeAndActiveTrue(
                                                slotId, VehicleType.TRUCK);

                return new SlotAvailabilityResponseDTO(
                                slotId,

                                slot.getCarCapacity(),
                                (int) carOccupied,
                                slot.getCarCapacity() - (int) carOccupied,

                                slot.getBikeCapacity(),
                                (int) bikeOccupied,
                                slot.getBikeCapacity() - (int) bikeOccupied,

                                slot.getTruckCapacity(),
                                (int) truckOccupied,
                                slot.getTruckCapacity() - (int) truckOccupied);
        }

        // SEARCH PARKING SLOTS
        // This looks formidable, but it's just a big filter!
        // It checks every slot to see if it matches the user's search criteria.
        @Transactional(readOnly = true)
        public List<ParkingSlotResponseDTO> searchParkingSlots(String location, String vehicleType) {
                // 1. Fetch ALL slots from the database
                List<ParkingSlot> allSlots = slotRepository.findAll();

                // 2. Stream through them and filter out the ones that don't match
                return allSlots.stream()
                                .filter(slot -> {
                                        // Filter by Location (if user typed one)
                                        // We check if the location, city, or district matches the search text
                                        if (location != null && !location.trim().isEmpty()) {
                                                String search = location.toLowerCase();
                                                boolean matchLoc = slot.getLocation().toLowerCase().contains(search);
                                                boolean matchCity = slot.getCity() != null
                                                                && slot.getCity().toLowerCase().contains(search);
                                                boolean matchAddress = slot.getAddress() != null
                                                                && slot.getAddress().toLowerCase().contains(search);
                                                if (!matchLoc && !matchCity && !matchAddress) {
                                                        return false; // Skip this slot if no match
                                                }
                                        }

                                        // Filter by Vehicle Type (if user selected one)
                                        // We check if the slot has capacity > 0 for that type
                                        if (vehicleType != null && !vehicleType.trim().isEmpty()) {
                                                try {
                                                        VehicleType type = VehicleType
                                                                        .valueOf(vehicleType.toUpperCase());
                                                        boolean hasCapacity = switch (type) {
                                                                case CAR -> slot.getCarCapacity() > 0;
                                                                case BIKE -> slot.getBikeCapacity() > 0;
                                                                case TRUCK -> slot.getTruckCapacity() > 0;
                                                        };
                                                        if (!hasCapacity)
                                                                return false; // Skip if full or not allowed
                                                } catch (IllegalArgumentException e) {
                                                        // Ignore invalid types
                                                }
                                        }
                                        // Filter by Status (Only APPROVED slots are visible)
                                        if (!"APPROVED".equals(slot.getStatus())) {
                                                return false;
                                        }

                                        return true; // Keep this slot!
                                })
                                .map(slot -> new ParkingSlotResponseDTO(
                                                slot.getId(),
                                                slot.getLocation(),
                                                slot.getLatitude(),
                                                slot.getLongitude(),
                                                slot.getCarCapacity(),
                                                slot.getBikeCapacity(),
                                                slot.getTruckCapacity(),
                                                slot.getImageUrl(),
                                                slot.getAddress(),
                                                slot.getCity(),
                                                slot.getReviews(),
                                                slot.getUpiId(),
                                                slot.isEnabled(),
                                                slot.getCarPricePerHour(),
                                                slot.getBikePricePerHour(),
                                                slot.getTruckPricePerHour())) // Convert to DTO (Data Transfer Object)
                                                                              // for safe
                                // sending
                                .toList();
        }

        @Transactional(readOnly = true)
        public List<ParkingSlotResponseDTO> getSlotsByOwner() {
                String email = SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName();
                return slotRepository.findByOwner_Email(email)
                                .stream()
                                .map(slot -> new ParkingSlotResponseDTO(
                                                slot.getId(),
                                                slot.getLocation(),
                                                slot.getLatitude(),
                                                slot.getLongitude(),
                                                slot.getCarCapacity(),
                                                slot.getBikeCapacity(),
                                                slot.getTruckCapacity(),
                                                slot.getImageUrl(),
                                                slot.getAddress(),
                                                slot.getCity(),
                                                slot.getReviews(),
                                                slot.getUpiId(),
                                                slot.isEnabled(),
                                                slot.getCarPricePerHour(),
                                                slot.getBikePricePerHour(),
                                                slot.getTruckPricePerHour()))
                                .toList();
        }

        @Transactional(readOnly = true)
        public com.smartparking.smart_parking_backend.dto.OwnerDashboardSummary getOwnerSummary() {
                String email = SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName();

                List<ParkingSlot> slots = slotRepository.findByOwner_Email(email);
                int totalSlots = slots.size();
                int totalCar = slots.stream().mapToInt(ParkingSlot::getCarCapacity).sum();
                int totalBike = slots.stream().mapToInt(ParkingSlot::getBikeCapacity).sum();
                int totalTruck = slots.stream().mapToInt(ParkingSlot::getTruckCapacity).sum();

                long activeBookings = bookingRepository.countByParkingSlot_Owner_EmailAndActiveTrue(email);
                double totalRevenue = bookingRepository.getTotalRevenueByOwnerEmail(email);

                return new com.smartparking.smart_parking_backend.dto.OwnerDashboardSummary(
                                totalSlots, totalCar, totalBike, totalTruck, activeBookings, totalRevenue);
        }

        @Transactional
        public ParkingSlotResponseDTO updatePricing(Long slotId, double carPrice, double bikePrice, double truckPrice) {
                String email = SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName();

                ParkingSlot slot = slotRepository.findById(slotId)
                                .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

                if (!slot.getOwner().getEmail().equals(email)) {
                        throw new RuntimeException("You are not the owner of this parking slot");
                }

                slot.setCarPricePerHour(carPrice);
                slot.setBikePricePerHour(bikePrice);
                slot.setTruckPricePerHour(truckPrice);

                ParkingSlot saved = slotRepository.save(slot);

                return new ParkingSlotResponseDTO(
                                saved.getId(),
                                saved.getLocation(),
                                saved.getLatitude(),
                                saved.getLongitude(),
                                saved.getCarCapacity(),
                                saved.getBikeCapacity(),
                                saved.getTruckCapacity(),
                                saved.getImageUrl(),
                                saved.getAddress(),
                                saved.getCity(),
                                saved.getReviews(),
                                saved.getUpiId(),
                                saved.isEnabled(),
                                saved.getCarPricePerHour(),
                                saved.getBikePricePerHour(),
                                saved.getTruckPricePerHour());
        }

        @Transactional
        public ParkingSlotResponseDTO updateUpiId(Long slotId, String upiId) {
                String email = SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName();

                ParkingSlot slot = slotRepository.findById(slotId)
                                .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

                if (!slot.getOwner().getEmail().equals(email)) {
                        throw new RuntimeException("You are not the owner of this parking slot");
                }

                slot.setUpiId(upiId);
                ParkingSlot saved = slotRepository.save(slot);

                return new ParkingSlotResponseDTO(
                                saved.getId(),
                                saved.getLocation(),
                                saved.getLatitude(),
                                saved.getLongitude(),
                                saved.getCarCapacity(),
                                saved.getBikeCapacity(),
                                saved.getTruckCapacity(),
                                saved.getImageUrl(),
                                saved.getAddress(),
                                saved.getCity(),
                                saved.getReviews(),
                                saved.getUpiId(),
                                saved.isEnabled(),
                                saved.getCarPricePerHour(),
                                saved.getBikePricePerHour(),
                                saved.getTruckPricePerHour());
        }

        // -----------------------------------
        // DELETE PARKING SLOT
        // -----------------------------------
        @Transactional
        public void deleteSlot(Long slotId) {
                String email = SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName();

                ParkingSlot slot = slotRepository.findById(slotId)
                                .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

                // 🔐 Ownership check
                if (!slot.getOwner().getEmail().equals(email)) {
                        throw new RuntimeException("You are not the owner of this parking slot");
                }

                // Check if there are active bookings
                long activeBookings = bookingRepository.countByParkingSlot_IdAndActiveTrue(slotId);
                if (activeBookings > 0) {
                        throw new RuntimeException("Cannot delete slot with active bookings");
                }

                slotRepository.delete(slot);
        }

}
