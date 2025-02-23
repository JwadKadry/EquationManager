package com.example.equationmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.equationmanager.models.Polynomial;
import java.util.ArrayList;
import java.util.List;

public class PolynomialDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "PolynomialsDB";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_POLYNOMIALS = "polynomials";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DEGREE = "degree";
    public static final String COLUMN_COEFFICIENTS = "coefficients";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_POLYNOMIALS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_DEGREE + " INTEGER NOT NULL, " +
            COLUMN_COEFFICIENTS + " TEXT NOT NULL);";

    public PolynomialDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POLYNOMIALS);
        onCreate(db);
    }

    public void addPolynomial(int degree, String coefficients) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("degree", degree);
        values.put("coefficients", coefficients);

        db.insert("polynomials", null, values);
        db.close();
    }


    public List<Polynomial> getAllPolynomials() {
        List<Polynomial> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_POLYNOMIALS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                int degree = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DEGREE));
                String coefficients = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COEFFICIENTS));
                list.add(new Polynomial(id, degree, coefficients));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }
    public Polynomial getPolynomialById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_POLYNOMIALS, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int degree = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DEGREE));
            String coefficients = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COEFFICIENTS));
            cursor.close();
            db.close();
            return new Polynomial(id, degree, coefficients);
        }

        return null;
    }
    public void deletePolynomial(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_POLYNOMIALS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
    public void updatePolynomial(Polynomial polynomial) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DEGREE, polynomial.getDegree());
        values.put(COLUMN_COEFFICIENTS, polynomial.getCoefficientsAsString());

        db.update(TABLE_POLYNOMIALS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(polynomial.getId())});
        db.close();
    }



}
