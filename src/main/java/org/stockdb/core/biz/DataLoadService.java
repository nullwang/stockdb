package org.stockdb.core.biz;
/*
 * @author nullwang@hotmail.com
 * created at 2016/3/21
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

import com.google.gson.JsonSyntaxException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.AbstractFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stockdb.core.datastore.DataPoint;
import org.stockdb.core.datastore.DataStore;
import org.stockdb.core.datastore.Env;
import org.stockdb.core.exception.StockDBException;
import org.stockdb.startup.StockDBService;
import org.stockdb.util.Commons;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

//用于提供案例数据，在服务器启动时启动

@Service
public class DataLoadService implements StockDBService {

    @Autowired
    private DataStore dataStore;

    private Logger logger = LoggerFactory.getLogger(DataLoadService.class);

    @Override
    public void start(Env env) throws StockDBException {
        String dir = env.get("init_data_dir");
        List<URL> urlList = collectResource(dir);
        loadFromResource(urlList.toArray(new URL[urlList.size()]));
        logger.info(" service [DATA_LOAD] started ");
    }

    private List<URL> collectResource(String rootDir)
    {
        String dir = rootDir;
        List<URL> resources = new ArrayList();
        resources.add(this.getClass().getResource("/stock_0A0A0A.dat"));
        if( !StringUtils.isEmpty(dir)){
            Collection<File> files = FileUtils.listFiles(new File(dir), new AbstractFileFilter() {
                @Override
                public boolean accept(File file) {
                    try {
                        return file.getName().endsWith(".dat") && getId(file.toURL()) != null;
                    } catch (MalformedURLException e) {
                        logger.error("error data file name" + file.getName());
                        return false;
                    }
                }
            }, null);
            for(File file:files){
                try {
                    resources.add(file.toURL());
                } catch (MalformedURLException e) {
                    //e.printStackTrace();
                }
            }
        }
        return resources;
    }

    private void loadFromResource(URL... urls) {
        assert (urls != null);
        for (URL url : urls) {
            BufferedReader bufferedReader = null;
            InputStreamReader inputStreamReader = null;
            InputStream inputStream = null;
            int dataCnt=0, objAttrCnt= 0, metricAttrCnt = 0, section = 2 ,lineNo =0;
            try {
                String id = getId(url);
                inputStream = url.openStream();
                inputStreamReader = new InputStreamReader(inputStream,"utf-8");
                bufferedReader = new BufferedReader(inputStreamReader);
                String line,metricName=null;
                while( (line = bufferedReader.readLine()) != null){
                    lineNo ++;
                    if(StringUtils.startsWith(line,"//") || StringUtils.isEmpty(line))continue;
                    if( StringUtils.startsWith(line,"#object")) {
                        section = 1; // 对象区
                        continue;
                    }
                    else if( StringUtils.startsWith(line,"#data")) {
                        section = 2; // 数据区
                        continue;
                    }else if( StringUtils.startsWith(line,"#")){
                        section = 3; // 指标区
                        metricName = StringUtils.substringAfter(line,"#");
                        continue;
                    }
                    if( section == 2) {
                        String[] data = StringUtils.split(line, ",");
                        if (data.length < 3) throw new IOException(" error format of {"+lineNo + "} in {" + url.getFile() + " }");
                        dataStore.putData(id, data[0], new DataPoint(data[1], data[2]));
                        dataCnt++;
                    }
                    else {
                        try {
                            Map<String,String> attrMap = Commons.jsonMap(line);
                            for(Map.Entry<String,String> entry: attrMap.entrySet()){
                                if( section == 1) {
                                    dataStore.setObjAttr(id, entry.getKey(), entry.getValue());
                                }
                                if(section == 3){
                                    dataStore.setMetricAttr(metricName, entry.getKey(),entry.getValue());
                                }
                            }
                        }catch (JsonSyntaxException e){
                            throw new IOException(" error format of {"+lineNo + "} in {" + url.getFile() + " }");
                        }
                    }
                }
                logger.info("load data from resource {} total {} ok", url.toString(),dataCnt);
            }catch (Exception e) {
                logger.error("load data from resource" + url.toString() + " error ",e);
            }finally {
                IOUtils.closeQuietly(bufferedReader);
                IOUtils.closeQuietly(inputStreamReader);
                IOUtils.closeQuietly(inputStream);
            }
        }
    }

    @Override
    public void stop() throws StockDBException {

    }

    @Override
    public int getLevel() {
        return 999;
    }

    String getId (URL url){
        String path = url.getPath();
        String name = StringUtils.substringAfterLast(path, "/");
        return StringUtils.substringBetween(name,"_",".");
    }

    public DataStore getDataStore() {
        return dataStore;
    }

    public void setDataStore(DataStore dataStore) {
        this.dataStore = dataStore;
    }
}
