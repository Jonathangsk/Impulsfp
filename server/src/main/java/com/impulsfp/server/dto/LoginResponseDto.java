package com.impulsfp.server.dto;


/**
 * DTO per representar les dades de resposta d'inici de sessió enviades al client
 *
 * @author Jonathan Giraldo Giraldo
 */
public class LoginResponseDto {

    private String sessionId;
    private String userType;

    public LoginResponseDto(String sessionId, String userType) {
        this.sessionId = sessionId;
        this.userType = userType;
    }

    public String getSessionId() { return sessionId; }
    public String getUserType() { return userType; }
}