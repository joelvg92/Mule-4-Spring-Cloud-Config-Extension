package com.mule.spring.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class SpringCloudConfig {

    private static final Logger logger = LoggerFactory.getLogger(SpringCloudConfig.class);

    private static final String NAME_PROPERTY = "name";


    private static final String PROPERTY_SOURCES_PROPERTY = "propertySources";


    private static final String SOURCE_PROPERTY = "source";

    public static Properties getProperties(String host, String basePath) throws Exception {
        Properties properties = new Properties();
        SpringCloudConfigConnection springCloudConfigConnection = new SpringCloudConfigConnection(host,basePath);
        URLConnection connection = springCloudConfigConnection.getConnection();
        Map<String, Object> result = new ObjectMapper().readValue(connection.getInputStream(), HashMap.class);;

        logger.debug("Got settings from cloud config server: " + result.toString());
        
        List<Map> sources = (List<Map>) result.get(PROPERTY_SOURCES_PROPERTY);

        logger.debug("Property sources are: " + sources);

        for(int i = sources.size() - 1; i >= 0; i--) {

            Map<String, String> source = (Map<String, String>) sources.get(i).get(SOURCE_PROPERTY);

            String name = (String) sources.get(i).get(NAME_PROPERTY);

            if (name != null && logger.isDebugEnabled()) {
                logger.debug("Reading properties from source: " + name);
            }

            for(Map.Entry<String, String> entry : source.entrySet()) {

                if (logger.isDebugEnabled()) {
                    logger.debug("Read property with key: " + entry.getKey() + " from source.");
                }

                properties.put(entry.getKey(), entry.getValue());
            }

        }
        return properties;

    }

}
