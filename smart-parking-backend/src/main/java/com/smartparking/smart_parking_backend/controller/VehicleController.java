package com.smartparking.smart_parking_backend.controller;

import com.smartparking.smart_parking_backend.dto.VehicleRequestDTO;
import com.smartparking.smart_parking_backend.dto.VehicleResponseDTO;
import com.smartparking.smart_parking_backend.model.Vehicle;
import com.smartparking.smart_parking_backend.model.User;
import com.smartparking.smart_parking_backend.repository.UserRepository;
import com.smartparking.smart_parking_backend.repository.VehicleRepository;
import com.smartparking.smart_parking_backend.exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

        private final VehicleRepository vehicleRepository;
        private final UserRepository userRepository;

        public VehicleController(VehicleRepository vehicleRepository, UserRepository userRepository) {
                this.vehicleRepository = vehicleRepository;
                this.userRepository = userRepository;
        }

        // GET CURRENT USER'S VEHICLES
        @PreAuthorize("hasAnyRole('USER', 'OWNER')")
        @GetMapping("/me")
        @Transactional(readOnly = true)
        public List<VehicleResponseDTO> getMyVehicles() {
                String email = SecurityContextHolder.getContext().getAuthentication().getName();
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                return vehicleRepository.findByUser(user)
                                .stream()
                                .map(vehicle -> new VehicleResponseDTO(
                                                vehicle.getId(),
                                                vehicle.getVehicleNumber(),
                                                vehicle.getVehicleModel(),
                                                vehicle.getVehicleType(),
                                                vehicle.getOwnerName()))
                                .toList();
        }

        // CREATE VEHICLE FOR CURRENT USER
        @PreAuthorize("hasAnyRole('USER', 'OWNER')")
        @PostMapping
        @Transactional
        public VehicleResponseDTO createVehicle(@Valid @RequestBody VehicleRequestDTO dto) {
                String email = SecurityContextHolder.getContext().getAuthentication().getName();
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                Vehicle vehicle = new Vehicle();
                vehicle.setVehicleNumber(dto.getVehicleNumber());
                vehicle.setVehicleModel(dto.getVehicleModel() != null ? dto.getVehicleModel() : "");
                vehicle.setVehicleType(dto.getVehicleType());
                vehicle.setOwnerName(dto.getOwnerName() != null ? dto.getOwnerName() : user.getName());
                vehicle.setUser(user);

                Vehicle savedVehicle = vehicleRepository.save(vehicle);

                return new VehicleResponseDTO(
                                savedVehicle.getId(),
                                savedVehicle.getVehicleNumber(),
                                savedVehicle.getVehicleModel(),
                                savedVehicle.getVehicleType(),
                                savedVehicle.getOwnerName());
        }

        // DELETE VEHICLE
        // @PreAuthorize: Ensures only logged-in users (USER or OWNER) can access this
        @PreAuthorize("hasAnyRole('USER', 'OWNER')")
        @DeleteMapping("/{id}")
        @Transactional
        public void deleteVehicle(@PathVariable Long id) {
                // 1. Get the current logged-in user's email from the security context
                String email = SecurityContextHolder.getContext().getAuthentication().getName();
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                // 2. Find the vehicle coming from the request
                Vehicle vehicle = vehicleRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

                // 3. Security Check: Ensure the user TRYING to delete matches the vehicle OWNER
                // We don't want User A deleting User B's vehicle!
                if (!vehicle.getUser().getId().equals(user.getId())) {
                        throw new RuntimeException("You are not authorized to delete this vehicle");
                }

                // 4. Finally, remove it from the database
                vehicleRepository.delete(vehicle);
        }
}
