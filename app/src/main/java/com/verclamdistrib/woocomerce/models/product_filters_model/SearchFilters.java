package com.verclamdistrib.woocomerce.models.product_filters_model;

import com.verclamdistrib.woocomerce.models.attibute_model.AttributeDetails;
import com.verclamdistrib.woocomerce.models.category_model.CategoryDetails;
import com.verclamdistrib.woocomerce.models.tag_model.TagDetails;

import java.util.ArrayList;
import java.util.List;


public class SearchFilters {

    private String minPrice;

    private String maxPrice;

    private List<AttributeDetails> attributeList = new ArrayList<>();

    private List<CategoryDetails> categoryList = new ArrayList<>();

    private List<TagDetails> tagList = new ArrayList();

    
    public String getMinPrice() {
        return minPrice;
    }
    
    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }
    
    public String getMaxPrice() {
        return maxPrice;
    }
    
    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }


    public List<AttributeDetails> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<AttributeDetails> attributeList) {
        this.attributeList = attributeList;
    }

    public List<CategoryDetails> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<CategoryDetails> categoryList) {
        this.categoryList = categoryList;
    }

    public List<TagDetails> getTagList() {
        return tagList;
    }

    public void setTagList(List<TagDetails> tagList) {
        this.tagList = tagList;
    }
}
