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
import com.purpuligo.ajeevikafarmfresh.Presenter.PaymentOptionPresenter;
import com.purpuligo.ajeevikafarmfresh.View.PaymentOptionView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PaymentOptionInteractorImpl implements PaymentOptionPresenter{

    private String TAG = "PaymentOptionInteractorImpl";
    private PaymentOptionView paymentOptionView;
    private ArrayList<AddressDetails> addressList;
    private String notAvailable = "";

    public PaymentOptionInteractorImpl(PaymentOptionView paymentOptionView) {
        this.paymentOptionView = paymentOptionView;
        addressList = new ArrayList<>();
    }

    @Override
    public void fetchAddressListFromServer(String customer_email) {

        paymentOptionView.showProgressBar();
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
                paymentOptionView.hideProgressBar();
                paymentOptionView.showServerError();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.d(TAG, "OnError: " + 12);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue((Context) this.paymentOptionView);
        requestQueue.add(stringRequest);
    }

    private void fetchAddressListFromJson(JSONObject response) throws JSONException {
        Log.d(TAG, "fetchAddressListFromJson: "+response);

        JSONObject parentObject = new JSONObject(response.toString());
        if (parentObject.has(Constants.PAYMENT_OPTION.KEY_SHIPPING_ADDRESS) && !parentObject.isNull(Constants.PAYMENT_OPTION.KEY_SHIPPING_ADDRESS) &&
                parentObject.getString(Constants.PAYMENT_OPTION.KEY_SHIPPING_ADDRESS).length()>0){
            JSONArray rootArray = parentObject.getJSONArray(Constants.PAYMENT_OPTION.KEY_SHIPPING_ADDRESS);
            //for (int i=0; i<rootArray.length(); i++){
            if (parentObject.getJSONArray(Constants.PAYMENT_OPTION.KEY_SHIPPING_ADDRESS).length()>0){
                paymentOptionView.showAddressDetails();
                JSONObject rootObject = rootArray.getJSONObject(0);
                AddressDetails addressDetails = new AddressDetails();
                if (rootObject.has(Constants.PAYMENT_OPTION.KEY_ID_SHIPPING_ADDRESS) && !rootObject.isNull(Constants.PAYMENT_OPTION.KEY_ID_SHIPPING_ADDRESS) &&
                        rootObject.getString(Constants.PAYMENT_OPTION.KEY_ID_SHIPPING_ADDRESS).length()>0){
                    paymentOptionView.showAddressId(rootObject.getString(Constants.PAYMENT_OPTION.KEY_ID_SHIPPING_ADDRESS));
                    //addressDetails.setId_shipping_address(rootObject.getString(Constants.PAYMENT_OPTION.KEY_ID_SHIPPING_ADDRESS));
                }else {}
                if (rootObject.has(Constants.PAYMENT_OPTION.KEY_FIRST_NAME) && !rootObject.isNull(Constants.PAYMENT_OPTION.KEY_FIRST_NAME) &&
                        rootObject.getString(Constants.PAYMENT_OPTION.KEY_FIRST_NAME).length()>0){
                    paymentOptionView.showFirstName(rootObject.getString(Constants.PAYMENT_OPTION.KEY_FIRST_NAME));
                    //addressDetails.setFirstname(rootObject.getString(Constants.PAYMENT_OPTION.KEY_FIRST_NAME));
                }else {}
                if (rootObject.has(Constants.PAYMENT_OPTION.KEY_LAST_NAME) && !rootObject.isNull(Constants.PAYMENT_OPTION.KEY_LAST_NAME) &&
                        rootObject.getString(Constants.PAYMENT_OPTION.KEY_LAST_NAME).length()>0){
                    paymentOptionView.showLastName(rootObject.getString(Constants.PAYMENT_OPTION.KEY_LAST_NAME));
                    //addressDetails.setLastname(rootObject.getString(Constants.PAYMENT_OPTION.KEY_LAST_NAME));
                }else {}
                if (rootObject.has(Constants.PAYMENT_OPTION.KEY_COMPANY) && !rootObject.isNull(Constants.PAYMENT_OPTION.KEY_COMPANY) &&
                        rootObject.getString(Constants.PAYMENT_OPTION.KEY_COMPANY).length()>0){
                    //addressDetails.setCompany(rootObject.getString(Constants.PAYMENT_OPTION.KEY_COMPANY));
                }else {}
                if (rootObject.has(Constants.PAYMENT_OPTION.KEY_MOBILE_NUMBER) && !rootObject.isNull(Constants.PAYMENT_OPTION.KEY_MOBILE_NUMBER) &&
                        rootObject.getString(Constants.PAYMENT_OPTION.KEY_MOBILE_NUMBER).length()>0){
                    paymentOptionView.showMobile(rootObject.getString(Constants.PAYMENT_OPTION.KEY_MOBILE_NUMBER));
                    //addressDetails.setMobile_no(rootObject.getString(Constants.PAYMENT_OPTION.KEY_MOBILE_NUMBER));
                }else {}
                if (rootObject.has(Constants.PAYMENT_OPTION.KEY_ADDRESS) && !rootObject.isNull(Constants.PAYMENT_OPTION.KEY_ADDRESS) &&
                        rootObject.getString(Constants.PAYMENT_OPTION.KEY_ADDRESS).length()>0){
                    paymentOptionView.showAddress(rootObject.getString(Constants.PAYMENT_OPTION.KEY_ADDRESS)
                            +" , "+rootObject.getString(Constants.PAYMENT_OPTION.KEY_LANDMARK));
                    //addressDetails.setAddress_1(rootObject.getString(Constants.PAYMENT_OPTION.KEY_ADDRESS));
                }else {}
                if (rootObject.has(Constants.PAYMENT_OPTION.KEY_LANDMARK) && !rootObject.isNull(Constants.PAYMENT_OPTION.KEY_LANDMARK) &&
                        rootObject.getString(Constants.PAYMENT_OPTION.KEY_LANDMARK).length()>0){
                    paymentOptionView.showLandmark(rootObject.getString(Constants.PAYMENT_OPTION.KEY_LANDMARK));
                    //addressDetails.setAddress_2(rootObject.getString(Constants.PAYMENT_OPTION.KEY_LANDMARK));
                }else {}
                if (rootObject.has(Constants.PAYMENT_OPTION.KEY_CITY) && !rootObject.isNull(Constants.PAYMENT_OPTION.KEY_CITY) &&
                        rootObject.getString(Constants.PAYMENT_OPTION.KEY_CITY).length()>0){
                    //addressDetails.setCity(rootObject.getString(Constants.PAYMENT_OPTION.KEY_CITY));
                    paymentOptionView.showCity(rootObject.getString(Constants.PAYMENT_OPTION.KEY_CITY));
                }else {}
                if (rootObject.has(Constants.PAYMENT_OPTION.KEY_STATE) && !rootObject.isNull(Constants.PAYMENT_OPTION.KEY_STATE) &&
                        rootObject.getString(Constants.PAYMENT_OPTION.KEY_STATE).length()>0){
                    paymentOptionView.showState(rootObject.getString(Constants.PAYMENT_OPTION.KEY_STATE));
                    //addressDetails.setState(rootObject.getString(Constants.PAYMENT_OPTION.KEY_STATE));
                }else {}
                if (rootObject.has(Constants.PAYMENT_OPTION.KEY_COUNTRY) && !rootObject.isNull(Constants.PAYMENT_OPTION.KEY_COUNTRY) &&
                        rootObject.getString(Constants.PAYMENT_OPTION.KEY_COUNTRY).length()>0){
                    addressDetails.setCountry(rootObject.getString(Constants.PAYMENT_OPTION.KEY_COUNTRY));
                }else {}
                if (rootObject.has(Constants.PAYMENT_OPTION.KEY_POSTAL_CODE) && !rootObject.isNull(Constants.PAYMENT_OPTION.KEY_POSTAL_CODE) &&
                        rootObject.getString(Constants.PAYMENT_OPTION.KEY_POSTAL_CODE).length()>0){
                    paymentOptionView.showPostalCode(rootObject.getString(Constants.PAYMENT_OPTION.KEY_POSTAL_CODE));
                    //addressDetails.setPostcode(rootObject.getString(Constants.PAYMENT_OPTION.KEY_POSTAL_CODE));
                }else {}
                if (rootObject.has(Constants.PAYMENT_OPTION.KEY_ALIAS) && !rootObject.isNull(Constants.PAYMENT_OPTION.KEY_ALIAS) &&
                        rootObject.getString(Constants.PAYMENT_OPTION.KEY_ALIAS).length()>0){
                    //addressDetails.setAlias(rootObject.getString(Constants.PAYMENT_OPTION.KEY_ALIAS));
                }else {}
                //addressList.add(addressDetails);
                //Log.d(TAG, "fetchAddressListFromJson: "+addressDetails);
                //Log.d(TAG, "fetchAddressListFromJson: "+addressList);
                //}
                //paymentOptionView.showAddressList(addressList);
                paymentOptionView.hideProgressBar();
            }else {
                paymentOptionView.hideProgressBar();
                paymentOptionView.hideAddressDetails();}
        }else {
            paymentOptionView.hideProgressBar();
            //paymentOptionView.showAddressListError();
        }
    }
}
