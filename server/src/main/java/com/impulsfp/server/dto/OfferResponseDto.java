package com.impulsfp.server.dto;

import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * DTO per representar les dades d'una oferta de feina en les respostes de l'API; Inclou informació com el títol, descripció, ubicació, modalitat, tipus de contracte, salari, data de creació, estat, cicle formatiu associat, nom de l'empresa, habilitats requerides i nombre d'aplicants.
 *
 * @author Jonathan Giraldo Giraldo
 */

public class OfferResponseDto {

    private Long id;
    private String title;
    private String description;
    private String location;
    private String modality;
    private String contractType;
    private Double salary;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime creationDate;
    private String state;
    private String cycle;
    private String companyName;
    private List<String> skills;
    private int applicantsCount;
    private String testType;
    private String testQuestion;
    private String codeSnippet;
    private String options;

    // getters & setters


    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getTestQuestion() {
        return testQuestion;
    }

    public void setTestQuestion(String testQuestion) {
        this.testQuestion = testQuestion;
    }

    public String getCodeSnippet() {
        return codeSnippet;
    }

    public void setCodeSnippet(String codeSnippet) {
        this.codeSnippet = codeSnippet;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

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