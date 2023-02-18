package com.kongo.history.api.kongohistoryapi.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.ZoneId;


public class AppUtilities {
   
    public static final String DATA_FORMAT = "yyyy-MM-dd";
    

    public static String convertDateToString(final Date date){
        final var sdf=new SimpleDateFormat(AppUtilities.DATA_FORMAT);
        return sdf.format(date);
    }

    public static Date convertStringFormatToDate(final String dateString){
        final var dateTimeFormatter = DateTimeFormatter.ofPattern(AppUtilities.DATA_FORMAT);
        final var localDate = LocalDate.parse(dateString, dateTimeFormatter);
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

}
