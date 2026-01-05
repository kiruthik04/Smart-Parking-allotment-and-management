package com.smartparking.smart_parking_backend.controller;

import com.smartparking.smart_parking_backend.dto.LoginRequestDTO;
import com.smartparking.smart_parking_backend.dto.LoginResponseDTO;
import com.smartparking.smart_parking_backend.dto.UserProfileUpdateDTO;
import com.smartparking.smart_parking_backend.dto.UserProfileResponseDTO;
import com.smartparking.smart_parking_backend.model.User;
import com.smartparking.smart_parking_backend.repository.UserRepository;
import com.smartparking.smart_parking_backend.repository.ParkingSlotRepository;
import com.smartparking.smart_parking_backend.security.JwtService;
import com.smartparking.smart_parking_backend.exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final ParkingSlotRepository slotRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;


    public UserController(UserRepository userRepository, ParkingSlotRepository slotRepository, BCryptPasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.slotRepository = slotRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // CREATE USER (TEST / PHASE 26)
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    // SIGN UP (OWNER CAN REGISTER SLOTS)
    @PostMapping("/signup")
    public User signup(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    // LOGIN USER (temporary – JWT next step)
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO dto)
    {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(
            user.getEmail(),
            user.getRole().name()
        );

        boolean hasSlots = slotRepository.countByOwner_Email(user.getEmail()) > 0;

        return new LoginResponseDTO(
            token,
            user.getId(),
            user.getName(),
            user.getEmail(),
            hasSlots,
            user.getRole() != null ? user.getRole().name() : null
        );
    }

    // GET CURRENT USER PROFILE
    @PreAuthorize("hasAnyRole('USER', 'OWNER')")
    @GetMapping("/me")
    @Transactional(readOnly = true)
    public UserProfileResponseDTO getMyProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return new UserProfileResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getCity(),
                user.getState(),
                user.getZipCode(),
                user.getCountry(),
                user.getRole() != null ? user.getRole().name() : null
        );
    }

    // UPDATE CURRENT USER PROFILE
    @PreAuthorize("hasAnyRole('USER', 'OWNER')")
    @PutMapping("/me")
    @Transactional
    public UserProfileResponseDTO updateMyProfile(@RequestBody UserProfileUpdateDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Update fields if provided
        if (dto.getName() != null && !dto.getName().trim().isEmpty()) {
            user.setName(dto.getName());
        }
        if (dto.getPhone() != null) {
            user.setPhone(dto.getPhone());
        }
        if (dto.getAddress() != null) {
            user.setAddress(dto.getAddress());
        }
        if (dto.getCity() != null) {
            user.setCity(dto.getCity());
        }
        if (dto.getState() != null) {
            user.setState(dto.getState());
        }
        if (dto.getZipCode() != null) {
            user.setZipCode(dto.getZipCode());
        }
        if (dto.getCountry() != null) {
            user.setCountry(dto.getCountry());
        }

        User savedUser = userRepository.save(user);

        return new UserProfileResponseDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getPhone(),
                savedUser.getAddress(),
                savedUser.getCity(),
                savedUser.getState(),
                savedUser.getZipCode(),
                savedUser.getCountry(),
                savedUser.getRole() != null ? savedUser.getRole().name() : null
        );
    }
}
