package com.salesforceiq.augmenteddriver.modules;

import com.google.common.base.Strings;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.salesforceiq.augmenteddriver.util.CommandLineArguments;
import com.salesforceiq.augmenteddriver.util.Util;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Guice Module that loads all the properties file.
 */
public class PropertiesModule extends AbstractModule {
    public static final String TEAM_CITY_INTEGRATION = "TEAM_CITY_INTEGRATION";
    public static final String REPORTING = "REPORTING";
    public static final String REMOTE_ADDRESS = "REMOTE_ADDRESS";
    public static final String UNIQUE_ID = "UNIQUE_ID";
    public static final String WAIT_IN_SECONDS = "WAIT_TIME_IN_SECONDS";
    public static final String MAX_RETRIES = "MAX_RETRIES";
    public static final String SAUCE_USER = "SAUCE_USER";
    public static final String SAUCE_KEY = "SAUCE_KEY";
    public static final String LOCAL_ADDRESS = "LOCAL_ADDRESS";
    public static final String SAUCE_ADDRESS = "SAUCE_ADDRESS";

    private static final String ID = Util.getRandomAsString();

    /**
     * For now all the properties are defined here.
     */
    private static final Map<String, String> defaultProperties = new HashMap<String, String>() {
        {
            put(LOCAL_ADDRESS, "http://127.0.0.1:7777/wd/hub");
            put(SAUCE_ADDRESS, "http://ondemand.saucelabs.com:80/wd/hub");
            put(WAIT_IN_SECONDS, "30");
            put(TEAM_CITY_INTEGRATION, "false");
            put(REPORTING, "false");
            put(SAUCE_KEY, "");
            put(SAUCE_USER, "");
            put(MAX_RETRIES, "2");
        }
    };

    @Override
    protected void configure() {
        Properties properties = new Properties();
        defaultProperties.entrySet()
                .stream()
                .forEach(entry -> properties.setProperty(entry.getKey(), entry.getValue()));

        if (CommandLineArguments.ARGUMENTS == null) {
            throw new IllegalStateException("Arguments were not loaded. Please use @Capabilities or command line args.");
        }

        Path propertiesPath = Paths.get(CommandLineArguments.ARGUMENTS.conf());
        if (Files.exists(propertiesPath)) {
            try {
                properties.load(new FileInputStream(propertiesPath.toFile()));
            } catch (IOException e) {
                throw new IllegalStateException("Failed to load properties file " + propertiesPath, e);
            }
        } else {
            throw new IllegalArgumentException("Properties file does not exist " + propertiesPath);
        }

        if (CommandLineArguments.ARGUMENTS.sauce()) {
            setSauceProperties(properties);
        } else {
            properties.setProperty(PropertiesModule.REMOTE_ADDRESS, properties.getProperty(PropertiesModule.LOCAL_ADDRESS));
        }

        Names.bindProperties(binder(), properties);
        bind(DesiredCapabilities.class).toInstance(CommandLineArguments.ARGUMENTS.capabilities());
        bind(String.class).annotatedWith(Names.named(PropertiesModule.UNIQUE_ID)).toInstance(ID);
    }

    /**
     * Hack to set the sauce key and sauce user into the capabilities.
     */
    private void setSauceProperties(Properties properties) {
        properties.setProperty(PropertiesModule.REMOTE_ADDRESS, properties.getProperty(PropertiesModule.SAUCE_ADDRESS));

        if (Strings.isNullOrEmpty(properties.getProperty(PropertiesModule.SAUCE_KEY))) {
            throw new IllegalArgumentException("To run on Sauce Labs, define SAUCE_KEY in the properties file");
        }

        if (Strings.isNullOrEmpty(properties.getProperty(PropertiesModule.SAUCE_USER))) {
            throw new IllegalArgumentException("To run on Sauce Labs, define SAUCE_USER in the properties file");
        }

        // To override the app in the yaml.
        if (!Strings.isNullOrEmpty(CommandLineArguments.ARGUMENTS.app())) {
            CommandLineArguments.ARGUMENTS.capabilities().setCapability("app", "sauce-storage:" + CommandLineArguments.ARGUMENTS.app());
        }

        CommandLineArguments.ARGUMENTS.capabilities().setCapability("username", properties.getProperty(PropertiesModule.SAUCE_USER));
        CommandLineArguments.ARGUMENTS.capabilities().setCapability("access-key", properties.getProperty(PropertiesModule.SAUCE_KEY));
    }

}
