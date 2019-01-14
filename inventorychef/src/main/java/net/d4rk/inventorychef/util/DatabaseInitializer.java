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

    public static void insertIngredientAsync(@NonNull final AppDatabase db,
                                             final String name) {
        IngredientInsertAsync task = new IngredientInsertAsync(db);
        task.execute(name);
    }

    public static void updateAmountAsync(@NonNull final AppDatabase db,
                                         final Ingredient ingredient) {
        IngredientAmountUpdateAsync task = new IngredientAmountUpdateAsync(db);
        task.execute(ingredient);
    }

    public static void populateSync(@NonNull final AppDatabase db) {
        populateWithTestData(db);
    }

    private static Ingredient addIngredient(final AppDatabase db,
                                            Ingredient ingredient) {
        Log.d(TAG, "(addIngredient): insert ingredient to database");

        db.ingredientDao().insertAll(ingredient);
        return ingredient;
    }

    private static void populateWithTestData(AppDatabase db) {
        Ingredient ingredient = new Ingredient();
        ingredient.setGroup("Gemüse");
        ingredient.setName("Kartoffel");
        ingredient.setUnit("kg");

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

    private static class IngredientInsertAsync extends AsyncTask<String, Void, Void> {

        private final AppDatabase mDb;

        IngredientInsertAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final String... names) {
            if(android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();

            Log.d(TAG, "(doInBackground): insert test data amount in background");

            Ingredient ingredient = new Ingredient();
            ingredient.setGroup("");
            ingredient.setName(names[0]);
            ingredient.setUnit("");

            Log.d(TAG, "(populateWithTestData): ingredient created");

            addIngredient(mDb, ingredient);

            Log.d(TAG, "(populateWithTestData): ingredient added to list");
            return null;
        }

    }

    private static class IngredientAmountUpdateAsync extends AsyncTask<Ingredient, Void, Void> {

        private final AppDatabase mDb;

        IngredientAmountUpdateAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Ingredient... ingredients) {
            if(android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();

            Log.d(TAG, "(doInBackground): update test data amount in background");

            Ingredient ingredient = ingredients[0];

            mDb.ingredientDao().updateAmount(ingredient.getId(), ingredient.getAmount());
            return null;
        }

    }
}
