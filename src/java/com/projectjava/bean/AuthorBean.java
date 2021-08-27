/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectjava.bean;

import com.projectjava.pojo.Author;
import com.projectjava.pojo.Book;
import com.projectjava.service.AuthorService;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 *
 * @author Hokichi
 */
@ManagedBean
@Named(value = "authorBean")
@SessionScoped
public class AuthorBean implements Serializable{
    
    private int id;
    private String name;
    private Set<Book> book;
    private static AuthorService authorServ = new AuthorService();
    private boolean showTable = false;

    /**
     * Creates a new instance of AuthorBean
     */
    public AuthorBean() {
    }
    
    public List<Author> getAuthors(){
        return getAuthorServ().getAuthors();
    }
    
    public String addAuthor(){
        Author a = new Author();
        a.setName(this.getName());
        
        if(authorServ.addOrSaveAuthor(a))
            return "authors?faces-redirect=true";
        return "authors";
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

    /**
     * @return the authorServ
     */
    public static AuthorService getAuthorServ() {
        return authorServ;
    }

    /**
     * @param aAuthorServ the authorServ to set
     */
    public static void setAuthorServ(AuthorService aAuthorServ) {
        authorServ = aAuthorServ;
    }

    /**
     * @return the showTable
     */
    public boolean isShowTable() {
        return showTable;
    }
    
    public String enableTable(){
        showTable = true;
        return "";
    }

    /**
     * @param showTable the showTable to set
     */
    public void setShowTable(boolean showTable) {
        this.showTable = showTable;
    }
    
}
