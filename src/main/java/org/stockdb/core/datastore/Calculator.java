package org.stockdb.core.datastore;
/*
 * @author nullwang@hotmail.com
 * created at 2016/2/5
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

import org.apache.commons.collections.iterators.ArrayListIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stockdb.core.event.MetricListener;
import org.stockdb.core.exception.StockDBException;
import org.stockdb.core.functions.*;
import org.stockdb.core.util.DataPointUtil;

import java.util.*;
import java.util.concurrent.*;

/**
 * 数据计算器，用于对指标进行计算
 */
public class Calculator implements MetricListener{
    RedisDataStore redisDataStore;
    int number; //计算者个数
    ExecutorService executorService;

    private Logger logger = LoggerFactory.getLogger(Calculator.class);

    private List<Future> calcFuture = new ArrayList<Future>();

    protected Calculator(RedisDataStore redisDataStore, int number){
        assert(number > 0);
        this.number = number;
        this.redisDataStore = redisDataStore;
    }

    public void start()
    {
        executorService = Executors.newFixedThreadPool(number);
        this.redisDataStore.addMetricListener(this);
        //check every 3 minutes
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                clean();
            }
        },500,1000*60*180); //every 3minutes

        logger.info(" service [Calculator] started ");
    }

    private synchronized void clean(){
        Iterator<Future> iterator = calcFuture.iterator();
        while (iterator.hasNext()){
            Future f = iterator.next();
            if( f.isCancelled() || f.isDone()){
                iterator.remove();
            }
        }
    }

    public void stop() {
        this.redisDataStore.removeMetricListener(this);
        executorService.shutdown();
        try {
            executorService.awaitTermination(3,TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            //log
        }
    }

    //ms
    public void waitCalcFinish(long ms) throws TimeoutException,InterruptedException{
        long remainMs = ms;
        ArrayList<Future> lst = new ArrayList(calcFuture);
        for(Future f:lst){
            try {
                long currentTimeMillis = System.currentTimeMillis();
                f.get(remainMs,TimeUnit.MILLISECONDS);
                remainMs-= System.currentTimeMillis() - currentTimeMillis;
            } catch (ExecutionException e) {
                //ignore
            }
        }
    }

    /**
     * 提交计算指定的指标的任务
     * @param id 对象id
     * @param metricName 指标名称
     */
    protected synchronized void submitCalcMetricTask(final String id, final String metricName,final DataPoint... dataPoints)
    {
        calcFuture.add(executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    List<FunctionMetric> metricList = redisDataStore.getFunctionMetrics(metricName);
                    //calc every function metric
                    for (FunctionMetric functionMetric : metricList) {
                        Function function = FunctionBuilder.build(functionMetric.getFunctionName());
                        //day series function
                        if (function instanceof DayFunction) {
                            DayFunction dayFunction = (DayFunction) function;
                            Map<String, List<DataPoint>> groupData = DataPointUtil.groupByDay(dataPoints);
                            //calc every group
                            for (String day : groupData.keySet()) {
                                TimeScope ts = TimeScope.buildByDay(day);
                                DataPoint value = dayFunction.invoke(redisDataStore, id, metricName, ts);
                                if (value != null) {
                                    //update functionMetric value,remove old data
                                    redisDataStore.removeData(id, functionMetric.getName(), ts.getStartTime(), ts.getEndTime());
                                    redisDataStore.putData(id, functionMetric.getName(), value);
                                }
                            }
                        }
                    }
                } catch (StockDBException e) {
                    //log
                }
            }
        }));
    }

    @Override
    public void dataPointChange(String id, String metric, DataPoint... dataPoints) {
        assert(dataPoints != null);
        assert(dataPoints.length != 0);

        //提交依赖指标计算任务
        submitCalcMetricTask(id,metric,dataPoints);
    }

    @Override
    public void dataPointRemove(String id, String metric, DataPoint... dataPoints) {
        dataPointChange(id,metric,dataPoints);
    }
}
