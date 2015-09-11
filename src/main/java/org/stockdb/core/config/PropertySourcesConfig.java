package org.stockdb.core.config;
/*
 * @author nullwang@hotmail.com
 * created at 2015/4/1
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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Configuration
@SuppressWarnings("UnusedDeclaration")
public class PropertySourcesConfig {
    private static final Resource DEV_PROPERTIES = new ClassPathResource("example-dev.properties");
    private static final Resource TEST_PROPERTIES = new ClassPathResource("example-test.properties");
    private static final Resource PROD_PROPERTIES = new ClassPathResource("stock.properties");

    @Profile("dev")
    @SuppressWarnings("UnusedDeclaration")
    public static class DevConfig {
        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            PropertySourcesPlaceholderConfigurer pspc = new StockPropertyConfigurer();
            pspc.setLocations(addConfPropertyFile(DEV_PROPERTIES));
            return pspc;
        }
    }

    @Profile("test")
    @SuppressWarnings("UnusedDeclaration")
    public static class TestConfig {
        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            PropertySourcesPlaceholderConfigurer pspc = new StockPropertyConfigurer();
            pspc.setLocations(addConfPropertyFile(TEST_PROPERTIES));
            return pspc;
        }
    }

    @Profile("prod")
    @SuppressWarnings("UnusedDeclaration")
    public static class ProdConfig {
        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            PropertySourcesPlaceholderConfigurer pspc = new StockPropertyConfigurer();
            List<Resource> resourceList = new ArrayList<Resource>();

            pspc.setLocations(addConfPropertyFile(PROD_PROPERTIES));

            return pspc;
        }
    }

    private static Resource[] addConfPropertyFile(Resource... resources)
    {
        List<Resource> resourceList = new ArrayList<Resource>();
        for(Resource resource:resources){
            resourceList.add(resource);
        }
        //conf/stock.conf
        File f = StockPropertyConfigurer.getConfFile();
        if( f.exists()){
            resourceList.add(new FileSystemResource(f.getAbsolutePath()));
        }

        return resourceList.toArray(new Resource[resourceList.size()]);
    }
}
