package com.impulsfp.server.repository;

import com.impulsfp.server.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByStudent(Student student);

    List<Application> findByOffer(Offer offer);

    boolean existsByStudentAndOffer(Student student, Offer offer);

    long countByOffer(Offer offer);
}