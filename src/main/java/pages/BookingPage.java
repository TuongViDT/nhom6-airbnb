package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class BookingPage extends BasePage{
    private HomePage homePage;
    private static final String CHECK_IN_BUTTON = "//div[contains(@class,'font-bold') and contains(text(),'Nhận phòng')]/parent::div";
    private static final String DATE_LABEL = "//div[contains(@class,'rdrMonthName')]";
    private static final String DAY_BUTTON = "//span[text()='%s']/parent::span[not(contains(@class,'rdrDayPassive'))]/..";
    private static final String CLOSE_BUTTON = "//div[contains(@class,'ant-modal-title')]//button[text()='Close']";
    private static final String PLUS_BUTTON = "//div[text()='Khách']/following-sibling::div/div[.='+']";
    private static final String MINUS_BUTTON = "//div[text()='Khách']/following-sibling::div/div[.='–']";
    private static final String WARNING_MESSAGE = "//div[contains(@class,'ant-message-notice-content')]" +
            "//span[contains(text(),'Phải có tối thiểu 1 khách!')]";
    private static final String BOOKING_BUTTON = "//button[text()='Đặt phòng']";
    private static final String CONFIRMATION_BUTTON = "//h5/following::div//button[text()='Xác nhận']";
    private static final String SUCCESS_BOOKING_MESSAGE = "//div[contains(@class,'ant-notification-notice-success')]" +
            "//div[contains(text(),'Thêm mới thành công!')]";
    private static final String NOTICE_WARNING = "//div[contains(@class,'ant-notification-notice-warning')]" +
            "//div[contains(text(),'Vui lòng đăng nhập để tiếp tục đặt phòng.')]";
    private static final String PRICE_PER_NIGHT = "//div[contains(@class,'flex-wrap')]//div[contains(text(),'night')]//span";




    public BookingPage(Page page){
        super(page);
        this.homePage = new HomePage(page);
    }


    public void openPickDate() {
//        B1: click ô check in
        page.waitForSelector(CHECK_IN_BUTTON);
        page.click(CHECK_IN_BUTTON);
    }

    public void chooseDateBooking(String dateText){
//        tach ngay thang nam input
        String[] parts = dateText.split(" ");
        String targetMonth = parts[0];
        String targetMonthShort = targetMonth.substring(0, 3);
        String targetDay = parts[1].replace(","," ").trim();
        String targetYear = parts[2];
//        tach thang nam displayed
        String dateTitle = page.locator(DATE_LABEL).innerText().trim();
        String[] partsDisplayed = dateTitle.split(" ");
        String monthDisplayed = partsDisplayed[0];
        String yearDisplayed = partsDisplayed[1];
//            so sanh voi input
        if(targetMonthShort.equals(monthDisplayed) && targetYear.equals(yearDisplayed)){
            System.out.println("Tìm thấy target month/year trong calender: " + dateTitle);
            Locator dayButton = page.locator(String.format(DAY_BUTTON, targetDay));
            dayButton.click();
        }else {
            homePage.selectYear(targetYear);
            homePage.selectMonth(targetMonth);

            Locator dayButton = page.locator(String.format(DAY_BUTTON, targetDay));
            dayButton.click();
        }
    }

    public void clickCloseButton(){
        page.waitForSelector(CLOSE_BUTTON);
        page.click(CLOSE_BUTTON);
    }


    public int getCurrentGuestNumber(){
        String guestCount = MINUS_BUTTON + "/following-sibling::div[text()]";
        return Integer.parseInt(page.locator(guestCount)
                .innerText().replace(" khách", "").trim());
    }

    public void setGuests(int numberOfGuests){
        int current = getCurrentGuestNumber();

        if(numberOfGuests <= 0){
            throw new IllegalArgumentException("Guest number must be greater than 0");
        }
//        Tang khach
        while (current < numberOfGuests){
            page.click(PLUS_BUTTON);
            page.waitForTimeout(200);
            int newValue = getCurrentGuestNumber();
            if(newValue == current){
                System.out.println("Đã đạt tới số khách tối đa!");
                break;
            }

            current = newValue;
        }

        while (current > numberOfGuests){
            page.click(MINUS_BUTTON);
            page.waitForTimeout(200);
            int newValue = getCurrentGuestNumber();
            if(newValue == current){
                System.out.println("Phải có tối thiểu 1 khách");
                break;
            }
            current = newValue;
        }

    }

    public String getGuestWarning(){
        Locator warning = page.locator(WARNING_MESSAGE);
        if(warning.isVisible()){
            return warning.innerText().trim();
        }
        return "";
    }

    public void clickBookingButton(){
        page.waitForSelector(BOOKING_BUTTON);
        page.click(BOOKING_BUTTON);
    }

    public void clickConfirm(){
        page.waitForSelector(CONFIRMATION_BUTTON);
        page.click(CONFIRMATION_BUTTON);
    }

    public boolean isBookingSuccess(){
        Locator successMessage = page.locator(SUCCESS_BOOKING_MESSAGE);
        successMessage.waitFor();
        return successMessage.isVisible();
    }

    public boolean hasNoticeWarning(){
        Locator noticeWarning = page.locator(NOTICE_WARNING);
        noticeWarning.waitFor();
        return noticeWarning.isVisible();
    }

    public boolean hasError(){
        Locator errorMessage = page.locator("//div[contains(@class,'ant-notification-notice-warning')]");
        errorMessage.waitFor();
        return errorMessage.isVisible();
    }

    public int getPricePerNight(){
        String price = page.locator(PRICE_PER_NIGHT).innerText().replace("$","").trim();
        return Integer.parseInt(price);
    }

    public int priceNights(){
        return getPricePerNight() * calculateNights();
    }

    public int priceTotal(){
        String cleanFee = page.locator("//p[@class='underline text-base' and contains(text(),'Cleaning fee')]" +
                "/following-sibling::p").innerText().replace("$","").trim();
        int serviceFee = Integer.parseInt(cleanFee);
        return serviceFee + priceNights();
    }

    public int calculateNights(){
        String checkIn = page.locator("//div[@class='font-bold' and contains(text(),'Nhận phòng')]" +
                "/following-sibling::div").innerText().trim();
        String checkOut = page.locator("//div[@class='font-bold' and contains(text(),'Trả phòng')]" +
                "/following-sibling::div").innerText().trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate checkInDate = LocalDate.parse(checkIn, formatter);
        LocalDate checkOutDate = LocalDate.parse(checkOut, formatter);
        return Math.toIntExact(ChronoUnit.DAYS.between(checkInDate, checkOutDate));
    }

    public int checkPrice(){
        String total = page.locator("//div//p[contains(text(),'Total before taxes')]" +
                "/following-sibling::p").innerText().trim();
        return Integer.parseInt(total);
    }

}
