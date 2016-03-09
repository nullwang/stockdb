package org.stockdb.core.datastore;
/*
 * @author nullwang@hotmail.com
 * created at 2015/3/16
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

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.stockdb.startup.StockDBService;
import org.stockdb.core.datastore.value.NormalProcess;
import org.stockdb.core.datastore.value.SeparatorProcess;
import org.stockdb.core.datastore.value.ValueProcess;
import org.stockdb.core.event.MetricListener;
import org.stockdb.core.exception.DataStoreException;
import org.stockdb.core.exception.StockDBException;
import org.stockdb.core.util.TimeFormatUtil;
import org.stockdb.util.Commons;
import org.stockdb.util.Key;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisException;

import java.io.IOException;
import java.util.*;

@Component
public class RedisDataStore extends AbstractDataStore implements Scanable,StockDBService {

    final static int  redisDefaultPort = 6379;

    /*
   "metrics": {
       "metricName":{  "attrName":"attrValue", "attrName2":"attrValue"  },
       "metricName2":{ "attrName":"attrValue", "attrName2":"attrValue"  }
       ...
   }
   */
    final static String METRICS_KEY="metrics";
    //作为key 保存 对象的属性,当对象保存metric的数据时，会默认增加 两个 属性： id 属性 其值为 对象的id,metricNames 属性 其值为 metricName通过逗号分隔
    final static String OBJECTS_KEY ="objects";
    /*
    "objects": {
        "objectId":{"id":"idValue", "metricNames":"metricName1,metricName2,metricNameN", "attrName":"attrValue", "attrName2":"attrValue"  },
        "objectId2":{ }
        "objectId3":{}
        ...
    }
    */

    final static String METRIC_NAME_SEPARATOR=",";

    JedisWrapper jc ;
    ValueProcess valueProcess;
    Calculator calculator;
    List<MetricListener> metricListeners = new ArrayList<MetricListener>();
    Env env;

    @Override
    public void start(Env env) throws StockDBException {
        this.env = env;

        String hosts = env.get("stockdb.redis.hosts");
        LinkedList<HostAndPort> jedisClusterNodes = new LinkedList<HostAndPort>();
        String[] hostAndPorts = StringUtils.split(hosts,",");
        if( hostAndPorts.length == 0) throw new DataStoreException("stockdb.redis.hosts is empty");
        for(int i=0; i<hostAndPorts.length; i++) {
            String[] hostAndPort = StringUtils.split(hostAndPorts[i],":");
            int port = redisDefaultPort;
            if(hostAndPort.length == 0)
                throw new DataStoreException("stockdb.redis.host " + hostAndPort + " is illegal");
            if( hostAndPort.length == 2) {
                try {
                    port = Integer.parseInt(hostAndPort[1]);
                } catch (NumberFormatException e) {
                    throw new DataStoreException("stockdb.redis.host " + hostAndPort + " is illegal");
                }
            }

            jedisClusterNodes.add(new HostAndPort(hostAndPort[0], port));
        }
        JedisPool jedisPool = null;
        JedisCluster jedisCluster = null;
        if( jedisClusterNodes.size() == 1){
            jedisPool = new JedisPool(jedisClusterNodes.get(0).getHost(),jedisClusterNodes.get(0).getPort());
        }else {
            jedisCluster = new JedisCluster(new HashSet(jedisClusterNodes));
        }
        jc = new JedisWrapper(jedisPool,jedisCluster);

        if("auto".equals(env.get("stockdb.decimal") )){
            valueProcess = new SeparatorProcess();
        }else {
            valueProcess = new NormalProcess();
        }

        calculator = new Calculator(this,5);
        calculator.start();
    }

    @Override
    public void stop() throws StockDBException {
        calculator.stop();
        if( jc != null) try {
            jc.close();
        } catch (IOException e) {
            throw new StockDBException(e);
        }
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public void putData(String id,String metricName, DataPoint... dataPoints) throws StockDBException{
        assert(dataPoints != null);
        String index = getMetricAttr(metricName,Constants.METRIC_SAMPLE_INTERVAL);
        String rowKey = Key.makeRowKey(id,metricName);
        for(DataPoint dataPoint:dataPoints) {
            try {
                int dataPointTimeIndex = TimeFormatUtil.detectFormat(dataPoint.getTimeStr());
                if (dataPointTimeIndex < 0)
                    throw new StockDBException("DataPoint[" + rowKey + "]" + "has bad format timeStr[" + dataPoint.getTimeStr() + "]");

                if (index == null) {
                    setSampleInterval(metricName, dataPointTimeIndex);
                } else {
                    int i = Integer.parseInt(index);
                    if (dataPointTimeIndex != i) {
                        throw new StockDBException("Metric [" + metricName + "]" + "has two different time format[" +
                                TimeFormatUtil.getFormatStr(i) + "," +
                                TimeFormatUtil.getFormatStr(dataPointTimeIndex) + "]");
                    }
                }
            } catch (NumberFormatException e) {
                throw new StockDBException("Metric[" + metricName + "]attribute[" + Constants.METRIC_SAMPLE_INTERVAL + "]value error");
            }
        }
        for(DataPoint dataPoint: dataPoints){
            String v = filterValue(dataPoint.getValue());
            jc.hset(rowKey,dataPoint.getTimeStr(),v);
        }
        addObjectMeta(id, metricName);
        notifyListener(id, metricName, dataPoints);
    }

    @Override
    public void putMetric(String name) throws StockDBException
    {
        if( StringUtils.isEmpty(name)){
            throw new StockDBException("metric name should not be empty");
        }

        if(! jc.hexists(METRICS_KEY,name) ) {
            jc.hset(METRICS_KEY, name,"");
        }
    }

    @Override
    public void setMetricAttr(String name, String attr, String value) throws StockDBException{
        if( StringUtils.isEmpty(name)){
            throw new StockDBException("metric name should not be empty");
        }
        if(StringUtils.isEmpty(attr)){
            throw new StockDBException("attribute name should not be empty");
        }

        String attrValue = jc.hget(METRICS_KEY,name);
        jc.hset(METRICS_KEY,name, Commons.jsonPut(attrValue,attr,value));
    }

    @Override
    public void removeMetricAttr(String name, String attrName) throws StockDBException {
        if( name == null){
            throw new IllegalArgumentException("metric name should not be null");
        }
        if(attrName == null){
            throw new IllegalArgumentException("attribute name should not be null");
        }

        String attrValue = jc.hget(METRICS_KEY,name);
        jc.hset(METRICS_KEY,name, Commons.jsonRemove(attrValue,attrName));
    }

    @Override
    public String getMetricAttr(String name, String attr) {
        if( name == null){
            throw new IllegalArgumentException("metric name should not be null");
        }
        if(attr == null){
            throw new IllegalArgumentException("attribute name should not be null");
        }
        Map<String,String> attrValue = Commons.jsonMap(jc.hget(METRICS_KEY, name));
        return attrValue.get(attr);
    }

    @Override
    public void setObjAttr(String id, String attr, String value) throws StockDBException {
        if( id == null){
            throw new StockDBException("object id should not be null");
        }
        if(attr == null){
            throw new StockDBException("attribute name should not be null");
        }

        String attrValue = jc.hget(OBJECTS_KEY,id);
        jc.hset(OBJECTS_KEY,id, Commons.jsonPut(attrValue,attr,value));
    }

    @Override
    public void removeObjAttr(String id, String attr) throws StockDBException {
        if( id == null){
            throw new StockDBException("object id should not be null");
        }
        if(attr == null){
            throw new StockDBException("attribute name should not be null");
        }

        String attrValue = jc.hget(OBJECTS_KEY,id);
        jc.hset(OBJECTS_KEY,id, Commons.jsonRemove(attrValue,attr));
    }

    @Override
    public String getObjAttr(String id, String attr) throws StockDBException {
        if( id == null){
            throw new IllegalArgumentException("object id should not be null");
        }
        if(attr == null){
            throw new IllegalArgumentException("attribute name should not be null");
        }
        Map<String,String> attrValue = Commons.jsonMap(jc.hget(OBJECTS_KEY, id));
        return attrValue.get(attr);
    }

    @Override
    public Map<String, String> getObjAttr(String id) throws StockDBException {
        if( id == null){
            throw new IllegalArgumentException("object id should not be null");
        }
        Map<String,String> attrValue = Commons.jsonMap(jc.hget(OBJECTS_KEY, id));
        return attrValue;
    }

    @Override
    public Set<String> getMetricNames() {
        return jc.hkeys(METRICS_KEY);
    }

    @Override
    public Map<String, String> getMetricAttr(String name) throws StockDBException {
        return Commons.jsonMap(jc.hget(METRICS_KEY, name));
    }

    @Override
    public String getValue(String id, String metricName, String timeStr) {
        return jc.hget(Key.makeRowKey(id,metricName),timeStr);
    }

    /**
     *
     * @param id
     * @param metricName
     * @param startTime the start time >=起始时间
     * @param endTime the end time <=结束时间
     * @return 数据点
     */
    @Override
    public List<DataPoint> getData(String id, String metricName, String startTime, String endTime) {
        //jc.hvals(Key.makeRowKey(id,metricName));
        Map<String,String> datas = jc.hgetAll(Key.makeRowKey(id,metricName));
        List<DataPoint> points = new ArrayList<DataPoint> ();
        for(Map.Entry<String,String> entry: datas.entrySet()){
            if( Commons.between(startTime,endTime,entry.getKey())){
                DataPoint dataPoint = new DataPoint();
                dataPoint.setTimeStr(entry.getKey());
                dataPoint.setValue(entry.getValue());
                points.add(dataPoint);
            }
        }
        return points;
    }

    public synchronized void addMetricListener(MetricListener metricListener){
        metricListeners.add(metricListener);
    }

    public synchronized void removeMetricListener(MetricListener metricListener){
        metricListeners.remove(metricListener);
    }

    private void notifyListener(String id, String metricName, DataPoint... dataPoints)
    {
        List<MetricListener> listeners = new ArrayList<MetricListener>(metricListeners);
        for(MetricListener listener: listeners){
            listener.dataPointChange(id, metricName,dataPoints);
        }
    }

    @Override
    public Collection<DataPoint> queryData(String id, String metricName, String startTime, String endTime) {
        return ScanableAdapter.build(this,id,metricName,startTime,endTime);
    }

    @Override
    public void clearMetrics() {
        //remove
        jc.del(METRICS_KEY);
    }

    @Override
    public void clearData() {
        //clean all data
        Set<String> ids = jc.hkeys(OBJECTS_KEY);
        for (String id: ids){
            String m = getObjAttr(id,"metricNames");
            String[] metricNames = StringUtils.split(m,METRIC_NAME_SEPARATOR);
            if(metricNames == null) return;
            for(String metricName: metricNames){
                String rowKey = Key.makeRowKey(id,metricName);
                jc.del(rowKey); //del data
            }
        }

        //clean meta
        jc.del(OBJECTS_KEY);
    }

    @Override
    public void clearData(String id, String metricName) {
        String rowKey = Key.makeRowKey(id,metricName);
        jc.del(rowKey); //del data
        removeObjectMeta(id,metricName);
    }

    @Override
    public Map getObjectMeta(String id) {
        return Commons.jsonMap(jc.hget(OBJECTS_KEY,id));
    }

    private void addObjectMeta(String id, String metricName)
    {
        String v = getObjAttr(id,"id");
        if( v == null ){
            setObjAttr(id,"id",id);
        }
        String m = getObjAttr(id,"metricNames");
        String[] metricNames = StringUtils.split(m,METRIC_NAME_SEPARATOR);
        if(!Commons.contains(metricNames,metricName) ){
            setObjAttr(id,"metricNames", StringUtils.isEmpty(m) ? metricName : m+","+metricName);
        }
    }

    private void removeObjectMeta(String id, String metricName)
    {
        String m = getObjAttr(id,"metricNames");
        String[] metricNames = StringUtils.split(m,METRIC_NAME_SEPARATOR);
        metricNames = Commons.remove(metricNames,metricName);
        if( Commons.isEmpty(metricNames) ){
            removeObjAttr(id,"id");
            removeObjAttr(id,"metricNames");
        }else{
            setObjAttr(id,"metricNames",StringUtils.join(metricNames,","));
        }
    }

    @Override
    public ScanResult scan(String id, String metricName, String cursor) {
        return  jc.hscan(Key.makeRowKey(id,metricName),"0");
    }

    private final String filterValue(String v){
        return valueProcess.filter(v);
    }

    @ExceptionHandler(JedisException.class)
    private void handlerJedisException(JedisException e) throws StockDBException{
        throw new StockDBException(e);
    }

    @ExceptionHandler(Exception.class)
    private void handlerException(Exception e) throws StockDBException
    {
        throw new StockDBException(e);
    }
}