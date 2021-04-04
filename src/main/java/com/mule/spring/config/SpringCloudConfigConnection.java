package com.mule.spring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class SpringCloudConfigConnection {
    URLConnection con = null;

    private static final Logger logger = LoggerFactory.getLogger(SpringCloudConfigConnection.class);

    public SpringCloudConfigConnection(String host, String basePath) {
        con = createConnection(host,basePath);
    }

    public URLConnection createConnection(String host, String basePath) {
        URLConnection connection = null;
        String protocol = host.split(":")[0].toUpperCase();
        logger.debug("Creating connection "+host + basePath);
        try {
            connection = new URL(host + basePath).openConnection();
            logger.debug("Connection created successfully "+host + basePath);
        }
        catch(IOException e) {
            logger.error("Error occurred while creating connection");
            e.printStackTrace();
        }
        connection.addRequestProperty("User-Agent", "Mozilla");
        return "HTTPS".equals(protocol) ? (HttpsURLConnection) connection : (HttpURLConnection) connection;
    }

    public URLConnection getConnection() {
        logger.debug("Connection Requested in getConnection()");
        return this.con;
    }
}
