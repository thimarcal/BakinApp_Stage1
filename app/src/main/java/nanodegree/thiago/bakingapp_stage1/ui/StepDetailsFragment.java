package nanodegree.thiago.bakingapp_stage1.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import nanodegree.thiago.bakingapp_stage1.OnFragmentInteractionListener;
import nanodegree.thiago.bakingapp_stage1.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class StepDetailsFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = StepDetailsFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    private String mVideoUrl;
    private String mDescription;
    private int mStepPosition;
    private int mTotalSteps;
    private Context mContext;

    private CardView mVideoCardview;
    private TextView mDescriptionTv;
    private Button mNextButton;
    private Button mPreviousButton;

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;

    private boolean dataSet = false;

    public StepDetailsFragment() {
        // Required empty public constructor
        dataSet = false;
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
        mContext = getContext();

        mPlayerView = view.findViewById(R.id.video_exoplayer);
        mVideoCardview = view.findViewById(R.id.video_cardview);
        if (dataSet && (null == mVideoUrl || mVideoUrl.isEmpty())) {
            mVideoCardview.setVisibility(View.GONE);
        } else if (null != mVideoUrl && !mVideoUrl.isEmpty()){
            createPlayer();
        }
        mDescriptionTv = view.findViewById(R.id.description_textview);
        if (null != mDescriptionTv) {
            mDescriptionTv.setText(mDescription);
        }

        mNextButton = view.findViewById(R.id.next_button);
        if (null != mNextButton) {
            mNextButton.setOnClickListener(this);
        }
        mPreviousButton = view.findViewById(R.id.previous_button);
        if (null != mPreviousButton) {
            mPreviousButton.setOnClickListener(this);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        if (null != savedInstanceState) {
            mVideoUrl = savedInstanceState.getString(getString(R.string.video_url_key));
            mDescription = savedInstanceState.getString(getString(R.string.description_key));
            mStepPosition = savedInstanceState.getInt(getString(R.string.current_step_key));
            mTotalSteps = savedInstanceState.getInt(getString(R.string.total_steps_key));

            if (null != mDescriptionTv) {
                mDescriptionTv.setText(mDescription);
            }
            createPlayer();
        }

        if (mStepPosition == 0 && null != mPreviousButton) {
            mPreviousButton.setVisibility(View.INVISIBLE);
        }

        if (dataSet && mStepPosition == mTotalSteps-1 && null != mNextButton) {
            mNextButton.setVisibility(View.INVISIBLE);
        }
    }

    public void setStepData(Context context, String videoUrl, String description, int position, int totalSteps) {
        mContext = context;
        mVideoUrl = videoUrl;
        mDescription = description;
        mStepPosition = position;
        mTotalSteps = totalSteps;

        if ((null == videoUrl || videoUrl.isEmpty()) && (null != mVideoCardview)) {
            mVideoCardview.setVisibility(View.GONE);
        } else if (null != videoUrl && !videoUrl.isEmpty()){
            createPlayer();
        }

        if (null != mDescriptionTv) {
            mDescriptionTv.setText(mDescription);
        }
        dataSet = true;
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
        if (null != mExoPlayer) {
            mExoPlayer.release();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mExoPlayer) {
            mExoPlayer.release();
        }
    }

    private void createPlayer() {
        BandwidthMeter meter = new DefaultBandwidthMeter();
        TrackSelection.Factory trackSelectionFactory =
                new AdaptiveVideoTrackSelection.Factory(meter);

        TrackSelector trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();

        mExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, loadControl);

        Uri uri = Uri.parse(mVideoUrl);
        // Prepare the MediaSource.
        String userAgent = Util.getUserAgent(mContext, TAG);
        MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(
                mContext, userAgent), new DefaultExtractorsFactory(), null, null);

        mExoPlayer.prepare(mediaSource);
        if (null != mPlayerView) {
            mPlayerView.setPlayer(mExoPlayer);
        }
    }


    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.extra_position), mStepPosition);
        int action = OnFragmentInteractionListener.ACTION_INVALID;
        if (view.getId() == R.id.next_button) {
            action = OnFragmentInteractionListener.ACTION_NEXT_STEP;
        } else if (view.getId() == R.id.previous_button) {
            action = OnFragmentInteractionListener.ACTION_PREVIOUS_STEP;
        }

        mListener.onFragmentInteraction(action, bundle);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(getString(R.string.video_url_key), mVideoUrl);
        outState.putString(getString(R.string.description_key), mDescription);
        outState.putInt(getString(R.string.current_step_key), mStepPosition);
        outState.putInt(getString(R.string.total_steps_key), mTotalSteps);

        super.onSaveInstanceState(outState);
    }
}
