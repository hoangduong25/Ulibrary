/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectjava.service;

import com.projectjava.HibernateUtil;
import com.projectjava.pojo.Publisher;
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
public class PubService {
    private final static SessionFactory factory = HibernateUtil.getSessionFactory();
    public List<Publisher> getPubs(){
        try(Session session = factory.openSession()){
           CriteriaBuilder builder = session.getCriteriaBuilder();
           CriteriaQuery<Publisher> q = builder.createQuery(Publisher.class);
           Root<Publisher> root = q.from(Publisher.class);
           q.select(root);
           
           return session.createQuery(q).getResultList();
        }
    }
    
    public boolean addOrSavePub(Publisher g){
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
    
    public Publisher getPubByID(int id){
        try(Session session = factory.openSession()){
            return session.get(Publisher.class, id);
        }
    }
}
