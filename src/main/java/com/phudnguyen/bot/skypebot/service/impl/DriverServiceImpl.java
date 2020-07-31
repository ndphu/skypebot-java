package com.phudnguyen.bot.skypebot.service.impl;

import com.google.gson.Gson;
import com.phudnguyen.bot.skypebot.config.SkypeConfig;
import com.phudnguyen.bot.skypebot.service.DriverService;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.function.Function;

@Service
public class DriverServiceImpl implements DriverService {
    @Autowired
    private SkypeConfig skypeConfig;

    @Autowired
    private Gson gson;

    private WebDriver driver;

    @PostConstruct
    public void initDriver() throws IOException {

    }


    @Override
    public void login() throws IOException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200", "--ignore-certificate-errors");
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        dc.setCapability(ChromeOptions.CAPABILITY, options);
        driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), dc);
        driver.get("https://web.skype.com");
        sendText(By.xpath("//input[@name='loginfmt']"), skypeConfig.getUsername());
        click(By.xpath("//input[@type='submit' and @value='Next']"));
        sendText(By.xpath("//input[@name='passwd']"), skypeConfig.getPassword());
        click(By.xpath("//input[@type='submit' and @value='Sign in']"));
        getWebDriverWait(5 * 60).until((Function<WebDriver, Object>) driver -> driver.getCurrentUrl().startsWith("https://web.skype.com"));
        IOUtils.write(gson.toJson(driver.manage().getCookies()),
                new FileOutputStream("cookies.json"), Charset.forName("UTF-8"));
        driver.quit();
    }

    private WebDriverWait getWebDriverWait(int timeoutSecond) {
        return new WebDriverWait(driver, timeoutSecond, 500);
    }

    @Override
    public void sendImage(String groupName, String image) {
        click(By.xpath("//div[@data-text-as-pseudo-element='" + groupName + "']"));
        click(By.xpath("//button[@title='Add files' and @role='button']"));
        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {

        }
        Actions action = new Actions(driver);
        action.sendKeys(Keys.ESCAPE).build().perform();
    }

    @Override
    public void openSkypeWeb() {
//        driver.get("https://web.skype.com");
    }

    @Override
    public void selectConversation(String groupName) {
        click(By.xpath("//div[@data-text-as-pseudo-element='" + groupName + "']"));
    }

    @Override
    public void uploadImage(String path) {
        click(By.xpath("//button[@title='Add files' and @role='button']"));
        sendText(By.xpath("//input[@type='file']"), path);
        click(By.xpath("//button[@title='Send message' and @role='button']"));
    }

    @Override
    public void closeBrowser() {
        driver.quit();
    }

    private void sendText(By by, String text) {
        WebDriverWait w = getWebDriverWait(30);
        w.until((Function<WebDriver, Object>) driver -> {
            try {
                WebElement e = driver.findElement(by);
                e.sendKeys(text);
            } catch (Exception ex) {
                return false;
            }
            return true;
        });
    }

    private void click(By by) {
        WebDriverWait w = getWebDriverWait(30);
        w.until((Function<WebDriver, Object>) driver -> {
            try {
                WebElement e = driver.findElement(by);
                e.click();
            } catch (Exception ex) {
                return false;
            }
            return true;
        });
    }
}
