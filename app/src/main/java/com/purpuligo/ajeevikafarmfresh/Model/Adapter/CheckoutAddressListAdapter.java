package com.purpuligo.ajeevikafarmfresh.Model.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.purpuligo.ajeevikafarmfresh.Model.Pojo.AddressDetails;
import com.purpuligo.ajeevikafarmfresh.R;

import java.util.ArrayList;

public class CheckoutAddressListAdapter extends RecyclerView.Adapter<CheckoutAddressListAdapter.CheckoutAddressListViewHolder>{

    private String TAG = "CheckoutAddressListAdapter";
    private Context context;
    private ArrayList<AddressDetails> addressList;
    private String addressId;
    private String firstName;
    private String lastName;
    private String address;
    private String landmark;
    private String city;
    private String state;
    private String postalCode;
    private String mobile;
    private int mCheckedPostion = -1;

    public CheckoutAddressListAdapter(Context context, ArrayList<AddressDetails> addressList) {
        this.context = context;
        this.addressList = addressList;
    }

    @NonNull
    @Override
    public CheckoutAddressListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.checkout_address_list_layout,parent,false);

        return new CheckoutAddressListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CheckoutAddressListViewHolder holder, final int position) {
        holder.customerFirstName.setText(addressList.get(position).getFirstname());
        holder.customerLastName.setText(addressList.get(position).getLastname());
        holder.customerAddress.setText(addressList.get(position).getAddress_1()+" , "+addressList.get(position).getAddress_2());
        //holder.customerLandmark.setText(addressList.get(position).getAddress_2());
        holder.customerState.setText(addressList.get(position).getState());
        holder.customerPostalCode.setText(addressList.get(position).getPostcode());
        holder.customerMobileNumber.setText(addressList.get(position).getMobile_no());
        holder.customerCity.setText(addressList.get(position).getCity());

        //check checkbox and uncheck previous selected button
        holder.addressCheckbox.setChecked(position == mCheckedPostion);
        holder.addressCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == mCheckedPostion) {
                    holder.addressCheckbox.setChecked(false);
                    mCheckedPostion = -1;
                } else {
                    //Log.d(TAG, "onClick: "+addressList.get(position).getId_shipping_address());
                    addressId = addressList.get(position).getId_shipping_address();
                    firstName = addressList.get(position).getFirstname();
                    lastName = addressList.get(position).getLastname();
                    address = addressList.get(position).getAddress_1();
                    landmark = addressList.get(position).getAddress_2();
                    city = addressList.get(position).getCity();
                    state = addressList.get(position).getState();
                    postalCode = addressList.get(position).getPostcode();
                    mobile = addressList.get(position).getMobile_no();
                    Log.d(TAG, "onClick: "+addressId);
                    mCheckedPostion = position;
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public static class CheckoutAddressListViewHolder extends RecyclerView.ViewHolder {
        TextView customerFirstName,customerLastName,customerAddress,customerLandmark,customerState,customerPostalCode,customerMobileNumber,customerCity;
        LinearLayout checkoutAddressLayout;
        CheckBox addressCheckbox;

        public CheckoutAddressListViewHolder(@NonNull View itemView) {
            super(itemView);
            checkoutAddressLayout = itemView.findViewById(R.id.checkoutAddressLayout);
            addressCheckbox = itemView.findViewById(R.id.addressCheckbox);
            customerFirstName = itemView.findViewById(R.id.customerFirstName);
            customerLastName = itemView.findViewById(R.id.customerLastName);
            customerAddress = itemView.findViewById(R.id.customerAddress);
            //customerLandmark = itemView.findViewById(R.id.customerLandmark);
            customerState = itemView.findViewById(R.id.customerState);
            customerPostalCode = itemView.findViewById(R.id.customerPostalCode);
            customerMobileNumber = itemView.findViewById(R.id.customerMobileNumber);
            customerCity = itemView.findViewById(R.id.customerCity);
        }
    }

    public String showAddressId(){
        return addressId;
    }

    public String showFirstName(){
        return firstName;
    }

    public String showLastName(){
        return lastName;
    }

    public String showAddress(){
        return address;
    }

    public String showLandmark(){
        return landmark;
    }

    public String showCity(){
        return city;
    }

    public String showState(){
        return state;
    }

    public String showPostalCode(){
        return postalCode;
    }

    public String showMobile(){
        return mobile;
    }
}
