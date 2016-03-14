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

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.stockdb.core.datastore.impl.FunctionMetricImpl;
import org.stockdb.core.functions.DayFirstFunction;
import org.stockdb.core.functions.DayLastFunction;
import org.stockdb.core.functions.DayMaxFunction;
import org.stockdb.core.functions.DayMinFunction;

import java.util.List;
import java.util.concurrent.TimeoutException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class CalculatorTester {

    private static RedisDataStore redisDataStore;

    private static Env env = new EnvMock();

    private MetricListenerMock metricListener = new MetricListenerMock();

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
        redisDataStore.addMetricListener(metricListener);

        FunctionMetric functionMetric = new FunctionMetricImpl("dayOpenPrice", DayFirstFunction.NAME,"price");
        redisDataStore.putMetric(functionMetric);

        functionMetric = new FunctionMetricImpl("dayClosePrice", DayLastFunction.NAME,"price");
        redisDataStore.putMetric(functionMetric);

        functionMetric = new FunctionMetricImpl("dayHighPrice", DayMaxFunction.NAME,"price");
        redisDataStore.putMetric(functionMetric);

        functionMetric = new FunctionMetricImpl("dayLowPrice", DayMinFunction.NAME,"price");
        redisDataStore.putMetric(functionMetric);

        DataPoint dataPoint= new DataPoint();
        dataPoint.setTimeStr("200101011201");
        dataPoint.setValue("13.8");
        redisDataStore.putData("000001", "price", dataPoint);

        dataPoint= new DataPoint();
        dataPoint.setTimeStr("200101010602");
        dataPoint.setValue("13.9");
        redisDataStore.putData("000001", "price", dataPoint);

        dataPoint= new DataPoint();
        dataPoint.setTimeStr("200101010405");
        dataPoint.setValue("1.92");
        redisDataStore.putData("000001", "price", dataPoint);

        dataPoint= new DataPoint();
        dataPoint.setTimeStr("200101011105");
        dataPoint.setValue("3.87");
        redisDataStore.putData("000001", "price", dataPoint);

        dataPoint= new DataPoint();
        dataPoint.setTimeStr("200101012305");
        dataPoint.setValue("13.654");
        redisDataStore.putData("000001", "price", dataPoint);

        //wait calc to finish
        try {
            Calculator calculator = redisDataStore.getCalculator();
            calculator.waitCalcFinish(1000*3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        List<DataPoint> points = redisDataStore.getData("000001","dayOpenPrice",null,null);
        assertTrue(points.size() == 1);
        assertEquals(points.get(0), new DataPoint("200101010405", "1.92"));

        points = redisDataStore.getData("000001","dayClosePrice",null,null);
        assertTrue(points.size() == 1);
        assertEquals(points.get(0), new DataPoint("200101012305", "13.654"));

        points = redisDataStore.getData("000001","dayHighPrice",null,null);
        assertTrue(points.size() == 1);
        assertEquals(points.get(0), new DataPoint("200101010602", "13.9"));

        points = redisDataStore.getData("000001","dayLowPrice",null,null);
        assertTrue(points.size() == 1);
        assertEquals(points.get(0), new DataPoint("200101010405", "1.92"));

        redisDataStore.removeMetricListener(metricListener);
    }
}