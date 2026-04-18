package com.impulsfp.server.model;

import com.impulsfp.server.enums.*;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "offers")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String description;

    private String location;

    @Enumerated(EnumType.STRING)
    private Cycle cycle;

    @Enumerated(EnumType.STRING)
    private Modality modality;

    @Enumerated(EnumType.STRING)
    private ContractType contractType;

    private Double salary;

    private LocalDateTime creationDate;

    @Enumerated(EnumType.STRING)
    private OfferState state;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "offer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OfferSkill> requiredSkills;

    @ManyToMany
    @JoinTable(
            name = "offer_applicants",
            joinColumns = @JoinColumn(name = "offer_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> applicants;

    // GETTERS & SETTERS

    public Long getId() { return id; }

    public Cycle getCycle() {
        return cycle;
    }

    public void setCycle(Cycle cycle) {
        this.cycle = cycle;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Modality getModality() { return modality; }
    public void setModality(Modality modality) { this.modality = modality; }

    public ContractType getContractType() { return contractType; }
    public void setContractType(ContractType contractType) { this.contractType = contractType; }

    public Double getSalary() { return salary; }
    public void setSalary(Double salary) { this.salary = salary; }

    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }

    public OfferState getState() { return state; }
    public void setState(OfferState state) { this.state = state; }

    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }

    public List<OfferSkill> getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(List<OfferSkill> requiredSkills) { this.requiredSkills = requiredSkills; }

    public List<Student> getApplicants() { return applicants; }
    public void setApplicants(List<Student> applicants) { this.applicants = applicants; }
}