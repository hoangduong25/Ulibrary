/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectjava.service;

import com.projectjava.HibernateUtil;
import com.projectjava.pojo.Booking;
import com.projectjava.pojo.BookingDetails;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author Hokichi
 */
public class BookingService {
    private final static SessionFactory factory = HibernateUtil.getSessionFactory();
    
    public boolean addBooking(Booking booking, List<BookingDetails> details) {
        try (Session session = factory.openSession()) {
            try {
                session.getTransaction().begin();
                
                session.save(booking);
                for (BookingDetails detail: details)
                    session.save(detail);
                
                session.getTransaction().commit();
                
                return true;
            } catch (Exception ex) {
                session.getTransaction().rollback();
            }
        }
        
        return false;
    }
    
    public List<Booking> getBookings(){
        try(Session session = factory.openSession()){
           CriteriaBuilder builder = session.getCriteriaBuilder();
           CriteriaQuery<Booking> q = builder.createQuery(Booking.class);
           Root<Booking> root = q.from(Booking.class);
           q.select(root);
           
           return session.createQuery(q).getResultList();
        }
    }
    
    public List<Object[]> getBookingsByCount(){
        try(Session session = factory.openSession()){
           CriteriaBuilder builder = session.getCriteriaBuilder();
           CriteriaQuery<Object[]> q = builder.createQuery(Object[].class);
           Root<BookingDetails> root = q.from(BookingDetails.class);
           q.multiselect(root.get("book").get("genre").get("name"),
                         root.get("book").get("title"),
                         root.get("book").get("cover"),
                         builder.count(root.get("id")),
                         root.get("book").get("id"));
           q.groupBy(root.get("book").get("id"));
           q.orderBy(builder.desc(root.get("book").get("id")));
           
           return session.createQuery(q).getResultList();
        }
    }
    
    public List<Booking> getBookingByUser(int id){
        try(Session session = factory.openSession()){
           CriteriaBuilder builder = session.getCriteriaBuilder();
           CriteriaQuery<Booking> q = builder.createQuery(Booking.class);
           Root<Booking> root = q.from(Booking.class);
           q.select(root).where(builder.equal(root.get("user").get("id"), id));
           
           return session.createQuery(q).getResultList();
        }
    }
    
    public List<BookingDetails> getDetailsByBookingID(int id){
        try(Session session = factory.openSession()){
           CriteriaBuilder builder = session.getCriteriaBuilder();
           CriteriaQuery<BookingDetails> q = builder.createQuery(BookingDetails.class);
           Root<BookingDetails> root = q.from(BookingDetails.class);
           q.select(root).where(builder.equal(root.get("booking").get("id"), id));
           
           return session.createQuery(q).getResultList();
        }
    }
    
    public void sendMail(Booking booking, List<BookingDetails> details) {
        final String email = "ulib2021@gmail.com";
        final String password = "ulibrary2021";
        
        String content = "";
        
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        
        javax.mail.Session session = javax.mail.Session.getInstance(prop, 
                new javax.mail.Authenticator(){
                    protected PasswordAuthentication getPasswordAuthentication(){
                        return new PasswordAuthentication(email, password);
            }
        });
        
        content += "<img src=\"\" style=\"width: 100%; height: 100px; margin-bottom: 50px\"><h1 class=\"text-center\">C???m ??n b???n ???? m?????n s??ch t???i ULibrary!</h1>";
        content += "<p class=\"pt-4\">ULibrary ???? nh???n ???????c y??u c???u m?????n s??ch c???a b???n v?? ??ang x??? l?? nh??.</p>";
        content += "<h5 style=\"color:cornflowerblue\">TH??NG TIN ????N H??NG #" + booking.getId() + "<span style=\"color: black; font-size:smaller; font-style: italic;\">(Ng??y" + booking.getCreatedDate() + ")</span></h5>";
        content += "<hr style=\"margin-top:0;\">";
        content += "<ul style=\"list-style-type: none;\">";
        content += "<li><b>T??n: </b>" + booking.getUser().getName() + "</li>";
        content += "<li><b>??i???n tho???i: </b>" + booking.getUser().getPhone_number() + "</li>";
        content += "<li><b>?????a ch???: </b>" + booking.getUser().getAddress() + "</li>";
        content += "<li><b>Email: </b>" + booking.getUser().getEmail() + "</li></ul>";
        content += "<li><b>T??nh tr???ng: </b>" + booking.getStatus().getName() + "</li></ul>";
        content += "<h5 style=\"color:cornflowerblue\">CHI TI???T ????N S??CH</h5><hr style=\"margin-top:0;\"><table class=\"table\"><thead class=\"thead-light\"><tr><th>T??n s??ch</th><th>H??nh th???c m?????n</th></tr></thead>";
        content += "<tbody>";
        for(BookingDetails d : details) {
            content += "<tr><td>";
            content += d.getBook().getTitle();
            content += "</td><td>";
            if(d.isOnline())
                content += "E-Book";
            else
                content += "M?????n t???i th?? vi???n";
        }
        content += "</tbody></table><hr/>";
        content += "<div><br><p>B???n c???n ???????c h??? tr??? ngay? Ch??? c???n email<a href=\"mailto:ulib2021@gmail.vn\">ulib2021@gmail.vn</a> , ho???c g???i s??? ??i???n tho???i 1900-6035 (8-21h c??? T7,CN). ?????i ng?? ULibrary lu??n s???n s??ng h??? tr??? b???n b???t k?? l??c n??o.</p></div>";
        
        try {
            String subject = "X??c th???c t??i kho???n t???i ULIBRARY";
            
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(booking.getUser().getEmail()));
            message.setSubject(subject, "utf-8");
            message.setContent(content, "text/html; charset=utf-8");
           
            Transport.send(message);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}
