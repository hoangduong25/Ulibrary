/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectjava.bean;

import com.projectjava.pojo.Author;
import com.projectjava.pojo.Book;
import com.projectjava.pojo.Favorite;
import com.projectjava.pojo.Genre;
import com.projectjava.pojo.Language;
import com.projectjava.pojo.Publisher;
import com.projectjava.pojo.Shelf;
import com.projectjava.pojo.User;
import com.projectjava.service.BookService;
import com.projectjava.service.BookingService;
import com.projectjava.service.FavoriteService;
import com.projectjava.service.UserService;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

/**
 *
 * @author Hokichi
 */
@ManagedBean
@Named(value = "bookBean")
@SessionScoped
public class BookBean implements Serializable {
    
    private int id;
    private String title;
    private int publishYear;
    private String description;
    private String cover;
    private Set<Author> author;
    private Genre genre;
    private Language lang;
    private Publisher pub;
    private Author auth;
    private Part img;
    private Favorite fav;
    private Shelf shelf;
    private int copyCount;
    private String bookkw, authorkw, kw;
    private Genre searchGenre;
    private Language searchLang;
    private boolean bookedOnline;
    private String onlineCopy;
    
    private String txt1;
    
    private static final BookService bookServ = new BookService();
    private static final FavoriteService favServ = new FavoriteService();
    private static final UserService userServ = new UserService();
    private static final BookingService bookingServ = new BookingService();
    
    /**
     * Creates a new instance of BookBean
     */
    
    public BookBean() {
        if(!FacesContext.getCurrentInstance().isPostback()){
            String bookID = FacesContext.getCurrentInstance()
                                        .getExternalContext()
                                        .getRequestParameterMap()
                                        .get("bookid");
            if(bookID != null && !bookID.isEmpty()){
                Book b = bookServ.getBookByID(Integer.parseInt(bookID));
                this.id = b.getId();
                this.author = b.getAuthor();
                this.title = b.getTitle();
                this.publishYear = b.getPublishYear();
                this.pub = b.getPub();
                this.cover = b.getCover();
                this.lang = b.getLang();
                this.genre = b.getGenre();
                this.description = b.getDescription();   
                this.copyCount = b.getCopyCount();
                this.shelf = b.getShelf();
                if(b.getOnlineCopy() != null && !b.getOnlineCopy().isEmpty())
                    this.onlineCopy = b.getOnlineCopy();
            }
        }
    }
    
    public String search(){
        String redirect = "";
        
        if(this.getKw() != null && !this.getKw().isEmpty())
            redirect += "&searchQuery=" + this.getKw();
        return "library?faces-redirect=true" + redirect;
    }
    
    public List<Book> getBooks(){
        String authorID = FacesContext.getCurrentInstance()
                                        .getExternalContext()
                                        .getRequestParameterMap()
                                        .get("authorid");
        
        String genreID = FacesContext.getCurrentInstance()
                                        .getExternalContext()
                                        .getRequestParameterMap()
                                        .get("genreid");
                
        String langID = FacesContext.getCurrentInstance()
                                        .getExternalContext()
                                        .getRequestParameterMap()
                                        .get("langid");
        
        String globalKey = FacesContext.getCurrentInstance()
                                .getExternalContext()
                                .getRequestParameterMap()
                                .get("searchQuery");
        
        if(!(authorID == null && genreID == null && langID == null)){
            if(genreID == null && langID == null){
                return bookServ.getBookByAuthorID(Integer.parseInt(authorID));
            }
            else if(authorID == null && langID == null){
                return bookServ.getBookByGenreID(Integer.parseInt(genreID));
            }
            else if(authorID == null && genreID == null){
                return bookServ.getBookByLangID(Integer.parseInt(langID));
            }
        }
        else {
            if(globalKey != null && !globalKey.isEmpty())
                return bookServ.getSearchResults(globalKey);
        }
       return bookServ.getBooks();
    }
    
    public List<Book> getBooks(Genre genre){
        return bookServ.getBookByGenreID(genre.getId());
    }
    
    public List<Book> getBooks(Author author){
        return bookServ.getBookByGenreID(author.getId());
    }
    
    public String addBook(){
        String bookID = FacesContext.getCurrentInstance()
                                        .getExternalContext()
                                        .getRequestParameterMap()
                                        .get("bookid");
        Book b;
        if(this.getId() > 0){
            b = bookServ.getBookByID(this.getId());
        }
        else{
            b = new Book();
        }
        b.setAuthor(this.getAuthor());
        b.setGenre(this.getGenre());
        b.setId(this.getId());
        b.setDescription(this.getDescription());
        b.setLang(this.getLang());
        b.setPublishYear(this.getPublishYear());
        b.setPub(this.getPub());
        b.setTitle(this.getTitle());
        
        try {
            if(getImg() != null){
                this.uploadFile();
                b.setCover(this.getImg().getSubmittedFileName());
            }
            
            if(bookServ.addOrSaveBook(b))
                return "books_admin?faces-redirect=true";
        } catch (IOException ex) {
            Logger.getLogger(BookBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    
    private void uploadFile() throws IOException{
        String path = FacesContext.getCurrentInstance()
                      .getExternalContext().getRealPath("/resources/img/book")
                      + "/" + this.getImg().getSubmittedFileName();  
        try(InputStream in = this.getImg().getInputStream();
            FileOutputStream out = new FileOutputStream(path)){
            byte[] b = new byte[1024];
            int byteRead;
            while((byteRead = in.read(b)) != -1)
                out.write(b, 0, byteRead);
        }
    }
    
    public String addPhysCopy(){
            Book b = bookServ.getBookByID(this.id);
            b.setCopyCount(this.getCopyCount());
            b.setShelf(this.getShelf());
            if(bookServ.addOrSaveBook(b))
                return "books_admin?faces-redirect=true";
        return "";
    }
    
    public String addFavorite(int id){
        Favorite f = new Favorite();
        f.setBook(bookServ.getBookByID(id));
        f.setUser((User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user"));
        
        if(!favServ.getFavs().contains(f))
            favServ.addFavorite(f);
        return "library?faces-redirect=true";
    }
    
    public String deleteBook(Book b) throws Exception{
        if(bookServ.deleteBook(b.getId()))
            return "successful";
        
        throw new Exception("!!!");
    }
    
    public boolean isFav() {
        User u = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
        if(u != null)
            return favServ.getFavorite(this.id, u.getId());
        else
            return false;
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
     * @return the img
     */
    public Part getImg() {
        return img;
    }

    /**
     * @param img the img to set
     */
    public void setImg(Part img) {
        this.img = img;
    }

    /**
     * @return the auth
     */
    public Author getAuth() {
        return auth;
    }

    /**
     * @param auth the auth to set
     */
    public void setAuth(Author auth) {
        this.auth = auth;
    }
    
    /**
     * @return the txt1
     */
    public String getTxt1() {
        return txt1;
    }

    /**
     * @param txt1 the txt1 to set
     */
    public void setTxt1(String txt1) {
        this.txt1 = txt1;
    }

    /**
     * @return the fav
     */
    public Favorite getFav() {
        return fav;
    }

    /**
     * @param fav the fav to set
     */
    public void setFav(Favorite fav) {
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
     * @return the kw
     */
    public String getKw() {
        return kw;
    }

    /**
     * @param kw the kw to set
     */
    public void setKw(String kw) {
        this.kw = kw;
    }

    /**
     * @return the authorkw
     */
    public String getAuthorkw() {
        return authorkw;
    }

    /**
     * @param authorkw the authorkw to set
     */
    public void setAuthorkw(String authorkw) {
        this.authorkw = authorkw;
    }

    /**
     * @return the bookkw
     */
    public String getBookkw() {
        return bookkw;
    }

    /**
     * @param bookkw the bookkw to set
     */
    public void setBookkw(String bookkw) {
        this.bookkw = bookkw;
    }

    /**
     * @return the searchGenre
     */
    public Genre getSearchGenre() {
        return searchGenre;
    }

    /**
     * @param searchGenre the searchGenre to set
     */
    public void setSearchGenre(Genre searchGenre) {
        this.searchGenre = searchGenre;
    }

    /**
     * @return the searchLang
     */
    public Language getSearchLang() {
        return searchLang;
    }

    /**
     * @param searchLang the searchLang to set
     */
    public void setSearchLang(Language searchLang) {
        this.searchLang = searchLang;
    }

    /**
     * @return the bookedOnline
     */
    public boolean isBookedOnline(int id) {
        Book b = bookServ.getBookByID(id);
        if(b.getOnlineCopy() != null && !b.getOnlineCopy().isEmpty())
            return true;
        else
            return false;
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
