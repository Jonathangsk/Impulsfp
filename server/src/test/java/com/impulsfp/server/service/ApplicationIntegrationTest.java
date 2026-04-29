package com.impulsfp.server.service;

import com.impulsfp.server.dto.CreateOfferDto;
import com.impulsfp.server.dto.RegisterCompanyRequestDto;
import com.impulsfp.server.dto.RegisterStudentRequestDto;
import com.impulsfp.server.model.Offer;
import com.impulsfp.server.repository.*;
import com.impulsfp.server.session.SessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Test d'integració per al procés complet d'aplicació a una oferta: registre d'empresa i estudiant, creació d'oferta i aplicació a l'oferta.
 */
@SpringBootTest
public class ApplicationIntegrationTest {



    @Autowired
    private AuthService authService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Neteja la base de dades abans de cada prova per garantir que les proves no interfereixin entre si i que cada prova comenci amb un estat net i previsible. Esborra totes les dades de les taules d'aplicacions, ofertes, estudiants, empreses i usuaris.
     */
    @BeforeEach
    void cleanDatabase() {
        applicationRepository.deleteAll();
        offerRepository.deleteAll();
        studentRepository.deleteAll();
        companyRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void applyFlowComplete() {
        RegisterStudentRequestDto studentReq = new RegisterStudentRequestDto();
        studentReq.setUsername("studentTest2");
        studentReq.setPassword("Password1");
        studentReq.setEmail("student2@test.com");

        authService.registerStudent(studentReq);

        RegisterCompanyRequestDto companyReq = new RegisterCompanyRequestDto();
        companyReq.setUsername("companyTest2");
        companyReq.setPassword("Password1");
        companyReq.setEmail("company2@test.com");

        authService.registerCompany(companyReq);

        String companySession = SessionManager.createSession("companyTest2");

        CreateOfferDto offerDto = new CreateOfferDto();
        offerDto.setTitle("Oferta test");
        offerDto.setLocation("Barcelona");
        offerDto.setDescription("Descripció de l'oferta de prova");
        offerDto.setModality("HYBRID");
        offerDto.setContractType("FP_DUAL");
        offerDto.setCycle("DAM");
        offerDto.setSkills(List.of("Java", "Spring Boot"));
        offerDto.setSalary(25000.0);

        offerService.createOffer(companySession, offerDto);

        List<Offer> offers = offerRepository.findAll();
        assertFalse(offers.isEmpty());

        Long offerId = offers.get(0).getId();

        String studentSession = SessionManager.createSession("studentTest2");

        applicationService.apply(studentSession, offerId);

        assertEquals(1, applicationRepository.findAll().size());
    }

}
