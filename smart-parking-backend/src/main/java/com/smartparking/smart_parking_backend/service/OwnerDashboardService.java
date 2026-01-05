package com.smartparking.smart_parking_backend.service;

import com.smartparking.smart_parking_backend.model.ParkingSlot;
import com.smartparking.smart_parking_backend.repository.BookingRepository;
import com.smartparking.smart_parking_backend.repository.ParkingSlotRepository;
import com.smartparking.smart_parking_backend.dto.OwnerDashboardSummary;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OwnerDashboardService {

    private final ParkingSlotRepository slotRepository;
    private final BookingRepository bookingRepository;

    public OwnerDashboardService(
            ParkingSlotRepository slotRepository,
            BookingRepository bookingRepository
    ) {
        this.slotRepository = slotRepository;
        this.bookingRepository = bookingRepository;
    }

    public OwnerDashboardSummary getSummary() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        List<ParkingSlot> slots = slotRepository.findByOwner_Email(email);

        int totalCarCapacity = slots.stream().mapToInt(ParkingSlot::getCarCapacity).sum();
        int totalBikeCapacity = slots.stream().mapToInt(ParkingSlot::getBikeCapacity).sum();
        int totalTruckCapacity = slots.stream().mapToInt(ParkingSlot::getTruckCapacity).sum();

        long activeBookings =
                bookingRepository.countByParkingSlot_Owner_EmailAndActiveTrue(email);

        double totalRevenue =
                bookingRepository.getTotalRevenueByOwnerEmail(email);

        return new OwnerDashboardSummary(
                slots.size(),
                totalCarCapacity,
                totalBikeCapacity,
                totalTruckCapacity,
                activeBookings,
                totalRevenue
        );
    }
}
