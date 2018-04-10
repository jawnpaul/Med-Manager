/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package ng.org.knowit.med_manager.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ng.org.knowit.med_manager.Data.MedicineContract.*;

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
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MedicineEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


}
