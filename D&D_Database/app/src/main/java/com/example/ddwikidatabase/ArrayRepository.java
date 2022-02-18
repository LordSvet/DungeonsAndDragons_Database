package com.example.ddwikidatabase;

//A class where all the arrays with different data for the List Menu will be kept

import java.util.ArrayList;

public class ArrayRepository {

    static String[] parentArray = {"Character Data", "Classes", "Races", "Equipment", "Game Mechanics", "Rules",
                                   "Spells", "Monsters"}; //Array for all the items on the main Expandable List

    static String[] characterData = {"Ability Scores", "Skills", "Proficiencies", "Languages", "Alignments"};

    static String[] classes = {"Classes", "Subclasses", "Features"};

    static String[] races = {"Races", "Subraces", "Traits"};

    static String[] equipment = {"Equipment", "Magic Items", "Weapon Properties"};

    static String[] gameMechanics = {"Conditions", "Damage Types", "Magic Schools"};

    static String[] rules = {"Rules", "Rule Sections"};

    static ArrayList<String[]> listArray = new ArrayList<>();

}
