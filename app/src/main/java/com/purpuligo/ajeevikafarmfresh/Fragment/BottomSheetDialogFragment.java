package com.purpuligo.ajeevikafarmfresh.Fragment;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.purpuligo.ajeevikafarmfresh.Model.HomeInteractorImpl;
import com.purpuligo.ajeevikafarmfresh.Model.Pojo.ProductCategoriesDetails;
import com.purpuligo.ajeevikafarmfresh.Presenter.HomePresenter;
import com.purpuligo.ajeevikafarmfresh.R;
import com.purpuligo.ajeevikafarmfresh.View.HomeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class BottomSheetDialogFragment extends com.google.android.material.bottomsheet.BottomSheetDialogFragment {

    private static final String TAG = "BottomSheetDialogFrag";
    @BindView(R.id.category_recyclerView_list) RecyclerView category_recyclerView_list;
    private ArrayList<ProductCategoriesDetails> productCategoriesDetailsList;
    private HomePresenter homePresenter;

    public BottomSheetDialogFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);
        ButterKnife.bind(this,view);
        homePresenter = new HomeInteractorImpl((HomeView) getContext());

        try {
            Log.d(TAG, "onCreateView: "+getArguments().getInt("categoriesPosition"));
            //homePresenter.fetchCategoryDataFromServer(getArguments().getInt("categoriesPosition"));
        }catch (Exception e){e.printStackTrace();}

        return view;
    }

    public void getCategoryDataList(ArrayList<ProductCategoriesDetails> categoriesData){
        try {
            Log.d(TAG, "getData: "+categoriesData);
            category_recyclerView_list.setLayoutManager(new LinearLayoutManager(getContext()));
        }catch (Exception e){e.printStackTrace();}
    }

    public void getCategoryDataListError(){
        Log.d(TAG, "getCategoryDataListError: ");
    }
}
