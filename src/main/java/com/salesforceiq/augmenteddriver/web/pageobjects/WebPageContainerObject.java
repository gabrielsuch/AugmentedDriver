package com.salesforceiq.augmenteddriver.web.pageobjects;

import com.salesforceiq.augmenteddriver.util.PageObject;
import com.salesforceiq.augmenteddriver.util.PageObjectAssertionsInterface;
import com.salesforceiq.augmenteddriver.web.AugmentedWebDriver;
import com.salesforceiq.augmenteddriver.web.AugmentedWebElement;
import com.salesforceiq.augmenteddriver.web.AugmentedWebFunctions;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page Object for WebPages with a container.
 */
public abstract class WebPageContainerObject implements  WebPageObjectActionsInterface, PageObjectAssertionsInterface, PageObject {
    private static final Logger LOG = LoggerFactory.getLogger(WebPageObject.class);

    /**
     * Important we use a Provider, since we need the driver to be initialized when the first test starts to run
     * not at creation time, like Guice wants.
     */
    @Inject
    private Provider<AugmentedWebDriver> driverProvider;

    @Inject
    private WebPageObjectActions webPageObjectActions;

    private AugmentedWebElement container;

    @Override
    public <T extends WebPageObject> T get(Class<T> clazz) {
        return webPageObjectActions.get(clazz);
    }

    @Override
    public <T extends WebPageContainerObject> T get(Class<T> clazz, AugmentedWebElement container) {
        return webPageObjectActions.get(clazz, container);
    }

    @Override
    public void assertPresent() {
        if (visibleBy().isPresent()) {
            container().augmented().findElementsVisible(visibleBy().get());
        }
    }

    @Override
    public AugmentedWebDriver driver() {
        return driverProvider.get();
    }

    @Override
    public AugmentedWebFunctions augmented() {
        return driverProvider.get().augmented();
    }

    /**
     * DO NOT USE
     */
    void setContainer(AugmentedWebElement container) {
        this.container = container;
    }

    public AugmentedWebElement container() {
        return container;
    }
}
