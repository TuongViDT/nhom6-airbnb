package tests;

import listener.ExtentTestNGListener;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.RoomsPage;
import utils.TestConfig;

@Listeners(ExtentTestNGListener.class)
public class RoomsTest extends BaseTest {
    private HomePage homePage;
    private RoomsPage roomsPage;

    @BeforeMethod
    public void init(){
        homePage = new HomePage(page);
        roomsPage = new RoomsPage(page);
    }

    @Test
    public void testRoomCardInfo(){
        //        B1: truy cap trang web
        ExtentTestNGListener.info("TRuy cập trang web: " + TestConfig.getBaseUrl());
        page.navigate(TestConfig.getBaseUrl());
        page.waitForLoadState();
//        B2: tim kiem phong vd: theo dia diem
        ExtentTestNGListener.info("Chọn địa điểm đặt phòng");
        homePage.clickLocation();
        page.waitForLoadState();
        ExtentTestNGListener.info("Xác minh thông tin trên thẻ phòng đầy đủ");
        boolean isRoomInfoDisplayed = roomsPage.isRoomInfoDisplayed();
        Assert.assertTrue(isRoomInfoDisplayed);
    }
}
