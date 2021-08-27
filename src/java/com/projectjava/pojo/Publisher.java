/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectjava.pojo;

import java.io.Serializable;
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
@Table(name = "publisher")
public class Publisher implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    
    @Override
    public String toString(){
        return String.valueOf(this.id);
    }
    
    @Override
    public boolean equals(Object obj){
        Publisher p = (Publisher) obj;
        return this.id == p.id;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + this.id;
        return hash;
    }
    
    @OneToMany(mappedBy = "pub")
    private Set<Book> book;

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
