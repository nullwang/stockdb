package org.stockdb.core.function;
/*
 * @author nullwang@hotmail.com
 * created at 2016/3/3
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
import org.stockdb.core.datastore.DataPoint;
import org.stockdb.core.functions.DayMaxFunction;
import org.stockdb.core.functions.DayMinFunction;

import static junit.framework.Assert.assertTrue;

public class DayMinFunctionTester {

    @Test
    public void testGetData()
    {
        DayMinFunction dayMinFunction = new DayMinFunction();
        DataPoint[] dataPoints = new DataPoint[]{
                new DataPoint("201601011223","12"),
                new DataPoint("201601011314","1314"),
                new DataPoint("201601010114","1314123"),
                new DataPoint("201601020101","2"),
                new DataPoint("201601020301","2345"),
        };
        DataPoint [] ds = dayMinFunction.call(dataPoints);
        assertTrue(ds.length == 2);
        assertTrue(ds[0].equals(new DataPoint("201601011223","12"))
                && ds[1].equals(new DataPoint("201601020101","2"))
                || ds[1].equals(new DataPoint("201601011223","12"))
                   && ds[0].equals(new DataPoint("201601020101","2"))
        );
    }
}
