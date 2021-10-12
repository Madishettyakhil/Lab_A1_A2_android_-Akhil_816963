package com.android.androidassignment;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class ProductDBHelper extends SQLiteOpenHelper {

    public static final String database_name = "products.db";
    public static final String table_name = "products";
    public static final String column_id = "id";
    public static final String column_product_name = "productname";
    public static final String column_product_desc = "productdesc";
    public static final String column_product_price = "productprice";
    public static final String column_provider_name = "providername";
    public static final String column_provider_email = "provideremail";
    public static final String column_provider_phone = "providerphone";
    public static final String column_latitude = "latitude";
    public static final String column_longitude = "longitude";

    private SQLiteDatabase database;
    private String path = "/data/data/com.android.androidassignment/databases/";
    private String opname;
    private final Context mycontext;

    public ProductDBHelper(@Nullable Context context) {
        super(context, database_name, null, 1);
        mycontext = context;
        opname = path+database_name;
        try {
            this.createProductDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createProductDataBase() throws IOException {
        boolean databaseexist = checkProduct();
        if (databaseexist) {

        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkProduct() {
        SQLiteDatabase checkproduct = null;
        try {
            checkproduct = SQLiteDatabase.openDatabase(opname, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            try {
                copyDataBase();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        if (checkproduct != null) {
            checkproduct.close();
        }
        return checkproduct != null ? true : false;
    }

    private void copyDataBase() throws IOException {
        byte[] buffer = new byte[1024];
        String Outputfilename = path + database_name;
        int length;
        InputStream Input = mycontext.getAssets().open(database_name);
        OutputStream myOutput = new FileOutputStream(Outputfilename);
        try {
            while ((length = Input.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.close();
            myOutput.flush();
            Input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openDataBase() throws SQLException {

        String myPath = path + database_name;
        database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public synchronized void close() {
        if (database != null)
            database.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<String> getProvider() {

        ArrayList<String> provider = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result =  db.rawQuery( "select providername from products", null );

        if (result.moveToFirst()) {
            do {
                provider.add(result.getString(result.getColumnIndex(column_provider_name)));
            } while (result.moveToNext());
        }
        result.close();
        return provider;
    }

    public ArrayList<Integer> getid() {

        ArrayList<Integer> id = new ArrayList<Integer>();

        database = this.getReadableDatabase();
        Cursor result =  database.rawQuery( "select id from products", null );

        if (result.moveToFirst()) {
            do {
                id.add(Integer.valueOf(result.getString(result.getColumnIndex(column_id))));
            } while (result.moveToNext());
        }
        result.close();
        return id;
    }

    public ArrayList<String> getProduct() {

        ArrayList<String> product = new ArrayList<String>();

        database = this.getReadableDatabase();
        Cursor result =  database.rawQuery( "select productname from products", null );

        if (result.moveToFirst()) {
            do {
                product.add(result.getString(result.getColumnIndex(column_product_name)));
            } while (result.moveToNext());
        }
        result.close();
        return product;
    }

    public ArrayList<String> getProductPrice() {

        ArrayList<String> productprice = new ArrayList<String>();

        database = this.getReadableDatabase();
        Cursor result =  database.rawQuery( "select productprice from products", null );

        if (result.moveToFirst()) {
            do {
                productprice.add(result.getString(result.getColumnIndex(column_product_price)));
            } while (result.moveToNext());
        }

        result.close();
        return productprice;
    }

    public Product getProductbyID(int id) {
        Product product = null;
        database = this.getReadableDatabase();
        Cursor result =  database.rawQuery( "select * from products where id="+id+"", null );

        if(result!=null)
        {
            result.moveToFirst();
            product = new Product(result.getInt(result.getColumnIndex(column_id)),result.getString(result.getColumnIndex(column_product_name)),
                    result.getString(result.getColumnIndex(column_product_desc)),result.getString(result.getColumnIndex(column_product_price)),
                    result.getString(result.getColumnIndex(column_provider_name)),result.getString(result.getColumnIndex(column_provider_email)),
                    result.getString(result.getColumnIndex(column_provider_phone)),result.getString(result.getColumnIndex(column_latitude)),
                    result.getString(result.getColumnIndex(column_longitude)));
        }

        result.close();
        return product;
    }

    public int RowCount(){
        database = this.getReadableDatabase();
        int count = (int) DatabaseUtils.queryNumEntries(database, table_name);
        return count;
    }
}
