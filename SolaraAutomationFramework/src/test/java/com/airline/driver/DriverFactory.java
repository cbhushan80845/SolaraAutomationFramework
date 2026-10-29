
package com.airline.driver;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.airline.config.ConfigReader;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactory {

    private DriverFactory() {
        // Prevent object creation.
    }

    public static WebDriver createDriver(String browser) {
        if (browser == null || browser.isBlank()) {
            throw new IllegalArgumentException(
                    "Browser must be configured");
        }

        String executionMode =
                ConfigReader.getProperty("executionMode");

        if (executionMode == null || executionMode.isBlank()) {
            executionMode = "local";
        }

        if (executionMode.equalsIgnoreCase("remote")) {
            return createRemoteDriver(browser);
        }

        if (executionMode.equalsIgnoreCase("local")) {
            return createLocalDriver(browser);
        }

        throw new IllegalArgumentException(
                "Unsupported execution mode: " + executionMode);
    }

    private static WebDriver createLocalDriver(String browser) {
        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            return new ChromeDriver();
        }

        if (browser.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            return new EdgeDriver();
        }

        throw new IllegalArgumentException(
                "Unsupported browser: " + browser);
    }

    private static WebDriver createRemoteDriver(String browser) {
        String remoteUrl = ConfigReader.getProperty("remoteUrl");

        if (remoteUrl == null || remoteUrl.isBlank()) {
            throw new IllegalArgumentException(
                    "remoteUrl must be configured for remote execution");
        }

        try {
            URL gridUrl = new URL(remoteUrl);

            if (browser.equalsIgnoreCase("chrome")) {
                ChromeOptions options = new ChromeOptions();
                return new RemoteWebDriver(gridUrl, options);
            }

            if (browser.equalsIgnoreCase("edge")) {
                EdgeOptions options = new EdgeOptions();
                return new RemoteWebDriver(gridUrl, options);
            }

            throw new IllegalArgumentException(
                    "Unsupported remote browser: " + browser);

        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(
                    "Invalid Selenium Grid URL: " + remoteUrl, e);
        }
    }
}
