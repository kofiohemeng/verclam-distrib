package com.verclamdistrib.woocomerce.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.verclamdistrib.woocomerce.R;


import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.verclamdistrib.woocomerce.adapters.ProductAdapter;
import com.verclamdistrib.woocomerce.constant.ConstantValues;
import com.verclamdistrib.woocomerce.customs.EndlessRecyclerViewScroll;
import com.verclamdistrib.woocomerce.customs.SearchDialog;
import com.verclamdistrib.woocomerce.models.api_response_model.ErrorResponse;
import com.verclamdistrib.woocomerce.models.attibute_model.AttributeDetails;
import com.verclamdistrib.woocomerce.models.category_model.CategoryDetails;
import com.verclamdistrib.woocomerce.app.App;
import com.verclamdistrib.woocomerce.models.product_filters_model.FilterDetails;
import com.verclamdistrib.woocomerce.models.product_filters_model.FilterTerm;
import com.verclamdistrib.woocomerce.models.product_filters_model.PostFilters;
import com.verclamdistrib.woocomerce.models.product_filters_model.ProductFilters;
import com.verclamdistrib.woocomerce.models.product_model.ProductDetails;
import com.verclamdistrib.woocomerce.models.term_model.TermDetails;
import com.verclamdistrib.woocomerce.network.APIClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;


public class SearchFragment extends Fragment {

    View rootView;

    AdView mAdView;
    SearchView search_editText;
    FrameLayout banner_adView;

    int pageNo = 1;
    boolean isGridView;
    boolean isFilterApplied;

    PostFilters postFilters = null;
    ProductFilters productFilters = null;

    String order = "desc";
    String sortBy = "date";
    String search = "";

    LinearLayout bottomBar;
    LinearLayout sortList;
    TextView emptyRecord;
    TextView sortListText;
    ProgressBar progressBar;
    ProgressBar loadingProgress;
    ImageButton removeFilterBtn;
    ToggleButton filterButton;
    ToggleButton toggleLayoutView;
    RecyclerView all_products_recycler;

    LoadMoreTask loadMoreTask;
    SearchDialog filterDialog;

    ProductAdapter productAdapter;
    List<ProductDetails> productsList;
    List<FilterDetails> productFiltersList;

    GridLayoutManager gridLayoutManager;
    LinearLayoutManager linearLayoutManager;

    List<AttributeDetails> attributeList;
    List<CategoryDetails> allCategoriesList;
    List<CategoryDetails> subCategoriesList;

    public static boolean FLAG_SEARCHED = false;
    public static boolean CLEAR_LIST = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.search_fragment_new, container, false);

        setHasOptionsMenu(true);
        if (getArguments() != null) {
            if (getArguments().containsKey("sortBy")) {
                sortBy = getArguments().getString("sortBy", "date");
            }
        }

        // Binding Layout Views
        bottomBar = (LinearLayout) rootView.findViewById(R.id.bottomBar);
        sortList = (LinearLayout) rootView.findViewById(R.id.sort_list);
        sortListText = (TextView) rootView.findViewById(R.id.sort_text);
        emptyRecord = (TextView) rootView.findViewById(R.id.empty_record);
        progressBar = (ProgressBar) rootView.findViewById(R.id.loading_bar);
        loadingProgress = (ProgressBar) rootView.findViewById(R.id.loading_progress);
        removeFilterBtn = (ImageButton) rootView.findViewById(R.id.removeFilterBtn);
        filterButton = (ToggleButton) rootView.findViewById(R.id.filterBtn);
        toggleLayoutView = (ToggleButton) rootView.findViewById(R.id.layout_toggleBtn);
        all_products_recycler = (RecyclerView) rootView.findViewById(R.id.products_recycler);

        emptyRecord.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        loadingProgress.setVisibility(View.GONE);


        sortListText.setText(getString(R.string.sortby));

        isGridView = true;
        //isFilterApplied = (isSaleApplied || isFeaturedApplied);

        toggleLayoutView.setChecked(isGridView);
        filterButton.setChecked(isFilterApplied);
        removeFilterBtn.setVisibility(isFilterApplied? View.VISIBLE : View.GONE);


        productsList = new ArrayList<>();
        productFiltersList = new ArrayList<>();


        // Initialize GridLayoutManager and LinearLayoutManager
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        linearLayoutManager = new LinearLayoutManager(getContext());


        // Initialize the ProductAdapter for RecyclerView
        productAdapter = new ProductAdapter(getContext(), productsList, false);

        setRecyclerViewLayoutManager(isGridView);
        all_products_recycler.setAdapter(productAdapter);


        if (isFilterApplied) {
            postFilters = new PostFilters();

            loadingProgress.setVisibility(View.VISIBLE);

            //RequestFilteredProducts(pageNo, postFilters);
            RequestAllProducts(pageNo, postFilters);

        }
        else {
            postFilters = new PostFilters();
            loadingProgress.setVisibility(View.VISIBLE);


            // Request for Products of given Category based on PageNo.
            RequestAllProducts(pageNo,postFilters);
        }

        try {
            // Initialize FilterDialog and Override its abstract methods
            filterDialog = new SearchDialog(getContext(), productFiltersList, productFilters, postFilters) {
                @Override
                public void clearFilters() {
                    // Clear Filters
                    isFilterApplied = false;
                    filterButton.setChecked(false);
                    removeFilterBtn.setVisibility(View.GONE);
                    postFilters = null;
                    productFilters = null;
                    productsList.clear();
                    pageNo = 1;
                    productAdapter.notifyDataSetChanged();
                    loadingProgress.setVisibility(View.VISIBLE);
                    new LoadMoreTask(pageNo, postFilters).execute();
                    RequestFilters(postFilters);
                }

                @Override
                public void applyFilters(PostFilters postFilterData) {
                    // Apply Filters
                    isFilterApplied = true;
                    filterButton.setChecked(true);
                    removeFilterBtn.setVisibility(View.VISIBLE);
                    postFilters = postFilterData;
                    productsList.clear();
                    pageNo = 1;
                    productAdapter.notifyDataSetChanged();
                    loadingProgress.setVisibility(View.VISIBLE);
                    new LoadMoreTask(pageNo, postFilters).execute();
                    RequestFilters(postFilters);
                }
            };
        }
        catch (Exception e){
            e.getCause();
        }

        // Request Server for Product Filters
        RequestFilters(postFilters);

        // Handle the Scroll event of Product's RecyclerView
        all_products_recycler.addOnScrollListener(new EndlessRecyclerViewScroll(bottomBar) {
            @Override
            public void onLoadMore(final int current_page) {

                progressBar.setVisibility(View.VISIBLE);

                if (isFilterApplied) {
                    // Initialize LoadMoreTask to Load More Products from Server against some Filters
                    loadMoreTask = new LoadMoreTask(current_page, postFilters);
                } else {
                    // Initialize LoadMoreTask to Load More Products from Server without Filters
                    loadMoreTask = new LoadMoreTask(current_page, null);
                }
                // Execute AsyncTask LoadMoreTask to Load More Products from Server
                loadMoreTask.execute();
            }
        });

        productAdapter.notifyDataSetChanged();


        // Toggle RecyclerView's LayoutManager
        toggleLayoutView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isGridView = isChecked;
                setRecyclerViewLayoutManager(isGridView);
            }
        });


        // Handle the Click event of Filter Button
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isFilterApplied) {
                    filterButton.setChecked(true);
                    filterDialog.show();

                } else {
                    filterButton.setChecked(false);
                    filterDialog.show();
                }
            }
        });


        sortList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get sortBy_array from String_Resources
                final String[] sortArray = getResources().getStringArray(R.array.sortBy_array);

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setCancelable(true);

                dialog.setItems(sortArray, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String selectedText = sortArray[which];
                        sortListText.setText(selectedText);


                        if (selectedText.equalsIgnoreCase(sortArray[0])) {
                            order = "asc";
                            sortBy = "date";
                        }
                        else if (selectedText.equalsIgnoreCase(sortArray[1])) {
                            order = "desc";
                            sortBy = "date";
                        }
                        if (selectedText.equalsIgnoreCase(sortArray[2])) {
                            order = "asc";
                            sortBy = "title";
                        }
                        else if (selectedText.equalsIgnoreCase(sortArray[3])) {
                            order = "desc";
                            sortBy = "title";
                        }
                        productsList.clear();
                        pageNo = 1;
                        productAdapter.notifyDataSetChanged();
                        loadingProgress.setVisibility(View.VISIBLE);
                        CLEAR_LIST = true;
                        if(isFilterApplied){
                            // Initialize LoadMoreTask to Load More Products from Server against some Filters
                            RequestAllProducts(pageNo, postFilters);
                        }
                        else {
                            postFilters=new PostFilters();

                            // Request for Products of given Category based on PageNo.
                            RequestAllProducts(pageNo,postFilters);
                            // Initialize LoadMoreTask to Load More Products from Server without Filters
                            //RequestAllProducts(pageNo);
                        }
                        dialog.dismiss();


                        // Handle the Scroll event of Product's RecyclerView
                        all_products_recycler.addOnScrollListener(new EndlessRecyclerViewScroll(bottomBar) {
                            @Override
                            public void onLoadMore(int current_page) {

                                progressBar.setVisibility(View.VISIBLE);

                                // Execute AsyncTask LoadMoreTask to Load More Products from Server
                                loadMoreTask = new LoadMoreTask(current_page, postFilters);
                                loadMoreTask.execute();
                            }
                        });

                    }
                });
                dialog.show();
            }
        });


        removeFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFilterApplied = false;
                filterButton.setChecked(false);
                removeFilterBtn.setVisibility(View.GONE);
                postFilters = null;
                productFilters = null;
                productsList.clear();
                CLEAR_LIST = true;
                pageNo = 1;
                productAdapter.notifyDataSetChanged();
                loadingProgress.setVisibility(View.VISIBLE);
                new LoadMoreTask(pageNo, postFilters).execute();
                RequestFilters(postFilters);

            }
        });

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.actionSearch));


        // Binding Layout Views
        banner_adView = (FrameLayout) rootView.findViewById(R.id.banner_adView);
        search_editText = (SearchView) rootView.findViewById(R.id.search_editText);

        banner_adView.setVisibility(View.GONE);

        if (ConstantValues.IS_ADMOBE_ENABLED) {
            // Initialize Admobe
            mAdView = new AdView(getContext());
            mAdView.setAdSize(AdSize.BANNER);
            mAdView.setAdUnitId(ConstantValues.AD_UNIT_ID_BANNER);
            AdRequest adRequest = new AdRequest.Builder().build();
            banner_adView.addView(mAdView);
            mAdView.loadAd(adRequest);
            banner_adView.setVisibility(View.GONE);
        }


        subCategoriesList = new ArrayList<>();

        // Get All CategoriesList from ApplicationContext
        allCategoriesList = ((App) getContext().getApplicationContext()).getCategoriesList();

        for (int i=0;  i<allCategoriesList.size();  i++) {
            if (allCategoriesList.get(i).getId() != 0) {
                subCategoriesList.add(allCategoriesList.get(i));
            }
        }


        // Show Categories

        search_editText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
               // search_editText.clearFocus();
                search = s;
                isFilterApplied = true;
                CLEAR_LIST = true;
                pageNo = 1;
                RequestAllProducts(pageNo, postFilters);
                FLAG_SEARCHED = true;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                if(s.isEmpty()){
                    FLAG_SEARCHED = false;
                    search = "";
                    pageNo = 1;
                    CLEAR_LIST = true;
                    RequestAllProducts(pageNo, postFilters);
                }
                else if (!s.isEmpty() && s.length() > 2) {
                    CLEAR_LIST = true;
                    pageNo = 1;
                    if(FLAG_SEARCHED) {
                        RequestAllProducts(pageNo, postFilters);
                    }
                    //return true;
                }

                return false;
            }
        });
        return rootView;
    }

    //*********** Request Search Results from the Server based on the given Query ********//

    private class LoadMoreTask extends AsyncTask<String, Void, String> {

        int page_number;
        PostFilters filters;

        private LoadMoreTask(int page_number, PostFilters filters) {
            this.page_number = page_number;
            this.filters = filters;
        }

        //*********** Runs on the UI thread before #doInBackground() ********//

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //*********** Performs some Processes on Background Thread and Returns a specified Result  ********//

        @Override
        protected String doInBackground(String... params) {

            // Check if any of the Filter is applied
            if (isFilterApplied) {
                RequestAllProducts(page_number, postFilters);
            }
            else {

                postFilters=new PostFilters();
                RequestAllProducts(page_number,postFilters);
            }

            return "All Done!";
        }

        //*********** Runs on the UI thread after #doInBackground() ********//

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    public void RequestAllProducts(final int pageNumber,PostFilters filters) {

        emptyRecord.setVisibility(View.GONE);

        Map<String, String> params = new LinkedHashMap<>();
        params.put("page", String.valueOf(pageNumber));
        params.put("order", order);
        params.put("orderby", sortBy);
        params.put("search", search);
        params.put("lang",ConstantValues.LANGUAGE_CODE);


        if (filters.getSelectedAttributes().size() > 0) {


            for (int i=0;  i<filters.getSelectedAttributes().size();  i++) {

                String attributeSlug = filters.getSelectedAttributes().get(i).getAttributeSlug();
                String attributeTerms = "";

                if (filters.getSelectedAttributes().get(i).getAttributeTerms().size() > 0) {

                    String[] attrTermsArray = new String[filters.getSelectedAttributes().get(i).getAttributeTerms().size()];

                    for (int j=0;  j<filters.getSelectedAttributes().get(i).getAttributeTerms().size();  j++) {
                        attrTermsArray[j] = filters.getSelectedAttributes().get(i).getAttributeTerms().get(j).getSlug();
                    }

                    attributeTerms = TextUtils.join(",", attrTermsArray);
                }


                if (!attributeTerms.equalsIgnoreCase("")) {

                    if(attributeSlug.contains("pa_"))
                    {
                        params.put("attribute",attributeSlug);
                        params.put("attribute_term",attributeTerms);
                    }else {
                        params.put(attributeSlug, ""+attributeTerms);
                    }

                }
            }
        }

        Call<List<ProductDetails>> call = APIClient.getInstance()
                .getAllProducts ( params );

        call.enqueue(new Callback<List<ProductDetails>>() {
            @Override
            public void onResponse(Call<List<ProductDetails>> call, retrofit2.Response<List<ProductDetails>> response) {

                // Hide the ProgressBar
                progressBar.setVisibility(View.GONE);
                loadingProgress.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    addProducts(response.body());
                } else {
                    Converter<ResponseBody, ErrorResponse> converter = APIClient.retrofit.responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                    ErrorResponse error;
                    try {
                        error = converter.convert(response.errorBody());
                    } catch (IOException e) {
                        error = new ErrorResponse();
                    }

                    Toast.makeText(App.getContext(), "Error : "+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProductDetails>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                loadingProgress.setVisibility(View.GONE);
                Toast.makeText(App.getContext(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addProducts(List<ProductDetails> products) {

        // Add Products to ProductsList from the List of ProductData
        //productsList.clear();
        if(CLEAR_LIST){
            productsList.clear();
            CLEAR_LIST = false;
        }
        if (products.size() > 0) {
            productsList.addAll(products);

            for (int i=0;  i<products.size();  i++) {
                if (products.get(i).getStatus() != null  &&  !"publish".equalsIgnoreCase(products.get(i).getStatus())) {
                    for (int j=0;  j<productsList.size();  j++) {
                        if (products.get(i).getId() == productsList.get(j).getId()) {
                            productsList.remove(j);
                        }
                    }
                }
            }
        }

        productAdapter.notifyDataSetChanged();


        // Change the Visibility of emptyRecord Text based on ProductList's Size
        if (productAdapter.getItemCount() == 0) {
            emptyRecord.setVisibility(View.VISIBLE);
        }
        else {
            emptyRecord.setVisibility(View.GONE);
        }

    }


    public void setRecyclerViewLayoutManager(Boolean isGridView) {
        int scrollPosition = 0;

        // If a LayoutManager has already been set, get current Scroll Position
        if (all_products_recycler.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) all_products_recycler.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }

        productAdapter.toggleLayout(isGridView);

        all_products_recycler.setLayoutManager(isGridView ? gridLayoutManager : linearLayoutManager);
        all_products_recycler.setAdapter(productAdapter);

        all_products_recycler.scrollToPosition(scrollPosition);
    }
    public void RequestFilters(final PostFilters postFilters) {

        Map<String, String> params = new LinkedHashMap<>();
        params.put("insecure", "cool");
        params.put("lang",ConstantValues.LANGUAGE_CODE);

        if (postFilters != null) {
            productFilters=new ProductFilters();


        }
        updateFilters(productFilters, postFilters);
    }

    public void updateFilters(final ProductFilters updatedProductFilters, final PostFilters updatedPostFilters) {
        CLEAR_LIST = true;
        attributeList = ((App) getContext().getApplicationContext()).getAttributeList();
        allCategoriesList = ((App) getContext().getApplicationContext()).getCategoriesList();

        productFiltersList.clear();

        FilterDetails  categoryFilter=new FilterDetails();
        categoryFilter.setAttributeName("category");
        categoryFilter.setAttributeSlug("category");
        List<FilterTerm> categoryAllTerms=new ArrayList<>();
        for(CategoryDetails categoryDetails:((App) getContext().getApplicationContext()).getCategoriesList())
        {
            FilterTerm filterTerm=new FilterTerm();
            filterTerm.setTermId(categoryDetails.getId());
            filterTerm.setName(categoryDetails.getName());
            filterTerm.setSlug(""+categoryDetails.getId());
            filterTerm.setTaxonomy("category");
            categoryAllTerms.add(filterTerm);
        }

        categoryFilter.setAttributeTerms(categoryAllTerms);

        productFiltersList.add(categoryFilter);

        for(AttributeDetails attributeDetails:attributeList)
        {
            FilterDetails  attributeFilter=new FilterDetails();
            attributeFilter.setAttributeName(attributeDetails.getName());
            attributeFilter.setAttributeSlug(attributeDetails.getSlug());


            List<FilterTerm> attributeTerms=new ArrayList<>();

            for(TermDetails tagDetails:attributeDetails.getTermListe())
            {
                FilterTerm filterTerm=new FilterTerm();
                filterTerm.setTermId(tagDetails.getId());
                filterTerm.setName(tagDetails.getName());
                filterTerm.setSlug(""+tagDetails.getId());
                filterTerm.setTaxonomy(attributeDetails.getSlug());


                attributeTerms.add(filterTerm);
            }

            attributeFilter.setAttributeTerms(attributeTerms);
            productFiltersList.add(attributeFilter);
        }

        try {

            filterDialog = new SearchDialog(getContext(), productFiltersList, updatedProductFilters, updatedPostFilters) {
                @Override
                public void clearFilters() {
                    // Clear Filters
                    isFilterApplied = false;
                    filterButton.setChecked(false);
                    removeFilterBtn.setVisibility(View.GONE);
                    postFilters = null;
                    CLEAR_LIST = true;
                    pageNo =1;
                    productFilters = null;
                    productsList.clear();
                    productAdapter.notifyDataSetChanged();
                    loadingProgress.setVisibility(View.VISIBLE);
                    new LoadMoreTask(pageNo, postFilters).execute();
                    RequestFilters(postFilters);
                }

                @Override
                public void applyFilters(PostFilters postFilterData) {
                    // Apply Filters
                    isFilterApplied = true;
                    filterButton.setChecked(true);
                    removeFilterBtn.setVisibility(View.VISIBLE);
                    postFilters = postFilterData;
                    CLEAR_LIST = true;
                    pageNo =1;
                    productsList.clear();
                    productAdapter.notifyDataSetChanged();
                    loadingProgress.setVisibility(View.VISIBLE);
                    new LoadMoreTask(pageNo, postFilters).execute();
                    RequestFilters(postFilters);
                }
            };
        }
        catch (Exception e){
            e.getCause();
        }
    }

}



