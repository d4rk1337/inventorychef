package net.d4rk.inventorychef.database.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import net.d4rk.inventorychef.database.dao.Ingredient;
import net.d4rk.inventorychef.database.dao.IngredientDao;

/**
 * Created by d4rk on 30/03/2018.
 */

@Database(entities = {Ingredient.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = AppDatabase.class.getSimpleName();

    private static AppDatabase INSTANCE;

    public abstract IngredientDao ingredientDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                INSTANCE =
                        Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "inventorychef.db")
                                // allow queries on the main thread.
                                // Don't do this on a real app! See PersistenceBasicSample for an example.
                                .allowMainThreadQueries()
                                .build();

                Log.d(TAG, "(AppDatabase): instance initialized");
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE.close();

        INSTANCE = null;
    }
}