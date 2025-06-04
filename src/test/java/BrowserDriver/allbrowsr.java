package BrowserDriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;


public class allbrowsr {

    public static WebDriver driver;

    public static void launchbrow(String browser, String url) {
        try {
            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            options.addArguments("user-agent=Mozilla/5.0 (...)");
            options.addArguments("--disable-web-security");
            options.addArguments("--allow-running-insecure-content");

            driver = new ChromeDriver(options);
            Thread.sleep(2000);
            driver.get(url);
            Thread.sleep(3000);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Browser launch failed", e);
        }
    }
}

