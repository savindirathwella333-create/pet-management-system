package com.petmgmt.ownerservice.controller;

import com.petmgmt.ownerservice.model.Owner;
import com.petmgmt.ownerservice.service.OwnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Owner profile lookups. Consumed internally by pet-service to validate
 * that a given ownerId is a real, registered owner before a pet is created.
 */
@RestController
@RequestMapping("/api/owners")
@Tag(name = "Owners", description = "Owner profile data, also used internally by pet-service")
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an owner by id")
    public ResponseEntity<Owner> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ownerService.getById(id));
    }

    @GetMapping
    @Operation(summary = "List all owners")
    public ResponseEntity<List<Owner>> getAll() {
        return ResponseEntity.ok(ownerService.getAll());
    }
}
