package com.salesforceiq.augmenteddriver.testcases;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.salesforceiq.augmenteddriver.asserts.AugmentedAssertInterface;
import com.salesforceiq.augmenteddriver.integrations.Integration;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import com.salesforceiq.augmenteddriver.runners.AugmentedJUnitRunner;
import com.salesforceiq.augmenteddriver.util.CommandLineArguments;
import com.salesforceiq.augmenteddriver.util.Util;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;


/**
 * Base Test Case for all tests.
 *
 * <p>
 *     No one should extend from this, AugmentedWebTestCase or the like should be used.
 * </p>
 */
@RunWith(AugmentedJUnitRunner.class)
public abstract class AugmentedBaseTestCase implements AugmentedAssertInterface {

    @Rule
    public TestName testName = new TestName();

    @Inject
    @Named(PropertiesModule.WAIT_IN_SECONDS)
    private String waitTimeInSeconds;

    @Inject
    @Named(PropertiesModule.UNIQUE_ID)
    private String uniqueId;

    @Named(PropertiesModule.REMOTE_ADDRESS)
    @Inject(optional = true)
    protected String remoteAddress;

    @Named(PropertiesModule.LOCAL_ADDRESS)
    @Inject
    protected String localAddress;

    @Inject
    protected DesiredCapabilities capabilities;

    @Inject
    protected CommandLineArguments arguments;

    @Inject(optional = true)
    protected Set<Integration> integrations = new HashSet<>();

    /**
     * Hack, but there is no way to get the session Id in other way.
     */
    protected String sessionId;

    protected abstract Logger logger();

    protected abstract void initializeDriver() throws MalformedURLException;

    protected abstract void closeDriver();

    /**
     * <p>
     *     IMPORTANT, the session of the driver is set after the driver is initialized.
     * </p>
     */
    @Before
    public void setUp() {
        logIntegrationsAvailable();

        if (remoteAddress == null) {
            logger().info("No Remote Address defined, using local address");
            remoteAddress = localAddress;
        }

        long start = System.currentTimeMillis();
        logger().info("Creating Augmented Driver");

        try {
            initializeDriver();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Check your addresses on the properties file", e);
        }

        logger().info("Augmented Driver created in " + Util.TO_PRETTY_FORNAT.apply(System.currentTimeMillis() - start));

        integrations
                .stream()
                .filter(each -> each.isEnabled())
                .forEach(each -> {
                    each.jobName(getFullTestName(), sessionId);
                    each.buildName(getUniqueId(), sessionId);
                });
    }

    @After
    public void tearDown() {
        closeDriver();
    }

    private void logIntegrationsAvailable() {
        logger().info(integrations.size() + " Integration(s) available:");

        integrations
                .stream()
                .forEach(each -> logger().info(each.getClass() + " - Enabled: " + each.isEnabled()));
    }

    /**
     * @return the wait time in seconds defined in the com.salesforceiq.augmenteddriver.properties (or 30 by default)
     */
    protected int waitTimeInSeconds() {
        return Integer.valueOf(waitTimeInSeconds);
    }

    /**
     * @return Unique 10 digit Id for the run (tests will share it in the suite, or if a same test is running
     *         repeated times.
     */
    protected String getUniqueId() {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(uniqueId));
        return uniqueId;
    }

    /**
     * @return Unique name of the test, including the unique id, the class and the test name
     */
    protected String getFullTestName() {
        return String.format("%s:%s:%s", getUniqueId(), Util.shortenClass(this.getClass()), testName.getMethodName());
    }

    /**
     * Rule for executing code after the test finished, whether it failed or not.
     *
     * <p>
     *     IMPORTANT. It's implemented here since many if the data is generated at runtime.
     * </p>
     */
    @Rule
    public TestWatcher testWatcher = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            integrations
                    .stream()
                    .filter(each -> each.isEnabled())
                    .forEach(each -> each.testPassed(false, sessionId));
        }

        @Override
        protected void succeeded(Description description) {
            integrations
                    .stream()
                    .filter(each -> each.isEnabled())
                    .forEach(each -> each.testPassed(true, sessionId));
        }
    };

}
