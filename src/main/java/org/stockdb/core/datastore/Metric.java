package org.stockdb.core.datastore;
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

public class Metric {
    String SAMPLE_INTERVAL = "sampleInterval";

    int MILLI_INTERVAL=0;
    int SECOND_INTERVAL=1;
    int MINUTE_INTERVAL=2;
    int HOUR_INTERVAL=3;
    int DAY_INTERVAL=4;
    int MONTH_INTERVAL=5;
    int YEAR_INTERVAL=6;
    int WEEK_INTERVAL=7;

    String name;
    String attr;
    String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
