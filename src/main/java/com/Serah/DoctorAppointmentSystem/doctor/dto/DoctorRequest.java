package com.Serah.DoctorAppointmentSystem.doctor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorRequest {

    private String name;
    private String speciality;
    private String email;
    private String hospitalDetails;
    private String workingHours;
    private String description;
    private String password;
    private String gender;
    private String uniqueIdentifier;
}
