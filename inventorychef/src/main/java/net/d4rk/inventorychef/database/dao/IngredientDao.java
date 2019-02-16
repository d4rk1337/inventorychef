package net.d4rk.inventorychef.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by d4rk on 30/03/2018.
 */

@Dao
public interface IngredientDao {

    @Query("SELECT * FROM Ingredient")
    LiveData<List<Ingredient>> getAll();

    @Query("SELECT * FROM Ingredient ORDER BY Priority DESC")
    LiveData<List<Ingredient>> getAllSortByPriority();

    @Query("SELECT * FROM Ingredient ORDER BY Priority DESC, Amount")
    LiveData<List<Ingredient>> getAllSortByPriorityAmount();

    @Query("SELECT * FROM Ingredient where id LIKE :id")
    Ingredient findById(String id);

    @Query("SELECT * FROM Ingredient where Name LIKE :name")
    Ingredient findByName(String name);

    @Query("SELECT COUNT(*) from Ingredient")
    int countIngredients();

    @Insert
    void insertAll(Ingredient... ingredients);

//    @Query("UPDATE Ingredient SET Amount = :amount WHERE id = :id")
//    void updateAmount(long id, long amount);

    @Update
    void updateIngredients(Ingredient... ingredients);


    @Query("WITH AllPurchases AS (SELECT IngredientID, COUNT(id) AS PurchasesCount FROM Purchase GROUP BY IngredientID) UPDATE Ingredient SET Priority = (SELECT PurchasesCount FROM AllPurchases WHERE IngredientID = Ingredient.id)")
    void updatePriorities();

    @Delete
    void delete(Ingredient ingredient);
}
