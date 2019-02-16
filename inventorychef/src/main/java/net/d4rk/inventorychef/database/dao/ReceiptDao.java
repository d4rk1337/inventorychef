package net.d4rk.inventorychef.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by d4rk on 30/03/2018.
 */

@Dao
public interface ReceiptDao {

    @Query("SELECT * FROM Receipt")
    List<Receipt> getAll();

    @Query("SELECT * FROM Receipt WHERE Name LIKE :name")
    List<Receipt> findByName(String name);

    @Query("SELECT COUNT(*) from Receipt")
    int countReceipts();

    @Insert
    void insertAll(Receipt... receipts);

    @Delete
    void delete(Receipt receipt);
}
