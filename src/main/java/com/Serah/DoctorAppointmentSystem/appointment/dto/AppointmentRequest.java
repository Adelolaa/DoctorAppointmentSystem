package com.Serah.DoctorAppointmentSystem.appointment.dto;

import com.Serah.DoctorAppointmentSystem.doctor.entity.Doctor;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class AppointmentRequest {
    private String complain;
    private Date appointmentDate;
    private Long patientId;
    //private Long doctorId;
    private Doctor doctorId;

}
