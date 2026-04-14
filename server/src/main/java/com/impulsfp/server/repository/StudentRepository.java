package com.impulsfp.server.repository;

import com.impulsfp.server.model.Student;
import com.impulsfp.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository per accedir a les dades dels estudiants; Proporciona mètode per buscar estudiants per correu electrònic.
 *
 * @author Jonathan Giraldo Giraldo
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);
    Optional<Student> findByUser(User user);
}