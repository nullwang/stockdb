package org.stockdb.core.util;
/*
 * @author nullwang@hotmail.com
 * created at 2015/9/8
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.lang3.StringUtils;
import org.stockdb.core.exception.TimeFormatException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeFormatUtil {

    public static int YY = 0, YYMM = 1, YYMMDD = 2, YYMMDDHH = 3, YYMMDDHHMM = 4, YYMMDDHHMMSS = 5, YYMMDDHHMMSSZZZ = 6;

    public static String PATTERN_YY = "((19|20|21|22)\\d\\d)";
    public static String PATTERN_YYMM = PATTERN_YY + "(0[1-9]|1[012])";
    public static String PATTERN_YYMMDD = PATTERN_YYMM + "(0[1-9]|[12][0-9]|3[01])";
    public static String PATTERN_YYMMDDHH = PATTERN_YYMMDD + "([0-1][0-9]|2[0-3])";
    public static String PATTERN_YYMMDDHHMM = PATTERN_YYMMDDHH + "([0-5][0-9])";
    public static String PATTERN_YYMMDDHHMMSS = PATTERN_YYMMDDHHMM + "([0-5][0-9])";
    public static String PATTERN_YYMMDDHHMMSSZZZ = PATTERN_YYMMDDHHMMSS + "(\\d{3})";

    public static Pattern[] timeStrPatterns = {
            Pattern.compile("^" + PATTERN_YY),
            Pattern.compile("^" + PATTERN_YYMM),
            Pattern.compile("^" + PATTERN_YYMMDD),
            Pattern.compile("^" + PATTERN_YYMMDDHH),
            Pattern.compile("^" + PATTERN_YYMMDDHHMM),
            Pattern.compile("^" + PATTERN_YYMMDDHHMMSS),
            Pattern.compile("^" + PATTERN_YYMMDDHHMMSSZZZ),
    };

    static String[] formats = {
            "yyyy", "yyyyMM", "yyyyMMdd", "yyyyMMddHH", "yyyyMMddHHmm", "yyyyMMddHHmmss", "yyyyMMddHHmmsszzz"
    };

    static private  int getFormatLength(int i) {
        checkIndex(i);
        return formats[i].length();
    }

    static public String getFormatStr(int i) {
        checkIndex(i);
        return formats[i];
    }

    private static void checkIndex(int i) {
        if (i < 0 || i > 6) throw new TimeFormatException(" time format index must between 0 - 6");
    }

    /**
     * detectFormat(PATTERN_YY) = 0
     * detectFormat(PATTERN_YYMM) = 1
     * detectFormat(PATTERN_YYMMDD) = 2
     * detectFormat(PATTERN_YYMMDDHH) = 3
     * detectFormat(PATTERN_YYMMDDHHMM) = 4
     * detectFormat(PATTERN_YYMMDDHHMMSS) = 5
     * detectFormat(PATTERN_YYMMDDHHMMSSZZZ) = 6
     *
     * @param timeStr 时间格式串
     * @return 时间格式串所对应的值
     */
    static public int detectFormat(String timeStr) {
        for (int i = 0; i < timeStrPatterns.length; i++) {
            if (TimeFormatUtil.timeStrPatterns[i].matcher(timeStr).matches())
                return i;
        }

        return -1;
    }

    static public String convertFormat(String timeStr, int format) throws TimeFormatException {
        int f = detectFormat(timeStr);
        if (f < format)
            throw new TimeFormatException("{0} can not convert to format {1}", timeStr, getFormatStr(format));

        Matcher matcher = timeStrPatterns[format].matcher(timeStr);
        if (matcher.find()) {
            return matcher.group();
        }

        throw new TimeFormatException("{0} can not convert to format {1}", timeStr, getFormatStr(format));
    }

    static public String min(String dateStr, int formatIndex) throws TimeFormatException {
        int len = getFormatLength(formatIndex);
        String prefix = StringUtils.substring(dateStr,0,len);
        return StringUtils.rightPad(prefix,17,'0');
    }

    static public long toMills(String timeStr) throws TimeFormatException
    {
        int f = detectFormat(timeStr);
        if (f < 0 || f > 6)
            throw new TimeFormatException("invalidate time format str {0}", timeStr);

        String format = formats[f];
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            Date d = simpleDateFormat.parse(timeStr);
            return d.getTime();
        } catch (ParseException e) {
            throw new TimeFormatException("invalidate time format str {0}, format {1}", timeStr, format);
        }
    }

    static public String max(String dateStr, int formatIndex) throws TimeFormatException {
        int len = getFormatLength(formatIndex);
        String prefix = StringUtils.substring(dateStr,0,len);

        int pad = formatIndex - StringUtils.length(dateStr); //补偿位数

        StringBuilder sb = new StringBuilder();
        int i = 17 - prefix.length();
        if( pad > 0 ) i += pad;

        if( i > 0 ) {
            sb.insert(0,"999");
            i -= 3;
        }
        if( i > 0){
            sb.insert(0,"59");
            i-=2;
        }
        if( i > 0){
            sb.insert(0,"59");
            i-=2;
        }
        if( i > 0){
            sb.insert(0,"23");
            i-=2;
        }
        if( i > 0){
            sb.insert(0,"31");
            i-=2;
        }
        if( i > 0){
            sb.insert(0,"12");
            i-=2;
        }
        return prefix + sb.toString();
    }

}
