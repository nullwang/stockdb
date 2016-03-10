package org.stockdb.core.datastore;
/*
 * @author nullwang@hotmail.com
 * created at 2016/3/8
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

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.stockdb.core.event.MetricListener;

import java.util.HashMap;
import java.util.Map;

public class MetricListenerMock implements MetricListener {

    Map<Pair<String,String>,DataPoint[]> map = new HashMap();

    public boolean has(String id, String metric){
        return map.containsKey(new MutablePair<String, String>(id,metric));
    }

    public DataPoint[] get(String id, String metric){
        return map.get(new MutablePair<String, String>(id,metric));
    }

    @Override
    public void dataPointChange(String id, String metric, DataPoint... dataPoints) {
           map.put(new MutablePair(id,metric),dataPoints );
    }

    @Override
    public void dataPointRemove(String id, String metric, DataPoint... dataPoints) {
        //map.remove()
    }
}
