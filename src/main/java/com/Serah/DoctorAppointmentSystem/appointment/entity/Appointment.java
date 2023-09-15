package com.Serah.DoctorAppointmentSystem.appointment.entity;

import com.Serah.DoctorAppointmentSystem.doctor.entity.Doctor;
import com.Serah.DoctorAppointmentSystem.patient.entity.Patient;
import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "appointment")
@Builder
@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @DateTimeFormat(pattern = "yyyy-mm-dd HH:MM:SS")
    private Date appointmentDate;
    private String complain;
    private String email;
    private String status;
    //private Long patientId;
//    private Long doctorId;

   @ManyToOne
    private Patient patient;

    @ManyToOne
    private Doctor doctor;
}
