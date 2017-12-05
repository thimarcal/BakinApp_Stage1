package nanodegree.thiago.bakingapp_stage1.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nanodegree.thiago.bakingapp_stage1.R;
import nanodegree.thiago.bakingapp_stage1.data.RecipeJson;

public class MainActivity extends AppCompatActivity implements RecipesListFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            RecipesListFragment fragment = new RecipesListFragment();
            transaction.replace(R.id.recipes_list_fragment, fragment);
            transaction.commit();
        }
    }

    @Override
    public void onFragmentInteraction(RecipeJson recipe) {
        // TODO: Here I'l need to check if single fragment screen, or tablet.. etc..
        Intent intent = new Intent(this, RecipeStepsActivity.class);
        intent.putExtra(getString(R.string.extra_recipe), recipe);

        startActivity(intent);
    }
}
