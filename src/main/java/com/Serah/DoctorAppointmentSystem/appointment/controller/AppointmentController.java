package com.Serah.DoctorAppointmentSystem.appointment.controller;

import com.Serah.DoctorAppointmentSystem.appointment.dto.AppointmentRequest;
import com.Serah.DoctorAppointmentSystem.appointment.dto.AppointmentResponse;
import com.Serah.DoctorAppointmentSystem.appointment.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointment")

public class AppointmentController {

@Autowired
private AppointmentService appointmentService;

    @PostMapping("/bookAppointment")
    public String bookAppointment(@RequestBody AppointmentRequest appointmentRequest){
        return appointmentService.bookAppointment(appointmentRequest);
    }

}
