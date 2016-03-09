package org.stockdb.util;
/*
 * @author nullwang@hotmail.com
 * created at 2016/3/9
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
import java.util.ArrayList;
import static junit.framework.Assert.assertTrue;

public class CommonsTester {

    @Test
    public void testEmpty() {
        int a[] = {};

        assertTrue(Commons.isEmpty(null));
        assertTrue(Commons.isEmpty(""));
        assertTrue(Commons.isEmpty(a));
        assertTrue(Commons.isEmpty(new Object[0]));
        assertTrue(Commons.isEmpty(new String[0]));
        assertTrue(Commons.isEmpty(new ArrayList()));
    }
}
