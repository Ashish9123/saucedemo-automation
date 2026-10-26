package com.saucedemo.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.saucedemo.base.DriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

/**
 * Wires TestNG's execution events into an ExtentReports HTML report.
 * Registered via testng.xml <listeners> so it runs for every suite
 * without any test class needing to know about it.
 */
public class ExtentReportListener implements ITestListener {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> TEST = new ThreadLocal<>();
    private static final String REPORT_DIR = "test-output/ExtentReports/";

    @Override
    public void onStart(ITestContext context) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String reportPath = REPORT_DIR + "SauceDemo_Report_" + timestamp + ".html";
        new File(REPORT_DIR).mkdirs();

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setDocumentTitle("SauceDemo Automation Report");
        spark.config().setReportName("SauceDemo – Selenium/TestNG Execution Results");

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Application", "https://www.saucedemo.com/");
        extent.setSystemInfo("Framework", "Selenium WebDriver + TestNG + POM");
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = extent.createTest(result.getMethod().getMethodName());
        TEST.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        TEST.get().log(Status.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = TEST.get();
        test.log(Status.FAIL, result.getThrowable());
        attachScreenshot(test, result.getMethod().getMethodName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        TEST.get().log(Status.SKIP, "Test skipped: " +
                (result.getThrowable() != null ? result.getThrowable().getMessage() : "dependency failed"));
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }

    private void attachScreenshot(ExtentTest test, String testName) {
        try {
            TakesScreenshot ts = (TakesScreenshot) DriverManager.getDriver();
            File src = ts.getScreenshotAs(OutputType.FILE);
            Path dest = Paths.get(REPORT_DIR, testName + "_failure.png");
            Files.copy(src.toPath(), dest, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            test.addScreenCaptureFromPath(dest.toString());
        } catch (IOException | RuntimeException e) {
            test.log(Status.WARNING, "Could not capture screenshot: " + e.getMessage());
        }
    }
}
