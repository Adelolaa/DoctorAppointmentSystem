package com.Serah.DoctorAppointmentSystem.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response {

    private String responseCode;
    private String responseMessage;
    private com.Serah.DoctorAppointmentSystem.response.Data data;
    private Object object;



}
