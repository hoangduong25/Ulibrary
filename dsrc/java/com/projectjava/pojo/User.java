/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectjava.pojo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Hokichi
 */
@Entity
@Table(name = "user")
public class User implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String password;
    private String email;
    private String phone_number;
    private String address;
    private LocalDate added_date;
    private String card_id;
    private String userhash;
    private boolean verified;
    
    @OneToMany(mappedBy = "user")
    private Set<Favorite> fav;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the confirmPassword
     */
   

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the phone_number
     */
    public String getPhone_number() {
        return phone_number;
    }

    /**
     * @param phone_number the phone_number to set
     */
    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the added_date
     */
    public LocalDate getAdded_date() {
        return added_date;
    }

    /**
     * @param added_date the added_date to set
     */
    public void setAdded_date(LocalDate added_date) {
        this.added_date = added_date;
    }

    /**
     * @return the card_id
     */
    public String getCard_id() {
        return card_id;
    }

    /**
     * @param card_id the card_id to set
     */
    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    /**
     * @return the fav
     */
    public Set<Favorite> getFav() {
        return fav;
    }

    /**
     * @param fav the fav to set
     */
    public void setFav(Set<Favorite> fav) {
        this.fav = fav;
    }

    /**
     * @return the userhash
     */
    public String getUserhash() {
        return userhash;
    }

    /**
     * @param userhash the userhash to set
     */
    public void setUserhash(String userhash) {
        this.userhash = userhash;
    }

    /**
     * @return the verified
     */
    public boolean isVerified() {
        return verified;
    }

    /**
     * @param verified the verified to set
     */
    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
