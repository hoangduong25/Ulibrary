/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectjava.bean;

import com.projectjava.pojo.User;
import com.projectjava.service.UserService;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 *
 * @author Hokichi
 */
@ManagedBean
@Named(value = "activationBean")
@RequestScoped
public class ActivationBean implements Serializable{
    
    private String key = FacesContext.getCurrentInstance()
                             .getExternalContext()
                             .getRequestParameterMap()
                             .get("key");
    
    private boolean valid;
    private String title;
    
    
    private final static UserService userServ = new UserService();
    
    /**
     * Creates a new instance of ActivationBean
     */
    public ActivationBean() {
    }
    
    @PostConstruct
    public void init(){
        if(key != null && !key.isEmpty()){
            User u = userServ.getUserByKey(key);
            if(u != null) {
                u.setPassword(u.getPassword());
                u.setVerified(true);
                u.setUserhash(null);
                userServ.addOrUpdateUser(u);
                this.setValid(true);
            }
        }
        else
            this.setValid(false);
        
    }

    /**
     * @return the valid
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * @param valid the valid to set
     */
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        if(this.valid) {
            return "thành công";
        }
        else
            return "thất bại";
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
