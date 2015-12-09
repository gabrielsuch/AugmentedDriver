package com.salesforceiq.augmenteddriver.testcases;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.salesforceiq.augmenteddriver.annotations.GuiceModules;
import com.salesforceiq.augmenteddriver.asserts.AugmentedAssert;
import com.salesforceiq.augmenteddriver.modules.AugmentedWebDriverModule;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import com.salesforceiq.augmenteddriver.web.*;
import com.salesforceiq.augmenteddriver.web.pageobjects.WebPageContainerObject;
import com.salesforceiq.augmenteddriver.web.pageobjects.WebPageObject;
import com.salesforceiq.augmenteddriver.web.pageobjects.WebPageObjectActions;
import com.salesforceiq.augmenteddriver.web.pageobjects.WebPageObjectActionsInterface;
import org.openqa.selenium.By;
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
    private AugmentedWebFunctions augmentedWebFunctions;

    @Inject
    private AugmentedWebDriverProvider augmentedWebDriverProvider;

    @Inject
    private AugmentedWebFunctionsFactory augmentedWebFunctionsFactory;

    @Inject
    private WebPageObjectActions webPageObjectActions;

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

    @Override
    protected Logger logger() {
        return LOG;
    }

    @Override
    protected void initializeDriver() throws MalformedURLException {
        this.driver = new AugmentedWebDriver(remoteAddress, capabilities);
        this.augmentedWebFunctions = augmentedWebFunctionsFactory.create(driver);

        driver.setAugmentedFunctions(augmentedWebFunctions);
        augmentedWebDriverProvider.set(driver);

        this.sessionId = driver.getSessionId().toString();
    }

    @Override
    public void closeDriver() {
        if (driver == null) return;
        driver.close();
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
