package com.salesforceiq.augmenteddriver.modules;

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
    public static final String LOCAL_ADDRESS = "LOCAL_ADDRESS";
    public static final String CAPABILITIES = "CAPABILITIES";

    private static final String ID = Util.getRandomAsString();

    /**
     * For now all the properties are defined here.
     */
    private static final Map<String, String> defaultProperties = new HashMap<String, String>() {
        {
            put(LOCAL_ADDRESS, "http://127.0.0.1:7777/wd/hub");
            put(WAIT_IN_SECONDS, "30");
            put(TEAM_CITY_INTEGRATION, "false");
            put(REPORTING, "false");
            put(MAX_RETRIES, "2");
        }
    };

    @Override
    protected void configure() {
        Properties properties = new Properties();
        defaultProperties.entrySet()
                .stream()
                .forEach(entry -> properties.setProperty(entry.getKey(), entry.getValue()));

        String path = CommandLineArguments.ARGUMENTS == null ? CommandLineArguments.DEFAULT_CONFIG : CommandLineArguments.ARGUMENTS.conf();
        Path propertiesPath = Paths.get(path);

        if (Files.exists(propertiesPath)) {
            try {
                properties.load(new FileInputStream(propertiesPath.toFile()));
            } catch (IOException e) {
                throw new IllegalStateException("Failed to load properties file " + propertiesPath, e);
            }
        } else {
            throw new IllegalArgumentException("Properties file does not exist " + propertiesPath);
        }

        if (properties.get(CAPABILITIES) != null) {
            CommandLineArguments.initialize(properties);
        }

        if (CommandLineArguments.ARGUMENTS == null) {
            throw new IllegalStateException("Capabilities were not loaded. Please set on properties file or command line args.");
        }

        if (!CommandLineArguments.ARGUMENTS.sauce() && (properties.get("SAUCE") == null)) {
            properties.setProperty(PropertiesModule.REMOTE_ADDRESS, properties.getProperty(PropertiesModule.LOCAL_ADDRESS));
        }

        Names.bindProperties(binder(), properties);
        bind(DesiredCapabilities.class).toInstance(CommandLineArguments.ARGUMENTS.capabilities());
        bind(String.class).annotatedWith(Names.named(PropertiesModule.UNIQUE_ID)).toInstance(ID);
    }

}
