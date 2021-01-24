package com.purpuligo.pcweb.Model;

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
import com.purpuligo.pcweb.Constants.Constants;
import com.purpuligo.pcweb.Global.Url;
import com.purpuligo.pcweb.Model.Pojo.CartItemList;
import com.purpuligo.pcweb.Presenter.OrderHistoryDetailsPresenter;
import com.purpuligo.pcweb.View.OrderHistoryDetailsView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderHistoryDetailsInteractorImpl implements OrderHistoryDetailsPresenter{

    private String TAG = "OrderHistoryDetailsInteractorImpl";
    private OrderHistoryDetailsView orderHistoryDetailsView;
    private ArrayList<CartItemList> orderHistoryProductList;
    private String notAvailable = "Not Available";
    private String notAvailableAmount = "0.00";

    public OrderHistoryDetailsInteractorImpl(OrderHistoryDetailsView orderHistoryDetailsView) {
        this.orderHistoryDetailsView = orderHistoryDetailsView;

        orderHistoryProductList = new ArrayList<>();
    }

    @Override
    public void fetchOrderHistoryAllDetails(String order_id) {

        orderHistoryDetailsView.showProgress();
        //String JSON_URL = "http://166.62.52.188/Murighonto_new/index.php?route=restapi/checkout/checkout/appGetOrderDetails";
        String JSON_URL = Url.ORDER_HISTORY_DETAILS_URL;
        JSONObject postData = new JSONObject();
        try {
            postData.put("order_id",order_id);
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
                                fetchDetailsFromServer(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        orderHistoryDetailsView.hideProgress();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                orderHistoryDetailsView.hideProgress();
                orderHistoryDetailsView.showServerError();
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

        RequestQueue requestQueue = Volley.newRequestQueue((Context) this.orderHistoryDetailsView);
        requestQueue.add(stringRequest);
    }

    private void fetchDetailsFromServer(JSONObject response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response.toString());
        if (jsonObject.getString(Constants.ORDER_HISTORY.KEY_STATUS).equalsIgnoreCase("success")){
            if (jsonObject.has(Constants.ORDER_HISTORY.KEY_ORDER_DETAILS) && !jsonObject.isNull(Constants.ORDER_HISTORY.KEY_ORDER_DETAILS) &&
                    jsonObject.getJSONObject(Constants.ORDER_HISTORY.KEY_ORDER_DETAILS).length()>0){
                JSONObject rootObject = jsonObject.getJSONObject(Constants.ORDER_HISTORY.KEY_ORDER_DETAILS);

                //-------------------product details---------------------
                if (rootObject.has(Constants.ORDER_HISTORY.KEY_PRODUCTS) && !rootObject.isNull(Constants.ORDER_HISTORY.KEY_PRODUCTS) &&
                        rootObject.getJSONArray(Constants.ORDER_HISTORY.KEY_PRODUCTS).length()>0){
                    JSONArray productArray = rootObject.getJSONArray(Constants.ORDER_HISTORY.KEY_PRODUCTS);
                    for (int i=0; i<productArray.length(); i++){
                        JSONObject productObject = productArray.getJSONObject(i);
                        CartItemList cartItemList = new CartItemList();
                        if (productObject.has(Constants.ORDER_HISTORY.KEY_ID) && !productObject.isNull(Constants.ORDER_HISTORY.KEY_ID) &&
                                productObject.getString(Constants.ORDER_HISTORY.KEY_ID).length()>0){
                            cartItemList.setProduct_id(productObject.getString(Constants.ORDER_HISTORY.KEY_ID));
                        }else {cartItemList.setProduct_id(notAvailable);}
                        if (productObject.has(Constants.ORDER_HISTORY.KEY_TITLE) && !productObject.isNull(Constants.ORDER_HISTORY.KEY_TITLE) &&
                                productObject.getString(Constants.ORDER_HISTORY.KEY_TITLE).length()>0){
                            cartItemList.setTitle(productObject.getString(Constants.ORDER_HISTORY.KEY_TITLE));
                        }else {cartItemList.setTitle(notAvailable);}
                        if (productObject.has(Constants.ORDER_HISTORY.KEY_IS_GIFT_PRODUCT) && !productObject.isNull(Constants.ORDER_HISTORY.KEY_IS_GIFT_PRODUCT) &&
                                productObject.getString(Constants.ORDER_HISTORY.KEY_IS_GIFT_PRODUCT).length()>0){
                            cartItemList.setIs_gift_product(productObject.getString(Constants.ORDER_HISTORY.KEY_IS_GIFT_PRODUCT));
                        }else {cartItemList.setIs_gift_product(notAvailable);}
                        if (productObject.has(Constants.ORDER_HISTORY.KEY_ID_PRODUCT_ATTRIBUTE) && !productObject.isNull(Constants.ORDER_HISTORY.KEY_ID_PRODUCT_ATTRIBUTE) &&
                                productObject.getString(Constants.ORDER_HISTORY.KEY_ID_PRODUCT_ATTRIBUTE).length()>0){
                            cartItemList.setId_product_attribute(productObject.getString(Constants.ORDER_HISTORY.KEY_ID_PRODUCT_ATTRIBUTE));
                        }else {cartItemList.setId_product_attribute(notAvailable);}
                        if (productObject.has(Constants.ORDER_HISTORY.KEY_STOCK) && !productObject.isNull(Constants.ORDER_HISTORY.KEY_STOCK)){
                            cartItemList.setStock(productObject.getBoolean(Constants.ORDER_HISTORY.KEY_STOCK));
                        }else {cartItemList.setStock(false);}
                        if (productObject.has(Constants.ORDER_HISTORY.KEY_DISCOUNT_PRICE) && !productObject.isNull(Constants.ORDER_HISTORY.KEY_DISCOUNT_PRICE) &&
                                productObject.getString(Constants.ORDER_HISTORY.KEY_DISCOUNT_PRICE).length()>0){
                            cartItemList.setDiscount_price(productObject.getString(Constants.ORDER_HISTORY.KEY_DISCOUNT_PRICE));
                        }else {cartItemList.setDiscount_price(notAvailable);}
                        if (productObject.has(Constants.ORDER_HISTORY.KEY_DISCOUNT_PERCENTAGE) && !productObject.isNull(Constants.ORDER_HISTORY.KEY_DISCOUNT_PERCENTAGE)){
                            cartItemList.setDiscount_percentage(productObject.getInt(Constants.ORDER_HISTORY.KEY_DISCOUNT_PERCENTAGE));
                        }else {cartItemList.setDiscount_percentage(0);}
                        if (productObject.has(Constants.ORDER_HISTORY.KEY_IMAGES) && !productObject.isNull(Constants.ORDER_HISTORY.KEY_IMAGES) &&
                                productObject.getString(Constants.ORDER_HISTORY.KEY_IMAGES).length()>0){
                            cartItemList.setImages(productObject.getString(Constants.ORDER_HISTORY.KEY_IMAGES));
                        }else {cartItemList.setImages(notAvailable);}
                        if (productObject.has(Constants.ORDER_HISTORY.KEY_MODEL) && !productObject.isNull(Constants.ORDER_HISTORY.KEY_MODEL) &&
                                productObject.getString(Constants.ORDER_HISTORY.KEY_MODEL).length()>0){
                            cartItemList.setModel(productObject.getString(Constants.ORDER_HISTORY.KEY_MODEL));
                        }else {cartItemList.setModel(notAvailable);}
                        if (productObject.has(Constants.ORDER_HISTORY.KEY_PRICE) && !productObject.isNull(Constants.ORDER_HISTORY.KEY_PRICE) &&
                                productObject.getString(Constants.ORDER_HISTORY.KEY_PRICE).length()>0){
                            cartItemList.setPrice(productObject.getString(Constants.ORDER_HISTORY.KEY_PRICE));
                        }else {cartItemList.setPrice(notAvailable);}
                        if (productObject.has(Constants.ORDER_HISTORY.KEY_QUANTITY) && !productObject.isNull(Constants.ORDER_HISTORY.KEY_QUANTITY) &&
                                productObject.getString(Constants.ORDER_HISTORY.KEY_QUANTITY).length()>0){
                            cartItemList.setQuantity(productObject.getString(Constants.ORDER_HISTORY.KEY_QUANTITY));
                        }else {cartItemList.setQuantity(notAvailable);}
                        if (productObject.getJSONArray(Constants.ORDER_HISTORY.KEY_PRODUCT_ITEMS).length()>0){
                            JSONArray product_items = productObject.getJSONArray(Constants.ORDER_HISTORY.KEY_PRODUCT_ITEMS);
                            for (int k=0; k<1; k++ ){
                                JSONObject product_items_object = product_items.getJSONObject(k);
                                cartItemList.setProduct_items(product_items_object.getString("name") +" : "+
                                        product_items_object.getString("value"));
                            }
                        }
                        orderHistoryProductList.add(cartItemList);
                    }
                    orderHistoryDetailsView.showProductList(orderHistoryProductList);
                }else {orderHistoryDetailsView.showProductListError();
                orderHistoryDetailsView.hideProgress();}

                //--------------------shipping address-------------------------
                if (rootObject.has(Constants.ORDER_HISTORY.KEY_SHIPPING_ADDRESS) && !rootObject.isNull(Constants.ORDER_HISTORY.KEY_SHIPPING_ADDRESS) &&
                        rootObject.getJSONObject(Constants.ORDER_HISTORY.KEY_SHIPPING_ADDRESS).length()>0){
                    JSONObject shipping_address = rootObject.getJSONObject(Constants.ORDER_HISTORY.KEY_SHIPPING_ADDRESS);
                    if (shipping_address.has(Constants.ORDER_HISTORY.KEY_FIRST_NAME) && !shipping_address.isNull(Constants.ORDER_HISTORY.KEY_FIRST_NAME) &&
                            shipping_address.getString(Constants.ORDER_HISTORY.KEY_FIRST_NAME).length()>0){
                        orderHistoryDetailsView.showShippingFirstName(shipping_address.getString(Constants.ORDER_HISTORY.KEY_FIRST_NAME));
                    }else {}
                    if (shipping_address.has(Constants.ORDER_HISTORY.KEY_LAST_NAME) && !shipping_address.isNull(Constants.ORDER_HISTORY.KEY_LAST_NAME) &&
                            shipping_address.getString(Constants.ORDER_HISTORY.KEY_LAST_NAME).length()>0){
                        orderHistoryDetailsView.showShippingLastName(shipping_address.getString(Constants.ORDER_HISTORY.KEY_LAST_NAME));
                    }else {}
                    if (shipping_address.has(Constants.ORDER_HISTORY.KEY_COMPANY) && !shipping_address.isNull(Constants.ORDER_HISTORY.KEY_COMPANY) &&
                            shipping_address.getString(Constants.ORDER_HISTORY.KEY_COMPANY).length()>0){

                    }else {}
                    if (shipping_address.has(Constants.ORDER_HISTORY.KEY_MOBILE_NUMBER) && !shipping_address.isNull(Constants.ORDER_HISTORY.KEY_MOBILE_NUMBER) &&
                            shipping_address.getString(Constants.ORDER_HISTORY.KEY_MOBILE_NUMBER).length()>0){
                        orderHistoryDetailsView.showShippingMobile(shipping_address.getString(Constants.ORDER_HISTORY.KEY_MOBILE_NUMBER));
                    }else {orderHistoryDetailsView.showShippingMobile(notAvailable);}
                    if (shipping_address.has(Constants.ORDER_HISTORY.KEY_ADDRESS) && !shipping_address.isNull(Constants.ORDER_HISTORY.KEY_ADDRESS) &&
                            shipping_address.getString(Constants.ORDER_HISTORY.KEY_ADDRESS).length()>0){
                        orderHistoryDetailsView.showShippingAddress(shipping_address.getString(Constants.ORDER_HISTORY.KEY_ADDRESS)+" , "+
                                shipping_address.getString(Constants.ORDER_HISTORY.KEY_LANDMARK));
                    }else {orderHistoryDetailsView.showShippingAddress(notAvailable);}
                    if (shipping_address.has(Constants.ORDER_HISTORY.KEY_LANDMARK) && !shipping_address.isNull(Constants.ORDER_HISTORY.KEY_LANDMARK) &&
                            shipping_address.getString(Constants.ORDER_HISTORY.KEY_LANDMARK).length()>0){
                        //orderHistoryDetailsView.showShippingLandmark(shipping_address.getString(Constants.ORDER_HISTORY.KEY_LANDMARK));
                    }else {}
                    if (shipping_address.has(Constants.ORDER_HISTORY.KEY_CITY) && !shipping_address.isNull(Constants.ORDER_HISTORY.KEY_CITY) &&
                            shipping_address.getString(Constants.ORDER_HISTORY.KEY_CITY).length()>0){
                        orderHistoryDetailsView.showShippingCity(shipping_address.getString(Constants.ORDER_HISTORY.KEY_CITY));
                    }else {orderHistoryDetailsView.showShippingCity(notAvailable);}
                    if (shipping_address.has(Constants.ORDER_HISTORY.KEY_STATE) && !shipping_address.isNull(Constants.ORDER_HISTORY.KEY_STATE) &&
                            shipping_address.getString(Constants.ORDER_HISTORY.KEY_STATE).length()>0){
                        orderHistoryDetailsView.showShippingState(shipping_address.getString(Constants.ORDER_HISTORY.KEY_STATE));
                    }else {orderHistoryDetailsView.showShippingState(notAvailable);}
                    if (shipping_address.has(Constants.ORDER_HISTORY.KEY_COUNTRY) && !shipping_address.isNull(Constants.ORDER_HISTORY.KEY_COUNTRY) &&
                            shipping_address.getString(Constants.ORDER_HISTORY.KEY_COUNTRY).length()>0){

                    }else {}
                    if (shipping_address.has(Constants.ORDER_HISTORY.KEY_POSTAL_CODE) && !shipping_address.isNull(Constants.ORDER_HISTORY.KEY_POSTAL_CODE) &&
                            shipping_address.getString(Constants.ORDER_HISTORY.KEY_POSTAL_CODE).length()>0){
                        orderHistoryDetailsView.showShippingPostalCode(shipping_address.getString(Constants.ORDER_HISTORY.KEY_POSTAL_CODE));
                    }else {orderHistoryDetailsView.showShippingPostalCode(notAvailable);}
                    if (shipping_address.has(Constants.ORDER_HISTORY.KEY_ALIAS) && !shipping_address.isNull(Constants.ORDER_HISTORY.KEY_ALIAS) &&
                            shipping_address.getString(Constants.ORDER_HISTORY.KEY_ALIAS).length()>0){

                    }else {}
                }

                //---------------------billing address-------------------------------
                if (rootObject.has(Constants.ORDER_HISTORY.KEY_BILLING_ADDRESS) && !rootObject.isNull(Constants.ORDER_HISTORY.KEY_BILLING_ADDRESS) &&
                        rootObject.getJSONObject(Constants.ORDER_HISTORY.KEY_BILLING_ADDRESS).length()>0){
                    JSONObject billing_address = rootObject.getJSONObject(Constants.ORDER_HISTORY.KEY_BILLING_ADDRESS);
                    if (billing_address.has(Constants.ORDER_HISTORY.KEY_FIRST_NAME) && !billing_address.isNull(Constants.ORDER_HISTORY.KEY_FIRST_NAME) &&
                            billing_address.getString(Constants.ORDER_HISTORY.KEY_FIRST_NAME).length()>0){
                        orderHistoryDetailsView.showBillingFirstName(billing_address.getString(Constants.ORDER_HISTORY.KEY_FIRST_NAME));
                    }else {orderHistoryDetailsView.showBillingFirstName(notAvailable);}
                    if (billing_address.has(Constants.ORDER_HISTORY.KEY_LAST_NAME) && !billing_address.isNull(Constants.ORDER_HISTORY.KEY_LAST_NAME) &&
                            billing_address.getString(Constants.ORDER_HISTORY.KEY_LAST_NAME).length()>0){
                        orderHistoryDetailsView.showBillingLastName(billing_address.getString(Constants.ORDER_HISTORY.KEY_LAST_NAME));
                    }else {}
                    if (billing_address.has(Constants.ORDER_HISTORY.KEY_COMPANY) && !billing_address.isNull(Constants.ORDER_HISTORY.KEY_COMPANY) &&
                            billing_address.getString(Constants.ORDER_HISTORY.KEY_COMPANY).length()>0){

                    }else {}
                    if (billing_address.has(Constants.ORDER_HISTORY.KEY_MOBILE_NUMBER) && !billing_address.isNull(Constants.ORDER_HISTORY.KEY_MOBILE_NUMBER) &&
                            billing_address.getString(Constants.ORDER_HISTORY.KEY_MOBILE_NUMBER).length()>0){
                        orderHistoryDetailsView.showBillingMobile(billing_address.getString(Constants.ORDER_HISTORY.KEY_MOBILE_NUMBER));
                    }else {orderHistoryDetailsView.showBillingMobile(notAvailable);}
                    if (billing_address.has(Constants.ORDER_HISTORY.KEY_ADDRESS) && !billing_address.isNull(Constants.ORDER_HISTORY.KEY_ADDRESS) &&
                            billing_address.getString(Constants.ORDER_HISTORY.KEY_ADDRESS).length()>0){
                        orderHistoryDetailsView.showBillingAddress(billing_address.getString(Constants.ORDER_HISTORY.KEY_ADDRESS)+" , "+
                                billing_address.getString(Constants.ORDER_HISTORY.KEY_LANDMARK));
                    }else {orderHistoryDetailsView.showBillingAddress(notAvailable);}
                    if (billing_address.has(Constants.ORDER_HISTORY.KEY_LANDMARK) && !billing_address.isNull(Constants.ORDER_HISTORY.KEY_LANDMARK) &&
                            billing_address.getString(Constants.ORDER_HISTORY.KEY_LANDMARK).length()>0){
                        //orderHistoryDetailsView.showBillingLandmark(billing_address.getString(Constants.ORDER_HISTORY.KEY_LANDMARK));
                    }else {}
                    if (billing_address.has(Constants.ORDER_HISTORY.KEY_CITY) && !billing_address.isNull(Constants.ORDER_HISTORY.KEY_CITY) &&
                            billing_address.getString(Constants.ORDER_HISTORY.KEY_CITY).length()>0){
                        orderHistoryDetailsView.showBillingCity(billing_address.getString(Constants.ORDER_HISTORY.KEY_CITY));
                    }else {orderHistoryDetailsView.showBillingCity(notAvailable);}
                    if (billing_address.has(Constants.ORDER_HISTORY.KEY_STATE) && !billing_address.isNull(Constants.ORDER_HISTORY.KEY_STATE) &&
                            billing_address.getString(Constants.ORDER_HISTORY.KEY_STATE).length()>0){
                        orderHistoryDetailsView.showBillingState(billing_address.getString(Constants.ORDER_HISTORY.KEY_STATE));
                    }else {orderHistoryDetailsView.showBillingState(notAvailable);}
                    if (billing_address.has(Constants.ORDER_HISTORY.KEY_COUNTRY) && !billing_address.isNull(Constants.ORDER_HISTORY.KEY_COUNTRY) &&
                            billing_address.getString(Constants.ORDER_HISTORY.KEY_COUNTRY).length()>0){

                    }else {}
                    if (billing_address.has(Constants.ORDER_HISTORY.KEY_POSTAL_CODE) && !billing_address.isNull(Constants.ORDER_HISTORY.KEY_POSTAL_CODE) &&
                            billing_address.getString(Constants.ORDER_HISTORY.KEY_POSTAL_CODE).length()>0){
                        orderHistoryDetailsView.showBillingPostalCode(billing_address.getString(Constants.ORDER_HISTORY.KEY_POSTAL_CODE));
                    }else {orderHistoryDetailsView.showBillingPostalCode(notAvailable);}
                    if (billing_address.has(Constants.ORDER_HISTORY.KEY_ALIAS) && !billing_address.isNull(Constants.ORDER_HISTORY.KEY_ALIAS) &&
                            billing_address.getString(Constants.ORDER_HISTORY.KEY_ALIAS).length()>0){

                    }else {}
                }

                //-----------------------price details-------------------------------
                if (rootObject.has(Constants.ORDER_HISTORY.KEY_ORDER_HISTORY) && !rootObject.isNull(Constants.ORDER_HISTORY.KEY_ORDER_HISTORY) &&
                        rootObject.getJSONObject(Constants.ORDER_HISTORY.KEY_ORDER_HISTORY).length()>0){
                    JSONObject order_history = rootObject.getJSONObject(Constants.ORDER_HISTORY.KEY_ORDER_HISTORY);
                    if (order_history.has(Constants.ORDER_HISTORY.KEY_TOTAL) && !order_history.isNull(Constants.ORDER_HISTORY.KEY_TOTAL) &&
                            order_history.getString(Constants.ORDER_HISTORY.KEY_TOTAL).length()>0){
                        orderHistoryDetailsView.showTotalAmount(order_history.getString(Constants.ORDER_HISTORY.KEY_TOTAL));
                    }
                }
                orderHistoryDetailsView.hideProgress();
            }else {orderHistoryDetailsView.showProductListError();
                orderHistoryDetailsView.hideProgress();}
        }else {orderHistoryDetailsView.showProductListError();
            orderHistoryDetailsView.hideProgress();}
    }
}
