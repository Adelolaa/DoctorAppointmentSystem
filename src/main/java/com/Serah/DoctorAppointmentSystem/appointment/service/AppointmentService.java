package com.Serah.DoctorAppointmentSystem.appointment.service;

import com.Serah.DoctorAppointmentSystem.appointment.dto.AppointmentRequest;
import com.Serah.DoctorAppointmentSystem.appointment.dto.AppointmentResponse;
import com.Serah.DoctorAppointmentSystem.patient.dto.PatientRequest;
import com.Serah.DoctorAppointmentSystem.response.Response;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AppointmentService {

    AppointmentResponse createAppointment(AppointmentRequest appointmentRequestDto);

    AppointmentResponse updateAppointment(Long id);

    List<AppointmentResponse> viewAllAppointments();

    List<AppointmentResponse> findAppointmentByUser(String username);

    AppointmentResponse viewSingleAppointment(Long id);

    String markAppointmentAsFulfilled(Long id);

    List<AppointmentResponse> fetchFulfilledAppointments();

    List<AppointmentResponse> fetchUnfulfilledAppointments();

}
