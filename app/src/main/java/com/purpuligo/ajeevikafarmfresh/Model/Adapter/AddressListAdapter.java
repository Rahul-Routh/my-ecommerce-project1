package com.purpuligo.ajeevikafarmfresh.Model.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.purpuligo.ajeevikafarmfresh.Activity.EditUserDetailsActivity;
import com.purpuligo.ajeevikafarmfresh.Constants.Constants;
import com.purpuligo.ajeevikafarmfresh.Model.AddressListInteractorImpl;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.AddressDetails;
import com.purpuligo.ajeevikafarmfresh.Presenter.AddressListPresenter;
import com.purpuligo.ajeevikafarmfresh.R;
import com.purpuligo.ajeevikafarmfresh.View.AddressListView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.AddressListViewHolder> {

    private String TAG = "AddressListAdapter";
    private Context context;
    private ArrayList<AddressDetails> addressList;
    private AddressListPresenter addressListPresenter;
    private String customer_email;

    public AddressListAdapter(Context context, ArrayList<AddressDetails> addressList) {
        this.context = context;
        this.addressList = addressList;

        addressListPresenter = new AddressListInteractorImpl((AddressListView) context);
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.USER_SESSION_MANAGEMENT.KEY_PREFER_NAME,MODE_PRIVATE);
        customer_email = sharedPreferences.getString(Constants.USER_SESSION_MANAGEMENT.KEY_CUSTOMER_EMAIL,"");
    }

    @NonNull
    @Override
    public AddressListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.address_list_layout,parent,false);

        return new AddressListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressListViewHolder holder, final int position) {
        holder.customerFirstName.setText(addressList.get(position).getFirstname());
        holder.customerLastName.setText(addressList.get(position).getLastname());
        holder.customerAddress.setText(addressList.get(position).getAddress_1() +" , "+ addressList.get(position).getAddress_2());
        //holder.customerLandmark.setText(addressList.get(position).getAddress_2());
        holder.customerState.setText(addressList.get(position).getState());
        holder.customerPostalCode.setText(addressList.get(position).getPostcode());
        holder.customerMobileNumber.setText(addressList.get(position).getMobile_no());
        holder.customerCity.setText(addressList.get(position).getCity());

        holder.editAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editUserDetails = new Intent(context, EditUserDetailsActivity.class);
                editUserDetails.putExtra(Constants.UPDATE_USER_DETAIL.KEY_FIRST_NAME,addressList.get(position).getFirstname());
                editUserDetails.putExtra(Constants.UPDATE_USER_DETAIL.KEY_LAST_NAME,addressList.get(position).getLastname());
                editUserDetails.putExtra(Constants.UPDATE_USER_DETAIL.KEY_PHONE,addressList.get(position).getMobile_no());
                editUserDetails.putExtra(Constants.UPDATE_USER_DETAIL.KEY_ADDRESS,addressList.get(position).getAddress_1());
                editUserDetails.putExtra(Constants.UPDATE_USER_DETAIL.KEY_LANDMARK,addressList.get(position).getAddress_2());
                editUserDetails.putExtra(Constants.UPDATE_USER_DETAIL.KEY_CITY,addressList.get(position).getCity());
                editUserDetails.putExtra(Constants.UPDATE_USER_DETAIL.KEY_POSTAL_CODE,addressList.get(position).getPostcode());
                editUserDetails.putExtra(Constants.UPDATE_USER_DETAIL.KEY_ADDRESS_ID,addressList.get(position).getId_shipping_address());
                context.startActivity(editUserDetails);
            }
        });

        holder.removeAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeAlert(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public static class AddressListViewHolder extends RecyclerView.ViewHolder {

        TextView customerFirstName,customerLastName,customerAddress,customerLandmark,customerState,customerPostalCode,customerMobileNumber,customerCity;
        Button removeAddressBtn,editAddressBtn;

        public AddressListViewHolder(@NonNull View itemView) {
            super(itemView);
            customerFirstName = itemView.findViewById(R.id.customerFirstName);
            customerLastName = itemView.findViewById(R.id.customerLastName);
            customerAddress = itemView.findViewById(R.id.customerAddress);
            //customerLandmark = itemView.findViewById(R.id.customerLandmark);
            customerState = itemView.findViewById(R.id.customerState);
            customerPostalCode = itemView.findViewById(R.id.customerPostalCode);
            customerMobileNumber = itemView.findViewById(R.id.customerMobileNumber);
            removeAddressBtn = itemView.findViewById(R.id.removeAddressBtn);
            editAddressBtn = itemView.findViewById(R.id.editAddressBtn);
            customerCity = itemView.findViewById(R.id.customerCity);
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
                try {
                    addressListPresenter.removeAddress(customer_email,(int)Double.parseDouble(addressList.get(position).getId_shipping_address()));
                    Log.d(TAG, "onClick: "+customer_email+"-"+(int)Double.parseDouble(addressList.get(position).getId_shipping_address()));
                }catch (Exception e){e.printStackTrace();}
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
