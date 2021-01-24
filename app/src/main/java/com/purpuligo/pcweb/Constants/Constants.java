package com.purpuligo.pcweb.Constants;


public interface Constants {

    interface LOGIN_SUBMIT_DETAILS{
        String KEY_EMAIL = "email";
        String KEY_PASSWORD = "password";
    }

    interface LOGIN_DETAILS{
        String KEY_LOGIN_USER = "login_user";
        String KEY_STATUS = "status";
        String KEY_MESSAGE = "message";
        String KEY_CUSTOMER_ID = "customer_id";
        String KEY_CUSTOMER_NAME = "customer_name";
        String KEY_WISHLIST_COUNT = "wishlist_count";
        String KEY_CART_COUNT = "cart_count";
        String KEY_SESSION_DATA = "session_data";
        String KEY_ADDRESS = "address";
        String KEY_ADDRESS_ONE = "address_1";
        String KEY_ADDRESS_TWO = "address_2";
        String KEY_CITY = "city";
        String KEY_STATE = "state";
        String KEY_POSTAl_CODE = "postcode";
    }

    interface SIGN_UP_SUBMIT_DETAILS{
        String KEY_FIRST_NAME = "firstname";
        String KEY_LAST_NAME = "lastname";
        String KEY_EMAIL = "email";
        String KEY_TELEPHONE = "telephone";
        String KEY_ADDRESS = "address_1";
        String KEY_LANDMARK = "address_2";
        String KEY_POSTAL_CODE = "postcode";
        String KEY_PASSWORD = "password";
        String KEY_CITY = "city";
        String KEY_COUNTRY = "country";
        String KEY_ZONE_ID = "zone";
    }

    interface FORGOT_PASSWORD{
        String KEY_EMAIL = "email";
        String KEY_NEW_PASSWORD = "new_password";
    }

    interface FORGOT_PASSWORD_RESPONSE{
        String KEY_STATUS = "status";
        String KEY_MESSAGE = "message";
    }

    interface USER_SESSION_MANAGEMENT{
        String KEY_PREFER_NAME = "Ajeevika_Farm_Fresh";
        String KEY_IS_USER_LOGIN = "IsUserLoggedIn";
        String KEY_CUSTOMER_EMAIL = "customer_email";
        String KEY_PASSWORD = "customer_password";
        String KEY_CUSTOMER_ID = "customer_id";
        String KEY_CUSTOMER_NAME = "customer_name";
        String KEY_WISHLIST_COUNT = "wishlist_count";
        String KEY_SESSION_DATA = "session_data";
        String KEY_CART_COUNT = "cart_count";
        String KEY_INTRO_PAGE = "intro_splash_page";
        String KEY_ADDRESS_ONE = "address_1";
        String KEY_ADDRESS_TWO = "address_2";
        String KEY_CITY = "city";
        String KEY_STATE = "state";
        String KEY_POSTAl_CODE = "postcode";
    }

    interface CATEGORIES_DETAILS{
        String KEY_PARENT_NAME = "parent_name";
        String KEY_PARENT_ID = "parent_id";
        String KEY_PARENT_IMAGE = "parent_image";
        String KEY_PARENT_CATEGORIES = "categories";
        String KEY_CATEGORIES_NAME = "category_name";
        String KEY_CATEGORIES_ID = "category_id";
        String KEY_CATEGORIES_IMAGE = "category_image";
    }

    interface PRODUCT_LIST_DETAILS{
        String KEY_PRODUCT = "products";
        String KEY_PRODUCT_ID = "product_id";
        String KEY_PRODUCT_NAME = "product_name";
        String KEY_PRODUCT_PRICE = "product_price";
        String KEY_PRODUCT_IMAGE = "product_image";
    }

    interface SUB_CATEGORIES_DETAILS{
        String KEY_CATEGORY = "category";
        String KEY_CATEGORY_NAME = "category_name";
        String KEY_CATEGORY_ID = "category_id";
        String KEY_CATEGORY_IMAGE = "category_image";
    }

    interface PRODUCT_DESCRIPTION{
        String KEY_PRODUCT_ID = "product_id";
        String KEY_PRODUCT_NAME = "product_name";
        String KEY_PRODUCT_IMAGE = "product_image";
        String KEY_PRODUCT_DESCRIPTION = "description";
        String KEY_PRODUCT_PRICE = "price";
        String KEY_PRODUCT_STOCK = "stock";
        String KEY_DELIVERY_TIME = "delivery_time";
    }

    interface PRODUCT_SIZE_DETAILS{
        String KEY_PRODUCT_OPTION_ID = "product_option_id";
        String KEY_PRODUCT_OPTION_VALUE_ID = "product_option_value_id";
        String KEY_OPTION_VALUE_ID = "option_value_id";
        String KEY_PRODUCT_OPTION_NAME = "product_option_name";
        String KEY_PRICE = "price";
        String KEY_PRICE_PREFIX = "price_prefix";
        String KEY_OPTION_ID = "option_id";
        String KEY_OPTION_NAME = "options_name";
        String KEY_TYPE = "type";
        String KEY_OPTION = "options";
        String KEY_PRODUCT_OPTION_VALUE = "product_option_value";
    }

    interface ADD_TO_WISH_LIST{
        String KEY_CUSTOMER_EMAIL = "email";
        String KEY_PRODUCT_ID = "product_id";
    }

    interface ADD_TO_CART{
        String KEY_CUSTOMER_EMAIL = "email";
    }

    interface FETCH_WISH_LIST_DATA{
        String KEY_WISHLIST_PRODUCT = "wishlist_products";
        String KEY_STATUS = "status";
        String KEY_MESSAGE = "message";
        String KEY_PRODUCT_ID = "product_id";
        String KEY_PRODUCT_TITLE = "title";
        String KEY_IS_GIFT_PRODUCT = "is_gift_product";
        String KEY_ID_PRODUCT_ATTRIBUTE = "id_product_attribute";
        String KEY_ID_ADDRESS_DELIVERY = "id_address_delivery";
        String KEY_STOCK = "stock";
        String KEY_DISCOUNT_PRICE = "discount_price";
        String KEY_DISCOUNT_PERCENTAGE = "discount_percentage";
        String KEY_IMAGES = "images";
        String KEY_PRICE = "price";
        String KEY_ALLOW_OUT_OF_STOCK = "allow_out_of_stock";
        String KEY_SHOW_PRICE = "show_price";
        String KEY_AVAILABLE_FOR_ORDER = "available_for_order";
        String KEY_MINIMAL_QUANTITY ="minimal_quantity";
        String KEY_QUANTITY = "quantity";
        String KEY_PRODUCT_ITEMS = "product_items";
        String KEY_CUSTOMIZABLE_ITEMS = "customizable_items";
    }

    interface CART_DATA{
        String KEY_PRODUCTS = "products";
        String KEY_PRODUCT_ID = "product_id";
        String KEY_TITLE = "title";
        String KEY_IS_GIFT_PRODUCT = "is_gift_product";
        String KEY_ID_PRODUCT_ATTRIBUTE = "id_product_attribute";
        String KEY_ID_ADDRESS_DELIVERY = "id_address_delivery";
        String KEY_STOCK = "stock";
        String KEY_DISCOUNT_PRICE = "discount_price";
        String KEY_DISCOUNT_PERCENTAGE = "discount_percentage";
        String KEY_IMAGES = "images";
        String KEY_PRICE = "price";
        String KEY_QUANTITY = "quantity";
        String KEY_PRODUCT_ITEMS = "product_items";
        String KEY_OPTION_NAME = "name";
        String KEY_OPTION_VALUE = "value";

        String KEY_TOTAL_AMOUNT = "totals";
        String KEY_NAME = "name";
        String KEY_VALUE = "value";
    }

    interface CART_REMOVE_ITEMS{
        String KEY_PRODUCT_ID = "product_id";
        String KEY_QUANTITY = "quantity";
        String KEY_EMAIL = "email";
        String KEY_CART_ID = "cart_id";
        String KEY_SESSION_DATA = "session_data";
    }

    interface PAYMENT_OPTION{
        String KEY_CUSTOMER_EMAIL = "email";
        String KEY_SHIPPING_ADDRESS = "shipping_address";
        String KEY_ID_SHIPPING_ADDRESS = "id_shipping_address";
        String KEY_FIRST_NAME = "firstname";
        String KEY_LAST_NAME = "lastname";
        String KEY_COMPANY = "company";
        String KEY_MOBILE_NUMBER = "mobile_no";
        String KEY_ADDRESS = "address_1";
        String KEY_LANDMARK = "address_2";
        String KEY_CITY = "city";
        String KEY_STATE = "state";
        String KEY_COUNTRY = "country";
        String KEY_POSTAL_CODE = "postcode";
        String KEY_ALIAS = "alias";
    }

    interface UPDATE_USER_DETAIL{
        String KEY_FIRST_NAME = "firstname";
        String KEY_LAST_NAME = "lastname";
        String KEY_EMAIL = "email";
        String KEY_COMPANY = "company";
        String KEY_TELEPHONE = "mobile_no";
        String KEY_PHONE = "telephone";
        String KEY_ADDRESS = "address_1";
        String KEY_LANDMARK = "address_2";
        String KEY_POSTAL_CODE = "postcode";
        String KEY_CITY = "city";
        String KEY_STATE = "state";
        String KEY_COUNTRY = "country";
        String KEY_ADDRESS_ID = "address_id";
    }

    interface CHECKOUT_PAGE{
        //for address details
        String KEY_CHECKOUT_PAGE = "checkout_page";
        String KEY_SHIPPING_ADDRESS = "shipping_address";
        String KEY_ID_SHIPPING_ADDRESS = "id_shipping_address";
        String KEY_FIRST_NAME = "firstname";
        String KEY_LAST_NAME = "lastname";
        String KEY_COMPANY = "company";
        String KEY_MOBILE_NUMBER = "mobile_no";
        String KEY_ADDRESS = "address_1";
        String KEY_LANDMARK = "address_2";
        String KEY_CITY = "city";
        String KEY_STATE = "state";
        String KEY_COUNTRY_ID = "country_id";
        String KEY_COUNTRY = "country";
        String KEY_ZONE_ID = "zone_id";
        String KEY_POSTAL_CODE = "postcode";
        String KEY_ALIAS = "alias";

        //for product details
        String KEY_PRODUCTS = "products";
        String KEY_PRODUCT_ID = "product_id";
        String KEY_TITLE = "title";
        String KEY_IS_GIFT_PRODUCT = "is_gift_product";
        String KEY_ID_PRODUCT_ATTRIBUTE = "id_product_attribute";
        String KEY_ID_ADDRESS_DELIVERY = "id_address_delivery";
        String KEY_STOCK = "stock";
        String KEY_DISCOUNT_PRICE = "discount_price";
        String KEY_DISCOUNT_PERCENTAGE = "discount_percentage";
        String KEY_IMAGES = "images";
        String KEY_PRICE = "price";
        String KEY_QUANTITY = "quantity";
        String KEY_PRODUCT_ITEMS = "product_items";
        String KEY_OPTION_NAME = "name";
        String KEY_OPTION_VALUE = "value";

        //for amount details
        String KEY_TOTAL_AMOUNT = "totals";
        String KEY_NAME = "name";
        String KEY_VALUE = "value";
    }

    interface ORDER_HISTORY{
        String KEY_CUSTOMER_EMAIL = "email";
        String KEY_RESPONSE_STATUS = "status";

        String KEY_ORDER_DETAILS = "order_details";
        String KEY_ORDER_HISTORY = "order_history";
        String KEY_ORDER_ID = "order_id";
        String KEY_CART_ID = "cart_id";
        String KEY_ORDER_NUMBER = "order_number";
        String KEY_STATUS_COLOR = "status_color";
        String KEY_REORDER_ALLOWED = "reorder_allowed";
        String KEY_FIRST_NAME = "firstname";
        String KEY_LAST_NAME = "lastname";
        String KEY_EMAIL = "email";
        String KEY_TELEPHONE = "telephone";
        String KEY_STATUS = "status";
        String KEY_DATE_ADDED = "date_added";
        String KEY_TOTAL_SUM_PRICE = "total";

        //------product details-------------
        String KEY_PRODUCTS = "products";
        String KEY_ID = "id";
        String KEY_TITLE = "title";
        String KEY_IS_GIFT_PRODUCT = "is_gift_product";
        String KEY_ID_PRODUCT_ATTRIBUTE = "id_product_attribute";
        String KEY_STOCK = "stock";
        String KEY_IMAGES = "images";
        String KEY_MODEL = "model";
        String KEY_CUSTOMIZABLE_ITEMS = "customizable_items";
        String KEY_DISCOUNT_PRICE = "discount_price";
        String KEY_DISCOUNT_PERCENTAGE = "discount_percentage";
        String KEY_PRODUCT_ITEMS = "product_items";
        String KEY_QUANTITY = "quantity";
        String KEY_PRICE = "price";
        String KEY_TOTAL = "total";

        //--------address details------------
        String KEY_SHIPPING_ADDRESS = "shipping_address";
        String KEY_BILLING_ADDRESS = "billing_address";

        String KEY_COMPANY = "company";
        String KEY_MOBILE_NUMBER = "mobile_no";
        String KEY_ADDRESS = "address_1";
        String KEY_LANDMARK = "address_2";
        String KEY_POSTAL_CODE = "postcode";
        String KEY_CITY = "city";
        String KEY_STATE = "state";
        String KEY_COUNTRY = "country";
        String KEY_ALIAS = "alias";
    }
}
