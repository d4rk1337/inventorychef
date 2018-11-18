package net.d4rk.inventorychef.util;

/**
 * Created by d4rk on 31/03/2018.
 */

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import net.d4rk.inventorychef.database.dao.Ingredient;
import net.d4rk.inventorychef.database.room.AppDatabase;

import java.util.List;

public class DatabaseInitializer {

    private static final String TAG = DatabaseInitializer.class.getName();

    public static void populateAsync(@NonNull final AppDatabase db) {
        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    public static void populateSync(@NonNull final AppDatabase db) {
        populateWithTestData(db);
    }

    private static Ingredient addIngredient(final AppDatabase db, Ingredient ingredient) {
        Log.d(TAG, "(addIngredient): insert ingredient to database");

        db.ingredientDao().insertAll(ingredient);
        return ingredient;
    }

    private static void populateWithTestData(AppDatabase db) {
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientGroup("Gemüse");
        ingredient.setIngredientName("Kartoffel");
        ingredient.setIngredientUnit("kg");

        Log.d(TAG, "(populateWithTestData): ingredient created");

        addIngredient(db, ingredient);

        Log.d(TAG, "(populateWithTestData): ingredient added to list");

//        LiveData<List<Ingredient>> ingredientList = db.ingredientDao().getAll();
//
//        Log.d(TAG, "(populateWithTestData): Rows Count: " + ingredientList.getValue().size());
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;

        PopulateDbAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            Log.d(TAG, "(doInBackground): create test data in background");

            populateWithTestData(mDb);
            return null;
        }

    }
}
