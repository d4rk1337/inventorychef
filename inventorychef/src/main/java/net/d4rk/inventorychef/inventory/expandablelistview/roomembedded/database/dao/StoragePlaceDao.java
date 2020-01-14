package net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface StoragePlaceDao {

    @Query("SELECT * FROM StoragePlace WHERE deleted = 0")
    List<StoragePlace> getAllActiveStoragePlaces();

    @Query("SELECT StoragePlace.id, StoragePlace.name, StoragePlace.deleted, StoragePlace.deleteTimestamp, COUNT(Ingredient.id) AS ingredientCount FROM StoragePlace LEFT JOIN Ingredient ON Ingredient.storageId = StoragePlace.id WHERE StoragePlace.deleted = 0 GROUP BY StoragePlace.id, StoragePlace.name ORDER BY LOWER(StoragePlace.name)")
    LiveData<List<StoragePlace>> getAllActiveStoragePlacesAsLiveData();

    @Query("SELECT * FROM StoragePlace WHERE id = :storageId")
    StoragePlaceAndAllIngredients loadAllStorageIngredientsByStorageId(long storageId);

    @Query("SELECT * FROM StoragePlace")
    LiveData<List<StoragePlaceAndAllIngredients>> getAllStoragePlacesAndIngredients();

    @Query("SELECT * FROM StoragePlace INNER JOIN Ingredient ON StoragePlace.id = Ingredient.storageId WHERE Ingredient.deleted = 0")
    LiveData<List<StoragePlaceAndAllIngredients>> getAllActiveStoragePlacesAndIngredients();

    @Query("SELECT CASE WHEN COUNT(id) > 0 THEN 1 ELSE 0 END FROM StoragePlace WHERE LOWER(name) = :name")
    boolean isStorageNameTaken(String name);

    @Insert
    long insert(StoragePlace storagePlace);

    @Insert
    void insertAll(StoragePlace... storagePlaces);

    @Update
    void update(StoragePlace storagePlace);

    @Delete
    void delete(StoragePlace storagePlace);

}
