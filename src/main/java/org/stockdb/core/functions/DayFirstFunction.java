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
import org.stockdb.core.util.TimeFormatUtil;

import java.util.*;

/**
 * 将数据通过天进行分组，取每一天的第一个值
 */
public class DayFirstFunction extends DayFunction {

    public static final String NAME = "DAY_FIRST";

    //将时间按照天进行分组，返回每组中最小元素,返回的值不一定按照时间排序
    @Override
    public DataPoint[] call(DataPoint... dataPoints) {
        assert(dataPoints != null);
        List<DataPoint> pointsRet = new ArrayList<DataPoint>();
        Map<String,List<DataPoint>> result = DataPointUtil.groupByDay(dataPoints);
        for(List<DataPoint> points : result.values()){
            List<DataPoint> vs = new ArrayList(points);
            Collections.sort(vs,timeComparator);
            if( ! vs.isEmpty()) {
                pointsRet.add(vs.get(0));
            }
        }
        return pointsRet.toArray(new DataPoint[0]);
    }

    @Override
    public void invoke(DataStore dataStore,Map parameters) {
//        assert(parameters != null);
//
//        Map<String,List<DataPoint>> result = DataPointUtil.groupByDay(dataPoints);
//        //分别取每组的最大最小时间
//        for(List<DataPoint> points : result.values()){
//            List<DataPoint> vs = new ArrayList(points);
//            Collections.sort(vs,timeComparator);
//            DataPoint fp = vs.get(0); //第一个点
//            DataPoint lp = vs.get(vs.size()-1);//最后一个点
//            //获取每组点最小及最大时间范围
//            String minStr = TimeFormatUtil.min(fp.getTimeStr(),TimeFormatUtil.YYMMDD);
//            String maxStr = TimeFormatUtil.max(lp.getTimeStr(),TimeFormatUtil.YYMMDD);
//
//
//
//
//
//
//        }

    }

    @Override
    public DataPoint invoke(DataStore dataStore,String id, String metricName, TimeScope timeScope) {
        assert(timeScope!=null);
        if( !timeScope.isInSameDay()){
            throw new IllegalArgumentException(" Not in same day " + timeScope.toString());
        }
        String str = timeScope.getStartTime();
        String maxDay = TimeFormatUtil.max(str,TimeFormatUtil.YYMMDD);
        String minDay = TimeFormatUtil.min(str,TimeFormatUtil.YYMMDD);

        List<DataPoint> points = dataStore.getData(id,metricName,minDay,maxDay);
        if(points.isEmpty()) return null;

        Collections.sort(points,timeComparator);
        return points.get(0);
    }
}
