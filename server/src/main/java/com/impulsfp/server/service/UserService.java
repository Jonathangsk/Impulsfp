package com.impulsfp.server.service;

import com.impulsfp.server.exception.ApiException;
import com.impulsfp.server.exception.ErrorCode;
import com.impulsfp.server.model.User;
import com.impulsfp.server.repository.UserRepository;
import com.impulsfp.server.session.SessionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Servei per gestionar les operacions relacionades amb els usuaris, com la eliminació de comptes, modificació de dades personals, etc.
 *
 * @author Jonathan Giraldo Giraldo
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Elimina el compte de l'usuari associat a la sessió actual. Verifica que la sessió és vàlida i la contrassenya abans de procedir amb l'eliminació.
     * @param sessionId
     */
    @Transactional
    public void deleteAccount(String sessionId, String password){

        if(!SessionManager.isValid(sessionId)){
            throw new ApiException(ErrorCode.INVALID_SESSION, "Sessió no vàlida");
        }

        String username = SessionManager.getUsername(sessionId);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Usuari no trobat"));

        //verifica que la contrassenya proporcionada coincideix amb la contrassenya emmagatzemada a la base de dades
        if(password == null || !passwordEncoder.matches(password, user.getPassword())){
            throw new ApiException(ErrorCode.INVALID_PASSWORD, "La contrasenya no és correcta");
        }

        userRepository.delete(user);

        SessionManager.removeSession(sessionId);
    }
}