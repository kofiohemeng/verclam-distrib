package com.verclamdistrib.woocomerce.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.support.design.widget.Snackbar;

import com.verclamdistrib.woocomerce.app.App;
import com.verclamdistrib.woocomerce.app.MyAppPrefsManager;
import com.verclamdistrib.woocomerce.models.device_model.AppSettingsDetails;

import com.verclamdistrib.woocomerce.R;

import com.verclamdistrib.woocomerce.utils.Utilities;
import com.verclamdistrib.woocomerce.constant.ConstantValues;
import com.verclamdistrib.woocomerce.network.StartAppRequests;

import java.util.Locale;


/**
 * SplashScreen activity, appears on App Startup
 **/

public class SplashScreen extends Activity {
    
    View rootView;
    ProgressBar progressBar;
    
    MyTask myTask;
    StartAppRequests startAppRequests;
    MyAppPrefsManager myAppPrefsManager;
    private String responseResult ;
    public final static int ACCESS_LOCATION_PERMISSION = 100;

    Locale myLocale;

    String postType = "";
    String postID = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);

        if(getIntent().getExtras() != null) {
            postType = getIntent().getExtras().getString("postType");
            postID = getIntent().getExtras().getString("postID");
        }

        Locale locale = new Locale("fr");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        Log.i("VC_Shop", "AndroidWoocommerce_Version= "+ConstantValues.CODE_VERSION);

        //Toast.makeText(this, "skip", Toast.LENGTH_SHORT).show();

        // Bind Layout Views
        progressBar = (ProgressBar) findViewById(R.id.splash_loadingBar);
        rootView = progressBar;

        //android.os.Debug.waitForDebugger();

        // Initializing StartAppRequests and PreferencesManager
        startAppRequests = new StartAppRequests(this);
        myAppPrefsManager = new MyAppPrefsManager(this);


        ConstantValues.LANGUAGE_CODE = myAppPrefsManager.getUserLanguageCode();

        ConstantValues.IS_USER_LOGGED_IN = myAppPrefsManager.isUserLoggedIn();
        ConstantValues.IS_PUSH_NOTIFICATIONS_ENABLED = myAppPrefsManager.isPushNotificationsEnabled();
        ConstantValues.IS_LOCAL_NOTIFICATIONS_ENABLED = myAppPrefsManager.isLocalNotificationsEnabled();


        // Start MyTask after 3 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                myTask = new MyTask();
                myTask.execute();
            }
        }, 0);
        
    }

    
    //*********** Sets App configuration ********//
    
    private void setAppConfig() {
        
        AppSettingsDetails appSettings = ((App) getApplicationContext()).getAppSettingsDetails();
        
        if (appSettings != null) {
            
            if (appSettings.getAppName() != null  &&  !TextUtils.isEmpty(appSettings.getAppName())) {
                ConstantValues.APP_HEADER = appSettings.getAppName();
            } else {
                ConstantValues.APP_HEADER = getString(R.string.app_name);
            }

            if (appSettings.getCurrencySymbol() != null  &&  !TextUtils.isEmpty(appSettings.getCurrencySymbol())) {
                ConstantValues.CURRENCY_SYMBOL = appSettings.getCurrencySymbol();
            } else {
                ConstantValues.CURRENCY_SYMBOL = "$";
            }
            
            
            if (appSettings.getFilterMaxPrice() != null  &&  !TextUtils.isEmpty(appSettings.getFilterMaxPrice())) {
                ConstantValues.FILTER_MAX_PRICE = appSettings.getFilterMaxPrice();
            } else {
                ConstantValues.FILTER_MAX_PRICE = "10000";
            }
            
            
            if (appSettings.getNewProductDuration() != null  &&  !TextUtils.isEmpty(appSettings.getNewProductDuration())) {
                ConstantValues.NEW_PRODUCT_DURATION = Long.parseLong(appSettings.getNewProductDuration());
            } else {
                ConstantValues.NEW_PRODUCT_DURATION = 30;
            }
            
            
            ConstantValues.DEFAULT_HOME_STYLE = getString(R.string.actionHome) +" "+ appSettings.getHomeStyle();
            ConstantValues.DEFAULT_CATEGORY_STYLE = getString(R.string.actionCategory) +" "+ appSettings.getCategoryStyle();
            
            ConstantValues.IS_GUEST_CHECKOUT_ENABLED = ("yes".equalsIgnoreCase(appSettings.getGuestCheckout()));
            //ConstantValues.IS_ONE_PAGE_CHECKOUT_ENABLED = ("1".equalsIgnoreCase(appSettings.getOnePageCheckout()));
            ConstantValues.IS_ONE_PAGE_CHECKOUT_ENABLED = true;
            
            ConstantValues.IS_GOOGLE_LOGIN_ENABLED = ("1".equalsIgnoreCase(appSettings.getGoogleLogin()));
            ConstantValues.IS_FACEBOOK_LOGIN_ENABLED = ("1".equalsIgnoreCase(appSettings.getFacebookLogin()));
            ConstantValues.IS_ADD_TO_CART_BUTTON_ENABLED = ("1".equalsIgnoreCase(appSettings.getCartButton()));
            
            ConstantValues.IS_ADMOBE_ENABLED = ("1".equalsIgnoreCase(appSettings.getAdmob()));
            ConstantValues.ADMOBE_ID = appSettings.getAdmobId();
            ConstantValues.AD_UNIT_ID_BANNER = appSettings.getAdUnitIdBanner();
            ConstantValues.AD_UNIT_ID_INTERSTITIAL = appSettings.getAdUnitIdInterstitial();
            
            ConstantValues.ABOUT_US = appSettings.getAboutPage();
            ConstantValues.TERMS_SERVICES = appSettings.getTermsPage();
            ConstantValues.PRIVACY_POLICY = appSettings.getPrivacyPage();
            ConstantValues.REFUND_POLICY = appSettings.getRefundPage();
            
            myAppPrefsManager.setLocalNotificationsTitle(appSettings.getNotificationTitle());
            myAppPrefsManager.setLocalNotificationsDuration(appSettings.getNotificationDuration());
            myAppPrefsManager.setLocalNotificationsDescription(appSettings.getNotificationText());
            
        }
        else {
            ConstantValues.APP_HEADER = getString(R.string.app_name);
            
            ConstantValues.CURRENCY_SYMBOL = "$";
            ConstantValues.FILTER_MAX_PRICE = "10000";
            ConstantValues.NEW_PRODUCT_DURATION = 30;
            
            ConstantValues.IS_GUEST_CHECKOUT_ENABLED =  false;
            ConstantValues.IS_ONE_PAGE_CHECKOUT_ENABLED = true;
            
            ConstantValues.IS_ADMOBE_ENABLED = false;
            ConstantValues.IS_GOOGLE_LOGIN_ENABLED = false;
            ConstantValues.IS_FACEBOOK_LOGIN_ENABLED = false;
            ConstantValues.IS_ADD_TO_CART_BUTTON_ENABLED = true;
            
            ConstantValues.DEFAULT_HOME_STYLE = getString(R.string.actionHome) +" "+ 1;
            ConstantValues.DEFAULT_CATEGORY_STYLE = getString(R.string.actionCategory) +" "+ 1;
            
            ConstantValues.ABOUT_US = getString(R.string.lorem_ipsum);
            ConstantValues.TERMS_SERVICES = getString(R.string.lorem_ipsum);
            ConstantValues.PRIVACY_POLICY = getString(R.string.lorem_ipsum);
            ConstantValues.REFUND_POLICY = getString(R.string.lorem_ipsum);
        }
        
    }
    
    
    
    /************* MyTask is Inner Class, that handles StartAppRequests on Background Thread *************/
    
    private class MyTask extends AsyncTask<String, Void, String> {
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        
        @Override
        protected String doInBackground(String... params) {
            
            // Check for Internet Connection from the static method of Helper class
            if (Utilities.isNetworkAvailable(SplashScreen.this)) {
                
                // Call the method of StartAppRequests class to process App Startup Requests
                startAppRequests.StartRequests();
                
                return "1";
            } else {
                return "0";
            }
        }
        
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            responseResult = result;
            checkLocationPermission();
            progressBar.setVisibility(View.GONE);

        }
        
    }
    private void launchActiivity(String result){
        if (result.equalsIgnoreCase("0")) {



            // No Internet Connection
            Snackbar.make(rootView, getString(R.string.no_internet), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.retry), new View.OnClickListener() {

                        // Handle the Retry Button Click
                        @Override
                        public void onClick(View v) {

                            progressBar.setVisibility(View.VISIBLE);

                            // Restart MyTask after 3 seconds
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    myTask = new MyTask();
                                    myTask.execute();
                                }
                            }, 0);
                        }
                    })
                    .show();

        }
        else {
            setAppConfig();
            MyAppPrefsManager myAppPrefsManager = new MyAppPrefsManager(SplashScreen.this);
            ConstantValues.IS_USER_LOGGED_IN = myAppPrefsManager.isUserLoggedIn();

            if(ConstantValues.IS_USER_LOGGED_IN){// If this user was registered already.
                // Navigate to MainActivity

                if(postType != null && postType != "") {
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    intent.putExtra("postType",postType);
                    intent.putExtra("postID",postID);
                    startActivity(intent);
                }
                else {
                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                }

                finish();
            }else {// if This user is new
                // Navigate to Login Screen
                startActivity(new Intent(getBaseContext(), Login.class));
                finish();
            }
        }
    }

    private void checkLocationPermission(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            // Permission has already been granted
            launchActiivity(responseResult);
        }
        else {
            ActivityCompat.requestPermissions(SplashScreen.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    ACCESS_LOCATION_PERMISSION);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ACCESS_LOCATION_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    launchActiivity(responseResult);
                } else {
                    checkLocationPermission();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}


