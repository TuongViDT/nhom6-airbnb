package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class RoomsPage extends BasePage{
    private static final String ROOM_CARD = "//h1[contains(text(),'Chỗ ở tại khu vực')]/following-sibling::div//a";
    private static final String ROOM_IMG = "(//div[contains(@class, 'ant-card-body')]//img)[1]";
    private static final String ROOM_NAME = "//div[@class='ant-card-body']//div[@class='relative']/p[2]";
    private static final String ROOM_PRICE_PER_NIGHT = "//div[@class='ant-card-body']//div[contains(text(),' / đêm')]//span";
    private static final String ROOM_CAPACITY = "//div[contains(@class,'ant-card-body')]" +
            "//div[contains(@class,'relative')]/following-sibling::p[contains(normalize-space(.),'khách')]";

    public RoomsPage(Page page) {
        super(page);
    }

    public boolean isRoomInfoDisplayed(){
        page.waitForSelector(ROOM_CARD);
        Locator card = page.locator(ROOM_CARD);
        page.waitForTimeout(2000);
        int count = card.count();
        for(int i = 0; i < count; i++){
            Locator nameRoom = card.nth(i).locator(ROOM_NAME);
            Locator imgRoom = card.nth(i).locator(ROOM_IMG);
            Locator capacityRoom = card.nth(i).locator(ROOM_CAPACITY);
            Locator priceRoom = card.nth(i).locator(ROOM_PRICE_PER_NIGHT);
            if(!nameRoom.isVisible()){
                System.out.println("Card " + (i+1) + " thiếu tên phòng!");
                return false;}
            if(!imgRoom.isVisible()){
                System.out.println("Card " + (i+1) + " thiếu ảnh phòng!");
                return false;}
            if(!capacityRoom.isVisible()){
                System.out.println("Card " + (i+1) + " thiếu số khách tối đa phòng!");
                return false;}
            if(!priceRoom.isVisible()){
                System.out.println("Card " + (i+1) + " thiếu giá phòng mỗi đêm!");
                return false;}
        }
        return true;
    }


    public String getRoomCard(){
        page.waitForSelector(ROOM_CARD);
        return page.locator(ROOM_CARD).nth(2).getAttribute("href");
    }




}
