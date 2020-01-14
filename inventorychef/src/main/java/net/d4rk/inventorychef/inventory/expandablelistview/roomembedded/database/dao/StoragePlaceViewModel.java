package net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.database.dao;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import net.d4rk.inventorychef.database.room.AppDatabase;
import net.d4rk.inventorychef.util.DatabaseInitializer;

import java.util.List;

public class StoragePlaceViewModel
        extends AndroidViewModel {

    private Application mApplication = null;

    private StoragePlaceRepository mStoragePlaceRepository;

    private LiveData<List<StoragePlace>> mAllActiveStoragePlaces;

    public StoragePlaceViewModel(Application application) {
        super(application);

        mApplication = application;

        mStoragePlaceRepository = new StoragePlaceRepository(application);
        mAllActiveStoragePlaces = mStoragePlaceRepository.getAllActiveStoragePlaces();
    }

    public LiveData<List<StoragePlace>> getAllActiveStoragePlaces() {
        return mAllActiveStoragePlaces;
    }

    public boolean isStoragePlaceNameTaken(String name) {
        return mStoragePlaceRepository.isStoragePlaceNameTaken(name);
    }

    public void insertNewStoragePlace(StoragePlace newStoragePlace) {
        DatabaseInitializer.insertStoragePlaceAsync(AppDatabase.getAppDatabase(mApplication), newStoragePlace);
    }
}
