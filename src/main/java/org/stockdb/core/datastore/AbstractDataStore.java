package org.stockdb.core.datastore;
/*
 * @author nullwang@hotmail.com
 * created at 2015/4/7
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

import org.stockdb.core.datastore.impl.MetricBuilder;
import org.stockdb.core.exception.StockDBException;
import org.stockdb.util.Commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class AbstractDataStore implements DataStore {

    @Override
    public void putMetric(Metric... metrics) throws StockDBException {
        for(Metric metric: metrics ) {
            for(Attribute attribute: metric.getAttrs()) {
                setMetricAttr(metric.getName(), attribute.getName(), attribute.getValue());
            }
        }
    }
    @Override
    public Metric getMetric(String metricName) throws StockDBException
    {
        Map map = getMetricAttr(metricName);
        return MetricBuilder.build(metricName,map);
    }

    public void setSampleInterval(String name, int i) throws StockDBException
    {
        setMetricAttr(name, Constants.METRIC_SAMPLE_INTERVAL,String.valueOf(i));
    }

    public void putData(ObjectMetricDataSet objectMetricDataSet) throws StockDBException{
        assert(objectMetricDataSet != null);
        String id = objectMetricDataSet.getId();
        String metricName = objectMetricDataSet.getMetricName();
        putData(id,metricName,objectMetricDataSet.getDataPoints());
    }

    @Override
    public List<FunctionMetric> getFunctionMetrics(String metricName) throws StockDBException{
        Collection<String> names = getMetricNames();
        List<FunctionMetric> metricList = new ArrayList();
        for(String name: names){
            Metric metric = getMetric(name);
            if( metric instanceof FunctionMetric){
                FunctionMetric functionMetric = (FunctionMetric) metric;
                if( Commons.contains(functionMetric.getBaseMetrics(),metricName)){
                    metricList.add(functionMetric);
                }
            }
        }
        return metricList;
    }

}
