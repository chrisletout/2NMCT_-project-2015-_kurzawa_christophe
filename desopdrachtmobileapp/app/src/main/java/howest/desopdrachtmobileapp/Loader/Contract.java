package howest.desopdrachtmobileapp.Loader;

import android.provider.BaseColumns;

/**
 * Created by chris on 12/05/15.
 */
public class Contract {
    public interface placesColumns extends BaseColumns {
        public static final String COLUMN_NAAM = "Jaar";
        public static final String COLUMN_STRAAT = "Maand";
        public static final String COLUMN_LAT = "Straat";
        public static final String COLUMN_LONG = "Postcode";
        public static final String COLUMN_TYPE = "Type";
    }
}
