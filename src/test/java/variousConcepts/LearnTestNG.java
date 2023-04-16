package variousConcepts;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LearnTestNG {

	String browser;
	String url;
	WebDriver driver;

	// Element List
	By USER_NAME_FIELD = By.xpath("//input[@id='username']");
	By PASSWORD_FIELD = By.xpath("//input[@id='password']");
	By SIGN_IN_BUTTON_FIELD = By.xpath("//button[@name='login']");
	By DASHBOARD_HEADER_FIELD = By.xpath("//h2[text()=' Dashboard ']");
	By CUSTOMER_MENU_FIELD = By.xpath("//span[contains(text(), 'Customers')]");
	By ADD_CUSTOMER_MENU_FIELD = By.xpath("//a[contains(text(), 'Add Customer')]");
	By ADD_CUSTOMER_HEADER_FIELD = By.xpath("//h5[contains(text(), 'Add Contact')]");
	By FULL_NAME_FIELD = By.xpath("//*[@id=\"account\"]");
	By COMPANY_DROPDOWN_FIELD = By.xpath("//select[@id='cid']");
	By EMAIL_FIELD = By.xpath("//*[@id=\"email\"]");
	By COUNTRY_DROPDOWN_FIELD = By.xpath("//select[@id='country']");

	// Test/Mock Data
	String userName = "demo@techfios.com";
	String password = "abc123";
	String fullName = "selenium";
	String company = "Techfios";
	String email = "demo@techfios.com";
	String country = "Afghanistan";

	@BeforeClass
	public void readConfig() {

		// Scanner //FileReader //InputStream //BufferedReader
		try {
			InputStream input = new FileInputStream("src\\test\\java\\config\\config.properties");

			Properties prop = new Properties();
			prop.load(input);
			browser = prop.getProperty("browser");
			url = prop.getProperty("url");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@BeforeMethod
	public void init() {

		if (browser.equalsIgnoreCase("Chrome")) {
			System.setProperty("webdriver.chrome.driver", "drivers\\chromedriver.exe");
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("Edge")) {
			System.setProperty("webdriver.edge.driver", "drivers\\msedgedriver.exe");
			driver = new EdgeDriver();
		} else {
			System.out.println("Browser configuration required!!");
		}

		driver.manage().deleteAllCookies();
		driver.get(url);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

	}

	@Test(priority = 1)
	public void loginTest() {
		driver.findElement(USER_NAME_FIELD).sendKeys(userName);
		driver.findElement(PASSWORD_FIELD).sendKeys(password);
		driver.findElement(SIGN_IN_BUTTON_FIELD).click();

		Assert.assertEquals(driver.findElement(DASHBOARD_HEADER_FIELD).getText(), "Dashboard",
				"Dashboard page not found!!!");

	}
	
	@Test(priority = 2)
	public void addCustomer() throws InterruptedException {
		loginTest();
		driver.findElement(CUSTOMER_MENU_FIELD).click();
		driver.findElement(ADD_CUSTOMER_MENU_FIELD).click();
		
//		Thread.sleep(2000);
		waitForElement(driver, 10, ADD_CUSTOMER_HEADER_FIELD);
		
		Assert.assertEquals(driver.findElement(ADD_CUSTOMER_HEADER_FIELD).getText(), "Add Contact", "Add Customer page not found!!!");
		
		driver.findElement(FULL_NAME_FIELD).sendKeys(fullName + generateRandomNum(9999));
		
		selectFromDropdown(driver.findElement(COMPANY_DROPDOWN_FIELD), company);
		
		driver.findElement(EMAIL_FIELD).sendKeys(generateRandomNum(999) + email);
		
		selectFromDropdown(driver.findElement(COUNTRY_DROPDOWN_FIELD), country);
		
		
	}

	private void waitForElement(WebDriver driver, int timeInSeconds, By locator) {
		WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	private void selectFromDropdown(WebElement element, String visibleText) {
		Select sel = new Select(element);
		sel.selectByVisibleText(visibleText);
	}
	
	private int generateRandomNum(int bounderyNum) {
		Random rnd = new Random();
		int generatedNum = rnd.nextInt(bounderyNum);
		return generatedNum;
	}

	
	

}
