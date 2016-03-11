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
import java.util.Objects;

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
        assertTrue(dataPoints.size() == 1);

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
    public void testGetAndRemoveMeta()
    {
        redisDataStore.addObjectMeta("0001","price");
        redisDataStore.addObjectMeta("0001","dayHighestPrice");

        Map map = redisDataStore.getObjectMeta("0001");
        assertTrue(map.get(redisDataStore.META_ID_KEY).equals("0001"));
        assertTrue(map.get(redisDataStore.META_METRIC_NAMES_KEY).equals("price,dayHighestPrice"));

        redisDataStore.removeObjectMeta("0001","dayHighestPrice");
        map = redisDataStore.getObjectMeta("0001");
        assertTrue(map.get(redisDataStore.META_METRIC_NAMES_KEY).equals("price"));
        assertTrue(map.get(redisDataStore.META_ID_KEY).equals("0001"));

        redisDataStore.removeObjectMeta("0001","price");
        map = redisDataStore.getObjectMeta("0001");
        assertTrue(map.isEmpty());
    }

    @Test
    public void testGetAndRemoveMetaByChangeData()
    {
        redisDataStore.putData("0001","metric1",new DataPoint("20010708","xsv") );
        Map map = redisDataStore.getObjectMeta("0001");
        Assert.assertEquals(map.get("id"),"0001");
        Assert.assertEquals(map.get("metricNames"),"metric1");

        redisDataStore.putData("0001","metric2",new DataPoint("20010708","xsv") );
        map = redisDataStore.getObjectMeta("0001");
        Assert.assertEquals(map.get("id"),"0001");
        Assert.assertEquals(map.get("metricNames"),"metric1,metric2");

        redisDataStore.putData("0001","metric1",new DataPoint("20010708","xsv") );
        map = redisDataStore.getObjectMeta("0001");
        Assert.assertEquals(map.get("id"),"0001");
        Assert.assertEquals(map.get("metricNames"),"metric1,metric2");

        //remove data, no data will be removed
        redisDataStore.removeData("0001","metric1",new DataPoint("2001070809","xsv"));
        map = redisDataStore.getObjectMeta("0001");
        Assert.assertEquals(map.get("id"),"0001");
        Assert.assertEquals(map.get("metricNames"),"metric1,metric2");

        redisDataStore.removeData("0001","metric1",new DataPoint("20010708","xsv"));
        map = redisDataStore.getObjectMeta("0001");
        Assert.assertEquals(map.get("id"),"0001");
        Assert.assertEquals(map.get("metricNames"),"metric2");

        redisDataStore.removeData("0001","metric2",new DataPoint("20010708","xsv"));
        map = redisDataStore.getObjectMeta("0001");
        assertTrue(map.isEmpty());
    }

    @Test
    public void testRemoveData(){
        redisDataStore.putData("0001","metric1",new DataPoint("2001070808","xsv") );
        redisDataStore.putData("0001","metric1",new DataPoint("2001070809","xsv1") );

        List<DataPoint> points = redisDataStore.getData("0001","metric1","2001070808","2001070809");
        Assert.assertTrue(Objects.equals(points.get(0),new DataPoint("2001070808","xsv")));
        Assert.assertTrue(Objects.equals(points.get(1),new DataPoint("2001070809","xsv1")));

        redisDataStore.removeData("0001","metric1",new DataPoint("2001070808","xsv"));
        points = redisDataStore.getData("0001","metric1","2001070808","2001070809");
        Assert.assertTrue(Objects.equals(points.get(0),new DataPoint("2001070809","xsv1")));

        redisDataStore.removeData("0001","metric1",new DataPoint("2001070809","xsv1"));
        points = redisDataStore.getData("0001","metric1","2001070808","2001070809");
        assertTrue(points.isEmpty());
    }

    @Test
    public void testClearData()
    {
        redisDataStore.putData("0001","metric1",new DataPoint("20010708","xsv") );
        Map map = redisDataStore.getObjectMeta("0001");
        Assert.assertEquals(map.get("id"),"0001");

        redisDataStore.clearData();
        map = redisDataStore.getObjectMeta("0001");
        assertTrue(map.isEmpty());

        redisDataStore.putData("0001","metric1",new DataPoint("20010708","xsv") );
        map = redisDataStore.getObjectMeta("0001");
        Assert.assertEquals(map.get("id"),"0001");

        redisDataStore.removeData("0001", "metric1");
        map = redisDataStore.getObjectMeta("0001");
        assertTrue(map.isEmpty());
    }
}
