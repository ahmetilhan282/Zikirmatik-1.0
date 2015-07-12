package com.skykhan.zikirmatik;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by GOKHAN on 09/07/2015.
 */
public class ZikirDb extends SQLiteOpenHelper {

    public static final String TABLE = "ZKR_SAYI";
    public static final String ID = "ID";
    public static final String SAYI = "SAYI";
    public static final String ADI = "ADI";
    private static final String DB = "zikirDb";
    private static final int VERS = 1;

    public ZikirDb(Context cont) {
        super(cont, DB, null, VERS);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {


        if (!checkDatabase()) {
            String CREATE_TABLE = "CREATE TABLE " + TABLE + "("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + ADI + " TEXT,"
                    + SAYI + " INTEGER" + ")";
            db.execSQL(CREATE_TABLE);
            Log.d("CREATE TABLE ", "Tablo basari ile olusturuldu.");
        } else {
            SQLiteDatabase.openDatabase(DB, null, SQLiteDatabase.OPEN_READWRITE);
            Log.d("OPEN TABLE ", "Tablo basari ile acildi.");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private boolean checkDatabase() {
        SQLiteDatabase checkDB = null;

        try {
            checkDB = SQLiteDatabase.openDatabase(DB, null, SQLiteDatabase.OPEN_READONLY);
            Log.d("OPEN DATABASE ", "Database islemler icin hazir.");
        } catch (SQLiteException e) {

        }

        return checkDB != null;
    }


    public void zikirEkle(Integer sayi, String ad) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ADI, ad);
        values.put(SAYI, sayi);
        db.insert(TABLE, null, values);
        db.close();
    }

    public void zikirSil(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE, ID + "= ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void zikirGuncelle(int id, String ad, int sayi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ADI, ad);
        values.put(SAYI, sayi);
        db.update(TABLE, values, ID + "= ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public HashMap<String, String> zikir(int id) {
        HashMap<String, String> kayit = new HashMap<>();
        String sq = "SELECT * FROM " + TABLE + " WHERE ID=" + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor crs = db.rawQuery(sq, null);
        crs.moveToFirst();
        if (crs.getCount() > 0) {
            kayit.put(ID, String.valueOf(crs.getInt(crs.getColumnIndex("ID"))));
            kayit.put(ADI, crs.getString(crs.getColumnIndex("ADI")));
            kayit.put(SAYI, String.valueOf(crs.getString(crs.getColumnIndex("SAYI"))));
        }
        crs.close();
        db.close();
        return kayit;
    }

    public ArrayList<HashMap<String, String>> zikirler() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sq = "SELECT ID,ADI,SAYI FROM " + TABLE;
        Cursor crs = db.rawQuery(sq, null);
        ArrayList<HashMap<String, String>> kayitlar = new ArrayList<>();

        if (crs.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                for (int i = 0; i < crs.getColumnCount(); i++) {
                    map.put(crs.getColumnName(i), crs.getString(i));
                }
                kayitlar.add(map);
            } while (crs.moveToNext());
        }
        crs.close();
        db.close();
        return kayitlar;

    }

    public int sonKayitGetir() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sq = "SELECT MAX(ID) AS ID FROM " + TABLE;
        Cursor crs = db.rawQuery(sq, null);
        int m = 0;
        crs.moveToFirst();
        if (crs.getCount()>0) {
            m = crs.getInt(crs.getColumnIndex("ID"));
        }
        crs.close();
        db.close();
        return m;
    }


}
