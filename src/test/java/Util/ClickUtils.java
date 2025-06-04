package Util; // adjust this if your package is different

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

public class ClickUtils {

    public static void clickWithActionsAndJSRetry(WebDriver driver, WebElement element, int retries, int delayMs) {
        Actions actions = new Actions(driver);
        JavascriptExecutor js = (JavascriptExecutor) driver;

        for (int i = 0; i < retries; i++) {
            try {
                System.out.println("Click attempt " + (i + 1));
                actions.moveToElement(element).perform();
                js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
                js.executeScript("arguments[0].click();", element);
                Thread.sleep(500); // optional: allow UI to respond
                return;
            } catch (Exception e) {
                System.out.println("Click attempt " + (i + 1) + " failed: " + e.getMessage());
                try {
                    Thread.sleep(delayMs);
                } catch (InterruptedException ignored) {}
            }
        }

        throw new RuntimeException("Clicking element failed after " + retries + " retries");
    }
}
