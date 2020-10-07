package com.purpuligo.ajeevikafarmfresh.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.purpuligo.ajeevikafarmfresh.Constants.Constants;
import com.purpuligo.ajeevikafarmfresh.Global.LoginDialog;
import com.purpuligo.ajeevikafarmfresh.Global.NetworkState;
import com.purpuligo.ajeevikafarmfresh.Model.Adapter.OrderHistoryAdapter;
import com.purpuligo.ajeevikafarmfresh.Model.OrderHistoryInteractorImpl;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.OrderHistoryDetails;
import com.purpuligo.ajeevikafarmfresh.Presenter.OrderHistoryPresenter;
import com.purpuligo.ajeevikafarmfresh.R;
import com.purpuligo.ajeevikafarmfresh.View.OrderHistoryView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderHistoryActivity extends AppCompatActivity implements OrderHistoryView{

    private String TAG = "OrderHistoryActivity";
    @BindView(R.id.orderHistoryRecyclerView) RecyclerView orderHistoryRecyclerView;
    @BindView(R.id.cartBtn) ImageView cartBtn;
    @BindView(R.id.wishListBtn) ImageView wishListBtn;
    @BindView(R.id.backBtn) ImageView backBtn;
    @BindView(R.id.count_cart) TextView count_cart;
    @BindView(R.id.count_wishList) TextView count_wishList;

    private String customer_email;
    private String session_data;
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    private NetworkState networkState;
    private LoginDialog loginDialog;
    private OrderHistoryPresenter orderHistoryPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        ButterKnife.bind(this);
        networkState = new NetworkState();
        loginDialog = new LoginDialog();
        orderHistoryPresenter = new OrderHistoryInteractorImpl(this);

        sharedPreferences = getSharedPreferences(Constants.USER_SESSION_MANAGEMENT.KEY_PREFER_NAME,MODE_PRIVATE);
        customer_email = sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"");
        session_data = sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_SESSION_DATA,"");
        Log.d(TAG, "onCreate: "+sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_NAME,""));

        if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
            if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CART_COUNT,"").equalsIgnoreCase("0")){
                count_cart.setVisibility(View.GONE);
            }else {count_cart.setText(sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CART_COUNT,""));}
            if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_WISHLIST_COUNT,"").equalsIgnoreCase("0")){
                count_wishList.setVisibility(View.GONE);
            }else {count_wishList.setText(sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_WISHLIST_COUNT,""));}
        }else {
            count_cart.setVisibility(View.GONE);
            count_wishList.setVisibility(View.GONE);
        }

        if (networkState.isNetworkAvailable(this)){
            if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                orderHistoryPresenter.fetchOrderHistoryFromServer(customer_email);
            }else {loginDialog.loginDialog(this);}
        }else {
            Toast.makeText(this, "Please Check Internet", Toast.LENGTH_SHORT).show();
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void cart(View view){
        startActivity(new Intent(OrderHistoryActivity.this,CartActivity.class)
                .putExtra("CartActivity","OrderHistoryActivity"));
        finish();
    }

    public void wishList(View view){
        startActivity(new Intent(OrderHistoryActivity.this,WishListActivity.class));
        finish();
    }

    @Override
    public void showProgressBar() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Processing.....");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void hideProgressBar() {
        progressDialog.dismiss();
    }

    @Override
    public void showOrderHistoryList(ArrayList<OrderHistoryDetails> orderHistoryList) {
        try {
            Log.d(TAG, "showOrderHistoryList: "+orderHistoryList);
            orderHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            orderHistoryRecyclerView.setAdapter(new OrderHistoryAdapter(this,orderHistoryList));
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showOrderHistoryError() {

    }

    @Override
    public void showServerError() {
        Snackbar.make(backBtn,"Oops, Something went wrong",Snackbar.LENGTH_LONG).show();
    }
}
