/*
 * (c) 2003-2018 MuleSoft, Inc. This software is protected under international copyright
 * law. All use of this software is subject to MuleSoft's Master Subscription Agreement
 * (or other master license agreement) separately entered into in writing between you and
 * MuleSoft. If such an agreement is not in place, you may not use the software.
 */
package com.mule.spring.config;

import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.runtime.api.meta.Category.SELECT;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.runtime.api.meta.model.declaration.fluent.ConfigurationDeclarer;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterGroupDeclarer;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.api.loader.ExtensionLoadingDelegate;


public class CustomConfigurationPropertiesExtensionLoadingDelegate implements ExtensionLoadingDelegate {

  // TODO replace with you extension name. This must be a meaningful name for this module.
    public static final String EXTENSION_NAME = "Mule Custom Spring Config Provider";
    public static final String CONFIG_ELEMENT = "config";

  @Override
  public void accept(ExtensionDeclarer extensionDeclarer, ExtensionLoadingContext context) {
    ConfigurationDeclarer configurationDeclarer = extensionDeclarer.named(EXTENSION_NAME)
        .describedAs(String.format("Crafted %s Extension", EXTENSION_NAME))
        .withCategory(SELECT)
        .onVersion("1.0.0")
        .fromVendor("SpringConfig")
        .withConfig(CONFIG_ELEMENT);

    ParameterGroupDeclarer defaultParameterGroup = configurationDeclarer.onDefaultParameterGroup();
    // TODO you can add/remove configuration parameter using the code below.
    defaultParameterGroup
        .withRequiredParameter("applicationName").ofType(BaseTypeBuilder.create(JAVA).stringType().build())
        .withExpressionSupport(NOT_SUPPORTED)
        .describedAs("Spring configuration application name");
    defaultParameterGroup
            .withRequiredParameter("configServerBaseUrl").ofType(BaseTypeBuilder.create(JAVA).stringType().build())
            .withExpressionSupport(NOT_SUPPORTED)
            .describedAs("Spring configuration server base url");
    defaultParameterGroup
            .withRequiredParameter("environment").ofType(BaseTypeBuilder.create(JAVA).stringType().build())
            .withExpressionSupport(NOT_SUPPORTED)
            .describedAs("Spring configuration environment/profile");
  }

}
