package Login;

import BrowserDriver.ConfigReader;
import BrowserDriver.allbrowsr;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import java.io.File;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import Util.ClickUtils;
import Util.FormUtils;



public class Newlogin extends allbrowsr {

    public void newlogin(ScenarioReader.EnrollmentScenario scenario, String excelPath) {
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        try {
            driver.findElement(By.xpath("//label[contains(text(), 'First name')]/following-sibling::input")).sendKeys("Alex");
            driver.findElement(By.xpath("//label[contains(text(), 'Last name')]/following-sibling::input")).sendKeys("Runa");

            String email = EmailGenerator.generateRandomEmail("mrunalchim@gmail.com");
            scenario.generatedEmail = email;
            driver.findElement(By.xpath("//label[contains(text(), ' Email')]/following-sibling::input")).sendKeys(email);
            driver.findElement(By.xpath("//input[@data-id='termsAccepted' and @type='checkbox']")).click();

            TakesScreenshot t = (TakesScreenshot) driver;
            File src = t.getScreenshotAs(OutputType.FILE);
            File dest = new File("C:\\Users\\mruna\\Downloads\\mysch.jpg");
            FileUtils.copyFile(src, dest);

            WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement element = driver.findElement(By.xpath("//input[@data-id='privacyPolicyAccepted' and @type='checkbox']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
            wait1.until(ExpectedConditions.elementToBeClickable(element)).click();

            driver.findElement(By.xpath("//button[text()='Create account']")).click();

            // OTP
            String otp = GmailOTPReader.waitForLatestOtpEmail(60);
            driver.findElement(By.xpath("//input[@id='input-43']")).sendKeys(otp);
            driver.findElement(By.xpath("//button[text()='Verify code']")).click();

            // Cards
            WebDriverWait waitcard = new WebDriverWait(driver, Duration.ofSeconds(30));
            WebElement card1Option = waitcard.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//span[text()='" + scenario.card1 + "']")));
            waitcard.until(ExpectedConditions.elementToBeClickable(card1Option)).click();

            driver.findElement(By.xpath("//button[@title='Next']")).click();

            System.out.println("Running scenario with card2: " + scenario.card2);

            driver.findElement(By.xpath("//span[text()='" + scenario.card2 + "']")).click();
            driver.findElement(By.xpath("//button[@title='Next']")).click();

            List<String> skipCard3Options = Arrays.asList(
                    "I have not been diagnosed with gMG.",
                    "I have not been diagnosed with HPP.",
                    "They have not been diagnosed with gMG.",
                    "They have not been diagnosed with HPP."
            );

            WebDriverWait next = new WebDriverWait(driver, Duration.ofSeconds(10));

            if (scenario.card3 != null && !skipCard3Options.contains(scenario.card2)) {
                // Click card3
                System.out.println("Running scenario with card3: " + scenario.card3);

                WebElement card3 = next.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//span[text()='" + scenario.card3 + "']")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", card3);
                card3.click();

                // Then click 'Next'
                WebElement nextButton = next.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[@title='Next']")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", nextButton);
                nextButton.click();
            } else {
                // Skip card3 and click 'Finish'
                WebElement finishButton = driver.findElement(By.xpath("//button[text()='Finish']"));

                try {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", finishButton);
                    Thread.sleep(500);
                    if (driver.findElements(By.xpath("//*[text()='Home']")).isEmpty()) {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", finishButton);
                    }
                } catch (Exception e) {
                    System.out.println("JavaScript click failed: " + e.getMessage());
                }

            }

            // Address section
            List<String> minimalAddressOptions = Arrays.asList(
                    "I am undecided about starting treatment with STRENSIQ.",
                    "I am undecided about starting treatment with ULTOMIRIS.",
                    "I have not been diagnosed with gMG.",
                    "I have not been diagnosed with HPP."
            );

            List<String> extendedAddressOptions = Arrays.asList(
                    "They are currently being treated with STRENSIQ.",
                    "They are currently being treated with ULTOMIRIS.",
                    "They have discussed STRENSIQ with their physician and intend to start treatment.",
                    "They are currently being treated with a different prescription medicine and are considering switching to ULTOMIRIS.",
                    "They have discussed ULTOMIRIS with their physician and intend to start treatment."

            );

            List<String> minextendedAddressOptions = Arrays.asList(
                    "They are undecided about starting treatment with STRENSIQ.",
                    "They are undecided about starting treatment with ULTOMIRIS.",
                    "They have not been diagnosed with HPP.",
                    "They have not been diagnosed with gMG."

            );
            boolean isExtendedAddress = extendedAddressOptions.contains(scenario.card2) || extendedAddressOptions.contains(scenario.card3);
            boolean isMinimalAddress = minimalAddressOptions.contains(scenario.card2) || minimalAddressOptions.contains(scenario.card3);
            boolean isminextendedAddressOptions = minextendedAddressOptions.contains(scenario.card2) ||minextendedAddressOptions.contains(scenario.card3);

            if (isMinimalAddress) {
                FormUtils.enterZipCode(driver, "02210");
                //phone
                WebElement phoneInput = driver.findElement(
                        By.xpath("//label[text()='Primary phone number']//following-sibling::div[@class='slds-form-element__control slds-grow']/input"));
                phoneInput.sendKeys("9876545678");

                WebElement finishButton = new WebDriverWait(driver, Duration.ofSeconds(10))
                        .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[text()='Finish']")));

                ClickUtils.clickWithActionsAndJSRetry(driver, finishButton, 3, 1000);
            }

            else if (isExtendedAddress) {
                //phone
                driver.findElement(By.xpath("//label[text()='Primary phone number']/following-sibling::div[@class='slds-form-element__control slds-grow']/input")).sendKeys("9876789090");
                //relation to patient
                WebDriverWait rel = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement relOption = rel.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//label[text()='Relationship to patient']/following-sibling::div[@class='slds-form-element__control']//button/span")));
                relOption.click();


                driver.findElement(By.xpath("//span[text()='Grandparent']")).click();

                //first name
                driver.findElement(By.xpath("//label[text()='First Name']/following-sibling::div[@class='slds-form-element__control slds-grow']/input")).sendKeys("Patient");

                //last name
                driver.findElement(By.xpath("//label[text()='Last Name']/following-sibling::div[@class='slds-form-element__control slds-grow']/input")).sendKeys("pat");

                //add
                FormUtils.enterAddressLine1(driver, "abc");

                //zip
                FormUtils.enterZipCode(driver, "02210");

                //dob
                FormUtils.fillDateOfBirth(driver, wait1, "2012", "15");

                driver.findElement(By.xpath("//button[@title='Next']")).click();

                WebElement complete = driver.findElement(By.xpath("//button[@title='Complete enrollment']"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", complete);
                wait1.until(ExpectedConditions.elementToBeClickable(complete)).click();
            }

            else if(isminextendedAddressOptions){
                //zip
                FormUtils.enterZipCode(driver, "02210");

                //phone
                driver.findElement(By.xpath("//label[text()='Primary phone number']/following-sibling::div[@class='slds-form-element__control slds-grow']/input")).sendKeys("9876789090");

                //relation to patient
                WebDriverWait rel = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement relOption = rel.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//label[text()='Relationship to patient']/following-sibling::div[@class='slds-form-element__control']//button/span")));
                relOption.click();

                WebDriverWait relop = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement relOption1 = relop.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//span[text()='Grandparent']")));
                relOption1.click();
                //driver.findElement(By.xpath("//span[text()='Grandparent']")).click();

                WebElement finishButton = new WebDriverWait(driver, Duration.ofSeconds(10))
                        .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[text()='Finish']")));

                ClickUtils.clickWithActionsAndJSRetry(driver, finishButton, 3, 1000);
            }

            else {
                //address
                FormUtils.fillDefaultAddressFields(driver);
                //zip
                WebElement zipInput = driver.findElement(By.xpath("//label[text()='ZIP code']/following-sibling::div[@class='slds-form-element__control slds-grow']/input"));
                zipInput.sendKeys("02210");

               /* WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement zipOption = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//div[@data-value='02210']")));
                zipOption.click(); */

                WebElement phoneInput = driver.findElement(
                        By.xpath("//lightning-input[@data-id='AddressLine1']/following-sibling::div[@class='slds-grid slds-wrap slds-gutters']/div[2]//div[1]/div/input")
                );
                phoneInput.sendKeys("9876545678");

                WebElement calendarButton = driver.findElement(By.xpath("//label[text()='Date of birth']/following-sibling::div[@class='slds-form-element__control slds-input-has-icon slds-input-has-icon_right']/input"));
                calendarButton.click();
                Select yearSelect = new Select(driver.findElement(By.xpath("//select[@part='select']")));
                yearSelect.selectByVisibleText("2012");
                driver.findElement(By.xpath("//button[@title='Previous Month']")).click();
                driver.findElement(By.xpath("//span[@class='slds-day' and @role='button' and text()='15']")).click();

                driver.findElement(By.xpath("//button[@title='Next']")).click();
                WebElement complete = driver.findElement(By.xpath("//button[@title='Complete enrollment']"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", complete);
                wait1.until(ExpectedConditions.elementToBeClickable(complete)).click();
            }

            // Dashboard check and driver quit
            try {
                WebElement dashboard = new WebDriverWait(driver, Duration.ofSeconds(60))
                        .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='Welcome to OneSource']")));

                ScenarioReader.updateScenarioStatus(excelPath, scenario.rowIndex, "DONE", email);
            } catch (TimeoutException e) {
                ScenarioReader.updateScenarioStatus(excelPath, scenario.rowIndex, "PENDING", email);
            }
//            finally {
//                driver.quit();
//            }

        } catch (Exception e) {
            e.printStackTrace();
            ScenarioReader.updateScenarioStatus(excelPath, scenario.rowIndex, "FAILED", null);
            //driver.quit();
        }
    }

    public static void main(String[] args) {
        String[] excelPaths = {
                "src/test/java/resources/Strenciq_enrollment_scenarios.xlsx",
                "src/test/java/resources/Ultomiris_enrollment_scenarios.xlsx"
        };

        for (String excelPath : excelPaths) {
            List<ScenarioReader.EnrollmentScenario> scenarios = ScenarioReader.readScenarios(excelPath);
            String browser = ConfigReader.getProperty("browser");
            String url = excelPath.contains("Ultomiris") ? ConfigReader.getProperty("url") : ConfigReader.getProperty("url1");

            for (ScenarioReader.EnrollmentScenario scenario : scenarios) {
                if (scenario.status != null && (scenario.status.equalsIgnoreCase("PENDING") || scenario.status.equalsIgnoreCase("FAILED"))) {
                    try {
                        Newlogin test = new Newlogin();
                        launchbrow(browser, url);
                        test.newlogin(scenario, excelPath);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ScenarioReader.updateScenarioStatus(excelPath, scenario.rowIndex, "FAILED", scenario.generatedEmail);
                        driver.quit();
                    }
                }
            }
        }
    }
}
