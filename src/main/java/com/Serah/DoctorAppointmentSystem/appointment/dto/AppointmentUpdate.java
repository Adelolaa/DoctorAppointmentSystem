package com.Serah.DoctorAppointmentSystem.appointment.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class AppointmentUpdate {

    private String username;
    private String doctorFirstName;
    private String doctorLastName;
    private String response;

}
