package com.verclamdistrib.woocomerce.fragments;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Transformers.BaseTransformer;

import com.verclamdistrib.woocomerce.activities.MainActivity;
import com.verclamdistrib.woocomerce.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.verclamdistrib.woocomerce.app.App;
import com.verclamdistrib.woocomerce.constant.ConstantValues;
import com.verclamdistrib.woocomerce.models.banner_model.BannerDetails;
import com.verclamdistrib.woocomerce.models.category_model.CategoryDetails;


public class HomePage_4 extends Fragment implements BaseSliderView.OnSliderClickListener {

    SliderLayout sliderLayout;
    PagerIndicator pagerIndicator;

    List<BannerDetails> bannerImages = new ArrayList<>();
    List<CategoryDetails> allCategoriesList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.homepage_4, container, false);

        // Enable Drawer Indicator with static variable actionBarDrawerToggle of MainActivity
        MainActivity.actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(ConstantValues.APP_HEADER);

        // Get BannersList from ApplicationContext
        bannerImages = ((App) getContext().getApplicationContext()).getBannersList();


        // Binding Layout Views
        sliderLayout = (SliderLayout) rootView.findViewById(R.id.banner_slider);
        pagerIndicator = (PagerIndicator) rootView.findViewById(R.id.banner_slider_indicator);


        // Setup BannerSlider
        setupBannerSlider(bannerImages);


        // Initialize new Bundle for Fragment arguments
        Bundle bundle = new Bundle();
        bundle.putBoolean("isHeaderVisible", true);
        bundle.putBoolean("isMenuItem", false);

        // Get FragmentManager
        FragmentManager fragmentManager = getFragmentManager();


        // Add MainCategories Fragment to specified FrameLayout
        Fragment categories = new Categories_1();
        categories.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.all_categories_fragment, categories).commit();

        // Add ProductsFeatured Fragment to specified FrameLayout
        Fragment topSeller = new ProductsFeatured();
        topSeller.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.top_seller_fragment, topSeller).commit();

        // Add ProductsOnSale Fragment to specified FrameLayout
        Fragment specialDeals = new ProductsOnSale();
        specialDeals.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.super_deals_fragment, specialDeals).commit();

        // Add ProductsNewest Fragment to specified FrameLayout
        Fragment mostLiked = new ProductsNewest();
        mostLiked.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.most_liked_fragment, mostLiked).commit();


        return rootView;

    }
    
    
    
    //*********** Setup the BannerSlider with the given List of BannerImages ********//
    
    private void setupBannerSlider(final List<BannerDetails> bannerImages) {
        
        // Initialize new LinkedHashMap<ImageName, ImagePath>
        final LinkedHashMap<String, String> slider_covers = new LinkedHashMap<>();
        
        
        for (int i=0;  i< bannerImages.size();  i++) {
            // Get bannerDetails at given Position from bannerImages List
            BannerDetails bannerDetails =  bannerImages.get(i);
            
            // Put Image's Name and URL to the HashMap slider_covers
            slider_covers.put
                    (
                            "Image"+bannerDetails.getBannersTitle(),
                            bannerDetails.getBannersImage()
                    );
        }
        
        
        for(String name : slider_covers.keySet()) {
            // Initialize DefaultSliderView
            final DefaultSliderView defaultSliderView = new DefaultSliderView(getContext());
            
            // Set Attributes(Name, Image, Type etc) to DefaultSliderView
            defaultSliderView
                    .description(name)
                    .image(slider_covers.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            
            
            // Add DefaultSliderView to the SliderLayout
            sliderLayout.addSlider(defaultSliderView);
        }
        
        // Set PresetTransformer type of the SliderLayout
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        
        
        // Check if the size of Images in the Slider is less than 2
        if (slider_covers.size() < 2) {
            // Disable PagerTransformer
            sliderLayout.setPagerTransformer(false, new BaseTransformer() {
                @Override
                protected void onTransform(View view, float v) {
                }
            });
            
            // Hide Slider PagerIndicator
            sliderLayout.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
            
        } else {
            // Set custom PagerIndicator to the SliderLayout
            sliderLayout.setCustomIndicator(pagerIndicator);
            // Make PagerIndicator Visible
            sliderLayout.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Visible);
        }
        
    }
    
    
    
    //*********** Handle the Click Listener on BannerImages of Slider ********//
    
    @Override
    public void onSliderClick(BaseSliderView slider) {
        
        int position = sliderLayout.getCurrentPosition();
        String url = bannerImages.get(position).getBannersUrl();
        String type = bannerImages.get(position).getType();
        
        if (type.equalsIgnoreCase("product")) {
            if (!url.isEmpty()) {
                // Get Product Info
                Bundle bundle = new Bundle();
                bundle.putInt("itemID", Integer.parseInt(url));
                
                // Navigate to Product_Description of selected Product
                Fragment fragment = new Product_Description();
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(getString(R.string.actionHome)).commit();
            }
            
        }
        else if (type.equalsIgnoreCase("category")) {
            if (!url.isEmpty()) {
                int categoryID = 0;
                String categoryName = "";
                
                for (int i=0;  i<allCategoriesList.size();  i++) {
                    if (url.equalsIgnoreCase(String.valueOf(allCategoriesList.get(i).getId()))) {
                        categoryName = allCategoriesList.get(i).getName();
                        categoryID = allCategoriesList.get(i).getId();
                    }
                }
                
                // Get Category Info
                Bundle bundle = new Bundle();
                bundle.putInt("CategoryID", categoryID);
                bundle.putString("CategoryName", categoryName);
                
                // Navigate to Products Fragment
                Fragment fragment = new Products();
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(getString(R.string.actionHome)).commit();
            }
            
        }
        else if (type.equalsIgnoreCase("on_sale")) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("on_sale", true);
            bundle.putBoolean("isMenuItem", true);
            
            // Navigate to Products Fragment
            Fragment fragment = new Products();
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(getString(R.string.actionHome)).commit();
            
        }
        else if (type.equalsIgnoreCase("featured")) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("featured", true);
            bundle.putBoolean("isMenuItem", true);
            
            // Navigate to Products Fragment
            Fragment fragment = new Products();
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(getString(R.string.actionHome)).commit();
            
        }
        else if (type.equalsIgnoreCase("latest")) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isMenuItem", true);
            
            // Navigate to Products Fragment
            Fragment fragment = new Products();
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(getString(R.string.actionHome)).commit();
            
        }
        
    }
    
}
