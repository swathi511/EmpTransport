package com.hjsoft.emptransport;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hjsoft.emptransport.adapter.DBAdapter;

/**
 * Created by hjsoft on 28/2/17.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    Context context;

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(DBAdapter.TABLE_LATLNG);
        sqLiteDatabase.execSQL(DBAdapter.TABLE_STORE_TIMES);
        sqLiteDatabase.execSQL(DBAdapter.TABLE_TRIPID);
        sqLiteDatabase.execSQL(DBAdapter.TABLE_JOURNEY_DETAILS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "LATLNG_DETAILS");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "INFO");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "JOURNEY_DETAILS");
        // Create a new one.
        onCreate(sqLiteDatabase);
    }
}
