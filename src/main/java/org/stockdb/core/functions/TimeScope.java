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

public abstract class TimeScope {

    abstract public String getStartTime();

    abstract public String getEndTime();

    //根据日期进行时间对齐
    //TimeScope buildByDayAlign()

    static public TimeScope build(String startTime, String endTime) {
       return new TimeScopeImpl(startTime,endTime);
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
    }


}
