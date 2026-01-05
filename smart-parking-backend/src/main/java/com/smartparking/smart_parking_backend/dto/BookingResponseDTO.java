package com.smartparking.smart_parking_backend.dto;

import com.smartparking.smart_parking_backend.model.VehicleType;
import com.smartparking.smart_parking_backend.model.PaymentStatus;
import java.time.LocalDateTime;

public class BookingResponseDTO {
    private Long bookingId;
    private Long slotId;
    private String location;
    private String vehicleNumber;
    private String vehicleModel;
    private VehicleType vehicleType;
    private Long userId;
    private String userName;
    private String userPhone;
    private String slotUpiId; // Added slotUpiId
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean active;
    private double totalPrice;
    private PaymentStatus paymentStatus;

    public BookingResponseDTO(Long bookingId, Long slotId, String location, String vehicleNumber, String vehicleModel,
            VehicleType vehicleType, Long userId, String userName, String userPhone, String slotUpiId,
            LocalDateTime startTime,
            LocalDateTime endTime,
            boolean active, double totalPrice, PaymentStatus paymentStatus) {
        this.bookingId = bookingId;
        this.slotId = slotId;
        this.location = location;
        this.vehicleNumber = vehicleNumber;
        this.vehicleModel = vehicleModel;
        this.vehicleType = vehicleType;
        this.userId = userId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.slotUpiId = slotUpiId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.active = active;
        this.totalPrice = totalPrice;
        this.paymentStatus = paymentStatus;
    }

    // MANUAL GETTERS AND SETTERS
    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() { // Getter
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) { // Setter
        this.endTime = endTime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
