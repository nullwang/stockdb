package org.stockdb.core.util;
/*
 * @author nullwang@hotmail.com
 * created at 2016/2/14
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

import org.apache.commons.lang.ObjectUtils;
import org.stockdb.core.datastore.DataPoint;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * comparator of data point, compare by timeStr
 */
public class DataPointComparator implements Comparator<DataPoint> {

    private int c = 0; // 0- compare by time,
                       // 1- compare by value;

    //default compare by timeStr
    public DataPointComparator()
    {
       this(0);
    }

    public DataPointComparator(int c)
    {
        this.c = c;
    }

    @Override
    public int compare(DataPoint o1, DataPoint o2) {
        if( o1 == null ) return o2 == null ? 0 : -1;
        else if( o2 == null) return 1;

        String v1;
        String v2;
        if( c == 0) {
            v1 = o1.getTimeStr();
            v2 = o2.getTimeStr();
            return v1.compareTo(v2);
        }else{
            v1 = o1.getValue();
            v2 = o2.getValue();
            BigDecimal bd1 = new BigDecimal(v1);
            BigDecimal bd2 = new BigDecimal(v2);
            return bd1.compareTo(bd2);
        }
    }
}