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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.stockdb.core.exception.DatastoreException;
import org.stockdb.core.exception.StockDBException;
import org.stockdb.util.Commons;
import org.stockdb.util.Key;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class RedisDataStore extends AbstractDataStore implements Scanable {

    final static int  redisDefaultPort = 6379;

    final static String METRICS_KEY="metrics";
    final static String OBJECT_KEY="objects";
    final static String META_KEY="meta";
    final static String META_SETTING_KEY="meta_set";

    JedisWrapper jc ;

    @Autowired
    public RedisDataStore(@Value("${stockdb.redis.hosts}") String hosts) throws StockDBException
    {
        LinkedList<HostAndPort> jedisClusterNodes = new LinkedList<HostAndPort>();
        String[] hostAndPorts = StringUtils.split(hosts,",");
        if( hostAndPorts.length == 0) throw new DatastoreException("stockdb.redis.hosts is empty");
        for(int i=0; i<hostAndPorts.length; i++) {
            String[] hostAndPort = StringUtils.split(hostAndPorts[i],":");
            int port = redisDefaultPort;
            if(hostAndPort.length == 0)
                throw new DatastoreException("stockdb.redis.host " + hostAndPort + " is illegal");
            if( hostAndPort.length == 2) {
                try {
                    port = Integer.parseInt(hostAndPort[1]);
                } catch (NumberFormatException e) {
                    throw new DatastoreException("stockdb.redis.host " + hostAndPort + " is illegal");
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
    public void putData(DataPoint[] dataPoints) throws StockDBException{
        for(DataPoint dataPoint:dataPoints) {
            String metricName = dataPoint.getMetricName();
            String index = getMetricAttr(metricName,Constants.METRIC_SAMPLE_INTERVAL);
            try{
                int i = Integer.parseInt(index);
                if( !DataPoint.timeStrPatterns[i].matcher(dataPoint.getTimeStr()).matches()){
                    throw new StockDBException("DataPoint["+dataPoint.getKey()+"]" + "has bad format timeStr["+ dataPoint.getTimeStr() +"]");
                }
            }catch (NumberFormatException e){
                throw new StockDBException("Metric[" + metricName + "]attribute["+ Constants.METRIC_SAMPLE_INTERVAL +"]value error");
            }
            jc.hset(dataPoint.getKey(),dataPoint.getTimeStr(),dataPoint.getValue());
        }
    }

    @Override
    public void setMetricAttr(String name, String attr, String value) throws StockDBException{
        if( name == null){
            throw new StockDBException("metric name should not be null");
        }
        if(attr == null){
            throw new StockDBException("attribute name should not be null");
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
    public Set<String> getMetrics() {
        return jc.hkeys(METRICS_KEY);
    }

    @Override
    public DataPoint getData(String id, String metricName, String timeStr, int diff, TimeUnit timeUnit) {
        return null;
    }

    @Override
    public List<DataPoint> getData(String id, String metricName, String startTime, String endTime) {
        return null;
    }

    @Override
    public Collection<DataPoint> queryData(String id, String metricName, String startTime, String endTime) {
        return ScanableAdapter.build(this,id,metricName,startTime,endTime);
    }


    @Override
    public ScanResult scan(String id, String metricName, String cursor) {
        return  jc.hscan(Key.makeRowKey(id,metricName),"0");
    }

    @ExceptionHandler(JedisException.class)
    private void handlerJedisException(JedisException e) throws StockDBException{
        throw new StockDBException(e);
    }
}
