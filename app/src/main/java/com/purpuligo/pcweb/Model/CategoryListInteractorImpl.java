package com.purpuligo.pcweb.Model;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.purpuligo.pcweb.Constants.Constants;
import com.purpuligo.pcweb.Global.Url;
import com.purpuligo.pcweb.Model.Pojo.ImageSliderPojo;
import com.purpuligo.pcweb.Model.Pojo.ProductCategoriesDetails;
import com.purpuligo.pcweb.Presenter.CategoryListPresenter;
import com.purpuligo.pcweb.View.CategoryListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoryListInteractorImpl implements CategoryListPresenter {

    private static final String TAG = "CategoryListInterImpl";
    private CategoryListView categoryListView;
    private ArrayList<ProductCategoriesDetails> categoryDataList;
    private List<ImageSliderPojo> imageSliderList;

    public CategoryListInteractorImpl(CategoryListView categoryListView) {
        this.categoryListView = categoryListView;

        categoryDataList = new ArrayList<>();
        imageSliderList = new ArrayList<>();
    }

    @Override
    public void fetchCategoryListFromServer(final int category_position) {

        categoryListView.showProgress();
        //String JSON_URL = "http://166.62.52.188/Murighonto_new/index.php?route=restapi/product/category";
        String JSON_URL = Url.HOME_DATA_URL;
        Log.d(TAG, "fetchCategoryListFromServer: "+JSON_URL);

        JsonArrayRequest req = new JsonArrayRequest(JSON_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "jsonData : "+response.toString());
                        if (response.length() > 0){
                            try {
                                JSONArray rootArray = new JSONArray(response.toString());
                                JSONObject rootObject = rootArray.getJSONObject(category_position-1);
                                Log.d(TAG, "onResponse: "+rootObject);
                                JSONArray insideArray = rootObject.getJSONArray(Constants.CATEGORIES_DETAILS.KEY_PARENT_CATEGORIES);
                                Log.d(TAG, "onResponse: "+rootObject.getJSONArray(Constants.CATEGORIES_DETAILS.KEY_PARENT_CATEGORIES));
                                if (!insideArray.equals(null)){
                                    if (rootObject.getJSONArray(Constants.CATEGORIES_DETAILS.KEY_PARENT_CATEGORIES).length()>0){
                                        for (int j=0; j<insideArray.length(); j++){
                                            JSONObject insideObject = insideArray.getJSONObject(j);
                                            ProductCategoriesDetails rawFishDetails = new ProductCategoriesDetails();
                                            rawFishDetails.setCategory_name(insideObject.getString(Constants.CATEGORIES_DETAILS.KEY_CATEGORIES_NAME));
                                            rawFishDetails.setCategory_id(insideObject.getString(Constants.CATEGORIES_DETAILS.KEY_CATEGORIES_ID));
                                            rawFishDetails.setCategory_image(insideObject.getString(Constants.CATEGORIES_DETAILS.KEY_CATEGORIES_IMAGE));
                                            categoryDataList.add(rawFishDetails);
                                        }
                                    }else {categoryListView.hideProgress();}
                                }
                                Log.d(TAG, "onResponse: "+categoryDataList);
                                if (categoryDataList.size() > 0){categoryListView.setProductListSuccess(categoryDataList);
                                } else{categoryListView.setProductListError();}

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            categoryListView.hideProgress();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                categoryListView.hideProgress();
                categoryListView.showServerError();
                Log.d(TAG, "Master Data Server Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue((Context) this.categoryListView);
        int socketTimeout = 200000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        req.setRetryPolicy(policy);
        requestQueue.add(req);
    }

    @Override
    public void fetchWishListDataFromServer() {
        categoryListView.navigateToWishList();
    }

    @Override
    public void fetchCartDataFromServer() {
        categoryListView.navigateToCart();
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
                                categoryListView.showImageSlider(imageSliderList);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                categoryListView.showImageSliderError();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                categoryListView.showImageSliderError();
                Log.d(TAG, "Master Data Server Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue((Context) this.categoryListView);
        int socketTimeout = 200000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        req.setRetryPolicy(policy);
        requestQueue.add(req);
    }
}
