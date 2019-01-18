package net.d4rk.inventorychef.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import net.d4rk.inventorychef.R;
import net.d4rk.inventorychef.database.dao.Ingredient;
import net.d4rk.inventorychef.database.room.AppDatabase;
import net.d4rk.inventorychef.util.DatabaseInitializer;

import java.util.Collections;
import java.util.List;

public class IngredientAdapter
        extends RecyclerView.Adapter<IngredientAdapter.IngredientHolder> {

    class IngredientHolder
            extends RecyclerView.ViewHolder {

        private final EditText amount;
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

        holder.amount.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    DatabaseInitializer.updateAmountAsync(AppDatabase.getAppDatabase(holder.amount.getContext()), current);

                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }

                    if (holder.amountBar.getVisibility() == View.VISIBLE) {
                        holder.amountBar.setVisibility(View.GONE);
                    }
                    return true;
                }
                return false;
            }
        });

        holder.amount.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                holder.amountBar.setVisibility(View.VISIBLE);
//                holder.amountBar.requestFocus();
//                holder.amountBar.bringToFront();
//                holder.amountBar.findFocus();
//                holder.amountBar.performClick();
                holder.amountBar.setProgress(current.getAmount());
//
//                long downTime = SystemClock.uptimeMillis();
//                long eventTime = SystemClock.uptimeMillis() + 100;
//                float x = 0.0f;
//                float y = 0.0f;
//// List of meta states found here:     developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
//                int metaState = 0;
//                MotionEvent motionEvent = MotionEvent.obtain(
//                        downTime,
//                        eventTime,
//                        MotionEvent.ACTION_MOVE,
//                        x,
//                        y,
//                        metaState
//                );
//
//// Dispatch touch event to view
//                holder.amountBar.dispatchTouchEvent(motionEvent);
//
                if (holder.amount.isFocused()) {
                    holder.amount.clearFocus();
                }
//                holder.amountBar.requestFocusFromTouch();

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
                        InputMethodManager imm = (InputMethodManager) seekBar.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(seekBar.getWindowToken(), 0);
                        }
                    }
                });

                return false;
            }
        });

        holder.increaseAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current.setAmount(current.getAmount() + 1);

//                notifyDataSetChanged();

                DatabaseInitializer.updateAmountAsync(AppDatabase.getAppDatabase(holder.amount.getContext()), current);
            }
        });
    }

    public void setIngredients(List<Ingredient> ingredients) {
        mIngredients = ingredients;

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }
}
