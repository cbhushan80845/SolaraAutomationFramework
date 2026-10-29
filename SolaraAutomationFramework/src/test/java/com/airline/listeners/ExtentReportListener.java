
package com.airline.listeners;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.airline.annotations.Author;
import com.airline.base.BaseTest;
import com.airline.driver.DriverManager;
import com.airline.utils.ScreenshotUtil;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportListener implements ITestListener {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> test =
            new ThreadLocal<>();

    @Override
    public synchronized void onStart(ITestContext context) {
        String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));

        String reportPath = Paths.get(
                System.getProperty("user.dir"),
                "reports",
                "AirlineReport_" + timestamp + ".html"
        ).toString();

        ExtentSparkReporter sparkReporter =
                new ExtentSparkReporter(reportPath);

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        extent.setSystemInfo("Project", "Solara Automation Framework");
        extent.setSystemInfo("Framework", "Selenium Java TestNG");

        String env = context.getCurrentXmlTest().getParameter("env");
        String browser = context.getCurrentXmlTest().getParameter("browser");

        extent.setSystemInfo(
                "Environment", env == null ? "qa" : env);
        extent.setSystemInfo(
                "Browser", browser == null ? "chrome" : browser);
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest = extent.createTest(
                result.getMethod().getMethodName(),
                result.getMethod().getDescription());

        for (String group : result.getMethod().getGroups()) {
            extentTest.assignCategory(group);
        }

        Author author = result.getMethod()
                .getConstructorOrMethod()
                .getMethod()
                .getAnnotation(Author.class);

        if (author != null) {
            extentTest.assignAuthor(author.value());
        }

        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest extentTest = test.get();

        if (extentTest != null) {
            extentTest.log(Status.PASS, "Test passed");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest extentTest = test.get();

        if (extentTest == null) {
            return;
        }

        extentTest.log(Status.FAIL, "Test failed");
        extentTest.log(Status.FAIL, result.getThrowable());

        try {
            WebDriver driver = DriverManager.getDriver();

            if (driver == null && result.getInstance() instanceof BaseTest) {
                driver = ((BaseTest) result.getInstance()).getDriver();
            }

            String screenshotPath = ScreenshotUtil.captureScreenshot(
                    driver, result.getMethod().getMethodName());

            if (screenshotPath != null) {
                extentTest.addScreenCaptureFromPath(screenshotPath);
            }
        } catch (Exception e) {
            extentTest.log(
                    Status.WARNING,
                    "Screenshot could not be attached: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest extentTest = test.get();

        if (extentTest != null) {
            extentTest.log(Status.SKIP, "Test skipped");

            if (result.getThrowable() != null) {
                extentTest.log(Status.SKIP, result.getThrowable());
            }
        }
    }

    @Override
    public synchronized void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
        }

        test.remove();
    }
}
