package com.majavrella.bloodfactory.user;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.base.UserFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class DonarList extends UserFragment {

    View DirectDonarList;
    private ListView listView;

    public static DonarList newInstance() {
        return new DonarList();
    }


    public DonarList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DirectDonarList = inflater.inflate(R.layout.list_item, container, false);
        //listView = (ListView)DirectDonarList.findViewById(R.id.list_view_all);
        return DirectDonarList;
    }

    @Override
    protected String getTitle() {
        return "List of Donars";
    }
}
