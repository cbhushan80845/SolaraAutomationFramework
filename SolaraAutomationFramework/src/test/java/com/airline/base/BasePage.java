
package com.airline.base;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.airline.utils.WaitUtil;

public class BasePage {

    protected final WebDriver driver;
    protected final WaitUtil waitUtil;
    protected final int explicitWait;

    public BasePage(WebDriver driver, int explicitWait) {
        if (driver == null) {
            throw new IllegalArgumentException("WebDriver cannot be null");
        }

        if (explicitWait <= 0) {
            throw new IllegalArgumentException(
                    "Explicit wait must be greater than zero");
        }

        this.driver = driver;
        this.explicitWait = explicitWait;
        this.waitUtil = new WaitUtil(driver, explicitWait);
    }

    protected void click(By locator) {
        waitUtil.waitForElementClickable(locator).click();
    }

    protected void clickWithJsFallback(By locator) {
        WebElement element = waitUtil.waitForElementClickable(locator);

        try {
            element.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            WebElement refreshedElement =
                    waitUtil.waitForElementClickable(locator);

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].click();", refreshedElement);
        }
    }

    protected void clicks(By locator) {
        int attempts = 0;

        while (attempts < 2) {
            try {
                waitUtil.waitForElementClickable(locator).click();
                return;
            } catch (StaleElementReferenceException e) {
                attempts++;
            }
        }

        throw new StaleElementReferenceException(
                "Element remained stale after retry: " + locator);
    }

    protected void enterText(By locator, String value) {
        WebElement element = waitUtil.waitForElementVisible(locator);
        element.clear();
        element.sendKeys(value);
    }

    protected String getText(By locator) {
        return waitUtil.waitForElementVisible(locator).getText();
    }

    protected WebElement getElement(By locator) {
        return waitUtil.waitForElementVisible(locator);
    }

    protected boolean isDisplayed(By locator) {
        return waitUtil.waitForElementVisible(locator).isDisplayed();
    }

    protected void switchToFrame(By locator) {
        waitUtil.waitForFrameToBeAvailableAndSwitch(locator);
    }

    protected void clickByWebelement(WebElement element) {
        waitUtil.waitForElementClickableByWebElement(element).click();
    }

    protected List<WebElement> getElements(By locator) {
        return waitUtil.waitForElementsVisible(locator);
    }

    protected String getCurrentWindow() {
        return driver.getWindowHandle();
    }

    protected void switchToNewWindow(String mainWindow) {
        Set<String> windows = driver.getWindowHandles();

        for (String window : windows) {
            if (!window.equals(mainWindow)) {
                driver.switchTo().window(window);
                return;
            }
        }

        throw new IllegalStateException(
                "No new browser window was found");
    }

    protected void switchToWindow(String windowHandle) {
        driver.switchTo().window(windowHandle);
    }

    protected void closeCurrentWindow() {
        driver.close();
    }

    protected void uploadFile(By locator, String filePath) {
        waitUtil.waitForElementVisible(locator).sendKeys(filePath);
    }

    protected void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});",
                element);
    }
}
