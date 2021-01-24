package com.purpuligo.pcweb.View;

import com.purpuligo.pcweb.Model.Pojo.OrderHistoryDetails;

import java.util.ArrayList;

public interface OrderHistoryView {
    void showProgressBar();
    void hideProgressBar();
    void showOrderHistoryList(ArrayList<OrderHistoryDetails> orderHistoryList);
    void showOrderHistoryError();
    void showServerError();
}
