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
import com.purpuligo.pcweb.Model.Pojo.StateDetails;
import com.purpuligo.pcweb.Presenter.EditUserDetailsPresenter;
import com.purpuligo.pcweb.View.EditUserDetailsView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditUserDetailsInteractorImpl implements EditUserDetailsPresenter{

    private String TAG = "EditUserDetailsInteractorImpl";
    private EditUserDetailsView editUserDetailsView;
    private String namePattern = "^[\\p{L} .'-]+$";
    private ArrayList<StateDetails> stateDetailsList;

    public EditUserDetailsInteractorImpl(EditUserDetailsView editUserDetailsView) {
        this.editUserDetailsView = editUserDetailsView;

        stateDetailsList = new ArrayList<>();
    }

    @Override
    public void validateSubmittedCustomerDetails(String customer_email, String addressId,String firstName, String lastName, String telephone,
                                                 String address1, String address2, String city, String postalCode, String state, int country) {
        if (TextUtils.isEmpty(firstName))
            editUserDetailsView.setFirstNameError();
        else if (TextUtils.isEmpty(lastName))
            editUserDetailsView.setLastNameError();
        else if (TextUtils.isEmpty(telephone))
            editUserDetailsView.setTelephoneError();
        else if (TextUtils.isEmpty(address1))
            editUserDetailsView.setAddress1Error();
        else if (TextUtils.isEmpty(address2))
            editUserDetailsView.setAddress2Error();
        else if (TextUtils.isEmpty(city))
            editUserDetailsView.setCityError();
        else if (TextUtils.isEmpty(postalCode))
            editUserDetailsView.setPostalCodeError();
        else if (firstName.length() > 0){
            if (firstName.matches(namePattern)){
                if (lastName.matches(namePattern)){
                    if (telephone.length() == 10){
                        if (postalCode.length() == 6){
                            editUserDetailsView.showProgress();
                            submitUpdatedDetails(customer_email,addressId,firstName,lastName,telephone,address1,address2,city,postalCode,state,country);
                        }else {editUserDetailsView.setPostalCodeValidation();}
                    }else {editUserDetailsView.setTelephoneValidation();}
                }else {editUserDetailsView.setLastNameValidation();}
            }else { editUserDetailsView.setFirstNameValidation();
            }
        }
    }

    private void submitUpdatedDetails(String customer_email, String addressId,String firstName, String lastName, String telephone,
                                      String address1, String address2, String city, String postalCode, String state, int country){
        //editUserDetailsView.showProgress();
        String company = "";
        //String city = "";
        //int state = 16;
        //int country = 99;

        //String JSON_URL = "http://166.62.52.188/Murighonto_new/index.php?route=restapi/checkout/checkout/updateDetailsOfShippingAddress";
        String JSON_URL = Url.EDIT_CUSTOMER_DETAILS_URL;

        JSONObject submitData = new JSONObject();
        try {
            submitData.put(Constants.UPDATE_USER_DETAIL.KEY_FIRST_NAME,firstName);
            submitData.put(Constants.UPDATE_USER_DETAIL.KEY_LAST_NAME,lastName);
            submitData.put(Constants.UPDATE_USER_DETAIL.KEY_EMAIL,customer_email);
            submitData.put(Constants.UPDATE_USER_DETAIL.KEY_ADDRESS_ID,addressId);
            submitData.put(Constants.UPDATE_USER_DETAIL.KEY_COMPANY,company);
            submitData.put(Constants.UPDATE_USER_DETAIL.KEY_PHONE,telephone);
            submitData.put(Constants.UPDATE_USER_DETAIL.KEY_ADDRESS,address1);
            submitData.put(Constants.UPDATE_USER_DETAIL.KEY_LANDMARK,address2);
            submitData.put(Constants.UPDATE_USER_DETAIL.KEY_POSTAL_CODE,postalCode);
            submitData.put(Constants.UPDATE_USER_DETAIL.KEY_CITY,city);
            submitData.put(Constants.UPDATE_USER_DETAIL.KEY_STATE,(int)Double.parseDouble(state));
            submitData.put(Constants.UPDATE_USER_DETAIL.KEY_COUNTRY,country);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "sendUpdatedDetailsToServer: "+submitData);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, JSON_URL,submitData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "getDataFromServer: "+response.toString());
                        if (response.length()>0){
                            try {
                                JSONObject rootObject = new JSONObject(response.toString());
                                JSONObject object = rootObject.getJSONObject("shipping_address_reponse");
                                if (object.getString("status").equalsIgnoreCase("success")){
                                    editUserDetailsView.hideProgress();
                                    editUserDetailsView.onSuccess(object.getString("message"));
                                }else {
                                    editUserDetailsView.hideProgress();
                                    editUserDetailsView.onFailure(object.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        editUserDetailsView.hideProgress();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                editUserDetailsView.hideProgress();
                editUserDetailsView.showServerError();
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

        RequestQueue requestQueue = Volley.newRequestQueue((Context) this.editUserDetailsView);
        requestQueue.add(stringRequest);
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
            editUserDetailsView.setStateDetailsList(stateDetailsList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
