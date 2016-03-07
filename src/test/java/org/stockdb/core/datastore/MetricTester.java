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

import org.junit.Test;
import org.stockdb.core.functions.DayFirstFunction;
import org.stockdb.core.functions.DayLastFunction;
import org.stockdb.core.functions.DayMaxFunction;
import org.stockdb.core.functions.DayMinFunction;

import static org.junit.Assert.assertEquals;

public class MetricTester {

    @Test
    public void testMetric()
    {
        Metric metric = MockObjectBuilder.getMetric("price");
        FunctionMetric dayFirst = MockObjectBuilder.getFunctionMetric("dayFirstPrice", DayFirstFunction.NAME,metric);
        assertEquals(dayFirst.getFunctionName(), DayFirstFunction.NAME);
        assertEquals(dayFirst.getBaseMetrics(), new String[]{"price"});

        FunctionMetric dayMax = MockObjectBuilder.getFunctionMetric("dayMaxPrice", DayMaxFunction.NAME,metric);
        assertEquals(dayMax.getFunctionName(), DayMaxFunction.NAME);
        assertEquals(dayMax.getBaseMetrics(), new String[]{"price"});

        FunctionMetric dayMin = MockObjectBuilder.getFunctionMetric("dayMinPrice", DayMinFunction.NAME,metric);
        assertEquals(dayMin.getFunctionName(), DayMinFunction.NAME);
        assertEquals(dayMin.getBaseMetrics(), new String[]{"price"});

        FunctionMetric dayLast = MockObjectBuilder.getFunctionMetric("dayLastPrice", DayLastFunction.NAME,metric);
        assertEquals(dayLast.getFunctionName(), DayLastFunction.NAME);
        assertEquals(dayLast.getBaseMetrics(), new String[]{"price"});
    }
}