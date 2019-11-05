package net.d4rk.inventorychef.inventory.expandablelistview.storageplaceingredientlist.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.d4rk.inventorychef.R;
import net.d4rk.inventorychef.database.dao.Ingredient;
import net.d4rk.inventorychef.database.dao.StoragePlace;
import net.d4rk.inventorychef.inventory.expandablelistview.storageplaceingredientlist.model.Inventory;

import java.util.List;

public abstract class ExpandableInventoryAdapter<
        GVH extends ExpandableInventoryAdapter.GroupViewHolder,
        EVH extends ExpandableInventoryAdapter.ElementViewHolder
        >
        extends RecyclerView.Adapter {

    private static final String TAG = ExpandableInventoryAdapter.class.getSimpleName();

    public static final int TYPE_STORAGE = 0;
    public static final int TYPE_INGREDIENT = 1;

//    protected LiveData<List<Inventory>> mInventory;
    private List<Inventory> mInventory;

    private StoragePlace currentStoragePlace;
    private Ingredient currentIngredient;

    public ExpandableInventoryAdapter(
//            LiveData<List<Inventory>> inventory
            List<Inventory> inventory
    ) {
        mInventory = inventory;
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
                View ingredientView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ingredients, parent, false);
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
//        int currentPosition = 0;
//
//        if (position > 0) {
//            for (Inventory inventoryItem : mInventory) {
//
//            }
//        }
        switch (getItemViewType(position)) {
            case 0:
                ((StorageViewHolder) viewHolder).setStorageTitle("Group");
                break;
            case 1:
                ((IngredientViewHolder) viewHolder).setIngredientTitle("Item");
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int maxIngredientPosition = 0;
        int currentIngredientIndex = 0;

        if (position == 0) {
            currentStoragePlace = mInventory.get(0).getStoragePlace();

            return TYPE_STORAGE;
        } else {
            for (int currentGroupIndex = 0; currentGroupIndex < mInventory.size(); currentGroupIndex++) {

                Inventory currentInventory = mInventory.get(currentGroupIndex);

                maxIngredientPosition += currentInventory.getVisibleCount();

                if (position < maxIngredientPosition) {
                    if ((currentIngredientIndex = (maxIngredientPosition - position -1)) == 0) {
                        currentStoragePlace = mInventory.get(currentGroupIndex).getStoragePlace();

                        return TYPE_STORAGE;
                    } else {
                        currentIngredient = mInventory.get(currentGroupIndex).getIngredient(currentIngredientIndex);

                        return TYPE_INGREDIENT;
                    }
                } else {
                    continue;
                }
            }

        }
        throw new RuntimeException("Position out of range");
    }

    @Override
    public int getItemCount() {
        int visibleCount = 0;

//        for (Inventory inventoryElement : mInventory.getValue()) {
        for (Inventory inventoryElement : mInventory) {
            // get visible count of all Inventory elements in the list
            visibleCount += inventoryElement.getVisibleCount();
        }

        return visibleCount;
    }

    abstract class GroupViewHolder extends RecyclerView.ViewHolder {

        GroupViewHolder(View itemView) {
            super(itemView);
        }
    }

    abstract class ElementViewHolder extends RecyclerView.ViewHolder {

        ElementViewHolder(View itemView) {
            super(itemView);
        }
    }

    class StorageViewHolder extends GroupViewHolder {

        TextView mStorageTitle;

        StorageViewHolder(View itemView) {
            super(itemView);

            mStorageTitle = itemView.findViewById(R.id.storage_name);
        }

        void setStorageTitle(String storageTitle) {
            mStorageTitle.setText(storageTitle);
        }

    }

    class IngredientViewHolder extends ElementViewHolder {

        TextView mIngredientTitle;

        IngredientViewHolder(View itemView) {
            super(itemView);

            mIngredientTitle = itemView.findViewById(R.id.ingredient_name);
        }

        void setIngredientTitle(String ingredientTitle) {
            mIngredientTitle.setText(ingredientTitle);
        }
    }
}
