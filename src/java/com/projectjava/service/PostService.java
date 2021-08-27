/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectjava.service;

import com.projectjava.HibernateUtil;
import com.projectjava.pojo.Posts;
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
public class PostService {
    private final static SessionFactory factory = HibernateUtil.getSessionFactory();
    
    public List<Posts> getPosts(){
        try(Session session = factory.openSession()){
           CriteriaBuilder builder = session.getCriteriaBuilder();
           CriteriaQuery<Posts> q = builder.createQuery(Posts.class);
           Root<Posts> root = q.from(Posts.class);
           q.select(root);
           
           return session.createQuery(q).getResultList();
        }
    }
    
    public Posts getPostByID(int id){
        try(Session session = factory.openSession()){
            return session.get(Posts.class, id);
        }
    }
    
    public boolean addOrSavePost(Posts p){
        try(Session session = factory.openSession()) {
            try {
                session.getTransaction().begin();
                session.saveOrUpdate(p);
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
