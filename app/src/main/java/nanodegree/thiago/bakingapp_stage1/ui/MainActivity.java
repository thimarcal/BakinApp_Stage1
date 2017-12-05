package nanodegree.thiago.bakingapp_stage1.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import nanodegree.thiago.bakingapp_stage1.R;
import nanodegree.thiago.bakingapp_stage1.data.RecipeJson;

public class MainActivity extends AppCompatActivity
        implements RecipesListFragment.OnFragmentInteractionListener,
        StepsListFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Log.d("MAIN", "" +transaction.isEmpty());

            RecipesListFragment fragment = new RecipesListFragment();
            transaction.replace(R.id.fragments_container, fragment);
            transaction.commit();
        }
    }

    @Override
    public void onFragmentInteraction(RecipeJson recipe) {
        // TODO: Here I'll need to check if single fragment screen, or tablet.. etc..
        //Intent intent = new Intent(this, RecipeStepsActivity.class);
       // intent.putExtra(getString(R.string.extra_recipe), recipe);

        //startActivity(intent);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        StepsListFragment fragment = new StepsListFragment();
        fragment.setStepsList(recipe.getSteps());

        transaction.replace(R.id.fragments_container, fragment, "RECIPES_FRAGMENT");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
