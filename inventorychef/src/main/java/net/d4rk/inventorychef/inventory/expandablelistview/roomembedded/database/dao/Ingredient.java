package net.d4rk.inventorychef.inventory.expandablelistview.roomembedded.database.dao;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by d4rk on 30/03/2018.
 */
    @Entity(foreignKeys = @ForeignKey(
            entity = StoragePlace.class,
            childColumns = "storageId",
            parentColumns = "id",
            onDelete = ForeignKey.SET_NULL),
            indices = {@Index("storageId")}
    )
    public class Ingredient {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private long storageId;
    private String name;
    private String group;
    private String unit;
    private int amount = 0;
    private Integer priority;
    @NonNull
    private boolean deleted = false;
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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
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
