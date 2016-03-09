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

import org.apache.commons.lang3.ObjectUtils;

public class DataPoint {


    private String timeStr;
    private String value;

    /**
     * The format of yyyyMMddHHmmSS.sss
     * yyyyMMddHHmmSS
     * yyyyMMddHHmm
     * yyyyMMddHH
     * yyyyMMdd
     * yyyyMM
     * yyyy
     *
     * @return
     */
    public String getTimeStr() {
        return timeStr;
    }

    public String getValue() {
        return value;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DataPoint(String timeStr, String value) {
        this.timeStr = timeStr;
        this.value = value;
    }

    public DataPoint() {
    }

    @Override
    public int hashCode()
    {
        String str = (timeStr+value);
        return str.hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        if( !(o instanceof DataPoint) ) return false;
        DataPoint dp = (DataPoint) o;
        return ObjectUtils.equals(dp.timeStr,timeStr) &&
                ObjectUtils.equals(dp.value, value);
    }
}
