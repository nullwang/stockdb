package org.stockdb.startup.config;
/*
 * @author nullwang@hotmail.com
 * created at 2015/9/10
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

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurablePropertyResolver;
import org.stockdb.core.datastore.Env;

import java.io.File;

public class StockPropertyConfigurer extends PropertySourcesPlaceholderConfigurer implements Env {

    //private static Map<String, Object> ctxPropertiesMap;

    private static ConfigurablePropertyResolver propertyResolver;

    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, ConfigurablePropertyResolver propertyResolver) throws org.springframework.beans.BeansException {
        super.processProperties(beanFactoryToProcess, propertyResolver);
        this.propertyResolver = propertyResolver;
    }

    //static method for accessing context properties
    public String get(String name) {
        return propertyResolver.getProperty(name);
    }

    public Object get(String name, Object defaultValue) {
        String v = get(name);
        if( v == null ) return defaultValue;
        else return v;
    }

    //Returns the config file "conf/stock.conf"
    public static File getConfFile() {
        return new File("conf", "stock.conf");
    }
}