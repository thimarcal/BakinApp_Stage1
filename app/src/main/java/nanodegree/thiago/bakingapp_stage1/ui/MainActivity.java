package nanodegree.thiago.bakingapp_stage1.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import nanodegree.thiago.bakingapp_stage1.OnFragmentInteractionListener;
import nanodegree.thiago.bakingapp_stage1.R;
import nanodegree.thiago.bakingapp_stage1.RecipesWidget;
import nanodegree.thiago.bakingapp_stage1.data.RecipeJson;

/**
 * Main Activity handles Fragments and Clicks, to change between fragments. It's also responsible
 * for identifying different screen sizes and rotation.
 */

public class MainActivity extends AppCompatActivity
        implements OnFragmentInteractionListener {

    // URL to get recipes for the example
    private static final String RECIPES_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private ArrayList<RecipeJson> mRecipesList;
    private FrameLayout largeContainer;

    private int mCurrentRecipe;
    private int mCurrentRecipeStep;

    private Fragment mCurrentFragment = null;
    private Fragment mCurrentLargeFragment = null;
    private boolean mLargeScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Detect it's not sw600dp screen
        largeContainer = findViewById(R.id.big_fragment_container);
        mLargeScreen = (largeContainer != null);

        /*
         * Detects screen changes, like screen rotation, and does not need to reload data
         */
        if (savedInstanceState == null) {
            mCurrentFragment = new RecipesListFragment();
            ((RecipesListFragment) mCurrentFragment).setLargeScreen(mLargeScreen);
            if (mLargeScreen) {
                largeContainer.setVisibility(View.GONE);
            }

            if (null != mRecipesList) {
                ((RecipesListFragment) (mCurrentFragment)).setRecipes(mRecipesList);
            }

            /*
             * Data is retrieved in MainActivity using Volley Lib, that will handle Fragment changes
             */
            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest request = new StringRequest(
                    Request.Method.GET,
                    RECIPES_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Gson gson = new Gson();
                            RecipeJson[] recipes = gson.fromJson(response, RecipeJson[].class);
                            mRecipesList = new ArrayList();
                            mRecipesList.addAll(Arrays.asList(recipes));

                            if (null != mCurrentFragment && mCurrentFragment instanceof RecipesListFragment) {
                                ((RecipesListFragment) (mCurrentFragment)).setRecipes(mRecipesList);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });

            queue.add(request);

        } else {
            /*
             * Data is already available
             */
            mCurrentFragment = getSupportFragmentManager().getFragment(savedInstanceState,
                    getString(R.string.fragment_key));
            mCurrentRecipeStep = savedInstanceState.getInt(getString(R.string.current_step_key));
            mCurrentRecipe = savedInstanceState.getInt(getString(R.string.current_recipe_key));
            mRecipesList = savedInstanceState.getParcelableArrayList(getString(R.string.recipes_key));
            mLargeScreen = savedInstanceState.getBoolean(getString(R.string.large_screen_key));

            mCurrentLargeFragment = getSupportFragmentManager().getFragment(savedInstanceState,
                    getString(R.string.large_fragment_key));

            if (mCurrentFragment instanceof RecipesListFragment && mLargeScreen) {
                largeContainer.setVisibility(View.GONE);
                ((RecipesListFragment) mCurrentFragment).setLargeScreen(mLargeScreen);
            }

            if (mLargeScreen && null != mCurrentLargeFragment) {

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.big_fragment_container, mCurrentLargeFragment)
                        .commit();

                if (mCurrentLargeFragment instanceof IngredientsFragment) {
                    ((IngredientsFragment)mCurrentLargeFragment).setIngredients(
                            mRecipesList.get(mCurrentRecipe).getIngredients());
                }

            }
        }

        // Adds mCurrent Fragment, that may be initial screen, or other screen, if rotated
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragments_container, mCurrentFragment)
                .commit();


    }

    /*
     * Saves current state, that means current Fragment and useful data
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(getString(R.string.current_recipe_key), mCurrentRecipe);
        outState.putInt(getString(R.string.current_step_key), mCurrentRecipeStep);
        outState.putParcelableArrayList(getString(R.string.recipes_key), mRecipesList);
        outState.putBoolean(getString(R.string.large_screen_key), mLargeScreen);

        getSupportFragmentManager().putFragment(outState,
                getString(R.string.fragment_key),
                mCurrentFragment);
        if (null != mCurrentLargeFragment) {
            getSupportFragmentManager().putFragment(outState,
                    getString(R.string.large_fragment_key),
                    mCurrentLargeFragment);
        }

        super.onSaveInstanceState(outState);
    }

    /*
     * This method handles click on Back in Action Bar. It just calls onBackPressed that has the
     * needed logic.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
         * Handles back press, that will pop previous fragment and set data if needed
         */
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
        }

        mCurrentFragment = getSupportFragmentManager().getFragments().get(0);

        // When returning, I need to update my fragment with Data, so checking its type allows me
        // to call the appropriate method
        if (mCurrentFragment instanceof RecipesListFragment) {
            mCurrentRecipe = -1;
            if (mLargeScreen) {
                largeContainer.setVisibility(View.GONE);
            }
            ((RecipesListFragment)mCurrentFragment).setRecipes(mRecipesList);
            ((RecipesListFragment) (mCurrentFragment)).setLargeScreen(mLargeScreen);

            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle(getString(R.string.app_name));
        }

    }

    /*
     * This listener method identifies which action was taken, and appropriately changes fragment.
     * For each fragment, it enables Back Button in Action Bar.
     */
    @Override
    public void onFragmentInteraction(int action, Bundle extras) {
        if (mLargeScreen) {
            largeContainer.setVisibility(View.VISIBLE);
        }
        switch (action) {
            // Recipe was clicked, must Open Steps
            case OnFragmentInteractionListener.ACTION_RECIPE_SELECTED:
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                mCurrentFragment = new StepsListFragment();
                mCurrentRecipe = extras.getInt(getString(R.string.extra_position));
                ((StepsListFragment) (mCurrentFragment)).setStepsList(mRecipesList.get(mCurrentRecipe).getSteps());
                getSupportActionBar().setTitle(mRecipesList.get(mCurrentRecipe).getName());

                if (!mLargeScreen) {
                    transaction.replace(R.id.fragments_container, mCurrentFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();


                } else {
                    /*
                     * In case of Large Screen, display the Ingredients initially
                     */
                    mCurrentLargeFragment = new IngredientsFragment();
                    ((IngredientsFragment) (mCurrentLargeFragment)).setIngredients(
                            mRecipesList.get(mCurrentRecipe).getIngredients());

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragments_container, mCurrentFragment)
                            .replace(R.id.big_fragment_container, mCurrentLargeFragment)
                            .addToBackStack(null)
                            .commit();
                }

                // When recipe is selected, update Widgets
                RecipesWidget.updateWidgets(this,
                        mRecipesList.get(mCurrentRecipe).getName(),
                        mRecipesList.get(mCurrentRecipe).getIngredients());
                break;

            // When a step is selected, it must be shown
            case ACTION_STEP_SELECTED:
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                mCurrentRecipeStep = extras.getInt(getString(R.string.extra_position));
                RecipeJson.StepsBean step = mRecipesList.get(mCurrentRecipe)
                        .getSteps().get(mCurrentRecipeStep);
                String videoUrl = step.getVideoURL();

                if (!mLargeScreen) {
                    mCurrentFragment = new StepDetailsFragment();
                    ((StepDetailsFragment) (mCurrentFragment)).setStepData(this, videoUrl,
                            step.getDescription(), mCurrentRecipeStep,
                            mRecipesList.get(mCurrentRecipe).getSteps().size());

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragments_container, mCurrentFragment)
                            .addToBackStack(null)
                            .commit();
                } else {
                    mCurrentLargeFragment = new StepDetailsFragment();

                    ((StepDetailsFragment) (mCurrentLargeFragment)).setStepData(this, videoUrl,
                            step.getDescription(), mCurrentRecipeStep,
                            mRecipesList.get(mCurrentRecipe).getSteps().size());
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.big_fragment_container, mCurrentLargeFragment)
                            .addToBackStack(null)
                            .commit();
                }
                break;

            // Ingredients are shown, when selected
            case ACTION_INGREDIENTS_SELECTED:
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                if (!mLargeScreen) {
                    mCurrentFragment = new IngredientsFragment();
                    ((IngredientsFragment) (mCurrentFragment)).setIngredients(
                            mRecipesList.get(mCurrentRecipe).getIngredients());

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragments_container, mCurrentFragment)
                            .addToBackStack(null)
                            .commit();
                } else {
                    mCurrentLargeFragment = new IngredientsFragment();
                    ((IngredientsFragment) (mCurrentLargeFragment)).setIngredients(
                            mRecipesList.get(mCurrentRecipe).getIngredients());

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.big_fragment_container, mCurrentLargeFragment)
                            .addToBackStack(null)
                            .commit();
                }
                break;

            // Handles navigation between steps
            case ACTION_NEXT_STEP:
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                mCurrentRecipeStep = extras.getInt(getString(R.string.extra_position));
                mCurrentRecipeStep += 1;
                RecipeJson.StepsBean nextStep = mRecipesList.get(mCurrentRecipe).getSteps().get(mCurrentRecipeStep);
                if (!mLargeScreen) {
                    mCurrentFragment = new StepDetailsFragment();

                    ((StepDetailsFragment) (mCurrentFragment)).setStepData(this,
                            nextStep.getVideoURL(),
                            nextStep.getDescription(),
                            mCurrentRecipeStep,
                            mRecipesList.get(mCurrentRecipe).getSteps().size());

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragments_container, mCurrentFragment)
                            .addToBackStack(null)
                            .commit();
                } else {
                    mCurrentLargeFragment = new StepDetailsFragment();

                    ((StepDetailsFragment) (mCurrentLargeFragment)).setStepData(this,
                            nextStep.getVideoURL(),
                            nextStep.getDescription(),
                            mCurrentRecipeStep,
                            mRecipesList.get(mCurrentRecipe).getSteps().size());

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.big_fragment_container, mCurrentLargeFragment)
                            .addToBackStack(null)
                            .commit();
                }
                break;

            case ACTION_PREVIOUS_STEP:
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                mCurrentRecipeStep = extras.getInt(getString(R.string.extra_position));
                mCurrentRecipeStep -= 1;
                RecipeJson.StepsBean prevStep = mRecipesList.get(mCurrentRecipe).getSteps().get(mCurrentRecipeStep);
                mCurrentFragment = new StepDetailsFragment();

                ((StepDetailsFragment) (mCurrentFragment)).setStepData(this,
                        prevStep.getVideoURL(),
                        prevStep.getDescription(),
                        mCurrentRecipeStep,
                        mRecipesList.get(mCurrentRecipe).getSteps().size());

                if (!mLargeScreen) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragments_container, mCurrentFragment)
                            .addToBackStack(null)
                            .commit();
                } else {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.big_fragment_container, mCurrentFragment)
                            .addToBackStack(null)
                            .commit();
                }
                break;

            default:
                break;
        }

    }
}
