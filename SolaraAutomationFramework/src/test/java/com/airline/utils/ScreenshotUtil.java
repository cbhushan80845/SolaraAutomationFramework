
package com.airline.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;

public final class ScreenshotUtil {

    private ScreenshotUtil() {
        // Prevent object creation.
    }

    public static String captureScreenshot(
            WebDriver driver, String testName) {

        if (driver == null) {
            return null;
        }

        try {
            Path screenshotDirectory = Paths.get(
                    System.getProperty("user.dir"), "screenshots");

            Files.createDirectories(screenshotDirectory);

            String timestamp = LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));

            String safeTestName = testName.replaceAll(
                    "[^a-zA-Z0-9._-]", "_");

            Path destination = screenshotDirectory.resolve(
                    safeTestName + "_" + timestamp + ".png");

            File source = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.FILE);

            FileHandler.copy(source, destination.toFile());

            return destination.toAbsolutePath().toString();

        } catch (IOException | RuntimeException e) {
            System.err.println(
                    "Screenshot capture failed: " + e.getMessage());
            return null;
        }
    }
}
