package com.example.myapplication.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.myapplication.OrderProduct;

import java.util.ArrayList;
import java.util.List;

public class DbOrderProductHelper extends SQLiteOpenHelper {
    public static final String DbOrderProduct = "OrderProduct.db";

    public DbOrderProductHelper(@Nullable Context context) {
        super(context, DbOrderProduct, null, 1);
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

    public List<OrderProduct> getAllOrderProductFromDB(String id_order)
    {
        List<OrderProduct> orderProducts = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Order_Product where id_order = ? ", new String[]{id_order});
        while (cursor.moveToNext())
        {
            String id_ord = cursor.getString(0);
            String id_prd = cursor.getString(1);
            int amount = cursor.getInt(2);
            OrderProduct product = new OrderProduct(id_ord, id_prd, amount);
            orderProducts.add(product);
        }
        cursor.close();
        return orderProducts;
    }

    public void deleteAllFromDB(String id_order)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("delete from Order_Product where id_order = ?", new String[]{id_order});
        cursor.close();
    }
}
