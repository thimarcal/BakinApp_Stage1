package nanodegree.thiago.bakingapp_stage1.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import nanodegree.thiago.bakingapp_stage1.R;
import nanodegree.thiago.bakingapp_stage1.adapter.RecipeListAdapter;
import nanodegree.thiago.bakingapp_stage1.data.RecipeJson;


public class RecipesListFragment extends Fragment
                                 implements RecipeListAdapter.RecipeOnClickListener {

    private static final String TAG = RecipesListFragment.class.getSimpleName();

    private static final String RECIPES_URL =
                "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private RequestQueue queue;

    private OnFragmentInteractionListener mListener;

    private RecyclerView recipesListRecyclerView;
    private LinearLayoutManager layoutManager;
    private RecipeListAdapter recipeListAdapter;


    public RecipesListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipes_list, container, false);
        recipesListRecyclerView = (RecyclerView)view.findViewById(R.id.recipe_list_recyclerview);

        layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL,
                false);
        recipesListRecyclerView.setLayoutManager(layoutManager);

        recipeListAdapter = new RecipeListAdapter(getContext(), this);
        recipesListRecyclerView.setAdapter(recipeListAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        StringRequest request = new StringRequest(
                Request.Method.GET,
                RECIPES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (null != recipeListAdapter) {
                            Gson gson = new Gson();
                            RecipeJson []recipes = gson.fromJson(response, RecipeJson[].class);
                            ArrayList<RecipeJson> recipesList = new ArrayList();
                            for (RecipeJson recipe : recipes) {
                                recipesList.add(recipe);
                            }
                            recipeListAdapter.setRecipesList(recipesList);
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
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

    @Override
    public void onRecipeClicked(View view) {
        RecipeJson recipe = recipeListAdapter.getRecipeAt((Integer) view.getTag());

        mListener.onFragmentInteraction(recipe);
        Log.d(TAG, recipe.getName());
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(RecipeJson recipe);
    }
}
