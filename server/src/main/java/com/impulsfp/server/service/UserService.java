package com.impulsfp.server.service;

import com.impulsfp.server.exception.ApiException;
import com.impulsfp.server.exception.ErrorCode;
import com.impulsfp.server.model.User;
import com.impulsfp.server.repository.UserRepository;
import com.impulsfp.server.session.SessionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servei per gestionar les operacions relacionades amb els usuaris, com la eliminació de comptes, modificació de dades personals, etc.
 *
 * @author Jonathan Giraldo Giraldo
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Elimina el compte de l'usuari associat a la sessió actual. Verifica que la sessió és vàlida abans de procedir amb l'eliminació.
     * @param sessionId
     */
    @Transactional
    public void deleteAccount(String sessionId){

        if(!SessionManager.isValid(sessionId)){
            throw new ApiException(ErrorCode.INVALID_SESSION, "Sessió no vàlida");
        }

        User user = userRepository.findByUsername(SessionManager.getUsername(sessionId)) //busca l'usuari associat a la sessió actual utilitzant el nom d'usuari obtingut de SessionManager; si no es troba, llança una excepció
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Usuari no trobat"));

        userRepository.delete(user);

        SessionManager.removeSession(sessionId);
    }
}