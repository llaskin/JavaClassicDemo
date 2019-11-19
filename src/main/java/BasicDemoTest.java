import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.TestResults;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.text.SimpleDateFormat;

public class BasicDemoTest {

    public static void main(String[] args) {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");
        options.addArguments("window-size=1200x600");

        // Use Chrome browser
        WebDriver driver = new ChromeDriver();


        // Initialize the eyes SDK and set your private API key.
        Eyes eyes = new Eyes();
        eyes.setSendDom(false);
        eyes.setLogHandler(new StdoutLogHandler(true));

        //Set only once per Jenkins job
        BatchInfo mybatch = new BatchInfo(System.getenv("APPLITOOLS_BATCH_NAME"));
//        mybatch.setId(System.getenv("APPLITOOLS_BATCH_ID"));
//        //End of - Set only once per Jenkins job
//        eyes.setBatch(mybatch);

//		// Set the API key from the env variable. Please read the "Important Note"
//		// section above.
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
//        eyes.setStitchMode(StitchMode.CSS);
        eyes.setForceFullPageScreenshot(false);


        try {

            // Start the test by setting AUT's name, test name and viewport size (width X
            // height)
            eyes.open(driver, "Timestamp  App", "Timestamp Test", new RectangleSize(800,600));
//			eyes.open(driver, "Demo App", "Smoke Test");

            //To see visual bugs, change the above URL to:
            driver.get("https://www.applitools.com/");

            String timeStamp_start = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(new java.util.Date());
            System.out.println("Starting screenshot: " + timeStamp_start);
            TakesScreenshot scrShot =((TakesScreenshot)driver);

            String timeStamp_screenshot = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(new java.util.Date());
            System.out.println("Pre screenshot: " + timeStamp_screenshot);
            File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
            File DestFile=new File("image.png");
            //Copy file at destination
            FileUtils.copyFile(SrcFile, DestFile);
            String timeStamp_postShot = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(new java.util.Date());
            System.out.println("Post screenshot: " + timeStamp_postShot);

            // Visual checkpoint #1 - Check the login page
            String timeStamp_preCheck = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(new java.util.Date());
            System.out.println("Pre checkwindow: " + timeStamp_preCheck);
            eyes.checkWindow("Question Page 1");
            String timeStamp_checkWindow = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(new java.util.Date());
            System.out.println("Post Checkwindow: " + timeStamp_checkWindow);

            //			//Visual Region Checkpoint
            String timeStamp_checkRegion = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(new java.util.Date());
            System.out.println("Pre CheckRegion: " + timeStamp_checkRegion);
            eyes.checkRegion(By.cssSelector("#callout"), "Hackathon");
            String timeStamp_end = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(new java.util.Date());
            System.out.println("Post Check Region: " + timeStamp_end);

            // End the test.
            TestResults results = eyes.close();
            System.out.println(results);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        } finally {
            // Close the browser.
            driver.quit();

            // If the test was aborted before eyes.close was called, ends the test as
            // aborted.
            eyes.abortIfNotClosed();

            // End main test
            System.exit(0);
        }

    }
}