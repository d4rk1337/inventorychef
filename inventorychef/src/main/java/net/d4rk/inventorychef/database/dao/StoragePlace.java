package net.d4rk.inventorychef.database.dao;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by d4rk on 30/03/2018.
 */

    @Entity(tableName = "StoragePlace")
    public class StoragePlace {

    @PrimaryKey()
    private long id;

    @ColumnInfo(name = "Name")
    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
