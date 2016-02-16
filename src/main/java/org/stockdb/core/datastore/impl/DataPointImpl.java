package org.stockdb.core.datastore.impl;
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.stockdb.util.Key;

public class DataPointImpl {

    String id; //object id
    String metricName;
    String timeStr;
    String value;

    public String getId() {
        return id;
    }

    public String getMetricName() {
        return metricName;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public String getValue() {
        return value;
    }

    @JsonIgnore
    public String getKey() {
        return Key.makeRowKey(id,metricName);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
