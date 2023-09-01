package com.Serah.DoctorAppointmentSystem.doctor.controller;
import com.Serah.DoctorAppointmentSystem.doctor.dto.DoctorRequest;
import com.Serah.DoctorAppointmentSystem.doctor.dto.GetRequest;
import com.Serah.DoctorAppointmentSystem.doctor.service.DoctorService;
import com.Serah.DoctorAppointmentSystem.response.Response;
import com.Serah.DoctorAppointmentSystem.security.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
public class DoctorController {


    private final DoctorService doctorService;

    @PostMapping("/create")
    public Response createAccount(@RequestBody DoctorRequest doctorRequest) {
        return doctorService.createAccount(doctorRequest);
    }

    @PostMapping("/signin")
    ResponseEntity<Response> signIn(@RequestBody LoginRequest loginRequest) {
        return doctorService.signIn(loginRequest);
    }

    @PutMapping("/resetpassword")
    public ResponseEntity<Response> resetPassword(@RequestBody LoginRequest loginRequest) {
        return doctorService.resetPassword(loginRequest);
    }

    @GetMapping("/getDoctorByEmail")
    public ResponseEntity<Response> getDoctorEmail(@RequestBody GetRequest getRequest) {
        return ResponseEntity.ok(doctorService.getDoctorByEmail(getRequest));

    }

    @GetMapping("/getAllDoctors")
    public List<Response> getAllDoctors() {
        return doctorService.getAllDoctor();

    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable("id") long id) {
        return ResponseEntity.ok(doctorService.deleteDoctor(id));
    }
}