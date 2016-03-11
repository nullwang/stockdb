package org.stockdb.core.event;
/*
 * @author nullwang@hotmail.com
 * created at 2016/3/11
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

public class MetricEvent {

    static public int POINTS_PUT = 0;

    static public int POINTS_REMOVE = 1;

    int type;

    String id;

    String metricName;

    DataPoint[] points;

    public MetricEvent() {
    }

    public MetricEvent(int type, String id, String metricName, DataPoint[] points) {
        this.type = type;
        this.id = id;
        this.metricName = metricName;
        this.points = points;
    }

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

    public DataPoint[] getPoints() {
        return points;
    }

    public void setPoints(DataPoint[] points) {
        this.points = points;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
