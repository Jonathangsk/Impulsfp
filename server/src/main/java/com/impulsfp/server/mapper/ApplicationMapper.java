package com.impulsfp.server.mapper;

import com.impulsfp.server.dto.ApplicationDto;
import com.impulsfp.server.dto.StudentProfileDto;
import com.impulsfp.server.dto.StudentProfileForCompanyDto;
import com.impulsfp.server.model.Application;
import com.impulsfp.server.model.Student;
import org.springframework.stereotype.Component;

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

        // reutilizamos mapper existente
        StudentProfileDto base = profileMapper.toStudentDto(student);

        StudentProfileForCompanyDto dto = new StudentProfileForCompanyDto();

        // copiamos todos los campos automáticamente
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

        // 🔥 añadimos lo nuevo
        dto.setApplicationId(app.getId());
        dto.setStatus(app.getStatus().name());

        return dto;
    }
}