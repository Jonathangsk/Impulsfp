package com.impulsfp.server.service;

import com.impulsfp.server.dto.CreateOfferDto;
import com.impulsfp.server.dto.OfferResponseDto;
import com.impulsfp.server.dto.StudentProfileDto;
import com.impulsfp.server.dto.UpdateOfferDto;
import com.impulsfp.server.enums.*;
import com.impulsfp.server.exception.*;
import com.impulsfp.server.mapper.OfferMapper;
import com.impulsfp.server.mapper.ProfileMapper;
import com.impulsfp.server.model.*;
import com.impulsfp.server.repository.*;
import com.impulsfp.server.session.SessionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;

@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final StudentRepository studentRepository;
    private final OfferMapper offerMapper;
    private final ProfileMapper profileMapper;

    public OfferService(OfferRepository offerRepository,
                        UserRepository userRepository,
                        CompanyRepository companyRepository,
                        StudentRepository studentRepository,
                        OfferMapper offerMapper, ProfileMapper profileMapper) {

        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.studentRepository = studentRepository;
        this.offerMapper = offerMapper;
        this.profileMapper = profileMapper;
    }

    @Transactional
    public void createOffer(String sessionId, CreateOfferDto dto){

        if(!SessionManager.isValid(sessionId)){
            throw new ApiException(ErrorCode.INVALID_SESSION, "Sessió no vàlida");
        }

        String username = SessionManager.getUsername(sessionId);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Usuari no trobat"));

        if(!user.getRole().equals("COMPANY")){
            throw new ApiException(ErrorCode.INVALID_REQUEST, "Només empreses poden crear ofertes");
        }

        Company company = companyRepository.findByUser(user)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Empresa no trobada"));

        //TODO: Validar que el número d'ofertes actives de l'empresa no superi el màxim permès
        //TODO: Valida que els camps obligatoris del DTO estiguin correctament omplerts (per exemple, que el salari sigui un número positiu, que la modalitat i el tipus de contracte siguin valors vàlids, etc.)
        Offer offer = new Offer();
        offer.setTitle(dto.getTitle());
        offer.setDescription(dto.getDescription());
        offer.setLocation(dto.getLocation());
        offer.setModality(Modality.valueOf(dto.getModality()));
        offer.setContractType(ContractType.valueOf(dto.getContractType()));
        offer.setSalary(dto.getSalary());
        offer.setCreationDate(LocalDateTime.now());
        offer.setState(OfferState.OPEN);
        offer.setCompany(company);
        offer.setCycle(Cycle.valueOf(dto.getCycle()));

        List<OfferSkill> skills = dto.getSkills().stream().map(s -> {
            OfferSkill skill = new OfferSkill();
            skill.setSkill(s);
            skill.setOffer(offer);
            return skill;
        }).toList();

        offer.setRequiredSkills(skills);

        offerRepository.save(offer);

        company.setActiveOffers(
                company.getActiveOffers() == null ? 1 : company.getActiveOffers() + 1
        );

    }




    @Transactional
    public void applyToOffer(String sessionId, Long offerId){

        if(!SessionManager.isValid(sessionId)){
            throw new ApiException(ErrorCode.INVALID_SESSION, "Sessió no vàlida");
        }

        String username = SessionManager.getUsername(sessionId);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Usuari no trobat"));

        if(!user.getRole().equals("STUDENT")){
            throw new ApiException(ErrorCode.INVALID_REQUEST, "Només estudiants poden aplicar");
        }

        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Estudiant no trobat"));

        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_REQUEST, "Oferta no trobada"));


        if(offer.getApplicants().contains(student)){
            throw new ApiException(ErrorCode.INVALID_REQUEST, "Ja estàs inscrit a aquesta oferta");
        }

        offer.getApplicants().add(student);
        offerRepository.save(offer);
    }






    public List<Offer> getMyOffers(String sessionId){

        if(!SessionManager.isValid(sessionId)){
            throw new ApiException(ErrorCode.INVALID_SESSION, "Sessió no vàlida");
        }

        String username = SessionManager.getUsername(sessionId);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Usuari no trobat"));

        if(!user.getRole().equals("COMPANY")){
            throw new ApiException(ErrorCode.INVALID_REQUEST, "Només empreses poden veure les seves ofertes");
        }

        Company company = companyRepository.findByUser(user)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Empresa no trobada"));

        return offerRepository.findByCompany(company);
    }






    public List<OfferResponseDto> getAllOffers(){

        return offerRepository.findByState(OfferState.OPEN)
                .stream()
                .map(offerMapper::toDto)
                .toList();
    }






    public List<OfferResponseDto> getOffersByLocation(String location){

        return offerRepository
                .findByLocationContainingIgnoreCaseAndState(location, OfferState.OPEN)
                .stream()
                .map(offerMapper::toDto)
                .toList();
    }





    public List<OfferResponseDto> getOffersByModality(String modality){

        return offerRepository
                .findByModalityAndState(Modality.valueOf(modality), OfferState.OPEN)
                .stream()
                .map(offerMapper::toDto)
                .toList();
    }





    public List<StudentProfileDto> getApplicants(String sessionId, Long offerId){

        if(!SessionManager.isValid(sessionId)){
            throw new ApiException(ErrorCode.INVALID_SESSION, "Sessió no vàlida");
        }

        String username = SessionManager.getUsername(sessionId);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Usuari no trobat"));

        if(!user.getRole().equals("COMPANY")){
            throw new ApiException(ErrorCode.INVALID_REQUEST, "Només empreses");
        }

        Company company = companyRepository.findByUser(user)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Empresa no trobada"));

        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_REQUEST, "Oferta no trobada"));

        if(!offer.getCompany().getId().equals(company.getId())){
            throw new ApiException(ErrorCode.INVALID_REQUEST, "No pots veure aquesta oferta");
        }

        return offer.getApplicants().stream()
                .map(profileMapper::toStudentDto)
                .toList();
    }






    @Transactional
    public void deleteOffer(String sessionId, Long offerId){

        if(!SessionManager.isValid(sessionId)){
            throw new ApiException(ErrorCode.INVALID_SESSION, "Sessió no vàlida");
        }

        String username = SessionManager.getUsername(sessionId);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Usuari no trobat"));

        if(!user.getRole().equals("COMPANY")){
            throw new ApiException(ErrorCode.INVALID_REQUEST, "Només empreses");
        }

        Company company = companyRepository.findByUser(user)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Empresa no trobada"));

        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_REQUEST, "Oferta no trobada"));

        if(!offer.getCompany().getId().equals(company.getId())){
            throw new ApiException(ErrorCode.INVALID_REQUEST, "No pots eliminar aquesta oferta");
        }

        offerRepository.delete(offer);

        int current = company.getActiveOffers() == null ? 0 : company.getActiveOffers();
        company.setActiveOffers(Math.max(0, current - 1));
    }





    @Transactional
    public void updateOffer(String sessionId, Long offerId, UpdateOfferDto dto){

        if(!SessionManager.isValid(sessionId)){
            throw new ApiException(ErrorCode.INVALID_SESSION, "Sessió no vàlida");
        }

        String username = SessionManager.getUsername(sessionId);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Usuari no trobat"));

        if(!user.getRole().equals("COMPANY")){
            throw new ApiException(ErrorCode.INVALID_REQUEST, "Només empreses");
        }

        Company company = companyRepository.findByUser(user)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Empresa no trobada"));

        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_REQUEST, "Oferta no trobada"));

        if(!offer.getCompany().getId().equals(company.getId())){
            throw new ApiException(ErrorCode.INVALID_REQUEST, "No pots modificar aquesta oferta");
        }

        // updates
        if(dto.getTitle() != null) offer.setTitle(dto.getTitle());
        if(dto.getDescription() != null) offer.setDescription(dto.getDescription());
        if(dto.getLocation() != null) offer.setLocation(dto.getLocation());
        if(dto.getSalary() != null) offer.setSalary(dto.getSalary());
        if(dto.getState() != null) offer.setState(OfferState.valueOf(dto.getState()));
        if(dto.getCycle() != null) offer.setCycle(Cycle.valueOf(dto.getCycle()));

        if(dto.getModality() != null){
            offer.setModality(Modality.valueOf(dto.getModality()));
        }

        if(dto.getContractType() != null){
            offer.setContractType(ContractType.valueOf(dto.getContractType()));
        }

        // skills (reset complet)
        if(dto.getSkills() != null){
            offer.getRequiredSkills().clear();

            List<OfferSkill> newSkills = dto.getSkills().stream().map(s -> {
                OfferSkill skill = new OfferSkill();
                skill.setSkill(s);
                skill.setOffer(offer);
                return skill;
            }).toList();

            offer.getRequiredSkills().addAll(newSkills);
        }

        offerRepository.save(offer);
    }


    public OfferResponseDto getOfferById(Long id) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_REQUEST, "Oferta no trobada"));

        return offerMapper.toDto(offer);
    }
}