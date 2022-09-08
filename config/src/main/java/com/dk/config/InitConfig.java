package com.dk.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

/**
 * @author dkay
 * @version 1.0
 */
public class InitConfig {
    private static final String SERVER_NAME_KEY = "server-name";

    public static void initializationLogger() {
        Properties properties = new Properties();
        try (InputStream asStream = InitConfig.class.getClassLoader().getResourceAsStream("config/config.properties")) {
            properties.load(asStream);
        } catch (IOException e) {
            // ignore
        }

        String serverName = properties.getProperty(SERVER_NAME_KEY);
        if (Objects.isNull(serverName)) {
            // use element FILE_NAME default value simple-questions of log4j2.xml
            return;
        }
        System.setProperty(SERVER_NAME_KEY, serverName);
    }
}
