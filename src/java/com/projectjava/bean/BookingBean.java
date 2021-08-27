/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectjava.bean;

import com.projectjava.HibernateUtil;
import com.projectjava.pojo.Book;
import com.projectjava.pojo.Booking;
import com.projectjava.pojo.BookingDetails;
import com.projectjava.pojo.BookingStatus;
import com.projectjava.pojo.User;
import com.projectjava.service.BookService;
import com.projectjava.service.BookingService;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 *
 * @author Hokichi
 */
@ManagedBean
@Named(value = "bookingBean")
@SessionScoped
public class BookingBean implements Serializable {
    
    private static BookingService bookingServ = new BookingService();
    private static BookService bookServ = new BookService();
    /**
     * Creates a new instance of BookingBean
     */
    public BookingBean() {
    }
    
    public List<Booking> getBookings(){
        return bookingServ.getBookings();
    }
    
    public List<Object[]> getBookingsByCount(){
        return bookingServ.getBookingsByCount();
    }
    
    public List<Object[]> getBookingsByCount(String count){
        int cnt = Integer.parseInt(count);
        List<Object[]> topResult = new ArrayList<>();
        int c = 1;
        for(Object[] o: bookingServ.getBookingsByCount()){
            if(c > cnt)
                break;
            topResult.add(o);
            c++;
        }
        return topResult;
    }
    
    public String add() {
        Map<Integer, Object> cart = (Map<Integer, Object>) FacesContext.getCurrentInstance()
                                               .getExternalContext()
                                               .getSessionMap().get("cart");
        User u = (User) FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap().get("user");
        if (cart != null) {
            Booking p = new Booking();
            p.setCreatedDate(LocalDate.now());
            p.setDueDate(p.getCreatedDate().plusDays(90));
            p.setStatus(HibernateUtil.getSessionFactory()
                                     .openSession()
                                     .get(BookingStatus.class, 1));
            
            p.setUser(u);
            
            
            List<BookingDetails> details = new ArrayList<>();
            for (Object o: cart.values()) {
                Map<String, Object> d = (Map<String, Object>) o;
                Book book = bookServ.getBookByID(
                        Integer.parseInt(d.get("bookId").toString()));
                
                BookingDetails detail = new BookingDetails();
                detail.setBook(book);
                detail.setBooking(p);
                details.add(detail);
            }
            
            if (bookingServ.addBooking(p, details) == true) {
                bookingServ.sendMail(p, details);
                FacesContext.getCurrentInstance().getExternalContext()
                        .getSessionMap().remove("cart");
                
                return "index?faces-redirect=true";
            }
        }
        return "checkout";
    }
}
