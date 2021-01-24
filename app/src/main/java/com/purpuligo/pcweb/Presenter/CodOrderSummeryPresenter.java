package com.purpuligo.pcweb.Presenter;

public interface CodOrderSummeryPresenter {
    void getCheckoutDetails(String address_id, String customer_email, String session_data);
    void submitCodOrder(String customer_email, String session_data, int id_shipping_address);
    void submitOnlinePaymentOrder(String customer_email, String session_data, String payment_id, int id_shipping_address);
}
