package com.impulsfp.server.session;

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
     * @return
     */
    public static String createSession(String username) {
        String sessionId = UUID.randomUUID().toString(); //genera un identificador de sessió únic utilitzant UUID
        sessions.put(sessionId, username);
        return sessionId;
    }

    /**
     * Mètode per obtenir el nom d'usuari associat a un sessionId; retorna el nom d'usuari si el sessionId és vàlid o null si no ho és.
     * @param sessionId
     * @return
     */
    public static String getUsername(String sessionId) {
        return sessions.get(sessionId);
    }


    /**
     * Mètode per validar una sessió; comprova si el sessionId existeix al map de sessions, retornant true si és vàlid o false si no ho és.
     * @param sessionId
     * @return
     */
    public static boolean isValid(String sessionId) {
        return sessions.containsKey(sessionId);
    }



    /**
     * Mètode per eliminar una sessió; elimina el sessionId del map de sessions, invalidant la sessió corresponent.
     * @param sessionId
     */
    public static void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }
}