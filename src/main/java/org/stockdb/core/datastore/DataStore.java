package org.stockdb.core.datastore;
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

import org.stockdb.core.exception.StockDBException;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface DataStore {


    /** Set the attribute value of metric
     * @param name the name of metric, should not be null
     * @param attr the name of attribute,should not be null
     * @param value the value of attribute
     */
    void setMetricAttr(String name, String attr, String value) throws StockDBException;

    String getMetricAttr(String name, String attr) throws StockDBException;

    void setObjAttr(String id, String attr, String value) throws StockDBException;

    String getObjAttr(String id, String attr) throws StockDBException;

    Set<String> getMetrics();

    DataPoint getData(String id, String metricName, String timeStr);
    /**
     * Returns data points which timeStr is between [startTime,endTime)
     * @param startTime the start time
     * @param endTime the end time
     * @return data points
     */
    List<DataPoint> getData(String id, String metricName, String startTime, String endTime);

    void putData(DataPoint... dataPoints) throws StockDBException;

    void putMetric(Metric... metrics) throws StockDBException;

    /**
     * Returns the iterator of data points which timeStr is between [startTime,endTime)
     * @param startTime the start time
     * @param endTime the end time
     * @return data points
     */
    Collection<DataPoint> queryData(String id, String metricName, String startTime, String endTime);


}
