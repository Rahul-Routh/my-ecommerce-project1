package com.purpuligo.pcweb.Model;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.purpuligo.pcweb.Constants.Constants;
import com.purpuligo.pcweb.Global.Url;
import com.purpuligo.pcweb.Global.UserSessionManager;
import com.purpuligo.pcweb.Presenter.LoginPresenter;
import com.purpuligo.pcweb.View.LoginView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginInteractorImpl implements LoginPresenter{

    private String TAG = "LoginInteractorImpl";
    private Context context;
    private LoginView loginView;
    private UserSessionManager userSessionManager;

    public LoginInteractorImpl(LoginView loginView) {
        this.loginView = loginView;
        userSessionManager = new UserSessionManager((Context) this.loginView);
    }

    @Override
    public void validateLoginDetails(final String username, final String password) {
        loginView.showProgress();
        if (TextUtils.isEmpty(username))
            loginView.setUsernameError();
        else if (TextUtils.isEmpty(password))
            loginView.setPasswordError();
        else if (username.length()>0 && password.length()>0){
//            new android.os.Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
                    getDataFromServer(username,password);
//                }
//            },2000);
        }else {
            loginView.hideProgress();
            loginView.showAlert("Invalid");
        }
    }

    private void getDataFromServer(final String username, final String password){

        //String JSON_URL = "http://166.62.52.188/Murighonto_new/index.php?route=restapi/account/login";
        String JSON_URL = Url.LOGIN_URL;
        Log.d(TAG, "postUrl: "+JSON_URL);

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put(Constants.LOGIN_SUBMIT_DETAILS.KEY_EMAIL, username);
        postParam.put(Constants.LOGIN_SUBMIT_DETAILS.KEY_PASSWORD, password);

        JSONObject postData = new JSONObject(postParam);
        Log.d(TAG, "requestJsonObject: "+postData);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, JSON_URL,postData,
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

                                    userSessionManager.createUserSession(username,password,customer_id,customer_name,wishlist_count,session_data,
                                            cart_count,address_one,address_two,city,state,postal_code);

                                    loginView.hideProgress();
                                    loginView.loginSuccess(customer_name);
                                }else {
                                    loginView.hideProgress();
                                    loginView.loginFailure();
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
                loginView.hideProgress();
                loginView.showServerError();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("error", "OnError: " + 12);
                Log.d("error1", "OnError1: " + error.getStackTrace());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue((Context) this.loginView);
        requestQueue.add(stringRequest);
    }
}
