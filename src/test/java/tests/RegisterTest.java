package tests;

import listener.ExtentTestNGListener;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.RegisterPage;
import utils.TestConfig;

import java.util.List;
import java.util.Map;
@Listeners(ExtentTestNGListener.class)
public class RegisterTest extends BaseTest {

    private RegisterPage registerForm;

    @BeforeMethod
    public void init(){
        registerForm = new RegisterPage(page);
    }



//    TC_01: Đăng ký thất bại - Email đã tồn tại
    @Test
    public void testRegistrationFailsWithExistingEmail(){
//        B1: truy cap website
//        B2: mo menu user
//        B3: click option register
        ExtentTestNGListener.info("Truy cập trang web: " + TestConfig.getBaseUrl());
        page.navigate(TestConfig.getBaseUrl());
        page.waitForLoadState();
        ExtentTestNGListener.info("Mở form đăng ký");
        registerForm.openRegisterForm();
        ExtentTestNGListener.info("Thực hiện đăng ký với email đã tồn tại");
//        B4: Nhap du lieu vao cac field
        String name = TestConfig.getRegisterValidName();
        String email = TestConfig.getRegisterExistingEmail();
        String password = TestConfig.getRegisterValidPassword();
        String phone = TestConfig.getRegisterPhone();
        String birthday = TestConfig.getRegisterBirthday();
        registerForm.register(name, email, password, phone, birthday);
//        B6: Kiem tra thong bao
        page.waitForLoadState();
        ExtentTestNGListener.info("Xác minh hệ thống không cho phép đăng ký với thông tin không hợp lệ");
        boolean hasErrorMessage = registerForm.hasErrorMessage();
        Assert.assertTrue(hasErrorMessage);
    }


//    Validate các message lỗi hiển thị đúng
    @Test
    public void testValidationError(){
        ExtentTestNGListener.info("Truy cập trang web: " + TestConfig.getBaseUrl());
        page.navigate(TestConfig.getBaseUrl());
        page.waitForLoadState();
        ExtentTestNGListener.info("Mở form đăng ký");
        registerForm.openRegisterForm();
        ExtentTestNGListener.info("Thực hiện đăng ký khi thiếu dữ liệu");
//        B4: Nhap du lieu vao cac field
        String name = TestConfig.getRegisterValidName();
        String email = TestConfig.getRegisterExistingEmail();

        String phone = TestConfig.getRegisterPhone();
        String birthday = TestConfig.getRegisterBirthday();
        registerForm.register(name, email, "", "", birthday);
        page.waitForTimeout(3000);
        ExtentTestNGListener.info("Xác minh hiển thị message lỗi cho các field bắt buộc");
        Map<String, String> expected = registerForm.getExpectedErrorMessage();
        List<String> emptyFields = List.of("password", "phone");
        emptyFields.forEach(field -> {
            String actual = registerForm.getErrorMessage(field);
            Assert.assertEquals(actual, expected.get(field),
                    "Sai message tại field: " + field);
        });
    }

    @DataProvider(name = "invalidEmailData")
    public Object[][] invalidEmailData() {
        return new Object[][]{
                {"testing06gmail"},
                {"abc@"},
                {"@gmail.com"},
                {"abc@gmail"},
                {"abc@gmail..com"}
        };
    }


    @Test(dataProvider = "invalidEmailData")
    public void testEmailFormatValid(String email){
        ExtentTestNGListener.info("Truy cập trang web: " + TestConfig.getBaseUrl());
        page.navigate(TestConfig.getBaseUrl());
        page.waitForLoadState();
        ExtentTestNGListener.info("Mở form đăng ký");
        registerForm.openRegisterForm();
        ExtentTestNGListener.info("Nhập email");
//        B4: Nhap du lieu vao cac field
//        registerForm.enterEmail(email);
        registerForm.inputEmailAndBlurb(email);
        page.waitForTimeout(3000);
        ExtentTestNGListener.info("Xác minh validate message lỗi format email hiển thị đúng");
        boolean expectedIvalid = registerForm.checkEmailFormat(email);
        boolean actualErrorDisplayed = registerForm.hasValidationError();
        Assert.assertEquals(actualErrorDisplayed,expectedIvalid,
                "FAIL: Hệ thống xử lý sai định dạng email [" + email + "]");
        Assert.assertTrue(registerForm.getErrorMessage("email")
                        .contains("Vui lòng nhập đúng định dạng email"),
                "FAIL: Validate message sai nội dung với email [" + email + "]");
    }
}
