package com.petmgmt.recordservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "medical_records")
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long petId; // references Pet.id in pet-service

    @NotNull
    private Long appointmentId; // references Appointment.id in appointment-service

    @NotBlank
    private String diagnosis;

    private String treatment;

    private LocalDateTime recordDate = LocalDateTime.now();

    public MedicalRecord() {}

    public MedicalRecord(Long petId, Long appointmentId, String diagnosis, String treatment) {
        this.petId = petId;
        this.appointmentId = appointmentId;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPetId() { return petId; }
    public void setPetId(Long petId) { this.petId = petId; }
    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }
    public LocalDateTime getRecordDate() { return recordDate; }
    public void setRecordDate(LocalDateTime recordDate) { this.recordDate = recordDate; }
}
