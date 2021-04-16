package com.cxylk.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Classname ComUtil
 * @Description TODO
 * @Author likui
 * @Date 2021/4/16 13:26
 **/
public class ComUtil {
    private ComUtil() {

    }

    private static final int NUMBER_13 = 13;
    private static final int NUMBER_UNDER_12 = -12;
    private static final String INTEGER_PATTERN = "^[-+]?[\\d]*$";
    private static final Character CHAR_POINT = '.';

    public static boolean isEmpty(Object aObj) {
        if (aObj instanceof String) {
            return isEmpty((String) aObj);
        } else if (aObj instanceof Long) {
            return isEmpty((Long) aObj);
        } else if (aObj instanceof Date) {
            return isEmpty((Date) aObj);
        } else if (aObj instanceof Collection) {
            return isEmpty((Collection) aObj);
        } else if (aObj instanceof Map) {
            return isEmpty((Map) aObj);
        } else if (aObj != null && aObj.getClass().isArray()) {
            return isEmptyArray(aObj);
        } else {
            return isNull(aObj);
        }
    }

    private static boolean isEmptyArray(Object array) {
        int length = 0;
        if (array instanceof int[]) {
            length = ((int[]) array).length;
        } else if (array instanceof byte[]) {
            length = ((byte[]) array).length;
        } else if (array instanceof short[]) {
            length = ((short[]) array).length;
        } else if (array instanceof char[]) {
            length = ((char[]) array).length;
        } else if (array instanceof float[]) {
            length = ((float[]) array).length;
        } else if (array instanceof double[]) {
            length = ((double[]) array).length;
        } else if (array instanceof long[]) {
            length = ((long[]) array).length;
        } else if (array instanceof boolean[]) {
            length = ((boolean[]) array).length;
        } else {
            length = ((Object[]) array).length;
        }
        return length == 0;
    }


    public static boolean isEmpty(Date aDate) {
        return aDate == null;
    }

    public static boolean isEmpty(Long aLong) {
        return aLong == null;
    }


    public static boolean isEmpty(Map m) {
        return m == null || m.size() == 0;
    }

    public static boolean isEmpty(Collection c) {
        return c == null || c.size() == 0;
    }

    public static boolean isEmpty(String aStr) {
        return aStr == null || aStr.trim().isEmpty();
    }


    public static String trim(String aStr) {
        return Optional.ofNullable(aStr).map(a -> "").orElse(aStr.trim());
    }

    public static boolean isNull(Object oStr) {
        return oStr == null;
    }


    public static boolean equals(String str1, String str2) {
        return Objects.equals(str1, str2);
    }

    public static boolean equals(Long l1, Long l2) {
        return Objects.equals(l1, l2);
    }

    public static boolean equals(Object obj1, Object obj2) {
        boolean result;
        if (obj1 != null) {
            result = obj1.equals(obj2);
        } else {
            result = (obj2 == null);
        }
        return result;
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 != null ? str1.equalsIgnoreCase(str2) : str2 == null;
    }

    /**
     * timeZoneOffset原为int类型，为班加罗尔调整成float类型
     * timeZoneOffset表示时区，如中国一般使用东八区，因此timeZoneOffset就是8
     *
     * @param timeZoneOffset
     * @return
     */
    public static String getFormattedDateString(Date date, float timeZoneOffset) {
        if (timeZoneOffset > NUMBER_13 || timeZoneOffset < NUMBER_UNDER_12) {
            timeZoneOffset = 0;
        }

        int newTime = (int) (timeZoneOffset * 60 * 60 * 1000);
        TimeZone timeZone;
        String[] ids = TimeZone.getAvailableIDs(newTime);
        if (ids.length == 0) {
            timeZone = TimeZone.getDefault();
        } else {
            timeZone = new SimpleTimeZone(newTime, ids[0]);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(timeZone);
        return sdf.format(date);
    }

    /**
     * LocalDate转Date
     *
     * @param localDate
     * @return
     */
    public static Date localDate2Date(LocalDate localDate) {
        if (null == localDate) {
            return null;
        }
        TimeZone china = TimeZone.getTimeZone("GMT+:08:00");
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(china.toZoneId());
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * Date转LocalDate
     *
     * @param date
     */
    public static LocalDate date2LocalDate(Date date) {
        if (null == date) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static boolean isInteger(String str) {
        if (!ComUtil.isEmpty(str)) {
            Pattern pattern = Pattern.compile(INTEGER_PATTERN);
            return pattern.matcher(str).matches();
        } else {
            return false;
        }
    }

    public static boolean isBigDecimal(String str) {
        if (str == null || str.trim().length() == 0) {
            return false;
        }
        char[] chars = str.toCharArray();
        int sz = chars.length;
        int i = (chars[0] == '-') ? 1 : 0;
        if (i == sz) {
            return false;
        }

        if (chars[i] == CHAR_POINT) {
            //除了负号，第一位不能为'小数点'
            return false;
        }

        boolean radixPoint = false;
        for (; i < sz; i++) {
            if (chars[i] == '.') {
                if (radixPoint) {
                    return false;
                }
                radixPoint = true;
            } else if (!(chars[i] >= '0' && chars[i] <= '9')) {
                return false;
            }
        }
        return true;
    }

    public static Long getNumberFromStr(String str) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return Long.parseLong(m.replaceAll("").trim());
    }

    public static String getCurrentYear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date date = new Date();
        return sdf.format(date);
    }

    public static List<Object> mapTransitionList(Map<String, Object> map) {
        List<Object> list = new ArrayList<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            list.add(entry.getValue());
        }
        return list;
    }

    public static List<String> getDuplicateElements(List<String> list) {
        return list.stream()
                .collect(Collectors.toMap(e -> e, e -> 1, Integer::sum))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
