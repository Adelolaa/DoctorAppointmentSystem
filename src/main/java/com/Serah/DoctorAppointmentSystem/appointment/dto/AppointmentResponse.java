package com.Serah.DoctorAppointmentSystem.appointment.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class AppointmentResponse {
    private String username;
    private String name;
    private String response;
}
