package com.takeaway.challenge.util;

import com.takeaway.challenge.constant.GlobalConstant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
}
