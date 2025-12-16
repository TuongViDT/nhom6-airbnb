package tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import utils.ScreenshotHelper;
import utils.TestConfig;

public class BaseTest {
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext browserContext;
    protected Page page;
    protected ScreenshotHelper screenshotHelper;

    @BeforeClass
    public void setUpClass(){
        playwright = Playwright.create();
        browser = TestConfig.getBrowserType(playwright).launch(TestConfig.getLaunchOptions());
    }

    @BeforeMethod
    public void setUp(){
        browserContext = browser.newContext(TestConfig.getNewContextOptions());
        page = browserContext.newPage();
        screenshotHelper = new ScreenshotHelper(page);
    }

    @AfterMethod
    public void tearDown(){
        if(browserContext!= null){
            browserContext.close();
        }
    }

    @AfterClass
    public void tearDownClass(){
        if(browser != null){
            browser.close();
        }
        if(playwright != null){
            playwright.close();
        }
    }

    public ScreenshotHelper getScreenshotHelper(){
        return screenshotHelper;
    }


}
