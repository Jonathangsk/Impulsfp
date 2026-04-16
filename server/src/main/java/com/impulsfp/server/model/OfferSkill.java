package com.impulsfp.server.model;

import jakarta.persistence.*;

@Entity
@Table(name = "offer_skills")
public class OfferSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String skill;

    @ManyToOne
    @JoinColumn(name = "offer_id")
    private Offer offer;

    public Long getId() { return id; }

    public String getSkill() { return skill; }
    public void setSkill(String skill) { this.skill = skill; }

    public Offer getOffer() { return offer; }
    public void setOffer(Offer offer) { this.offer = offer; }
}