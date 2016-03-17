package org.stockdb.http.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.stockdb.core.datastore.DataPoint;
import org.stockdb.core.datastore.DataStore;
import org.stockdb.core.datastore.ObjectMetricDataSet;
import org.stockdb.core.exception.StockDBException;
import org.stockdb.core.exception.TimeFormatException;
import org.stockdb.core.util.TimeFormatUtil;
import org.stockdb.http.rest.model.DataQueryReq;
import org.stockdb.http.rest.model.StockObjectReq;

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
    private String[] TIME_INDEX_METRIC_NAMES={"price","volume"};

    /**
     *k 线接口
     *
     * k 线 默认查询 day_lowest_price
     *              day_highest_price
     *              day_open_price
     *              day_close_price
     * 4个metric ， 默认查询所有时间范围内数据
     *
     */
    @RequestMapping(value = "/kline/{id}", method = RequestMethod.GET )
    public @ResponseBody
    List<ObjectMetricDataSet> klineData(@PathVariable("id") String id) throws StockDBException {
        StockObjectReq req = new StockObjectReq();
        req.setId(id);
        return queryDataPoints(req,K_METRIC_NAMES);
    }

    /**
     *分时图接口
     *默认查询       price
     *              volume
     * 2个metric ， 默认查询所有时间范围内数据
     *
     */
    @RequestMapping(value = "/timeIndex/{id}", method = RequestMethod.GET )
    public @ResponseBody
    List<ObjectMetricDataSet> timeIndexData(@PathVariable("id") String id) throws StockDBException {
        StockObjectReq req = new StockObjectReq();
        req.setId(id);
        return queryDataPoints(req,TIME_INDEX_METRIC_NAMES);
    }

    List<ObjectMetricDataSet> queryDataPoints(StockObjectReq req, String[] metricNames) throws StockDBException {
        String startTime = req.getStartTime();
        String endTime = req.getEndTime();
        String objId = req.getId();
        List<ObjectMetricDataSet> objectMetricDataSets = new ArrayList<ObjectMetricDataSet>();
        for(String metricName: metricNames){
            ObjectMetricDataSet objectMetricDataSet= new ObjectMetricDataSet();
            List<DataPoint> points = dataStore.getData(objId,metricName,startTime,endTime);
            points = timeConvert(points);
            objectMetricDataSet.setId(objId);
            objectMetricDataSet.setMetricName(metricName);
            objectMetricDataSet.setDataPoints(points.toArray(new DataPoint[points.size()]));
            objectMetricDataSets.add(objectMetricDataSet);
        }
        return objectMetricDataSets;
    }

    List<DataPoint> timeConvert(List<DataPoint> dps) throws TimeFormatException {
        List<DataPoint> dataPointList = new ArrayList<DataPoint>();
        for(DataPoint dp: dps){
            DataPoint dataPoint = new DataPoint();
            dataPoint.setTimeStr(String.valueOf(TimeFormatUtil.toMills(dp.getTimeStr())));
            dataPoint.setValue(dp.getValue());
            dataPointList.add(dp);
        }
        return dataPointList;
    }

}
