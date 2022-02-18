package com.example.ddwikidatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    ArrayList<ListObjects> objectsArrayList;
    TextView title;
    TextView description;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        title = findViewById(R.id.resultTitle);
        description = findViewById(R.id.resultDesc);
        description.setTextSize(17);
        description.setPadding(20,10,20,10);
        resultParse();

    }

    public void resultParse(){
        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, MainActivity.url, null, new Response.Listener<JSONObject>() { //MainActivity.url at this point is the specific url for the selected item
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray;
                try {
                    switch(MainActivity.categoryUrl) {
                        case "https://www.dnd5eapi.co/api/ability-scores":
                            title.setText(response.getString("full_name"));
                            jsonArray = response.getJSONArray("desc");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                description.append(jsonArray.getString(i) + "\n\n");
                            }

                            JSONArray skillsArray = response.getJSONArray("skills");        //Appends skills to description TextView
                            if (skillsArray.length() != 0) {
                                description.append("Skills that it's used for: ");
                                int j = jsonArray.length();
                                for (int i = 0; i < skillsArray.length(); i++) {
                                    JSONObject skills = skillsArray.getJSONObject(i);
                                    if (i == skillsArray.length() - 1) {
                                        description.append(skills.getString("name") + ".");
                                        break;
                                    }
                                    description.append(skills.getString("name") + ", ");
                                }
                            }
                            break;

                        case "https://www.dnd5eapi.co/api/skills":
                            title.setText(response.getString("name"));
                            jsonArray = response.getJSONArray("desc");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                description.append(jsonArray.getString(i) + "\n\n");
                            }
                            description.append("Ability Score that is associated: " + response.getJSONObject("ability_score").getString("name") + ".");
                            break;

                        case "https://www.dnd5eapi.co/api/proficiencies":
                            if (response.getJSONArray("classes").length() <= 0 && response.getJSONArray("races").length() <= 0) {
                                title.setText(response.getString("name"));
                                description.append("No classes nor races have proficiency with " + response.getString("name"));
                            }
                            if (response.getJSONArray("classes").length() > 0) {
                                description.append("Category: " + response.getString("type") + "\n\n");
                                jsonArray = response.getJSONArray("classes");
                                description.append("Classes that start with this proficiency: ");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject classObject = jsonArray.getJSONObject(i);
                                    if (i == jsonArray.length() - 1) {
                                        description.append(classObject.getString("name") + ".\n\n");
                                        break;
                                    }
                                    description.append(classObject.getString("name") + ", ");
                                }
                            }
                            if (response.getJSONArray("races").length() > 0) {
                                description.append("Races that start with this proficiency: ");
                                jsonArray = response.getJSONArray("races");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject racesObject = jsonArray.getJSONObject(i);
                                    if (i == jsonArray.length() - 1) {
                                        description.append(racesObject.getString("name") + ".\n\n");
                                        break;
                                    }
                                    description.append(racesObject.getString("name") + ", ");
                                }
                            }
                            break;

                        case "https://www.dnd5eapi.co/api/languages":
                            title.setText(response.getString("name"));
                            description.append("Type of language: " + response.getString("type") + "\n\nTypical speakers: ");
                            jsonArray = response.getJSONArray("typical_speakers");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                if (i == jsonArray.length() - 1) {
                                    description.append(jsonArray.getString(i) + ".\n\nScript used for writing in this language: " + response.getString("script"));
                                    break;
                                }
                                description.append(jsonArray.getString(i) + ", ");
                            }
                            break;

                        case "https://www.dnd5eapi.co/api/alignments":

                        case "https://www.dnd5eapi.co/api/rule-sections":

                        case "https://www.dnd5eapi.co/api/magic-schools":
                            title.setText(response.getString("name"));
                            description.append(response.getString("desc") + "\n\n");
                            break;

                        case "https://www.dnd5eapi.co/api/classes":
                            title.setText(response.getString("name"));
                            description.append("Hit Die: 1d" + response.getInt("hit_die"));
                            jsonArray = response.getJSONArray("proficiency_choices");
                            description.append("\n\nStarting proficiencies: \n");
                            JSONArray proficiencies = response.getJSONArray("proficiencies");   //List of given proficiencies
                            for (int i = 0; i < proficiencies.length(); i++) {
                                JSONObject profObject = proficiencies.getJSONObject(i);
                                if (i == proficiencies.length() - 1) {
                                    description.append(profObject.getString("name") + ".\n\n");
                                    break;
                                }
                                description.append(profObject.getString("name") + ", ");
                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject choices = jsonArray.getJSONObject(i);    //List of selectable proficiencies
                                description.append("Choose " + choices.getInt("choose") + " of these proficiencies: \n");
                                JSONArray choicesArray = choices.getJSONArray("from");
                                for (int j = 0; j < choicesArray.length(); j++) {
                                    JSONObject proficiencyObject = choicesArray.getJSONObject(j);
                                    if (j == choicesArray.length() - 1) {
                                        description.append("\t~" + proficiencyObject.getString("name") + "\n\n");
                                        break;
                                    }
                                    description.append("\t~" + proficiencyObject.getString("name") + "\n");
                                }
                            }
                            description.append("Saving throws that the class is proficient in: ");      //Saving throws proficiency
                            for (int i = 0; i < response.getJSONArray("saving_throws").length(); i++) {
                                JSONObject savingThrows = response.getJSONArray("saving_throws").getJSONObject(i);
                                if (i == response.getJSONArray("saving_throws").length() - 1) {
                                    description.append(savingThrows.getString("name") + ".\n\n");
                                    break;
                                }
                                description.append(savingThrows.getString("name") + ", ");
                            }
                            try {
                                description.append("Starting equipment: \n\n");
                                for (int i = 0; i < response.getJSONArray("starting_equipment").length(); i++) {
                                    JSONObject startingEquipment = response.getJSONArray("starting_equipment").getJSONObject(i);
                                    description.append("\t" + startingEquipment.getInt("quantity") + "x "
                                            + startingEquipment.getJSONObject("equipment").getString("name") + ".\n");
                                }
                                for (int i = 0; i < response.getJSONArray("starting_equipment_options").length(); i++) {     //Choosable equipment
                                    JSONObject startingEquipmentOptions = response.getJSONArray("starting_equipment_options").getJSONObject(i);
                                    if(response.getString("name").equals("Cleric") || response.getString("name").equals("Druid") && i >= 2) { //Fixing Minor problem when class Cleric is selected
                                        break;
                                    }
                                    description.append("\n\tChoose " + startingEquipmentOptions.getString("choose") + " from: ");


                                    for (int j = 0; j < startingEquipmentOptions.getJSONArray("from").length(); j++) {
                                        if (startingEquipmentOptions.getJSONArray("from").getJSONObject(j).has("equipment_option")) {
                                            if (j == startingEquipmentOptions.getJSONArray("from").length() - 1) {
                                                description.append(startingEquipmentOptions.getJSONArray("from").getJSONObject(j).getJSONObject("equipment_option").getJSONObject("from").getJSONObject("equipment_category").getString("name") + ".\n");
                                                break;
                                            }
                                            description.append(startingEquipmentOptions.getJSONArray("from").getJSONObject(j).getJSONObject("equipment_option").getString("name") + ", ");
                                        } else {

                                            if (j == startingEquipmentOptions.getJSONArray("from").length() - 1) {
                                                description.append(startingEquipmentOptions.getJSONArray("from").getJSONObject(j).getJSONObject("equipment").getString("name") + ".\n");
                                                break;
                                            }
                                            description.append(startingEquipmentOptions.getJSONArray("from").getJSONObject(j).getJSONObject("equipment").getString("name") + ", ");
                                        }
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            if (response.getJSONObject("spellcasting").length() != 0) {                                //Spellcasting
                                description.append("\nSpellcasting for this class begins at level " + response.getJSONObject("spellcasting").getInt("level") + ".\n\n");
                                description.append("Spellcasting ability for this class is " + response.getJSONObject("spellcasting").getJSONObject("spellcasting_ability").getString("name")
                                        + ".\n\nAdditional spellcasting info: \n\n");
                                JSONArray classSpellsArray = response.getJSONObject("spellcasting").getJSONArray("info");
                                for (int i = 0; i < classSpellsArray.length(); i++) {
                                    description.append(classSpellsArray.getJSONObject(i).getString("name") + ": ");
                                    for (int j = 0; j < classSpellsArray.getJSONObject(i).getJSONArray("desc").length(); j++) {
                                        if (j == classSpellsArray.getJSONObject(i).getJSONArray("desc").length() - 1) {
                                            description.append(classSpellsArray.getJSONObject(i).getJSONArray("desc").getString(j) + "\n\n");
                                            break;
                                        }
                                        description.append(classSpellsArray.getJSONObject(i).getJSONArray("desc").getString(j));
                                    }

                                }
                            }

                            break;

                        case "https://www.dnd5eapi.co/api/subclasses":
                            title.setText(response.getString("name"));
                            description.append("Parent class: " + response.getJSONObject("class").getString("name") + "\n\n");
                            jsonArray = response.getJSONArray("desc");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                description.append(jsonArray.getString(i) + "\n\n");
                            }
                            if (response.getJSONArray("spells").length() > 0) {
                                description.append("Spells and prerequisites(class and level needed to unlock): \n\n");
                                jsonArray = response.getJSONArray("spells");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject spellsObject = jsonArray.getJSONObject(i);
                                    description.append("~" + spellsObject.getJSONObject("spell").getString("name") + "(Prerequisites: ");
                                    for (int j = 0; j < spellsObject.getJSONArray("prerequisites").length(); j++) {
                                        description.append(spellsObject.getJSONArray("prerequisites").getJSONObject(j).getString("name"));
                                    }
                                    description.append(")\n\n");
                                }
                            }

                            break;

                        case "https://www.dnd5eapi.co/api/features":
                            title.setText(response.getString("name"));
                            description.append("Class that gains this feature: " + response.getJSONObject("class").getString("name") + "\n\n");
                            description.append(response.getJSONObject("class").getString("name") + " unlocks at level " + response.getInt("level") + "\n\n");
                            jsonArray = response.getJSONArray("desc");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                description.append(jsonArray.getString(i) + "\n\n");
                            }
                            break;

                        case "https://www.dnd5eapi.co/api/races":
                            title.setText(response.getString("name"));
                            description.append("Speed: " + response.getString("speed") + " ft." + "\n\n");
                            jsonArray = response.getJSONArray("ability_bonuses");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                description.append("Your " + jsonArray.getJSONObject(i).getJSONObject("ability_score").getString("name") + " is increased by " + jsonArray.getJSONObject(i).getInt("bonus") + "\n");
                            }
                            description.append("\nRecommended alignment/s for this race: " + response.getString("alignment") + "\n\n");
                            description.append("Recommended age for this race: " + response.getString("age") + "\n\n");
                            description.append("Size category: " + response.getString("size") + "\n\n");
                            description.append("Recommended size for this race: " + response.getString("size_description") + "\n\n");
                            jsonArray = response.getJSONArray("starting_proficiencies");
                            if (jsonArray.length() > 0) {
                                description.append("Starting proficiencies for this race: \n");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject profObject = jsonArray.getJSONObject(i);
                                    description.append("\t~" + profObject.getString("name") + "\n");
                                }
                            }
                            if (response.has("starting_proficiency_options")) {
                                JSONObject profOptions = response.getJSONObject("starting_proficiency_options");
                                jsonArray = profOptions.getJSONArray("from");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject choices = jsonArray.getJSONObject(i);
                                    description.append("Choose " + jsonArray.getJSONObject(i).getInt("choose") + " of these proficiencies: ");
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        JSONObject proficiencyObject = jsonArray.getJSONObject(j);
                                        if (j == jsonArray.length() - 1) {
                                            description.append(proficiencyObject.getString("name") + ".\n\n");
                                            break;
                                        }
                                        description.append(proficiencyObject.getString("name") + ", ");
                                    }
                                }
                            }
                            description.append("\n" + response.getString("language_desc") + "\n\n");   //Languages it can speak
                            if (response.getJSONArray("traits").length() > 0) {
                                description.append("Racial traits that provide benefits to its members: \n");
                                jsonArray = response.getJSONArray("traits");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject traitObject = jsonArray.getJSONObject(i);
                                    description.append("\t~" + traitObject.getString("name") + "\n");
                                }
                            }
                            if (response.getJSONArray("subraces").length() > 0) {
                                description.append("\nAll possible subraces that this race includes: ");
                                jsonArray = response.getJSONArray("subraces");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject traitObject = jsonArray.getJSONObject(i);
                                    if (i == jsonArray.length() - 1) {
                                        description.append(traitObject.getString("name") + ".\n\n");
                                        break;
                                    }
                                    description.append(traitObject.getString("name") + ", ");
                                }
                            }
                            break;

                        case "https://www.dnd5eapi.co/api/subraces":
                            title.setText(response.getString("name"));
                            description.append("Parent race: " + response.getJSONObject("race").getString("name") + "\n\n");
                            description.append(response.getString("desc") + "\n\n");
                            jsonArray = response.getJSONArray("ability_bonuses");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                description.append("Your " + jsonArray.getJSONObject(i).getJSONObject("ability_score").getString("name") + " is increased by " + jsonArray.getJSONObject(i).getInt("bonus") + "\n\n");
                            }
                            if (response.has("starting_proficiencies") && response.getJSONArray("starting_proficiencies").length() > 0) {
                                jsonArray = response.getJSONArray("starting_proficiencies");
                                description.append("Starting proficiencies for this race: \n");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject profObject = jsonArray.getJSONObject(i);
                                    description.append("\t~" + profObject.getString("name") + "\n");
                                }

                            }
                            if (response.has("languages") && response.getJSONArray("languages").length() > 0) {
                                description.append("Languages learnt by this subrace: \n");
                                jsonArray = response.getJSONArray("languages");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject profObject = jsonArray.getJSONObject(i);
                                    description.append("\t~" + profObject.getString("name") + "\n");
                                }
                            }
                            if (response.has("language_options")) {
                                JSONObject profOptions = response.getJSONObject("language_options");
                                jsonArray = profOptions.getJSONArray("from");
                                description.append("\nChoose " + profOptions.getInt("choose") + " of these languages: ");
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    JSONObject languageOption = jsonArray.getJSONObject(j);
                                    if (j == jsonArray.length() - 1) {
                                        description.append(languageOption.getString("name") + ".\n\n");
                                        break;
                                    }
                                    description.append(languageOption.getString("name") + ", ");
                                }
                            }

                            if (response.has("racial_traits") && response.getJSONArray("racial_traits").length() > 0) {
                                description.append("\nAll racial traits that this race includes: ");
                                jsonArray = response.getJSONArray("racial_traits");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject traitObject = jsonArray.getJSONObject(i);
                                    if (i == jsonArray.length() - 1) {
                                        description.append(traitObject.getString("name") + ".\n\n");
                                        break;
                                    }
                                    description.append(traitObject.getString("name") + ", ");
                                }
                            }
                            break;

                        case "https://www.dnd5eapi.co/api/traits":
                            title.setText(response.getString("name"));
                            jsonArray = response.getJSONArray("races");
                            if (jsonArray.length() > 0) {
                                description.append("Races that have this trait: \n");
                                for (int i = 0; i < response.getJSONArray("races").length(); i++) {
                                    JSONObject raceList = jsonArray.getJSONObject(i);
                                    description.append("\t~" + raceList.getString("name") + "\n");
                                }
                            }
                            jsonArray = response.getJSONArray("subraces");
                            if (jsonArray.length() > 0) {
                                description.append("\nSubraces that have this trait: \n");
                                for (int i = 0; i < response.getJSONArray("subraces").length(); i++) {
                                    JSONObject raceList = jsonArray.getJSONObject(i);
                                    description.append("\t~" + raceList.getString("name") + "\n");
                                }
                            }
                            jsonArray = response.getJSONArray("desc");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                description.append("\n" + jsonArray.getString(i) + "\n");
                            }
                            break;

                        case "categories!!":
                            break;

                        case "https://www.dnd5eapi.co/api/equipment":       //Make cases
                            title.setText(response.getString("name"));
                            String exactCat = "";
                            String cat = response.getJSONObject("equipment_category").getString("name");

                            switch (cat) {
                                case "Weapon":
                                    exactCat = "weapon_category";
                                    break;
                                case "Armor":
                                    exactCat = "armor_category";
                                    break;
                                case "Adventuring Gear":
                                    exactCat = "gear_category";
                                    break;
                                default:
                                    break;
                            }
                            description.append("Category: " + cat + "\n\n");
                            if (exactCat.equals("gear_category")) {
                                description.append(cat + " category: " + response.getJSONObject(exactCat).getString("name") + "\n\n");
                            } else {
                                description.append(cat + " category: " + response.getString(exactCat));
                            }
                            if (cat.equals("Weapon")) {
                                description.append("\n\nWeapon range: " + response.getString("weapon_range") + "\n\n");
                                JSONObject damage = response.getJSONObject("damage");
                                description.append("Damage is: " + damage.getString("damage_dice") + ", "
                                        + damage.getJSONObject("damage_type").getString("name") + "\n\n");
                                if (response.has("two_handed_damage")) {
                                    JSONObject twoHanded = response.getJSONObject("two_handed_damage");
                                    description.append("Two-Handed Damage is: " + twoHanded.getString("damage_dice")
                                            + ", " + twoHanded.getJSONObject("damage_type").getString("name") + "\n\n");
                                }
                                JSONObject range = response.getJSONObject("range");
                                if (!range.getString("normal").equals("null")) {
                                    description.append("Normal range is: " + range.getString("normal") + "ft.\n\n");
                                } else
                                    description.append("This weapon has only long range.\n\n");
                                if (!range.getString("long").equals("null")) {
                                    description.append("Long range is: " + range.getString("long") + "ft.\n\n");
                                } else
                                    description.append("This weapon has no long range.\n\n");
                                JSONArray properties = response.getJSONArray("properties");
                                description.append("Properties of weapon: ");
                                for (int i = 0; i < properties.length(); i++) {
                                    JSONObject singleProperty = properties.getJSONObject(i);
                                    if (i == properties.length() - 1) {
                                        description.append(singleProperty.getString("name") + ".\n\n");
                                        break;
                                    }
                                    description.append(singleProperty.getString("name") + ", ");
                                }

                            } else if (cat.equals("Armor")) {
                                JSONObject ac = response.getJSONObject("armor_class");
                                String dex = "";
                                if (ac.getBoolean("dex_bonus")) {
                                    dex = " + DEX modifier";
                                }
                                if (!ac.getString("max_bonus").equals("null")) {
                                    if (ac.getInt("max_bonus") > 0)
                                        dex += "(Max bonus is +" + ac.getInt("max_bonus") + ")";
                                }
                                description.append("\n\nArmor Class: " + ac.getString("base") + dex + "\n\n");  //Armor class

                                description.append("Minimum Strength needed: " + response.getInt("str_minimum") + "\n\n");
                                if (response.getBoolean("stealth_disadvantage")) {
                                    description.append("This armor gives stealth disadvantage.\n\n");
                                }
                            }
                            if (response.has("contents") && response.getJSONArray("contents").length() > 0) {
                                JSONArray contents = response.getJSONArray("contents");
                                description.append("This pack includes: ");
                                for (int i = 0; i < contents.length(); i++) {
                                    JSONObject exactItem = contents.getJSONObject(i).getJSONObject("item");
                                    if (i == contents.length() - 1) {
                                        description.append(contents.getJSONObject(i).getString("quantity") + "x "
                                                + exactItem.getString("name") + ".\n\n");
                                        break;
                                    }
                                    description.append(contents.getJSONObject(i).getString("quantity") + "x "
                                            + exactItem.getString("name") + ", ");
                                }
                            }
                            if (response.has("desc")) {
                                JSONArray desc = response.getJSONArray("desc");
                                for (int i = 0; i < desc.length(); i++) {
                                    description.append(desc.getString(i) + "\n\n");
                                }
                            }
                            description.append("Cost of item: " + response.getJSONObject("cost").getString("quantity")
                                    + response.getJSONObject("cost").getString("unit") + "\n\n");

                            if (response.has("weight")) {
                                description.append("Weight: " + response.getString("weight") + "lb.");
                            }
                            break;

                        case "https://www.dnd5eapi.co/api/magic-items":
                            title.setText(response.getString("name"));
                            description.append(response.getJSONObject("equipment_category").getString("name") + "\n\n");
                            jsonArray = response.getJSONArray("desc");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                description.append(jsonArray.getString(i) + "\n\n");
                            }
                            break;

                        case "https://www.dnd5eapi.co/api/weapon-properties":

                        case "https://www.dnd5eapi.co/api/conditions":

                        case "https://www.dnd5eapi.co/api/damage-types":
                            title.setText(response.getString("name"));
                            jsonArray = response.getJSONArray("desc");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                description.append(jsonArray.getString(i) + "\n\n");
                            }
                            break;

                        case "https://www.dnd5eapi.co/api/spells":
                            title.setText(response.getString("name"));
                            jsonArray = response.getJSONArray("desc");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                description.append(jsonArray.getString(i) + "\n\n");
                            }
                            if (response.has("higher_level")) {
                                jsonArray = response.getJSONArray("higher_level");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    description.append(jsonArray.getString(i) + "\n\n");
                                }
                            }
                            if(response.getString("range").equals("Self")){
                                description.append("Range: Self\n\n");
                            }else description.append("Range: " + response.getString("range") + "\n\n");
                            description.append("Components: ");
                            jsonArray = response.getJSONArray("components");
                            for(int i = 0; i < jsonArray.length();i++){
                                if (i == jsonArray.length() - 1) {
                                    description.append(jsonArray.getString(i) + ".\n\n");
                                    break;
                                }
                                description.append(jsonArray.getString(i) + ", ");
                            }
                            if(response.has("material")){
                                description.append("Material/s needed: " + response.getString("material") + "\n\n");
                            }
                            if(response.getBoolean("ritual")){
                                description.append("A minimum 10-minute ritual is needed for this spell.\n\n");
                            }
                            description.append("Spell lasts " + response.getString("duration") + "\n\n");
                            if(response.getBoolean("concentration")){
                                description.append("Concentration is needed for this spell to persist.\n\n");
                            }
                            description.append("Casting time: " + response.getString("casting_time") + "\n\n");
                            description.append("Spell level: " + response.getString("level") + "\n\n");

                            if(response.has("damage")) {
                                JSONObject damage = response.getJSONObject("damage");
                                description.append("Type: " + damage.getJSONObject("damage_type").getString("name") + "\n\n");
                                JSONObject damageAtLevel = damage.getJSONObject("damage_at_slot_level");
                                description.append("Damage type is: " + damage.getJSONObject("damage_type").getString("name")+"\n\n");
                                description.append("Damage at different spell slot levels: "+ "\n");
                                for(int i = response.getInt("level"); i <= 9;i++){
                                    description.append("\t" + String.valueOf(i) + ": " + damageAtLevel.getString(String.valueOf(i)) + "\n");
                                }

                                description.append("\nSpell belongs to the school of " + response.getJSONObject("school").getString("name") + "\n\n");
                                jsonArray = response.getJSONArray("classes");
                                description.append("Spell belongs to the following classes: ");
                                for(int i = 0; i < jsonArray.length();i++){
                                    if (i == jsonArray.length() - 1) {
                                        description.append(jsonArray.getJSONObject(i).getString("name") + ".\n\n");
                                        break;
                                    }
                                    description.append(jsonArray.getJSONObject(i).getString("name") + ", ");
                                }
                            }


                            break;

                        case "https://www.dnd5eapi.co/api/monsters":
                            title.setText(response.getString("name"));
                            description.append("Size: " + response.getString("size") + "\n\n");
                            description.append("Type: " + response.getString("type") + "\n\n");
                            description.append("Alignment: " + response.getString("alignment") + "\n\n");
                            description.append("Armor Class: " + response.getString("armor_class") + "\n\n");
                            description.append("Hit Points: " + response.getString("hit_points") + "\n\n");
                            description.append("Hit Die: " + response.getString("hit_dice") + "\n\n");
                            if (response.getJSONObject("speed").has("walk")) {
                                description.append("Walking Speed: " + response.getJSONObject("speed").getString("walk") + "\n\n");
                            }if (response.getJSONObject("speed").has("fly")) {
                                description.append("Flying Speed: " + response.getJSONObject("speed").getString("fly") + "\n\n");
                            }if (response.getJSONObject("speed").has("swim")) {
                                description.append("Swimming Speed: " + response.getJSONObject("speed").getString("swim") + "\n\n");
                            }if (response.getJSONObject("speed").has("burrow")) {
                                description.append("Burrow Speed: " + response.getJSONObject("speed").getString("burrow") + "\n\n");
                            }
                            description.append("Stats for monster are:\n\tStrength: " + response.getString("strength")
                                            + "\n\tDexterity: " + response.getString("dexterity")
                                            + "\n\tConstitution: " + response.getString("constitution")
                                            + "\n\tIntelligence: " + response.getString("intelligence")
                                            + "\n\tWisdom: " + response.getString("wisdom")
                                            + "\n\tCharisma: " + response.getString("charisma") + "\n\n");

                            if(response.has("senses")) {
                                description.append("Passive Perception: " + response.getJSONObject("senses").getString("passive_perception") + "\n\n");
                            }
                            if(response.has("proficiencies") && response.getJSONArray("proficiencies").length()>0) {
                                jsonArray = response.getJSONArray("proficiencies");
                                description.append("Proficiencies: \n");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject proficiency = jsonArray.getJSONObject(i);
                                    if (i == jsonArray.length() - 1) {
                                        description.append("\t" + proficiency.getJSONObject("proficiency").getString("name") + "(+" + proficiency.getString("value") + ")\n\n");
                                        break;
                                    }
                                    description.append("\t" + proficiency.getJSONObject("proficiency").getString("name") + "(+" + proficiency.getString("value") + ")\n");
                                }
                            }
                            if(response.has("damage_vulnerabilities") && response.getJSONArray("damage_vulnerabilities").length()>0){
                                description.append("Damage Vulnerabilities: ");
                                jsonArray = response.getJSONArray("damage_vulnerabilities");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    if (i == jsonArray.length() - 1) {
                                        description.append(jsonArray.getString(i) + ".\n\n");
                                        break;
                                    }
                                    description.append(jsonArray.getString(i) + ", ");
                                }
                            }
                            if(response.has("damage_resistances") && response.getJSONArray("damage_resistances").length()>0){
                                description.append("Damage Resistances: ");
                                jsonArray = response.getJSONArray("damage_resistances");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    if (i == jsonArray.length() - 1) {
                                        description.append(jsonArray.getString(i) + ".\n\n");
                                        break;
                                    }
                                    description.append(jsonArray.getString(i) + ", ");
                                }
                            }
                            if(response.has("damage_immunities") && response.getJSONArray("damage_immunities").length()>0){
                                description.append("Damage Immunities: ");
                                jsonArray = response.getJSONArray("damage_immunities");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    if (i == jsonArray.length() - 1) {
                                        description.append(jsonArray.getString(i) + ".\n\n");
                                        break;
                                    }
                                    description.append(jsonArray.getString(i) + ", ");
                                }
                            }
                            if(response.has("condition_immunities") && response.getJSONArray("condition_immunities").length()>0){
                                description.append("Condition Immunities: ");
                                jsonArray = response.getJSONArray("condition_immunities");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    if (i == jsonArray.length() - 1) {
                                        description.append(jsonArray.getJSONObject(i).getString("name") + ".\n\n");
                                        break;
                                    }
                                    description.append(jsonArray.getJSONObject(i).getString("name") + ", ");
                                }
                            }

                            if(response.has("languages")) {
                                description.append("Languages the monster can speak: " + response.getString("languages"));
                            }
                            description.append("\n\nChallenge Rating: " + response.getString("challenge_rating") + "\n\n");

                            if(response.has("special_abilities") && response.getJSONArray("special_abilities").length()>0){
                                description.append("Special abilities: ");
                                jsonArray = response.getJSONArray("special_abilities");
                                for(int i = 0; i < jsonArray.length();i++){
                                    JSONObject ability = jsonArray.getJSONObject(i);
                                    description.append("\n\t~" + ability.getString("name") + " - " + ability.getString("desc"));

                                }
                            }

                            if(response.has("actions") && response.getJSONArray("actions").length()>0){
                                description.append("\n\nActions: ");
                                jsonArray = response.getJSONArray("actions");
                                for(int i = 0; i < jsonArray.length();i++){
                                    JSONObject action = jsonArray.getJSONObject(i);
                                    description.append("\n\t~" + action.getString("name") + " - " + action.getString("desc"));
                                }
                            }

                            if(response.has("legendary_actions") && response.getJSONArray("legendary_actions").length()>0){
                                description.append("\n\nLegendary Actions: ");
                                jsonArray = response.getJSONArray("legendary_actions");
                                for(int i = 0; i < jsonArray.length();i++){
                                    JSONObject action = jsonArray.getJSONObject(i);
                                    description.append("\n\t~" + action.getString("name") + " - " + action.getString("desc"));
                                }
                            }


                            break;

                        case "https://www.dnd5eapi.co/api/rules":
                            title.setText(response.getString("name"));
                            description.append(response.getString("desc") + " \n\n");
                            description.append("For more information check these under the Rule Sections menu: \n\n");
                            if(response.has("subsections")){
                                jsonArray = response.getJSONArray("subsections");
                                for(int i = 0; i < jsonArray.length();i++){
                                    description.append("\t~" + jsonArray.getJSONObject(i).getString("name") + "\n");
                                }
                            }
                            break;

                        default:
                            break;
                    }
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

    @Override
    public void onBackPressed() {   //Returns to main screen when back button is pressed as if it goes back to the search one it stops the runnable
        Intent main = new Intent(ResultActivity.this, MainActivity.class);
        main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(main);
    }
}