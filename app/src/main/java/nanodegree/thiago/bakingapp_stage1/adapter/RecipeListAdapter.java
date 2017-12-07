package nanodegree.thiago.bakingapp_stage1.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import nanodegree.thiago.bakingapp_stage1.R;
import nanodegree.thiago.bakingapp_stage1.data.RecipeJson;

/**
 * Created by thiagom on 12/3/17.
 */

public class RecipeListAdapter extends
                RecyclerView.Adapter<RecipeListAdapter.RecipeListItemViewHolder> {

    private ArrayList<RecipeJson> mRecipesList = new ArrayList<>();
    private Context mContext;
    private RecipeOnClickListener mListener;

    public RecipeListAdapter(Context context, RecipeOnClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    public RecipeListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recipe_item, parent, false);
        return new RecipeListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeListItemViewHolder holder, int position) {
        RecipeJson recipe = mRecipesList.get(position);
        String recipeImage = recipe.getImage();
        if (null != recipe && null != recipeImage && !recipeImage.isEmpty()) {
            Uri uri = Uri.parse(recipeImage);

            Picasso.with(mContext)
                    .load(uri)
                    .placeholder(R.drawable.food)
                    .into(holder.recipeImage);
        }

        holder.recipeCard.setTag(position);
        holder.recipeName.setText(recipe.getName());
        holder.recipeServings.setText(""+recipe.getServings());
    }

    @Override
    public int getItemCount() {
        if (null == mRecipesList) {
            return 0;
        }
        return mRecipesList.size();
    }

    public void setRecipesList(List list) {
        mRecipesList = new ArrayList<>(list);
        notifyDataSetChanged();
    }

    public List<RecipeJson> getRecipesList() {
        return mRecipesList;
    }

    public RecipeJson getRecipeAt(int position) {
        if (null != mRecipesList) {
            return mRecipesList.get(position);
        }
        return null;
    }

    public interface RecipeOnClickListener {
        public void onRecipeClicked(View view);
    }

    class RecipeListItemViewHolder extends RecyclerView.ViewHolder
                                    implements View.OnClickListener {

        private CardView recipeCard;
        private ImageView recipeImage;
        private TextView recipeName;
        private TextView recipeServings;

        public RecipeListItemViewHolder(View itemView) {
            super(itemView);

            recipeCard = (CardView)itemView.findViewById(R.id.recipe_card);
            recipeImage = (ImageView)itemView.findViewById(R.id.recipe_image);
            recipeName = (TextView)itemView.findViewById(R.id.recipe_name_textview);
            recipeServings = (TextView)itemView.findViewById(R.id.recipe_servings_textview);

            recipeCard.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (null != mListener) {
                mListener.onRecipeClicked(view);
            }
        }
    }
}
