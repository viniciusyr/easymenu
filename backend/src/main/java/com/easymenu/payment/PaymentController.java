package com.easymenu.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/payment")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<PaymentResponseDTO> requestPayment(@RequestBody PaymentRecordDTO paymentRecordDTO, @AuthenticationPrincipal UserDetails userDetails){
        PaymentResponseDTO paymentResponseDTO = paymentService.processPayment(paymentRecordDTO, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponseDTO);
    }
}
