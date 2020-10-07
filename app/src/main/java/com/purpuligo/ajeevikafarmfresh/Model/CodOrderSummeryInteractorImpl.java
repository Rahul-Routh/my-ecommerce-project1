package com.purpuligo.ajeevikafarmfresh.Model;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.purpuligo.ajeevikafarmfresh.Constants.Constants;
import com.purpuligo.ajeevikafarmfresh.Global.Url;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.CartItemList;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.CartPriceDetails;
import com.purpuligo.ajeevikafarmfresh.Presenter.CodOrderSummeryPresenter;
import com.purpuligo.ajeevikafarmfresh.View.CodOrderSummeryView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Response.ErrorListener;

public class CodOrderSummeryInteractorImpl implements CodOrderSummeryPresenter{

    private String TAG = "CodOrderSummeryInteractorImpl";
    private CodOrderSummeryView codOrderSummeryView;
    private ArrayList<CartItemList> checkoutProductList;
    private ArrayList<CartPriceDetails> checkoutPriceList;
    private String notAvailable = "Not Available";
    private String notAvailableAmount = "0.00";
    private String extra_charge_name,extra_charge_value;

    public CodOrderSummeryInteractorImpl(CodOrderSummeryView codOrderSummeryView) {
        this.codOrderSummeryView = codOrderSummeryView;

        checkoutProductList = new ArrayList<>();
        checkoutPriceList = new ArrayList<>();
    }

    @Override
    public void getCheckoutDetails(String address_id, String customer_email, String session_data) {

        codOrderSummeryView.showProgress();
        //String JSON_URL = "http://166.62.52.188/Murighonto_new/index.php?route=restapi/checkout/checkout/appCheckout";
        String JSON_URL = Url.CHECKOUT_DETAILS_URL;
        Log.d(TAG, "getCheckoutDetails: "+JSON_URL);

        JSONObject postData = new JSONObject();
        try {
            postData.put("id_shipping_address",address_id);
            postData.put("email",customer_email);
            postData.put("session_data",session_data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "getCheckoutDetails: "+postData);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, JSON_URL,postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "getCheckoutDetails: "+response.toString());
                        if (response.length()>0){
                            try {
                                fetchJsonData(response);
                            } catch (JSONException e) { e.printStackTrace(); }catch (Exception e){ e.printStackTrace(); }
                        }
                        codOrderSummeryView.hideProgress();
                    }
                }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                codOrderSummeryView.hideProgress();
                codOrderSummeryView.showServerError();
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

        RequestQueue requestQueue = Volley.newRequestQueue((Context) this.codOrderSummeryView);
        requestQueue.add(stringRequest);
    }

    private void fetchJsonData(JSONObject response) throws JSONException {
        JSONObject parentObject = new JSONObject(response.toString());
        JSONObject checkout_page = parentObject.getJSONObject(Constants.CHECKOUT_PAGE.KEY_CHECKOUT_PAGE);

        JSONArray extra_charge_array = parentObject.getJSONArray("extra_charge");
        for (int i=0; i<extra_charge_array.length(); i++){
            JSONObject extra_charge_object = extra_charge_array.getJSONObject(i);
            extra_charge_name = extra_charge_object.getString("name");
            extra_charge_value = extra_charge_object.getString("value");
            Log.d(TAG, "fetchJsonData: "+extra_charge_name+"/"+extra_charge_value);
        }


        if (checkout_page.has(Constants.CHECKOUT_PAGE.KEY_SHIPPING_ADDRESS) && !checkout_page.isNull(Constants.CHECKOUT_PAGE.KEY_SHIPPING_ADDRESS)){
            JSONObject shipping_address = checkout_page.getJSONObject(Constants.CHECKOUT_PAGE.KEY_SHIPPING_ADDRESS);
            if (shipping_address.has(Constants.CHECKOUT_PAGE.KEY_ID_SHIPPING_ADDRESS) && !shipping_address.isNull(Constants.CHECKOUT_PAGE.KEY_ID_SHIPPING_ADDRESS) &&
                    shipping_address.getString(Constants.CHECKOUT_PAGE.KEY_ID_SHIPPING_ADDRESS).length()>0){
                shipping_address.getString(Constants.CHECKOUT_PAGE.KEY_ID_SHIPPING_ADDRESS);
            }else {}
            if (shipping_address.has(Constants.CHECKOUT_PAGE.KEY_FIRST_NAME) && !shipping_address.isNull(Constants.CHECKOUT_PAGE.KEY_FIRST_NAME) &&
                    shipping_address.getString(Constants.CHECKOUT_PAGE.KEY_FIRST_NAME).length()>0){
                codOrderSummeryView.showFirstName(shipping_address.getString(Constants.PAYMENT_OPTION.KEY_FIRST_NAME));
            }else {codOrderSummeryView.showFirstNameError();}
            if (shipping_address.has(Constants.CHECKOUT_PAGE.KEY_LAST_NAME) && !shipping_address.isNull(Constants.CHECKOUT_PAGE.KEY_LAST_NAME) &&
                    shipping_address.getString(Constants.CHECKOUT_PAGE.KEY_LAST_NAME).length()>0){
                codOrderSummeryView.showLastName(shipping_address.getString(Constants.CHECKOUT_PAGE.KEY_LAST_NAME));
            }else {codOrderSummeryView.showLastNameError();}
            if (shipping_address.has(Constants.CHECKOUT_PAGE.KEY_COMPANY) && !shipping_address.isNull(Constants.CHECKOUT_PAGE.KEY_COMPANY) &&
                    shipping_address.getString(Constants.CHECKOUT_PAGE.KEY_COMPANY).length()>0){
                shipping_address.getString(Constants.CHECKOUT_PAGE.KEY_COMPANY);
            }else {}
            if (shipping_address.has(Constants.CHECKOUT_PAGE.KEY_MOBILE_NUMBER) && !shipping_address.isNull(Constants.CHECKOUT_PAGE.KEY_MOBILE_NUMBER) &&
                    shipping_address.getString(Constants.CHECKOUT_PAGE.KEY_MOBILE_NUMBER).length()>0){
                codOrderSummeryView.showMobile(shipping_address.getString(Constants.CHECKOUT_PAGE.KEY_MOBILE_NUMBER));
            }else {codOrderSummeryView.showMobileError();}
            if (shipping_address.has(Constants.CHECKOUT_PAGE.KEY_ADDRESS) && !shipping_address.isNull(Constants.CHECKOUT_PAGE.KEY_ADDRESS) &&
                    shipping_address.getString(Constants.CHECKOUT_PAGE.KEY_ADDRESS).length()>0){
                codOrderSummeryView.showAddress(shipping_address.getString(Constants.CHECKOUT_PAGE.KEY_ADDRESS)
                                        +" , "+shipping_address.getString(Constants.CHECKOUT_PAGE.KEY_LANDMARK));
            }else {codOrderSummeryView.showAddressError();}
            if (shipping_address.has(Constants.CHECKOUT_PAGE.KEY_LANDMARK) && !shipping_address.isNull(Constants.CHECKOUT_PAGE.KEY_LANDMARK) &&
                    shipping_address.getString(Constants.CHECKOUT_PAGE.KEY_LANDMARK).length()>0){
                codOrderSummeryView.showLandmark(shipping_address.getString(Constants.CHECKOUT_PAGE.KEY_LANDMARK));
            }else {codOrderSummeryView.showLandmarkError();}
            if (shipping_address.has(Constants.CHECKOUT_PAGE.KEY_CITY) && !shipping_address.isNull(Constants.CHECKOUT_PAGE.KEY_CITY) &&
                    shipping_address.getString(Constants.CHECKOUT_PAGE.KEY_CITY).length()>0){
                codOrderSummeryView.showCity(shipping_address.getString(Constants.CHECKOUT_PAGE.KEY_CITY));
            }else {}
            if (shipping_address.has(Constants.CHECKOUT_PAGE.KEY_STATE) && !shipping_address.isNull(Constants.CHECKOUT_PAGE.KEY_STATE) &&
                    shipping_address.getString(Constants.CHECKOUT_PAGE.KEY_STATE).length()>0){
                codOrderSummeryView.showState(shipping_address.getString(Constants.CHECKOUT_PAGE.KEY_STATE));
            }else {codOrderSummeryView.showStateError();}
            if (shipping_address.has(Constants.CHECKOUT_PAGE.KEY_COUNTRY_ID) && !shipping_address.isNull(Constants.CHECKOUT_PAGE.KEY_COUNTRY_ID) &&
                    shipping_address.getString(Constants.CHECKOUT_PAGE.KEY_COUNTRY_ID).length()>0){
                shipping_address.getString(Constants.CHECKOUT_PAGE.KEY_COUNTRY_ID);
            }else {}
            if (shipping_address.has(Constants.CHECKOUT_PAGE.KEY_COUNTRY) && !shipping_address.isNull(Constants.CHECKOUT_PAGE.KEY_COUNTRY) &&
                    shipping_address.getString(Constants.CHECKOUT_PAGE.KEY_COUNTRY).length()>0){
                shipping_address.getString(Constants.CHECKOUT_PAGE.KEY_COUNTRY);
            }else {}
            if (shipping_address.has(Constants.CHECKOUT_PAGE.KEY_ZONE_ID) && !shipping_address.isNull(Constants.CHECKOUT_PAGE.KEY_ZONE_ID) &&
                    shipping_address.getString(Constants.CHECKOUT_PAGE.KEY_ZONE_ID).length()>0){
                shipping_address.getString(Constants.CHECKOUT_PAGE.KEY_ZONE_ID);
            }else {}
            if (shipping_address.has(Constants.CHECKOUT_PAGE.KEY_POSTAL_CODE) && !shipping_address.isNull(Constants.CHECKOUT_PAGE.KEY_POSTAL_CODE) &&
                    shipping_address.getString(Constants.CHECKOUT_PAGE.KEY_POSTAL_CODE).length()>0){
                codOrderSummeryView.showPostalCode(shipping_address.getString(Constants.CHECKOUT_PAGE.KEY_POSTAL_CODE));
            }else {codOrderSummeryView.showPostalCodeError();}
            if (shipping_address.has(Constants.CHECKOUT_PAGE.KEY_ALIAS) && !shipping_address.isNull(Constants.CHECKOUT_PAGE.KEY_ALIAS) &&
                    shipping_address.getString(Constants.CHECKOUT_PAGE.KEY_ALIAS).length()>0){
                shipping_address.getString(Constants.CHECKOUT_PAGE.KEY_ALIAS);
            }else {}
        }else {}

        //-----------------------Product details list---------------------------------------
        if (parentObject.has(Constants.CART_DATA.KEY_PRODUCTS) && !parentObject.isNull(Constants.CART_DATA.KEY_PRODUCTS) &&
                parentObject.getJSONArray(Constants.CART_DATA.KEY_PRODUCTS).length()>0){
            JSONArray productArray = parentObject.getJSONArray(Constants.CART_DATA.KEY_PRODUCTS);
            for (int i=0; i<productArray.length(); i++){
                JSONObject productObject = productArray.getJSONObject(i);
                CartItemList cartItemList = new CartItemList();
                if (productObject.has(Constants.CART_DATA.KEY_PRODUCT_ID) && !productObject.isNull(Constants.CART_DATA.KEY_PRODUCT_ID) &&
                        productObject.getString(Constants.CART_DATA.KEY_PRODUCT_ID).length()>0){
                    cartItemList.setProduct_id(productObject.getString(Constants.CART_DATA.KEY_PRODUCT_ID));
                }else {cartItemList.setProduct_id(notAvailable);}
                if (productObject.has(Constants.CART_DATA.KEY_TITLE) && !productObject.isNull(Constants.CART_DATA.KEY_TITLE) &&
                        productObject.getString(Constants.CART_DATA.KEY_TITLE).length()>0){
                    cartItemList.setTitle(productObject.getString(Constants.CART_DATA.KEY_TITLE));
                }else {cartItemList.setTitle(notAvailable);}
                if (productObject.has(Constants.CART_DATA.KEY_IS_GIFT_PRODUCT) && !productObject.isNull(Constants.CART_DATA.KEY_IS_GIFT_PRODUCT) &&
                        productObject.getString(Constants.CART_DATA.KEY_IS_GIFT_PRODUCT).length()>0){
                    cartItemList.setIs_gift_product(productObject.getString(Constants.CART_DATA.KEY_IS_GIFT_PRODUCT));
                }else {cartItemList.setIs_gift_product(notAvailable);}
                if (productObject.has(Constants.CART_DATA.KEY_ID_PRODUCT_ATTRIBUTE) && !productObject.isNull(Constants.CART_DATA.KEY_ID_PRODUCT_ATTRIBUTE) &&
                        productObject.getString(Constants.CART_DATA.KEY_ID_PRODUCT_ATTRIBUTE).length()>0){
                    cartItemList.setId_product_attribute(productObject.getString(Constants.CART_DATA.KEY_ID_PRODUCT_ATTRIBUTE));
                }else {cartItemList.setId_product_attribute(notAvailable);}
                if (productObject.has(Constants.CART_DATA.KEY_ID_ADDRESS_DELIVERY) && !productObject.isNull(Constants.CART_DATA.KEY_ID_ADDRESS_DELIVERY)){
                    cartItemList.setId_address_delivery(productObject.getInt(Constants.CART_DATA.KEY_ID_ADDRESS_DELIVERY));
                }else {cartItemList.setId_address_delivery(0);}
                if (productObject.has(Constants.CART_DATA.KEY_STOCK) && !productObject.isNull(Constants.CART_DATA.KEY_STOCK)){
                    cartItemList.setStock(productObject.getBoolean(Constants.CART_DATA.KEY_STOCK));
                    codOrderSummeryView.showStock(productObject.getBoolean(Constants.CART_DATA.KEY_STOCK));
                }else {cartItemList.setStock(false); codOrderSummeryView.showStockError(); }
                if (productObject.has(Constants.CART_DATA.KEY_DISCOUNT_PRICE) && !productObject.isNull(Constants.CART_DATA.KEY_DISCOUNT_PRICE) &&
                        productObject.getString(Constants.CART_DATA.KEY_DISCOUNT_PRICE).length()>0){
                    cartItemList.setDiscount_price(productObject.getString(Constants.CART_DATA.KEY_DISCOUNT_PRICE));
                }else {cartItemList.setDiscount_price(notAvailable);}
                if (productObject.has(Constants.CART_DATA.KEY_DISCOUNT_PERCENTAGE) && !productObject.isNull(Constants.CART_DATA.KEY_DISCOUNT_PERCENTAGE)){
                    cartItemList.setDiscount_percentage(productObject.getInt(Constants.CART_DATA.KEY_DISCOUNT_PERCENTAGE));
                }else {cartItemList.setDiscount_percentage(0);}
                if (productObject.has(Constants.CART_DATA.KEY_IMAGES) && !productObject.isNull(Constants.CART_DATA.KEY_IMAGES) &&
                        productObject.getString(Constants.CART_DATA.KEY_IMAGES).length()>0){
                    cartItemList.setImages(productObject.getString(Constants.CART_DATA.KEY_IMAGES));
                }else {cartItemList.setImages(notAvailable);}
                if (productObject.has(Constants.CART_DATA.KEY_PRICE) && !productObject.isNull(Constants.CART_DATA.KEY_PRICE) &&
                        productObject.getString(Constants.CART_DATA.KEY_PRICE).length()>0){
                    cartItemList.setPrice(productObject.getString(Constants.CART_DATA.KEY_PRICE));
                }else {cartItemList.setPrice(notAvailable);}
                if (productObject.has(Constants.CART_DATA.KEY_QUANTITY) && !productObject.isNull(Constants.CART_DATA.KEY_QUANTITY) &&
                        productObject.getString(Constants.CART_DATA.KEY_QUANTITY).length()>0){
                    cartItemList.setQuantity(productObject.getString(Constants.CART_DATA.KEY_QUANTITY));
                }else {cartItemList.setQuantity(notAvailable);}
                if (productObject.has(Constants.CART_DATA.KEY_PRODUCT_ITEMS) && !productObject.isNull(Constants.CART_DATA.KEY_PRODUCT_ITEMS) &&
                        productObject.getString(Constants.CART_DATA.KEY_PRODUCT_ITEMS).length()>0){
                    cartItemList.setProduct_items(productObject.getString(Constants.CART_DATA.KEY_PRODUCT_ITEMS));
                }
                checkoutProductList.add(cartItemList);
                Log.d(TAG, "fetchJsonData: "+checkoutProductList);
            }
            codOrderSummeryView.showProductDetailsList(checkoutProductList);
        }else {codOrderSummeryView.showPaymentDetailsError();}

        //---------------------amount details----------------------------------
        if (parentObject.has(Constants.CART_DATA.KEY_TOTAL_AMOUNT) && !parentObject.isNull(Constants.CART_DATA.KEY_TOTAL_AMOUNT) &&
                parentObject.getJSONArray(Constants.CART_DATA.KEY_TOTAL_AMOUNT).length()>0){
            JSONArray total_amount_details = parentObject.getJSONArray(Constants.CART_DATA.KEY_TOTAL_AMOUNT);
            for (int j=0; j<total_amount_details.length(); j++){
                JSONObject total_amount = total_amount_details.getJSONObject(j);
                CartPriceDetails cartPriceDetails = new CartPriceDetails();
                if (total_amount.has(Constants.CART_DATA.KEY_NAME) && !total_amount.isNull(Constants.CART_DATA.KEY_NAME) &&
                        total_amount.getString(Constants.CART_DATA.KEY_NAME).length()>0){
                    cartPriceDetails.setPrice_title(total_amount.getString(Constants.CART_DATA.KEY_NAME));
                }else {cartPriceDetails.setPrice_title(notAvailable);}
                if (total_amount.has(Constants.CART_DATA.KEY_VALUE) && !total_amount.isNull(Constants.CART_DATA.KEY_VALUE) &&
                        total_amount.getString(Constants.CART_DATA.KEY_VALUE).length()>0){
                    cartPriceDetails.setPrice_amount(total_amount.getString(Constants.CART_DATA.KEY_VALUE));
                }else {cartPriceDetails.setPrice_amount(notAvailableAmount);}
                checkoutPriceList.add(cartPriceDetails);
                Log.d(TAG, "fetchJsonData: "+checkoutPriceList);
            }
            codOrderSummeryView.showPaymentDetails(checkoutPriceList,extra_charge_name,extra_charge_value);
        }else {codOrderSummeryView.showPaymentDetailsError();}
        codOrderSummeryView.hideProgress();
    }

    @Override
    public void submitCodOrder(final String customer_email, final String session_data,final int id_shipping_address) {

        codOrderSummeryView.showProgress();
        final String payment_method_name = "Cash On Delivery";
        final String payment_method_code = "cod";
        final String status = "approved";
        final String order_message = "Thank You";

        String _payment_method_name = "payment_method_name";
        String _payment_method_code = "payment_method_code";
        String _email_ = "email";
        String _session_data_ = "session_data";
        String _id_shipping_address = "id_shipping_address";
        String _status = "status";
        String _order_message = "order_message";

        //String JSON_URL = "http://166.62.52.188/Murighonto_new/index.php?route=restapi/checkout/checkout/appCreateOrder";
        String JSON_URL = Url.SUBMIT_ORDER_URL;
        Log.d(TAG, "jsonUrl: "+JSON_URL);

        try {
            JSONObject submitData = new JSONObject();
            submitData.put(_payment_method_name,payment_method_name);
            submitData.put(_payment_method_code,payment_method_code);
            submitData.put(_email_,customer_email);
            submitData.put(_session_data_, session_data);
            submitData.put(_id_shipping_address,id_shipping_address);
            submitData.put(_status,status);
            submitData.put(_order_message,order_message);
            Log.d(TAG, "jsonObject: "+submitData);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, JSON_URL, submitData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "onResponse: "+response);
                            try {
                                JSONObject jsonObject = new JSONObject(response.toString());
                                if (jsonObject.getString("status").equalsIgnoreCase("success")){
                                    codOrderSummeryView.setCartCount();
                                    codOrderSummeryView.hideProgress();
                                    codOrderSummeryView.showOrderPlacedMessage(jsonObject.getString("message"),jsonObject.getString("order_id"));
                                }else {
                                    codOrderSummeryView.hideProgress();
                                    codOrderSummeryView.showOrderPlacedFilure(jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            codOrderSummeryView.hideProgress();
                            codOrderSummeryView.showServerError();
                            Log.d(TAG, "onErrorResponse: "+error);
                        }
                    }) {
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    try {
                        String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                        Log.d(TAG, "parseNetworkResponse: "+jsonString);
                        return Response.success(new JSONObject(jsonString),
                                HttpHeaderParser.parseCacheHeaders(response));
                    } catch (UnsupportedEncodingException e) {
                        Log.d(TAG, "parseNetworkResponse: "+Response.error(new ParseError(e)));
                        return Response.error(new ParseError(e));
                    } catch (JSONException je) {
                        Log.d(TAG, "parseNetworkResponse: "+Response.error(new ParseError(je)));
                        return Response.error(new ParseError(je));
                    }
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue((Context) this.codOrderSummeryView);
            int socketTimeout = 200000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            requestQueue.add(jsonObjectRequest);

        }catch (JSONException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void submitOnlinePaymentOrder(String customer_email, String session_data, String payment_id,int id_shipping_address) {

        codOrderSummeryView.showProgress();
        final String payment_method_name = "Use Debit Card Credit Card Net Banking";
        final String payment_method_code = "instamojo";
        final String status = "approved";
        final String order_message = "Thank You";

        String _payment_method_name = "payment_method_name";
        String _payment_method_code = "payment_method_code";
        String _email_ = "email";
        String _session_data_ = "session_data";
        String _id_shipping_address = "id_shipping_address";
        String _status = "status";
        String _order_message = "order_message";
        String _payment_id = "payment_id";

        String JSON_URL = Url.PLACE_ONLINE_PAYMENT_ORDER;
        Log.d(TAG, "jsonUrl: "+JSON_URL);

        try {
            JSONObject submitData = new JSONObject();
            submitData.put(_payment_method_name,payment_method_name);
            submitData.put(_payment_method_code,payment_method_code);
            submitData.put(_email_,customer_email);
            submitData.put(_session_data_, session_data);
            submitData.put(_id_shipping_address,id_shipping_address);
            submitData.put(_status,status);
            submitData.put(_order_message,order_message);
            submitData.put(_payment_id,payment_id);
            Log.d(TAG, "jsonObject place order : "+submitData);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, JSON_URL, submitData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "onResponse: "+response);
                            try {
                                JSONObject jsonObject = new JSONObject(response.toString());
                                if (jsonObject.getString("status").equalsIgnoreCase("success")){
                                    codOrderSummeryView.setCartCount();
                                    codOrderSummeryView.hideProgress();
                                    codOrderSummeryView.showOrderPlacedMessage(jsonObject.getString("message"),"");
                                }else {
                                    codOrderSummeryView.hideProgress();
                                    codOrderSummeryView.showOrderPlacedFilure(jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    codOrderSummeryView.hideProgress();
                    codOrderSummeryView.showServerError();
                    Log.d(TAG, "onErrorResponse: "+error);
                }
            }) {
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    try {
                        String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                        Log.d(TAG, "parseNetworkResponse: "+jsonString);
                        return Response.success(new JSONObject(jsonString),
                                HttpHeaderParser.parseCacheHeaders(response));
                    } catch (UnsupportedEncodingException e) {
                        Log.d(TAG, "parseNetworkResponse: "+Response.error(new ParseError(e)));
                        return Response.error(new ParseError(e));
                    } catch (JSONException je) {
                        Log.d(TAG, "parseNetworkResponse: "+Response.error(new ParseError(je)));
                        return Response.error(new ParseError(je));
                    }
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue((Context) this.codOrderSummeryView);
            int socketTimeout = 200000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            requestQueue.add(jsonObjectRequest);

        }catch (JSONException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }
}
