
package com.airline.driver;

import org.openqa.selenium.WebDriver;

public class DriverManager {

    private static final ThreadLocal<WebDriver> driver =
            new ThreadLocal<>();

    private DriverManager() {
        // Prevent object creation.
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void setDriver(WebDriver webDriver) {
        if (webDriver == null) {
            throw new IllegalArgumentException(
                    "WebDriver cannot be null");
        }

        driver.set(webDriver);
    }

    public static void quitDriver() {
        WebDriver currentDriver = driver.get();

        try {
            if (currentDriver != null) {
                currentDriver.quit();
            }
        } finally {
            driver.remove();
        }
    }
}
