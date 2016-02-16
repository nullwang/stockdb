package org.stockdb.core.datastore.impl;
/*
 * @author nullwang@hotmail.com
 * created at 2015/3/26
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang.StringUtils;
import org.stockdb.core.datastore.FunctionMetric;

public class FunctionMetricImpl extends MetricImpl implements FunctionMetric {

    private static final String FUNCTION_NAME="functionName";
    private static final String BASE_METRICS="baseMetrics";
    private static final String METRIC_SEPARATOR=",";

    @JsonIgnore
    public String getFunctionName() {
        return getAttrValue(FUNCTION_NAME);
    }

    public void setFunctionName(String functionName) {
        setAttrValue(FUNCTION_NAME,functionName);
    }

    public String[] getBaseMetrics() {
        return StringUtils.split(getAttrValue(BASE_METRICS),METRIC_SEPARATOR);
    }

    public void setBaseMetrics(String[] baseMetrics) {
        String v = StringUtils.join(baseMetrics,METRIC_SEPARATOR);
        setAttrValue(BASE_METRICS,v);
    }
}
