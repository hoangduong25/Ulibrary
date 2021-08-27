/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectjava.bean;

import com.projectjava.pojo.Book;
import com.projectjava.pojo.Publisher;
import com.projectjava.service.PubService;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Hokichi
 */
@ManagedBean
@Named(value = "pubBean")
@SessionScoped
public class PubBean implements Serializable{

    /**
     * @return the pubServ
     */
    public static PubService getPubServ() {
        return pubServ;
    }

    /**
     * @param aPubServ the pubServ to set
     */
    public static void setPubServ(PubService aPubServ) {
        pubServ = aPubServ;
    }

    private int id;
    private String name;
    private Set<Book> book;
    
    private static PubService pubServ = new PubService();
    /**
     * Creates a new instance of PubBean
     */
    public PubBean() {
    }
    
    public String addPub(){
        Publisher p = new Publisher();
        p.setName(this.getName());

        if(pubServ.addOrSavePub(p))
            return "pubs?faces-redirect=true";
        return "pubs";
        }

    public List<Publisher> getPubs(){
        return getPubServ().getPubs();
    }

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
     * @return the book
     */
    public Set<Book> getBook() {
        return book;
    }

    /**
     * @param book the book to set
     */
    public void setBook(Set<Book> book) {
        this.book = book;
    }
    
}
