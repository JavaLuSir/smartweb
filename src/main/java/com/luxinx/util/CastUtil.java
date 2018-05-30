package com.luxinx.util;


import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 类型转换工具类
 */
public final class CastUtil {

    private static final Logger LOG = Logger.getLogger(CastUtil.class);

    public static String castString(Object obj) {
        return castString(obj, "");
    }

    public static String castString(Object obj, String defaultval) {
        return obj != null ? String.valueOf(obj) : defaultval;
    }

    public static double castDouble(Object obj) {
        return castDouble(obj, 0);
    }

    public static double castDouble(Object obj, int defaultval) {
        double value = defaultval;
        if (obj != null) {
            String strdouble = castString(obj);
            if (StringUtils.isNotEmpty(strdouble)) {
                try {
                    value = Double.parseDouble(strdouble);
                } catch (NumberFormatException e) {
                    value = defaultval;
                }
            }
        }
        return value;
    }

    public static long castLong(Object obj) {
        return castLong(obj, 0L);
    }

    public static long castLong(Object obj, long defaultval) {
        long value = defaultval;
        if (obj != null) {
            String strlong = castString(obj);
            if (StringUtils.isNotEmpty(strlong)) {
                try {
                    value = Long.parseLong(strlong);
                } catch (NumberFormatException e) {
                    value = defaultval;
                }
            }
        }
        return value;
    }

    public static int castInt(Object obj){
        return castInt(obj,0);
    }

    public static int castInt(Object obj, int defaultval) {
        int value = defaultval;
        if(obj!=null){
            String strint = castString(obj);
            if(StringUtils.isNotEmpty(strint)){
                try {
                    value = Integer.parseInt(strint);
                } catch (NumberFormatException e) {
                    value = defaultval;
                }
            }
        }
        return value;
    }

    public static boolean castBoolean(Object obj){
        return castBoolean(obj,false);
    }

    public static boolean castBoolean(Object obj, boolean bool) {
        boolean value = bool;
        if(obj !=null){
            value = Boolean.parseBoolean(castString(obj));
        }
        return value;
    }
}
