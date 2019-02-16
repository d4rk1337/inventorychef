package net.d4rk.inventorychef.database.dao;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by d4rk on 30/03/2018.
 */
    @Entity(tableName = "Purchase")
    public class Purchase {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "IngredientID")
    private long ingredientId;

    @ColumnInfo(name = "Amount")
    private int amount;

    @ColumnInfo(name = "PurchaseTimestamp")
    private long purchaseTimestamp;

    @ColumnInfo(name = "Deleted")
    private boolean deleted;

    @ColumnInfo(name = "DeleteTimestamp")
    private long deleteTimestamp;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getPurchaseTimestamp() {
        return purchaseTimestamp;
    }

    public void setPurchaseTimestamp(long purchaseTimestamp) {
        this.purchaseTimestamp = purchaseTimestamp;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public long getDeleteTimestamp() {
        return deleteTimestamp;
    }

    public void setDeleteTimestamp(long deleteTimestamp) {
        this.deleteTimestamp = deleteTimestamp;
    }
}
