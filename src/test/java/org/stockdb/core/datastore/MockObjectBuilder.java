package org.stockdb.core.datastore;
/*
 * @author nullwang@hotmail.com
 * created at 2016/3/5
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

import org.stockdb.core.datastore.impl.MetricBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class MockObjectBuilder {

    public static Metric getMetric(String metricName){
        return MetricBuilder.build(metricName, Collections.emptyMap());
    }

    public static FunctionMetric getFunctionMetric(String metricName,String functionName,
                                            Metric... metrics){
        Map m = new HashMap();
        m.put(FunctionMetric.FUNCTION_NAME,functionName);
        StringBuilder sb = new StringBuilder();
        for(Metric metric:metrics){
            if(sb.length()> 0) sb.append(FunctionMetric.METRIC_SEPARATOR);
            sb.append(metric.getName());
        }
        m.put(FunctionMetric.BASE_METRICS,sb.toString());

        return (FunctionMetric)MetricBuilder.build(metricName,m);
    }
}
