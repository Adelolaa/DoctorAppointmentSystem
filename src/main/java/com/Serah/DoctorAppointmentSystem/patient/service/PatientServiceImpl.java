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
import com.Serah.DoctorAppointmentSystem.security.JwtTokenProvider;
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
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

       private final PatientRepository patientRepository;
       private final RoleRepository roleRepository;
       private final PasswordEncoder passwordEncoder;
       private final EmailService emailService;
       private final AuthenticationManager authenticationManager;
       private final JwtTokenProvider jwtTokenProvider;

    public PatientServiceImpl(PatientRepository patientRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, EmailService emailService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.patientRepository = patientRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    public ResponseEntity<Response> signup(PatientRequest patientRequest) {

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
                    .username(patientRequest.getUsername())
                    .address(patientRequest.getAddress())
                    .nextOfKin(patientRequest.getNextOfKin())
                    .password(passwordEncoder.encode(patientRequest.getPassword()))

                    .role(Collections.singleton(roles))

                    .build();

            Patient savedPatient = patientRepository.save(patient);

            EmailDetails message = EmailDetails.builder()
                    .recipient(savedPatient.getEmail())
                    .subject("Account Created Successfully")
                    .messageBody("You're Welcome to Doc on the Go." + "Your username is " + savedPatient.getUsername())
                    .build();
            emailService.sendSimpleEmail(message);


            return ResponseEntity.ok(Response.builder()
                    .responseCode(AccountUtil.ACCOUNT_CREATION_CODE)
                    .responseMessage(AccountUtil.ACCOUNT_CREATION_MESSAGE)
                    .data(Data.builder()
                            .name(savedPatient.getName())
                            .email(savedPatient.getEmail())
                            .username(savedPatient.getUsername())
                            .description("User Created Successfully")
                            .build())
                    .build());
        }


    @Override
    public ResponseEntity<Response>signin(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        authentication.getName();
        authentication.getCredentials();

        return new  ResponseEntity<>(
                Response.builder()
                        .responseCode(AccountUtil.SUCCESSFUL_LOGIN_RESPONSE_CODE)
                        .responseMessage(AccountUtil.SUCCESSFUL_LOGIN_MESSAGE)
                        .data(Data.builder()
                                .name(authentication.getName())
                                .description("token: " + jwtTokenProvider.generateToken(authentication))
                                .build())
                        .build(), HttpStatus.CREATED);


//            boolean exists = patientRepository.existsByEmail(loginRequest.getEmail());
//
//            if (!exists ){
//                return ResponseEntity.ok(Response.builder()
//                        .responseCode(AccountUtil.UNSUCCESSFUL_LOGIN_RESPONSE_CODE)
//                        .responseMessage(AccountUtil.USERNAME_OR_PASSWORD_INCORRECT_MESSAGE)
//                        .build());
//            }else {
//
//                Authentication authentication = authenticationManager.authenticate(
//                        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//                return new ResponseEntity<>(
//                        Response.builder()
//                                .responseCode(AccountUtil.SUCCESSFUL_LOGIN_RESPONSE_CODE)
//                                .responseMessage(AccountUtil.SUCCESSFUL_LOGIN_MESSAGE)
//                                .build(), HttpStatus.CREATED);
//            }
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
    public Response getPatientByEmail(String email) {

        boolean isExistsByEmail= patientRepository.existsByEmail(email);
        if (isExistsByEmail){
            Patient patients = patientRepository.findByEmail(email).get();

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

//    @Override
//    public ResponseEntity<Response> updatePatient(PatientRequest patientRequest) {
//        boolean isExistsByEmail = patientRepository.existsByEmail(patientRequest.getEmail());
//        if (isExistsByEmail) {
//            Patient patient = Patient.builder()
//                    .username(patientRequest.getUsername())
//                    .name(patientRequest.getName())
//                    .email(patientRequest.getEmail())
//                    .gender(patientRequest.getGender())
//                    .age(patientRequest.getAge())
//                    //.dateOfBirth(patientRequest.getDateOfBirth())
//                    .address(patientRequest.getAddress())
//                    .nextOfKin(patientRequest.getNextOfKin())
//                    .build();
//
//            Patient savedPatient = patientRepository.save(patient);
//
//            EmailDetails message = EmailDetails.builder()
//                    .recipient(savedPatient.getEmail())
//                    .subject("Account Successfully Updated")
//                    .messageBody(savedPatient.getUsername() + "Your Doc on the Go account has been successfully updated ")
//                    .build();
//            emailService.sendSimpleEmail(message);
//
//
//            return ResponseEntity.ok(Response.builder()
//                    .responseCode(AccountUtil.USER_UPDATE_SUCCESS_CODE)
//                    .responseMessage(AccountUtil.USER_UPDATE_SUCCESS_MESSAGE)
//                    .data(Data.builder()
//                            .username(patientRequest.getUsername())
//                            .description("User Updated Successfully")
//                            .build())
//                    .build());
//
//        }else return ResponseEntity.ofNullable(Response.builder()
//                .responseCode(AccountUtil.USER_NOT_FOUND_CODE)
//                .responseMessage(AccountUtil.USER_NOT_FOUND_MESSAGE)
//                .data(null)
//                .build());

   // }
    @Override
    public ResponseEntity<Response> updatePatient(PatientRequest patientRequest) {
        Optional<Patient> existingPatientOptional = patientRepository.findByEmail(patientRequest.getEmail());

        if (existingPatientOptional.isPresent()) {


            Patient existingPatient = existingPatientOptional.get();


            existingPatient.setUsername(patientRequest.getUsername());
            existingPatient.setName(patientRequest.getName());
            existingPatient.setGender(patientRequest.getGender());
            existingPatient.setAge(patientRequest.getAge());
            existingPatient.setAddress(patientRequest.getAddress());
            existingPatient.setNextOfKin(patientRequest.getNextOfKin());

            // Save the updated patient information
            Patient updatedPatient = patientRepository.save(existingPatient);

            // Send an email notification about the account update
            EmailDetails message = EmailDetails.builder()
                    .recipient(updatedPatient.getEmail())
                    .subject("Account Successfully Updated")
                    .messageBody(updatedPatient.getUsername() + ", your Doc on the Go account has been successfully updated.")
                    .build();
            emailService.sendSimpleEmail(message);

            // Return a success response
            return ResponseEntity.ok(Response.builder()
                    .responseCode(AccountUtil.USER_UPDATE_SUCCESS_CODE)
                    .responseMessage(AccountUtil.USER_UPDATE_SUCCESS_MESSAGE)
                    .data(Data.builder()
                            .username(updatedPatient.getUsername())
                            .description("User Updated Successfully")
                            .build())
                    .build());
        } else {
            // The patient with the given email does not exist, so return an error response
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.builder()
                    .responseCode(AccountUtil.USER_NOT_FOUND_CODE)
                    .responseMessage(AccountUtil.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build());
        }
    }


}
