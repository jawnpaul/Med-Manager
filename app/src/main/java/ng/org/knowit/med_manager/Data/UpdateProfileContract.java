package ng.org.knowit.med_manager.Data;

import android.provider.BaseColumns;

/**
 * Created by john on 4/5/18.
 */

public class UpdateProfileContract {

    public static final class UpdateProfileEntry implements BaseColumns{

        public static final String TABLE_NAME = "UpdateProfileEntry";
        public static final  String COLUMN_PROFILE_PHONE_NUMBER = "ProfilePhoneNumber";
        public static final String COLUMN_PROFILE_QUOTES ="ProfileQuotes";

    }

}
