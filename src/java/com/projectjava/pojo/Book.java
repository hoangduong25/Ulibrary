/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectjava.pojo;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Hokichi
 */
@Entity
@Table(name = "book")
public class Book implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private int publishYear;
    private String description;
    private String cover;
    private int copyCount;
    private String onlineCopy;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
                name = "author_book",
                joinColumns = {
                    @JoinColumn(name = "book_id")
                },
            inverseJoinColumns = {
                    @JoinColumn(name = "author_id")
                })
    private Set<Author> author;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "genre_id")
    private Genre genre;
    
    @ManyToOne
    @JoinColumn(name = "lang_id")
    private Language lang;
    
    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher pub;
    
    @OneToMany(mappedBy = "book")
    private Set<Favorite> fav;
    
    @ManyToOne
    @JoinColumn(name = "shelf_id")
    private Shelf shelf;
    
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
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the publishYear
     */
    public int getPublishYear() {
        return publishYear;
    }

    /**
     * @param publishYear the publishYear to set
     */
    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the cover
     */
    public String getCover() {
        return cover;
    }

    /**
     * @param cover the cover to set
     */
    public void setCover(String cover) {
        this.cover = cover;
    }

    /**
     * @return the author
     */
    public Set<Author> getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(Set<Author> author) {
        this.author = author;
    }

    /**
     * @return the genre
     */
    public Genre getGenre() {
        return genre;
    }

    /**
     * @param genre the genre to set
     */
    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    /**
     * @return the lang
     */
    public Language getLang() {
        return lang;
    }

    /**
     * @param lang the lang to set
     */
    public void setLang(Language lang) {
        this.lang = lang;
    }

    /**
     * @return the pub
     */
    public Publisher getPub() {
        return pub;
    }

    /**
     * @param pub the pub to set
     */
    public void setPub(Publisher pub) {
        this.pub = pub;
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
     * @return the shelf
     */
    public Shelf getShelf() {
        return shelf;
    }

    /**
     * @param shelf the shelf to set
     */
    public void setShelf(Shelf shelf) {
        this.shelf = shelf;
    }

    /**
     * @return the copyCount
     */
    public int getCopyCount() {
        return copyCount;
    }

    /**
     * @param copyCount the copyCount to set
     */
    public void setCopyCount(int copyCount) {
        this.copyCount = copyCount;
    }

    /**
     * @return the onlineCopy
     */
    public String getOnlineCopy() {
        return onlineCopy;
    }

    /**
     * @param onlineCopy the onlineCopy to set
     */
    public void setOnlineCopy(String onlineCopy) {
        this.onlineCopy = onlineCopy;
    }
}
