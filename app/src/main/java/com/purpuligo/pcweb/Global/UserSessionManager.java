package com.purpuligo.pcweb.Global;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.purpuligo.pcweb.Activity.LoginActivity;
import com.purpuligo.pcweb.Constants.Constants;

import static android.content.Context.MODE_PRIVATE;

public class UserSessionManager{

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    //private int PRIVATE_MODE = 0;
    private Activity activity;

    public UserSessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(Constants.USER_SESSION_MANAGEMENT.KEY_PREFER_NAME,MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void createUserSession(String customer_email,String password,String customer_id,String customer_name,int wishlist_count,
                                  String session_data,int cart_count,String address_one,String address_two,String city,String state,String postal_code){

        //editor.putBoolean(Constants.USER_SESSION_MANAGEMENT.KEY_IS_USER_LOGIN,true);
        editor.putString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,customer_email);
        editor.putString(Constants.USER_SESSION_MANAGEMENT.KEY_PASSWORD,password);
        editor.putString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_ID,customer_id);
        editor.putString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_NAME,customer_name);
        editor.putString(Constants.USER_SESSION_MANAGEMENT.KEY_WISHLIST_COUNT,String.valueOf(wishlist_count));
        editor.putString(Constants.USER_SESSION_MANAGEMENT.KEY_SESSION_DATA,session_data);
        editor.putString(Constants.USER_SESSION_MANAGEMENT.KEY_CART_COUNT,String.valueOf(cart_count));
        editor.putString(Constants.USER_SESSION_MANAGEMENT.KEY_ADDRESS_ONE,address_one);
        editor.putString(Constants.USER_SESSION_MANAGEMENT.KEY_ADDRESS_TWO,address_two);
        editor.putString(Constants.USER_SESSION_MANAGEMENT.KEY_CITY,city);
        editor.putString(Constants.USER_SESSION_MANAGEMENT.KEY_STATE,state);
        editor.putString(Constants.USER_SESSION_MANAGEMENT.KEY_POSTAl_CODE,postal_code);
        editor.putBoolean(Constants.USER_SESSION_MANAGEMENT.KEY_INTRO_PAGE,true);
        editor.commit();
    }

    public void updateWishListItem(String wishListCount){
        editor.putString(Constants.USER_SESSION_MANAGEMENT.KEY_WISHLIST_COUNT,wishListCount);
        editor.commit();
    }

    public void updateCartCount(String cartCount){
        editor.putString(Constants.USER_SESSION_MANAGEMENT.KEY_CART_COUNT,cartCount);
        editor.commit();
    }

    public void loginCheck(){
        if (checkLogin()){
            activity.finish();
        }else {
            Toast.makeText(context,"Valid", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkLogin(){

        if(!this.isUserLoggedIn())
        {
            Intent i = new Intent(context,LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            return true;
        }
        return false;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
    }

    public boolean isUserLoggedIn(){
        return sharedPreferences.getBoolean(Constants.USER_SESSION_MANAGEMENT.KEY_INTRO_PAGE, false);
    }
}
