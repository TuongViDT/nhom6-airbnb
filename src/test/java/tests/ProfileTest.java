package tests;

import listener.ExtentTestNGListener;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProfilePage;
import pages.components.HeaderComponent;
import utils.TestConfig;

import java.nio.file.Paths;
@Listeners(ExtentTestNGListener.class)
public class ProfileTest extends BaseTest {
    private HeaderComponent header;
    private LoginPage loginPage;
    private ProfilePage profilePage;

    @BeforeMethod
    public void init(){
        header = new HeaderComponent(page);
        loginPage = new LoginPage(page);
        profilePage = new ProfilePage(page);
    }

//    Xem thông tin profile
    @Test(priority = 1)
    public void testViewProfile(){
//        B1: truy cập trang web
        ExtentTestNGListener.info("Truy cập trang web: " + TestConfig.getBaseUrl());
        page.navigate(TestConfig.getBaseUrl());
        page.waitForLoadState();

//        B2: Đăng nhập tài khoản
        ExtentTestNGListener.info("Mở form đăng nhập");
        loginPage.openSignInForm();
        ExtentTestNGListener.info("Đăng nhập với thông tin hợp lệ");
        loginPage.login(TestConfig.getLoginValidEmail(), TestConfig.getLoginValidPassword());
        page.waitForLoadState();

//        B3: mo profile
        ExtentTestNGListener.info("Mở menu User account");
        header.openUserMenu();
        ExtentTestNGListener.info("Mở trang profile");
        header.clickOptionUserMenu("Dashboard");
        page.waitForLoadState();
        ExtentTestNGListener.info("Xác minh điều hướng đến trang profile");
        String currentUrl = profilePage.getProfileUrl();
        Assert.assertTrue(currentUrl.contains("/info-user"), "Điều hướng thất bại");
    }

//    Update thông tin cá nhân
    @Test(priority = 2)
    public void testUpdateProfile(){
        //        B1: truy cập trang web
        ExtentTestNGListener.info("Truy cập trang web: " + TestConfig.getBaseUrl());
        page.navigate(TestConfig.getBaseUrl());
        page.waitForLoadState();

//        B2: Đăng nhập tài khoản
        ExtentTestNGListener.info("Mở form đăng nhập");
        loginPage.openSignInForm();
        ExtentTestNGListener.info("Đăng nhập với thông tin hợp lệ");
        loginPage.login(TestConfig.getLoginValidEmail(), TestConfig.getLoginValidPassword());
        page.waitForLoadState();

//        B3: mo profile
        ExtentTestNGListener.info("Mở menu User account");
        header.openUserMenu();
        ExtentTestNGListener.info("Mở trang profile");
        header.clickOptionUserMenu("Dashboard");
        page.waitForLoadState();

//        B4: mo modal update
        ExtentTestNGListener.info("Mở modal cập nhật thông tin user");
        profilePage.openUpdateModal();
        page.waitForLoadState();
//        B5: cap nhat profile
        ExtentTestNGListener.info("Thực hiện cập nhật số điện thoại");
        profilePage.updatePhone("0825800800");
        profilePage.clickUpdateButton();
        page.waitForLoadState();
        ExtentTestNGListener.info("Xác minh cập nhật thông tin người dùng thành công");
        boolean isUpdateSuccess = profilePage.isUpdateSuccess();
        Assert.assertTrue(isUpdateSuccess, "Update failed");
    }

//    Update email đã tồn tại
    @Test(priority = 3)
    public void testUpdateEmailInvalid(){
        //        B1: truy cập trang web
        ExtentTestNGListener.info("Truy cập trang web: " + TestConfig.getBaseUrl());
        page.navigate(TestConfig.getBaseUrl());
        page.waitForLoadState();

//        B2: Đăng nhập tài khoản
        ExtentTestNGListener.info("Mở form đăng nhập");
        loginPage.openSignInForm();
        ExtentTestNGListener.info("Đăng nhập với thông tin hợp lệ");
        loginPage.login(TestConfig.getLoginValidEmail(), TestConfig.getLoginValidPassword());
        page.waitForLoadState();

//        B3: mo profile
        ExtentTestNGListener.info("Mở menu User account");
        header.openUserMenu();
        ExtentTestNGListener.info("Mở trang profile");
        header.clickOptionUserMenu("Dashboard");
        page.waitForLoadState();

//        B4: mo modal update
        ExtentTestNGListener.info("Mở modal cập nhật thông tin user");
        profilePage.openUpdateModal();
        page.waitForLoadState();
//        B5: cap nhat profile
        ExtentTestNGListener.info("Thực hiện cập nhật email đã tồn tại");
        profilePage.updateEmail("testing07@gmail.com");
        profilePage.clickUpdateButton();
        ExtentTestNGListener.info("Xác minh hệ thống thông báo lỗi và không cho phép cập nhật");
        page.waitForLoadState();
        boolean hasErrorMessage = profilePage.hasErrorMessage("Email đã tồn tại !");
        Assert.assertTrue(hasErrorMessage, "Không hiển thị lỗi email đã tồn tại!");
    }


    @Test(priority = 4)
    public void testUploadAvatarSuccess(){
        //        B1: truy cập trang web
        ExtentTestNGListener.info("Truy cập trang web: " + TestConfig.getBaseUrl());
        page.navigate(TestConfig.getBaseUrl());
        page.waitForLoadState();

//        B2: Đăng nhập tài khoản
        ExtentTestNGListener.info("Mở form đăng nhập");
        loginPage.openSignInForm();
        ExtentTestNGListener.info("Đăng nhập với thông tin hợp lệ");
        loginPage.login(TestConfig.getLoginValidEmail(), TestConfig.getLoginValidPassword());
        page.waitForLoadState();

//        B3: mo profile
        ExtentTestNGListener.info("Mở menu User account");
        header.openUserMenu();
        ExtentTestNGListener.info("Mở trang profile");
        header.clickOptionUserMenu("Dashboard");
        page.waitForTimeout(3000);
        String avatarBeforeLink = profilePage.getAvatarLink();
        System.out.println("avatarBeforeLink: " + avatarBeforeLink);
//        B4: upload avatar
        String path = Paths.get("src/main/resources/image/avatar.png")
                .toAbsolutePath()
                .toString();
        ExtentTestNGListener.info("Thực hiện upload avatar mới");
        profilePage.uploadFile(path);
        ExtentTestNGListener.info("Xác minh avatar đã được cập nhật thành công");
        page.waitForTimeout(3000);
        String avatarAfterLink = profilePage.getAvatarLink();
        System.out.println(avatarAfterLink);
        Assert.assertNotEquals(avatarAfterLink, avatarBeforeLink, "Avatar has not been updated");
    }

//    Test case: kiểm tra định dạng file khi upload
    @Test(priority = 5)
    public void testInvalidFIleFormat(){
        //        B1: truy cập trang web
        ExtentTestNGListener.info("Truy cập trang web: "  + TestConfig.getBaseUrl());
        page.navigate(TestConfig.getBaseUrl());
        page.waitForLoadState();

    //        B2: Đăng nhập tài khoản
        ExtentTestNGListener.info("Mở form đăng nhập");
        loginPage.openSignInForm();
        ExtentTestNGListener.info("Đăng nhập với thông tin hợp lệ");
        loginPage.login(TestConfig.getLoginValidEmail(), TestConfig.getLoginValidPassword());
        page.waitForLoadState();

    //        B3: mo profile
        ExtentTestNGListener.info("Mở menu User account");
        header.openUserMenu();
        ExtentTestNGListener.info("Mở trang profile");
        header.clickOptionUserMenu("Dashboard");
    //        B4: upload avatar
        ExtentTestNGListener.info("Thực hiện upload file không hợp lệ");
        String path = Paths.get("src/main/resources/image/avatar1.txt")
                .toAbsolutePath()
                .toString();

        profilePage.uploadFile(path);
        ExtentTestNGListener.info("Xác minh hệ thống không cho phép upload file không hợp lệ");
        boolean hasError = profilePage.hasErrorMessage("Chỉ cho phép dịnh dạng (jpg, jpeg, png, gif)");
        Assert.assertTrue(hasError, "Hệ thống không hiển thị thông báo lỗi khi upload file sai định dạng!");
//        page.waitForTimeout(5000);

    }

//    TC: xem lịch sử đặt phòng
    @Test(priority = 6)
    public void testDisplayBookingList(){
        //        B1: truy cập trang web
        ExtentTestNGListener.info("Truy cập trang web: "  + TestConfig.getBaseUrl());
        page.navigate(TestConfig.getBaseUrl());
        page.waitForLoadState();

        //        B2: Đăng nhập tài khoản
        ExtentTestNGListener.info("Mở form đăng nhập");
        loginPage.openSignInForm();
        ExtentTestNGListener.info("Đăng nhập với thông tin hợp lệ");
        loginPage.login(TestConfig.getLoginValidEmail(), TestConfig.getLoginValidPassword());
        page.waitForLoadState();

        //        B3: mo profile
        ExtentTestNGListener.info("Mở menu User account");
        header.openUserMenu();
        ExtentTestNGListener.info("Mở trang profile");
        header.clickOptionUserMenu("Dashboard");

        ExtentTestNGListener.info("Xác minh hiển thị lịch sử đặt phòng");
        Assert.assertTrue(profilePage.isTitleDisplayed(), "Không hiển thị tiêu đề lịch sử đặt phòng");
        Assert.assertTrue(profilePage.getBookingCount() > 0, "User có booking nhưng không hiển thị");
        page.waitForTimeout(3000);
    }

}
