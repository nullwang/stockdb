package org.stockdb.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

public class EscapeTest {

    @Test
    public void testEscapeComma() {
        String input = "abcdefg";
        String output = Escape.escape(input);
        assertEquals(input,output);

        input = "abcd:efg";
        output = Escape.escape(input);
        assertEquals("abcd\\:efg", output);

        input = "a\\bcd:efg";
        output = Escape.escape(input);
        assertEquals("a\\\\bcd\\:efg",output);

        input = "a\\\\bcd:ef\\g";
        output = Escape.escape(input);
        assertEquals("a\\\\\\\\bcd\\:ef\\\\g", output);
    }

    @Test
   public void testUnEscape() {
       String input = "abcdefg";
       String output = Escape.unEscape(input);
       assertEquals(input,output);

       input = "abcd\\:efg";
       output = Escape.unEscape(input);
       assertEquals("abcd:efg", output);

       input = "a\\\\bcd\\:efg";
       output = Escape.unEscape(input);
       assertEquals("a\\bcd:efg",output);

       input = "a\\\\\\\\bcd\\:ef\\\\g";
       output = Escape.unEscape(input);
       assertEquals("a\\\\bcd:ef\\g", output);

    }
}
