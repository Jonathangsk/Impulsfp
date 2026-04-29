package com.impulsfp.server.dto;

import java.util.List;

/**
 * DTO per representar les dades del perfil d'estudiant enviades al client
 *
 * @author Jonathan Giraldo Giraldo
 */
public class StudentProfileDto {


    private long id;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;

    private String city;
    private String bio;
    private String cycle;
    private String experienceLevel;

    private String preferredLocation;
    private String availability;
    private String portfolio;

    private List<String> skills;
    private List<String> languages;
    private List<String> preferredRoles;

    // getters & setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getCycle() { return cycle; }
    public void setCycle(String cycle) { this.cycle = cycle; }

    public String getExperienceLevel() { return experienceLevel; }
    public void setExperienceLevel(String experienceLevel) { this.experienceLevel = experienceLevel; }

    public String getPreferredLocation() { return preferredLocation; }
    public void setPreferredLocation(String preferredLocation) { this.preferredLocation = preferredLocation; }

    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }

    public String getPortfolio() { return portfolio; }
    public void setPortfolio(String portfolio) { this.portfolio = portfolio; }

    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }

    public List<String> getLanguages() { return languages; }
    public void setLanguages(List<String> languages) { this.languages = languages; }

    public List<String> getPreferredRoles() { return preferredRoles; }
    public void setPreferredRoles(List<String> preferredRoles) { this.preferredRoles = preferredRoles; }
}