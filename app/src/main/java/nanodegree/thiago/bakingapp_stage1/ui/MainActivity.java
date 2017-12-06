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

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Log.d("MAIN", "" + transaction.isEmpty());

            recipesListFragment = new RecipesListFragment();
            transaction.replace(R.id.fragments_container, recipesListFragment);
            transaction.commit();
        }

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

                    }
                });

        queue.add(request);
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
        if (OnFragmentInteractionListener.ACTION_RECIPE_SELECTED == action) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            StepsListFragment fragment = new StepsListFragment();
            mCurrentRecipe = extras.getInt(RecipesListFragment.EXTRA_POSITION);
            fragment.setStepsList(mRecipesList.get(mCurrentRecipe).getSteps());

            transaction.replace(R.id.fragments_container, fragment, "STEPS_FRAGMENT");
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
