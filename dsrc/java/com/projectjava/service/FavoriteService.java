/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectjava.service;

import com.projectjava.HibernateUtil;
import com.projectjava.pojo.Favorite;
import java.util.List;
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
public class FavoriteService {
    private final static SessionFactory factory = HibernateUtil.getSessionFactory();
    
    public List<Favorite> getFavoriteOfUser(int userid){
        try(Session session = factory.openSession()){
           CriteriaBuilder builder = session.getCriteriaBuilder();
           CriteriaQuery<Favorite> q = builder.createQuery(Favorite.class);
           Root<Favorite> root = q.from(Favorite.class);
           q.select(root).where(builder.equal(root.get("user").get("id"), userid));
           
           return session.createQuery(q).getResultList();
        }
    }
    
    public boolean getFavorite(int bookid, int userid){
        try(Session session = factory.openSession()){
           CriteriaBuilder builder = session.getCriteriaBuilder();
           CriteriaQuery<Favorite> q = builder.createQuery(Favorite.class);
           Root<Favorite> root = q.from(Favorite.class);
           Predicate p1 = builder.equal(root.get("user").get("id"), userid);
           Predicate p2 = builder.equal(root.get("book").get("id"), bookid);
           q.select(root).where(builder.and(p1, p2));
           
           return session.createQuery(q).getSingleResult() != null;
        }
        catch (Exception e){
            return false;
        }
    }
    
    public List<Favorite> getFavs(){
        try(Session session = factory.openSession()){
           CriteriaBuilder builder = session.getCriteriaBuilder();
           CriteriaQuery<Favorite> q = builder.createQuery(Favorite.class);
           Root<Favorite> root = q.from(Favorite.class);
           q.select(root);
           
           return session.createQuery(q).getResultList();
        }
    }
    
    public boolean addFavorite(Favorite f){
        try(Session session = factory.openSession()) {
            try {
                session.getTransaction().begin();
                session.saveOrUpdate(f);
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
