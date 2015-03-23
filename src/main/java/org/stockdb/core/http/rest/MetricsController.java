package org.stockdb.core.http.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.stockdb.core.datastore.DataPoint;

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

    @RequestMapping(value = "/version", method = RequestMethod.GET )
    public void version()
    {



    }

    @RequestMapping(value = "/datapoints", method = RequestMethod.POST )
    public @ResponseBody
    void putDataPoints(@RequestBody DataPoint[] dataPoints)
    {




    }






}
