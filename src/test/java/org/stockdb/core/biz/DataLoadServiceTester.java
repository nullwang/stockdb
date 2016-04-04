package org.stockdb.core.biz;
/*
 * @author nullwang@hotmail.com
 * created at 2016/3/23
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

import org.junit.Before;
import org.junit.Test;
import org.stockdb.core.datastore.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class DataLoadServiceTester {

    DataLoadService dataLoadService = new DataLoadService();
    DataStore dataStore = new DataStoreMock();
    Env envMock = new EnvMock();

    public DataLoadServiceTester(){
        dataLoadService.setDataStore(dataStore);
    }

    @Before
    public void clean(){
        dataStore.clearData();
    }

    @Test
    public void testGetId() throws MalformedURLException {
        URL url = this.getClass().getResource("/stock_0A0A0A.dat");
        File f = new File("stock_0001.dat");
        assertTrue("0A0A0A".equals(dataLoadService.getId(url)));
        assertTrue("0001".equals(dataLoadService.getId(f.toURL())));
    }

    @Test
    public void testStart() {
        dataLoadService.start(envMock);

        List<DataPoint> dataPointList = dataStore.getData("0A0A0A", "day_open_price", "20150828", "20150828");
        assertEquals(dataPointList.get(0),new DataPoint("20150828","3125.26"));
        dataPointList = dataStore.getData("0A0A0A", "day_open_price", "20150701", "20150701");
        assertEquals(dataPointList.get(0),new DataPoint("20150701","4214.15"));

        dataPointList = dataStore.getData("0A0A0A", "day_lowest_price", "20150803", "20150803");
        assertEquals(dataPointList.get(0),new DataPoint("20150803","3549.50"));
    }
}