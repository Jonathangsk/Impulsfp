package com.impulsfp.server.repository;
import com.impulsfp.server.enums.Modality;

import com.impulsfp.server.model.Offer;
import com.impulsfp.server.model.Company;
import com.impulsfp.server.enums.OfferState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * Repository per accedir a les dades de les ofertes; Proporciona mètodes per buscar ofertes per empresa, estat, ubicació i modalitat.
 *
 * @author Jonathan Giraldo Giraldo
 */
public interface OfferRepository extends JpaRepository<Offer, Long> {



    List<Offer> findByCompany(Company company);

    List<Offer> findByState(OfferState state);

    List<Offer> findByLocationContainingIgnoreCaseAndState(String location, OfferState state);

    List<Offer> findByModalityAndState(Modality modality, OfferState state);

}