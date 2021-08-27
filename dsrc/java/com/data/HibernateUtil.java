/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.data;


import com.nhom26.pojo.Book;
import com.nhom26.pojo.Category;
import com.nhom26.pojo.Genre;
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
    private final static SessionFactory FACTORY;
    
    static{
        Configuration conf = new Configuration();
        Properties pros = new Properties();
        
        pros.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
        pros.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
        pros.put(Environment.URL, "jdbc:mysql://localhost:3306/demodb");
        pros.put(Environment.USER, "root");
        pros.put(Environment.PASS, "1234");
        
        conf.setProperties(pros);
        
        conf.addAnnotatedClass(Book.class);
        conf.addAnnotatedClass(Category.class);
        conf.addAnnotatedClass(Genre.class);
        ServiceRegistry registry = new StandardServiceRegistryBuilder()
                                  .applySettings(conf.getProperties()).build();
        
        FACTORY = conf.buildSessionFactory(registry);
    }
    
    public static SessionFactory getFactory() {
        return FACTORY;
    }
}