package com.sandemo.hrms.util;

import com.sandemo.hrms.constant.GlobalConstant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Sankar M <sankar.mm30@gmail.com>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Util {

    public static boolean isNull(final Object value) {

        return value == null;
    }

    public static boolean isNotNull(final Object value) {

        return value != null;
    }

    /**
     * This method used to cast CharSequence to String
     *
     * @param value
     * @return
     */
    public static String charSequenceToString(CharSequence value) {

        return isNotNull(value) ? value.toString() : null;
    }

    /**
     * This method used to convert string date value into ZonedDateTime
     * @param dateValue
     * @return
     */
    public static ZonedDateTime getZonedDateTime(final String dateValue) {

        return ZonedDateTime.parse(dateValue, DateTimeFormatter.ofPattern(GlobalConstant.DATE_TIME_FORMAT));
    }

    public static String getFormattedDate(final LocalDate date) {

        return isNotNull(date)
                ? date.format(DateTimeFormatter.ofPattern(GlobalConstant.DATE_FORMAT))
                : null ;
    }

    public static String getFormattedTimestamp(final ZonedDateTime dateTime) {

        return isNotNull(dateTime)
                ? dateTime.format(DateTimeFormatter.ofPattern(GlobalConstant.DATE_TIME_FORMAT))
                : null ;
    }
}
