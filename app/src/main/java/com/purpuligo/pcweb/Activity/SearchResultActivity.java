package com.purpuligo.pcweb.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.purpuligo.pcweb.Constants.Constants;
import com.purpuligo.pcweb.Global.LoginDialog;
import com.purpuligo.pcweb.Global.NetworkState;
import com.purpuligo.pcweb.Model.Adapter.ProductListAdapter;
import com.purpuligo.pcweb.Model.Pojo.ProductListDetails;
import com.purpuligo.pcweb.Model.SearchResultInteractorImpl;
import com.purpuligo.pcweb.Presenter.SearchResultPresenter;
import com.purpuligo.pcweb.R;
import com.purpuligo.pcweb.View.SearchResultView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchResultActivity extends AppCompatActivity implements SearchResultView {

    private String TAG = "SearchResultActivity";
    @BindView(R.id.productListRecyclerView) RecyclerView productListRecyclerView;
    @BindView(R.id.backBtn) ImageView backBtn;
    @BindView(R.id.emptyListIndicator) ImageView emptyListIndicator;
    @BindView(R.id.emptyListIndicator1) TextView emptyListIndicator1;
    @BindView(R.id.count_cart) TextView count_cart;
    @BindView(R.id.count_wishList) TextView count_wishList;
    @BindView(R.id.product_list_searchView) SearchView product_list_searchView;

    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    private SearchResultPresenter searchResultPresenter;
    private NetworkState networkState;
    private LoginDialog loginDialog;
    private ProductListAdapter productListAdapter;
    private String searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);
        networkState = new NetworkState();
        loginDialog = new LoginDialog();
        searchResultPresenter = new SearchResultInteractorImpl(this);

        sharedPreferences = getSharedPreferences(Constants.USER_SESSION_MANAGEMENT.KEY_PREFER_NAME,MODE_PRIVATE);
        final String customer_email = sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"");
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

        Intent intent = getIntent();
        searchQuery = intent.getStringExtra("searchQuery");
        Log.d(TAG, "onCreate: "+searchQuery);

        if (networkState.isNetworkAvailable(this)){
            searchResultPresenter.fetchProductListDataFromServer(searchQuery);
        }else {
            Toast.makeText(this, "Please connect to internet", Toast.LENGTH_SHORT).show();
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        searchName();
    }

    private void searchName(){
        try {
            product_list_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    try { productListAdapter.filter(newText); }catch (NullPointerException e){e.printStackTrace();}
                    return true;
                }
            });
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SearchResultActivity.this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        SearchResultActivity.this.finish();
        //overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void cart(View view){
        if (networkState.isNetworkAvailable(SearchResultActivity.this)){
            if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                searchResultPresenter.fetchCartDataFromServer();
            }else {loginDialog.loginDialog(SearchResultActivity.this);}
        }else {Toast.makeText(SearchResultActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();}
    }

    public void wishList(View view){
        if (networkState.isNetworkAvailable(SearchResultActivity.this)){
            if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                searchResultPresenter.fetchWishListDataFromServer();
            }else {loginDialog.loginDialog(SearchResultActivity.this);}
        }else {Toast.makeText(SearchResultActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();}
    }

    @Override
    public void showProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Processing.....");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void setProductListSuccess(ArrayList<ProductListDetails> productListArray) {
        try {
            Log.d(TAG, "setProductListSuccess: "+productListArray.toString());
            if (productListArray.isEmpty()){
                productListRecyclerView.setVisibility(View.INVISIBLE);
                emptyListIndicator.setVisibility(View.VISIBLE);
                emptyListIndicator1.setVisibility(View.VISIBLE);
            }else {
                productListRecyclerView.setVisibility(View.VISIBLE);
                productListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                productListAdapter = new ProductListAdapter(this,productListArray,"0","0","SearchResultActivity",searchQuery);
                productListRecyclerView.setAdapter(productListAdapter);
                productListRecyclerView.setNestedScrollingEnabled(false);
            }
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void setProductListError() {
        productListRecyclerView.setVisibility(View.INVISIBLE);
        emptyListIndicator.setVisibility(View.VISIBLE);
        emptyListIndicator1.setVisibility(View.VISIBLE);
    }

    @Override
    public void navigateToWishList() {
        startActivity(new Intent(SearchResultActivity.this,WishListActivity.class));
        finish();
    }

    @Override
    public void navigateToCart() {
        startActivity(new Intent(SearchResultActivity.this,CartActivity.class));
        finish();
    }

    @Override
    public void showServerError() {
        Snackbar.make(backBtn,"Oops, Something went wrong",Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }
}
