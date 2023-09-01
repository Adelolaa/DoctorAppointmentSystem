package com.Serah.DoctorAppointmentSystem.patient.service;

import com.Serah.DoctorAppointmentSystem.email.EmailDetails;
import com.Serah.DoctorAppointmentSystem.email.EmailService;
import com.Serah.DoctorAppointmentSystem.patient.dto.GetRequest;
import com.Serah.DoctorAppointmentSystem.patient.dto.PatientRequest;
import com.Serah.DoctorAppointmentSystem.patient.entity.Patient;
import com.Serah.DoctorAppointmentSystem.patient.repository.PatientRepository;
import com.Serah.DoctorAppointmentSystem.response.Data;
import com.Serah.DoctorAppointmentSystem.response.Response;
import com.Serah.DoctorAppointmentSystem.role.RoleRepository;
import com.Serah.DoctorAppointmentSystem.role.Roles;
import com.Serah.DoctorAppointmentSystem.security.dto.LoginRequest;
import com.Serah.DoctorAppointmentSystem.utils.AccountUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

       private final PatientRepository patientRepository;
       private final RoleRepository roleRepository;
       private final PasswordEncoder passwordEncoder;
       private final EmailService emailService;
       private final AuthenticationManager authenticationManager;

    public PatientServiceImpl(PatientRepository patientRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, EmailService emailService, AuthenticationManager authenticationManager) {
        this.patientRepository = patientRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.authenticationManager = authenticationManager;
    }


    @Override
    public ResponseEntity<Response> signUp(PatientRequest patientRequest) {

        boolean isEmailExist = patientRepository.existsByEmail(patientRequest.getEmail());
        if (isEmailExist) {
            return ResponseEntity.ok(Response.builder()
                    .responseCode(AccountUtil.USER_EXISTS_CODE)
                    .responseMessage(AccountUtil.USER_EXISTS_MESSAGE)
                    .data(null)
                    .build());
        }

        Roles roles = roleRepository.findByRoleName("ROLE_PATIENT").orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

            Patient patient = Patient.builder()
                    .email(patientRequest.getEmail())
                    .age(patientRequest.getAge())
                    .name(patientRequest.getName())
                    .gender(patientRequest.getGender())
                    .password(passwordEncoder.encode(patientRequest.getPassword()))

                    .role(Collections.singleton(roles))

                    .build();

            Patient savedPatient = patientRepository.save(patient);

            EmailDetails message = EmailDetails.builder()
                    .recipient(savedPatient.getEmail())
                    .subject("Account Created Successfully")
                    .messageBody("You're Welcome to Doc on the Go." + "Your username is " + savedPatient.getEmail())
                    .build();
            emailService.sendSimpleEmail(message);


            return ResponseEntity.ok(Response.builder()
                    .responseCode(AccountUtil.ACCOUNT_CREATION_CODE)
                    .responseMessage(AccountUtil.ACCOUNT_CREATION_MESSAGE)
                    .data(Data.builder()
                            .name(savedPatient.getName())
                            .email(savedPatient.getEmail())
                            .description("User Created Successfully")
                            .build())
                    .build());
        }


    @Override
    public ResponseEntity<Response>signIn(LoginRequest loginRequest) {
            boolean exists = patientRepository.existsByEmail(loginRequest.getEmail());

            if (!exists ){
                return ResponseEntity.ok(Response.builder()
                        .responseCode(AccountUtil.UNSUCCESSFUL_LOGIN_RESPONSE_CODE)
                        .responseMessage(AccountUtil.USERNAME_OR_PASSWORD_INCORRECT_MESSAGE)
                        .build());
            }else {

                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                return new ResponseEntity<>(
                        Response.builder()
                                .responseCode(AccountUtil.SUCCESSFUL_LOGIN_RESPONSE_CODE)
                                .responseMessage(AccountUtil.SUCCESSFUL_LOGIN_MESSAGE)
                                .build(), HttpStatus.CREATED);
            }
        }
    @Override
    public ResponseEntity<Response> resetPassword(LoginRequest loginRequest) {

        if (!patientRepository.existsByEmail(loginRequest.getEmail())){
            return ResponseEntity.ok(Response.builder()
                    .responseCode(AccountUtil.UNSUCCESSFUL_LOGIN_RESPONSE_CODE)
                    .responseMessage(AccountUtil.EMAIL_DOES_NOT_EXIST_MESSAGE)
                    .data(null)
                    .build());
        }else {
            Patient patient = patientRepository.findByEmail(loginRequest.getEmail()).get();
            String encoder = passwordEncoder.encode(loginRequest.getPassword());
            patient.setPassword(encoder);
            patientRepository.save(patient);

            return ResponseEntity.ok(Response.builder()
                    .responseCode(AccountUtil.SUCCESS_MESSAGE_CODE)
                    .responseMessage(AccountUtil.SUCCESS_MESSAGE)
                    .data(null)
                    .build());
        }
    }

    @Override
    public List<Response> getAllPatient() {
        List<Patient> patientsList = patientRepository.findAll();

        List<Response> responses = new ArrayList<>();

        for (Patient patients: patientsList){
            responses.add(Response.builder()
                    .responseCode(AccountUtil.SUCCESS_MESSAGE_CODE)
                    .responseMessage(AccountUtil.SUCCESS_MESSAGE)
                    .data(Data.builder()
                            .name(patients.getName())
                            .email(patients.getEmail())
                            .description(patients.getRole().toString())
                            .build())
                    .build());
        }
        return responses;
    }

    @Override
    public Response getPatientByEmail(GetRequest getRequest) {

        boolean isExistsByEmail= patientRepository.existsByEmail(getRequest.getEmail());
        if (isExistsByEmail){
            Patient patients = patientRepository.findByEmail(getRequest.getEmail()).get();

            return Response.builder()
                    .responseCode(AccountUtil.SUCCESS_MESSAGE_CODE)
                    .responseMessage(AccountUtil.SUCCESS_MESSAGE)
                    .data(Data.builder()
                            .name(patients.getName())
                            .email(patients.getEmail())
                            .description(patients.getRole().toString())
                            .build())
                    .build();

        }else{
            return Response.builder()
                    .responseCode(AccountUtil.USER_NOT_FOUND_CODE)
                    .responseMessage(AccountUtil.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
    }
}
