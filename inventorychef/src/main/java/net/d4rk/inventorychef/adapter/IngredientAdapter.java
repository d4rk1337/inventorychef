package net.d4rk.inventorychef.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import net.d4rk.inventorychef.database.dao.Purchase;
import net.d4rk.inventorychef.database.room.AppDatabase;
import net.d4rk.inventorychef.util.DatabaseInitializer;

import org.joda.time.DateTime;

import java.util.Collections;
import java.util.List;

public class IngredientAdapter
        extends RecyclerView.Adapter<IngredientAdapter.IngredientHolder> {

    private static final String TAG = IngredientAdapter.class.getSimpleName();

    private final LayoutInflater mInflater;
    private List<Ingredient> mIngredients = Collections.emptyList(); // Cached copy of words

    public IngredientAdapter(
            Context context
    ) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public IngredientHolder onCreateViewHolder(
            ViewGroup parent,
            int viewType
    ) {
        View itemView = mInflater.inflate(R.layout.row_ingredients, parent, false);

        return new IngredientHolder(itemView);
    }

    @Override
    public void onBindViewHolder(
            final IngredientHolder holder,
            int position
    ) {
        final Ingredient currentIngredient = mIngredients.get(position);

        holder.amount.setText(String.valueOf(currentIngredient.getAmount()));
        holder.name.setText(currentIngredient.getName());

        holder.amount.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    int previousAmount = currentIngredient.getAmount();

                    try {
                        currentIngredient.setAmount(Integer.parseInt(v.getText().toString()));

//                        DatabaseInitializer.updateAmountAsync(AppDatabase.getAppDatabase(holder.amount.getContext()), currentIngredient);

                        int amountDifference = currentIngredient.getAmount() - previousAmount;

                        if (amountDifference > 0) {
                            Purchase newPurchase = new Purchase();
                            newPurchase.setIngredientId(currentIngredient.getId());
                            newPurchase.setAmount(amountDifference);
                            newPurchase.setPurchaseTimestamp(new DateTime().getMillis());

//                            DatabaseInitializer.insertOrUpdatePurchase(AppDatabase.getAppDatabase(holder.amount.getContext()), newPurchase);
                        }

                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }

                        if (holder.amountBar.getVisibility() == View.VISIBLE) {
                            holder.amountBar.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "(onEditorAction): amount update not valid");
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
                holder.amountBar.setProgress(currentIngredient.getAmount());

                if (holder.amount.isFocused()) {
                    holder.amount.clearFocus();
                }

                holder.amountBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    int mPreviousAmount = 0;

                    @Override
                    public void onProgressChanged(
                            SeekBar seekBar,
                            int amountProgress,
                            boolean fromUser
                    ) {
                        if (mPreviousAmount == 0) {
                            mPreviousAmount = currentIngredient.getAmount();
                        }

                        holder.amount.setText(String.valueOf(amountProgress));
                        currentIngredient.setAmount(amountProgress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(
                            SeekBar seekBar
                    ) {
                        seekBar.setVisibility(View.GONE);

                        InputMethodManager imm = (InputMethodManager) seekBar.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(seekBar.getWindowToken(), 0);
                        }

//                        DatabaseInitializer.updateAmountAsync(AppDatabase.getAppDatabase(holder.amount.getContext()), currentIngredient);

                        int amountDifference = currentIngredient.getAmount() - mPreviousAmount;

                        if (amountDifference > 0) {
                            Purchase newPurchase = new Purchase();

                            newPurchase.setIngredientId(currentIngredient.getId());
                            newPurchase.setAmount(amountDifference);
                            newPurchase.setPurchaseTimestamp(new DateTime().getMillis());

//                            DatabaseInitializer.insertOrUpdatePurchase(AppDatabase.getAppDatabase(holder.amount.getContext()), newPurchase);
                        }
                    }
                });

                return false;
            }
        });

        holder.increaseAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentIngredient.setAmount(currentIngredient.getAmount() + 1);

//                DatabaseInitializer.updateAmountAsync(AppDatabase.getAppDatabase(holder.amount.getContext()), currentIngredient);

                Purchase newPurchase = new Purchase();
                newPurchase.setIngredientId(currentIngredient.getId());
                newPurchase.setAmount(1);
                newPurchase.setPurchaseTimestamp(new DateTime().getMillis());

//                DatabaseInitializer.insertOrUpdatePurchase(AppDatabase.getAppDatabase(holder.amount.getContext()), newPurchase);
            }
        });
    }

    public void setIngredients(
            List<Ingredient> ingredients
    ) {
        mIngredients = ingredients;

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    class IngredientHolder
            extends RecyclerView.ViewHolder {

        private final EditText amount;
        private final TextView name;
        private final ImageButton increaseAmount;
        private final SeekBar amountBar;

        private IngredientHolder(
                View rowIngredient
        ) {
            super(rowIngredient);

            this.amount = rowIngredient.findViewById(R.id.ingredient_amount);
            this.name = rowIngredient.findViewById(R.id.ingredient_name);
            this.increaseAmount = rowIngredient.findViewById(R.id.ingredient_increaseamount);
            this.amountBar = rowIngredient.findViewById(R.id.ingredient_amountbar);
        }
    }
}
