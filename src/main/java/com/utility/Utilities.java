package com.utility;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.mozilla.javascript.regexp.SubString;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.asserts.SoftAssert;

import com.driverInstance.DriverInstance;
import com.extent.ExtentReporter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Ordering;
import com.propertyfilereader.PropertyFileReader;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.AndroidTouchAction;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

public class Utilities extends ExtentReporter {

	/** Time out */
	@SuppressWarnings("unused")
	private int timeout;

	/** Retry Count */
	private int retryCount;

	public static PropertyFileReader prop = new PropertyFileReader(System.getProperty("user.dir")+"//properties//testData.properties");

	public static ExtentReporter extent = new ExtentReporter();

	public static Connection con;

	@SuppressWarnings("rawtypes")
	public TouchAction touchAction;

	private SoftAssert softAssert = new SoftAssert();

	public static boolean relaunch = false;

	public static String CTUserName;

	public static String CTPWD;

	public static String setPlatform = "";

	/** The Constant logger. */
//	final static Logger logger = Logger.getLogger("rootLogger");
	static LoggingUtils logger = new LoggingUtils();

	/** The Android driver. */
	public AndroidDriver<AndroidElement> androidDriver;

	/** window handler */
	static ArrayList<String> win = new ArrayList<>();

	/** The Android driver. */
	public IOSDriver<WebElement> iOSDriver;

	public int getTimeout() {
		return 60;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public static AppiumDriver<WebElement> getDriver() {
		return DriverInstance.tlDriver.get();
	}

	protected static WebDriver getWebDriver() {
		return DriverInstance.tlWebDriver.get();
	}

	public String getPlatform() {
		return DriverInstance.getPlatform();
	}

	public void setPlatform(String Platform) {
		DriverInstance.setPlatfrom(Platform);
	}

	public static AppiumDriver<WebElement> getTVDriver() {
		return DriverInstance.driverTV.get();
	}

	static WebDriverWait wait;

	public static JavascriptExecutor js;

	public void initDriver() {
		if (getPlatform().equals("Web")) {
			wait = new WebDriverWait(getWebDriver(), 600);
			js = (JavascriptExecutor) getWebDriver();
		} else if (getPlatform().equals("Android") || getPlatform().equals("MPWA") || getPlatform().equals("TV")) {
			wait = new WebDriverWait(getDriver(), 600);
			js = (JavascriptExecutor) getDriver();
		}
	}

	public boolean JSClick(By byLocator, String text) {
		try {
			js.executeScript("arguments[0].click();", findElement(byLocator));
			logger.info("" + text + " " + " is clicked");
			extent.extentLoggerPass("checkElementPresent", "" + text + " is clicked");
			return true;
		} catch (Exception e) {
			logger.error(text + " " + " is not clicked");
			extent.extentLoggerFail("checkElementNotPresent", "" + text + " is not clicked");
			screencapture();
			e.printStackTrace();
			return false;
		}
	}

	public WebElement findElement(By byLocator) throws Exception {
//		try{
		
		WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(byLocator));
//		softAssert.assertEquals(element.isDisplayed(), true, "");
		return element;
//		}catch(Exception e){
//			softAssert.assertEquals(e, true, "");
//			return null;
//		}
	}
	
	public WebElement findElement_specific(By byLocator) throws Exception {
//		try{
		WebDriverWait wait_specific = new WebDriverWait(getDriver(),05);
		WebElement element = wait_specific.until(ExpectedConditions.presenceOfElementLocated(byLocator));
//		softAssert.assertEquals(element.isDisplayed(), true, "");
		return element;
//		}catch(Exception e){
//			softAssert.assertEquals(e, true, "");
//			return null;
//		}
	}

	public void setWifiConnectionToONOFF(String Value) {
		try {
			if (Value.equalsIgnoreCase("On")) {
				System.out.println("Switching On Wifi");
				String cmd = "adb shell svc wifi enable";
				Runtime.getRuntime().exec(cmd);
				waitTime(5000);
				logger.info("Wifi Data toggle is Switched On");
				extent.extentLoggerPass("Wifi Toggle", "Wifi Data toggle is Switched On");
			} else if (Value.equalsIgnoreCase("Off")) {
				System.out.println("Switching Off Wifi");
				String cmd = "adb shell svc wifi disable";
				Runtime.getRuntime().exec(cmd);
				waitTime(3000);
				logger.info("Wifi Data toggle is Switched Off");
				extent.extentLoggerPass("Wifi Toggle", "Wifi Data toggle is Switched Off");
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * wait until element is displayed.
	 *
	 * @param webElement the by locator
	 * @return true, if successful
	 */
	public static boolean waitForElementDisplayed(By byLocator, int iTimeOut) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(byLocator));
			return false;
		} catch (Exception e) {
			return true;
		}
	}

	/**
	 * Check element not present.
	 *
	 * @param byLocator the by locator
	 * @return true, if successful
	 */
	public boolean verifyElementNotPresent(By byLocator, int iTimeOut) {
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), iTimeOut);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(byLocator));
			return false;
		} catch (NoSuchElementException e) {
			return true;
		}
	}

	public long ageCal(int Year, int Date) {
		Calendar birthDay = new GregorianCalendar(Year, Calendar.FEBRUARY, Date);
		Calendar today = new GregorianCalendar();
		today.setTime(new Date());
		int yearsInBetween = today.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
		int monthsDiff = today.get(Calendar.MONTH) - birthDay.get(Calendar.MONTH);
		long ageInMonths = yearsInBetween * 12 + monthsDiff;
		long age = yearsInBetween;
		logger.info("age is : " + age);
		extent.extentLogger("Age", "age is : " + age);
		return age;
	}

	/**
	 * Check element present.
	 *
	 * @param byLocator the by locator
	 * @return true, if successful
	 */
	public boolean verifyElementPresent(By byLocator, String validationtext) throws Exception {

		try {
			WebElement element = findElement(byLocator);
			softAssert.assertEquals(element.isDisplayed(), true, "" + validationtext + " " + " is displayed");
			logger.info(validationtext + " is displayed");
			extent.extentLoggerPass("checkElementPresent", validationtext + " is displayed");
			return true;
		} catch (Exception e) {
			softAssert.assertEquals(false, true, validationtext + " " + " is not displayed");
//			softAssert.assertAll();
			logger.error(validationtext + " is not displayed");
			extent.extentLoggerFail("checkElementPresent", validationtext + " is not displayed");
			return false;
		}
	}

	public boolean verifyElementPresent2(By byLocator, String validationtext) throws Exception {

		try {
			WebElement element = findElement(byLocator);
			softAssert.assertEquals(element.isDisplayed(), true, "" + validationtext + " " + " is displayed");
			logger.info(validationtext + " is displayed");
			extent.extentLoggerPass("checkElementPresent", validationtext + " is displayed");
			return true;
		} catch (Exception e) {
			softAssert.assertEquals(false, true, validationtext + " " + " is not displayed");
//			softAssert.assertAll();
			logger.error(validationtext + " is not displayed");
			extent.extentLogger("checkElementPresent", validationtext + " is not displayed");
			return false;
		}
	}

	public boolean verifyElementPresent1(By byLocator, String validationtext) throws Exception {

		WebElement element = findElement(byLocator);
		softAssert.assertEquals(element.isDisplayed(), true, "" + validationtext + " " + " is displayed");
		logger.info(validationtext + " is displayed");
		extent.extentLoggerPass("checkElementPresent", validationtext + " is displayed");
		return true;

	}

	public boolean verifyElementExist(By byLocator, String str) throws Exception {
		try {
			WebElement element = findElement(byLocator);
			if (element.isDisplayed()) {
				extent.extentLoggerPass("checkElementPresent", "" + str + " is displayed");
				logger.info("" + str + " is displayed");
				return true;
			}
		} catch (Exception e) {
			extent.extentLoggerFail("checkElementPresent", "" + str + " is not displayed");
			logger.info(str + " is not displayed");
			return false;
		}
		return false;
	}

	/**
	 * boolean return type for conditions
	 * 
	 * @param byLocator
	 * @return
	 * @throws Exception
	 */
	public boolean verifyElementDisplayed(By byLocator) throws Exception {
		try {
			WebElement element = findElement(byLocator);
			if (element.isDisplayed()) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public boolean checkElementExist(By byLocator, String str) throws Exception {

		try {
			getDriver().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			List<WebElement> list = getDriver().findElements(byLocator);
			getDriver().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			if (list.size() == 0) {
				extent.extentLogger("checkElementPresent", "" + str + " is not displayed");
				logger.info("" + str + " is not displayed");
				return false;
			} else {
				extent.extentLogger("checkElementPresent", "" + str + " is displayed");
				logger.info("" + str + " is displayed");
				return list.get(0).isDisplayed();
			}
		} catch (Exception e) {
			return false;
		}
	}

	public String getTextVal(By byLocator, String concatValue) throws Exception {
		String Value = null;
		WebElement element = findElement(byLocator);
		Value = element.getText();
		String finalValue = Value + " " + concatValue;
		return finalValue;
	}

	public void type1(By byLocator, String amount, String FieldName) {
		try {
			waitTime(2000);
			if (!getPlatform().equals("Web")) {
				Actions a = new Actions(getDriver());
				a.sendKeys(amount);
				a.perform();
			} else {
				WebElement element = findElement(byLocator);

				element.sendKeys(amount);
			}
			amount = amount.split("\n")[0];

			logger.info("Typed the value " + amount + " into " + FieldName);
			extent.extentLogger("", "Typed the value " + amount + " into " + FieldName);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * Check element present and click.
	 *
	 * @param byLocator the by locator
	 * @return true, if successful
	 */
	public boolean verifyElementPresentAndClick(By byLocator, String validationtext) throws Exception {
		try {
			WebElement element = findElement(byLocator);
			softAssert.assertEquals(element.isDisplayed(), true, "" + validationtext + " " + " is displayed");
			logger.info("" + validationtext + " " + "is displayed");
			extent.extentLogger("checkElementPresent", "" + validationtext + " is displayed");
			findElement(byLocator).click();
			logger.info("Clicked on " + validationtext);
			extent.extentLoggerPass("click", "Clicked on " + validationtext);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			softAssert.assertEquals(false, true, "Element" + validationtext + " " + " is not visible");
			logger.error("Element " + validationtext + " " + " is not visible");
			extent.extentLoggerFail("checkElementPresent", "" + validationtext + " is not displayed");
			screencapture();
//			softAssert.assertAll();
			return false;
		}
	}

	public boolean clickElementWithWebLocator(By locator) throws Exception {
		try {
			getWebDriver().findElement(locator).click();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static String getAdId() throws IOException {
		String cmd = "adb shell grep adid_key /data/data/com.google.android.gms/shared_prefs/adid_settings.xml";
		Process p = Runtime.getRuntime().exec(cmd);
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String strBuffer = br.readLine().trim();
		String[] getadid = strBuffer.split(">")[1].split("<");
		System.out.println("\nAdID: " + getadid[0]);
		return getadid[0];
	}

	/**
	 * @param byLocator
	 * @return true or false
	 */
	public boolean checkcondition(By byLocator) throws Exception {
		boolean iselementPresent = false;
		try {
			iselementPresent = getDriver().findElement(byLocator).isDisplayed();
			iselementPresent = true;
		} catch (Exception e) {
			iselementPresent = false;
		}
		return iselementPresent;
	}

	/**
	 * Click on a web element.
	 * 
	 * @param byLocator the by locator
	 * 
	 */
	public void click(By byLocator, String validationtext) throws Exception {
		try {
			WebElement element = findElement(byLocator);
			element.click();
			logger.info("Clicked on " + validationtext);
			extent.extentLogger("click", "Clicked on " + validationtext);
		} catch (Exception e) {
			screencapture();
		}
	}

	/**
	 * clear the text field
	 * 
	 * @param byLocator
	 */
	public void clearField(By byLocator, String text) {
		try {
			findElement(byLocator).clear();
			logger.info("Cleared the text in : " + text);
			extent.extentLogger("clear text", "Cleared the text in : " + text);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * Get Text from an object
	 * 
	 * @param byLocator
	 * @return
	 * @throws Exception
	 */
	public String getText(By byLocator) throws Exception {
		String Value = null;
		WebElement element = findElement(byLocator);
		Value = element.getText();
		return Value;
	}

//	public void hidePwdKeyboard() {
//		try {
//
//			if (getDriver().findElement(AMDGenericObjects.objHideKeyboard).isDisplayed()) {
//				click(AMDGenericObjects.objHideKeyboard, "HideKeyboard");
//			} else {
//				getDriver().hideKeyboard();
//			}
//			logger.info("Hiding keyboard was Successfull");
//			extent.extentLogger("hideKeyboard", "Hiding keyboard was Successfull");
//		} catch (Exception e) {
//			logger.error(e);
//		}
//	}

	@SuppressWarnings({ "rawtypes" })
	public String OTPNotification() {
		HeaderChildNode("Fetching Otp From Notification");
		waitTime(2000);
		getDriver().context("NATIVE_APP");
//		Action = new TouchActiotouchn(getDriver());
//		touchAction.press(PointOption.point(500, 0))
//		.waitAction(WaitOptions.waitOptions(Duration.ofMillis(4000)))
//		.moveTo(PointOption.point(1500, 500)).release().perform();
//		waitTime(8000);
		AndroidDriver driver = (AndroidDriver) getDriver();
		driver.openNotifications();
		waitTime(3000);
		List<WebElement> allnotifications = getDriver()
				.findElements(By.xpath("(//*[@resource-id='android:id/message_text'])[1]"));
		System.out.println("Size : " + allnotifications.size());
		String Otp = null;
		for (WebElement webElement : allnotifications) {
			Otp = webElement.getText();
			System.out.println("Get Text : " + webElement.getText());
			if (webElement.getText().contains("something")) {
				System.out.println("Notification found");
				break;
			}
		}
		Back(1);
		getDriver().context("WEBVIEW_1");
		return Otp;
	}

	public String fetchOtp() throws IOException {
		String SMSCommand = "adb shell content query --uri content://sms --projection body";
		Process process = Runtime.getRuntime().exec(SMSCommand);
		BufferedReader Result = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String getOTP = Result.readLine();
		// System.out.println(getOTP.length());
		// println getOTP;
		String OTP = getOTP.split(" is")[0];
		// println OTP
		// System.out.println(OTP);
		String finalotp = OTP.split("=")[1];
		// System.out.println(finalotp);

		// KeywordUtil.logInfo("OTP Fetched : "+OTP)

		return finalotp;
	}

	public boolean verifyElementIsNotDisplayed(By by) {
		try {
			getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			return getDriver().findElements(by).isEmpty();
		} catch (Exception e) {
			getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			return false;
		}
	}

	public boolean verifyIsElementDisplayed(By by) {
		List<WebElement> list = null;
		if (getPlatform().equals("Web")) {
			getWebDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			list = getWebDriver().findElements(by);
			getWebDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		} else {
			getDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			list = getDriver().findElements(by);
			getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		if (list.size() == 0) {
			return false;
		} else {
			return list.get(0).isDisplayed();
		}
	}

	public boolean verifyIsElementDisplayed(By by, String validationtext) {
		List<WebElement> list = null;
		if (getPlatform().equals("Web")) {
			getWebDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			list = getWebDriver().findElements(by);
			getWebDriver().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		} else {
			getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			list = getDriver().findElements(by);
			getDriver().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		}
		if (list.size() == 0) {
			logger.info("Element " + validationtext + " " + " is not displayed");
			extent.extentLogger("checkElementPresent", "" + validationtext + " is not displayed");
			return false;
		} else {
			logger.info("" + validationtext + " " + "is displayed");
			extent.extentLogger("checkElementPresent", "" + validationtext + " is displayed");
			return list.get(0).isDisplayed();
		}
	}

	public boolean checkElementExist(By byLocator) throws Exception {
		try {
			WebElement element = findElement(byLocator);
			if (element.isDisplayed()) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	/**
	 * Kill or start an application using activity
	 * 
	 * @param command  to START or KILL an application
	 * @param activity Start an application by passing the activity
	 */
	public static void adbStartKill(String command, String activity) {
		String cmd;
		try {
			if (command.equalsIgnoreCase("START")) {
				cmd = "adb shell am start -n" + " " + activity;
				Runtime.getRuntime().exec(cmd);
				logger.info("Started the activity" + cmd);
				extent.extentLogger("adbStart", "Started the activity" + cmd);
			} else if (command.equalsIgnoreCase("KILL")) {
				cmd = "adb shell am force-stop" + " " + activity;
				Runtime.getRuntime().exec(cmd);
				logger.info("Executed the App switch");
				extent.extentLogger("adbKill", "Executed the App switch");
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * @return true if keyboard is displayed
	 * @throws IOException
	 */
	public boolean checkKeyboardDisplayed() throws IOException {
		boolean mInputShown = false;
		try {
			String cmd = "adb shell dumpsys input_method | grep mInputShown";
			Process p = Runtime.getRuntime().exec(cmd);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String outputText = "";
			while ((outputText = br.readLine()) != null) {
				if (!outputText.trim().equals("")) {
					String[] output = outputText.split(" ");
					String[] value = output[output.length - 1].split("=");
					String keyFlag = value[1];
					if (keyFlag.equalsIgnoreCase("True")) {
						mInputShown = true;
					}
				}
			}
			br.close();
			p.waitFor();
		} catch (Exception e) {
			System.out.println(e);
		}
		return mInputShown;
	}

	/**
	 * Closes the Keyboard
	 */
	public void hideKeyboard() {
		try {
			getDriver().hideKeyboard();
			logger.info("Hiding keyboard was Successfull");
			extent.extentLogger("hideKeyboard", "Hiding keyboard was Successfull");
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * Type on a web element.
	 * 
	 * @param byLocator the by locator
	 * @param text      the text
	 */
	public void type(By byLocator, String input, String FieldName) {
		try {
			waitTime(1000);
			if (!getPlatform().equals("Web")) {
				Actions a = new Actions(getDriver());
				a.sendKeys(input);
				a.perform();
			} else {
				WebElement element = findElement(byLocator);
				element.sendKeys(input);
			}
			input = input.split("\n")[0];
			logger.info("Typed the value " + input + " into " + FieldName);
			extent.extentLogger("", "Typed the value " + input + " into " + FieldName);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * Wait
	 *
	 * @param x seconds to lock
	 */
	public void Wait(int x) {
		try {
			getDriver().manage().timeouts().implicitlyWait(x, TimeUnit.SECONDS);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public void waitTime(int x) {
		try {
			Thread.sleep(x);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * @param keyevent pass the android key event value to perform specific action
	 * 
	 */
	public void adbKeyevents(int keyevent) {
		try {
			String cmd = "adb shell input keyevent" + " " + keyevent;
			Runtime.getRuntime().exec(cmd);
			logger.info("Performed the Keyevent" + keyevent);
			extent.extentLogger("adbKeyevent", "Performed the Keyevent" + keyevent);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * @param byLocator
	 * @returns the list count of the element
	 */
	public int getCount(By byLocator) {

		int count = 0;
		try {
			count = getDriver().findElements(byLocator).size();
			logger.info("List count for" + " " + byLocator + " " + "is" + " " + count);
			extent.extentLogger("getCount", "List count for" + " " + byLocator + " " + "is" + " " + count);
		} catch (Exception e) {
			logger.error(e);
		}
		return count;
	}

	public List<WebElement> findElements(By byLocator) {
		if (getPlatform().equals("Android") || getPlatform().equals("MPWA")) {
			return getDriver().findElements(byLocator);
		} else {
			return getWebDriver().findElements(byLocator);
		}
	}

	/**
	 * @param i
	 * @param byLocator
	 * @returns the By locator
	 */
	public String iterativeXpathtoStringGenerator(int temp, By byLocator, String property) {

		WebElement element = null;
		String drug = null;
		try {

			String xpath = byLocator.toString();
			String var = "'" + temp + "'";
			xpath = xpath.replaceAll("__placeholder", var);
			String[] test = xpath.split(": ");
			xpath = test[1];
			element = getDriver().findElement(By.xpath(xpath));
			drug = element.getAttribute(property);
		} catch (Exception e) {
			System.out.println(e);
		}
		return drug;
	}

	/**
	 * Back
	 */
	public void Back(int x) {

		try {
			if (getPlatform().equals("Web")) {
				for (int i = 0; i < x; i++) {
					getWebDriver().navigate().back();
					logger.info("Back button is tapped");
					extent.extentLogger("Back", "Back button is tapped");
				}
			} else if (getPlatform().equals("Android") || getPlatform().equals("MPWA")) {
				for (int i = 0; i < x; i++) {
					getDriver().navigate().back();
					logger.info("Back button is tapped");
					extent.extentLogger("Back", "Back button is tapped");
					waitTime(3000);
				}
			}
		} catch (Exception e) {
			logger.error(e);
			screencapture();
		}
	}

	/**
	 * Finding the duplicate elements in the list
	 * 
	 * @param mono
	 * @param content
	 * @param dosechang
	 * @param enteral
	 */
	public List<String> findDuplicateElements(List<String> mono) {

		List<String> duplicate = new ArrayList<String>();
		Set<String> s = new HashSet<String>();
		try {
			if (mono.size() > 0) {
				for (String content : mono) {
					if (s.add(content) == false) {
						int i = 1;
						duplicate.add(content);
						System.out.println("List of duplicate elements is" + i + content);
						i++;
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return duplicate;
	}

	/**
	 * @param contents
	 * @return the list without duplicate elements
	 */
	public List<String> removeDuplicateElements(List<String> contents) {

		LinkedHashSet<String> set = new LinkedHashSet<String>(contents);
		ArrayList<String> listWithoutDuplicateElements = new ArrayList<String>();
		try {

			if (contents.size() > 0) {
				listWithoutDuplicateElements = new ArrayList<String>(set);
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return listWithoutDuplicateElements;
	}

	/**
	 * @param i
	 * @param byLocator
	 */
	public void iteratorClick(int temp, By byLocator) {

		try {
			String xpath = byLocator.toString();
			String var = "'" + temp + "'";
			xpath = xpath.replaceAll("__placeholder", var);
			String[] test = xpath.split(": ");
			xpath = test[1];
			getDriver().findElement(By.xpath(xpath)).click();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * get specific property value of a web element and stores to string variable.
	 * 
	 * @param property  the property of the element.
	 * @param byLocator the by locator
	 * @return value of the property.
	 */
	public String getElementPropertyToString(String property, By byLocator, String object) {
		String propertyValue = null;
		try {
			WebElement element = findElement(byLocator);
			propertyValue = element.getAttribute(property);
		} catch (Exception e) {
			logger.error(e);
		}
		return propertyValue;
	}

	/**
	 * @param sorted
	 * @return true if the list is sorted
	 * @return false if the list is not sorted
	 */
	public boolean checkListIsSorted(List<String> ListToSort) {

		boolean isSorted = false;

		if (ListToSort.size() > 0) {
			try {
				if (Ordering.natural().isOrdered(ListToSort)) {
					extent.extentLogger("Check sorting", "List is sorted");
					logger.info("List is sorted");
					isSorted = true;
					return isSorted;
				} else {
					extent.extentLogger("Check sorting", ListToSort + " " + "List is not sorted");
					logger.info(ListToSort + "List is notsorted");
					isSorted = false;
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		} else {
			logger.info("The size of the list is zero");
			extent.extentLogger("", ListToSort + " " + "There are no elements in the list to check the sort order");
		}
		return isSorted;
	}

	/**
	 * @param byLocator
	 * @returns the list count of the element
	 */
	public int iterativeGetCount(int temp, By byLocator) {

		int count = 0;
		try {

			String xpath = byLocator.toString();
			String var = "'" + temp + "'";
			xpath = xpath.replaceAll("__placeholder", var);
			String[] test = xpath.split(": ");
			xpath = test[1];
			count = getDriver().findElements(By.xpath(xpath)).size();
			logger.info("List count for" + " " + xpath + " " + "is" + " " + count);
			extent.extentLogger("getCount", "List count for" + " " + xpath + " " + "is" + " " + count);
		} catch (Exception e) {
			logger.error(e);
		}
		return count;
	}

	/**
	 * @param temp
	 * @param byLocator
	 * @return
	 */
	public By iterativeXpathText(String temp, By byLocator) {

		By searchResultList = null;

		try {

			String xpath = byLocator.toString();
			String var = "'" + temp + "'";
			xpath = xpath.replaceAll("__placeholder", var);
			String[] test = xpath.split(": ");
			xpath = test[1];
			searchResultList = By.xpath(xpath);
		} catch (Exception e) {
			System.out.println(e);
		}
		return searchResultList;
	}

	/**
	 * @param byLocator Checks whether element is not displayed
	 */
	public void checkElementNotPresent(By byLocator) {
		boolean isElementNotPresent = true;
		try {
			isElementNotPresent = checkcondition(byLocator);
			softAssert.assertEquals(isElementNotPresent, false);
			logger.info("" + byLocator + " " + "is not displayed");
			extent.extentLogger("checkElementNotPresent", "" + byLocator + "is not displayed");
		} catch (Exception e) {
			softAssert.assertEquals(isElementNotPresent, true, "Element" + byLocator + " " + "is visible");
//			softAssert.assertAll();
			logger.error(byLocator + " " + "is visible");
			extent.extentLogger("checkElementNotPresent", "" + byLocator + "is displayed");
			screencapture();
		}
	}

	/**
	 * Swipes the screen in left or right or Up or Down or direction
	 * 
	 * @param direction to swipe Left or Right or Up or Down
	 * @param count     to swipe
	 */
	@SuppressWarnings("rawtypes")
	public void Swipe(String direction, int count) {
		touchAction = new TouchAction(getDriver());
		String dire = direction;
		try {
			if (dire.equalsIgnoreCase("LEFT")) {

				for (int i = 0; i < count; i++) {
					Dimension size = getDriver().manage().window().getSize();
					int startx = (int) (size.width * 0.5);
					int endx = (int) (size.width * 0.1);
					int starty = size.height / 2;
					touchAction.press(PointOption.point(startx, starty))
							.waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
							.moveTo(PointOption.point(endx, starty)).release().perform();
					logger.info("Swiping screen in " + " " + dire + " direction" + " " + (i + 1) + " times");
					extent.extentLogger("SwipeLeft",
							"Swiping screen in " + " " + dire + " direction" + " " + (i + 1) + " times");
				}
			} else if (dire.equalsIgnoreCase("RIGHT")) {

				for (int j = 0; j < count; j++) {
					Dimension size = getDriver().manage().window().getSize();
					int endx = (int) (size.width * 0.8);
					int startx = (int) (size.width * 0.20);
					if (size.height > 2000) {
						int starty = (int) (size.height / 2);
						touchAction.press(PointOption.point(startx, starty))
								.waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
								.moveTo(PointOption.point(endx, starty)).release().perform();
					} else {
						int starty = (int) (size.height / 1.5);
						touchAction.press(PointOption.point(startx, starty))
								.waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
								.moveTo(PointOption.point(endx, starty)).release().perform();
					}

					logger.info("Swiping screen in " + " " + dire + " direction" + " " + (j + 1) + " times");
					extent.extentLogger("SwipeRight",
							"Swiping screen in " + " " + dire + " direction" + " " + (j + 1) + " times");
				}
			} else if (dire.equalsIgnoreCase("UP")) {

				for (int j = 0; j < count; j++) {
					Dimension size = getDriver().manage().window().getSize();
					System.out.println("size : " + size);
					int starty = (int) (size.height * 0.70);// 0.8
					int endy = (int) (size.height * 0.30);// 0.2
					int startx = size.width / 2;
					touchAction.press(PointOption.point(startx, starty))
							.waitAction(WaitOptions.waitOptions(Duration.ofMillis(3000)))
							.moveTo(PointOption.point(startx, endy)).release().perform();
					logger.info("Swiping screen in " + " " + dire + " direction" + " " + (j + 1) + " times");
					extent.extentLogger("SwipeUp",
							"Swiping screen in " + " " + dire + " direction" + " " + (j + 1) + " times");

				}
			} else if (dire.equalsIgnoreCase("DOWN")) {
				for (int j = 0; j < count; j++) {

					Dimension size = getDriver().manage().window().getSize();
					int starty = (int) (size.height * 0.70);
					int endy = (int) (size.height * 0.30);
					int startx = size.width / 2;
					touchAction.press(PointOption.point(startx, endy))
							.waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
							.moveTo(PointOption.point(startx, starty)).release().perform();
					logger.info("Swiping screen in " + " " + dire + " direction" + " " + (j + 1) + " times");
					extent.extentLogger("SwipeDown",
							"Swiping screen in " + " " + dire + " direction" + " " + (j + 1) + " times");

				}
			}

		} catch (Exception e) {
			logger.error(e);

		}

	}

	@SuppressWarnings("rawtypes")
	public void SwipeRail(By From) throws Exception {

		WebElement element = findElement(From);
		Dimension size = getDriver().manage().window().getSize();
		int startx = (int) (size.width * 0.8);
		int endx = (int) (size.width * 0.1);

		String eleY = element.getAttribute("y");
		int currentPosY = Integer.parseInt(eleY);
		System.out.println(currentPosY);
		currentPosY = Integer.parseInt(eleY) + 200;
		System.out.println(currentPosY);
		touchAction = new TouchAction(getDriver());
		touchAction.press(PointOption.point(startx, currentPosY))
				.waitAction(WaitOptions.waitOptions(Duration.ofMillis(2000)))
				.moveTo(PointOption.point(endx, currentPosY)).release().perform();
	}

	/**
	 * Swipes the screen in left or right or Up or Down or direction
	 * 
	 * @param direction to swipe Left or Right or Up or Down
	 * @param count     to swipe
	 */
	@SuppressWarnings("rawtypes")
	public void PartialSwipe(String direction, int count) {
		touchAction = new TouchAction(getDriver());
		String dire = direction;

		try {

			if (dire.equalsIgnoreCase("LEFT")) {

				for (int i = 0; i < count; i++) {
					Dimension size = getDriver().manage().window().getSize();
					int startx = (int) (size.width * 0.4);
					int endx = (int) (size.width * 0.1);
					int starty = size.height / 2;
					// getDriver().swipe(startx, starty, endx, starty, 1000);
					touchAction.press(PointOption.point(startx, starty))
							.waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
							.moveTo(PointOption.point(endx, starty)).release().perform();
					logger.info("Swiping screen in " + " " + dire + " direction" + " " + (i + 1) + " times");
					extent.extentLogger("SwipeLeft",
							"Swiping screen in " + " " + dire + " direction" + " " + (i + 1) + " times");
				}
			} else if (dire.equalsIgnoreCase("RIGHT")) {

				for (int j = 0; j < count; j++) {
					Dimension size = getDriver().manage().window().getSize();
					int endx = (int) (size.width * 0.4);
					int startx = (int) (size.width * 0.1);
					int starty = size.height / 2;
					touchAction.press(PointOption.point(startx, starty))
							.waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
							.moveTo(PointOption.point(endx, starty)).release().perform();
					logger.info("Swiping screen in " + " " + dire + " direction" + " " + (j + 1) + " times");
					extent.extentLogger("SwipeRight",
							"Swiping screen in " + " " + dire + " direction" + " " + (j + 1) + " times");
				}
			} else if (dire.equalsIgnoreCase("UP")) {

				for (int j = 0; j < count; j++) {
					Dimension size = getDriver().manage().window().getSize();
					int starty = (int) (size.height * 0.40);
					int endy = (int) (size.height * 0.1);
					int startx = size.width / 2;
					touchAction.press(PointOption.point(startx, starty))
							.waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
							.moveTo(PointOption.point(startx, endy)).release().perform();
					logger.info("Swiping screen in " + dire + " direction" + " " + (j + 1) + " times");
					extent.extentLogger("SwipeUp",
							"Swiping screen in " + " " + dire + " direction" + " " + (j + 1) + " times");

				}
			} else if (dire.equalsIgnoreCase("DOWN")) {
				for (int j = 0; j < count; j++) {
					Dimension size = getDriver().manage().window().getSize();
					int starty = (int) (size.height * 0.4);
					int endy = (int) (size.height * 0.1);
					int startx = size.width / 2;
					touchAction.press(PointOption.point(startx, endy))
							.waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
							.moveTo(PointOption.point(startx, starty)).release().perform();
					logger.info("Swiping screen in " + " " + dire + " direction" + " " + (j + 1) + " times");
					extent.extentLogger("SwipeDown",
							"Swiping screen in " + " " + dire + " direction" + " " + (j + 1) + " times");

				}
			}

		} catch (Exception e) {
			logger.error(e);

		}
	}

	@SuppressWarnings("rawtypes")
	public void SwipeRailContentCards(By From) throws Exception {

		Dimension size = getDriver().manage().window().getSize();
		int screenWidth = (int) (size.width * 0.8);

		WebElement element = findElement(From);
		String eleX = element.getAttribute("x");
		String eleY = element.getAttribute("y");
		int currentPosX = Integer.parseInt(eleX);
		int currentPosY = Integer.parseInt(eleY);

		currentPosX = currentPosX + screenWidth;
		currentPosY = currentPosY + 150;

		touchAction = new TouchAction(getDriver());
		touchAction.press(PointOption.point(currentPosX, currentPosY))
				.waitAction(WaitOptions.waitOptions(Duration.ofMillis(2000))).moveTo(PointOption.point(0, currentPosY))
				.release().perform();
	}

	/**
	 * Drag window
	 * 
	 * @param byLocator, byTimer
	 */
	@SuppressWarnings("rawtypes")
	public void DragWindow(By byLocator, String direction) throws Exception {
		WebElement element = getDriver().findElement(byLocator);
		touchAction = new TouchAction(getDriver());
		if (direction.equalsIgnoreCase("LEFT")) {
			Dimension size = element.getSize();
			int startx = (int) (size.width * 0.4);
			int endx = (int) (size.width * 0.1);
			int starty = size.height / 2;
			touchAction.longPress(PointOption.point(startx, starty))
					.waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
					.moveTo(PointOption.point(endx, starty)).release().perform();
			logger.info("Swiping " + " " + direction + " direction");
			extent.extentLogger("SwipeLeft", "Swiping " + " " + direction + " direction");
		} else if (direction.equalsIgnoreCase("DOWN")) {
			Dimension size = getDriver().manage().window().getSize();
			int starty = (int) (size.height * 0.80);
			int endy = (int) (size.height * 0.20);
			int startx = size.width / 2;
			touchAction.longPress(PointOption.point(startx, endy))
					.waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
					.moveTo(PointOption.point((int) startx, (int) starty)).release().perform();
			logger.info("Swiping " + " " + direction + " direction");
			extent.extentLogger("SwipeLeft", "Swiping " + " " + direction + " direction");
		}
	}

	// Calender wheeler
	public void dateWheeler(String text) {

		String uiSelector = "new UiSelector().textMatches(\"" + text + "\")";

		String command = "new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(" + uiSelector
				+ ");";

		getDriver().findElement(MobileBy.AndroidUIAutomator(command));

	}

	public void dateWheeler1(String text) {

		String uiSelector = "new UiSelector().textMatches(\"" + text + "\")";

		String command = "new UiScrollable(new UiSelector().scrollable(true).instance(1)).scrollIntoView(" + uiSelector
				+ ");";

		getDriver().findElement(MobileBy.AndroidUIAutomator(command));

	}

	public void dateWheeler2(String text) {

		String uiSelector = "new UiSelector().textMatches(\"" + text + "\")";

		String command = "new UiScrollable(new UiSelector().scrollable(true).instance(2)).scrollIntoView(" + uiSelector
				+ ");";

		getDriver().findElement(MobileBy.AndroidUIAutomator(command));
	}

	public void datePicker(By element) throws Exception {
		String pastyear = null;
		String presentyear = null;
		int year;

		if (verifyElementExist(element, "Year in Sign up Screen")) {

			pastyear = getText(element);
			logger.info("Present year Before swiping:" + pastyear);
			extent.extentLogger("", "Present year Before swiping: " + pastyear);
			for (int i = 1; i <= 15; i++) {
				swipeDatePicker(element, "Year box");
				waitTime(2000);
				presentyear = getText(element);
				year = Integer.parseInt(presentyear);
				logger.info("For swipe " + i + " Present year is :" + year);
				extent.extentLogger("", "For swipe " + i + "Present year is :" + year);
				if (year < 2004) {
					logger.info("Swiped more than 18 years");
					extent.extentLogger("", "Swiped more than 18 years");
					break;
				} else {
					logger.info("Year present less than 18 years looking for next swipe");
					extent.extentLogger("", "Year present less than 18 years looking for next swipe");
				}

			}

		}
		waitTime(2000);

	}

	public void swipeDatePicker(By byLocator, String box) {
		WebElement element = getDriver().findElement(byLocator);

		TouchAction action = new TouchAction(getDriver());

		int pointX = element.getLocation().getX();
		int pointY = element.getLocation().getY();
		System.out.println(element.getLocation());
		// 222,350
		int incY = pointY + 250;
		// 222,600
		waitTime(2000);
		SwipeAnElement(element, pointX, incY);
		logger.info("Swiped the " + box);
		extent.extentLogger("", "Swiped the " + box);

	}

	/**
	 * Seek by dimensions
	 * 
	 * @param byLocator
	 */

	@SuppressWarnings({ "rawtypes", "unused" })
	public void TapToSeekChromecast(By byLocator) throws Exception {
		WebElement element = getDriver().findElement(byLocator);
		Dimension size = element.getSize();
		int startx = (int) (size.width);
		TouchAction action = new TouchAction(getDriver());
		SwipeAnElement(element, startx, 0);
		logger.info("Scrolling the seek bar");
	}

	/**
	 * @param bundleID
	 */
	public void launchiOSApp(String bundleID) {

		try {
			iOSDriver = (IOSDriver<WebElement>) getDriver();
			logger.info("Started the bundle id" + " " + bundleID);
			extent.extentLogger("Started the bundle id" + " " + bundleID, "Started the bundle id" + " " + bundleID);
		} catch (Exception e) {
			logger.info("Unable to Start the bundle id" + " " + bundleID);
			extent.extentLogger("Unable to Start the bundle id" + " " + bundleID,
					"Unable to Start the bundle id" + " " + bundleID);
		}
	}

	/**
	 * Get the Mobile Orientation
	 */
	public void GetAndVerifyOrientation(String Value) {
		ScreenOrientation orientation = getDriver().getOrientation();
		String ScreenOrientation = orientation.toString();
		try {
			softAssert.assertEquals(ScreenOrientation.equalsIgnoreCase(Value), true,
					"The screen Orientation is " + ScreenOrientation);
			logger.info("The screen Orientation is " + ScreenOrientation);
			extent.extentLogger("Screen Orientation", "The screen Orientation is " + ScreenOrientation);
		} catch (Exception e) {
			softAssert.assertEquals(false, true, "The screen Orientation is not " + ScreenOrientation);
//			softAssert.assertAll();
			logger.error("The screen Orientation is not " + ScreenOrientation);
			extent.extentLogger("Screen Orientation", "The screen Orientation is not " + ScreenOrientation);
			screencapture();
		}
	}

	/**
	 * Closes the iOS keyboard
	 */
	public void closeIosKeyboard() {

		try {
			iOSDriver = (IOSDriver<WebElement>) getDriver();
			extent.extentLogger("Hiding keyboard successful", "Hiding keyboard successful");
		} catch (Exception e) {
			extent.extentLogger("Hiding keyboard not successful", "Hiding keyboard not successful");
		}
	}

	/**
	 * closes the application
	 */
	public void closeiOSApp() {
		try {
			iOSDriver = (IOSDriver<WebElement>) getDriver();
			iOSDriver.closeApp();
			extent.extentLogger("Killed the appliaction successfully", "Killed the appliaction successfully");
		} catch (Exception e) {
			extent.extentLogger("Unable to Kill the application", "Unable to Kill the application");

		}
	}

	/**
	 * closes the Android application
	 */
	public void closeAndroidApp() {
		try {
			getDriver().resetApp();
			extent.extentLogger("Killed the application successfully", "Killed the application successfully");
		} catch (Exception e) {
			extent.extentLogger("Unable to Kill the application", "Unable to Kill the application");

		}
	}

	/**
	 * Verifies if a new page or app is open
	 */

	public boolean newPageOrNt() {
		boolean app = false;
		try {
			String cmd = "adb shell \"dumpsys window windows | grep -E 'mCurrentFocus|mFocusedApp'\"";
			Process p = Runtime.getRuntime().exec(cmd);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String outputText = "";
			while ((outputText = br.readLine()) != null) {
				if (!outputText.trim().contains("com.tv.v18.viola")) {
					app = true;
					logger.info("The app is navigated to ad page");
					extent.extentLogger("Navigated to ad page or not", "The app is navigated to ad page");
				} else {
					logger.error("The app is not navigated to ad page");
					extent.extentLogger("Navigated to ad page or not", "The app is not navigated to ad page");
				}
			}
			br.close();
			p.waitFor();
		} catch (Exception e) {
			System.out.println(e);
		}
		return app;
	}

	public void IosDragWindow(By byLocator, String direction) throws Exception {
		WebElement element = getDriver().findElement(byLocator);
		@SuppressWarnings({ "rawtypes", "unused" })
		TouchAction action = new TouchAction(getDriver());
		if (direction.equalsIgnoreCase("LEFT")) {
			Dimension size = element.getSize();
			int startx = (int) (size.width * 0.4);
			System.out.println(startx);
			int endx = 0;
			System.out.println(endx);
			int starty = 1250;
			System.out.println(starty);
			SwipeAnElement(element, startx, starty);
			logger.info("Swiping " + " " + direction + " direction");
			extent.extentLogger("SwipeLeft", "Swiping " + " " + direction + " direction");
		}
	}

	public String getAttributValue(String property, By byLocator) throws Exception {
		String Value = null;
		WebElement element = findElement(byLocator);
		Value = element.getAttribute(property);
		return Value;
	}

	/*
	 * public void captureScreenshotAndCompare(String SSName) throws
	 * InterruptedException { Thread.sleep(10000); File src =
	 * getDriver().getScreenshotAs(OutputType.FILE); String dir =
	 * System.getProperty("user.dir"); String fileName = dir +
	 * "/Applitool/baseLine/" + SSName + ".png"; System.out.println(fileName); try {
	 * FileUtils.copyFile(src, new File(fileName)); } catch (IOException e) {
	 * System.out.println(e.getMessage()); } BufferedImage img; try { img =
	 * ImageIO.read(new File(fileName)); getEye().checkImage(img, SSName);
	 * extent.extentLogger("UI Validation", "UI for " + SSName + " is validated"); }
	 * catch (IOException e) { System.out.println(e.getMessage()); } }
	 */

	public void SwipeAnElement(WebElement element, int posx, int posy) {
		AndroidTouchAction touch = new AndroidTouchAction(getDriver());
		touch.longPress(LongPressOptions.longPressOptions().withElement(ElementOption.element(element)))
				.waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000))).moveTo(PointOption.point(posx, posy))
				.release().perform();
	}

	public void longPressContent(By element) throws Exception {
		AndroidTouchAction touch = new AndroidTouchAction(getDriver());
		touch.longPress(LongPressOptions.longPressOptions().withElement(ElementOption.element(findElement(element))))
				.release().perform();

		TouchActions action = new TouchActions(getDriver());
		action.singleTap(findElement(element));
		action.click();

	}

	public boolean verifyElementExist1(WebElement ele, String str) throws Exception {
		try {
			WebElement element = ele;
			if (element.isDisplayed()) {
				extent.extentLogger("checkElementPresent", "<b>" + str + "</b> is displayed");
				logger.info("" + str + " is displayed");
				return true;
			}
		} catch (Exception e) {
			extent.extentLogger("checkElementPresent", "<b>" + str + "</b> is not displayed");
			logger.info(str + " is not displayed");
			return false;
		}
		return false;
	}

	public boolean checkcondition(String s) throws Exception {
		boolean iselementPresent = false;
		try {
			String element = "//*[@text='[" + s + "]']";
			iselementPresent = ((WebElement) getDriver().findElementsByXPath(element)).isDisplayed();
		} catch (Exception e) {
			iselementPresent = false;
		}
		return iselementPresent;
	}

	public void switchtoLandscapeMode() throws IOException {
		Runtime.getRuntime().exec(
				"adb shell content insert --uri content://settings/system --bind name:s:user_rotation --bind value:i:1");
	}

	public void switchtoPortraitMode() throws IOException {
		Runtime.getRuntime().exec(
				"adb shell content insert --uri content://settings/system --bind name:s:user_rotation --bind value:i:0");
	}

	@SuppressWarnings("rawtypes")
	public void PartialSwipeInConsumptionScreen(String direction, int count) {
		touchAction = new TouchAction(getDriver());
		String dire = direction;

		try {

			if (dire.equalsIgnoreCase("UP")) {

				for (int j = 0; j < count; j++) {
					Dimension size = getDriver().manage().window().getSize();
					int starty = (int) (size.height * 0.8);
					int endy = (int) (size.height * 0.5);
					int startx = size.width / 2;
					touchAction.press(PointOption.point(startx, starty))
							.waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
							.moveTo(PointOption.point(startx, endy)).release().perform();
					logger.info("Swiping screen in " + dire + " direction" + " " + (j + 1) + " times");
					extent.extentLogger("SwipeUp",
							"Swiping screen in " + " " + dire + " direction" + " " + (j + 1) + " times");

				}
			} else if (dire.equalsIgnoreCase("DOWN")) {
				for (int j = 0; j < count; j++) {
					Dimension size = getDriver().manage().window().getSize();
					int starty = (int) (size.height * 0.8);
					int endy = (int) (size.height * 0.5);
					int startx = size.width / 2;
					touchAction.press(PointOption.point(startx, endy))
							.waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
							.moveTo(PointOption.point(startx, starty)).release().perform();
					logger.info("Swiping screen in " + " " + dire + " direction" + " " + (j + 1) + " times");
					extent.extentLogger("SwipeDown",
							"Swiping screen in " + " " + dire + " direction" + " " + (j + 1) + " times");

				}
			}

		} catch (Exception e) {
			logger.error(e);

		}
	}

//====================================================================================================================================
	/** ::::::::::::::::Web Utilities:::::::::::: */

	/**
	 * Function to ExplicitWait Visibility
	 * 
	 * @param element
	 * @param time
	 * @throws Exception
	 */

	public void explicitWaitVisibilityNonDefault(By element, int time) throws Exception {
		wait.until(ExpectedConditions.visibilityOf(findElement(element)));
	}

	public void explicitWaitVisibility(By element, int time) throws Exception {
		wait.until(ExpectedConditions.visibilityOf(findElement(element)));
	}

	/**
	 * Function to ExplicitWait for Clickable
	 * 
	 * @param element
	 * @param time
	 * @throws Exception
	 */
	public void explicitWaitClickable(By element, int time) throws Exception {
		wait.until(ExpectedConditions.elementToBeClickable(findElement(element)));
	}

	/**
	 * Function to ExplicitWait for windows
	 * 
	 * @param noOfWindows
	 */
	public static void explicitWaitForWindows(int noOfWindows) {
		wait.until(ExpectedConditions.numberOfWindowsToBe(noOfWindows));
	}

	/**
	 * Function for ExplicitWait of Element Refresh
	 * 
	 * @throws Exception
	 */
	public void explicitWaitForElementRefresh(By element) throws Exception {
		wait.until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(findElement(element))));
	}

	/**
	 * Function for implicitWait
	 */
	public void implicitWait() {
		getWebDriver().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	/**
	 * Function to select by visible text from drop down
	 * 
	 * @param element
	 * @param value
	 * @throws Exception
	 */
	public void selectByVisibleTextFromDD(By element, String value) throws Exception {
		explicitWaitVisibility(element, 20);
		Select select = new Select(findElement(element));
		select.selectByVisibleText(value);
	}

	/**
	 * Function to select by value from drop down
	 * 
	 * @param element
	 * @param value
	 * @throws Exception
	 */
	public void selectByValueFromDD(By element, String value) throws Exception {
		explicitWaitVisibility(element, 20);
		Select select = new Select(findElement(element));
		select.selectByValue(value);
	}

	/**
	 * Function to select By index From Drop down
	 * 
	 * @param element
	 * @param value
	 * @throws Exception
	 */
	public void selectByIndexFromDD(By element, String value) throws Exception {
		explicitWaitVisibility(element, 20);
		Select select = new Select(findElement(element));
		select.selectByValue(value);
	}

	/**
	 * Function to get First Element from Drop down
	 * 
	 * @param element
	 * @return
	 * @throws Exception
	 */
	public String getFirstElementFromDD(By element) throws Exception {
		Select select = new Select(findElement(element));
		return select.getFirstSelectedOption().getText();
	}

	/**
	 * Function to scroll down
	 */
	public static void scrollDownWEB() {
		js.executeScript("window.scrollBy(0,250)", "");
	}

	/**
	 * Function to Scroll By
	 */
	public static void scrollByWEB() {
		js.executeScript("window.scrollBy(0,250)", "");
	}

	/**
	 * Function to scroll bottom of page
	 */
	public static void scrollToBottomOfPageWEB() {
		js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
	}

	public static void scrollToBottomOfPage() {
		js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
	}

	/**
	 * Function to scroll to top of the page
	 */
	public static void scrollToTopOfPageWEB() {
		js.executeScript("window.scrollBy(0,-250)", "");
	}

	public static void scrollToTopOfPage() {
		js.executeScript("window.scrollBy(0,-250)", "");
	}

	/**
	 * Function Scroll to Element
	 * 
	 * @param element
	 * @throws Exception
	 */
	public void ScrollToTheElement(By element) throws Exception {
		js.executeScript("arguments[0].scrollIntoView(true);", findElement(element));
		js.executeScript("window.scrollBy(0,-50)", "");
	}

	/**
	 * Function to switch to window
	 * 
	 * @param noOfWindows
	 */
	public void switchToWindow(int noOfWindows) {
		try {
			wait.until(ExpectedConditions.numberOfWindowsToBe(noOfWindows));
			for (String winHandle : getWebDriver().getWindowHandles()) {
				win.add(winHandle);
				getWebDriver().switchTo().window(winHandle);
				getWebDriver().manage().window().maximize();
			}
		} catch (Exception e) {
			System.out.println("\n No window is displayed!");
		}
	}

	/**
	 * Function to switch to parent Window
	 */
	public void switchToParentWindow() {
		try {
			getWebDriver().switchTo().window(win.get(0));
		} catch (Exception e) {
			System.out.println("\n No window is displayed!");
		}
	}

	/**
	 * Function for hard sleep
	 * 
	 * @param seconds
	 */
	public void sleep(int seconds) {
		try {
			int ms = 1000 * seconds;
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Function to switch the tab
	 * 
	 * @param tab
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void switchTab(int tab) {
		ArrayList<String> window = new ArrayList(getWebDriver().getWindowHandles());
		getWebDriver().switchTo().window(window.get(tab));
	}

	/**
	 * Function to generate random integer of specified maxValue
	 * 
	 * @param maxValue
	 * @return
	 */
	public String generateRandomInt(int maxValue) {
		Random rand = new Random();
		int x = rand.nextInt(maxValue);
		String randomInt = Integer.toString(x);
		return randomInt;
	}

	public String RandomIntegerGenerator(int n) {
		String number = "0123456789";
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0; i < n; i++) {
			int index = (int) (number.length() * Math.random());

			sb.append(number.charAt(index));
		}
		return sb.toString();
	}

	/**
	 * Function to generate Random String of length 4
	 * 
	 * @return
	 */
	public String generateRandomString(int size) {
		String strNumbers = "abcdefghijklmnopqrstuvwxyzacvbe";
		Random rnd = new Random();
		StringBuilder strRandomNumber = new StringBuilder(9);
		strRandomNumber.append(strNumbers.charAt(rnd.nextInt(strNumbers.length())));
		String s1 = strRandomNumber.toString().toUpperCase();
		for (int i = 1; i < size; i++) {
			strRandomNumber.append(strNumbers.charAt(rnd.nextInt(strNumbers.length())));
		}
		return s1 + strRandomNumber.toString();
	}

	/**
	 * Function to generate Random characters of specified length
	 * 
	 * @param candidateChars
	 * @param length
	 * @return
	 */
	public String generateRandomChars(String candidateChars, int length) {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(candidateChars.charAt(random.nextInt(candidateChars.length())));
		}
		return sb.toString();
	}

	/**
	 * Function to generate Random Integer between range
	 * 
	 * @param maxValue
	 * @param minValue
	 * @return
	 * @throws InterruptedException
	 */
	public String generateRandomIntwithrange(int maxValue, int minValue) throws InterruptedException {
		Thread.sleep(2000);
		Random rand = new Random();
		int x = rand.nextInt(maxValue - minValue) + 1;
		String randomInt = Integer.toString(x);
		System.out.println("Auto generate of number from generic method : " + randomInt);
		return randomInt;
	}

	/**
	 * Function to drag and drop an object
	 * 
	 * @param From
	 * @param To
	 * @throws Exception
	 */
	public void dragnddrop(By From, By To) throws Exception {
		WebElement Drag = findElement(From);
		WebElement Drop = findElement(To);
		Thread.sleep(1000);
		Actions builder = new Actions(getDriver());
		builder.clickAndHold(Drag).moveToElement(Drop).release(Drop).build().perform();
	}

	/**
	 * Function Convert from String to Integer @param(String)
	 */
	public int convertToInt(String string) {
		int i = Integer.parseInt(string);
		return i;
	}

	/**
	 * Function Convert from Integer to String @param(integer)
	 */
	public String convertToString(int i) {
		String string = Integer.toString(i);
		return string;
	}

	/**
	 * Click On element without waiting or verifying
	 *
	 * @param byLocator the by locator
	 *
	 */
	public void clickDirectly(By byLocator, String validationtext) throws Exception {
		try {
			getDriver().findElement(byLocator).click();
			logger.info("Clicked on the " + validationtext);
			extent.extentLogger("click", "Clicked on the <b> " + validationtext);
		} catch (Exception e) {
			logger.error(e);
			screencapture();
		}
	}

	public void verifyAlert() {
		try {
			getWebDriver().switchTo().alert().dismiss();
			logger.info("Dismissed the alert Pop Up");
			extent.extentLogger("Alert PopUp", "Dismissed the alert Pop Up");
		} catch (Exception e) {

		}
	}

	public void acceptAlert() {
		try {
			getWebDriver().switchTo().alert().accept();
			logger.info("Dismissed the alert Pop Up");
			extent.extentLogger("Alert PopUp", "Dismissed the alert Pop Up");
		} catch (Exception e) {

		}
	}

	public boolean clickElementWithLocator(By locator) throws Exception {
		try {
			getDriver().findElement(locator).click();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean clickElementWithWebElement(WebElement element) throws Exception {
		try {
			element.click();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static int timeToSec(String s) {
		String[] t = s.split(":");
		int num = 0;
		System.out.println(t.length);

		if (t.length == 2) {
			num = Integer.parseInt(t[0]) * 60 + Integer.parseInt(t[1]); // minutes since 00:00
		}
		if (t.length == 3) {
			num = ((Integer.parseInt(t[0]) * 60) * 60) + Integer.parseInt(t[1]) * 60 + Integer.parseInt(t[2]);
		}

		return num;
	}

	public static void partialScrollDown() {
		JavascriptExecutor jse = (JavascriptExecutor) getWebDriver();
		jse.executeScript("window.scrollBy(0,500)", "");
	}

	public void clickByElement(WebElement ele, String validationtext) throws Exception {
		try {
			WebElement element = ele;
			element.click();
			logger.info("Clicked on the " + validationtext);
			extent.extentLogger("click", "Clicked on the <b> " + validationtext);
		} catch (Exception e) {
			logger.error(e);
			screencapture();
		}
	}

	public boolean verifyElementEnabled(By byLocator, String str) throws Exception {

		try {
			WebElement element = findElement(byLocator);
			if (element.isEnabled()) {
				extent.extentLogger("checkElementPresent", "" + str + " is displayed");
				logger.info("" + str + " is displayed");
				return true;
			}
		} catch (Exception e) {
			extent.extentLogger("checkElementPresent", "" + str + " is not displayed");
			logger.info(str + " is not displayed");
			return false;
		}
		return false;
	}

	public int getCountweb(By byLocator) {

		int count = 0;
		try {
			count = getWebDriver().findElements(byLocator).size();
			logger.info("List count for" + " " + byLocator + " " + "is" + " " + count);
			extent.extentLogger("getCount", "List count for" + " " + byLocator + " " + "is" + " " + count);
		} catch (Exception e) {
			logger.error(e);
		}
		return count;
	}

	public boolean waitForElementAndClickIfPresent(By locator, int seconds, String message)
			throws InterruptedException {
		try {
			if (getPlatform().equals("Web")) {
				for (int time = 0; time <= seconds; time++) {
					try {
						getWebDriver().findElement(locator).click();
						logger.info("Clicked element " + message);
						extent.extentLogger("clickedElement", "Clicked element " + message);
						return true;
					} catch (Exception e) {
						Thread.sleep(1000);
					}
				}
			} else if (getPlatform().equals("Android") || getPlatform().equals("MPWA")) {
				for (int time = 0; time <= seconds; time++) {
					try {
						getDriver().findElement(locator).click();
						logger.info("Clicked on " + message);
						extent.extentLogger("clickedElement", "Clicked on " + message);
						return true;
					} catch (Exception e) {
						Thread.sleep(1000);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
			screencapture();
		}
		return false;
	}

	public String RandomStringGenerator(int n) {
		{

			String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
			StringBuilder sb = new StringBuilder(n);
			for (int i = 0; i < n; i++) {
				int index = (int) (AlphaNumericString.length() * Math.random());

				sb.append(AlphaNumericString.charAt(index));
			}
			return sb.toString();
		}
	}

	public void swipeToBottomOfPage() throws Exception {
		for (int i = 0; i < 5; i++) {
			scrollToBottomOfPage();
			waitTime(4000);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void androidSwitchTab() {
		ArrayList<String> window = new ArrayList(getDriver().getWindowHandles());
		getDriver().switchTo().window(window.get(window.size() - 1));
	}

	/**
	 * Function to switch to parent Window
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void AndroidSwitchToParentWindow() {
		try {
			ArrayList<String> window = new ArrayList(getDriver().getWindowHandles());
			getDriver().switchTo().window(window.get(0));
		} catch (Exception e) {
			System.out.println("\n No window is displayed!");
		}
	}

	public static String getTheOSVersion() {
		String version = null;
		try {
			String cmd1 = "adb shell getprop ro.build.version.release";
			Process p1 = Runtime.getRuntime().exec(cmd1);
			BufferedReader br = new BufferedReader(new InputStreamReader(p1.getInputStream()));
			// outputText1 ="";
			while ((version = br.readLine()) != null) {
				logger.info("Version :: " + version.toString());
				Thread.sleep(3000);
				break;
			}

		} catch (Exception e) {
			// logger.error(e);
		}
		return version;
	}

	public void TurnOFFWifi() throws IOException {
		String Deviceversion = getTheOSVersion();
		System.out.println("Turn off wifi");
		if (Deviceversion.contains("6")) {
			Runtime.getRuntime().exec("adb shell am broadcast -a io.appium.settings.wifi --es setstatus disable");
			logger.info("Turning off wifi");
			extent.extentLoggerPass("Turning off wifi", "Turning off wifi");
		} else {
			Runtime.getRuntime().exec("adb shell svc wifi disable");
			logger.info("Turning off wifi");
			extent.extentLoggerPass("Turning off wifi", "Turning off wifi");
		}
	}

	public void TurnONWifi() throws IOException {
		String Deviceversion = getTheOSVersion();
		System.out.println("Turn on wifi");
		if (Deviceversion.contains("6")) {
			Runtime.getRuntime().exec("adb shell am broadcast -a io.appium.settings.wifi --es setstatus enable");
			logger.info("Turning ON wifi");
			extent.extentLoggerPass("Turning ON wifi", "Turning ON wifi");
		} else {
			Runtime.getRuntime().exec("adb shell svc wifi enable");
			logger.info("Turning ON wifi");
			extent.extentLoggerPass("Turning ON wifi", "Turning ON wifi");
		}
	}

	@SuppressWarnings("rawtypes")
	public void carouselSwipe(String direction, int count) {
		touchAction = new TouchAction(getDriver());
		String dire = direction;
		try {
			if (dire.equalsIgnoreCase("LEFT")) {

				for (int i = 0; i < count; i++) {
					Dimension size = getDriver().manage().window().getSize();

					int startx = (int) (size.width * 0.9);
					int endx = (int) (size.width * 0.20);
					int starty = size.height / 2;
					touchAction.press(PointOption.point(startx, starty))
							.waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
							.moveTo(PointOption.point(endx, starty)).release().perform();
					logger.info("Swiping screen in " + " " + dire + " direction" + " " + (i + 1) + " times");
					extent.extentLogger("SwipeLeft",
							"Swiping screen in " + " " + dire + " direction" + " " + (i + 1) + " times");
				}
			} else if (dire.equalsIgnoreCase("RIGHT")) {

				for (int j = 0; j < count; j++) {
					Dimension size = getDriver().manage().window().getSize();
					int endx = (int) (size.width * 0.9);
					int startx = (int) (size.width * 0.20);
					if (size.height > 2000) {
						int starty = (int) (size.height / 2);
						touchAction.press(PointOption.point(startx, starty))
								.waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
								.moveTo(PointOption.point(endx, starty)).release().perform();
					} else {
						int starty = (int) (size.height / 1.5);
						touchAction.press(PointOption.point(startx, starty))
								.waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
								.moveTo(PointOption.point(endx, starty)).release().perform();
					}

					logger.info("Swiping screen in " + " " + dire + " direction" + " " + (j + 1) + " times");
					extent.extentLogger("SwipeRight",
							"Swiping screen in " + " " + dire + " direction" + " " + (j + 1) + " times");
				}
			}
		} catch (Exception e) {
			logger.error(e);

		}
	}

	public void ScrollToTheElementWEB(By element) throws Exception {
		js.executeScript("arguments[0].scrollIntoView(true);", findElement(element));
		js.executeScript("window.scrollBy(0,-250)", "");
	}

	/**
	 * Function to Initialize mandatoryRegistrationPopUp count to one
	 * 
	 * @param userType
	 */
	public void mandatoryRegistrationPopUp(String userType) {
		if (userType.contains("Guest")) {
			js.executeScript("window.localStorage.setItem('mandatoryRegistrationVideoCount','1')");
		}
	}

	public boolean checkElementDisplayed(By byLocator, String str) throws Exception {

		try {
			WebElement element = findElement(byLocator);
			if (element.isDisplayed()) {
				extent.extentLogger("checkElementPresent", "" + str + " is displayed");
				logger.info("" + str + " is displayed");
				return true;
			}
		} catch (Exception e) {
			extent.extentLogger("checkElementPresent", "" + str + " is not displayed");
			logger.info("" + str + " is not displayed");
			return false;
		}
		return false;
	}

	public String getParameterFromXML(String param) {
		return Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter(param);
	}

	@SuppressWarnings("rawtypes")
	public void SwipeInLandscapeMode(String direction, int count) {
		touchAction = new TouchAction(getDriver());
		String dire = direction;
		try {
			if (dire.equalsIgnoreCase("DOWN") | dire.equalsIgnoreCase("LEFT")) {

				for (int i = 0; i < count; i++) {
					Dimension size = getDriver().manage().window().getSize();
					int xCor = (int) (size.height / 2);
					int startY = (int) (size.width * 0.20);
					int endY = (int) (size.width * 0.85);
					System.out.println(startY + " X " + endY);
					touchAction.press(PointOption.point(xCor, startY))
							.waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
							.moveTo(PointOption.point(xCor, endY)).release().perform();
					logger.info("Swiping screen in " + dire + " direction" + (i + 1) + " times");
					extent.extentLogger("SwipeLeft", "Swiping screen in " + dire + " direction" + (i + 1) + " times");
				}
			} else if (dire.equalsIgnoreCase("UP") | dire.equalsIgnoreCase("RIGHT")) {

				for (int j = 0; j < count; j++) {
					Dimension size = getDriver().manage().window().getSize();
					int xCor = (int) (size.height / 2);
					int startY = (int) (size.width * 0.85);
					int endY = (int) (size.width * 0.20);
					System.out.println(startY + " X " + endY);
					System.out.println(xCor);
					touchAction.press(PointOption.point(xCor, startY))
							.waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
							.moveTo(PointOption.point(xCor, endY)).release().perform();

					logger.info("Swiping screen in " + dire + " direction " + (j + 1) + " times");
					extent.extentLogger("SwipeRight", "Swiping screen in " + dire + " direction " + (j + 1) + " times");
				}
			}

		} catch (Exception e) {
			logger.error(e);

		}
	}

	public void clearBackgroundApps() throws IOException {
		String adbRecentApp = "adb shell input keyevent KEYCODE_APP_SWITCH";
		String adbSelectApp = "adb shell input keyevent KEYCODE_DPAD_DOWN";
		String adbClearApp = "adb shell input keyevent KEYCODE_DEL";
		String adbHomeScreen = "adb shell input keyevent KEYCODE_HOME";

		Runtime.getRuntime().exec(adbRecentApp);

		for (int iterator = 1; iterator <= 7; iterator++) {
			waitTime(1000);
			Runtime.getRuntime().exec(adbClearApp);
			Runtime.getRuntime().exec(adbSelectApp);
		}

		waitTime(1000);
		Runtime.getRuntime().exec(adbHomeScreen);
		System.out.println("Cleared all background Apps");
	}

	public boolean findElementInRefreshingConvivaPage(WebDriver webdriver, By locator, String displayText)
			throws Exception {
		for (int i = 1; i <= 500; i++) {
			webdriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
			try {
				webdriver.findElement(locator);
				logger.info(displayText + " is displayed");
				extent.extentLogger("", displayText + " is displayed");
				return true;
			} catch (Exception e) {
				try {
					js.executeScript("window.scrollBy(0,100)", "");
					waitTime(2000);
					System.out.println("Waiting ..");
				} catch (Exception e1) {
				}
			}
		}
		return false;
	}

//	====================================================TV=================================================
	public boolean verifyElementExistTv(By byLocator, String str) throws Exception {

		try {

			if (getDriver().findElement(byLocator).isDisplayed()) {
				extent.extentLoggerPass("checkElementPresent", str + " is displayed");
				logger.info("" + str + " is displayed");
				return true;
			}
		} catch (Exception e) {
			extent.extentLogger("checkElementPresent", str + " is not displayed");
			logger.info(str + " is not displayed");
			return false;
		}
		return false;
	}

	public void Aclick(By byLocator, String validationtext) throws Exception {

		try {
			getDriver().findElement(byLocator).click();
			logger.info("Clicked on " + validationtext);
			extent.extentLogger("click", "Clicked on " + validationtext);
		} catch (Exception e) {
			logger.error(e);
		}
	}

//	public void  TVTabSelect(String str) throws Exception
//	{
//		
//		TVclick(Zee5TvHomePage.objSelectTab(str), str);
//		Thread.sleep(2000);
//		try{
//			
//			if(TVgetAttributValue("focused", Zee5TvHomePage.objSelectTab(str)).equals("false"))
//			{
//				TVclick(Zee5TvHomePage.objSelectTab(str), str);
//			}
//			else{
//				logger.info("Highlighted Tab:"+TVgetText(Zee5TvHomePage.objHighlightedTab));
//				extent.extentLoggerPass("Tab", "Highlighted Tab:"+TVgetText(Zee5TvHomePage.objHighlightedTab));
//			}
//		}
//		catch(Exception e)
//		{		
//			System.out.println(e);
//		}
//	}
	
	public String calendarDateCashBack(int days) throws Exception{
		LocalDateTime ldt = LocalDateTime.now().plusDays(days);
		DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH);

		String formatter = formmat1.format(ldt);
		//System.out.println(formatter);
		return formatter;
	}

	public String TVgetText(By byLocator) throws Exception {
		String Value = null;
		Value = getDriver().findElement(byLocator).getText();
		return Value;
	}

//	public void type(String array[]) throws Exception {
//		String searchdata[] = array;
//		int searchdatalength = searchdata.length;
//		StringBuilder searchData = new StringBuilder();
//		for (int j = 0; j < searchdatalength; j++) {
//			getDriver().findElement(Zee5TvSearchPage.objSearchKeyboardBtn(searchdata[j])).click();
//			waitTime(2000);
//			getDriver().findElement(Zee5TvSearchPage.objSearchKeyboardBtn(searchdata[j])).click();
//			searchData.append(searchdata[j]);
//		}
//		waitTime(2000);
//		
//		logger.info("Typing the content : " + searchData);
//		extentLogger("search", "Typing the content : "  +  searchData);
//	}
	public void TVRemoteEvent(int value) throws Exception {

		String cmd = "adb shell input keyevent " + value + "";
		Runtime.getRuntime().exec(cmd);

	}

	public boolean TVVerifyElementNotPresent(By byLocator, int waitTime) {
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), waitTime);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(byLocator));
			return false;
		} catch (Exception e) {
			return true;
		}
	}

	public String TVgetAttributValue(String property, By byLocator) throws Exception {
		String Value = null;
		Value = getDriver().findElement(byLocator).getAttribute(property);
		return Value;
	}

	public void BrowsertearDown() {
		getWebDriver().quit();
	}

	public void decode() {
		CTUserName = new String(Base64.getDecoder().decode(getParameterFromXML("CTUser")));
		CTPWD = new String(Base64.getDecoder().decode(getParameterFromXML("CTPwd")));
	}

	public boolean TVVerifyElementPresent(By byLocator, int waitTime, String string) {
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), waitTime);
			wait.until(ExpectedConditions.visibilityOfElementLocated(byLocator));
			logger.info("Element " + string + " visible");
			extent.extentLoggerPass("", "Element " + string + " visible");
			return true;
		} catch (Exception e) {
			logger.error("Element " + string + " not visible");
			return false;
		}
	}

	public boolean waitForElementPresence(By locator, int seconds, String message) throws Exception {
		try {
			WebDriverWait w = new WebDriverWait(getWebDriver(), seconds);
			w.until(ExpectedConditions.visibilityOfElementLocated(locator));
			logger.info(message + " is displayed");
			extent.extentLogger("element is displayed", message + " is displayed");
			return true;
		} catch (Exception e) {
			return false;
		}

	}

//	=========Handle OTP==================
	public static void handleOtp(String otp) throws IOException, InterruptedException {
		for (int i = 0; i < otp.length(); i++) {
			char ch = otp.charAt(i);
			Thread.sleep(2000);
			String cmd = "adb shell input text " + ch + "";
			Runtime.getRuntime().exec(cmd);
		}
		logger.info("Entered OTP " + otp + " Successfully");
		extent.extentLogger("Enter OTP", "Entered OTP " + otp + " Successfully");
	}

//	------------------swipe up----------------
	public void swipeUp() {
		JavascriptExecutor js = (JavascriptExecutor) getDriver();
		HashMap<String, String> scrollObject = new HashMap<>();
		scrollObject.put("direction", "up");
		js.executeScript("mobile:swipe", scrollObject);
	}

	public void swipedown() {
		JavascriptExecutor js = (JavascriptExecutor) getDriver();
		HashMap<String, String> scrollObject = new HashMap<>();
		scrollObject.put("direction", "down");
		js.executeScript("mobile:swipe", scrollObject);
	}

	public static void typeNumber(String number) throws IOException {
		String cmd = "adb shell input text " + number + "";
		Runtime.getRuntime().exec(cmd);
		logger.info("Entered Number " + number + " Successfully");
		extent.extentLogger("Enter Number", "Entered Number " + number + " Successfully");
	}

	public static void androidTextFieldClear(String validationtext) throws IOException {
		try {
			String cmd = "adb shell input keyevent --longpress 67 67 67 67 67 67 67";
			Thread.sleep(1000);
			Runtime.getRuntime().exec(cmd);
			logger.info("Cleared the text in : " + validationtext);
			extent.extentLogger("clear text", "Cleared the text in : " + validationtext);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public static ValidatableResponse RingPayAPI(Object[][] data, String url) {
		Random rand = new Random();

		HashMap<String, String> req_body = new HashMap<>();
//		System.out.println((String) data[0][3]);
		req_body.put("source_app", (String) data[0][0]);
		req_body.put("mobile_number", (String) data[0][1]);
		try {
			req_body.put("otp", (String) data[0][2]);
			req_body.put("client_id", (String) data[0][3]);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		JSONObject Myrequestbody = new JSONObject();

		Myrequestbody.put("source_app", req_body.get("source_app"));
		Myrequestbody.put("mobile_number", req_body.get("mobile_number"));
		Myrequestbody.put("otp", req_body.get("otp"));
		Myrequestbody.put("client_id", req_body.get("client_id"));

		HashMap<String, Object> headers = new HashMap<>();
		headers.put("x-request-id", rand.nextInt(1001));
		headers.put("X-Client-App", "android");
		headers.put("X-Client-Version", 4.9);
		headers.put("X-Client-OS-Type", "android");
		headers.put("X-Client-OS-Version", 10);
		headers.put("x-login-token",
				"eyJhbGciOiJSUzI1NiIsIng1YyI6WyJNSUlGWVRDQ0JFbWdBd0lCQWdJUkFQaEtkUXdrSUFNRENRQUFBQUM4QzZvd0RRWUpLb1pJaHZjTkFRRUxCUUF3UmpFTE1Ba0dBMVVFQmhNQ1ZWTXhJakFnQmdOVkJBb1RHVWR2YjJkc1pTQlVjblZ6ZENCVFpYSjJhV05sY3lCTVRFTXhFekFSQmdOVkJBTVRDa2RVVXlCRFFTQXhSRFF3SGhjTk1qRXhNREUzTVRjd05qQTNXaGNOTWpJd01URTFNVGN3TmpBMldqQWRNUnN3R1FZRFZRUURFeEpoZEhSbGMzUXVZVzVrY205cFpDNWpiMjB3Z2dFaU1BMEdDU3FHU0liM0RRRUJBUVVBQTRJQkR3QXdnZ0VLQW9JQkFRQ3ZnU2VHM3JTVlcwSVBpWkJGVmJoMktjYjNoTnl3R2VJOUZmaVgyUXZRQnBmUkIvT0xiUUFwZGdDWTZJL1dqNEw0aHVNQzRMVHA3OFZXbmhtZGJ3Y1NxbXJzNkpDM3kwWnVmVm4ydzhsV0NYODNsYytFUmdRVHhmaGUwTVNIakhlWk9mWGROQ3dqejZrTXJkZEVPUlJ5T3V3SWdjcXcrNGoycS9mSktHbkUyNXQ5NndOTDgrUDg1V294ZXhaZEROR1pzMmkzNmRvZkdVTGR1YTZaWFI1YjFlODJkd0dra0Rkd3RFMjZCeDRhTTl4VDEwK3A0S3FKNXZ0MWpvY1N0K2tTWHFRaEowQlJjS082OWhGUTRDSUdKYk5EYlRIMENGYlMvanJsNThGWnhVTUVwaUNHbG9JdmJyZ20xSlFzRDE2UmtIZlQ0NVM5UERNc3k5WFI4bjVqQWdNQkFBR2pnZ0p4TUlJQ2JUQU9CZ05WSFE4QkFmOEVCQU1DQmFBd0V3WURWUjBsQkF3d0NnWUlLd1lCQlFVSEF3RXdEQVlEVlIwVEFRSC9CQUl3QURBZEJnTlZIUTRFRmdRVUJ0M1lUWkFYZ3pGYXdpV2FXN3hmaStYRDhnZ3dId1lEVlIwakJCZ3dGb0FVSmVJWURySlhrWlFxNWRSZGhwQ0QzbE96dUpJd2JRWUlLd1lCQlFVSEFRRUVZVEJmTUNvR0NDc0dBUVVGQnpBQmhoNW9kSFJ3T2k4dmIyTnpjQzV3YTJrdVoyOXZaeTluZEhNeFpEUnBiblF3TVFZSUt3WUJCUVVITUFLR0pXaDBkSEE2THk5d2Eya3VaMjl2Wnk5eVpYQnZMMk5sY25SekwyZDBjekZrTkM1a1pYSXdIUVlEVlIwUkJCWXdGSUlTWVhSMFpYTjBMbUZ1WkhKdmFXUXVZMjl0TUNFR0ExVWRJQVFhTUJnd0NBWUdaNEVNQVFJQk1Bd0dDaXNHQVFRQjFua0NCUU13UHdZRFZSMGZCRGd3TmpBMG9ES2dNSVl1YUhSMGNEb3ZMMk55YkhNdWNHdHBMbWR2YjJjdlozUnpNV1EwYVc1MEwxZ3lTakpJY2w4M1VHbE5MbU55YkRDQ0FRUUdDaXNHQVFRQjFua0NCQUlFZ2ZVRWdmSUE4QUIxQUZHanNQWDlBWG1jVm0yNE4zaVBES1I2ekJzbnkvZWVpRUthRGY3VWl3WGxBQUFCZkk5dXVqSUFBQVFEQUVZd1JBSWdYd3JxbEEvV21IRFVySVpSWDIrS24raldjRVlsQjliVCtsRk9HT3RaTEtNQ0lGUzRXYU14Q09GaVAxTnhVN3hMcVBQVGlwR2dlaFgwS0IwTFgrTXhkdEl0QUhjQUtYbSs4SjQ1T1NId1ZuT2ZZNlYzNWI1WGZaeGdDdmo1VFYwbVhDVmR4NFFBQUFGOGoyNjZLUUFBQkFNQVNEQkdBaUVBNDdRNldJYmVnQUZuL0liUUM5OEFoR0dlY0xGVWowcjRCMnlrSkFlN2tzd0NJUURiQ2RNNFdzQ2JVUHJsSDhIV3M1ZGpqQWluKy9jWDZPNHpDTldMbzJxakhEQU5CZ2txaGtpRzl3MEJBUXNGQUFPQ0FRRUFMWHlhOUhVVm5rZURkUFgyd0tzQ2QybDhNcGpTeW5iVWVKWGI5Um04dXRsczRjRzkvdXEzRzZ3clRGWkNhdldJMnE5SmxlUnA1Q21DeCtrcElPVVh3T0dPQUZ3SVFrUFhCRnFrOGJscmE1MmhGTTluMUROYzY1bmNVRHkybXFYbjNXaVByN0crZEdSNlkzRnFKMjQ3K0VySlllbTZnM28rR3ZVcERxbWpkZ01SdHFFTXlmTVZIa0xoN3ZucWlXdnYzQ2VlU1ViRjkvMFdxUklNdTdPSFZyTkVET1ZUUEZuWENVczgyUk1OVVd0dVJTS1Njelh3QXFNN0JFWGR4TjNYcXE1Z1dOUDdUeFowczZzRTZGOHovWmN0OFVLdHRkNVBidGhrdGdFMmVvUmFaYTB1alNWVmtUeTVGb1pvMWJ1ZXhjbnM5WjlEWDFCUy9RU1JXbjNBUHc9PSIsIk1JSUZqRENDQTNTZ0F3SUJBZ0lOQWdDT3NnSXpObVdMWk0zYm16QU5CZ2txaGtpRzl3MEJBUXNGQURCSE1Rc3dDUVlEVlFRR0V3SlZVekVpTUNBR0ExVUVDaE1aUjI5dloyeGxJRlJ5ZFhOMElGTmxjblpwWTJWeklFeE1RekVVTUJJR0ExVUVBeE1MUjFSVElGSnZiM1FnVWpFd0hoY05NakF3T0RFek1EQXdNRFF5V2hjTk1qY3dPVE13TURBd01EUXlXakJHTVFzd0NRWURWUVFHRXdKVlV6RWlNQ0FHQTFVRUNoTVpSMjl2WjJ4bElGUnlkWE4wSUZObGNuWnBZMlZ6SUV4TVF6RVRNQkVHQTFVRUF4TUtSMVJUSUVOQklERkVORENDQVNJd0RRWUpLb1pJaHZjTkFRRUJCUUFEZ2dFUEFEQ0NBUW9DZ2dFQkFLdkFxcVBDRTI3bDB3OXpDOGRUUElFODliQSt4VG1EYUc3eTdWZlE0YyttT1dobFVlYlVRcEsweXYycjY3OFJKRXhLMEhXRGplcStuTElITjFFbTVqNnJBUlppeG15UlNqaElSMEtPUVBHQk1VbGRzYXp0SUlKN08wZy84MnFqL3ZHRGwvLzN0NHRUcXhpUmhMUW5UTFhKZGVCKzJEaGtkVTZJSWd4NndON0U1TmNVSDNSY3NlamNxajhwNVNqMTl2Qm02aTFGaHFMR3ltaE1Gcm9XVlVHTzN4dElIOTFkc2d5NGVGS2NmS1ZMV0szbzIxOTBRMExtL1NpS21MYlJKNUF1NHkxZXVGSm0ySk05ZUI4NEZrcWEzaXZyWFdVZVZ0eWUwQ1FkS3ZzWTJGa2F6dnh0eHZ1c0xKekxXWUhrNTV6Y1JBYWNEQTJTZUV0QmJRZkQxcXNDQXdFQUFhT0NBWFl3Z2dGeU1BNEdBMVVkRHdFQi93UUVBd0lCaGpBZEJnTlZIU1VFRmpBVUJnZ3JCZ0VGQlFjREFRWUlLd1lCQlFVSEF3SXdFZ1lEVlIwVEFRSC9CQWd3QmdFQi93SUJBREFkQmdOVkhRNEVGZ1FVSmVJWURySlhrWlFxNWRSZGhwQ0QzbE96dUpJd0h3WURWUjBqQkJnd0ZvQVU1SzhySm5FYUswZ25oUzlTWml6djhJa1RjVDR3YUFZSUt3WUJCUVVIQVFFRVhEQmFNQ1lHQ0NzR0FRVUZCekFCaGhwb2RIUndPaTh2YjJOemNDNXdhMmt1WjI5dlp5OW5kSE55TVRBd0JnZ3JCZ0VGQlFjd0FvWWthSFIwY0RvdkwzQnJhUzVuYjI5bkwzSmxjRzh2WTJWeWRITXZaM1J6Y2pFdVpHVnlNRFFHQTFVZEh3UXRNQ3N3S2FBbm9DV0dJMmgwZEhBNkx5OWpjbXd1Y0d0cExtZHZiMmN2WjNSemNqRXZaM1J6Y2pFdVkzSnNNRTBHQTFVZElBUkdNRVF3Q0FZR1o0RU1BUUlCTURnR0Npc0dBUVFCMW5rQ0JRTXdLakFvQmdnckJnRUZCUWNDQVJZY2FIUjBjSE02THk5d2Eya3VaMjl2Wnk5eVpYQnZjMmwwYjNKNUx6QU5CZ2txaGtpRzl3MEJBUXNGQUFPQ0FnRUFJVlRveTI0andYVXIwckFQYzkyNHZ1U1ZiS1F1WXczbkxmbExmTGg1QVlXRWVWbC9EdTE4UUFXVU1kY0o2by9xRlpiaFhrQkgwUE5jdzk3dGhhZjJCZW9EWVk5Q2svYitVR2x1aHgwNnpkNEVCZjdIOVA4NG5ucndwUis0R0JEWksrWGgzSTB0cUp5MnJnT3FORGZscjVJTVE4WlRXQTN5bHRha3pTQktaNlhwRjBQcHF5Q1J2cC9OQ0d2MktYMlR1UENKdnNjcDEvbTJwVlR0eUJqWVBSUStRdUNRR0FKS2p0TjdSNURGcmZUcU1XdllnVmxwQ0pCa3dsdTcrN0tZM2NUSWZ6RTdjbUFMc2tNS05MdUR6K1J6Q2NzWVRzVmFVN1ZwM3hMNjBPWWhxRmt1QU9PeERaNnBIT2o5K09KbVlnUG1PVDRYMys3TDUxZlhKeVJIOUtmTFJQNm5UMzFENW5tc0dBT2daMjYvOFQ5aHNCVzF1bzlqdTVmWkxaWFZWUzVIMEh5SUJNRUt5R01JUGhGV3JsdC9oRlMyOE4xemFLSTBaQkdEM2dZZ0RMYmlEVDlmR1hzdHBrK0ZtYzRvbFZsV1B6WGU4MXZkb0VuRmJyNU0yNzJIZGdKV28rV2hUOUJZTTBKaSt3ZFZtblJmZlhnbG9Fb2x1VE5jV3pjNDFkRnBnSnU4ZkYzTEcwZ2wyaWJTWWlDaTlhNmh2VTBUcHBqSnlJV1hoa0pUY01KbFByV3gxVnl0RVVHclgybDBKRHdSalcvNjU2cjBLVkIwMnhIUkt2bTJaS0kwM1RnbExJcG1WQ0sza0JLa0tOcEJOa0Z0OHJoYWZjQ0tPYjlKeC85dHBORmxRVGw3QjM5ckpsSldrUjE3UW5acVZwdEZlUEZPUm9abUZ6TT0iLCJNSUlGWWpDQ0JFcWdBd0lCQWdJUWQ3ME5iTnMyK1JycUlRL0U4RmpURFRBTkJna3Foa2lHOXcwQkFRc0ZBREJYTVFzd0NRWURWUVFHRXdKQ1JURVpNQmNHQTFVRUNoTVFSMnh2WW1Gc1UybG5iaUJ1ZGkxellURVFNQTRHQTFVRUN4TUhVbTl2ZENCRFFURWJNQmtHQTFVRUF4TVNSMnh2WW1Gc1UybG5iaUJTYjI5MElFTkJNQjRYRFRJd01EWXhPVEF3TURBME1sb1hEVEk0TURFeU9EQXdNREEwTWxvd1J6RUxNQWtHQTFVRUJoTUNWVk14SWpBZ0JnTlZCQW9UR1VkdmIyZHNaU0JVY25WemRDQlRaWEoyYVdObGN5Qk1URU14RkRBU0JnTlZCQU1UQzBkVVV5QlNiMjkwSUZJeE1JSUNJakFOQmdrcWhraUc5dzBCQVFFRkFBT0NBZzhBTUlJQ0NnS0NBZ0VBdGhFQ2l4N2pvWGViTzl5L2xENjNsYWRBUEtIOWd2bDlNZ2FDY2ZiMmpILzc2TnU4YWk2WGw2T01TL2tyOXJINXpvUWRzZm5GbDk3dnVmS2o2YndTaVY2bnFsS3IrQ01ueTZTeG5HUGIxNWwrOEFwZTYyaW05TVphUncxTkVEUGpUckVUbzhnWWJFdnMvQW1RMzUxa0tTVWpCNkcwMGowdVlPRFAwZ21IdTgxSThFM0N3bnFJaXJ1Nnoxa1oxcStQc0Fld25qSHhnc0hBM3k2bWJXd1pEclhZZmlZYVJRTTlzSG1rbENpdEQzOG01YWdJL3Bib1BHaVVVKzZET29nckZaWUpzdUI2akM1MTFwenJwMVprajVaUGFLNDlsOEtFajhDOFFNQUxYTDMyaDdNMWJLd1lVSCtFNEV6Tmt0TWc2VE84VXBtdk1yVXBzeVVxdEVqNWN1SEtaUGZtZ2hDTjZKM0Npb2o2T0dhSy9HUDVBZmw0L1h0Y2QvcDJoL3JzMzdFT2VaVlh0TDBtNzlZQjBlc1dDcnVPQzdYRnhZcFZxOU9zNnBGTEtjd1pwRElsVGlyeFpVVFFBczZxemttMDZwOThnN0JBZStkRHE2ZHNvNDk5aVlINlRLWC8xWTdEemt2Z3RkaXpqa1hQZHNEdFFDdjlVdyt3cDlVN0RiR0tvZ1BlTWEzTWQrcHZlejdXMzVFaUV1YSsrdGd5L0JCakZGRnkzbDNXRnBPOUtXZ3o3enBtN0FlS0p0OFQxMWRsZUNmZVhra1VBS0lBZjVxb0liYXBzWld3cGJrTkZoSGF4MnhJUEVEZ2ZnMWF6Vlk4MFpjRnVjdEw3VGxMbk1RLzBsVVRiaVN3MW5INjlNRzZ6TzBiOWY2QlFkZ0FtRDA2eUs1Nm1EY1lCWlVDQXdFQUFhT0NBVGd3Z2dFME1BNEdBMVVkRHdFQi93UUVBd0lCaGpBUEJnTlZIUk1CQWY4RUJUQURBUUgvTUIwR0ExVWREZ1FXQkJUa3J5c21jUm9yU0NlRkwxSm1MTy93aVJOeFBqQWZCZ05WSFNNRUdEQVdnQlJnZTJZYVJRMlh5b2xRTDMwRXpUU28vL3o5U3pCZ0JnZ3JCZ0VGQlFjQkFRUlVNRkl3SlFZSUt3WUJCUVVITUFHR0dXaDBkSEE2THk5dlkzTndMbkJyYVM1bmIyOW5MMmR6Y2pFd0tRWUlLd1lCQlFVSE1BS0dIV2gwZEhBNkx5OXdhMmt1WjI5dlp5OW5jM0l4TDJkemNqRXVZM0owTURJR0ExVWRId1FyTUNrd0o2QWxvQ09HSVdoMGRIQTZMeTlqY213dWNHdHBMbWR2YjJjdlozTnlNUzluYzNJeExtTnliREE3QmdOVkhTQUVOREF5TUFnR0JtZUJEQUVDQVRBSUJnWm5nUXdCQWdJd0RRWUxLd1lCQkFIV2VRSUZBd0l3RFFZTEt3WUJCQUhXZVFJRkF3TXdEUVlKS29aSWh2Y05BUUVMQlFBRGdnRUJBRFNrSHJFb285QzBkaGVtTVhvaDZkRlNQc2piZEJaQmlMZzlOUjN0NVArVDRWeGZxN3ZxZk0vYjVBM1JpMWZ5Sm05YnZoZEdhSlEzYjJ0NnlNQVlOL29sVWF6c2FMK3l5RW45V3ByS0FTT3NoSUFyQW95WmwrdEphb3gxMThmZXNzbVhuMWhJVnc0MW9lUWExdjF2ZzRGdjc0elBsNi9BaFNydzlVNXBDWkV0NFdpNHdTdHo2ZFRaL0NMQU54OExaaDFKN1FKVmoyZmhNdGZUSnI5dzR6MzBaMjA5Zk9VMGlPTXkrcWR1Qm1wdnZZdVI3aFpMNkR1cHN6Zm53MFNrZnRoczE4ZEc5WktiNTlVaHZtYVNHWlJWYk5RcHNnM0JabHZpZDBsSUtPMmQxeG96Y2xPemdqWFBZb3ZKSkl1bHR6a011MzRxUWI5U3oveWlscmJDZ2o4PSJdfQ.eyJub25jZSI6IlBvSEJNR1FXVTZMTHZuQ21tQUlqUkt4dTJ4ND0iLCJ0aW1lc3RhbXBNcyI6MTYzNzc1MTY1NTE2OSwiYXBrUGFja2FnZU5hbWUiOiJjb20uZmFzdGJhbmtpbmcuZGVidWciLCJhcGtEaWdlc3RTaGEyNTYiOiJsRHF1bDJxejdyd2owRDFJSzBkcTZwTnNaUmR0QW9BbUNNOVh5MGg2bkNjPSIsImN0c1Byb2ZpbGVNYXRjaCI6dHJ1ZSwiYXBrQ2VydGlmaWNhdGVEaWdlc3RTaGEyNTYiOlsiR3k3N1doNFRkR0ZXd3NoaS9VVXdDdUJIL0NBZ2V4VFFLdmJzbW5pWHFpTT0iXSwiYmFzaWNJbnRlZ3JpdHkiOnRydWUsImV2YWx1YXRpb25UeXBlIjoiQkFTSUMsSEFSRFdBUkVfQkFDS0VEIn0.ShOvWqQ_5i-T1ixx59sbk0-6LMo8oKiC5PfZCt9dVJrnfeap8JMQ9x8v19-Yh-M07y54BjQPXFGU-Y602uFc_V7TKHonDqjaEOsx6VfRwiQeZmtaO-Hhmlr2g-xRHFoDOnXy2wHYGfDkMbir50EraIyny3xfs-guIDMwg5qAzQaN999KRsrbHXX-a6wwoQ0qyUSVKGN57T_qOcXaq9X5bI1B3nD1m5Inu7TW0xrCb0sfUn8GDimAtnXELKf048S4iaXBObbgtiNyVQtTEfqHA8WdfhANIZWcV4XQDHbv69wcvrmUTDeZJienIfkmesfYnFDngW2NfR9A9m_Q5sorig");
		headers.put("x-login-nonce", "B6B667EB514890789F56F9B78BFA509AB41B673B");
		headers.put("x-login-timestamp", "1636960116339");

		ValidatableResponse response = RestAssured.given().baseUri(url).contentType(ContentType.JSON).headers(headers)
				.body(Myrequestbody.toJSONString()).when().post().then();

		System.out.println("Request Url -->" + url);
		System.out.println("Request :" + Myrequestbody);

		String Resp = response.extract().body().asString();
		System.out.println("Response Body= " + Resp);

		return response;
	}
	
	public static ValidatableResponse txnApi(String userRef,String mobNo, String txnAmt) throws Exception{
		Random rand = new Random();

		HashMap<String, String> req_body = new HashMap<>();
//	    System.out.println((String) data[0][3]);
		req_body.put("encrypted_name", prop.getproperty("encryptedName"));
		req_body.put("merchant_id", prop.getproperty("txn_vpa"));
		req_body.put("actual_amount",txnAmt);
		req_body.put("user_reference_number", userRef);
		req_body.put("first_name", "Sunil");
		req_body.put("last_name", "Chatla");
		req_body.put("email", "ajsjaskjas@gmail.com");
		req_body.put("mobile_number", mobNo);
		req_body.put("advertising_id", "132132");
		
		JSONObject Myrequestbody = new JSONObject();

		Myrequestbody.put("encrypted_name", req_body.get("encrypted_name"));
		Myrequestbody.put("merchant_id", req_body.get("merchant_id"));
		Myrequestbody.put("actual_amount", req_body.get("actual_amount"));
		Myrequestbody.put("user_reference_number", req_body.get("user_reference_number"));
		Myrequestbody.put("first_name", req_body.get("first_name"));
		Myrequestbody.put("last_name", req_body.get("last_name"));
		Myrequestbody.put("email", req_body.get("email"));
		Myrequestbody.put("mobile_number", req_body.get("mobile_number"));
		Myrequestbody.put("advertising_id", req_body.get("advertising_id"));
		HashMap<String, Object> headers = new HashMap<>();
		headers.put("client-id", "zx2789");

		ValidatableResponse response = RestAssured.given().baseUri(prop.getproperty("txnAPI_url")).contentType(ContentType.JSON).headers(headers)
				.body(Myrequestbody.toJSONString()).when().post().then();

		System.out.println("Request Url -->" + prop.getproperty("txnAPI_url"));
		// ExtentReporter.extentLogger("", "Request Url -->" + url);
		System.out.println("Request :" + Myrequestbody);
		// ExtentReporter.extentLogger("", "Request :"+Myrequestbody);
		String Resp = response.extract().body().asString();
		System.out.println("Response Body= " + Resp);
		// ExtentReporter.extentLogger("", "Response Body= "+Resp);

		return response;
	}

	public static Object[][] getTestData(String filePath, String sheetName, String testcaseName) throws IOException {
		// fileInputStream argument
		FileInputStream fis = new FileInputStream(new File(filePath));
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet sheet = wb.getSheet(sheetName);

		ArrayList<ArrayList<String>> tcResultArray = new ArrayList<ArrayList<String>>();
		Iterator<Row> it = sheet.rowIterator();
		// Skip first row
		if (it.hasNext()) {
			it.next();
		}
		// if more than one row exist than iterate..
		while (it.hasNext()) {
			Row row = it.next();
			Iterator<Cell> tsCell = row.cellIterator();
			if (testcaseName.equalsIgnoreCase(tsCell.next().getStringCellValue())) {
				ArrayList<String> tc = new ArrayList<String>();
				while (tsCell.hasNext()) {
					Cell c = tsCell.next();
					if (c.getCellType() == CellType.STRING) {
						tc.add(c.getStringCellValue());
					} else if (c.getCellType() == CellType.BLANK) {
						tc.add("");
					} else {

						tc.add(NumberToTextConverter.toText(c.getNumericCellValue()));

					}
				}
				tcResultArray.add(tc);
			}
		}

		// convert from array list to array of object.
		Object[][] arrayObj = new String[tcResultArray.size()][];
		for (int i = 0; i < tcResultArray.size(); i++) {
			ArrayList<String> row = tcResultArray.get(i);
			arrayObj[i] = row.toArray(new String[row.size()]);
		}

		fis.close();
		return arrayObj;
	}

	/**
	 * gps
	 */
	public void setLocationConnectionToONOFF(String Value) {
		try {
			if (Value.equalsIgnoreCase("On")) {
				System.out.println("Switching On GPS");
				String cmd = "adb shell settings put secure location_mode 3";
				Runtime.getRuntime().exec(cmd);
				waitTime(5000);
				logger.info("GPS toggle is Switched On");
				extent.extentLoggerPass("GPS Toggle", "GPS toggle is Switched On");
			} else if (Value.equalsIgnoreCase("Off")) {
				System.out.println("Switching Off GPS");
				String cmd = "adb shell settings put secure location_mode 0";
				Runtime.getRuntime().exec(cmd);
				waitTime(3000);
				logger.info("GPS toggle is Switched Off");
				extent.extentLoggerPass("GPS Toggle", "GPS toggle is Switched Off");
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public static ValidatableResponse MockuserAPI(String url, String gender, String encrypted_name) {
		Random rand = new Random();

		HashMap<String, String> req_body = new HashMap<>();

		req_body.put("gender", gender);
		req_body.put("encrypted_name", encrypted_name);

		JSONObject Myrequestbody = new JSONObject();

		Myrequestbody.put("gender", req_body.get("gender"));
		Myrequestbody.put("encrypted_name", req_body.get("encrypted_name"));

		HashMap<String, Object> headers = new HashMap<>();
		headers.put("client-id", "zx2789");

		ValidatableResponse response = RestAssured.given().baseUri(url).contentType(ContentType.JSON).headers(headers)
				.body(Myrequestbody.toJSONString()).when().post().then();

		System.out.println("Request Url -->" + url);

		System.out.println("Request :" + Myrequestbody);

		String Resp = response.extract().body().asString();
		System.out.println("Response Body= " + Resp);

		return response;
	}

	public static ValidatableResponse skip_kyc(String url, String userRef, String PanNumber, String firstName,
			String middleName, String lastName, String encrypted_name) {
		Random rand = new Random();

		HashMap<String, String> req_body = new HashMap<>();
		req_body.put("user_reference_number", userRef);
		req_body.put("pan_number", PanNumber);
		req_body.put("first_name", firstName);
		req_body.put("middle_name", middleName);
		req_body.put("last_name", lastName);
		req_body.put("encrypted_name", encrypted_name);

		JSONObject Myrequestbody = new JSONObject();

		Myrequestbody.put("user_reference_number", req_body.get("user_reference_number"));
		Myrequestbody.put("pan_number", req_body.get("pan_number"));
		Myrequestbody.put("first_name", req_body.get("first_name"));
		Myrequestbody.put("middle_name", req_body.get("middle_name"));
		Myrequestbody.put("last_name", req_body.get("last_name"));
		Myrequestbody.put("encrypted_name", req_body.get("encrypted_name"));

		HashMap<String, Object> headers = new HashMap<>();
		headers.put("client-id", "zx2789");

		ValidatableResponse response = RestAssured.given().baseUri(url).contentType(ContentType.JSON).headers(headers)
				.body(Myrequestbody.toJSONString()).when().post().then();

		System.out.println("Request Url -->" + url);
		System.out.println("Request :" + Myrequestbody);
		String Resp = response.extract().body().asString();
		System.out.println("Response Body= " + Resp);

		return response;
	}

	// execute query from DB
	public String executeQuery(String query, String columName) throws SQLException, ClassNotFoundException {
		// Setting the driver
		Class.forName("com.mysql.cj.jdbc.Driver");
		ResultSet rs = null;
		String columnData = "";
		try {
			// Open a connection to the database

			Connection con = DriverManager.getConnection(prop.getproperty("dbHostUrl"), prop.getproperty("dbUserName"),
					prop.getproperty("dbPassword"));
			Statement st = con.createStatement();

			rs = st.executeQuery(query);

			while (rs.next()) {
				System.out.println(rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4) + " "
						+ rs.getString(5) + " " + rs.getString(6) + " " + rs.getString(7) + " " + rs.getString(8));

				columnData = rs.getString(columName);

			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return columnData;
	}

	public void executeUpdate(String updateExecutionQuery, String dbTable) throws ClassNotFoundException, SQLException {

		// Setting the driver
		Class.forName("com.mysql.cj.jdbc.Driver");

		// Open a connection to the database
		Connection con = DriverManager.getConnection(prop.getproperty("dbHostUrl"), prop.getproperty("dbUserName"),
				prop.getproperty("dbPassword"));
		Statement st = con.createStatement();

		// Executing the update query
		st.executeUpdate(updateExecutionQuery);

		ResultSet rs = st.executeQuery(dbTable);
		System.out.println(
				"=================================================================================================");
		while (rs.next()) {
			System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4)
					+ " " + rs.getString(5) + " " + rs.getString(6) + " " + rs.getString(7) + " " + rs.getString(8));
		}
		con.close();
	}

	public void executeInsert(String refNo, String eligibilityType, String approved_amount, String offer_id,
			String repeat_offer_id) throws ClassNotFoundException, SQLException {

		// Setting the driver
		Class.forName("com.mysql.cj.jdbc.Driver");

		// Open a connection to the database
		Connection con = DriverManager.getConnection(prop.getproperty("dbHostUrl"), prop.getproperty("dbUserName"),
				prop.getproperty("dbPassword"));
		Statement st = con.createStatement();

		PreparedStatement ps = con.prepareStatement(
				"Insert into db_txn_service.instaloan_whitelisted_users(user_reference_number,eligible_type,approved_amount,offer_id,repeat_offer_id)VALUES('"
						+ refNo + "','" + eligibilityType + "','" + approved_amount + "','" + offer_id + "','"
						+ repeat_offer_id + "')");
		ps.executeUpdate();
		con.close();
	}
	
	public void executeInsertCashback(String refNo, String cashBackPer, String billingMaxCap, String isActive,String uploadedAt,String expiryDate) throws Exception {

		// Setting the driver
		Class.forName("com.mysql.cj.jdbc.Driver");

		// Open a connection to the database
		Connection con = DriverManager.getConnection(prop.getproperty("dbHostUrl"), prop.getproperty("dbUserName"),
				prop.getproperty("dbPassword"));
		Statement st = con.createStatement();

		PreparedStatement ps = con.prepareStatement(
				"Insert into db_engagement_service.cashback_whitelisted_users(user_reference_number,cashback_percentage,billing_max_cap,is_active,uploaded_at,campaign_expiry_date)VALUES('"
						+ refNo + "','" + cashBackPer + "','" + billingMaxCap + "','" + isActive + "','"
						+ uploadedAt + "','" + expiryDate + "')");
		ps.executeUpdate();
		con.close();
	}

	/**
	 * Scroll down using Robot class
	 * 
	 * @return
	 * @throws AWTException
	 * @throws IOException
	 */
	public void robotDownClass() throws AWTException {
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_PAGE_DOWN);
		robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
	}

	/**
	 * Scroll up using Robot class
	 * 
	 * @return
	 * @throws AWTException
	 * @throws IOException
	 */
	public void robotUpClass() throws AWTException {
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_PAGE_UP);
		robot.keyRelease(KeyEvent.VK_PAGE_UP);
	}

	public boolean swipeElementAndroid(By particularBanner, By tillElement) throws Exception {
		boolean elementFound = false;
		// Get location of element you want to swipe
		WebElement banner = findElement_specific(particularBanner);
		Point bannerPoint = banner.getLocation();
		// Get size of device screen
		Dimension screenSize = getDriver().manage().window().getSize();
		// Get start and end coordinates for horizontal swipe
		int startX = Math.toIntExact(Math.round(screenSize.getWidth() * 0.70));
		int startY = Math.toIntExact(Math.round(screenSize.getHeight() * 0.25));
		int endX = Math.toIntExact(Math.round(screenSize.getWidth() * 0.10));
		;
		int endY = 0;

		TouchAction action = new TouchAction(getDriver());
		for (int i = 0; i <= 4; i++) {

			try {
				WebElement element = findElement_specific(tillElement);

				elementFound = element.isDisplayed();
				waitTime(2000);
				// elementFound=verifyElementDisplayed(tillElement);

			} catch (Exception e) {
				// e.printStackTrace();
				System.out.println("Swap-------Element Searching");
				if (elementFound == false) {

					action.press(PointOption.point(startX, startY))
							.waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
							.moveTo(PointOption.point(endX, startY)).release();
					getDriver().performTouchAction(action);
				}
				elementFound = false;
			}
			if (elementFound == true)
				break;
		}
		return elementFound;
	}

	public void doubleTap(By loacator, int count, String fieldName) {
		try {
			TouchAction t = new TouchAction(getDriver());
			WebElement element = findElement(loacator);
			t.tap(TapOptions.tapOptions().withElement(ElementOption.element(element)).withTapsCount(count)).perform();
			// t.tap(TapOptions.tapOptions().withTapsCount(count)).perform();
			logger.info("Tapped on the element " + count + " times on " + fieldName);
			extent.extentLogger("Tap", "Tapped on the element " + count + " times on " + fieldName);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public boolean checkElementExist1(By byLocator, String message) throws Exception {
		try {
			List<WebElement> element = findElements(byLocator);
			if (element.size() != 0) {
				logger.info(message + " is displayed");
				extent.extentLoggerPass("Message", message + " is displayed");
				return true;
			}
		} catch (Exception e) {
			logger.info(message + " is not displayed");
			extent.extentLoggerFail("Message", message + " is not displayed");
			return false;
		}
		return false;
	}

	public boolean iskeyboardShown(String typeofkeyboard) {
		try {
			getDriver().getKeyboard();
			logger.info(typeofkeyboard + " Keyboard is displayed");
			extent.extentLogger("Keyboard ", typeofkeyboard + " Keyboard is displayed");
			return true;
		} catch (Exception e) {
			logger.info(typeofkeyboard + " Keyboard is not displayed");
			extent.extentLogger("Keyboard", typeofkeyboard + "Keyboard is not displayed");
			return false;
		}
	}

	public String executeQueryWithReferanceNumber(String ref) throws SQLException, ClassNotFoundException {
		// Setting the driver
		Class.forName("com.mysql.cj.jdbc.Driver");
		ResultSet rs = null;
		String reff = "";
		try {
			// Open a connection to the database

			Connection con = DriverManager.getConnection(prop.getproperty("dbHostUrl"), prop.getproperty("dbUserName"),
					prop.getproperty("dbPassword"));
			Statement st = con.createStatement();
			PreparedStatement ps = con.prepareStatement(
					"select * from instaloan_whitelisted_users where user_reference_number=" + "'" + ref + "';");
			rs = ps.executeQuery();
			while (rs.next()) {
//				System.out.println(rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4)
//						+ " " + rs.getString(5) + " " + rs.getString(6) + " " + rs.getString(7) + " "
//						+ rs.getString(8));
				reff = rs.getString(2);
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reff;
	}

	public void updateEmail(String email, String ref) throws ClassNotFoundException, SQLException {

		// Setting the driver
		Class.forName("com.mysql.cj.jdbc.Driver");

		// Open a connection to the database
		Connection con = DriverManager.getConnection(prop.getproperty("dbHostUrl"), prop.getproperty("dbUserName"),
				prop.getproperty("dbPassword"));
		Statement st = con.createStatement();

		// Executing the update query
		st.executeUpdate(
				"update users set gcm_id='NULL',email='" + email + "' where user_reference_number='" + ref + "';");
//
//		ResultSet rs = st.executeQuery("select * from users;");
//		System.out.println("=================================================================================================");
//		while (rs.next()) {
//			System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4)
//			+ " " + rs.getString(5) + " " + rs.getString(6) + " " + rs.getString(7) + " "
//			+ rs.getString(8));
//		}
		con.close();
	}

	public String updateBankAccStatus(String Status, String OldbankAccNumber, String ref)
			throws ClassNotFoundException, SQLException {

		String newBankAcc = "";
		String instaloneStatus = Status.toUpperCase();
		switch (instaloneStatus) {
		case "ONHOLD":

			String onHold = OldbankAccNumber.substring(1);
			newBankAcc = "9" + onHold;
			System.out.println(onHold);
			break; // optional

		case "INPROGRESS":
			String inProgress = OldbankAccNumber.substring(1);
			newBankAcc = "2" + inProgress;
			System.out.println(inProgress);
			break; // optional

		default:
			String disbursed = OldbankAccNumber.substring(1);
			newBankAcc = "5" + disbursed;
			System.out.println(disbursed);
		}
		System.out.println();
		System.out.println("New Bank Account----" + newBankAcc);

		// Setting the driver
		Class.forName("com.mysql.cj.jdbc.Driver");

		// Open a connection to the database
		Connection con = DriverManager.getConnection(prop.getproperty("dbHostUrl"), prop.getproperty("dbUserName"),
				prop.getproperty("dbPassword"));
		Statement st = con.createStatement();

		// Executing the update query
		st.executeUpdate("update user_bank_accounts set bank_account_no='" + newBankAcc + "' where bank_account_no='"
				+ OldbankAccNumber + "' AND user_reference_number='" + ref + "';");

		con.close();

		return newBankAcc;
	}

	public void DBUpdate(String query) throws ClassNotFoundException, SQLException {

		// Setting the driver
		Class.forName("com.mysql.cj.jdbc.Driver");

		// Open a connection to the database
		Connection con = DriverManager.getConnection(prop.getproperty("dbHostUrl"), prop.getproperty("dbUserName"),
				prop.getproperty("dbPassword"));
		Statement st = con.createStatement();

		// Executing the update query
		st.executeUpdate(query);

		con.close();

	}

	public void clearWebField(By locator) {
		WebElement element = getWebDriver().findElement(locator);
		while (!element.getAttribute("value").equals("")) {
			element.sendKeys(Keys.BACK_SPACE);
		}
	}

	public static ValidatableResponse dataPoint() {
		// Creating a File instance
		String uri = "https://testing-gateway.test.paywithring.com/api/v1/custom-data-points/customDataPoints";
		File jsonData = new File(".\\Mock_Files\\dataPoint.json");
		HashMap<String, Object> headers = new HashMap<>();
		headers.put("client-id", "zx2789");

		ValidatableResponse response = RestAssured.given().baseUri(uri).contentType(ContentType.JSON).headers(headers)
				.body(jsonData).when().post().then();

		return response;

	}
/*
	public static ValidatableResponse customDataPoints_policy(String cabal_count, String transactionCable_count,String userRef) throws Exception {

		String body = "{\r\n" + "    \"encrypted_name\" : \"/MbvFhpTi7dS0IWLBp2EqA==\",\r\n"
				+ "    \"user_reference_number\" : \"" + userRef + "\",\r\n" + "    \"data_expiry_time\" : 864000,\r\n"
				+ "    \"custom_data_points\": {\r\n" + "        \"ntc_v1\": {\r\n"
				+ "            \"v2_last_mon_9_12_debit_amount\": \"\",\r\n"
				+ "            \"avg_9_12_debit_amount_per_count\": \"\",\r\n"
				+ "            \"avg_diff_credit_debit\": \"\",\r\n" + "            \"v2_last_mon_income\": \"\",\r\n"
				+ "            \"last_mon_9_12_debit_amount\": \"\",\r\n" + "            \"last_mon_income\": \"\",\r\n"
				+ "            \"total_debit_sum\": \"\",\r\n"
				+ "            \"six_mon_avg_9_12_debit_amount\": \"\",\r\n"
				+ "            \"avg_balance_3_month\": \"\",\r\n" + "            \"debit_sum_avg\": \"\",\r\n"
				+ "            \"diff_credit_debit\": \"\",\r\n" + "            \"total_debit_count\": \"\",\r\n"
				+ "            \"debit_unique_senders\": \"\",\r\n"
				+ "            \"available_recent_balance\": \"\",\r\n"
				+ "            \"available_max_balance\": \"\",\r\n"
				+ "            \"ratio_parsed_total_sms\": 0.01,\r\n" + "            \"parsed_sms_count\": 3\r\n"
				+ "        },\r\n" + "        \"fresh_ntc\": {\r\n"
				+ "            \"v2_last_mon_9_12_debit_amount\": \"\",\r\n"
				+ "            \"avg_9_12_debit_amount_per_count\": \"\",\r\n"
				+ "            \"avg_diff_credit_debit\": \"\",\r\n" + "            \"v2_last_mon_income\": \"\",\r\n"
				+ "            \"last_mon_9_12_debit_amount\": \"\",\r\n" + "            \"last_mon_income\": \"\",\r\n"
				+ "            \"total_debit_sum\": \"\",\r\n"
				+ "            \"six_mon_avg_9_12_debit_amount\": \"\",\r\n"
				+ "            \"avg_balance_3_month\": \"\",\r\n" + "            \"debit_sum_avg\": \"\"\r\n"
				+ "        },\r\n" + "        \"fresh_v16\": {\r\n" + "            \"avg_spend_count\": \"\",\r\n"
				+ "            \"avg_balance_9_month\": \"\",\r\n" + "            \"total_credit_sum\": \"\",\r\n"
				+ "            \"avg_9_12_debit_amount_per_count\": \"\",\r\n"
				+ "            \"avg_9_12_debit_count\": \"\",\r\n" + "            \"avg_balance_3_month\": \"\",\r\n"
				+ "            \"avg_balance_6_month\": \"\",\r\n"
				+ "            \"avg_cashwithdrawl_6_month\": \"\",\r\n"
				+ "            \"avg_cc_spend_count\": \"\",\r\n" + "            \"avg_diff_credit_debit\": \"\",\r\n"
				+ "            \"credit_sum_avg\": \"\",\r\n"
				+ "            \"kreditb_loan_overdue_before_fa\": \"\",\r\n"
				+ "            \"kreditbee_sum_paid_loans\": \"\",\r\n"
				+ "            \"last_mon_9_12_debit_amount\": \"\",\r\n" + "            \"last_mon_income\": \"\",\r\n"
				+ "            \"loan_overdues_1m\": \"\",\r\n"
				+ "            \"primary_bank_avg_balance_3_mon\": \"\",\r\n"
				+ "            \"total_credit_count\": \"\",\r\n"
				+ "            \"total_loan_approval_before_fa\": \"\",\r\n"
				+ "            \"all_sum_paid_loans_last_month\": \"\",\r\n"
				+ "            \"all_count_paid_loans_last_month\": \"\",\r\n"
				+ "            \"avg_9_month_income\": \"\",\r\n" + "            \"avg_income\": \"\",\r\n"
				+ "            \"count_loan_overdues_1m\": \"\",\r\n" + "            \"count_loan_overdues\": \"\",\r\n"
				+ "            \"unique_cc_count_12m\": \"\",\r\n" + "            \"is_close_comp\": 2,\r\n"
				+ "            \"is_true_comp\": 3,\r\n" + "            \"count_unique_account_name\": \"\",\r\n"
				+ "            \"message_type_unique_account_name_product_flag\": \"\",\r\n"
				+ "            \"debit_sum_avg\": \"\",\r\n"
				+ "            \"six_month_sum_overdue_overall\": \"\",\r\n"
				+ "            \"three_month_sum_overdue_overall\": \"\",\r\n"
				+ "            \"twelve_month_sum_emi_overall\": \"\",\r\n"
				+ "            \"twelve_month_sum_overdue_overall\": \"\",\r\n"
				+ "            \"last_month_count_loans_overall\": \"\",\r\n"
				+ "            \"last_month_max_pl\": \"\",\r\n"
				+ "            \"last_month_sum_overdue_pl\": \"\",\r\n"
				+ "            \"last_six_month_max_pl\": \"\",\r\n" + "            \"total_sms_count\": 814,\r\n"
				+ "            \"count_name_match\": 0,\r\n"
				+ "            \"device_model_hash\": \"88e50b8b269c2b2c2ff4aaa1bcc92dac\"\r\n" + "        },\r\n"
				+ "        \"fresh_v19\": {\r\n" + "            \"three_mon_avg_9_12_debit_amount\": \"\",\r\n"
				+ "            \"total_debit_sum\": \"\",\r\n"
				+ "            \"avg_9_12_debit_amount_per_count\": \"\",\r\n"
				+ "            \"avg_balance_3_month\": \"\",\r\n"
				+ "            \"last_mon_9_12_debit_amount\": \"\",\r\n" + "            \"last_mon_income\": \"\",\r\n"
				+ "            \"avg_cc_spend_count\": \"\"\r\n" + "        },\r\n" + "        \"policy\": {\r\n"
				+ "            \"unique_cc_count_12m\": 1,\r\n" + "            \"is_close_comp\": \"\",\r\n"
				+ "            \"is_true_comp\": \"\",\r\n" + "            \"count_unique_account_name\": 1,\r\n"
				+ "            \"message_type_unique_account_name_product_flag\": 1,\r\n"
				+ "            \"debit_sum_avg\": \"\",\r\n" + "            \"estimated_income_v3\": \"\",\r\n"
				+ "            \"estimated_income_v3_3m\": \"\",\r\n"
				+ "            \"estimated_income_v3_1m\": \"\",\r\n"
				+ "            \"device_model_hash\": \"2efd0eaef6b3e8c35def960a437381a2\",\r\n"
				+ "            \"count_loan_overdues_1\": \"\",\r\n"
				+ "            \"cabal_linked_user_reference_numbers\": [],\r\n" + "            \"cabal_count\":"
				+ cabal_count + ",\r\n" + "            \"ratio_parsed_total_sms\": 0.01\r\n" + "        },\r\n"
				+ "        \"repeat_v5\": {\r\n" + "            \"six_mon_avg_9_12_debit_amount\": \"\",\r\n"
				+ "            \"three_mon_avg_9_12_debit_amount\": \"\",\r\n"
				+ "            \"max_balance\": \"\",\r\n" + "            \"v2_last_mon_9_12_debit_amount\": \"\",\r\n"
				+ "            \"v2_last_mon_income\": \"\",\r\n" + "            \"avg_9_12_debit_amount\": \"\",\r\n"
				+ "            \"credit_sum_avg\": \"\",\r\n" + "            \"last_mon_income\": \"\"\r\n"
				+ "        },\r\n" + "        \"repeat_v5_offline\": {},\r\n"
				+ "        \"transaction_policy_variables\": {\r\n" + "            \"cabal_count\": "
				+ transactionCable_count + "\r\n" + "        },\r\n" + "        \"FRESH-V16\": {\r\n"
				+ "            \"avg_spend_count\": \"\",\r\n" + "            \"avg_balance_9_month\": \"\",\r\n"
				+ "            \"total_credit_sum\": \"\",\r\n"
				+ "            \"avg_9_12_debit_amount_per_count\": \"\",\r\n"
				+ "            \"avg_9_12_debit_count\": \"\",\r\n" + "            \"avg_balance_3_month\": \"\",\r\n"
				+ "            \"avg_balance_6_month\": \"\",\r\n"
				+ "            \"avg_cashwithdrawl_6_month\": \"\",\r\n"
				+ "            \"avg_cc_spend_count\": \"\",\r\n" + "            \"avg_diff_credit_debit\": \"\",\r\n"
				+ "            \"credit_sum_avg\": \"\",\r\n"
				+ "            \"kreditb_loan_overdue_before_fa\": \"\",\r\n"
				+ "            \"kreditbee_sum_paid_loans\": \"\",\r\n"
				+ "            \"last_mon_9_12_debit_amount\": \"\",\r\n" + "            \"last_mon_income\": \"\",\r\n"
				+ "            \"loan_overdues_1m\": \"\",\r\n"
				+ "            \"primary_bank_avg_balance_3_mon\": \"\",\r\n"
				+ "            \"total_credit_count\": \"\",\r\n"
				+ "            \"total_loan_approval_before_fa\": \"\",\r\n"
				+ "            \"all_sum_paid_loans_last_month\": \"\",\r\n"
				+ "            \"all_count_paid_loans_last_month\": \"\",\r\n"
				+ "            \"avg_9_month_income\": \"\",\r\n" + "            \"avg_income\": \"\",\r\n"
				+ "            \"count_loan_overdues_1m\": \"\",\r\n" + "            \"count_loan_overdues\": \"\",\r\n"
				+ "            \"unique_cc_count_12m\": \"\",\r\n" + "            \"is_close_comp\": 5,\r\n"
				+ "            \"is_true_comp\": 6,\r\n" + "            \"count_unique_account_name\": \"\",\r\n"
				+ "            \"message_type_unique_account_name_product_flag\": \"\",\r\n"
				+ "            \"debit_sum_avg\": \"\",\r\n"
				+ "            \"six_month_sum_overdue_overall\": \"\",\r\n"
				+ "            \"three_month_sum_overdue_overall\": \"\",\r\n"
				+ "            \"twelve_month_sum_emi_overall\": \"\",\r\n"
				+ "            \"twelve_month_sum_overdue_overall\": \"\",\r\n"
				+ "            \"last_month_count_loans_overall\": \"\",\r\n"
				+ "            \"last_month_max_pl\": \"\",\r\n"
				+ "            \"last_month_sum_overdue_pl\": \"\",\r\n"
				+ "            \"last_six_month_max_pl\": \"\",\r\n" + "            \"total_sms_count\": 814,\r\n"
				+ "            \"count_name_match\": 0,\r\n"
				+ "            \"device_model_hash\": \"88e50b8b269c2b2c2ff4aaa1bcc92dac\"\r\n" + "        }\r\n"
				+ "    }\r\n" + "}";

		HashMap<String, Object> headers = new HashMap<>();
		headers.put("client-id", "zx2789");

		ValidatableResponse response = RestAssured.given().baseUri(prop.getproperty("datapoints_url"))
				.contentType(ContentType.JSON).headers(headers).body(body).when().post().then();

		System.out.println("Request Url -->" + prop.getproperty("datapoints_url"));

		System.out.println("Request :" + body);

		String Resp = response.extract().body().asString();
		System.out.println("Response Body= " + Resp);

		return response;

	} */

	public void clearFieldUsingRobot(By byLocator, String text) throws AWTException {
		try {
			findElement(byLocator).click();
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_A);
			robot.keyPress(KeyEvent.VK_DELETE);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.keyRelease(KeyEvent.VK_A);
			robot.keyRelease(KeyEvent.VK_DELETE);
			logger.info("Cleared the text in : " + text);
			extent.extentLogger("clear text", "Cleared the text in : " + text);
		} catch (Exception e) {
			logger.error(e);
		}

	}

	public String executeQuery3(String dbTable, int columnNo) throws SQLException, ClassNotFoundException {
		// Setting the driver
		String ref = null;
		Class.forName("com.mysql.cj.jdbc.Driver");
		// Open a connection to the database

		Connection con = DriverManager.getConnection(prop.getproperty("dbHostUrl"), prop.getproperty("dbUserName"),
				prop.getproperty("dbPassword"));
		Statement st = con.createStatement();

		ResultSet rs = st.executeQuery(dbTable);
		while (rs.next()) {
			ref = rs.getString(columnNo);

		}
		return ref;
	}

	public String executeQuery2(String dbTable) throws SQLException, ClassNotFoundException {
		// Setting the driver
		String ref = null;
		Class.forName("com.mysql.cj.jdbc.Driver");
		// Open a connection to the database

		Connection con = DriverManager.getConnection(prop.getproperty("dbHostUrl"), prop.getproperty("dbUserName"),
				prop.getproperty("dbPassword"));
		Statement st = con.createStatement();

		ResultSet rs = st.executeQuery(dbTable);
		while (rs.next()) {
			ref = rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4) + " " + rs.getString(5) + " "
					+ rs.getString(6) + " " + rs.getString(7);

		}
		return ref;
	}

	public void executeInsertBlockUser(String blocked_reason, String entity_value, String entryType)
			throws ClassNotFoundException, SQLException {
		String ref = null;
		// Setting the driver
		Class.forName("com.mysql.cj.jdbc.Driver");

		// Open a connection to the database
		Connection con = DriverManager.getConnection(prop.getproperty("dbHostUrl"), prop.getproperty("dbUserName"),
				prop.getproperty("dbPassword"));
		Statement st = con.createStatement();

		PreparedStatement ps = con.prepareStatement(
				"Insert into db_txn_service.blocked_users(blocked_reason,entity_value,entity_type)VALUES('"
						+ blocked_reason + "','" + entity_value + "','" + entryType + "')");
		ps.executeUpdate();
		con.close();
	}

	public void deleteRow(String query) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");

		Connection con = DriverManager.getConnection(prop.getproperty("dbHostUrl"), prop.getproperty("dbUserName"),
				prop.getproperty("dbPassword"));
		Statement st = con.createStatement();
		String sql = query;
		st.executeUpdate(sql);
		st.close();
	}

	public void executeUpdateFatherName(String updateExecutionQuery, String dbTable)
			throws ClassNotFoundException, SQLException {

		// Setting the driver
		Class.forName("com.mysql.cj.jdbc.Driver");

		// Open a connection to the database
		Connection con = DriverManager.getConnection(prop.getproperty("dbHostUrl"), prop.getproperty("dbUserName"),
				prop.getproperty("dbPassword"));
		Statement st = con.createStatement();

		// Executing the update query
		st.executeUpdate(updateExecutionQuery);

		ResultSet rs = st.executeQuery(dbTable);
		System.out.println(
				"=================================================================================================");
		while (rs.next()) {
			System.out.println(rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(5) + " " + rs.getString(7)
					+ " " + rs.getString(10));
		}
		con.close();
	}

	public MobileElement scrollToElement(String an, String av) {

		return (MobileElement) getDriver().findElement(MobileBy
				.AndroidUIAutomator("new UiScrollable(new UiSelector()).scrollIntoView(" + an + "(\"" + av + "\"))"));
	}

	public void tapUsingCoordinates(int x, int y) {
		TouchAction t = new TouchAction(getDriver());
		try {
			t.tap(PointOption.point(x, y)).perform().release();
			logger.info("Tapped on " + x + "," + y + " co-ordinates");
			extent.extentLoggerPass("Tap", "Tapped on " + x + " , " + y + " co-ordinates");
		} catch (Exception e) {
			logger.info("Failed to tap on" + x + "," + y + " co-ordinates");
			extent.extentLoggerFail("Tap", "Failed to Tap on" + x + "," + y + " co-ordinates");
		}
	}

	public boolean isClickable(By locator) {

		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), 5);
			wait.until(ExpectedConditions.elementToBeClickable(locator));
			logger.info("Element is clickable");
			extent.extentLoggerPass("Clickable", "Element is clickable");
			return true;
		} catch (Exception e) {
			logger.info("Element is not clickable");
			extent.extentLoggerPass("Clickable", "Element is not clickable");
			return false;
		}
	}

	public static ResultSet executeQuery11(String dbTable) throws SQLException, ClassNotFoundException {
		// Setting the driver
		Class.forName("com.mysql.cj.jdbc.Driver");
		try {
			// Open a connection to the database

			con = DriverManager.getConnection(prop.getproperty("dbHostUrl"), prop.getproperty("dbUserName"),
					prop.getproperty("dbPassword"));
			Statement st = con.createStatement();

			ResultSet rs = st.executeQuery(dbTable);

//			while (rs.next()) {
//				System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4)
//						+ " " + rs.getString(5) + " " + rs.getString(6) + " " + rs.getString(7) + " "
//						+ rs.getString(8));
//			}

			// con.close();
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		// return dbTable;

	}

	public String getTextVal1(By byLocator, String concatValue) throws Exception {
		String finalValue = null;
		try {
			String Value = null;
			WebElement element = findElement(byLocator);
			Value = element.getText();
			finalValue = Value + " " + concatValue;
			logger.info(finalValue + " is Displayed");
			extent.extentLogger("", finalValue + " is Displayed");
			return finalValue;
		} catch (Exception e) {
			logger.error(e);
			extent.extentLogger("", finalValue + " is not Displayed" + e.getMessage());
			return null;
		}

	}

	public void executeUpdateOptional(String updateExecutionQuery, String dbTable)      throws ClassNotFoundException, SQLException { 
		// Setting the driver   
		Class.forName("com.mysql.cj.jdbc.Driver");   
		// Open a connection to the database   
		Connection con = DriverManager.getConnection(prop.getproperty("dbHostUrl"), prop.getproperty("dbUserName"), prop.getproperty("dbPassword"));   
		Statement st = con.createStatement();   
		// Executing the update query   
		st.executeUpdate(updateExecutionQuery);   
		ResultSet rs = st.executeQuery(dbTable);   
		while (rs.next()) {      
			System.out.println(rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(5) + " " + rs.getString(7)+ " " + rs.getString(10));   
			}   
		con.close();
		}
	

	public static ValidatableResponse cibilDummy(String score, String userRef, String onlyThinFlag,String name) {
		Random rand = new Random();

		HashMap<String, String> req_body = new HashMap<>();
//	    System.out.println((String) data[0][3]);
		req_body.put("encrypted_name", prop.getproperty("encryptedName"));
		req_body.put("score", score);
		req_body.put("user_reference_number", userRef);
		req_body.put("cibil_user_name", name);
		req_body.put("is_only_thin_cibil_loan_accounts", onlyThinFlag);
		req_body.put("no_of_loan_entries", "10.5");
		req_body.put("latest_loan_history_months_count", "27");

		JSONObject Myrequestbody = new JSONObject();

		Myrequestbody.put("encrypted_name", req_body.get("encrypted_name"));
		Myrequestbody.put("score", req_body.get("score"));
		Myrequestbody.put("user_reference_number", req_body.get("user_reference_number"));
		Myrequestbody.put("is_only_thin_cibil_loan_accounts", req_body.get("is_only_thin_cibil_loan_accounts"));
		Myrequestbody.put("cibil_user_name", req_body.get("cibil_user_name"));
		Myrequestbody.put("no_of_loan_entries", req_body.get("no_of_loan_entries"));
		Myrequestbody.put("latest_loan_history_months_count", req_body.get("latest_loan_history_months_count"));
		HashMap<String, Object> headers = new HashMap<>();
		headers.put("client-id", "zx2789");

		ValidatableResponse response = RestAssured.given().baseUri(prop.getproperty("cibil_url")).contentType(ContentType.JSON).headers(headers)
				.body(Myrequestbody.toJSONString()).when().post().then();

		System.out.println("Request Url -->" + prop.getproperty("cibil_url"));
		// ExtentReporter.extentLogger("", "Request Url -->" + url);
		System.out.println("Request :" + Myrequestbody);
		// ExtentReporter.extentLogger("", "Request :"+Myrequestbody);
		String Resp = response.extract().body().asString();
		System.out.println("Response Body= " + Resp);
		// ExtentReporter.extentLogger("", "Response Body= "+Resp);

		return response;
	}
	
	public String executeQuery1(String dbTable) throws SQLException, ClassNotFoundException {
		// Setting the driver
		String json_data;
		Class.forName("com.mysql.cj.jdbc.Driver");
		
			Connection con = DriverManager.getConnection(prop.getproperty("dbHostUrl"),prop.getproperty("dbUserName"),prop.getproperty("dbPassword"));
			Statement st = con.createStatement();

			ResultSet rs = st.executeQuery(dbTable);
			    
			rs.next();
			json_data=rs.getString(1);    
			con.close();
			
		return json_data;
	}
	
	public void executeInsert_block(String entity, String type, String reason) throws ClassNotFoundException, SQLException {

		// Setting the driver
		Class.forName("com.mysql.cj.jdbc.Driver");

		// Open a connection to the database
		Connection con = DriverManager.getConnection(prop.getproperty("dbHostUrl"),prop.getproperty("dbUserName"),prop.getproperty("dbPassword"));
		Statement st = con.createStatement();

		PreparedStatement ps = con.prepareStatement("Insert into db_txn_service.blocked_users(blocked_reason,entity_value,entity_type)VALUES('" +reason+ "','" +entity+ "','" +type+ "')");
		ps.executeUpdate();
		con.close();
	}

	public static ValidatableResponse customDataPoints(String cc_flag, String kla_flag,String userRef) {
		Random rand = new Random();

		HashMap<String,Object> kla_cc = new HashMap<String,Object>();
		kla_cc.put("is_true_comp", kla_flag);
		kla_cc.put("unique_cc_count_12m", cc_flag);
		kla_cc.put("avg_spend_count", "");
		kla_cc.put("avg_balance_9_month", "");
		kla_cc.put("total_credit_sum", "");
		kla_cc.put("avg_9_12_debit_amount_per_count", "");
		kla_cc.put("avg_9_12_debit_count", "");
		kla_cc.put("avg_balance_3_month", "");
		kla_cc.put("avg_balance_6_month", "");
		kla_cc.put("avg_cashwithdrawl_6_month", "");
		kla_cc.put("avg_cc_spend_count", "");
		kla_cc.put("avg_diff_credit_debit", "");
		kla_cc.put("credit_sum_avg", "");
		kla_cc.put("kreditb_loan_overdue_before_fa", "");
		kla_cc.put("kreditbee_sum_paid_loans", "");
		kla_cc.put("last_mon_9_12_debit_amount", "");
		kla_cc.put("last_mon_income", "");
		kla_cc.put("loan_overdues_1m", "");
		kla_cc.put("primary_bank_avg_balance_3_mon", "");
		kla_cc.put("total_credit_count", "");
		kla_cc.put("total_loan_approval_before_fa", "");
		kla_cc.put("all_sum_paid_loans_last_month", "");
		kla_cc.put("all_count_paid_loans_last_month", "");
		kla_cc.put("avg_9_month_income", "");
		kla_cc.put("avg_income", "");
		kla_cc.put("count_loan_overdues_1m", "");
		kla_cc.put("count_loan_overdues", "");
		kla_cc.put("is_close_comp", "");
		kla_cc.put("count_unique_account_name", "");
		kla_cc.put("message_type_unique_account_name_product_flag", "");
		kla_cc.put("debit_sum_avg", "");
		kla_cc.put("six_month_sum_overdue_overall", "");
		kla_cc.put("three_month_sum_overdue_overall", "");
		kla_cc.put("twelve_month_sum_emi_overall", "");
		kla_cc.put("twelve_month_sum_overdue_overall", "");
		kla_cc.put("last_month_count_loans_overall", "");
		kla_cc.put("last_month_max_pl", "");
		kla_cc.put("last_month_sum_overdue_pl", "");
		kla_cc.put("last_six_month_max_pl", "");
		kla_cc.put("total_sms_count", "");
		kla_cc.put("count_name_match", "");
		kla_cc.put("device_model_hash", "");
		kla_cc.put("ratio_parsed_total_sms", "0.15");
		
		/*
		 * HashMap<String,Object> ntc_keys = new HashMap<String,Object>();
		 * ntc_keys.put("v2_last_mon_9_12_debit_amount", "");
		 * ntc_keys.put("avg_9_12_debit_amount_per_count", "");
		 * ntc_keys.put("avg_diff_credit_debit", ""); ntc_keys.put("v2_last_mon_income",
		 * ""); ntc_keys.put("last_mon_9_12_debit_amount", "");
		 * ntc_keys.put("last_mon_income", ""); ntc_keys.put("total_debit_sum", "");
		 * ntc_keys.put("six_mon_avg_9_12_debit_amount", "");
		 * ntc_keys.put("avg_balance_3_month", ""); ntc_keys.put("debit_sum_avg", "");
		 * ntc_keys.put("diff_credit_debit", ""); ntc_keys.put("total_debit_count", "");
		 * ntc_keys.put("debit_unique_senders", "");
		 * ntc_keys.put("available_recent_balance", "");
		 * ntc_keys.put("available_max_balance", "");
		 * ntc_keys.put("ratio_parsed_total_sms", "0.15");
		 * ntc_keys.put("parsed_sms_count", "");
		 */
		
		/*HashMap<String,Object> ntc = new HashMap<String,Object>();
		ntc.put("ntc_v1", ntc_keys);*/
		
		HashMap<String,Object> fresh = new HashMap<String,Object>();
		fresh.put("FRESH-V16", kla_cc);
		
		HashMap<String,Object> custom = new HashMap<String,Object>();
		custom.put("custom_data_points", fresh);
		custom.put("encrypted_name", prop.getproperty("encryptedName"));
		custom.put("user_reference_number", userRef);
		//custom.put("custom_data_points", ntc);
		
		JSONObject Myrequestbody = new JSONObject();
		Myrequestbody.putAll(custom);

		/*Myrequestbody.put("encrypted_name", custom.get("encrypted_name"));
		Myrequestbody.put(, req_body.get("custom_data_points.fresh_v16.is_true_comp"));
		Myrequestbody.put("user_reference_number", custom.get("user_reference_number"));
		Myrequestbody.put("custom_data_points.fresh_v16.unique_cc_count_12m", req_body.get("custom_data_points.fresh_v16.unique_cc_count_12m"));*/
		HashMap<String, Object> headers = new HashMap<>();
		headers.put("client-id", "zx2789");

		ValidatableResponse response = RestAssured.given().baseUri(prop.getproperty("datapoints_url")).contentType(ContentType.JSON).headers(headers)
				.body(Myrequestbody.toJSONString()).when().post().then();

		System.out.println("Request Url -->" + prop.getproperty("datapoints_url"));
		// ExtentReporter.extentLogger("", "Request Url -->" + url);
		System.out.println("Request :" + Myrequestbody);
		// ExtentReporter.extentLogger("", "Request :"+Myrequestbody);
		String Resp = response.extract().body().asString();
		System.out.println("Response Body= " + Resp);
		// ExtentReporter.extentLogger("", "Response Body= "+Resp);

		return response;
	}
	
	
	public static ValidatableResponse customDataPoints_policy(String cabal_count,String transactionCable_count, String userRef) throws Exception {
		Random rand = new Random();
			String body="{\r\n"
					+ "    \"encrypted_name\" : \"/MbvFhpTi7dS0IWLBp2EqA==\",\r\n"
					+ "    \"user_reference_number\" : \""+userRef+"\",\r\n"
					+ "    \"data_expiry_time\" : 864000,\r\n"
					+ "    \"custom_data_points\": {\r\n"
					+ "        \"ntc_v1\": {\r\n"
					+ "            \"v2_last_mon_9_12_debit_amount\": \"\",\r\n"
					+ "            \"avg_9_12_debit_amount_per_count\": \"\",\r\n"
					+ "            \"avg_diff_credit_debit\": \"\",\r\n"
					+ "            \"v2_last_mon_income\": \"\",\r\n"
					+ "            \"last_mon_9_12_debit_amount\": \"\",\r\n"
					+ "            \"last_mon_income\": \"\",\r\n"
					+ "            \"total_debit_sum\": \"\",\r\n"
					+ "            \"six_mon_avg_9_12_debit_amount\": \"\",\r\n"
					+ "            \"avg_balance_3_month\": \"\",\r\n"
					+ "            \"debit_sum_avg\": \"\",\r\n"
					+ "            \"diff_credit_debit\": \"\",\r\n"
					+ "            \"total_debit_count\": \"\",\r\n"
					+ "            \"debit_unique_senders\": \"\",\r\n"
					+ "            \"available_recent_balance\": \"\",\r\n"
					+ "            \"available_max_balance\": \"\",\r\n"
					+ "            \"ratio_parsed_total_sms\": 0.01,\r\n"
					+ "            \"parsed_sms_count\": 3\r\n"
					+ "        },\r\n"
					+ "        \"fresh_ntc\": {\r\n"
					+ "            \"v2_last_mon_9_12_debit_amount\": \"\",\r\n"
					+ "            \"avg_9_12_debit_amount_per_count\": \"\",\r\n"
					+ "            \"avg_diff_credit_debit\": \"\",\r\n"
					+ "            \"v2_last_mon_income\": \"\",\r\n"
					+ "            \"last_mon_9_12_debit_amount\": \"\",\r\n"
					+ "            \"last_mon_income\": \"\",\r\n"
					+ "            \"total_debit_sum\": \"\",\r\n"
					+ "            \"six_mon_avg_9_12_debit_amount\": \"\",\r\n"
					+ "            \"avg_balance_3_month\": \"\",\r\n"
					+ "            \"debit_sum_avg\": \"\"\r\n"
					+ "        },\r\n"
					+ "        \"fresh_v16\": {\r\n"
					+ "            \"avg_spend_count\": \"\",\r\n"
					+ "            \"avg_balance_9_month\": \"\",\r\n"
					+ "            \"total_credit_sum\": \"\",\r\n"
					+ "            \"avg_9_12_debit_amount_per_count\": \"\",\r\n"
					+ "            \"avg_9_12_debit_count\": \"\",\r\n"
					+ "            \"avg_balance_3_month\": \"\",\r\n"
					+ "            \"avg_balance_6_month\": \"\",\r\n"
					+ "            \"avg_cashwithdrawl_6_month\": \"\",\r\n"
					+ "            \"avg_cc_spend_count\": \"\",\r\n"
					+ "            \"avg_diff_credit_debit\": \"\",\r\n"
					+ "            \"credit_sum_avg\": \"\",\r\n"
					+ "            \"kreditb_loan_overdue_before_fa\": \"\",\r\n"
					+ "            \"kreditbee_sum_paid_loans\": \"\",\r\n"
					+ "            \"last_mon_9_12_debit_amount\": \"\",\r\n"
					+ "            \"last_mon_income\": \"\",\r\n"
					+ "            \"loan_overdues_1m\": \"\",\r\n"
					+ "            \"primary_bank_avg_balance_3_mon\": \"\",\r\n"
					+ "            \"total_credit_count\": \"\",\r\n"
					+ "            \"total_loan_approval_before_fa\": \"\",\r\n"
					+ "            \"all_sum_paid_loans_last_month\": \"\",\r\n"
					+ "            \"all_count_paid_loans_last_month\": \"\",\r\n"
					+ "            \"avg_9_month_income\": \"\",\r\n"
					+ "            \"avg_income\": \"\",\r\n"
					+ "            \"count_loan_overdues_1m\": \"\",\r\n"
					+ "            \"count_loan_overdues\": \"\",\r\n"
					+ "            \"unique_cc_count_12m\": \"\",\r\n"
					+ "            \"is_close_comp\": 0,\r\n"
					+ "            \"is_true_comp\": 0,\r\n"
					+ "            \"count_unique_account_name\": \"\",\r\n"
					+ "            \"message_type_unique_account_name_product_flag\": \"\",\r\n"
					+ "            \"debit_sum_avg\": \"\",\r\n"
					+ "            \"six_month_sum_overdue_overall\": \"\",\r\n"
					+ "            \"three_month_sum_overdue_overall\": \"\",\r\n"
					+ "            \"twelve_month_sum_emi_overall\": \"\",\r\n"
					+ "            \"twelve_month_sum_overdue_overall\": \"\",\r\n"
					+ "            \"last_month_count_loans_overall\": \"\",\r\n"
					+ "            \"last_month_max_pl\": \"\",\r\n"
					+ "            \"last_month_sum_overdue_pl\": \"\",\r\n"
					+ "            \"last_six_month_max_pl\": \"\",\r\n"
					+ "            \"total_sms_count\": 814,\r\n"
					+ "            \"count_name_match\": 0,\r\n"
					+ "            \"device_model_hash\": \"88e50b8b269c2b2c2ff4aaa1bcc92dac\"\r\n"
					+ "        },\r\n"
					+ "        \"fresh_v19\": {\r\n"
					+ "            \"three_mon_avg_9_12_debit_amount\": \"\",\r\n"
					+ "            \"total_debit_sum\": \"\",\r\n"
					+ "            \"avg_9_12_debit_amount_per_count\": \"\",\r\n"
					+ "            \"avg_balance_3_month\": \"\",\r\n"
					+ "            \"last_mon_9_12_debit_amount\": \"\",\r\n"
					+ "            \"last_mon_income\": \"\",\r\n"
					+ "            \"avg_cc_spend_count\": \"\"\r\n"
					+ "        },\r\n"
					+ "        \"policy\": {\r\n"
					+ "            \"unique_cc_count_12m\": 0,\r\n"
					+ "            \"is_close_comp\": \"\",\r\n"
					+ "            \"is_true_comp\": \"\",\r\n"
					+ "            \"count_unique_account_name\": 1,\r\n"
					+ "            \"message_type_unique_account_name_product_flag\": 1,\r\n"
					+ "            \"debit_sum_avg\": \"\",\r\n"
					+ "            \"estimated_income_v3\": \"\",\r\n"
					+ "            \"estimated_income_v3_3m\": \"\",\r\n"
					+ "            \"estimated_income_v3_1m\": \"\",\r\n"
					+ "            \"device_model_hash\": \"2efd0eaef6b3e8c35def960a437381a2\",\r\n"
					+ "            \"count_loan_overdues_1\": \"\",\r\n"
					+ "            \"cabal_linked_user_reference_numbers\": [],\r\n"
					+ "            \"cabal_count\":"+cabal_count+",\r\n"
					+ "            \"ratio_parsed_total_sms\": 0.1\r\n"
					+ "        },\r\n"
					+ "        \"repeat_v5\": {\r\n"
					+ "            \"six_mon_avg_9_12_debit_amount\": \"\",\r\n"
					+ "            \"three_mon_avg_9_12_debit_amount\": \"\",\r\n"
					+ "            \"max_balance\": \"\",\r\n"
					+ "            \"v2_last_mon_9_12_debit_amount\": \"\",\r\n"
					+ "            \"v2_last_mon_income\": \"\",\r\n"
					+ "            \"avg_9_12_debit_amount\": \"\",\r\n"
					+ "            \"credit_sum_avg\": \"\",\r\n"
					+ "            \"last_mon_income\": \"\"\r\n"
					+ "        },\r\n"
					+ "        \"repeat_v5_offline\": {},\r\n"
					+ "        \"transaction_policy_variables\": {\r\n"
					+ "            \"cabal_count\": "+transactionCable_count+"\r\n"
					+ "        },\r\n"
					+ "        \"FRESH-V16\": {\r\n"
					+ "            \"avg_spend_count\": \"\",\r\n"
					+ "            \"avg_balance_9_month\": \"\",\r\n"
					+ "            \"total_credit_sum\": \"\",\r\n"
					+ "            \"avg_9_12_debit_amount_per_count\": \"\",\r\n"
					+ "            \"avg_9_12_debit_count\": \"\",\r\n"
					+ "            \"avg_balance_3_month\": \"\",\r\n"
					+ "            \"avg_balance_6_month\": \"\",\r\n"
					+ "            \"avg_cashwithdrawl_6_month\": \"\",\r\n"
					+ "            \"avg_cc_spend_count\": \"\",\r\n"
					+ "            \"avg_diff_credit_debit\": \"\",\r\n"
					+ "            \"credit_sum_avg\": \"\",\r\n"
					+ "            \"kreditb_loan_overdue_before_fa\": \"\",\r\n"
					+ "            \"kreditbee_sum_paid_loans\": \"\",\r\n"
					+ "            \"last_mon_9_12_debit_amount\": \"\",\r\n"
					+ "            \"last_mon_income\": \"\",\r\n"
					+ "            \"loan_overdues_1m\": \"\",\r\n"
					+ "            \"primary_bank_avg_balance_3_mon\": \"\",\r\n"
					+ "            \"total_credit_count\": \"\",\r\n"
					+ "            \"total_loan_approval_before_fa\": \"\",\r\n"
					+ "            \"all_sum_paid_loans_last_month\": \"\",\r\n"
					+ "            \"all_count_paid_loans_last_month\": \"\",\r\n"
					+ "            \"avg_9_month_income\": \"\",\r\n"
					+ "            \"avg_income\": \"\",\r\n"
					+ "            \"count_loan_overdues_1m\": \"\",\r\n"
					+ "            \"count_loan_overdues\": \"\",\r\n"
					+ "            \"unique_cc_count_12m\": \"\",\r\n"
					+ "            \"is_close_comp\": 0,\r\n"
					+ "            \"is_true_comp\": 0,\r\n"
					+ "            \"count_unique_account_name\": \"\",\r\n"
					+ "            \"message_type_unique_account_name_product_flag\": \"\",\r\n"
					+ "            \"debit_sum_avg\": \"\",\r\n"
					+ "            \"six_month_sum_overdue_overall\": \"\",\r\n"
					+ "            \"three_month_sum_overdue_overall\": \"\",\r\n"
					+ "            \"twelve_month_sum_emi_overall\": \"\",\r\n"
					+ "            \"twelve_month_sum_overdue_overall\": \"\",\r\n"
					+ "            \"last_month_count_loans_overall\": \"\",\r\n"
					+ "            \"last_month_max_pl\": \"\",\r\n"
					+ "            \"last_month_sum_overdue_pl\": \"\",\r\n"
					+ "            \"last_six_month_max_pl\": \"\",\r\n"
					+ "            \"total_sms_count\": 814,\r\n"
					+ "            \"count_name_match\": 0,\r\n"
					+ "            \"device_model_hash\": \"88e50b8b269c2b2c2ff4aaa1bcc92dac\"\r\n"
					+ "        }\r\n"
					+ "    }\r\n"
					+ "}";
			
			
	        
	        HashMap<String, Object> headers = new HashMap<>();
	        headers.put("client-id", "zx2789");

	 

	        ValidatableResponse response = RestAssured.given().baseUri(prop.getproperty("datapoints_url")).contentType(ContentType.JSON).headers(headers)
	                .body(body).when().post().then();

	 

	        System.out.println("Request Url -->" + prop.getproperty("datapoints_url"));
	      
	        System.out.println("Request :" + body);
	       
	        String Resp = response.extract().body().asString();
	        System.out.println("Response Body= " + Resp);
	        
	 

	        return response;
	
		
		
	}
	
	public static ValidatableResponse customDigiScore(String userRef,String noOfDevices, String noOfDevices2w) throws Exception {
		Random rand = new Random();
			String body="{\r\n"
					+ "    \"encrypted_name\" : \"/MbvFhpTi7dS0IWLBp2EqA==\",\r\n"
					+ "    \"user_reference_number\" : \""+userRef+"\",\r\n"
					+ "    \"data_expiry_time\" : 864000,\r\n"
					+ "    \"custom_digiscore\": {\r\n"
					+ "            \"is_digiscore_reliable\": 1,\r\n"            
					+ "        \"income_estimated\": {\r\n"
					+ "            \"salaried\": 0,\r\n"
					+ "            \"self_employed\": 0,\r\n"					            
					+ "        		},\r\n"
					+ "            \"estimated_salary_v2\": 0,\r\n"
					+ "            \"estimated_income_v2\": 7000,\r\n"
					+ "            \"estimated_income_v3\": \"\",\r\n"
					+ "            \"estimated_income_v3_3m\": \"\",\r\n"
					+ "            \"estimated_income_v3_1m\": \"\",\r\n"
					+ "            \"social_score\": 9,\r\n"
					+ "            \"app_last_data_ts\": 2020-11-25,\r\n"
					+ "            \"digi_activity\": \"\",\r\n"
					+ "            \"cc_cash_withdrawal_3m\": \"\",\r\n"
					+ "            \"cheque_bounce_6m\": \"\",\r\n"
					+ "            \"emi_bounce_6m\": 1,\r\n"
					+ "            \"emi_bounce_count_6m\": \"\",\r\n"
					+ "            \"loan_overdues\": 1,\r\n"
					+ "            \"shopping_6m\": \"\",\r\n"
					+ "            \"utility_6m\": \"\",\r\n"
					+ "            \"cash_withdrawal_3m\": \"\",\r\n"
					+ "            \"is_customer_name_matched\": true,\r\n"
					+ "            \"duplicate_imei\": false,\r\n"
					+ "            \"count_loan_overdues\": \"\",\r\n"
					+ "            \"count_loan_overdues_1m\": \"\",\r\n"
					+ "            \"count_loan_overdues_2m\": \"\",\r\n"
					+ "            \"count_name_match\": 1,\r\n"
					+ "            \"defaulters_in_contacts\": \"\",\r\n"
					+ "            \"loan_overdues_1m\": \"\",\r\n"
					+ "            \"loan_overdues_2m\": \"\",\r\n"
					+ "            \"total_contacts\": 0,\r\n"
					+ "            \"device_age_in_days\": \"\",\r\n"
					+ "            \"device_present_age_in_days\": \"\",\r\n"
					+ "            \"number_of_imei_users\": \"\",\r\n"
					+ "            \"number_of_imei_users_3m\": 2,\r\n"
					+ "            \"number_of_advertising_id_users\": \"\",\r\n"
					+ "            \"number_of_advertising_id_users_3m\": \"\",\r\n"
					+ "            \"number_of_android_id_users\": \"\",\r\n"
					+ "            \"number_of_android_id_users_3m\": \"\",\r\n"
					+ "            \"total_contacts_imei\": \"\",\r\n"
					+ "            \"total_contacts_advertising_id\": \"\",\r\n"
					+ "            \"total_contacts_android_id\": \"\",\r\n"
					+ "            \"request_imei\": \"\",\r\n"
					+ "            \"list_of_imei_users\": [],\r\n"
					+ "            \"list_of_imei_users_3m\": [],\r\n"
					+ "            \"list_of_advertising_id_users\": [],\r\n"
					+ "            \"list_of_advertising_id_users_3m\": [],\r\n"
					+ "            \"list_of_android_id_users\": [],\r\n"
					+ "            \"list_of_android_id_users_3m\": [],\r\n"
					+ "            \"number_of_devices\": \""+noOfDevices+"\",\r\n"
					+ "            \"number_of_devices_2w\": \""+noOfDevices2w+"\",\r\n"
					+ "            \"total_debit_count\": \"\",\r\n"
					+ "            \"total_credit_count\": \"\",\r\n"
					+ "            \"kreditbee_count_loan_overdue_sms\": \"\"\r\n"
					+ "            \"kreditbee_count_loans\": 0,\r\n"
					+ "            \"is_app_installed\": true,\r\n"
					+ "            \"digiscore_generated_for\": 2020-11-25 11:23:37,\r\n"
					+ "            \"last_generated_timestamp\": 2020-11-25 11:23:37,\r\n"
					+ "            \"number_of_gdi_users_3m\": 2,\r\n"
					+ "            \"number_of_gdi_users\": 5,\r\n"           
					+ "        \"top_customer_names\": {\r\n"
					+ "            \"HARISHA\": 2,\r\n"
					+ "            \"HARISHA SHETTY\": 2,\r\n"
					+ "        },\r\n"
					+ "        \"masked_pan_numbers\": [\r\n"
					+ "             GT*******6A,\r\n"
					+ "             ACTXXXXX3L,\r\n"
					+ "             ADQXXXXX8Ar\n"
					+ "        ],\r\n"
					+ "        \"aadhaar_numbers\": [\r\n"
					+ "             1234,\r\n"
					+ "             5678\r\n"           
					+ "        ]\r\n"
					+ "    }\r\n"
					+ "}";
			
			
	        
	        HashMap<String, Object> headers = new HashMap<>();
	        headers.put("client-id", "zx2789");

	 

	        ValidatableResponse response = RestAssured.given().baseUri(prop.getproperty("digiScore_url")).contentType(ContentType.JSON).headers(headers)
	                .body(body).when().post().then();

	 

	        System.out.println("Request Url -->" + prop.getproperty("digiScore_url"));
	      
	        System.out.println("Request :" + body);
	       
	        String Resp = response.extract().body().asString();
	        System.out.println("Response Body= " + Resp);
	        
	 

	        return response;
	
		
		
	}
	public static ValidatableResponse engagementWhiteList(String userRef, String offerCode) throws Exception{
		
		Random rand = new Random();

		HashMap<String, String> req_body = new HashMap<>();
//	    System.out.println((String) data[0][3]);
		req_body.put("encrypted_name", prop.getproperty("encryptedName"));
		req_body.put("user_reference_number", userRef);
		req_body.put("offer_code", offerCode);
		
		JSONObject Myrequestbody = new JSONObject();

		Myrequestbody.put("encrypted_name", req_body.get("encrypted_name"));
		Myrequestbody.put("user_reference_number", req_body.get("user_reference_number"));
		Myrequestbody.put("offer_code", req_body.get("offer_code"));
		
		HashMap<String, Object> headers = new HashMap<>();
		headers.put("client-id", "zx2789");

		ValidatableResponse response = RestAssured.given().baseUri(prop.getproperty("whiteList_url")).contentType(ContentType.JSON).headers(headers)
				.body(Myrequestbody.toJSONString()).when().post().then();

		System.out.println("Request Url -->" + prop.getproperty("whiteList_url"));
		// ExtentReporter.extentLogger("", "Request Url -->" + url);
		System.out.println("Request :" + Myrequestbody);
		// ExtentReporter.extentLogger("", "Request :"+Myrequestbody);
		String Resp = response.extract().body().asString();
		System.out.println("Response Body= " + Resp);
		// ExtentReporter.extentLogger("", "Response Body= "+Resp);

		return response;
		
	}
	
public static ValidatableResponse autoDocUpload(String userRef, String mobNo) throws Exception{
		
		Random rand = new Random();

		String body="{\r\n"
				+ "    \"encrypted_name\" : \"/MbvFhpTi7dS0IWLBp2EqA==\",\r\n"
				+ "    \"project\" : \"APM\",\r\n"
				+ "    \"user_reference_number\" : \""+userRef+"\",\r\n"
				+ "    \"aadhaar_front_image\": \"https://testing-service.test.ideopay.in/storage/generated_documents/aadhaar/"+mobNo+"-front.png\",\r\n"
				+ "    \"aadhaar_back_image\":\"https://testing-service.test.ideopay.in/storage/generated_documents/aadhaar/"+mobNo+"-back.png\"\r\n"
				+ "}";
		HashMap<String, Object> headers = new HashMap<>();
        headers.put("client-id", "zx2789");

 

        ValidatableResponse response = RestAssured.given().baseUri(prop.getproperty("autoDoc_url")).contentType(ContentType.JSON).headers(headers)
                .body(body).when().post().then();

 

        System.out.println("Request Url -->" + prop.getproperty("autoDoc_url"));
      
        System.out.println("Request :" + body);
       
        String Resp = response.extract().body().asString();
        System.out.println("Response Body= " + Resp);
        
 

        return response;
		
	}

public static ValidatableResponse verifyPan(String panNo, String dob,String firstName,String lastName,String middleName) throws Exception{
	
	Random rand = new Random();

	HashMap<String, String> req_body = new HashMap<>();
//    System.out.println((String) data[0][3]);
	req_body.put("encrypted_name", prop.getproperty("encryptedName"));
	req_body.put("pan", panNo);
	req_body.put("pan_status","2");
	req_body.put("dob",dob);
	req_body.put("first_name",firstName);
	req_body.put("last_name",lastName);
	req_body.put("middle_name",middleName);
	req_body.put("actual_pan_status","E");
	req_body.put("dob_verification_status","VERIFIED");
	JSONObject Myrequestbody = new JSONObject();

	Myrequestbody.put("encrypted_name", req_body.get("encrypted_name"));
	Myrequestbody.put("pan", req_body.get("pan"));
	Myrequestbody.put("pan_status", req_body.get("pan_status"));
	Myrequestbody.put("dob", req_body.get("dob"));
	Myrequestbody.put("first_name", req_body.get("first_name"));
	Myrequestbody.put("last_name", req_body.get("last_name"));
	Myrequestbody.put("middle_name", req_body.get("middle_name"));
	Myrequestbody.put("actual_pan_status", req_body.get("actual_pan_status"));
	Myrequestbody.put("dob_verification_status", req_body.get("dob_verification_status"));
	
	HashMap<String, Object> headers = new HashMap<>();
	headers.put("client-id", "zx2789");

	ValidatableResponse response = RestAssured.given().baseUri(prop.getproperty("panVerify_url")).contentType(ContentType.JSON).headers(headers)
			.body(Myrequestbody.toJSONString()).when().post().then();

	System.out.println("Request Url -->" + prop.getproperty("panVerify_url"));
	// ExtentReporter.extentLogger("", "Request Url -->" + url);
	System.out.println("Request :" + Myrequestbody);
	// ExtentReporter.extentLogger("", "Request :"+Myrequestbody);
	String Resp = response.extract().body().asString();
	System.out.println("Response Body= " + Resp);
	// ExtentReporter.extentLogger("", "Response Body= "+Resp);

	return response;
	
}
	
	public static ValidatableResponse generateForceBill(String userRef,String lineRef, String stmtDate) throws Exception{
		Random rand = new Random();

		HashMap<String, String> req_body = new HashMap<>();
//	    System.out.println((String) data[0][3]);
		req_body.put("encrypted_name", prop.getproperty("encryptedName"));
		req_body.put("user_reference_number", userRef);
		req_body.put("line_reference_number", lineRef);
		req_body.put("force_generate_for_other_days", "true");
		req_body.put("statement_date", stmtDate);
		
		JSONObject Myrequestbody = new JSONObject();

		Myrequestbody.put("encrypted_name", req_body.get("encrypted_name"));
		Myrequestbody.put("user_reference_number", req_body.get("user_reference_number"));
		Myrequestbody.put("line_reference_number", req_body.get("line_reference_number"));
		Myrequestbody.put("force_generate_for_other_days", req_body.get("force_generate_for_other_days"));
		Myrequestbody.put("statement_date", req_body.get("statement_date"));
		//Myrequestbody.put("latest_loan_history_months_count", req_body.get("latest_loan_history_months_count"));
		HashMap<String, Object> headers = new HashMap<>();
		headers.put("client-id", "zx2789");

		ValidatableResponse response = RestAssured.given().baseUri(prop.getproperty("bill_statement")).contentType(ContentType.JSON).headers(headers)
				.body(Myrequestbody.toJSONString()).when().post().then();

		System.out.println("Request Url -->" + prop.getproperty("bill_statement"));
		// ExtentReporter.extentLogger("", "Request Url -->" + url);
		System.out.println("Request :" + Myrequestbody);
		// ExtentReporter.extentLogger("", "Request :"+Myrequestbody);
		String Resp = response.extract().body().asString();
		System.out.println("Response Body= " + Resp);
		// ExtentReporter.extentLogger("", "Response Body= "+Resp);

		return response;
	}
	public String calendarDate(int days) throws Exception{
		LocalDateTime ldt = LocalDateTime.now().plusDays(days);
		DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);

		String formatter = formmat1.format(ldt);
		return formatter;
	}
	
	public void executeUpdateOnly(String updateExecutionQuery , String dbTable) throws ClassNotFoundException, SQLException {

		// Setting the driver
		Class.forName("com.mysql.cj.jdbc.Driver");

		// Open a connection to the database
		Connection con = DriverManager.getConnection(prop.getproperty("dbHostUrl"),prop.getproperty("dbUserName"),prop.getproperty("dbPassword"));
		Statement st = con.createStatement();

		// Executing the update query
		st.executeUpdate(updateExecutionQuery);

		
		con.close();
	}
	
	
	public boolean executeQuery(String dbTable) throws SQLException, ClassNotFoundException {
		// Setting the driver
		Class.forName("com.mysql.cj.jdbc.Driver");
		try {
			// Open a connection to the database
			
			Connection con = DriverManager.getConnection(prop.getproperty("dbHostUrl"),prop.getproperty("dbUserName"),prop.getproperty("dbPassword"));
			Statement st = con.createStatement();

			ResultSet rs = st.executeQuery(dbTable);

			while (rs.next()) {
				System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4)
						+ " " + rs.getString(5) + " " + rs.getString(6) + " " + rs.getString(7) + " "
						+ rs.getString(8));
				return true;
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return false;
	}

}