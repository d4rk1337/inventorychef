package net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.unused;

import android.arch.persistence.room.Embedded;

import net.d4rk.inventorychef.database.dao.Ingredient;
import net.d4rk.inventorychef.database.dao.StoragePlace;

public class StoragePlaceAndIngredient {

    @Embedded
    StoragePlace mStoragePlace;

    @Embedded
    Ingredient mIngredient;

}
