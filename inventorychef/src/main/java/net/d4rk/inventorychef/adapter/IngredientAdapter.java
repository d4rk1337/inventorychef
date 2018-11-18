package net.d4rk.inventorychef.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.d4rk.inventorychef.R;
import net.d4rk.inventorychef.database.dao.Ingredient;

import java.util.Collections;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientHolder> {

    class IngredientHolder extends RecyclerView.ViewHolder {
        private final TextView ingredientNameItemView;

        private IngredientHolder(View itemView) {
            super(itemView);
            ingredientNameItemView = itemView.findViewById(R.id.textView);
        }
    }

    private final LayoutInflater mInflater;
    private List<Ingredient> mIngredients = Collections.emptyList(); // Cached copy of words

    public IngredientAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public IngredientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new IngredientHolder(itemView);
    }

    @Override
    public void onBindViewHolder(IngredientHolder holder, int position) {
        Ingredient current = mIngredients.get(position);
        holder.ingredientNameItemView.setText(current.getGroup());
    }

    public void setIngredients(List<Ingredient> ingredient) {
        mIngredients = ingredient;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }
}
