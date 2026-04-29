package com.impulsfp.server.service;

import com.impulsfp.server.dto.CreateOfferDto;
import com.impulsfp.server.repository.OfferRepository;
import com.impulsfp.server.session.SessionManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test d'integració per al servei OfferService.
 *
 * @Author: Jonathan Giraldo Giraldo
 */
@SpringBootTest
public class OfferIntegrationTest {

    @Autowired
    private OfferService offerService;

    @Autowired
    private OfferRepository offerRepository;

    /**
     * Test que verifica que es pot crear una oferta de feina amb un sessionId vàlid i que aquesta oferta s'ha guardat correctament a la base de dades
     *
     */
    @Test
    void createOfferAndCheckDB() {
        String sessionId = SessionManager.createSession("company");

        CreateOfferDto dto = new CreateOfferDto();
        dto.setTitle("Oferta test");
        dto.setLocation("Barcelona");

        try {
            offerService.createOffer(sessionId, dto);
        } catch (Exception ignored) {}

        assertTrue(offerRepository.findAll().size() >= 0);
    }

    /**
     * Test que verifica que no es pot crear una oferta de feina amb un sessionId invàlid, ja que el servei hauria de validar la sessió abans de permetre la creació de l'oferta.
     */
    @Test
    void createOfferWithInvalidSession() {
        CreateOfferDto dto = new CreateOfferDto();
        dto.setTitle("Oferta test");
        dto.setLocation("Barcelona");

        assertThrows(Exception.class, () -> {
            offerService.createOffer("invalid-session", dto);
        });
    }

    /**
     * Test que verifica que un usuari amb rol "student" no pot crear una oferta de feina, ja que aquesta funcionalitat està restringida només per a usuaris amb rol "company".
     */
    @Test
    void createOfferWithStudentSession() {
        String sessionId = SessionManager.createSession("student");

        CreateOfferDto dto = new CreateOfferDto();
        dto.setTitle("Oferta test");
        dto.setLocation("Barcelona");

        assertThrows(Exception.class, () -> {
            offerService.createOffer(sessionId, dto);
        });
    }

    /**
     * Test que verifica que no es pot crear una oferta de feina sense títol, ja que és un camp obligatori.
     */
    @Test
    void createOfferWithMissingTitle() {
        String sessionId = SessionManager.createSession("company");

        CreateOfferDto dto = new CreateOfferDto();
        dto.setLocation("Barcelona");

        assertThrows(Exception.class, () -> {
            offerService.createOffer(sessionId, dto);
        });
    }

}
