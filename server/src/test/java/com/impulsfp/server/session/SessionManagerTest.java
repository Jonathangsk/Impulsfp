package com.impulsfp.server.session;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**

 * Classe de proves per a SessionManager.
 * Es comprova la creació, validació i eliminació de sessions.
 *
 * @author Jonathan
 */
public class SessionManagerTest {

    /**

     * Prova la creació d'una sessió vàlida.
     */
    @Test
    void crearSessio() {
        String sessionId = SessionManager.createSession("user");

        assertNotNull(sessionId);
        assertTrue(SessionManager.isValid(sessionId));
    }

    /**

     * Prova l'eliminació d'una sessió.
     */
    @Test
    void eliminarSessio() {
        String sessionId = SessionManager.createSession("user");

        SessionManager.removeSession(sessionId);

        assertFalse(SessionManager.isValid(sessionId));
    }

    /**

     * Prova la validació d'una sessió inexistent.
     */
    @Test
    void sessioInvalida() {
        assertFalse(SessionManager.isValid("fake-session-id"));
    }


    /**
     * Prova que una sessió deixa de ser vàlida després de fer logout.
     */
    @Test
    void sessioInvalidaDespresLogout() {
        String sessionId = SessionManager.createSession("user");

        SessionManager.removeSession(sessionId);

        assertFalse(SessionManager.isValid(sessionId));
    }
}
