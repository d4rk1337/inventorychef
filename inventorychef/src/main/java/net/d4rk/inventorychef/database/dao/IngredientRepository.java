package net.d4rk.inventorychef.database.dao;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import net.d4rk.inventorychef.database.room.AppDatabase;

import java.util.List;

public class IngredientRepository {

    private IngredientDao mIngredientDao;
    private LiveData<List<Ingredient>> mAllIngredients;

    IngredientRepository(Application application) {
        AppDatabase db = AppDatabase.getAppDatabase(application);

        mIngredientDao = db.ingredientDao();
        mAllIngredients = mIngredientDao.getAllSortByPriorityAmount();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Ingredient>> getAllIngredients() {
        return mAllIngredients;
    }
}
