package com.impulsfp.server.model;

import jakarta.persistence.*;


/**
 * Entitat que representa un usuari del sistema, amb camps per a nom d'usuari, contrasenya i rol; aquesta classe es mapeja a la taula "users" de la base de dades.
 *
 * @author Jonathan Giraldo Giraldo
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //l'identificador de l'usuari es genera automàticament per la base de dades
    private Long id;

    private String username;
    private String password;
    private String role;

    // getters y setters
    public Long getId() { return id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}