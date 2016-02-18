package org.stockdb.util;
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

import org.junit.Test;
import org.stockdb.core.util.TimeFormatUtil;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class TimeFormatUtilTest {

    @Test
    public void testFormat()
    {
        assertTrue(TimeFormatUtil.detectFormat("200") < 0);
        assertTrue(TimeFormatUtil.detectFormat("2006") == 0);
        assertTrue(TimeFormatUtil.detectFormat("200601") == 1);
        assertTrue(TimeFormatUtil.detectFormat("20060102") == 2);
        assertTrue(TimeFormatUtil.detectFormat("2006010203") == 3);
        assertTrue(TimeFormatUtil.detectFormat("200601020305") == 4);
        assertTrue(TimeFormatUtil.detectFormat("20060102030506") == 5);
        assertTrue(TimeFormatUtil.detectFormat("20060102030506078") == 6);

    }

    @Test
    public void testConvert()
    {
        assertEquals(TimeFormatUtil.convertFormat("20060102", TimeFormatUtil.YY), "2006");
        assertEquals(TimeFormatUtil.convertFormat("2006010203", TimeFormatUtil.YYMM), "200601");
        assertEquals(TimeFormatUtil.convertFormat("2006010203", TimeFormatUtil.YYMMDD), "20060102");
    }


}
