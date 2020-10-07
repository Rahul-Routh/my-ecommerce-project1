package com.purpuligo.ajeevikafarmfresh.Model;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.purpuligo.ajeevikafarmfresh.Constants.Constants;
import com.purpuligo.ajeevikafarmfresh.Global.Url;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.ProductCategoriesDetails;
import com.purpuligo.ajeevikafarmfresh.Presenter.SubCategoryPresenter;
import com.purpuligo.ajeevikafarmfresh.View.SubCategoryView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubCategoryInteractorImpl implements SubCategoryPresenter {

    private String TAG = "SubCategoryInteractorImpl";
    private SubCategoryView subCategoryView;
    private ArrayList<ProductCategoriesDetails> subCategoryDataList;

    public SubCategoryInteractorImpl(SubCategoryView subCategoryView) {
        this.subCategoryView = subCategoryView;

        subCategoryDataList = new ArrayList<>();
    }

    @Override
    public void fetchCategoryListFromServer(int category_id) {
        subCategoryView.showProgress();
        //String JSON_URL = "http://166.62.52.188/Murighonto_new/index.php?route=restapi/product/category";
        String JSON_URL = Url.PRODUCT_LIST_URL+category_id;
        Log.d(TAG, "fetchCategoryListFromServer: "+JSON_URL);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "jsonData : "+response.toString());
                        if (response.length() > 0){
                            try {
                                JSONObject rootObject = new JSONObject(response.toString());
                                JSONArray insideArray = rootObject.getJSONArray(Constants.SUB_CATEGORIES_DETAILS.KEY_CATEGORY);
                                Log.d(TAG, "onResponse: "+rootObject.getJSONArray(Constants.SUB_CATEGORIES_DETAILS.KEY_CATEGORY));
                                if (!insideArray.equals(null)){
                                    if (rootObject.getJSONArray(Constants.SUB_CATEGORIES_DETAILS.KEY_CATEGORY).length()>0){
                                        for (int j=0; j<insideArray.length(); j++){
                                            JSONObject insideObject = insideArray.getJSONObject(j);
                                            ProductCategoriesDetails rawFishDetails = new ProductCategoriesDetails();
                                            rawFishDetails.setCategory_name(insideObject.getString(Constants.SUB_CATEGORIES_DETAILS.KEY_CATEGORY_NAME));
                                            rawFishDetails.setCategory_id(insideObject.getString(Constants.SUB_CATEGORIES_DETAILS.KEY_CATEGORY_ID));
                                            rawFishDetails.setCategory_image(insideObject.getString(Constants.SUB_CATEGORIES_DETAILS.KEY_CATEGORY_IMAGE));
                                            subCategoryDataList.add(rawFishDetails);
                                        }
                                    }else {subCategoryView.hideProgress();}
                                }
                                Log.d(TAG, "onResponse: "+subCategoryDataList);
                                if (subCategoryDataList.size() > 0){subCategoryView.setProductListSuccess(subCategoryDataList);
                                } else{subCategoryView.setProductListError();}

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            subCategoryView.hideProgress();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                subCategoryView.hideProgress();
                subCategoryView.showServerError();
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

        RequestQueue requestQueue = Volley.newRequestQueue((Context) this.subCategoryView);
        int socketTimeout = 200000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    @Override
    public void fetchWishListDataFromServer() {
        subCategoryView.navigateToWishList();
    }

    @Override
    public void fetchCartDataFromServer() {
        subCategoryView.navigateToCart();
    }
}
