package com.salesforceiq.augmenteddriver.util;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.esotericsoftware.yamlbeans.YamlException;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * For parsing command line arguments.
 *
 * <p>
 *     All the command line arguments should be here.
 * </p>
 */
public class CommandLineArguments {

    private static Class<?> hackClass;
    public static CommandLineArguments ARGUMENTS;

    public static CommandLineArguments initialize(String[] args) {
        CommandLineArguments result = new CommandLineArguments();
        JCommander jCommander = new JCommander();
        jCommander.setAcceptUnknownOptions(true);
        jCommander.addObject(result);
        jCommander.parse(args);
        ARGUMENTS = result;
        return ARGUMENTS;
    }

    public static CommandLineArguments initializeForCapabilities(String path) {
        CommandLineArguments result = new CommandLineArguments();
        result.capabilities = new CapabilitiesConverter().convert(path);
        ARGUMENTS = result;
        return ARGUMENTS;
    }

    public Class<?> clazz() {
        Preconditions.checkNotNull(ARGUMENTS, "Call CommandLineArguments#intialize first");
        return ARGUMENTS.clazz;
    }

    public Method test() {
        Preconditions.checkNotNull(ARGUMENTS, "Call CommandLineArguments#intialize first");
        return ARGUMENTS.test;
    }

    public List<String> suites() {
        Preconditions.checkNotNull(ARGUMENTS, "Call CommandLineArguments#intialize first");
        return Arrays.asList(ARGUMENTS.suites.split(","));
    }

    public String suitesPackage() {
        Preconditions.checkNotNull(ARGUMENTS, "Call CommandLineArguments#intialize first");
        return ARGUMENTS.suitesPackage;
    }

    public int quantity() {
        Preconditions.checkNotNull(ARGUMENTS, "Call CommandLineArguments#intialize first");
        return ARGUMENTS.quantity;
    }

    public int parallel() {
        Preconditions.checkNotNull(ARGUMENTS, "Call CommandLineArguments#intialize first");
        return ARGUMENTS.parallel;
    }

    public int timeoutInMinutes() {
        Preconditions.checkNotNull(ARGUMENTS, "Call CommandLineArguments#intialize first");
        return ARGUMENTS.timeoutInMinutes;
    }

    public boolean sauce() {
        Preconditions.checkNotNull(ARGUMENTS, "Call CommandLineArguments#intialize first");
        return ARGUMENTS.sauce;
    }

    public boolean quarantine() {
        Preconditions.checkNotNull(ARGUMENTS, "Call CommandLineArguments#intialize first");
        return ARGUMENTS.quarantine;
    }

    public DesiredCapabilities capabilities() {
        Preconditions.checkNotNull(ARGUMENTS, "Call CommandLineArguments#intialize first");
        return ARGUMENTS.capabilities;
    }

    public String conf() {
        Preconditions.checkNotNull(ARGUMENTS, "Call CommandLineArguments#intialize first");
        return ARGUMENTS.conf;
    }

    public String app() {
        Preconditions.checkNotNull(ARGUMENTS, "Call CommandLineArguments#intialize first");
        return ARGUMENTS.app;
    }

    @Parameter(names = "-clazz", description = "Class to run", converter = ClassConverter.class)
    private Class<?> clazz;

    @Parameter(names = "-test", description = "Test to run", converter = MethodConverter.class)
    private Method test;

    @Parameter(names = "-suites", description = "Comma delimited suites to run")
    private String suites;

    @Parameter(names = "-suitesPackage", description = "Base package to grab the tests")
    private String suitesPackage;

    @Parameter(names = "-quantity", description = "How many times the test is going to run")
    private Integer quantity = 1;

    @Parameter(names = "-parallel", description = "How many tests in parallel are going to run")
    private Integer parallel = 1;

    @Parameter(names = "-timeoutInMinutes", description = "Timeout for tests to finish")
    private Integer timeoutInMinutes = 20;

    @Parameter(names = "-sauce", description = "Whether to run tests on SauceLabs or not")
    private boolean sauce = false;

    @Parameter(names = "-quarantine", description = "Run quarantined tests")
    private boolean quarantine = false;

    @Parameter(names = "-capabilities", description = "Path to the YAML with the desired capabilities", converter = CapabilitiesConverter.class)
    private DesiredCapabilities capabilities;

    @Parameter(names = "-conf", description = "Path to the properties file, conf/augmented.properties by default")
    private String conf = "conf/augmented.properties";

    @Parameter(names = "-app", description = "Path to file to use as app (IOS) or apk (Android)")
    private String app = "";

    public static class ClassConverter implements IStringConverter<Class<?>> {
        @Override
        public Class<?> convert(String value) {
            try {
                Class<?> theClass = Class.forName(value);
                CommandLineArguments.hackClass = theClass;
                return theClass;
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException(String.format("Parameter -clazz should be a class, got : %s", value));
            }
        }
    }

    public static class CapabilitiesConverter implements IStringConverter<DesiredCapabilities> {
        @Override
        public DesiredCapabilities convert(String path) {
            Preconditions.checkArgument(!Strings.isNullOrEmpty(path), "-capabilities should contain an argument");
            Path yamlPath = Paths.get(path);
            if (!Files.exists(yamlPath)) {
                throw new IllegalArgumentException(String.format("File %s does not exist", yamlPath));
            }
            try {
                return YamlCapabilitiesConverter.convert(yamlPath);
            } catch (YamlException e) {
                throw new IllegalArgumentException(String.format("File %s cannot be parsed as YAML file", yamlPath), e);
            }
        }
    }

    public static class MethodConverter implements IStringConverter<Method> {
        @Override
        public Method convert(String value) {
            try {
                Preconditions.checkArgument(hackClass != null, "Need to declare -clazz argument first");
                return hackClass.getMethod(value);
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException(String.format("Parameter -test should be a method of %s, got %s", hackClass.getCanonicalName(), value));
            }
        }
    }
}
