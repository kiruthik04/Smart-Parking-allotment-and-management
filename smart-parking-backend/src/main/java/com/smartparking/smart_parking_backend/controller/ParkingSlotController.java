package com.smartparking.smart_parking_backend.controller;

import com.smartparking.smart_parking_backend.dto.ParkingSlotResponseDTO;
import com.smartparking.smart_parking_backend.service.ParkingSlotService;
import com.smartparking.smart_parking_backend.dto.SlotAvailabilityResponseDTO;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import com.smartparking.smart_parking_backend.dto.ParkingSlotRequestDTO;
import jakarta.validation.Valid;
import java.util.List;

@RestController // response body, handles web request and automatically convert the return value
                // into client understand format(JSON)
@RequestMapping("/api/slots") // Base path for methods, Maps HTTP request.

public class ParkingSlotController {
    private final ParkingSlotService service;

    public ParkingSlotController(ParkingSlotService service) {

        this.service = service;
    }

    // -------------------------------
    // CREATE PARKING SLOT (ADMIN)
    // POST /api/slots
    // -------------------------------

    @PostMapping
    public ParkingSlotResponseDTO createSlot(
            @Valid @RequestBody ParkingSlotRequestDTO dto) {
        return service.createSlot(dto);
    }

    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("/owner")
    public List<ParkingSlotResponseDTO> getOwnerSlots() {
        return service.getSlotsByOwner();
    }

    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("/owner/summary")
    public com.smartparking.smart_parking_backend.dto.OwnerDashboardSummary getOwnerSummary() {
        return service.getOwnerSummary();
    }

    @PreAuthorize("hasRole('OWNER')")
    @PutMapping("/{slotId}/price")
    public ParkingSlotResponseDTO updatePrice(@PathVariable Long slotId,
            @RequestParam double carPrice,
            @RequestParam double bikePrice,
            @RequestParam double truckPrice) {
        return service.updatePricing(slotId, carPrice, bikePrice, truckPrice);
    }

    @PreAuthorize("hasRole('OWNER')")
    @PutMapping("/{slotId}/upi")
    public ParkingSlotResponseDTO updateUpiId(@PathVariable Long slotId,
            @RequestParam String upiId) {
        return service.updateUpiId(slotId, upiId);
    }

    // -------------------------------
    // GET ALL PARKING SLOTS
    // GET /api/slots
    // -------------------------------
    @GetMapping
    public List<ParkingSlotResponseDTO> getAllSlots() {
        return service.getAllSlots();
    }

    @GetMapping("/map")
    public List<ParkingSlotResponseDTO> getSlotsForMap() {
        return service.getAllSlotsForMap()
                .stream()
                .map(slot -> new ParkingSlotResponseDTO(
                        slot.getId(),
                        slot.getLocation(),
                        slot.getLatitude(),
                        slot.getLongitude(),
                        slot.getCarCapacity(),
                        slot.getBikeCapacity(),
                        slot.getTruckCapacity(),
                        slot.getImageUrl(),
                        slot.getAddress(),
                        slot.getCity(),
                        slot.getReviews(),
                        slot.getUpiId(), // Added upiId here
                        slot.isEnabled() // Added enabled status
                ))
                .toList();
    }

    @GetMapping("/{slotId}/availability")
    public SlotAvailabilityResponseDTO getAvailability(@PathVariable Long slotId) {
        return service.getSlotAvailability(slotId);
    }

    @PreAuthorize("hasRole('OWNER')")
    @PutMapping("/{slotId}/enable")
    public ParkingSlotResponseDTO enableSlot(@PathVariable Long slotId) {
        return service.setSlotEnabled(slotId, true);
    }

    @PreAuthorize("hasRole('OWNER')")
    @PutMapping("/{slotId}/disable")
    public ParkingSlotResponseDTO disableSlot(@PathVariable Long slotId) {
        return service.setSlotEnabled(slotId, false);
    }

    @PreAuthorize("hasRole('OWNER')")
    @PutMapping("/{slotId}/capacity")
    public ParkingSlotResponseDTO updateCapacity(
            @PathVariable Long slotId,
            @RequestParam int car,
            @RequestParam int bike,
            @RequestParam int truck) {
        return service.updateCapacity(slotId, car, bike, truck);
    }

    @PreAuthorize("hasRole('OWNER')")
    @DeleteMapping("/{slotId}")
    public void deleteSlot(@PathVariable Long slotId) {
        service.deleteSlot(slotId);
    }

}

// @PostMapping("/{id}/book")
// public ParkingSlot bookSlot(@PathVariable Long id)
// {
// return service.bookSlot(id);
// }

// Get request, GET-Retrieve data.
// @GetMapping
// public List<ParkingSlot> getAllSlots() {
// return service.getAllSlots();
// }

// POST request, POST - Create slot.
// @RequestBody - framework that converts JSON income into ParkingSlot java
// object.

// public ParkingSlot createSlot(@RequestBody ParkingSlot slot)
// {
// return service.createSlot(slot);
// } replace with DTO, links controller, DTO & service layer using data
// validation.

// @valid says, heyy execute all validation defined in PSRDTO, before request
// proceeding.
// This DTO is data safety and control instead of entity.
