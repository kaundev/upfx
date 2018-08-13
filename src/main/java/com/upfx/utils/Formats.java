package com.upfx.utils;

import javafx.scene.control.DatePicker;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;

public class Formats {

    public static String defaultDecimalFormat(Double value){

        DecimalFormat format = new DecimalFormat("#####.##");
        return format.format(value);
    }

    public static String defaultDecimalFormat(BigDecimal value){
        try{
            DecimalFormat format = new DecimalFormat("#####.##");
            return format.format(value);
        }catch (IllegalArgumentException e){
            return "";
        }
    }

    public static String getDefaultDateFormat(Calendar calendar){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(calendar.getTime());
    }

    public static String getDefaultDateTimeFormat(Calendar calendar){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return format.format(calendar.getTime());
    }


    public static String defaultMinutesFormat(Calendar date){
        return String.valueOf(date.get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(date.get(Calendar.MINUTE));
    }

    public static Calendar dateTimePickerToCalendar(DatePicker datePicker){
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getValue().getYear(), datePicker.getValue().getMonthValue()-1, datePicker.getValue().getDayOfMonth(),0, 0, 0);

        return calendar;
    }

    public static LocalDate calendarToLocalDate(Calendar calendar){
        return LocalDate.of(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
    }

}
