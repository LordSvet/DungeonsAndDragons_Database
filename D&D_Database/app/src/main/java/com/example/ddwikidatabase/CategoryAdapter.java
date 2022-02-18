package com.example.ddwikidatabase;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class CategoryAdapter extends BaseExpandableListAdapter {        //Custom adapter for the main layout with the ExpendableTextView
    ArrayList<String> parentList;
    HashMap<String, ArrayList<String>> childList;

    CategoryAdapter(ArrayList<String> parentList, HashMap<String, ArrayList<String>> childList){
        this.parentList = parentList;
        this.childList = childList;
    }

    @Override
    public int getGroupCount() {
        return parentList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return childList.get(parentList.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return parentList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return childList.get(parentList.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate
                                  (android.R.layout.simple_expandable_list_item_1,
                                  viewGroup, false);

        TextView tv1 = view.findViewById(android.R.id.text1);
        tv1.setText(String.valueOf(getGroup(i)));
        tv1.setTypeface(null, Typeface.BOLD);
        tv1.setTextColor(Color.WHITE);

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate
                                  (android.R.layout.simple_selectable_list_item,
                                   viewGroup,false);
        TextView tv1 = view.findViewById(android.R.id.text1);
        tv1.setText(String.valueOf(getChild(i, i1)));
        tv1.setTypeface(null, Typeface.ITALIC);
        tv1.setTextColor(Color.WHITE);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


}
