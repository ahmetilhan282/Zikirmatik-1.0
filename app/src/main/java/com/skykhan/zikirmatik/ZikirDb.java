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

    private static final String DB = "zikirDB";
    private static final int VERS = 2;
    private static final String[] DBTABLES = new String[]{"ZKR_GRUP", "ZKR_DETAY", "ZKR_SAYI", "ZKR_GECMIS"};

    public ZikirDb(Context cont) {
        super(cont, DB, null, VERS);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        if (!checkDatabase()) {
            tblZikirDetay(db);
            tblZikirGrup(db);
            tblZikirGecmis(db);
            tblZikirSayi(db);

        } else {
            SQLiteDatabase.openDatabase(DB, null, SQLiteDatabase.OPEN_READWRITE);
            Log.d("OPEN TABLE ", "Database baþarý ile açýldý.");
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (checkDatabase()) {
            SQLiteDatabase.openDatabase(DB, null, SQLiteDatabase.OPEN_READWRITE);
            if (db.getVersion() < newVersion) {
                for (int i = 0; i < DBTABLES.length; i++) {
                    db.execSQL("DROP TABLE IF EXIST " + DBTABLES[i]);
                }
            }
        } else return;

    }

    private boolean checkDatabase() {
        SQLiteDatabase checkDB = null;

        try {
            checkDB = SQLiteDatabase.openDatabase(DB, null, SQLiteDatabase.OPEN_READONLY);
            Log.d("OPEN DATABASE ", "Database iþlemler için hazýr.");
        } catch (SQLiteException e) {

        }

        return checkDB != null;
    }



    public void zikirGrupEkle(String ad, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ADI", ad);
        db.insert("ZKR_GRUP", null, values);
        db.close();
    }

    public void zikirEkle(String ad, int grup,int hedef) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ADI", ad);
        values.put("AKTIF", 1);
        values.put("HEDEF",hedef);
        values.put("GRUP", grup);
        db.insert("ZKR_DETAY", null, values);
        int c = sonKayitGetir("ZKR_DETAY");
        zikirArtir(c, 0);
        db.close();
    }

    public void zikirSil(int id) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor crs = db.rawQuery("SELECT ID FROM ZKR_GECMIS WHERE ID = " + id, null);
        if (crs.getCount() == 0) {
            db.delete("ZKR_DETAY", "ID=?", new String[]{String.valueOf(id)});
        } else {
            ContentValues cVal = new ContentValues();
            cVal.put("AKTIF", 1);
            db.update("ZKR_DETAY", cVal, "ID=?", new String[]{String.valueOf(id)});
        }
        crs.close();
        db.close();
    }

    public void zikirArtir(int id, int sayi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cVal = new ContentValues();
        cVal.put("ID", id);
        cVal.put("SAYI", sayi);
        db.delete("ZKR_SAYI", "ID=?", new String[]{String.valueOf(id)});
        db.insert("ZKR_SAYI", null, cVal);
        db.close();
    }

    public void zikirGecmiseEkle(int id, int sayi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cVal = new ContentValues();
        int yeni = 0;
        Cursor crs = db.rawQuery("SELECT SAYI FROM ZKR_GECMIS WHERE ID = " + id, null);
        if (crs.getCount() > 0) {
            crs.moveToFirst();
            yeni = crs.getInt(crs.getColumnIndex("SAYI"));
        }
        if (yeni > 0) {
            db.delete("ZKR_GECMIS", "ID=?", new String[]{String.valueOf(id)});
        }
        yeni = yeni + sayi;
        cVal.put("ID", id);
        cVal.put("SAYI", yeni);
        db.insert("ZKR_GECMIS", null, cVal);
        db.close();
    }

    public HashMap<String, String> zikir(int id) {
        return zikir(id,0);
    }

    public HashMap<String, String> zikir(int id,int aktif) {
        HashMap<String, String> kayit = new HashMap<>();
        String sq = "SELECT ID,ADI,HEDEF,GRUP  FROM ZKR_DETAY WHERE ID=" + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor crs = db.rawQuery(sq, null);
        crs.moveToFirst();
        if (crs.getCount() > 0) {
            kayit.put("ID", String.valueOf(crs.getInt(crs.getColumnIndex("ID"))));
            kayit.put("ADI", crs.getString(crs.getColumnIndex("ADI")));
            kayit.put("HEDEF", String.valueOf(crs.getString(crs.getColumnIndex("HEDEF"))));
            kayit.put("GRUP", String.valueOf(crs.getInt(crs.getColumnIndex("GRUP"))));
        }
        crs.close();
        db.close();
        return kayit;
    }

    public ArrayList<HashMap<String, String>> zikirler(int grupId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sq = "SELECT ZKR_DETAY.ID,ZKR_DETAY.ADI,ZKR_DETAY.GRUP,ZKR_DETAY.HEDEF," +
                "ZKR_SAYI.SAYI FROM ZKR_DETAY,ZKR_SAYI WHERE ZKR_DETAY.ID = ZKR_SAYI.ID AND ZKR_DETAY.GRUP ="+grupId;
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


    public ArrayList<HashMap<String,String>>zikirGruplar(){
        SQLiteDatabase db = this.getReadableDatabase();
        String sq = "SELECT FROM ZKR_GRUP ORDER BY "
    }

/*BURADA KALDIM GRUPLARI ÇEKECEKTÝM*/

    public int sonKayitGetir(String table) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sq = "SELECT MAX(ID) AS ID FROM "+ table ;
        Cursor crs = db.rawQuery(sq, null);
        int m = 0;
        crs.moveToFirst();
        if (crs.getCount() > 0) {
            m = crs.getInt(crs.getColumnIndex("ID"));
        }
        crs.close();
        db.close();
        return m;
    }

    private void tblZikirGrup(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ZKR_GRUP (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ADI TEXT," +
                "AKTIF INTEGER )";
        db.execSQL(CREATE_TABLE);
        Log.d("CREATE TABLE ", "Tablo ZKR_GRUP baþarý ile oluþturuldu.");

    }

    private void tblZikirDetay(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ZKR_DETAY (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ADI TEXT," +
                "AKTIF INTEGER," +
                "HEDEF INTEGER DEFAULT 0," +
                "GRUP INTEGER )";
        db.execSQL(CREATE_TABLE);
        Log.d("CREATE TABLE ", "Tablo ZKR_DETAY baþarý ile oluþturuldu.");

    }

    private void tblZikirSayi(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ZKR_SAYI (" +
                "ID INTEGER," +
                "SAYI INTEGER)";
        db.execSQL(CREATE_TABLE);
        Log.d("CREATE TABLE ", "Tablo ZKR_SAYI baþarý ile oluþturuldu.");


    }

    private void tblZikirGecmis(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ZKR_GECMIS (" +
                "ID INTEGER," +
                "SAYI INTEGER)";
        db.execSQL(CREATE_TABLE);
        Log.d("CREATE TABLE ", "Tablo ZKR_GECMIS baþarý ile oluþturuldu.");

    }


}
