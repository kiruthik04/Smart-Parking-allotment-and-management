package com.smartparking.smart_parking_backend.dto;

public class ParkingSlotResponseDTO {

    private Long id;
    private String location;
    private double latitude;
    private double longitude;
    private int carCapacity;
    private int bikeCapacity;
    private int truckCapacity;

    private String imageUrl;
    private String address;
    private String city;
    private String reviews;
    private String upiId; // Added UPI ID
    private boolean enabled; // Added status field

    public ParkingSlotResponseDTO(Long id, String location, double latitude, double longitude, int carCapacity,
            int bikeCapacity, int truckCapacity, String imageUrl, String address, String city, String reviews,
            String upiId, boolean enabled) {
        this.id = id;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.carCapacity = carCapacity;
        this.bikeCapacity = bikeCapacity;
        this.truckCapacity = truckCapacity;
        this.imageUrl = imageUrl;
        this.address = address;
        this.city = city;
        this.reviews = reviews;
        this.upiId = upiId;
        this.enabled = enabled;
    }

    public ParkingSlotResponseDTO(Long id, String location, double latitude, double longitude) {
        this(id, location, latitude, longitude, 0, 0, 0, null, null, null, null, null, true);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getReviews() {
        return reviews;
    }

    public Long getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getCarCapacity() {
        return carCapacity;
    }

    public int getBikeCapacity() {
        return bikeCapacity;
    }

    public int getTruckCapacity() {
        return truckCapacity;
    }

    public String getUpiId() {
        return upiId;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
