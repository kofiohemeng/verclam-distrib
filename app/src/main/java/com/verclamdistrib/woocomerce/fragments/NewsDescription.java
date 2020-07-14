package com.verclamdistrib.woocomerce.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.verclamdistrib.woocomerce.R;

import com.verclamdistrib.woocomerce.activities.MainActivity;
import com.verclamdistrib.woocomerce.customs.DialogLoader;


public class NewsDescription extends Fragment {

    View rootView;
    
    int postID;
    
    //ImageView news_cover;
 //   TextView news_title, news_date;
    WebView news_description_webView;
    String linkUrl;
    DialogLoader dialogLoader;

    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.news_description, container, false);
        

        // Get NewsDetails from bundle arguments
        postID = getArguments().getInt("postID");
    
        linkUrl = getArguments().getString("link_url");
        // Enable Drawer Indicator with static variable actionBarDrawerToggle of MainActivity
        MainActivity.actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.news_description));


        // Binding Layout Views
//        news_cover = (ImageView) rootView.findViewById(R.id.news_cover);
//        news_title = (TextView) rootView.findViewById(R.id.news_title);
//        news_date = (TextView) rootView.findViewById(R.id.news_date);
        news_description_webView = (WebView) rootView.findViewById(R.id.news_description_webView);
        progressBar =  rootView.findViewById(R.id.progress_loader);
        
        dialogLoader = new DialogLoader(getContext());
        
//        Glide
//            .with(getContext())
//            .load(R.drawable.placeholder)
//            .placeholder(R.drawable.placeholder)
//            .error(R.drawable.placeholder)
//            .into(news_cover);
        
        
       // RequestPostDetails(postID);
        

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadLink();

    }
    private void loadLink(){
        news_description_webView.getSettings().setJavaScriptEnabled(true); // enable javascript

        news_description_webView.setWebViewClient(new WebViewController());
        news_description_webView.loadUrl(linkUrl);

    }

    public class WebViewController extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
           // dialogLoader.showProgressDialog();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //dialogLoader.hideProgressDialog();
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Toast.makeText(getActivity(),"Error in loading post",Toast.LENGTH_SHORT).show();
         //   dialogLoader.hideProgressDialog();
            progressBar.setVisibility(View.GONE);
        }
    }


    //*********** Set Post Details to Views ********//
    
 //   private void setPostDetails(PostDetails postDetails) {
    
        // Set News Details
//        news_title.setText(postDetails.getTitle().getRendered());
//        news_date.setText(String.valueOf(postDetails.getDate()));
//
//
//        String description = postDetails.getContent().getRendered();
//        String styleSheet = "<style> " +
//                                "body{background:#eeeeee; margin:0; padding:0} " +
//                                "p{color:#666666;} " +
//                                "img{display:inline; height:auto; max-width:100%;}" +
//                            "</style>";
//
//        news_description_webView.setHorizontalScrollBarEnabled(false);
//        news_description_webView.loadDataWithBaseURL(null, styleSheet+description, "text/html", "utf-8", null);
//
//    }
    
    
    
    //*********** Request Post Details from the Server based on postID ********//
    
//    public void RequestPostDetails(final int postID) {
        
//        dialogLoader.showProgressDialog();
//
//        Call<PostDetails> call = APIClient.getInstance()
//                .getSinglePost
//                        (
//                                String.valueOf(postID),
//                                ConstantValues.LANGUAGE_CODE
//                        );
//
//        call.enqueue(new Callback<PostDetails>() {
//            @Override
//            public void onResponse(Call<PostDetails> call, retrofit2.Response<PostDetails> response) {
//
//                dialogLoader.hideProgressDialog();
//
//                if (response.isSuccessful()) {
//
//                    setPostDetails(response.body());
//
//                    RequestPostMedia(response.body().getFeaturedMedia());
//                }
//                else {
//                    Converter<ResponseBody, ErrorResponse> converter = APIClient.retrofit.responseBodyConverter(ErrorResponse.class, new Annotation[0]);
//                    ErrorResponse error;
//                    try {
//                        error = converter.convert(response.errorBody());
//                    } catch (IOException e) {
//                        error = new ErrorResponse();
//                    }
//
//                    Toast.makeText(getContext(), "Error : "+error.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PostDetails> call, Throwable t) {
//                dialogLoader.hideProgressDialog();
//                Toast.makeText(getContext(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
//            }
//        });
        
//    }
    
    
    
    //*********** Request Post Media from the Server based on MediaID ********//
//
//    public void RequestPostMedia(final int media_id) {
//
//        Call<PostMedia> call = APIClient.getInstance()
//                .getPostMedia
//                        (
//                                String.valueOf(media_id),
//                                ConstantValues.LANGUAGE_CODE
//                        );
//
//        call.enqueue(new Callback<PostMedia>() {
//            @Override
//            public void onResponse(Call<PostMedia> call, retrofit2.Response<PostMedia> response) {
//
//                if (response.isSuccessful()) {
//
//                    PostMedia postMedia = response.body();
//
//                    Glide
//                        .with(getContext())
//                        .load(postMedia.getSourceUrl()).asBitmap()
//                        .placeholder(R.drawable.placeholder)
//                        .error(R.drawable.placeholder)
//                        .into(news_cover);
//
//                }
//                else {
//                    Converter<ResponseBody, ErrorResponse> converter = APIClient.retrofit.responseBodyConverter(ErrorResponse.class, new Annotation[0]);
//                    ErrorResponse error;
//                    try {
//                        error = converter.convert(response.errorBody());
//                    } catch (IOException e) {
//                        error = new ErrorResponse();
//                    }
//
//                    Toast.makeText(getContext(), "Error : "+error.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PostMedia> call, Throwable t) {
//                Toast.makeText(getContext(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
//            }
//        });
//    }

}

