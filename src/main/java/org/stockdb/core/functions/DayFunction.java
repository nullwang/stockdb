package org.stockdb.core.functions;
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

import org.stockdb.core.datastore.DataPoint;
import org.stockdb.core.datastore.DataStore;
import org.stockdb.core.util.TimeFormatUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class DayFunction extends TimeFunction{

    /**
     * 对 id ,metricName 指定时间范围内进行函数计算
     * @param dataStore
     * @param id
     * @param metricName
     * @param timeScope
     * @return
     */
    public abstract DataPoint invoke(DataStore dataStore, String id, String metricName, TimeScope timeScope);

    //获取函数影响的时间访问-基于天
    public TimeScope getTimeScope(DataPoint... dataPoints) {
        assert (dataPoints != null);
        List<DataPoint> dataPointList = Arrays.asList(dataPoints);
        Collections.sort(dataPointList, timeComparator);

        if (dataPointList.isEmpty()) return null;
        DataPoint dp = dataPointList.get(0);
        DataPoint dpEnd = dataPointList.get(dataPointList.size() - 1);
        String startTime = TimeFormatUtil.min(dp.getTimeStr(),TimeFormatUtil.YYMMDD);
        String endTime = TimeFormatUtil.max(dpEnd.getTimeStr(),TimeFormatUtil.YYMMDD);
        return TimeScope.build(startTime,endTime);
    }


}
