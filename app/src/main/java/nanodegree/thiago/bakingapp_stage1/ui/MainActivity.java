package nanodegree.thiago.bakingapp_stage1.ui;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;

import nanodegree.thiago.bakingapp_stage1.OnFragmentInteractionListener;
import nanodegree.thiago.bakingapp_stage1.R;
import nanodegree.thiago.bakingapp_stage1.data.RecipeJson;

public class MainActivity extends AppCompatActivity
        implements OnFragmentInteractionListener {

    private static final String RECIPES_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private RequestQueue queue;
    private ArrayList<RecipeJson> mRecipesList;

    private int mCurrentRecipe;
    private int mCurrentRecipeStep;

    private RecipesListFragment recipesListFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recipesListFragment = new RecipesListFragment();
        if (savedInstanceState == null) {
            if (null != mRecipesList) {
                recipesListFragment.setRecipes(mRecipesList);
            }

        } else {

        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragments_container, recipesListFragment)
                .commit();

        /**
         * Data is retrieved in MainActivity, that will handle Fragment changes
         */
        queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(
                Request.Method.GET,
                RECIPES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        RecipeJson[] recipes = gson.fromJson(response, RecipeJson[].class);
                        mRecipesList = new ArrayList();
                        Log.d("MAIN", "Loaded recipes");
                        for (RecipeJson recipe : recipes) {
                            mRecipesList.add(recipe);
                        }

                        if (null != recipesListFragment) {
                            recipesListFragment.setRecipes(mRecipesList);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MAIN", "Failed to load: "+error.getMessage());
                    }
                });

        queue.add(request);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(getString(R.string.current_recipe_key), mCurrentRecipe);
        outState.putInt(getString(R.string.current_step_key), mCurrentRecipeStep);
        outState.putParcelableArrayList(getString(R.string.recipes_key), mRecipesList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
        }

        // When returning, I need to update my fragment with Data, so checking its type allows me
        // to call the appropriate method
        if (getSupportFragmentManager().getFragments().get(0) instanceof RecipesListFragment) {
            mCurrentRecipe = -1;
            recipesListFragment.setRecipes(mRecipesList);
        }

    }

    @Override
    public void onFragmentInteraction(int action, Bundle extras) {
        switch (action) {
            case OnFragmentInteractionListener.ACTION_RECIPE_SELECTED:
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                StepsListFragment fragment = new StepsListFragment();
                mCurrentRecipe = extras.getInt(getString(R.string.extra_position));
                fragment.setStepsList(mRecipesList.get(mCurrentRecipe).getSteps());

                transaction.replace(R.id.fragments_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;

            case ACTION_STEP_SELECTED:
                int stepIndex = extras.getInt(getString(R.string.extra_position));
                StepDetailsFragment detailsFragment = new StepDetailsFragment();
                RecipeJson.StepsBean step = mRecipesList.get(mCurrentRecipe).getSteps().get(stepIndex);
                String videoUrl = step.getVideoURL();

                detailsFragment.setStepData(this, videoUrl, step.getDescription(), stepIndex,
                        mRecipesList.get(mCurrentRecipe).getSteps().size());

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragments_container, detailsFragment)
                        .addToBackStack(null)
                        .commit();
                break;

            case ACTION_INGREDIENTS_SELECTED:
                IngredientsFragment ingredientsFragment = new IngredientsFragment();
                ingredientsFragment.setIngredients(
                        mRecipesList.get(mCurrentRecipe).getIngredients());

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragments_container, ingredientsFragment)
                        .addToBackStack(null)
                        .commit();
                break;

            case ACTION_NEXT_STEP:
                mCurrentRecipeStep += 1;
                RecipeJson.StepsBean nextStep = mRecipesList.get(mCurrentRecipe).getSteps().get(mCurrentRecipeStep);
                StepDetailsFragment nextStepDetailsFragment = new StepDetailsFragment();

                nextStepDetailsFragment.setStepData(this,
                        nextStep.getVideoURL(),
                        nextStep.getDescription(),
                        mCurrentRecipeStep,
                        mRecipesList.get(mCurrentRecipe).getSteps().size());

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragments_container, nextStepDetailsFragment)
                        .addToBackStack(null)
                        .commit();
                break;

            case ACTION_PREVIOUS_STEP:
                mCurrentRecipeStep -= 1;
                RecipeJson.StepsBean prevStep = mRecipesList.get(mCurrentRecipe).getSteps().get(mCurrentRecipeStep);
                StepDetailsFragment prevStepDetailsFragment = new StepDetailsFragment();

                prevStepDetailsFragment.setStepData(this,
                        prevStep.getVideoURL(),
                        prevStep.getDescription(),
                        mCurrentRecipeStep,
                        mRecipesList.get(mCurrentRecipe).getSteps().size());

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragments_container, prevStepDetailsFragment)
                        .addToBackStack(null)
                        .commit();
                break;

            default:
                break;
        }

    }
}
