package com.verclamdistrib.woocomerce.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.verclamdistrib.woocomerce.app.MyAppPrefsManager;
import com.verclamdistrib.woocomerce.constant.ConstantValues;
import com.verclamdistrib.woocomerce.databases.User_Cart_DB;
import com.verclamdistrib.woocomerce.models.api_response_model.BaseResponse;
import com.verclamdistrib.woocomerce.network.APIClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseActivity extends AppCompatActivity {

    public void onStart() {
        super.onStart();

        if (ConstantValues.IS_USER_LOGGED_IN) {
            SharedPreferences pref = getSharedPreferences("UserInfo", MODE_PRIVATE);
            String userEmail = pref.getString("userEmail", "");
            String userPassword = pref.getString("userPassword", "");
            Log.e("userEmail >>> ",">> "+userEmail+"   ppp "+userPassword);
            if (!userEmail.equals("")) {
                Call<BaseResponse> call = APIClient.getInstance().checkUser(userEmail, userPassword);

                call.enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.body().getMessage().equals("false")) {
                            try{
                                ConstantValues.IS_USER_LOGGED_IN = false;
                                SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
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
                                MyAppPrefsManager myAppPrefsManager = new MyAppPrefsManager(BaseActivity.this);
                                myAppPrefsManager.setUserLoggedIn(false);

                                finish();
                                startActivity(new Intent(BaseActivity.this,Login.class));
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        ConstantValues.IS_USER_LOGGED_IN = false;
                    }
                });
            }
        }
    }
}
