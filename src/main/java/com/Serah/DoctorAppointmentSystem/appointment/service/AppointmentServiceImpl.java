package com.Serah.DoctorAppointmentSystem.appointment.service;

import com.Serah.DoctorAppointmentSystem.appointment.dto.AppointmentRequest;
import com.Serah.DoctorAppointmentSystem.appointment.dto.AppointmentResponse;
import com.Serah.DoctorAppointmentSystem.appointment.entity.Appointment;
import com.Serah.DoctorAppointmentSystem.appointment.repository.AppointmentRepository;
import com.Serah.DoctorAppointmentSystem.doctor.entity.Doctor;
import com.Serah.DoctorAppointmentSystem.doctor.repository.DoctorRepository;
import com.Serah.DoctorAppointmentSystem.doctor.service.DoctorService;
import com.Serah.DoctorAppointmentSystem.email.EmailDetails;
import com.Serah.DoctorAppointmentSystem.email.EmailService;
import com.Serah.DoctorAppointmentSystem.patient.entity.Patient;
import com.Serah.DoctorAppointmentSystem.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import java.security.Principal;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {


    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final EmailService emailService;


    public String bookAppointment(AppointmentRequest appointmentRequest) {

        try {
            Optional<Appointment> appointmentOptional = appointmentRepository.findAppointmentByDoctorIdAndAppointmentDate(
                    appointmentRequest.getDoctorId(), appointmentRequest.getAppointmentDate());
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
           UserDetails userDetails = (UserDetails) principal;
            String email = userDetails.getUsername();
            System.out.println(userDetails.getUsername());
            if (appointmentOptional.isPresent()) {
                Appointment appointment = appointmentOptional.get();
                Patient patient = appointment.getPatient();
                if (patient != null) {

                    EmailDetails emailDetails = EmailDetails.builder()
                            .recipient(email)
                            .subject("Appointment Not Booked")
                            .messageBody("Dear " + email + ", Your appointment has not been successfully booked. Kindly choose another date.")
                            .build();
                    emailService.sendSimpleEmail(emailDetails);

                    return "Appointment Date has already been Booked";
                } else {

                    return "No patient associated with this appointment";
                }
            }
            Optional<Doctor>optionalDoctor = doctorRepository.findById(appointmentRequest.getDoctorId());
            Optional<Patient>optionalPatient = patientRepository.findByEmail(email);
            if(optionalPatient.isEmpty() || optionalDoctor.isEmpty())
            {
                return "Patient or doctor does not exist";
            }
            System.out.println(optionalDoctor.get().getName());

            Appointment appointment = Appointment.builder()
                    .appointmentDate(appointmentRequest.getAppointmentDate())
                    .doctor(optionalDoctor.get())
                    .complain(appointmentRequest.getComplain())
                    .build();
            appointmentRepository.save(appointment);

            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(email)
                    .subject("Appointment Booked Successfully")
                    .messageBody("Dear patient, Your appointment has been successfully booked. Your appointment date is " + appointmentRequest.getAppointmentDate())
                    .build();
            emailService.sendSimpleEmail(emailDetails);

            return "Appointment Successfully Booked";
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }


    }
}









