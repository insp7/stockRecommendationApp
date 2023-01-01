import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Executor {
    public static void main(String[] args) throws InterruptedException {
        List<String> stockStatusOutput = new ArrayList();
        String userHome = System.getProperty("user.home");
        System.setProperty("webdriver.chrome.driver", userHome + "\\IdeaProjects\\StockRecommendationApp\\src\\main\\resources\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();


        driver.get("https://kite.zerodha.com/");

        driver.manage().window().maximize();


        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("userid")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("password")));


        /*First Login Page*/
        WebElement email = driver.findElement(By.id("userid"));
        WebElement password = driver.findElement(By.id("password"));
        email.sendKeys(Constants.KITE_APPLICATION_ID);
        password.sendKeys(Constants.KITE_PASSWORD_ID);
        WebElement submit = driver.findElement(By.xpath("//*[@id=\"container\"]/div/div/div/form/div[4]/button"));
        submit.click();

        /*Second Login Page*/
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pin")));
        WebElement pin = driver.findElement(By.id("pin"));
        pin.sendKeys(Constants.PIN);
        submit = driver.findElement(By.xpath("//*[@id=\"container\"]/div/div/div/form/div[3]/button"));
        submit.click();

        /*Loop through all added stocks*/
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("vddl-draggable")));
        System.out.println("All Stocks should be loaded in list");

        WebElement watchList = driver.findElement(By.className("vddl-list"));
        List<WebElement> watchListStocks = watchList.findElements(By.className("vddl-draggable"));
        loopAllStocks(driver, wait, watchListStocks, stockStatusOutput);

        Thread.sleep(1000);
        WebElement watchListSelector = driver.findElement(By.className("marketwatch-selector"));
        watchListSelector.findElement(By.xpath("./li[2]")).click();
        Thread.sleep(1000);
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("vddl-draggable")));
         watchList = driver.findElement(By.className("vddl-list"));
         watchListStocks = watchList.findElements(By.className("vddl-draggable"));

        loopAllStocks(driver, wait, watchListStocks, stockStatusOutput);

        driver.close();
        try {
            Path out = Paths.get("output.txt");

            Files.write(out, stockStatusOutput, Charset.defaultCharset());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loopAllStocks(WebDriver driver, WebDriverWait wait, List<WebElement> watchListStocks, List<String> stockStatusOutput) {
        String stockName = "";
        int scrollValue =0;
        for (WebElement watchListStock : watchListStocks) {

            try {
                WebElement stockNameElement = watchListStock.findElement(By.xpath("./div/div/span[1]/span/span"));
                stockName = stockNameElement.getText();
                System.out.println(stockName);

                /*CLICK ON CHARTS*/
                Actions builder = new Actions(driver);
                builder.moveToElement(stockNameElement);
                builder.build().perform();
                WebElement chartsButton = watchListStock.findElement(By.xpath("./div/span/span[4]"));
                chartsButton.click();


                /*Print Values of Charts*/

                wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(0));
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div/div[1]/span/span/span/span[1]")));
                Thread.sleep(5000);
                System.out.println("You should see fully loaded chart");


                WebElement paneLegend = driver.findElement(By.xpath("//div[@class='pane-legend']"));

                WebElement ema9Ele = paneLegend.findElement(By.xpath("./div[2]/div/span[1]/span"));
                System.out.println("EMA9 value is :" + ema9Ele.getText());
                WebElement ema21Ele = paneLegend.findElement(By.xpath("./div[3]/div/span[1]/span"));
                System.out.println("EMA12 value is :" + ema21Ele.getText());

                String ema9 = ema9Ele.getText();
                String ema21 = ema21Ele.getText();


                String screenshotBase64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);

                stockStatusOutput.add(stockName + " EMA9:" + ema9 + " EMA21:" + ema21 + "");
                new ActionThread(stockName, ema9, ema21, screenshotBase64);
                driver.switchTo().defaultContent();

                JavascriptExecutor jsExec = (JavascriptExecutor) driver;
                jsExec.executeScript("document.getElementsByClassName('marketwatch-sidebar')[0].scrollTo(0,"+scrollValue+")");

                Thread.sleep(1000);
            } catch (Exception e) {
                driver.switchTo().defaultContent();
                stockStatusOutput.add(stockName + "GOT exception");
                e.printStackTrace();
            }
            scrollValue+=50;

        }
    }

}
