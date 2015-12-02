package com.salesforceiq.augmenteddriver.testcases;

import com.salesforceiq.augmenteddriver.asserts.AugmentedAssert;
import com.salesforceiq.augmenteddriver.annotations.GuiceModules;
import com.salesforceiq.augmenteddriver.integrations.IntegrationFactory;
import com.salesforceiq.augmenteddriver.modules.AugmentedWebDriverModule;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import com.salesforceiq.augmenteddriver.util.CommandLineArguments;
import com.salesforceiq.augmenteddriver.util.Util;
import com.salesforceiq.augmenteddriver.web.*;
import com.salesforceiq.augmenteddriver.web.pageobjects.*;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;

/**
 * Base test class for all Web tests.
 */
@GuiceModules({PropertiesModule.class, AugmentedWebDriverModule.class})
public class AugmentedWebTestCase extends AugmentedBaseTestCase implements WebPageObjectActionsInterface {
    private static final Logger LOG = LoggerFactory.getLogger(AugmentedWebTestCase.class);

    private AugmentedWebDriver driver;

    @Inject
    private AugmentedWebDriverProvider augmentedWebDriverProvider;

    @Inject
    private AugmentedWebFunctionsFactory augmentedWebFunctionsFactory;

    private AugmentedWebFunctions augmentedWebFunctions;

    @Inject
    private WebPageObjectActions webPageObjectActions;

    @Inject
    private IntegrationFactory integrations;

    @Inject
    private CommandLineArguments arguments;

    @Named(PropertiesModule.REMOTE_ADDRESS)
    @Inject
    private String remoteAddress;

    @Inject
    private DesiredCapabilities capabilities;

    @Override
    public AugmentedWebDriver driver() {
        return Preconditions.checkNotNull(driver);
    }

    @Override
    public AugmentedWebFunctions augmented() {
        return Preconditions.checkNotNull(augmentedWebFunctions);
    }

    @Override
    public <T extends WebPageObject> T get(Class<T> clazz) {
        return webPageObjectActions.get(clazz);
    }

    @Override
    public <T extends WebPageContainerObject> T get(Class<T> clazz, AugmentedWebElement container) {
        return webPageObjectActions.get(clazz, container);
    }

    /**
     * <p>
     *     IMPORTANT, the session of the driver is set after the driver is initialized.
     * </p>
     */
    @Before
    public void setUp() {
        Preconditions.checkNotNull(augmentedWebDriverProvider);
        Preconditions.checkNotNull(augmentedWebFunctionsFactory);
        Preconditions.checkNotNull(integrations);
        Preconditions.checkNotNull(arguments);
        Preconditions.checkNotNull(webPageObjectActions);
        Preconditions.checkNotNull(remoteAddress);
        Preconditions.checkNotNull(capabilities);

        long start = System.currentTimeMillis();
        LOG.info("Creating AugmentedWebDriver");
        try {
            driver = new AugmentedWebDriver(remoteAddress, capabilities);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Check your addresses on the properties file", e);
        }
        augmentedWebFunctions = augmentedWebFunctionsFactory.create(driver);
        driver.setAugmentedFunctions(augmentedWebFunctions);
        augmentedWebDriverProvider.set(driver);
        LOG.info("AugmentedWebDriver created in " + Util.TO_PRETTY_FORNAT.apply(System.currentTimeMillis() - start));


        sessionId = driver.getSessionId().toString();
        if (integrations.isSauceLabsEnabled()) {
            integrations.sauceLabs().jobName(getFullTestName(), sessionId);
            integrations.sauceLabs().buildName(getUniqueId(), sessionId);
        }
        if (integrations.isTeamCityEnabled() && integrations.isSauceLabsEnabled()) {
            integrations.teamCity().printSessionId(getFullTestName(), sessionId);
        }
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Override
    public void assertElementIsPresentAfter(By by, int timeoutInSeconds) {
        AugmentedAssert.assertElementIsPresentAfter(augmentedWebFunctions, by, timeoutInSeconds);
    }

    @Override
    public void assertElementIsPresent(By by) {
        AugmentedAssert.assertElementIsPresentAfter(augmentedWebFunctions, by, waitTimeInSeconds());
    }

    @Override
    public void assertElementIsVisibleAfter(By by, int timeoutInSeconds) {
        AugmentedAssert.assertElementIsVisibleAfter(augmentedWebFunctions, by, timeoutInSeconds);
    }

    @Override
    public void assertElementIsVisible(By by) {
        AugmentedAssert.assertElementIsVisibleAfter(augmentedWebFunctions, by, waitTimeInSeconds());
    }

    @Override
    public void assertElementIsClickableAfter(By by, int timeoutInSeconds) {
        AugmentedAssert.assertElementIsClickableAfter(augmentedWebFunctions, by, timeoutInSeconds);
    }

    @Override
    public void assertElementIsClickable(By by) {
        AugmentedAssert.assertElementIsClickableAfter(augmentedWebFunctions, by, waitTimeInSeconds());
    }

    @Override
    public void assertElementContainsAfter(By by, String text, int timeoutInSeconds) {
        AugmentedAssert.assertElementContainsAfter(augmentedWebFunctions, by, text, timeoutInSeconds);
    }

    @Override
    public void assertElementContains(By by, String text) {
        AugmentedAssert.assertElementContainsAfter(augmentedWebFunctions, by, text, waitTimeInSeconds());
    }

    @Override
    public void assertElementIsNotClickableAfter(By by, int timeoutInSeconds) {
        AugmentedAssert.assertElementIsNotClickableAfter(augmentedWebFunctions, by, timeoutInSeconds);
    }

    @Override
    public void assertElementIsNotClickable(By by) {
        AugmentedAssert.assertElementIsNotClickableAfter(augmentedWebFunctions, by, waitTimeInSeconds());
    }

    @Override
    public void assertElementIsNotVisibleAfter(By by, int timeoutInSeconds) {
        AugmentedAssert.assertElementIsNotVisibleAfter(augmentedWebFunctions, by, timeoutInSeconds);
    }

    @Override
    public void assertElementIsNotVisible(By by) {
        AugmentedAssert.assertElementIsNotVisibleAfter(augmentedWebFunctions, by, waitTimeInSeconds());
    }

    @Override
    public void assertElementIsNotPresentAfter(By by, int timeoutInSeconds) {
        AugmentedAssert.assertElementIsNotPresentAfter(augmentedWebFunctions, by, timeoutInSeconds);
    }

    @Override
    public void assertElementIsNotPresent(By by) {
        AugmentedAssert.assertElementIsNotPresentAfter(augmentedWebFunctions, by, waitTimeInSeconds());
    }
}
