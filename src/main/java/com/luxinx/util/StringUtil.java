package com.luxinx.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public final class StringUtil {

    private static final Logger LOG = Logger.getLogger(StringUtil.class);

    public static boolean isEmpty(String str){
        if(str!=null){
            str=str.trim();
        }
        return StringUtils.isEmpty(str);
    }

    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }
}
