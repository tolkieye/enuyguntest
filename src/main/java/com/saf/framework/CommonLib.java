package com.saf.framework;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
import org.openqa.selenium.*;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class CommonLib extends MyTestNGBaseClass {
    public static WebDriver oDriver;
    public String page = "common";
    public static String referenceNumber;
    int timeout = 30;
    Parser parser = new Parser();
    Actions actions = new Actions(oDriver);

    public String getTheElementInformation(String elem, int index) {
        System.out.println(findElement(elem, index).getText());
        String elementText = findElement(elem, index).getText();
        this.referenceNumber = elementText;
        System.out.println(elementText);
        return elementText;
    }

    public void doubleClickElement(WebElement object) {
        actions.doubleClick(object).perform();
    }

    public WebElement findElement(String elem, int index) {
        WebElement object = null;
        String element = parser.getElement(page, elem);

        try {
            if (element != null) {
                if (element.startsWith("//") || element.startsWith("(//")) {
                    object = oDriver.findElements(By.xpath(element)).get(index - 1);

                    System.out.println("Element found : " + elem);
                } else if (element.startsWith("#") || element.startsWith(".")) {
                    object = oDriver.findElements(By.cssSelector(element)).get(index - 1);
                    System.out.println("Element found : " + elem);
                } else {
                    object = oDriver.findElements(By.id(element)).get(index - 1);
                    System.out.println("Element found : " + elem);
                }
            } else if (element == null) {
                object = oDriver.findElement(By.xpath("//*[text()='" + elem + "'or contains(text(),'" + elem + "')]"));
            }

            if (object == null) {
                System.out.println("Element not found: " + elem);
                Assert.fail("Element not found : " + elem);
            }
            return object;
        } catch (Exception e) {
            System.out.println("Element not found: " + elem);
            Allure.addAttachment("There is no such element.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            reportResult("FAIL", "There is no such element. " + elem, true);
            Assert.fail("Element not found : " + elem);

            return null;

        }
    }
    public String seePage(String page) {
        List<String> returnValue = parser.isPageExist(page);

        try {
            if (returnValue.get(0).equalsIgnoreCase(page)) {
                System.out.println(page + " page found!");
                this.page = page;

                if (returnValue.get(1).length() > 0) {
                    waitElement(returnValue.get(1), timeout, 1);
                }
                reportResult("PASS", "I see " + page + " page.(Page found)", false);
                return page;
            }
        } catch (Exception e) {
            reportResult("FAIL", "I see " + page + " page.(Page not found)", true);
            Assert.fail("Page not found! '" + page + "'");
        }
        return null;
    }
    public WebElement waitElement(String element, int timeout, int index) throws InterruptedException {
        WebElement object;
        try {
            for (int i = 0; i < timeout; i++) {

                object = findElement(element, index);
                if (object != null) {
                    Thread.sleep(2000);
                    return object;
                } else {
                    Thread.sleep(2000);
                }
            }
        } catch (Exception e) {
            Assert.fail("Waiting element is not found!");
            reportResult("FAIL", "Element could not find. ", true);
        }
        return null;
    }
    public static DesiredCapabilities getCapability() throws Exception {
        DesiredCapabilities oCapability = new DesiredCapabilities();
        oCapability.setJavascriptEnabled(true);
        return oCapability;
    }
    public static ChromeOptions getChromeOptions() throws Exception {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.merge(getCapability());
        chromeOptions.addArguments("test-type");
        chromeOptions.addArguments("disable-translate");
        chromeOptions.addArguments("start-maximized");
        chromeOptions.addArguments("disable-popup-blocking");
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NONE);
        return chromeOptions;
    }
    public static int getBrowserId(String sBrowserName) throws Exception {
        if (sBrowserName.equalsIgnoreCase("ie")) return 1;
        if (sBrowserName.equalsIgnoreCase("firefox")) return 2;
        if (sBrowserName.equalsIgnoreCase("chrome")) return 3;
        if (sBrowserName.equalsIgnoreCase("htmlunit")) return 4;
        return -1;
    }
    public static WebDriver getDriver(String sBrowserName) throws Exception {
        switch (getBrowserId(sBrowserName)) {
            case 3:
                WebDriverManager.chromedriver().setup();
                oDriver = new ChromeDriver(getChromeOptions());
                break;
            default:
                throw new Exception("Unknown browsername =" + sBrowserName +
                        " valid names are: ie,firefox,chrome,htmlunit");
        }
        oDriver.manage().deleteAllCookies();
        oDriver.manage().timeouts().pageLoadTimeout(AutomationConstants.lngPageLoadTimeout, TimeUnit.SECONDS);
        oDriver.manage().timeouts().implicitlyWait(AutomationConstants.lngImplicitWaitTimeout, TimeUnit.SECONDS);
        return oDriver;
    }
    public static WebDriver getRemoteDriver(String sHubUrl, String sBrowserName) throws Exception {
        WebDriver oDriver;
        DesiredCapabilities oCapability = getCapability();
        switch (getBrowserId(sBrowserName)) {
            case 1:
                oCapability.setBrowserName("internet explorer");
                break;

            case 2:
                oCapability.setBrowserName("firefox");
                break;

            case 3:
                oCapability.setBrowserName("chrome");
                break;

            case 4:
                oCapability.setBrowserName("htmlunit");

            default:
                throw new Exception("Unknown browsername = " + sBrowserName +
                        "  Valid names are: ie,firefox,chrome,htmlunit");
        }
        oCapability.setPlatform(Platform.WINDOWS);
        oDriver = new RemoteWebDriver(new URL(sHubUrl), oCapability);
        if (getBrowserId(sBrowserName) != 4) {
            oDriver.manage().window().maximize();
        }
        oDriver.manage().deleteAllCookies();
        oDriver.manage().timeouts().pageLoadTimeout(AutomationConstants.lngPageLoadTimeout, TimeUnit.SECONDS);
        oDriver.manage().timeouts().implicitlyWait(AutomationConstants.lngImplicitWaitTimeout, TimeUnit.SECONDS);
        return oDriver;
    }
    public static void navigateToURL(WebDriver oDriver, String URL) {
        oDriver.navigate().to(URL);
    }

    public static boolean sendKeys(WebElement element, String text) {
        boolean flag = false;
        try {
            if (element.isDisplayed() && element.isEnabled()) {
                waitSeconds(1);
                element.click();
                if (element.getText().equals("")) {
                    element.clear();
                    waitSeconds(1);
                }
                element.sendKeys(text);
                MyTestNGBaseClass.reportResult("PASS", "A value has been entered in the " + element.getText() + " Input field.", true);
                return true;
            }
        } catch (Exception e) {
            MyTestNGBaseClass.reportResult("FAIL", "Could not enter value for " + element.getText() + " element.", true);
            Assert.fail("Could not enter value for element." + element.getText());
            flag = false;
        }
        return flag;
    }
    public static void waitSeconds(int sec) {
        try {
            Thread.sleep(sec * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public boolean confirmElementExist(String element) {
        boolean result = false;
        try {
            WebElement object = null;
            object = findElement(element,1);
            if(object!=null)
                result = true;
            else
                result = false;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}