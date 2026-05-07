package com.impulsfp.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;


/**
 * DTO per representar les dades d'una sol·licitud d'aplicació a una oferta de pràctiques, incloent informació sobre l'oferta, l'empresa i l'estat de la sol·licitud.
 *
 * @author Jonathan Giraldo Giraldo
 */
public class ApplicationDto {

    private Long id;
    private String offerTitle;
    private String companyName;
    private String location;
    private String status;


    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime appliedAt;

    // getters & setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOfferTitle() { return offerTitle; }
    public void setOfferTitle(String offerTitle) { this.offerTitle = offerTitle; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getAppliedAt() { return appliedAt; }
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }
}