package com.Serah.DoctorAppointmentSystem.doctor.service;

import com.Serah.DoctorAppointmentSystem.doctor.dto.DoctorRequest;
import com.Serah.DoctorAppointmentSystem.doctor.dto.GetRequest;
import com.Serah.DoctorAppointmentSystem.doctor.entity.Doctor;
import com.Serah.DoctorAppointmentSystem.response.Response;
import com.Serah.DoctorAppointmentSystem.security.dto.LoginRequest;
import org.springframework.http.ResponseEntity;

import javax.print.Doc;
import java.time.LocalDate;
import java.util.List;

public interface DoctorService {
    Response createAccount(DoctorRequest doctorRequest);

    ResponseEntity<Response> signIn(LoginRequest loginRequest);

    ResponseEntity<Response> resetPassword(LoginRequest loginRequest);
    Response getDoctorByEmail(GetRequest getRequest);

     List<Response> getAllDoctor();

    Doctor randomDoctorOnSpecificDay(LocalDate localDate);

    Doctor randomizingRandomDoctors(Doctor doctor, LocalDate localDate);

    String deleteDoctor(long id);
}