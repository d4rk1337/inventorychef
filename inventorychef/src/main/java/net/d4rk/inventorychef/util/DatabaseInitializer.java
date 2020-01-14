package net.d4rk.inventorychef.util;

/**
 * Created by d4rk on 31/03/2018.
 */

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import net.d4rk.inventorychef.database.room.AppDatabase;
import net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.database.dao.Ingredient;
import net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.database.dao.Purchase;
import net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.database.dao.StoragePlace;

import org.joda.time.DateTime;

import java.util.List;

public class DatabaseInitializer {

    private static final String TAG = DatabaseInitializer.class.getName();

    public static void insertStoragePlaceAsync(@NonNull final AppDatabase db,
                                             final StoragePlace storagePlace) {
        StoragePlaceInsertAsync task = new StoragePlaceInsertAsync(db);
        task.execute(storagePlace);
    }

    public static void deleteStoragePlaceAsync(@NonNull final AppDatabase db,
                                              final StoragePlace... storagePlaces) {
        StoragePlaceDeleteAsync task = new StoragePlaceDeleteAsync(db);
        task.execute(storagePlaces);
    }

    public static void reactivateStoragePlacesAsync(@NonNull final AppDatabase db,
                                                  final StoragePlace... storagePlaces) {
        StoragePlaceReactivateAsync task = new StoragePlaceReactivateAsync(db);
        task.execute(storagePlaces);
    }

    public static void insertIngredientAsync(@NonNull final AppDatabase db,
                                             final Ingredient ingredient) {
        IngredientInsertAsync task = new IngredientInsertAsync(db);
        task.execute(ingredient);
    }

    public static void deleteIngredientsAsync(@NonNull final AppDatabase db,
                                              final Ingredient... ingredients) {
        IngredientDeleteAsync task = new IngredientDeleteAsync(db);
        task.execute(ingredients);
    }

    public static void reactivateIngredientsAsync(@NonNull final AppDatabase db,
                                              final Ingredient... ingredients) {
        IngredientReactivateAsync task = new IngredientReactivateAsync(db);
        task.execute(ingredients);
    }



    public static void updateAmountAsync(@NonNull final AppDatabase db,
                                         final Ingredient ingredient) {
        IngredientAmountUpdateAsync task = new IngredientAmountUpdateAsync(db);
        task.execute(ingredient);
    }

//    public static void insertOrUpdatePurchase(@NonNull final AppDatabase db,
//                                              final Purchase purchase) {
//        PurchaseAsync task = new PurchaseAsync(db);
//        task.execute(purchase);
//    }

    private static void addStoragePlace(
            final AppDatabase db,
            StoragePlace storagePlace
    ) {
        Log.d(TAG, "(addStoragePlace): insert storage place to database");

        db.storagePlaceDao().insert(storagePlace);
    }

    private static Ingredient addIngredient(final AppDatabase db,
                                            Ingredient ingredient) {
        Log.d(TAG, "(addIngredient): insert ingredient to database");

        long ingredientId = db.ingredientDao().insert(ingredient);

        ingredient.setId(ingredientId);

        return ingredient;
    }

    private static class StoragePlaceInsertAsync extends AsyncTask<StoragePlace, Void, Void> {

        private final AppDatabase mDb;

        StoragePlaceInsertAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final StoragePlace... storagePlaces) {
            if(android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();

            Log.d(TAG, "(doInBackground): insert new storage place to database");

            addStoragePlace(mDb, storagePlaces[0]);

            Log.d(TAG, "(populateWithTestData): ingredient added to list");
            return null;
        }

    }

    private static class StoragePlaceDeleteAsync extends AsyncTask<StoragePlace, Void, Void> {

        private final AppDatabase mDb;

        StoragePlaceDeleteAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final StoragePlace... storagePlaces) {
            if(android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();

            Log.d(TAG, "(doInBackground): set storage place deleted in background");

            for (StoragePlace currentStoragePlace: storagePlaces) {
                currentStoragePlace.setDeleted(true);
                currentStoragePlace.setDeleteTimestamp(new DateTime().getMillis());

                mDb.storagePlaceDao().update(currentStoragePlace);

                Log.d(TAG, "(doInBackground): storage place set deleted");
            }

            return null;
        }

    }

    private static class StoragePlaceReactivateAsync extends AsyncTask<StoragePlace, Void, Void> {

        private final AppDatabase mDb;

        StoragePlaceReactivateAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final StoragePlace... storagePlaces) {
            if(android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();

            Log.d(TAG, "(doInBackground): set storage place reactivated in background");

            for (StoragePlace currentStoragePlace : storagePlaces) {
                currentStoragePlace.setDeleted(false);

                mDb.storagePlaceDao().update(currentStoragePlace);

                Log.d(TAG, "(doInBackground): storage place reactivated");
            }

            return null;
        }

    }

    private static class IngredientInsertAsync extends AsyncTask<Ingredient, Void, Void> {

        private final AppDatabase mDb;

        IngredientInsertAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Ingredient... ingredients) {
            if(android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();

            Log.d(TAG, "(doInBackground): insert test data amount in background");

//            StoragePlace storagePlace = new StoragePlace();
//            storagePlace.setName("Test");
//
//            storagePlace = addStoragePlace(mDb, storagePlace);

            Ingredient ingredient;
//            ingredient.setStorageId(storagePlace.getId());
//            ingredient.setGroup("");
//            ingredient.setName(names[0]);
//            ingredient.setUnit("");

            Log.d(TAG, "(populateWithTestData): ingredient created");

            ingredient = addIngredient(mDb, ingredients[0]);

            Log.d(TAG, "(populateWithTestData): ingredient added to list");
            return null;
        }

    }

    private static class IngredientDeleteAsync extends AsyncTask<Ingredient, Void, Void> {

        private final AppDatabase mDb;

        IngredientDeleteAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Ingredient... ingredients) {
            if(android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();

            Log.d(TAG, "(doInBackground): set ingredient deleted in background");

            for (Ingredient currentIngredient : ingredients) {
                currentIngredient.setDeleted(true);
                currentIngredient.setDeleteTimestamp(new DateTime().getMillis());

                mDb.ingredientDao().updateIngredient(currentIngredient);

                Log.d(TAG, "(doInBackground): ingredient set deleted");
            }

            return null;
        }

    }

    private static class IngredientReactivateAsync extends AsyncTask<Ingredient, Void, Void> {

        private final AppDatabase mDb;

        IngredientReactivateAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Ingredient... ingredients) {
            if(android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();

            Log.d(TAG, "(doInBackground): set ingredient reactivated in background");

            for (Ingredient currentIngredient : ingredients) {
                currentIngredient.setDeleted(false);

                mDb.ingredientDao().updateIngredient(currentIngredient);

                Log.d(TAG, "(doInBackground): ingredient reactivated");
            }

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
