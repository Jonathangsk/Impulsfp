package com.impulsfp.server.repository;

import com.impulsfp.server.model.Student;
import com.impulsfp.server.model.StudentSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository per accedir a les dades de les habilitats dels estudiants; proporciona mètodes per gestionar les habilitats associades als estudiants.
 *
 * @author Jonathan Giraldo Giraldo
 */
@Repository
public interface StudentSkillRepository extends JpaRepository<StudentSkill, Long> {
    void deleteByStudent(Student student);
}