package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ImageView ingredientsIv = findViewById(R.id.image_iv);

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
        TextView mTVAlsoKnownTitle = findViewById(R.id.also_known_title_tv);
        TextView mTVAlsoKnown = findViewById(R.id.also_known_tv);
        TextView mTVOriginTitle = findViewById(R.id.origin_title_tv);
        TextView mTVOrigin = findViewById(R.id.origin_tv);
        TextView mTVDescriptionTitle = findViewById(R.id.description_title_tv);
        TextView mTVDescription = findViewById(R.id.description_tv);
        TextView mTVIngredientsTitle = findViewById(R.id.ingredients_title_tv);
        TextView mTVIngredients = findViewById(R.id.ingredients_tv);

        List<String> alsoKnownAsList = sandwich.getAlsoKnownAs();
        if (alsoKnownAsList.isEmpty() || alsoKnownAsList == null) {
            mTVAlsoKnownTitle.setVisibility(View.GONE);
            mTVAlsoKnown.setVisibility(View.GONE);
        } else {
            for (String current : alsoKnownAsList) {
                String last = alsoKnownAsList.get(alsoKnownAsList.size() - 1);
                if (current != last)
                    mTVAlsoKnown.append(current + ", ");
                else
                    mTVAlsoKnown.append(current + ".");
            }
        }
        if (sandwich.getPlaceOfOrigin().isEmpty() || sandwich.getPlaceOfOrigin() == null) {
            mTVOriginTitle.setVisibility(View.GONE);
            mTVOrigin.setVisibility(View.GONE);
        } else
            mTVOrigin.setText(sandwich.getPlaceOfOrigin());
        if (sandwich.getDescription().isEmpty() || sandwich.getDescription() == null) {
            mTVDescriptionTitle.setVisibility(View.GONE);
            mTVDescription.setVisibility(View.GONE);
        } else
            mTVDescription.setText(sandwich.getDescription());
        List<String> ingredientsList = sandwich.getIngredients();
        if (ingredientsList.isEmpty() || ingredientsList == null) {
            mTVIngredientsTitle.setVisibility(View.GONE);
            mTVIngredients.setVisibility(View.GONE);
        } else {
            for (String current : ingredientsList) {
                String last = ingredientsList.get(ingredientsList.size() - 1);
                if (current != last)
                    mTVIngredients.append(current + ", ");
                else
                    mTVIngredients.append(current + ".");
            }
        }
    }
}
