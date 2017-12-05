package nanodegree.thiago.bakingapp_stage1.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import nanodegree.thiago.bakingapp_stage1.R;
import nanodegree.thiago.bakingapp_stage1.adapter.RecipeStepAdapter;
import nanodegree.thiago.bakingapp_stage1.data.RecipeJson;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StepsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StepsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepsListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private RecyclerView stepsRecyclerView;
    private RecipeStepAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private List<RecipeJson.StepsBean> mStepsList;

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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        void onFragmentInteraction(Uri uri);
    }
}
