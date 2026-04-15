package com.impulsfp.server.repository;

import com.impulsfp.server.model.Company;
import com.impulsfp.server.model.CompanyTechnology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository per accedir a les dades de les tecnologies associades a les empreses; proporciona mètodes per gestionar les tecnologies associades a les empreses.
 *
 * @author Jonathan Giraldo Giraldo
 */
@Repository
public interface CompanyTechnologyRepository extends JpaRepository<CompanyTechnology, Long> {
    void deleteByCompany(Company company);
}