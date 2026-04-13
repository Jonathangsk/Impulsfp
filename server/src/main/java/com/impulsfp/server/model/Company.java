package com.impulsfp.server.model;

import jakarta.persistence.*;

import java.util.List;

/**
 * Entitat que representa una empresa dins del sistema; conté informació bàsica de l'empresa i està associada a un usuari.
 *
 * @author Jonathan Giraldo Giraldo
 */
@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String address;
    private String vatNumber;

    private String website;
    private String phone;

    private String niche;
    private Integer activeOffers;

    //relació one-to-many amb CompanyTechnology, on una empresa pot tenir moltes tecnologies, però cada tecnologia está associada a una sola empresa
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompanyTechnology> technologies;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // GETTERS & SETTERS

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getVatNumber() { return vatNumber; }
    public void setVatNumber(String vatNumber) { this.vatNumber = vatNumber; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getNiche() { return niche; }
    public void setNiche(String niche) { this.niche = niche; }

    public Integer getActiveOffers() { return activeOffers; }
    public void setActiveOffers(Integer activeOffers) { this.activeOffers = activeOffers; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}