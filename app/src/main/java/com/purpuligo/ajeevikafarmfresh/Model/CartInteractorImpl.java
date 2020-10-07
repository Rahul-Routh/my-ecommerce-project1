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
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.CartItemList;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.CartPriceDetails;
import com.purpuligo.ajeevikafarmfresh.Presenter.CartPresenter;
import com.purpuligo.ajeevikafarmfresh.View.CartView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartInteractorImpl implements CartPresenter {

    private String TAG = "CartInteractorImpl";
    private CartView cartView;
    private ArrayList<CartItemList> cartItemArrayList;
    private ArrayList<CartPriceDetails> cartPriceList;
    private String notAvailable = "Not Available";
    private String notAvailableAmount = "0.00";

    public CartInteractorImpl(CartView cartView) {
        this.cartView = cartView;

        cartItemArrayList = new ArrayList<>();
        cartPriceList = new ArrayList<>();
    }

    @Override
    public void fetchCartDetailsFromServer(String customer_email,String session_data) {

        cartView.showProgressBar();
        //String JSON_URL = "http://166.62.52.188/Murighonto_new/index.php?route=restapi/checkout/cart/appGetCartDetails";
        String JSON_URL = Url.CART_URL;
        Map<String, String> postParam = new HashMap<>();
        postParam.put(Constants.ADD_TO_CART.KEY_CUSTOMER_EMAIL, customer_email);
        postParam.put("session_data",session_data);
        JSONObject postData = new JSONObject(postParam);
        Log.d(TAG, "fetchCartDetailsFromServer: "+postData);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,JSON_URL,postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "getDataFromServer: "+response.toString());
                        if (response.length()>0){
                            fetchCartData(response);
                        }else {
                            cartView.hideProgressBar();
                            cartView.showCartItemListError("0");}
                        //cartView.hideProgressBar();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cartView.hideProgressBar();
                cartView.showServerError();
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.d("error", "OnError: " + 12);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue((Context) this.cartView);
        requestQueue.add(stringRequest);
    }

    private void fetchCartData(JSONObject response){
        try {
            JSONObject rootObject = new JSONObject(response.toString());
            if (rootObject.has(Constants.CART_DATA.KEY_PRODUCTS) && !rootObject.isNull(Constants.CART_DATA.KEY_PRODUCTS) &&
                    rootObject.getJSONArray(Constants.CART_DATA.KEY_PRODUCTS).length()>0){
                cartView.showCartItemListError(String.valueOf(rootObject.getJSONArray(Constants.CART_DATA.KEY_PRODUCTS).length()));
                JSONArray productArray = rootObject.getJSONArray(Constants.CART_DATA.KEY_PRODUCTS);
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
                    }else {cartItemList.setStock(false);}
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
                    cartItemArrayList.add(cartItemList);
                    Log.d(TAG, "fetchCartData: "+cartItemList.toString());
                }
                cartView.showCartItemList(cartItemArrayList);
                Log.d(TAG, "fetchCartData: "+rootObject.getJSONArray(Constants.CART_DATA.KEY_PRODUCTS).length());
            }else {cartView.showCartItemListError("0");}

            //Log.d(TAG, "fetchCartData: "+rootObject.getJSONArray(Constants.CART_DATA.KEY_TOTAL_AMOUNT));
            if (rootObject.has(Constants.CART_DATA.KEY_TOTAL_AMOUNT) && !rootObject.isNull(Constants.CART_DATA.KEY_TOTAL_AMOUNT) &&
                    rootObject.getJSONArray(Constants.CART_DATA.KEY_TOTAL_AMOUNT).length()>0){
                JSONArray total_amount_details = rootObject.getJSONArray(Constants.CART_DATA.KEY_TOTAL_AMOUNT);
                for (int j=0; j<total_amount_details.length(); j++){
                    JSONObject total_amount = total_amount_details.getJSONObject(j);
                    //Log.d(TAG, "fetchCartData: "+total_amount);
                    CartPriceDetails cartPriceDetails = new CartPriceDetails();
                    if (total_amount.has(Constants.CART_DATA.KEY_NAME) && !total_amount.isNull(Constants.CART_DATA.KEY_NAME) &&
                            total_amount.getString(Constants.CART_DATA.KEY_NAME).length()>0){
                        cartPriceDetails.setPrice_title(total_amount.getString(Constants.CART_DATA.KEY_NAME));
                        //Log.d(TAG, "fetchCartData: "+total_amount.getString(Constants.CART_DATA.KEY_NAME));
                    }else {cartPriceDetails.setPrice_title(notAvailable);}
                    if (total_amount.has(Constants.CART_DATA.KEY_VALUE) && !total_amount.isNull(Constants.CART_DATA.KEY_VALUE) &&
                            total_amount.getString(Constants.CART_DATA.KEY_VALUE).length()>0){
                        cartPriceDetails.setPrice_amount(total_amount.getString(Constants.CART_DATA.KEY_VALUE));
                        //Log.d(TAG, "fetchCartData: "+Constants.CART_DATA.KEY_VALUE);
                    }else {cartPriceDetails.setPrice_amount(notAvailableAmount);}
                    cartPriceList.add(cartPriceDetails);
                }
                cartView.showCartPriceDetailsList(cartPriceList);
            }else {}
            cartView.hideProgressBar();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeCartItem(int product_id, int quantity, String email, int cart_id, String session_data) {

        cartView.showProgressBar();
        //String JSON_URL = "http://166.62.52.188/Murighonto_new/index.php?route=restapi/checkout/cart/appRemoveProduct";
        String JSON_URL = Url.REMOVE_CART_ITEM_URL;
        try {
            JSONObject rootObject = new JSONObject();
            rootObject.put(Constants.CART_REMOVE_ITEMS.KEY_PRODUCT_ID,product_id);
            rootObject.put(Constants.CART_REMOVE_ITEMS.KEY_QUANTITY,quantity);
            rootObject.put(Constants.CART_REMOVE_ITEMS.KEY_EMAIL,email);
            rootObject.put(Constants.CART_REMOVE_ITEMS.KEY_CART_ID,cart_id);
            rootObject.put(Constants.CART_REMOVE_ITEMS.KEY_SESSION_DATA,session_data);
            Log.d(TAG, "removeCartItem: "+rootObject);

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,JSON_URL,rootObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "removeCartItem: "+response.toString());
                            try {
                                JSONObject rootObject = new JSONObject(response.toString());
                                cartView.removeItemSuccess(rootObject.getString("message"));
                                cartView.hideProgressBar();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    cartView.hideProgressBar();
                    cartView.showServerError();
                    VolleyLog.d("", "Error: " + error.getMessage());
                    Log.d("error", "OnError: " + 12);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue((Context) this.cartView);
            requestQueue.add(stringRequest);
        }catch (JSONException e){e.printStackTrace();}
    }
}
