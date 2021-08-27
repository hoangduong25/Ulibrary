/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectjava;

import com.projectjava.pojo.Book;
import com.projectjava.pojo.Genre;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

/**
 *
 * @author Hokichi
 */
public class Demo {
    private static final SessionFactory factory = HibernateUtil.getSessionFactory();
    public static void main(String[] args){
        try(Session session = factory.openSession()){
           CriteriaBuilder builder = session.getCriteriaBuilder();
           CriteriaQuery<Book> q = builder.createQuery(Book.class);
           Root<Book> root = q.from(Book.class);
           q.select(root).where(builder.equal(root.get("genre").get("id"), 8));
           
           Query<Book> query = session.createQuery(q);
           List<Book> obj = query.getResultList();
           
           for(Book b : obj){
               System.out.printf("%s\n", b.getTitle());
           }
        }
    }
}
