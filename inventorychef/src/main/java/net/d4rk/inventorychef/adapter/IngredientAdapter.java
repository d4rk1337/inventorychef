package net.d4rk.inventorychef.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import net.d4rk.inventorychef.R;
import net.d4rk.inventorychef.database.dao.Ingredient;
import net.d4rk.inventorychef.database.room.AppDatabase;
import net.d4rk.inventorychef.util.DatabaseInitializer;

import java.util.Collections;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientHolder> {

    class IngredientHolder extends RecyclerView.ViewHolder {
        private final TextView amount;
        private final TextView name;
        private final ImageButton increaseAmount;
        private final SeekBar amountBar;

        private IngredientHolder(View rowIngredient) {
            super(rowIngredient);
            this.amount = rowIngredient.findViewById(R.id.ingredient_amount);
            this.name = rowIngredient.findViewById(R.id.ingredient_name);
            this.increaseAmount = rowIngredient.findViewById(R.id.ingredient_increaseamount);
            this.amountBar = rowIngredient.findViewById(R.id.ingredient_amountbar);
        }
    }

    private final LayoutInflater mInflater;
    private List<Ingredient> mIngredients = Collections.emptyList(); // Cached copy of words

    public IngredientAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public IngredientHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View itemView = mInflater.inflate(R.layout.row_ingredients, parent, false);
        return new IngredientHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final IngredientHolder holder,
                                 int position) {
        final Ingredient current = mIngredients.get(position);

        holder.amount.setText(String.valueOf(current.getAmount()));
        holder.name.setText(current.getName());

        holder.amount.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                holder.amountBar.setVisibility(View.VISIBLE);
                holder.amountBar.requestFocus();
                holder.amountBar.bringToFront();
                holder.amountBar.findFocus();
                holder.amountBar.performClick();

                if (holder.amount.isFocused()) {
                    holder.amount.clearFocus();
                }
                holder.amountBar.requestFocusFromTouch();

                holder.amountBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int amount, boolean b) {
                        holder.amount.setText(Integer.toString(amount));
                        current.setAmount(amount);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        seekBar.setVisibility(View.GONE);
                        DatabaseInitializer.updateAmountAsync(AppDatabase.getAppDatabase(holder.amount.getContext()), current);
                    }
                });

                return false;
            }
        });

        holder.increaseAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current.setAmount(current.getAmount() + 1);
                notifyDataSetChanged();

                DatabaseInitializer.updateAmountAsync(AppDatabase.getAppDatabase(holder.amount.getContext()), current);
            }
        });
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
