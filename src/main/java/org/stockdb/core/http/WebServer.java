package org.stockdb.core.http;
/*
 * @author nullwang@hotmail.com
 * created at 2015/3/13
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

import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.ssl.SslSelectChannelConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.stockdb.core.StockDBService;
import org.stockdb.core.exception.StockDBException;


public class WebServer implements StockDBService {

    private Logger logger = LoggerFactory.getLogger(WebServer.class);

    private Server server;

    @Value("${stockdb.jetty.port:8080}")
    private int jettyPort;

    @Value("${stockdb.jetty.static_web_root}")
    private String jettyWebRoot;

    @Value("${stockdb.jetty.ssl_port:8443}")
    private int jettySslPort;

    @Value("${stockdb.jetty.key_store_path:}")
    private String keyStorePath;

    @Value("${stockdb.jetty.key_store_password:}")
    private String keyStorePassword;

    @Override
    public void start() throws StockDBException {
        try {
            if (jettyPort > 0) {
                server = new Server(jettyPort);
            } else {
                server = new Server();
            }

            if (!StringUtils.isEmpty(keyStorePath)) {
                logger.info("Using ssl");
                SslContextFactory sslContextFactory = new SslContextFactory(keyStorePath);
                sslContextFactory.setKeyStorePassword(keyStorePassword);
                SslSelectChannelConnector selectChannelConnector = new SslSelectChannelConnector(sslContextFactory);
                selectChannelConnector.setPort(jettySslPort);
                server.addConnector(selectChannelConnector);
            }

            ServletContextHandler servletContextHandler =
                    new ServletContextHandler();

            servletContextHandler.addServlet(DefaultServlet.class, "/api/*");
            ResourceHandler resourceHandler = new ResourceHandler();
            resourceHandler.setDirectoriesListed(true);
            resourceHandler.setWelcomeFiles(new String[]{"index.html"});
            resourceHandler.setResourceBase(jettyWebRoot);

            HandlerList handlers = new HandlerList();
            handlers.setHandlers(new Handler[]{servletContextHandler, resourceHandler, new DefaultHandler()});
            server.setHandler(handlers);

            server.start();
        }catch (Exception e){
            throw new StockDBException(e);
        }
    }

    @Override
    public void stop() throws StockDBException {
        try{
            if( server != null){
                server.stop();
                server.join();
            }
        }catch (Exception e){
            throw new StockDBException(e);
        }
    }
}
