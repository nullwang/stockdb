package org.stockdb.core.biz;
/*
 * @author nullwang@hotmail.com
 * created at 2015/5/21
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stockdb.core.datastore.DataStore;
import org.stockdb.core.datastore.ObjectMetricDataSet;
import org.stockdb.core.exception.StockDBException;

import java.util.ArrayList;
import java.util.List;

public class StockService {

    public static String STR_NAME="name";

    private DataStore dataStore;

    public DataStore getDataStore() {
        return dataStore;
    }

    public void setDataStore(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public void setIndicatorName(String indicatorId, String name) throws StockDBException {
        dataStore.setMetricAttr(indicatorId,STR_NAME,name);
    }

    public String[] getIndicatorNames(String... indicatorIds) throws StockDBException
    {
        if( indicatorIds == null ) return null;
        List<String> names = new ArrayList();
        for(String id: indicatorIds ) {
            names.add(dataStore.getMetricAttr(id, STR_NAME));
        }
        return names.toArray(new String[names.size()]);
    }

    public ObjectMetricDataSet getKLineData(String id, String metricName, String startTime,String endTime ) throws StockDBException{

    }

    public getK30lineData(String ids) throws StockDBException{

    }

}