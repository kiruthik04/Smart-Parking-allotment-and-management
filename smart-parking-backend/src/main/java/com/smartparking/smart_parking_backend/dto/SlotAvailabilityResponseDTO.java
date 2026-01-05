package com.smartparking.smart_parking_backend.dto;

public class SlotAvailabilityResponseDTO {

    private Long slotId;
    private int carCapacity;
    private int carOccupied;
    private int carAvailable;
    private int bikeCapacity;
    private int bikeOccupied;
    private int bikeAvailable;
    private int truckCapacity;
    private int truckOccupied;
    private int truckAvailable;

    public SlotAvailabilityResponseDTO(Long slotId, int carCapacity, int carOccupied, int carAvailable,
            int bikeCapacity, int bikeOccupied, int bikeAvailable, int truckCapacity, int truckOccupied,
            int truckAvailable) {
        this.slotId = slotId;
        this.carCapacity = carCapacity;
        this.carOccupied = carOccupied;
        this.carAvailable = carAvailable;
        this.bikeCapacity = bikeCapacity;
        this.bikeOccupied = bikeOccupied;
        this.bikeAvailable = bikeAvailable;
        this.truckCapacity = truckCapacity;
        this.truckOccupied = truckOccupied;
        this.truckAvailable = truckAvailable;
    }

    public Long getSlotId() {
        return slotId;
    }

    public int getCarCapacity() {
        return carCapacity;
    }

    public int getCarOccupied() {
        return carOccupied;
    }

    public int getCarAvailable() {
        return carAvailable;
    }

    public int getBikeCapacity() {
        return bikeCapacity;
    }

    public int getBikeOccupied() {
        return bikeOccupied;
    }

    public int getBikeAvailable() {
        return bikeAvailable;
    }

    public int getTruckCapacity() {
        return truckCapacity;
    }

    public int getTruckOccupied() {
        return truckOccupied;
    }

    public int getTruckAvailable() {
        return truckAvailable;
    }
}
