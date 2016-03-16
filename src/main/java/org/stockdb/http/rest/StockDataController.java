package org.stockdb.http.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.stockdb.core.datastore.DataPoint;
import org.stockdb.core.datastore.DataStore;
import org.stockdb.core.datastore.ObjectMetricDataSet;
import org.stockdb.core.exception.StockDBException;
import org.stockdb.http.rest.model.DataQueryReq;
import org.stockdb.http.rest.model.KlineReq;

import java.util.ArrayList;
import java.util.List;

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
@Controller
@RequestMapping(value="/stock")
public class StockDataController {

    @Autowired
    private DataStore dataStore;

    private String[] K_METRIC_NAMES={"day_lowest_price","day_highest_price","day_open_price","day_close_price"};

    /**
     *k 线接口
     */
    @RequestMapping(value = "/kline", method = RequestMethod.POST )
    public @ResponseBody
    List<ObjectMetricDataSet> klineData(@RequestBody KlineReq req) throws StockDBException {
        String startTime = req.getStartTime();
        String endTime = req.getEndTime();
        String objId = req.getId();
        List<ObjectMetricDataSet> objectMetricDataSets = new ArrayList<ObjectMetricDataSet>();
        for(String metricName: K_METRIC_NAMES){
            ObjectMetricDataSet objectMetricDataSet= new ObjectMetricDataSet();
            List<DataPoint> points = dataStore.getData(objId,metricName,startTime,endTime);

            objectMetricDataSet.setId(objId);
            objectMetricDataSet.setMetricName(metricName);
            objectMetricDataSet.setDataPoints(points.toArray(new DataPoint[points.size()]));
            objectMetricDataSets.add(objectMetricDataSet);
        }
        return objectMetricDataSets;
    }

    List<ObjectMetricDataSet> queryDataPoints(@RequestBody DataQueryReq dataQueryReq) throws StockDBException {
        dataQueryReq.getObjectMetrics();

        String startTime = dataQueryReq.getStartTime();
        String endTime = dataQueryReq.getEndTime();
        List<ObjectMetricDataSet> objectMetricDataSets = new ArrayList<ObjectMetricDataSet>();
        for(DataQueryReq.ObjectMetric objectMetric : dataQueryReq.getObjectMetrics()){
            String objId = objectMetric.getId();
            String metricName = objectMetric.getMetricName();
            ObjectMetricDataSet objectMetricDataSet = new ObjectMetricDataSet();
            List<DataPoint> points = dataStore.getData(objId,metricName,startTime,endTime);

            objectMetricDataSet.setId(objId);
            objectMetricDataSet.setMetricName(metricName);
            objectMetricDataSet.setDataPoints(points.toArray(new DataPoint[points.size()]));
            objectMetricDataSets.add(objectMetricDataSet);
        }
        return objectMetricDataSets;
    }

}
