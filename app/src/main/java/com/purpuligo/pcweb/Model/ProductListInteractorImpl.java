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
import com.purpuligo.pcweb.Presenter.ProductListPresenter;
import com.purpuligo.pcweb.View.ProductListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductListInteractorImpl implements ProductListPresenter {

    private static final String TAG = "ProductListInteractor";
    private ProductListView productListView;
    private ArrayList<ProductListDetails> productListArray;

    public ProductListInteractorImpl(ProductListView productListView) {
        this.productListView = productListView;

        productListArray = new ArrayList<>();
    }

    @Override
    public void fetchProductListDataFromServer(String category_id) {

        productListView.showProgress();
        //String JSON_URL = "http://166.62.52.188/Murighonto_new/index.php?route=restapi/product/category/product&cat_id="+category_id;
        String JSON_URL = Url.PRODUCT_LIST_URL+category_id;
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
                                        productListDetails.setProduct_id(innerObject.getString(Constants.PRODUCT_LIST_DETAILS.KEY_PRODUCT_ID));
                                        productListDetails.setProduct_name(innerObject.getString(Constants.PRODUCT_LIST_DETAILS.KEY_PRODUCT_NAME));
                                        productListDetails.setProduct_price(innerObject.getString(Constants.PRODUCT_LIST_DETAILS.KEY_PRODUCT_PRICE));
                                        productListDetails.setProduct_image(innerObject.getString(Constants.PRODUCT_LIST_DETAILS.KEY_PRODUCT_IMAGE));
                                        productListArray.add(productListDetails);
                                    }
                                }else {productListView.hideProgress();}
                            } catch (JSONException e) {
                                productListView.hideProgress();
                                e.printStackTrace();
                            }

                            if (productListArray.size() > 0){
                                productListView.setProductListSuccess(productListArray);
                                productListView.hideProgress();
                            }else {
                                productListView.hideProgress();
                                productListView.setProductListError();
                            }
                        }else {
                            productListView.hideProgress();
                            productListView.setProductListError();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                productListView.hideProgress();
                productListView.showServerError();
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

        RequestQueue requestQueue = Volley.newRequestQueue((Context) this.productListView);
        requestQueue.add(stringRequest);
    }

    @Override
    public void fetchWishListDataFromServer() {
        productListView.navigateToWishList();
    }

    @Override
    public void fetchCartDataFromServer() {
        productListView.navigateToCart();
    }
}
