package com.purpuligo.pcweb.Model.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.purpuligo.pcweb.Activity.ProductListActivity;
import com.purpuligo.pcweb.Model.Pojo.ProductCategoriesDetails;
import com.purpuligo.pcweb.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    Context context;
    private ArrayList<ProductCategoriesDetails> restaurantData;
    private Activity activity;

    public RestaurantAdapter(Context context, ArrayList<ProductCategoriesDetails> restaurantData) {
        this.context = context;
        this.restaurantData = restaurantData;

        //activity = new Activity();
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_view_layout,parent,false);

        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, final int position) {
        try{
            holder.textView.setText(restaurantData.get(position).getCategory_name());
            Picasso.get().load(restaurantData.get(position).getCategory_image()).into(holder.rawFishImage);
            holder.productType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,ProductListActivity.class);
                    intent.putExtra("category_id",restaurantData.get(position).getCategory_id());
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            });
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public int getItemCount() {
        return restaurantData.size();
    }

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        LinearLayout productType;
        ImageView rawFishImage;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
            productType = itemView.findViewById(R.id.productType);
            rawFishImage = itemView.findViewById(R.id.rawFishImage);
        }
    }
}
