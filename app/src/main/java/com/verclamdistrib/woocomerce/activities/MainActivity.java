package com.verclamdistrib.woocomerce.activities;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.WrappedDrawable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.graphics.drawable.DrawableWrapper;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieSyncManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.MobileAds;
import com.verclamdistrib.woocomerce.R;
import com.verclamdistrib.woocomerce.adapters.DrawerExpandableListAdapter;
import com.verclamdistrib.woocomerce.app.App;
import com.verclamdistrib.woocomerce.app.MyAppPrefsManager;
import com.verclamdistrib.woocomerce.constant.ConstantValues;
import com.verclamdistrib.woocomerce.customs.CircularImageView;
import com.verclamdistrib.woocomerce.customs.NotificationBadger;
import com.verclamdistrib.woocomerce.databases.User_Cart_DB;
import com.verclamdistrib.woocomerce.fragments.About;
import com.verclamdistrib.woocomerce.fragments.Categories_1;
import com.verclamdistrib.woocomerce.fragments.Categories_2;
import com.verclamdistrib.woocomerce.fragments.Categories_3;
import com.verclamdistrib.woocomerce.fragments.Categories_4;
import com.verclamdistrib.woocomerce.fragments.Categories_5;
import com.verclamdistrib.woocomerce.fragments.Categories_6;
import com.verclamdistrib.woocomerce.fragments.Checkout;
import com.verclamdistrib.woocomerce.fragments.ContactUs;
import com.verclamdistrib.woocomerce.fragments.Download;
import com.verclamdistrib.woocomerce.fragments.HomePage_1;
import com.verclamdistrib.woocomerce.fragments.HomePage_2;
import com.verclamdistrib.woocomerce.fragments.HomePage_3;
import com.verclamdistrib.woocomerce.fragments.HomePage_4;
import com.verclamdistrib.woocomerce.fragments.HomePage_5;
import com.verclamdistrib.woocomerce.fragments.My_Cart;
import com.verclamdistrib.woocomerce.fragments.My_Orders;
import com.verclamdistrib.woocomerce.fragments.News;
import com.verclamdistrib.woocomerce.fragments.NewsDescription;
import com.verclamdistrib.woocomerce.fragments.Product_Description;
import com.verclamdistrib.woocomerce.fragments.Products;
import com.verclamdistrib.woocomerce.fragments.SearchFragment;
import com.verclamdistrib.woocomerce.fragments.SettingsFragment;
import com.verclamdistrib.woocomerce.fragments.Shipping_Address;
import com.verclamdistrib.woocomerce.fragments.Update_Account;
import com.verclamdistrib.woocomerce.fragments.WishList;
import com.verclamdistrib.woocomerce.models.device_model.AppSettingsDetails;
import com.verclamdistrib.woocomerce.models.drawer_model.Drawer_Items;
import com.verclamdistrib.woocomerce.receivers.AlarmReceiver;
import com.verclamdistrib.woocomerce.utils.LocaleHelper;
import com.verclamdistrib.woocomerce.utils.NotificationScheduler;
import com.verclamdistrib.woocomerce.utils.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.verclamdistrib.woocomerce.app.App.getContext;


/**
 * MainActivity of the App
 **/

public class MainActivity extends BaseActivity {
    
    Toolbar toolbar;
    ActionBar actionBar;

    ImageView drawer_header;
    DrawerLayout drawerLayout;
    NavigationView navigationDrawer;
    
    SharedPreferences sharedPreferences;
    MyAppPrefsManager myAppPrefsManager;
    
    ExpandableListView main_drawer_list;
    DrawerExpandableListAdapter drawerExpandableAdapter;

    android.support.v4.app.FragmentManager fragmentManager;
    
    boolean doublePressedBackToExit = false;
    
    private static String mSelectedItem;
    private static final String SELECTED_ITEM_ID = "selected";
    public static ActionBarDrawerToggle actionBarDrawerToggle;
    
    List<Drawer_Items> listDataHeader = new ArrayList<>();
    Map<Drawer_Items, List<Drawer_Items>> listDataChild = new HashMap<>();
    
    AppSettingsDetails appSettings;

    Locale myLocale;
    
    
    //*********** Called when the Activity is becoming Visible to the User ********//
    
    @Override
    public void onStart() {
        super.onStart();
        
        if (myAppPrefsManager.isFirstTimeLaunch()) {
            NotificationScheduler.setReminder(MainActivity.this, AlarmReceiver.class);
            
        }
        
        myAppPrefsManager.setFirstTimeLaunch(false);
//        User_Cart_DB.initCartInstance();
    }
    
    
    
    //*********** Called when the Activity is first Created ********//
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, ConstantValues.ADMOBE_ID);


        // Get MyAppPrefsManager
        myAppPrefsManager = new MyAppPrefsManager(MainActivity.this);
        sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);


        // Binding Layout Views
        toolbar = (Toolbar) findViewById(R.id.myToolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationDrawer = (NavigationView) findViewById(R.id.main_drawer);


        // Get ActionBar and Set the Title and HomeButton of Toolbar
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle(ConstantValues.APP_HEADER);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        appSettings = ((App) getApplicationContext()).getAppSettingsDetails();

        Locale myLocale;
        // Setup Expandable DrawerList
        setupExpandableDrawerList();

        // Setup Expandable Drawer Header
        setupExpandableDrawerHeader();
        // Initialize ActionBarDrawerToggle
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawerOpen, R.string.drawerClose) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // Hide OptionsMenu
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Recreate the OptionsMenu
                invalidateOptionsMenu();
            }
        };


        // Add ActionBarDrawerToggle to DrawerLayout
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        // Synchronize the indicator with the state of the linked DrawerLayout
        actionBarDrawerToggle.syncState();


        // Get the default ToolbarNavigationClickListener
        final View.OnClickListener toggleNavigationClickListener = actionBarDrawerToggle.getToolbarNavigationClickListener();

        // Handle ToolbarNavigationClickListener with OnBackStackChangedListener
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                // Check BackStackEntryCount of FragmentManager
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

                    // Set new ToolbarNavigationClickListener
                    actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Close the Drawer if Opened
                            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                                drawerLayout.closeDrawers();
                            } else {
                                // Pop previous Fragment from BackStack
                                getSupportFragmentManager().popBackStack();
                            }
                        }
                    });


                } else {
                    // Set DrawerToggle Indicator and default ToolbarNavigationClickListener
                    actionBar.setTitle(ConstantValues.APP_HEADER);
                    actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
                    actionBarDrawerToggle.setToolbarNavigationClickListener(toggleNavigationClickListener);
                }
            }
        });

        if(getIntent().getExtras() != null) {

            String postType = getIntent().getExtras().getString("postType");
            String postID = getIntent().getExtras().getString("postID");

            if (postType.equalsIgnoreCase("product")) {
                // PRODUCT PAGE

                Fragment productsFragment;

                fragmentManager = getSupportFragmentManager();

                // Add Products Fragment to specified FrameLayout
                productsFragment = new Product_Description();
                Bundle bundle = new Bundle();
                bundle.putInt("itemID", Integer.parseInt(postID));
                productsFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.main_fragment, productsFragment).commit();
                //clearStack();

            } else if (postType.equalsIgnoreCase("post")) {
                // NEWS PAGE

                Fragment productsFragment;
                fragmentManager = getSupportFragmentManager();
                // Add Products Fragment to specified FrameLayout
                productsFragment = new NewsDescription();
                Bundle bundle = new Bundle();
                bundle.putInt("itemID", Integer.parseInt(postID));
                bundle.putString("link_url", "https://verclamdistrib.com/633-2/");
                productsFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.main_fragment, productsFragment).commit();
                //clearStack();

            }
        }
        else {

            if (appSettings != null) {

                // Select SELECTED_ITEM from SavedInstanceState
                mSelectedItem = savedInstanceState == null ? getString(R.string.actionHome) + " " + appSettings.getHomeStyle() : savedInstanceState.getString(SELECTED_ITEM_ID);
                // Navigate to SelectedItem
                drawerSelectedItemNavigation(mSelectedItem);
            }

        }

    }

    private void clearStack() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
                fragmentManager.popBackStack();
            }
        }

        if (fragmentManager.getFragments().size() > 0) {
            for (int i = 0; i < fragmentManager.getFragments().size(); i++) {
                Fragment mFragment = fragmentManager.getFragments().get(i);
                if (mFragment != null) {
                    fragmentManager.beginTransaction().remove(mFragment).commit();
                }
            }
        }
    }


    //*********** Setup Header of Navigation Drawer ********//




    public void setupExpandableDrawerHeader() {
        
        // Binding Layout Views of DrawerHeader
        drawer_header = (ImageView) findViewById(R.id.drawer_header);
        CircularImageView drawer_profile_image = (CircularImageView) findViewById(R.id.drawer_profile_image);
        TextView drawer_profile_name = (TextView) findViewById(R.id.drawer_profile_name);
        TextView drawer_profile_email = (TextView) findViewById(R.id.drawer_profile_email);
        TextView drawer_profile_welcome = (TextView) findViewById(R.id.drawer_profile_welcome);
        
        // Check if the User is Authenticated
        if (ConstantValues.IS_USER_LOGGED_IN) {
            // Check User's Info from SharedPreferences
            if (!TextUtils.isEmpty(sharedPreferences.getString("userEmail", ""))) {
                
                // Set User's Name, Email and Photo
                drawer_profile_email.setText(sharedPreferences.getString("userEmail", ""));
                
                if (!TextUtils.isEmpty(sharedPreferences.getString("userDisplayName", ""))) {
                    drawer_profile_name.setText(sharedPreferences.getString("userDisplayName", ""));
                }
                else {
                    drawer_profile_name.setText(sharedPreferences.getString("userName", ""));
                }
                
                if (!TextUtils.isEmpty(sharedPreferences.getString("userPicture", "")))
                    Glide.with(this)
                            .load(sharedPreferences.getString("userPicture", "")).asBitmap()
                            .placeholder(R.drawable.profile)
                            .error(R.drawable.profile)
                            .into(drawer_profile_image);
                
            }
            else {
                // Set Default Name, Email and Photo
                drawer_profile_image.setImageResource(R.drawable.profile);
                drawer_profile_name.setText(getString(R.string.login_or_signup));
                drawer_profile_email.setText(getString(R.string.login_or_create_account));
            }
            
        }
        else {
            // Set Default Name, Email and Photo
            drawer_profile_welcome.setVisibility(View.GONE);
            drawer_profile_image.setImageResource(R.drawable.profile);
            drawer_profile_name.setText(getString(R.string.login_or_signup));
            drawer_profile_email.setText(getString(R.string.login_or_create_account));
        }
        
        
        // Handle DrawerHeader Click Listener
        drawer_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                // Check if the User is Authenticated
                if (ConstantValues.IS_USER_LOGGED_IN) {
                    
                    // Navigate to Update_Account Fragment
                   // Fragment fragment = new SettingsFragment();
                    Fragment fragment = new Update_Account();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_fragment, fragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack(getString(R.string.actionHome)).commit();
                    
                    // Close NavigationDrawer
                    drawerLayout.closeDrawers();
                    
                }
                else {
                    // Navigate to Login Activity
                    startActivity(new Intent(MainActivity.this, Login.class));
                    finish();
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
                }
            }
        });
    }
    
    //*********** Setup Expandable List of Navigation Drawer ********//
    
    public void setupExpandableDrawerList() {
        
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        
        
        if (appSettings != null) {
            
            listDataHeader.add(new Drawer_Items(R.drawable.ic_home, getString(R.string.actionHome)));
            listDataHeader.add(new Drawer_Items(R.drawable.ic_categories, getString(R.string.actionCategories)));
            listDataHeader.add(new Drawer_Items(R.drawable.ic_cart, getString(R.string.actionShop)));
            
            if ("1".equalsIgnoreCase(appSettings.getEditProfilePage()))
                listDataHeader.add(new Drawer_Items(R.drawable.ic_account, getString(R.string.actionAccount)));
            if ("1".equalsIgnoreCase(appSettings.getMyOrdersPage()))
                listDataHeader.add(new Drawer_Items(R.drawable.ic_order, getString(R.string.actionOrders)));
            if ("1".equalsIgnoreCase(appSettings.getWishListPage()))
                listDataHeader.add(new Drawer_Items(R.drawable.ic_favorite, getString(R.string.actionFavourites)));
//            if ("1".equalsIgnoreCase(appSettings.getIntroPage()))
//                listDataHeader.add(new Drawer_Items(R.drawable.ic_intro, getString(R.string.actionIntro)));
//            if ("1".equalsIgnoreCase(appSettings.getBill_ship_info()))
//                if (ConstantValues.IS_USER_LOGGED_IN) {
//                    listDataHeader.add(new Drawer_Items(R.drawable.map_marker, getString(R.string.address_info)));
//                }
            if ("1".equalsIgnoreCase(appSettings.getNewsPage()))
                listDataHeader.add(new Drawer_Items(R.drawable.ic_newspaper, getString(R.string.actionNews)));
            if ("1".equalsIgnoreCase(appSettings.getContactUsPage()))
                listDataHeader.add(new Drawer_Items(R.drawable.ic_chat_bubble, getString(R.string.actionContactUs)));
//            if ("1".equalsIgnoreCase(appSettings.getAboutUsPage()))
//                listDataHeader.add(new Drawer_Items(R.drawable.ic_info, getString(R.string.actionAbout)));
//            if ("1".equalsIgnoreCase(appSettings.getShareApp()))
//                listDataHeader.add(new Drawer_Items(R.drawable.ic_share, getString(R.string.actionShareApp)));
//            if ("1".equalsIgnoreCase(appSettings.getRateApp()))
//                listDataHeader.add(new Drawer_Items(R.drawable.ic_star_circle, getString(R.string.actionRateApp)));
//            if ("1".equalsIgnoreCase(appSettings.getDownloads()))
//                if (ConstantValues.IS_USER_LOGGED_IN) {
//                    listDataHeader.add(new Drawer_Items(R.drawable.download, getString(R.string.download)));
//                }
//            if ("1".equalsIgnoreCase(appSettings.getSettingPage()))
//                listDataHeader.add(new Drawer_Items(R.drawable.ic_settings, getString(R.string.actionSettings)));
//
            // Add last Header Item in Drawer Header List
            if (ConstantValues.IS_USER_LOGGED_IN) {
                listDataHeader.add(new Drawer_Items(R.drawable.ic_logout, getString(R.string.actionLogout)));
            } else {
                listDataHeader.add(new Drawer_Items(R.drawable.ic_logout, getString(R.string.actionLogin)));
            }
            
            
            if (!ConstantValues.IS_CLIENT_ACTIVE) {
                List<Drawer_Items> home_styles = new ArrayList<>();
                home_styles.add(new Drawer_Items(R.drawable.ic_home, getString(R.string.homeStyle1)));
                home_styles.add(new Drawer_Items(R.drawable.ic_home, getString(R.string.homeStyle2)));
                home_styles.add(new Drawer_Items(R.drawable.ic_home, getString(R.string.homeStyle3)));
                home_styles.add(new Drawer_Items(R.drawable.ic_home, getString(R.string.homeStyle4)));
                home_styles.add(new Drawer_Items(R.drawable.ic_home, getString(R.string.homeStyle5)));
                
                List<Drawer_Items> category_styles = new ArrayList<>();
                category_styles.add(new Drawer_Items(R.drawable.ic_categories, getString(R.string.categoryStyle1)));
                category_styles.add(new Drawer_Items(R.drawable.ic_categories, getString(R.string.categoryStyle2)));
                category_styles.add(new Drawer_Items(R.drawable.ic_categories, getString(R.string.categoryStyle3)));
                category_styles.add(new Drawer_Items(R.drawable.ic_categories, getString(R.string.categoryStyle4)));
                category_styles.add(new Drawer_Items(R.drawable.ic_categories, getString(R.string.categoryStyle5)));
                category_styles.add(new Drawer_Items(R.drawable.ic_categories, getString(R.string.categoryStyle6)));
                
                List<Drawer_Items> shop_childs = new ArrayList<>();
                shop_childs.add(new Drawer_Items(R.drawable.ic_sale, getString(R.string.Sale)));
                shop_childs.add(new Drawer_Items(R.drawable.ic_arrow_up, getString(R.string.Newest)));
                shop_childs.add(new Drawer_Items(R.drawable.ic_star_circle, getString(R.string.Featured)));
                
                
                // Add Child to selective Headers
                listDataChild.put(listDataHeader.get(0), home_styles);
                listDataChild.put(listDataHeader.get(1), category_styles);
                listDataChild.put(listDataHeader.get(2), shop_childs);
            }
            
            
        }
        else{
            
            listDataHeader.add(new Drawer_Items(R.drawable.ic_home, getString(R.string.actionHome)));
            listDataHeader.add(new Drawer_Items(R.drawable.ic_categories, getString(R.string.actionCategories)));
            listDataHeader.add(new Drawer_Items(R.drawable.ic_cart, getString(R.string.actionShop)));
            listDataHeader.add(new Drawer_Items(R.drawable.ic_account, getString(R.string.actionAccount)));
            listDataHeader.add(new Drawer_Items(R.drawable.ic_order, getString(R.string.actionOrders)));
            listDataHeader.add(new Drawer_Items(R.drawable.ic_favorite, getString(R.string.actionFavourites)));
 //           listDataHeader.add(new Drawer_Items(R.drawable.ic_intro, getString(R.string.actionIntro)));
//            if (ConstantValues.IS_USER_LOGGED_IN) {
//                listDataHeader.add(new Drawer_Items(R.drawable.map_marker, getString(R.string.address_info)));
//            }
            listDataHeader.add(new Drawer_Items(R.drawable.ic_newspaper, getString(R.string.actionNews)));
//            listDataHeader.add(new Drawer_Items(R.drawable.ic_info, getString(R.string.actionAbout)));
            listDataHeader.add(new Drawer_Items(R.drawable.ic_chat_bubble, getString(R.string.actionContactUs)));
 //           listDataHeader.add(new Drawer_Items(R.drawable.ic_share, getString(R.string.actionShareApp)));
  //          listDataHeader.add(new Drawer_Items(R.drawable.ic_star_circle, getString(R.string.actionRateApp)));
//            if (ConstantValues.IS_USER_LOGGED_IN) {
//                listDataHeader.add(new Drawer_Items(R.drawable.download, getString(R.string.download)));
//            }
//            listDataHeader.add(new Drawer_Items(R.drawable.ic_settings, getString(R.string.actionSettings)));
            // Add last Header Item in Drawer Header List
            if (ConstantValues.IS_USER_LOGGED_IN) {
                listDataHeader.add(new Drawer_Items(R.drawable.ic_logout, getString(R.string.actionLogout)));
            } else {
                listDataHeader.add(new Drawer_Items(R.drawable.ic_logout, getString(R.string.actionLogin)));
            }
            
            
            if (!ConstantValues.IS_CLIENT_ACTIVE) {
                List<Drawer_Items> home_styles = new ArrayList<>();
                home_styles.add(new Drawer_Items(R.drawable.ic_home, getString(R.string.homeStyle1)));
                home_styles.add(new Drawer_Items(R.drawable.ic_home, getString(R.string.homeStyle2)));
                home_styles.add(new Drawer_Items(R.drawable.ic_home, getString(R.string.homeStyle3)));
                home_styles.add(new Drawer_Items(R.drawable.ic_home, getString(R.string.homeStyle4)));
                home_styles.add(new Drawer_Items(R.drawable.ic_home, getString(R.string.homeStyle5)));
                
                List<Drawer_Items> category_styles = new ArrayList<>();
                category_styles.add(new Drawer_Items(R.drawable.ic_categories, getString(R.string.categoryStyle1)));
                category_styles.add(new Drawer_Items(R.drawable.ic_categories, getString(R.string.categoryStyle2)));
                category_styles.add(new Drawer_Items(R.drawable.ic_categories, getString(R.string.categoryStyle3)));
                category_styles.add(new Drawer_Items(R.drawable.ic_categories, getString(R.string.categoryStyle4)));
                category_styles.add(new Drawer_Items(R.drawable.ic_categories, getString(R.string.categoryStyle5)));
                category_styles.add(new Drawer_Items(R.drawable.ic_categories, getString(R.string.categoryStyle6)));
                
                List<Drawer_Items> shop_childs = new ArrayList<>();
                shop_childs.add(new Drawer_Items(R.drawable.ic_sale, getString(R.string.Sale)));
                shop_childs.add(new Drawer_Items(R.drawable.ic_arrow_up, getString(R.string.Newest)));
                shop_childs.add(new Drawer_Items(R.drawable.ic_star_circle, getString(R.string.Featured)));
                
                
                // Add Child to selective Headers
                listDataChild.put(listDataHeader.get(0), home_styles);
                listDataChild.put(listDataHeader.get(1), category_styles);
                listDataChild.put(listDataHeader.get(2), shop_childs);
            }
            
        }
        
        
        
        // Initialize DrawerExpandableListAdapter
        drawerExpandableAdapter = new DrawerExpandableListAdapter(this, listDataHeader, listDataChild);
        
        // Bind ExpandableListView and set DrawerExpandableListAdapter to the ExpandableListView
        main_drawer_list = (ExpandableListView) findViewById(R.id.main_drawer_list);
        main_drawer_list.setAdapter(drawerExpandableAdapter);
        
        drawerExpandableAdapter.notifyDataSetChanged();
        
        
        
        // Handle Group Item Click Listener
        main_drawer_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                
                if (drawerExpandableAdapter.getChildrenCount(groupPosition) < 1) {
                    // Navigate to Selected Main Item
                    if (groupPosition == 0) {
                        drawerSelectedItemNavigation(ConstantValues.DEFAULT_HOME_STYLE);
                    }
                    else if (groupPosition == 1) {
                        drawerSelectedItemNavigation(ConstantValues.DEFAULT_CATEGORY_STYLE);
                    }
                    else {
                        drawerSelectedItemNavigation(listDataHeader.get(groupPosition).getTitle());
                    }
                }
                return false;
            }
        });
        
        
        // Handle Child Item Click Listener
        main_drawer_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                // Navigate to Selected Child Item
                drawerSelectedItemNavigation(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getTitle());
                return false;
            }
        });
        
        
        // Handle Group Expand Listener
        main_drawer_list.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {}
        });
        // Handle Group Collapse Listener
        main_drawer_list.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {}
        });
        
    }
    
    //*********** Navigate to given Selected Item of NavigationDrawer ********//
    
    private void drawerSelectedItemNavigation(String selectedItem) {
        
        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        
        if(selectedItem.equalsIgnoreCase(getString(R.string.actionHome))) {
            mSelectedItem = selectedItem;

            // Navigate to any selected HomePage Fragment
            fragment = new HomePage_1();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();

            drawerLayout.closeDrawers();

        }
        else if(selectedItem.equalsIgnoreCase(getString(R.string.homeStyle1))) {
            mSelectedItem = selectedItem;
            
            // Navigate to HomePage1 Fragment
            fragment = new HomePage_1();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
            
            drawerLayout.closeDrawers();
            
        }
        else if(selectedItem.equalsIgnoreCase(getString(R.string.homeStyle2))) {
            mSelectedItem = selectedItem;
            
            // Navigate to HomePage2 Fragment
            fragment = new HomePage_2();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
            
            drawerLayout.closeDrawers();
            
        }
        else if(selectedItem.equalsIgnoreCase(getString(R.string.homeStyle3))) {
            mSelectedItem = selectedItem;
            
            // Navigate to HomePage3 Fragment
            fragment = new HomePage_3();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
            
            drawerLayout.closeDrawers();
            
        }
        else if(selectedItem.equalsIgnoreCase(getString(R.string.homeStyle4))) {
            mSelectedItem = selectedItem;
            
            // Navigate to HomePage4 Fragment
            fragment = new HomePage_4();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
            
            drawerLayout.closeDrawers();

            
        }
        else if(selectedItem.equalsIgnoreCase(getString(R.string.homeStyle5))) {
            mSelectedItem = selectedItem;
            
            // Navigate to HomePage5 Fragment
            fragment = new HomePage_5();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
            
            drawerLayout.closeDrawers();

            
        }
        else if(selectedItem.equalsIgnoreCase(getString(R.string.actionCategories))) {
            mSelectedItem = selectedItem;
            
            Bundle bundle = new Bundle();
            bundle.putBoolean("isHeaderVisible", false);
            
            // Navigate to any selected CategoryPage Fragment
            fragment = new Categories_1();
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(getString(R.string.actionHome)).commit();

            drawerLayout.closeDrawers();
            
        }
        else if(selectedItem.equalsIgnoreCase(getString(R.string.categoryStyle1))) {
            mSelectedItem = selectedItem;
            
            Bundle bundle = new Bundle();
            bundle.putBoolean("isHeaderVisible", false);
            
            // Navigate to Categories_1 Fragment
            fragment = new Categories_1();
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(getString(R.string.actionHome)).commit();
            
            drawerLayout.closeDrawers();

        }
        else if(selectedItem.equalsIgnoreCase(getString(R.string.categoryStyle2))) {
            mSelectedItem = selectedItem;
            
            Bundle bundle = new Bundle();
            bundle.putBoolean("isHeaderVisible", false);
            
            // Navigate to Categories_2 Fragment
            fragment = new Categories_2();
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(getString(R.string.actionHome)).commit();
            
            drawerLayout.closeDrawers();
            
        }
        else if(selectedItem.equalsIgnoreCase(getString(R.string.categoryStyle3))) {
            mSelectedItem = selectedItem;
            
            Bundle bundle = new Bundle();
            bundle.putBoolean("isHeaderVisible", false);
            
            // Navigate to Categories_3 Fragment
            fragment = new Categories_3();
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(getString(R.string.actionHome)).commit();
            
            drawerLayout.closeDrawers();
            
        }
        else if(selectedItem.equalsIgnoreCase(getString(R.string.categoryStyle4))) {
            mSelectedItem = selectedItem;
            
            Bundle bundle = new Bundle();
            bundle.putBoolean("isHeaderVisible", false);
            
            // Navigate to Categories_4 Fragment
            fragment = new Categories_4();
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(getString(R.string.actionHome)).commit();
            
            drawerLayout.closeDrawers();
            
        }
        else if(selectedItem.equalsIgnoreCase(getString(R.string.categoryStyle5))) {
            mSelectedItem = selectedItem;
            
            Bundle bundle = new Bundle();
            bundle.putBoolean("isHeaderVisible", false);
            
            // Navigate to Categories_5 Fragment
            fragment = new Categories_5();
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(getString(R.string.actionHome)).commit();
            
            drawerLayout.closeDrawers();
            
        }
        else if(selectedItem.equalsIgnoreCase(getString(R.string.categoryStyle6))) {
            mSelectedItem = selectedItem;
            
            Bundle bundle = new Bundle();
            bundle.putBoolean("isHeaderVisible", false);
            
            // Navigate to Categories_6 Fragment
            fragment = new Categories_6();
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(getString(R.string.actionHome)).commit();
            
            drawerLayout.closeDrawers();
            
        }
        else if(selectedItem.equalsIgnoreCase(getString(R.string.actionShop))) {
            mSelectedItem = selectedItem;
            
            Bundle bundle = new Bundle();
            bundle.putString("sortBy", "date");
            bundle.putBoolean("isMenuItem", true);
            
            // Navigate to Products Fragment
            fragment = new Products();
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(getString(R.string.actionHome)).commit();
            
            drawerLayout.closeDrawers();
            
        }
        else if (selectedItem.equalsIgnoreCase(getString(R.string.Sale))) {
            mSelectedItem = selectedItem;
            
            Bundle bundle = new Bundle();
            bundle.putBoolean("on_sale", true);
            bundle.putBoolean("isMenuItem", true);
            
            // Navigate to Products Fragment
            fragment = new Products();
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(getString(R.string.actionHome)).commit();
            
            drawerLayout.closeDrawers();
            
        }
        else if (selectedItem.equalsIgnoreCase(getString(R.string.Newest))) {
            mSelectedItem = selectedItem;
            
            Bundle bundle = new Bundle();
            bundle.putBoolean("isMenuItem", true);
            
            // Navigate to Products Fragment
            fragment = new Products();
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(getString(R.string.actionHome)).commit();
            
            drawerLayout.closeDrawers();
            
        }
        else if (selectedItem.equalsIgnoreCase(getString(R.string.Featured))) {
            mSelectedItem = selectedItem;
            
            Bundle bundle = new Bundle();
            bundle.putBoolean("featured", true);
            bundle.putBoolean("isMenuItem", true);
            
            // Navigate to Products Fragment
            fragment = new Products();
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(getString(R.string.actionHome)).commit();
            
            drawerLayout.closeDrawers();
            
        }
        
        
        else if (selectedItem.equalsIgnoreCase(getString(R.string.actionAccount))) {
            if (ConstantValues.IS_USER_LOGGED_IN) {
                mSelectedItem = selectedItem;
                
                // Navigate to Update_Account Fragment
                fragment = new Update_Account();
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(getString(R.string.actionHome)).commit();
                
                drawerLayout.closeDrawers();
                
            }
            else {
                // Navigate to Login Activity
                startActivity(new Intent(MainActivity.this, Login.class));
                finish();
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
            }
            
        }
        else if (selectedItem.equalsIgnoreCase(getString(R.string.actionOrders))) {
            if (ConstantValues.IS_USER_LOGGED_IN) {
                mSelectedItem = selectedItem;
                
                // Navigate to My_Orders Fragment
                fragment = new My_Orders();
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(getString(R.string.actionHome)).commit();
                
                drawerLayout.closeDrawers();
                
            }
            else {
                // Navigate to Login Activity
                startActivity(new Intent(MainActivity.this, Login.class));
                finish();
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
            }
            
        }
        else if (selectedItem.equalsIgnoreCase(getString(R.string.actionFavourites))) {
            mSelectedItem = selectedItem;
            
            // Navigate to WishList Fragment
            fragment = new WishList();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(getString(R.string.actionHome)).commit();
            
            drawerLayout.closeDrawers();
            
        }
        else if (selectedItem.equalsIgnoreCase(getString(R.string.actionNews))) {
            mSelectedItem = selectedItem;
            
            // Navigate to News Fragment
            fragment = new News();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(getString(R.string.actionHome)).commit();
            
            drawerLayout.closeDrawers();
            
        }
        else if (selectedItem.equalsIgnoreCase(getString(R.string.address_info))) {
            mSelectedItem = selectedItem;
    
            Bundle bundle = new Bundle();
            bundle.putString("shipping", "1");
            // Navigate to News Fragment
            fragment = new Shipping_Address();
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(getString(R.string.actionHome)).commit();
    
            drawerLayout.closeDrawers();
            
        }

        else if (selectedItem.equalsIgnoreCase(getString(R.string.download))) {
            mSelectedItem = selectedItem;
    
            fragment = new Download();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(getString(R.string.actionHome)).commit();
    
            drawerLayout.closeDrawers();
    
        }
        else if (selectedItem.equalsIgnoreCase(getString(R.string.actionIntro))) {
            mSelectedItem = selectedItem;
            
            // Navigate to IntroScreen
            startActivity(new Intent(getBaseContext(), IntroScreen.class));
            
        }
        else if (selectedItem.equalsIgnoreCase(getString(R.string.actionAbout))) {
            mSelectedItem = selectedItem;
            
            // Navigate to About Fragment
            fragment = new About();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(getString(R.string.actionHome)).commit();
            
            drawerLayout.closeDrawers();
            
        }
        else if (selectedItem.equalsIgnoreCase(getString(R.string.actionShareApp))) {
            mSelectedItem = selectedItem;
            
            // Share App with the help of static method of Utilities class
            Utilities.shareMyApp(MainActivity.this);
            
            drawerLayout.closeDrawers();
            
        }
        else if (selectedItem.equalsIgnoreCase(getString(R.string.actionRateApp))) {
            mSelectedItem = selectedItem;
            
            // Rate App with the help of static method of Utilities class
            Utilities.rateMyApp(MainActivity.this);
            
            drawerLayout.closeDrawers();
            
        }
        else if (selectedItem.equalsIgnoreCase(getString(R.string.actionContactUs))) {
            mSelectedItem = selectedItem;
            
            // Navigate to ContactUs Fragment
            fragment = new ContactUs();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(getString(R.string.actionHome)).commit();
            
            drawerLayout.closeDrawers();
            
        }
        else if (selectedItem.equalsIgnoreCase(getString(R.string.actionSettings))) {
            mSelectedItem = selectedItem;
            
            // Navigate to SettingsFragment Fragment
            fragment = new SettingsFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(getString(R.string.actionHome)).commit();
            
            drawerLayout.closeDrawers();
            
        }
        else if (selectedItem.equalsIgnoreCase(getString(R.string.actionLogin))) {
            mSelectedItem = selectedItem;
            
            // Navigate to Login Activity
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_right);
            
        }
        else if (selectedItem.equalsIgnoreCase(getString(R.string.actionLogout))) {
            mSelectedItem = selectedItem;
            // cache delete
            if (Checkout.checkout_webView != null){
                Checkout.checkout_webView.clearCache(true);
                Checkout.checkout_webView.clearFormData();
                Checkout.checkout_webView.clearHistory();
                //
                CookieSyncManager.createInstance(getContext());
                android.webkit.CookieManager.getInstance().removeAllCookie();
            }
            // Edit UserID in SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userID", "");
            editor.putString("userCookie", "");
            editor.putString("userPicture", "");
            editor.putString("userName", "");
            editor.putString("userDisplayName", "");
            editor.apply();
            User_Cart_DB user_cart_db = new User_Cart_DB();
            user_cart_db.clearCart();

            // Set UserLoggedIn in MyAppPrefsManager
            MyAppPrefsManager myAppPrefsManager = new MyAppPrefsManager(this);
            myAppPrefsManager.setUserLoggedIn(false);
            
            // Set isLogged_in of ConstantValues
            ConstantValues.IS_USER_LOGGED_IN = myAppPrefsManager.isUserLoggedIn();
            
            
            // Navigate to Login Activity
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_right);
        }
    }
    
    
    
    //*********** Called by the System when the Device's Configuration changes while Activity is Running ********//
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Configure ActionBarDrawerToggle with new Configuration
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    
    
    //*********** Invoked to Save the Instance's State when the Activity may be Temporarily Destroyed ********//
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the Selected NavigationDrawer Item
        outState.putString(SELECTED_ITEM_ID, mSelectedItem);
    }
    
    
    
    //*********** Set the Base Context for the ContextWrapper ********//
    
    @Override
    protected void attachBaseContext(Context newBase) {

        String languageCode = ConstantValues.LANGUAGE_CODE;
        if ("".equalsIgnoreCase(languageCode))
            languageCode = ConstantValues.LANGUAGE_CODE = "fr";

        super.attachBaseContext(LocaleHelper.wrapLocale(newBase, languageCode));
    }

    
    
    //*********** Receives the result from a previous call of startActivityForResult(Intent, int) ********//
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    
    
    //*********** Creates the Activity's OptionsMenu ********//
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate toolbar_menu Menu
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        
        // Bind Menu Items
        MenuItem languageItem = menu.findItem(R.id.toolbar_ic_language);
        MenuItem searchItem = menu.findItem(R.id.toolbar_ic_search);
        MenuItem cartItem = menu.findItem(R.id.toolbar_ic_cart);
    
        languageItem.setVisible(false);

        
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageView image = (ImageView) inflater.inflate(R.layout.layout_animated_ic_cart, null);
        
        Drawable itemIcon = cartItem.getIcon().getCurrent();
        image.setImageDrawable(itemIcon);
        
        cartItem.setActionView(image);
        
        
        cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to My_Cart Fragment
                Fragment fragment = new My_Cart();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(getString(R.string.actionHome)).commit();
            }
        });
        
        
        // Tint Menu Icons with the help of static method of Utilities class
        Utilities.tintMenuIcon(MainActivity.this, searchItem, R.color.white);
        Utilities.tintMenuIcon(MainActivity.this, cartItem, R.color.white);

        
        return true;
    }
    
    
    
    //*********** Prepares the OptionsMenu of Toolbar ********//
    
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    
        // Clear OptionsMenu if NavigationDrawer is Opened
        if (drawerLayout.isDrawerOpen(navigationDrawer)) {
        
            MenuItem languageItem = menu.findItem(R.id.toolbar_ic_language);
            MenuItem searchItem = menu.findItem(R.id.toolbar_ic_search);
            MenuItem cartItem = menu.findItem(R.id.toolbar_ic_cart);
        
            languageItem.setVisible(true);
            searchItem.setVisible(false);
            cartItem.setVisible(false);
        
        }
        else {
            MenuItem cartItem = menu.findItem(R.id.toolbar_ic_cart);
        
            // Get No. of Cart Items with the static method of My_Cart Fragment
            int cartSize = My_Cart.getCartSize();
        
        
            // if Cart has some Items
            if (cartSize > 0) {
                // Animation for cart_menuItem
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake_icon);
                animation.setRepeatMode(Animation.REVERSE);
                animation.setRepeatCount(1);
            
                cartItem.getActionView().startAnimation(animation);
                cartItem.getActionView().setAnimation(null);
            
            
                LayerDrawable icon = null;
                Drawable drawable = cartItem.getIcon();
            
                if (drawable instanceof DrawableWrapper) {
                    drawable = ((DrawableWrapper)drawable).getWrappedDrawable();
                }
                else if (drawable instanceof WrappedDrawable) {
                    drawable = ((WrappedDrawable)drawable).getWrappedDrawable();
                }
            
            
                if (drawable instanceof LayerDrawable) {
                    icon = (LayerDrawable) drawable;
                }
                else if (drawable instanceof DrawableWrapper) {
                    DrawableWrapper wrapper = (DrawableWrapper) drawable;
                    if (wrapper.getWrappedDrawable() instanceof LayerDrawable) {
                        icon = (LayerDrawable) wrapper.getWrappedDrawable();
                    }
                }

//                icon = (LayerDrawable) drawable;
            
            
                // Set BadgeCount on Cart_Icon with the static method of NotificationBadger class
                if (icon != null)
                    NotificationBadger.setBadgeCount(this, icon, String.valueOf(cartSize));
            
            
            } else {
                // Set the Icon for Empty Cart
                cartItem.setIcon(R.drawable.ic_cart_empty);
            }
        
        }
    
        return super.onPrepareOptionsMenu(menu);
    }
    
    
    
    //*********** Called whenever an Item in OptionsMenu is Selected ********//
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        
        
        switch (item.getItemId()) {
    
            case R.id.toolbar_ic_language:
        
                drawerLayout.closeDrawers();
                // Navigate to Languages Fragment
//                fragment = new Languages();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.main_fragment, fragment)
//                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                        .addToBackStack(getString(R.string.actionHome)).commit()

                break;
            
            case R.id.toolbar_ic_search:
    
              SearchFragment.FLAG_SEARCHED = false;
                // Navigate to SearchFragment Fragment
                fragment = new SearchFragment();
                //fragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(getString(R.string.actionHome)).commit();
                break;
            
            case R.id.toolbar_ic_cart:
                
                // Navigate to My_Cart Fragment
                fragment = new My_Cart();
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(getString(R.string.actionHome)).commit();
                break;
            
            default:
                break;
        }
        
        return true;
    }
    
    public void openCheckOut(){
        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragment = new My_Cart();
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(getString(R.string.actionHome)).commit();
    }
    
    //*********** Called when the Activity has detected the User pressed the Back key ********//
    
    @Override
    public void onBackPressed() {
        
        // Get FragmentManager
        FragmentManager fm = getSupportFragmentManager();
        
        
        // Check if NavigationDrawer is Opened
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            
        }
        // Check if BackStack has some Fragments
        else  if (fm.getBackStackEntryCount() > 0) {
            
            // Pop previous Fragment
            fm.popBackStack();
            
        }
        // Check if doubleBackToExitPressed is true
        else if (doublePressedBackToExit) {
            super.onBackPressed();
//            if (appSettings != null) {
//
//                // Select SELECTED_ITEM from SavedInstanceState
//                mSelectedItem = getString(R.string.actionHome) + " " + appSettings.getHomeStyle();
//                // Navigate to SelectedItem
//                drawerSelectedItemNavigation(mSelectedItem);
//            }
        }
        else {
            this.doublePressedBackToExit = true;
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
            
            // Delay of 2 seconds
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    
                    // Set doublePressedBackToExit false after 2 seconds
                    doublePressedBackToExit = false;
                }
            }, 2000);
        }
    }

    public void setLocale(String lang) {
        myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        finish();
    }
}

