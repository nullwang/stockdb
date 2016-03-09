package org.stockdb.core.functions;
/*
 * @author nullwang@hotmail.com
 * created at 2016/2/6
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
import org.stockdb.core.util.DataPointUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 将数据通过天进行分组，取每一天的最小值
 */
public class DayMinFunction extends DayFunction {

    public static final String NAME = "DAY_MIN";

    //将时间按照天进行分组，返回每组中最小值元素
    @Override
    public DataPoint[] call(DataPoint... dataPoints) {
        assert(dataPoints != null);
        List<DataPoint> pointsRet = new ArrayList<DataPoint>();
        Map<String,List<DataPoint>> result = DataPointUtil.groupByDay(dataPoints);
        for(List<DataPoint> points : result.values()){
            List<DataPoint> vs = new ArrayList(points);
            Collections.sort(vs,valueComparator);
            if( ! vs.isEmpty()) {
                pointsRet.add(vs.get(0));
            }
        }
        return pointsRet.toArray(new DataPoint[0]);
    }

    @Override
    public void invoke(DataStore dataStore) {

    }
}
