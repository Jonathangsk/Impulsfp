package com.impulsfp.server.repository;

import com.impulsfp.server.model.Offer;
import com.impulsfp.server.model.Company;
import com.impulsfp.server.enums.OfferState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {

    List<Offer> findByCompany(Company company);

    List<Offer> findByState(OfferState state);
}