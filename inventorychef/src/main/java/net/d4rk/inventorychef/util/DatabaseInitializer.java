package net.d4rk.inventorychef.util;

/**
 * Created by d4rk on 31/03/2018.
 */

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import net.d4rk.inventorychef.database.dao.Ingredient;
import net.d4rk.inventorychef.database.dao.Purchase;
import net.d4rk.inventorychef.database.room.AppDatabase;

import org.joda.time.DateTime;

import java.util.List;

public class DatabaseInitializer {

    private static final String TAG = DatabaseInitializer.class.getName();

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

    public static void insertOrUpdatePurchase(@NonNull final AppDatabase db,
                                              final Purchase purchase) {
        PurchaseAsync task = new PurchaseAsync(db);
        task.execute(purchase);
    }

    private static Ingredient addIngredient(final AppDatabase db,
                                            Ingredient ingredient) {
        Log.d(TAG, "(addIngredient): insert ingredient to database");

        db.ingredientDao().insertAll(ingredient);
        return ingredient;
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

//            Ingredient ingredient = ingredients[0];

//            mDb.ingredientDao().updateAmount(ingredient.getId(), ingredient.getAmount());
            mDb.ingredientDao().updateIngredients(ingredients);

            return null;
        }

    }

    private static class PurchaseAsync extends AsyncTask<Purchase, Void, Void> {

        private final AppDatabase mDb;

        PurchaseAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Purchase... purchases) {
            if(android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();

            Log.d(TAG, "(doInBackground): insert or update last purchase for ingredient");

            Purchase purchase = purchases[0];
            Purchase duplicate = null;

            DateTime purchaseTimestamp = new DateTime(purchase.getPurchaseTimestamp());
            DateTime purchaseStartTimestamp = purchaseTimestamp.minusDays(1);

            List<Purchase> duplicates = mDb.purchaseDao().findByIngredientIdPurchaseIntervalSortByPurchaseTimestamp(purchase.getIngredientId(), purchaseStartTimestamp.getMillis(), purchaseTimestamp.getMillis());

            switch (duplicates.size()) {
                case 0:
                    mDb.purchaseDao().insertAll(purchase);
                    break;

                default:
                    duplicate = duplicates.get(0);

                    mDb.purchaseDao().updateAmount(duplicate.getId(), purchase.getAmount());
            }

            mDb.ingredientDao().updatePriorities();

            return null;
        }

    }
}
