/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectjava.service;

import com.projectjava.HibernateUtil;
import com.projectjava.pojo.Shelf;
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
public class ShelfService {
    private final static SessionFactory factory = HibernateUtil.getSessionFactory();
    
    public List<Shelf> getShelves(){
        try(Session session = factory.openSession()){
           CriteriaBuilder builder = session.getCriteriaBuilder();
           CriteriaQuery<Shelf> q = builder.createQuery(Shelf.class);
           Root<Shelf> root = q.from(Shelf.class);
           q.select(root);
           
           return session.createQuery(q).getResultList();
        }
    }
}
