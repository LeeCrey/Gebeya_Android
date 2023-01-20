package com.example.online_ethio_gebeya.helpers;

import com.example.online_ethio_gebeya.models.Customer;
import com.example.online_ethio_gebeya.models.responses.RegistrationResponse;
import com.example.online_ethio_gebeya.models.responses.SessionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper {
    public static RegistrationResponse parseAccountError(String message) throws JsonProcessingException {
        return new ObjectMapper().readerFor(RegistrationResponse.class).readValue(message);
    }

    public static SessionResponse parseLoginError(String message) throws JsonProcessingException {
        return new ObjectMapper().readerFor(SessionResponse.class).readValue(message);
    }

    public static Customer parseCustomer(String message) throws JsonProcessingException {
        return new ObjectMapper().readerFor(Customer.class).readValue(message);
    }

    public static RegistrationResponse parseSignUpError(String error) throws JsonProcessingException {
        return new ObjectMapper().readerFor(RegistrationResponse.class).readValue(error);
    }
}
