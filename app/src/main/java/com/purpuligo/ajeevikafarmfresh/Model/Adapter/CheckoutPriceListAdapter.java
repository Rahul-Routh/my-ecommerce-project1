package com.purpuligo.ajeevikafarmfresh.Model.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.purpuligo.ajeevikafarmfresh.Model.Pojo.CartPriceDetails;
import com.purpuligo.ajeevikafarmfresh.R;

import java.util.ArrayList;

public class CheckoutPriceListAdapter extends RecyclerView.Adapter<CheckoutPriceListAdapter.CheckoutPriceListViewHolder> {

    private String TAG = "CheckoutPriceListAdapter";
    private Context context;
    private ArrayList<CartPriceDetails> checkoutPriceList;

    public CheckoutPriceListAdapter(Context context, ArrayList<CartPriceDetails> checkoutPriceList) {
        this.context = context;
        this.checkoutPriceList = checkoutPriceList;
    }

    @NonNull
    @Override
    public CheckoutPriceListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.checkout_price_details_layout,parent,false);

        return new CheckoutPriceListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutPriceListViewHolder holder, int position) {
        holder.cartAmountTitle.setText(checkoutPriceList.get(position).getPrice_title());
        holder.cartAmount.setText(checkoutPriceList.get(position).getPrice_amount());
        Log.d(TAG, "onBindViewHolder: ");
    }

    @Override
    public int getItemCount() {
        return checkoutPriceList.size()-1;
    }

    public static class CheckoutPriceListViewHolder extends RecyclerView.ViewHolder {
        TextView cartAmountTitle,cartAmount;
        public CheckoutPriceListViewHolder(@NonNull View itemView) {
            super(itemView);
            cartAmountTitle = itemView.findViewById(R.id.cartAmountTitle);
            cartAmount = itemView.findViewById(R.id.cartAmount);
        }
    }
}
