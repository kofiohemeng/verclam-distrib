package com.verclamdistrib.woocomerce.activities;


import android.support.annotation.Nullable;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.verclamdistrib.woocomerce.R;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

import com.verclamdistrib.woocomerce.app.App;
import com.verclamdistrib.woocomerce.app.MyAppPrefsManager;
import com.verclamdistrib.woocomerce.models.api_response_model.ErrorResponse;
import com.verclamdistrib.woocomerce.models.user_model.UserData;
import com.verclamdistrib.woocomerce.network.APIClient;
import com.verclamdistrib.woocomerce.customs.DialogLoader;
import com.verclamdistrib.woocomerce.utils.LocaleHelper;
import com.verclamdistrib.woocomerce.utils.ValidateInputs;
import com.verclamdistrib.woocomerce.constant.ConstantValues;
import com.verclamdistrib.woocomerce.models.user_model.UserDetails;


/**
 * Login activity handles User's Email, Facebook and Google Login
 **/


public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    View parentView;
    Boolean showGuest = false;
    Boolean cartNavigation = false;

    Toolbar toolbar;
    ActionBar actionBar;

    EditText user_email, user_password;
    TextView forgotPasswordText;
    Button signupBtn;
    Button loginBtn;
    
    DialogLoader dialogLoader;
    
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("show_guest")) {
                showGuest = getIntent().getExtras().getBoolean("show_guest", false);
            }
            if (getIntent().getExtras().containsKey("cart_navigation")) {
                cartNavigation = getIntent().getExtras().getBoolean("cart_navigation", false);
            }
        }

        setContentView(R.layout.login);

        // setting Toolbar
        toolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.login));
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        
        // Binding Layout Views
        user_email = (EditText) findViewById(R.id.user_email);
        user_password = (EditText) findViewById(R.id.user_password);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        signupBtn = (Button) findViewById(R.id.btn_login_SignUp);
        forgotPasswordText = (TextView) findViewById(R.id.forgot_password_text);
    
        parentView = signupBtn;

        dialogLoader = new DialogLoader(Login.this);
        
        sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
    
        
        //user_email.setText(sharedPreferences.getString("userName", ""));
        
        
        // Handle on Forgot Password Click
        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(Login.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_input, null);
                dialog.setView(dialogView);
                dialog.setCancelable(true);
    
                final Button dialog_button = (Button) dialogView.findViewById(R.id.dialog_button);
                final EditText dialog_input = (EditText) dialogView.findViewById(R.id.dialog_input);
                final TextView dialog_title = (TextView) dialogView.findViewById(R.id.dialog_title);
    
                dialog_button.setText(getString(R.string.send));
                dialog_title.setText(getString(R.string.forgot_your_password));
                
    
                final AlertDialog alertDialog = dialog.create();
                alertDialog.show();
                
                dialog_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        
                        if (ValidateInputs.isValidEmail(dialog_input.getText().toString().trim())) {
                            // Request for Password Reset
                            processForgotPassword(dialog_input.getText().toString());
                            
                        }
                        else {
                            Snackbar.make(parentView, getString(R.string.invalid_email), Snackbar.LENGTH_LONG).show();
                        }
    
                        alertDialog.dismiss();
                    }
                });
            }
        });
    
    
    
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to SignUp Activity
                startActivity(new Intent(Login.this, Signup.class));
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate Login Form Inputs
                boolean isValidData = validateLogin();

                if (isValidData) {

                    // Proceed User Login
                    processLogin();
                }
            }
        });

//        user_email.setText("tester@tester.com");
//        user_password.setText("3f1F283j7cC*4AUEE94Lu*R8");

    }
    
    
    
    //*********** Called if Connection fails for Google Login ********//
    
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // If Connection fails for GoogleApiClient
    }


    //*********** Proceed Login with User Email and Password ********//

    private void processLogin() {

        dialogLoader.showProgressDialog();

        Call<UserData> call = APIClient.getInstance()
                .processLogin
                        (
                                "cool",
                                user_email.getText().toString().trim(),
                                user_password.getText().toString().trim()
                        );

        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {

                dialogLoader.hideProgressDialog();
                
                if (response.isSuccessful()) {
                    
                    if ("ok".equalsIgnoreCase(response.body().getStatus())  &&  response.body().getCookie() != null) {
    
                        // Get the User Details from Response
                        UserDetails userDetails = response.body().getUserDetails();
                        userDetails.setCookie(response.body().getCookie());
                        
                        
                        if (response.body().getId() != null) {
                            userDetails.setId(response.body().getId());
                        }
                        else {
                            userDetails.setId(userDetails.getId());
                        }
                        
                        
                        if (response.body().getUser_login() != null) {
                            userDetails.setUsername(response.body().getUser_login());
                        }
                        else {
                            userDetails.setUsername(userDetails.getUsername());
                        }
    
                        
                        if (userDetails.getName() != null) {
                            userDetails.setDisplay_name(userDetails.getName());
                        }
    
                        
                        ((App) getApplicationContext().getApplicationContext()).setUserDetails(userDetails);
    
    
                        // Save necessary details in SharedPrefs
                        editor = sharedPreferences.edit();
                        editor.putString("userID", userDetails.getId());
                        editor.putString("userCookie", userDetails.getCookie());
                        editor.putString("userEmail", userDetails.getEmail());
                        editor.putString("userName", userDetails.getUsername());
                        editor.putString("userDisplayName", userDetails.getDisplay_name());
                        editor.putString("userPicture", "");
                        editor.putString("userPassword",user_password.getText().toString().trim() );

                        if (userDetails.getPicture() != null  &&  userDetails.getPicture().getData() != null)
                            if (!TextUtils.isEmpty(userDetails.getPicture().getData().getUrl()))
                                editor.putString("userPicture", userDetails.getPicture().getData().getUrl());
    
                        editor.putBoolean("isLogged_in", true);
                        editor.apply();
    
    
                        // Set UserLoggedIn in MyAppPrefsManager
                        MyAppPrefsManager myAppPrefsManager = new MyAppPrefsManager(Login.this);
                        myAppPrefsManager.setUserLoggedIn(true);
    
                        // Set isLogged_in of ConstantValues
                        ConstantValues.IS_GUEST_LOGGED_IN = false;
                        ConstantValues.IS_USER_LOGGED_IN = myAppPrefsManager.isUserLoggedIn();


                        if (myAppPrefsManager.isFirstTimeLaunch()) {
                            // Navigate to IntroScreen
                            startActivity(new Intent(Login.this, MainActivity.class));
                           // startActivity(new Intent(getBaseContext(), IntroScreen.class));
                            finish();
                        }else {
                            // Navigate back to MainActivity
                            if (!cartNavigation) {
                                startActivity(new Intent(Login.this, MainActivity.class));
                            }
                            finish();
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_right);
                        }
                        
                    }
                    else if ("ok".equalsIgnoreCase(response.body().getStatus())) {
                        if (response.body().getMsg() != null)
                            Snackbar.make(parentView, response.body().getMsg(), Snackbar.LENGTH_SHORT).show();
                    }
                    else {
                        if (response.body().getError() != null)
                            Snackbar.make(parentView, response.body().getError(), Snackbar.LENGTH_SHORT).show();
                    }

                }
                else {
                    Converter<ResponseBody, UserData> converter = APIClient.retrofit.responseBodyConverter(UserData.class, new Annotation[0]);
                    UserData userData;
                    try {
                        userData = converter.convert(response.errorBody());
                    } catch (IOException e) {
                        userData = new UserData();
                    }
    
                    Toast.makeText(Login.this, "Error : "+userData.getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                dialogLoader.hideProgressDialog();
                Toast.makeText(Login.this, "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
            }
        });
    }
    
    
    
    //*********** Proceed Forgot Password Request ********//

    private void processForgotPassword(String email) {
    
        dialogLoader.showProgressDialog();

        Call<UserData> call = APIClient.getInstance()
                .processForgotPassword
                        (
                                "cool",
                                email
                        );

        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
    
                dialogLoader.hideProgressDialog();
                
                if (response.isSuccessful()) {
                    // Show the Response Message
                    if (response.body().getMsg() != null) {
                        Snackbar.make(parentView, response.body().getMsg(), Snackbar.LENGTH_LONG).show();
                    }
                    else if (response.body().getError() != null) {
                        Snackbar.make(parentView, response.body().getError(), Snackbar.LENGTH_LONG).show();
                    }

                }
                else {
                    Converter<ResponseBody, ErrorResponse> converter = APIClient.retrofit.responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                    ErrorResponse error;
                    try {
                        error = converter.convert(response.errorBody());
                    } catch (IOException e) {
                        error = new ErrorResponse();
                    }
    
                    Toast.makeText(Login.this, "Error : "+error.getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                dialogLoader.hideProgressDialog();
                Toast.makeText(Login.this, "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
            }
        });
    }


    //*********** Validate Login Form Inputs ********//

    private boolean validateLogin() {
        if (!ValidateInputs.isValidName(user_email.getText().toString().trim())) {
            user_email.setError(getString(R.string.invalid_first_name));
            return false;
        }
        else if (!ValidateInputs.isValidPassword(user_password.getText().toString().trim())) {
            user_password.setError(getString(R.string.invalid_password));
            return false;
        }
        else {
            return true;
        }
    }
    
    
    
    //*********** Set the Base Context for the ContextWrapper ********//

    @Override
    protected void attachBaseContext(Context newBase) {
    
        String languageCode = ConstantValues.LANGUAGE_CODE;
        if ("".equalsIgnoreCase(languageCode))
            languageCode = ConstantValues.LANGUAGE_CODE ="fr";
        super.attachBaseContext(LocaleHelper.wrapLocale(newBase, languageCode));
    }

    //*********** Called when the Activity has detected the User pressed the Back key ********//
    
    @Override
    public void onBackPressed() {
        finish();
    }
    
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            
            case android.R.id.home:

                finish();
                
                return true;
            
            default:
                return super.onOptionsItemSelected(item);
                
        }
    }
    
}

