/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectjava.bean;

import com.projectjava.pojo.Shelf;
import com.projectjava.service.ShelfService;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Hokichi
 */
@ManagedBean
@Named(value = "shelfBean")
@SessionScoped
public class ShelfBean implements Serializable {

    private final static ShelfService shelfServ = new ShelfService();
    /**
     * Creates a new instance of ShelfBean
     */
    public ShelfBean() {
    }
    
    public List<Shelf> getShelves(){
        return shelfServ.getShelves();
    }
    
}
