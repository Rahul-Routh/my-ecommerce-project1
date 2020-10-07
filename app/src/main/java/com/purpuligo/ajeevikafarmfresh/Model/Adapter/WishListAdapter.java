package com.purpuligo.ajeevikafarmfresh.Model.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.purpuligo.ajeevikafarmfresh.Constants.Constants;
import com.purpuligo.ajeevikafarmfresh.Global.NetworkState;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.WishListItems;
import com.purpuligo.ajeevikafarmfresh.Model.WishListInteractorImpl;
import com.purpuligo.ajeevikafarmfresh.Presenter.WishListPresenter;
import com.purpuligo.ajeevikafarmfresh.R;
import com.purpuligo.ajeevikafarmfresh.View.WishListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.WishListViewHolder> {

    private String TAG = "WishListAdapter";
    private Context context;
    private ArrayList<WishListItems> wishListArray;
    private WishListPresenter wishListPresenter;
    private NetworkState networkState;
    private String customer_email;

    public WishListAdapter(Context context, ArrayList<WishListItems> wishListArray) {
        this.context = context;
        this.wishListArray = wishListArray;

        wishListPresenter = new WishListInteractorImpl((WishListView) context);
        networkState = new NetworkState();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.USER_SESSION_MANAGEMENT.KEY_PREFER_NAME,MODE_PRIVATE);
        customer_email = sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"");
        Log.d(TAG, "WishListAdapter: "+customer_email);
    }

    @NonNull
    @Override
    public WishListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.wishlist_layout,parent,false);

        return new WishListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishListViewHolder holder, final int position) {
        try{
            holder.productName.setText(wishListArray.get(position).getProduct_title());
            holder.productPrice.setText(wishListArray.get(position).getPrice());
            Picasso.get().load(wishListArray.get(position).getImages()).into(holder.wishListImage);

//            holder.addToCart.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
//                }
//            });

            holder.removeWishListItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeAlert(position);
                }
            });
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public int getItemCount() {
        return wishListArray.size();
    }

    public static class WishListViewHolder extends RecyclerView.ViewHolder {

        TextView productName,productPrice;
        ImageView wishListImage;
        Button addToCart,removeWishListItem;

        public WishListViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            wishListImage = itemView.findViewById(R.id.wishListImage);
            //addToCart = itemView.findViewById(R.id.addToCart);
            removeWishListItem = itemView.findViewById(R.id.removeWishListItem);
        }
    }

    private void removeAlert(final int position){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Remove Item");
        alertDialogBuilder.setMessage("Are you really want to remove.");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (networkState.isNetworkAvailable(context)){
                    wishListPresenter.removeSelectedWishListItem(customer_email,wishListArray.get(position).getProduct_id());
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                wishListArray.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position,wishListArray.size());
                            }catch (Exception e){e.printStackTrace();}
                        }
                    },1000);
                }else {
                    Toast.makeText(context, "Please connect to internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
