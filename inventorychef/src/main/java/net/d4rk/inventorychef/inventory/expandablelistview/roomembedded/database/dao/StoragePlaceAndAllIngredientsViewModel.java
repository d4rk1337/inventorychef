package net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.database.dao;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class StoragePlaceAndAllIngredientsViewModel
        extends AndroidViewModel {

    private StoragePlaceAndAllIngredientsRepository mStoragePlaceRepository;

    private LiveData<List<StoragePlaceAndAllIngredients>> mAllStoragePlacesAndIngredients;

    public StoragePlaceAndAllIngredientsViewModel(Application application) {
        super(application);

        mStoragePlaceRepository = new StoragePlaceAndAllIngredientsRepository(application);
        mAllStoragePlacesAndIngredients = mStoragePlaceRepository.getAllStoragePlacesAndIngredients();
    }

    public LiveData<List<StoragePlaceAndAllIngredients>> getAllStoragePlacesAndIngredients() {
        return mAllStoragePlacesAndIngredients;
    }
}
