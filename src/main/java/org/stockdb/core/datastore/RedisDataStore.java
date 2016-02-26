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

import com.google.gson.stream.JsonReader;
import org.apache.commons.lang.StringUtils;
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
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Component
public class RedisDataStore extends AbstractDataStore implements Scanable,StockDBService {

    final static int  redisDefaultPort = 6379;

    final static String METRICS_KEY="metrics";
    final static String OBJECT_KEY="objects";
    final static String META_KEY="meta";
    final static String META_SETTING_KEY="meta_set";

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

        loadMeta();

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

    //load meta data
    void loadMeta() throws StockDBException{
        //check if stock meta is loaded
        long exist = jc.setnx(META_SETTING_KEY, "1");
        if( exist == 0) return;
        try {
            JsonReader jsonReader = new JsonReader(new InputStreamReader(this.getClass().getResourceAsStream("/stock_metric.dat"),"utf-8"));
            jsonReader.beginArray();
            while(jsonReader.hasNext()){
                jsonReader.beginObject();
                jsonReader.nextName();
                String metricName = jsonReader.nextString();
                jsonReader.nextName();
                String attrName = jsonReader.nextString();
                jsonReader.nextName();
                String attrValue = jsonReader.nextString();
                setMetricAttr(metricName,attrName,attrValue);
                jsonReader.endObject();
            }
            jsonReader.endArray();
            jsonReader.close();
        } catch (UnsupportedEncodingException e) {
            throw new StockDBException(e);
        } catch (IOException e) {
            throw new StockDBException(e);
        }
    }

    @Override
    public void putData(String id,String metricName, DataPoint[] dataPoints) throws StockDBException{
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
                        throw new StockDBException("Metric [" + metricName + "]" + "has two different time format[" + i + "," + dataPointTimeIndex + "]");
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

        String attrValue = jc.hget(OBJECT_KEY,id);
        jc.hset(OBJECT_KEY,id, Commons.jsonPut(attrValue,attr,value));
    }

    @Override
    public String getObjAttr(String id, String attr) throws StockDBException {
        if( id == null){
            throw new IllegalArgumentException("object id should not be null");
        }
        if(attr == null){
            throw new IllegalArgumentException("attribute name should not be null");
        }
        Map<String,String> attrValue = Commons.jsonMap(jc.hget(OBJECT_KEY, id));
        return attrValue.get(attr);
    }

    @Override
    public Map<String, String> getObjAttr(String id) throws StockDBException {
        if( id == null){
            throw new IllegalArgumentException("object id should not be null");
        }
        Map<String,String> attrValue = Commons.jsonMap(jc.hget(OBJECT_KEY, id));
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