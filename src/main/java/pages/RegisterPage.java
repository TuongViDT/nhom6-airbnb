package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import pages.components.HeaderComponent;
import utils.TestConfig;

import java.util.Map;

public class RegisterPage extends BasePage{
    private HeaderComponent header;
    private static final String NAME_INPUT = "//input[@id='name']";
    private static final String EMAIL_INPUT = "//input[@id='email']";
    private static final String PASSWORD_INPUT = "//input[@id='password']";
    private static final String PHONE_NUMBER_INPUT = "//input[@id='phone']";
    private static final String BIRTHDAY_INPUT = "//input[@id='birthday']";
    private static final String GENDER_LIST = "//input[@id='gender']";
    private static final String GENDER_OPTION = "//div[@id='gender_list']/following-sibling::div//div[contains(@title,'%s')]";
    private static final String REGISTER_BUTTON = "//button[@type='submit' and contains(text(),'Đăng ký')]";
    private static final String ERROR_MESSAGE_NOTICE = "//div[contains(@class,'ant-message-notice-content')]//span[contains(text(),'Email đã tồn tại !')]";
    private static final String VALIDATION_ERROR = "//div[@id='%s_help']//div[contains(@class,'ant-form-item-explain-error')]";
    private static final String PICKER_YEAR_BUTTON = "//button[contains(@class,'ant-picker-year-btn')]";
    private static final String PICKER_MONTH_BUTTON = "//button[contains(@class,'ant-picker-month-btn')]";
    private static final String DAY_BUTTON = "//table[contains(@class,'ant-picker-content')]" +
            "//td[contains(@class,'ant-picker-cell-in-view')]//div[text()='%s']";
    private static final String DATE_PICKER = "//div[contains(@class,'ant-picker-panel-container')]";
    private static final String PICKER_DECADE_BUTTON = "//button[contains(@class,'ant-picker-decade-btn')]";
    private static final String YEAR_BUTTON = "//tbody//td[contains(@class,'ant-picker-cell-in-view')]//div[text()='%s']";
    private static final String MONTH_BUTTON = "//table[contains(@class,'ant-picker-content')]" +
            "//td[contains(@class,'ant-picker-cell-in-view')]//div[text()='%s']";
    private static final String PREV_BUTTON = "//button[contains(@class,'ant-picker-header-super-prev-btn')]";
    private static final String NEXT_BUTTON = "//button[contains(@class,'ant-picker-header-super-next-btn')]";


//
    public RegisterPage(Page page) {
        super(page);
        this.header = new HeaderComponent(page);
    }

    public void openRegisterForm(){
        page.waitForLoadState();
        header.openGuestMenu();
        header.clickOptionGuestMenu("Đăng ký");
        page.waitForSelector(NAME_INPUT);
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
    public void chooseMonthBirthday(String targetMonth){
        Locator month = page.locator(String.format(MONTH_BUTTON, targetMonth));
        month.waitFor();
        month.click();
    }
    public boolean isCalendarVisible(){
        page.waitForSelector(DATE_PICKER);
        return page.locator(PICKER_YEAR_BUTTON).isVisible()
                && page.locator(PICKER_MONTH_BUTTON).isVisible();
    }


    public void chooseYearBirthday(String targetYear){
        page.waitForSelector(PICKER_YEAR_BUTTON);
        page.click(PICKER_YEAR_BUTTON);
        page.waitForSelector(PICKER_DECADE_BUTTON);

        int year = Integer.parseInt(targetYear);
//        so sánh targetYear có nằm trong thập kỷ này không, nếu có chọn năm
        while (true){
            String decadeTitle = page.locator(PICKER_DECADE_BUTTON).innerText().trim();
            String[] parts = decadeTitle.split("-");
            int yearStart = Integer.parseInt(parts[0]);
            int yearEnd = Integer.parseInt(parts[1]);
            if(year >= yearStart && year <= yearEnd){
                page.locator(String.format(YEAR_BUTTON, targetYear)).click();
                break;
            }
            if(year < yearStart){
                page.waitForSelector(PREV_BUTTON);
                page.click(PREV_BUTTON);
            }else{
                page.waitForSelector(NEXT_BUTTON);
                page.click(NEXT_BUTTON);
            }
        }
    }

    public void chooseBirthDay(String birthdayText){
//        December 18, 2025
        String[] parts = birthdayText.split(" ");
        String targetMonthShort = parts[0].substring(0,3);
        String targetDay = parts[1].replace(",", "").trim();
        String targetYear = parts[2];
        page.waitForSelector(BIRTHDAY_INPUT);
        page.click(BIRTHDAY_INPUT);
        page.waitForSelector(DATE_PICKER);
        String currentYear = page.locator(PICKER_YEAR_BUTTON).innerText().trim();
        String currentMonth = page.locator(PICKER_MONTH_BUTTON).innerText().trim().substring(0,3);


//        so sánh năm, nếu khác năm thì chọn năm, ngược lại thì tiếp tục
        if(!currentYear.equals(targetYear)){
            chooseYearBirthday(targetYear);
        }
//        trường hợp nếu hiển thị lịch đầy thủ, title tháng năm thì kiểm tra xem tháng giống không
//        nếu không hiển thị lịch đầy đủ, hiển thị table có cell tháng thì chọn tháng
        if(isCalendarVisible()){
            if(!currentMonth.equals(targetMonthShort)){
                chooseMonthBirthday(targetMonthShort);
            }
        }else {
            chooseMonthBirthday(targetMonthShort);
        }
        Locator dayButton = page.locator(String.format(DAY_BUTTON, targetDay));
        dayButton.waitFor();
        dayButton.click();
        String birthday = page.locator(BIRTHDAY_INPUT).getAttribute("title");
        page.waitForTimeout(2000);
        System.out.println("Da chon ngay sinh: " + birthday);
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
        chooseBirthDay(birthday);
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
