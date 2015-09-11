package org.stockdb.core.util;
/*
 * @author nullwang@hotmail.com
 * created at 2015/9/11
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PropertyUtil {

    static private Logger logger = LoggerFactory.getLogger(PropertyUtil.class);

    static public Properties mergeProperties(Properties... properties) {
        Properties confProperties = new Properties();
        for (Properties property : properties) {
            if( property != null )
                confProperties.putAll(property);
        }
        return confProperties;
    }

    static public Properties mergePropertyFileIgnoreException(Properties property,File... propertiesFile) {
        List<Properties> propertiesList = new ArrayList<Properties>();
        if( property != null ) {
            propertiesList.add(property);
        }

        for (File f : propertiesFile) {
            Properties p = new Properties();
            try {
                p.load(new FileInputStream(f));
                propertiesList.add(p);
            } catch (IOException e) {
                logger.warn("reade property file " + f.getAbsolutePath() + " error", e);
            }
        }
        return mergeProperties(propertiesList.toArray(new Properties[propertiesList.size()]));
    }
}
