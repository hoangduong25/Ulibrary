/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectjava.bean;

import com.projectjava.pojo.Book;
import com.projectjava.pojo.Language;
import com.projectjava.service.LangService;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Hokichi
 */
@ManagedBean
@Named(value = "langBean")
@SessionScoped
public class LangBean implements Serializable {
    
    private int id;
    private String name;
    private Set<Book> book;
    
    private static LangService langServ = new LangService();
    /**
     * Creates a new instance of LangBean
     */
    public LangBean() {
    }
    
    public List<Language> getLangs(){
        return langServ.getLangs();
    }
    
    public String addLang(){
        Language l = new Language();
        l.setName(this.getName());

        if(langServ.addOrSaveLang(l))
            return "langs?faces-redirect=true";
        return "langs";
        }

        public List<Language> getPubs(){
            return langServ.getLangs();
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
