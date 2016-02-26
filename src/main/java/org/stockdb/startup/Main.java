package org.stockdb.startup;
/*
 * @author nullwang@hotmail.com
 * created at 2015/3/11
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

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.stockdb.startup.config.StockPropertyConfigurer;
import org.stockdb.core.exception.StockDBException;
import org.stockdb.core.util.PropertyUtil;

import java.io.File;
import java.util.List;
import java.util.Properties;

public class Main {

    public static final Logger logger = (Logger) LoggerFactory.getLogger(Main.class);

    private static final Arguments arguments = new Arguments();

    private final static Object s_shutdownObject = new Object();

    private JettyServer jettyServer;

    protected Main(Properties properties) throws StockDBException {
        int port = Integer.parseInt(properties.getProperty("stockdb.jetty.port","7070"));
        int sslPort = Integer.parseInt(properties.getProperty("stockdb.jetty.ssl_port","7443"));
        String webRoot = properties.getProperty("stockdb.jetty.static_web_root","webroot");
        String keyStorePath = properties.getProperty("stockdb.jetty.key_store_path");
        String keyStorePassword = properties.getProperty("stockdb.jetty.key_store_password");

        jettyServer = new JettyServer(port,sslPort,webRoot,keyStorePath,keyStorePassword);
    }

    public static void main(String[] args) {
        JCommander commander = new JCommander(arguments);
        try {
            commander.parse(args);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            commander.usage();
            System.exit(0);
        }

        if (arguments.helpMessage || arguments.help) {
            commander.usage();
            System.exit(0);
        }

        if (!arguments.operationCommand.equals("run")) {
            //Turn off console logging
            Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
            root.getAppender("stdout").addFilter(new Filter<ILoggingEvent>() {
                @Override
                public FilterReply decide(ILoggingEvent iLoggingEvent) {
                    return (FilterReply.DENY);
                }
            });
        }

        try {
            Properties properties = null;
            if (!StringUtils.isEmpty(arguments.propertiesFile)) {
                File propertiesFile = new File(arguments.propertiesFile);
                properties = PropertyUtil.mergePropertyFileIgnoreException(null,propertiesFile);
                logger.info(" loading property file {}", propertiesFile);
            }

            File f = StockPropertyConfigurer.getConfFile();
            if( f.exists()){
                properties = PropertyUtil.mergePropertyFileIgnoreException(properties,f);
                logger.info(" loading property file {}", f);
            }

            //system properties override configure property
            properties = PropertyUtil.mergeProperties(properties,System.getProperties());

            final Main main = new Main(properties);
            if (arguments.operationCommand.equals("run") || arguments.operationCommand.equals("start")) {
                Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                    public void run() {
                        try {
                            main.stopServices();

                            synchronized (s_shutdownObject) {
                                s_shutdownObject.notify();
                            }
                        } catch (Exception e) {
                            logger.error("Shutdown exception:", e);
                        }
                    }
                }));

                main.startServices();

                logger.info("------------------------------------------");
                logger.info("     StockDB service started");
                logger.info("------------------------------------------");

                waitForShutdown();
            }
        } catch (Exception e) {
            logger.error("Failed starting up services", e);
            //main.stopServices();
            System.exit(0);
        } finally {
            logger.info("--------------------------------------");
            logger.info("     StockDB service is now down!");
            logger.info("--------------------------------------");
        }
    }

    private static void waitForShutdown() {
        try {
            synchronized (s_shutdownObject) {
                s_shutdownObject.wait();
            }
        } catch (InterruptedException ignore) {
        }
    }

    private void startServices() throws StockDBException {
        jettyServer.start();
    }

    private void stopServices() throws StockDBException {
        jettyServer.stop();
    }


    @SuppressWarnings("UnusedDeclaration")
    private static class Arguments {
        @Parameter(names = "-p", description = "A custom properties file")
        private String propertiesFile;

        @Parameter(names = "-f", description = "File to save export to or read from depending on command.")
        private String exportFile;

        @Parameter(names = "-n", description = "Name of metrics to export. If not specified, then all metrics are exported.")
        private List<String> exportMetricNames;

        @Parameter(names = "-r", description = "Full path to a recovery file. The file tracks metrics that have been exported. " +
                "If export fails and is run again it uses this file to pickup where it left off.")
        private String exportRecoveryFile;

        @Parameter(names = "-a", description = "Appends to the export file. By default, the export file is overwritten.")
        private boolean appendToExportFile;

        @Parameter(names = "--help", description = "Help message.", help = true)
        private boolean helpMessage;

        @Parameter(names = "-h", description = "Help message.", help = true)
        private boolean help;

        /**
         * start is identical to run except that logging data only goes to the log file
         * and not to standard out as well
         */
        @Parameter(names = "-c", description = "Command to run: export, import, run, start.")
        private String operationCommand;
    }
}