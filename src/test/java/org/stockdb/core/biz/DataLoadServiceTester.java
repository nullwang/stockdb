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

import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import static junit.framework.TestCase.assertTrue;

public class DataLoadServiceTester {

    DataLoadService dataLoadService = new DataLoadService();

    @Test
    public void testGetId() throws MalformedURLException {
        URL url = this.getClass().getResource("/stock_0A0A0A.dat");
        File f = new File("stock_0001.dat");
        assertTrue("0A0A0A".equals(dataLoadService.getId(url)));
        assertTrue("0001".equals(dataLoadService.getId(f.toURL())));
    }
}
