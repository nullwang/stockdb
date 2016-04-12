package org.stockdb.http.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.stockdb.core.datastore.*;
import org.stockdb.core.exception.StockDBException;
import org.stockdb.http.rest.model.DataQueryReq;

import java.util.*;

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
public class MetricsController {

    @Autowired
    private DataStore dataStore;

    @Autowired
    private Env env;

    @RequestMapping(value = "/version", method = RequestMethod.GET )
    public @ResponseBody String version() throws StockDBException
    {
        return "{\"version\":\"1.0\"}";
    }


    @RequestMapping(value = "/clean", method = RequestMethod.GET )
    public @ResponseBody String clean(){
        if( "true".equals(env.get("stockdb.debug"))){
            dataStore.clearMetrics();
            dataStore.clearData();

            return "clean metrics & data ok";
        }
        return "sockdb.debug is disable";
    }

    /**
     * 数据集导入
     * @param objectMetricDataSet
     * @throws StockDBException
     */
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

    /**
     * 查询 起始
     * @param dataQueryReq
     * @return
     * @throws StockDBException
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST )
    public @ResponseBody
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

    /**
     *
     * @param id 对象标示
     * @param metricName 属性字段
     * @param startTime 起始时间, >=
     * @param endTime 截止时间, <=
     * @return 指定对象特定属性在某个时间范围的结果集
     * @throws StockDBException
     */
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

    @RequestMapping(value = "/{id}/{attrName}", method = RequestMethod.GET )
    public @ResponseBody
    String getAttribute(@PathVariable("id") String id,
                           @PathVariable String attrName) throws StockDBException {
        String value  = dataStore.getObjAttr(id,attrName);
        return value;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET )
    public @ResponseBody
    Map<String,String> getAttributes(@PathVariable("id") String id) throws StockDBException {
        return dataStore.getObjAttr(id);
    }

    @RequestMapping(value = "/metrics", method = RequestMethod.POST )
    public @ResponseBody
    void putMetrics(@RequestBody Metric[] metrics) throws StockDBException
    {
        dataStore.putMetric(metrics);
    }

    @RequestMapping(value = "/metrics", method = RequestMethod.GET )
    public @ResponseBody
    Map getMeta() throws StockDBException {
        Map map = new HashMap();
        for(String metric:  dataStore.getMetricNames()){
            map.put(metric,dataStore.getMetricAttr(metric));
        }
        return map;
    }

    //指标属性列表
    @RequestMapping(value = "/metric/{metricName}", method = RequestMethod.GET )
    public @ResponseBody
    Map<String,String> getMetrics(@PathVariable("metricName") String metricName) throws StockDBException
    {
        return dataStore.getMetricAttr(metricName);
    }

    //指标属性查询
    @RequestMapping(value = "/metric/{metricName}/{attrName}", method = RequestMethod.GET )
    public @ResponseBody
    String getMetricAttr(@PathVariable("metricName") String metricName,
                         @PathVariable("attrName") String attrName) throws StockDBException
    {
        return dataStore.getMetricAttr(metricName,attrName);
    }

    //对象属性列表
    @RequestMapping(value = "/object/{objId}", method = RequestMethod.GET )
    public @ResponseBody
    Map<String,String> getObject(@PathVariable("objId") String objectId) throws StockDBException
    {
        return dataStore.getObjAttr(objectId);
    }

    //对象属性查询
    @RequestMapping(value = "/object/{objId}/{attrName}", method = RequestMethod.GET )
    public @ResponseBody
    String getObjectAttr(@PathVariable("objId") String objectId,
                                     @PathVariable("attrName") String attrName) throws StockDBException
    {
        return dataStore.getObjAttr(objectId,attrName);
    }
}
