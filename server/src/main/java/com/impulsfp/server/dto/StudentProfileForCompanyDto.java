package com.impulsfp.server.dto;

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
