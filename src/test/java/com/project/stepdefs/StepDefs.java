package com.project.stepdefs;

import com.mysql.cj.util.TestUtils;
import com.saf.framework.*;
import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.qameta.allure.Allure;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;



import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;


public class StepDefs extends MyTestNGBaseClass {
    CommonLib commonLib = new CommonLib();
    int timeout = 30;
    public static HashMap<String, String> strings = new HashMap<String, String>();
    InputStream stringsis;
    TestUtils utils;

    @Before
    public void setReportName(Scenario scenario) {
        commonLib.startTest(scenario.getName());
    }

    @Given("^Open the (.*) URL$")
    public void openUrl(String URL) {
        CommonLib.navigateToURL(oDriver, URL);
    }

    @Then("^I see (.*) page$")
    public void seePage(String page) {
        commonLib.seePage(page);
    }

    @When("^(?:I )?click element: (\\w+(?: \\w+)*) at index (\\d+)")
    public boolean clickElement(String element, int index) {
        WebElement object = commonLib.findElement(element, index);
        boolean flag = false;
        try {
            if (object != null) {
                object.click();
                System.out.println("Clicked on object-->" + element);
                Allure.addAttachment("Element is clicked.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                reportResult("PASS", "I clicked the element: " + element, true);
                return true;
            }
        } catch (Exception e) {
            reportResult("FAIL", "I cannot clicked the element: " + element, true);
            Allure.addAttachment("Element is not clicked.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            Assert.fail("Could not clicked the element:" + element);
            flag = false;
        }
        return flag;
    }


    @When("^(?:I )?have to verify the text for: (\\w+(?: \\w+)*) at index (\\d+)")
    public boolean verifyText(String element, int index) throws Exception {
        WebElement object = commonLib.findElement(element, index);
        boolean flag = false;
        try {
            if (object != null) {
                String xmlFileName = "strings.xml";
                stringsis = this.getClass().getClassLoader().getResourceAsStream(xmlFileName);
                utils = new TestUtils();
                object.click();
                String actualErrTxt = object.getText();
                if (element.contains("approve popup")) {
                    String expectedErrText = strings.get("approve popup");
                    System.out.println("actual popup text - " + actualErrTxt + "\n" + "expected popup text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                } else if (element.contains("assign to pool popup")) {
                    String expectedErrText = strings.get("assign to pool popup");
                    System.out.println("actual popup text - " + actualErrTxt + "\n" + "expected popup text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                } else if (element.contains("cancel popup")) {
                    String expectedErrText = strings.get("cancel popup");
                    System.out.println("actual popup text - " + actualErrTxt + "\n" + "expected popup text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                }
            }
        } catch (Exception e) {
            Allure.addAttachment("Verification does not completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            reportResult("FAIL", "An error during assertion. " + element, true);
            Assert.fail("Could not clicked the element:" + element);
            flag = false;
        } finally {
            if (stringsis != null) {
                stringsis.close();
            }
        }
        return flag;
    }


    @Then("^I enter \"([^\"]*)\" text to (.*) at index (\\d+)")
    public boolean enterText(String text, String element, int index) throws InterruptedException {
        WebElement object;
        object = commonLib.waitElement(element, timeout, index);
        boolean flag = false;
        try {
            if (object != null) {
                object.sendKeys(text);
                System.out.println("The text has been entered:" + text);
                Allure.addAttachment("The text has been entered.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                reportResult("PASS", "I entered the text: " + text, true);

                return true;
            }
        } catch (Exception e) {
            Allure.addAttachment("The text has not been entered.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            reportResult("FAIL", "I cannot entered the element: " + text, true);
            Assert.fail("Could not entered the text:" + text);
            flag = false;
        }
        return flag;
    }

    @Then("^I verify the area (.*) by read only at index (\\d+)")
    public boolean readOnlyAreaCheck(String element, int index) throws InterruptedException {
        WebElement object;
        object = commonLib.waitElement(element, timeout, index);
        boolean flag = false;

        try {
            if (object != null) {
                if (!object.isEnabled()) {
                    System.out.println("The area is a read only area. Cannot be editable.");
                    Allure.addAttachment("The area is a read only area. Cannot be editable.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    reportResult("PASS", "The area is a read only area. Cannot be editable.", true);
                    return true;
                }
            }
        } catch (Exception e) {
            Allure.addAttachment("The area is not a read only area. Can be editable.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            reportResult("FAIL", "The area is not a read only area. Can be editable.", true);
            Assert.fail("The area is not a read only area. Can be editable.");
            flag = false;
        }
        return flag;
    }
    @And("^I wait (.*) element (\\d+) seconds at index (\\d+)")
    public void waitElement(String element, int timeout, int index) throws InterruptedException {
        commonLib.waitElement(element, timeout, index);
    }
    @When("^(?:I )?select element: \"([^\"]*)\" under (\\w+(?: \\w+)*) at index (\\d+)")
    public boolean selectElement(String text, String element, int index) {
        WebElement object = commonLib.findElement(element, index);
        boolean flag = false;
        try {
            if (object != null) {
                object.click();
                System.out.println("Select the object type-->" + text + element);
                Select select = new Select(object);
                select.selectByVisibleText(text);
                Allure.addAttachment("The selection is done.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                reportResult("PASS", "The selection is done.", true);
                return true;
            }
        } catch (Exception e) {
            System.out.println("The selection cannot be done.");
            Allure.addAttachment("The selection cannot be done.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            reportResult("FAIL", "The selection cannot be done.", true);
            Assert.fail("The selection cannot be done!");
            flag = false;
        }
        return flag;
    }
    @And("^I need to just wait")
    public void justWait() throws InterruptedException {
        Thread.sleep(10000);
    }
    @And("^I need to wait for (\\d+) seconds")
    public void waitForIt(int seconds) throws InterruptedException {

        int secondsToMilis = seconds * 1000;
        Thread.sleep(secondsToMilis);
    }
    @Then("I go to {string} page at index {int}")
    public void iGoToPage(String page,int index) throws InterruptedException {



        try {
            switch (page) {
                case "list campaign":

                    clickElement("left menu selection",1);
                    clickElement("listCampaign",1);
                    seePage("Campaign");
                    break;
                case "add campaign":

                    clickElement("left menu selection",1);
                    clickElement("addCampaign",1);
                    seePage("Campaign");
                    break;
                case "homepage":
                    clickElement("left menu selection",3);
                    seePage("homepage");
                    break;
                case "add categories":
                    clickElement("left menu selection",5);
                    clickElement("add categories",1);
                    seePage("Categories");
                    break;
                case "list categories":
                    clickElement("left menu selection",5);
                    clickElement("list categories",1);
                    seePage("Categories");
                    break;
                case "add firm":
                    clickElement("left menu selection",6);
                    clickElement("add firm",1);
                    seePage("Firms");
                    break;
                case "list firm":
                    clickElement("left menu selection",6);
                    clickElement("list firm",1);
                    seePage("Firms");
                    break;
                case "list dynamic segments":
                    clickElement("left menu selection",8);
                    clickElement("listDynamicSegments",1);
                    seePage("DynamicSegment");
                    break;
                case "add dynamic segments":
                    clickElement("left menu selection",8);
                    clickElement("addDynamicSegments",1);
                    seePage("DynamicSegment");
                    break;

                case "upload voucher code":
                    clickElement("left menu selection",9);
                    clickElement("upload voucher code file",1);
                    seePage("UploadVoucherCode");
                    break;
                case "delete voucher code":
                    clickElement("left menu selection",9);
                    clickElement("delete voucher code",1);
                    seePage("DeleteVoucherCode");
                    break;
                case "voucher code reports":
                    clickElement("left menu selection",9);
                    clickElement("voucher code reports",1);
                    seePage("VoucherCodeReports");
                    break;



            }
            Thread.sleep(3000);
        } catch (Exception e) {

            System.out.println(e.getMessage());
        }

    }
}
