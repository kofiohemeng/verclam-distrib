package com.verclamdistrib.woocomerce.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.verclamdistrib.woocomerce.customs.CircularImageView;

import com.verclamdistrib.woocomerce.R;

import com.verclamdistrib.woocomerce.constant.ConstantValues;
import com.verclamdistrib.woocomerce.customs.DialogLoader;
import com.verclamdistrib.woocomerce.models.device_model.DeviceInfo;
import com.verclamdistrib.woocomerce.models.state_model.State;
import com.verclamdistrib.woocomerce.models.state_model.StateResponse;
import com.verclamdistrib.woocomerce.models.user_model.Nonce;
import com.verclamdistrib.woocomerce.models.user_model.UserData;
import com.verclamdistrib.woocomerce.utils.LocaleHelper;
import com.verclamdistrib.woocomerce.network.APIClient;
import com.verclamdistrib.woocomerce.utils.Utilities;
import com.verclamdistrib.woocomerce.utils.ValidateInputs;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;


/**
 * SignUp activity handles User's Registration
 **/

public class Signup extends AppCompatActivity {

    View parentView;
    String registerNonce = "";

    Toolbar toolbar;
    ActionBar actionBar;

    DialogLoader dialogLoader;
    
    AdView mAdView;
    Button signupBtn;
    FrameLayout banner_adView;
    TextView signup_loginText;
    CircularImageView user_photo;
    FloatingActionButton user_photo_edit_fab;
    TextView service_terms, privacy_policy, refund_policy, and_text;
    EditText user_firstname, user_lastname, username, user_email, user_password,
            user_company, user_address1, user_address2, user_city, user_state, user_phone;

    private Spinner spnCity,spnState;
    private HashMap<String,List<String>> statesMap;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
    
        MobileAds.initialize(this, ConstantValues.ADMOBE_ID);

        DeviceInfo info =  Utilities.getDeviceInfo(Signup.this);

        // setting Toolbar
        toolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.signup));
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        // Binding Layout Views
        user_photo = (CircularImageView) findViewById(R.id.user_photo);
        user_firstname = (EditText) findViewById(R.id.user_firstname);
        user_lastname = (EditText) findViewById(R.id.user_lastname);
        username = (EditText) findViewById(R.id.username);
        user_email = (EditText) findViewById(R.id.user_email);
        user_password = (EditText) findViewById(R.id.user_password);
        user_company = (EditText) findViewById(R.id.company);
        user_address1 = (EditText) findViewById(R.id.address_1);
        user_address2 = (EditText) findViewById(R.id.address_2);
        user_city = (EditText) findViewById(R.id.city);
        user_state = (EditText) findViewById(R.id.state);
        user_phone = (EditText) findViewById(R.id.phone);
        signupBtn = (Button) findViewById(R.id.signupBtn);
        and_text = (TextView) findViewById(R.id.and);
        service_terms = (TextView) findViewById(R.id.service_terms);
        privacy_policy = (TextView) findViewById(R.id.privacy_policy);
        refund_policy = (TextView) findViewById(R.id.refund_policy);
        signup_loginText = (TextView) findViewById(R.id.signup_loginText);
        banner_adView = (FrameLayout) findViewById(R.id.banner_adView);
        user_photo_edit_fab = (FloatingActionButton) findViewById(R.id.user_photo_edit_fab);
    
        spnCity = findViewById(R.id.spn_city);
        spnState = findViewById(R.id.spn_state);

        user_photo.setVisibility(View.GONE);
        user_photo_edit_fab.setVisibility(View.GONE);
        user_address1.setText(info.getDeviceLocation());
        
        if (ConstantValues.IS_ADMOBE_ENABLED) {
            // Initialize Admobe
            mAdView = new AdView(Signup.this);
            mAdView.setAdSize(AdSize.BANNER);
            mAdView.setAdUnitId(ConstantValues.AD_UNIT_ID_BANNER);
            AdRequest adRequest = new AdRequest.Builder().build();
            banner_adView.addView(mAdView);
            mAdView.loadAd(adRequest);
            mAdView.setAdListener(new AdListener(){
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    banner_adView.setVisibility(View.VISIBLE);
                }
                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    banner_adView.setVisibility(View.GONE);
                }
            });
        }
    
    
        and_text.setText(" "+getString(R.string.and)+" ");
        
        dialogLoader = new DialogLoader(Signup.this);
    
    
        getRegisterationNonce();
    
        
        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Signup.this, android.R.style.Theme_NoTitleBar);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_webview_fullscreen, null);
                dialog.setView(dialogView);
                dialog.setCancelable(true);
    
                final ImageButton dialog_button = (ImageButton) dialogView.findViewById(R.id.dialog_button);
                final TextView dialog_title = (TextView) dialogView.findViewById(R.id.dialog_title);
                final WebView dialog_webView = (WebView) dialogView.findViewById(R.id.dialog_webView);
            
                dialog_title.setText(getString(R.string.privacy_policy));
            
            
                String description = ConstantValues.PRIVACY_POLICY;
                String styleSheet = "<style> " +
                                        "body{background:#eeeeee; margin:10; padding:10} " +
                                        "p{color:#757575;} " +
                                        "img{display:inline; height:auto; max-width:100%;}" +
                                    "</style>";
    
                dialog_webView.setVerticalScrollBarEnabled(true);
                dialog_webView.setHorizontalScrollBarEnabled(false);
                dialog_webView.setBackgroundColor(Color.TRANSPARENT);
                dialog_webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                dialog_webView.loadDataWithBaseURL(null, styleSheet+description, "text/html", "utf-8", null);
            
            
                final AlertDialog alertDialog = dialog.create();
    
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    alertDialog.getWindow().setStatusBarColor(ContextCompat.getColor(Signup.this, R.color.colorPrimaryDark));
                }
            
                dialog_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            
                alertDialog.show();
            
            }
        });
    
        refund_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Signup.this, android.R.style.Theme_NoTitleBar);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_webview_fullscreen, null);
                dialog.setView(dialogView);
                dialog.setCancelable(true);
    
                final ImageButton dialog_button = (ImageButton) dialogView.findViewById(R.id.dialog_button);
                final TextView dialog_title = (TextView) dialogView.findViewById(R.id.dialog_title);
                final WebView dialog_webView = (WebView) dialogView.findViewById(R.id.dialog_webView);
            
                dialog_title.setText(getString(R.string.refund_policy));
            
            
                String description = ConstantValues.REFUND_POLICY;
                String styleSheet = "<style> " +
                                        "body{background:#eeeeee; margin:10; padding:10} " +
                                        "p{color:#757575;} " +
                                        "img{display:inline; height:auto; max-width:100%;}" +
                                    "</style>";
    
                dialog_webView.setVerticalScrollBarEnabled(true);
                dialog_webView.setHorizontalScrollBarEnabled(false);
                dialog_webView.setBackgroundColor(Color.TRANSPARENT);
                dialog_webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                dialog_webView.loadDataWithBaseURL(null, styleSheet+description, "text/html", "utf-8", null);
            
            
                final AlertDialog alertDialog = dialog.create();
    
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    alertDialog.getWindow().setStatusBarColor(ContextCompat.getColor(Signup.this, R.color.colorPrimaryDark));
                }
            
                dialog_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            
                alertDialog.show();
            }
        });
    
        service_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Signup.this, android.R.style.Theme_NoTitleBar);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_webview_fullscreen, null);
                dialog.setView(dialogView);
                dialog.setCancelable(true);
    
                final ImageButton dialog_button = (ImageButton) dialogView.findViewById(R.id.dialog_button);
                final TextView dialog_title = (TextView) dialogView.findViewById(R.id.dialog_title);
                final WebView dialog_webView = (WebView) dialogView.findViewById(R.id.dialog_webView);
            
                dialog_title.setText(getString(R.string.service_terms));
            
            
                String description = ConstantValues.TERMS_SERVICES;
                String styleSheet = "<style> " +
                                        "body{background:#eeeeee; margin:10; padding:10} " +
                                        "p{color:#757575;} " +
                                        "img{display:inline; height:auto; max-width:100%;}" +
                                    "</style>";
    
                dialog_webView.setVerticalScrollBarEnabled(true);
                dialog_webView.setHorizontalScrollBarEnabled(false);
                dialog_webView.setBackgroundColor(Color.TRANSPARENT);
                dialog_webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                dialog_webView.loadDataWithBaseURL(null, styleSheet+description, "text/html", "utf-8", null);
            
            
                final AlertDialog alertDialog = dialog.create();
    
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    alertDialog.getWindow().setStatusBarColor(ContextCompat.getColor(Signup.this, R.color.colorPrimaryDark));
                }
            
                dialog_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            
                alertDialog.show();
            }
        });


        // Handle Click event of signup_loginText TextView
        signup_loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish SignUpActivity to goto the LoginActivity
                finish();
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_right);
            }
        });


        // Handle Click event of signupBtn Button
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate Login Form Inputs
                boolean isValidData = validateForm();

                if (isValidData) {
                    parentView = v;
                    
                    if (!TextUtils.isEmpty(registerNonce))
                        processRegistration();
                    
                }
            }
        });
        spnState.setPrompt("Select state");
        spnCity.setPrompt("Select city");
        getStates();
    }


    
    //*********** Proceed User Registration Request ********//
    
    private void getRegisterationNonce() {
    
        Map<String, String> params = new LinkedHashMap<>();
        params.put("controller", "AndroidAppUsers");
        params.put("method", "android_register");
        
        Call<Nonce> call = APIClient.getInstance()
                .getNonce
                        (
                                params
                        );
        
        call.enqueue(new Callback<Nonce>() {
            @Override
            public void onResponse(Call<Nonce> call, retrofit2.Response<Nonce> response) {
                
                // Check if the Response is successful
                if (response.isSuccessful()) {
                    
                    if (!TextUtils.isEmpty(response.body().getNonce())) {
                        registerNonce = response.body().getNonce();
                    }
                    else {
                        Toast.makeText(Signup.this, "Nonce is Empty", Toast.LENGTH_SHORT).show();
                    }
                    
                }
                else {
                    Toast.makeText(Signup.this, "Nonce is Empty", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<Nonce> call, Throwable t) {
                Toast.makeText(Signup.this, "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
            }
        });
    }
    
    
    
    //*********** Proceed User Registration Request ********//

    private void processRegistration() {
    
        dialogLoader.showProgressDialog();
        
        Call<UserData> call = APIClient.getInstance()
                .processRegistration
                        (
                                "cool",
                                user_firstname.getText().toString().trim(),
                                user_lastname.getText().toString().trim(),
                                username.getText().toString().trim(),
                                user_email.getText().toString().trim(),
                                user_password.getText().toString().trim(),
                                user_company.getText().toString(),
                                user_address1.getText().toString(),
                                user_address2.getText().toString(),
                                spnCity.getSelectedItem().toString(),
                                spnState.getSelectedItem().toString(),
                                user_phone.getText().toString(),
                                registerNonce
                        );

        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, retrofit2.Response<UserData> response) {

                dialogLoader.hideProgressDialog();

                // Check if the Response is successful
                if (response.isSuccessful()) {
                    
                    if ("ok".equalsIgnoreCase(response.body().getStatus())) {
                        
                        // Finish SignUpActivity to goto the LoginActivity
                        finish();
                        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
                        
                    }
                    else if ("error".equalsIgnoreCase(response.body().getStatus())) {
                        Toast.makeText(Signup.this, response.body().getError(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(Signup.this, getString(R.string.unexpected_response), Toast.LENGTH_SHORT).show();
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
    
                    Toast.makeText(Signup.this, "Error : "+userData.getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                dialogLoader.hideProgressDialog();
                Toast.makeText(Signup.this, "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getStates(){

//        Call<ResponseBody> myCall = APIClient.getInstance().getStates();
//        myCall.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                System.out.println("response code" + response.code());
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });

        dialogLoader.showProgressDialog();
        Call<StateResponse> stateResponseCall = APIClient.getInstance().getStates();
        stateResponseCall.enqueue(new Callback<StateResponse>() {
            @Override
            public void onResponse(Call<StateResponse> call, Response<StateResponse> response) {
                dialogLoader.hideProgressDialog();
                arrangeStatesWithCity(response.body());
                displayCityState();
            }

            @Override
            public void onFailure(Call<StateResponse> call, Throwable t) {
                dialogLoader.hideProgressDialog();
            }
        });
    }

    //*********** Validate SignUp Form Inputs ********//

    private boolean validateForm() {
        if (!ValidateInputs.isValidName(user_firstname.getText().toString().trim())) {
            user_firstname.setError(getString(R.string.invalid_first_name));
            return false;
        } else if (!ValidateInputs.isValidName(user_lastname.getText().toString().trim())) {
            user_lastname.setError(getString(R.string.invalid_last_name));
            return false;
        } else if (!ValidateInputs.isValidInput(username.getText().toString().trim())) {
            username.setError(getString(R.string.invalid_username));
            return false;
        } else if (!ValidateInputs.isValidEmail(user_email.getText().toString().trim())) {
            user_email.setError(getString(R.string.invalid_email));
            return false;
        } else if (!ValidateInputs.isValidPassword(user_password.getText().toString().trim())) {
            user_password.setError(getString(R.string.invalid_password));
            return false;
        } else if (!ValidateInputs.isValidInput(user_company.getText().toString().trim())) {
            user_company.setError(getString(R.string.required_feild));
            return false;
        } else if (!ValidateInputs.isValidInput(user_address1.getText().toString().trim())) {
            user_address1.setError(getString(R.string.required_feild));
            return false;
        }

     //   else if (!ValidateInputs.isValidInput(spnCity.getSelectedItem().toString().trim())) {
        else if(TextUtils.isEmpty(spnCity.getSelectedItem().toString().trim())){
            Toast.makeText(Signup.this,"Select State",Toast.LENGTH_SHORT).show();
            return false;
        }
     //   else if (!ValidateInputs.isValidInput(spnState.getSelectedItem().toString().trim())) {
        else if(TextUtils.isEmpty(spnState.getSelectedItem().toString().trim())){
            Toast.makeText(Signup.this,"Select City",Toast.LENGTH_SHORT).show();
            return false;
        }

// else if (!ValidateInputs.isValidInput(user_city.getText().toString().trim())) {
//            user_city.setError(getString(R.string.required_feild));
//            return false;
//        } else if (!ValidateInputs.isValidInput(user_state.getText().toString().trim())) {
//            user_state.setError(getString(R.string.required_feild));
//            return false;
//        }
        else if (!ValidateInputs.isValidPhoneNo(user_phone.getText().toString().trim())) {
            user_phone.setError(getString(R.string.invalid_phone_no));
            return false;
        }else {
            return true;
        }
    }
    private void arrangeStatesWithCity(StateResponse stateResponse){
        if (stateResponse != null && stateResponse.getStatus().equalsIgnoreCase("ok")&&stateResponse.getState()!=null){
            List<State> states = stateResponse.getState();
            if(statesMap == null)
                statesMap = new HashMap<>();
            for (int i = 0;i<states.size();i++){
                State state = states.get(i);

                if (!statesMap.containsKey(state.getName())){
                    statesMap.put(state.getName(),new ArrayList<String>());
                }
                ArrayList<String> cities = (ArrayList<String>) statesMap.get(state.getName());

                if (!cities.contains(state.getCity())){
                    cities.add(state.getCity());
                    statesMap.put(state.getName(),cities);

                }
            }
        }
    }

    private void displayCityState(){
        if (statesMap!=null && statesMap.size()>0){
            final List<String> states = new ArrayList<>(statesMap.keySet());
            Collections.sort(states, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    int op1 = Integer.parseInt(o1.split(" ")[0]);
                    int op2 = Integer.parseInt(o2.split(" ")[0]);

                    return op1 - op2;
                }
            });

            ArrayAdapter stateAdapter = new ArrayAdapter(Signup.this,android.R.layout.simple_list_item_1,states);

         //   spnState.setPrompt("Select State");
            spnState.setAdapter(stateAdapter);
            int pos = states.indexOf(" 16 Alger");
            if (pos < 0)
                pos = 0;

            spnState.setSelection(pos);
            spnState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    List<String> cities = statesMap.get(states.get(i));
                    Collections.sort(cities);
                    ArrayAdapter cityAdapter = new ArrayAdapter(Signup.this,android.R.layout.simple_list_item_1,cities);
                    spnCity.setAdapter(cityAdapter);
                    spnCity.setSelection(0);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            List<String> cities = statesMap.get(states.get(pos));
            Collections.sort(cities);
            ArrayAdapter cityAdapter = new ArrayAdapter(Signup.this,android.R.layout.simple_spinner_dropdown_item,cities);
            spnCity.setAdapter(cityAdapter);
           // spnState.setPrompt("Select City");
        }
    }
    //*********** Set the Base Context for the ContextWrapper ********//
    
    @Override
    protected void attachBaseContext(Context newBase) {
    
        String languageCode = ConstantValues.LANGUAGE_CODE;
        if ("".equalsIgnoreCase(languageCode))
            languageCode = ConstantValues.LANGUAGE_CODE = "fr";
    
        super.attachBaseContext(LocaleHelper.wrapLocale(newBase, languageCode));
    }
    
    
    
    //*********** Called when the Activity has detected the User pressed the Back key ********//
    
    @Override
    public void onBackPressed() {
        // Finish SignUpActivity to goto the LoginActivity
        finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_right);
    }
    
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            
            case android.R.id.home:
    
                // Finish SignUpActivity to goto the LoginActivity
                finish();
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_right);
                
                return true;
            
            default:
                return super.onOptionsItemSelected(item);
            
        }
    }
}

