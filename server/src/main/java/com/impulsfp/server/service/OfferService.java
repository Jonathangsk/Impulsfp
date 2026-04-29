package com.impulsfp.server.service;

import com.impulsfp.server.dto.CreateOfferDto;
import com.impulsfp.server.dto.OfferResponseDto;
import com.impulsfp.server.dto.StudentProfileDto;
import com.impulsfp.server.dto.UpdateOfferDto;
import com.impulsfp.server.enums.*;
import com.impulsfp.server.exception.*;
import com.impulsfp.server.mapper.ApplicationMapper;
import com.impulsfp.server.mapper.OfferMapper;
import com.impulsfp.server.mapper.ProfileMapper;
import com.impulsfp.server.model.*;
import com.impulsfp.server.repository.*;
import com.impulsfp.server.session.SessionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servei que gestiona les ofertes de feina, incloent la creació, actualització, eliminació i consulta d'ofertes, així com la gestió de les aplicacions dels estudiants a les ofertes.
 *
 * @author Jonathan Giraldo Giraldo
 */

@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final StudentRepository studentRepository;
    private final OfferMapper offerMapper;
    private final ProfileMapper profileMapper;
    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;

    public OfferService(OfferRepository offerRepository,
                        UserRepository userRepository,
                        CompanyRepository companyRepository,
                        StudentRepository studentRepository,
                        OfferMapper offerMapper, ProfileMapper profileMapper, ApplicationRepository applicationRepository, ApplicationMapper applicationMapper) {

        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.studentRepository = studentRepository;
        this.offerMapper = offerMapper;
        this.profileMapper = profileMapper;
        this.applicationRepository = applicationRepository;
        this.applicationMapper = applicationMapper;
    }


    /**
     * Crea una nova oferta de feina associada a l'empresa de l'usuari autenticat. Valida que la sessió sigui vàlida, que l'usuari sigui una empresa i que els camps obligatoris del DTO estiguin correctament omplerts abans de guardar l'oferta a la base de dades.
     * @param sessionId La ID de la sessió de l'usuari autenticat que està intentant crear l'oferta.
     * @param dto Un objecte CreateOfferDto que conté les dades necessàries per crear una nova oferta
     */
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
        try {
            Modality modality = Modality.valueOf(dto.getModality());
            // seguir amb la creació de l'oferta
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "Modality inválida: ");
        }

        try {
            ContractType contractType = ContractType.valueOf(dto.getContractType());
            // seguir amb la creació de la oferta
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "ContractType inválido: ");
        }
        offer.setSalary(dto.getSalary());
        offer.setCreationDate(LocalDateTime.now());
        offer.setState(OfferState.OPEN);
        offer.setCompany(company);


        try {
            offer.setCycle(Cycle.valueOf(dto.getCycle()));
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "Cycle inválido: ");
        }

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


    /**
     * Permet a un estudiant aplicar a una oferta de feina específica. Valida que la sessió sigui vàlida, que l'usuari sigui un estudiant, que l'oferta existeixi i que l'estudiant no hagi aplicat prèviament a la mateixa oferta abans de crear una nova aplicació amb estat "PENDING" i guardar-la a la base de dades.
     * @param sessionId La ID de la sessió de l'usuari autenticat que està intentant aplicar a l'oferta.
     * @param offerId La ID de l'oferta a la qual l'estudiant vol aplicar.
     */
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


        if(applicationRepository.existsByStudentAndOffer(student, offer)){
            throw new ApiException(ErrorCode.INVALID_REQUEST, "Ja estàs inscrit a aquesta oferta");
        }

        // crear applicacion
        Application app = new Application();
        app.setStudent(student);
        app.setOffer(offer);
        app.setStatus(ApplicationStatus.PENDING);
        app.setAppliedAt(LocalDateTime.now());

        applicationRepository.save(app);

    }


    /**
     * Retorna una llista de les ofertes creades per l'empresa associada a l'usuari autenticat. Valida que la sessió sigui vàlida i que l'usuari sigui una empresa abans de recuperar les ofertes associades a l'empresa i retornar-les com a llista d'entitats Offer.
     * @param sessionId La ID de la sessió de l'usuari autenticat que està intentant recuperar les seves ofertes.
     * @return Una llista d'entitats Offer que representa les ofertes creades per l'empresa associada a l'usuari autenticat.
     */
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


    /**
     * Retorna una llista de totes les ofertes de feina obertes disponibles a la plataforma, convertides a DTOs de resposta. Recupera totes les ofertes amb estat "OPEN" de la base de dades, les converteix a DTOs utilitzant el mapper i les retorna com a llista.
     * @return Una llista de DTOs de resposta que representen les ofertes de feina obertes disponibles a la plataforma.
     */
    public List<OfferResponseDto> getAllOffers(){

        return offerRepository.findByState(OfferState.OPEN)
                .stream()
                .map(offerMapper::toDto)
                .toList();
    }


    /**
     * Retorna una llista de les ofertes de feina obertes que coincideixen amb la ubicació especificada, convertides a DTOs de resposta.
     * @param location La ubicació que es vol filtrar les ofertes de feina; el mètode busca ofertes que continguin aquesta ubicació (ignorant majúscules/minúscules) i que estiguin obertes.
     * @return Una llista de DTOs de resposta que representen les ofertes de feina obertes que coincideixen amb la ubicación especificada.
     */
    public List<OfferResponseDto> getOffersByLocation(String location){

        return offerRepository
                .findByLocationContainingIgnoreCaseAndState(location, OfferState.OPEN)
                .stream()
                .map(offerMapper::toDto)
                .toList();
    }


    /**
     * Retorna una llista de les ofertes de feina obertes que coincideixen amb la modalitat especificada, convertides a DTOs de resposta.
     * @param modality La modalitat que es vol filtrar les ofertes de feina; el mètode busca ofertes que tinguin aquesta modalitat i que estiguin obertes.
     * @return Una llista de DTOs de resposta que representen les ofertes de feina obertes que coincideixen amb la modalitat especificada.
     */
    public List<OfferResponseDto> getOffersByModality(String modality){

        return offerRepository
                .findByModalityAndState(Modality.valueOf(modality), OfferState.OPEN)
                .stream()
                .map(offerMapper::toDto)
                .toList();
    }


    /**
     * Retorna una llista dels perfils dels estudiants que han aplicat a una oferta de feina específica
     * @param sessionId L'id de la sessió de l'usuari autenticat que està intentant recuperar els perfils dels estudiants que han aplicat a la seva oferta
     * @param offerId l'id de l'oferta de feina de la qual es volen recuperar els perfils dels estudiants que han aplicat
     * @return Una lista de DTOs de perfil de estudiante que representan los perfiles de los estudiantes que han aplicado a la oferta especificada.
     */
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

        return applicationRepository.findByOffer(offer)
                .stream()
                .map(applicationMapper::toCompanyDto)
                .collect(Collectors.toList());
    }


    /**
     * Elimina una oferta de feina específica associada a l'empresa de l'usuari autenticat
     * @param sessionId La ID de la sessió de l'usuari autenticat que està intentant eliminar l'oferta.
     * @param offerId La ID de l'oferta que es vol eliminar; el mètode valida que aquesta oferta existeixi i que estigui associada a l'empresa de l'usuari abans de procedir a eliminar-la.
     */
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


    /**
     * Actualitza una oferta de feina específica associada a l'empresa de l'usuari autenticat
     * @param sessionId La ID de la sessió de l'usuari autenticat que està intentant actualizar la oferta.
     * @param offerId L'Id de l'oferta que es vol actualizar; el mètode valida que aquesta oferta existeixi i que estigui associada a l'empresa de l'usuari abans de procedir a aplicar les actualitzacions especificades
     * @param dto Un objecte UpdateOfferDto que conté les dades que es volen actualizar de l'oferta
     */
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


    /**
     * Retorna els detalls d'una oferta de feina específica, convertits a un DTO de resposta. Valida que l'oferta existeixi abans de convertir-la a un DTO utilitzant el mapper i retornar-lo.
     * @param id La ID de l'oferta de feina de la qual es volen recuperar los detalles; el mètode valida que esta oferta exista antes de proceder a convertirla a un DTO y retornarla.
     * @return Un DTO que representa els detalls de l'oferta de feina especificada
     */
    public OfferResponseDto getOfferById(Long id) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_REQUEST, "Oferta no trobada"));

        return offerMapper.toDto(offer);
    }
}