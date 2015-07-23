package com.skykhan.zikirmatik;

/**
 * Created by GÖKHAN on 15/07/2015.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("unchecked")
public class ZikirAdapter extends BaseExpandableListAdapter {

    public ArrayList<HashMap<String,String>> grupList, tempDetay;
    public ArrayList<ArrayList<HashMap<String,String>>> detayList = new ArrayList<>();
    public LayoutInflater inflater;
    public Activity activity;

    public ZikirAdapter(ArrayList<HashMap<String,String>> grupList, ArrayList<ArrayList<HashMap<String,String>>> detayList) {
        this.grupList = grupList;
        this.detayList = detayList;
    }

    public void setInflater(LayoutInflater inflater, Activity activity) {
        this.inflater = inflater;
        this.activity = activity;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        tempDetay = detayList.get(groupPosition);
        TextView text = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.detay_satir, null);
        }
        text = (TextView) convertView.findViewById(R.id.textView1);
        text.setText(tempDetay.get(childPosition).get("ADI"));
        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, tempDetay.get(childPosition).get("ADI"),
                        Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return detayList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public int getGroupCount() {
        return detayList.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grup_satir, null);
        }
        ((CheckedTextView) convertView).setText(grupList.get(groupPosition).get("ADI"));
        ((CheckedTextView) convertView).setChecked(isExpanded);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}

