package net.d4rk.inventorychef.database.dao;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by d4rk on 30/03/2018.
 */
    @Entity(tableName = "Ingredient")
    public class Ingredient {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "StorageId")
    private long storageId;

    @ColumnInfo(name = "Name")
    private String name;

    @ColumnInfo(name = "Group")
    private String group;

    @ColumnInfo(name = "Unit")
    private String unit;

    @ColumnInfo(name = "Amount")
    private int amount = 0;

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

    public long getStorageId() {
        return storageId;
    }

    public void setStorageId(long storageId) {
        this.storageId = storageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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
