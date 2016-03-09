package org.stockdb.util;
/*
 * @author nullwang@hotmail.com
 * created at 2015/3/16
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

import org.apache.commons.lang3.StringUtils;

public class Key {

    public static String makeRowKey(String id, String metricsName)
    {
        String escapeId = Escape.escape(id);
        String escapeMetricsName = Escape.escape(metricsName);

        return escapeId + ":" + escapeMetricsName;
    }

    public static String[] splitRowKey(String rowKey)
    {
        boolean slash = false;
        int commaIndex = 0;
        if( rowKey == null) return new String[0];
        for(int i=0; i<rowKey.length();i++){
            char ch = rowKey.charAt(i);
            if( !slash && ch == ':'){
                commaIndex = i;
                break;
            }
            if(ch == '\\'){
                slash = true;
            }else
                slash = false;
        }
        String[] rets = new String[2];
        rets[0] = StringUtils.substring(rowKey,0,commaIndex);
        rets[1] = StringUtils.substring(rowKey,commaIndex);

        return rets;
    }
}
