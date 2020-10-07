package com.purpuligo.ajeevikafarmfresh.Model;

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
import com.purpuligo.ajeevikafarmfresh.Constants.Constants;
import com.purpuligo.ajeevikafarmfresh.Global.Url;
import com.purpuligo.ajeevikafarmfresh.Global.UserSessionManager;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.StateDetails;
import com.purpuligo.ajeevikafarmfresh.Presenter.SignupPresenter;
import com.purpuligo.ajeevikafarmfresh.View.SignupView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignupInteractorImpl implements SignupPresenter {

    private String TAG = "SignupInteractorImpl";
    private String newsLetter;
    private SignupView signupView;
    private String namePattern = "^[\\p{L} .'-]+$";
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ArrayList<StateDetails> stateDetailsList;
    private UserSessionManager userSessionManager;

    public SignupInteractorImpl(SignupView signupView) {
        this.signupView = signupView;

        stateDetailsList = new ArrayList<>();
        userSessionManager = new UserSessionManager((Context) this.signupView);
    }

    @Override
    public void validateSignupDetails(String firstName, String lastName, String email, String telephone, String address1, String address2,
                                      String city,String postalCode, String state, String country, String password, String repeatPassword) {
        if (TextUtils.isEmpty(firstName))
            signupView.setFirstNameError();
        else if (TextUtils.isEmpty(lastName))
            signupView.setLastNameError();
        else if (TextUtils.isEmpty(email))
            signupView.setEmailError();
        else if (TextUtils.isEmpty(telephone))
            signupView.setTelephoneError();
        else if (TextUtils.isEmpty(address1))
            signupView.setAddress1Error();
        else if (TextUtils.isEmpty(address2))
            signupView.setAddress2Error();
        else if (TextUtils.isEmpty(city))
            signupView.setCityError();
        else if (TextUtils.isEmpty(postalCode))
            signupView.setPostalCodeError();
        else if (TextUtils.isEmpty(password))
            signupView.setPasswordError();
        else if (TextUtils.isEmpty(repeatPassword))
            signupView.setRepeatPasswordError();
        else if (firstName.length() > 0){
            if (password.equalsIgnoreCase(repeatPassword)){
                if (firstName.matches(namePattern)){
                    if (lastName.matches(namePattern)){
                        if (email.matches(emailPattern)){
                            if (telephone.length() == 10){
                                sendSignUpDataOnServer(firstName,lastName,email,telephone,address1,address2,city,state,country,postalCode,password);
                            }else {signupView.setTelephoneValidation();}
                        }else {signupView.setEmailValidation();}
                    }else {signupView.setLastNameValidation();}
                }else { signupView.setFirstNameValidation();}
            }else {
                signupView.setMatchPasswordError();
            }
        }
        else {
            signupView.showAlert("Invalid");
        }
    }

    private void sendSignUpDataOnServer(String firstName, String lastName, final String email, String telephone, String address1, String address2,
                                        String city, String state, String country, String postalCode, final String password){

        try {
            signupView.showProgress();
            //String JSON_URL = "http://166.62.52.188/Murighonto_new/index.php?route=restapi/account/usersignup/appRegisterUser";
            String JSON_URL = Url.SIGN_UP_URL;
            Log.d(TAG, "sendSignUpDataOnServer 1: "+JSON_URL);

            JSONObject signUpData = new JSONObject();
            signUpData.put(Constants.SIGN_UP_SUBMIT_DETAILS.KEY_FIRST_NAME,firstName);
            signUpData.put(Constants.SIGN_UP_SUBMIT_DETAILS.KEY_LAST_NAME, lastName);
            signUpData.put(Constants.SIGN_UP_SUBMIT_DETAILS.KEY_EMAIL, email);
            signUpData.put(Constants.SIGN_UP_SUBMIT_DETAILS.KEY_TELEPHONE, telephone);
            signUpData.put(Constants.SIGN_UP_SUBMIT_DETAILS.KEY_ADDRESS, address1);
            signUpData.put(Constants.SIGN_UP_SUBMIT_DETAILS.KEY_LANDMARK, address2);
            signUpData.put(Constants.SIGN_UP_SUBMIT_DETAILS.KEY_POSTAL_CODE, postalCode);
            signUpData.put(Constants.SIGN_UP_SUBMIT_DETAILS.KEY_PASSWORD, password);
            signUpData.put(Constants.SIGN_UP_SUBMIT_DETAILS.KEY_CITY,city);
            signUpData.put(Constants.SIGN_UP_SUBMIT_DETAILS.KEY_COUNTRY,(int)Double.parseDouble(country));
            signUpData.put(Constants.SIGN_UP_SUBMIT_DETAILS.KEY_ZONE_ID,(int)Double.parseDouble(state));
            Log.d(TAG, "sendSignUpDataOnServer 2: "+signUpData);

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, JSON_URL,signUpData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "getDataFromServer: "+response.toString());
                            if (response.length()>0){
                                try {
                                    JSONObject rootObject = new JSONObject(response.toString());
                                    JSONObject signup_user = rootObject.getJSONObject("signup_user");
                                    if (signup_user.getString("status").equalsIgnoreCase("success")){
                                        signupView.hideProgress();
                                        //signupView.navigateToLogin(signup_user.getString("message"));
                                        getDataFromServer(email,password);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    signupView.hideProgress();
                    signupView.showServerError();
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

            RequestQueue requestQueue = Volley.newRequestQueue((Context) this.signupView);
            requestQueue.add(stringRequest);
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void fetchStateDetails(String stateJson) {
        Log.d(TAG, "fetchStateDetails 1: "+stateJson);
        try {
            JSONArray rootArray = new JSONArray(stateJson);
            for (int i=0; i<rootArray.length(); i++){
                JSONObject rootObject = rootArray.getJSONObject(i);
                StateDetails stateDetails = new StateDetails();
                stateDetails.setZone_id(rootObject.getString("zone_id"));
                stateDetails.setCountry_id(rootObject.getString("country_id"));
                stateDetails.setName(rootObject.getString("name"));
                stateDetails.setCode(rootObject.getString("code"));
                stateDetails.setStatus(rootObject.getString("status"));
                stateDetailsList.add(stateDetails);
                Log.d(TAG, "fetchStateDetails 2: "+stateDetails);
            }
            signupView.showStateDetailsList(stateDetailsList);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getDataFromServer(final String username, final String password){

        signupView.showProgress();
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

                                    signupView.hideProgress();
                                    signupView.loginSuccess(customer_name);
                                }else {
                                    signupView.hideProgress();
                                    signupView.loginFailure();
                                }
                                Log.d(TAG, "onResponse: "+status);
                            } catch (JSONException e) {
                                signupView.hideProgress();
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                signupView.hideProgress();
                signupView.showServerError();
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

        RequestQueue requestQueue = Volley.newRequestQueue((Context) this.signupView);
        requestQueue.add(stringRequest);
    }
}
