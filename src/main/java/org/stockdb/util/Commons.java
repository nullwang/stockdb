package org.stockdb.util;
/*
 * @author nullwang@hotmail.com
 * created at 2015/3/27
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

public class Commons {

    /**
     * compareStr(null,null) == 0
     * compareStr("anything",null) > 0
     * compareStr(null,"anything") < 0
     * @param str1
     * @param str2
     * @return
     */
    public static int compareStr(String str1, String str2) {
        if (str1 == str2) return 0;
        if (str1 == null) return -1;
        if (str2 == null) return 1;
        return str1.compareTo(str2);
    }
}
