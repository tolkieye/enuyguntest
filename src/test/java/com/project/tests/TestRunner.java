package com.project.tests;


import com.saf.framework.MyTestNGBaseClass;
import io.cucumber.testng.*;


import org.testng.annotations.*;


@CucumberOptions(
        features = "src/test/features/",
        tags = "@1",
        plugin = {"pretty", "io.qameta.allure.cucumber4jvm.AllureCucumber4Jvm"},
        glue = {"com.project.stepdefs"})

public class TestRunner extends MyTestNGBaseClass {
    private io.cucumber.testng.TestNGCucumberRunner testNGCucumberRunner;

    //String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
    @BeforeClass(alwaysRun = true)
    public void setUpClass() {
        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
    }

    @Test(groups = "cucumber", description = "Runs Cucumber Feature", dataProvider = "features", invocationCount =1)
    public void runScenario(PickleEventWrapper pickleWrapper, CucumberFeatureWrapper featureWrapper) throws Throwable {
        this.testNGCucumberRunner.runScenario(pickleWrapper.getPickleEvent());

    }

    @DataProvider
    public Object[][] features() {
        return testNGCucumberRunner.provideScenarios();
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        testNGCucumberRunner.finish();
    }

}
