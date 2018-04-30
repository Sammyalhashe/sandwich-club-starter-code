package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    public static final String TAG = "DetailActivity";

    // Layout Elements of DetailActivity whose text is dynamically altered
    private TextView mOriginTextView;
    private TextView mDescriptionTextView;
    private TextView mIngredientsTextView;
    private TextView mAlsoKnownTextView;

    // static layout views
    private TextView mStaticDescriptionTextView;
    private TextView mStaticOriginTextView;
    private TextView mStaticIngredientsTextView;
    private TextView mStaticAlsoKnownAsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // grabbing references to Layout Elements
        ImageView ingredientsIv = findViewById(R.id.image_iv);
        this.mOriginTextView = (TextView) findViewById(R.id.origin_tv);
        this.mDescriptionTextView = (TextView) findViewById(R.id.description_tv);
        this.mIngredientsTextView = (TextView) findViewById(R.id.ingredients_tv);
        this.mAlsoKnownTextView = (TextView) findViewById(R.id.also_known_tv);

        // static elements
        this.mStaticDescriptionTextView = (TextView) findViewById(R.id.description_label_tv);
        this.mStaticOriginTextView = (TextView) findViewById(R.id.origin_label_tv);
        this.mStaticIngredientsTextView = (TextView) findViewById(R.id.ingredients_label_tv);
        this.mStaticAlsoKnownAsTextView = (TextView) findViewById(R.id.also_known_label_tv);


        // get the intent that triggered this activity
        // if the intent is null, we close the activity immediately
        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        // get the position passed into the intent, with it defaulting to default position if it is not found
        // it will close for default position
        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        // access resources for a defined array of sandwich details with elements in String JSON format
        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        // Toast.makeText(this, json, Toast.LENGTH_LONG).show();
        Log.d(TAG, json); // to see the JSON
        // parse the JSON and return a sandwich object
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        // populate the UI using Picasso (an image API) and a class method populateUI defined below
        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    /*
    This function poulate the UI by setting the text of the text views. If no text fills them, I hide their visibility
    */
    private void populateUI(Sandwich sandwich) {
        if (sandwich.getDescription() == null) {
            this.mDescriptionTextView.setVisibility(View.GONE);
            this.mStaticDescriptionTextView.setVisibility(View.GONE);

        } else {
            this.mDescriptionTextView.setText(sandwich.getDescription());
        }
        if (sandwich.getPlaceOfOrigin() == null) {
            this.mOriginTextView.setVisibility(View.GONE);
            this.mStaticOriginTextView.setVisibility(View.GONE);
        } else {
            this.mOriginTextView.setText(sandwich.getPlaceOfOrigin());
        }
        if (sandwich.getAlsoKnownAs() == null) {
            this.mAlsoKnownTextView.setVisibility(View.GONE);
            this.mStaticAlsoKnownAsTextView.setVisibility(View.GONE);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.mAlsoKnownTextView.setText(String.join("\n",sandwich.getAlsoKnownAs()));
            } else {
                String AKA = "";
                ArrayList<String> AKAList = (ArrayList<String>) sandwich.getAlsoKnownAs();
                for (int i = 0; i< AKAList.size(); i++) {
                    AKA += (String) AKAList.get(i) + "\n";
                }
                this.mAlsoKnownTextView.setText(AKA);
            }
        }
        if (sandwich.getIngredients() == null) {
            this.mIngredientsTextView.setVisibility(View.GONE);
            this.mStaticIngredientsTextView.setVisibility(View.GONE);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.mIngredientsTextView.setText(String.join("\n", sandwich.getIngredients()));
            } else {
                String Ingredients = "";
                ArrayList<String> ingredientsList = (ArrayList<String>) sandwich.getIngredients();
                for (int i = 0; i< ingredientsList.size(); i++) {
                    Ingredients += (String) ingredientsList.get(i) + "\n";
                }
                this.mIngredientsTextView.setText(Ingredients);
            }
        }
    }

}
