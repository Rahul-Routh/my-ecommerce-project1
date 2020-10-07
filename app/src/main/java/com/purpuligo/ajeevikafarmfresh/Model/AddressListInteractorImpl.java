package com.purpuligo.ajeevikafarmfresh.Model;

import android.content.Context;
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
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.AddressDetails;
import com.purpuligo.ajeevikafarmfresh.Presenter.AddressListPresenter;
import com.purpuligo.ajeevikafarmfresh.View.AddressListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddressListInteractorImpl implements AddressListPresenter {

    private String TAG = "AddressListInteractorImpl";
    private AddressListView addressListView;
    private ArrayList<AddressDetails> addressList;
    private String notAvailable = "";

    public AddressListInteractorImpl(AddressListView addressListView) {
        this.addressListView = addressListView;

        addressList = new ArrayList<>();
    }

    @Override
    public void fetchAddressListFromServer(String customer_email) {

        addressListView.showProgressBar();
        //String JSON_URL = "http://166.62.52.188/Murighonto_new/index.php?route=restapi/checkout/checkout/getAllShippingAddress";
        String JSON_URL = Url.ADDRESS_LIST_URL;
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put(Constants.PAYMENT_OPTION.KEY_CUSTOMER_EMAIL, customer_email);
        JSONObject postData = new JSONObject(postParam);
        Log.d(TAG, "fetchAddressFromServer: "+postData);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, JSON_URL,postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "getDataFromServer: "+response.toString());
                        try {
                            fetchAddressListFromJson(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                addressListView.hideProgressBar();
                addressListView.showServerError();
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

        RequestQueue requestQueue = Volley.newRequestQueue((Context) this.addressListView);
        requestQueue.add(stringRequest);
    }

    private void fetchAddressListFromJson(JSONObject response) throws JSONException {
        Log.d(TAG, "fetchAddressListFromJson: "+response);

        JSONObject parentObject = new JSONObject(response.toString());
        if (parentObject.has(Constants.PAYMENT_OPTION.KEY_SHIPPING_ADDRESS) && !parentObject.isNull(Constants.PAYMENT_OPTION.KEY_SHIPPING_ADDRESS) &&
                parentObject.getString(Constants.PAYMENT_OPTION.KEY_SHIPPING_ADDRESS).length()>0){
            JSONArray rootArray = parentObject.getJSONArray(Constants.PAYMENT_OPTION.KEY_SHIPPING_ADDRESS);
            for (int i=0; i<rootArray.length(); i++){
                JSONObject rootObject = rootArray.getJSONObject(i);
                AddressDetails addressDetails = new AddressDetails();
                if (rootObject.has(Constants.PAYMENT_OPTION.KEY_ID_SHIPPING_ADDRESS) && !rootObject.isNull(Constants.PAYMENT_OPTION.KEY_ID_SHIPPING_ADDRESS) &&
                        rootObject.getString(Constants.PAYMENT_OPTION.KEY_ID_SHIPPING_ADDRESS).length()>0){
                    addressDetails.setId_shipping_address(rootObject.getString(Constants.PAYMENT_OPTION.KEY_ID_SHIPPING_ADDRESS));
                }else {addressDetails.setId_shipping_address(notAvailable);}
                if (rootObject.has(Constants.PAYMENT_OPTION.KEY_FIRST_NAME) && !rootObject.isNull(Constants.PAYMENT_OPTION.KEY_FIRST_NAME) &&
                        rootObject.getString(Constants.PAYMENT_OPTION.KEY_FIRST_NAME).length()>0){
                    addressDetails.setFirstname(rootObject.getString(Constants.PAYMENT_OPTION.KEY_FIRST_NAME));
                }else {addressDetails.setFirstname(notAvailable);}
                if (rootObject.has(Constants.PAYMENT_OPTION.KEY_LAST_NAME) && !rootObject.isNull(Constants.PAYMENT_OPTION.KEY_LAST_NAME) &&
                        rootObject.getString(Constants.PAYMENT_OPTION.KEY_LAST_NAME).length()>0){
                    addressDetails.setLastname(rootObject.getString(Constants.PAYMENT_OPTION.KEY_LAST_NAME));
                }else {addressDetails.setLastname(notAvailable);}
                if (rootObject.has(Constants.PAYMENT_OPTION.KEY_COMPANY) && !rootObject.isNull(Constants.PAYMENT_OPTION.KEY_COMPANY) &&
                        rootObject.getString(Constants.PAYMENT_OPTION.KEY_COMPANY).length()>0){
                    addressDetails.setCompany(rootObject.getString(Constants.PAYMENT_OPTION.KEY_COMPANY));
                }else {addressDetails.setCompany(notAvailable);}
                if (rootObject.has(Constants.PAYMENT_OPTION.KEY_MOBILE_NUMBER) && !rootObject.isNull(Constants.PAYMENT_OPTION.KEY_MOBILE_NUMBER) &&
                        rootObject.getString(Constants.PAYMENT_OPTION.KEY_MOBILE_NUMBER).length()>0){
                    addressDetails.setMobile_no(rootObject.getString(Constants.PAYMENT_OPTION.KEY_MOBILE_NUMBER));
                }else {addressDetails.setMobile_no(notAvailable);}
                if (rootObject.has(Constants.PAYMENT_OPTION.KEY_ADDRESS) && !rootObject.isNull(Constants.PAYMENT_OPTION.KEY_ADDRESS) &&
                        rootObject.getString(Constants.PAYMENT_OPTION.KEY_ADDRESS).length()>0){
                    addressDetails.setAddress_1(rootObject.getString(Constants.PAYMENT_OPTION.KEY_ADDRESS));
                }else {addressDetails.setAddress_1(notAvailable);}
                if (rootObject.has(Constants.PAYMENT_OPTION.KEY_LANDMARK) && !rootObject.isNull(Constants.PAYMENT_OPTION.KEY_LANDMARK) &&
                        rootObject.getString(Constants.PAYMENT_OPTION.KEY_LANDMARK).length()>0){
                    addressDetails.setAddress_2(rootObject.getString(Constants.PAYMENT_OPTION.KEY_LANDMARK));
                }else {addressDetails.setAddress_2(notAvailable);}
                if (rootObject.has(Constants.PAYMENT_OPTION.KEY_CITY) && !rootObject.isNull(Constants.PAYMENT_OPTION.KEY_CITY) &&
                        rootObject.getString(Constants.PAYMENT_OPTION.KEY_CITY).length()>0){
                    addressDetails.setCity(rootObject.getString(Constants.PAYMENT_OPTION.KEY_CITY));
                }else {addressDetails.setCity(notAvailable);}
                if (rootObject.has(Constants.PAYMENT_OPTION.KEY_STATE) && !rootObject.isNull(Constants.PAYMENT_OPTION.KEY_STATE) &&
                        rootObject.getString(Constants.PAYMENT_OPTION.KEY_STATE).length()>0){
                    addressDetails.setState(rootObject.getString(Constants.PAYMENT_OPTION.KEY_STATE));
                }else {addressDetails.setState(notAvailable);}
                if (rootObject.has(Constants.PAYMENT_OPTION.KEY_COUNTRY) && !rootObject.isNull(Constants.PAYMENT_OPTION.KEY_COUNTRY) &&
                        rootObject.getString(Constants.PAYMENT_OPTION.KEY_COUNTRY).length()>0){
                    addressDetails.setCountry(rootObject.getString(Constants.PAYMENT_OPTION.KEY_COUNTRY));
                }else {addressDetails.setCountry(notAvailable);}
                if (rootObject.has(Constants.PAYMENT_OPTION.KEY_POSTAL_CODE) && !rootObject.isNull(Constants.PAYMENT_OPTION.KEY_POSTAL_CODE) &&
                        rootObject.getString(Constants.PAYMENT_OPTION.KEY_POSTAL_CODE).length()>0){
                    addressDetails.setPostcode(rootObject.getString(Constants.PAYMENT_OPTION.KEY_POSTAL_CODE));
                }else {addressDetails.setPostcode(notAvailable);}
                if (rootObject.has(Constants.PAYMENT_OPTION.KEY_ALIAS) && !rootObject.isNull(Constants.PAYMENT_OPTION.KEY_ALIAS) &&
                        rootObject.getString(Constants.PAYMENT_OPTION.KEY_ALIAS).length()>0){
                    addressDetails.setAlias(rootObject.getString(Constants.PAYMENT_OPTION.KEY_ALIAS));
                }else {addressDetails.setAlias(notAvailable);}
                addressList.add(addressDetails);
                //Log.d(TAG, "fetchAddressListFromJson: "+addressDetails);
                //Log.d(TAG, "fetchAddressListFromJson: "+addressList);
            }
            addressListView.showAddressList(addressList);
            addressListView.hideProgressBar();
        }else {
            addressListView.hideProgressBar();
            addressListView.showAddressListError();
        }
    }

    @Override
    public void removeAddress(String customer_email, int address_id) {
        addressListView.showProgressBar();
        String JSON_URL = Url.REMOVE_ADDRESS;
        JSONObject postData = new JSONObject();
        try {
            postData.put("email",customer_email);
            postData.put("address_id",address_id);
            Log.d(TAG, "removeAddress: "+postData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, JSON_URL,postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "getDataFromServer: "+response.toString());
                        if (response.length()>0){
                            try {
                                JSONObject rootObject = new JSONObject(response.toString());
                                JSONObject address_reponse = rootObject.getJSONObject("address_reponse");
                                if (address_reponse.getString("status").equalsIgnoreCase("Success")){
                                    addressListView.hideProgressBar();
                                    addressListView.removeAddressSuccess();
                                }else {
                                    addressListView.hideProgressBar();
                                    addressListView.removeAddressFailure();}
                            } catch (JSONException e) {
                                addressListView.hideProgressBar();
                                addressListView.removeAddressFailure();
                                e.printStackTrace();
                            }
                        }else {
                            addressListView.hideProgressBar();
                            addressListView.showServerError();}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                addressListView.hideProgressBar();
                addressListView.showServerError();
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

        RequestQueue requestQueue = Volley.newRequestQueue((Context) this.addressListView);
        requestQueue.add(stringRequest);
    }
}
