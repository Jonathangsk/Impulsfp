package com.impulsfp.server.dto;


/**
 * DTO per representar les dades d'inici de sessió enviades pel client.
 *
 * @author Jonathan Giraldo Giraldo
 */
public class LoginRequestDto {

    private String username;
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}