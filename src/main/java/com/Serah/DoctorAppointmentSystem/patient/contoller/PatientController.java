package com.Serah.DoctorAppointmentSystem.patient.contoller;
import com.Serah.DoctorAppointmentSystem.patient.dto.GetRequest;
import com.Serah.DoctorAppointmentSystem.patient.dto.PatientRequest;
import com.Serah.DoctorAppointmentSystem.patient.service.PatientService;
import com.Serah.DoctorAppointmentSystem.response.Response;
import com.Serah.DoctorAppointmentSystem.security.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/patient")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {

        this.patientService = patientService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Response> signup(@RequestBody PatientRequest patientRequest) {
        System.out.println("==signing up patient===");
        return patientService.signup(patientRequest);
    }

    @PostMapping("/signin")
    public ResponseEntity<Response> signin(@RequestBody LoginRequest loginRequest) {
        return patientService.signin(loginRequest);
    }

    @PutMapping("/resetPassword")
    public ResponseEntity<Response> resetPassword(@RequestBody LoginRequest loginRequest) {
        return patientService.resetPassword(loginRequest);
    }

    @GetMapping("/{Email}")
    public ResponseEntity<Response> getPatientByEmail(@PathVariable(name = "Email", required = true) String email) {
        return ResponseEntity.ok(patientService.getPatientByEmail(email));
    }

    @GetMapping("/getAllPatients")
    public List<Response> getAllPatients() {
        return patientService.getAllPatient();
    }


}