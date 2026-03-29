package com.impulsfp.server.repository;

import com.impulsfp.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Repository per accedir a les dades dels usuaris; Proporciona mètode per buscar usuaris per nom d'usuari.
 *
 * @author Jonathan Giraldo Giraldo
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

}