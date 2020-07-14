package com.verclamdistrib.woocomerce;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.onesignal.OSNotification;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.verclamdistrib.woocomerce.activities.MainActivity;

import org.json.JSONObject;

public class NotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
    String postType;
    String postID;
    Context context2;
    public NotificationReceivedHandler(Context context) {
        context2 = context;
    }

    @Override
    public void notificationReceived(OSNotification notification) {

//        OSNotificationAction.ActionType actionType = result.action.type;
//
//        JSONObject data = result.notification.payload.additionalData;
//
//        if (data != null) {
//
//            postType = data.optString("postType", null);
//
//            if (postType.equalsIgnoreCase("product")) {
//                // PRODUCT PAGE
//                postID = data.optString("postID", null);
//
//                Log.e("arpit", "postType set with value: " + postType+"  "+postID);
//
//            }
//            else if (postType.equalsIgnoreCase("post")) {
//                // NEWS PAGE
//                postID = data.optString("postID", null);
//                Log.e("arpit", "postType set with value: " + postType+"  "+postID);
//
//            }
//        }
//        Intent intent = new Intent(context2, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra("postType",postType);
//        intent.putExtra("postID",postID);
//        context2.startActivity(intent);

    }
}