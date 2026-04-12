package com.impulsfp.server.model;

import jakarta.persistence.*;

/**
 * Entitat que representa les habilitats associades als estudiants; cada instància d'aquesta classe representa una habilitat específica que un estudiant posseeix.
 *
 * @author Jonathan Giraldo Giraldo
 */
@Entity
@Table(name = "student_skills")
public class StudentSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String skill;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    // GETTERS & SETTERS

    public Long getId() { return id; }

    public String getSkill() { return skill; }
    public void setSkill(String skill) { this.skill = skill; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
}