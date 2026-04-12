package com.impulsfp.server.model;

import jakarta.persistence.*;

/**
 * Entitat que representa la relació entre una empresa i les tecnologies associades a aquesta; permet emmagatzemar les tecnologies que una empresa utilitza o requereix.
 *
 * @author Jonathan Giraldo Giraldo
 */
@Entity
@Table(name = "company_technologies")
public class CompanyTechnology {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String technology;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    // GETTERS & SETTERS

    public Long getId() { return id; }

    public String getTechnology() { return technology; }
    public void setTechnology(String technology) { this.technology = technology; }

    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }
}