package com.example.online_ethio_gebeya.helpers;

import com.example.online_ethio_gebeya.models.Customer;
import com.example.online_ethio_gebeya.models.responses.InstructionsResponse;
import com.example.online_ethio_gebeya.models.responses.SessionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper {

    public static SessionResponse parseLoginError(String message) throws JsonProcessingException {
        return new ObjectMapper().readerFor(SessionResponse.class).readValue(message);
    }

    public static Customer parseCustomer(String message) throws JsonProcessingException {
        return new ObjectMapper().readerFor(Customer.class).readValue(message);
    }

    public static InstructionsResponse parseOperationError(String error) throws JsonProcessingException {
        return new ObjectMapper().readerFor(InstructionsResponse.class).readValue(error);
    }
}
