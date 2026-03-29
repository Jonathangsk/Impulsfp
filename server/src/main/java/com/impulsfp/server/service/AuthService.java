package com.impulsfp.server.service;

import com.impulsfp.server.dto.LoginResponseDto;
import com.impulsfp.server.model.User;
import com.impulsfp.server.repository.UserRepository;
import com.impulsfp.server.session.SessionManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Clase Service que implementa la lògica d'autenticació dels usuaris. Verifica les credencials i gestiona les sessions.
 *
 * @author Jonathan Giraldo Giraldo
 */
@Service
public class AuthService {

    private final UserRepository userRepository; //repositori per accedir a les dades de l'usuari (base de dades)

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Mètode amb la lògica iniciar sessió; verifica les credencials de l'usuari i, si són correctes, crea una sessió i retorna un objecte DTO de resposta amb les dades de sessió (sessionId i userType)
     * Si les credencials no són correctes, retorna null.
     * @param username nom d'usuari
     * @param password contrasenya
     * @return
     */
    public LoginResponseDto login(String username, String password) {

        Optional<User> userOpt = userRepository.findByUsername(username);

        if(userOpt.isPresent()) {
            User user = userOpt.get();

            if(user.getPassword().equals(password)) {
                String sessionId = SessionManager.createSession(username);
                return new LoginResponseDto(sessionId, user.getRole());
            }
        }

        return null;
    }
}