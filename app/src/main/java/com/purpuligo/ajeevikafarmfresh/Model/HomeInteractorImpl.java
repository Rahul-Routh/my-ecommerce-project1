package com.purpuligo.ajeevikafarmfresh.Model;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.purpuligo.ajeevikafarmfresh.Constants.Constants;
import com.purpuligo.ajeevikafarmfresh.Fragment.BottomSheetDialogFragment;
import com.purpuligo.ajeevikafarmfresh.Global.Url;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.ImageSliderPojo;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.ParentDetails;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.ProductCategoriesDetails;
import com.purpuligo.ajeevikafarmfresh.Presenter.HomePresenter;
import com.purpuligo.ajeevikafarmfresh.R;
import com.purpuligo.ajeevikafarmfresh.View.HomeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeInteractorImpl implements HomePresenter {

    private static final String TAG = "HomeInteractorImpl";

    private HomeView homeView;
    private ArrayList<ParentDetails> parentDetailList;
    private ArrayList<ProductCategoriesDetails> rawFishData;
    private ArrayList<ProductCategoriesDetails> restaurantData;
    //private BottomSheetDialogFragment bottomSheetDialogFragment;
    private List<ImageSliderPojo> imageSliderList;

    //horizontal product Layout
    private TextView horizontalLayoutTitle;
    private Button horizontalViewAllbtn;
    private RecyclerView horizontalRecyclerView;
    //horizontal product Layout

    public HomeInteractorImpl(HomeView homeView) {
        this.homeView = homeView;

        parentDetailList = new ArrayList<>();
        rawFishData = new ArrayList<>();
        imageSliderList = new ArrayList<>();
        restaurantData = new ArrayList<>();
        //bottomSheetDialogFragment = new BottomSheetDialogFragment();
    }

    @Override
    public void fetchCurrentVersion() {
        String JSON_URL = Url.VERSION_COMPARE_URL;
        Log.d(TAG, "fetchCurrentVersion: "+JSON_URL);

        JsonArrayRequest req = new JsonArrayRequest(JSON_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "fetchCurrentVersion jsonData : "+response.toString());
                        if (response.length() > 0){
                            try {
                                JSONArray rootArray = new JSONArray(response.toString());
                                for (int i=0; i<rootArray.length(); i++){
                                    JSONObject rootObject = rootArray.getJSONObject(i);
                                    homeView.showCurrentVersion(rootObject.getString("version_code"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                homeView.showCurrentVersionError();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                homeView.showCurrentVersionError();
                Log.d(TAG, "Master Data Server Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue((Context) this.homeView);
        int socketTimeout = 200000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        req.setRetryPolicy(policy);
        requestQueue.add(req);
    }

    @Override
    public void fetchDataFromServer() {

        homeView.showProgress();
        //String JSON_URL = "http://166.62.52.188/Murighonto_new/index.php?route=restapi/product/category";
        String JSON_URL = Url.HOME_DATA_URL;
        Log.d(TAG, "fetchDataFromServer: "+JSON_URL);

        JsonArrayRequest req = new JsonArrayRequest(JSON_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "jsonData : "+response.toString());
                        if (response.length() > 0){
                            try {
                                JSONArray rootArray = new JSONArray(response.toString());
                                for (int i=0; i<rootArray.length(); i++){
                                    JSONObject rootObject = rootArray.getJSONObject(i);
                                    ParentDetails parentDetails = new ParentDetails();
                                    parentDetails.setParent_name(rootObject.getString(Constants.CATEGORIES_DETAILS.KEY_PARENT_NAME));
                                    parentDetails.setParent_id(rootObject.getString(Constants.CATEGORIES_DETAILS.KEY_PARENT_ID));
                                    parentDetails.setParent_image(rootObject.getString(Constants.CATEGORIES_DETAILS.KEY_PARENT_IMAGE));
                                    JSONArray insideArray = rootObject.getJSONArray(Constants.CATEGORIES_DETAILS.KEY_PARENT_CATEGORIES);
                                    if (insideArray != null){
                                        for (int j=0; j<insideArray.length(); j++){
                                            JSONObject insideObject = insideArray.getJSONObject(j);
                                            ProductCategoriesDetails rawFishDetails = new ProductCategoriesDetails();

                                            rawFishDetails.setCategory_name(insideObject.getString(Constants.CATEGORIES_DETAILS.KEY_CATEGORIES_NAME));
                                            rawFishDetails.setCategory_id(insideObject.getString(Constants.CATEGORIES_DETAILS.KEY_CATEGORIES_ID));
                                            rawFishDetails.setCategory_image(insideObject.getString(Constants.CATEGORIES_DETAILS.KEY_CATEGORIES_IMAGE));
                                            rawFishData.add(rawFishDetails);
                                            Log.d(TAG, "onResponse: "+rawFishDetails);
                                        }
                                    }
                                    parentDetails.setCategories(rawFishData);
                                    parentDetailList.add(parentDetails);
                                    Log.d(TAG, "onResponse: "+parentDetails);
                                }
                                Log.d(TAG, "onResponse: "+parentDetailList);
                                if (parentDetailList.size() > 0)
                                    homeView.setRawFishListSuccess(parentDetailList);
                                else
                                    homeView.setRawFishListError();

//                                if (restaurantData.size() > 0)
//                                    homeView.setRestaurantListSuccess(restaurantData);
//                                else
//                                    homeView.setRestaurantListError();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            homeView.hideProgress();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                homeView.hideProgress();
                Log.d(TAG, "Master Data Server Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue((Context) this.homeView);
        int socketTimeout = 200000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        req.setRetryPolicy(policy);
        requestQueue.add(req);
    }

    @Override
    public void fetchWishListDataFromServer() {
        homeView.navigateToWishList();
    }

    @Override
    public void fetchCartDataFromServer() {
        homeView.navigateToCart();
    }

    @Override
    public void fetchWishListCount(String customer_email) {

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
                        Log.d(TAG, "fetchWishListDataFromServer: "+response.toString());
                        if (response.length() > 0){
                            try {
                                JSONObject rootObject = new JSONObject(response.toString());
                                if (rootObject.getString(Constants.FETCH_WISH_LIST_DATA.KEY_STATUS).equalsIgnoreCase("success")){
                                    if (rootObject.has(Constants.FETCH_WISH_LIST_DATA.KEY_WISHLIST_PRODUCT) && !rootObject.isNull(Constants.FETCH_WISH_LIST_DATA.KEY_WISHLIST_PRODUCT) &&
                                            rootObject.getJSONArray(Constants.FETCH_WISH_LIST_DATA.KEY_WISHLIST_PRODUCT).length() > 0){
                                        Log.d(TAG, "fetchWishListCount: "+rootObject.getJSONArray(Constants.FETCH_WISH_LIST_DATA.KEY_WISHLIST_PRODUCT).length());
                                        homeView.setWishListCount(String.valueOf(rootObject.getJSONArray(Constants.FETCH_WISH_LIST_DATA.KEY_WISHLIST_PRODUCT).length()));
                                    }else {homeView.setWishListCountError();}
                                }else {homeView.setWishListCountError();}
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }catch (Exception e){e.printStackTrace();}
                        }else {}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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

        RequestQueue requestQueue = Volley.newRequestQueue((Context) this.homeView);
        requestQueue.add(stringRequest);
    }

    @Override
    public void fetchCartCount(String customer_email, String session_data) {

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
                        Log.d(TAG, "fetchCartDetailsFromServer: "+response.toString());
                        if (response.length()>0){
                            try {
                                JSONObject rootObject = new JSONObject(response.toString());
                                if (rootObject.has(Constants.CART_DATA.KEY_PRODUCTS) && !rootObject.isNull(Constants.CART_DATA.KEY_PRODUCTS) &&
                                        rootObject.getJSONArray(Constants.CART_DATA.KEY_PRODUCTS).length()>0){
                                    Log.d(TAG, "fetchCartCount: "+rootObject.getJSONArray(Constants.CART_DATA.KEY_PRODUCTS).length());
                                    homeView.setCartCount(String.valueOf(rootObject.getJSONArray(Constants.CART_DATA.KEY_PRODUCTS).length()));
                                }else {homeView.setCartCountError();}
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {homeView.setCartCountError();}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
        RequestQueue requestQueue = Volley.newRequestQueue((Context) this.homeView);
        requestQueue.add(stringRequest);
    }

    @Override
    public void fetchImageSlider() {
        String JSON_URL = Url.IMAGE_SLIDER_URL;
        Log.d(TAG, "fetchImageSlider: "+JSON_URL);

        JsonArrayRequest req = new JsonArrayRequest(JSON_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "fetchImageSlider jsonData : "+response.toString());
                        if (response.length() > 0){
                            try {
                                JSONArray rootArray = new JSONArray(response.toString());
                                for (int i=0; i<rootArray.length(); i++){
                                    JSONObject rootObject = rootArray.getJSONObject(i);
                                    ImageSliderPojo imageSliderPojo = new ImageSliderPojo();
                                    imageSliderPojo.setSlider_id(rootObject.getString("slider_id"));
                                    imageSliderPojo.setSlider_name(rootObject.getString("slider_name"));
                                    imageSliderPojo.setSlider_url(rootObject.getString("slider_url"));
                                    imageSliderList.add(imageSliderPojo);
                                }
                                homeView.showImageSlider(imageSliderList);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                homeView.showImageSliderError();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                homeView.showImageSliderError();
                Log.d(TAG, "Master Data Server Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue((Context) this.homeView);
        int socketTimeout = 200000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        req.setRetryPolicy(policy);
        requestQueue.add(req);
    }

}
