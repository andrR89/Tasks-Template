package com.devmasterteam.tasks.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatUrlParameters {

    public static String formatBoolean(Boolean value){
        if(value){
            return "true";
        }
        return "false";
    }

    public static String formatDate(Date value){
        if (value == null){
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(value);
    }

}
