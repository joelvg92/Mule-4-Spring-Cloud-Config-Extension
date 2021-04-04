/*
 * (c) 2003-2018 MuleSoft, Inc. This software is protected under international copyright
 * law. All use of this software is subject to MuleSoft's Master Subscription Agreement
 * (or other master license agreement) separately entered into in writing between you and
 * MuleSoft. If such an agreement is not in place, you may not use the software.
 */
package com.mule.spring.config;

import static org.mule.runtime.api.component.ComponentIdentifier.builder;
import org.mule.runtime.api.component.ComponentIdentifier;
import org.mule.runtime.config.api.dsl.model.ConfigurationParameters;
import org.mule.runtime.config.api.dsl.model.ResourceProvider;
import org.mule.runtime.config.api.dsl.model.properties.ConfigurationPropertiesProvider;
import org.mule.runtime.config.api.dsl.model.properties.ConfigurationPropertiesProviderFactory;
import org.mule.runtime.config.api.dsl.model.properties.ConfigurationProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Properties;

public class CustomConfigurationPropertiesProviderFactory implements ConfigurationPropertiesProviderFactory {
  private static final Logger logger = LoggerFactory.getLogger(CustomConfigurationPropertiesProviderFactory.class);
  public static final String EXTENSION_NAMESPACE =
      CustomConfigurationPropertiesExtensionLoadingDelegate.EXTENSION_NAME.toLowerCase().replace(" ", "-");
  private static final ComponentIdentifier CUSTOM_PROPERTIES_PROVIDER =
      builder().namespace(EXTENSION_NAMESPACE).name(CustomConfigurationPropertiesExtensionLoadingDelegate.CONFIG_ELEMENT).build();
  private final static String CUSTOM_PROPERTIES_PREFIX = "custom::";
  private static Properties props;

  @Override
  public ComponentIdentifier getSupportedComponentIdentifier() {
    return CUSTOM_PROPERTIES_PROVIDER;
  }

  @Override
  public ConfigurationPropertiesProvider createProvider(ConfigurationParameters parameters,
                                                        ResourceProvider externalResourceProvider) {

    try {
      String host = parameters.getStringParameter("configServerBaseUrl");
      String path = "/"+parameters.getStringParameter("applicationName") + "/" + parameters.getStringParameter("environment");
      props = SpringCloudConfig.getProperties(host,path);
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
    return new ConfigurationPropertiesProvider() {

      @Override
      public Optional<ConfigurationProperty> getConfigurationProperty(String configurationAttributeKey) {
        if (configurationAttributeKey.startsWith(CUSTOM_PROPERTIES_PREFIX)) {
          String effectiveKey = configurationAttributeKey.substring(CUSTOM_PROPERTIES_PREFIX.length());
            return Optional.of(new ConfigurationProperty() {

              @Override
              public Object getSource() {
                try {
                  return props;
                } catch (Exception e) {
                  logger.error(e.getMessage());
                }
                return "default source";
              }

              @Override
              public Object getRawValue() {
                return props.get(effectiveKey);
              }

              @Override
              public String getKey() {
                return effectiveKey;
              }
            });
        }
        return Optional.empty();
      }

      @Override
      public String getDescription() {
        // TODO change to a meaningful name for error reporting.
        return "Mule Custom Spring Config Provider";
      }
    };
  }

}
