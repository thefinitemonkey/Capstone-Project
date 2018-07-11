package com.finitemonkey.dougb.nflcrimewatch.data.converters;

import android.arch.persistence.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {
    @TypeConverter
    public static Date toDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, yyyy-MM-dd");
        Date parsedDate;
        try {
            parsedDate = formatter.parse(date);
        } catch (ParseException e) {
            parsedDate = new  Date(2000, 01, 01);
        }

        return parsedDate;
    }

    @TypeConverter
    public static String toString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }
}
