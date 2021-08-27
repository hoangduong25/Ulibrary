/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectjava.service;

import com.projectjava.HibernateUtil;
import com.projectjava.pojo.Book;
import com.projectjava.pojo.Genre;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author Hokichi
 */
public class GenreService {
    private final static SessionFactory factory = HibernateUtil.getSessionFactory();
    public List<Genre> getGenres(){
        try(Session session = factory.openSession()){
           CriteriaBuilder builder = session.getCriteriaBuilder();
           CriteriaQuery<Genre> q = builder.createQuery(Genre.class);
           Root<Genre> root = q.from(Genre.class);
           q.select(root);
           
           return session.createQuery(q).getResultList();
        }
    }
    
    public boolean addOrSaveGenre(Genre g){
        try(Session session = factory.openSession()) {
            try {
                session.getTransaction().begin();
                session.saveOrUpdate(g);
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
    
    public Genre getGenreByID(int id){
        try(Session session = factory.openSession()){
            return session.get(Genre.class, id);
        }
    }
}
