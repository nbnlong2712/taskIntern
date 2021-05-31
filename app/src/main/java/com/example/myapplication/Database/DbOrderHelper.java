package com.example.myapplication.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbOrderHelper extends SQLiteOpenHelper {
    public static final String DbProduct = "Product.db";

    public DbOrderHelper(@Nullable Context context) {
        super(context, DbProduct, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Chú ý: bảng Orders có s, vì Order trùng với lệnh trong SQL nên phải thêm s cuối
        db.execSQL("create table if not exists Orders (id TEXT primary key, sum_price INTEGER, date, customer, phone_number, address, note)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists Orders");
    }

    public Boolean insertData(String id, int sum_price, long date, String customer, String phone_number, String address, String note)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("id", id);
        contentValues.put("sum_price", sum_price);
        contentValues.put("date", date);
        contentValues.put("customer", customer);
        contentValues.put("phone_number", phone_number);
        contentValues.put("address", address);
        contentValues.put("note", note);

        long result = MyDB.insert("Orders", null, contentValues);
        if(result == 1) return false;
        else return true;
    }
}
