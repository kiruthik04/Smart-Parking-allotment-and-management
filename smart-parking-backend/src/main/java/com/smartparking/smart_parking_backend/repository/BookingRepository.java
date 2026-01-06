package com.smartparking.smart_parking_backend.repository;

import com.smartparking.smart_parking_backend.model.Booking;
import com.smartparking.smart_parking_backend.model.ParkingSlot;
import com.smartparking.smart_parking_backend.model.VehicleType;
import com.smartparking.smart_parking_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
        // User Side
        Optional<Booking> findByParkingSlotAndActiveTrue(ParkingSlot parkingSlot);

        List<Booking> findByUser(User user);

        // Owner Side
        List<Booking> findByParkingSlot_Owner_Email(String email);

        long countByParkingSlot_Owner_EmailAndActiveTrue(String email);

        long countByParkingSlotAndVehicle_VehicleTypeAndActiveTrue(
                        ParkingSlot parkingSlot,
                        VehicleType vehicleType);

        long countByParkingSlot_IdAndVehicle_VehicleTypeAndActiveTrue(
                        Long slotId,
                        com.smartparking.smart_parking_backend.model.VehicleType vehicleType);

        long countByParkingSlot_IdAndActiveTrue(Long slotId);

        @Query("""
                            SELECT COALESCE(SUM(b.totalPrice), 0)
                            FROM Booking b
                            WHERE b.parkingSlot.owner.email = :email
                              AND b.active = false
                        """)
        double getTotalRevenueByOwnerEmail(@Param("email") String email);

        // RAZORPAY / ADMIN
        @Query("SELECT CASE WHEN SUM(b.totalPrice) IS NULL THEN 0.0 ELSE SUM(b.totalPrice) END FROM Booking b")
        Double getTotalRevenue();

        @Query("SELECT CASE WHEN SUM(b.totalPrice) IS NULL THEN 0.0 ELSE SUM(b.totalPrice) END FROM Booking b WHERE b.parkingSlot.owner.id = :ownerId")
        Double getTotalRevenueByOwnerId(@Param("ownerId") Long ownerId);

        @Query("SELECT CASE WHEN SUM(b.totalPrice) IS NULL THEN 0.0 ELSE SUM(b.totalPrice) END FROM Booking b WHERE b.parkingSlot.owner.id = :ownerId AND b.paymentStatus = 'COMPLETED'")
        Double getTotalConfirmedRevenueByOwnerId(@Param("ownerId") Long ownerId);

        // Razorpay support
        Optional<Booking> findByRazorpayOrderId(String razorpayOrderId);
}
