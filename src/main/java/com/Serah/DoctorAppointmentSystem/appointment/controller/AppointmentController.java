package com.Serah.DoctorAppointmentSystem.appointment.controller;

import com.Serah.DoctorAppointmentSystem.appointment.dto.AppointmentRequest;
import com.Serah.DoctorAppointmentSystem.appointment.dto.AppointmentResponse;
import com.Serah.DoctorAppointmentSystem.appointment.service.AppointmentService;
import com.Serah.DoctorAppointmentSystem.response.Response;
import lombok.AllArgsConstructor;
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

    @PostMapping("/createAppointment")
    public ResponseEntity<AppointmentResponse> createAppointment(@RequestBody AppointmentRequest appointmentRequest){
        return new ResponseEntity<>(appointmentService.createAppointment(appointmentRequest), HttpStatus.CREATED);
    }

    @GetMapping("/fetchAllAppointments")
    public ResponseEntity<List<AppointmentResponse>> fetchAllAppointments(){
        return ResponseEntity.ok(appointmentService.viewAllAppointments());
    }

    @GetMapping("{id}")
    public ResponseEntity<AppointmentResponse> fetchSingleAppointmentById(@PathVariable("id") Long id){
        return ResponseEntity.ok(appointmentService.viewSingleAppointment(id));
    }

    @GetMapping("{id}/completed")
    public ResponseEntity<String> markAppointmentAsFulfilled(@PathVariable("id") Long id){
        return new ResponseEntity<>(appointmentService.markAppointmentAsFulfilled(id), HttpStatus.OK);
    }

    @GetMapping("user")
    public ResponseEntity<List<AppointmentResponse>> fetchAppointmentByUser(@RequestParam("user") String username){
        return ResponseEntity.ok(appointmentService.findAppointmentByUser(username));
    }

    @GetMapping("fulfilled")
    public ResponseEntity<List<AppointmentResponse>> fetchFulfilledAppointments(){
        return ResponseEntity.ok(appointmentService.fetchFulfilledAppointments());
    }

    @GetMapping("unfulfilled")
    public ResponseEntity<List<AppointmentResponse>> fetchUnFulfilledAppointments(){
        return ResponseEntity.ok(appointmentService.fetchUnfulfilledAppointments());
    }

    @PutMapping("{id}")
    public ResponseEntity<AppointmentResponse> updateAppointmentById(@PathVariable("id") Long id){
        return ResponseEntity.ok(appointmentService.updateAppointment(id));
    }

}
