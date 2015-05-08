package org.stockdb.core.http.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.stockdb.core.datastore.DataPoint;
import org.stockdb.core.datastore.DataStore;
import org.stockdb.core.datastore.Metric;
import org.stockdb.core.exception.StockDBException;

/*
 * @author nullwang@hotmail.com
 * created at 2015/3/13
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
@Controller
@RequestMapping(value="/api/v1")
public class MetricsController {

    @Autowired
    private DataStore dataStore;

    @RequestMapping(value = "/version", method = RequestMethod.GET )
    @ResponseBody
    public String version() throws StockDBException
    {
        return "{\"version\":\"1.0\"}";
    }

    @RequestMapping(value = "/data", method = RequestMethod.POST )
    public @ResponseBody
    void putDataPoints(@RequestBody DataPointImpl[] dataPoints) throws StockDBException {
         dataStore.putData(dataPoints);
    }

    @RequestMapping(value = "/metrics", method = RequestMethod.POST )
    public @ResponseBody
    void putDataPoints(@RequestBody Metric[] metrics) throws StockDBException
    {
        dataStore.putMetric(metrics);
    }

}
