package com.example.ddwikidatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    EditText input;
    ListView listView;
    ListObjects listObject;
    ArrayList<ListObjects> objectsArrayList;
    SearchRunnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        input =(EditText) findViewById(R.id.search_bar);
        listView = findViewById(R.id.searchList);
        runnable = new SearchRunnable(this);    //Makes the search done on the UI thread
        if(!runnable.isRunning()){runnable.startStop();}   //Sets the boolean to true enabling it
        Thread tr = new Thread(runnable);
        tr.start();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                objectsArrayList = runnable.getObjectsArrayList();
                listObject = new ListObjects(objectsArrayList.get(position).getName(),objectsArrayList.get(position).getUrl());
                MainActivity.url = "https://www.dnd5eapi.co" + listObject.getUrl();
                Intent intent = new Intent(SearchActivity.this, ResultActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                runnable.startStop();

            }
        });
    }

}