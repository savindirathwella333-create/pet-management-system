package com.petmgmt.ownerservice.controller;

import com.petmgmt.ownerservice.dto.AuthResponse;
import com.petmgmt.ownerservice.dto.LoginRequest;
import com.petmgmt.ownerservice.dto.RegisterRequest;
import com.petmgmt.ownerservice.model.Owner;
import com.petmgmt.ownerservice.service.OwnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Function 1 (interconnection root): owner registration & login.
 * Endpoints: POST /auth/register, POST /auth/login
 * The Gateway calls /auth/login to obtain a JWT (simplified OAuth2-style flow),
 * then attaches that token to subsequent requests for pet/appointment/record services.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Owner registration & login (simplified OAuth2-style token issuance)")
public class AuthController {

    private final OwnerService ownerService;

    public AuthController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new pet owner")
    public ResponseEntity<Owner> register(@Valid @RequestBody RegisterRequest request) {
        Owner created = ownerService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/login")
    @Operation(summary = "Login and receive a JWT access token")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(ownerService.login(request));
    }
}
