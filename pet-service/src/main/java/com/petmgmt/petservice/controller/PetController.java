package com.petmgmt.petservice.controller;

import com.petmgmt.petservice.model.Pet;
import com.petmgmt.petservice.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Function 2: Pet profile management.
 * Endpoints: POST /api/pets, GET /api/pets/{id}, GET /api/pets/owner/{ownerId},
 *            PUT /api/pets/{id}, DELETE /api/pets/{id}
 * Interconnected with owner-auth-service (validates ownerId on create) and
 * consumed by appointment-service (validates petId when booking).
 */
@RestController
@RequestMapping("/api/pets")
@Tag(name = "Pets", description = "Pet profile CRUD, linked to owners")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    @Operation(summary = "Register a new pet for an owner")
    public ResponseEntity<Pet> create(@Valid @RequestBody Pet pet) {
        return ResponseEntity.status(HttpStatus.CREATED).body(petService.create(pet));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a pet by id")
    public ResponseEntity<Pet> getById(@PathVariable Long id) {
        return ResponseEntity.ok(petService.getById(id));
    }

    @GetMapping("/owner/{ownerId}")
    @Operation(summary = "List all pets belonging to an owner")
    public ResponseEntity<List<Pet>> getByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(petService.getByOwnerId(ownerId));
    }

    @GetMapping
    @Operation(summary = "List all pets")
    public ResponseEntity<List<Pet>> getAll() {
        return ResponseEntity.ok(petService.getAll());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a pet's details")
    public ResponseEntity<Pet> update(@PathVariable Long id, @Valid @RequestBody Pet pet) {
        return ResponseEntity.ok(petService.update(id, pet));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove a pet")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        petService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
