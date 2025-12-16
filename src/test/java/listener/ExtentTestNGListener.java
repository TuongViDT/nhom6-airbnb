package listener;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.*;
import pages.BasePage;
import tests.BaseTest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExtentTestNGListener implements ITestListener, ISuiteListener {
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static ExtentReports extentReports;

    private static synchronized ExtentReports createReporter() {
        // Tạo thư mục "reports" nếu chưa tồn tại
        try {
            Files.createDirectories(Paths.get("reports"));
        } catch (Exception ignored) {
            // Nếu tạo thư mục thất bại, bỏ qua (có thể đã tồn tại)
        }

        // Tạo ExtentSparkReporter - reporter để tạo HTML report đẹp
        // File output: reports/ExtentReport.html
        ExtentSparkReporter spark = new ExtentSparkReporter("reports/ExtentReport.html");

        // Cấu hình Spark Reporter
        spark.config().setDocumentTitle("Automation Test Report");      // Tiêu đề document
        spark.config().setReportName("Playwright Test Execution");      // Tên report
        spark.config().setTheme(Theme.STANDARD);                        // Theme (STANDARD hoặc DARK)

        // Tạo ExtentReports instance
        ExtentReports extent = new ExtentReports();

        // Gắn Spark Reporter vào ExtentReports
        extent.attachReporter(spark);

        // Thêm thông tin hệ thống vào report
        extent.setSystemInfo("Framework", "Playwright Java");  // Framework đang dùng
        extent.setSystemInfo("Runner", "TestNG");              // Test runner đang dùng

        // Trả về ExtentReports instance đã cấu hình
        return extent;
    }
    public static ExtentTest getTest() {
        return test.get();
    }

    @Override
    public void onStart(ISuite suite) {
        extentReports = createReporter();
    }

    @Override
    public void onFinish(ISuite suite) {
        if (extentReports != null) {
            extentReports.flush();
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();
        ExtentTest extentTest = extentReports.createTest(className + "::" + testName);
        test.set(extentTest);
        extentTest.info("Test started");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        getTest().pass("Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = getTest();
        getTest().fail(result.getThrowable());

//        capture screenshot automatically
        Object instance = result.getInstance();
        if(instance instanceof BaseTest bt){
            String fileName = "fail_" + result.getMethod().getMethodName();
            String path = bt.getScreenshotHelper().takeScreenshot(fileName);
            getTest().addScreenCaptureFromPath("../" + path, "Failure Screenshot");
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        getTest().skip("Test skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // no-op
    }

    @Override
    public void onStart(ITestContext context) {

        // no-op
    }

    public static void logStep(String message) {
        ExtentTest extentTest = test.get();
        if (extentTest != null) {
            extentTest.info(message);
        }
    }

    public static void info(String message){
        ExtentTest test = getTest();
        if(test != null) {
            test.info(message);
        }
    }


    @Override
    public void onFinish(ITestContext context) {
        if(extentReports != null){
            extentReports.flush();
        }
    }

}
