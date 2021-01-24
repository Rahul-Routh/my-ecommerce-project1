package com.purpuligo.pcweb.Model.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.purpuligo.pcweb.Model.Pojo.StateDetails;
import com.purpuligo.pcweb.R;

import java.util.ArrayList;

public class StateDetailsListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<StateDetails> stateDetailsList;
    private String zone_id;

    public StateDetailsListAdapter(Context context, ArrayList<StateDetails> stateDetailsList) {
        this.context = context;
        this.stateDetailsList = stateDetailsList;
    }

    @Override
    public int getCount() {
        return stateDetailsList.size();
    }

    @Override
    public Object getItem(int position) {
        return stateDetailsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.state_details_list,viewGroup,false);

        try {
            TextView stateName = v.findViewById(R.id.stateName);
            stateName.setText(stateDetailsList.get(position).getName());
            zone_id = stateDetailsList.get(position).getZone_id();
        }catch (Exception e){e.printStackTrace();}

        return v;
    }

    public String getZoneId(){
        return zone_id;
    }
}
