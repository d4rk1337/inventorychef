package net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.database.dao;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import net.d4rk.inventorychef.database.room.AppDatabase;

import java.util.List;

public class StoragePlaceRepository {

    private StoragePlaceDao mStoragePlaceDao;
    private LiveData<List<StoragePlace>> mAllActiveStoragePlaces;

    StoragePlaceRepository(Application application) {
        AppDatabase db = AppDatabase.getAppDatabase(application);

        mStoragePlaceDao = db.storagePlaceDao();
        mAllActiveStoragePlaces = mStoragePlaceDao.getAllActiveStoragePlacesAsLiveData();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<StoragePlace>> getAllActiveStoragePlaces() {
        return mAllActiveStoragePlaces;
    }

    boolean isStoragePlaceNameTaken(String name) {
        return mStoragePlaceDao.isStorageNameTaken(name);
    }
}
