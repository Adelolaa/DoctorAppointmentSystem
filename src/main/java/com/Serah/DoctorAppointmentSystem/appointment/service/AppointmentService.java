package com.Serah.DoctorAppointmentSystem.appointment.service;

import com.Serah.DoctorAppointmentSystem.appointment.dto.AppointmentRequest;
import com.Serah.DoctorAppointmentSystem.appointment.dto.AppointmentResponse;


import java.util.List;

public interface AppointmentService {

    String bookAppointment  (AppointmentRequest appointmentRequest);


}
