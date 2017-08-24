package com.gcr.common.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by gongchunru
 * Date：2017/8/24.
 * Time：12:41
 */
public class StringUtil {

    public static boolean isEmpty(String str){
        if (str != null){
            str = str.trim();
        }
        return StringUtils.isEmpty(str);
    }

    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }

    /**
     * 分割固定格式的字符串
     * @param str
     * @param separator
     * @return
     */
    public static String[] split(String str, String separator){
        return StringUtils.splitByWholeSeparator(str, separator);
    }

}
