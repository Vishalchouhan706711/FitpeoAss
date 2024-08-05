package learningSelenium;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import junit.framework.Assert;

public class Fitpeo {
    WebDriver driver;
    JavascriptExecutor js;

    @BeforeClass
    public void setUp() {
        // Set up ChromeDriver path
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\HP\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test
    public void automateFitPeo() throws InterruptedException {
        // Step 1: Navigate to the FitPeo Homepage
        driver.get("https://www.fitpeo.com/");

        // Step 2: Navigate to the Revenue Calculator Page
        driver.findElement(By.linkText("Revenue Calculator")).click();

        // Step 3: Scroll down to the Slider section
        js = (JavascriptExecutor) driver;
        WebElement scroll = driver.findElement(By.xpath("(//div[@class='MuiBox-root css-19xu03j'])[1]"));
        js.executeScript("arguments[0].scrollIntoView(true);", scroll);
        System.out.println("user is able to Scroll upto element");

        // Step 4: Adjust the Slider to 820
        WebElement slider = driver.findElement(By.xpath("//input[@aria-valuenow='200']"));
        System.out.println("location of the slider: " + slider.getLocation()); // (803, 654)
        System.out.println("size of the slider: " + slider.getSize()); // (20, 20)

        Actions move = new Actions(driver);
        move.dragAndDropBy(slider, 94, 3).perform();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        // Step 5: Update the Text Field to 560
        Actions cleardata = new Actions(driver);
        cleardata.click(driver.findElement(By.xpath("//input[@type='number']")))
                .keyDown(Keys.CONTROL)
                .sendKeys("a")
                .keyUp(Keys.CONTROL)
                .sendKeys(Keys.BACK_SPACE)
                .build()
                .perform();
   
        driver.findElement(By.xpath("//input[@type='number']")).sendKeys("560");
     
        // Step 6: Validate Slider Value
        String sliderValue = slider.getAttribute("value");
        Assert.assertEquals(sliderValue, "560");

        // Step 7: Select CPT Codes
        driver.findElement(By.xpath("(//input[@class='PrivateSwitchBase-input css-1m9pwf3'])[1]")).click();
        driver.findElement(By.xpath("(//input[@class='PrivateSwitchBase-input css-1m9pwf3'])[2]")).click();
        driver.findElement(By.xpath("(//input[@class='PrivateSwitchBase-input css-1m9pwf3'])[3]")).click();
        driver.findElement(By.xpath("(//input[@class='PrivateSwitchBase-input css-1m9pwf3'])[8]")).click();

        // Step 8: Validate Total Recurring Reimbursement
        WebElement reimbursementHeader = driver.findElement(By.xpath("(//p[@class='MuiTypography-root MuiTypography-body1 inter css-hocx5c'])[4]")); // Update with actual ID
        String reimbursementValue = reimbursementHeader.getText();
        System.out.println("Reimbursement value: " + reimbursementValue);
        Assert.assertEquals(reimbursementValue, "$110700");

        if (reimbursementValue.equals("$110700")) {
            System.out.println("Total Recurring Reimbursement for all patients per month should display $110700");
        } else {
            System.out.println("Total Recurring Reimbursement for all patients per month but should display as $75600");
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public static void main(String[] args) {
        Fitpeo test = new Fitpeo();
        test.setUp();
        try {
            test.automateFitPeo();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            test.tearDown();
        }
    }
}