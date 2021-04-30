package com.epam.esm.dao.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateTimeUtil {

    private DateTimeUtil() {
    }

    public static LocalDateTime toZone(final LocalDateTime time, final ZoneId fromZone, final ZoneId toZone) {
        final ZonedDateTime zonedTime = time.atZone(fromZone);
        final ZonedDateTime converted = zonedTime.withZoneSameInstant(toZone);
        return converted.toLocalDateTime();
    }
}
