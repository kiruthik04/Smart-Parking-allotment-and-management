package com.smartparking.smart_parking_backend.controller;

import com.smartparking.smart_parking_backend.dto.OwnerDashboardSummary;
import com.smartparking.smart_parking_backend.service.OwnerDashboardService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owner/dashboard")
public class OwnerDashboardController {

    private final OwnerDashboardService service;

    public OwnerDashboardController(OwnerDashboardService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("/summary")
    public OwnerDashboardSummary getSummary() {
        return service.getSummary();
    }
}
