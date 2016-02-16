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

import com.google.gson.Gson;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class Commons {

    private static int NULL_IS_BIGGER = 1;
    private static int NULL_IS_SMALL = -1;

    /**
     * compareStr(null,null) == 0
     * compareStr("anyStr",null) > 0
     * compareStr("a","b") < 0
     * @param str1
     * @param str2
     * @return
     */
    public static int compareStrA(String str1, String str2) {
        if (str1 == str2) return 0;
        if (str1 == null) return NULL_IS_SMALL ;
        if( str2 == null) return 1;

        return str1.compareTo(str2);
    }

    /**
     * null > anyStr
     * @param str1
     * @param str2
     * @return
     */
    public static int compareStrB(String str1, String str2) {
        if (str1 == str2) return 0;
        if (str1 == null) return NULL_IS_BIGGER ;
        if( str2 == null) return -1;

        return str1.compareTo(str2);
    }

    /**
     * str between [str1,str2]
     *  between(null,null,"anyStr") return true
     *  between(null,"b,"anyStr") return true
     *  between(null,"b","c") return false
     *  between("a",null,"c") return true
     *  between("c",null,"c") return true
     *  between("c",null,"a") return false
     * @param str1 more or equal
     * @param str2 less or equal
     * @param str 被比较的字符串
     * @return
     */
    public static boolean between(String str1, String str2, String str)
    {
        return compareStrA(str,str1)>=0 && compareStrB(str,str2)<=0;
    }

    public static Map<String,String> jsonMap(String json)
    {
        if (StringUtils.isEmpty(json)) return new HashMap<String, String>();
        Gson gson = new Gson();
        return gson.fromJson(json,Map.class);
    }

    public static String jsonPut(String json, String key, String value)
    {
        Map map = jsonMap(json);
        map.put(key,value);
        return mapJson(map);
    }

    public static String jsonRemove(String json, String key)
    {
        Map map = jsonMap(json);
        map.remove(key);
        return mapJson(map);
    }

    public static String mapJson(Map<String,String> map){
        Gson gson = new Gson();
        assert(map != null);
        return gson.toJson(map);
    }

    public static String mapJson(String key, String value){
        Map map = new HashMap();
        map.put(key,value);
        return mapJson(map);
    }

    public static String toJson(Object object)
    {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> tClass){
        Gson gson = new Gson();
        return gson.fromJson(json,tClass);
    }

    static public boolean contains(Object[] container, Object element){
        if( container == null) return element == null;
        for(Object object: container){
            if(ObjectUtils.equals(object,element))
                return true;
        }
        return false;
    }
}
