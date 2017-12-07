package nanodegree.thiago.bakingapp_stage1.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nanodegree.thiago.bakingapp_stage1.R;
import nanodegree.thiago.bakingapp_stage1.data.RecipeJson;

/**
 * Created by thiagom on 12/6/17.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {

    private ArrayList<RecipeJson.IngredientsBean> mIngredients;

    @Override
    public IngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.ingredient_item, parent, false);

        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientsViewHolder holder, int position) {
        RecipeJson.IngredientsBean ingredient = mIngredients.get(position);

        holder.ingredientTitle.setText(ingredient.getIngredient());
        holder.ingredientQuantity.setText(""+ingredient.getQuantity());
        holder.ingredientMeasure.setText(ingredient.getMeasure());
    }

    @Override
    public int getItemCount() {
        if (null == mIngredients) {
            return 0;
        }
        return mIngredients.size();
    }

    public void setIngredients(List<RecipeJson.IngredientsBean> list) {
        mIngredients = new ArrayList<>(list);
        notifyDataSetChanged();
    }

    class IngredientsViewHolder extends RecyclerView.ViewHolder {

        private TextView ingredientTitle;
        private TextView ingredientQuantity;
        private TextView ingredientMeasure;

        public IngredientsViewHolder(View itemView) {
            super(itemView);

            ingredientTitle = itemView.findViewById(R.id.ingredient_textview);
            ingredientQuantity = itemView.findViewById(R.id.quantity_textview);
            ingredientMeasure = itemView.findViewById(R.id.unity_textview);
        }
    }
}
