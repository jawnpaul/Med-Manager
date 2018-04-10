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

import android.provider.BaseColumns;

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
