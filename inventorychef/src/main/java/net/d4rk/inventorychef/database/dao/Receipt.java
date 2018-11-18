package net.d4rk.inventorychef.database.dao;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by d4rk on 30/03/2018.
 */

    @Entity(tableName = "Receipt")
    public class Receipt {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "Name")
    private String name;

    @ColumnInfo(name = "PreparationTime")
    private String preparationTime;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(String preparationTime) {
        this.preparationTime = preparationTime;
    }

}
