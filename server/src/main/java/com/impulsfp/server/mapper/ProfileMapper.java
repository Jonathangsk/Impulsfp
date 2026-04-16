package com.impulsfp.server.mapper;

import com.impulsfp.server.dto.CompanyProfileDto;
import com.impulsfp.server.dto.StudentProfileDto;
import com.impulsfp.server.model.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper per convertir les entitats de perfil d'estudiant i empresa en els seus respectius DTOs; Proporciona mètodes per transformar les dades de les entitats en un format adequat per a la transferència de dades.
 *
 * @author Jonathan Giraldo Giraldo
 */
@Component
public class ProfileMapper {

    /**
     * Mètode per convertir una entitat Student en un StudentProfileDto; transforma les dades de l'entitat Student en un format adequat per a la transferència de dades.
     * @param student L'entitat Student que es vol convertir en un StudentProfileDto.
     * @return Un StudentProfileDto amb les dades transformades de l'entitat Student.
     */
    public StudentProfileDto toStudentDto(Student student){

        StudentProfileDto dto = new StudentProfileDto();

        dto.setUsername(student.getUser().getUsername());
        dto.setName(student.getName());
        dto.setSurname(student.getSurname());
        dto.setEmail(student.getEmail());
        dto.setPhoneNumber(student.getPhoneNumber());

        dto.setCity(student.getCity());
        dto.setBio(student.getBio());
        dto.setCycle(student.getCycle());
        dto.setExperienceLevel(student.getExperienceLevel());

        dto.setPreferredLocation(student.getPreferredLocation());
        dto.setAvailability(student.getAvailability());
        dto.setPortfolio(student.getPortfolio());

        //convertir les cadenes separades per comes en llistes de cadenes fent us de java streams
        dto.setLanguages(
                student.getLanguages() != null
                        ? List.of(student.getLanguages().split(","))
                        : List.of()
        );

        //convertir les cadenes separades per comes en llistes de cadenes fent us de java streams
        dto.setPreferredRoles(
                student.getPreferredRoles() != null
                        ? List.of(student.getPreferredRoles().split(","))
                        : List.of()
        );

        //convertir la llista de StudentSkill a una llista de cadenes amb els noms de les habilitats fent ús de Java Streams
        dto.setSkills(
                student.getSkills().stream()
                        .map(StudentSkill::getSkill)
                        .toList()
        );

        return dto;
    }

    /**
     * Mètode per convertir una entitat Company en un CompanyProfileDto; transforma les dades de l'entitat Company en un format adequat per a la transferència de dades.
     * @param company L'entitat Company que es vol convertir en un CompanyProfileDto.
     * @return Un CompanyProfileDto amb les dades transformades de l'entitat Company.
     */
    public CompanyProfileDto toCompanyDto(Company company){

        CompanyProfileDto dto = new CompanyProfileDto();

        dto.setUsername(company.getUser().getUsername());
        dto.setName(company.getName());
        dto.setEmail(company.getEmail());
        dto.setAddress(company.getAddress());
        dto.setVatNumber(company.getVatNumber());

        dto.setWebsite(company.getWebsite());
        dto.setPhone(company.getPhone());

        dto.setNiche(company.getNiche());
        dto.setActiveOffers(company.getActiveOffers());

        //convertir la llista de CompanyTechnology a una llista de cadenes amb els noms de les tecnologies fent ús de Java Streams
        dto.setTechnologies(
                company.getTechnologies() != null
                        ? company.getTechnologies().stream()
                          .map(CompanyTechnology::getTechnology)
                          .toList()
                        : List.of()
        );

        return dto;
    }
}