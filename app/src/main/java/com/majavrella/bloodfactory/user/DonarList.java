package com.majavrella.bloodfactory.user;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.base.UserFragment;

import org.json.JSONArray;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class DonarList extends UserFragment {

    private static View mDonarList;
    private static JSONArray mDonarsListArray;
    @Bind(R.id.list_container) LinearLayout mListContainer;

    public static DonarList newInstance(JSONArray jsonArray) {
        mDonarsListArray = jsonArray;
        return new DonarList();
    }


    public DonarList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDonarList = inflater.inflate(R.layout.fragment_donar_list, container, false);
        ButterKnife.bind(this, mDonarList);
        progress.setMessage("Displaying result...");
        progress.show();
        createListOfDonars(mDonarsListArray);
        return mDonarList;
    }

    private void createListOfDonars(JSONArray mDonarsListArray) {
        for(int i=0; i<mDonarsListArray.length(); i++){
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.list_item, null);
            TextView bloodGroup = (TextView)view.findViewById(R.id.blood_group);
            TextView status = (TextView)view.findViewById(R.id.status);
            TextView name = (TextView)view.findViewById(R.id.name);
            TextView gender = (TextView)view.findViewById(R.id.gender);
            TextView city = (TextView)view.findViewById(R.id.city);
            TextView state = (TextView)view.findViewById(R.id.state);
            TextView phone = (TextView)view.findViewById(R.id.phone_no);
            TextView more = (TextView)view.findViewById(R.id.more);
            ImageView call = (ImageView) view.findViewById(R.id.call);
            ImageView sms = (ImageView) view.findViewById(R.id.sms);
            ImageView share = (ImageView) view.findViewById(R.id.share);
            ImageView about = (ImageView) view.findViewById(R.id.about);
            final LinearLayout moreContainer = (LinearLayout) view.findViewById(R.id.more_container);
            mListContainer.addView(view, i);
            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(moreContainer.getVisibility()==View.VISIBLE){
                        moreContainer.setVisibility(View.GONE);
                    } else {
                        moreContainer.setVisibility(View.VISIBLE);
                    }
                }
            });

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            sms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            about.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }

    @Override
    protected String getTitle() {
        return "List of Donars";
    }
}
