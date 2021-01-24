package com.purpuligo.pcweb.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.purpuligo.pcweb.Constants.Constants;
import com.purpuligo.pcweb.Global.LoginDialog;
import com.purpuligo.pcweb.Global.NetworkState;
import com.purpuligo.pcweb.Model.Adapter.CategoryListAdapter;
import com.purpuligo.pcweb.Model.Adapter.HomeImageSliderAdapter;
import com.purpuligo.pcweb.Model.CategoryListInteractorImpl;
import com.purpuligo.pcweb.Model.Pojo.ImageSliderPojo;
import com.purpuligo.pcweb.Model.Pojo.ProductCategoriesDetails;
import com.purpuligo.pcweb.Presenter.CategoryListPresenter;
import com.purpuligo.pcweb.R;
import com.purpuligo.pcweb.View.CategoryListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryListActivity extends AppCompatActivity implements CategoryListView {

    private String TAG = "CategoryListActivity";
    @BindView(R.id.categoryListRecyclerView) RecyclerView categoryListRecyclerView;
    @BindView(R.id.backBtn) ImageView backBtn;
    @BindView(R.id.emptyListIndicator) ImageView emptyListIndicator;
    @BindView(R.id.emptyListIndicator1) TextView emptyListIndicator1;
    @BindView(R.id.count_cart) TextView count_cart;
    @BindView(R.id.count_wishList) TextView count_wishList;
    @BindView(R.id.category_searchView) androidx.appcompat.widget.SearchView category_searchView;
    @BindView(R.id.homeViewPager) ViewPager viewPager;
    @BindView(R.id.sliderDots) TabLayout sliderDots;

    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    private CategoryListPresenter categoryListPresenter;
    private NetworkState networkState;
    private LoginDialog loginDialog;
    private CategoryListAdapter categoryListAdapter;
    private String parent_position;

    private int counter = 0;
    private int images = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        ButterKnife.bind(this);
        networkState = new NetworkState();
        loginDialog = new LoginDialog();
        categoryListPresenter = new CategoryListInteractorImpl(this);
        sharedPreferences = getSharedPreferences(Constants.USER_SESSION_MANAGEMENT.KEY_PREFER_NAME,MODE_PRIVATE);

        updateBadgeCount();

        Intent intent = getIntent();
        parent_position = intent.getStringExtra("parent_position");
        Log.d(TAG, "onCreate: "+parent_position);

        if (networkState.isNetworkAvailable(this)){
            categoryListPresenter.fetchImageSlider();
        }else { Toast.makeText(this, "Check Connection", Toast.LENGTH_SHORT).show(); }

        if (networkState.isNetworkAvailable(this)){
            categoryListPresenter.fetchCategoryListFromServer(Integer.parseInt(parent_position));
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

    private void updateBadgeCount(){
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
    }

    private void searchName(){
        try {
            category_searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    try { categoryListAdapter.filter(newText); }catch (Exception e){e.printStackTrace();}
                    return true;
                }
            });
        }catch (Exception e){e.printStackTrace();}
    }

    public void cart(View view){
        if (networkState.isNetworkAvailable(CategoryListActivity.this)){
            if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                categoryListPresenter.fetchCartDataFromServer();
            }else {loginDialog.loginDialog(CategoryListActivity.this);}
        }else {Toast.makeText(CategoryListActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();}
    }

    public void wishList(View view){
        if (networkState.isNetworkAvailable(CategoryListActivity.this)){
            if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                categoryListPresenter.fetchWishListDataFromServer();
            }else {loginDialog.loginDialog(CategoryListActivity.this);}
        }else {Toast.makeText(CategoryListActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();}
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CategoryListActivity.this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("customer_name","Guest");
        startActivity(intent);
        CategoryListActivity.this.finish();
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
    public void setProductListSuccess(ArrayList<ProductCategoriesDetails> categoryListArray) {
        try {
            Log.d(TAG, "setProductListSuccess: "+categoryListArray);
            categoryListRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
            categoryListAdapter = new CategoryListAdapter(this,categoryListArray,parent_position,"CategoryListActivity");
            categoryListRecyclerView.setAdapter(categoryListAdapter);
            categoryListRecyclerView.setNestedScrollingEnabled(false);
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void setProductListError() {
        categoryListRecyclerView.setVisibility(View.INVISIBLE);
        emptyListIndicator.setVisibility(View.VISIBLE);
        emptyListIndicator1.setVisibility(View.VISIBLE);
    }

    @Override
    public void navigateToWishList() {
        startActivity(new Intent(CategoryListActivity.this,WishListActivity.class));
        finish();
    }

    @Override
    public void navigateToCart() {
        startActivity(new Intent(CategoryListActivity.this,CartActivity.class)
                .putExtra("CartActivity","ProductListActivity")
                .putExtra("category_id",parent_position));
        finish();
    }

    @Override
    public void showServerError() {
        Snackbar.make(backBtn,"Oops, Something went wrong",Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showImageSlider(List<ImageSliderPojo> imageSliderList) {
        try {
            //for image slider
            HomeImageSliderAdapter homeImageSliderAdapter = new HomeImageSliderAdapter(this,imageSliderList);
            viewPager.setAdapter(homeImageSliderAdapter);
            sliderDots.setupWithViewPager(viewPager, true);
            images = homeImageSliderAdapter.getCount();
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showImageSliderError() {
        Log.d(TAG, "showImageSliderError: ");
    }

    private class SliderTimer extends TimerTask {
        @Override
        public void run() {
            CategoryListActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < images - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }
}
