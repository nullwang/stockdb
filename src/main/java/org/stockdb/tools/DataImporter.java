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
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class DataImporter {

    private static final Arguments arguments = new Arguments();
    private static Logger logger = LoggerFactory.getLogger(DataImporter.class);

    //import data using cmd "org.stockdb.tools.DataImporter -d example\stock_shanghai_a.dat -f d"
    //import metric using cmd "org.stockdb.tools.DataImporter -d example\stock_metric.dat -f m"

    public static void main(String[] args) {

        RestTemplate restTemplate = new RestTemplate();
        //restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

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
        StringBuilder data = new StringBuilder();
        LineIterator lt = null;
        try {
             lt = FileUtils.lineIterator(file,"UTF-8");
            while(lt.hasNext()){
                String str = lt.next();
                if(!StringUtils.startsWith(str,"//")){
                    data.append(str);
                }
            }
        } catch (IOException e) {
            logger.error("read file " + file + "error", e);
        }finally {
            if( lt !=null) lt.close();
        }
        //import metrics
        if ("m".equals(arguments.format)) {
            sb.append("/metrics");
        } else if ("d".equals(arguments.format)) {
            //import data
            sb.append("/d");
        }else if( "ds".equals(arguments.format)){
            sb.append("/ds");
        }

        String host = arguments.host == null ? "localhost" : arguments.host;
        String port = arguments.port == null ? "7070" : arguments.port;

        URI expanded = new UriTemplate(sb.toString()).expand(host,port);
        logger.info("request url : " + expanded);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(data.toString(),headers);
        ResponseEntity<String> responseEntity = restTemplate
                .exchange(sb.toString(), HttpMethod.POST, entity, String.class,host,port);
        logger.info("response code : " + responseEntity.getStatusCode() + " ,body: " + responseEntity.getBody());

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            //JSONObject userJson = new JSONObject(loginResponse.getBody());
        } else if (responseEntity.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            // nono... bad credentials
        }

        //String result = restTemplate.postForObject(sb.toString(), str,String.class,host,port);

    }

    @SuppressWarnings("UnusedDeclaration")
    private static class Arguments {
        @Parameter(names = "-f", description = "the format of the data m-metrics,d-单行数据,ds-多行数据]",required = true)
        private String format;

        @Parameter(names = "-h", description = "host")
        private String host;

        @Parameter(names = "-p", description = "port")
        private String port;

        @Parameter(names = "-d", description = "the data or metrics file" ,required = true)
        private String file;
    }
}