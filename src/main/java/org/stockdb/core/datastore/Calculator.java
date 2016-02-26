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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stockdb.core.event.MetricListener;
import org.stockdb.core.exception.StockDBException;
import org.stockdb.core.functions.*;

import java.util.List;
import java.util.concurrent.*;

/**
 * 数据计算器，用于对指标进行计算
 */
public class Calculator implements MetricListener{
    RedisDataStore redisDataStore;
    int number; //计算者个数
    ExecutorService executorService;

    private Logger logger = LoggerFactory.getLogger(Calculator.class);

    protected Calculator(RedisDataStore redisDataStore, int number){
        assert(number > 0);
        this.number = number;
        this.redisDataStore = redisDataStore;
    }

    public void start()
    {
        executorService = Executors.newFixedThreadPool(number);
        this.redisDataStore.addMetricListener(this);
    }

    public void stop() {
        this.redisDataStore.removeMetricListener(this);
        executorService.shutdown();
        try {
            executorService.awaitTermination(30,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            //log
        }
    }

    /**
     * 提交计算指定的指标的任务
     * @param id 对象id
     * @param metricName 指标名称
     */
    protected void submitCalcMetricTask(final String id, final String metricName,final DataPoint... dataPoints)
    {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    List<FunctionMetric> metricList = redisDataStore.getFunctionMetrics(metricName);
                    //calc every function metric
                    for(FunctionMetric functionMetric:metricList){
                        Function function = FunctionBuilder.build(functionMetric.getFunctionName());

                        //dayFirst, dayLast
                        if(function instanceof TimeFunction){
                            TimeFunction timeFunction = (TimeFunction) function;
                            TimeScope timeScope = timeFunction.getTimeScope(dataPoints);

                            String startTime = timeScope.getStartTime();
                            String endTime = timeScope.getEndTime();

                            //受影响的数据
                            List<DataPoint> effectedData = redisDataStore.getData(id,metricName,startTime,endTime);
                            DataPoint[] points = timeFunction.call(effectedData.toArray(new DataPoint[0]));
                            //保存计算结果
                            redisDataStore.putData(id,functionMetric.getName(),points);
                        }
                    }
                }catch (StockDBException e){
                    //log
                }
            }
        });
    }

    @Override
    public void dataPointChange(String id, String metric, DataPoint... dataPoints) {
        //提交依赖指标计算任务
        submitCalcMetricTask(id,metric,dataPoints);
    }
}
