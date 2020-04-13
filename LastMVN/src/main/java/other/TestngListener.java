package other;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
public class TestngListener extends TestListenerAdapter  {
    public static WebDriver driver;
    @Override
    public void onTestFailure(ITestResult tr) {
        super.onTestFailure(tr);
        try {
            takeScreenShot(tr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void takeScreenShot(ITestResult tr) throws IOException{
        SimpleDateFormat smf = new SimpleDateFormat("MMddHHmmss") ;
        String curTime = smf.format(new java.util.Date());
        String fileName = tr.getName()+"_"+curTime+".png";
        File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        //把截图拷贝到自定义的目录
        FileUtils.copyFile(srcFile, new File("D:\\"+fileName));
    }
}