package pages;

import com.microsoft.playwright.Page;
import pages.components.HeaderComponent;

public class LoginPage extends BasePage{
    private HeaderComponent header;
    private static final String LOGIN_FORM = "//div[@role='dialog']//h2[contains(text(),'Đăng nhập Airbnb')]";
    private static final String EMAIL_INPUT = "//input[@id='email']";
    private static final String PASSWORD_INPUT = "//input[@id='password']";
    private static final String LOGIN_BUTTON = "//button[@type='submit' and contains(text(),'Đăng nhập')]";
    private static final String ERROR_MESSAGE_NOTICE = "//div[contains(@class,'ant-message-notice-content')]" +
            "//span[contains(text(),'Email hoặc mật khẩu không đúng !')]";

    public LoginPage(Page page){
        super(page);
        this.header = new HeaderComponent(page);
    }

    /*Mo form dang nhap
    1. Truy cập trang web airbnb
    2. click user menu icon
    3. click sign in button
     */

    /*sign in
    1. nhap email
    2. nhap password
    3. click button Dang nhap
     */

    public void openSignInForm(){
        header.openGuestMenu();
        header.clickOptionGuestMenu("Đăng nhập");
        page.waitForSelector(EMAIL_INPUT);
    }

    public void enterEmail(String email){
        page.waitForSelector(EMAIL_INPUT);
        page.fill(EMAIL_INPUT, email);
        System.out.println("Da nhap email");
    }

    public void enterPassword(String password){
        page.waitForSelector(PASSWORD_INPUT);
        page.fill(PASSWORD_INPUT, password);
        System.out.println("Da nhap password");
    }

    public void clickLoginButton(){
        page.waitForSelector(LOGIN_BUTTON);
        page.click(LOGIN_BUTTON);
        System.out.println("Da click login button");
    }

    public void login(String email, String password){
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
        page.waitForLoadState();
    }

    public boolean hasErrorMessage(){
        page.waitForSelector(ERROR_MESSAGE_NOTICE);
//        int countFindErrorSelector = page.locator(ERROR_MESSAGE_NOTICE).count();
//        return countFindErrorSelector > 0;
        return page.locator(ERROR_MESSAGE_NOTICE).isVisible();
    }








}


