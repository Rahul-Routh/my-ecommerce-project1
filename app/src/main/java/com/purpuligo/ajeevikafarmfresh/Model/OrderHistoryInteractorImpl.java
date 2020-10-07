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
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.OrderHistoryDetails;
import com.purpuligo.ajeevikafarmfresh.Presenter.OrderHistoryPresenter;
import com.purpuligo.ajeevikafarmfresh.View.OrderHistoryView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderHistoryInteractorImpl implements OrderHistoryPresenter{

    private String TAG = "OrderHistoryInteractorImpl";
    private OrderHistoryView orderHistoryView;
    private ArrayList<OrderHistoryDetails> orderHistoryList;

    public OrderHistoryInteractorImpl(OrderHistoryView orderHistoryView) {
        this.orderHistoryView = orderHistoryView;

        orderHistoryList = new ArrayList<>();
    }

    @Override
    public void fetchOrderHistoryFromServer(String customer_email) {

        orderHistoryView.showProgressBar();
        //String JSON_URL = "http://166.62.52.188/Murighonto_new/index.php?route=restapi/checkout/checkout/appGetOrders";
        String JSON_URL = Url.ORDER_HISTORY_URL;
        JSONObject postData = new JSONObject();
        try {
            postData.put(Constants.ORDER_HISTORY.KEY_CUSTOMER_EMAIL,customer_email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "fetchOrderHistoryFromServer: "+postData);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, JSON_URL,postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "fetchOrderHistoryFromServer: "+response.toString());
                        if (response.length()>0){
                            try {
                                fetchOrderHistory(response);
                            } catch (Exception e){e.printStackTrace();}
                        }
                        orderHistoryView.hideProgressBar();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                orderHistoryView.hideProgressBar();
                orderHistoryView.showServerError();
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

        RequestQueue requestQueue = Volley.newRequestQueue((Context) this.orderHistoryView);
        requestQueue.add(stringRequest);
    }

    private void fetchOrderHistory(JSONObject response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response.toString());
        if (jsonObject.getString(Constants.ORDER_HISTORY.KEY_RESPONSE_STATUS).equalsIgnoreCase("success")){
            JSONArray rootArray = jsonObject.getJSONArray(Constants.ORDER_HISTORY.KEY_ORDER_HISTORY);
            for (int i=0; i<rootArray.length(); i++){
               JSONObject object = rootArray.getJSONObject(i);
               OrderHistoryDetails orderHistoryDetails = new OrderHistoryDetails();
               if (object.has(Constants.ORDER_HISTORY.KEY_ORDER_ID) && !object.isNull(Constants.ORDER_HISTORY.KEY_ORDER_ID) &&
                       object.getString(Constants.ORDER_HISTORY.KEY_ORDER_ID).length()>0){
                    orderHistoryDetails.setOrder_id(object.getString(Constants.ORDER_HISTORY.KEY_ORDER_ID));
               }else {}
               if (object.has(Constants.ORDER_HISTORY.KEY_CART_ID) && !object.isNull(Constants.ORDER_HISTORY.KEY_CART_ID) &&
                       object.getString(Constants.ORDER_HISTORY.KEY_CART_ID).length()>0){
                    orderHistoryDetails.setCart_id(object.getString(Constants.ORDER_HISTORY.KEY_CART_ID));
               }else {}
               if (object.has(Constants.ORDER_HISTORY.KEY_ORDER_NUMBER) && !object.isNull(Constants.ORDER_HISTORY.KEY_CART_ID) &&
                       object.getString(Constants.ORDER_HISTORY.KEY_CART_ID).length()>0){
                    orderHistoryDetails.setStatus_color(object.getString(Constants.ORDER_HISTORY.KEY_CART_ID));
               }else {}
               if (object.has(Constants.ORDER_HISTORY.KEY_STATUS_COLOR) && !object.isNull(Constants.ORDER_HISTORY.KEY_STATUS_COLOR) &&
                       object.getString(Constants.ORDER_HISTORY.KEY_STATUS_COLOR).length()>0){
                    orderHistoryDetails.setStatus_color(object.getString(Constants.ORDER_HISTORY.KEY_STATUS_COLOR));
               }else {}
               if (object.has(Constants.ORDER_HISTORY.KEY_REORDER_ALLOWED) && !object.isNull(Constants.ORDER_HISTORY.KEY_REORDER_ALLOWED)){
                    orderHistoryDetails.setReorder_allowed(object.getInt(Constants.ORDER_HISTORY.KEY_REORDER_ALLOWED));
               }else {}
               if (object.has(Constants.ORDER_HISTORY.KEY_FIRST_NAME) && !object.isNull(Constants.ORDER_HISTORY.KEY_FIRST_NAME) &&
                       object.getString(Constants.ORDER_HISTORY.KEY_FIRST_NAME).length()>0){
                    orderHistoryDetails.setFirstname(object.getString(Constants.ORDER_HISTORY.KEY_FIRST_NAME));
               }else {}
               if (object.has(Constants.ORDER_HISTORY.KEY_LAST_NAME) && !object.isNull(Constants.ORDER_HISTORY.KEY_LAST_NAME) &&
                       object.getString(Constants.ORDER_HISTORY.KEY_LAST_NAME).length()>0){
                    orderHistoryDetails.setFirstname(object.getString(Constants.ORDER_HISTORY.KEY_LAST_NAME));
               }else {}
                if (object.has(Constants.ORDER_HISTORY.KEY_EMAIL) && !object.isNull(Constants.ORDER_HISTORY.KEY_EMAIL) &&
                        object.getString(Constants.ORDER_HISTORY.KEY_EMAIL).length()>0){
                   orderHistoryDetails.setEmail(object.getString(Constants.ORDER_HISTORY.KEY_EMAIL));
                }else {}
                if (object.has(Constants.ORDER_HISTORY.KEY_TELEPHONE) && !object.isNull(Constants.ORDER_HISTORY.KEY_TELEPHONE) &&
                        object.getString(Constants.ORDER_HISTORY.KEY_TELEPHONE).length()>0){
                    orderHistoryDetails.setTelephone(object.getString(Constants.ORDER_HISTORY.KEY_TELEPHONE));
                }else {}
                if (object.has(Constants.ORDER_HISTORY.KEY_STATUS) && !object.isNull(Constants.ORDER_HISTORY.KEY_STATUS) &&
                        object.getString(Constants.ORDER_HISTORY.KEY_STATUS).length()>0){
                    orderHistoryDetails.setStatus(object.getString(Constants.ORDER_HISTORY.KEY_STATUS));
                }else {}
                if (object.has(Constants.ORDER_HISTORY.KEY_DATE_ADDED) && !object.isNull(Constants.ORDER_HISTORY.KEY_DATE_ADDED) &&
                        object.getString(Constants.ORDER_HISTORY.KEY_DATE_ADDED).length()>0){
                    orderHistoryDetails.setDate_added(object.getString(Constants.ORDER_HISTORY.KEY_DATE_ADDED));
                }else {}
                if (object.has(Constants.ORDER_HISTORY.KEY_TOTAL_SUM_PRICE) && !object.isNull(Constants.ORDER_HISTORY.KEY_TOTAL_SUM_PRICE) &&
                        object.getString(Constants.ORDER_HISTORY.KEY_TOTAL_SUM_PRICE).length()>0){
                    orderHistoryDetails.setTotal(object.getString(Constants.ORDER_HISTORY.KEY_TOTAL_SUM_PRICE));
                }else {}
                orderHistoryList.add(orderHistoryDetails);
            }
            orderHistoryView.showOrderHistoryList(orderHistoryList);
            orderHistoryView.hideProgressBar();
        }else {
            orderHistoryView.hideProgressBar();
            orderHistoryView.showOrderHistoryError();
        }
    }
}
