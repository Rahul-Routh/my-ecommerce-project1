package com.purpuligo.pcweb.Model.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.purpuligo.pcweb.Activity.OrderHistoryDetailsActivity;
import com.purpuligo.pcweb.Constants.Constants;
import com.purpuligo.pcweb.Global.LoginDialog;
import com.purpuligo.pcweb.Global.NetworkState;
import com.purpuligo.pcweb.Model.Pojo.OrderHistoryDetails;
import com.purpuligo.pcweb.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> {

    private Context context;
    private ArrayList<OrderHistoryDetails> orderHistoryList;
    private SharedPreferences sharedPreferences;
    private NetworkState networkState;
    private LoginDialog loginDialog;

    public OrderHistoryAdapter(Context context, ArrayList<OrderHistoryDetails> orderHistoryList) {
        this.context = context;
        this.orderHistoryList = orderHistoryList;

        networkState = new NetworkState();
        loginDialog = new LoginDialog();
        sharedPreferences = context.getSharedPreferences(Constants.USER_SESSION_MANAGEMENT.KEY_PREFER_NAME,MODE_PRIVATE);
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.order_history_layout,parent,false);

        return new OrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, final int position) {
        try{
            holder.orderId.setText("Order Id : "+orderHistoryList.get(position).getOrder_id());
            holder.totalPrice.setText("Total Price : "+orderHistoryList.get(position).getTotal());
            holder.telephone.setText("Mobile : "+orderHistoryList.get(position).getTelephone());
            holder.orderStatus.setText("Order Status : "+orderHistoryList.get(position).getStatus());
            holder.date.setText("Order Date : "+orderHistoryList.get(position).getDate_added());

            holder.details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (networkState.isNetworkAvailable(context)){
                        if (sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"").length()>0){
                            Intent intent = new Intent(context, OrderHistoryDetailsActivity.class);
                            intent.putExtra("order_id",orderHistoryList.get(position).getOrder_id());
                            context.startActivity(intent);
                        }else {loginDialog.loginDialog(context);}
                    }else { Toast.makeText(context, "Please Check Internet", Toast.LENGTH_SHORT).show(); }
                }
            });

        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public int getItemCount() {
        return orderHistoryList.size();
    }

    public static class OrderHistoryViewHolder extends RecyclerView.ViewHolder {

        TextView orderId,totalPrice,telephone,orderStatus,date;
        Button details;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            orderId = itemView.findViewById(R.id.orderId);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            telephone = itemView.findViewById(R.id.telephone);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            date = itemView.findViewById(R.id.date);
            details = itemView.findViewById(R.id.details);
        }
    }
}
