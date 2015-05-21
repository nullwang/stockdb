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

import org.stockdb.core.exception.StockDBException;

import java.util.concurrent.TimeUnit;

public abstract class AbstractDataStore implements DataStore {

    @Override
    public void putMetric(Metric... metrics) throws StockDBException {
        for(Metric metric: metrics ) {
            setMetricAttr(metric.name,metric.attr,metric.value);
        }
    }

    public void putData(ObjectMetricDataSet objectMetricDataSet) throws StockDBException{
        assert(objectMetricDataSet != null);
        String id = objectMetricDataSet.getId();
        String metricName = objectMetricDataSet.getMetricName();
        putData(id,metricName,objectMetricDataSet.getDataPoints());
    }

}
