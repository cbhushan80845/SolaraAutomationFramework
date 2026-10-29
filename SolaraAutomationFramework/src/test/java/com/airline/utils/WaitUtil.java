
package com.airline.utils;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitUtil {

    private final WebDriverWait wait;

    public WaitUtil(WebDriver driver, int timeoutInSeconds) {
        if (driver == null) {
            throw new IllegalArgumentException(
                    "WebDriver cannot be null");
        }

        if (timeoutInSeconds <= 0) {
            throw new IllegalArgumentException(
                    "Wait timeout must be greater than zero");
        }

        this.wait = new WebDriverWait(
                driver, Duration.ofSeconds(timeoutInSeconds));
    }

    public WebElement waitForElementVisible(By locator) {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForElementClickable(By locator) {
        return wait.until(
                ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement waitForElementClickableByWebElement(
            WebElement element) {
        return wait.until(
                ExpectedConditions.elementToBeClickable(element));
    }

    public List<WebElement> waitForElementsVisible(By locator) {
        return wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public void waitForFrameToBeAvailableAndSwitch(By locator) {
        wait.until(
                ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
    }
}
