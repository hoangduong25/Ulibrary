/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectjava.bean;

import com.projectjava.pojo.Book;
import com.projectjava.pojo.Genre;
import com.projectjava.service.BookService;
import com.projectjava.service.GenreService;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 *
 * @author Hokichi
 */
@ManagedBean
@Named(value = "genreBean")
@SessionScoped
public class GenreBean implements Serializable {
    private int id;
    private String name;
    
    private static GenreService genreServ = new GenreService();
    private static BookService bookServ = new BookService();
    /**
     * Creates a new instance of GenreBean
     */
    public GenreBean() {
    }
    
    public List<Genre> getGenres(){
        return getGenreServ().getGenres();
    }
    
    public String addGenre(){
        Genre g = new Genre();
        g.setName(this.getName());
        
        if(getGenreServ().addOrSaveGenre(g))
            return "genres?faces-redirect=true";
        return "genres";
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
     * @return the genreServ
     */
    public static GenreService getGenreServ() {
        return genreServ;
    }

    /**
     * @param aGenreServ the genreServ to set
     */
    public static void setGenreServ(GenreService aGenreServ) {
        genreServ = aGenreServ;
    }

    /**
     * @return the bookServ
     */
    public static BookService getBookServ() {
        return bookServ;
    }

    /**
     * @param aBookServ the bookServ to set
     */
    public static void setBookServ(BookService aBookServ) {
        bookServ = aBookServ;
    }
    
}
