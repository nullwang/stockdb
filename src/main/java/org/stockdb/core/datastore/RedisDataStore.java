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

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.stockdb.core.exception.DatastoreException;
import org.stockdb.core.exception.StockDBException;
import org.stockdb.util.Key;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ScanResult;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RedisDataStore implements DataStore,Scanable {

    final static int  redisDefaultPort = 7379;

    JedisCluster jc ;

    @Autowired
    public RedisDataStore(@Value("stockdb.redis.hosts") final String hosts) throws DatastoreException
    {
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
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

        jc = new JedisCluster(jedisClusterNodes);
    }

    @Override
    public void putData(DataPoint[] dataPoints) throws StockDBException{
        for(DataPoint dataPoint:dataPoints) {
            String metricName = dataPoint.getMetricName();
            String index = getMetricAttr(metricName,Metric.SAMPLE_INTERVAL);
            try{
                int i = Integer.parseInt(index);
                if( !DataPoint.timeStrPatterns[i].matcher(dataPoint.getTimeStr()).matches()){
                    throw new StockDBException("DataPoint["+dataPoint.getKey()+"]" + "has bad format timeStr["+ dataPoint.getTimeStr() +"]");
                }
            }catch (NumberFormatException e){
                throw new StockDBException("Metric[" + metricName + "]attribute["+ Metric.SAMPLE_INTERVAL +"]value error");
            }
            jc.hset(dataPoint.getKey(),dataPoint.getTimeStr(),dataPoint.getObjValue());
        }
    }

    @Override
    public void setMetric(String name, String attr, String value) throws StockDBException{
        if( name == null){
            throw new StockDBException("metric name should not be null");
        }
        if(attr == null){
            throw new StockDBException("attribute name should not be null");
        }

        jc.hset(name,attr,value);
    }

    @Override
    public String getMetricAttr(String name, String attr) {
        if( name == null){
            throw new IllegalArgumentException("metric name should not be null");
        }
        if(attr == null){
            throw new IllegalArgumentException("attribute name should not be null");
        }
       return jc.hget(name,attr);
    }

    @Override
    public Collection<DataPoint> getDataPoints(String id, String metricName, String startTime, String endTime) {
        return ScanableAdapter.build(this,id,metricName,startTime,endTime);
    }


    @Override
    public ScanResult scan(String id, String metricName, String cursor) {
        return  jc.hscan(Key.makeRowKey(id,metricName),"0");
    }
}
