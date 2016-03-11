package org.stockdb.core.functions;
/*
 * @author nullwang@hotmail.com
 * created at 2016/2/15
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

import org.stockdb.core.exception.TimeFormatException;
import org.stockdb.core.util.TimeFormatUtil;

import java.util.Objects;

public abstract class TimeScope {

    abstract public String getStartTime();

    abstract public String getEndTime();

    abstract boolean isInSameDay();

    abstract boolean isInSameMonth();

    abstract boolean isInSameHour();

    abstract boolean isInSameMinute();

    static public TimeScope build(String startTime, String endTime) {
       return new TimeScopeImpl(startTime,endTime);
    }

    static public TimeScope buildByDay(String day){
        String st = TimeFormatUtil.min(day, TimeFormatUtil.YYMMDD);
        String et = TimeFormatUtil.max(day, TimeFormatUtil.YYMMDD);
        return new TimeScopeImpl(st,et);
    }

    private static  class TimeScopeImpl extends TimeScope{
        String startTime;
        String endTime;

        private TimeScopeImpl(String startTime, String endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        @Override
        boolean isInSameDay() {
            try {
                String st = TimeFormatUtil.convertFormat(startTime, TimeFormatUtil.YYMMDD);
                String et = TimeFormatUtil.convertFormat(startTime, TimeFormatUtil.YYMMDD);
                return st != null && st.equals(et);
            }catch (TimeFormatException te){
                return false;
            }
        }

        @Override
        boolean isInSameMonth() {
            try {
                String st = TimeFormatUtil.convertFormat(startTime, TimeFormatUtil.YYMM);
                String et = TimeFormatUtil.convertFormat(startTime, TimeFormatUtil.YYMM);
                return st != null && st.equals(et);
            }catch (TimeFormatException te){
                return false;
            }
        }

        @Override
        boolean isInSameHour() {
            try {
                String st = TimeFormatUtil.convertFormat(startTime, TimeFormatUtil.YYMMDDHH);
                String et = TimeFormatUtil.convertFormat(startTime, TimeFormatUtil.YYMMDDHH);
                return st != null && st.equals(et);
            }catch (TimeFormatException te){
                return false;
            }
        }

        @Override
        boolean isInSameMinute() {
            try {
                String st = TimeFormatUtil.convertFormat(startTime, TimeFormatUtil.YYMMDDHHMM);
                String et = TimeFormatUtil.convertFormat(startTime, TimeFormatUtil.YYMMDDHHMM);
                return st != null && st.equals(et);
            }catch (TimeFormatException te){
                return false;
            }
        }

        @Override
        public String toString(){
            return "startTime="+ startTime + ",endTime=" + endTime;
        }

        @Override
        public int hashCode(){
            return Objects.hashCode(startTime) + Objects.hashCode(endTime);
        }

        @Override
        public boolean equals( Object o){
            if( o instanceof TimeScopeImpl){
                TimeScopeImpl ts = (TimeScopeImpl) o;
                return Objects.equals(ts.startTime,startTime) &&
                        Objects.equals(ts.endTime, endTime);
            }
            return false;
        }
    }

}
