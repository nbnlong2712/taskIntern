package com.example.myapplication.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.myapplication.Product;

import java.util.ArrayList;
import java.util.List;

public class DbProductHelper extends SQLiteOpenHelper {
    public static final String DbProduct = "Product.db";

    public DbProductHelper(@Nullable Context context) {
        super(context, DbProduct, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table if not exists Product (id TEXT primary key, name, unit, price INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists Product");
    }

    public Boolean insertData(String id, String name, String unit, int price)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("unit", unit);
        contentValues.put("price", price);

        long result = MyDB.insert("Product", null, contentValues);
        if(result == 1) return false;
        else return true;
    }

    public Boolean checkID(String id)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from Product where id = ?", new String[]{id});
        if (cursor.getCount() > 0)
            return true;
        else return false;
    }

    public Boolean deleteProduct(String id)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        int result = MyDB.delete("Product", " id = ?", new String[]{id});
        return result > 1;
    }

    public Boolean checkName(String name)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from Product where name = ?", new String[]{name});
        if (cursor.getCount() > 0)
            return true;
        else return false;
    }

    public List<Product> getAllProductFromDB()
    {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Product", null);
        while (cursor.moveToNext()) {
            String productId = cursor.getString(0);
            String productName = cursor.getString(1);
            String productUnit = cursor.getString(2);
            int productPrice = cursor.getInt(3);
            Product prod = new Product(productId, productName, productUnit, productPrice, 0);
            productList.add(prod);
        }
        return productList;
    }
}
