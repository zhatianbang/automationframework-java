
package demo3;
 
import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.annotations.ITestAnnotation;
 
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
 
/**
 * create by Anthony on 2017/11/27
 */
public class MyRetryListener implements IAnnotationTransformer {
 
 
    @Override
    public void transform(ITestAnnotation iTestAnnotation, Class aClass, Constructor constructor, Method method) {
 
        IRetryAnalyzer myRetry = iTestAnnotation.getRetryAnalyzer();
        if (myRetry == null) {
            iTestAnnotation.setRetryAnalyzer(MyRetry.class);
        }
    }
}
