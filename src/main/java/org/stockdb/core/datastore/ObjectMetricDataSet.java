package org.stockdb.core.datastore;
/*
 * @author nullwang@hotmail.com
 * created at 2015/5/21
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

public class ObjectMetricDataSet {
    String id; //object id

    String metricName;

    DataPoint[] dataPoints;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public DataPoint[] getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(DataPoint[] dataPoints) {
        this.dataPoints = dataPoints;
    }
}