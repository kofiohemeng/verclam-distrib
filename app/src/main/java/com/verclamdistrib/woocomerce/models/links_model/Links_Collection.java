package com.verclamdistrib.woocomerce.models.links_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Links_Collection {
    
    @SerializedName("href")
    @Expose
    private String href;
    
    public String getHref() {
        return href;
    }
    
    public void setHref(String href) {
        this.href = href;
    }

}
