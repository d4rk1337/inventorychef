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

    @Query("SELECT * FROM StoragePlace WHERE id = :storageId")
    StoragePlaceAndAllIngredients loadAllStorageIngredientsByStorageId(long storageId);

    @Query("SELECT * FROM StoragePlace")
    LiveData<List<StoragePlaceAndAllIngredients>> getAllStoragePlacesAndIngredients();

    @Insert
    long insert(StoragePlace storagePlace);

    @Insert
    void insertAll(StoragePlace... storagePlaces);

    @Update
    void update(StoragePlace storagePlace);

    @Delete
    void delete(StoragePlace storagePlace);

}
