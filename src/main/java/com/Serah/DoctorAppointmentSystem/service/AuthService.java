package com.Serah.DoctorAppointmentSystem.service;

import com.Serah.DoctorAppointmentSystem.response.AuthResponse;
import com.Serah.DoctorAppointmentSystem.security.dto.LoginRequest;



public interface AuthService {
    AuthResponse login(LoginRequest loginRequest);

}
