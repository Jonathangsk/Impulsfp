package com.impulsfp.server.model;

import com.impulsfp.server.enums.TestType;
import jakarta.persistence.*;

/**
 * Entitat que representa les proves tècniques associades a les ofertes de feina; conté informació sobre el tipus de prova, la pregunta, el codi associat i les opcions de resposta.
 *
 * @author Jonathan Giraldo Giraldo
 */

@Entity
@Table(name = "offer_tests")
public class OfferTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "offer_id")
    private Offer offer;

    @Enumerated(EnumType.STRING)
    private TestType type;

    private String question;

    @Column(name = "code_snippet")
    private String codeSnippet;

    private String options;

    @Column(name = "correct_answer")
    private String correctAnswer;

    // getters & setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public TestType getType() {
        return type;
    }

    public void setType(TestType type) {
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCodeSnippet() {
        return codeSnippet;
    }

    public void setCodeSnippet(String codeSnippet) {
        this.codeSnippet = codeSnippet;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}