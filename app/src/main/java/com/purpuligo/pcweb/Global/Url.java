package com.purpuligo.pcweb.Global;

public class Url {
    //private static final String IP_ADDRESS = "http://166.62.52.188";
    //private static final String BASE_URL_MURIGHONTO = IP_ADDRESS + "/Murighonto_new/index.php?route=restapi";
    //private static final String BASE_URL = "http://purpuligo.com/aajeevika/index.php?route=restapi";
    //===============production base url===================================================
    //private static final String BASE_URL = "http://115.124.105.16/aajeevika/index.php?route=restapi";
    private static final String BASE_URL = "http://purpuligo.com/pcbzr/index.php?route=restapi";

    public static final String LOGIN_URL = BASE_URL + "/account/login";
    public static final String VERSION_COMPARE_URL = BASE_URL + "/account/login/version_compare_api";
    public static final String IMAGE_SLIDER_URL = BASE_URL + "/common/home/mobile_slider";
    public static final String SIGN_UP_URL = BASE_URL + "/account/usersignup/appRegisterUser";
    public static final String FORGOT_PASSWORD = BASE_URL + "/account/forgotten/appForgotPassword";
    public static final String HOME_DATA_URL = BASE_URL + "/product/category";
    public static final String WISH_LIST_URL = BASE_URL + "/account/wishlist/appGetWishlist";
    public static final String CART_URL = BASE_URL + "/checkout/cart/appGetCartDetails";
    public static final String PRODUCT_LIST_URL = BASE_URL + "/product/category/product&cat_id=";
    public static final String PRODUCT_DESCRIPTION_URL = BASE_URL + "/product/product&product_id=";
    public static final String SAVE_WISH_LIST_ON_SERVER_URL = BASE_URL + "/account/wishlist/appAddToWishlist";
    public static final String ADD_ITEM_IN_CART_URL = BASE_URL + "/checkout/cart/appAddToCart";
    public static final String ADDRESS_LIST_URL = BASE_URL + "/checkout/checkout/getAllShippingAddress";
    public static final String REMOVE_CART_ITEM_URL = BASE_URL + "/checkout/cart/appRemoveProduct";
    public static final String CHECKOUT_DETAILS_URL = BASE_URL + "/checkout/checkout/appCheckout";
    public static final String SUBMIT_ORDER_URL = BASE_URL + "/checkout/checkout/appCreateOrder";
    public static final String UPDATE_CUSTOMER_DETAILS_URL = BASE_URL + "/checkout/checkout/addDetailsOfShippingAddress";
    public static final String EDIT_CUSTOMER_DETAILS_URL = BASE_URL + "/checkout/checkout/updateDetailsOfShippingAddress";
    public static final String ORDER_HISTORY_DETAILS_URL = BASE_URL + "/checkout/checkout/appGetOrderDetails";
    public static final String ORDER_HISTORY_URL = BASE_URL + "/checkout/checkout/appGetOrders";
    public static final String REMOVE_WISH_LIST_ITEM_URL = BASE_URL + "/account/wishlist/appRemoveWishlist";
    public static final String PLACE_ONLINE_PAYMENT_ORDER = BASE_URL + "/checkout/payment_method/confirm";
    public static final String REMOVE_ADDRESS = BASE_URL + "/account/usersignup/deleteAddress";
    public static final String SEARCH_LIST_URL = BASE_URL + "/product/search&search=";
}
