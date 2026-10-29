
package com.airline.base;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.airline.config.ConfigReader;
import com.airline.driver.DriverFactory;
import com.airline.driver.DriverManager;

public class BaseTest {

    protected String environment;
    protected String browserName;

    @Parameters({"browser", "env"})
    @BeforeMethod(alwaysRun = true)
    public void setup(
            @Optional("") String browserFromXml,
            @Optional("") String envFromXml) {

        ConfigReader.loadConfig();

        String browser = firstNonBlank(
                System.getProperty("browser"),
                browserFromXml,
                ConfigReader.getProperty("browser"));

        if (browser == null) {
            throw new IllegalStateException(
                    "Browser is not configured");
        }

        String env = firstNonBlank(
                System.getProperty("env"),
                envFromXml,
                ConfigReader.getProperty("env"),
                "qa");

        String appUrl = ConfigReader.getRequiredProperty(env + "Url");

        WebDriver driver = DriverFactory.createDriver(browser);
        DriverManager.setDriver(driver);

        try {
            int pageLoadTimeout = Integer.parseInt(
                    ConfigReader.getRequiredProperty("pageLoadTimeout"));

            driver.manage().timeouts()
                    .implicitlyWait(Duration.ZERO);

            driver.manage().timeouts()
                    .pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));

            driver.manage().window().maximize();
            driver.get(appUrl);

            environment = env;
            browserName = browser;

            System.out.println("Environment: " + environment);
            System.out.println("Application URL: " + appUrl);
            System.out.println("Browser: " + browserName);

        } catch (RuntimeException | Error e) {
            DriverManager.quitDriver();
            throw e;
        }
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }
        return null;
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }

    public WebDriver getDriver() {
        WebDriver driver = DriverManager.getDriver();

        if (driver == null) {
            throw new IllegalStateException(
                    "WebDriver is not initialized for this test");
        }

        return driver;
    }
}
