package com.verclamdistrib.woocomerce.adapters;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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

import com.verclamdistrib.woocomerce.fragments.NewsDescription;
import com.verclamdistrib.woocomerce.models.post_model.PostDetails;


/**
 * NewsListAdapter is the adapter class of RecyclerView holding List of News in News_All
 **/

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.MyViewHolder> {

    Context context;
    private List<PostDetails> postsList;


    public NewsListAdapter(Context context, List<PostDetails> postsList) {
        this.context = context;
        this.postsList = postsList;
    }



    //********** Called to Inflate a Layout from XML and then return the Holder *********//

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        // Inflate the custom layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_news_all, parent, false);

        return new MyViewHolder(itemView);
    }



    //********** Called by RecyclerView to display the Data at the specified Position *********//

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        
        // Get the data model based on Position
        final PostDetails postDetails = postsList.get(position);
    
        holder.news_title.setText(postDetails.getTitle().getRendered());
        holder.news_date.setText(postDetails.getDate().replaceAll("[a-zA-Z]", " "));
        holder.news_description.setText(stripHtml(postDetails.getContent().getRendered()));
    
        
        if (postDetails.getEmbedded() != null  &&  postDetails.getEmbedded().getWpFeaturedmedia() != null) {
            // Set News Image on ImageView with Glide Library
            Glide
                .with(context)
                .load(postDetails.getEmbedded().getWpFeaturedmedia().get(0).getSourceUrl())
                .error(R.drawable.placeholder)
                .into(holder.news_image);
        }
        else {
            // Set News Image on ImageView with Glide Library
            Glide
                .with(context)
                .load(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.news_image);
        }
        
    }



    //********** Returns the total number of items in the data set *********//

    @Override
    public int getItemCount() {
        return postsList.size();
    }
    
    
    
    //********** Returns displayable plain string from the provided HTML string *********//
    
    @SuppressWarnings("deprecation")
    private String stripHtml(String html) {
        
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString();
        }
        else {
            return Html.fromHtml(html).toString();
        }
    }



    /********** Custom ViewHolder provides a direct reference to each of the Views within a Data_Item *********/

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout news_item;
        ImageView news_image;
        TextView news_title, news_date, news_description;


        public MyViewHolder(final View itemView) {
            super(itemView);
            news_item = (RelativeLayout) itemView.findViewById(R.id.news_item);
            news_image = (ImageView) itemView.findViewById(R.id.news_image);
            news_title = (TextView) itemView.findViewById(R.id.news_title);
            news_date = (TextView) itemView.findViewById(R.id.news_date);
            news_description = (TextView) itemView.findViewById(R.id.news_description);

            news_item.setOnClickListener(this);
        }


        // Handle Click Listener on News item
        @Override
        public void onClick(View view) {
            // Get News Info
            Bundle newsInfo = new Bundle();
            newsInfo.putInt("postID", postsList.get(getAdapterPosition()).getId());
            newsInfo.putString("link_url",postsList.get(getAdapterPosition()).getLink());
            Fragment newsDescription = new NewsDescription();
            newsDescription.setArguments(newsInfo);
            FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, newsDescription)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null).commit();
        }
    }

}

