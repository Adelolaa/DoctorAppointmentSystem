package com.Serah.DoctorAppointmentSystem.patient.service;

import com.Serah.DoctorAppointmentSystem.patient.dto.GetRequest;
import com.Serah.DoctorAppointmentSystem.patient.dto.PatientRequest;
import com.Serah.DoctorAppointmentSystem.response.Response;
import com.Serah.DoctorAppointmentSystem.security.dto.LoginRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PatientService {

    ResponseEntity<Response> signUp (PatientRequest patientRequest);

    ResponseEntity <Response> signIn (LoginRequest loginRequest);
    ResponseEntity<Response> resetPassword(LoginRequest loginRequest);

    List<Response> getAllPatient();

    Response getPatientByEmail(GetRequest getRequest);


}






