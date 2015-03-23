package org.stockdb.util;
/*
 * @author nullwang@hotmail.com
 * created at 2015/3/16
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

import java.io.StringWriter;

public class Escape {


    /**
     * Escape ':' to '\:' , '\' to '\\'
     *
     * @param value
     * @return
     */
    public static String escape(String value) {
        if (value == null) {
            return null;
        }
        StringWriter writer = new StringWriter(value.length() * 2);
        int sz;
        sz = value.length();
        for (int i = 0; i < sz; i++) {
            char ch = value.charAt(i);
            switch (ch) {
                case '\\':
                    writer.write('\\');
                    writer.write('\\');
                    break;
                case ':':
                    writer.write('\\');
                    writer.write(':');
                    break;
                default:
                    writer.write(ch);
            }

        }
        return writer.toString();
    }


    public static String unEscape(String value) {
        if (value == null) {
            return null;
        }
        boolean inSlash = false;
        StringWriter writer = new StringWriter(value.length());
        int sz;
        sz = value.length();
        for (int i = 0; i < sz; i++) {
            char ch = value.charAt(i);
            if(inSlash){
                inSlash = false;
                switch (ch) {
                    case '\\':
                        writer.write('\\');
                        break;
                    case ':':
                        writer.write(':');
                        break;
                }
                continue;
            }else if( ch == '\\'){
                inSlash = true;
                continue;
            }
            writer.write(ch);
        }
        return writer.toString();
    }
}
