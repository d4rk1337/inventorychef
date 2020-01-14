package net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import net.d4rk.inventorychef.R;
import net.d4rk.inventorychef.database.room.AppDatabase;
import net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.database.dao.Ingredient;
import net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.database.dao.Purchase;
import net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.database.dao.StoragePlace;
import net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.database.dao.StoragePlaceAndAllIngredients;
import net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.model.PositionMapping;
import net.d4rk.inventorychef.util.DatabaseInitializer;

import org.joda.time.DateTime;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public abstract class ExpandableInventoryAdapter<
        SVH extends ExpandableInventoryAdapter.StorageViewHolder,
        IVH extends ExpandableInventoryAdapter.IngredientViewHolder
        >
        extends RecyclerView.Adapter {

    private static final String TAG = ExpandableInventoryAdapter.class.getSimpleName();

    private static final int TYPE_STORAGE = 0;
    private static final int TYPE_INGREDIENT = 1;

    List<StoragePlaceAndAllIngredients> mStoragePlacesAndIngredients = Collections.emptyList();

    SparseIntArray mGroupIndexList = new SparseIntArray();
    SparseArray<PositionMapping> mElementIndexList = new SparseArray<>();

    ExpandableInventoryAdapter() {
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        switch (viewType) {
            case TYPE_STORAGE:
                View inventoryView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_storageplaces, parent, false);
                return new StorageViewHolder(inventoryView);
            case TYPE_INGREDIENT:
//                View ingredientView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ingredients, parent, false);
                View ingredientView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ingredients_swipe, parent, false);
                return new IngredientViewHolder(ingredientView);
            default:
                throw new IllegalArgumentException("Given viewType is not valid");
        }
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecyclerView.ViewHolder viewHolder,
            int position
    ) {
        switch (getItemViewType(position)) {
            case TYPE_STORAGE:
                StoragePlace currentStoragePlace = mStoragePlacesAndIngredients.get(mGroupIndexList.get(position)).getStoragePlace();
                ((SVH) viewHolder).setStorageTitle(currentStoragePlace.getName());
                break;
            case TYPE_INGREDIENT:
                Ingredient currentIngredient = mStoragePlacesAndIngredients.get(mElementIndexList.get(position).getGroupIndex()).getIngredients().get(mElementIndexList.get(position).getChildIndex());

                IVH ingredientViewHolder = (IVH) viewHolder;

                ingredientViewHolder.itemView.setTag(currentIngredient);
                ingredientViewHolder.initialize();
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (mGroupIndexList.get(position, -1) != -1) {
            return TYPE_STORAGE;
        } else if (mElementIndexList.get(position) != null) {
            return TYPE_INGREDIENT;
        } else {
            throw new RuntimeException("Position out of range");
        }
    }

    @Override
    public int getItemCount() {
        int visibleCount = 0;

        for (StoragePlaceAndAllIngredients inventoryElement : mStoragePlacesAndIngredients) {
            // get visible count of all Inventory elements in the list
            visibleCount += inventoryElement.getIngredients().size() + 1;
        }

        return visibleCount;
    }

    class StorageViewHolder extends RecyclerView.ViewHolder {

        TextView mStorageTitle;

        StorageViewHolder(View itemView) {
            super(itemView);

            mStorageTitle = itemView.findViewById(R.id.storage_name);
        }

        void setStorageTitle(String storageTitle) {
            mStorageTitle.setText(storageTitle);
        }

    }

    public class IngredientViewHolder
            extends RecyclerView.ViewHolder {

        public RelativeLayout viewBackground, viewForeground;

        EditText mAmount;
        TextView mName;
        ImageButton mIncreaseAmount;
        SeekBar mAmountBar;

        IngredientViewHolder(View itemView) {
            super(itemView);

            this.viewBackground = itemView.findViewById(R.id.ingredient_background);
            this.viewForeground = itemView.findViewById(R.id.ingredient_foreground);

            this.mAmount = itemView.findViewById(R.id.ingredient_amount);
            this.mName = itemView.findViewById(R.id.ingredient_name);
            this.mIncreaseAmount = itemView.findViewById(R.id.ingredient_increaseamount);
            this.mAmountBar = itemView.findViewById(R.id.ingredient_amountbar);
        }

        void initialize() {
            Ingredient currentIngredient = (Ingredient) itemView.getTag();

            setIngredientAmount(currentIngredient.getAmount());
            setIngredientName(currentIngredient.getName());

            mAmount.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        int previousAmount = ((Ingredient) itemView.getTag()).getAmount();

                        try {
                            ((Ingredient) itemView.getTag()).setAmount(Integer.parseInt(v.getText().toString()));

                            DatabaseInitializer.updateAmountAsync(AppDatabase.getAppDatabase(mAmount.getContext()), (Ingredient) itemView.getTag());

//                            int amountDifference = ((Ingredient) itemView.getTag()).getAmount() - previousAmount;
//
//                            if (amountDifference > 0) {
//                                Purchase newPurchase = new Purchase();
//                                newPurchase.setIngredientId(((Ingredient) itemView.getTag()).getId());
//                                newPurchase.setAmount(amountDifference);
//                                newPurchase.setPurchaseTimestamp(new DateTime().getMillis());
//
//                                DatabaseInitializer.insertOrUpdatePurchase(AppDatabase.getAppDatabase(mAmount.getContext()), newPurchase);
//                            }

                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                            if (imm != null) {
                                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            }

                            if (mAmountBar.getVisibility() == View.VISIBLE) {
                                mAmountBar.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "(onEditorAction): amount update not valid");
                        }
                        return true;
                    }
                    return false;
                }
            });

            mAmount.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View view) {
                    mAmountBar.setVisibility(View.VISIBLE);
                    mAmountBar.setProgress(((Ingredient) itemView.getTag()).getAmount());

                    if (mAmount.isFocused()) {
                        mAmount.clearFocus();
                    }

                    mAmountBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        int mPreviousAmount = 0;

                        @Override
                        public void onProgressChanged(
                                SeekBar seekBar,
                                int amountProgress,
                                boolean fromUser
                        ) {
                            if (mPreviousAmount == 0) {
                                mPreviousAmount = ((Ingredient) itemView.getTag()).getAmount();
                            }

                            mAmount.setText(String.valueOf(amountProgress));
                            ((Ingredient) itemView.getTag()).setAmount(amountProgress);
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

                            DatabaseInitializer.updateAmountAsync(AppDatabase.getAppDatabase(mAmount.getContext()), (Ingredient) itemView.getTag());

//                            int amountDifference = ((Ingredient) itemView.getTag()).getAmount() - mPreviousAmount;
//
//                            if (amountDifference > 0) {
//                                Purchase newPurchase = new Purchase();
//
//                                newPurchase.setIngredientId(((Ingredient) itemView.getTag()).getId());
//                                newPurchase.setAmount(amountDifference);
//                                newPurchase.setPurchaseTimestamp(new DateTime().getMillis());
//
//                                DatabaseInitializer.insertOrUpdatePurchase(AppDatabase.getAppDatabase(mAmount.getContext()), newPurchase);
//                            }
                        }
                    });

                    return false;
                }
            });

            mIncreaseAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((Ingredient) itemView.getTag()).setAmount(((Ingredient) itemView.getTag()).getAmount() + 1);

                    DatabaseInitializer.updateAmountAsync(AppDatabase.getAppDatabase(mAmount.getContext()), (Ingredient) itemView.getTag());

//                    Purchase newPurchase = new Purchase();
//                    newPurchase.setIngredientId(((Ingredient) itemView.getTag()).getId());
//                    newPurchase.setAmount(1);
//                    newPurchase.setPurchaseTimestamp(new DateTime().getMillis());
//
//                    DatabaseInitializer.insertOrUpdatePurchase(AppDatabase.getAppDatabase(mAmount.getContext()), newPurchase);
                }
            });
        }

        void setIngredientAmount(int ingredientAmount) {
            mAmount.setText(String.format(Locale.getDefault(), "%d", ingredientAmount));
        }

        void setIngredientName(String ingredientName) {
            mName.setText(ingredientName);
        }
    }
}
