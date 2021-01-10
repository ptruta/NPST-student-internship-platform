package ro.ubbcluj.util;

import ro.ubbcluj.exception.GenericException;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class formats dates
 */
@Slf4j
public class DateFormatter {
    public static Date getDate(String dateInString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern("dd/MM/yyyy");

        try {
            return simpleDateFormat.parse(dateInString);
        } catch (ParseException e) {
            log.error("DateFormatterError: " + e.getLocalizedMessage());
            throw new GenericException("DateFormatter: " + e.getLocalizedMessage());
        }
    }
}
