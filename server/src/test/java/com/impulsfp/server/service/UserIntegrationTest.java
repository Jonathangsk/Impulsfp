package com.impulsfp.server.service;

import com.impulsfp.server.dto.RegisterStudentRequestDto;
import com.impulsfp.server.model.User;
import com.impulsfp.server.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test d'integració per al registre d'usuaris a AuthService.
 */
@SpringBootTest
public class UserIntegrationTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    /**
     * Prova el registre d'un nou usuari i verifica que s'ha guardat correctament a la base de dades.
     */
    @Test
    void registerUserAndCheckDB() {
        RegisterStudentRequestDto req = new RegisterStudentRequestDto();
        req.setUsername("user");
        req.setPassword("Password1");
        req.setEmail("provatest@hotmail.com");

        authService.registerStudent(req);

        User user = userRepository.findByUsername("user").orElse(null);

        assertNotNull(user);
        assertEquals("user", user.getUsername());
    }

    /**
     * Prova el registre d'un usuari amb un username ja existent i verifica que es maneja l'error correctament.
     */
    @Test
    void registerUserWithExistingUsername() {
        RegisterStudentRequestDto req = new RegisterStudentRequestDto();
        req.setUsername("user");
        req.setPassword("Password1");
        req.setEmail("test1@hotmail.com");

        authService.registerStudent(req);


        RegisterStudentRequestDto req2 = new RegisterStudentRequestDto();
        req2.setUsername("user");
        req2.setPassword("Password1");
        req2.setEmail("test2@hotmail.com");

        assertThrows(Exception.class, () -> {
            authService.registerStudent(req2);
        });

    }

    /**
     * Prova el registre d'un usuari amb un email ja existent i verifica que es maneja l'error correctament.
     *
     */
    @Test
    void registerUserWithExistingEmail() {
        RegisterStudentRequestDto req = new RegisterStudentRequestDto();
        req.setUsername("user1");
        req.setPassword("Password1");
        req.setEmail("test@hotmail.com");

        authService.registerStudent(req);

        RegisterStudentRequestDto req2 = new RegisterStudentRequestDto();
        req2.setUsername("user2");
        req2.setPassword("Password1");
        req2.setEmail("test@hotmail.com");

        assertThrows(Exception.class, () -> {
            authService.registerStudent(req2);
        });

    }

    /**
     * Prova el registre d'un usuari amb camps obligatoris faltants i verifica que es maneja l'error correctament.
     */
    @Test
    void registerUserWithMissingFields() {
        RegisterStudentRequestDto req = new RegisterStudentRequestDto();
        req.setUsername(null); // campo obligatorio
        req.setPassword("Password1");
        req.setEmail("test@hotmail.com");

        assertThrows(Exception.class, () -> {
            authService.registerStudent(req);
        });

    }

    /**
     * Prova el registre d'un usuari amb una contrasenya que no compleix los requisitos de seguridad y verifica que se maneja el error correctamente.
     */
    @Test
    void registerUserWithInvalidPassword() {
        RegisterStudentRequestDto req = new RegisterStudentRequestDto();
        req.setUsername("user");
        req.setPassword("123"); // demasiado corto (si validas)
        req.setEmail("test@hotmail.com");

        assertThrows(Exception.class, () -> {
            authService.registerStudent(req);
        });

    }

    /**
     * Prova el registre d'un usuari amb un email no válido y verifica que se maneja el error correctamente.
     */
    @Test
    void registerUserWithInvalidEmail() {
        RegisterStudentRequestDto req = new RegisterStudentRequestDto();
        req.setUsername("user");
        req.setPassword("Password1");
        req.setEmail("email-invalido"); // sin @

        assertThrows(Exception.class, () -> {
            authService.registerStudent(req);
        });

    }

}
