package pages;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.nio.file.Paths;
import java.util.regex.Pattern;

public class ProfilePage extends BasePage{
    private static final String EDIT_PROFILE_BUTTON = "//h1[contains(text(),'Phòng đã thuê')]/preceding-sibling::button";
    private static final String UPDATE_BUTTON = "//div[@class='ant-modal-content']//button[@type='button']" +
            "//span[text()='Cập nhật']";
    private static final String EMAIL_INPUT = "//label[@title='Email']/../following-sibling::div//input[@id='email']";
    private static final String PHONE_INPUT = "//input[@id='phone']";
    private static final String NAME_INPUT = "//input[@id='name']";
    private static final String BIRTHDAY_INPUT = "//input[@id='birthday']";
    private static final String GENDER_INPUT = "//input[@id='gender']";
    private static final String GENDER_OPTION = "//div[@id='gender_list']//div[@aria-label='%s']";
    private static final String MESSAGE_NOTICE = "//div[@class='ant-message-notice-content']" +
            "//span[contains(text(),'Cập nhật thông tin thành công')]";
    private static final String ERROR_MESSAGE = "//div[@class='ant-message-notice-content']" +
            "//span[@aria-label='close-circle']/following-sibling::span";
    private static final String UPLOAD_AVATAR_MESSAGE = "//div[@class='ant-message-notice-content']" +
            "//span[contains(text(),'Cập nhật avatar thành công!')]";
    private static final String CHANGE_AVATAR_BUTTON = "//div[@class='ant-card-body']//button[contains(text(),'Cập nhật ảnh')]";
    private static final String CHOOSE_FILE_INPUT = "//input[@type='file']";
    private static final String UPLOAD_AVATAR_BUTTON = CHOOSE_FILE_INPUT + "/following-sibling::button[contains(text(),'Upload Avatar')]";
    private static final String BOOKING_HISTORY = "//h1[contains(text(),'Phòng đã thuê')]";
    private static final String EMPTY_MESSAGE = "//h1[contains(text(),'Phòng đã thuê')]/following-sibling::p";
    private static final String BOOKING_ITEM = "//div[@class='ant-card-body']/ancestor::a";
    private static final String AVATAR_IMG = "//button[contains(text(),'Cập nhật ảnh')]/../preceding-sibling::img";

    public ProfilePage(Page page){
        super(page);
    }

    public void openUpdateModal(){
        page.waitForSelector(EDIT_PROFILE_BUTTON);
        page.click(EDIT_PROFILE_BUTTON);
    }

    public void clickUpdateButton(){
        page.waitForSelector(UPDATE_BUTTON);
        page.click(UPDATE_BUTTON);
    }

    public void updateEmail(String email){
        Locator emailInput = page.locator(EMAIL_INPUT);
        emailInput.waitFor();
        emailInput.clear();
        emailInput.fill(email);
    }

    public void updatePhone(String phone){
        Locator phoneInput = page.locator(PHONE_INPUT);
        phoneInput.waitFor();
        phoneInput.clear();
        phoneInput.fill(phone);
    }

    public void updateGender(String gender){
        page.waitForSelector(GENDER_INPUT);
        page.click(GENDER_INPUT);
        String genderOption = String.format(GENDER_OPTION, gender);
        page.click(genderOption);
    }

    public void updateName(String name){
        Locator nameInput = page.locator(NAME_INPUT);
        nameInput.waitFor();
        nameInput.clear();
        nameInput.fill(name);
    }

    public void updateBirthday(String birthday){
        Locator birthdayInput = page.locator(BIRTHDAY_INPUT);
        birthdayInput.waitFor();
        birthdayInput.clear();
        birthdayInput.fill(birthday);
    }

    public boolean isUpdateSuccess(){
        page.waitForSelector(MESSAGE_NOTICE);
        return page.locator(MESSAGE_NOTICE).isVisible();
    }



    public void uploadFile(String filePath){
        page.waitForSelector(CHANGE_AVATAR_BUTTON);
        page.click(CHANGE_AVATAR_BUTTON);
        page.waitForSelector(CHOOSE_FILE_INPUT);
        page.locator(CHOOSE_FILE_INPUT).setInputFiles(Paths.get(filePath));
        page.waitForLoadState();
        page.click(UPLOAD_AVATAR_BUTTON);
        page.waitForLoadState();
    }

    public boolean hasUploadSuccessMessage(){
        page.waitForSelector(UPLOAD_AVATAR_MESSAGE);
        return page.locator(UPLOAD_AVATAR_MESSAGE).isVisible();
    }

    public String getAvatarLink(){
        Locator avatar = page.locator(AVATAR_IMG);
        avatar.waitFor();
        return avatar.getAttribute("src");
    }



//Chỉ cho phép dịnh dạng (jpg, jpeg, png, gif)

    public boolean validateFileFormat(String fileName){
        String[] allowedExtensions = {"png", "jpg", "jpeg", "gif"};
        for(String extension : allowedExtensions){
            if(fileName.toLowerCase().endsWith("." + extension)){
                return true;
            }
        }
        return false;
    }

    public boolean hasErrorMessage(String message){
        String error = String.format(ERROR_MESSAGE, message);
        page.waitForSelector(error);
        return page.locator(error).isVisible();
    }

    public boolean isEmptyMessage(){
        page.waitForSelector(EMPTY_MESSAGE);
        return page.locator(EMPTY_MESSAGE).isVisible();
    }

    public int getBookingCount() {
        page.waitForSelector(BOOKING_ITEM);
        System.out.println(page.locator(BOOKING_ITEM).count());
        return page.locator(BOOKING_ITEM).count();
    }
    public boolean isTitleDisplayed(){
        page.waitForSelector(BOOKING_HISTORY);
        return page.locator(BOOKING_HISTORY).isVisible();
    }

    public String getProfileUrl(){
        return page.url();
    }


}
