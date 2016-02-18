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
        List<DataPoint> dataPointList = Arrays.asList(dataPoints);
        Collections.sort(dataPointList,  dataPointComparator);
        if( dataPointList.isEmpty()) return null;
        List<DataPoint> dataPointRetLst = new ArrayList<DataPoint>();
        DataPoint d = dataPointList.get(0);
        dataPointRetLst.add(d);
        for(DataPoint dataPoint: dataPointList){

        }

        //return dataPointList.get(0);
        return null;
    }
}
