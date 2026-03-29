package com.impulsfp.server.service;

import com.impulsfp.server.dto.LoginResponseDto;
import com.impulsfp.server.model.User;
import com.impulsfp.server.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test d'integració per AuthService.
 *
 * Aquest test comprova el funcionament real del servei utilitzant
 * la base de dades, sense mocks
 * Es verifica que es poden inserir dades a la BD, que el servei les consulta correctament i
 * que el login funciona amb dades reals
 *
 * @author Jonathan
 */
@SpringBootTest
public class AuthServiceIntegrationTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Prepara la base de dades abans de cada prova;s'insereix un usuri de prova
     */
    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("user");
        user.setPassword("1234");
        user.setRole("STUDENT");

        userRepository.save(user);
    }

    /**
     * Prova el login correcte utilitzant dades reals de la base de dades.
     * Es comprova que el servei accedeix correctament a la BD,
     * valida l'usuari i retorna una resposta amb sessionId i userType
     */
    @Test
    void loginCorrecteAmbBD() {
        LoginResponseDto response = authService.login("user", "1234");

        assertNotNull(response);
        assertNotNull(response.getSessionId());
        assertEquals("STUDENT", response.getUserType());
    }

    /**
     * Prova el login incorrecte amb contrasenya errònia.
     *
     * Es comprova que el servei no valida l'usuari
     * i retorna null.
     */
    @Test
    void loginIncorrecteAmbBD() {
        LoginResponseDto response = authService.login("user", "wrong");

        assertNull(response);
    }
}
