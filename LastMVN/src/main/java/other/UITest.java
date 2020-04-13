package other;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.testing.common.AutoLogger;

/**
 * @author Young
 *
 */
public class UITest {
    WebDriver driver;

    public WebDriver getDriver() {
        return driver;
    }

    /**
     * init test case
     * 
     * @param driver
     */
    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public void init(WebDriver driver) {
        AutoLogger.log.info("Start WebDriver");
        setDriver(driver);
    }

    /**
     * stop webdriver
     * 
     * @param driver
     */
    public void stop() {
        AutoLogger.log.info("Stop WebDriver");
        driver.quit();

    }

    /**
     * @author Young
     */
    public void takeScreenShot() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        String dateStr = sf.format(date);
        String path = this.getClass().getSimpleName() + "_" + dateStr + ".png";
        takeScreenShot((TakesScreenshot) this.getDriver(), path);
    }

    /**
     * @author Young
     * @param drivername
     * @param path
     */
    public void takeScreenShot(TakesScreenshot drivername, String path) {
        // this method will take screen shot ,require two parameters ,one is
        // driver name, another is file name
        String currentPath = System.getProperty("user.dir"); // get current work
        AutoLogger.log.info(currentPath);
        File scrFile = drivername.getScreenshotAs(OutputType.FILE);
        // Now you can do whatever you need to do with it, for example copy
        try {
            AutoLogger.log.info("save snapshot path is:" + currentPath + path);
            FileUtils.copyFile(scrFile, new File(currentPath + "\\" + path));
        } catch (Exception e) {
            AutoLogger.log.error("Can't save screenshot");
            e.printStackTrace();
        } finally {
            AutoLogger.log.info("screen shot finished");
        }
    }

}