package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    public static final String TAG = "DetailActivity";

    // Layout Elements of DetailActivity
    private TextView mOriginTextView;
    private TextView mDescriptionTextView;
    private TextView mIngredientsTextView;
    private TextView mAlsoKnownTextView;

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

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        // Toast.makeText(this, json, Toast.LENGTH_LONG).show();
        Log.d(TAG, json);
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

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

    private void populateUI(Sandwich sandwich) {
        this.mDescriptionTextView.setText(sandwich.getDescription());
        this.mOriginTextView.setText(sandwich.getPlaceOfOrigin());
        this.mAlsoKnownTextView.setText(String.join("\n",sandwich.getAlsoKnownAs()));
        this.mIngredientsTextView.setText(String.join("\n",sandwich.getIngredients()));
    }

}
