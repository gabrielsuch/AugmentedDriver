package com.salesforceiq.augmenteddriver.mobile.ios;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.salesforceiq.augmenteddriver.mobile.android.AugmentedAndroidFunctionsFactory;
import org.openqa.selenium.*;

import java.util.List;

public class AugmentedIOSElement implements WebElement {
    private final WebElement webElement;
    private final AugmentedIOSFunctions augmentedFunctions;

    @Inject
    public AugmentedIOSElement(@Assisted WebElement webElement,
                               AugmentedIOSFunctionsFactory augmentedFunctions) {
        this.webElement = Preconditions.checkNotNull(webElement);
        this.augmentedFunctions = Preconditions.checkNotNull(augmentedFunctions.create(webElement));
    }

    public AugmentedIOSFunctions augmented() {
        return augmentedFunctions;
    }

    public WebElement webElement() {
        return webElement;
    }

    @Override
    public void click() {
        webElement.click();
    }

    @Override
    public void submit() {
        webElement.submit();
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        webElement.sendKeys(keysToSend);
    }

    @Override
    public void clear() {
        webElement.clear();
    }

    @Override
    public String getTagName() {
        return webElement.getTagName();
    }

    @Override
    public String getAttribute(String name) {
        return webElement.getAttribute(name);
    }

    @Override
    public boolean isSelected() {
        return webElement.isSelected();
    }

    @Override
    public boolean isEnabled() {
        return webElement.isEnabled();
    }

    @Override
    public String getText() {
        return webElement.getText();
    }

    @Override
    public List<WebElement> findElements(By by) {
        return webElement.findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return webElement.findElement(by);
    }

    @Override
    public boolean isDisplayed() {
        return webElement.isDisplayed();
    }

    @Override
    public Point getLocation() {
        return webElement.getLocation();
    }

    @Override
    public Dimension getSize() {
        return webElement.getSize();
    }

    @Override
    public String getCssValue(String propertyName) {
        return webElement.getCssValue(propertyName);
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return webElement.getScreenshotAs(target);
    }
}
