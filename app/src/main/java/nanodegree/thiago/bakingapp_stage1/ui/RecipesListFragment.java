package nanodegree.thiago.bakingapp_stage1.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import nanodegree.thiago.bakingapp_stage1.OnFragmentInteractionListener;
import nanodegree.thiago.bakingapp_stage1.R;
import nanodegree.thiago.bakingapp_stage1.adapter.RecipeListAdapter;
import nanodegree.thiago.bakingapp_stage1.data.RecipeJson;


public class RecipesListFragment extends Fragment
                                 implements RecipeListAdapter.RecipeOnClickListener {

    private static final String TAG = RecipesListFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    private RecyclerView recipesListRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecipeListAdapter recipeListAdapter;

    private List<RecipeJson> mRecipes;

    private boolean mLargeScreen;

    public RecipesListFragment() {
        // Required empty public constructor
    }

    public void setLargeScreen(boolean largeScreen) {
        mLargeScreen = largeScreen;
        if (mLargeScreen && null != recipesListRecyclerView) {
            layoutManager = new GridLayoutManager(getActivity(),
                    3,
                    GridLayoutManager.VERTICAL,
                    false);
            recipesListRecyclerView.setLayoutManager(layoutManager);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipes_list, container, false);
        recipesListRecyclerView = (RecyclerView)view.findViewById(R.id.recipe_list_recyclerview);

        if (!mLargeScreen) {
            layoutManager = new LinearLayoutManager(getActivity(),
                    LinearLayoutManager.VERTICAL,
                    false);
        } else {
            layoutManager = new GridLayoutManager(getActivity(),
                    3,
                    GridLayoutManager.VERTICAL,
                    false);
        }
        recipesListRecyclerView.setLayoutManager(layoutManager);

        recipeListAdapter = new RecipeListAdapter(getContext(), this);
        recipesListRecyclerView.setAdapter(recipeListAdapter);

        return view;
    }

    public void setRecipes(ArrayList<RecipeJson> recipes) {
        mRecipes = recipes;
        if (null != recipeListAdapter) {
            recipeListAdapter.setRecipesList(mRecipes);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null != savedInstanceState) {
            mRecipes = savedInstanceState.getParcelableArrayList(getString(R.string.recipes_key));

            if(null != recipeListAdapter) {
                recipeListAdapter.setRecipesList(mRecipes);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof nanodegree.thiago.bakingapp_stage1.OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /*
     * When a Recipe is clicked we inform its TAG (position in the adapter), so that it's possible
     * to open its steps.
     */
    @Override
    public void onRecipeClicked(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.extra_position), (Integer)view.getTag());

        mListener.onFragmentInteraction(OnFragmentInteractionListener.ACTION_RECIPE_SELECTED,
                                        bundle);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(getString(R.string.recipes_key),
                new ArrayList<Parcelable>(mRecipes));
        super.onSaveInstanceState(outState);
    }
}
