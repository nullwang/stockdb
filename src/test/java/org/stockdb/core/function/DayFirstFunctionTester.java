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
import org.stockdb.core.functions.DayFirstFunction;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class DayFirstFunctionTester {

    @Test
    public void testGetData()
    {
        DayFirstFunction dayFirstFunction = new DayFirstFunction();
        DataPoint[] dataPoints = new DataPoint[]{
                new DataPoint("201601011223","v12"),
                new DataPoint("201601011314","v1314"),
                new DataPoint("201601010114","v1314123"),
                new DataPoint("201601020101","v2"),
                new DataPoint("201601020301","v2345"),
        };
        DataPoint [] ds = dayFirstFunction.call(dataPoints);
        assertTrue(ds.length == 2);
        assertTrue(ds[0].equals(new DataPoint("201601010114","v1314123"))
                && ds[1].equals(new DataPoint("201601020101","v2"))
                ||
                        ds[1].equals(new DataPoint("201601010114","v1314123"))
                                && ds[0].equals(new DataPoint("201601020101","v2"))
        );
    }
}
