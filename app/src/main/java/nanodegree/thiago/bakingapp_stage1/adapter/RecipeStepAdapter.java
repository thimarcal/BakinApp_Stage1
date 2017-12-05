package nanodegree.thiago.bakingapp_stage1.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

        if (null != step.getThumbnailURL() && !step.getThumbnailURL().isEmpty()) {
            //TODO: Implement Picasso to load the image
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

    class RecipeStepViewHolder extends RecyclerView.ViewHolder {
        private ImageView stepThumbnail;
        private TextView stepShortDescription;

        public RecipeStepViewHolder(View itemView) {
            super(itemView);

            stepThumbnail = (ImageView)itemView.findViewById(R.id.step_thumbnail);
            stepShortDescription = (TextView)itemView.findViewById(R.id.step_short_description);
        }
    }
}
