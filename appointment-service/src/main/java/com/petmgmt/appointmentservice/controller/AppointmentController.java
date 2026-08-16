package com.petmgmt.appointmentservice.controller;

import com.petmgmt.appointmentservice.model.Appointment;
import com.petmgmt.appointmentservice.model.AppointmentStatus;
import com.petmgmt.appointmentservice.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Function 3: Vet appointment booking.
 * Endpoints: POST /api/appointments, GET /api/appointments/{id},
 *            GET /api/appointments/pet/{petId}, PUT /api/appointments/{id}/status
 * Interconnected with pet-service (validates petId on create) and consumed by
 * medical-record-service (validates appointmentId when adding a record).
 */
@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Appointments", description = "Vet appointment booking, linked to pets")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    @Operation(summary = "Book a new appointment for a pet")
    public ResponseEntity<Appointment> create(@Valid @RequestBody Appointment appointment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.create(appointment));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an appointment by id")
    public ResponseEntity<Appointment> getById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getById(id));
    }

    @GetMapping("/pet/{petId}")
    @Operation(summary = "List all appointments for a pet")
    public ResponseEntity<List<Appointment>> getByPet(@PathVariable Long petId) {
        return ResponseEntity.ok(appointmentService.getByPetId(petId));
    }

    @GetMapping
    @Operation(summary = "List all appointments")
    public ResponseEntity<List<Appointment>> getAll() {
        return ResponseEntity.ok(appointmentService.getAll());
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update an appointment's status (SCHEDULED, COMPLETED, CANCELLED)")
    public ResponseEntity<Appointment> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        AppointmentStatus status = AppointmentStatus.valueOf(body.get("status").toUpperCase());
        return ResponseEntity.ok(appointmentService.updateStatus(id, status));
    }
}
