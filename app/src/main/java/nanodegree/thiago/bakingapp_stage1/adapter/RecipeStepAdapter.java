package nanodegree.thiago.bakingapp_stage1.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import nanodegree.thiago.bakingapp_stage1.R;
import nanodegree.thiago.bakingapp_stage1.data.RecipeJson;

/**
 * Created by thiagom on 12/4/17.
 */

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.RecipeStepViewHolder> {

    private ArrayList<RecipeJson.StepsBean> mSteps;
    private Context mContext;
    private OnStepClickListener mListener;

    public RecipeStepAdapter (Context context, OnStepClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    public RecipeStepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.select_step_item, parent, false);

        return new RecipeStepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeStepViewHolder holder, int position) {
        RecipeJson.StepsBean step = mSteps.get(position);

        holder.itemView.setTag(position);
        String url = step.getThumbnailURL();
        if (null != url && !url.isEmpty()) {
            Uri uri = Uri.parse(url);

            Picasso.with(mContext)
                    .load(uri)
                    .placeholder(R.drawable.food)
                    .into(holder.stepThumbnail);
        }
        holder.stepShortDescription.setText(step.getShortDescription());
    }

    @Override
    public int getItemCount() {
        if (null == mSteps) {
            return 0;
        }
        return mSteps.size();
    }

    public void setSteps(List<RecipeJson.StepsBean> list) {
        mSteps = new ArrayList<>(list);
        notifyDataSetChanged();
    }

    class RecipeStepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView stepThumbnail;
        private TextView stepShortDescription;

        public RecipeStepViewHolder(View itemView) {
            super(itemView);

            stepThumbnail = (ImageView)itemView.findViewById(R.id.step_thumbnail);
            stepShortDescription = (TextView)itemView.findViewById(R.id.step_short_description);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (null != mListener) {
                mListener.onItemClicked((Integer)view.getTag());
            }
        }
    }

    public interface OnStepClickListener {
        public void onItemClicked(int position);
    }
}
