import org.junit.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MailHelperTest {

    @Test
    public void testMail() {
        assertTrue(MailHelper.sendMail(Constants.APPLICATION_MAILER, "<h1>Hello please check the chart</h1>", "<h1>Hello please check the chart</h1>"));
    }

    @Test
    public void testMailWithScreenShot() {
        String userHome = System.getProperty("user.home");
        System.setProperty("webdriver.chrome.driver", userHome + "\\IdeaProjects\\zelter\\src\\main\\resources\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://kite.zerodha.com/");
        driver.manage().window().maximize();
        String screenshotBase64 = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BASE64);
        System.out.println(screenshotBase64);
        driver.close();
        assertTrue(MailHelper.sendMailWithImagesToTeam(
                "<h1>Hello please check the chart</h1>", "<h1>Hello please check the chart</h1>",screenshotBase64));
    }
}