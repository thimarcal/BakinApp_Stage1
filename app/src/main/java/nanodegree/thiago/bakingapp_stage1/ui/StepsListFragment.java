package nanodegree.thiago.bakingapp_stage1.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import nanodegree.thiago.bakingapp_stage1.OnFragmentInteractionListener;
import nanodegree.thiago.bakingapp_stage1.R;
import nanodegree.thiago.bakingapp_stage1.adapter.RecipeStepAdapter;
import nanodegree.thiago.bakingapp_stage1.data.RecipeJson;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
  */
public class StepsListFragment extends Fragment implements View.OnClickListener,
                                                        RecipeStepAdapter.OnStepClicked {

    private OnFragmentInteractionListener mListener;

    private RecyclerView stepsRecyclerView;
    private RecipeStepAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private List<RecipeJson.StepsBean> mStepsList;
    private CardView mIngredientsCard;

    public StepsListFragment() {
        // Required empty public constructor

    }

    public void setStepsList(List<RecipeJson.StepsBean> steps) {
        mStepsList = steps;
        if (null != mAdapter) {
            mAdapter.setSteps(mStepsList);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steps_list, container, false);
        stepsRecyclerView = (RecyclerView) view.findViewById(R.id.steps_list_recyclerview);

        mIngredientsCard = view.findViewById(R.id.ingredients_card);
        mIngredientsCard.setOnClickListener(this);

        layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL,
                false);
        stepsRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new RecipeStepAdapter();
        if (null != mStepsList) {
            mAdapter.setSteps(mStepsList);
        }
        stepsRecyclerView.setAdapter(mAdapter);

        return view;
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

    /*
     * OnClick will handle clicks to the Ingredients Card
     */
    @Override
    public void onClick(View view) {

    }

    /*
     * onItemClick will handle clicks to the Steps
     */
    @Override
    public void onItemClicked(int position) {

    }
}
