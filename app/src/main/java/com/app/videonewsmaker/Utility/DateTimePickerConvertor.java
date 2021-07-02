package com.app.videonewsmaker.Utility;
import android.content.Context;
import android.os.Build;


import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimePickerConvertor {

    public enum INPUT {
        dd_MM_yy,;
    }

    public enum OUTPUT {
        ONE, TWO;
    }

    /**
     * This function will return current device time if no time or input format is provided
     *
     * @param ctx          Context
     * @param inputFormat  input pattern
     * @param outputFormat output pattern you required
     * @param mDate        date which is to be converted
     * @return returns date in string format as specified.
     */


    public static String convert(Context ctx, String inputFormat, String outputFormat, String mDate, Date date) {
        String formattedDate = "N/A";
        Locale locale;

        inputFormat = inputFormat.replace("/mm/", "/MM/")
                .replace("-mm-", "-MM-");
//        Functions.print("DateTimeConverter==inputFormat" + inputFormat);
//        Functions.print("DateTimeConverter==outputFormat" + outputFormat);
//        Functions.print("DateTimeConverter==mDate" + mDate);
        try {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = ctx.getResources().getConfiguration().getLocales().get(0);
            } else {
//                locale = ctx.getResources().getConfiguration().locale;
                locale = ctx.getResources().getConfiguration().locale;
            }
            if (!is_valid(outputFormat)) {
                outputFormat = "yyyy/MM/dd";
            }

            mDate = convertDateUtcToDefault(mDate, "yyyy-MM-dd HH:mm:ss");

//            Functions.print("DateTimeConverter==inputFormat" + inputFormat);
//            Functions.print("DateTimeConverter==outputFormat" + outputFormat);
//            Functions.print("DateTimeConverter==mDate" + mDate);
            SimpleDateFormat mOutputDateFormat = new SimpleDateFormat(outputFormat, Locale.ENGLISH);

            if (date == null) {
                if (is_valid(inputFormat) && is_valid(mDate)) {
                    SimpleDateFormat mInputDateFormat = new SimpleDateFormat(inputFormat, Locale.ENGLISH);
                    Date inputDate = mInputDateFormat.parse(mDate);
                    formattedDate = mOutputDateFormat.format(inputDate);
                } else {
                    Date inputDate = Calendar.getInstance().getTime();
                    formattedDate = mOutputDateFormat.format(inputDate);
                }
            } else {
                formattedDate = mOutputDateFormat.format(date);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
//        Functions.print("formattedDate" + formattedDate);
        formattedDate = formattedDate.replace("a.m.", "AM")
                .replace("p.m.", "PM");
//        Functions.print("inputFormat" + inputFormat);
//        Functions.print("outputFormat" + outputFormat);
//        Functions.print("mDate" + mDate);
        return formattedDate;
    }

    private static boolean is_valid(String str) {
        return !(str == null || str.equalsIgnoreCase("null") || str.trim().length() == 0);
    }


    public static String convertDateUtcToDefault(String mDate, String format) {
        try {
            format = format.replace("-mm-", "-MM-")
                    .replace("/mm/", "/MM/");
            System.out.println(format + "=====convertDateUtcToDefault_" + mDate);
            SimpleDateFormat inputFormat = new SimpleDateFormat(format);
//            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = inputFormat.parse(mDate);
            System.out.println("===convertDateUtcToDefault_ 1 " + inputFormat.format(value));

            SimpleDateFormat outputFormat = new SimpleDateFormat(format, Locale.getDefault());
            outputFormat.setTimeZone(TimeZone.getDefault());
            mDate = outputFormat.format(value);

//            Functions.print("==Converted Date" + mDate.toUpperCase());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDate.toUpperCase();
    }

    private String formats(String format) {
/*        h:mm a                        //12:08 PM
        yyyy-MM-dd
        dd-MM-yyyy
        yyyy-MM-dd kk:mm:ss
        yyyy-MM-dd HH:mm:ss
        MM/dd/yyyy HH:mm:ss
        dd MMM yyyy
        MMM dd, yyyy hh:mm:ss aaa//   Mar 10, 2016 6:30:00 PM
        E, MMM dd yyyy                Fri, June 7 2013
        EEEE, MMM dd, yyyy HH:mm:ss a   //Friday, Jun 7, 2013 12:10:56 PM


        No.	Format	Example
        1	dd/mm/yy	03/08/06
        2	dd/mm/yyyy	03/08/2006
        3	d/m/yy	3/8/06
        4	d/m/yyyy	3/8/2006
        5	ddmmyy	030806
        6	ddmmyyyy	03082006
        7	ddmmmyy	03Aug06
        8	ddmmmyyyy	03Aug2006
        9	dd-mmm-yy	03-Aug-06
        10	dd-mmm-yyyy	03-Aug-2006
        11	dmmmyy	3Aug06
        12	dmmmyyyy	3Aug2006
        13	d-mmm-yy	3-Aug-06
        14	d-mmm-yyyy	3-Aug-2006
        15	d-mmmm-yy	3-August-06
        16	d-mmmm-yyyy	3-August-2006
        17	yymmdd	060803
        18	yyyymmdd	20060803
        19	yy/mm/dd	06/08/03
        20	yyyy/mm/dd	2006/08/03
        21	mmddyy	080306
        22	mmddyyyy	08032006
        23	mm/dd/yy	08/03/06
        24	mm/dd/yyyy	08/03/2006
        25	mmm-dd-yy	Aug-03-06
        26	mmm-dd-yyyy	Aug-03-2006
        27	yyyy-mm-dd	2006-08-03
        28	weekday, dth mmmm yyyy	Monday, 3 of August 2006
        29	weekday	Monday
        30	mmm-yy	Aug-06
        31	yy	06
        32	yyyy	2006
        33	dd-mmm-yyyy time	03-Aug-2006 18:55:30.35
        34	yyyy-mm-dd time24 (ODBC Std)	2006-08-03 18:55:30
        35	dd-mmm-yyyy time12	03-Aug-2006 6:55:30 pm
        36	time24	18:55:30
        37	time12	6:55:30 pm
        38	hours	48:55:30
        39	seconds	68538.350*/

        return "";
    }

    public static boolean isValidFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return date != null;
    }


    public static String getFileNameFromURL(String url) {
        if (url == null) {
            return "";
        }
        try {
            URL resource = new URL(url);
            String host = resource.getHost();
            if (host.length() > 0 && url.endsWith(host)) {
                // handle ...example.com
                return "";
            }
        }
        catch(MalformedURLException e) {
            return "";
        }

        int startIndex = url.lastIndexOf('/') + 1;
        int length = url.length();

        // find end index for ?
        int lastQMPos = url.lastIndexOf('?');
        if (lastQMPos == -1) {
            lastQMPos = length;
        }

        // find end index for #
        int lastHashPos = url.lastIndexOf('#');
        if (lastHashPos == -1) {
            lastHashPos = length;
        }

        // calculate the end index
        int endIndex = Math.min(lastQMPos, lastHashPos);
        return url.substring(startIndex, endIndex);
    }
}
