package pl.taskyers.restauranty.core.utils;

import lombok.experimental.UtilityClass;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@UtilityClass
public class DateUtils {
    
    private static final DateFormat DATETIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    
    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
    
    public String parseStringDatetime(Date date) {
        return DATETIME_FORMAT.format(date);
    }
    
    public String parseStringDate(Date date) {
        return DATE_FORMAT.format(date);
    }
    
    public String parseStringTime(Date date) {
        return TIME_FORMAT.format(date);
    }
    
    public static Date parseTime(String time) {
        try {
            return TIME_FORMAT.parse(time);
        } catch ( ParseException e ) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Date parseDate(String date) {
        try {
            return DATE_FORMAT.parse(date);
        } catch ( ParseException e ) {
            e.printStackTrace();
            return null;
        }
    }
    
}
