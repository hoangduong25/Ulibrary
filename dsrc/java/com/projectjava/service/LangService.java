/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectjava.service;

import com.projectjava.HibernateUtil;
import com.projectjava.pojo.Language;
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
public class LangService {
    private final static SessionFactory factory = HibernateUtil.getSessionFactory();
    public List<Language> getLangs(){
        try(Session session = factory.openSession()){
           CriteriaBuilder builder = session.getCriteriaBuilder();
           CriteriaQuery<Language> q = builder.createQuery(Language.class);
           Root<Language> root = q.from(Language.class);
           q.select(root);
           
           return session.createQuery(q).getResultList();
        }
    }
    public boolean addOrSaveLang(Language l){
        try(Session session = factory.openSession()) {
            try {
                session.getTransaction().begin();
                session.saveOrUpdate(l);
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
    public Language getLangByID(int id){
        try(Session session = factory.openSession()){
            return session.get(Language.class, id);
        }
    }
}
