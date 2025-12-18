package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import utils.TestConfig;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class BasePage {
    protected final Page page;
    protected final String baseUrl = TestConfig.getBaseUrl();

    public BasePage(Page page) {
        this.page = page;
    }

    public void scrollToElement(String locator){
        Locator element = page.locator(locator);
        element.evaluate("el => el.scrollIntoView({behavior: 'smooth', block: 'center'})");

    }

}
