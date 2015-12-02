package com.salesforceiq.augmenteddriver.util.saucelabs;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.AbstractModule;
import com.saucelabs.saucerest.SauceREST;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class SauceLabsModule extends AbstractModule {

    @Override
    protected void configure() {
        Properties properties = new Properties();
        bind(SauceCommandLineArguments.class).toInstance(SauceCommandLineArguments.ARGUMENTS);
        Path propertiesPath = Paths.get(SauceCommandLineArguments.ARGUMENTS.conf());

        if (Files.exists(propertiesPath)) {
            try {
                properties.load(new FileInputStream(propertiesPath.toFile()));
            } catch (IOException e) {
                throw new IllegalStateException("Failed to load properties file " + propertiesPath, e);
            }
        } else {
            throw new IllegalArgumentException("Properties file does not exist " + propertiesPath);
        }

        String sauceKey = properties.getProperty(PropertiesModule.SAUCE_KEY);
        String sauceUser = properties.getProperty(PropertiesModule.SAUCE_USER);

        Preconditions.checkArgument(!Strings.isNullOrEmpty(sauceKey), String.format("Set %s in the properties file", PropertiesModule.SAUCE_KEY));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(sauceUser), String.format("Set %s in the properties file", PropertiesModule.SAUCE_USER));

        bind(SauceREST.class).toInstance(new SauceREST(sauceUser, sauceKey));
    }

}
