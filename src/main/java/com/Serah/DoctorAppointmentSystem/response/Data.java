package com.Serah.DoctorAppointmentSystem.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Data {

    private String name;
    private String email;
    private String description;
    private String speciality;
}
