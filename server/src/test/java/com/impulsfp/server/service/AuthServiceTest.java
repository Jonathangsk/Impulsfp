package com.impulsfp.server.service;

import com.impulsfp.server.dto.LoginResponseDto;
import com.impulsfp.server.model.User;
import com.impulsfp.server.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**

 * Classe de proves per a AuthService.
 * Es comprova el correcte funcionament del login amb diferents casos.
 * Aquesta prova és una prova unitària que utilitza Mockito per simular el comportament del UserRepository i
 * verificar la lògica d'autenticació sense necessitat de connectar-se a una base de dades real
 *
 * @author Jonathan
 */
public class AuthServiceTest {

    private AuthService authService;
    private UserRepository userRepository;

    /**
     * Inicialitza el mock del repositori abans de cada test.
     */
    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        authService = new AuthService(userRepository);
    }

    /**
     * Prova el login correcte amb usuari existent i contrasenya vàlida.
     */
    @Test
    void loginCorrecte() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("1234");
        user.setRole("STUDENT");

        Mockito.when(userRepository.findByUsername("user"))
                .thenReturn(Optional.of(user));

        LoginResponseDto response = authService.login("user", "1234");

        assertNotNull(response);
        assertNotNull(response.getSessionId());
        assertEquals("STUDENT", response.getUserType());
    }

    /**
     * Prova el login incorrecte amb contrasenya errònia.
     */
    @Test
    void loginIncorrectePassword() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("1234");

        Mockito.when(userRepository.findByUsername("user"))
                .thenReturn(Optional.of(user));

        LoginResponseDto response = authService.login("user", "wrong");

        assertNull(response);
    }

    /**
     * Prova el login amb usuari inexistent.
     */
    @Test
    void loginUsuariInexistent() {
        Mockito.when(userRepository.findByUsername("user"))
                .thenReturn(Optional.empty());

        LoginResponseDto response = authService.login("user", "1234");

        assertNull(response);
    }
}
