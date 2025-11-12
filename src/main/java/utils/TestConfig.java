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

    public static String getValidUsername(){
        return getProperty("test.username");
    }

    public static String getValidPassword(){
        return getProperty("test.password");
    }

    public static String getInvalidUsername(){
        return getProperty("test.invalid.username");
    }

    public static String getInvalidPassword(){
        return getProperty("test.invalid.password");
    }
}
