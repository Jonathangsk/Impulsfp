package com.impulsfp.server.service;

import com.impulsfp.server.exception.ApiException;
import com.impulsfp.server.mapper.ApplicationMapper;
import com.impulsfp.server.model.*;
import com.impulsfp.server.repository.*;
import com.impulsfp.server.session.SessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test de unitat per ApplicationService, que verifica el comportament dels mètodes d'aplicació i consulta d'aplicacions.
 *
 * @author Jonathan Giraldo Giraldo
 */

public class ApplicationServiceTest {


    private ApplicationService service;

    private ApplicationRepository applicationRepository;
    private OfferRepository offerRepository;
    private StudentRepository studentRepository;
    private ApplicationMapper applicationMapper;
    private UserRepository userRepository;
    private CompanyRepository companyRepository;


    /**
     * Configura els mocks i inicialitza el servei abans de cada test; crea mocks per les dependències del servei i els injecta en una nova instància d'ApplicationService.
     */
    @BeforeEach
    void setUp() {
        applicationRepository = Mockito.mock(ApplicationRepository.class);
        offerRepository = Mockito.mock(OfferRepository.class);
        studentRepository = Mockito.mock(StudentRepository.class);
        applicationMapper = Mockito.mock(ApplicationMapper.class);
        userRepository = Mockito.mock(UserRepository.class);

        service = new ApplicationService(
                applicationRepository,
                userRepository,
                studentRepository,
                offerRepository,
                companyRepository,
                applicationMapper
        );
    }

    /**
     * Test que verifica que un estudiant pot aplicar correctament a una oferta
     */
    @Test
    void applyOk() {
        String sessionId = SessionManager.createSession("user");

        User user = new User();
        user.setUsername("user");
        user.setRole("STUDENT");

        Student student = new Student();
        student.setUser(user);

        Offer offer = new Offer();

        Mockito.when(userRepository.findByUsername("user"))
                .thenReturn(Optional.of(user));

        Mockito.when(studentRepository.findByUser(user))
                .thenReturn(Optional.of(student));

        Mockito.when(offerRepository.findById(1L))
                .thenReturn(Optional.of(offer));

        assertDoesNotThrow(() ->
                service.apply(sessionId, 1L)
        );
    }

    /**
     * Test que verifica que el servei llança una excepció quan es proporciona un sessionId invàlid per aplicar a una oferta
     */
    @Test
    void applyInvalidSession() {
        assertThrows(ApiException.class, () ->
                service.apply("invalid", 1L)
        );
    }


    /**
     * Test que verifica que el servei llança una excepció quan un usuari que no és estudiant intenta aplicar a una oferta
     */
    @Test
    void applyNotStudent() {
        String sessionId = SessionManager.createSession("user");

        User user = new User();
        user.setUsername("user");
        user.setRole("COMPANY");

        Mockito.when(userRepository.findByUsername("user"))
                .thenReturn(Optional.of(user));

        assertThrows(ApiException.class, () ->
                service.apply(sessionId, 1L)
        );
    }

    /**
     * Test que verifica que el servei llança una excepció quan es proporciona un sessionId vàlid però l'oferta a la qual s'intenta aplicar no existeix
     */
    @Test
    void applyOfferNotFound() {
        String sessionId = SessionManager.createSession("user");

        User user = new User();
        user.setUsername("user");
        user.setRole("STUDENT");

        Student student = new Student();
        student.setUser(user);

        Mockito.when(userRepository.findByUsername("user"))
                .thenReturn(Optional.of(user));

        Mockito.when(studentRepository.findByUser(user))
                .thenReturn(Optional.of(student));

        Mockito.when(offerRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ApiException.class, () ->
                service.apply(sessionId, 1L)
        );

    }

    /**
     * Test que verifica que el servei llança una excepció quan un estudiant intenta aplicar a una oferta a la qual ja ha aplicat abans
     */
    @Test
    void applyAlreadyApplied() {
        String sessionId = SessionManager.createSession("user");

        User user = new User();
        user.setUsername("user");
        user.setRole("STUDENT");

        Student student = new Student();
        student.setUser(user);

        Offer offer = new Offer();

        Mockito.when(userRepository.findByUsername("user"))
                .thenReturn(Optional.of(user));

        Mockito.when(studentRepository.findByUser(user))
                .thenReturn(Optional.of(student));

        Mockito.when(offerRepository.findById(1L))
                .thenReturn(Optional.of(offer));

        Mockito.when(applicationRepository.existsByStudentAndOffer(student, offer))
                .thenReturn(true);

        assertThrows(ApiException.class, () ->
                service.apply(sessionId, 1L)
        );
    }

    /**
     * Test que verifica que un estudiant pot obtenir correctament les seves aplicacions
     */
    @Test
    void getMyApplicationsOk() {
        String sessionId = SessionManager.createSession("user");

        User user = new User();
        user.setUsername("user");
        user.setRole("STUDENT");

        Student student = new Student();
        student.setUser(user);

        Mockito.when(userRepository.findByUsername("user"))
                .thenReturn(Optional.of(user));

        Mockito.when(studentRepository.findByUser(user))
                .thenReturn(Optional.of(student));

        assertDoesNotThrow(() ->
                service.getMyApplications(sessionId)
        );
    }

}
