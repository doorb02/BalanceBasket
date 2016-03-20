package com.example.group21.balancebasket;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static com.example.group21.balancebasket.UserContract.NewProduct.*;

public class UserDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ProductsDB.db";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_QUERY = "CREATE TABLE " +  TABLE_NAME + "(" + COLUMN_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + PRODUCT_NAME + " TEXT, " +
            PRODUCT_PRICE + " TEXT " + ");";

    public UserDBHelper (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase db){
        db.execSQL(CREATE_QUERY);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

//    public void addProduct (SQLiteDatabase db, String name, int price){
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(PRODUCT_NAME, name);
//        contentValues.put(PRODUCT_PRICE, price);
//
//        db.insert(TABLE_NAME, null, contentValues);
//
//    }
//
//    public static Cursor getProducts(SQLiteDatabase db){
//        Cursor cursor = null;
//        String[] projections = {PRODUCT_NAME, PRODUCT_PRICE};
//
//        db.query(TABLE_NAME, projections, null,null,null,null,null);
//        return cursor;
//
//    }




}
