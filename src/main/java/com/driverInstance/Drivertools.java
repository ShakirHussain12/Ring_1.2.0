package com.driverInstance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Stream;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Reporter;
import org.testng.SkipException;
import com.applitools.eyes.images.Eyes;
import com.extent.ExtentReporter;
import com.propertyfilereader.PropertyFileReader;
import com.utility.LoggingUtils;
import com.utility.Utilities;
import io.appium.java_client.AppiumDriver;

public class Drivertools {

	private String host;
	private int port;
	private String deviceName;
	private static String platform;
	private int appTimeOut;
	private String remoteUrl;
	private String appPackage;
	private String appActivity;
	private String appVersion;
	private String APkFileName;
	private PropertyFileReader handler;
	private static String testName;
	private String browserType;
	private String url = "";
	public static String runModule;
	private URLConnection connection;
	private URL connectURL;
	public static boolean startTest = false;
	private static String runMode = "null";
	private static String driverVersion = "";
	public static boolean click = true;
	public static String methodName = "";
	Date date = new Date();
	long StartTime;
	public static Instant startTime;
	public static Duration timeElapsed;
	private static String DeviceList;
	private static String TVDeviceList;
	private static String apk;
	public static String apkName = null;
	public static String dir = System.getProperty("user.dir") + "\\APK\\";

	public static String getApk() {
		return apk;
	}

	public static void setApk(String apk) {
		Drivertools.apk = apk;
	}

	public static String getDeviceList() {
		return DeviceList;
	}

	public static void setDeviceList(String deviceList) {
		DeviceList = deviceList;
	}

	public static String getTestName() {
		return testName;
	}

	@SuppressWarnings("static-access")
	public void setTestName(String testName) {
		this.testName = testName;
	}

	public static ThreadLocal<AppiumDriver<WebElement>> tlDriver = new ThreadLocal<>();

	public static ThreadLocal<WebDriver> tlWebDriver = new ThreadLocal<>();

	public static ThreadLocal<AppiumDriver<WebElement>> driverTV = new ThreadLocal<>();

	public static Eyes eyes = new Eyes();

	public static ExtentReporter extent = new ExtentReporter();

	protected DesiredCapabilities capabilities = new DesiredCapabilities();

	protected Utilities util = new Utilities();

	private static String ENV = "";

	/** The Constant logger. */
//	final static Logger logger = Logger.getLogger("rootLogger");
	static LoggingUtils logger = new LoggingUtils();

	public static AppiumDriver<WebElement> getDriver() {
		return tlDriver.get();
	}

	public static WebDriver getWebDriver() {
		return tlWebDriver.get();
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public static String getPlatform() {
		return platform;
	}

	public static void setPlatfrom(String Platform) {
		platform = Platform;
	}

	protected int getappTimeOut() {
		return appTimeOut;
	}

	protected void setappTimeOut(int timeOut) {
		this.appTimeOut = timeOut;
	}

	protected String getremoteUrl() {
		return this.remoteUrl;
	}

	protected void setremoteUrl(String remoteUrl) {
		this.remoteUrl = remoteUrl;
	}

	protected void setAppPackage(String appPackage) {
		this.appPackage = appPackage;
	}

	protected String getAppPackage() {
		return this.appPackage;
	}

	protected void setAppActivity(String appActivity) {
		this.appActivity = appActivity;
	}

	protected String getappActivity() {
		return this.appActivity;
	}

	protected void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	protected String getAppVersion() {
		return this.appVersion;
	}

	protected String getAPKName() {
		return this.APkFileName;
	}

	protected void setAPKName(String apkName) {
		this.APkFileName = apkName;
	}

	protected PropertyFileReader getHandler() {
		return handler;
	}

	protected void setHandler(PropertyFileReader handler) {
		this.handler = handler;
	}

	protected void setBrowserType(String BrowserType) {
		this.browserType = BrowserType;
	}

	protected String getBrowserType() {
		return this.browserType;
	}

	protected void setURL(String url) {
		this.url = url;
	}

	protected String getURL() {
		return this.url;
	}

	protected String runMode() {
		return this.runMode();
	}

	@SuppressWarnings("static-access")
	protected void setRunModule(String runModule) {
		this.runModule = runModule;
	}

	public static String getRunModule() {
		return runModule;
	}

	@SuppressWarnings("static-access")
	public void setRunMode(String runMode) {
		this.runMode = runMode;
	}

	@SuppressWarnings("static-access")
	public String getRunMode() {
		return this.runMode;
	}

	@SuppressWarnings("static-access")
	public void setENV(String env) {
		this.ENV = env;
	}

	public static String getENV() {
		return ENV;
	}

	public static String getDriverVersion() {
		return driverVersion;
	}

	@SuppressWarnings("static-access")
	public void setDriverVersion(String driverVersion) {
		this.driverVersion = driverVersion;
	}

	public static String getTVDeviceList() {
		return TVDeviceList;
	}

	@SuppressWarnings("static-access")
	public void setTVDeviceList(String TVDeviceList) {
		this.TVDeviceList = TVDeviceList;
	}

	public static String getENvironment() {
		return "<h5> ENV : <a href=\"" + getENV() + "\" onclick='return " + click + ";'\">" + getENV() + "</a></h5>";
	}

	public Drivertools(String application) {
		setHandler(new PropertyFileReader("properties/Execution.properties"));
		setHost(getHandler().getproperty("HOST_IP"));
		setPort(Integer.parseInt(getHandler().getproperty("HOST_PORT")));
		setappTimeOut(Integer.parseInt(getHandler().getproperty("APP_TIMEOUT")));
		setremoteUrl("http://" + getHost() + ":" + getPort() + "/wd/hub");

		setHandler(new PropertyFileReader("properties/AppPackageActivity.properties"));
		setAppPackage(getHandler().getproperty(application + "Package"));
		setAppActivity(getHandler().getproperty(application + "Activity"));
		setAppVersion(getHandler().getproperty(application + "Version"));
		setAPKName(getHandler().getproperty(application + "apkfile"));
		setDriverVersion(getHandler().getproperty("DriverVersion"));
	}

	{
		setPlatfrom(Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getSuite().getName());
		setTestName(Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getName());
		setBrowserType(
				Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("browserType"));
		setURL(Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("url"));
		setRunModule(Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("runModule"));
		setRunMode(Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("runMode"));
		if (getPlatform().equals("Android")) {
			setDeviceList(getListOfDevicesConnected().get(0).toString());
			if (getTestName().equals("Android_ChromeCast")) {
				setTVDeviceList(getListOfDevicesConnected().get(1).toString());
			}
		}

		if (getTestName().equals("Android_UserSessionManagement")) {
			setPlatfrom(Utilities.setPlatform);
		} else if (methodName.equals("merchantFlow") || methodName.equals("userScanAndPayFlow") || methodName.equals("promocodeFlow")
				|| methodName.equals("transactionSetPin") || methodName.equals("WebRingApp1")
				|| methodName.equals("WebRingApp") || methodName.equals("offerFlow")|| methodName.equals("kycSkipped") || methodName.equals("bannerLogicOnHomepage")||  methodName.equals("ringInstaWhitelist")||  methodName.equals("feedBackMechanism")
				|| methodName.equals("ringPolicy_TC_3") || methodName.equals("ringPolicy_TC_4") || methodName.equals("subscription")
				|| methodName.equals("ringPolicy_TC_9") || methodName.equals("ringPolicy_TC_10")
				|| methodName.equals("ringPolicy_TC_11") || methodName.equals("ringPolicy_TC_12")
				|| methodName.equals("ringPolicy_TC_17") || methodName.equals("merchantFlow")
				|| methodName.equals("userScanAndPayFlow") || methodName.equals("promocodeFlow")
				|| methodName.equals("transactionSetPin") || methodName.equals("WebRingApp1")
				|| methodName.equals("WebRingApp") || methodName.equals("offerFlow") ||  methodName.equals("ringInstaWhitelist") ||methodName.equals("ringPolicy_TC_28") 
				|| methodName.equals("ringPolicy_TC_29") || methodName.equals("ringPolicy_TC_30") || methodName.equals("ringPolicy_TC_31") || methodName.equals("ringPolicy_TC_32") || methodName.equals("ringPolicy_TC_33")
				|| methodName.equals("ringPolicy_TC_1") || methodName.equals("ringPolicy_TC_2") || methodName.equals("ringPolicy_TC_5") || methodName.equals("ringPolicy_TC_6")
				|| methodName.equals("ringPolicy_TC_158_digi") || methodName.equals("ringPolicy_TC_158_L1") || methodName.equals("ringPolicy_TC_159_BC1") || methodName.equals("ringPolicy_TC_160")
				|| methodName.equals("ringPolicy_TC_62") || methodName.equals("ringPolicy_TC_63") || methodName.equals("ringPolicy_TC_64") || methodName.equals("ringPolicy_TC_65")
				|| methodName.equals("ringPolicy_TC_70") || methodName.equals("ringPolicy_TC_73") || methodName.equals("ringPolicy_TC_74")
				|| methodName.equals("ringPolicy_TC_170") || methodName.equals("ringPolicy_TC_171") || methodName.equals("ringPolicy_TC_172")|| methodName.equals("ringPolicy_TC_173")
				|| methodName.equals("ringPolicy_TC_174") || methodName.equals("ringPolicy_TC_165") ||methodName.equals("ringPolicy_TC_166") || methodName.equals("ringPolicy_TC_131") 
				||methodName.equals("ringPolicy_TC_99") || methodName.equals("ringPolicy_TC_106")
				||methodName.equals("ringPolicy_TC_105") ||methodName.equals("ringPolicy_TC_103") || methodName.equals("ringPolicy_TC_107")
				||methodName.equals("ringPolicy_TC_40") || methodName.equals("ringPolicy_TC_41") || methodName.equals("ringPolicy_TC_42")
				||methodName.equals("ringPolicy_TC_43") || methodName.equals("ringPolicy_TC_44") || methodName.equals("ringPolicy_TC_45")
				|| methodName.equals("ringPolicy_TC_46") || methodName.equals("ringPolicy_TC_167") || methodName.equals("ringPolicy_TC_168")
				||methodName.equals("ringPolicy_TC_169") || methodName.equals("ringPolicy_TC_7") || methodName.equals("ringPolicy_TC_8")
				||methodName.equals("ringPolicy_TC_151") || methodName.equals("ringPolicy_TC_47") || methodName.equals("ringPolicy_TC_48")|| methodName.equals("ringPolicy_TC_49")
				||methodName.equals("ringPolicy_TC_52") ||methodName.equals("ringPolicy_TC_53") ||methodName.equals("ringPolicy_TC_54") ||methodName.equals("ringPolicy_TC_55")
				||methodName.equals("ringPolicy_TC_56") ||methodName.equals("ringPolicy_TC_149") ||methodName.equals("ringPolicy_TC_144")||methodName.equals("ringPolicy_TC_132")
				||methodName.equals("ringPolicy_TC_133") ||methodName.equals("ringPolicy_TC_134") ||methodName.equals("ringPolicy_TC_135")||methodName.equals("ringPolicy_TC_145")
				||methodName.equals("ringPolicy_TC_146") || methodName.equals("instaLoanHomeScreenScenario") || methodName.equals("ringInstaLoanOptionalJourney")||methodName.equals("InstaLoanMandatory") ||methodName.equals("afterClickOnApplyNowForLoanOfferBanner")
				||methodName.equals("ringInstaTransactionHistory")||methodName.equals("instaLoanRepayment")||methodName.equals("InstaLoanDetailScreen") || methodName.equals("offerScreen") || methodName.equals("ageCheck") || methodName.equals("kycSkipped")
				 || methodName.equals("transactionSetPin") || methodName.equals("repaymentmultipleCase")
				|| methodName.equals("bankTransferFlow") || methodName.equals("addAddresFlow") || methodName.equals("userScanAndPayFlow") || methodName.equals("ringEngagement_TC_01") 
				|| methodName.equals("ringEngagement_TC_02") || methodName.equals("ringEngagement_TC_03") || methodName.equals("ringEngagement_TC_04") || methodName.equals("ringEngagement_TC_05")
				|| methodName.equals("ringEngagement_TC_08") || methodName.equals("ringEngagement_TC_09") || methodName.equals("ringEngagement_TC_06") || methodName.equals("ringEngagement_TC_10")
				|| methodName.equals("ringEngagement_TC_12")|| methodName.equals("ringEngagement_TC_11")|| methodName.equals("ringEngagement_TC_13") 
				|| methodName.equals("ringEngagement_TC_14")|| methodName.equals("ringEngagement_TC_15")|| methodName.equals("ringEngagement_TC_16")
				|| methodName.equals("ringPolicy_TC_120") || methodName.equals("ringPolicy_TC_125")|| methodName.equals("ringPolicy_TC_126") || methodName.equals("ringPolicy_TC_141") 
				|| methodName.equals("ringPolicy_TC_122_123_124")|| methodName.equals("ringPolicy_TC_142") 
				|| methodName.equals("ringPolicy_TC_143") || methodName.equals("ringPolicy_TC_162")|| methodName.equals("ringPolicy_TC_163")|| methodName.equals("ringPolicy_TC_164") 
				|| methodName.equals("ringPolicy_TC_108") || methodName.equals("ringPolicy_TC_109")||methodName.equals("ringPolicy_TC_110")||methodName.equals("ringPolicy_TC_111")){
			setPlatfrom("Web");
		}

		try {
			connectURL = new URL("https://www.google.com");
			connection = connectURL.openConnection();
			connection.connect();
			connection.getInputStream().close();
		} catch (IOException e1) {
			System.out.println("<<<<<<---- Network is Down  ---->>>>>>>");
			System.exit(0);
		}

		if (getPlatform().equals("Web")) {
			if (getURL().equals("https://ci-api-gateway.test.ideopay.in/admin/login")) {
				setENV(getURL());
			} else if (getPlatform().equals("Android")) {
				setENV("Native App");
				setApk(Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("Platform"));
				click = false;
			} else if (getPlatform().equals("ios")) {
				setENV("Native");
				click = false;
			}
		}
		logger.info("PlatForm :: " + getPlatform());
		if (Stream.of("Android", "ios", "Web").anyMatch(getPlatform()::equals)) {
			setHandler(new PropertyFileReader("properties/ExecutionControl.properties"));
			if (getHandler().getproperty(getTestName()).equals("Y") && (getRunMode().contentEquals(getTestName()))
					|| (getRunMode().contentEquals("Suites"))) {
				logger.info("Running Test :: " + getTestName());
				logger.info("Run Mode :: YES");
				startTest = true;
			} else {
				logger.info(getTestName() + " : Test Skipped");
				logger.info("RunMode is :: No");
				startTest = false;
				throw new SkipException(getTestName() + " : Test Skipped ");
			}
		} else {
			throw new SkipException("PlatForm not matched...");
		}
	}

	public static ArrayList<String> getListOfDevicesConnected() {
		ArrayList<String> deviceID = new ArrayList<>();
		try {
			String cmd2 = "adb devices";
			Process p1 = Runtime.getRuntime().exec(cmd2);
			BufferedReader br = new BufferedReader(new InputStreamReader(p1.getInputStream()));
			String s = "";
			Thread.sleep(1000);
			while (!(s = br.readLine()).isEmpty()) {
				if (!s.equals("List of devices attached")) {
					deviceID.add(s.replaceAll("device", "").trim());
				}
			}
			return deviceID;
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return deviceID;
	}
}
