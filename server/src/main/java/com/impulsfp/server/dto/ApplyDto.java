package com.impulsfp.server.dto;

/**
 * DTO per representar les dades necessàries per a que un estudiant pugui aplicar a una oferta de feina, incloent l'identificador de l'oferta i la resposta a la prova tècnica associada.
 *
 * @author Jonathan Giraldo Giraldo
 */
public class ApplyDto {

    private Long offerId;
    private String answer;

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}