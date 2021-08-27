/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectjava.bean;

import com.projectjava.pojo.Booking;
import com.projectjava.pojo.BookingDetails;
import com.projectjava.pojo.Favorite;
import com.projectjava.pojo.User;
import com.projectjava.service.BookingService;
import com.projectjava.service.FavoriteService;
import com.projectjava.service.UserService;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Transient;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author Hokichi
 */
@ManagedBean
@Named(value = "userBean")
@ViewScoped
public class UserBean implements Serializable{
    private int id;
    private String name;
    private String password;
    @Transient
    private String confirmPassword;
    @Transient
    private String newPassword;
    private String email;
    private String phone_number;
    private String address;
    private String card_id;
    private LocalDate added_date;
    private String userHash;
    
    private boolean noFav;
    
    
    private static UserService userServ = new UserService();
    private static FavoriteService favServ = new FavoriteService();
    private static BookingService bookingServ = new BookingService();
    /**
     * Creates a new instance of UserBean
     */
    public UserBean() {
        if(!FacesContext.getCurrentInstance().isPostback()){
            User user = (User) FacesContext.getCurrentInstance()
                                        .getExternalContext()
                                        .getSessionMap()
                                        .get("user");
            if(user != null){
                User u = userServ.getUserByID(user.getId());
                this.id = u.getId();
                this.name = u.getName();
                this.email = u.getEmail();
                this.phone_number = u.getPhone_number();
                this.address = u.getAddress();
                this.card_id = u.getCard_id();
                this.added_date = u.getAdded_date();
            }
        }
    }
    
    public List<Favorite> getFavs(){
        return favServ.getFavoriteOfUser(this.id);
    }
    
    public List<BookingDetails> getBookings(){
        List<Booking> booking = bookingServ.getBookingByUser(this.id);
        List<BookingDetails> result = new ArrayList();
        for(Booking bk: booking){
           result.addAll(bookingServ.getDetailsByBookingID(bk.getId()));
        }
        return result;
    }
    
    public String editInfo(){
        User u = (User) FacesContext.getCurrentInstance()
                                   .getExternalContext()
                                   .getSessionMap()
                                   .get("user");
        if(u != null){
            u.setName(this.getName());
            u.setEmail(this.getEmail());
            u.setPhone_number(this.getPhone_number());
            u.setAddress(this.getAddress());
            if(this.getNewPassword() != null && !this.getNewPassword().isEmpty() && !this.getPassword().isEmpty() &&
                this.getNewPassword().equals(this.getConfirmPassword())){
                if(u.getPassword().equals(DigestUtils.md5Hex(this.getNewPassword())))
                    u.setPassword(this.getNewPassword());
                if (userServ.addOrUpdateUser(u)) {
                    return "user?faces-redirect=true";
                }
            }
        }
        return "user";
    }
    public String register() {
        if (this.getPassword().equals(this.getConfirmPassword()) && !this.password.isEmpty()) {
            User u = new User();
            u.setName(this.getName());
            u.setPassword(DigestUtils.md5Hex(this.getPassword()));
            u.setEmail(this.getEmail());
            u.setPhone_number(this.getPhone_number());
            u.setAddress(this.getAddress());
            u.setAdded_date(java.time.LocalDate.now());
            Random rd = new Random();
            String hash = DigestUtils.md5Hex("" + rd.nextInt(999999));
            u.setUserhash(hash);
            this.userHash = u.getUserhash();
            u.setVerified(false);

            if (userServ.addOrUpdateUser(u)) {
                userServ.sendMail(u.getEmail(), u.getUserhash());
                return "index?faces-redirect=true";
            }
        }
        return null;
    }

    public String login() {
        User u = userServ.login(this.getEmail(), this.getPassword());
        if (u != null) {
            if(u.isVerified()){    
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getSessionMap()
                        .put("user", u);
                return "";
            }
        }
        return "index?faces-redirect=true";
    }

    public String isLoggedIn() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user") == null) {
            return "index?faces-redirect=true";
        }
        return null;
    }
    
    public boolean loggedIn() {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user") == null;
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        return "index?faces-redirect=true";
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
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * @param confirmPassword the confirmPassword to set
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

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
     * @return the userServ
     */
    public static UserService getUserServ() {
        return userServ;
    }

    /**
     * @param aUserServ the userServ to set
     */
    public static void setUserServ(UserService aUserServ) {
        userServ = aUserServ;
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
     * @return the newPassword
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * @param newPassword the newPassword to set
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * @return the noFav
     */
    public boolean isNoFav() {
        return favServ.getFavoriteOfUser(this.id).isEmpty();
    }

    /**
     * @param noFav the noFav to set
     */
    public void setNoFav(boolean noFav) {
        this.noFav = noFav;
    }

    /**
     * @return the userHash
     */
    public String getUserHash() {
        return userHash;
    }

    /**
     * @param userHash the userHash to set
     */
    public void setUserHash(String userHash) {
        this.userHash = userHash;
    }
}
