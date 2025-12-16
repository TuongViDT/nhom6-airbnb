package utils;

import com.microsoft.playwright.Page;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotHelper {
    private Page page;
    public ScreenshotHelper(Page page){
        this.page = page;
    }
    public String takeScreenshot(String stepName){
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
            return pathFile;

        } catch (Exception e) {
            System.out.println("[Screenshot] Loi chup man hinh: " + e.getMessage());
            return null;
        }

    }
}
