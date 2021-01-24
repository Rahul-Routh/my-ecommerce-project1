package com.purpuligo.pcweb.Model.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.purpuligo.pcweb.Model.Pojo.CartPriceDetails;
import com.purpuligo.pcweb.R;

import java.util.ArrayList;

public class CartPriceDetailsAdapter extends RecyclerView.Adapter<CartPriceDetailsAdapter.CartPriceDetailsHolder> {

    private Context context;
    private ArrayList<CartPriceDetails> cartPriceDetails;

    public CartPriceDetailsAdapter(Context context, ArrayList<CartPriceDetails> cartPriceDetails) {
        this.context = context;
        this.cartPriceDetails = cartPriceDetails;
    }

    @NonNull
    @Override
    public CartPriceDetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cart_price_details_layout,parent,false);

        return new CartPriceDetailsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartPriceDetailsHolder holder, int position) {
        try {
            holder.cartAmountTitle.setText(cartPriceDetails.get(position).getPrice_title());
            holder.cartAmount.setText(cartPriceDetails.get(position).getPrice_amount());
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public int getItemCount() {
        return cartPriceDetails.size()-1;
    }

    public static class CartPriceDetailsHolder extends ViewHolder {
        TextView cartAmountTitle,cartAmount;
        public CartPriceDetailsHolder(@NonNull View itemView) {
            super(itemView);
            cartAmountTitle = itemView.findViewById(R.id.cartAmountTitle);
            cartAmount = itemView.findViewById(R.id.cartAmount);
        }
    }
}
