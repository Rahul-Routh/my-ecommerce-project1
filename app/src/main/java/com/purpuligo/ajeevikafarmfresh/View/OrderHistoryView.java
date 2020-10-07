package com.purpuligo.ajeevikafarmfresh.View;

import com.purpuligo.ajeevikafarmfresh.Model.Pojo.OrderHistoryDetails;

import java.util.ArrayList;

public interface OrderHistoryView {
    void showProgressBar();
    void hideProgressBar();
    void showOrderHistoryList(ArrayList<OrderHistoryDetails> orderHistoryList);
    void showOrderHistoryError();
    void showServerError();
}
