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
import org.stockdb.core.functions.Function;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DataStore {


    /** Set the attribute value of metric
     * @param name the name of metric, should not be null
     * @param attr the name of attribute,should not be null
     * @param value the value of attribute
     */
    void setMetricAttr(String name, String attr, String value) throws StockDBException;

    /**
     * 获取名称为name 的 metric 的 指定属性 attr 的值
     * @param name the name of metric
     * @param attrName the name of attribute
     * @return the value
     * @throws StockDBException
     */
    String getMetricAttr(String name, String attrName) throws StockDBException;

    void removeMetricAttr(String name, String attrName ) throws StockDBException;

    void setObjAttr(String id, String attr, String value) throws StockDBException;

    String getObjAttr(String id, String attr) throws StockDBException;

    void removeObjAttr(String id, String attr) throws StockDBException;

    Map<String,String> getObjAttr(String id) throws StockDBException;

    Set<String> getMetricNames();

    /**
     * 获取子metric，即通过metricName进行运算而产生的function metric
     * @param metricName
     * @return
     * @throws StockDBException
     */
    List<FunctionMetric> getFunctionMetrics(String metricName) throws StockDBException;

    Map<String,String> getMetricAttr(String name) throws StockDBException;

    String getValue(String id, String metricName, String timeStr);
    /**
     * Returns data points which timeStr is between [startTime,endTime)
     * @param startTime the start time
     * @param endTime the end time
     * @return data points
     */
    List<DataPoint> getData(String id, String metricName, String startTime, String endTime);

    /**
     * 移除指定时间段内的数据
     * @param id
     * @param metricName
     * @param startTime
     * @param endTime
     * @return
     */
    List<DataPoint> removeData(String id, String metricName, String startTime, String endTime);

    void putData(String id, String metricName, DataPoint... dataPoints) throws StockDBException;

    void removeData(String id,String metricName, DataPoint... dataPoints);

    void putData(ObjectMetricDataSet objectMetricDataSet) throws StockDBException;

    void putMetric(Metric... metrics) throws StockDBException;

    Metric getMetric(String metricName) throws StockDBException;

    void putMetric(String metricName) throws StockDBException;

    /**
     * Returns the iterator of data points which timeStr is between [startTime,endTime)
     * @param startTime the start time
     * @param endTime the end time
     * @return data points
     */
    Collection<DataPoint> queryData(String id, String metricName, String startTime, String endTime);

    void clearMetrics();

    void clearData();

    void removeData(String id, String metricName);

    Map getObjectMeta(String id);

    /**
     * 使用指定参数进行函数调用，返回结果又函数进行处理
     * @param function
     * @param parameters
     */
    void invoke(Function function, Map parameters);

}
