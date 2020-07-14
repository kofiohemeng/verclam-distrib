package com.verclamdistrib.woocomerce.models.attibute_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.verclamdistrib.woocomerce.models.links_model.Links;
import com.verclamdistrib.woocomerce.models.term_model.TermDetails;

import java.util.List;


public class AttributeDetails {
    
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("order_by")
    @Expose
    private String orderBy;
    @SerializedName("has_archives")
    @Expose
    private boolean hasArchives;
    @SerializedName("_links")
    @Expose
    private Links links;

    private List<TermDetails> termListe;

    public List<TermDetails> getTermListe() {
        return termListe;
    }

    public void setTermListe(List<TermDetails> termListe) {
        this.termListe = termListe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Boolean getHasArchives() {
        return hasArchives;
    }

    public void setHasArchives(Boolean hasArchives) {  this.hasArchives = hasArchives;   }

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
    
}
