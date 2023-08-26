import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

public class TestSetup {
    static WebDriver driver;
    static Actions action;
    static JavascriptExecutor js;
    static WebDriverWait wait;

    @Parameters({"browser"})
    @BeforeMethod
    public static void setUp(String browser) {
        if ("chrome".equalsIgnoreCase(browser)) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } else if ("firefox".equalsIgnoreCase(browser)) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        }
        action = new Actions(driver);
        js = (JavascriptExecutor) driver;
        wait = new WebDriverWait(driver, 20);
        driver.manage().window().maximize();
    }

    @AfterMethod
    public static void tearDown() {
        if (driver != null) driver.quit();
    }
}
