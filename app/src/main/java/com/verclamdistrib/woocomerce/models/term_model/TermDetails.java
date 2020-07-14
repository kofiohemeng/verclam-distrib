package com.verclamdistrib.woocomerce.models.term_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.verclamdistrib.woocomerce.models.links_model.Links;


public class TermDetails {
    
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("menu_order")
    @Expose
    private int menuOrder;
    @SerializedName("_links")
    @Expose
    private Links links;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMenuOrder() { return menuOrder; }

    public void setMenuOrder(int menuOrder) {
        this.id = menuOrder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Links getLinks() {
        return links;
    }
    
    public void setLinks(Links links) {
        this.links = links;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    
}
