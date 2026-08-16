package com.petmgmt.ownerservice.service;

import com.petmgmt.ownerservice.dto.AuthResponse;
import com.petmgmt.ownerservice.dto.LoginRequest;
import com.petmgmt.ownerservice.dto.RegisterRequest;
import com.petmgmt.ownerservice.exception.BadRequestException;
import com.petmgmt.ownerservice.exception.ResourceNotFoundException;
import com.petmgmt.ownerservice.model.Owner;
import com.petmgmt.ownerservice.repository.OwnerRepository;
import com.petmgmt.ownerservice.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public OwnerService(OwnerRepository ownerRepository, JwtUtil jwtUtil) {
        this.ownerRepository = ownerRepository;
        this.jwtUtil = jwtUtil;
    }

    public Owner register(RegisterRequest request) {
        if (ownerRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("An owner with this email already exists");
        }
        Owner owner = new Owner(
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getPhone());
        return ownerRepository.save(owner);
    }

    public AuthResponse login(LoginRequest request) {
        Owner owner = ownerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), owner.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(owner.getId(), owner.getEmail());
        return new AuthResponse(token, owner.getId(), owner.getName(), owner.getEmail());
    }

    public Owner getById(Long id) {
        return ownerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found with id: " + id));
    }

    public List<Owner> getAll() {
        return ownerRepository.findAll();
    }
}
