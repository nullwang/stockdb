package org.stockdb.core.functions;
/*
 * @author nullwang@hotmail.com
 * created at 2016/2/15
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
import org.stockdb.core.util.DataPointComparator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 时间相关函数
 */
public abstract class TimeFunction implements Function {

    DataPointComparator dataPointComparator = new DataPointComparator();

    //获取函数影响的时间访问
    public TimeScope getTimeScope(DataPoint... dataPoints) {
        assert (dataPoints != null);
        List<DataPoint> dataPointList = Arrays.asList(dataPoints);
        Collections.sort(dataPointList, dataPointComparator);

        if (dataPointList.isEmpty()) return null;
        DataPoint dp = dataPointList.get(0);
        DataPoint dpEnd = dataPointList.get(dataPointList.size() - 1);
        return new TimeScope(dp.getTimeStr(), dpEnd.getTimeStr());
    }
}
