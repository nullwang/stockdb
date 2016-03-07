package org.stockdb.core.datastore;
/*
 * @author nullwang@hotmail.com
 * created at 2016/3/7
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
import org.stockdb.core.datastore.impl.FunctionMetricImpl;
import org.stockdb.core.event.MetricListener;
import org.stockdb.core.functions.DayFirstFunction;

import java.util.Collection;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class CalculatorTester {

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
    public void testCalc()
    {
        FunctionMetric functionMetric = new FunctionMetricImpl("dayOpeningPrice", DayFirstFunction.NAME,"price");
        redisDataStore.putMetric(functionMetric);

        DataPoint dataPoint= new DataPoint();
        dataPoint.setTimeStr("200101011201");
        dataPoint.setValue("13.8");
        redisDataStore.putData("000001", "price", dataPoint);

        dataPoint= new DataPoint();
        dataPoint.setTimeStr("200101011202");
        dataPoint.setValue("13.7");
        redisDataStore.putData("000001", "price", dataPoint);

        dataPoint= new DataPoint();
        dataPoint.setTimeStr("200101010405");
        dataPoint.setValue("1.92");
        redisDataStore.putData("000001", "price", dataPoint);
        try {
            Thread.sleep(1000);
            //wait calc finished
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        redisDataStore.addMetricListener(new MetricListener() {
            @Override
            public void dataPointChange(String id, String metric, DataPoint... dataPoints) {
                if( id.equals("000001") && "dayOpeningPrice".equals(metric)){
                    List<DataPoint> points = redisDataStore.getData("000001","dayOpeningPrice",null,null);
                    assertTrue(points.size() == 1);
                    assertEquals(points.get(0), new DataPoint("200101011201", "13.8"));
                }
            }
        });

        redisDataStore.stop();
    }

}
