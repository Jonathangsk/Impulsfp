package com.impulsfp.server.dto;

/**
 * Data Transfer Object (DTO) per representar el perfil d'un estudiant des de la perspectiva d'una empresa, incloent informació sobre l'aplicació i l'estat de la candidatura.
 *
 * @author Jonathan Giraldo Giraldo
 */
public class StudentProfileForCompanyDto extends StudentProfileDto {

    private Long applicationId;
    private String status;

    // getters & setters

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
