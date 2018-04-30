package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.DetailActivity;
import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {
        try {
            // get the full JSON object
            JSONObject fullSandwhichInfo = new JSONObject(json);

            // extract JSON object "name" from full Object
            JSONObject name = fullSandwhichInfo.getJSONObject("name");

            // from name, get the mainName String
            String mainName = name.getString("mainName");

            // Define Lists for alsoKnown as and ingredients
            List<String> alsoKnownAs = new ArrayList<>();
            List<String> ingredients = new ArrayList<>();

            // Loop through the JSONArray "alsoKnownAs" add transfer its elements to List var of same name
            JSONArray JSONalsoKnownAs =  name.getJSONArray("alsoKnownAs");
            for (int i = 0; i < JSONalsoKnownAs.length(); i++) {
                alsoKnownAs.add(i, JSONalsoKnownAs.getString(i));
            }

            // same for ingredients
            JSONArray JSONingredients = fullSandwhichInfo.getJSONArray("ingredients");
            for (int i = 0; i< JSONingredients.length(); i++) {
                ingredients.add(i,JSONingredients.getString(i));
            }

            // get description from JSONObject
            String description = fullSandwhichInfo.getString("description");

            // get placeOfOrigin from JSONObject
            String placeOfOrigin = fullSandwhichInfo.getString("placeOfOrigin");

            // get String image from JSONObject
            String image = fullSandwhichInfo.getString("image");

            // use extracted info from JSONObject to return Sandwich Object
            return new Sandwich(mainName, alsoKnownAs, placeOfOrigin, description, image, ingredients);

        } catch (JSONException e) {
            Log.e(DetailActivity.TAG, "Error thrown in JSON Handling", e);
            return null;
        }

    }
}
