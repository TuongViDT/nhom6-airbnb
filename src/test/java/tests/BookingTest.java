package tests;

import listener.ExtentTestNGListener;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.BookingPage;
import pages.HomePage;
import pages.LoginPage;
import pages.RoomDetailPage;
import utils.TestConfig;

@Listeners(ExtentTestNGListener.class)
public class BookingTest extends BaseTest {
    private BookingPage bookingPage;
    private HomePage homePage;
    private RoomDetailPage roomDetailPage;
    private LoginPage loginPage;


    @BeforeMethod
    public void init() {
        homePage = new HomePage(page);
        roomDetailPage = new RoomDetailPage(page);
        bookingPage = new BookingPage(page);
        loginPage = new LoginPage(page);
    }



    @Test(priority = 1)
    public void testBookingSuccess(){
//        B1: truy cap trang web
        ExtentTestNGListener.info("Truy cap thanh cong trang web");
        page.navigate(TestConfig.getBaseUrl());
        page.waitForLoadState();
//        Dang nhap thanh cong
        ExtentTestNGListener.info("Đăng nhập tài khoản");
        String email = TestConfig.getLoginValidEmail();
        String password = TestConfig.getLoginValidPassword();
        loginPage.openSignInForm();
        loginPage.login(email, password);
        page.waitForLoadState();

//        B2: chon dia diem
        ExtentTestNGListener.info("Chọn địa điểm");
        homePage.clickLocation();
        page.waitForLoadState();

//        B3: chon phong
        ExtentTestNGListener.info("Chọn phòng");
        page.waitForTimeout(2000);
        roomDetailPage.chooseRoom();
        page.waitForLoadState();

//        B4: thay doi ngay
        ExtentTestNGListener.info("Chon ngay check in check out");
        bookingPage.openPickDate();
        bookingPage.chooseDateBooking("March 29, 2026");
        bookingPage.chooseDateBooking("April 5, 2026");
        bookingPage.clickCloseButton();
        ExtentTestNGListener.info("Chon so luong khach thue");
        bookingPage.setGuests(2);
        ExtentTestNGListener.info("Thực hiện thao tác đặt phòng");
        bookingPage.clickBookingButton();
        bookingPage.clickConfirm();
        ExtentTestNGListener.info("Xác minh đặt phòng thành công");
        boolean isSuccessBooking = bookingPage.isBookingSuccess();
        Assert.assertTrue(isSuccessBooking, "Booking was not successful");
        page.waitForTimeout(3000);
    }

//    TC: booking failed - chua dang nhap
    @Test(priority = 2)
    public void testBookingNotLogin(){
//        B1: truy cap trang web
        ExtentTestNGListener.info("Truy cập trang web: " + TestConfig.getBaseUrl());
        page.navigate(TestConfig.getBaseUrl());
        page.waitForLoadState();

//        B2: chon dia diem
        ExtentTestNGListener.info("Chọn địa điểm để tìm kiếm phòng");
        homePage.clickLocation();
        page.waitForLoadState();

//        B3: chon phong
        ExtentTestNGListener.info("Chọn một phòng từ danh sách kết quả");
        page.waitForTimeout(2000);
        roomDetailPage.chooseRoom();
        page.waitForLoadState();

//        B4: thay doi ngay
        ExtentTestNGListener.info("Chọn ngày check-in và ngày check-out hợp lệ");
        bookingPage.openPickDate();
        bookingPage.chooseDateBooking("March 29, 2026");
        bookingPage.chooseDateBooking("April 5, 2026");
        bookingPage.clickCloseButton();

        ExtentTestNGListener.info("Thiết lập số lượng khách thuê phòng");
        bookingPage.setGuests(2);

        ExtentTestNGListener.info("Thực hiện thao tác đặt phòng");
        bookingPage.clickBookingButton();
        ExtentTestNGListener.info("Xác minh hệ thống không cho phép đặt phòng khi chưa đăng nhập");
        boolean hasNoticeWarning = bookingPage.hasNoticeWarning();
        Assert.assertTrue(hasNoticeWarning);
        page.waitForTimeout(3000);
    }

//    TC: Đặt phòng thất bại - Ngày không hợp lệ
    @Test(priority = 3)
    public void testBookingInvalidDate(){
//        B1: truy cap trang web
        ExtentTestNGListener.info("Truy cập trang web: " + TestConfig.getBaseUrl());
        page.navigate(TestConfig.getBaseUrl());
        page.waitForLoadState();

//        Dang nhap thanh cong
        ExtentTestNGListener.info("Đăng nhập hệ thống với tài khoản hợp lệ");
        String email = TestConfig.getLoginValidEmail();
        String password = TestConfig.getLoginValidPassword();
        loginPage.openSignInForm();
        loginPage.login(email, password);
        page.waitForLoadState();

//        B2: chon dia diem
        ExtentTestNGListener.info("Chọn địa điểm để tìm kiếm phòng");
        homePage.clickLocation();
        page.waitForLoadState();

//        B3: chon phong
        ExtentTestNGListener.info("Chọn một phòng trong danh sách kết quả");
        page.waitForTimeout(2000);
        roomDetailPage.chooseRoom();
        page.waitForLoadState();

//        B4: chọn ngày check in check out cùng 1 ngày
        ExtentTestNGListener.info("Chọn ngày check-in và check-out trùng nhau");
        bookingPage.openPickDate();
        bookingPage.chooseDateBooking("February 5, 2026");
        bookingPage.chooseDateBooking("February 5, 2026");
        bookingPage.clickCloseButton();
        ExtentTestNGListener.info("Thực hiện thao tác đặt phòng");
        bookingPage.clickBookingButton();
        bookingPage.clickConfirm();
        ExtentTestNGListener.info("Xác minh hệ thống không cho phép đặt phòng với ngày không hợp lệ");
        boolean isSuccessBooking = bookingPage.isBookingSuccess();
        Assert.assertFalse(isSuccessBooking, "Hệ thống vẫn cho đặt phòng dù ngày không hợp lệ!");
        page.waitForTimeout(3000);}

//    TC: so sánh giá mỗi đêm trên card phòng và trang chi tiết phòng
    @Test(priority = 4)
    public void testComparePriceBetweenCardAndDetailPage(){
        //        B1: truy cap trang web
        ExtentTestNGListener.info("Truy cập trang web: " + TestConfig.getBaseUrl());
        page.navigate(TestConfig.getBaseUrl());
        page.waitForLoadState();
//        B2: chon dia diem
        ExtentTestNGListener.info("Chọn địa điểm để tìm kiếm phòng");
        homePage.clickLocation();
        page.waitForLoadState();
        ExtentTestNGListener.info("Lấy giá phòng hiển thị trên thẻ phòng muốn chọn");
        int priceCard = roomDetailPage.getPricePerNight();
        ExtentTestNGListener.info("Chọn phòng để xem chi tiết");
        roomDetailPage.chooseRoom();
        page.waitForLoadState();
        page.waitForTimeout(3000);
        ExtentTestNGListener.info("Lấy giá phòng hiển thị trên trang chi tiết");
        int priceDetail = bookingPage.getPricePerNight();
        ExtentTestNGListener.info("Xác minh giá phòng trên thẻ và trang chi tiết là giống nhau");
        Assert.assertEquals(priceCard, priceDetail, "Price card has wrong value");
    }


    @Test(priority = 5)
    public void testTotalPriceCalculatedCorrectly(){
//        B1: truy cap trang web
        ExtentTestNGListener.info("Truy cập trang web: " + TestConfig.getBaseUrl());
        page.navigate(TestConfig.getBaseUrl());
        page.waitForLoadState();

//        B2: chon dia diem
        ExtentTestNGListener.info("Chọn địa điểm để tìm kiếm phòng");
        homePage.clickLocation();
        page.waitForLoadState();

//        B3: chon phong
        ExtentTestNGListener.info("Chọn phòng để xem chi tiết");
        roomDetailPage.chooseRoom();
        page.waitForLoadState();

//        B4: thay doi ngay
        ExtentTestNGListener.info("Chọn ngày check-in check-out");
        bookingPage.openPickDate();
        bookingPage.chooseDateBooking("March 29, 2026");
        bookingPage.chooseDateBooking("April 5, 2026");
        bookingPage.clickCloseButton();
        ExtentTestNGListener.info("Xác minh giá phòng được hiển thị và tính toán đúng");
        int expectedTotal = bookingPage.priceTotal();
        int currentTotal = bookingPage.checkPrice();
        Assert.assertEquals(expectedTotal, currentTotal, "Price has wrong value");
        page.waitForTimeout(3000);
    }
}
