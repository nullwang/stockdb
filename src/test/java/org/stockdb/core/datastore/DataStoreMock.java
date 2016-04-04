package org.stockdb.core.datastore;
/*
 * @author nullwang@hotmail.com
 * created at 2016/3/28
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
import org.stockdb.util.Commons;
import org.stockdb.util.Key;

import java.util.*;

public class DataStoreMock extends AbstractDataStore {
    Map<String,List<DataPoint>> data = new HashMap();

    @Override
    public void setMetricAttr(String name, String attr, String value) throws StockDBException {

    }

    @Override
    public String getMetricAttr(String name, String attrName) throws StockDBException {
        return null;
    }

    @Override
    public void removeMetricAttr(String name, String attrName) throws StockDBException {

    }

    @Override
    public void setObjAttr(String id, String attr, String value) throws StockDBException {

    }

    @Override
    public String getObjAttr(String id, String attr) throws StockDBException {
        return null;
    }

    @Override
    public void removeObjAttr(String id, String attr) throws StockDBException {

    }

    @Override
    public Map<String, String> getObjAttr(String id) throws StockDBException {
        return null;
    }

    @Override
    public Set<String> getMetricNames() {
        return null;
    }

    @Override
    public Map<String, String> getMetricAttr(String name) throws StockDBException {
        return null;
    }

    @Override
    public String getValue(String id, String metricName, String timeStr) {
        return null;
    }

    @Override
    public List<DataPoint> getData(String id, String metricName, String startTime, String endTime) {
        List<DataPoint> points = data.get(Key.makeRowKey(id,metricName));
        List<DataPoint> dataPointList = new ArrayList<DataPoint>();
        for(DataPoint dp: points){
            if(Commons.between(startTime,endTime,dp.getTimeStr())) {
                dataPointList.add(dp);
            }
        }
        return dataPointList;
    }

    @Override
    public void putData(String id, String metricName, DataPoint... dataPoints) throws StockDBException {
        List<DataPoint> points = data.get(Key.makeRowKey(id,metricName));
        if( points == null ) points = new ArrayList<DataPoint>();
        for(DataPoint dp : dataPoints){
            points.add(dp);
        }
        data.put(Key.makeRowKey(id,metricName),points);
    }

    @Override
    public void removeData(String id, String metricName, DataPoint... dataPoints) {

    }

    @Override
    public void putMetric(String metricName) throws StockDBException {

    }

    @Override
    public Collection<DataPoint> queryData(String id, String metricName, String startTime, String endTime) {
        return null;
    }

    @Override
    public void clearMetrics() {

    }

    @Override
    public void clearData() {
        data.clear();
    }

    @Override
    public void removeData(String id, String metricName) {

    }

    @Override
    public Map getObjectMeta(String id) {
        return null;
    }

    @Override
    public void invoke(Function function, Map parameters) {

    }
}
