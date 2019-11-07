package net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.database.dao;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class StoragePlaceAndAllIngredients {

    // planned feature: collapse group and hide items
    // private boolean mVisible = true;

    // getter/setter mVisible

    // public int getVisibleCount() {
    // if (mVisible == true) {
    // return mIngredients.size() + 1;
    // } else {
    // return 1;
    // }

    @Embedded
    StoragePlace mStoragePlace;

    @Relation(parentColumn = "id", entityColumn = "storageId", entity = Ingredient.class)
    private List<Ingredient> mIngredients;

    public StoragePlace getStoragePlace() {
        return mStoragePlace;
    }

    public void setStoragePlace(StoragePlace storagePlace) {
        this.mStoragePlace = storagePlace;
    }

    public List<Ingredient> getIngredients() {
        return mIngredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.mIngredients = ingredients;
    }
}
