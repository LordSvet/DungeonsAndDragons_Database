package com.example.ddwikidatabase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListObjectsAdapter extends BaseAdapter {       //Custom adapter for the list of items in the SearchActivity
    private ArrayList<ListObjects> objectsArrayList;

    public ListObjectsAdapter(ArrayList<ListObjects> objectsArrayList) {
        this.objectsArrayList = objectsArrayList;
    }

    public ArrayList<ListObjects> getObjectsArrayList() {
        return objectsArrayList;
    }

    public void setObjectsArrayList(ArrayList<ListObjects> objectsArrayList) {
        this.objectsArrayList = objectsArrayList;
    }

    @Override
    public int getCount() {
        return objectsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return objectsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();
        ListObjects listObjectEntry = (ListObjects) getItem(position);

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_names, null, false);
        }
        TextView namesList = view.findViewById(R.id.names_list_view);
        namesList.setText(listObjectEntry.getName());

        return view;
    }
}
