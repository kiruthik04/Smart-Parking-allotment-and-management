package com.smartparking.smart_parking_backend.controller;

import com.smartparking.smart_parking_backend.dto.AdminStatsDTO;
import com.smartparking.smart_parking_backend.dto.OwnerPayoutDTO;
import com.smartparking.smart_parking_backend.model.ParkingSlot;
import com.smartparking.smart_parking_backend.model.User;
import com.smartparking.smart_parking_backend.service.AdminService;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
// @CrossOrigin(origins = "*") // Removed to avoid conflict with Global CORS
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/stats")
    public ResponseEntity<AdminStatsDTO> getStats() {
        return ResponseEntity.ok(adminService.getGlobalStats());
    }

    @GetMapping("/payouts")
    public ResponseEntity<List<OwnerPayoutDTO>> getPayouts() {
        return ResponseEntity.ok(adminService.getOwnersPayoutStatus());
    }

    @PostMapping("/payout")
    public ResponseEntity<?> createPayout(@RequestBody Map<String, Object> payload) {
        Long ownerId = Long.valueOf(payload.get("ownerId").toString());
        double amount = Double.parseDouble(payload.get("amount").toString());

        adminService.processPayout(ownerId, amount);
        return ResponseEntity.ok("Payout processed successfully");
    }

    // Slot Verification
    @GetMapping("/slots/pending")
    public ResponseEntity<List<ParkingSlot>> getPendingSlots() {
        return ResponseEntity.ok(adminService.getPendingSlots());
    }

    @PutMapping("/slots/{id}/verify")
    public ResponseEntity<?> verifySlot(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String status = payload.get("status"); // APPROVED or REJECTED
        String comments = payload.get("comments");

        adminService.verifySlot(id, status, comments);
        return ResponseEntity.ok("Slot verification updated");
    }

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfile() {
        User admin = adminService.getAdminProfile();
        return ResponseEntity.ok(Map.of(
                "name", admin.getName(),
                "email", admin.getEmail(),
                "upiId", admin.getUpiId() != null ? admin.getUpiId() : ""));
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, String> payload) {
        String upiId = payload.get("upiId");
        adminService.updateAdminUpi(upiId);
        return ResponseEntity.ok("Admin profile updated");
    }

    // User Management
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
