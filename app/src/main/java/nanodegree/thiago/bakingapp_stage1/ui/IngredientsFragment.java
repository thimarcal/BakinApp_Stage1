package nanodegree.thiago.bakingapp_stage1.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import nanodegree.thiago.bakingapp_stage1.OnFragmentInteractionListener;
import nanodegree.thiago.bakingapp_stage1.R;
import nanodegree.thiago.bakingapp_stage1.adapter.IngredientsAdapter;
import nanodegree.thiago.bakingapp_stage1.data.RecipeJson;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class IngredientsFragment extends Fragment {
    private IngredientsAdapter adapter;

    private List<RecipeJson.IngredientsBean> mIngredients;

    public IngredientsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ingredients, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.ingredients_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,
                false);

        recyclerView.setLayoutManager(layoutManager);

        adapter = new IngredientsAdapter();
        if (null != mIngredients) {
            adapter.setIngredients(mIngredients);
        }
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void setIngredients (List<RecipeJson.IngredientsBean> list) {
        mIngredients = list;
        if (null != adapter) {
            adapter.setIngredients(mIngredients);
        }
    }

 }
