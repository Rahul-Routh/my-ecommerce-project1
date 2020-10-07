package com.purpuligo.ajeevikafarmfresh.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.purpuligo.ajeevikafarmfresh.Constants.Constants;
import com.purpuligo.ajeevikafarmfresh.Global.LoginDialog;
import com.purpuligo.ajeevikafarmfresh.Global.NetworkState;
import com.purpuligo.ajeevikafarmfresh.Global.Url;
import com.purpuligo.ajeevikafarmfresh.Global.UserSessionManager;
import com.purpuligo.ajeevikafarmfresh.Model.Adapter.DeliveryOptionAdapter;
import com.purpuligo.ajeevikafarmfresh.Model.Adapter.ProductDetailsAdapter;
import com.purpuligo.ajeevikafarmfresh.Model.Adapter.ProductListAdapter;
import com.purpuligo.ajeevikafarmfresh.Model.Adapter.SizeListAdapter;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.ProductListDetails;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.ProductSizeDetails;
import com.purpuligo.ajeevikafarmfresh.Model.ProductDescriptionInteractorImpl;
import com.purpuligo.ajeevikafarmfresh.Presenter.ProductDescriptionPresenter;
import com.purpuligo.ajeevikafarmfresh.R;
import com.purpuligo.ajeevikafarmfresh.View.ProductDescriptionView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductDescriptionActivity extends AppCompatActivity implements ProductDescriptionView {

    private String TAG = "ProductDescription";
    @BindView(R.id.backBtn) ImageView backBtn;
    @BindView(R.id.imageView) ImageView imageView;
    @BindView(R.id.sizeOptionLayout) TableLayout sizeOptionLayout;
    @BindView(R.id.deliveryOptionLayout) TableLayout deliveryOptionLayout;
    @BindView(R.id.sizeListSpinner) Spinner sizeListSpinner;
    //@BindView(R.id.imageViewPager) ViewPager imageViewPager;
    @BindView(R.id.sliderDots) LinearLayout sliderDotPanel;
    @BindView(R.id.productDescriptionWishList) Button productDescriptionWishList;
    @BindView(R.id.productDescriptionCart) Button productDescriptionCart;
    @BindView(R.id.productName) TextView productName;
    @BindView(R.id.productDescription) TextView productDescription;
    //@BindView(R.id.deliveryTime) TextView deliveryTime;
    @BindView(R.id.productPrice) TextView productPrice;
    @BindView(R.id.productStock) TextView productStock;
    @BindView(R.id.deliveryOptionList) RecyclerView deliveryOptionList;
    @BindView(R.id.cartDecrement) Button cartDecrement;
    @BindView(R.id.cartIncrement) Button cartIncrement;
    @BindView(R.id.countView) TextView countView;
    //@BindView(R.id.itemQuantityInKgLayout) TableLayout itemQuantityInKgLayout;
    //@BindView(R.id.itemQuantityInKgSpinner) Spinner itemQuantityInKgSpinner;
    @BindView(R.id.itemQuantityInNumberLayout) TableLayout itemQuantityInNumberLayout;
    @BindView(R.id.count_cart) TextView count_cart;
    @BindView(R.id.count_wishList) TextView count_wishList;
    @BindView(R.id.searchView) SearchView searchView;

//    @BindView(R.id.see_all) TextView see_all;
//    @BindView(R.id.productListRecyclerView) RecyclerView productListRecyclerView;



    //private ArrayList<String> itemQuantityInKgList;
    //private ArrayList<String> itemQuantityInKgListTwo;
    //private ArrayList<String> itemQuantityInKgListThree;
    private ArrayList<String> deliveryOptionValueList;
    //private int dotCount;
    private String customer_email;
    private String session_data;
    //private ImageView[] dots;
    private int count = 0;
    private String product_id,points,product_stock;
    private String category_id,prev_activity,searchQuery;
    private String parent_position,sub_category_id;
    private String product_price = null;
    private ProgressDialog progressDialog;
    private ProductDescriptionPresenter productDescriptionPresenter;
    private NetworkState networkState;
    private SizeListAdapter sizeListAdapter;
    private DeliveryOptionAdapter deliveryOptionAdapter;
    //private QuantityInKgAdapter quantityInKgAdapter;
    private LoginDialog loginDialog;
    private String parent_id;
    private String product_option_id;
    private ArrayList<String> product_option_value_id;
    private String size_product_option_id;
    private String size_product_option_value_id;
    private SharedPreferences sharedPreferences;
    private UserSessionManager userSessionManager;
    //private ProductDescription productDesc;

    private ProductListAdapter productListAdapter;

//    private ViewPager productDetailsViewPage;
//    private TabLayout productDetailsTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);
        ButterKnife.bind(this);
        networkState = new NetworkState();
        loginDialog = new LoginDialog();
        //itemQuantityInKgList = new ArrayList<>();
        //itemQuantityInKgListTwo = new ArrayList<>();
        //itemQuantityInKgListThree = new ArrayList<>();
        userSessionManager = new UserSessionManager(this);

//        itemQuantityInKgList.add("Select One");
//        itemQuantityInKgList.add("1.00 K.G.");
//        itemQuantityInKgList.add("2.00 K.G.");
//        itemQuantityInKgList.add("3.00 K.G.");
//        itemQuantityInKgList.add("4.00 K.G.");
//        itemQuantityInKgList.add("5.00 K.G.");
//
//        itemQuantityInKgListTwo.add("Select One");
//        itemQuantityInKgListTwo.add("2.00 K.G.");
//        itemQuantityInKgListTwo.add("3.00 K.G.");
//        itemQuantityInKgListTwo.add("4.00 K.G.");
//        itemQuantityInKgListTwo.add("5.00 K.G.");
//
//        itemQuantityInKgListThree.add("Select One");
//        itemQuantityInKgListThree.add("3.00 K.G.");
//        itemQuantityInKgListThree.add("4.00 K.G.");
//        itemQuantityInKgListThree.add("5.00 K.G.");



        productDescriptionPresenter = new ProductDescriptionInteractorImpl(this);

        sharedPreferences = getSharedPreferences(Constants.USER_SESSION_MANAGEMENT.KEY_PREFER_NAME,MODE_PRIVATE);
        customer_email = sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"");
        session_data = sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_SESSION_DATA,"");
        Log.d(TAG, "onCreate: "+sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_NAME,""));

        Intent intent = getIntent();
        product_id = intent.getStringExtra("product_id");
        category_id = intent.getStringExtra("category_id");
        parent_position = intent.getStringExtra("parent_position");
        prev_activity = intent.getStringExtra("prev_activity");
        searchQuery = intent.getStringExtra("searchQuery");
        Log.d(TAG, "onCreate: "+product_id);

        if (networkState.isNetworkAvailable(this)){
            productDescriptionPresenter.fetchDescriptionDataFromServer(product_id);
            getParentId(product_id);
        }else { Toast.makeText(this, "Please connect to internet", Toast.LENGTH_SHORT).show(); }

        setBadge();

        productDescriptionWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (networkState.isNetworkAvailable(ProductDescriptionActivity.this)){
                    if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                        productDescriptionPresenter.saveWishListItemOnServer(customer_email,product_id);
                    }else {loginDialog.loginDialog(ProductDescriptionActivity.this);}
                }else {Toast.makeText(ProductDescriptionActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();}
            }
        });

//        if (networkState.isNetworkAvailable(this)){
//            productDescriptionPresenter.fetchProductListDataFromServer(category_id);
//        }else {
//            Toast.makeText(this, "Please connect to internet", Toast.LENGTH_SHORT).show();
//        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        productDescriptionCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (networkState.isNetworkAvailable(ProductDescriptionActivity.this)){
                        if (customer_email.length()>0){
                            Log.d(TAG, "onClick: "+parent_id);
                            if (points.equalsIgnoreCase("0")){
//                                if (sizeListAdapter.getProductOptionId() == null){
//                                    //size_product_option_id = "b";
//                                    Toast.makeText(ProductDescriptionActivity.this, "Please select size", Toast.LENGTH_SHORT).show();
//                                }else {
//                                    if (sizeListAdapter.getProductOptionIdValue() == null){
//                                        //size_product_option_value_id = "c";
//                                        Toast.makeText(ProductDescriptionActivity.this, "Please select size", Toast.LENGTH_SHORT).show();
//                                    } else {
                                if (Integer.valueOf(product_stock) > 0){
                            if (Integer.parseInt(String.valueOf(countView.getText()))>0){
                                if (Integer.parseInt(String.valueOf(countView.getText()))<=5){
                                Log.d(TAG, "onClick deliveryOptionAdapter: "+deliveryOptionAdapter.getProductOptionId());
                                if (deliveryOptionAdapter.getProductOptionId() == null){
                                    //product_option_value_id = deliveryOptionAdapter.getProductOptionIdValue();
                                    //product_option_id = deliveryOptionAdapter.getProductOptionId();
                                    if (sizeListAdapter.getProductOptionId() == null & sizeListAdapter.getProductOptionIdValue() == null){
                                        product_option_value_id = new ArrayList<>();
                                        product_option_value_id.add("0");
                                        product_option_value_id.add("0");
                                        product_option_id = "0";
                                        size_product_option_id = "0";
                                        size_product_option_value_id = "0";
                                        productDescriptionPresenter.addRawFishItemToCart(customer_email,session_data,
                                                //(int)Double.parseDouble(itemQuantityInKgSpinner.getSelectedItem().toString().replace(" K.G.",""))
                                                Integer.parseInt(String.valueOf(countView.getText())),
                                                Integer.parseInt(product_id),
                                                product_option_id,
                                                product_option_value_id,
                                                size_product_option_id,
                                                size_product_option_value_id);
                                    }else {
                                        product_option_value_id = new ArrayList<>();
                                        product_option_value_id.add("0");
                                        product_option_value_id.add("0");
                                        product_option_id = "0";
                                        size_product_option_id = sizeListAdapter.getProductOptionId();
                                        size_product_option_value_id = sizeListAdapter.getProductOptionIdValue();
                                        productDescriptionPresenter.addRawFishItemToCart(customer_email,session_data,
                                                //(int)Double.parseDouble(itemQuantityInKgSpinner.getSelectedItem().toString().replace(" K.G.",""))
                                                Integer.parseInt(String.valueOf(countView.getText())),
                                                Integer.parseInt(product_id),
                                                product_option_id,
                                                product_option_value_id,
                                                size_product_option_id,
                                                size_product_option_value_id);
                                    }
                                }else {
                                    if (sizeListAdapter.getProductOptionId() == null & sizeListAdapter.getProductOptionIdValue() == null){
                                        product_option_value_id = deliveryOptionAdapter.getProductOptionIdValue();
                                        product_option_id = deliveryOptionAdapter.getProductOptionId();
                                        size_product_option_id = "0";
                                        size_product_option_value_id = "0";
                                        productDescriptionPresenter.addRawFishItemToCart(customer_email,session_data,
                                                //(int)Double.parseDouble(itemQuantityInKgSpinner.getSelectedItem().toString().replace(" K.G.",""))
                                                Integer.parseInt(String.valueOf(countView.getText())),
                                                Integer.parseInt(product_id),
                                                product_option_id,
                                                product_option_value_id,
                                                size_product_option_id,
                                                size_product_option_value_id);
                                    }else {
                                        product_option_value_id = deliveryOptionAdapter.getProductOptionIdValue();
                                        product_option_id = deliveryOptionAdapter.getProductOptionId();
                                        size_product_option_id = sizeListAdapter.getProductOptionId();
                                        size_product_option_value_id = sizeListAdapter.getProductOptionIdValue();
                                        productDescriptionPresenter.addRawFishItemToCart(customer_email,session_data,
                                                //(int)Double.parseDouble(itemQuantityInKgSpinner.getSelectedItem().toString().replace(" K.G.",""))
                                                Integer.parseInt(String.valueOf(countView.getText())),
                                                Integer.parseInt(product_id),
                                                product_option_id,
                                                product_option_value_id,
                                                size_product_option_id,
                                                size_product_option_value_id);
                                    }
                                }
                                }else {Toast.makeText(ProductDescriptionActivity.this, "Maximum Quantity is 5 Allowed.", Toast.LENGTH_SHORT).show();}
                            }else {
                                Toast.makeText(ProductDescriptionActivity.this, "Please Select minimum 1 quantity", Toast.LENGTH_SHORT).show();
                            }
                                }else { Toast.makeText(ProductDescriptionActivity.this, "Product is Out of Stock.", Toast.LENGTH_SHORT).show(); }
//                                    }
//                                }
                            }else if (points.equalsIgnoreCase("1")){
                                if (Integer.parseInt(String.valueOf(countView.getText()))>0){
                                    productDescriptionPresenter.addRestaurantItemToCart(customer_email,session_data,Integer.parseInt(String.valueOf(countView.getText())),
                                            Integer.parseInt(product_id));
                                }else {
                                    Toast.makeText(ProductDescriptionActivity.this, "Please Select minimum 1 quantity", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }else {loginDialog.loginDialog(ProductDescriptionActivity.this);}
                    }else {Toast.makeText(ProductDescriptionActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();}
                }catch (Exception e){
                    e.printStackTrace();
                    Snackbar.make(backBtn,"Oops, Something went wrong",Snackbar.LENGTH_LONG).show();
                }
            }
        });

        cartIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (Integer.valueOf(product_stock) > 0){
                        if (Integer.valueOf(countView.getText().toString())<5){
                            Log.d(TAG, "onClick: "+product_stock);
                            if (Integer.valueOf(product_stock) > Integer.valueOf(countView.getText().toString())){
                                count++;
                                countView.setText(String.valueOf(count));
                            }else { Toast.makeText(ProductDescriptionActivity.this, "Stock is low. Maximum order quantity is " + countView.getText().toString() , Toast.LENGTH_LONG).show(); }
                        }else { Toast.makeText(ProductDescriptionActivity.this, "Maximum Quantity 5 Allowed.", Toast.LENGTH_SHORT).show(); }
                    }else { Toast.makeText(ProductDescriptionActivity.this, "Product is Out of Stock.", Toast.LENGTH_SHORT).show(); }
                }catch (Exception e){e.printStackTrace();}
            }
        });

        cartDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.valueOf(countView.getText().toString()) > 0){
                    count--;
                    countView.setText(String.valueOf(count));
                }
            }
        });

        searchProduct();

        tabLayout();

//        see_all.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(ProductDescriptionActivity.this,ProductListActivity.class);
//                startActivity(i);
//            }
//        });

    }

    //tabLayout
    private void tabLayout(){
        TabLayout productDetailsTabLayout = findViewById(R.id.product_details_tabLayout) ;
        TabItem tabFeatures = findViewById(R.id.tabFeatures);
        TabItem tabInfo = findViewById(R.id.tabInfo);
        ViewPager productDetailsViewPage = findViewById(R.id.product_details_viewpager );

        PagerAdapter pagerAdapter =new ProductDetailsAdapter (getSupportFragmentManager(),
                productDetailsTabLayout.getTabCount());

        productDetailsViewPage.setAdapter(pagerAdapter);

        productDetailsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productDetailsViewPage.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    //product List
    private void searchProduct(){
        try {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    try { Log.d(TAG, "onQueryTextChange: "+query);
                        startActivity(new Intent(ProductDescriptionActivity.this,SearchResultActivity.class)
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


//    @Override
//    public void setProductListSuccess(ArrayList<ProductListDetails> productListArray) {
//        try {
//            Log.d(TAG, "setProductListSuccess: "+productListArray.toString());
//            if (productListArray.isEmpty()){
//                productListRecyclerView.setVisibility(View.INVISIBLE);
//
//            }else {
//                productListRecyclerView.setVisibility(View.VISIBLE);
//                productListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//                productListAdapter = new ProductListAdapter(this,productListArray,category_id,parent_position,"ProductListActivity","0");
//                productListRecyclerView.setAdapter(productListAdapter);
//                productListRecyclerView.setNestedScrollingEnabled(false);
//            }
//        }catch (Exception e){e.printStackTrace();}
//    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showProgress() {

    }


    @Override
    public void onBackPressed() {
        if (prev_activity.equalsIgnoreCase("SearchResultActivity")){
            super.onBackPressed();
            startActivity(new Intent(ProductDescriptionActivity.this,SearchResultActivity.class)
                    .putExtra("searchQuery",searchQuery));
            finish();
        }else if (prev_activity.equalsIgnoreCase("ProductListActivity")){
            super.onBackPressed();
            startActivity(new Intent(ProductDescriptionActivity.this,ProductListActivity.class)
                    .putExtra("category_id",category_id)
                    .putExtra("parent_position",parent_position));
            finish();
        }
    }

    public void cart(View view){
        if (networkState.isNetworkAvailable(ProductDescriptionActivity.this)){
            if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                Intent intent1 = new Intent(ProductDescriptionActivity.this,CartActivity.class);
                intent1.putExtra("CartActivity","ProductDescriptionActivity");
                intent1.putExtra("product_id",product_id);
                intent1.putExtra("category_id",category_id);
                startActivity(intent1);
                finish();
            }else {loginDialog.loginDialog(ProductDescriptionActivity.this);}
        }else {Toast.makeText(ProductDescriptionActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();}
    }

    public void wishList(View view){
        if (networkState.isNetworkAvailable(ProductDescriptionActivity.this)){
            if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                productDescriptionPresenter.gotoWishList();
            }else {loginDialog.loginDialog(ProductDescriptionActivity.this);}
        }else {Toast.makeText(ProductDescriptionActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();}
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
    public void showDeliveryTime(String delivery_time, String parent_id) {
        try {
//            if (parent_id.equalsIgnoreCase("1")){
//                deliveryTime.setText("Delivery Will Be Between 9am-11am The Next Day If Order Before 9:30pm.");
//            }else if (parent_id.equalsIgnoreCase("2")){
//                //deliveryTime.setText("Delivery Within Hour For Salt Lake Area And 1 Hour And 30 Minutes For New Town Area.");
//                deliveryTime.setText("Delivery Within 1 Hour Approximately.");
//            }
            //deliveryTime.setText("Delivery charges will be applied according to pin code.");
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showDeliveryTimeError(String error) {
        //deliveryTime.setText(error);
    }

    @Override
    public void showDescription(String description) {
        try {
            productDescription.setText(description.replace("amp;","").replace("nbsp;",""));
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showDescriptionError(String error) {
        productDescription.setText(error);
    }

    @Override
    public void showProductName(String product_name) {
        try {
            productName.setText(product_name);
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showProductNameError(String error) {
        productName.setText(error);
    }

    @Override
    public void showProductPrice(String price) {
        try {
            product_price = price;
            productPrice.setText(price);
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showImage(String descriptionImage) {
        try {
            Log.d(TAG, "showImage: "+descriptionImage);
            Picasso.get().load(descriptionImage).into(imageView);
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showImageError(String error) { imageView.setImageResource(R.drawable.image_not_available); }

    @Override
    public void showProductPriceError(String error) {
        productPrice.setText(error);
    }

    @Override
    public void showProductStock(String stock) {
        try {
            if (Integer.parseInt(stock) > 0){
                productStock.setText("In Stock");
                productStock.setTextColor(getResources().getColor(R.color.colorAccent));
            }else { productStock.setText("Out of Stock"); productStock.setTextColor(Color.RED); }
            this.product_stock = stock;
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showProductStockError(String error) {
        productStock.setText(error);
    }

    @Override
    public void showDeliveryOption(ArrayList<ProductSizeDetails> deliveryOption) {
        try{
            Log.d(TAG, "showDeliveryOption: "+deliveryOption.toString());
            deliveryOptionLayout.setVisibility(View.VISIBLE);
            deliveryOptionList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            deliveryOptionAdapter = new DeliveryOptionAdapter(this,deliveryOption);
            deliveryOptionList.setAdapter(deliveryOptionAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void showDeliveryOptionError() {
        Log.d(TAG, "showDeliveryOptionError: ");
        deliveryOptionLayout.setVisibility(View.GONE);
    }

    @Override
    public void showSizeList(ArrayList<ProductSizeDetails> productSize) {
        try {
            Log.d(TAG, "showSizeList: "+productSize.toString());
            sizeListSpinner.setVisibility(View.VISIBLE);
            sizeListAdapter = new SizeListAdapter(this,productSize);
            sizeListSpinner.setAdapter(sizeListAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void showSizeListError() {
        sizeOptionLayout.setVisibility(View.GONE);
    }

    @Override
    public void showSizeRelatedPrice(final ArrayList<ProductSizeDetails> productSize,final String product_id) {
        sizeListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                try {
                    Log.d(TAG, "showSizeRelatedPrice: "+productSize.get(position).getPrice());
                    Log.d(TAG, "showSizeRelatedPrice: "+product_price);
                    if (!((product_price.equalsIgnoreCase("Not Available")) & (product_price == null))){
                        String sizePriceString = productSize.get(position).getPrice();
                        if (sizePriceString.equalsIgnoreCase("false")){
                            Log.d(TAG, "onItemSelected: false");
                            productPrice.setText(product_price);
                        }else {
                            String sizePriceRemoveRs = sizePriceString.replace("Rs.","").replace(",","");
                            Log.d(TAG, "onItemSelected: "+sizePriceRemoveRs);
                            Double sizePriceDouble = Double.parseDouble(sizePriceRemoveRs);
                            Log.d(TAG, "onItemSelected: "+sizePriceDouble);
                            String productPriceRemoveRs = product_price.replace("Rs.","").replace(",","");
                            Log.d(TAG, "onItemSelected: "+productPriceRemoveRs);
                            Double productPriceDouble = Double.parseDouble(productPriceRemoveRs);
                            Log.d(TAG, "onItemSelected: "+productPriceDouble);

                            if (productSize.get(position).getPrice_prefix().equalsIgnoreCase("+")){
                                if (productSize.get(position).getPrice().equalsIgnoreCase("false")){
                                    productPrice.setText(product_price);
                                }else {
                                    Double totalPrice = sizePriceDouble + productPriceDouble;
                                    productPrice.setText("Rs."+String.format("%.2f",totalPrice));
                                }
                            }else if(productSize.get(position).getPrice_prefix().equalsIgnoreCase("-")){
                                if (productSize.get(position).getPrice().equalsIgnoreCase("false")){
                                    productPrice.setText(product_price);
                                }else {
                                    Double totalPrice = productPriceDouble - sizePriceDouble;
                                    productPrice.setText("Rs."+String.format("%.2f",totalPrice));
                                }
                            }
                        }

//                        if (product_id.equalsIgnoreCase("89")){
//                            if (position == 2){
//                                Log.d(TAG, "onItemSelected: 1");
//                                quantityInKgAdapter = new QuantityInKgAdapter(ProductDescriptionActivity.this,itemQuantityInKgListTwo);
//                                itemQuantityInKgSpinner.setAdapter(quantityInKgAdapter);
//                            }else if (position == 3){
//                                Log.d(TAG, "onItemSelected: 2");
//                                quantityInKgAdapter = new QuantityInKgAdapter(ProductDescriptionActivity.this,itemQuantityInKgListThree);
//                                itemQuantityInKgSpinner.setAdapter(quantityInKgAdapter);
//                            }else {
//                                quantityInKgAdapter = new QuantityInKgAdapter(ProductDescriptionActivity.this,itemQuantityInKgList);
//                                itemQuantityInKgSpinner.setAdapter(quantityInKgAdapter);
//                            }
//                        }
                    }
                }catch (Exception e){e.printStackTrace();}
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void showSizeRelatedPriceError() {

    }


//    @Override
//    public void showItemQuantityInKg(String product_id){
//        try {
//            quantityInKgAdapter = new QuantityInKgAdapter(this,itemQuantityInKgList);
//            itemQuantityInKgSpinner.setAdapter(quantityInKgAdapter);
//        }catch (Exception e){e.printStackTrace();}
//    }
//
//    @Override
//    public void showItemQuantityInKgError() { itemQuantityInKgLayout.setVisibility(View.GONE); }

    @Override
    public void showItemQuantityInNumber() { }

    @Override
    public void showItemQuantityInNumberError() { itemQuantityInNumberLayout.setVisibility(View.GONE); }

    @Override
    public void showWishListedItemSuccess(String message) {
        try {
            Toast.makeText(this, "Product has been added to wishlist successfully.", Toast.LENGTH_SHORT).show();
            productDescriptionWishList.setBackgroundResource(R.drawable.button_red);
            productDescriptionWishList.setText("Wishlisted");
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showWishListedItemFailure() {
        Toast.makeText(this, "Please try again", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showItemAddedToCartSuccess(String message) {
        try {
            Log.d(TAG, "showItemAddedToCartSuccess: "+message);
            Toast.makeText(this, "Product has been added to cart successfully.", Toast.LENGTH_SHORT).show();
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void showItemAddToCartError() {
        Toast.makeText(this, "Please try again", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setWishListCount() {
        try {
            if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_WISHLIST_COUNT,"").equalsIgnoreCase("0")){
                Log.d(TAG, "setWishListCount: "+sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_WISHLIST_COUNT,""));
                count_wishList.setVisibility(View.VISIBLE);
                count_wishList.setText("1");
                userSessionManager.updateWishListItem("1");
                setBadge();
            }else {
                int wishListCount = (int)Double.parseDouble(sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_WISHLIST_COUNT,""))+1;
                Log.d(TAG, "setWishListCount: "+wishListCount);
                userSessionManager.updateWishListItem(String.valueOf(wishListCount));
                setBadge();
            }
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void setWishListCountError() {

    }

    @Override
    public void setCartCount() {
        try {
            Log.d(TAG, "setCartCount 1: "+sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CART_COUNT,""));
            if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CART_COUNT,"").equalsIgnoreCase("0")){
                Log.d(TAG, "setCartCount 2: "+sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CART_COUNT,""));
                count_cart.setVisibility(View.VISIBLE);
                count_cart.setText("1");
                userSessionManager.updateCartCount("1");
                setBadge();
            }else {
                int cartCount = (int)Double.parseDouble(sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CART_COUNT,""))+1;
                Log.d(TAG, "setCartCount 3: "+cartCount);
                userSessionManager.updateCartCount(String.valueOf(cartCount));
                setBadge();
            }
        }catch (NullPointerException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void setCartCountError() {

    }

    @Override
    public void showOrderQuantity() { }

    @Override
    public void showOrderQuantityError(String error) { }

    @Override
    public void parentCategory(String category) {
        try {
            Log.d(TAG, "parentCategory: "+category);
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void navigateToWishList() {
        startActivity(new Intent(ProductDescriptionActivity.this,WishListActivity.class));
        finish();
    }

    @Override
    public void navigateToCart() {
        startActivity(new Intent(ProductDescriptionActivity.this,CartActivity.class)
            .putExtra("CartActivity","ProductDescriptionActivity"));
    }

    @Override
    public void showServerError() {
        Snackbar.make(backBtn,"Oops, Something went wrong",Snackbar.LENGTH_LONG).show();
    }

    public void getParentId(String product_id){
        //String JSON_URL = "http://166.62.52.188/Murighonto_new/index.php?route=restapi/product/product&product_id="+product_id;
        String JSON_URL = Url.PRODUCT_DESCRIPTION_URL+product_id;
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response.toString());
                        if (response.length() > 0){
                            try {
                                JSONObject jsonObject = new JSONObject(response.toString());
                                parent_id = jsonObject.getString("parent_id");
                                points = jsonObject.getString("points");
                                //ProductDescription productDescription = new ProductDescription();
                                //productDescription.setParent_id(jsonObject.getString("parent_id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {

                        }
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

//    private void imageSlider(String descriptionImage){
//        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this,descriptionImage);
//        imageViewPager.setAdapter(viewPagerAdapter);
//
//        dotCount = viewPagerAdapter.getCount();
//        dots = new ImageView[dotCount];
//
//        for(int i = 0; i < dotCount; i++){
//
//            dots[i] = new ImageView(this);
//            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            params.setMargins(8, 0, 8, 0);
//            sliderDotPanel.addView(dots[i], params);
//        }
//        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
//
//        imageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                for(int i = 0; i< dotCount; i++){
//                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
//                }
//                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//    }

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
}
