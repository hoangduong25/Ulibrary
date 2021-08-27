/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.projectjava.bean;

import com.projectjava.pojo.Posts;
import com.projectjava.service.PostService;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

/**
 *
 * @author Hokichi
 */
@ManagedBean
@Named(value = "postBean")
@SessionScoped
public class PostBean implements Serializable {
    private int id;
    private String title;
    private String content;
    private String cover;
    private LocalDate creation_date;
    private Part img;
    private Part web;
    
    private static PostService postServ = new PostService();

    /**
     * Creates a new instance of PostBean
     */
    public PostBean() {
        if(!FacesContext.getCurrentInstance().isPostback()){
            String postID = FacesContext.getCurrentInstance()
                                        .getExternalContext()
                                        .getRequestParameterMap()
                                        .get("postid");
            if(postID != null && !postID.isEmpty()){
                Posts p = postServ.getPostByID(Integer.parseInt(postID));
                this.id = p.getId();
                this.title = p.getTitle();
                this.content = p.getContent();
                this.cover = p.getCover();
                this.creation_date = p.getCreation_date();
            }
        }
    }
    
    public List<Posts> getPosts(){
        return getPostServ().getPosts();
    }
    
    public String addPost(){
        Posts p;
        if(this.getId() > 0){
            p  = postServ.getPostByID(this.getId());
        }
        else{
            p = new Posts();
        }
        
        p.setId(this.getId());
        p.setTitle(this.getTitle());
        p.setCreation_date(LocalDate.now());
        
        try {
            if(getImg() != null){
                this.uploadFile(this.getImg(), "resources/img/news");
                p.setCover(this.getImg().getSubmittedFileName());
            }
            
            if(getWeb() != null){
                this.uploadFile(this.getWeb(), "");
                p.setContent(this.getWeb().getSubmittedFileName());
            }
            if(postServ.addOrSavePost(p))
                return "posts?faces-redirect=true";
        } catch (IOException ex) {
            Logger.getLogger(PostBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    
    private void uploadFile(Part p, String d) throws IOException{
        String path = FacesContext.getCurrentInstance()
                      .getExternalContext().getRealPath(d)
                      + "/" + p.getSubmittedFileName();  
        try(InputStream in = p.getInputStream();
            FileOutputStream out = new FileOutputStream(path)){
            byte[] b = new byte[1024];
            int byteRead;
            while((byteRead = in.read(b)) != -1)
                out.write(b, 0, byteRead);
        }
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the cover
     */
    public String getCover() {
        return cover;
    }

    /**
     * @param cover the cover to set
     */
    public void setCover(String cover) {
        this.cover = cover;
    }

    /**
     * @return the creation_date
     */
    public LocalDate getCreation_date() {
        return creation_date;
    }

    /**
     * @param creation_date the creation_date to set
     */
    public void setCreation_date(LocalDate creation_date) {
        this.creation_date = creation_date;
    }

    /**
     * @return the postServ
     */
    public static PostService getPostServ() {
        return postServ;
    }

    /**
     * @param aPostServ the postServ to set
     */
    public static void setPostServ(PostService aPostServ) {
        postServ = aPostServ;
    }

    /**
     * @return the img
     */
    public Part getImg() {
        return img;
    }

    /**
     * @param img the img to set
     */
    public void setImg(Part img) {
        this.img = img;
    }

    /**
     * @return the web
     */
    public Part getWeb() {
        return web;
    }

    /**
     * @param web the web to set
     */
    public void setWeb(Part web) {
        this.web = web;
    }
    
}
