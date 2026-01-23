package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class HomePage extends BasePage{
    private static final String LOCATION_LABEL = "//p[contains(text(),'Địa điểm')]/parent::div[contains(@class,'col-span-3')]";
    private static final String DATE_LABEL = LOCATION_LABEL + "/following-sibling::div[contains(@class,'col-span-4')]";
    private static final String ADD_GUEST_FIELD = "//p[contains(text(),'Thêm khách')]/parent::div[contains(@class,'col-span-3')]";
    private static final String SEARCH_BUTTON = "//span[@aria-label='search']/parent::div";
    private static final String LOCATION_OPTION = "//h1[contains(text(),'Tìm kiếm địa điểm')]" +
            "/following-sibling::div//p[contains(text(),'%s')]/preceding-sibling::div";
    private static final String NONE_LOCATION = "//h1[contains(text(),'Tìm kiếm địa điểm')]" +
            "/following-sibling::div//div[contains(text(),'None')]";
    private static final String DATE_DISPLAYED = "//div[contains(@class,'rdrMonthName')]";
    private static final String DAY_BUTTON = "//div[@class='rdrMonth'][%d]//span[text()='%s']" +
            "/ancestor::button[not(contains(@class,'rdrDayPassive'))]";
    private static final String MONTH_DROPDOWN = "//span[@class='rdrMonthPicker']//select";
    private static final String YEAR_DROPDOWN = "//span[@class='rdrYearPicker']//select";
    private static final String INCREASE_BUTTON = "//div[text()='Khách']/following-sibling::div/button[.='+']";
    private static final String DECREASE_BUTTON = "//div[text()='Khách']/following-sibling::div/div[.='–']";
    private static final String GUEST_NUMBER = "//div[text()='Khách']/following-sibling::div/div";
    private static final String LOCATION_CARD = "//a[@href='/rooms/ho-chi-minh' and .//h2[text()='Hồ Chí Minh']]";
    private static final String LOGOUT_NOTICE = "//div[contains(@class,'ant-message-notice-content')]" +
            "//span[contains(text(),'Đăng xuất thành công')]";
    private static final String FILTER_PRICE_BUTTON = "//div[contains(@class,'container')]//button[.='Giá']";
    private static final String BANNER = "//div[contains(@class,'banner-responsive')]";

    public HomePage(Page page){
        super(page);
    }


    public void chooseLocation(String locationOption){
        page.waitForSelector(LOCATION_LABEL);
        page.click(LOCATION_LABEL);
        String location = String.format(LOCATION_OPTION, locationOption);
        page.waitForSelector(location);
        scrollToElement(location);
        page.click(location);
    }

    public void noneLocation(){
        page.click(LOCATION_LABEL);
        page.waitForSelector(NONE_LOCATION);
        page.click(NONE_LOCATION);
    }

    public void openDatePicker(){
        page.waitForSelector(DATE_LABEL);
        page.click(DATE_LABEL);
    }

    public void selectMonth(String month){
        Locator monthSelect = page.locator(MONTH_DROPDOWN);
        monthSelect.waitFor();
        monthSelect.selectOption(month);

    }

    public void selectYear(String year){
        Locator yearDropdown = page.locator(YEAR_DROPDOWN);
        yearDropdown.waitFor();
        yearDropdown.selectOption(year);
    }

    public void pickDate(String dateText){
//        tìm locator title calender

//        tách tháng năm hiển thị trên calender ==> {tháng, năm}

//        so sánh ngày input vs ngày displayed
//        nếu giống thì click chọn ngày trong calender đó
//        Nếu cả 2 calendar không có thì click dropdown năm tháng
//        chọn tháng năm giống input, rồi chọn ngày

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
        LocalDate date = LocalDate.parse(dateText, formatter);
        System.out.println(date);
//        Ket qua: Nov 2, 2025
//        Lay thang, ngay, nam tu date
        String targetMonth = date.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        System.out.println(targetMonth);

        String targetDay = String.valueOf(date.getDayOfMonth());
        String targetYear = String.valueOf(date.getYear());

        int count = page.locator(DATE_DISPLAYED).count();
        boolean foundInDisplayedCalendar = false;

        for (int i = 0; i < count; i++){
//            lấy tháng năm hiển thị trên calender
            String monthLabel = page.locator(DATE_DISPLAYED).nth(i).innerText().trim();
            String[] parseDisplayed = monthLabel.split(" ");
            String monthDisplayed = parseDisplayed[0];
            String yearDisplayed = parseDisplayed[1];
//            so sanh voi input
            if(targetMonth.equals(monthDisplayed) && targetYear.equals(yearDisplayed)){
                System.out.println("Tìm thấy target month/year trong calender: " + monthLabel);
                Locator dayButton = page.locator(String.format(DAY_BUTTON, (i + 1), targetDay));
                dayButton.click();
                foundInDisplayedCalendar = true;
                break;
            }
        }
        if(!foundInDisplayedCalendar){
            String targetMonthFull =
                    date.format(DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH));

            selectYear(targetYear);
            selectMonth(targetMonthFull);
            page.waitForLoadState();
            Locator dayButton = page.locator(String.format(DAY_BUTTON, 1, targetDay));
            dayButton.click();
        }
    }


    /*
    lấy ngày UI hiển thị
    Tách ngày tháng năm input
    so sánh ngày UI và ngày input
    nếu khác nhau thực hiện click chọn năm tháng
    chọn ngày
    ===> check in
    ===> check out tương tự

     */


    public void filterNumberOfGuests(int guestInput){
        int guestDisplayed = Integer.parseInt(page.locator(GUEST_NUMBER).innerText().trim());

        if(guestInput <= 0){
            throw new IllegalArgumentException("Guest number must be greater than 0");
        }
        if(guestInput == guestDisplayed){
            return;
        }
//        Tang khach
        while (guestDisplayed < guestInput){
            page.click(INCREASE_BUTTON);
            guestDisplayed++;
        }

        while (guestDisplayed > guestInput){
            page.click(DECREASE_BUTTON);
            guestDisplayed--;
        }

    }

    public void clickLocation(){
        page.waitForSelector(LOCATION_CARD);
        page.click(LOCATION_CARD);
    }
    public void clickAddGuestButton(){
        page.waitForSelector(ADD_GUEST_FIELD);
        page.click(ADD_GUEST_FIELD);
    }

    public void clickSearchButton(){
        page.waitForSelector(SEARCH_BUTTON);
        page.click(SEARCH_BUTTON);
        page.waitForLoadState();
    }

    public boolean isSearchValid(String expectedLocation){
        String currentUrl = page.url().toLowerCase();
        return currentUrl.contains(expectedLocation.toLowerCase());

    }


    public boolean isLogoutSuccess(){
        page.waitForSelector(LOGOUT_NOTICE);
        return page.locator(LOGOUT_NOTICE).isVisible();
    }

//    public boolean checkUserAccount(){
//        page.waitForSelector(USER_ACCOUNT);
//        return page.locator(USER_ACCOUNT).isVisible();
//    }



    public boolean isPriceButtonVisible(){
        return page.locator(FILTER_PRICE_BUTTON).isVisible();
    }

    public boolean isPriceButtonClickable(){
        return page.locator(FILTER_PRICE_BUTTON).isEnabled();
    }

    public void clickPriceButton(){
        page.waitForSelector(FILTER_PRICE_BUTTON);
        page.click(FILTER_PRICE_BUTTON);
    }

    public boolean isBannerVisible(){
        page.waitForSelector(BANNER);
        return page.locator(BANNER).isVisible();
    }





}
