package com.purpuligo.pcweb.Model.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.purpuligo.pcweb.Model.Pojo.CartItemList;
import com.purpuligo.pcweb.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CodOrderSummeryAdapter extends RecyclerView.Adapter<CodOrderSummeryAdapter.CodOrderSummeryViewHolder> {

    private String TAG = "CodOrderSummeryAdapter";
    private Context context;
    private ArrayList<CartItemList> cartItemArrayList;
    //ArrayList list;
    //private Integer[] images = {R.drawable.order,R.drawable.order_two};
    //int size = 0;

    public CodOrderSummeryAdapter(Context context, ArrayList<CartItemList> cartItemArrayList) {
        this.context = context;
        this.cartItemArrayList = cartItemArrayList;
    }

    @NonNull
    @Override
    public CodOrderSummeryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.order_summery_recycler_layout,parent,false);

        return new CodOrderSummeryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CodOrderSummeryViewHolder holder, int position) {
        try{
            holder.productName.setText(cartItemArrayList.get(position).getTitle());
            holder.productPrice.setText(cartItemArrayList.get(position).getPrice());
            holder.productQuantity.setText(cartItemArrayList.get(position).getQuantity().replace(".00",""));
            holder.foodTest.setText(cartItemArrayList.get(position).getProduct_items());
            Picasso.get().load(cartItemArrayList.get(position).getImages()).into(holder.cartImage);
            Log.d(TAG, "onBindViewHolder: ");
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public int getItemCount() {
        return cartItemArrayList.size();
    }

    public static class CodOrderSummeryViewHolder extends RecyclerView.ViewHolder {

        TextView productName,productPrice,productQuantity,foodTest;
        ImageView cartImage;

        public CodOrderSummeryViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            cartImage = itemView.findViewById(R.id.cartImage);
            foodTest = itemView.findViewById(R.id.foodTest);
        }
    }
}
