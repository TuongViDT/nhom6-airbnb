package pages.components;

import com.microsoft.playwright.Page;
import pages.BasePage;

public class HeaderComponent extends BasePage {
    private static final String GUEST_MENU_ICON = "//img[@class='h-10']";
    private static final String USER_MENU_ICON = "id=user-menu-button";
    private static final String MENU_OPTION_GUEST = "//ul[@aria-labelledby='user-menu-button']//button[contains(text(),'%s')]";
    private static final String MENU_OPTION_USER = "//button[@id='user-menu-button']/following-sibling::div//a[contains(text(),'%s')]";
    private static final String LOGOUT_BUTTON = "//button[@id='user-menu-button']/following-sibling::div//button[contains(text(),'Sign out')]";
    public HeaderComponent(Page page){
        super(page);
    }


//moử menu user (login hoặc guest)
    public void openGuestMenu(){
        page.waitForSelector(GUEST_MENU_ICON);
        page.click(GUEST_MENU_ICON);
    }

    public void openUserMenu(){
        page.waitForSelector(USER_MENU_ICON);
        page.click(USER_MENU_ICON);
    }

    public boolean isLoggedIn(){
        page.waitForSelector(USER_MENU_ICON);
        return page.locator(USER_MENU_ICON).isVisible();
    }

    public boolean isGuest(){
        page.waitForSelector(GUEST_MENU_ICON);
        return page.locator(GUEST_MENU_ICON).isVisible();
    }

    public void clickOptionGuestMenu(String optionText){
        String guestOption = String.format(MENU_OPTION_GUEST, optionText);
        page.click(guestOption);
    }

    public void clickOptionUserMenu(String optionText){
        String userOption = String.format(MENU_OPTION_USER, optionText);
        page.click(userOption);
        page.waitForLoadState();
    }

    public void clickLogout(){
        page.waitForSelector(LOGOUT_BUTTON);
        page.click(LOGOUT_BUTTON);
    }












}
