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
import android.widget.ImageView;
import android.widget.TextView;

import com.purpuligo.ajeevikafarmfresh.Constants.Constants;
import com.purpuligo.ajeevikafarmfresh.Model.CartInteractorImpl;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.CartItemList;
import com.purpuligo.ajeevikafarmfresh.Presenter.CartPresenter;
import com.purpuligo.ajeevikafarmfresh.R;
import com.purpuligo.ajeevikafarmfresh.View.CartView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private String TAG = "CartAdapter";
    private Context context;
    private ArrayList<CartItemList> cartItemArrayList;
    private CartPresenter cartPresenter;
    private String customer_email;
    private String session_data;
    //ArrayList list;
    //private Integer[] images = {R.drawable.order,R.drawable.order_two};

    public CartAdapter(Context context, ArrayList<CartItemList> cartItemArrayList) {
        this.context = context;

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.USER_SESSION_MANAGEMENT.KEY_PREFER_NAME,MODE_PRIVATE);
        customer_email = sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"");
        session_data = sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_SESSION_DATA,"");

        cartPresenter = new CartInteractorImpl((CartView) context);
        this.cartItemArrayList = cartItemArrayList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cart_item_layout,parent,false);

        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder holder, final int position) {
        try {
            holder.productName.setText(cartItemArrayList.get(position).getTitle());
            holder.productPrice.setText(cartItemArrayList.get(position).getPrice());
            holder.productType.setText(cartItemArrayList.get(position).getProduct_items());
            holder.productQuantity.setText(cartItemArrayList.get(position).getQuantity().replace(".00",""));
            Picasso.get().load(cartItemArrayList.get(position).getImages()).into(holder.cartImage);
            Log.d(TAG, "onBindViewHolder: "+cartItemArrayList.get(position).getImages());
            holder.removeCartItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeAlert(position);
                }
            });
        }catch (NumberFormatException e){e.printStackTrace();}catch (Exception e){e.printStackTrace();}
    }

    @Override
    public int getItemCount() {
        return cartItemArrayList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        TextView productName,productPrice,productQuantity,productType;
        ImageView cartImage;
        ImageView removeCartItem;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            cartImage = itemView.findViewById(R.id.cartImage);
            removeCartItem = itemView.findViewById(R.id.removeCartItem);
            productType = itemView.findViewById(R.id.productType);
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
                try{
                    cartPresenter.removeCartItem(Integer.parseInt(cartItemArrayList.get(position).getProduct_id()),
                        (int)Double.parseDouble(cartItemArrayList.get(position).getQuantity()),
                        customer_email,Integer.parseInt(cartItemArrayList.get(position).getId_product_attribute()),
                        session_data);
                    Log.d(TAG, "onClick: "+Integer.parseInt(cartItemArrayList.get(position).getProduct_id())+"/"+
                            (int)Double.parseDouble(cartItemArrayList.get(position).getQuantity())+"/"+customer_email+"/"+
                            Integer.parseInt(cartItemArrayList.get(position).getId_product_attribute())+"/"+session_data);}catch (Exception e){e.printStackTrace();}
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
