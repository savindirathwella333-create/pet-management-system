package com.petmgmt.recordservice.controller;

import com.petmgmt.recordservice.model.MedicalRecord;
import com.petmgmt.recordservice.service.MedicalRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Function 4: Pet medical record management.
 * Endpoints: POST /api/records, GET /api/records/{id},
 *            GET /api/records/pet/{petId}, DELETE /api/records/{id}
 * Completes the interconnection chain: owner-auth-service -> pet-service ->
 * appointment-service -> medical-record-service (this service validates
 * appointmentId against appointment-service on create).
 */
@RestController
@RequestMapping("/api/records")
@Tag(name = "Medical Records", description = "Pet medical/vaccination records, linked to appointments")
public class MedicalRecordController {

    private final MedicalRecordService recordService;

    public MedicalRecordController(MedicalRecordService recordService) {
        this.recordService = recordService;
    }

    @PostMapping
    @Operation(summary = "Add a medical record for a completed/scheduled appointment")
    public ResponseEntity<MedicalRecord> create(@Valid @RequestBody MedicalRecord record) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recordService.create(record));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a medical record by id")
    public ResponseEntity<MedicalRecord> getById(@PathVariable Long id) {
        return ResponseEntity.ok(recordService.getById(id));
    }

    @GetMapping("/pet/{petId}")
    @Operation(summary = "List all medical records for a pet")
    public ResponseEntity<List<MedicalRecord>> getByPet(@PathVariable Long petId) {
        return ResponseEntity.ok(recordService.getByPetId(petId));
    }

    @GetMapping
    @Operation(summary = "List all medical records")
    public ResponseEntity<List<MedicalRecord>> getAll() {
        return ResponseEntity.ok(recordService.getAll());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove a medical record")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        recordService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
