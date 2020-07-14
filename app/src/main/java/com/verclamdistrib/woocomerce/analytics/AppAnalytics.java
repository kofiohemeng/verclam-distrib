package com.verclamdistrib.woocomerce.analytics;

import android.content.Context;
import android.os.Bundle;

//import com.google.android.gms.analytics.HitBuilders;
//import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;

public class AppAnalytics {

    private static final String PRODUCT_VIEW = "product_view";

//    public static void addScreenDisplay(App application,String screenName){
//
//        Tracker tracker = application.getDefaultTracker();
//        tracker.setScreenName("Screen : " + screenName);
//        tracker.send(new HitBuilders.ScreenViewBuilder().build());
//    }
//
//    public static void addScreenEvent(App application,String category,String action){
//
//        Tracker tracker = application.getDefaultTracker();
//        tracker.send(new HitBuilders.EventBuilder()
//                .setCategory(category)
//                .setAction(action)
//                .build());
//    }
//    public static void addProductVisit(App application,String category,String productName) {
//        Tracker tracker = application.getDefaultTracker();
//        tracker.send(new HitBuilders.EventBuilder()
//                .setCategory(category)
//                .setAction(PRODUCT_VIEW)
//                .setLabel(productName)
//                .setValue(1)
//                .build());
//    }
//
public static void addProductVisit(Context context, String category, String productName) {
    FirebaseAnalytics firebaseAnalytics =  FirebaseAnalytics.getInstance(context);

    Bundle bundle = new Bundle();
    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Product name : "+productName);
    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, category);
    firebaseAnalytics.logEvent(PRODUCT_VIEW, bundle);
}
}
