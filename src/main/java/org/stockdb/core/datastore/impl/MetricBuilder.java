package org.stockdb.core.datastore.impl;
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

import org.stockdb.core.datastore.Metric;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class MetricBuilder {

    public static Metric build(String name, Map attrMap) {
        if( attrMap == null || attrMap == Collections.emptyMap()){
            attrMap = new HashMap();
        }
        if( attrMap.containsKey(FunctionMetricImpl.FUNCTION_NAME)){
            return new FunctionMetricImpl(name,attrMap);
        }
        return new MetricImpl(name,attrMap);
    }
}
