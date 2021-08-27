/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectjava.service;

import com.projectjava.HibernateUtil;
import com.projectjava.pojo.Author;
import com.projectjava.pojo.Book;
import com.projectjava.pojo.BookingDetails;
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
public class BookService {
    private final static SessionFactory factory = HibernateUtil.getSessionFactory();
    public List<Book> getBooks(){
        try(Session session = factory.openSession()){
           CriteriaBuilder builder = session.getCriteriaBuilder();
           CriteriaQuery<Book> q = builder.createQuery(Book.class);
           Root<Book> root = q.from(Book.class);
           q.select(root);
           
           return session.createQuery(q).getResultList();
        }
    }
    
    public boolean addOrSaveBook(Book b){
        try(Session session = factory.openSession()) {
            try {
                session.getTransaction().begin();
                session.saveOrUpdate(b);
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
    
    public boolean deleteBook(int id){
        try(Session session = factory.openSession()) {
            try {
                session.getTransaction().begin();
                Book b = (Book)session.load(Book.class, id);
                session.remove(b);
                System.out.print("success");
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
    
    public Book getBookByID(int id){
        try(Session session = factory.openSession()){
            return session.get(Book.class, id);
        }
    }
    
    public List<Book> getBookByAuthorID(int id){
        try(Session session = factory.openSession()){
           CriteriaBuilder builder = session.getCriteriaBuilder();
           CriteriaQuery<Book> q = builder.createQuery(Book.class);
           Root<Book> bRoot = q.from(Book.class);
           Join<Book, Author> aRoot = bRoot.join("author");
           q.select(bRoot).where(builder.equal(aRoot.get("id"), id));
           
           return session.createQuery(q).getResultList();
        }
    }
    
    public List<Book> getBookByGenreID(int id){
        try(Session session = factory.openSession()){
           CriteriaBuilder builder = session.getCriteriaBuilder();
           CriteriaQuery<Book> q = builder.createQuery(Book.class);
           Root<Book> bRoot = q.from(Book.class);
           q.select(bRoot).where(builder.equal(bRoot.get("genre").get("id"), id));
           
           return session.createQuery(q).getResultList();
        }
    }
    
    public List<Book> getBookByLangID(int id){
        try(Session session = factory.openSession()){
           CriteriaBuilder builder = session.getCriteriaBuilder();
           CriteriaQuery<Book> q = builder.createQuery(Book.class);
           Root<Book> bRoot = q.from(Book.class);
           q.select(bRoot).where(builder.equal(bRoot.get("lang").get("id"), id));
           
           return session.createQuery(q).getResultList();
        }
    }
    
    public List<Book> getSearchResults(String kw) {
        try(Session session = factory.openSession()){
           CriteriaBuilder builder = session.getCriteriaBuilder();
           CriteriaQuery<Book> q = builder.createQuery(Book.class);
           Root<Book> bRoot = q.from(Book.class);
           Join<Book, Author> aRoot = bRoot.join("author");
           List<Predicate> p = new ArrayList<>();
           
               String s = String.format("%%%s%%", kw);
               p.add(builder.or(
                       builder.like(bRoot.get("title").as(String.class), s), 
                       builder.like(bRoot.get("description").as(String.class), s)));
               p.add(builder.like(aRoot.get("name").as(String.class), s));
               
           Predicate finalP = builder.or(p.toArray(new Predicate[p.size()]));
           q.select(bRoot).where(finalP);
           
           return session.createQuery(q).getResultList();
        }
    }
    
    public boolean isBooked(int bookid, int userid) {
        try(Session session = factory.openSession()){
           CriteriaBuilder builder = session.getCriteriaBuilder();
           CriteriaQuery<BookingDetails> q = builder.createQuery(BookingDetails.class);
           Root<BookingDetails> root = q.from(BookingDetails.class);
           Predicate p1 = builder.equal(root.get("booking").get("user").get("id"), userid);
           Predicate p2 = builder.equal(root.get("book").get("id"), bookid);
           
           q = q.select(root).where(builder.and(p1, p2));
           
           if (session.createQuery(q).getSingleResult() != null)
               return true;
           else
               return false;
        }
    }
}
