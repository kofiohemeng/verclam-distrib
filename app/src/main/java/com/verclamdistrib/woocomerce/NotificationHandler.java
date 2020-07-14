package com.verclamdistrib.woocomerce;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.verclamdistrib.woocomerce.activities.MainActivity;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.verclamdistrib.woocomerce.activities.SplashScreen;
import com.verclamdistrib.woocomerce.app.App;

import org.json.JSONObject;

import java.util.List;

public class NotificationHandler implements OneSignal.NotificationOpenedHandler {
    String postType;
    String postID;
    Context context2;
    public NotificationHandler(Context context) {
        context2 = context;
    }


    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        OSNotificationAction.ActionType actionType = result.action.type;
        /*JSONObject data = result.notification.payload.additionalData;
        String customKey;

        if (data != null) {
            customKey = data.optString("customkey", null);
            if (customKey != null)
                Log.e("OneSignalExample", "customkey set with value: " + customKey);
        }

        if (actionType == OSNotificationAction.ActionType.ActionTaken)
            Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);
*/

        JSONObject data = result.notification.payload.additionalData;


        if (data != null) {

             postType = data.optString("postType", null);

            if (postType.equalsIgnoreCase("product")) {
                // PRODUCT PAGE
                postID = data.optString("postID", null);

                Log.e("arpit", "postType set with value: " + postType+"  "+postID);

            }
             else if (postType.equalsIgnoreCase("post")) {
                // NEWS PAGE
                postID = data.optString("postID", null);
                Log.e("arpit", "postType set with value: " + postType+"  "+postID);

            }



        }

        //android.os.Debug.waitForDebugger();

        Intent intent = new Intent(context2, MainActivity.class);

        if (((App)context2.getApplicationContext()).getAppSettingsDetails() == null) {
            // App is not running
            intent = new Intent(context2, SplashScreen.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("postType",postType);
        intent.putExtra("postID",postID);
        context2.startActivity(intent);

    }
}
