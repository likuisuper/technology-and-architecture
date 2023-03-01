package com.startdt.ctcc.cdp.data.api.util;

import com.startdt.ctcc.cdp.data.api.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * @author st
 * @date 2018/6/1 下午2:34
 */
public class DateTimeFormatUtils {

    private static final Logger logger = LoggerFactory.getLogger(DateTimeFormatUtils.class);
    private static final String STR1 = "yyyy-MM-dd HH:mm:ss";
    private static final String STR2 = "yyyy-MM-dd";
    private static final String STR3 = "yyyyMMdd";

    private static final DateTimeFormatter DATE_TIME_FORMATTER01 = DateTimeFormatter.ofPattern(STR1);
    public static final DateTimeFormatter DATE_TIME_FORMATTER02 = DateTimeFormatter.ofPattern(STR2);
    private static final DateTimeFormatter DATE_TIME_FORMATTER03 = DateTimeFormatter.ofPattern(STR3);
    private static final DateTimeFormatter DATE_TIME_FORMATTER04 = DateTimeFormatter.ofPattern("yyyyMM/dd");
    private static final DateTimeFormatter DATE_TIME_FORMATTER05 = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    public static final DateTimeFormatter DATE_TIME_FORMATTER06 = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    public static final DateTimeFormatter DATE_TIME_FORMATTER07 = DateTimeFormatter.ofPattern("yyyy-MM");
    private static final DateTimeFormatter DATE_TIME_FORMATTER08 = DateTimeFormatter.ofPattern("yyyyMM");
    private static final DateTimeFormatter DATE_TIME_FORMATTER09 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final DateTimeFormatter DATE_TIME_FORMATTER10 = DateTimeFormatter.ofPattern("yyMM");
    private static final DateTimeFormatter DATE_TIME_FORMATTER11 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

    /**
     * 计算当前日期与{@code endDate}的间隔天数
     *
     * @param endDate
     * @return 间隔天数
     */
    public static long until(LocalDate endDate) {
        return LocalDate.now().until(endDate, ChronoUnit.DAYS);
    }

    public static long until(LocalDate startDate, LocalDate endDate) {
        return startDate.until(endDate, ChronoUnit.DAYS);
    }

    public static long until(Date startDate, Date endDate) {
        return dateToLocalDate(startDate).until(dateToLocalDate(endDate), ChronoUnit.DAYS);
    }


    public static String formatyyyymmdd() {
        return FastDateFormat.getInstance(STR3).format(new Date());
    }

    public static String formatyyyymmdd(long mills) {
        return FastDateFormat.getInstance(STR3).format(mills);
    }

    public static String formatyyyymmddhhmmss01(long mills) {
        return FastDateFormat.getInstance("yyyyMMddHHmmss").format(mills);
    }

    public static String formatyyyymmddhhmmsssss(long mills) {
        return FastDateFormat.getInstance("yyyyMMddHHmmssSSS").format(mills);
    }

    public static String formatyyyymmdd02() {
        return FastDateFormat.getInstance(STR2).format(new Date());
    }

    public static String formatyyyymmdd02(long mills) {
        return FastDateFormat.getInstance(STR2).format(mills);
    }

    public static Date pasreyyyymmdd(String format) throws ParseException {
        return FastDateFormat.getInstance(STR2).parse(format);
    }

    public static Date pasreyyyymm(String format) {
        try {
            return FastDateFormat.getInstance("yyyy-MM").parse(format);
        } catch (ParseException e) {
            throw new BizException(e.getMessage());
        }
    }

    public static Date pasreyyyymmddhhmmss(String format) {
        if (StringUtils.isEmpty(format)) {
            return null;
        }
        try {
            return FastDateFormat.getInstance(STR1).parse(format);
        } catch (ParseException e) {
            logger.error("日期解析异常 " + format, e);
            return null;
        }
    }

    public static Date dateFormatDefault(String date) {
        SimpleDateFormat format = new SimpleDateFormat(STR1);
        if (date == null) {
            return null;
        }
        try {
            return format.parse(date);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    public static String formatyyyymmddhhmmss02(Long mills) {
        return FastDateFormat.getInstance(STR1).format(mills);
    }

    public static String formatyyyymmddhhmm(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return "";
        }
        return localDateTime.format(DATE_TIME_FORMATTER01);
    }


    /**
     * java.util.Date --> java.time.LocalDateTime
     *
     * @param date
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * java.util.Date --> java.time.LocalDate
     */
    public static LocalDate dateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalDate();
    }

    /**
     * java.util.Date --> java.time.LocalTime
     */
    public static LocalTime dateToLocalTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalTime();
    }

    public static long getTimeDifference(Date startDate,Date endDate){
        LocalDateTime startLocal = dateToLocalDateTime(startDate);
        LocalDateTime endLocal = dateToLocalDateTime(endDate);
        return startLocal.until(endLocal, ChronoUnit.HOURS);
    }


    /**
     * java.time.LocalDateTime --> java.util.Date
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    /**
     * java.time.LocalDateTime --> java.util.Date
     */
    public static Date localDateTimeToUdate(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static String localDateTimeFormat(LocalDateTime localDateTime) {
        return localDateTime.format(DATE_TIME_FORMATTER01);
    }

    public static String formatyyyymmdd02(LocalDate localDate) {
        return localDate.format(DATE_TIME_FORMATTER02);
    }

    public static String formatyyyymmdd(LocalDate localDate) {
        return localDate.format(DATE_TIME_FORMATTER03);
    }

    public static String formatyyyymmdd04(LocalDate localDate) {
        return localDate.format(DATE_TIME_FORMATTER04);
    }

    public static String formatyyyymmdd05(LocalDate localDate) {
        return localDate.format(DATE_TIME_FORMATTER05);
    }

    public static LocalDate formatyyyymmdd05(String localDate) {
        return LocalDate.parse(localDate, DATE_TIME_FORMATTER05);
    }


    public static LocalDate parseyyyymmdd(String format) {
        return LocalDate.parse(format, DATE_TIME_FORMATTER03);
    }

    public static LocalDateTime pasreFormatToLocalDateTime(String format) {
        if (StringUtils.isEmpty(format)) {
            return null;
        }
        return LocalDateTime.parse(format, DATE_TIME_FORMATTER01);
    }

    /**
     * java.time.LocalDate --> java.util.Date
     */
    public static Date localDateToUdate(LocalDate localDate) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }

    /**
     * java.time.LocalTime --> java.util.Date
     */
    public static Date localTimeToUdate(LocalDate localDate, LocalTime localTime) {
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    /**
     * 判断是否是今天
     */
    public static boolean isToday(LocalDateTime localDateTime) {
        final LocalDate localDate = LocalDateTime.now().toLocalDate();
        return localDate.isEqual(localDateTime.toLocalDate());
    }

    public static String timeRangeEndMin(LocalDateTime startTime, LocalDateTime endTime) {
        String reservationTimeEndMin = DateTimeFormatUtils.formatyyyymmddhhmm(startTime);
        int hour = endTime.getHour();
        int minute = endTime.getMinute();
        StringBuilder stringBuilder = new StringBuilder(5);
        StringBuilder append = stringBuilder.append(reservationTimeEndMin).append("-")
                .append(String.format("%02d", hour)).append(":").append(String.format("%02d", minute));
        return append.toString();
    }

    public static LocalDate getLastDay(LocalDate localDate) {
        LocalDate lastDay = localDate.with(TemporalAdjusters.lastDayOfMonth());

        return lastDay;
    }

    public static LocalDate getFirstDay(LocalDate localDate) {
        return localDate.with(TemporalAdjusters.firstDayOfMonth());
    }


    /**
     * 格式yyyyMM
     *
     * @param time
     * @return yyyy-MM
     */
    public static String formatYyyyMM(String time) {
        if (StringUtils.isBlank(time)) {
            return null;
        }
        LocalDate localDate = LocalDate.parse(time, DATE_TIME_FORMATTER03);
        time = localDate.format(DATE_TIME_FORMATTER07);
        return time;
    }

    /**
     * 格式yyyyMM
     *
     * @return yyyy-MM
     */
    public static String formatYyMM() {
        LocalDate localDate = dateToLocalDate(new Date());
        return localDate.format(DATE_TIME_FORMATTER10);
    }

    public static String formatYyMMdd(Date date) {
        LocalDateTime localDate = dateToLocalDateTime(date);
        return localDate.format(DATE_TIME_FORMATTER01);
    }

    public static String formatYyMMdd1(Date date) {
        LocalDateTime localDate = dateToLocalDateTime(date);
        return localDate.format(DATE_TIME_FORMATTER02);
    }


    /**
     * 操作日期字符串月份
     *
     * @param dateStr
     * @param sumNum
     * @return
     */
    public static String calculateMonthYY_MM(String dateStr, Long sumNum) {

        YearMonth yearMonth = YearMonth.parse(dateStr, DATE_TIME_FORMATTER07);

        YearMonth yearMonthSum = yearMonth.plusMonths(sumNum);

        return DATE_TIME_FORMATTER07.format(yearMonthSum);
    }


    /**
     * 操作日期字符串月份
     *
     * @param dateStr
     * @param sumNum
     * @return
     */
    public static String calculateMonthYYMM(String dateStr, Long sumNum) {

        YearMonth yearMonth = YearMonth.parse(dateStr, DATE_TIME_FORMATTER08);

        YearMonth yearMonthSum = yearMonth.plusMonths(sumNum);

        return DATE_TIME_FORMATTER08.format(yearMonthSum);
    }

    public static String formatDATE_TIME_FORMATTER05(String time) {
        if (StringUtils.isBlank(time)) {
            return null;
        }
        String paymentDueDate = null;
        if (time.contains(".")) {

            paymentDueDate = LocalDate.parse(time, DateTimeFormatUtils.DATE_TIME_FORMATTER06)
                    .format(DateTimeFormatUtils.DATE_TIME_FORMATTER05);
        } else if (time.contains("-")) {
            paymentDueDate = LocalDate.parse(time, DateTimeFormatUtils.DATE_TIME_FORMATTER02)
                    .format(DateTimeFormatUtils.DATE_TIME_FORMATTER05);
        }

        return paymentDueDate;


    }

    public static String formatDATE_TIME_FORMATTER01(String time) {
        if (StringUtils.isBlank(time)) {
            return null;
        }
        LocalDateTime localDateTime = LocalDateTime.parse(time, DATE_TIME_FORMATTER09);

        return localDateTime.format(DATE_TIME_FORMATTER01);

    }

    public static Date getDateEnd(LocalDate localDate) {
        return localDateTimeToDate(LocalDateTime.of(localDate, LocalTime.MAX));
    }

    public static Date getDateStart(LocalDate localDate) {
        return localDateTimeToDate(LocalDateTime.of(localDate, LocalTime.MIN));
    }


    /**
     * 格式化日期为 yyyyMMddHHmmss格式
     *
     * @param localDateTime
     * @return
     */
    public static String fromatDATE_TIME_FORMATTER09(LocalDateTime localDateTime) {
        return DATE_TIME_FORMATTER09.format(localDateTime);
    }


    public static LocalDateTime parseFormat11ToLocalDateTime(String format) {
        return LocalDateTime.parse(format, DATE_TIME_FORMATTER11);
    }

    public static LocalDateTime getTodayStart() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
    }

    public static LocalDateTime getTodayEnd() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
    }

    private static LocalDate getTodayDate() {
        return LocalDate.now();
    }

    /**
     * 获取前一天0点0分0秒
     *
     * @return
     */
    public static LocalDateTime getPreDayStartTime() {
        LocalDate localDate = getTodayDate();
        localDate = localDate.plusDays(1);
        return LocalDateTime.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth(), 0, 0, 0);
    }

    /**
     * 获取前一天23点59分59秒
     *
     * @return
     */
    public static LocalDateTime getPreDayEndTime() {
        LocalDate localDate = getTodayDate();
        localDate = localDate.plusDays(1);
        return LocalDateTime.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth(), 23, 59, 59);
    }

    public static Date getFirstDayStartOfQuarter(LocalDate localDate) {
        //季度第一个月
        Month firstMonth = localDate.getMonth().firstMonthOfQuarter();
        LocalDate firstDay = LocalDate.of(localDate.getYear(), firstMonth, 1);
        return getDateStart(firstDay);

    }

    public static Date getLastDayEndOfQuarter(LocalDate localDate) {
        Month lastMonth = Month.of(localDate.getMonth().firstMonthOfQuarter().getValue() + 2);
        LocalDate lastDay = LocalDate.of(localDate.getYear(), lastMonth, lastMonth.length(localDate.isLeapYear()));
        return getDateEnd(lastDay);
    }

    public static Date plusDays(Date date, long i) {
        return localDateToUdate(dateToLocalDate(date).plusDays(i));
    }

    /**
     * 获取前n天
     * @return
     */
    public static String getBeforeNDay(int day) {
        LocalDate localDate = getTodayDate();
        localDate = localDate.plusDays(-day);
        return DATE_TIME_FORMATTER03.format(localDate);
    }

    /**
     * 昨天
     * @return
     */
    public static String getYesterday() {
        LocalDate localDate = getTodayDate();
        localDate = localDate.plusDays(-1);
        return DATE_TIME_FORMATTER03.format(localDate);
    }

    /**
     * 昨天
     * @return
     */
    public static String getYesterday(Long timestamp) {
        LocalDate localDate = dateToLocalDate(new Date(timestamp));
        localDate = localDate.plusDays(-1);
        return DATE_TIME_FORMATTER03.format(localDate);
    }

    public static String getAfterNDay(int day) {
        LocalDate localDate = getTodayDate();
        localDate = localDate.plusDays((long)day);
        return FastDateFormat.getInstance("yyyyMMdd").format(localDateToUdate(localDate));
    }

}
