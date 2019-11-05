package net.d4rk.inventorychef.inventory.expandablelistview.storageplaceingredientlist.model;

import net.d4rk.inventorychef.database.dao.Ingredient;
import net.d4rk.inventorychef.database.dao.StoragePlace;

import java.util.List;

public class Inventory {//implements Parent<Ingredient> {

    private StoragePlace mStoragePlace;
    private List<Ingredient> mIngredients;

    private boolean mIsVisible = true;

    public Inventory(
            StoragePlace storagePlace,
            List<Ingredient> ingredients
    ) {
        mStoragePlace = storagePlace;
        mIngredients = ingredients;
    }

    public StoragePlace getStoragePlace() {
        return mStoragePlace;
    }

    public Ingredient getIngredient(int position) {
        return mIngredients.get(position);
    }

    public int getVisibleCount() {
        if (mIsVisible) {
            // all ingredients and group title are visible
            return mIngredients.size() + 1;
        } else {
            // only group title visible
            return 1;
        }
    }
}
