package com.Serah.DoctorAppointmentSystem.email;

public interface EmailService {

    String sendSimpleEmail(EmailDetails emailDetails);

    String sendSimpleMessage(EmailDetails emailDetails);

}
