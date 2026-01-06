package com.smartparking.smart_parking_backend.service;

import com.smartparking.smart_parking_backend.dto.AdminStatsDTO;
import com.smartparking.smart_parking_backend.dto.OwnerPayoutDTO;
import com.smartparking.smart_parking_backend.model.ParkingSlot;
import com.smartparking.smart_parking_backend.model.Payout;
import com.smartparking.smart_parking_backend.model.Role;
import com.smartparking.smart_parking_backend.model.User;
import com.smartparking.smart_parking_backend.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final PayoutRepository payoutRepository;
    private final ParkingSlotRepository parkingSlotRepository;

    public AdminService(BookingRepository bookingRepository, UserRepository userRepository,
            PayoutRepository payoutRepository, ParkingSlotRepository parkingSlotRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.payoutRepository = payoutRepository;
        this.parkingSlotRepository = parkingSlotRepository;
    }

    public AdminStatsDTO getGlobalStats() {
        long totalUsers = userRepository.count();
        long totalOwners = userRepository.findAll().stream().filter(u -> Role.OWNER.equals(u.getRole())).count();
        long totalBookings = bookingRepository.count();
        double totalRevenue = bookingRepository.getTotalRevenue() != null ? bookingRepository.getTotalRevenue() : 0.0;
        long pendingSlots = parkingSlotRepository.findAll().stream().filter(s -> "PENDING".equals(s.getStatus()))
                .count();

        return new AdminStatsDTO(totalUsers, totalOwners, totalBookings, totalRevenue, pendingSlots);
    }

    public List<OwnerPayoutDTO> getOwnersPayoutStatus() {
        List<User> owners = userRepository.findAll().stream()
                .filter(u -> Role.OWNER.equals(u.getRole()))
                .toList();

        List<OwnerPayoutDTO> stats = new ArrayList<>();

        for (User owner : owners) {
            Double totalEarned = bookingRepository.getTotalConfirmedRevenueByOwnerId(owner.getId());
            Double totalPaid = payoutRepository.getTotalPaidToOwner(owner.getId());

            if (totalEarned == null)
                totalEarned = 0.0;
            if (totalPaid == null)
                totalPaid = 0.0;

            String upiId = "N/A";

            // 1. Try to find UPI from any of the owner's slots (as requested)
            if (owner.getParkingSlots() != null) {
                for (ParkingSlot slot : owner.getParkingSlots()) {
                    if (slot.getUpiId() != null && !slot.getUpiId().isEmpty()) {
                        upiId = slot.getUpiId();
                        break;
                    }
                }
            }

            // 2. Fallback to Owner's Profile UPI if slot UPI is not found
            if ("N/A".equals(upiId) && owner.getUpiId() != null && !owner.getUpiId().isEmpty()) {
                upiId = owner.getUpiId();
            }

            stats.add(new OwnerPayoutDTO(
                    owner.getId(),
                    owner.getName(),
                    owner.getEmail(),
                    upiId,
                    totalEarned,
                    totalPaid));
        }
        return stats;
    }

    public void processPayout(Long ownerId, double amount) {
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("Owner not found"));

        Payout payout = new Payout(owner, amount, "PAID");
        payoutRepository.save(payout);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<ParkingSlot> getPendingSlots() {
        return parkingSlotRepository.findAll().stream()
                .filter(s -> "PENDING".equals(s.getStatus()))
                .toList();
    }

    public void verifySlot(Long slotId, String status, String comments) {
        ParkingSlot slot = parkingSlotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));
        slot.setStatus(status);
        if (comments != null && !comments.isEmpty()) {
            slot.setAdminComments(comments);
        }
        parkingSlotRepository.save(slot);
    }

    public User getAdminProfile() {
        return userRepository.findByEmail("admin@smartparking.com")
                .orElseThrow(() -> new RuntimeException("Admin not found"));
    }

    public void updateAdminUpi(String upiId) {
        User admin = getAdminProfile();
        admin.setUpiId(upiId);
        userRepository.save(admin);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }
}
