package ro.ase.test2_sub1;

import androidx.room.TypeConverter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    @TypeConverter
    public static Date fromString(String date) {
        try {
            return new SimpleDateFormat("dd-MM-yyyy", Locale.US).parse(date);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @TypeConverter
    public static String fromDate(Date date) {
        return new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(date);
    }
}
