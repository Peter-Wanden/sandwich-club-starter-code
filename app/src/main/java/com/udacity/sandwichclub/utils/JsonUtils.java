package com.udacity.sandwichclub.utils;

import android.text.TextUtils;

import com.udacity.sandwichclub.R;
import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    // Log tag for this class
    private static final String LOG_TAG = JsonUtils.class.getSimpleName();

    public static Sandwich parseSandwichJson(String json) {
        // If the JSON String is null or empty, return early
        if (TextUtils.isEmpty(json)){
            return null;
        }

        try {
            // Cast string to JSON object
            JSONObject jsonObject = new JSONObject(json);

            // Extract top level String values
            String placeOfOrigin = jsonObject.getString("placeOfOrigin");

            String description = jsonObject.getString("description");
            String image = jsonObject.getString("image");

            // Extract top level arrays
            JSONArray ingredients = jsonObject.getJSONArray("ingredients");
            // Create a List hold the ingredients
            List<String> ingredientList = new ArrayList<>();
            // If there is one or more ingredients in the list...
            if(ingredients != null) {
                // loop through and add all ingredients to the list
                for(int i=0; i < ingredients.length(); i++) {
                    ingredientList.add(ingredients.getString(i));
                }
            }

            // Extract name object
            JSONObject sandwichName = jsonObject.getJSONObject("name");

            // Extract String values from JSON object 'name'
            String mainName = sandwichName.getString("mainName");

            // Extract 'also known as' array from JSON object 'name'
            JSONArray alsoKnownAs = sandwichName.getJSONArray("alsoKnownAs");
            // Create a List to hold the alternative names
            List<String> alternativeNameList = new ArrayList<>();
            // If there is one or more names on the list
            if(alsoKnownAs != null){
                // loop through and add the alternative names to the list
                for(int i=0; i < alsoKnownAs.length(); i++){
                    alternativeNameList.add(alsoKnownAs.getString(i));
                }
            }

            // Return the data in a new Sandwich object
            return new Sandwich(mainName, alternativeNameList, placeOfOrigin, description, image,
                    ingredientList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
