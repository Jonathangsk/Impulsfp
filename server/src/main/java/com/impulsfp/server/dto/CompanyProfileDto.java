package com.impulsfp.server.dto;

import java.util.List;

/**
 * DTO per representar les dades del perfil d'empresa enviades al client
 *
 * @author Jonathan Giraldo Giraldo
 */
public class CompanyProfileDto {

    private long id;
    private String username;
    private String name;
    private String email;
    private String address;
    private String vatNumber;

    private String website;
    private String phone;

    private String niche;
    private Integer activeOffers;

    private List<String> technologies;

    // getters & setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

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

    public List<String> getTechnologies() { return technologies; }
    public void setTechnologies(List<String> technologies) { this.technologies = technologies; }
}