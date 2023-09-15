package com.Serah.DoctorAppointmentSystem.patient.dto;

import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientRequest {
    private String name;
    private String username;
    private String email;
    private String gender;
    //private String complain;
    private String age;
    private String password;
    private String address;
    private String nextOfKin;
    //private LocalDate dateOfBirth;
}
