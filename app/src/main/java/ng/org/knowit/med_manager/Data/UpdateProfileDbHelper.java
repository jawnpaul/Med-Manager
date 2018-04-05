package ng.org.knowit.med_manager.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import ng.org.knowit.med_manager.Data.UpdateProfileContract.*;

/**
 * Created by john on 4/5/18.
 */

public class UpdateProfileDbHelper extends SQLiteOpenHelper {

    private static final  String DATABASE_NAME = "UpdateProfileEntry.db";
    private static final int DATABASE_VERSION = 1;

    public UpdateProfileDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_UPDATE_PROFILE_ENTRY_TABLE = "CREATE TABLE " +
                UpdateProfileContract.UpdateProfileEntry.TABLE_NAME + " (" +
                UpdateProfileContract.UpdateProfileEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                UpdateProfileContract.UpdateProfileEntry.COLUMN_PROFILE_PHONE_NUMBER + " TEXT NOT NULL, " +
                UpdateProfileContract.UpdateProfileEntry.COLUMN_PROFILE_QUOTES + " TEXT NOT NULL" +
                "); ";
        sqLiteDatabase.execSQL(SQL_CREATE_UPDATE_PROFILE_ENTRY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UpdateProfileEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
