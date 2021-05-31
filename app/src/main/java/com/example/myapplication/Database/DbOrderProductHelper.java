package com.example.myapplication.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbOrderProductHelper extends SQLiteOpenHelper {
    public static final String DbProduct = "Product.db";

    public DbOrderProductHelper(@Nullable Context context) {
        super(context, DbProduct, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists Order_Product (id_order TEXT, id_product, amount INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Order_Product");
    }

    public Boolean insertData(String id_order, String id_product, int amount)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("id_order", id_order);
        contentValues.put("id_product", id_product);
        contentValues.put("amount", amount);

        long result = MyDB.insert("Order_Product", null, contentValues);
        if(result == 1) return false;
        else return true;
    }
}
