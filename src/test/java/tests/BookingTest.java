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



    @Test
    public void testBookingSuccess(){
//        B1: truy cap trang web

        page.navigate(TestConfig.getBaseUrl());
        page.waitForLoadState();
        ExtentTestNGListener.info("Truy cap thanh cong trang web");

//        Dang nhap thanh cong
        String email = TestConfig.getLoginValidEmail();
        String password = TestConfig.getLoginValidPassword();
        loginPage.openSignInForm();
        loginPage.login(email, password);
        page.waitForLoadState();
        ExtentTestNGListener.info("Da dang nhap thanh cong");

//        B2: chon dia diem
        homePage.clickLocation();
        page.waitForLoadState();
        ExtentTestNGListener.info("Da chon dia diem");

//        B3: chon phong
        roomDetailPage.chooseRoom();
        page.waitForLoadState();
        ExtentTestNGListener.info("Da chon phong");

//        B4: thay doi ngay
        bookingPage.openPickDate();
        bookingPage.chooseDateBooking("December 29, 2025");
        bookingPage.chooseDateBooking("January 5, 2026");


        bookingPage.clickCloseButton();
        ExtentTestNGListener.info("Da chon ngay check in check out");
        bookingPage.setGuests(2);
        ExtentTestNGListener.info("Da chon so luong khach thue");
        bookingPage.clickBookingButton();
        bookingPage.clickConfirm();
        boolean isSuccessBooking = bookingPage.isBookingSuccess();
        ExtentTestNGListener.info("Dat phong thanh cong");
        Assert.assertTrue(isSuccessBooking, "Booking was not successful");
        page.waitForTimeout(3000);
    }

//    TC: booking failed - chua dang nhap
    @Test
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
        roomDetailPage.chooseRoom();
        page.waitForLoadState();

//        B4: thay doi ngay
        ExtentTestNGListener.info("Chọn ngày check-in và ngày check-out hợp lệ");
        bookingPage.openPickDate();
        bookingPage.chooseDateBooking("December 29, 2025");
        bookingPage.chooseDateBooking("January 5, 2026");
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
    @Test
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
        roomDetailPage.chooseRoom();
        page.waitForLoadState();

//        B4: chọn ngày check in check out cùng 1 ngày
        ExtentTestNGListener.info("Chọn ngày check-in và check-out trùng nhau");
        bookingPage.openPickDate();
        bookingPage.chooseDateBooking("January 5, 2026");
        bookingPage.chooseDateBooking("January 5, 2026");
        bookingPage.clickCloseButton();
        ExtentTestNGListener.info("Thực hiện thao tác đặt phòng");
        bookingPage.clickBookingButton();
        bookingPage.clickConfirm();
        ExtentTestNGListener.info("Xác minh hệ thống không cho phép đặt phòng với ngày không hợp lệ");
        boolean isSuccessBooking = bookingPage.isBookingSuccess();
        Assert.assertFalse(isSuccessBooking, "Hệ thống vẫn cho đặt phòng dù ngày không hợp lệ!");
        page.waitForTimeout(3000);}

//    TC: so sánh giá mỗi đêm trên card phòng và trang chi tiết phòng
    @Test
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


    @Test
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
        bookingPage.chooseDateBooking("December 29, 2025");
        bookingPage.chooseDateBooking("January 5, 2026");
        bookingPage.clickCloseButton();
        ExtentTestNGListener.info("Xác minh giá phòng được hiển thị và tính toán đúng");
        int expectedTotal = bookingPage.priceTotal();
        int currentTotal = bookingPage.checkPrice();
        Assert.assertEquals(expectedTotal, currentTotal, "Price has wrong value");
        page.waitForTimeout(3000);
    }
}
