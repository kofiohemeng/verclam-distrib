package com.verclamdistrib.woocomerce.customs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.verclamdistrib.woocomerce.R;
import com.verclamdistrib.woocomerce.adapters.SearchAdapter;
import com.verclamdistrib.woocomerce.models.product_filters_model.FilterDetails;
import com.verclamdistrib.woocomerce.models.product_filters_model.PostFilters;
import com.verclamdistrib.woocomerce.models.product_filters_model.ProductFilters;

import java.util.ArrayList;
import java.util.List;


/**
 * FilterDialog will be used to implement Price and Attribute Filters on Products in different categories
 **/

public abstract class SearchDialog extends Dialog {

    private PostFilters filters;
    private ProductFilters productFilters;

    public SearchAdapter filtersAdapter;
    public List<FilterDetails> filterDetailsList;
    public List<FilterDetails> selectedFilters = new ArrayList<>();

    private RecyclerView filters_recycler;
    private LinearLayout filter_dialog_attributes;


    public SearchDialog(Context context, List<FilterDetails> filterDetailsList, ProductFilters productFilters, PostFilters filters) {
        super(context);
        this.filters = filters;
        this.productFilters = productFilters;
        this.filterDetailsList = filterDetailsList;
        filtersAdapter = new SearchAdapter(getContext(), SearchDialog.this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make the Window Full Screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setContentView(R.layout.search_dialog);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        filters_recycler = (RecyclerView) findViewById(R.id.filters_recycler);
        filter_dialog_attributes = (LinearLayout) findViewById(R.id.filter_dialog_attributes);


        filters_recycler.setNestedScrollingEnabled(false);


        if (filterDetailsList.size() > 0) {
            filter_dialog_attributes.setVisibility(View.VISIBLE);
        } else {
            filter_dialog_attributes.setVisibility(View.GONE);
        }


        // Initialize the FilterProductsAdapter for RecyclerView
        filters_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        filters_recycler.setAdapter(filtersAdapter);

        filtersAdapter.notifyDataSetChanged();

        if (filters != null) {
            if (filters.getSelectedAttributes() != null  &&  filters.getSelectedAttributes().size() > 0)
                selectedFilters = filters.getSelectedAttributes();
        }
    }
    
    
    
    //*********** Apply Selected Filters on the Products of a Category ********//
    
    public void submitFilters() {
        
        if (selectedFilters.size() < 1) {
            clearFilters();
            dismiss();
            
        }
        else {
            PostFilters appliedFilters = new PostFilters();
            appliedFilters.setSelectedAttributes(selectedFilters);
    
            // Apply Filters
            applyFilters(appliedFilters);
    
            dismiss();
        }
        
    }

    //*********** Apply Selected Filters on the Products of a Category ********//

    public abstract void applyFilters(PostFilters filters);

    //*********** Clear All Filters on the Products of a Category ********//

    public abstract void clearFilters();
    
}

