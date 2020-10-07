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
import com.purpuligo.ajeevikafarmfresh.Global.NetworkState;
import com.purpuligo.ajeevikafarmfresh.Global.Url;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.WishListItems;
import com.purpuligo.ajeevikafarmfresh.Presenter.WishListPresenter;
import com.purpuligo.ajeevikafarmfresh.View.WishListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WishListInteractorImpl implements WishListPresenter {

    private String TAG = "WishListInteractorImpl";
    private WishListView wishListView;
    private ArrayList<WishListItems> wishListArray;
    private NetworkState networkState;
    private String notAvailable = "Not Available";
    private int notValid = 0;

    public WishListInteractorImpl(WishListView wishListView) {
        this.wishListView = wishListView;

        networkState = new NetworkState();
        wishListArray = new ArrayList<>();
    }

    @Override
    public void fetchWishListDataFromServer(String customer_email) {

        wishListView.showProgressBar();
        //String JSON_URL = "http://166.62.52.188/Murighonto_new/index.php?route=restapi/account/wishlist/appGetWishlist";
        String JSON_URL = Url.WISH_LIST_URL;

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put(Constants.ADD_TO_WISH_LIST.KEY_CUSTOMER_EMAIL, customer_email);
        JSONObject postData = new JSONObject(postParam);
        Log.d(TAG, "fetchWishListDataFromServer: "+postData.toString());

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, JSON_URL,postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response.toString());
                        if (response.length() > 0){
                            try {
                                JSONObject rootObject = new JSONObject(response.toString());
                                if (rootObject.getString(Constants.FETCH_WISH_LIST_DATA.KEY_STATUS).equalsIgnoreCase("success")){
                                    if (rootObject.has(Constants.FETCH_WISH_LIST_DATA.KEY_WISHLIST_PRODUCT) && !rootObject.isNull(Constants.FETCH_WISH_LIST_DATA.KEY_WISHLIST_PRODUCT) &&
                                            rootObject.getJSONArray(Constants.FETCH_WISH_LIST_DATA.KEY_WISHLIST_PRODUCT).length() > 0){
                                        JSONArray rootArray = rootObject.getJSONArray(Constants.FETCH_WISH_LIST_DATA.KEY_WISHLIST_PRODUCT);
                                        for (int i=0; i<rootArray.length(); i++){
                                            JSONObject innerObject = rootArray.getJSONObject(i);
                                            WishListItems wishListItems = new WishListItems();
                                            if (innerObject.has(Constants.FETCH_WISH_LIST_DATA.KEY_PRODUCT_ID) && !innerObject.isNull(Constants.FETCH_WISH_LIST_DATA.KEY_PRODUCT_ID) &&
                                                    innerObject.getString(Constants.FETCH_WISH_LIST_DATA.KEY_PRODUCT_ID).length()>0){
                                                wishListItems.setProduct_id(innerObject.getString(Constants.FETCH_WISH_LIST_DATA.KEY_PRODUCT_ID));
                                            }else { wishListItems.setProduct_id(notAvailable); }
                                            if (innerObject.has(Constants.FETCH_WISH_LIST_DATA.KEY_PRODUCT_TITLE) && !innerObject.isNull(Constants.FETCH_WISH_LIST_DATA.KEY_PRODUCT_TITLE) &&
                                                    innerObject.getString(Constants.FETCH_WISH_LIST_DATA.KEY_PRODUCT_TITLE).length()>0){
                                                wishListItems.setProduct_title(innerObject.getString(Constants.FETCH_WISH_LIST_DATA.KEY_PRODUCT_TITLE));
                                            }else { wishListItems.setProduct_title(notAvailable); }
                                            if (innerObject.has(Constants.FETCH_WISH_LIST_DATA.KEY_IS_GIFT_PRODUCT) && !innerObject.isNull(Constants.FETCH_WISH_LIST_DATA.KEY_IS_GIFT_PRODUCT) &&
                                                    innerObject.getString(Constants.FETCH_WISH_LIST_DATA.KEY_IS_GIFT_PRODUCT).length()>0){
                                                wishListItems.setIs_gift_product(Constants.FETCH_WISH_LIST_DATA.KEY_IS_GIFT_PRODUCT);
                                            }else { wishListItems.setIs_gift_product(notAvailable); }
                                            if (innerObject.has(Constants.FETCH_WISH_LIST_DATA.KEY_ID_PRODUCT_ATTRIBUTE) && !innerObject.isNull(Constants.FETCH_WISH_LIST_DATA.KEY_ID_PRODUCT_ATTRIBUTE)){
                                                wishListItems.setId_product_attribute(innerObject.getInt(Constants.FETCH_WISH_LIST_DATA.KEY_ID_PRODUCT_ATTRIBUTE));
                                            }else { wishListItems.setId_product_attribute(notValid); }
                                            if (innerObject.has(Constants.FETCH_WISH_LIST_DATA.KEY_ID_ADDRESS_DELIVERY) && !innerObject.isNull(Constants.FETCH_WISH_LIST_DATA.KEY_ID_ADDRESS_DELIVERY)){
                                                wishListItems.setId_address_delivery(innerObject.getInt(Constants.FETCH_WISH_LIST_DATA.KEY_ID_PRODUCT_ATTRIBUTE));
                                            }else { wishListItems.setId_address_delivery(notValid); }
                                            if (innerObject.has(Constants.FETCH_WISH_LIST_DATA.KEY_STOCK) && !innerObject.isNull(Constants.FETCH_WISH_LIST_DATA.KEY_STOCK)){
                                                wishListItems.setStock(innerObject.getBoolean(Constants.FETCH_WISH_LIST_DATA.KEY_STOCK));
                                            }else {wishListItems.setStock(false);}
                                            if (innerObject.has(Constants.FETCH_WISH_LIST_DATA.KEY_DISCOUNT_PRICE) && !innerObject.isNull(Constants.FETCH_WISH_LIST_DATA.KEY_DISCOUNT_PRICE) &&
                                                    innerObject.getString(Constants.FETCH_WISH_LIST_DATA.KEY_DISCOUNT_PRICE).length()>0){
                                                wishListItems.setDiscount_price(Constants.FETCH_WISH_LIST_DATA.KEY_DISCOUNT_PRICE);
                                            }else { wishListItems.setDiscount_price(notAvailable); }
                                            if (innerObject.has(Constants.FETCH_WISH_LIST_DATA.KEY_DISCOUNT_PERCENTAGE) && !innerObject.isNull(Constants.FETCH_WISH_LIST_DATA.KEY_DISCOUNT_PERCENTAGE)){
                                                wishListItems.setDiscount_percentage(innerObject.getInt(Constants.FETCH_WISH_LIST_DATA.KEY_DISCOUNT_PERCENTAGE));
                                            }else {wishListItems.setDiscount_percentage(notValid);}
                                            if (innerObject.has(Constants.FETCH_WISH_LIST_DATA.KEY_IMAGES) && !innerObject.isNull(Constants.FETCH_WISH_LIST_DATA.KEY_IMAGES) &&
                                                    innerObject.getString(Constants.FETCH_WISH_LIST_DATA.KEY_IMAGES).length()>0){
                                                wishListItems.setImages(innerObject.getString(Constants.FETCH_WISH_LIST_DATA.KEY_IMAGES));
                                            }else {wishListItems.setImages(notAvailable);}
                                            if (innerObject.has(Constants.FETCH_WISH_LIST_DATA.KEY_PRICE) && !innerObject.isNull(Constants.FETCH_WISH_LIST_DATA.KEY_PRICE) &&
                                                    innerObject.getString(Constants.FETCH_WISH_LIST_DATA.KEY_PRICE).length()>0){
                                                wishListItems.setPrice(innerObject.getString(Constants.FETCH_WISH_LIST_DATA.KEY_PRICE));
                                            }else {wishListItems.setPrice(notAvailable);}
                                            if (innerObject.has(Constants.FETCH_WISH_LIST_DATA.KEY_ALLOW_OUT_OF_STOCK) && !innerObject.isNull(Constants.FETCH_WISH_LIST_DATA.KEY_ALLOW_OUT_OF_STOCK)){
                                                wishListItems.setAllow_out_of_stock(innerObject.getInt(Constants.FETCH_WISH_LIST_DATA.KEY_ALLOW_OUT_OF_STOCK));
                                            }else {wishListItems.setAllow_out_of_stock(notValid);}
                                            if (innerObject.has(Constants.FETCH_WISH_LIST_DATA.KEY_SHOW_PRICE) && !innerObject.isNull(Constants.FETCH_WISH_LIST_DATA.KEY_SHOW_PRICE) &&
                                                    innerObject.getString(Constants.FETCH_WISH_LIST_DATA.KEY_SHOW_PRICE).length()>0){
                                                wishListItems.setShow_price(innerObject.getString(Constants.FETCH_WISH_LIST_DATA.KEY_SHOW_PRICE));
                                            }else {wishListItems.setShow_price(notAvailable);}
                                            if (innerObject.has(Constants.FETCH_WISH_LIST_DATA.KEY_AVAILABLE_FOR_ORDER) && !innerObject.isNull(Constants.FETCH_WISH_LIST_DATA.KEY_AVAILABLE_FOR_ORDER)){
                                                wishListItems.setAvailable_for_order(innerObject.getInt(Constants.FETCH_WISH_LIST_DATA.KEY_AVAILABLE_FOR_ORDER));
                                            }else {wishListItems.setAvailable_for_order(notValid);}
                                            if (innerObject.has(Constants.FETCH_WISH_LIST_DATA.KEY_MINIMAL_QUANTITY) && !innerObject.isNull(Constants.FETCH_WISH_LIST_DATA.KEY_MINIMAL_QUANTITY) &&
                                                    innerObject.getString(Constants.FETCH_WISH_LIST_DATA.KEY_MINIMAL_QUANTITY).length()>0){
                                                wishListItems.setMinimal_quantity(innerObject.getString(Constants.FETCH_WISH_LIST_DATA.KEY_MINIMAL_QUANTITY));
                                            }else {wishListItems.setMinimal_quantity(notAvailable);}
                                            if (innerObject.has(Constants.FETCH_WISH_LIST_DATA.KEY_QUANTITY) && !innerObject.isNull(Constants.FETCH_WISH_LIST_DATA.KEY_QUANTITY) &&
                                                    innerObject.getString(Constants.FETCH_WISH_LIST_DATA.KEY_QUANTITY).length()>0){
                                                wishListItems.setQuantity(innerObject.getString(Constants.FETCH_WISH_LIST_DATA.KEY_QUANTITY));
                                            }else {wishListItems.setQuantity(notAvailable);}

                                            if (rootObject.has(Constants.FETCH_WISH_LIST_DATA.KEY_STATUS) && !rootObject.isNull(Constants.FETCH_WISH_LIST_DATA.KEY_STATUS) &&
                                                    rootObject.getString(Constants.FETCH_WISH_LIST_DATA.KEY_STATUS).length()>0){
                                                wishListItems.setStatus(rootObject.getString(Constants.FETCH_WISH_LIST_DATA.KEY_STATUS));
                                            }else {wishListItems.setStatus("failed");}
                                            if (rootObject.has(Constants.FETCH_WISH_LIST_DATA.KEY_MESSAGE) && !rootObject.isNull(Constants.FETCH_WISH_LIST_DATA.KEY_STATUS) &&
                                                    rootObject.getString(Constants.FETCH_WISH_LIST_DATA.KEY_STATUS).length()>0){
                                                wishListItems.setMessage(rootObject.getString(Constants.FETCH_WISH_LIST_DATA.KEY_STATUS));
                                            }else {wishListItems.setMessage(notAvailable);}
                                            wishListArray.add(wishListItems);
                                        }
                                        wishListView.showWishList(wishListArray);
                                        wishListView.hideProgressBar();
                                    }else {
                                        wishListView.showWishListError();
                                        wishListView.hideProgressBar();}
                                }else {
                                    wishListView.showWishListError();
                                    wishListView.hideProgressBar();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            wishListView.showWishListError();
                            wishListView.hideProgressBar();
                        }
                }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            wishListView.hideProgressBar();
            wishListView.showServerError();
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

    RequestQueue requestQueue = Volley.newRequestQueue((Context) this.wishListView);
        requestQueue.add(stringRequest);
    }

    @Override
    public void removeSelectedWishListItem(String customer_email, String product_id) {

        wishListView.showProgressBar();
        Log.d(TAG, "removeSelectedWishListItem: "+customer_email+"="+product_id);
        //String JSON_URL = "http://166.62.52.188/Murighonto_new/index.php?route=restapi/account/wishlist/appRemoveWishlist";
        String JSON_URL = Url.REMOVE_WISH_LIST_ITEM_URL;

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
                                if (rootObject.getString("status").equalsIgnoreCase("success")){
                                    wishListView.hideProgressBar();
                                    wishListView.showRemovedProductFromWishList(rootObject.getString("message"));
                                }else {
                                    wishListView.hideProgressBar();
                                    wishListView.showRemovedProductFromWishListError();}
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            wishListView.hideProgressBar();
                            wishListView.showRemovedProductFromWishListError();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                wishListView.hideProgressBar();
                wishListView.showServerError();
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

        RequestQueue requestQueue = Volley.newRequestQueue((Context) this.wishListView);
        requestQueue.add(stringRequest);
    }
}
