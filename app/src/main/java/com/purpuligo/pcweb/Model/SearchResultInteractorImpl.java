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
import com.purpuligo.pcweb.Model.Pojo.ProductListDetails;
import com.purpuligo.pcweb.Presenter.SearchResultPresenter;
import com.purpuligo.pcweb.View.SearchResultView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchResultInteractorImpl implements SearchResultPresenter {

    private static final String TAG = "SearchResultInterImpl";
    private SearchResultView searchResultView;
    private ArrayList<ProductListDetails> productListArray;

    public SearchResultInteractorImpl(SearchResultView searchResultView) {
        this.searchResultView = searchResultView;

        productListArray = new ArrayList<>();
    }

    @Override
    public void fetchProductListDataFromServer(String search_query) {

        searchResultView.showProgress();
        //String JSON_URL = "https://purpuligo.com/guchhi/index.php?route=restapi/product/search&search="+search_query;
        String JSON_URL = Url.SEARCH_LIST_URL+search_query;
        Log.d(TAG, "fetchProductListDataFromServer: "+JSON_URL);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response.toString());
                        if (response.length() > 0){
                            try {
                                JSONObject rootObject = new JSONObject(response.toString());
                                JSONArray rootArray = rootObject.getJSONArray(Constants.PRODUCT_LIST_DETAILS.KEY_PRODUCT);
                                if (rootObject.getJSONArray(Constants.PRODUCT_LIST_DETAILS.KEY_PRODUCT).length()>0){
                                    for (int i=0; i<rootArray.length(); i++){
                                        JSONObject innerObject = rootArray.getJSONObject(i);
                                        ProductListDetails productListDetails = new ProductListDetails();
                                        productListDetails.setProduct_id(innerObject.getString("product_id"));
                                        productListDetails.setProduct_name(innerObject.getString("name"));
                                        productListDetails.setProduct_price(innerObject.getString("price"));
                                        productListDetails.setProduct_image(innerObject.getString("thumb"));
                                        productListArray.add(productListDetails);
                                        Log.d(TAG, "onResponse: "+innerObject.getString("product_id"));
                                        Log.d(TAG, "onResponse: "+innerObject.getString("name"));
                                        Log.d(TAG, "onResponse: "+innerObject.getString("price"));
                                        Log.d(TAG, "onResponse: "+innerObject.getString("thumb"));
                                    }
                                }else {searchResultView.hideProgress();}
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (productListArray.size() > 0){
                                searchResultView.setProductListSuccess(productListArray);
                                searchResultView.hideProgress();
                            }else {
                                searchResultView.hideProgress();
                                searchResultView.setProductListError();
                            }
                        }else {
                            searchResultView.hideProgress();
                            searchResultView.setProductListError();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                searchResultView.hideProgress();
                searchResultView.showServerError();
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

        RequestQueue requestQueue = Volley.newRequestQueue((Context) this.searchResultView);
        requestQueue.add(stringRequest);
    }

    @Override
    public void fetchWishListDataFromServer() {
        searchResultView.navigateToWishList();
    }

    @Override
    public void fetchCartDataFromServer() {
        searchResultView.navigateToCart();
    }
}
