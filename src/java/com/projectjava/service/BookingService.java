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
        
        content += "<img src=\"\" style=\"width: 100%; height: 100px; margin-bottom: 50px\"><h1 class=\"text-center\">Cảm ơn bạn đã mượn sách tại ULibrary!</h1>";
        content += "<p class=\"pt-4\">ULibrary đã nhận được yêu cầu mượn sách của bạn và đang xử lý nhé.</p>";
        content += "<h5 style=\"color:cornflowerblue\">THÔNG TIN ĐƠN HÀNG #" + booking.getId() + "<span style=\"color: black; font-size:smaller; font-style: italic;\">(Ngày" + booking.getCreatedDate() + ")</span></h5>";
        content += "<hr style=\"margin-top:0;\">";
        content += "<ul style=\"list-style-type: none;\">";
        content += "<li><b>Tên: </b>" + booking.getUser().getName() + "</li>";
        content += "<li><b>Điện thoại: </b>" + booking.getUser().getPhone_number() + "</li>";
        content += "<li><b>Địa chỉ: </b>" + booking.getUser().getAddress() + "</li>";
        content += "<li><b>Email: </b>" + booking.getUser().getEmail() + "</li></ul>";
        content += "<li><b>Tình trạng: </b>" + booking.getStatus().getName() + "</li></ul>";
        content += "<h5 style=\"color:cornflowerblue\">CHI TIẾT ĐƠN SÁCH</h5><hr style=\"margin-top:0;\"><table class=\"table\"><thead class=\"thead-light\"><tr><th>Tên sách</th><th>Hình thức mượn</th></tr></thead>";
        content += "<tbody>";
        for(BookingDetails d : details) {
            content += "<tr><td>";
            content += d.getBook().getTitle();
            content += "</td><td>";
            if(d.isOnline())
                content += "E-Book";
            else
                content += "Mượn tại thư viện";
        }
        content += "</tbody></table><hr/>";
        content += "<div><br><p>Bạn cần được hỗ trợ ngay? Chỉ cần email<a href=\"mailto:ulib2021@gmail.vn\">ulib2021@gmail.vn</a> , hoặc gọi số điện thoại 1900-6035 (8-21h cả T7,CN). Đội ngũ ULibrary luôn sẵn sàng hỗ trợ bạn bất kì lúc nào.</p></div>";
        
        try {
            String subject = "Xác thực tài khoản tại ULIBRARY";
            
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
