package com.impulsfp.server.repository;

import com.impulsfp.server.model.Offer;
import com.impulsfp.server.model.OfferTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OfferTestRepository extends JpaRepository<OfferTest, Long> {

    Optional<OfferTest> findByOffer(Offer offer);
}