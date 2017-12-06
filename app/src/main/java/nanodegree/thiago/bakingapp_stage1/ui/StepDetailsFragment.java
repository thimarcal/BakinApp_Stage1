package nanodegree.thiago.bakingapp_stage1.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.SimpleExoPlayer;

import nanodegree.thiago.bakingapp_stage1.OnFragmentInteractionListener;
import nanodegree.thiago.bakingapp_stage1.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class StepDetailsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private String mVideoUrl;
    private String mDescription;
    private int mStepPosition;

    private SimpleExoPlayer mSimpleExoPlayer;
    private CardView mVideoCardview;
    private TextView mDescriptionTv;

    public StepDetailsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_step_details, container, false);

        //mSimpleExoPlayer = (SimpleExoPlayer)view.findViewById(R.id.video_exoplayer);
        mVideoCardview = (CardView)view.findViewById(R.id.video_cardview);
        mDescriptionTv = (TextView)view.findViewById(R.id.description_textview);
        return view;
    }

    public void setStepData(String videoUrl, String description, int position) {
        mVideoUrl = videoUrl;
        mDescription = description;
        mStepPosition = position;

        if ((null == videoUrl || videoUrl.isEmpty()) && (null != mVideoCardview)) {
            mVideoCardview.setVisibility(View.INVISIBLE);
        }
        mDescriptionTv.setText(mDescription);
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

}
