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
import org.stockdb.core.util.DataPointUtil;

import java.util.*;

/**
 * 用来从一系列值中获取第一个日值,序列点的时间值
 */
public class DayFirstFunction extends TimeFunction {

    public static final String NAME = "DAY_FIRST";

    //将时间按照天进行分组，返回每组中第一个元素,返回的值不一定按照时间排序
    @Override
    public DataPoint[] call(DataPoint... dataPoints) {
        assert(dataPoints != null);
        List<DataPoint> pointsRet = new ArrayList<DataPoint>();
        Map<String,List<DataPoint>> result = DataPointUtil.groupByDay(dataPoints);
        for(List<DataPoint> points : result.values()){
            if( ! points.isEmpty()) {
                pointsRet.add(points.get(0));
            }
        }
        return pointsRet.toArray(new DataPoint[0]);
    }
}
