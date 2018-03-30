package ng.org.knowit.med_manager.Data;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 3/30/18.
 */

public class TestUtil {

    public static void insertFakeData(SQLiteDatabase db){
        if(db == null){
            return;
        }

        List<ContentValues> list = new ArrayList<ContentValues>();

        ContentValues cv = new ContentValues();

        cv.put(MedicineContract.MedicineEntry.COLUMN_MEDICINE_NAME, "Paracetamol");
        cv.put(MedicineContract.MedicineEntry.COLUMN_MEDICINE_DESCRIPTION, "Iam paracetamol used for simple stuffs");
        cv.put(MedicineContract.MedicineEntry.COLUMN_MEDICINE_FREQUENCY, "Twice a day");
        cv.put(MedicineContract.MedicineEntry.COLUMN_MEDICINE_DURATION, "Jan 2014 - Jan 2018");
        list.add(cv);

        try {
            db.beginTransaction();
            //clear the table first
            db.delete(MedicineContract.MedicineEntry.TABLE_NAME, null, null);
            //go through the list and add one by one
            for (ContentValues c : list) {
                db.insert(MedicineContract.MedicineEntry.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            //too bad :(
        } finally {
            db.endTransaction();
        }
    }

}
