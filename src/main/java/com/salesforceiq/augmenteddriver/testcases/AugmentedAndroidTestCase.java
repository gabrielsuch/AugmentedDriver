package com.salesforceiq.augmenteddriver.testcases;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.salesforceiq.augmenteddriver.annotations.GuiceModules;
import com.salesforceiq.augmenteddriver.asserts.AugmentedAssert;
import com.salesforceiq.augmenteddriver.mobile.android.*;
import com.salesforceiq.augmenteddriver.mobile.android.pageobjects.AndroidPageContainerObject;
import com.salesforceiq.augmenteddriver.mobile.android.pageobjects.AndroidPageObject;
import com.salesforceiq.augmenteddriver.mobile.android.pageobjects.AndroidPageObjectActions;
import com.salesforceiq.augmenteddriver.mobile.android.pageobjects.AndroidPageObjectActionsInterface;
import com.salesforceiq.augmenteddriver.modules.AugmentedAndroidDriverModule;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;

/**
 * Base test class for all Android tests.
 */
@GuiceModules({PropertiesModule.class, AugmentedAndroidDriverModule.class})
public class AugmentedAndroidTestCase extends AugmentedBaseTestCase implements AndroidPageObjectActionsInterface {
    private static final Logger LOG = LoggerFactory.getLogger(AugmentedAndroidTestCase.class);

    private AugmentedAndroidDriver driver;
    private AugmentedAndroidFunctions augmentedAndroidFunctions;

    @Inject
    private AugmentedAndroidDriverProvider augmentedAndroidDriverProvider;

    @Inject
    private AugmentedAndroidFunctionsFactory augmentedAndroidFunctionsFactory;

    @Inject
    private AndroidPageObjectActions androidPageObjectActions;

    @Override
    protected Logger logger() {
        return LOG;
    }

    @Override
    protected void initializeDriver() throws MalformedURLException {
        this.driver = new AugmentedAndroidDriver(remoteAddress, capabilities, augmentedAndroidFunctions);
        this.augmentedAndroidFunctions= augmentedAndroidFunctionsFactory.create(driver);

        driver.setAugmentedFunctions(augmentedAndroidFunctions);
        augmentedAndroidDriverProvider.set(driver);

        this.sessionId = driver.getSessionId().toString();
    }

    @Override
    protected void closeDriver() {
        if (driver == null) return;
        driver.close();
    }

    @Override
    public AugmentedAndroidDriver driver() {
        return Preconditions.checkNotNull(driver);
    }

    @Override
    public AugmentedAndroidFunctions augmented() {
        return Preconditions.checkNotNull(augmentedAndroidFunctions);
    }

    @Override
    public <T extends AndroidPageObject> T get(Class<T> clazz) {
        return androidPageObjectActions.get(clazz);
    }

    @Override
    public <T extends AndroidPageContainerObject> T get(Class<T> clazz, AugmentedAndroidElement container) {
        return androidPageObjectActions.get(clazz, container);
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

