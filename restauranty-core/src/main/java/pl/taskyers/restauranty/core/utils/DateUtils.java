package pl.taskyers.restauranty.core.utils;

import lombok.experimental.UtilityClass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@UtilityClass
public class DateUtils {
    
    private static final DateFormat DATETIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    
    public String parseString(Date date) {
        return DATETIME_FORMAT.format(date);
    }
    
}
