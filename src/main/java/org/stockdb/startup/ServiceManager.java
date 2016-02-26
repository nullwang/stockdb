package org.stockdb.startup;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;
import org.stockdb.core.exception.StockDBException;
import org.stockdb.startup.config.StockPropertyConfigurer;

import java.util.*;

@Component
public class ServiceManager implements ApplicationListener<ApplicationContextEvent> {

    @Autowired
    private StockPropertyConfigurer stockPropertyConfigurer;

    private Logger logger = LoggerFactory.getLogger(ServiceManager.class);

    private List<StockDBService> services;

    private List<StockDBService> getServices(ApplicationContext ap) {
        if (services != null) return services;
        else {
            Map<String, StockDBService> serviceMap = ap.getBeansOfType(StockDBService.class);
            //sort level
            List<StockDBService> serviceList = new ArrayList(serviceMap.values());
            Collections.sort(serviceList, new Comparator<StockDBService>() {
                @Override
                public int compare(StockDBService o1, StockDBService o2) {
                    return o2.getLevel() - o1.getLevel();
                }
            });
            return services = serviceList;
        }
    }

    private void start(ApplicationContextEvent applicationContextEvent) {
        List<StockDBService> serviceList = getServices(applicationContextEvent.getApplicationContext());
        //start all services by order
        for (StockDBService stockDBService : serviceList) {
            try {
                stockDBService.start(stockPropertyConfigurer);
            } catch (StockDBException e) {
                logger.error("Start service " + stockDBService.getClass() + " error ", e);
            }
        }
    }

    private void stop(ApplicationContextEvent applicationContextEvent) {
        List<StockDBService> serviceList = getServices(applicationContextEvent.getApplicationContext());
        //start all services by order
        for (StockDBService stockDBService : serviceList) {
            try {
                stockDBService.stop();
            } catch (StockDBException e) {
                logger.error("Stop service " + stockDBService.getClass() + " error ", e);
            }
        }
    }

    @Override
    public void onApplicationEvent(ApplicationContextEvent applicationContextEvent) {
        if (applicationContextEvent instanceof ContextRefreshedEvent)
            start(applicationContextEvent);
        else if (applicationContextEvent instanceof ContextStoppedEvent) {
            stop(applicationContextEvent);
        }
    }
}
