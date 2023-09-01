package com.Serah.DoctorAppointmentSystem.appointment.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class AppointmentRequest {

    private String username;
    private String password;
    private LocalDate localDate;
    private String message;

}
