package utils;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;

public class TestConfig {
    //    tao doi tuong chua thong tin trong file config
    private static Properties properties;
    //    noi chua file config
    private static final String CONFIG_FILE = "src/main/resources/config.properties";
    static {
        loadProperties();
    }

    //    ham load properties tu file config
    private static void loadProperties(){
        properties = new Properties(); //tao doi tuong properties
        try{
            FileInputStream input = new FileInputStream(CONFIG_FILE);
            properties.load(input);
            input.close();
        }catch (IOException e){
            System.out.println("Error loading properties file: " + e.getMessage());
        }
    }

    //    ham lay property cu the tu file config
    public static String getProperty(String key){
        return properties.getProperty(key);
    }

    //    ham lay browser type
    public static BrowserType getBrowserType(Playwright playwright){
        String browserType = getProperty("browser");
        if(browserType.equals("chrome")){
            return playwright.chromium();
        }

        return playwright.chromium();
    }

    //    ham setup size screen, record video
    public static Browser.NewContextOptions getNewContextOptions(){
//        tạo thư mục videos nếu chưa tồn tại
        try{
            Files.createDirectories(Paths.get("videos"));
        }catch (IOException ignored){}

        return new Browser.NewContextOptions()
                .setViewportSize(null)
                .setIgnoreHTTPSErrors(true)
                .setRecordVideoDir(Paths.get("videos"))
                .setRecordVideoSize(1280, 720);
    }

    public static BrowserType.LaunchOptions getLaunchOptions(){
        //setHeadless(false) chỉ dùng trên môi trường test
        //khi trên môi trường production đổi thành true, không cho show browser
        return new BrowserType.LaunchOptions()
                .setChannel("chrome")
                .setHeadless(false)
                .setArgs(Arrays.asList(
                        "--start-maximized"
                ));
    }

    //    ham lay base url
    public static String getBaseUrl(){
        return getProperty("baseUrl");
    }

    public static String getLoginValidEmail(){
        return getProperty("login.email");
    }

    public static String getLoginValidPassword(){
        return getProperty("login.password");
    }

    public static String getLoginValidEmail1(){
        return getProperty("login.email1");
    }

    public static String getLoginValidPassword1(){
        return getProperty("login.password1");
    }

    public static String getLoginInvalidEmail(){
        return getProperty("login.invalid.email");
    }

    public static String getLoginInvalidPassword(){
        return getProperty("login.invalid.password");
    }

    public static String getRegisterValidName(){return getProperty("register.name");}
    public static String getRegisterExistingEmail(){return getProperty("register.existing.email");}
    public static String getRegisterValidPassword(){return getProperty("register.password");}
    public static String getRegisterPhone(){return getProperty("register.phone");}
    public static String getRegisterBirthday(){return getProperty("register.birthday");}
    public static String getRegisterGender(){return getProperty("register.gender");}
}
