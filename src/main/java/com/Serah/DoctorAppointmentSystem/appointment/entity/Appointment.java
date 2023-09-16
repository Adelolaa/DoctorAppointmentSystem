package com.Serah.DoctorAppointmentSystem.appointment.entity;

import com.Serah.DoctorAppointmentSystem.doctor.entity.Doctor;
import com.Serah.DoctorAppointmentSystem.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @DateTimeFormat(pattern ="yyyy-mm-dd HH:MM:SS")
    private Date appointmentDate;
    private String complain;
    @ManyToOne
    private Doctor doctor;
    @ManyToOne
    private Patient patient;
}
