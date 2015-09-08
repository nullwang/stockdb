package org.stockdb.core.http.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.stockdb.core.datastore.*;
import org.stockdb.core.exception.StockDBException;
import org.stockdb.core.http.rest.model.DataQueryReq;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@Controller
@RequestMapping(value="/api/v1")
public class MetricsController {

    @Autowired
    private DataStore dataStore;

    @RequestMapping(value = "/version", method = RequestMethod.GET )
    @ResponseBody
    public String version() throws StockDBException
    {
        return "{\"version\":\"1.0\"}";
    }

    @RequestMapping(value = "/ds", method = RequestMethod.POST )
    public @ResponseBody
    void putDataPoints(@RequestBody ObjectMetricDataSet objectMetricDataSet) throws StockDBException {
        dataStore.putData(objectMetricDataSet);
    }

    @RequestMapping(value = "/d", method = RequestMethod.POST )
    public @ResponseBody
    void putDataPoints(@RequestBody ObjectMetricData[] objectMetricData) throws StockDBException {
        for(ObjectMetricData data: objectMetricData){
            DataPoint dataPoint = new DataPoint(data.getTimeStr(),data.getValue());
            dataStore.putData(data.getId(),data.getMetricName(),dataPoint);
        }
    }

    @RequestMapping(value = "/meta", method = RequestMethod.GET )
    public @ResponseBody
    Map getMeta() throws StockDBException {
        Map map = new HashMap();
        for(String metric:  dataStore.getMetrics()){
            map.put(metric,dataStore.getMetricAttr(metric));
        }
        return map;
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST )
    public @ResponseBody
    ObjectMetricDataSet queryDataPoints(@RequestBody DataQueryReq dataQueryReq) throws StockDBException {
        dataQueryReq.getObjectMetrics();

//        return dataStore.getData(dataQueryReq.getId(), dataQueryReq.getMetricName(),
//                dataQueryReq.getStartTime(), dataQueryReq.getEndTime());
        return null;
    }

    @RequestMapping(value = "/list/{id}/{metricName}/{startTime}/{endTime}", method = RequestMethod.GET )
    public @ResponseBody
    List<DataPoint> getDataPoints(@PathVariable("id") String id,
                       @PathVariable String metricName,
                       @PathVariable String startTime, @PathVariable String endTime) throws StockDBException {
        return dataStore.getData(id, metricName,startTime,endTime);
    }

    @RequestMapping(value = "/{id}/{metricName}/{timeStr}", method = RequestMethod.GET )
    public @ResponseBody
    DataPoint getDataPoint(@PathVariable("id") String id,
                                  @PathVariable String metricName,
                                  @PathVariable String timeStr) throws StockDBException {
        String value  = dataStore.getValue(id,metricName,timeStr);
        return new DataPoint(metricName,value);
    }

    @RequestMapping(value = "/metrics", method = RequestMethod.POST )
    public @ResponseBody
    void putDataPoints(@RequestBody Metric[] metrics) throws StockDBException
    {
        dataStore.putMetric(metrics);
    }
}
