package ng.org.knowit.med_manager.Data;

import android.provider.BaseColumns;

/**
 * Created by john on 3/29/18.
 */

public class MedicineContract {

    public static final class MedicineEntry implements BaseColumns{

        public static final String TABLE_NAME = "MedicineEntry";
        public static final String COLUMN_MEDICINE_NAME = "MedicineName";
        public static final String COLUMN_MEDICINE_DESCRIPTION = "MedicineDescription";
        public static final String COLUMN_MEDICINE_FREQUENCY = "MedicineFrequency";
        public static final String COLUMN_MEDICINE_DURATION = "MedicineDuration";
        public static final String COLUMN_TIMESTAMP = "TimeStamp";
    }

}
