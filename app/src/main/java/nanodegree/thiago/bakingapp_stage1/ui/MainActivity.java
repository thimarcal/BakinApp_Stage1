package nanodegree.thiago.bakingapp_stage1.ui;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nanodegree.thiago.bakingapp_stage1.R;

public class MainActivity extends AppCompatActivity implements RecipesListFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
