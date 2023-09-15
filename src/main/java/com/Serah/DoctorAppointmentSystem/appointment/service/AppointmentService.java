package com.Serah.DoctorAppointmentSystem.appointment.service;

import com.Serah.DoctorAppointmentSystem.appointment.dto.AppointmentRequest;
import com.Serah.DoctorAppointmentSystem.appointment.dto.AppointmentResponse;
import com.Serah.DoctorAppointmentSystem.appointment.entity.Appointment;


import java.util.List;
import java.util.Optional;

public interface AppointmentService {

    String bookAppointment(AppointmentRequest appointmentRequest);
//    boolean acceptBooking (Long id);
// String acceptAppointment(Long doctorId, Long appointmentId);
    AppointmentResponse updateAppointment(Long id);

    //Optional<Appointment> findById(long id);

    List<AppointmentResponse> findAllAppointments();

    List<AppointmentResponse> findAppointmentByUser(String username);

    AppointmentResponse findSingleAppointment(Long id);

    String markAppointmentAsFulfilled(Long id);

    List<AppointmentResponse> findFulfilledAppointments();

    List<AppointmentResponse> findUnfulfilledAppointments();

}
