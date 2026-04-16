package com.impulsfp.server.mapper;

import com.impulsfp.server.dto.OfferResponseDto;
import com.impulsfp.server.model.Offer;

import org.springframework.stereotype.Component;

@Component
public class OfferMapper {

    public OfferResponseDto toDto(Offer offer){

        OfferResponseDto dto = new OfferResponseDto();

        dto.setId(offer.getId());
        dto.setTitle(offer.getTitle());
        dto.setDescription(offer.getDescription());
        dto.setLocation(offer.getLocation());
        dto.setModality(offer.getModality().name());
        dto.setContractType(offer.getContractType().name());
        dto.setSalary(offer.getSalary());
        dto.setCreationDate(offer.getCreationDate());
        dto.setState(offer.getState().name());

        dto.setCompanyName(offer.getCompany().getName());

        dto.setSkills(
                offer.getRequiredSkills().stream()
                        .map(s -> s.getSkill())
                        .toList()
        );

        return dto;
    }
}