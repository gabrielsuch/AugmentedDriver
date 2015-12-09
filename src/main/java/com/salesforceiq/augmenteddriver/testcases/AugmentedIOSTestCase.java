package com.salesforceiq.augmenteddriver.testcases;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.salesforceiq.augmenteddriver.annotations.GuiceModules;
import com.salesforceiq.augmenteddriver.asserts.AugmentedAssert;
import com.salesforceiq.augmenteddriver.mobile.ios.*;
import com.salesforceiq.augmenteddriver.mobile.ios.pageobjects.IOSPageContainerObject;
import com.salesforceiq.augmenteddriver.mobile.ios.pageobjects.IOSPageObject;
import com.salesforceiq.augmenteddriver.mobile.ios.pageobjects.IOSPageObjectActions;
import com.salesforceiq.augmenteddriver.mobile.ios.pageobjects.IOSPageObjectActionsInterface;
import com.salesforceiq.augmenteddriver.modules.AugmentedIOSDriverModule;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;

@GuiceModules({PropertiesModule.class, AugmentedIOSDriverModule.class})
public class AugmentedIOSTestCase extends AugmentedBaseTestCase implements IOSPageObjectActionsInterface {
    private static final Logger LOG = LoggerFactory.getLogger(AugmentedIOSTestCase.class);

    private AugmentedIOSDriver driver;
    private AugmentedIOSFunctions augmentedIOSFunctions;

    @Inject
    private AugmentedIOSFunctionsFactory augmentedIOSFunctionsFactory;

    @Inject
    private AugmentedIOSDriverProvider augmentedIOSDriverProvider;

    @Inject
    private IOSPageObjectActions iosPageObjectActions;

    @Override
    protected Logger logger() {
        return LOG;
    }

    @Override
    protected void initializeDriver() throws MalformedURLException {
        this.driver = new AugmentedIOSDriver(remoteAddress, capabilities, augmentedIOSFunctions);
        this.augmentedIOSFunctions = augmentedIOSFunctionsFactory.create(driver);

        driver.setAugmentedFunctions(augmentedIOSFunctions);
        augmentedIOSDriverProvider.set(driver);

        this.sessionId = driver.getSessionId().toString();
    }

    @Override
    protected void closeDriver() {
        if (driver == null) return;
        driver.close();
    }

    @Override
    public AugmentedIOSDriver driver() {
        return Preconditions.checkNotNull(driver);
    }

    @Override
    public AugmentedIOSFunctions augmented() {
        return Preconditions.checkNotNull(augmentedIOSFunctions);
    }

    @Override
    public <T extends IOSPageObject> T get(Class<T> clazz) {
        return iosPageObjectActions.get(clazz);
    }

    @Override
    public <T extends IOSPageContainerObject> T get(Class<T> clazz, AugmentedIOSElement container) {
        return iosPageObjectActions.get(clazz, container);
    }

    @Override
    public void assertElementIsPresentAfter(By by, int timeoutInSeconds) {
        AugmentedAssert.assertElementIsPresentAfter(augmented(), by, timeoutInSeconds);
    }

    @Override
    public void assertElementIsPresent(By by) {
        AugmentedAssert.assertElementIsPresentAfter(augmented(), by, waitTimeInSeconds());
    }

    @Override
    public void assertElementIsVisibleAfter(By by, int timeoutInSeconds) {
        AugmentedAssert.assertElementIsVisibleAfter(augmented(), by, timeoutInSeconds);
    }

    @Override
    public void assertElementIsVisible(By by) {
        AugmentedAssert.assertElementIsVisibleAfter(augmented(), by, waitTimeInSeconds());
    }

    @Override
    public void assertElementIsClickableAfter(By by, int timeoutInSeconds) {
        AugmentedAssert.assertElementIsClickableAfter(augmented(), by, timeoutInSeconds);
    }

    @Override
    public void assertElementIsClickable(By by) {
        AugmentedAssert.assertElementIsClickableAfter(augmented(), by, waitTimeInSeconds());
    }

    @Override
    public void assertElementContainsAfter(By by, String text, int timeoutInSeconds) {
        AugmentedAssert.assertElementContainsAfter(augmented(), by, text, timeoutInSeconds);
    }

    @Override
    public void assertElementContains(By by, String text) {
        AugmentedAssert.assertElementContainsAfter(augmented(), by, text, waitTimeInSeconds());
    }

    @Override
    public void assertElementIsNotClickableAfter(By by, int timeoutInSeconds) {
        AugmentedAssert.assertElementIsNotClickableAfter(augmented(), by, timeoutInSeconds);
    }

    @Override
    public void assertElementIsNotClickable(By by) {
        AugmentedAssert.assertElementIsNotClickableAfter(augmented(), by, waitTimeInSeconds());
    }

    @Override
    public void assertElementIsNotVisibleAfter(By by, int timeoutInSeconds) {
        AugmentedAssert.assertElementIsNotVisibleAfter(augmented(), by, timeoutInSeconds);
    }

    @Override
    public void assertElementIsNotVisible(By by) {
        AugmentedAssert.assertElementIsNotVisibleAfter(augmented(), by, waitTimeInSeconds());
    }

    @Override
    public void assertElementIsNotPresentAfter(By by, int timeoutInSeconds) {
        AugmentedAssert.assertElementIsNotPresentAfter(augmented(), by, timeoutInSeconds);
    }

    @Override
    public void assertElementIsNotPresent(By by) {
        AugmentedAssert.assertElementIsNotPresentAfter(augmented(), by, waitTimeInSeconds());
    }

}
