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

    public void takeScreenshot(String stepName){
        try{
//            B1: tao folder luu hinh
            Path screenShotDir = Paths.get("screenshots");
            if(!Files.exists(screenShotDir)){
                Files.createDirectories(screenShotDir);
            }
//            B2: Tao ten file voi stepName
            String timeStamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String pathFile = "screenshots/" + stepName + "-" + timeStamp + ".png";
//            B3: chup va luu hinh
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get(pathFile))
                    .setFullPage(true));
//            B4: Thong bao thanh cong
            System.out.println("[Screenshot] Da chup thanh cong: " + pathFile);

        } catch (Exception e) {
            System.out.println("[Screenshot] Loi chup man hinh: " + e.getMessage());
        }
    }

    public void scrollToElement(String locator){
        Locator element = page.locator(locator);
        element.evaluate("el => el.scrollIntoView({behavior: 'smooth', block: 'center'})");

    }

}
