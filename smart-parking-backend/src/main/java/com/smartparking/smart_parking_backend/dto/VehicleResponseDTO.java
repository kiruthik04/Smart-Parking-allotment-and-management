package com.smartparking.smart_parking_backend.dto;

import com.smartparking.smart_parking_backend.model.VehicleType;

public class VehicleResponseDTO {
    private Long id;
    private String vehicleNumber;
    private String vehicleModel;
    private VehicleType vehicleType;
    private String ownerName;

    public VehicleResponseDTO() {
    }

    public VehicleResponseDTO(Long id, String vehicleNumber, String vehicleModel, VehicleType vehicleType, String ownerName) {
        this.id = id;
        this.vehicleNumber = vehicleNumber;
        this.vehicleModel = vehicleModel;
        this.vehicleType = vehicleType;
        this.ownerName = ownerName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

