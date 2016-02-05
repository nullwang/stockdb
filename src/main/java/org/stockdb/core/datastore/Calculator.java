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

import java.util.concurrent.*;

/**
 * 数据计算器，用于对扩展指标的计算
 */
public class Calculator {
    RedisDataStore redisDataStore;
    int number; //计算者个数
    ExecutorService executorService;

    protected Calculator(RedisDataStore redisDataStore, int number){
        this.redisDataStore = redisDataStore;
        this.number = number;
        assert(number > 0);
    }

    public void start()
    {
        executorService = Executors.newFixedThreadPool(number);
    }

    /**
     * 重新计算指定的指标
     * @param id 对象id
     * @param metricName 指标名称
     */
    public void calcMetric(String id, String metricName)
    {
        executorService.submit(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

}
