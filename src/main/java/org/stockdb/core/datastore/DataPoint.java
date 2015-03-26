package org.stockdb.core.datastore;
/*
 * @author nullwang@hotmail.com
 * created at 2015/3/16
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

import java.util.regex.Pattern;

public interface DataPoint {
    /**
     * Returns object id
     * @return the object id
     */
    public String getObjId();

    public String getMetricName() ;

    /**
     * The format of yyyyMMddHHmmSS.sss
     * yyyyMMddHHmmSS
     * yyyyMMddHHmm
     * yyyyMMddHH
     * yyyyMMdd
     * yyyyMM
     * yyyy
     * @return
     */
    public String getTimeStr() ;

    public String getObjValue() ;

    public String getKey();

    int MILLI_INTERVAL=0;
    int SECOND_INTERVAL=1;
    int MINUTE_INTERVAL=2;
    int HOUR_INTERVAL=3;
    int DAY_INTERVAL=4;
    int WEEK_INTERVAL=5;
    int MONTH_INTERVAL=6;
    int YEAR_INTERVAL=7;

    String PATTERN_YY="((19|20|21|22)\\d\\d)";
    String PATTERN_YYMM=PATTERN_YY + "(0[1-9]|1[012])";
    String PATTERN_YYMMDD=PATTERN_YYMM + "(0[1-9]|[12][0-9]|3[01])";
    String PATTERN_YYMMDDHH=PATTERN_YYMMDD + "([0-1][0-9]|2[0-3])";
    String PATTERN_YYMMDDHHMM = PATTERN_YYMMDDHH + "([0-5][0-9])";
    String PATTERN_YYMMDDHHMMSS=PATTERN_YYMMDDHHMM + "([0-5][0-9])";
    String PATTERN_YYMMDDHHMMSSZZZ=PATTERN_YYMMDDHHMMSS + "\\.\\d{3}";

    Pattern[]  timeStrPatterns = {
            Pattern.compile("^" + PATTERN_YYMMDDHHMMSSZZZ +"$"),
            Pattern.compile("^" + PATTERN_YYMMDDHHMMSS +"$"),
            Pattern.compile("^" + PATTERN_YYMMDDHHMM +"$"),
            Pattern.compile("^" + PATTERN_YYMMDDHH +"$"),
            Pattern.compile("^" + PATTERN_YYMMDD +"$"),
            Pattern.compile("^" + PATTERN_YYMM +"$"),
            Pattern.compile("^" + PATTERN_YY +"$"),
    };

}
