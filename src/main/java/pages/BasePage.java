package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import utils.TestConfig;

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
