package com.lav.dsite.utils;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ResponseHandler {

    public static void writeResponseAsJson(HttpServletResponse response, Object body, HttpStatus status, ObjectMapper objectMapper) throws IOException {
        
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status.value());

        String jsonString = objectMapper.writeValueAsString(body);

        try (PrintWriter writer = response.getWriter()) {
            writer.write(jsonString);
            writer.flush();
        } catch (IOException e) {
            throw new IOException("Failed to write response", e);
        }
    }

    public static String getBearerToken(HttpServletRequest request) {
        
        final String authHeaderKey = "Authorization"; 
        final String bearerPrefix = "Bearer ";
        final String authHeaderValue = request.getHeader(authHeaderKey);
        
        if (authHeaderValue == null || !(authHeaderValue.startsWith(bearerPrefix))) {
            return null;
        }

        String result = authHeaderValue.substring(bearerPrefix.length()).trim();

        if (result.isEmpty()) {
            return null;
        }

        return result;
    }

    public static String getBearerToken(String authorizationHeader) {

        final String bearerPrefix = "Bearer ";

        if (!(authorizationHeader.startsWith(bearerPrefix))) {
            return null;
        }

        String result = authorizationHeader.substring(bearerPrefix.length()).trim();

        if (result.isEmpty()) {
            return null;
        }

        return result;
    }

}
