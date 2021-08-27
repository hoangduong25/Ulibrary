/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectjava.service;

import com.projectjava.HibernateUtil;
import com.projectjava.pojo.User;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author Hokichi
 */
public class UserService {
    private final static SessionFactory factory = HibernateUtil.getSessionFactory();
    
    public List<User> getUsers(){
        try(Session session = factory.openSession()){
           CriteriaBuilder builder = session.getCriteriaBuilder();
           CriteriaQuery<User> q = builder.createQuery(User.class);
           Root<User> root = q.from(User.class);
           q.select(root);
           
           return session.createQuery(q).getResultList();
        }
    }
    
    public boolean addOrUpdateUser(User u){
        try(Session session = factory.openSession()) {
            try {
                session.getTransaction().begin();
                session.saveOrUpdate(u);
                session.getTransaction().commit();
            }
            catch(Exception ex){
                session.getTransaction().rollback();
                System.out.println(ex);
                return false;
            }
        return true;
        }
    }
    public User login(String email, String password){
        password = DigestUtils.md5Hex(password);
        try(Session session = factory.openSession()) {
            CriteriaBuilder b = session.getCriteriaBuilder();
            CriteriaQuery<User> q = b.createQuery(User.class);
            Root<User> root = q.from(User.class);
            q.select(root);
            Predicate p1 = b.equal(root.get("email").as(String.class), email);
            Predicate p2 = b.equal(root.get("password").as(String.class), password);
            q.where(b.and(p1, p2));
            
            return session.createQuery(q).getSingleResult();
        }
        catch(Exception e){
            return null;
        }
    }
    
    public User getUserByID(int id){
        try(Session session = factory.openSession()){
            return session.get(User.class, id);
        }
    }
    
    public User getUserByKey(String key){
        try(Session session = factory.openSession()){
            
            CriteriaBuilder b = session.getCriteriaBuilder();
            CriteriaQuery<User> q = b.createQuery(User.class);
            Root<User> root = q.from(User.class);
            q.select(root).where(b.like(root.get("userhash").as(String.class), key));
            
            return session.createQuery(q).getSingleResult();
        }
    }
    
    public void sendMail(String userEmail, String userHash) {
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
        
        //thêm file html vào content ở đây
        try {
            Path path = FileSystems.getDefault().getPath(".").toAbsolutePath();
            String[] p = path.toString().split("apache");
            for(String data : Files.readAllLines(Paths.get(p[0] + "web\\resources\\emails\\Verify.txt"))) {
                if(data.contains("#{userBean.userHash}")){
                    String link = "localhost:8080/projectjava/activation.xhtml?key=" + userHash;
                    content += data.replace("localhost:8080/projectjava/activation.xhtml?key=#{userBean.userHash}", link);
                }
                else {
                    content += data;
                }
            }
            System.out.println("Mail sent");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            String subject = "Xác thực tài khoản tại ULIBRARY";
            
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
            message.setSubject(subject, "utf-8");
            message.setContent(content, "text/html; charset=utf-8");
           
            Transport.send(message);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}
