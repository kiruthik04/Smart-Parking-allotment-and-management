package com.smartparking.smart_parking_backend.dto;

public class LoginResponseDTO {

    private String token;
    private Long id;
    private String name;
    private String email;
    private boolean hasSlots;
    private String role;

    public LoginResponseDTO(String token, Long id, String name, String email, boolean hasSlots, String role) {
        this.token = token;
        this.id = id;
        this.name = name;
        this.email = email;
        this.hasSlots = hasSlots;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isHasSlots() {
        return hasSlots;
    }

    public void setHasSlots(boolean hasSlots) {
        this.hasSlots = hasSlots;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
