package com.verclamdistrib.woocomerce.app;


import android.content.Context;
import android.support.multidex.MultiDexApplication;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import com.verclamdistrib.woocomerce.NotificationHandler;
import com.verclamdistrib.woocomerce.NotificationReceivedHandler;
import com.verclamdistrib.woocomerce.models.attibute_model.AttributeDetails;
import com.verclamdistrib.woocomerce.models.tag_model.TagDetails;
import com.verclamdistrib.woocomerce.models.term_model.TermDetails;
import com.onesignal.OneSignal;
import com.verclamdistrib.woocomerce.databases.DB_Handler;
import com.verclamdistrib.woocomerce.databases.DB_Manager;
import com.verclamdistrib.woocomerce.models.coupons_model.CouponDetails;
import com.verclamdistrib.woocomerce.models.order_model.OrderDetails;
import com.verclamdistrib.woocomerce.models.drawer_model.Drawer_Items;
import com.verclamdistrib.woocomerce.models.banner_model.BannerDetails;
import com.verclamdistrib.woocomerce.models.category_model.CategoryDetails;
import com.verclamdistrib.woocomerce.models.device_model.AppSettingsDetails;
import com.verclamdistrib.woocomerce.models.order_model.OrderShippingMethod;
import com.verclamdistrib.woocomerce.models.user_model.UserDetails;


/**
 * App extending Application, is used to save some Lists and Objects with Application Context.
 **/


public class App extends MultiDexApplication {

    // Application Context
    private static Context context;
    private static DB_Handler db_handler;


    private List<Drawer_Items> drawerHeaderList;
    private Map<Drawer_Items, List<Drawer_Items>> drawerChildList;
    
    
    private AppSettingsDetails appSettings = null;
    private List<BannerDetails> bannersList = new ArrayList<>();
    private List<CategoryDetails> categoriesList = new ArrayList<>();
    private List<AttributeDetails> attributeList = new ArrayList<>();
    private List<TagDetails> tagsList = new ArrayList<>();
    private LinkedHashMap<Integer, List<TermDetails>> termsList = new LinkedHashMap<>();
    
    private UserDetails userDetails = new UserDetails();
    private OrderDetails orderDetails = new OrderDetails();
    private OrderShippingMethod shippingService = new OrderShippingMethod();
    private UserDetails shippingAddress = new UserDetails();
    private UserDetails billingAddress = new UserDetails();
    
   
    private CouponDetails couponDetails = new CouponDetails();


    @Override
    public void onCreate() {
        super.onCreate();

        // set App Context
        context = this.getApplicationContext();

        // initialize DB_Handler and DB_Manager
        db_handler = new DB_Handler();
        DB_Manager.initializeInstance(db_handler);
    
        
        // initialize OneSignal
        OneSignal.startInit(this)
                .filterOtherGCMReceivers(true)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.InAppAlert)
                .unsubscribeWhenNotificationsAreDisabled(false)
                .setNotificationOpenedHandler(new NotificationHandler(this))
                //.setNotificationReceivedHandler(new NotificationReceivedHandler(this))

                .init();
    }



    //*********** Returns Application Context ********//

    public static Context getContext() {
        return context;
    }




    public List<Drawer_Items> getDrawerHeaderList() {
        return drawerHeaderList;
    }

    public void setDrawerHeaderList(List<Drawer_Items> drawerHeaderList) {
        this.drawerHeaderList = drawerHeaderList;
    }

    public Map<Drawer_Items, List<Drawer_Items>> getDrawerChildList() {
        return drawerChildList;
    }

    public void setDrawerChildList(Map<Drawer_Items, List<Drawer_Items>> drawerChildList) {
        this.drawerChildList = drawerChildList;
    }
    
    
    
    public AppSettingsDetails getAppSettingsDetails() {
        return appSettings;
    }
    
    public void setAppSettingsDetails(AppSettingsDetails appSettings) {
        this.appSettings = appSettings;
    }
    
    
    public List<BannerDetails> getBannersList() {
        return bannersList;
    }
    
    public void setBannersList(List<BannerDetails> bannersList) {
        this.bannersList = bannersList;
    }
    
    public List<CategoryDetails> getCategoriesList() {
        return categoriesList;
    }
    
    public void setCategoriesList(List<CategoryDetails> categoriesList) {
        this.categoriesList = categoriesList;
    }

    public List<AttributeDetails> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<AttributeDetails> attributeList) {
        this.attributeList = attributeList;
    }
    public List<TagDetails> getTagsList() {
        return tagsList;
    }

    public void setTagsList(List<TagDetails> tagsList) {
        this.tagsList = tagsList;
    }

    public List<TermDetails> getTermList(Integer termId) {
        return termsList.get(termId);
    }

    public void setTermList(Integer id, List<TermDetails> termList) {
        this.termsList.put(id, termList);
    }


    public OrderDetails getOrderDetails() {
        return orderDetails;
    }
    
    public void setOrderDetails(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }
    
    public UserDetails getUserDetails() {
        return userDetails;
    }
    
    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }
    
    public OrderShippingMethod getShippingService() {
        return shippingService;
    }
    
    public void setShippingService(OrderShippingMethod shippingService) {
        this.shippingService = shippingService;
    }
    
    public CouponDetails getCouponDetails() {
        return couponDetails;
    }
    
    public void setCouponDetails(CouponDetails couponDetails) {
        this.couponDetails = couponDetails;
    }
    
    
}

