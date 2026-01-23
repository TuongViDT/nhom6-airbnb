package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.ArrayList;
import java.util.List;

public class RoomDetailPage extends BasePage{
    private HomePage homePage;
    private static final String ROOM_CARD = "//h1/following-sibling::div//a";
    private static final String PRICE = "//div[@class='ant-card-body']//div[contains(text(),' / đêm')]//span";
    private static final String ROOM_NAME = "//div[contains(@class,'container')]" +
            "//div[contains(@class,'grid')]/preceding-sibling::h2";
    private static final String ROOM_LOCATION = "//div[contains(@class,'container')]//h2/following-sibling::div//a";
    private static final String ROOM_IMG = "//div[contains(@class,'ant-image')]//img";
    private static final String MAX_GUEST = "//div[contains(@class,'grid')]//h3/following-sibling::p";
    private static final String FORM_BOOKING = "//button[contains(@class,'bg-main') and contains(text(),'Đặt phòng')]/..";
    private static final String FORM_REVIEW = "//button[@type='submit']/ancestor::form";
    private static final String COMMENTS_LIST = "//h3[contains(text(),'Bình luận')]/following-sibling::div[contains(@class,'grid')]";
    private static final String REVIEW_ALERT = "//div[@role='alert']//div[contains(text(),'Cần đăng nhập để bình luận')]";
    private static final String USER_ACCOUNT = "//button[@id='user-menu-button']";


    public RoomDetailPage(Page page){
        super(page);
    }

    public void chooseRoom(){
        Locator roomCard = page.locator(ROOM_CARD).first();
        roomCard.scrollIntoViewIfNeeded();
        roomCard.waitFor();
        roomCard.click();
    }

    public String getRoomCardInfo(){
        return page.locator(ROOM_CARD).first().getAttribute("href");
    }

    public String getCurrentUrl(){
        return page.url();
    }

    public int getPricePerNight(){
        String price = page.locator(PRICE).first().innerText().replace("$", "").trim();
        return Integer.parseInt(price);
    }
    public boolean isLoggedIn(){
        return page.locator(USER_ACCOUNT).count() > 0;
    }

    public boolean checkVisible(String locator){
        page.locator(locator).first().waitFor();
        return page.locator(locator).count() > 0
                && page.locator(locator).first().isVisible();
    }

    public List<String> getMissingParts(){
        List<String> missing = new ArrayList<>();
        if(!checkVisible(ROOM_NAME)) missing.add("Room Name");
        if(!checkVisible(ROOM_LOCATION)) missing.add("Room Location");
        if(!checkVisible(ROOM_IMG)) missing.add("Room Image");
        if(!checkVisible(MAX_GUEST)) missing.add("Max Guest");
        if(!checkVisible(FORM_BOOKING)) missing.add("Form Booking");
        if(isLoggedIn()){
            if(!checkVisible(FORM_REVIEW)) missing.add("Form Review");
        }else {
            if (!checkVisible(REVIEW_ALERT)) missing.add("Review Alert");
        }
        if(!checkVisible(COMMENTS_LIST)) missing.add("Comments List");
        return missing;
    }

    public boolean isDisplayed(){
        return getMissingParts().isEmpty();
    }

}
