package com.salesforceiq.augmenteddriver.modules;


import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.name.Names;
import com.salesforceiq.augmenteddriver.integrations.SauceLabsIntegration;
import com.salesforceiq.augmenteddriver.util.CommandLineArguments;
import com.salesforceiq.augmenteddriver.util.saucelabs.SauceCommandLineArguments;
import com.saucelabs.saucerest.SauceREST;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SaucelabsModule extends AbstractIntegrationModule {

    private static final Logger LOG = LoggerFactory.getLogger(SaucelabsModule.class);

    public static final String SAUCE_USER = "SAUCE_USER";
    public static final String SAUCE_KEY = "SAUCE_KEY";
    public static final String SAUCE_ADDRESS = "SAUCE_ADDRESS";

    private static final Map<String, String> defaultProperties = new HashMap<String, String>() {
        {
            put(SAUCE_ADDRESS, "http://ondemand.saucelabs.com:80/wd/hub");
            put(SAUCE_KEY, "");
            put(SAUCE_USER, "");
        }
    };

    @Override
    protected void configureActions() {
        if (!CommandLineArguments.ARGUMENTS.sauce()) {
            LOG.info("Sauce is false on properties. Ignoring module.");
            return;
        }

        Properties properties = new Properties();

        defaultProperties.entrySet()
                .stream()
                .forEach(entry -> properties.setProperty(entry.getKey(), entry.getValue()));

        String path = SauceCommandLineArguments.ARGUMENTS == null
                ? CommandLineArguments.DEFAULT_CONFIG
                : SauceCommandLineArguments.ARGUMENTS.conf();

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

        if (SauceCommandLineArguments.ARGUMENTS == null) {
            SauceCommandLineArguments.initialize(properties);
        }

        setSauceProperties(properties);

        String sauceKey = properties.getProperty(SAUCE_KEY);
        String sauceUser = properties.getProperty(SAUCE_USER);

        Preconditions.checkArgument(!Strings.isNullOrEmpty(sauceKey), String.format("Set %s in the properties file", SAUCE_KEY));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(sauceUser), String.format("Set %s in the properties file", SAUCE_USER));

        bind(SauceREST.class).toInstance(new SauceREST(sauceUser, sauceKey));
        bind(SauceCommandLineArguments.class).toInstance(SauceCommandLineArguments.ARGUMENTS);
        bindIntegration().to(SauceLabsIntegration.class);
    }

    /**
     * Hack to set the sauce key and sauce user into the capabilities.
     */
    private void setSauceProperties(Properties properties) {
        bind(String.class).annotatedWith(Names.named(PropertiesModule.REMOTE_ADDRESS)).toInstance(properties.getProperty(SAUCE_ADDRESS));

        if (Strings.isNullOrEmpty(properties.getProperty(SAUCE_KEY))) {
            throw new IllegalArgumentException("To run on Sauce Labs, define SAUCE_KEY in the properties file");
        }

        if (Strings.isNullOrEmpty(properties.getProperty(SAUCE_USER))) {
            throw new IllegalArgumentException("To run on Sauce Labs, define SAUCE_USER in the properties file");
        }

        // To override the app in the yaml.
        if (!Strings.isNullOrEmpty(CommandLineArguments.ARGUMENTS.app())) {
            CommandLineArguments.ARGUMENTS.capabilities().setCapability("app", "sauce-storage:" + CommandLineArguments.ARGUMENTS.app());
        }

        CommandLineArguments.ARGUMENTS.capabilities().setCapability("username", properties.getProperty(SAUCE_USER));
        CommandLineArguments.ARGUMENTS.capabilities().setCapability("access-key", properties.getProperty(SAUCE_KEY));
    }

}
