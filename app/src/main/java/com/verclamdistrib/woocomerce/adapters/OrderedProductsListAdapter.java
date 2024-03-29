package com.verclamdistrib.woocomerce.adapters;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.verclamdistrib.woocomerce.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.verclamdistrib.woocomerce.activities.MainActivity;
import com.verclamdistrib.woocomerce.constant.ConstantValues;
import com.verclamdistrib.woocomerce.fragments.Product_Description;
import com.verclamdistrib.woocomerce.models.product_model.ProductAttributes;
import com.verclamdistrib.woocomerce.models.product_model.ProductDetails;
import com.verclamdistrib.woocomerce.models.product_model.ProductMetaData;


/**
 * OrderedProductsListAdapter is the adapter class of RecyclerView holding List of Ordered Products in Order_Details
 **/

public class OrderedProductsListAdapter extends RecyclerView.Adapter<OrderedProductsListAdapter.MyViewHolder> {

    private Context context;
    private List<ProductDetails> orderProductsList;
    
    private ProductAdditionalValuesAdapter metaDataAdapter;


    public OrderedProductsListAdapter(Context context, List<ProductDetails> orderProductsList) {
        this.context = context;
        this.orderProductsList = orderProductsList;
    }



    //********** Called to Inflate a Layout from XML and then return the Holder *********//

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        // Inflate the custom layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_checkout_items, parent, false);

        return new MyViewHolder(itemView);
    }



    //********** Called by RecyclerView to display the Data at the specified Position *********//

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // Get the data model based on Position
        final ProductDetails product = orderProductsList.get(position);

        // Set Ordered Product Image on ImageView with Glide Library
        Glide
            .with(context)
            .load(product.getImage())
            .into(holder.checkout_item_cover);
    
        
        holder.checkout_item_category.setVisibility(View.GONE);
    
        holder.checkout_item_title.setText(product.getName());
        holder.checkout_item_quantity.setText(String.valueOf(product.getCustomersBasketQuantity()));
        holder.checkout_item_price.setText(ConstantValues.CURRENCY_SYMBOL + new DecimalFormat("#0.00").format(Double.parseDouble(product.getPrice())));
        holder.checkout_item_price_final.setText(ConstantValues.CURRENCY_SYMBOL + new DecimalFormat("#0.00").format(Double.parseDouble(product.getProductsFinalPrice())));
        holder.checkout_item_price_total.setText(ConstantValues.CURRENCY_SYMBOL + new DecimalFormat("#0.00").format(Double.parseDouble(product.getTotalPrice())));
    
    
        List<ProductMetaData> productMetaDataList = new ArrayList<>();
        List<ProductAttributes> productAttributesList = new ArrayList<>();
    
        productAttributesList = product.getAttributes();
    
        for (int i=0;  i<productAttributesList.size();  i++) {
            ProductMetaData metaData = new ProductMetaData();
            metaData.setId(productAttributesList.get(i).getId());
            metaData.setKey(productAttributesList.get(i).getName());
            metaData.setValue(productAttributesList.get(i).getOption());
        
            productMetaDataList.add(metaData);
        }
    
        // Initialize the ProductAdditionalValuesAdapter for RecyclerView
        metaDataAdapter = new ProductAdditionalValuesAdapter(context, productMetaDataList);
    
        holder.attributes_recycler.setAdapter(metaDataAdapter);
        holder.attributes_recycler.setLayoutManager(new LinearLayoutManager(context));
    
        metaDataAdapter.notifyDataSetChanged();
        

    }



    //********** Returns the total number of items in the data set *********//

    @Override
    public int getItemCount() {
        return orderProductsList.size();
    }



    /********** Custom ViewHolder provides a direct reference to each of the Views within a Data_Item *********/

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LinearLayout checkout_item;
        private ImageView checkout_item_cover;
        private RecyclerView attributes_recycler;
        private TextView checkout_item_title, checkout_item_quantity, checkout_item_price, checkout_item_price_final, checkout_item_price_total, checkout_item_category;


        public MyViewHolder(final View itemView) {
            super(itemView);

            checkout_item = (LinearLayout) itemView.findViewById(R.id.checkout_item);
            checkout_item_category = (TextView) itemView.findViewById(R.id.checkout_item_category);
            checkout_item_cover = (ImageView) itemView.findViewById(R.id.checkout_item_cover);
            checkout_item_title = (TextView) itemView.findViewById(R.id.checkout_item_title);
            checkout_item_quantity = (TextView) itemView.findViewById(R.id.checkout_item_quantity);
            checkout_item_price = (TextView) itemView.findViewById(R.id.checkout_item_price);
            checkout_item_price_final = (TextView) itemView.findViewById(R.id.checkout_item_price_final);
            checkout_item_price_total = (TextView) itemView.findViewById(R.id.checkout_item_price_total);
    
            attributes_recycler = (RecyclerView) itemView.findViewById(R.id.order_item_attributes_recycler);


            checkout_item.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
    
            int product_id = orderProductsList.get(getAdapterPosition()).getSelectedVariationID() == 0 ?
                    orderProductsList.get(getAdapterPosition()).getId() : orderProductsList.get(getAdapterPosition()).getParentId();

            Bundle categoryInfo = new Bundle();
            categoryInfo.putInt("itemID", product_id);

            Fragment fragment = new Product_Description();
            fragment.setArguments(categoryInfo);
            FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null).commit();
        }


    }


}

