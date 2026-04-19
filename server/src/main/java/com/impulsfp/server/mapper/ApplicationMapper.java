package com.impulsfp.server.mapper;

import com.impulsfp.server.dto.ApplicationDto;
import com.impulsfp.server.model.Application;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper {

    public ApplicationDto toDto(Application app){

        ApplicationDto dto = new ApplicationDto();

        dto.setId(app.getId());
        dto.setOfferTitle(app.getOffer().getTitle());
        dto.setCompanyName(app.getOffer().getCompany().getName());
        dto.setLocation(app.getOffer().getLocation());
        dto.setStatus(app.getStatus().name());
        dto.setAppliedAt(app.getAppliedAt());

        return dto;
    }
}