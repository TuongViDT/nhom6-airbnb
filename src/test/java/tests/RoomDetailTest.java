package tests;

import listener.ExtentTestNGListener;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.RoomDetailPage;
import utils.TestConfig;

import java.net.URI;


@Listeners(ExtentTestNGListener.class)
public class RoomDetailTest extends BaseTest{

    private HomePage homePage;
    private RoomDetailPage roomDetailPage;

    @BeforeMethod
    public void init(){
        homePage = new HomePage(page);
        roomDetailPage = new RoomDetailPage(page);
    }


//    TC_11: Xem chi tiết phòng
    @Test(priority = 1)
    public void testViewRoomDetail(){
//        B1: truy cap trang web
        ExtentTestNGListener.info("Truy cập trang web: " + TestConfig.getBaseUrl());
        page.navigate(TestConfig.getBaseUrl());
        page.waitForLoadState();
//        B2: tim kiem phong vd: theo dia diem
        ExtentTestNGListener.info("Chọn địa điểm đặt phòng");
        homePage.clickLocation();
        page.waitForLoadState();
        ExtentTestNGListener.info("Chọn phòng");
        String href = roomDetailPage.getRoomCardInfo();
        roomDetailPage.chooseRoom();
        page.waitForLoadState();
        ExtentTestNGListener.info("Xác minh trang chi tiết hiển thị đúng");
        String currentUrl = roomDetailPage.getCurrentUrl();
        String hrefPath = URI.create(href).getPath();
        String currentPath = URI.create(currentUrl).getPath();
        Assert.assertEquals(currentPath, hrefPath, "URL hiện tại không khớp với href phòng");
    }

//    hiển thị đầy đủ thông tin phòng
    @Test(priority = 2)
    public void testRoomInfoDisplay(){
        ExtentTestNGListener.info("TRuy cập trang web: " + TestConfig.getBaseUrl());
        page.navigate(TestConfig.getBaseUrl());
        page.waitForLoadState();

        ExtentTestNGListener.info("Chọn địa điểm để đặt phòng");
        homePage.clickLocation();
        page.waitForLoadState();
        ExtentTestNGListener.info("Chọn phòng");
        roomDetailPage.chooseRoom();
        page.waitForLoadState();
        ExtentTestNGListener.info("Xác minh trang chi tiết hiển thị đầy đủ thông phòng");
        Assert.assertTrue(roomDetailPage.isDisplayed(),
                "Thiếu thông tin: " + roomDetailPage.getMissingParts());
    }
}
