package org.stockdb.core.datastore;
/*
 * @author nullwang@hotmail.com
 * created at 2016/2/26
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

import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.stockdb.core.datastore.impl.MetricImpl;

import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNull;

public class RedisDataStoreTester {

    private static RedisDataStore redisDataStore;

    private static Env env = new EnvMock();

    @BeforeClass
    public static void startup() {
        redisDataStore=new RedisDataStore();
        redisDataStore.start(env);
    }

    @AfterClass
    public static void stop(){
        redisDataStore.stop();
    }

    @Before
    public void clean()
    {
        redisDataStore.clearMetrics();
        redisDataStore.clearData();
    }

    @Test
    public void testPutMetric()
    {
        Metric metric = new MetricImpl();
        metric.setName("test1");
        metric.setAttrValue("attr","value");
        redisDataStore.putMetric(metric);
        Metric m = redisDataStore.getMetric("test1");

        assertEquals(m,metric);
    }

    @Test
    public void testPutData()
    {
        DataPoint dataPoint= new DataPoint();
        dataPoint.setTimeStr("20010101");
        dataPoint.setValue("v1");

        redisDataStore.putData("0001", "dayHighest", dataPoint);
        List<DataPoint> dataPoints =  redisDataStore.getData("0001", "dayHighest", null, null);
        assertFalse(dataPoints.isEmpty());

        Assert.assertEquals(dataPoints.get(0),dataPoint);
    }

    @Test
    public void testSetMetricAttr()
    {
        redisDataStore.setMetricAttr("metric","attr","v");
        Assert.assertEquals("v", redisDataStore.getMetricAttr("metric", "attr"));
        assertNull(redisDataStore.getMetricAttr("metric", "attr1"));
    }

    @Test
    public void testSetObjectAttr()
    {
        redisDataStore.setObjAttr("0001", "attr", "v");
        Assert.assertEquals("v", redisDataStore.getObjAttr("0001", "attr"));
        assertNull(redisDataStore.getMetricAttr("0001", "attr1"));
    }

    @Test
    public void testGetMeta()
    {
        redisDataStore.putData("0001","attr1",new DataPoint("20010708","xsv") );
        Map map = redisDataStore.getObjectMeta("0001");
        Assert.assertEquals(map.get("id"),"0001");

        Assert.assertEquals(map.get("metricNames"),"attr1");
    }


}
