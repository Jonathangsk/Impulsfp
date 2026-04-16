package com.impulsfp.server.repository;

import com.impulsfp.server.model.Company;
import com.impulsfp.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository per accedir a les dades de les empreses; Proporciona mètode per buscar empreses per correu electrònic.
 *
 * @author Jonathan Giraldo Giraldo
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByEmail(String email);
    Optional<Company> findByUser(User user);
}