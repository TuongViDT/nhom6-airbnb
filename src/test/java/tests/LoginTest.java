package tests;

import listener.ExtentTestNGListener;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.LoginPage;
import pages.components.HeaderComponent;
import utils.TestConfig;

@Listeners(ExtentTestNGListener.class)
public class LoginTest extends BaseTest{
    private LoginPage loginPage;
    private HeaderComponent header;

    @BeforeMethod
    public void init() {
        loginPage = new LoginPage(page);
        header = new HeaderComponent(page);
    }

    //    TC_03: Đăng nhập thành công
    @Test(priority = 1)
    public void testLoginWithValidUser(){
        ExtentTestNGListener.info("Truy cập trang web: " + TestConfig.getBaseUrl());
        page.navigate(TestConfig.getBaseUrl());
        page.waitForLoadState();
        ExtentTestNGListener.info("Mở form đăng nhập");
        loginPage.openSignInForm();
        ExtentTestNGListener.info("Thực hiện đăng nhập với thông tin hợp lệ");
        String email = TestConfig.getLoginValidEmail();
        String password = TestConfig.getLoginValidPassword();
        loginPage.login(email, password);
        page.waitForLoadState();
        ExtentTestNGListener.info("Xác minh hệ thống đăng nhập thành công");
        boolean isLoginSuccess = header.isLoggedIn();
        Assert.assertTrue(isLoginSuccess);
        page.waitForTimeout(3000);
    }

    //    TC_04: Đăng nhập thất bại - Sai email/password
    @Test(priority = 2)
    public void testLoginWithInvalidUser(){
        ExtentTestNGListener.info("Truy cập trang web:  " + TestConfig.getBaseUrl());
        page.navigate(TestConfig.getBaseUrl());
        page.waitForLoadState();
        ExtentTestNGListener.info("Mở form đăng nhập");
        loginPage.openSignInForm();
        ExtentTestNGListener.info("Thực hiện đăng nhập với thông tin không hợp lệ");
        String invalidEmail = TestConfig.getLoginInvalidEmail();
        String invalidPassword = TestConfig.getLoginInvalidPassword();
        loginPage.login(invalidEmail, invalidPassword);
        page.waitForLoadState();
        ExtentTestNGListener.info("Xác minh hệ thống không cho phép đăng nhập với thông tin không hợp lệ");
        boolean hasErrorMessage = loginPage.hasErrorMessage();
        Assert.assertTrue(hasErrorMessage);
        page.waitForTimeout(3000);
    }

}
