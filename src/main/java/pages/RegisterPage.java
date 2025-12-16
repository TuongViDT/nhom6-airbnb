package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import pages.components.HeaderComponent;
import utils.TestConfig;

import java.util.Map;

public class RegisterPage extends BasePage{
    private HeaderComponent header;
    private static final String NAME_INPUT = "[id='name']";
    private static final String EMAIL_INPUT = "[id=\"email\"]";
    private static final String PASSWORD_INPUT = "[id=\"password\"]";
    private static final String PHONE_NUMBER_INPUT = "[id=\"phone\"]";
    private static final String BIRTHDAY_INPUT = "[id=\"birthday\"]";
    private static final String GENDER_LIST = "[id=\"gender\"]";
    private static final String REGISTER_FORM = "//div[@role=\"dialog\"]//h2[contains(text(),'Đăng ký tài khoản Airbnb')]";
    private static final String GENDER_OPTION = "//div[@id='gender_list']/following-sibling::div//div[contains(@title,'%s')]";
    private static final String REGISTER_BUTTON = "//button[@type='submit' and contains(text(),'Đăng ký')]";
    private static final String ERROR_MESSAGE_NOTICE = "//div[contains(@class,'ant-message-notice-content')]//span[contains(text(),'Email đã tồn tại !')]";
    private static final String VALIDATION_ERROR = "//div[@id='%s_help']//div[contains(@class,'ant-form-item-explain-error')]";

//
    public RegisterPage(Page page) {
        super(page);
        this.header = new HeaderComponent(page);
    }


    public void openRegisterForm(){
        page.waitForLoadState();
        header.openGuestMenu();
        header.clickOptionGuestMenu("Đăng ký");
        page.waitForSelector(REGISTER_FORM);
    }

    public void enterName(String name){
        page.waitForSelector(NAME_INPUT);
        page.fill(NAME_INPUT, name);
        System.out.println("Da fill name vao input");
    }

    public void enterEmail(String email){
        page.waitForSelector(EMAIL_INPUT);
        page.fill(EMAIL_INPUT, email);
        System.out.println("Da fill email vao input");
    }

    public void enterPassword(String password){
        page.waitForSelector(PASSWORD_INPUT);
        page.fill(PASSWORD_INPUT, password);
        System.out.println("Da fill password vao input");
    }

    public void enterPhoneNumber(String phoneNumber){
        page.waitForSelector(PHONE_NUMBER_INPUT);
        page.fill(PHONE_NUMBER_INPUT, phoneNumber);
        System.out.println("Da fill phone number vao input");
    }

    public void enterBirthDay(String birthday){
        page.waitForSelector(BIRTHDAY_INPUT);
        page.fill(BIRTHDAY_INPUT, birthday);
        System.out.println("Da fill birthday vao input");
    }

    public void selectGender(){
        String gender = String.format(GENDER_OPTION, TestConfig.getRegisterGender());
        page.waitForSelector(GENDER_LIST);
        page.click(GENDER_LIST);
        page.waitForSelector(gender);
        page.click(gender);
        System.out.println("Da chon gender");
    }

    public void clickRegisterButton(){
        page.waitForSelector(REGISTER_BUTTON);
        page.click(REGISTER_BUTTON);
        System.out.println("Da click register button");
    }

    public void register(String name,
                         String email,
                         String password,
                         String phoneNumber,
                         String birthday){
        enterName(name);
        enterEmail(email);
        enterPassword(password);
        enterPhoneNumber(phoneNumber);
        enterBirthDay(birthday);
        selectGender();
        clickRegisterButton();
        page.waitForLoadState();
    }

    public boolean hasErrorMessage(){
        page.waitForSelector(ERROR_MESSAGE_NOTICE);
        return page.locator(ERROR_MESSAGE_NOTICE).isVisible();
    }

    public Map<String, String> getExpectedErrorMessage(){
            return Map.of(
                "name", "Vui lòng không bỏ trống",
                "email", "Vui lòng không bỏ trống",
                "password", "Vui lòng không bỏ trống",
                "phone", "Vui lòng không bỏ trống",
                "birthday", "Vui lòng chọn ngày sinh"
        );
    }


    public String getErrorMessage(String field){
        String locator = String.format(VALIDATION_ERROR, field);
        return page.locator(locator).innerText().trim();
    }


    public boolean checkEmailFormat(String email){
        return email != null &&
                !email.matches("^(?!.*\\\\.\\\\.)[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\\\.[A-Za-z]{2,}$");
    }

    public boolean hasValidationError(){
        String errorMessage = String.format(VALIDATION_ERROR, "email");
        return page.locator(errorMessage).isVisible();
    }

    public void inputEmailAndBlurb(String email){
        enterEmail(email);
        Locator emailInput = page.locator(EMAIL_INPUT);
        emailInput.press("Tab");
    }















}
