package tests;

import listener.ExtentTestNGListener;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.RoomDetailPage;
import pages.components.HeaderComponent;
import utils.TestConfig;

@Listeners(ExtentTestNGListener.class)
public class LogoutTest extends BaseTest {
    private LoginPage loginForm;
    private HeaderComponent header;
    private HomePage homePage;
    private RoomDetailPage roomDetailPage;


    @BeforeMethod
    public void init() {
        loginForm = new LoginPage(page);
        header = new HeaderComponent(page);
        homePage = new HomePage(page);
        roomDetailPage = new RoomDetailPage(page);
    }

// Dang xuat thanh cong tu home page
    @Test
    public void testLogoutSuccess() {
//        B1: Dang nhap taif khoan
        ExtentTestNGListener.info("Truy cập trang web: " + TestConfig.getBaseUrl());
        page.navigate(TestConfig.getBaseUrl());
        page.waitForLoadState();
        ExtentTestNGListener.info("Mở form đăng nhập");
        loginForm.openSignInForm();
        ExtentTestNGListener.info("Đăng nhập với thông tin hợp lệ");
        String email = TestConfig.getLoginValidEmail();
        String password = TestConfig.getLoginValidPassword();
        loginForm.login(email, password);
        page.waitForLoadState();

//        B2: mo user menu
        ExtentTestNGListener.info("Mở menu User account");
        header.openUserMenu();
//        B3: click logout
        ExtentTestNGListener.info("Thực hiện đăng xuất khỏi tài khoản");
        header.clickLogout();
//        B4: kiem tra log out thanh cong
        ExtentTestNGListener.info("Xác minh hệ thống đăng xuất thành công");
        boolean isLogoutSuccess = homePage.isLogoutSuccess();
        Assert.assertTrue(isLogoutSuccess);
        page.waitForLoadState();
        Assert.assertTrue(homePage.checkUserAccount(), "User Account vẫn còn xuất hiện sau khi logout!");

    }

    // Đăng xuất khi đang ở room Page
    @Test
    public void testLogoutFromRoomPage() {
        //        B1: Dang nhap taif khoan
        ExtentTestNGListener.info("Truy cập trang web: " + TestConfig.getBaseUrl());
        page.navigate(TestConfig.getBaseUrl());
        page.waitForLoadState();
        ExtentTestNGListener.info("Mở form đăng nhập");
        loginForm.openSignInForm();
        ExtentTestNGListener.info("Đăng nhập tài khoản với thông tin hợp lệ");
        loginForm.login(TestConfig.getLoginValidEmail(), TestConfig.getLoginValidPassword());
        page.waitForLoadState();

        //        B2: chon dia diem
        ExtentTestNGListener.info("Chọn địa điểm tìm kiếm phòng");
        homePage.clickLocation();
        page.waitForLoadState();

        ExtentTestNGListener.info("Mở menu User account tại trang danh sách phòng");
        header.openUserMenu();
        ExtentTestNGListener.info("Thực hiện đăng xuất khỏi tài khoản");
        header.clickLogout();
        ExtentTestNGListener.info("Xác minh đăng xuất thành công");
        //        B4: kiem tra log out thanh cong
        boolean isLogoutSuccess = homePage.isLogoutSuccess();
        Assert.assertTrue(isLogoutSuccess);
        ExtentTestNGListener.info("Xác minh điều hướng thành công");
        page.waitForTimeout(3000);
        Assert.assertTrue(homePage.checkUrl(), "Redirect after logout failed");

    }

    // Đăng xuất khi đang ở detail Page
    @Test
    public void testLogoutFromDetailPage() {
    //        B1: Dang nhap taif khoan
        ExtentTestNGListener.info("Truy cập trang web: " + TestConfig.getBaseUrl());
        page.navigate(TestConfig.getBaseUrl());
        page.waitForLoadState();
        ExtentTestNGListener.info("Mở form đăng nhập");
        loginForm.openSignInForm();
        ExtentTestNGListener.info("Đăng nhập với thông tin hợp lệ");
        loginForm.login(TestConfig.getLoginValidEmail(), TestConfig.getLoginValidPassword());
        page.waitForLoadState();

        //        B2: chon dia diem
        ExtentTestNGListener.info("Chọn địa điểm tìm kiếm phòng");
        homePage.clickLocation();
        page.waitForLoadState();

//        B3: chon phong
        ExtentTestNGListener.info("Chọn một phòng trong danh sách kết quả");
        roomDetailPage.chooseRoom();
        page.waitForLoadState();

        ExtentTestNGListener.info("Mở menu User account trong trang chi tiết phòng");
        header.openUserMenu();
        ExtentTestNGListener.info("Thực hiện đăng xuất khỏi tài khoản");
        header.clickLogout();

    //        B4: kiem tra log out thanh cong
        ExtentTestNGListener.info("Xác minh đăng xuất thành công");
        boolean isLogoutSuccess = homePage.isLogoutSuccess();
        Assert.assertTrue(isLogoutSuccess);
        ExtentTestNGListener.info("Xác minh điều hướng thành công");
        page.waitForTimeout(3000);
        Assert.assertTrue(homePage.checkUrl(), "Redirect after logout failed");

    }

//    Dang xuat khi dang o profile page
    @Test
    public void testLogoutFromProfilePage() {
        //        B1: Dang nhap taif khoan
        ExtentTestNGListener.info("Truy cập trang web: " + TestConfig.getBaseUrl());
        page.navigate(TestConfig.getBaseUrl());
        page.waitForLoadState();
        ExtentTestNGListener.info("Mở form đăng nhập");
        loginForm.openSignInForm();
        ExtentTestNGListener.info("Đăng nhập với thông tin hợp lệ");
        loginForm.login(TestConfig.getLoginValidEmail(), TestConfig.getLoginValidPassword());
        page.waitForLoadState();

        ExtentTestNGListener.info("Mở menu User account");
        header.openUserMenu();
        ExtentTestNGListener.info("Mở trang profile");
        header.clickOptionUserMenu("Dashboard");
        page.waitForTimeout(2000);
        ExtentTestNGListener.info("Mở menu User account tại trang profile");
        header.openUserMenu();
        ExtentTestNGListener.info("Thực hiện đăng xuất khỏi tài khoản");
        header.clickLogout();

//                B4: kiem tra log out thanh cong
        ExtentTestNGListener.info("Xác minh đăng xuất thành công");
        boolean isLogoutSuccess = homePage.isLogoutSuccess();
        Assert.assertTrue(isLogoutSuccess);
        ExtentTestNGListener.info("Xác minh điều hướng thành công");
        page.waitForTimeout(3000);
        Assert.assertTrue(homePage.checkUrl(), "Redirect after logout failed");
    }
}
