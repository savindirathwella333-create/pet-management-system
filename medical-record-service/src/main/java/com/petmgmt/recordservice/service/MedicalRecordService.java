package com.petmgmt.recordservice.service;

import com.petmgmt.recordservice.client.AppointmentClient;
import com.petmgmt.recordservice.exception.BadRequestException;
import com.petmgmt.recordservice.exception.ResourceNotFoundException;
import com.petmgmt.recordservice.model.MedicalRecord;
import com.petmgmt.recordservice.repository.MedicalRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository recordRepository;
    private final AppointmentClient appointmentClient;

    public MedicalRecordService(MedicalRecordRepository recordRepository, AppointmentClient appointmentClient) {
        this.recordRepository = recordRepository;
        this.appointmentClient = appointmentClient;
    }

    public MedicalRecord create(MedicalRecord record) {
        if (!appointmentClient.appointmentExists(record.getAppointmentId())) {
            throw new BadRequestException("No appointment found with id: " + record.getAppointmentId());
        }
        return recordRepository.save(record);
    }

    public MedicalRecord getById(Long id) {
        return recordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found with id: " + id));
    }

    public List<MedicalRecord> getByPetId(Long petId) {
        return recordRepository.findByPetId(petId);
    }

    public List<MedicalRecord> getAll() {
        return recordRepository.findAll();
    }

    public void delete(Long id) {
        MedicalRecord existing = getById(id);
        recordRepository.delete(existing);
    }
}
