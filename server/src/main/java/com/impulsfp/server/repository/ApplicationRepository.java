package com.impulsfp.server.repository;

import com.impulsfp.server.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * Repository per accedir a les dades de les aplicacions; Proporciona mètodes per buscar aplicacions per estudiant, oferta, verificar si una aplicació existeix i comptar el nombre d'aplicacions per oferta.
 *
 * @author Jonathan Giraldo Giraldo
 */
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByStudent(Student student);

    List<Application> findByOffer(Offer offer);

    boolean existsByStudentAndOffer(Student student, Offer offer);

    long countByOffer(Offer offer);
}