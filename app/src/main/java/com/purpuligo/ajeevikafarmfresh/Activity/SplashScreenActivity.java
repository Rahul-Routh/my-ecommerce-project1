package com.purpuligo.ajeevikafarmfresh.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.purpuligo.ajeevikafarmfresh.Constants.Constants;
import com.purpuligo.ajeevikafarmfresh.Global.NetworkState;
import com.purpuligo.ajeevikafarmfresh.Global.UserSessionManager;
import com.purpuligo.ajeevikafarmfresh.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashScreenActivity extends AppCompatActivity {

    private String TAG = "SplashScreenActivity";
    private static final int TIME = 2000;
    private UserSessionManager userSessionManager;
    private SharedPreferences sharedPreferences;
    private NetworkState networkState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        networkState = new NetworkState();
        userSessionManager = new UserSessionManager(this);
        sharedPreferences = getSharedPreferences(Constants.USER_SESSION_MANAGEMENT.KEY_PREFER_NAME,MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //if (userSessionManager.isUserLoggedIn() == false){
                    //Intent i=new Intent(SplashScreenActivity.this,OnBoardingScreenActivity.class);
                    //startActivity(i);
                    //finish();
                //}else {
                    //if (networkState.isNetworkAvailable(SplashScreenActivity.this)){
                        //if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                            //getLoginDataFromServer(sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,""),
                                   // sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_PASSWORD,""));
                        //}else {
                            navigateToHome();
                        //}
                    //}else Toast.makeText(SplashScreenActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();{}
               // }
            }
        },TIME);
    }

    private void getLoginDataFromServer(final String email, final String password){

        String JSON_URL = "http://166.62.52.188/Murighonto_new/index.php?route=restapi/account/login";
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put(Constants.LOGIN_SUBMIT_DETAILS.KEY_EMAIL, email);
        postParam.put(Constants.LOGIN_SUBMIT_DETAILS.KEY_PASSWORD, password);

        JSONObject postData = new JSONObject(postParam);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, JSON_URL, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "getDataFromServer: "+response.toString());
                        if (response.length()>0){
                            try {
                                JSONObject jsonObject = new JSONObject(response.toString());
                                JSONObject login_user = jsonObject.getJSONObject(Constants.LOGIN_DETAILS.KEY_LOGIN_USER);
                                String status = login_user.getString(Constants.LOGIN_DETAILS.KEY_STATUS);
                                if (status.equalsIgnoreCase("success")){
                                    userSessionManager.logoutUser();
                                    String customer_email = email;
                                    String customer_password = password;
                                    String customer_id = login_user.getString(Constants.LOGIN_DETAILS.KEY_CUSTOMER_ID);
                                    String customer_name = login_user.getString(Constants.LOGIN_DETAILS.KEY_CUSTOMER_NAME);
                                    int wishlist_count = login_user.getInt(Constants.LOGIN_DETAILS.KEY_WISHLIST_COUNT);
                                    String session_data = login_user.getString(Constants.LOGIN_DETAILS.KEY_SESSION_DATA);
                                    int cart_count = login_user.getInt(Constants.LOGIN_DETAILS.KEY_CART_COUNT);
                                    JSONObject address = jsonObject.getJSONObject(Constants.LOGIN_DETAILS.KEY_ADDRESS);
                                    String address_one = address.getString(Constants.LOGIN_DETAILS.KEY_ADDRESS_ONE);
                                    String address_two = address.getString(Constants.LOGIN_DETAILS.KEY_ADDRESS_TWO);
                                    String city = address.getString(Constants.LOGIN_DETAILS.KEY_CITY);
                                    String state = address.getString(Constants.LOGIN_DETAILS.KEY_STATE);
                                    String postal_code = address.getString(Constants.LOGIN_DETAILS.KEY_POSTAl_CODE);

                                    userSessionManager.createUserSession(customer_email,customer_password,customer_id,customer_name,
                                            wishlist_count,session_data,cart_count,address_one,address_two,city,state,postal_code);
                                    navigateToHome();
                                }
                                Log.d(TAG, "onResponse: "+status);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("error", "OnError: " + 12);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void navigateToHome(){
        Intent loginActivity = new Intent(SplashScreenActivity.this, HomeActivity.class);
        loginActivity.putExtra("customer_name","Guest");
        loginActivity.putExtra("show_delivery_dialog","1");
        startActivity(loginActivity);
        finish();
    }
}
