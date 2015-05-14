package org.stockdb.tools;
/*
 * @author nullwang@hotmail.com
 * created at 2015/4/9
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

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class DataImporter {

    private static final Arguments arguments = new Arguments();
    private static Logger logger = LoggerFactory.getLogger(DataImporter.class);

    public static void main(String[] args) {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        JCommander commander = new JCommander(arguments);
        try {
            commander.parse(args);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            commander.usage();
            System.exit(0);
        }

        StringBuilder sb = new StringBuilder("http://{host}:{port}/api/v1");
        File file = new File(arguments.file);
        String str = null;
        try {
            str = FileUtils.readFileToString(file);
        } catch (IOException e) {
            logger.error("read file " + file + "error", e);
        }
        //import metrics
        if ("metrics".equals(arguments.format)) {
            sb.append("/metrics");
        } else {
            //import data
            sb.append("/data");
        }

        String host = arguments.host == null ? "localhost" : arguments.host;
        String port = arguments.port == null ? "7070" : arguments.port;

        URI expanded = new UriTemplate(sb.toString()).expand(host,port);
        logger.info("request url : " + expanded);
        String result = restTemplate.postForObject(sb.toString(), str,String.class,host,port);
        logger.info("response : " + result);
    }

    @SuppressWarnings("UnusedDeclaration")
    private static class Arguments {
        @Parameter(names = "-d", description = "the format of the data")
        private String format;

        @Parameter(names = "-h", description = "host")
        private String host;

        @Parameter(names = "-p", description = "host")
        private String port;

        @Parameter(names = "-f", description = "the data file")
        private String file;
    }
}
