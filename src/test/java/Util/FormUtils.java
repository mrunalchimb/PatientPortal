package Util;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;

public class FormUtils {

    public static void enterAddressLine1(WebDriver driver, String address) {
        driver.findElement(By.xpath("//label[text()='Address (line 1)']/following-sibling::div[@class='slds-form-element__control slds-grow']/input"))
                .sendKeys(address);
    }

  /*  public static void enterZipCode(WebDriver driver, String zipCode) {
//        WebElement zipInput = driver.findElement(By.xpath("//label[text()='ZIP code']/following-sibling::div[@class='slds-form-element__control slds-grow']/input"));
//        zipInput.sendKeys(zipCode);

       /* WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement zipOption = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@data-value='" + zipCode + "']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", zipOption);

        zipOption.click();

    }  */
    public static void enterZipCode(WebDriver driver, String zipCode) {
        try {
            WebElement zipInput = driver.findElement(By.xpath("//label[text()='ZIP code']/following-sibling::div//input"));
            zipInput.clear();
            zipInput.sendKeys(zipCode);

            // Wait for dropdown to show the entered zip code option
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement zipOption = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[@data-value='" + zipCode + "']")));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", zipOption);
            zipOption.click();

        } catch (TimeoutException e) {
            System.out.println("❌ Zip code '" + zipCode + "' not found in dropdown.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void enterPhoneNumber(WebDriver driver, String phone) {
        WebElement phoneInput = driver.findElement(
                By.xpath("//lightning-input[@data-id='AddressLine1']/following-sibling::div[@class='slds-grid slds-wrap slds-gutters']/div[2]//div[1]/div/input"));
        phoneInput.sendKeys(phone);

    }

    public static void fillDateOfBirth(WebDriver driver, WebDriverWait wait, String year, String day) {
        WebElement calendarButton = driver.findElement(By.xpath("//label[text()='Date of birth']/following-sibling::div[@class='slds-form-element__control slds-input-has-icon slds-input-has-icon_right']/input"));
        calendarButton.click();

        Select yearSelect = new Select(driver.findElement(By.xpath("//select[@part='select']")));
        yearSelect.selectByVisibleText(year);

        driver.findElement(By.xpath("//button[@title='Previous Month']")).click();
        driver.findElement(By.xpath("//span[@class='slds-day' and @role='button' and text()='" + day + "']")).click();
    }

    public static void fillDefaultAddressFields(WebDriver driver) {
        enterAddressLine1(driver, "123, ABC Street");
        enterZipCode(driver, "02210");
        enterPhoneNumber(driver, "9876545678");
    }
}
