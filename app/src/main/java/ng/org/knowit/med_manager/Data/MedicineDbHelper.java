package ng.org.knowit.med_manager.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by john on 3/29/18.
 */

public class MedicineDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MedicineEntry.db";
    private static final int DATABASE_VERSION = 1;

    public MedicineDbHelper(Context context){
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MEDICINE_ENTRY_TABLE = "CREATE TABLE " +
                MedicineContract.MedicineEntry.TABLE_NAME + " (" +
                MedicineContract.MedicineEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MedicineContract.MedicineEntry.COLUMN_MEDICINE_NAME + " TEXT NOT NULL, " +
                MedicineContract.MedicineEntry.COLUMN_MEDICINE_DESCRIPTION + " TEXT NOT NULL, " +
                MedicineContract.MedicineEntry.COLUMN_MEDICINE_FREQUENCY + " TEXT NOT NULL, " +
                MedicineContract.MedicineEntry.COLUMN_MEDICINE_DURATION + " TEXT NOT NULL, " +
                MedicineContract.MedicineEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";
        sqLiteDatabase.execSQL(SQL_CREATE_MEDICINE_ENTRY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MedicineContract.MedicineEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
