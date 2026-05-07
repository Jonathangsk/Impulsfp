package com.impulsfp.server.mapper;

import com.impulsfp.server.dto.ApplicationDto;
import com.impulsfp.server.dto.StudentProfileDto;
import com.impulsfp.server.dto.StudentProfileForCompanyDto;
import com.impulsfp.server.model.Application;
import com.impulsfp.server.model.Student;
import org.springframework.stereotype.Component;

/**
 * Mapper per convertir entre les entitats d'aplicació i els DTOs associats; proporciona mètodes per transformar les dades de les aplicacions en formats adequats per a la presentació i la comunicació amb altres capes de l'aplicació.
 *
 * @author Jonathan Giraldo Giraldo
 *
 */
@Component
public class ApplicationMapper {

    private final ProfileMapper profileMapper;

    public ApplicationMapper(ProfileMapper profileMapper) {
        this.profileMapper = profileMapper;
    }

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

    public StudentProfileForCompanyDto toCompanyDto(Application app){

        Student student = app.getStudent();

        //utilitzem el profileMapper per obtenir un StudentProfileDto amb les dades comunes del perfil d'estudiant
        StudentProfileDto base = profileMapper.toStudentDto(student);

        StudentProfileForCompanyDto dto = new StudentProfileForCompanyDto();

        //copiem els camps comuns del perfil d'estudiant
        dto.setName(base.getName());
        dto.setSurname(base.getSurname());
        dto.setEmail(base.getEmail());
        dto.setCity(base.getCity());
        dto.setCycle(base.getCycle());
        dto.setLanguages(base.getLanguages());
        dto.setSkills(base.getSkills());
        dto.setBio(base.getBio());
        dto.setPortfolio(base.getPortfolio());
        dto.setPhoneNumber(base.getPhoneNumber());
        dto.setPreferredLocation(base.getPreferredLocation());
        dto.setPreferredRoles(base.getPreferredRoles());
        dto.setAvailability(base.getAvailability());
        dto.setExperienceLevel(base.getExperienceLevel());
        dto.setUsername(base.getUsername());

        //afegim els camps específics de l'aplicació
        dto.setApplicationId(app.getId());
        dto.setStatus(app.getStatus().name());

        if(app.getTestResult() != null){
            dto.setTestResult(app.getTestResult().name());
        }

        return dto;
    }
}