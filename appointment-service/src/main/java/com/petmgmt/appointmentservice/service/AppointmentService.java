package com.petmgmt.appointmentservice.service;

import com.petmgmt.appointmentservice.client.PetClient;
import com.petmgmt.appointmentservice.exception.BadRequestException;
import com.petmgmt.appointmentservice.exception.ResourceNotFoundException;
import com.petmgmt.appointmentservice.model.Appointment;
import com.petmgmt.appointmentservice.model.AppointmentStatus;
import com.petmgmt.appointmentservice.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PetClient petClient;

    public AppointmentService(AppointmentRepository appointmentRepository, PetClient petClient) {
        this.appointmentRepository = appointmentRepository;
        this.petClient = petClient;
    }

    public Appointment create(Appointment appointment) {
        if (!petClient.petExists(appointment.getPetId())) {
            throw new BadRequestException("No pet found with id: " + appointment.getPetId());
        }
        return appointmentRepository.save(appointment);
    }

    public Appointment getById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
    }

    public List<Appointment> getByPetId(Long petId) {
        return appointmentRepository.findByPetId(petId);
    }

    public List<Appointment> getAll() {
        return appointmentRepository.findAll();
    }

    public Appointment updateStatus(Long id, AppointmentStatus status) {
        Appointment existing = getById(id);
        existing.setStatus(status);
        return appointmentRepository.save(existing);
    }
}
