package com.purpuligo.ajeevikafarmfresh.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.purpuligo.ajeevikafarmfresh.Constants.Constants;
import com.purpuligo.ajeevikafarmfresh.Global.LoginDialog;
import com.purpuligo.ajeevikafarmfresh.Global.NetworkState;
import com.purpuligo.ajeevikafarmfresh.Global.UserSessionManager;
import com.purpuligo.ajeevikafarmfresh.Model.Adapter.HomeImageSliderAdapter;
import com.purpuligo.ajeevikafarmfresh.Model.Adapter.ParentCategoryAdapter;
import com.purpuligo.ajeevikafarmfresh.Model.Adapter.RestaurantAdapter;
import com.purpuligo.ajeevikafarmfresh.Model.HomeInteractorImpl;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.ImageSliderPojo;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.ParentDetails;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.ProductCategoriesDetails;
import com.purpuligo.ajeevikafarmfresh.Presenter.HomePresenter;
import com.purpuligo.ajeevikafarmfresh.R;
import com.purpuligo.ajeevikafarmfresh.View.HomeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.purpuligo.ajeevikafarmfresh.R.string.drawer_open;

public class HomeActivity extends AppCompatActivity implements HomeView {

    private String TAG = "HomeActivity";
    @BindView(R.id.homeRawFishRecyclerView) RecyclerView homeRawFishRecyclerView;
//  @BindView(R.id.homeRestaurantRecyclerView) RecyclerView homeRestaurantRecyclerView;
//  @BindView(R.id.rawFishParentName) TextView rawFishParentName;
//  @BindView(R.id.restaurantParentName) TextView restaurantParentName;
    @BindView(R.id.homeToolBar) Toolbar homeToolBar;
    @BindView(R.id.drawerLayout) DrawerLayout drawerLayout;
    @BindView(R.id.navigationView) NavigationView navigationView;
    //@BindView(R.id.cartBtn) ImageButton cartBtn;
    //@BindView(R.id.wishListBtn) ImageButton wishListBtn;
    @BindView(R.id.connectionCheckImage) ImageView connectionCheckImage;
    @BindView(R.id.homeViewPager) ViewPager viewPager;
    @BindView(R.id.sliderDots) TabLayout sliderDots;
    @BindView(R.id.count_cart) TextView count_cart;
    @BindView(R.id.count_wishList) TextView count_wishList;
    @BindView(R.id.home_searchView) SearchView home_searchView;
    @BindView(R.id.versionBtn) Button versionBtn;
    @BindView(R.id.versionInfoLayout) LinearLayout versionInfoLayout;

    private ProgressDialog progressDialog;
    private TextView headingName;
    private ImageView right;

    private String customer_name;
    private String customer_email;
    private int counter = 0;
    private int images = 0;

    private HomePresenter homePresenter;
    private NetworkState networkState;
    private LoginDialog loginDialog;
    private UserSessionManager userSessionManager;
    private SharedPreferences sharedPreferences;
    private MenuItem loginSignUp;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);
        networkState = new NetworkState();
        loginDialog = new LoginDialog();
        userSessionManager = new UserSessionManager(this);
        homePresenter = new HomeInteractorImpl(this);

        setSupportActionBar(homeToolBar);


        sharedPreferences = getSharedPreferences(Constants.USER_SESSION_MANAGEMENT.KEY_PREFER_NAME,MODE_PRIVATE);
        customer_name = sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_NAME,"");
        customer_email = sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"");
        Log.d(TAG, "onCreate: "+sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_SESSION_DATA,""));

        setBadge();

        if (networkState.isNetworkAvailable(this)){
            homePresenter.fetchCurrentVersion();
        }else { Toast.makeText(this, "Check Connection", Toast.LENGTH_SHORT).show(); }

        if (networkState.isNetworkAvailable(this)){
            homePresenter.fetchImageSlider();
        }else { Toast.makeText(this, "Check Connection", Toast.LENGTH_SHORT).show(); }

        if (networkState.isNetworkAvailable(this)){
            if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                homePresenter.fetchWishListCount(sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,""));
                homePresenter.fetchCartCount(sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,""),
                        sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_SESSION_DATA,""));
            }
        }else { Toast.makeText(this, "Check Connection", Toast.LENGTH_SHORT).show(); }

        if (savedInstanceState != null){
            if (savedInstanceState.getBoolean("onCreate")){
                Log.d(TAG, "onCreate: onBackPresed Called");
            }else {
                Log.d(TAG, "onCreate: onBackPresed not Called");
            }
        }

        versionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkState.isNetworkAvailable(HomeActivity.this)){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+ HomeActivity.this.getPackageName() +"&hl=en")));
                }else { Toast.makeText(HomeActivity.this, "Check Connection", Toast.LENGTH_SHORT).show(); }
            }
        });


        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,homeToolBar, drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        //menu
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        //
        navigationItem(navigationView);

        if (networkState.isNetworkAvailable(this)){
            homePresenter.fetchDataFromServer();
        }else { Toast.makeText(this, "Check Connection", Toast.LENGTH_SHORT).show(); }

        Intent intent = getIntent();
        final View headerView = navigationView.getHeaderView(0);
        //LinearLayout subHeadingLayout = headerView.findViewById(R.id.subHeadingLayout);
        headingName = headerView.findViewById(R.id.headingName);
        right = headerView.findViewById(R.id.right);
        if (customer_name.length()>0){
            headingName.setText(customer_name);
        }else {
            headingName.setText(intent.getStringExtra("customer_name"));
        }

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
            }
        });

//        subHeadingLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (headingName.getText().toString().trim().equalsIgnoreCase("Login/SignUp")){
//                    startActivity(new Intent(HomeActivity.this,LoginActivity.class));
//                    finish();
//                }SplashScreenActivity
//            }
//        });

        Menu menu = navigationView.getMenu();
        loginSignUp = menu.findItem(R.id.navLogout);
        if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
            loginSignUp.setTitle("Logout");
            loginSignUp.setIcon(R.drawable.logout);
        }else {
            loginSignUp.setIcon(R.drawable.login);
            loginSignUp.setTitle("Login/SignUp"); }

            searchProduct();

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionString = pInfo.versionName;
            TextView version = findViewById(R.id.version);
            version.setText("Version : "+versionString);
        }catch (Exception e){e.printStackTrace();}
    }

    private void searchProduct(){
        try {
            home_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    try { Log.d(TAG, "onQueryTextChange: "+query);
                        startActivity(new Intent(HomeActivity.this,SearchResultActivity.class)
                        .putExtra("searchQuery",query));
                        //overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }catch (NullPointerException e){e.printStackTrace();}
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void onBackPressed() {
        counter++;
        if (counter == 1){
            Snackbar.make(homeRawFishRecyclerView,"Do you really want to close",Snackbar.LENGTH_LONG).show();
        }else if (counter == 2){
            finish();
        }
    }

    @Override
    public void showProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Processing.....");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void cart(View view){
        if (networkState.isNetworkAvailable(HomeActivity.this)){
            if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                homePresenter.fetchCartDataFromServer();
            }else {loginDialog.loginDialog(HomeActivity.this);}
        }else {Toast.makeText(HomeActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();}
    }

    public void wishList(View view){
        if (networkState.isNetworkAvailable(HomeActivity.this)){
            if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                startActivity(new Intent(HomeActivity.this,WishListActivity.class));
            }else {loginDialog.loginDialog(HomeActivity.this);}
        }else {Toast.makeText(HomeActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();}
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void showCurrentVersion(String currentVersion) {
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            int verCode = pInfo.versionCode;
            Log.d(TAG, "showCurrentVersion: "+version+"/"+verCode);
            if (currentVersion != null){
                if (Integer.parseInt(currentVersion) > verCode){
                    versionInfoLayout.setVisibility(View.VISIBLE);
                }else { versionInfoLayout.setVisibility(View.GONE); }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //Log.d(TAG, "showCurrentVersion: "+currentVersion);
    }

    @Override
    public void showCurrentVersionError() {
        Log.d(TAG, "showCurrentVersionError: ");
    }

    @Override
    public void setRawFishListError() {
        Log.d(TAG, "setRawFishListError: ");
    }

    @Override
    public void setRawFishListSuccess(ArrayList<ParentDetails> rawFishData) {
        try{
            Log.d(TAG, "setRawFishListSuccess: "+rawFishData.toString());
            //rawFishParentName.setText(rawFishData.get(0).getParent_name());
            homeRawFishRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            homeRawFishRecyclerView.setAdapter(new ParentCategoryAdapter(this,rawFishData));
            homeRawFishRecyclerView.setNestedScrollingEnabled(false);
            if ((getIntent().getStringExtra("show_delivery_dialog") != null) && getIntent().getStringExtra("show_delivery_dialog").equalsIgnoreCase("1")){
                showDeliverablePinCodeAlert();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    @Override
//    public void setRestaurantListError() {
//        Log.d(TAG, "setRestaurantListError: ");
//    }
//
//    @Override
//    public void setRestaurantListSuccess(ArrayList<ProductCategoriesDetails> restaurantData) {
//        try {
//            Log.d(TAG, "setRestaurantListSuccess: "+restaurantData.toString());
//            restaurantParentName.setText(restaurantData.get(0).getParent_name());
//            homeRestaurantRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
//            homeRestaurantRecyclerView.setAdapter(new RestaurantAdapter(this,restaurantData));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

    @Override
    public void setWishListCount(String wishListCount) {
        try {
            Log.d(TAG, "setWishListCount: "+wishListCount);
            userSessionManager.updateWishListItem(wishListCount);
            setBadge();
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void setWishListCountError() {
        try {
            userSessionManager.updateWishListItem("0");
            setBadge();
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void setCartCount(String cartCount) {
        try {
            Log.d(TAG, "setCartCount: "+cartCount);
            userSessionManager.updateCartCount(cartCount);
            setBadge();
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void setCartCountError() {
        try {
            userSessionManager.updateCartCount("0");
            setBadge();
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void navigateToWishList() {
        startActivity(new Intent(HomeActivity.this,WishListActivity.class));
        finish();
    }

    @Override
    public void navigateToCart() {
        startActivity(new Intent(HomeActivity.this,CartActivity.class).putExtra("CartActivity","HomeActivity"));
        finish();
    }

    @Override
    public void showAlert(String message) {
        Log.d(TAG, "showAlert: ");
    }

    @Override
    public void showServerError() {
        Snackbar.make(homeRawFishRecyclerView,"Oops, Something went wrong",Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showImageSlider(List<ImageSliderPojo> imageSliderList) {
        try {
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



    private void navigationItem(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();
                if (itemId == R.id.navHome){
                    drawerLayout.closeDrawers();
                    //startActivity(new Intent(HomeActivity.this,HomeActivity.class));
                }else if (itemId == R.id.navProfile){
                    if (networkState.isNetworkAvailable(HomeActivity.this)){
                        if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                            startActivity(new Intent(HomeActivity.this,ProfileActivity.class));
                        }else {loginDialog.loginDialog(HomeActivity.this);}
                    }else {Toast.makeText(HomeActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();}
                }else if (itemId == R.id.navOrderHistory){
                    if (networkState.isNetworkAvailable(HomeActivity.this)){
                        if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                            startActivity(new Intent(HomeActivity.this,OrderHistoryActivity.class));
                        }else {loginDialog.loginDialog(HomeActivity.this);}
                    }else {Toast.makeText(HomeActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();}
                }
//                else if (itemId == R.id.navGalley){
//                    if (networkState.isNetworkAvailable(HomeActivity.this)){
//                        if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
//                            startActivity(new Intent(HomeActivity.this,GalleryActivity.class));
//                        }else {loginDialog.loginDialog(HomeActivity.this);}
//                    }else {Toast.makeText(HomeActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();}
//                }
                else if(itemId == R.id.navAddressList){
                    if (networkState.isNetworkAvailable(HomeActivity.this)){
                        if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                            startActivity(new Intent(HomeActivity.this,AddressListActivity.class));
                        }else {loginDialog.loginDialog(HomeActivity.this);}
                    }else {Toast.makeText(HomeActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();}
                }
                else if (itemId == R.id.navContactUs){
                    startActivity(new Intent(HomeActivity.this,ContactUsActivity.class));
                }else if (itemId == R.id.navAboutUs){
                    startActivity(new Intent(HomeActivity.this,AboutUsActivity.class));
                }
//                else if (itemId == R.id.navTerms){
//                    startActivity(new Intent(HomeActivity.this,TermAndConditionActivity.class));
//                }
                else if(itemId == R.id.privacyPolicy){
                    startActivity(new Intent(HomeActivity.this,PrivacyPolicyActivity.class));
                }
                else if (itemId == R.id.navLogout){
                    if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                        logout();
                    }else {
                        startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                        finish();
                    }
                }
                return true;
            }
        });
    }

    private class SliderTimer extends TimerTask {
        @Override
        public void run() {
            HomeActivity.this.runOnUiThread(new Runnable() {
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

    private void logout(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Do you really want to logout?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                userSessionManager.logoutUser();
                count_cart.setVisibility(View.GONE);
                count_wishList.setVisibility(View.GONE);
                drawerLayout.closeDrawers();
                loginSignUp.setIcon(R.drawable.login);
                loginSignUp.setTitle("Login/SignUp");
                headingName.setText("Guest");
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void setBadge(){
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("onCreate",true);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
//        if (networkState.isNetworkAvailable(this)){
//            new HomeInteractorImpl(this).fetchCurrentVersion();
//        }else { Toast.makeText(this, "Check Connection", Toast.LENGTH_SHORT).show(); }
    }

    private void showDeliverablePinCodeAlert(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.delivery_alert_dialog_layout);
        TextView message_tv = dialog.findViewById(R.id.message_tv);
        message_tv.setText("We are delivering only in Ranchi containing this Pincode area - 834001, 834002, 834003, 834004, 834005, 834006, 834007, 834008, 834009");
        dialog.findViewById(R.id.removeCartItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //try { dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT); }catch (Exception e){e.printStackTrace();}
        dialog.show();
    }
}
