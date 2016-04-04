package org.stockdb.startup;
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

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.ssl.SslSelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.stockdb.core.exception.StockDBException;

public class JettyServer {

    private Logger logger = LoggerFactory.getLogger(JettyServer.class);

    private Server server;

    private int jettyPort;

    private String jettyWebRoot;

    private int jettySslPort;

    private String keyStorePath;

    private String keyStorePassword;

    private static final String CONTEXT_PATH = "/api";
    private static final String MAPPING_URL = "/";

    JettyServer(int jettyPort, String jettyWebRoot){
        this.jettyPort = jettyPort;
        this.jettyWebRoot = jettyWebRoot;
    }

    JettyServer  (int jettyPort, int jettySslPort, String jettyWebRoot, String keyStorePath, String keyStorePassword){
        this.jettyPort = jettyPort;
        this.jettySslPort = jettySslPort;
        this.jettyWebRoot = jettyWebRoot;
        this.keyStorePath = keyStorePath;
        this.keyStorePassword = keyStorePassword;
    }

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

            AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
            context.setConfigLocation("org.stockdb.startup.config");
            context.getEnvironment().setDefaultProfiles("prod");

            ServletContextHandler servletContextHandler =
                    new ServletContextHandler();

            servletContextHandler.setContextPath(CONTEXT_PATH);
            servletContextHandler.addServlet(new ServletHolder(new DispatcherServlet(context)), MAPPING_URL);

            ResourceHandler resourceHandler = new ResourceHandler();
            resourceHandler.setDirectoriesListed(true);
            resourceHandler.setWelcomeFiles(new String[]{"index.html"});
            resourceHandler.setResourceBase(new ClassPathResource(jettyWebRoot).getURI().toString());

            HandlerList handlers = new HandlerList();
            handlers.setHandlers(new Handler[]{servletContextHandler,resourceHandler, new DefaultHandler()});
            server.setHandler(handlers);

            server.start();
        }catch (Exception e){
            throw new StockDBException(e);
        }
    }

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
