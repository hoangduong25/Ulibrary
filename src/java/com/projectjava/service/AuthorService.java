/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectjava.service;

import com.projectjava.HibernateUtil;
import com.projectjava.pojo.Author;
import com.projectjava.pojo.Book;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author Hokichi
 */
public class AuthorService {
    private final static SessionFactory factory = HibernateUtil.getSessionFactory();
    public List<Author> getAuthors(){
        try(Session session = factory.openSession()){
           CriteriaBuilder builder = session.getCriteriaBuilder();
           CriteriaQuery<Author> q = builder.createQuery(Author.class);
           Root<Author> root = q.from(Author.class);
           q.select(root);
           
           return session.createQuery(q).getResultList();
        }
    }
    public Author getAuthorByID(int id){
        try(Session session = factory.openSession()){
            return session.get(Author.class, id);
        }
    }
    
    public boolean addOrSaveAuthor(Author a){
        try(Session session = factory.openSession()) {
            try {
                session.getTransaction().begin();
                session.saveOrUpdate(a);
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
}
