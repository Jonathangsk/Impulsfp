package com.impulsfp.server.service;

import com.impulsfp.server.dto.CreateOfferDto;
import com.impulsfp.server.exception.ApiException;
import com.impulsfp.server.mapper.ApplicationMapper;
import com.impulsfp.server.mapper.OfferMapper;
import com.impulsfp.server.mapper.ProfileMapper;
import com.impulsfp.server.model.Company;
import com.impulsfp.server.model.User;
import com.impulsfp.server.repository.*;
import com.impulsfp.server.session.SessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test per al servei OfferService
 *
 * @author Jonathan Giraldo Giraldo
 */
public class OfferServiceTest {


    private OfferService offerService;

    private OfferRepository offerRepository;
    private CompanyRepository companyRepository;
    private UserRepository userRepository;
    private OfferMapper offerMapper;
    private ApplicationRepository applicationRepository;
    private StudentRepository studentRepository;
    private CompanyTechnologyRepository companyTechnologyRepository;
    private StudentSkillRepository studentSkillRepository;
    private ProfileMapper profileMapper;
    private ApplicationMapper applicationMapper;


    /**
     * Configura els mocks i el servei abans de cada test; crea mocks per a les dependències del servei i inicialitza el servei amb aquests mocks.
     */
    @BeforeEach
    void setUp() {
        offerRepository = Mockito.mock(OfferRepository.class);
        companyRepository = Mockito.mock(CompanyRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        offerMapper = Mockito.mock(OfferMapper.class);
        applicationRepository = Mockito.mock(ApplicationRepository.class);
        studentRepository = Mockito.mock(StudentRepository.class);
        companyTechnologyRepository = Mockito.mock(CompanyTechnologyRepository.class);
        studentSkillRepository = Mockito.mock(StudentSkillRepository.class);

        offerService = new OfferService(
                offerRepository,
                userRepository,
                companyRepository,
                studentRepository,
                offerMapper,
                profileMapper,
                applicationRepository,
                applicationMapper
        );
    }

    /**
     * Test que verifica que es pot crear una oferta de feina correctament
     */
    @Test
    void createOfferOk() {
        String sessionId = SessionManager.createSession("company");

        User user = new User();
        user.setUsername("company");
        user.setRole("COMPANY");

        Company company = new Company();
        company.setUser(user);

        Mockito.when(userRepository.findByUsername("company"))
                .thenReturn(Optional.of(user));

        Mockito.when(companyRepository.findByUser(user))
                .thenReturn(Optional.of(company));

        CreateOfferDto dto = new CreateOfferDto();
        dto.setTitle("Test");
        dto.setLocation("BCN");
        dto.setDescription("Test description");
        dto.setModality("ONSITE");
        dto.setContractType("FCT");
        dto.setSalary(30000.0);
        dto.setSkills(java.util.Arrays.asList("Java", "Spring"));
        dto.setCycle("DAM");


        assertDoesNotThrow(() ->
                offerService.createOffer(sessionId, dto)
        );
    }

    /**
     * Test que verifica que no es pot crear una oferta de feina amb una sessió no vàlida; Intenta crear una oferta amb un sessionId invàlid i espera que es llanci una ApiException.
     */
    @Test
    void createOfferInvalidSession() {
        CreateOfferDto dto = new CreateOfferDto();

        assertThrows(ApiException.class, () ->
                offerService.createOffer("invalid", dto)
        );
    }

    /**
     * Test que verifica que no es pot crear una oferta de feina si l'usuari associat a la sessió no és una empresa
     */
    @Test
    void createOfferUserNotCompany() {
        String sessionId = SessionManager.createSession("student");

        User user = new User();
        user.setUsername("student");
        user.setRole("STUDENT");

        Mockito.when(userRepository.findByUsername("student"))
                .thenReturn(Optional.of(user));

        CreateOfferDto dto = new CreateOfferDto();

        assertThrows(ApiException.class, () ->
                offerService.createOffer(sessionId, dto)
        );
    }

    /**
     * Test que verifica que no es pot crear una oferta de feina si l'empresa associada a l'usuari no es troba a la base de dades
     */
    @Test
    void createOfferCompanyNotFound() {
        String sessionId = SessionManager.createSession("company");

        User user = new User();
        user.setUsername("company");
        user.setRole("COMPANY");

        Mockito.when(userRepository.findByUsername("company"))
                .thenReturn(Optional.of(user));

        Mockito.when(companyRepository.findByUser(user))
                .thenReturn(Optional.empty());

        CreateOfferDto dto = new CreateOfferDto();

        assertThrows(ApiException.class, () ->
                offerService.createOffer(sessionId, dto)
        );
    }

    /**
     * Test que verifica que no es pot crear una oferta de feina amb dades no vàlides; intenta crear una oferta amb un títol buit i espera que es llanci una ApiException.
     */
    @Test
    void createOfferInvalidRequest() {
        String sessionId = SessionManager.createSession("company");

        User user = new User();
        user.setUsername("company");
        user.setRole("COMPANY");

        Company company = new Company();
        company.setUser(user);

        Mockito.when(userRepository.findByUsername("company"))
                .thenReturn(Optional.of(user));

        Mockito.when(companyRepository.findByUser(user))
                .thenReturn(Optional.of(company));

        CreateOfferDto dto = new CreateOfferDto();
        dto.setTitle(""); // Títol no pot estar buit

        assertThrows(ApiException.class, () ->
                offerService.createOffer(sessionId, dto)
        );
    }


    /**
     * Test que verifica que no es pot crear una oferta de feina amb una modalitat no vàlida
     */
    @Test
    void createOfferInvalidModality() {
        String sessionId = SessionManager.createSession("company");

        User user = new User();
        user.setUsername("company");
        user.setRole("COMPANY");

        Company company = new Company();
        company.setUser(user);

        Mockito.when(userRepository.findByUsername("company"))
                .thenReturn(Optional.of(user));

        Mockito.when(companyRepository.findByUser(user))
                .thenReturn(Optional.of(company));

        CreateOfferDto dto = new CreateOfferDto();
        dto.setTitle("Test");
        dto.setLocation("BCN");
        dto.setDescription("Test description");
        dto.setModality("INVALID_MODALITY"); // Modalitat no vàlida
        dto.setContractType("FCT");
        dto.setSalary(30000.0);
        dto.setSkills(java.util.Arrays.asList("Java", "Spring"));
        dto.setCycle("DAM");

        assertThrows(ApiException.class, () ->
                offerService.createOffer(sessionId, dto)
        );
    }

    /**
     * Test que verifica que no es pot crear una oferta de feina amb un tipus de contracte no vàlid
     */
    @Test
    void createOfferInvalidContractType() {
        String sessionId = SessionManager.createSession("company");

        User user = new User();
        user.setUsername("company");
        user.setRole("COMPANY");

        Company company = new Company();
        company.setUser(user);

        Mockito.when(userRepository.findByUsername("company"))
                .thenReturn(Optional.of(user));

        Mockito.when(companyRepository.findByUser(user))
                .thenReturn(Optional.of(company));

        CreateOfferDto dto = new CreateOfferDto();
        dto.setTitle("Test");
        dto.setLocation("BCN");
        dto.setDescription("Test description");
        dto.setModality("ONSITE");
        dto.setContractType("INVALID_CONTRACT"); // Tipus de contracte no vàlid
        dto.setSalary(30000.0);
        dto.setSkills(java.util.Arrays.asList("Java", "Spring"));
        dto.setCycle("DAM");

        assertThrows(ApiException.class, () ->
                offerService.createOffer(sessionId, dto)
        );
    }

    /**
     *Test que verifica que no es pot crear una oferta de feina amb un cicle no vàlid
     */
    @Test
    void createOfferInvalidCycle() {
        String sessionId = SessionManager.createSession("company");

        User user = new User();
        user.setUsername("company");
        user.setRole("COMPANY");

        Company company = new Company();
        company.setUser(user);

        Mockito.when(userRepository.findByUsername("company"))
                .thenReturn(Optional.of(user));

        Mockito.when(companyRepository.findByUser(user))
                .thenReturn(Optional.of(company));

        CreateOfferDto dto = new CreateOfferDto();
        dto.setTitle("Test");
        dto.setLocation("BCN");
        dto.setDescription("Test description");
        dto.setModality("ONSITE");
        dto.setContractType("FCT");
        dto.setSalary(30000.0);
        dto.setSkills(java.util.Arrays.asList("Java", "Spring"));
        dto.setCycle("INVALID_CYCLE"); // Cicle no vàlid

        assertThrows(ApiException.class, () ->
                offerService.createOffer(sessionId, dto)
        );
    }
}
