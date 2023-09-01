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

    public PatientController(PatientService patientService){

        this.patientService = patientService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Response> registerPatient(@RequestBody PatientRequest patientRequest){
        return patientService.signUp(patientRequest);
    }

    @PostMapping("/signin")
    public ResponseEntity<Response> loginUsers(@RequestBody LoginRequest loginRequest){
        return patientService.signIn(loginRequest);
    }

    @PutMapping("/resetPassword")
    public ResponseEntity<Response> resetPassword(@RequestBody LoginRequest loginRequest){
        return patientService.resetPassword(loginRequest);
    }

    @GetMapping("/getPatientByUsername")
    public ResponseEntity<Response> getPatientByUsername(@RequestBody GetRequest getRequest){
        return ResponseEntity.ok(patientService.getPatientByEmail(getRequest));
    }

    @GetMapping("/getAllPatients")
    public List<Response> getAllPatients(){
        return patientService.getAllPatient();
    }

}
