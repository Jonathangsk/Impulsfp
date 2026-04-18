package com.impulsfp.server.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OfferResponseDto {

    private Long id;
    private String title;
    private String description;
    private String location;
    private String modality;
    private String contractType;
    private Double salary;
    private LocalDateTime creationDate;
    private String state;
    private String cycle;
    private String companyName;
    private List<String> skills;
    private int applicantsCount;

    // getters & setters


    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getModality() { return modality; }
    public void setModality(String modality) { this.modality = modality; }

    public String getContractType() { return contractType; }
    public void setContractType(String contractType) { this.contractType = contractType; }

    public Double getSalary() { return salary; }
    public void setSalary(Double salary) { this.salary = salary; }

    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }

    public int getApplicantsCount() {
        return applicantsCount;
    }
    public void setApplicantsCount(int applicantsCount) {
        this.applicantsCount = applicantsCount;
    }
}