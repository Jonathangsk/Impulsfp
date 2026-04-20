package com.impulsfp.server.dto;

import java.util.List;


/**
 * DTO per a la creació d'una oferta de pràctiques, que conté els camps necessaris per a la creació d'una nova oferta.
 *
 * @author Jonathan Giraldo Giraldo
 */
public class CreateOfferDto {

    private String title;
    private String description;
    private List<String> skills;
    private String location;
    private String modality;
    private String contractType;
    private Double salary;
    private String cycle;

    // getters & setters

    public String getCycle() {
        return cycle;
    }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public List<String> getSkills() { return skills; }
    public String getLocation() { return location; }
    public String getModality() { return modality; }
    public String getContractType() { return contractType; }
    public Double getSalary() { return salary; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setSkills(List<String> skills) { this.skills = skills; }
    public void setLocation(String location) { this.location = location; }
    public void setModality(String modality) { this.modality = modality; }
    public void setContractType(String contractType) { this.contractType = contractType; }
    public void setSalary(Double salary) { this.salary = salary; }
    public void setCycle(String cycle) {
            this.cycle = cycle;
        }
}