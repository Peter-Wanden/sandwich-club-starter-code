package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;
import java.util.Arrays;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private ImageView ingredientsIv;

    // TextViews
    private TextView originTv,
            descriptionTv,
            ingredientsTv,
            alsoKnownTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ingredientsIv = findViewById(R.id.image_iv);

        // Get a reference to the TextViews
        originTv = findViewById(R.id.origin_tv);
        descriptionTv = findViewById(R.id.description_tv);
        ingredientsTv = findViewById(R.id.ingredients_tv);
        alsoKnownTv = findViewById(R.id.also_known_tv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = 0;
        if (intent != null) {
            position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        }
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        // Set the title
        setTitle(sandwich.getMainName());

        // Set the picture
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        // Place of origin.
        if (sandwich.getPlaceOfOrigin().length() == 0) {
            sandwich.setPlaceOfOrigin(getString(R.string.origin_unknown));
        }
        originTv.setText(sandwich.getPlaceOfOrigin());

        // Acronyms.
        if (sandwich.getAlsoKnownAs().isEmpty()) {
            sandwich.setAlsoKnownAs
                    (Arrays.asList(getResources().getStringArray(R.array.no_acronyms)));
        }
        // Format the acronyms into a readable list and display it to the screen
        alsoKnownTv.setText(bulletMyList(sandwich.getAlsoKnownAs()));

        // Description.
        if (sandwich.getDescription().length() == 0) {
            sandwich.setDescription(getString(R.string.no_description));
        }
        descriptionTv.setText(sandwich.getDescription());

        // Ingredients.
        if (sandwich.getIngredients().isEmpty()) {
            sandwich.setIngredients
                    (Arrays.asList(getResources().getStringArray(R.array.no_ingredients)));
        }
        // Format the ingredients into a readable list and display it to the screen
        ingredientsTv.setText(bulletMyList(sandwich.getIngredients()));
    }

    // This method formats lists into readable strings
    private String bulletMyList(List<String> list) {
        StringBuilder listString = new StringBuilder();

        // If only one item in list, no need for formatting
        if(list.size()==1) {
            return listString.append(list.get(0)).toString();
        }

        // Iterate through the list adding a bullet point before and carriage return after each item
        for (String listItem : list) {
            listString.append("-  ");
            listString.append(listItem);
            listString.append("\n");
        }
        return listString.toString();
    }
}
