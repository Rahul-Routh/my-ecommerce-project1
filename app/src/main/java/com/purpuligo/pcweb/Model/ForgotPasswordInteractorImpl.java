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
import com.purpuligo.pcweb.Presenter.ForgotPasswordPresenter;
import com.purpuligo.pcweb.View.ForgotPasswordView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordInteractorImpl implements ForgotPasswordPresenter {

    private String TAG = "ForgotPasswordInteractorImpl";
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ForgotPasswordView forgotPasswordView;

    public ForgotPasswordInteractorImpl(ForgotPasswordView forgotPasswordView) {
        this.forgotPasswordView = forgotPasswordView;
    }

    @Override
    public void validateForgotPassword(String email, String newPassword, String repeatPassword) {
        if (TextUtils.isEmpty(email))
            forgotPasswordView.setEmailError();
        else if (TextUtils.isEmpty(newPassword))
            forgotPasswordView.setPasswordError();
        else if (TextUtils.isEmpty(repeatPassword))
            forgotPasswordView.setRepeatPasswordError();
        else if (email.length()>0){
            if (email.matches(emailPattern)){
                if (newPassword.equalsIgnoreCase(repeatPassword)){
                    updateForgotPassword(email,newPassword);
                }else {forgotPasswordView.setPasswordNotMatched();}
            }else { forgotPasswordView.setEmailFormatError(); } }
    }

    private void updateForgotPassword(String email, String newPassword){

        forgotPasswordView.showProgressBar();
        Log.d(TAG, "updateForgotPassword: "+email+"/"+newPassword);
        //String JSON_URL = "http://166.62.52.188/Murighonto_new/index.php?route=restapi/account/forgotten/appForgotPassword";
        String JSON_URL = Url.FORGOT_PASSWORD;
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put(Constants.FORGOT_PASSWORD.KEY_EMAIL, email);
        postParam.put(Constants.FORGOT_PASSWORD.KEY_NEW_PASSWORD, newPassword);

        JSONObject postData = new JSONObject(postParam);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, JSON_URL,postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "getDataFromServer: "+response.toString());
                        if (response.length()>0){
                            try {
                                JSONObject rootObject = new JSONObject(response.toString());
                                if (rootObject.getString(Constants.FORGOT_PASSWORD_RESPONSE.KEY_STATUS).equalsIgnoreCase("success")){
                                    forgotPasswordView.hideProgressBar();
                                    forgotPasswordView.onSuccess(rootObject.getString(Constants.FORGOT_PASSWORD_RESPONSE.KEY_MESSAGE));
                                }else {
                                    forgotPasswordView.hideProgressBar();
                                    forgotPasswordView.onFailure(rootObject.getString(Constants.FORGOT_PASSWORD_RESPONSE.KEY_MESSAGE));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            forgotPasswordView.hideProgressBar();
                            forgotPasswordView.onFailure("Error");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                forgotPasswordView.hideProgressBar();
                forgotPasswordView.showServerError();
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

        RequestQueue requestQueue = Volley.newRequestQueue((Context) this.forgotPasswordView);
        requestQueue.add(stringRequest);
    }
}
