package com.smartparking.smart_parking_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ParkingSlotRequestDTO {
    private Long id;

    @NotBlank(message = "Location must not be empty")
    private String location;

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;

    @Min(value = 0, message = "Car capacity cannot be negative")
    private int carCapacity;

    @Min(value = 0, message = "Bike capacity cannot be negative")
    private int bikeCapacity;

    @Min(value = 0, message = "Truck capacity cannot be negative")
    private int truckCapacity;

    @Min(0)
    private double carPricePerHour;

    @Min(0)
    private double bikePricePerHour;

    @Min(0)
    private double truckPricePerHour;

    private String imageUrl;
    private String address;
    private String city;
    private String reviews;

    // MANUAL GETTERS AND SETTERS
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public int getCarCapacity() {
        return carCapacity;
    }

    public void setCarCapacity(int carCapacity) {
        this.carCapacity = carCapacity;
    }

    public int getBikeCapacity() {
        return bikeCapacity;
    }

    public void setBikeCapacity(int bikeCapacity) {
        this.bikeCapacity = bikeCapacity;
    }

    public int getTruckCapacity() {
        return truckCapacity;
    }

    public void setTruckCapacity(int truckCapacity) {
        this.truckCapacity = truckCapacity;
    }

    public double getCarPricePerHour() {
        return carPricePerHour;
    }

    public void setCarPricePerHour(double carPricePerHour) {
        this.carPricePerHour = carPricePerHour;
    }

    public double getBikePricePerHour() {
        return bikePricePerHour;
    }

    public void setBikePricePerHour(double bikePricePerHour) {
        this.bikePricePerHour = bikePricePerHour;
    }

    public double getTruckPricePerHour() {
        return truckPricePerHour;
    }

    public void setTruckPricePerHour(double truckPricePerHour) {
        this.truckPricePerHour = truckPricePerHour;
    }

    private String upiId;

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }
}