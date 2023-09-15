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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {


    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorService doctorService;
    private final EmailService emailService;
//    private final Patient patient;
//    private final Doctor doctor;


    @Override
    public String bookAppointment(AppointmentRequest appointmentRequest) {
        //check doctor has been booked for chosen date;
        // return error response doctor not available for the date chosen
        Optional<Appointment>appointmentOptional = appointmentRepository.findAppointmentByDoctorIdAndAppointmentDate
        (appointmentRequest.getDoctorId(),appointmentRequest.getAppointmentDate());

        if (appointmentOptional.isPresent()) {
            Appointment appointment = appointmentOptional.get();

            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(appointment.getEmail())
                    .subject("Appointment Not Booked ")
                    .messageBody("Dear"+" "+appointment.getPatient().getName()  + ", Your appointment has not been successfully booked." + "Kindly choose another date.")
                    .build();

            return "Appointment Date has already been Booked";
        }
        Appointment appointment = Appointment.builder()
                .appointmentDate(appointmentRequest.getAppointmentDate())
                .id(appointmentRequest.getPatientId())
                .complain(appointmentRequest.getComplain())
                //.doctorId(appointmentRequest.getDoctorId())
                .build();
        appointmentRepository.save(appointment);

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(appointment.getEmail())
                .subject("Appointment Booked Successfully")
                .messageBody("Dear"+" "+appointment.getPatient().getName()  + ", Your appointment has been successfully booked."+ " "+"Your appointment date is"+ " "+appointmentRequest.getAppointmentDate())
                .build();

        emailService.sendSimpleEmail(emailDetails);
        return "Appointment Successfully Booked";

    }


    @Override
    public AppointmentResponse updateAppointment(Long id) {
        return null;
    }

    @Override
    public List<AppointmentResponse> findAllAppointments() {
        return null;
    }

    @Override
    public List<AppointmentResponse> findAppointmentByUser(String username) {
        return null;
    }

    @Override
    public AppointmentResponse findSingleAppointment(Long id) {
        return null;
    }

    @Override
    public String markAppointmentAsFulfilled(Long id) {
        return null;
    }

    @Override
    public List<AppointmentResponse> findFulfilledAppointments() {
        return null;
    }

    @Override
    public List<AppointmentResponse> findUnfulfilledAppointments() {
        return null;
    }


   // public boolean acceptBooking(Long appointmentId) {
//        Optional<Appointment> appointmentOptional = appointmentRepository.findById(appointmentId);
//
//        if (appointmentOptional.isPresent()) {
//            Appointment appointment = appointmentOptional.get();
//            appointment.setStatus("Accepted");
//            appointmentRepository.save(appointment);
//            return true;
//        } else {
//            return false; // Appointment not found
//        }
//
//    }


//        public String acceptAppointment(Long doctorId, Long appointmentId) {
//            // Retrieve the doctor and appointment from repositories
//            Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
//            Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
////
//            if (doctor == null || appointment == null) {
//                return " Doctor or appointment not found";
//            }
//
//            // Check if the doctor is available to accept the appointment
//            if (!isDoctorAvailable(doctor, String.valueOf(doctor.getSpeciality().equalsIgnoreCase("speciality")))) {
//                return "Doctor is not available to accept this appointment.";
//            }
//
//            // Update appointment status and add it to the doctor's accepted appointments
//            appointment.setStatus("Accepted");
//            doctor.getAcceptedAppointments().add(appointment.getId());
//
//            appointmentRepository.save(appointment);
//            doctorRepository.save(doctor);
//
//            // Send confirmation email to the patient (implement this)
//
//            EmailDetails emailDetails = EmailDetails.builder()
//                    .subject("Appointment Booked Successfully")
//                    .messageBody("Dear patients, Your appointment has been successfully booked. Our doctor will reach out to you.")
//                    .build();
//
//            Set<Patient> patients = appointment.getPatients();
//            for (Patient patient : patients) {
//                String recipient = patient.getEmail();
//                // Now, you can use the 'recipient' to send emails to each patient individually.
//                // You can also access other patient details like patient.getName() if needed.
//                // Send emails to each patient using the 'recipient' variable.
//            }
//
//
//            emailService.sendSimpleEmail(emailDetails);
//
//            return "Appointment accepted successfully.";
//        }

    private boolean isDoctorAvailable(Doctor doctor, String speciality) {
        // Check if the doctor has the required speciality
        return doctor.getSpeciality().equalsIgnoreCase(speciality);
    }


//        private boolean isDoctorAvailable(Doctor doctor, LocalDateTime appointmentDateTime) {
//
//            // Get the doctor's working hours (you may retrieve this from a database)
//            LocalTime doctorWorkingHoursStart = LocalTime.of(9, 0); // e.g., 9:00 AM
//            LocalTime doctorWorkingHoursEnd = LocalTime.of(17, 0);  // e.g., 5:00 PM
//
//            // Check if the appointment is within the doctor's working hours
//            LocalTime appointmentTime = appointmentDateTime.toLocalTime();
//            return !appointmentTime.isBefore(doctorWorkingHoursStart) && !appointmentTime.isAfter(doctorWorkingHoursEnd); // Appointment is outside of working hours


        }









