package org.stockdb.core.http.rest;
/*
 * @author nullwang@hotmail.com
 * created at 2015/3/13
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

import org.stockdb.core.datastore.DataPoint;
import org.stockdb.util.Key;

public class DataPointImpl implements DataPoint{

    String objId; //object objId
    String metricName;
    String timeStr;
    String objValue;

    public String getObjId() {
        return objId;
    }

    public String getMetricName() {
        return metricName;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public String getObjValue() {
        return objValue;
    }

    @Override
    public String getKey() {
        return Key.makeRowKey(objId,metricName);
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public void setObjValue(String objValue) {
        this.objValue = objValue;
    }
}
