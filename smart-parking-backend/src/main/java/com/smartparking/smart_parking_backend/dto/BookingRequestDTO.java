package com.smartparking.smart_parking_backend.dto;

import jakarta.validation.constraints.NotNull;
import com.smartparking.smart_parking_backend.model.VehicleType;

public class BookingRequestDTO {
    @NotNull(message = "Slot ID is required")
    private Long slotId;

    @NotNull(message = "Vehicle Number is required")
    private String vehicleNumber;

    @NotNull(message = "Vehicle Model is required")
    private String vehicleModel;

    @NotNull(message = "Vehicle Type is required")
    private VehicleType vehicleType;

    private String ownerName;

    // MANUAL GETTERS AND SETTERS
    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
