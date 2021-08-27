/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectjava;

import com.projectjava.pojo.Author;
import com.projectjava.pojo.Book;
import com.projectjava.pojo.Booking;
import com.projectjava.pojo.BookingDetails;
import com.projectjava.pojo.BookingStatus;
import com.projectjava.pojo.Favorite;
import com.projectjava.pojo.Genre;
import com.projectjava.pojo.Language;
import com.projectjava.pojo.Posts;
import com.projectjava.pojo.Publisher;
import com.projectjava.pojo.Shelf;
import com.projectjava.pojo.User;
import java.util.Properties;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

/**
 *
 * @author Hokichi
 */
public class HibernateUtil {
    private static SessionFactory FACTORY;
    
    public static SessionFactory getSessionFactory(){
        Configuration conf = new Configuration();
        Properties prop = new Properties();
        prop.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
        prop.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
        prop.put(Environment.URL, "jdbc:mysql://localhost:3306/library?useSSL=false");
        prop.put(Environment.USER, "root");
        prop.put(Environment.PASS, "iloveu123");
        conf.setProperties(prop);
        
        conf.addAnnotatedClass(Book.class);
        conf.addAnnotatedClass(Author.class);
        conf.addAnnotatedClass(Genre.class);
        conf.addAnnotatedClass(Language.class);
        conf.addAnnotatedClass(Publisher.class);
        conf.addAnnotatedClass(User.class);
        conf.addAnnotatedClass(Favorite.class);
        conf.addAnnotatedClass(Booking.class);
        conf.addAnnotatedClass(BookingDetails.class);
        conf.addAnnotatedClass(BookingStatus.class);
        conf.addAnnotatedClass(Shelf.class);
        conf.addAnnotatedClass(Posts.class);
        
        ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
        
        FACTORY = conf.buildSessionFactory(registry);
        return FACTORY;
    }
}
