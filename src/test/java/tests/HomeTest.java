package tests;

import listener.ExtentTestNGListener;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.HomePage;
import utils.TestConfig;
@Listeners(ExtentTestNGListener.class)
public class HomeTest extends BaseTest{
    private HomePage homePage;

    @BeforeMethod
    public void init() {
        homePage = new HomePage(page);

    }


//    TC_07: Search theo địa điểm (ví dụ: "Hồ Chí Minh")
    @Test
    public void testSearchWithLocation(){
//        B1: truy cap trang home
        ExtentTestNGListener.info("Truy cập trang web: " + TestConfig.getBaseUrl());
        page.navigate(TestConfig.getBaseUrl());
        page.waitForLoadState();
//        B2.1: chọn địa điểm
        ExtentTestNGListener.info("Chọn địa điểm tìm kiếm đặt phòng");
        homePage.chooseLocation("Hồ Chí Minh");
//        B2.2: click search button
        ExtentTestNGListener.info("Thực hiện tìm kiếm phòng");
        homePage.clickSearchButton();
//        B3: Kiểm tra url có đúng với mong đợi
        ExtentTestNGListener.info("Xác minh kết quả tìm kiếm theo địa điểm");
        boolean isSearchValid = homePage.isSearchValid("ho-chi-minh");
        Assert.assertTrue(isSearchValid, "URL does not contain expected location!");
    }

//    -----------------------------------------------CẦN XEM LẠI---------------------------------------
    @Test
    public void testSearchRoomsWithValidDate(){
//        B1: truy cap trang web
        ExtentTestNGListener.info("Truy cập trang web: " + TestConfig.getBaseUrl());
        page.navigate(TestConfig.getBaseUrl());
        page.waitForLoadState();
//          open date picker
        ExtentTestNGListener.info("Mở date picker để chọn ngày đặt phòng");
        homePage.openDatePicker();
        ExtentTestNGListener.info("Chọn ngày check-in");
        homePage.pickDateCheckIn("January 5, 2026");
        page.waitForTimeout(2000);
        ExtentTestNGListener.info("Chọn ngày check-out");
        homePage.pickDateCheckIn("January 10, 2026");
        page.waitForTimeout(2000);
        ExtentTestNGListener.info("Thực hiện thao tác tìm kiếm");
        homePage.clickSearchButton();
        page.waitForTimeout(3000);
        ExtentTestNGListener.info("Xác minh kết quả tìm kiếm hợp lệ");
        boolean isSearchValid = homePage.isSearchValid("rooms");
        Assert.assertTrue(isSearchValid, "URL does not contain expected location!");

    }

    @Test
    public void testFilterOfNumberGuests(){
        ExtentTestNGListener.info("Truy cập trang web: " + TestConfig.getBaseUrl());
        page.navigate(TestConfig.getBaseUrl());
        page.waitForLoadState();
        ExtentTestNGListener.info("Thiết lập số khách muốn thuê phòng");
        homePage.clickAddGuestButton();
        homePage.filterNumberOfGuests(5);
        ExtentTestNGListener.info("Thực hiện thao tác tìm kiếm phòng");
        homePage.clickSearchButton();
        ExtentTestNGListener.info("Xác minh kết quả tìm kiếm hợp lệ");
        boolean isSearchValid = homePage.isSearchValid("rooms");
        Assert.assertTrue(isSearchValid, "URL does not contain expected location!");
    }
}
