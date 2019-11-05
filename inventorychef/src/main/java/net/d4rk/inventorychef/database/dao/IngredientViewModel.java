package net.d4rk.inventorychef.database.dao;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class IngredientViewModel
        extends AndroidViewModel {

    private IngredientRepository mIngredientRepository;

    private LiveData<List<Ingredient>> mAllIngredients;

    public IngredientViewModel(Application application) {
        super(application);

        mIngredientRepository = new IngredientRepository(application);
        mAllIngredients = mIngredientRepository.getAllIngredients();
    }

    public LiveData<List<Ingredient>> getAllIngredients() {
        return mAllIngredients;
    }
}
