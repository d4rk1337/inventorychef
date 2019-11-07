package net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by d4rk on 30/03/2018.
 */

@Dao
public interface PurchaseDao {

    @Query("SELECT * FROM Purchase")
    List<net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.database.dao.Purchase> getAll();

    @Query("SELECT * FROM Purchase WHERE IngredientID = :ingredientId")
    List<net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.database.dao.Purchase> getAllByIngredientId(long ingredientId);

    @Query("SELECT * FROM Purchase where PurchaseTimestamp BETWEEN :purchaseStart AND :purchaseEnd")
    List<net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.database.dao.Purchase> findByPurchaseInterval(long purchaseStart,
                                                                                                                        long purchaseEnd);

    @Query("SELECT * from Purchase WHERE IngredientID = :ingredientId AND PurchaseTimestamp BETWEEN :purchaseStart AND :purchaseEnd ORDER BY PurchaseTimestamp DESC")
    List<net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.database.dao.Purchase> findByIngredientIdPurchaseIntervalSortByPurchaseTimestamp(long ingredientId,
                                                                                                                                                           long purchaseStart,
                                                                                                                                                           long purchaseEnd);

    @Query("SELECT * FROM Purchase where id LIKE :id")
    net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.database.dao.Purchase findById(String id);

    @Query("SELECT COUNT(*) from Purchase WHERE IngredientID = :ingredientId")
    int countPurchasesByIngredientId(long ingredientId);

    @Insert
    void insertAll(net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.database.dao.Purchase... purchases);

    @Query("UPDATE Purchase SET Amount = Amount + :amount WHERE id = :id")
    void updateAmount(long id,
                      long amount);

    @Delete
    void delete(net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.database.dao.Purchase purchase);
}
