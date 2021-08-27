/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectjava.bean;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author Hokichi
 */
@ManagedBean
@Named(value = "cartBean")
@SessionScoped
public class CartBean implements Serializable {
    
    private boolean cartEmpty;
    /**
     * Creates a new instance of CartBean
     */
    public CartBean() {
    }
    
    @PostConstruct
    public void init(){
        if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cart") == null){
           FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cart", new HashMap<>());
        }
    }
    
    public String addToCart(int bookID, String title, String cover){
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        Map<Integer, Object> cart = (Map<Integer, Object>) ctx.getSessionMap().get("cart");
        
        if(cart.get(bookID) == null) {
            Map<String, Object> p = new HashMap<>();
            p.put("bookId", bookID);
            p.put("title", title);
            p.put("cover", cover);
            
            cart.put(bookID, p);
        }
        else {
            
        }
        return "";
    }
    
    public List<Map<String, Object>> getCarts(){
        Map<Integer, Object> cart = (Map<Integer, Object>) FacesContext.getCurrentInstance()
                                                           .getExternalContext().getSessionMap().get("cart");
        
        List<Map<String, Object>> result = new ArrayList();
        for(Object o: cart.values()){
            Map<String, Object> d = (Map<String, Object>) o;
            result.add((Map<String, Object>) d);
        }
        return result;
    }
    
    public String removeItem(int id){
        Map<Integer, Object> cart = (Map<Integer, Object>) FacesContext.getCurrentInstance()
                                                           .getExternalContext().getSessionMap().get("cart");
        
        cart.remove((Map<Integer, Object>)cart.get(id));
        return "index?faces-redirect=true";
    }

    /**
     * @return the cartNotEmpty
     */
    public boolean isCartEmpty() {
        Map<Integer, Object> cart = (Map<Integer, Object>) FacesContext.getCurrentInstance().getExternalContext()
                                                                       .getSessionMap().get("cart");
        return cart == null;
    }

    /**
     * @param cartEmpty the cartEmpty to set
     */
    public void setCartEmpty(boolean cartEmpty) {
        this.cartEmpty = cartEmpty;
    }
}
