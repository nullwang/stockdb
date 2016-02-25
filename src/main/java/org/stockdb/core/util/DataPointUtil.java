package org.stockdb.core.util;
/*
 * @author nullwang@hotmail.com
 * created at 2016/2/18
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataPointUtil {

    public static DataPointComparator buildComparator(boolean byTime) {
        return new DataPointComparator(byTime ? 0 : 1);
    }

    /**
     * 根据日进行分组
     * @param points 数据集
     * @return 分组后的数据
     */
    public static Map<String,List<DataPoint>> groupByDay(DataPoint[] points) {
        Map<String,List<DataPoint>> map = group(points,TimeFormatUtil.YYMMDD);
        return map;
    }

    /**
     *
     * @param points 数据集
     * @param format the format of time, pls
     *  {@link TimeFormatUtil }
     * @return 分组后的数据集
     */
    public static Map<String,List<DataPoint>> group(DataPoint[] points,int format){
        assert(points != null);
        Map<String,List<DataPoint>> map =new HashMap();
        for(DataPoint dataPoint: points){
            String str = TimeFormatUtil.convertFormat(dataPoint.getTimeStr(), format);
            List<DataPoint> lst = map.get(str);
            if( lst == null){
                lst = new ArrayList<DataPoint>();
            }
            lst.add(dataPoint);
            map.put(str,lst);
        }
        return map;
    }
}
