package org.example.step;
import org.example.settings.Setting;
import org.example.model.ElementInfo;
import com.thoughtworks.gauge.Step;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestStep extends Setting{

    public static int DEFAULT_MAX_ITERATION_COUNT = 150;
    public static int DEFAULT_MILLISECOND_WAIT_AMOUNT = 100;

    private static String SAVED_ATTRIBUTE;
    private static int SAVED_VALUE;

    private String compareText;

    public TestStep() {
        initMap(getFileList());
    }

    WebElement findElement(String key) {
        By infoParam = getElementInfoToBy(findElementInfoByKey(key));
        WebDriverWait webDriverWait = new WebDriverWait(driver, 60);
        WebElement webElement = webDriverWait
                .until(ExpectedConditions.presenceOfElementLocated(infoParam));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                webElement);
        return webElement;
    }

    List<WebElement> findElements(String key) {

        return driver.findElements(getElementInfoToBy(findElementInfoByKey(key)));

    }


    public By getElementInfoToBy(ElementInfo elementInfo) {
        By by = null;
        if (elementInfo.getType().equals("css")) {
            by = By.cssSelector(elementInfo.getValue());
        } else if (elementInfo.getType().equals(("name"))) {
            by = By.name(elementInfo.getValue());
        } else if (elementInfo.getType().equals("id")) {
            by = By.id(elementInfo.getValue());
        } else if (elementInfo.getType().equals("xpath")) {
            by = By.xpath(elementInfo.getValue());
        } else if (elementInfo.getType().equals("linkText")) {
            by = By.linkText(elementInfo.getValue());
        } else if (elementInfo.getType().equals(("partialLinkText"))) {
            by = By.partialLinkText(elementInfo.getValue());
        }
        return by;
    }

    private void clickElement(WebElement element) {
        element.click();
    }

    private void clickElementBy(String key) {
        findElement(key).click();
    }

    private void hoverElement(WebElement element) {
        actions.moveToElement(element).build().perform();
    }

    private void hoverElementBy(String key) {
        WebElement webElement = findElement(key);
        actions.moveToElement(webElement).build().perform();
    }

    private void sendKeyESC(String key) {
        findElement(key).sendKeys(Keys.ESCAPE);

    }

    private boolean isDisplayed(WebElement element) {
        return element.isDisplayed();
    }

    private boolean isDisplayedBy(By by) {
        return driver.findElement(by).isDisplayed();
    }

    private String getPageSource() {
        return driver.switchTo().alert().getText();
    }

    public static String getSavedAttribute() {
        return SAVED_ATTRIBUTE;
    }

    public String randomString(int stringLength) {

        Random random = new Random();
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUWVXYZabcdefghijklmnopqrstuwvxyz0123456789".toCharArray();
        String stringRandom = "";
        for (int i = 0; i < stringLength; i++) {

            stringRandom = stringRandom + chars[random.nextInt(chars.length)];
        }

        return stringRandom;
    }

    public WebElement findElementWithKey(String key) {
        return findElement(key);
    }

    public String getElementText(String key) {
        return findElement(key).getText();
    }

    public String getElementAttributeValue(String key, String attribute) {
        return findElement(key).getAttribute(attribute);
    }


    @Step("<key> adrese git")
    public void GotoUrl(String key){
        driver.get(key);
        logger.info(key + " adresine gidiliyor.");
    }

    @Step("<key> elementine tikla")

    public void ElementClick(String key){
        findElement(key).click();
        logger.info(key + " elementine tiklandi");
        System.out.println(key + " elementine tiklandi");
    }

    @Step("<key> saniye bekle")
    public void WaitForSecond(int key){
        try {
            Thread.sleep(key*1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        logger.info(key + " saniye beklendi");

    }
  /*  @Step("<xpath> Elementin Yuklenmesini Bekle")
    public void ElementWait(String key){
        int loopCount = 0;
        while(loopCount<DEFAULT_MAX_ITERATION_COUNT) {
            if (driver.findElements(By.xpath(key)).size()>0){
                logger.info(key + " elementi bulundu.");
                return;
            }
            loopCount++;
            WaitForSecond(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail("element yüklenemedi.");
    } */

}
