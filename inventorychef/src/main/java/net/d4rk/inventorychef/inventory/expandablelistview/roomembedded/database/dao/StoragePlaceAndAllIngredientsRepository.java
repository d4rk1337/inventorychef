package net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.database.dao;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import net.d4rk.inventorychef.database.room.AppDatabase;

import java.util.List;

public class StoragePlaceAndAllIngredientsRepository {

    private StoragePlaceDao mStoragePlaceDao;
    private LiveData<List<StoragePlaceAndAllIngredients>> mAllStoragePlacesAndIngredients;

    StoragePlaceAndAllIngredientsRepository(Application application) {
        AppDatabase db = AppDatabase.getAppDatabase(application);

        mStoragePlaceDao = db.storagePlaceDao();
        mAllStoragePlacesAndIngredients = mStoragePlaceDao.getAllStoragePlacesAndIngredients();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<StoragePlaceAndAllIngredients>> getAllStoragePlacesAndIngredients() {
        return mAllStoragePlacesAndIngredients;
    }
}
