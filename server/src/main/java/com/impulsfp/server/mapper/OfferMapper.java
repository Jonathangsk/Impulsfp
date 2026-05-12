package com.impulsfp.server.mapper;

import com.impulsfp.server.dto.OfferResponseDto;
import com.impulsfp.server.model.Offer;

import com.impulsfp.server.repository.ApplicationRepository;
import com.impulsfp.server.repository.OfferTestRepository;
import org.springframework.stereotype.Component;


/**
 * Mapper per convertir les entitats de tipus Offer a DTOs de tipus OfferResponseDto; Utilitza el repositori d'aplicacions per comptar el nombre de sol·licituds associades a cada oferta.
 *
 * @author Jonathan Giraldo Giraldo
 */
@Component
public class OfferMapper {

    private final OfferTestRepository offerTestRepository;
    private final ApplicationRepository applicationRepository;

    public OfferMapper(OfferTestRepository offerTestRepository, ApplicationRepository applicationRepository) {
        this.offerTestRepository = offerTestRepository;
        this.applicationRepository = applicationRepository;
    }

    /**
     * Converteix una entitat Offer a un DTO OfferResponseDto; Mapeja tots els camps de l'oferta, incloent el nom de l'empresa i les habilitats requerides, i també compta el nombre de sol·licituds associades a l'oferta utilitzant el repositori d'aplicacions.
     * @param offer L'entitat Offer que es vol convertir a DTO; Ha de contenir tota la informació necessària per omplir els camps del DTO, incloent les relacions amb l'empresa i les habilitats.
     * @return Un objecte OfferResponseDto que representa l'oferta de feina, amb tots els camps mapejats i el nombre de sol·licituds associades a l'oferta.
     */
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

        offerTestRepository.findByOffer(offer).ifPresent(test -> {
            dto.setTestType(test.getType().name());
            dto.setTestQuestion(test.getQuestion());
            dto.setCodeSnippet(test.getCodeSnippet());
            dto.setOptions(test.getOptions());
        });

        dto.setApplicantsCount(
                (int) applicationRepository.countByOffer(offer)
        );

        return dto;
    }
}