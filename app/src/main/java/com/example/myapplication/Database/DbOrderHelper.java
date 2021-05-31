package com.example.myapplication.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.myapplication.Order;

import java.util.Date;
import java.util.UUID;

public class DbOrderHelper extends SQLiteOpenHelper {
    public static final String DbOrder = "Order.db";

    public DbOrderHelper(@Nullable Context context) {
        super(context, DbOrder, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Chú ý: bảng Orders có s, vì Order trùng với lệnh trong SQL nên phải thêm s cuối
        db.execSQL("create table if not exists Orders (id TEXT primary key, date, customer, phone_number, address, note)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists Orders");
    }

    public Boolean insertData(String id, long date, String customer, String phone_number, String address, String note) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("id", id);
        contentValues.put("date", date);
        contentValues.put("customer", customer);
        contentValues.put("phone_number", phone_number);
        contentValues.put("address", address);
        contentValues.put("note", note);

        long result = MyDB.insert("Orders", null, contentValues);
        if (result == 1) return false;
        else return true;
    }

    public Order getOrderFromDB(String id)
    {
        Order order = new Order(UUID.fromString(id));
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Orders where id = ?", new String[]{id});
        if(cursor.moveToFirst())
        {
            order.setDateOrder(new Date(cursor.getLong(1)));
        }
        return order;
    }

    public void updateCustomerInfo(String id, String customer, String phone_number, String address, String note)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("customer", customer);
        values.put("phone_number", phone_number);
        values.put("address", address);
        values.put("note", note);

        db.update("Orders", values, "id = ? ", new String[]{id});
    }

    public Boolean checkOrderExists(String id)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from Orders where id = ?", new String[]{id});
        if (cursor.getCount() > 0)
            return true;
        else return false;
    }
}
