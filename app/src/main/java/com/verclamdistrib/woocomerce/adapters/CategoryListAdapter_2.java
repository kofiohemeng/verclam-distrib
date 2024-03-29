package com.verclamdistrib.woocomerce.adapters;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.verclamdistrib.woocomerce.activities.MainActivity;
import com.verclamdistrib.woocomerce.R;

import java.util.List;

import com.verclamdistrib.woocomerce.fragments.Products;
import com.verclamdistrib.woocomerce.fragments.SubCategories_2;
import com.verclamdistrib.woocomerce.models.category_model.CategoryDetails;


/**
 * CategoryListAdapter is the adapter class of RecyclerView holding List of Categories in MainCategories
 **/

public class CategoryListAdapter_2 extends RecyclerView.Adapter<CategoryListAdapter_2.MyViewHolder> {

    boolean isSubCategory;

    Context context;
    List<CategoryDetails> categoriesList;


    public CategoryListAdapter_2(Context context, List<CategoryDetails> categoriesList, boolean isSubCategory) {
        this.context = context;
        this.isSubCategory = isSubCategory;
        this.categoriesList = categoriesList;
    }



    //********** Called to Inflate a Layout from XML and then return the Holder *********//

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        // Inflate the custom layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_categories_2, parent, false);

        return new MyViewHolder(itemView);
    }



    //********** Called by RecyclerView to display the Data at the specified Position *********//

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // Get the data model based on Position
        final CategoryDetails categoryDetails = categoriesList.get(position);
    
        // Set Category Image on ImageView with Glide Library
        if (categoryDetails.getImage() != null  &&  categoryDetails.getImage().getSrc() != null) {
            Glide.with(context)
                    .load(categoryDetails.getImage().getSrc())
                    .error(R.drawable.placeholder)
                    .into(holder.category_image);
        }
        else {
            Glide.with(context)
                    .load(R.drawable.placeholder)
                    .into(holder.category_image);
        }


//        holder.category_title.setText(categoryDetails.getName());
//        holder.category_products.setText(categoryDetails.getCount() + " "+ context.getString(R.string.products));
        holder.category_title.setText("");
        holder.category_products.setText("");
    }



    //********** Returns the total number of items in the data set *********//

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }



    /********** Custom ViewHolder provides a direct reference to each of the Views within a Data_Item *********/

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    
        RelativeLayout category_card;
        ImageView category_image;
        TextView category_title, category_products;


        public MyViewHolder(final View itemView) {
            super(itemView);
            category_card = (RelativeLayout) itemView.findViewById(R.id.category_card);
            category_image = (ImageView) itemView.findViewById(R.id.category_image);
            category_title = (TextView) itemView.findViewById(R.id.category_title);
            category_products = (TextView) itemView.findViewById(R.id.category_products);

            category_card.setOnClickListener(this);
        }


        // Handle Click Listener on Category item
        @Override
        public void onClick(View view) {
            // Get Category Info
            Bundle categoryInfo = new Bundle();
            categoryInfo.putInt("CategoryID", categoriesList.get(getAdapterPosition()).getId());
            categoryInfo.putString("CategoryName", categoriesList.get(getAdapterPosition()).getName());

            Fragment fragment;
    
            if (isSubCategory) {
                categoryInfo.putInt("subCategoryID", categoriesList.get(getAdapterPosition()).getId());
                categoryInfo.putInt("CategoryID", categoriesList.get(getAdapterPosition()).getParent());
                // Navigate to Products Fragment
                fragment = new Products();
            }
            else if (categoriesList.get(getAdapterPosition()).getSub_categories() == 0) {
                // Navigate to Products Fragment
                fragment = new Products();
            }
            else {
                // Navigate to SubCategories Fragment
                fragment = new SubCategories_2();
            }

            fragment.setArguments(categoryInfo);
            FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null).commit();
        }
    }

}

