package com.verclamdistrib.woocomerce.models.category_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CategoryImage {
    
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("date_created")
    @Expose
    private String dateCreated;
    @SerializedName("date_created_gmt")
    @Expose
    private String dateCreatedGmt;
    @SerializedName("date_modified")
    @Expose
    private String dateModified;
    @SerializedName("date_modified_gmt")
    @Expose
    private String dateModifiedGmt;
    @SerializedName("src")
    @Expose
    private String src;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("alt")
    @Expose
    private String alt;
    
    
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getDateCreated() {
        return dateCreated;
    }
    
    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
    
    public String getDateCreatedGmt() {
        return dateCreatedGmt;
    }
    
    public void setDateCreatedGmt(String dateCreatedGmt) {
        this.dateCreatedGmt = dateCreatedGmt;
    }
    
    public String getDateModified() {
        return dateModified;
    }
    
    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }
    
    public String getDateModifiedGmt() {
        return dateModifiedGmt;
    }
    
    public void setDateModifiedGmt(String dateModifiedGmt) {
        this.dateModifiedGmt = dateModifiedGmt;
    }
    
    public String getSrc() {
        return src;
    }
    
    public void setSrc(String src) {
        this.src = src;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAlt() {
        return alt;
    }
    
    public void setAlt(String alt) {
        this.alt = alt;
    }

}
