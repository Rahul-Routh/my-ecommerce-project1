package com.purpuligo.ajeevikafarmfresh.Model.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.purpuligo.ajeevikafarmfresh.Model.Pojo.CartItemList;
import com.purpuligo.ajeevikafarmfresh.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderHistoryDetailsAdapter extends RecyclerView.Adapter<OrderHistoryDetailsAdapter.OrderHistoryDetailsViewHolder> {

    private Context context;
    private ArrayList<CartItemList> orderHistoryProductList;

    public OrderHistoryDetailsAdapter(Context context, ArrayList<CartItemList> orderHistoryProductList) {
        this.context = context;
        this.orderHistoryProductList = orderHistoryProductList;
    }

    @NonNull
    @Override
    public OrderHistoryDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.order_summery_recycler_layout,viewGroup,false);

        return new OrderHistoryDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryDetailsViewHolder holder, int position) {
        try {
            holder.productName.setText(orderHistoryProductList.get(position).getTitle());
            holder.productPrice.setText(orderHistoryProductList.get(position).getPrice());
            holder.productQuantity.setText(orderHistoryProductList.get(position).getQuantity().replace(".00",""));
            holder.foodTest.setText(orderHistoryProductList.get(position).getProduct_items());
            Picasso.get().load(orderHistoryProductList.get(position).getImages()).into(holder.cartImage);
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public int getItemCount() {
        return orderHistoryProductList.size();
    }

    public static class OrderHistoryDetailsViewHolder extends RecyclerView.ViewHolder {
        ImageView cartImage;
        TextView productName,productPrice,productQuantity,foodTest;
        public OrderHistoryDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            cartImage = itemView.findViewById(R.id.cartImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            foodTest = itemView.findViewById(R.id.foodTest);
        }
    }
}
