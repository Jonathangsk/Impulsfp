package com.impulsfp.server.session;

import com.impulsfp.server.exception.ApiException;
import com.impulsfp.server.exception.ErrorCode;
import com.impulsfp.server.model.User;
import com.impulsfp.server.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Classe per gestionar les sessions d'usuari; proporciona mètodes per crear, validar i eliminar sessions.
 */
public class SessionManager {

    private static Map<String, String> sessions = new HashMap<>(); //map que associa sessionId amb username


    /**
     * Mètode per crear una nova sessió; genera un sessionId únic i l'associa amb el nom d'usuari, retornant el sessionId.
     * @param username
     * @return sessionId generat i associat al nom d'usuari proporcionat, que es pot utilitzar per identificar la sessió de l'usuari en les peticions posteriors
     */
    public static String createSession(String username) {
        String sessionId = UUID.randomUUID().toString(); //genera un identificador de sessió únic utilitzant UUID
        sessions.put(sessionId, username);
        return sessionId;
    }

    /**
     * Mètode per obtenir el nom d'usuari associat a un sessionId; retorna el nom d'usuari si el sessionId és vàlid o null si no ho és.
     * @param sessionId
     * @return nom d'usuari associat al sessionId proporcionat, o null si el sessionId no és vàlid
     */
    public static String getUsername(String sessionId) {
        return sessions.get(sessionId);
    }


    /**
     * Mètode per validar una sessió; comprova si el sessionId existeix al map de sessions, retornant true si és vàlid o false si no ho és.
     * @param sessionId
     * @return true si el sessionId proporcionat és vàlid (existeix al map de sessions), o false si no ho és (no existeix al map de sessions)
     */
    public static boolean isValid(String sessionId) {
        return sessions.containsKey(sessionId);
    }



    /**
     * Mètode per eliminar una sessió; elimina el sessionId del map de sessions, invalidant la sessió corresponent.
     * @param sessionId per eliminar, proporcionat com a paràmetre del mètode
     */
    public static void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }


    /**
     * mètode per requerir que una sessió sigui vàlida i que l'usuari associat a aquesta sessió tingui el rol d'administrador
     * @param sessionId de la sessió que es vol validar i verificar el rol d'administrador, proporcionat com a paràmetre del mètode
     * @param userRepository per accedir a les dades dels usuaris i verificar el rol de l'usuari associat al sessionId proporcionat, proporcionat com a paràmetre del mètode
     */
    public static void requireAdmin(String sessionId,
                                    UserRepository userRepository){

        if(!isValid(sessionId)){
            throw new ApiException(ErrorCode.INVALID_SESSION, "Sessió no vàlida");
        }

        String username = getUsername(sessionId);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Usuari no trobat"));

        if(!user.getRole().equals("ADMIN")){
            throw new ApiException(ErrorCode.INVALID_REQUEST, "Només admins");
        }
    }
}