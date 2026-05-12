package com.impulsfp.server.repository;

import com.impulsfp.server.model.Offer;
import com.impulsfp.server.model.OfferTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositori per a l'entitat OfferTest, que proporciona mètodes per accedir i gestionar les dades relacionades amb els tests associats a les ofertes de feina.
 *
 * @author Jonathan Giraldo Giraldo
 */

public interface OfferTestRepository extends JpaRepository<OfferTest, Long> {

    Optional<OfferTest> findByOffer(Offer offer);
}