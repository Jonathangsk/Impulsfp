package com.impulsfp.server.mapper;

import com.impulsfp.server.dto.OfferResponseDto;
import com.impulsfp.server.model.Offer;

import com.impulsfp.server.repository.ApplicationRepository;
import org.springframework.stereotype.Component;

@Component
public class OfferMapper {

    private final ApplicationRepository applicationRepository;

    public OfferMapper(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

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
        //dto.setCycle(offer.getCycle().name());
        dto.setCycle(
                offer.getCycle() != null ? offer.getCycle().name() : null
        ); //aquesta línia és per evitar el NullPointerException en cas que l'oferta no tingui cicle associat; però per ara, es obligatori als clients.

        dto.setCompanyName(offer.getCompany().getName());

        dto.setSkills(
                offer.getRequiredSkills().stream()
                        .map(s -> s.getSkill())
                        .toList()
        );

        dto.setApplicantsCount(
                (int) applicationRepository.countByOffer(offer)
        );

        return dto;
    }
}