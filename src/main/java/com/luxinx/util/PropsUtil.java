package com.luxinx.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 属性文件读取工具类
 */
public final class PropsUtil {
    private static final Logger LOG = LoggerFactory.getLogger(PropsUtil.class);

    /**
     * 加载配置文件
     * @param filename
     * @return
     */
    public static Properties loadProperties(String filename) {
        Properties prop = null;
        InputStream is = null;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
            if (is == null) {
                throw new FileNotFoundException(filename + "is Not Found");
            }
            prop = new Properties();

            prop.load(is);
        } catch (IOException e) {
            LOG.error("load properties failed!", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOG.error("close input stream failed !", e);
                }
            }
        }
        return prop;
    }

    public static String getString(Properties prop,String key){
        return getString(prop,key,"");
    }

    public static String getString(Properties prop, String key, String defaultval) {
        if(prop == null){
            return defaultval;
        }
        String value = defaultval;
        if(prop.containsKey(key)){
            value = prop.getProperty(key);
        }
        return value;
    }

    public static int getInt(Properties prop,String key){
        return getInt(prop,key,0);
    }

    public static int getInt(Properties prop, String key, int defaultval) {
        if(prop == null){
            return defaultval;
        }
        int value = defaultval;
        if(prop.containsKey(key)){
            value = Integer.parseInt(prop.getProperty(key));
        }
        return value;
    }

    public static boolean getBoolean(Properties prop,String key){
        return getBoolean(prop,key,false);
    }

    public static boolean getBoolean(Properties prop, String key, boolean defaultval) {
        if(prop == null){
            return defaultval;
        }
        boolean value = defaultval;
        if(prop.containsKey(key)){
            value = Boolean.parseBoolean(prop.getProperty(key));
        }
        return value;
    }



}
