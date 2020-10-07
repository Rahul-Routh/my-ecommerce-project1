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
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.DescriptionImageSlider;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.ProductListDetails;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.ProductSizeDetails;
import com.purpuligo.ajeevikafarmfresh.Presenter.ProductDescriptionPresenter;
import com.purpuligo.ajeevikafarmfresh.View.ProductDescriptionView;
import com.purpuligo.ajeevikafarmfresh.View.ProductListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductDescriptionInteractorImpl implements ProductDescriptionPresenter {

    private String TAG = "ProductDescriptionImpl";
    private ProductDescriptionView productDescriptionView;
    private ProductListView productListView;
    private ArrayList<DescriptionImageSlider> descriptionImage;
    private ArrayList<ProductSizeDetails> productSize;
    private ArrayList<ProductSizeDetails> deliveryOption;

    private ArrayList<ProductListDetails> productListArray;

    private String notFound = "Not Available";
    private String notAvailable = "Not Available";

    public ProductDescriptionInteractorImpl(ProductDescriptionView productDescriptionView) {
        this.productDescriptionView = productDescriptionView;

        descriptionImage = new ArrayList<>();
        productSize = new ArrayList<>();
        deliveryOption = new ArrayList<>();
    }

    @Override
    public void fetchDescriptionDataFromServer(String product_id) {

        productDescriptionView.showProgressBar();
        //String JSON_URL = "http://166.62.52.188/Murighonto_new/index.php?route=restapi/product/product&product_id="+product_id;
        String JSON_URL = Url.PRODUCT_DESCRIPTION_URL+product_id;
        Log.d(TAG, "fetchDescriptionDataFromServer: "+JSON_URL);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response.toString());
                        if (response.length() > 0){
                            try {
                                JSONObject jsonObject = new JSONObject(response.toString());
                                if (jsonObject.has("product_id") && !jsonObject.isNull("product_id")){
                                    //productDescriptionView.showItemQuantityInKg(String.valueOf(jsonObject.getInt("product_id")));
                                }else {
                                    //productDescriptionView.showItemQuantityInKgError();
                                }
                                if (jsonObject.has(Constants.PRODUCT_DESCRIPTION.KEY_PRODUCT_NAME) && !jsonObject.isNull(Constants.PRODUCT_DESCRIPTION.KEY_PRODUCT_NAME) &&
                                        jsonObject.getString(Constants.PRODUCT_DESCRIPTION.KEY_PRODUCT_NAME).length() > 0){
                                    productDescriptionView.showProductName(jsonObject.getString(Constants.PRODUCT_DESCRIPTION.KEY_PRODUCT_NAME));
                                }else {
                                    productDescriptionView.showProductNameError(notFound);
                                }
                                if (jsonObject.has(Constants.PRODUCT_DESCRIPTION.KEY_PRODUCT_DESCRIPTION) && !jsonObject.isNull(Constants.PRODUCT_DESCRIPTION.KEY_PRODUCT_DESCRIPTION) &&
                                        jsonObject.getString(Constants.PRODUCT_DESCRIPTION.KEY_PRODUCT_DESCRIPTION).length() > 0){
                                    productDescriptionView.showDescription(jsonObject.getString(Constants.PRODUCT_DESCRIPTION.KEY_PRODUCT_DESCRIPTION));
                                }else {
                                    productDescriptionView.showDescriptionError(notFound);
                                }
                                if (jsonObject.has(Constants.PRODUCT_DESCRIPTION.KEY_DELIVERY_TIME) && !jsonObject.isNull(Constants.PRODUCT_DESCRIPTION.KEY_DELIVERY_TIME) &&
                                        jsonObject.getString(Constants.PRODUCT_DESCRIPTION.KEY_DELIVERY_TIME).length() > 0){
                                    productDescriptionView.showDeliveryTime(jsonObject.getString(Constants.PRODUCT_DESCRIPTION.KEY_DELIVERY_TIME),
                                            jsonObject.getString("parent_id"));
                                }else {
                                    productDescriptionView.showDeliveryTimeError(notFound);
                                }
                                if (jsonObject.has(Constants.PRODUCT_DESCRIPTION.KEY_PRODUCT_PRICE) && !jsonObject.isNull(Constants.PRODUCT_DESCRIPTION.KEY_PRODUCT_PRICE) &&
                                        jsonObject.getString(Constants.PRODUCT_DESCRIPTION.KEY_PRODUCT_PRICE).length() > 0){
                                    productDescriptionView.showProductPrice(jsonObject.getString(Constants.PRODUCT_DESCRIPTION.KEY_PRODUCT_PRICE));
                                }else {
                                    productDescriptionView.showProductPriceError(notFound);
                                }
                                if (jsonObject.has(Constants.PRODUCT_DESCRIPTION.KEY_PRODUCT_STOCK) && !jsonObject.isNull(Constants.PRODUCT_DESCRIPTION.KEY_PRODUCT_STOCK) &&
                                        jsonObject.getString(Constants.PRODUCT_DESCRIPTION.KEY_PRODUCT_STOCK).length() > 0){
                                    productDescriptionView.showProductStock(jsonObject.getString(Constants.PRODUCT_DESCRIPTION.KEY_PRODUCT_STOCK));
                                }else {
                                    productDescriptionView.showProductStockError(notFound);
                                }
                                if (jsonObject.has(Constants.PRODUCT_DESCRIPTION.KEY_PRODUCT_IMAGE) && !jsonObject.isNull(Constants.PRODUCT_DESCRIPTION.KEY_PRODUCT_IMAGE)
                                        && jsonObject.getString(Constants.PRODUCT_DESCRIPTION.KEY_PRODUCT_IMAGE).length() > 0){
//                                    DescriptionImageSlider descriptionImageSlider = new DescriptionImageSlider();
//                                    descriptionImageSlider.setImageUrl(jsonObject.getString(Constants.PRODUCT_DESCRIPTION.KEY_PRODUCT_IMAGE));
//                                    descriptionImage.add(descriptionImageSlider);
                                    productDescriptionView.showImage(jsonObject.getString(Constants.PRODUCT_DESCRIPTION.KEY_PRODUCT_IMAGE));
                                }else {
                                    productDescriptionView.showImageError(notFound);
                                }

                                //----------------------Product Size Details---------------------------------------------------
                                if (jsonObject.has(Constants.PRODUCT_SIZE_DETAILS.KEY_OPTION)&& !jsonObject.isNull(Constants.PRODUCT_SIZE_DETAILS.KEY_OPTION) &&
                                        jsonObject.getJSONArray(Constants.PRODUCT_SIZE_DETAILS.KEY_OPTION).length()>0){
                                    JSONArray options = jsonObject.getJSONArray(Constants.PRODUCT_SIZE_DETAILS.KEY_OPTION);
                                    for (int i=0; i<options.length(); i++){
                                        JSONObject optionObject = options.getJSONObject(i);
                                        JSONArray product_option_value = optionObject.getJSONArray(Constants.PRODUCT_SIZE_DETAILS.KEY_PRODUCT_OPTION_VALUE);
                                        for (int j=0; j<product_option_value.length(); j++){
                                            JSONObject innerObject = product_option_value.getJSONObject(j);

                                            if (optionObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_TYPE).equalsIgnoreCase("select")){
                                                ProductSizeDetails productSizeDetails = new ProductSizeDetails();
                                                if (innerObject.has(Constants.PRODUCT_SIZE_DETAILS.KEY_PRODUCT_OPTION_VALUE_ID) && !innerObject.isNull(Constants.PRODUCT_SIZE_DETAILS.KEY_PRODUCT_OPTION_VALUE_ID) &&
                                                        innerObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_PRODUCT_OPTION_VALUE_ID).length() > 0){
                                                    productSizeDetails.setProduct_option_value_id(innerObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_PRODUCT_OPTION_VALUE_ID));
                                                }else { productSizeDetails.setProduct_option_value_id(notAvailable); }
                                                if (innerObject.has(Constants.PRODUCT_SIZE_DETAILS.KEY_OPTION_VALUE_ID) && !innerObject.isNull(Constants.PRODUCT_SIZE_DETAILS.KEY_OPTION_VALUE_ID) &&
                                                        innerObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_OPTION_VALUE_ID).length() > 0){
                                                    productSizeDetails.setOption_value_id(innerObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_OPTION_VALUE_ID));
                                                }else { productSizeDetails.setOption_value_id(notAvailable); }
                                                if (innerObject.has(Constants.PRODUCT_SIZE_DETAILS.KEY_PRODUCT_OPTION_NAME) && !innerObject.isNull(Constants.PRODUCT_SIZE_DETAILS.KEY_PRODUCT_OPTION_NAME) &&
                                                        innerObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_PRODUCT_OPTION_NAME).length()>0){
                                                    productSizeDetails.setProduct_option_name(innerObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_PRODUCT_OPTION_NAME));
                                                }else { productSizeDetails.setProduct_option_name(notAvailable); }
                                                if (innerObject.has(Constants.PRODUCT_SIZE_DETAILS.KEY_PRICE) && !innerObject.isNull(Constants.PRODUCT_SIZE_DETAILS.KEY_PRICE) &&
                                                        innerObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_PRICE).length() > 0){
                                                    productSizeDetails.setPrice(innerObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_PRICE));
                                                    Log.d(TAG, "onResponse Price: "+innerObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_PRICE));
                                                }else {  }
                                                if (innerObject.has(Constants.PRODUCT_SIZE_DETAILS.KEY_PRICE_PREFIX) && !innerObject.isNull(Constants.PRODUCT_SIZE_DETAILS.KEY_PRICE_PREFIX) &&
                                                        innerObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_PRICE_PREFIX).length() > 0){
                                                    productSizeDetails.setPrice_prefix(innerObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_PRICE_PREFIX));
                                                }else { productSizeDetails.setPrice_prefix(notAvailable);}
                                                if (optionObject.has(Constants.PRODUCT_SIZE_DETAILS.KEY_OPTION_ID) && !optionObject.isNull(Constants.PRODUCT_SIZE_DETAILS.KEY_OPTION_ID) &&
                                                        optionObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_OPTION_ID).length() > 0){
                                                    productSizeDetails.setOption_id(optionObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_OPTION_ID));
                                                }else {productSizeDetails.setOption_id(notAvailable);}
                                                if (optionObject.has(Constants.PRODUCT_SIZE_DETAILS.KEY_PRODUCT_OPTION_ID) && !optionObject.isNull(Constants.PRODUCT_SIZE_DETAILS.KEY_PRODUCT_OPTION_ID) &&
                                                        optionObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_PRODUCT_OPTION_ID).length()>0){
                                                    productSizeDetails.setProduct_option_id(optionObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_PRODUCT_OPTION_ID));
                                                    Log.d(TAG, "KEY_PRODUCT_OPTION_ID: "+optionObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_PRODUCT_OPTION_ID));
                                                }else {productSizeDetails.setProduct_option_id(notAvailable);}
                                                if (optionObject.has(Constants.PRODUCT_SIZE_DETAILS.KEY_OPTION_NAME) && !optionObject.isNull(Constants.PRODUCT_SIZE_DETAILS.KEY_OPTION_NAME) &&
                                                        optionObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_OPTION_NAME).length() > 0){
                                                    productSizeDetails.setOptions_name(optionObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_OPTION_NAME));
                                                }else {productSizeDetails.setOptions_name(notAvailable);}
                                                if (optionObject.has(Constants.PRODUCT_SIZE_DETAILS.KEY_TYPE) && !optionObject.isNull(Constants.PRODUCT_SIZE_DETAILS.KEY_TYPE) &&
                                                        optionObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_TYPE).length() > 0){
                                                    productSizeDetails.setType(optionObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_TYPE));
                                                }else {productSizeDetails.setType(notAvailable);}

                                                productSizeDetails.setProduct_category("rawFish");

                                                productSize.add(productSizeDetails);
                                                Log.d(TAG, "onResponse: "+productSize.toString());
                                                Log.d(TAG, "onResponse length: "+jsonObject.getJSONArray(Constants.PRODUCT_SIZE_DETAILS.KEY_OPTION).length());
                                            }else {
                                                //productDescriptionView.showSizeListError();
                                            }
                                            if (optionObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_TYPE).equalsIgnoreCase("checkbox")){
                                                ProductSizeDetails productDeliveryOption = new ProductSizeDetails();
                                                if (innerObject.has(Constants.PRODUCT_SIZE_DETAILS.KEY_PRODUCT_OPTION_VALUE_ID) && !innerObject.isNull(Constants.PRODUCT_SIZE_DETAILS.KEY_PRODUCT_OPTION_VALUE_ID) &&
                                                        innerObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_PRODUCT_OPTION_VALUE_ID).length() > 0){
                                                    productDeliveryOption.setProduct_option_value_id(innerObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_PRODUCT_OPTION_VALUE_ID));
                                                }else {productDeliveryOption.setProduct_option_value_id(notAvailable);}
                                                if (innerObject.has(Constants.PRODUCT_SIZE_DETAILS.KEY_OPTION_VALUE_ID) && !innerObject.isNull(Constants.PRODUCT_SIZE_DETAILS.KEY_OPTION_VALUE_ID) &&
                                                        innerObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_OPTION_VALUE_ID).length() > 0){
                                                    productDeliveryOption.setOption_value_id(innerObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_OPTION_VALUE_ID));
                                                }else {productDeliveryOption.setOption_value_id(notAvailable);}
                                                if (innerObject.has(Constants.PRODUCT_SIZE_DETAILS.KEY_PRODUCT_OPTION_NAME) && !innerObject.isNull(Constants.PRODUCT_SIZE_DETAILS.KEY_PRODUCT_OPTION_NAME) &&
                                                        innerObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_PRODUCT_OPTION_NAME).length()>0){
                                                    productDeliveryOption.setProduct_option_name(innerObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_PRODUCT_OPTION_NAME));
                                                }else {productDeliveryOption.setProduct_option_name(notAvailable);}
                                                if (innerObject.has(Constants.PRODUCT_SIZE_DETAILS.KEY_PRICE) && !innerObject.isNull(Constants.PRODUCT_SIZE_DETAILS.KEY_PRICE) &&
                                                        innerObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_PRICE).length() > 0){
                                                    productDeliveryOption.setPrice(innerObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_PRICE));
                                                }else { }
                                                if (innerObject.has(Constants.PRODUCT_SIZE_DETAILS.KEY_PRICE_PREFIX) && !innerObject.isNull(Constants.PRODUCT_SIZE_DETAILS.KEY_PRICE_PREFIX) &&
                                                        innerObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_PRICE_PREFIX).length() > 0){
                                                    productDeliveryOption.setPrice_prefix(innerObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_PRICE_PREFIX));
                                                }else {productDeliveryOption.setPrice_prefix(notAvailable);}
                                                if (optionObject.has(Constants.PRODUCT_SIZE_DETAILS.KEY_OPTION_ID) && !optionObject.isNull(Constants.PRODUCT_SIZE_DETAILS.KEY_OPTION_ID) &&
                                                        optionObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_OPTION_ID).length() > 0){
                                                    productDeliveryOption.setOption_id(optionObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_OPTION_ID));
                                                }else {productDeliveryOption.setOption_id(notAvailable);}
                                                if (optionObject.has(Constants.PRODUCT_SIZE_DETAILS.KEY_PRODUCT_OPTION_ID) && !optionObject.isNull(Constants.PRODUCT_SIZE_DETAILS.KEY_PRODUCT_OPTION_ID) &&
                                                        optionObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_PRODUCT_OPTION_ID).length()>0){
                                                    productDeliveryOption.setProduct_option_id(optionObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_PRODUCT_OPTION_ID));
                                                    Log.d(TAG, "KEY_PRODUCT_OPTION_ID: "+optionObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_PRODUCT_OPTION_ID));
                                                }else {productDeliveryOption.setProduct_option_id(notAvailable);}
                                                if (optionObject.has(Constants.PRODUCT_SIZE_DETAILS.KEY_OPTION_NAME) && !optionObject.isNull(Constants.PRODUCT_SIZE_DETAILS.KEY_OPTION_NAME) &&
                                                        optionObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_OPTION_NAME).length() > 0){
                                                    productDeliveryOption.setOptions_name(optionObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_OPTION_NAME));
                                                }else {productDeliveryOption.setOptions_name(notAvailable);}
                                                if (optionObject.has(Constants.PRODUCT_SIZE_DETAILS.KEY_TYPE) && !optionObject.isNull(Constants.PRODUCT_SIZE_DETAILS.KEY_TYPE) &&
                                                        optionObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_TYPE).length() > 0){
                                                    productDeliveryOption.setType(optionObject.getString(Constants.PRODUCT_SIZE_DETAILS.KEY_TYPE));
                                                }else {productDeliveryOption.setType(notAvailable);}

                                                productDeliveryOption.setProduct_category("rawFish");

                                                deliveryOption.add(productDeliveryOption);
                                                Log.d(TAG, "onResponse: "+deliveryOption.toString());
                                                Log.d(TAG, "onResponse length: "+jsonObject.getJSONArray(Constants.PRODUCT_SIZE_DETAILS.KEY_OPTION).length());
                                            }else {
                                                productDescriptionView.showDeliveryOptionError();
                                            }
                                        }
                                    }
                                    productDescriptionView.showDeliveryOption(deliveryOption);
                                    productDescriptionView.showSizeList(productSize);
                                    productDescriptionView.showSizeRelatedPrice(productSize,String.valueOf(jsonObject.getInt("product_id")));
                                    //productDescriptionView.showItemQuantityInNumberError();
                                }else {
                                    productDescriptionView.showDeliveryOptionError();
                                    productDescriptionView.showSizeListError();
                                    productDescriptionView.showSizeRelatedPriceError();
                                    //productDescriptionView.showItemQuantityInKgError();
                                }

                                productDescriptionView.hideProgressBar();
                                Log.d(TAG, "onResponse: "+jsonObject.getString(Constants.PRODUCT_DESCRIPTION.KEY_PRODUCT_IMAGE));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            productDescriptionView.showProductNameError(notFound);
                            productDescriptionView.showDescriptionError(notFound);
                            productDescriptionView.showDeliveryTimeError(notFound);
                            productDescriptionView.showProductPriceError(notFound);
                            productDescriptionView.showImageError(notFound);
                            productDescriptionView.hideProgressBar();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                productDescriptionView.showProductNameError(notFound);
                productDescriptionView.showDescriptionError(notFound);
                productDescriptionView.showDeliveryTimeError(notFound);
                productDescriptionView.showProductPriceError(notFound);
                productDescriptionView.showImageError(notFound);
                productDescriptionView.hideProgressBar();
                productDescriptionView.showServerError();
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

        RequestQueue requestQueue = Volley.newRequestQueue((Context) this.productDescriptionView);
        requestQueue.add(stringRequest);
    }

    @Override
    public void saveWishListItemOnServer(String customer_email,String product_id) {

        productDescriptionView.showProgressBar();
        //String JSON_URL = "http://166.62.52.188/Murighonto_new/index.php?route=restapi/account/wishlist/appAddToWishlist";
        String JSON_URL = Url.SAVE_WISH_LIST_ON_SERVER_URL;

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put(Constants.ADD_TO_WISH_LIST.KEY_CUSTOMER_EMAIL, customer_email);
        postParam.put(Constants.ADD_TO_WISH_LIST.KEY_PRODUCT_ID, product_id);

        JSONObject postData = new JSONObject(postParam);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, JSON_URL,postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "getDataFromServer: "+response.toString());
                        if (response.length()>0){
                            try {
                                JSONObject rootObject = new JSONObject(response.toString());
                                String status = rootObject.getString("status");
                                Log.d(TAG, "onResponse: "+status);
                                if (status.equalsIgnoreCase("success")){
                                    productDescriptionView.hideProgressBar();
                                    productDescriptionView.showWishListedItemSuccess(rootObject.getString("message"));

                                    productDescriptionView.setWishListCount();

                                }else {
                                    productDescriptionView.hideProgressBar();
                                    productDescriptionView.showWishListedItemFailure();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                productDescriptionView.hideProgressBar();
                productDescriptionView.showServerError();
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

        RequestQueue requestQueue = Volley.newRequestQueue((Context) this.productDescriptionView);
        requestQueue.add(stringRequest);
    }

    @Override
    public void addRawFishItemToCart(String customer_email, String session_data,int quantity_in_kg,int product_id, String product_option_id,
                              ArrayList<String> product_option_value_id, String size_product_option_id,
                              String size_product_option_value_id) {
        Log.d(TAG, "addRawFishItemToCart: "+customer_email+"/"+quantity_in_kg+"/"+product_id+"/"+product_option_id+"/"+product_option_value_id
                +"/"+size_product_option_id+"/"+size_product_option_value_id);

        productDescriptionView.showProgressBar();
        //String JSON_URL = "http://166.62.52.188/Murighonto_new/index.php?route=restapi/checkout/cart/appAddToCart";
        String JSON_URL = Url.ADD_ITEM_IN_CART_URL;
        Log.d(TAG, "addRawFishItemToCart: "+JSON_URL);

        try {
            JSONObject rootObject = new JSONObject();
            rootObject.put("product_id",product_id);
            rootObject.put("quantity",quantity_in_kg);
            rootObject.put("email",customer_email);
            rootObject.put("session_data",session_data);

            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for (int i=0; i<product_option_value_id.size(); i++){
                jsonArray.put(product_option_value_id.get(i));
            }
            jsonObject.put(product_option_id,jsonArray);
            jsonObject.put(size_product_option_id,size_product_option_value_id);
            rootObject.put("options",jsonObject);

            Log.d(TAG, "addItemToCart: "+rootObject.toString());

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, JSON_URL,rootObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "getDataFromServer: "+response.toString());
                            try {
                                JSONObject responseObject = new JSONObject(response.toString());
                                String status = responseObject.getString("status");
                                if (status.equalsIgnoreCase("success")){
                                    productDescriptionView.hideProgressBar();
                                    productDescriptionView.showItemAddedToCartSuccess(responseObject.getString("message"));
                                    productDescriptionView.setCartCount();
                                }else {productDescriptionView.showItemAddToCartError();}
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    productDescriptionView.hideProgressBar();
                    productDescriptionView.showServerError();
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

            RequestQueue requestQueue = Volley.newRequestQueue((Context) this.productDescriptionView);
            requestQueue.add(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void addRestaurantItemToCart(String customer_email, String session_data,int quantity, int product_id) {
        Log.d(TAG, "addRestaurantItemToCart: "+customer_email+"/"+quantity+"/"+product_id);

        productDescriptionView.showProgressBar();
        //String JSON_URL = "http://166.62.52.188/Murighonto_new/index.php?route=restapi/checkout/cart/appAddToCart";
        String JSON_URL = Url.ADD_ITEM_IN_CART_URL;

        try {
            JSONObject rootObject = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            rootObject.put("product_id",product_id);
            rootObject.put("quantity",quantity);
            rootObject.put("email",customer_email);
            rootObject.put("session_data",session_data);
            rootObject.put("options",jsonObject);
            Log.d(TAG, "addRestaurantItemToCart: "+rootObject);

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, JSON_URL,rootObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "getDataFromServer: "+response.toString());
                            try {
                                JSONObject responseObject = new JSONObject(response.toString());
                                String status = responseObject.getString("status");
                                if (status.equalsIgnoreCase("success")){
                                    productDescriptionView.hideProgressBar();
                                    productDescriptionView.showItemAddedToCartSuccess(responseObject.getString("message"));
                                    productDescriptionView.setCartCount();
                                }else {
                                    productDescriptionView.hideProgressBar();
                                    productDescriptionView.showItemAddToCartError();}

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    productDescriptionView.hideProgressBar();
                    productDescriptionView.showServerError();
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

            RequestQueue requestQueue = Volley.newRequestQueue((Context) this.productDescriptionView);
            requestQueue.add(stringRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gotoWishList() {
        productDescriptionView.navigateToWishList();
    }

    @Override
    public void gotoCart() {
        productDescriptionView.navigateToCart();
    }

//    @Override
//    public void fetchProductListDataFromServer(String category_id) {
//
//        productListView.showProgress();
//        //String JSON_URL = "http://166.62.52.188/Murighonto_new/index.php?route=restapi/product/category/product&cat_id="+category_id;
//        String JSON_URL = Url.PRODUCT_LIST_URL+category_id;
//        Log.d(TAG, "fetchProductListDataFromServer: "+JSON_URL);
//
//        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL,null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.d(TAG, "onResponse: "+response.toString());
//                        if (response.length() > 0){
//                            try {
//                                JSONObject rootObject = new JSONObject(response.toString());
//                                JSONArray rootArray = rootObject.getJSONArray(Constants.PRODUCT_LIST_DETAILS.KEY_PRODUCT);
//                                if (rootObject.getJSONArray(Constants.PRODUCT_LIST_DETAILS.KEY_PRODUCT).length()>0){
//                                    for (int i=0; i<rootArray.length(); i++){
//                                        JSONObject innerObject = rootArray.getJSONObject(i);
//                                        ProductListDetails productListDetails = new ProductListDetails();
//                                        productListDetails.setProduct_id(innerObject.getString(Constants.PRODUCT_LIST_DETAILS.KEY_PRODUCT_ID));
//                                        productListDetails.setProduct_name(innerObject.getString(Constants.PRODUCT_LIST_DETAILS.KEY_PRODUCT_NAME));
//                                        productListDetails.setProduct_price(innerObject.getString(Constants.PRODUCT_LIST_DETAILS.KEY_PRODUCT_PRICE));
//                                        productListDetails.setProduct_image(innerObject.getString(Constants.PRODUCT_LIST_DETAILS.KEY_PRODUCT_IMAGE));
//                                        productListArray.add(productListDetails);
//                                    }
//                                }else {productListView.hideProgress();}
//                            } catch (JSONException e) {
//                                productListView.hideProgress();
//                                e.printStackTrace();
//                            }
//
//                            if (productListArray.size() > 0){
//                                productListView.setProductListSuccess(productListArray);
//                                productListView.hideProgress();
//                            }else {
//                                productListView.hideProgress();
//                                productListView.setProductListError();
//                            }
//                        }else {
//                            productListView.hideProgress();
//                            productListView.setProductListError();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                productListView.hideProgress();
//                productListView.showServerError();
//                VolleyLog.d("", "Error: " + error.getMessage());
//                Log.d("error", "OnError: " + 12);
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json; charset=utf-8");
//                return headers;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue((Context) this.productListView);
//        requestQueue.add(stringRequest);
//    }
}
