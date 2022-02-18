package com.example.ddwikidatabase;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchRunnable implements Runnable{    //Takes in activity and uses its context to make the parsing run on the UI thread and load the names into a list
    private boolean running = false;
    private Activity activity;
    private ArrayList<ListObjects> objectsArrayList;
    String oldInput;

    public ArrayList<ListObjects> getObjectsArrayList() {
        return objectsArrayList;
    }

    public SearchRunnable(Activity activity){
        this.activity = activity;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void run() {
        while(true) {
            if (running) {
                listParse();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void startStop(){
        running = !running;
    }

    public void listParse() {
        EditText input =(EditText) activity.findViewById(R.id.search_bar);
        if(!String.valueOf(input.getText()).equals(oldInput)){

            RequestQueue requestQueue = VolleySingleton.getInstance(activity).getRequestQueue();
            oldInput = String.valueOf(input.getText()); ///oldInput is used to check if theres a new input inserted in the search bar so it doesn't constantly refresh the list and send back the user to the top
            ListView listView = activity.findViewById(R.id.searchList);

            String inputUrlSearch = MainActivity.url + String.valueOf(input.getText());    //Adding input search to the url

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, inputUrlSearch, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("results");
                        objectsArrayList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject resultObject = jsonArray.getJSONObject(i);
                            objectsArrayList.add(new ListObjects(resultObject.getString("name"), resultObject.getString("url")));
                        }
                        ListObjectsAdapter listObjectsAdapter = new ListObjectsAdapter(objectsArrayList);
                        listView.setAdapter(listObjectsAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            requestQueue.add(request);
        }
    }


}
