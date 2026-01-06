package com.smartparking.smart_parking_backend.controller;

import com.smartparking.smart_parking_backend.service.PaymentService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // 1. Create Order (Called when user clicks "Pay with Razorpay")
    @PostMapping("/create-order/{bookingId}")
    public String createOrder(@PathVariable Long bookingId) {
        return paymentService.createOrder(bookingId);
    }

    // 2. Verify Payment (Called after successful payment on frontend)
    @PostMapping("/verify")
    public String verifyPayment(@RequestBody Map<String, String> data) {
        return paymentService.verifyPayment(
                data.get("razorpay_order_id"),
                data.get("razorpay_payment_id"),
                data.get("razorpay_signature"));
    }
}
