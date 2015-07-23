package com.skykhan.zikirmatik;

/**
 * Created by GÖKHAN on 15/07/2015.
 */

import android.app.ExpandableListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class ZikirListesi extends ExpandableListActivity implements
        OnChildClickListener {

    ArrayList<HashMap<String,String>> groupItem = new ArrayList<>();
    ArrayList<ArrayList<HashMap<String,String>>> childItem = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExpandableListView expandbleLis = getExpandableListView();
        expandbleLis.setDividerHeight(2);
        expandbleLis.setGroupIndicator(null);
        expandbleLis.setClickable(true);

        setGroupData();
        setChildGroupData();

        ZikirAdapter exListAdapter = new ZikirAdapter(groupItem, childItem);
        exListAdapter
                .setInflater(
                        (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE),
                        this);
        getExpandableListView().setAdapter(exListAdapter);
        expandbleLis.setOnChildClickListener(this);
    }

    public void setGroupData() {
        //groupItem
    }

    public void setChildGroupData() {
        //childItem
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
                                int groupPosition, int childPosition, long id) {
        Toast.makeText(ZikirListesi.this, "Clicked On Child",
                Toast.LENGTH_SHORT).show();
        return true;
    }
}

