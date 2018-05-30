package com.luxinx.util;

import org.apache.commons.collections4.MapUtils;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Map;

public final class CollectionUtil {
    private static Logger LOG = Logger.getLogger(CollectionUtil.class);

    public static boolean isEmpty(Collection<?> collection) {
        return CollectionUtil.isEmpty(collection);
    }
    public static boolean isNotEmpty(Collection<?> collection){
        return !isEmpty(collection);
    }
    public static boolean isEmpty(Map<String,String> map){
        return MapUtils.isEmpty(map);
    }
    public static boolean isNotEmpty(Map<String,String> map){
        return !isEmpty(map);
    }

}
