package com.manjunath.moorthi.stockapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;



public class SymbolList extends SQLiteOpenHelper {

    private static final String TAG = "SymbolList";
    private static final int DATABASE_VERSION = 1;
    private static final String DB_Name = "MyDB";
    private static final String Table_Title = "MyTable";
    private static final String Sym = "Symbol";
    private static final String Name = "Name";
    private static final String ext="extra";
    private static final String ext1="extra1";
    private static final String Create = "CREATE TABLE " + Table_Title + " (" +
            Sym + " TEXT not null unique," +
            Name + " TEXT not null )";
    private static SymbolList instance;
    private SQLiteDatabase database;
    private static final String DATABASE_ALTER_TABLE_FOR_V2 = "ALTER TABLE "
            + Table_Title + " ADD COLUMN " + ext + " int not null default 0;";

    private static final String DATABASE_ALTER_TABLE_FOR_V3 = "ALTER TABLE "
            + Table_Title + " ADD COLUMN " + ext1 + " int not null default 0;";

    public static SymbolList getInstance(Context c) {
        Log.d(TAG, "getInstance: Came to get instance");
        if (instance == null)
            instance = new SymbolList(c);
        return instance;
    }

    private SymbolList(Context c) {
        super(c, DB_Name, null, DATABASE_VERSION);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Create);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == 2) {
            db.execSQL(DATABASE_ALTER_TABLE_FOR_V2);
        }
        if (newVersion == 3) {
            db.execSQL(DATABASE_ALTER_TABLE_FOR_V3);
        }
    }

    public void setupDb() {

        Log.d(TAG, "setupDb: came to setup");
        database = getWritableDatabase();
    }

    public void addentry(Stock stock) {
        ContentValues v = new ContentValues();
        v.put(Sym, stock.getStockSys());
        v.put(Name, stock.getCompanyName());
        database.insert(Table_Title, null, v);

    }

    public void Deleteentery(String s) {
        database.delete(Table_Title, Sym +"= ?", new String[]{s});




    }

    public ArrayList<String[]> Load() {
        ArrayList<String[]> ss = new ArrayList<>();
        Cursor c = database.query(Table_Title, new String[]{Sym, Name}, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();

            for (int i = 0; i < c.getCount(); i++) {
                String s1 = c.getString(0);
                String s2 = c.getString(1);
                ss.add(new String[]{s1,s2});
                c.moveToNext();
            }
            c.close();
        }
        return ss;
    }
    public void shutdown(){
        database.close();
    }

}