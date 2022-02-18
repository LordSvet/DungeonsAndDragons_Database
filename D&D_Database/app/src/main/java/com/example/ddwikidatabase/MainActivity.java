package com.example.ddwikidatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    static String url = "https://www.dnd5eapi.co/api/"; //url for the api from where the app extracts the data via JSON parsing
    static String categoryUrl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExpandableListView expView = findViewById(R.id.exp_main_list);  //Creating ExpandableList and also an ArrayList and HashMap
        ArrayList<String> parentList = new ArrayList<>();
        HashMap<String, ArrayList<String>> childList = new HashMap<>();

        ArrayRepository.listArray.add(ArrayRepository.characterData);       //Inserting arrays in the listArray from Repository class
        ArrayRepository.listArray.add(ArrayRepository.classes);
        ArrayRepository.listArray.add(ArrayRepository.races);
        ArrayRepository.listArray.add(ArrayRepository.equipment);
        ArrayRepository.listArray.add(ArrayRepository.gameMechanics);
        ArrayRepository.listArray.add(ArrayRepository.rules);

        for(int i = 0; i < ArrayRepository.parentArray.length; i++) {   //Sets up the hash map and fills it with the all the parent and child groups of the expandable list
            parentList.add(ArrayRepository.parentArray[i]);
            ArrayList<String> specificChildArray = new ArrayList<>();
            if(i <= 5) {
                specificChildArray.addAll(Arrays.asList(ArrayRepository.listArray.get(i)));
            }
            childList.put(parentList.get(i), specificChildArray);
        }
        CategoryAdapter adapter = new CategoryAdapter(parentList, childList);

        expView.setAdapter(adapter);

        expView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {   //Collapses group if another one is opened.
            @Override
            public void onGroupExpand(int i) {
                for(int j = 0; j < parentList.size(); j++){
                    if(j != i){
                        expView.collapseGroup(j);
                    }
                }
            }
        });
        expView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() { //OnClickListener for the children of the groups
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int parentPosition, int childPosition, long id) {
                url = "https://www.dnd5eapi.co/api/";   //Resetting the url
                String selectedChild = childList.get(parentList.get(parentPosition)).get(childPosition).toLowerCase();
                selectedChild = selectedChild.replaceAll("\\s","-");
                url += selectedChild + "/?name=";
                categoryUrl = "https://www.dnd5eapi.co/api/" + selectedChild;

                Intent intentSearch = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intentSearch);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                return true;
            }
        });
        expView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int position, long id) {  //Method for the groups with no children
                if(position>5) {
                    url = "https://www.dnd5eapi.co/api/";   //Resetting the url
                    String selectedParent = parentList.get(position).toLowerCase();
                    url += selectedParent + "/?name=";
                    categoryUrl = "https://www.dnd5eapi.co/api/" + selectedParent;
                    Intent intentSearch = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(intentSearch);
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

                    return true;
                }
                return false;
            }
        });
    }



}