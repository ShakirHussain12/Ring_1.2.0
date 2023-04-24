package com.business.RingPay;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.AssertJUnit.assertNotNull;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import com.Datasheet.RingPay_TestData_DataProvider;
import com.android.RingPayPages.AddAddresPage;
import com.android.RingPayPages.AddAddressPage;
import com.android.RingPayPages.AdminPage;
import com.android.RingPayPages.FeeDetailsPage;
import com.android.RingPayPages.FeedbackPage;
import com.android.RingPayPages.GmailLoginPage;
import com.android.RingPayPages.HomPageNew;
import com.android.RingPayPages.HomePage;
import com.android.RingPayPages.InstaLoanHomePage;
import com.android.RingPayPages.InstaLoanMandatoryJourney;
import com.android.RingPayPages.InstaLoanOptionalJourney;
import com.android.RingPayPages.InstaLoanPage;
import com.android.RingPayPages.InstaLoanTransactionHistoryPage;
import com.android.RingPayPages.InstaLoanViewDetailsPage;
import com.android.RingPayPages.InstaOfferPage;
import com.android.RingPayPages.KycDocument;
import com.android.RingPayPages.MerchantOfferPage;
import com.android.RingPayPages.MobileLoginPage;
import com.android.RingPayPages.OfferPage;
import com.android.RingPayPages.PAN_DetailsPage;
import com.android.RingPayPages.PayEarlyPaymentPage;
import com.android.RingPayPages.PaymentPage;
import com.android.RingPayPages.PermissionPage;
import com.android.RingPayPages.PromoCodeOfferPage;
import com.android.RingPayPages.RingLoginPage;
import com.android.RingPayPages.RingPayMerchantFlowPage;
import com.android.RingPayPages.RingPayMerchantFlowPage_New;
import com.android.RingPayPages.RingPromoCodeLogin;
import com.android.RingPayPages.RingUserDetailPage;
import com.android.RingPayPages.ScanAnyQRPage;
import com.android.RingPayPages.SignUP_LoginPage;
import com.android.RingPayPages.TermsAndConditionPage;
import com.android.RingPayPages.TestingPortalWebPage;
import com.android.RingPayPages.TransactionPIN;
import com.android.RingPayPages.UserRegistrationNew;
import com.android.RingPayPages.UserRegistrationPage;
import com.android.RingPayPages.UserSuspendedCases;
import com.android.RingPayPages.UtilitiesBillPage;
import com.android.RingPayPages.BankTransferModule;
import com.android.RingPayPages.CashBackPage;
import com.android.RingPayPages.DigiWeb;
import com.android.RingPayPages.FacebookLoginPage;
import com.driverInstance.CommandBase;
import com.epam.ta.reportportal.ws.annotations.In;
import com.excel.ExcelFunctions;
import com.extent.ExtentReporter;
import com.propertyfilereader.PropertyFileReader;
import com.utility.LoggingUtils;
import com.utility.Utilities;

import ch.qos.logback.core.recovery.ResilientSyslogOutputStream;
import io.appium.java_client.MobileBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.touch.offset.PointOption;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class RingPayBusinessLogic extends Utilities {

	public RingPayBusinessLogic(String Application) throws InterruptedException {
		new CommandBase(Application);
		init();
	}

	RingPay_TestData_DataProvider dataProvider = new RingPay_TestData_DataProvider();
	
	private int timeout;
	
	public long age;
	private String keyLogin;
	private static String mobileNumber;
	public static String firstName;
	private String lastName;
	private String mothersName;
	private String email;
	private String gender;
	private String year;
	private String monthByName;
	private String date;
	private String panCard;
	private String aadharFrontImage;
	private String aadharBackImage;
	private String panImage;
	private String middleName;
	private String bankAccountNumber;
	private String bankifsc;
	private String bankAccountType;
	private String bankAccountHolder;
	private String user_reference;
	private String userReferenceNo;
	private String BankAccountRandom;
	private String value;
	private String dob;
	private String refBeforeRepayment;

	SoftAssert softAssertion = new SoftAssert();
	boolean launch = "" != null;
	/** Retry Count */
	private int retryCount;
	ExtentReporter extent = new ExtentReporter();

	/** The Constant logger. */
	static LoggingUtils logger = new LoggingUtils();

	/** Test data from property file. */
	public static PropertyFileReader prop = new PropertyFileReader(System.getProperty("user.dir")+"//properties//testData.properties");

	/** The Android driver. */
	public AndroidDriver<AndroidElement> androidDriver;
	public String amount[] = { "1001", "0", "1" };
	public static boolean relaunchFlag = false;
	public static boolean appliTools = false;
	public String noTxt;
	public static boolean PopUp = false;

	public int getTimeout() {
		return timeout;
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

	/**
	 * Initiate Property File.
	 *
	 * @param byLocator the by locator
	 */

	public void init() {

		PropertyFileReader handler = new PropertyFileReader("properties/Execution.properties");
		setTimeout(Integer.parseInt(handler.getproperty("TIMEOUT")));
		setRetryCount(Integer.parseInt(handler.getproperty("RETRY_COUNT")));
		logger.info(
				"Loaded the following properties" + " TimeOut :" + getTimeout() + " RetryCount :" + getRetryCount());
	}

	public void TearDown() {
		logger.info("App tear Down");
		getDriver().quit();
	}

	
	public void trueCallerSkip() throws Exception {
		explicitWaitVisibilityNonDefault(MobileLoginPage.titleTruecaller, 10);
		click(MobileLoginPage.BtnSkipTrueCaller, "Skip");
		explicitWaitVisibilityNonDefault(RingLoginPage.objMobTextField,10);
	}
	
//=============================================User Play Store flow Start===============================================================//
	public void User_Play_Store_Flow(String key, String permission) throws Exception {
		extent.HeaderChildNode("User Play store Flow Module");
		
		switch (permission) {
		case "Let'sRing":
			extent.extentLoggerPass("PASS", "TC_Ring_Core_190 - To Verify if scanner requires Camera permission");
			break;
		case "MobileNo":
			getDriver().resetApp();
			//enablePermissions();
			extent.extentLoggerPass("PASS","TC_Ring_Core_01 - To Verify the Login screen when user opens the app by clicking on App Icon"); 
			extent.extentLoggerPass("TC_Ring_Core_02","TC_Ring_Core_02 - To verify When User selects Enable Permission option");
			break;
		default:
			break;

		}

		switch (key) {
		case "make Payment":
			verifyElementPresent(RingLoginPage.objMakePaymentLetsRingItBtn, "Make Payment page");
			click(RingLoginPage.objMakePaymentLetsRingItBtn, getText(RingLoginPage.objMakePaymentLetsRingItBtn));
			try {
				click(RingLoginPage.objMakePaymentLetsRingItBtn, getText(RingLoginPage.objMakePaymentLetsRingItBtn));
			} catch (Exception e) {
				// TODO: handle exception
			}
			click(RingPayMerchantFlowPage.objAmountTextField, "Enter Amount Field");
			type1(RingPayMerchantFlowPage.objAmountTextField, prop.getproperty("Amount_Merchant"), "Amount Field");
			verifyElementPresentAndClick(RingLoginPage.objMakePaymentPageProceedBtn,getText(RingLoginPage.objMakePaymentPageProceedBtn));
			break;
		case "MobileNo":
			Aclick(RingLoginPage.objLoginLink, "Sign up login link");
			break;
		default:
			break;

		}
		userPlayStoreLoginFlow(prop.getproperty("lessThanTenMob"), prop.getproperty("moreThanTenMob"),
				prop.getproperty("specialCharMob"), prop.getproperty("spaceMob"), prop.getproperty("validMob"),
				prop.getproperty("invalidOtp"), prop.getproperty("lessOtp"));
	}

	public void userPlayStoreLoginFlow(String lessThanTenMob, String moreThanTenMob, String specialCharMob,
			String spaceMob, String validMob, String invalidOtp, String lessOtp) throws Exception {

		explicitWaitVisibilityNonDefault(RingLoginPage.objLoginMobile, 10);
		String mobileTxt = getText(RingLoginPage.objLoginMobile);
		String googleTxt = getText(RingLoginPage.objLoginGoogle);
		String facebookTxt = getText(RingLoginPage.objLoginFacebook);
		String termsTxt = getText(RingLoginPage.objTermsLink_PrivacyFooter);

		extent.extentLoggerPass("TC_Ring_Core_03","TC_Ring_Core_03 - To verify User Selects signup/Login option under Don't have a QR Code?");

		loginMobile();
		extent.extentLoggerPass("TC_Ring_Core_04", "TC_Ring_Core_04 - To Verify when user Continue with mobile option");
		extent.extentLoggerPass("TC_Ring_Core_05", "TC_Ring_Core_05 - To Verify the Verify mobile screen");

		explicitWaitVisibilityNonDefault(RingLoginPage.objCountryCode, 10);
		extent.extentLoggerPass("TC_Ring_Core_06","TC_Ring_Core_06 - To Verify user should able to see  \"Enter your mobile number\" followed by +91.");

		logger.info("Verify mobile number with <10 digits");
		mobileNoValidation2(lessThanTenMob);
		explicitWaitVisibilityNonDefault(RingLoginPage.objMobError, 10);
		String errorMsg = getText(RingLoginPage.objMobError);
		softAssertion.assertEquals(errorMsg, " Please enter a valid mobile number");
		extent.extentLoggerPass("TC_Ring_Core_07","TC_Ring_Core_07 - To Verify User enter mobile number less than 10 digit & Click on Proceed Button");

		clearField(RingLoginPage.objMobTextField, "Mobile Text Field");
		logger.info("Verify mobile number with >10 digits");
		mobileNoValidation2(moreThanTenMob);
		try {
			String otpAutoRead = getText(RingLoginPage.OtpAutoRead);
			softAssertion.assertEquals(otpAutoRead, "Auto Reading OTP");

			extent.extentLoggerPass("TC_Ring_Core_08","TC_Ring_Core_08 - To Verify User enter mobile number more than 10 digit");
			Back(1);
			logger.info("Verify mobile number with special characters");
			clearField(RingLoginPage.objMobTextField, "Mobile Text Field");
			mobileNoValidation2(specialCharMob);
			explicitWaitVisibilityNonDefault(RingLoginPage.objMobError, 10);
			softAssertion.assertEquals(errorMsg, " Please enter a valid mobile number");
			extent.extentLoggerPass("TC_Ring_Core_10","TC_Ring_Core_10 - To Verify User tries enter punctuations or special character in field");

			logger.info("Verify mobile number with alphabets");
			clearField(RingLoginPage.objMobTextField, "Mobile Text Field");
			mobileNoValidation2(RandomIntegerGenerator(4) + generateRandomString(2));
			explicitWaitVisibilityNonDefault(RingLoginPage.objMobError, 10);
			softAssertion.assertEquals(errorMsg, " Please enter a valid mobile number");
			extent.extentLoggerPass("TC_Ring_Core_09","TC_Ring_Core_09 - To Verify User tries enter alphabets in field");

			clearField(RingLoginPage.objMobTextField, "Mobile Text Field");
			logger.info("Verify mobile number with space in between");
			mobileNoValidation2(spaceMob);
			explicitWaitVisibilityNonDefault(RingLoginPage.objMobError, 10);
			softAssertion.assertEquals(errorMsg, " Please enter a valid mobile number");
			extent.extentLoggerPass("TC_Ring_Core_11","TC_Ring_Core_11 - To Verify User tries enter space between the number");

			clearField(RingLoginPage.objMobTextField, "Mobile Text Field");

			logger.info("Verify mobile number with entering valid number");
			mobileNoValidation2(validMob);
			explicitWaitVisibilityNonDefault(RingLoginPage.OtpAutoRead, 10);
			extent.extentLoggerPass("TC_Ring_Core_15", "TC_Ring_Core_15 - To Verify OTP  auto reading time");

			softAssertion.assertEquals(otpAutoRead, "Auto Reading OTP");
			extent.extentLoggerPass("TC_Ring_Core_12","TC_Ring_Core_12 - To verify User tries enters mobile number without country code.");
			extent.extentLoggerPass("TC_Ring_Core_13","TC_Ring_Core_13 - To Verify User tries enter valid mobile number");
		} catch (Exception e) {
			e.printStackTrace();
		}

		WebElement resendOtp = findElement(RingLoginPage.resendOtpTxt);
		String clickable = getAttributValue("clickable", RingLoginPage.resendOtpTxt);
		softAssertion.assertEquals("false", clickable);
		extent.extentLoggerPass("TC_Ring_Core_17","TC_Ring_Core_17 - To Verify the text given below the OTP number box when the timer is in progress");

		explicitWaitClickable(RingLoginPage.resendOtpTxt, 10);
		extent.extentLoggerPass("TC_Ring_Core_18","TC_Ring_Core_18 - To Verify the text given below the OTP number box when the timer is completed.");

		String focused_before = getAttributValue("focused", RingLoginPage.objOtpTxtField1);
		System.out.println(focused_before);
		softAssertion.assertEquals("false", focused_before);
		extent.extentLoggerPass("TC_Ring_Core_19","TC_Ring_Core_19 - To Verify the OTP number box behaviour when the timer is started.");

		explicitWaitVisibilityNonDefault(RingLoginPage.resendOtpTxt, 10);
		String focused_after = getAttributValue("clickable", RingLoginPage.objOtpTxtField1);
		System.out.println(focused_after);
		click(RingLoginPage.objOtpTxtField1, "Otp text field");
		softAssertion.assertEquals("true", focused_after);
		extent.extentLoggerPass("TC_Ring_Core_20","TC_Ring_Core_20 - To Verify the OTP number box behaviour when the timer is completed.");

		click(RingLoginPage.objOtpTxtField1, "Otp text field");
		type(RingLoginPage.objOtpTxtField1, invalidOtp, "Enter OTP");
		explicitWaitVisibilityNonDefault(RingLoginPage.OtpError, 10);
		logger.info("OTP Error message");
		String otpErrorTxt = getText(RingLoginPage.OtpError);
		softAssertion.assertEquals(otpErrorTxt, "Please enter a valid OTP");
		extent.extentLoggerPass("TC_Ring_Core_21", "TC_Ring_Core_21 - To Verify User enter invalid OTP");

		clearField(RingLoginPage.objOtpTxtField1, "Enter OTP");
		click(RingLoginPage.objOtpTxtField1, "Otp text field");
		type(RingLoginPage.objOtpTxtField1, lessOtp, "Enter OTP");
		boolean Otp_flag = verifyElementNotPresent(RingLoginPage.OtpError, 10);
		softAssertion.assertEquals(false, Otp_flag);
		extent.extentLoggerPass("TC_Ring_Core_23","TC_Ring_Core_23 - Validation message should be display as 'Please enter a valid OTP'");
		extent.extentLoggerPass("TC_Ring_Core_24","TC_Ring_Core_24 - To Verify if user enters less than 6 digit number");

		explicitWaitClickable(RingLoginPage.resendOtpTxt, 10);
		extent.extentLoggerPass("TC_Ring_Core_25", "TC_Ring_Core_25 - To Verify Resend OTP should clickable");
		clearField(RingLoginPage.objOtpTxtField1, "Enter OTP");

		Back(1);
		Back(1);

		explicitWaitVisibility(RingLoginPage.objMobTextField,30);
		clearField(RingLoginPage.objMobTextField, "Mobile Text Field");
		String blockMobileNo = "9" + RandomIntegerGenerator(9);
		mobileNoValidation2(blockMobileNo);

		explicitWaitVisibilityNonDefault(MobileLoginPage.txtResendOtp, 20);
		verifyElementPresentAndClick(MobileLoginPage.txtResendOtp, "Resend Button 1 Time");
		String getAutoreadValidation = getText(MobileLoginPage.txtAutoReadingOTP);

		softAssertion.assertEquals("Auto Reading OTP", getAutoreadValidation);
		logger.info("Auto Reading OTP Validate " + getAutoreadValidation);
		extent.extentLoggerPass("Auto Reading OTP", "Auto Reading OTP Validate Successfully");
		try {
			explicitWaitVisibilityNonDefault(MobileLoginPage.txtOtpTimeStamp, 20);
			String otpTimeStamp = getText(MobileLoginPage.txtOtpTimeStamp);
			otpTimeStamp = otpTimeStamp.substring(0, 5);
			System.out.println("OTP Time Stamp::" + otpTimeStamp);
			waitTime(1000);
			String otpBoxDisable = getAttributValue("focused", MobileLoginPage.enterOTPNumberFiled);
			System.out.println("Element Focus:: " + otpBoxDisable);
			softAssertion.assertEquals(" 00:0", otpTimeStamp);
			if (otpBoxDisable.equals("false")) {
				softAssertion.assertEquals("false", otpBoxDisable);
				logger.info("OTP Box Disable");
				extent.extentLoggerPass("OTP Box Disable", "OTP Box Disable");
			} else {
				logger.info("OTP Box Enabled");
				extent.extentLoggerFail("OTP Box Enable", "OTP Box Enabled");
			}
		} catch (Exception e) {

		}
		extent.extentLoggerPass("TC_Ring_Core_26", "TC_Ring_Core_26 - To Verify Resend OTP option");

		// Attemp 2
		explicitWaitVisibilityNonDefault(MobileLoginPage.txtResendOtp, 20);
		Aclick(RingLoginPage.objOtpTxtField1, "Otp text field");
		type(RingLoginPage.objOtpTxtField1, RandomIntegerGenerator(6), "Enter OTP");
		verifyElementPresentAndClick(MobileLoginPage.txtResendOtp, "Resend Button 2 Time");

		String getAutoreadValidation1 = getText(MobileLoginPage.txtAutoReadingOTP);
		softAssertion.assertEquals("Auto Reading OTP", getAutoreadValidation1);
		logger.info("Auto Reading OTP Validate " + getAutoreadValidation1);
		extent.extentLoggerPass("Auto Reading OTP", "Auto Reading OTP Validate Successfully");

		// Attemp 3
		explicitWaitVisibilityNonDefault(MobileLoginPage.txtResendOtp, 20);
		Aclick(RingLoginPage.objOtpTxtField1, "Otp text field");
		type(RingLoginPage.objOtpTxtField1, RandomIntegerGenerator(6), "Enter OTP");
		verifyElementPresentAndClick(MobileLoginPage.txtResendOtp, "Resend Button 3 Time");

		// Attemp 4
		explicitWaitVisibilityNonDefault(MobileLoginPage.txtResendOtp, 20);
		Aclick(RingLoginPage.objOtpTxtField1, "Otp text field");
		type(RingLoginPage.objOtpTxtField1, RandomIntegerGenerator(6), "Enter OTP");
		verifyElementPresentAndClick(MobileLoginPage.txtResendOtp, "Resend Button 4 Time");

		// Attemp 5
		explicitWaitVisibilityNonDefault(MobileLoginPage.txtResendOtp, 20);
		Aclick(RingLoginPage.objOtpTxtField1, "Otp text field");
		type(RingLoginPage.objOtpTxtField1, RandomIntegerGenerator(6), "Enter OTP");
		verifyElementPresentAndClick(MobileLoginPage.txtResendOtp, "Resend Button 5 Time");
		if (verifyElementDisplayed(MobileLoginPage.txtResendOtp)) {
			Aclick(MobileLoginPage.txtResendOtp, "Resend Button 5 Time");
		}

		explicitWaitVisibilityNonDefault(RingLoginPage.txtBlockPhoneNoPopupMessage, 20);
		String popupMessageValidationOfBlockNumber = getText(RingLoginPage.txtBlockPhoneNoPopupMessage);
		logger.info("Expected: " + popupMessageValidationOfBlockNumber);
		softAssertion.assertEquals("Your Ring account has been temporarily blocked due to security reasons. Try again after 2 minutes.",popupMessageValidationOfBlockNumber);
		logger.info("Temprory Block Your Number For 2 Minutes Validated PopUp Message");
		extent.extentLoggerPass("PopUp Of Block User For 2 Minutes","Your Ring account has been temporarily blocked due to security reasons. Try again after 2 minutes.");
		explicitWaitClickable(RingLoginPage.objOkGotitBtn,30);
		click(RingLoginPage.btnOkGotIt, "Button Ok, Got It!");
		waitTime(2000);
		click(RingLoginPage.objNoneBtn,"None of the above");
		extent.extentLoggerPass("TC_Ring_Core_34","TC_Ring_Core_34 To verify when user click Skip/Cancel option on truecaller popup ");
		extent.extentLoggerPass("TC_Ring_Core_27","TC_Ring_Core_27 To Verify user enters wrong otp and attempts for 5th time  by clicking on Resend button ");
		// TC30
		explicitWaitVisibilityNonDefault(RingLoginPage.objVerifyMobHeader, 20);
		String verifyMoblieNumberEnter = getText(RingLoginPage.objVerifyMobHeader);
		softAssertion.assertEquals("Verify Mobile", verifyMoblieNumberEnter);
		logger.info(verifyMoblieNumberEnter);
		extent.extentLoggerPass("Verify Mobile Page", "Verify Mobile Page is visible");
		

		explicitWaitVisibility(RingLoginPage.objMobTextField,30);
		Aclick(RingLoginPage.objMobTextField, "Mobile text field");
		type(RingLoginPage.objMobTextField, blockMobileNo, "Mobile text field");
		System.out.println(blockMobileNo);
		explicitWaitVisibility(RingLoginPage.objOkGotitBtn,30);
		Aclick(RingLoginPage.objOkGotitBtn, "Button Ok, Got It!");
		extent.extentLoggerPass("TC_Ring_Core_28","TC_Ring_Core_28 To Verify when user click on Ok, Got It! on the bottom sheet ");
		// TC31
		click(RingLoginPage.objMobTextField, "Mobile Number Field");
		clearField(RingLoginPage.objMobTextField, "Mobile Number Field");

		logger.info("Expected: " + popupMessageValidationOfBlockNumber);
		softAssertion.assertEquals("Your Ring account has been temporarily blocked due to security reasons. Try again after 2 minutes.",popupMessageValidationOfBlockNumber);
		logger.info("Temprory Block Your Number For 2 Minutes Validated PopUp Message");
		extent.extentLoggerPass("PopUp Of Block User For 2 Minutes","Your Ring account has been temporarily blocked due to security reasons. Try again after 2 minutes.");
		logger.info("TC_Ring_Core_31 To Verify when user enter the same mobile number which is blocked");
		extent.extentLoggerPass("TC_Ring_Core_29","TC_Ring_Core_29 To Verify when user enter the same mobile number which is blocked");

		explicitWaitVisibilityNonDefault(RingLoginPage.objVerifyMobHeader, 30);
		logger.info("Verify Mobile Header");
		String verifyMobHeaderTxt = getText(RingLoginPage.objVerifyMobHeader);
		softAssertion.assertEquals(verifyMobHeaderTxt, "Verify Mobile");
		explicitWaitVisibility(RingLoginPage.objMobTextField,30);
		Aclick(RingLoginPage.objMobTextField, "Mobile text field");
		type(RingLoginPage.objMobTextField, "9" + RandomIntegerGenerator(9), "Mobile text field");
		enterOtp("888888");
		// TC32
		explicitWaitVisibilityNonDefault(MobileLoginPage.btnReadAndAccept, 10);
		String txtReadAndAccept = getText(MobileLoginPage.btnReadAndAccept);
		logger.info(txtReadAndAccept);
		softAssertion.assertEquals("Read & Accept", txtReadAndAccept);
		extent.extentLoggerPass("TC_Ring_Core_30", "TC_Ring_Core_30 To Verify user enters valid OTP");

		explicitWaitVisibilityNonDefault(RingLoginPage.objRingPermissionsHeader, 10);
	
		Swipe("up", 1);
		explicitWaitVisibilityNonDefault(RingLoginPage.ckycCheckBox, 10);
		Aclick(RingLoginPage.ckycCheckBox, "Terms and Conditions checkbox");

		Aclick(RingLoginPage.objReadAcceptBtn, "Read & Accept button");
		waitTime(2000);
		String kycError = getText(UserRegistrationPage.objToast);
		System.out.println(kycError);
		softAssertion.assertEquals("Please accept terms and conditions", kycError);
//		extent.extentLoggerPass("TC_Ring_Core_52","TC_Ring_Core_52 To Verify if user only checked in whatsapp notification check box and Continue with read & accept");
//
//		extent.extentLoggerPass("TC_Ring_Core_54","TC_Ring_Core_54 To Verify when user clicks on read and accept with unchecked CKYC consent");

		click(RingLoginPage.ckycCheckBox, "Terms and Conditions checkbox");
		Swipe("DOWN", 1);
		explicitWaitVisibility(RingLoginPage.objRingPermissionsHeader,30);
		// 50 to 54
		if (verifyElementPresent(RingLoginPage.objRingPermissionsHeader, "RingPay permissions")) {

			logger.info("Ring Pay Permissions page (SMS, LOCATION & PHONE)");
			String ringPermissionTxt = getText(RingLoginPage.objRingPermissionsHeader);
			Assert.assertEquals(ringPermissionTxt, "Permissions");

			click(RingLoginPage.objReadAcceptBtn, "Read & Accept button");

			explicitWaitVisibilityNonDefault(RingLoginPage.objLocAccess, 10);
			click(RingLoginPage.objLocDeny, "Location Deny option");
			click(RingLoginPage.objPhoneAccess, "Phone access option");
			click(RingLoginPage.objSMSAccess, "SMS access option");

			click(RingLoginPage.objPermCancel, "Cancel Button");
			explicitWaitClickable(RingLoginPage.objReadAcceptBtn, 10);
			click(RingLoginPage.objReadAcceptBtn, "Read & Accept button");

			explicitWaitVisibilityNonDefault(RingLoginPage.objLocAccess, 10);
			click(RingLoginPage.objLocAccess, "Location Access");

		}
		logger.info("TC_Ring_Core_59 To verify when users allow all the permission");
		//extent.extentLoggerPass("TC_Ring_Core_59", "TC_Ring_Core_59 To verify when users allow all the permission");
		softAssertion.assertAll();
	}

//===========================================User Play store Flow end===============================================================//
	
//========================================SIM binding Flow Start=================================================================//
	
	
	public void simBindingFlow() throws Exception {
		extent.HeaderChildNode("SIM Binding Flow");
		/*enablePermissions();
		
		explicitWaitVisibilityNonDefault(RingLoginPage.objLoginLink, 10);
		click(RingLoginPage.objLoginLink, "Signup/Login link");
		explicitWaitVisibilityNonDefault(RingLoginPage.objLoginMobile, 10);
		click(RingLoginPage.objLoginMobile, "Continue with Mobile option");
		waitTime(2000);
		verifyElementPresent(RingLoginPage.objNoneBtn,"None of the above");
		click(RingLoginPage.objNoneBtn,"None of the above");*/
		verifyElementPresent2(RingLoginPage.objMobTextField, "Mobile Number Fieled");
		logger.info("TC_Ring_Core_39-click Skip/Cancel option on truecaller popup");
		extent.extentLoggerPass("TC_Ring_Core_39", "TC_Ring_Core_39-To verify when user click Skip/Cancel option on truecaller popup");
		String nMobileNumber = "9" + RandomIntegerGenerator(9);
		click(RingLoginPage.objMobTextField, "Mobile Number Field");
		verifyElementPresent(RingLoginPage.objNoneBtn,"None of the above");
		click(RingLoginPage.objNoneBtn,"None of the above");
		type(RingLoginPage.objMobTextField, nMobileNumber, "Mobile Number Field");
		verifyElementPresentAndClick(MobileLoginPage.linkVerifyBySendingSMS, "Verify By Sending SMS");
		logger.info("TC_Ring_Core_31-To verify hyperlink on OTP screen");
		extent.extentLoggerPass("TC_Ring_Core_31", "TC_Ring_Core_31-To verify hyperlink on OTP screen");
		readAndAccept();
		verifyElementPresent2(MobileLoginPage.simcardnotfound, "SIM Card Not Found");
		logger.info("TC_Ring_Core_36-verify the SIM binding with no sim present");
		extent.extentLoggerPass("TC_Ring_Core_36", "TC_Ring_Core_36-To verify the SIM binding with no sim present");
		click(MobileLoginPage.btnTryAgain, "Try Again Button");
		String nMobileNumber1 = "9" + RandomIntegerGenerator(9);
		/*click(RingLoginPage.objLoginMobile, "Continue with Mobile option");
		waitTime(2000);
		verifyElementPresent(RingLoginPage.objNoneBtn,"None of the above");
		click(RingLoginPage.objNoneBtn,"None of the above");*/

		String nMobileNumber6 = "9" + RandomIntegerGenerator(9);
		click(RingLoginPage.objMobTextField, "Mobile Number Field");
		//type(RingLoginPage.objMobTextField, nMobileNumber6, "Mobile Number Field");
		verifyElementPresent(RingLoginPage.objNoneBtn,"None of the above");
		click(RingLoginPage.objNoneBtn,"None of the above");
		type(RingLoginPage.objMobTextField, nMobileNumber6, "Mobile Number Field");
		enterOtp("888888");
		explicitWaitVisibility(UserRegistrationNew.objUserDetailsHeader,30);
		verifyElementPresent2(UserRegistrationNew.objUserDetailsHeader, "Ring Register Page");
		logger.info("TC_Ring_Core_38-when user click continue  on truecaller popup");
		extentLoggerPass("TC_Ring_Core_38", "TC_Ring_Core_38-To verify when user click continue  on truecaller popup");
		
		getDriver().resetApp();
		/*enablePermissions();
		click(RingLoginPage.objLoginLink, "Signup/Login link");
		explicitWaitVisibilityNonDefault(SignUP_LoginPage.continueWithGmail, 20);
		click(SignUP_LoginPage.continueWithGmail, "Continue With Gmail Account");
		if (verifyElementPresent(GmailLoginPage.addAnotherAccount, "Add Another Account")) {
			click(GmailLoginPage.addAnotherAccount, "Add Another Account");
			
			logger.info("Google account is already added in the mobile");
			extent.extentLoggerPass("TC_Ring_Core_41", "TC_Ring_Core_41-To Verify If account is already added in the mobile");
		}
		explicitWaitVisibilityNonDefault(GmailLoginPage.enterEmailID, 20);
		logger.info("TC_Ring_Core_40-To verify when user Continue with Google option");
		extent.extentLoggerPass("TC_Ring_Core_40", "TC_Ring_Core_40-To verify when user Continue with Google option");
		logger.info("TC_Ring_Core_40-To verify when user Continue with Google option");
		extent.extentLoggerPass("TC_Ring_Core_43", "TC_Ring_Core_43-To Verify if user click on add account");
		getDriver().resetApp();
		enablePermissions();
		click(RingLoginPage.objLoginLink, "Signup/Login link");
		explicitWaitVisibilityNonDefault(SignUP_LoginPage.continueWithGmail, 20);
		click(SignUP_LoginPage.continueWithFacebook, "Continue With Facebook");
		explicitWaitVisibility(FacebookLoginPage.titleFacebookPage,30);
		verifyElementPresent2(FacebookLoginPage.titleFacebookPage, "Verify FaceBook page");
		logger.info("TC_Ring_Core_45-when user Continue with Facebook option");
		extent.extentLoggerPass("TC_Ring_Core_45", "TC_Ring_Core_45-To verify when user Continue with Facebook option");
		logger.info("TC_Ring_Core_47-To verify if facebook is not logged in mobile");
		extent.extentLoggerPass("TC_Ring_Core_47", "TC_Ring_Core_47-To verify if facebook is not logged in mobile");
		Back(2);*/
		
		/*explicitWaitVisibilityNonDefault(RingLoginPage.objLoginLink, 10);
		click(RingLoginPage.objLoginLink, "Signup/Login link");
		explicitWaitVisibilityNonDefault(RingLoginPage.objLoginMobile, 10);
		click(RingLoginPage.objLoginMobile, "Continue with Mobile option");
		waitTime(2000);
		verifyElementPresent(RingLoginPage.objNoneBtn,"None of the above");
		click(RingLoginPage.objNoneBtn,"None of the above");*/
		String nMobileNumber2 = "9" + RandomIntegerGenerator(9);
		click(RingLoginPage.objMobTextField, "Mobile Number Field");
		verifyElementPresent(RingLoginPage.objNoneBtn,"None of the above");
		click(RingLoginPage.objNoneBtn,"None of the above");
		type(RingLoginPage.objMobTextField, nMobileNumber2, "Mobile Number Field");
		enterOtp("888888");
		Swipe("UP",2);
		explicitWaitVisibilityNonDefault(RingLoginPage.objReadAcceptBtn, 10);
		explicitWaitVisibilityNonDefault(RingLoginPage.ckycCheckBox, 10);
		click(RingLoginPage.ckycCheckBox, "Terms and Conditions checkbox");

		click(RingLoginPage.objReadAcceptBtn, "Read & Accept button");
		waitTime(2000);
		String kycError = getText(UserRegistrationPage.objToast);
		softAssertion.assertEquals("Please accept terms and conditions", kycError);
		logger.info("TC_Ring_Core_52-if user only checked in whatsapp notification check box and Continue with read & accept");
		extent.extentLoggerPass("TC_Ring_Core_52","TC_Ring_Core_52 To Verify if user only checked in whatsapp notification check box and Continue with read & accept");
		logger.info("TC_Ring_Core_54 To To Verify when user clicks on read and accept with unchecked CKYC consent");
		extent.extentLoggerPass("TC_Ring_Core_54","TC_Ring_Core_54 To To Verify when user clicks on read and accept with unchecked CKYC consent");

		click(RingLoginPage.ckycCheckBox, "Terms and Conditions checkbox");
		/*tapUsingCoordinates(916,1825);
		verifyElementPresentAndClick(RingLoginPage.btnOkGotIt, "CKYC Consent");
		logger.info("To Verify if user click on ckyc consent and privacy policy check box");
		extent.extentLoggerPass("TC_Ring_Core_50", "TC_Ring_Core_50-To Verify if user click on ckyc consent and privacy policy check box");*/
//		tapUsingCoordinates(477, 1840);
//		verifyElementPresentAndClick(RingLoginPage.btnOkGotIt, "Credit Bureau"); 
		//TC_50 has a dependency on device under test's size, tapping by coordinates is dependent on phone's size
		
		verifyElementPresent(RingLoginPage.objReadAcceptBtn, "RingPay permissions");
		logger.info("Ring Pay Permissions page (SMS, LOCATION & PHONE)");
		
		click(RingLoginPage.objReadAcceptBtn, "Read & Accept button");
		
		// Location Deny Test Case

		explicitWaitVisibilityNonDefault(RingLoginPage.objLocDeny, 10);
		click(RingLoginPage.objLocDeny, "Location Access option");
		click(RingLoginPage.objPhoneAccess, "Phone access option");
		click(RingLoginPage.objSMSAccess, "SMS access option");
		verifyElementPresentAndClick(RingLoginPage.objPermCancel, "Cancel Button");
		logger.info("TC_Ring_Core_55 To Verify after successful login, user should navigate to permission page");
		extent.extentLoggerPass("TC_Ring_Core_55","TC_Ring_Core_55 To Verify after successful login, user should navigate to permission page");
		getDriver().resetApp();
		
		// Contact Deny Test Case
		/*enablePermissions();
		explicitWaitVisibilityNonDefault(RingLoginPage.objLoginLink, 10);
		click(RingLoginPage.objLoginLink, "Signup/Login link");
		explicitWaitVisibilityNonDefault(RingLoginPage.objLoginMobile, 10);
		click(RingLoginPage.objLoginMobile, "Continue with Mobile option");
		waitTime(2000);
		verifyElementPresent(RingLoginPage.objNoneBtn,"None of the above");
		click(RingLoginPage.objNoneBtn,"None of the above");*/

		String nMobileNumber3 = "9" + RandomIntegerGenerator(9);
		click(RingLoginPage.objMobTextField, "Mobile Number Field");
		verifyElementPresent(RingLoginPage.objNoneBtn,"None of the above");
		click(RingLoginPage.objNoneBtn,"None of the above");
		type(RingLoginPage.objMobTextField, nMobileNumber3, "Mobile Number Field");
		enterOtp("888888");
		Swipe("UP",2);
		explicitWaitVisibilityNonDefault(RingLoginPage.objReadAcceptBtn, 10);
		click(RingLoginPage.objReadAcceptBtn, "Read & Accept button");
		explicitWaitVisibilityNonDefault(RingLoginPage.objLocAccess, 10);
		click(RingLoginPage.objLocAccess, "Location Access option");
		click(RingLoginPage.objLocDeny, "Phone access option");
		click(RingLoginPage.objLocDeny, "SMS access option");
		verifyElementPresentAndClick(RingLoginPage.objPermCancel, "Cancel Button");
		logger.info("TC_Ring_Core_57-To Verify when user refuse the contact access");
		extent.extentLoggerPass("TC_Ring_Core_57","TC_Ring_Core_57-To Verify when user refuse the contact access");
	
				// all pemission Deny Test Case
				getDriver().resetApp();
				/*enablePermissions();
				explicitWaitVisibilityNonDefault(RingLoginPage.objLoginLink, 10);
				click(RingLoginPage.objLoginLink, "Signup/Login link");
				explicitWaitVisibilityNonDefault(RingLoginPage.objLoginMobile, 10);
				click(RingLoginPage.objLoginMobile, "Continue with Mobile option");
				waitTime(2000);
				verifyElementPresent(RingLoginPage.objNoneBtn,"None of the above");
				click(RingLoginPage.objNoneBtn,"None of the above");*/
				String nMobileNumber4 = "9" + RandomIntegerGenerator(9);
				click(RingLoginPage.objMobTextField, "Mobile Number Field");
				verifyElementPresent(RingLoginPage.objNoneBtn,"None of the above");
				click(RingLoginPage.objNoneBtn,"None of the above");
				type(RingLoginPage.objMobTextField, nMobileNumber4, "Mobile Number Field");
				enterOtp("888888");
				Swipe("UP",2);
				explicitWaitVisibilityNonDefault(RingLoginPage.objReadAcceptBtn, 10);
				click(RingLoginPage.objReadAcceptBtn, "Read & Accept button");
				explicitWaitVisibilityNonDefault(RingLoginPage.objLocDeny, 10);
				click(RingLoginPage.objLocDeny, "Location Access option");
				click(RingLoginPage.objLocDeny, "Phone access option");
				click(RingLoginPage.objLocDeny, "SMS access option");
				verifyElementPresentAndClick(RingLoginPage.objPermCancel, "Cancel Button");
				logger.info("TC_Ring_Core_58-To Verify When user refuse to give permission access");
				extent.extentLoggerPass("TC_Ring_Core_58","TC_Ring_Core_58-To Verify When user refuse to give permission access");
				click(RingLoginPage.btnNoThanks, "No, Thanks Button");
				verifyElementPresentAndClick(RingLoginPage.btnYesExitApp, "Exit App YES Button");
				//verifyElementPresentAndClick(RingLoginPage.objLoginLink, "Sign Up/Login");
				logger.info("TC_Ring_Core_53-To Verify when user clicks on exit app");
				extent.extentLoggerPass("TC_Ring_Core_53","TC_Ring_Core_53-To Verify when user clicks on exit app");
				//Allow All Permission
				/*explicitWaitVisibilityNonDefault(RingLoginPage.objLoginMobile, 10);
				click(RingLoginPage.objLoginMobile, "Continue with Mobile option");
				waitTime(2000);
				verifyElementPresent(RingLoginPage.objNoneBtn,"None of the above");
				click(RingLoginPage.objNoneBtn,"None of the above");*/
				String nMobileNumber5 = "9" + RandomIntegerGenerator(9);
				click(RingLoginPage.objMobTextField, "Mobile Number Field");
				verifyElementPresent(RingLoginPage.objNoneBtn,"None of the above");
				click(RingLoginPage.objNoneBtn,"None of the above");
				type(RingLoginPage.objMobTextField, nMobileNumber5, "Mobile Number Field");
				enterOtp("888888");
				Swipe("UP",2);
				click(RingLoginPage.ckbboxWhatsapp, "CheckBox Whatsapp");
				readAndAccept();
				explicitWaitVisibilityNonDefault(UserRegistrationNew.objUserDetailsHeader, 30);
				logger.info("TC_Ring_Core_51-To Verify if user only checked in CKYC consent check box and Continue with read & accept");
				extent.extentLoggerPass("TC_Ring_Core_51","TC_Ring_Core_51-To Verify if user only checked in CKYC consent check box and Continue with \r\n"
						+ "read & accept");
				logger.info("TC_Ring_Core_59-To verify when users allow all the permission");
				extent.extentLoggerPass("TC_Ring_Core_59","TC_Ring_Core_59-To verify when users allow all the permission");			
		
	}
	
	
	
//=========================================SIM binding Flow End=====================================================///
	
//=========================================User Registration Flow Start=============================================================
	public void User_Registration_Flow() throws Exception {

		extent.HeaderChildNode("User Registration Flow Module");

		logger.info("User Registration Details Page");
		extent.extentLogger("INFO", "User Registration Details Page");
		explicitWaitVisibilityNonDefault(UserRegistrationNew.objUserDetailsHeader, 10);
		hideKeyboard();
		explicitWaitVisibilityNonDefault(UserRegistrationNew.objFirstName, 10);
		explicitWaitVisibilityNonDefault(UserRegistrationNew.objLastName, 10);
		explicitWaitVisibilityNonDefault(UserRegistrationPage.objUserDOB, 10);
		explicitWaitVisibilityNonDefault(UserRegistrationNew.objMothersName, 10);
		explicitWaitVisibilityNonDefault(UserRegistrationNew.objGenderSelect, 10);

		extent.extentLoggerPass("TC_Ring_Core_73", "TC_Ring_Core_73 - To verify the 'User Details' screen");
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");

		explicitWaitVisibilityNonDefault(UserRegistrationNew.objFirstNameError, 10);
		String firstNameErrorTxt = getText(UserRegistrationNew.objFirstNameError);
		String lastNameErrorTxt = getText(UserRegistrationNew.objLastNameError);
		String dobErrorTxt = getText(UserRegistrationNew.objDOBError);
		Swipe("Up", 1);
		explicitWaitVisibilityNonDefault(UserRegistrationNew.objGender, 10);
		String genderErrorTxt = getText(UserRegistrationNew.objGender);
		softAssertion.assertEquals("Please enter valid first name", firstNameErrorTxt);
		softAssertion.assertEquals("Please enter valid last name", lastNameErrorTxt);
		softAssertion.assertEquals("Please select Date of Birth", dobErrorTxt);
		softAssertion.assertEquals("Please select Gender", genderErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_76","TC_Ring_Core_76 - To verify the response by clicking on 'Continue' button when all required fields are empty");
		extent.extentLoggerPass("TC_Ring_Core_109","TC_Ring_Core_109 - To verify whether the user is not able to 'Continue' when the 'Gender' is not selected from the drop down");

		Aclick(UserRegistrationNew.objLastName, "Last name text field");
		type(UserRegistrationNew.objLastName, "Huss", "Last Name text field");
		hideKeyboard();
		dateOfBirth(prop.getproperty("dateOfBirthMonth"),prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthYear"));
		Swipe("up", 2);
		click(UserRegistrationNew.objGenderSelect, "Gender dropdown");
		explicitWaitVisibilityNonDefault(UserRegistrationNew.objMaleRadio, 10);
		click(UserRegistrationNew.objMaleRadio, "Male gender");

		Swipe("down", 2);
		hideKeyboard();
		explicitWaitClickable(UserRegistrationNew.objProceed, 30);
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		explicitWaitVisibilityNonDefault(UserRegistrationNew.objFirstNameError, 10);
		softAssertion.assertEquals("Please enter valid first name", firstNameErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_77","TC_Ring_Core_77 - To verify the user is not able to 'Continue' with by keeping 'First Name' field empty with all valid details");

		explicitWaitVisibilityNonDefault(UserRegistrationNew.objFirstName, 10);
		Aclick(UserRegistrationNew.objFirstName, "First name text field");
		type(UserRegistrationNew.objFirstName, generateRandomString(1), "First Name text field");
		hideKeyboard();
		explicitWaitClickable(UserRegistrationNew.objProceed, 10);
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		explicitWaitVisibilityNonDefault(UserRegistrationNew.objFirstNameError, 10);
		softAssertion.assertEquals("Please enter valid first name", firstNameErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_78","TC_Ring_Core_78 - To verify the user is not able to 'Continue' with entering only 'First Name' initial in first name field with all valid details");

		clearField(UserRegistrationNew.objFirstName, "First name text field");
		type(UserRegistrationNew.objFirstName, generateRandomString(3) + RandomIntegerGenerator(3), "First Name text field");
		hideKeyboard();
		explicitWaitClickable(UserRegistrationNew.objProceed, 10);
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		explicitWaitVisibilityNonDefault(UserRegistrationNew.objFirstNameError, 10);
		softAssertion.assertEquals("Please enter valid first name", firstNameErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_79","TC_Ring_Core_79 - To verify the user is not able to 'Continue' with alphanumeric characters entered in 'First Name' field with all valid details");

		clearField(UserRegistrationNew.objFirstName, "First name text field");
		type(UserRegistrationNew.objFirstName, generateRandomString(3) + RandomIntegerGenerator(3) + "+}{", "First Name text field");
		hideKeyboard();
		explicitWaitClickable(UserRegistrationNew.objProceed, 10);
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		explicitWaitVisibilityNonDefault(UserRegistrationNew.objFirstNameError, 10);
		softAssertion.assertEquals("Please enter valid first name", firstNameErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_80","TC_Ring_Core_80 - To verify the user is not able to 'Continue' with special characters entered in 'First Name' field with all valid details");

		clearField(UserRegistrationNew.objFirstName, "First name text field");
		type(UserRegistrationNew.objFirstName, "  ", "First Name text field");
		hideKeyboard();
		explicitWaitClickable(UserRegistrationNew.objProceed, 10);
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		explicitWaitVisibilityNonDefault(UserRegistrationNew.objFirstNameError, 10);
		softAssertion.assertEquals("Please enter valid first name", firstNameErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_81","TC_Ring_Core_81 - To verify the user is not able to 'Continue' with <Space> entered in 'First Name' field with all valid details");

		clearField(UserRegistrationNew.objFirstName, "First name text field");
		type(UserRegistrationNew.objFirstName, generateRandomString(5), "First Name text field");
		hideKeyboard();

		Aclick(UserRegistrationNew.objLastName, "Last Name text field");
		clearField(UserRegistrationNew.objLastName, "Last Name text field");
		hideKeyboard();
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		explicitWaitVisibilityNonDefault(UserRegistrationNew.objLastNameError, 10);
		softAssertion.assertEquals("Please enter valid last name", lastNameErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_84","TC_Ring_Core_84 - To verify the user is not able to 'Continue' with by keeping 'Last Name' field empty with all valid details");

		clearField(UserRegistrationNew.objLastName, "Last Name text field");
		type(UserRegistrationNew.objLastName, generateRandomString(1), "Last Name text field");
		hideKeyboard();
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		explicitWaitVisibilityNonDefault(UserRegistrationNew.objLastNameError, 10);
		softAssertion.assertEquals("Please enter valid last name", lastNameErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_85","TC_Ring_Core_85 - To verify the user is not able to 'Continue' with entering only 'Last Name' initial in Last name field with all valid details");

		clearField(UserRegistrationNew.objLastName, "Last Name text field");
		type(UserRegistrationNew.objLastName, generateRandomString(3) + RandomIntegerGenerator(3), "Last Name text field");
		hideKeyboard();
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		explicitWaitVisibilityNonDefault(UserRegistrationNew.objLastNameError, 10);
		softAssertion.assertEquals("Please enter valid last name", lastNameErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_86","TC_Ring_Core_86 - To verify the user is not able to 'Continue' with alphanumeric characters entered in 'Last Name' field with all valid details");

		clearField(UserRegistrationNew.objLastName, "Last Name text field");
		type(UserRegistrationNew.objLastName, generateRandomString(3) + RandomIntegerGenerator(3)+"(((","Last Name text field");
		hideKeyboard();
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		explicitWaitVisibilityNonDefault(UserRegistrationNew.objLastNameError, 10);
		softAssertion.assertEquals("Please enter valid last name", lastNameErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_87","TC_Ring_Core_87 - To verify the user is not able to 'Continue' with special characters entered in 'Last Name' field with all valid details");

		clearField(UserRegistrationNew.objLastName, "Last Name text field");
		type(UserRegistrationNew.objLastName, "  ", "Last Name text field");
		hideKeyboard();
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		explicitWaitVisibilityNonDefault(UserRegistrationNew.objLastNameError, 10);
		softAssertion.assertEquals("Please enter valid last name", lastNameErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_88","TC_Ring_Core_88 - To verify the user is not able to 'Continue' with <Space> entered in 'Last  Name' field with all valid details");

		String duplicateString = generateRandomString(4);
		clearField(UserRegistrationNew.objFirstName, "First Name text field");
		Aclick(UserRegistrationNew.objFirstName, "First name text field");
		type(UserRegistrationNew.objFirstName, duplicateString, "First Name text field");
		Aclick(UserRegistrationNew.objLastName, "Last name text field");
		type(UserRegistrationNew.objLastName, duplicateString, "Last name text field");
		explicitWaitVisibilityNonDefault(UserRegistrationNew.objMothersName, 10);
		click(UserRegistrationNew.objMothersName, "Mother's Name Field");
		type(UserRegistrationNew.objMothersName, generateRandomString(3), "Mother's Name field");
		hideKeyboard();
		click(UserRegistrationNew.objUserEmail, "Email text field");
		Aclick(UserRegistrationNew.objNoneOfAbove, "None of the above button");
		type(UserRegistrationNew.objUserEmail, generateRandomString(4) + "@gmail.com", "Email text field");
		hideKeyboard();
		explicitWaitClickable(UserRegistrationNew.objProceed,30);
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		waitTime(3000);
		String toast_85 = getText(UserRegistrationNew.objToast);
		System.out.println(toast_85);
		logger.info(toast_85);
		extent.extentLogger("Toast Message", toast_85);
		softAssertion.assertEquals("First and Last name should be different", toast_85);
		extent.extentLoggerPass("TC_Ring_Core_90","TC_Ring_Core_90 - To verify the user is not able to 'Continue' with entering same 'First Name' and  'Last Name'");

		clearField(UserRegistrationNew.objFirstName, "First Name text field");
		clearField(UserRegistrationNew.objLastName, "Last Name text field");
		Aclick(UserRegistrationNew.objFirstName, "First name text field");
		type(UserRegistrationNew.objFirstName, duplicateString + " " + duplicateString, "First Name text field");
		waitTime(3000);
		Aclick(UserRegistrationNew.objLastName, "Last name text field");
		type(UserRegistrationNew.objLastName, generateRandomString(4), "First Name text field");

		explicitWaitVisibilityNonDefault(UserRegistrationNew.objMothersName, 10);
		click(UserRegistrationNew.objMothersName, "Mother's Name Field");
		type(UserRegistrationNew.objMothersName, generateRandomString(3), "Mother's Name field");

		explicitWaitClickable(UserRegistrationNew.objProceed,20);
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		waitTime(3000);
		String toast_86 = getText(UserRegistrationNew.objToast);
		System.out.println(toast_86);
		logger.info(toast_86);
		extent.extentLogger("Toast Message", toast_86);
		softAssertion.assertEquals("Enter valid First Name ", toast_86);
		extent.extentLoggerPass("TC_Ring_Core_91","TC_Ring_Core_91 - To verify the user is not able to 'Continue' with entering same 'First Name' repeatedly in same field");

		clearField(UserRegistrationNew.objFirstName, "First Name text field");
		clearField(UserRegistrationNew.objLastName, "Last Name text field");
		Aclick(UserRegistrationNew.objFirstName, "Last name text field");
		type(UserRegistrationNew.objFirstName, "shak", "First Name text field");
		Aclick(UserRegistrationNew.objLastName, "Last name text field");
		type(UserRegistrationNew.objLastName, duplicateString + " " + duplicateString, "First Name text field");
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		waitTime(3000);
		String toast_87 = getText(UserRegistrationNew.objToast);
		System.out.println(toast_87);
		logger.info(toast_87);
		extent.extentLogger("Toast Message", toast_87);
		softAssertion.assertEquals("Enter valid Last Name ", toast_87);
		extent.extentLoggerPass("TC_Ring_Core_92","TC_Ring_Core_92 - To verify the user is not able to 'Continue' with entering same 'Last Name' repeatedly in same field");

		explicitWaitVisibilityNonDefault(UserRegistrationNew.objMothersName, 10);
		extent.extentLoggerPass("TC_Ring_Core_93","TC_Ring_Core_93 - To verify new field is added after Last Name field");

		clearField(UserRegistrationNew.objFirstName, "First Name text field");
		clearField(UserRegistrationNew.objLastName, "Last Name text field");
		hideKeyboard();
		clearField(UserRegistrationNew.objMothersName, "Mother's name text field");
		Aclick(UserRegistrationNew.objFirstName, "First name text field");
		type(UserRegistrationNew.objFirstName, generateRandomString(4), "First Name text field");
		Aclick(UserRegistrationNew.objLastName, "Last name text field");
		type(UserRegistrationNew.objLastName, generateRandomString(4), "First Name text field");
		Aclick(UserRegistrationNew.objProceed, "Register Button");
		explicitWaitVisibilityNonDefault(UserRegistrationNew.objMotherError, 10);
		extent.extentLoggerPass("TC_Ring_Core_94","TC_Ring_Core_94 - To verify the user is not able to 'Proceed' with by keeping 'Mother's Name' field empty with all valid details");

		Aclick(UserRegistrationNew.objMothersName, "Mother's name text field");
		type(UserRegistrationNew.objMothersName, generateRandomString(1), "Mother's name text field");
		Aclick(UserRegistrationNew.objProceed, "Register Button");
		explicitWaitVisibilityNonDefault(UserRegistrationNew.objMotherError, 10);
		extent.extentLoggerPass("TC_Ring_Core_95","TC_Ring_Core_95 - To verify the user is not able to 'Proceed' with entering only 'Mother's Name' initial in Mother's Name field with all valid details");

		clearField(UserRegistrationNew.objMothersName, "Mother's name text field");
		Aclick(UserRegistrationNew.objMothersName, "Mother's name text field");
		type(UserRegistrationNew.objMothersName, generateRandomString(3) + RandomIntegerGenerator(3), "Mother's name text field");
		Aclick(UserRegistrationNew.objProceed, "Register Button");
		explicitWaitVisibilityNonDefault(UserRegistrationNew.objMotherError, 10);
		extent.extentLoggerPass("TC_Ring_Core_96","TC_Ring_Core_96 - To verify the user is not able to 'Proceed' with alphanumeric characters entered in 'Mother's Name' field with all valid details");

		clearField(UserRegistrationNew.objMothersName, "Mother's name text field");
		Aclick(UserRegistrationNew.objMothersName, "Mother's name text field");
		type(UserRegistrationNew.objMothersName, generateRandomString(3) + RandomIntegerGenerator(3)+"}|{", "Mother's name text field");
		Aclick(UserRegistrationNew.objProceed, "Register Button");
		explicitWaitVisibilityNonDefault(UserRegistrationNew.objMotherError, 10);
		extent.extentLoggerPass("TC_Ring_Core_97",
				"TC_Ring_Core_97 - To verify the user is not able to 'Proceed' with special characters entered in 'Mother's Name' field with all valid details");

		clearField(UserRegistrationNew.objMothersName, "Mother's name text field");
		Aclick(UserRegistrationNew.objMothersName, "Mother's name text field");
		type(UserRegistrationNew.objMothersName, " ", "Mother's name text field");
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		explicitWaitVisibilityNonDefault(UserRegistrationNew.objMotherError, 10);
		extent.extentLoggerPass("TC_Ring_Core_98",
				"TC_Ring_Core_98 - To verify the user is not able to 'Proceed' with <Space> entered in 'Mother's Name' field with all valid details");

		clearField(UserRegistrationNew.objFirstName, "First Name text field");
		clearField(UserRegistrationNew.objLastName, "Last Name text field");
		hideKeyboard();
		clearField(UserRegistrationNew.objMothersName, "Mother's name text field");

		clearField(UserRegistrationNew.objMothersName, "Mother's name text field");
		Aclick(UserRegistrationNew.objMothersName, "Mother's name text field");
		type(UserRegistrationNew.objMothersName, generateRandomString(4), "Mother's name text field");
		clearField(UserRegistrationNew.objLastName, "Last Name text field");
		Aclick(UserRegistrationNew.objLastName, "Last name text field");
		type(UserRegistrationNew.objLastName, generateRandomString(4), "last Name text field");
		Aclick(UserRegistrationNew.objFirstName, "First Name text field");
		type(UserRegistrationNew.objLastName, generateRandomString(4), "First Name text field");
		hideKeyboard();
		Aclick(UserRegistrationNew.objUserEmail, "Email text field");
		clearField(UserRegistrationNew.objUserEmail, "Email text field");
		hideKeyboard();
		explicitWaitVisibility(UserRegistrationNew.objProceed,30);
		Aclick(UserRegistrationNew.objProceed, "proceed Button");
		explicitWaitVisibilityNonDefault(UserRegistrationNew.objEmailError, 10);
		softAssertion.assertEquals("Please enter valid email address", getText(UserRegistrationNew.objEmailError));
		extent.extentLoggerPass("TC_Ring_Core_103",
				"TC_Ring_Core_103 - To verify the user is not able to 'Continue' with by keeping 'Email Address' field empty with all valid details");

		Aclick(UserRegistrationNew.objUserEmail, "Email text field");
		type(UserRegistrationNew.objUserEmail, generateRandomString(3) + "}}("+"@gmail.com", "Email text field");
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		hideKeyboard();
		explicitWaitVisibilityNonDefault(UserRegistrationNew.objEmailError1, 10);
		String emailErrorTxt = getText(UserRegistrationNew.objEmailError1);
		softAssertion.assertEquals("Please enter valid email id", emailErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_104",
				"TC_Ring_Core_104 - To verify the user is not able to 'Continue' with by entering 'Email Address' in invalid format OR with special characters with all valid details");

		clearField(UserRegistrationNew.objUserEmail, "Email text field");
		type(UserRegistrationNew.objUserEmail, generateRandomString(5) +" @gmail.com", "Email text field");
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		explicitWaitVisibilityNonDefault(UserRegistrationNew.objEmailError1, 10);
		softAssertion.assertEquals("Please enter valid email id", emailErrorTxt);
		extent.extentLoggerPass("TC_Ring_Core_105",
				"TC_Ring_Core_105 - To verify the user is not able to 'Continue' with <Space> entered in 'Email Address' field with all valid details");

		hideKeyboard();
		clearField(UserRegistrationNew.objUserEmail, "Email text field");
		String email = RandomStringGenerator(4) + "@example.com";
		type(UserRegistrationNew.objUserEmail, email, "Email text field");
		setWifiConnectionToONOFF("Off");
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		explicitWaitVisibilityNonDefault(UserRegistrationNew.objInternetError, 10);
		String intError = getText(UserRegistrationNew.objInternetError);
		softAssertion.assertEquals(" Check your connection & try again ", intError);
		extent.extentLoggerPass("TC_Ring_Core_96",
				"TC_Ring_Core_96 - To verify the user is getting 'Check internet connection' screen after clicking on 'Continue' button when the Device internet connection is down");

		setWifiConnectionToONOFF("On");
		explicitWaitClickable(UserRegistrationNew.objGotItBtn, 10);
		Aclick(UserRegistrationNew.objGotItBtn, "Okay Got It button");
		explicitWaitVisibilityNonDefault(UserRegistrationNew.objUserDetailsHeader, 10);
		extent.extentLoggerPass("TC_Ring_Core_97",
				"TC_Ring_Core_97 - To verify the user is able to 'Continue' after 'Okay Got It' once the device internet connection Up");

		hideKeyboard();
		userDetails();
		dateOfBirth("Oct", "12", "1995");
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		instaNewCommunicationAddress();
		waitTime(3000);

		verifyElementPresent(RingLoginPage.objCongratsPage, getText(RingLoginPage.objCongratsPage));
		verifyElementPresent(RingLoginPage.objApprovedRingLimit,
				getText(RingLoginPage.objApprovedRingLimit) + getText(RingLoginPage.objApprovedRinglimitAmt));
		verifyElementPresent(RingLoginPage.objIAcceptCheckBox,
				"I accept the Ring’s Terms & Conditions & IT Act 2000 checkbox");
		verifyElementPresent(RingLoginPage.objAcceptOfferBtn, getText(RingLoginPage.objAcceptOfferBtn));
		verifyElementPresentAndClick(RingLoginPage.objIAcceptCheckBox,
				"I accept the Ring’s Terms & Conditions & IT Act 2000 checkbox");
		verifyElementPresentAndClick(RingLoginPage.objAcceptOfferBtn, getText(RingLoginPage.objAcceptOfferBtn));

		explicitWaitVisibility(UserRegistrationPage.objSetPin,20);
		if (verifyElementPresent(UserRegistrationPage.objSetPin, "Set Pin Page")) {
			logger.info("Navigated to MPIN Page");
			extent.extentLoggerPass("MPIN Page", "Navigated to MPIN Page");
			verifyElementPresent(TermsAndConditionPage.objSetPin, getText(TermsAndConditionPage.objSetPin));
			softAssertion.assertEquals(getText(TermsAndConditionPage.objSetPin), "Set Pin");
			instaLoanSetPin(prop.getproperty("InstaLoanMPIN"), prop.getproperty("InstaLoanMPIN"));
		}

		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		String sHome = getText(HomePage.objHome);
		softAssertion.assertEquals(sHome, "Home");
		ringPayLogout();

		extent.extentLoggerPass("TC_Ring_Core_83",
				"TC_Ring_Core_83 - To verify the user is able to 'Continue' with entering valid 'First Name' with all valid details");
		extent.extentLoggerPass("TC_Ring_Core_89",
				"TC_Ring_Core_89 - To verify the user is able to 'Continue' with entering valid 'Last Name' with all valid details");
		extent.extentLoggerPass("TC_Ring_Core_99",
				"TC_Ring_Core_99 - To verify the user is able to 'Proceed' with entering valid 'Mother's Name' with all valid details");
		extent.extentLoggerPass("TC_Ring_Core_106",
				"TC_Ring_Core_106 - To verify the user is able to 'Continue' with entering valid 'Email Address' with all valid details");
		extent.extentLoggerPass("TC_Ring_Core_110",
				"TC_Ring_Core_110 - To verify whether the user is able to 'Continue' when the 'Gender' is selected from the drop down");
		extent.extentLoggerPass("TC_Ring_Core_115",
				"TC_Ring_Core_115 - To verify when user successfully enters all valid details and clicks on continue button");

	}

//============================================User Registration Flow End============================================================================================
//===============================================PromoCode Flow Start===============================================================================================
	public void promoCodeFlowModule() throws Exception {
		extent.HeaderChildNode("PromoCode Flow");
		getDriver().resetApp();

		if (verifyElementPresent(RingLoginPage.objCamPermPopUp, "Enable permissions button")) {
			enablePermissions();
		}
		scannQRSwitch();
		User_Play_Store_Flow(prop.getproperty("PaymentPage"), prop.getproperty("Permission"));
		userDetails();
		dateOfBirth("Feb", "10", "1996");
		click(UserRegistrationNew.objProceed, "Proceed Button");
		instaNewCommunicationAddress();
		explicitWaitVisibilityNonDefault(PromoCodeOfferPage.objMerchantOfferHeader,30);
		verifyElementPresent(PromoCodeOfferPage.objMerchantOfferHeader,getText(PromoCodeOfferPage.objMerchantOfferHeader));
		softAssertion.assertEquals(getText(PromoCodeOfferPage.objMerchantOfferHeader), "Woohoo, Sunil!");
		verifyElementPresent(PromoCodeOfferPage.objUnlockRingLimit, getText(PromoCodeOfferPage.objUnlockRingLimit)
				+ " of " + getText(PromoCodeOfferPage.objApprovedRinglimitAmt));
		verifyElementPresent(PromoCodeOfferPage.objPayingType, getText(PromoCodeOfferPage.objPayingType));
		verifyElementPresent(PromoCodeOfferPage.objUPIId, getText(PromoCodeOfferPage.objUPIId));
		verifyElementPresent(PromoCodeOfferPage.objMerchantPayAmt, getText(PromoCodeOfferPage.objMerchantPayAmt));
		verifyElementPresent(PromoCodeOfferPage.objRBIMsg,
				getText(PromoCodeOfferPage.objRBIMsg) + getText(PromoCodeOfferPage.objRBIMg2));
		verifyElementPresentAndClick(RingLoginPage.objIAcceptCheckBox,
				"I accept the Ring’s Terms & Conditions & IT Act 2000 checkbox");
		verifyElementPresentAndClick(TermsAndConditionPage.objAcceptAndPayBtn,
				getText(TermsAndConditionPage.objAcceptText));

		instaLoanSetPin(prop.getproperty("InstaLoanMPIN"), prop.getproperty("InstaLoanMPIN"));
		waitTime(25000);
		explicitWaitVisibilityNonDefault(PromoCodeOfferPage.objGoToHomePage,30);
		verifyElementPresentAndClick(PromoCodeOfferPage.objGoToHomePage, getText(PromoCodeOfferPage.objGoToHomePage));
		explicitWaitVisibilityNonDefault(RingUserDetailPage.objCrossBtn,30);
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		if (verifyElementPresent2(UserRegistrationPage.objNoBtn, "No Button")) {
			verifyElementPresentAndClick(UserRegistrationPage.objNoBtn, getText(UserRegistrationPage.objNoBtn));
		}
		String sHome = getText(HomePage.objHome);
		softAssertion.assertEquals(sHome, "Home");
		ringPayLogout();
		logger.info("TC_Ring_Core_71 - To verify  user Scans the Promo Code QR");
		extent.extentLoggerPass("TC_Ring_Core_71", "TC_Ring_Core_71-To verify  user Scans the Promo Code QR");

		logger.info("TC_Ring_Core_72 - Repeat  TC_Ring_Core_4 to TC_Ring_Core_59 steps Number  for login flow");
		extent.extentLoggerPass("TC_Ring_Core_72","TC_Ring_Core_72-Repeat  TC_Ring_Core_4 to TC_Ring_Core_59 steps Number  for login flow");
	}

//===============================================PromoCode Flow end================================================================
	//===============================================Offer Module Start================================================================
		public void offerScreenModule() throws Exception {
			extent.HeaderChildNode("RingPay Offer Screen");
			getDriver().resetApp();
			merchantOfferPageValidation();
			playStorePromocodeOfferPageValidation();

			logger.info("TC_Ring_Core_175 - To verify the 'Offer' screen on this page Accept and Pay button is displayed(Merchant flow)");
			extent.extentLoggerPass("TC_Ring_Core_175","TC_Ring_Core_175 - To verify the 'Offer' screen on this page Accept and Pay button is displayed(Merchant flow)");

			logger.info("TC_Ring_Core_176 - To verify the 'Offer' screen on this page Accept offer button is displayed(Playstore Promocode flow) ");
			extent.extentLoggerPass("TC_Ring_Core_176","TC_Ring_Core_175 - To verify the 'Offer' screen on this page Accept offer button is displayed(Playstore Promocode flow) ");

			logger.info("TC_Ring_Core_177 - To verify the response by Clicking on 'Fee Details' link from limit approval banner");
			extent.extentLoggerPass("TC_Ring_Core_177","TC_Ring_Core_177 - To verify the response by Clicking on 'Fee Details' link from limit approval banner");

			logger.info("TC_Ring_Core_178 - To verify the response by clicking on t&c link");
			extent.extentLoggerPass("TC_Ring_Core_178", "TC_Ring_Core_178 -To verify the response by clicking on t&c link");

			logger.info("TC_Ring_Core_179 - To verify the user is not able to proceed to pay amount without selecting t&c conditions checkbox by clicking on 'Accept & Pay' button");
			extent.extentLoggerPass("TC_Ring_Core_179","TC_Ring_Core_179 - To verify the user is not able to proceed to pay amount without selecting t&c conditions checkbox by clicking on 'Accept & Pay' button");

			logger.info("TC_Ring_Core_180 - To verify the user is able to pay amount by selecting t&c checkbox and clicking on 'Accept & Pay' button");
			extent.extentLoggerPass("TC_Ring_Core_181","TC_Ring_Core_181 - To verify the user is able to pay amount by selecting t&c checkbox and clicking on 'Accept & Pay' button");

			logger.info("TC_Ring_Core_181 - To verify the user is getting check internet connection screen after clicking on Accept and Pay button when the device internet connection is down");
			extent.extentLoggerPass("TC_Ring_Core_181","TC_Ring_Core_181 - To verify the user is getting check internet connection screen after clicking on Accept and Pay button when the device internet connection is down");

			logger.info("TC_Ring_Core_182 - To verify the user is able to make payment by clicking on Retry/Refresh and by clicking on Accept & Pay button once the device internet connection is Up");
			extent.extentLoggerPass("TC_Ring_Core_182","TC_Ring_Core_182 - To verify the user is able to make payment by clicking on Retry/Refresh and by clicking on Accept & Pay button once the device internet connection is Up");

			logger.info("TC_Ring_Core_183 - To verify the user turns off location on Accept and Pay screen");
			extent.extentLoggerPass("TC_Ring_Core_183", "TC_Ring_Core_183 - To verify the user turns off location on Accept and Pay screen");

			logger.info("TC_Ring_Core_184 - To  Verify when user selects allow option on location popup");
			extent.extentLoggerPass("TC_Ring_Core_184", "TC_Ring_Core_184 - To  Verify when user selects allow option on location popup");

			logger.info("TC_Ring_Core_185 - To verify when user select not now option on location popup");
			extent.extentLoggerPass("TC_Ring_Core_185", "TC_Ring_Core_185 - To verify when user select not now option on location popup");

			logger.info("TC_Ring_Core_186 - To verify when user kill or refresh app on accept and pay page");
			extent.extentLoggerPass("TC_Ring_Core_186", "TC_Ring_Core_186 - To verify when user kill or refresh app on accept and pay page");

			logger.info("TC_Ring_Core_187 - To verify the user turns off location on Accept offer screen");
			extent.extentLoggerPass("TC_Ring_Core_187", "TC_Ring_Core_187 - To verify the user turns off location on Accept offer screen");

			logger.info("TC_Ring_Core_188 - To Verify when user click on Accept offer/Accept and pay button");
			extent.extentLoggerPass("TC_Ring_Core_188", "TC_Ring_Core_188 - To Verify when user click on Accept offer/Accept and pay button");

			softAssertion.assertAll();
		}

	//============================================Offer Screen Merchant Flow=====================================================================================
		public void merchantOfferPageValidation() throws Exception {
			getDriver().resetApp();
			enablePermissions();
			verifyElementPresent(RingLoginPage.objScanQRPage, getText(RingLoginPage.objScanQRPage));

			scannQRSwitch();
			
			click(RingLoginPage.objMakePaymentLetsRingItBtn, getText(RingLoginPage.objMakePaymentLetsRingItBtn));
			click(RingPayMerchantFlowPage.objAmountTextField, "Enter Amount Field");
			type1(RingPayMerchantFlowPage.objAmountTextField, prop.getproperty("Amount_Merchant"), "Amount Field");
			verifyElementPresentAndClick(RingLoginPage.objMakePaymentPageProceedBtn,getText(RingLoginPage.objMakePaymentPageProceedBtn));
			verifyIsElementDisplayed(RingPayMerchantFlowPage.objLoginPageHeader,getTextVal(RingPayMerchantFlowPage.objLoginPageHeader, "Page"));
			softAssertion.assertEquals(getText(RingPayMerchantFlowPage.objLoginPageHeader), "Sign up/Login");
			logger.info("User redirected to Signup/Login Screen");
			extent.extentLogger("login", "User redirected to Signup/Login Screen");
			
			loginMobile();

			mobileNoValidation1("9" + RandomIntegerGenerator(9));
			enterOtp(prop.getproperty("OTP"));
			readAndAccept();
			userDetails();
			dateOfBirth("Feb", "10", "1996");
			verifyElementPresentAndClick(RingUserDetailPage.objRegisterBtn, "Proceed Button");
			instaNewCommunicationAddress();
			explicitWaitVisibility(PromoCodeOfferPage.objMerchantOfferHeader,30);
			verifyElementPresent(PromoCodeOfferPage.objMerchantOfferHeader,getText(PromoCodeOfferPage.objMerchantOfferHeader));
			softAssertion.assertEquals(getText(PromoCodeOfferPage.objMerchantOfferHeader), "Woohoo, Sunil!");
			verifyElementPresent(PromoCodeOfferPage.objUnlockRingLimit, getText(PromoCodeOfferPage.objUnlockRingLimit)
					+ " of " + getText(PromoCodeOfferPage.objApprovedRinglimitAmt));
			verifyElementPresent(PromoCodeOfferPage.objPayingType, getText(PromoCodeOfferPage.objPayingType));
			verifyElementPresent(PromoCodeOfferPage.objUPIId, getText(PromoCodeOfferPage.objUPIId));
			verifyElementPresent(PromoCodeOfferPage.objMerchantPayAmt, getText(PromoCodeOfferPage.objMerchantPayAmt));
			verifyElementPresent(PromoCodeOfferPage.objRBIMsg,
					getText(PromoCodeOfferPage.objRBIMsg) + getText(PromoCodeOfferPage.objRBIMsg));
			verifyElementPresent(RingLoginPage.objIAcceptCheckBox,
					"I accept the Ring’s Terms & Conditions & IT Act 2000 checkbox");
			verifyElementPresentAndClick(TermsAndConditionPage.objAcceptAndPayBtn,
					getText(TermsAndConditionPage.objAcceptText));

			verifyElementPresentAndClick(PromoCodeOfferPage.objFeeDetail, getText(PromoCodeOfferPage.objFeeDetail));
			verifyElementPresent(PromoCodeOfferPage.objFeeDetailPage, getText(PromoCodeOfferPage.objFeeDetailPage));
			click(PromoCodeOfferPage.objFeeDetailsCrossBtn, "Cross Button");

			verifyElementPresent(PromoCodeOfferPage.objTermsAndCondition,getTextVal(PromoCodeOfferPage.objTermsAndCondition, "Text"));
			/*tapUsingCoordinates(723, 1918);
			waitTime(5000);
			verifyElementPresent(TermsAndConditionPage.objTAndCondition,getTextVal(TermsAndConditionPage.objTAndCondition, "Text"));
			Back(1);*/
			
			//Commented this test case as separate xPath is not present for terms and conditions and key fact statement and also not feasible using cooridinates as they are dependent on the device's dimensions

			verifyElementPresentAndClick(TermsAndConditionPage.objAcceptAndPayBtn,getText(TermsAndConditionPage.objAcceptText));
			logger.info(getText(PromoCodeOfferPage.objToastMsg));
			extent.extentLoggerPass("Toast Msg", getText(PromoCodeOfferPage.objToastMsg));

			verifyElementPresentAndClick(RingLoginPage.objIAcceptCheckBox,"I accept the Ring’s Terms & Conditions & IT Act 2000 checkbox");
			setWifiConnectionToONOFF("Off");
			verifyElementPresentAndClick(TermsAndConditionPage.objAcceptAndPayBtn,getText(TermsAndConditionPage.objAcceptText));

			explicitWaitVisibility(PromoCodeOfferPage.objCheckInternetConnectionPage,30);
			verifyElementPresent(PromoCodeOfferPage.objCheckInternetConnectionPage,getText(PromoCodeOfferPage.objCheckInternetConnectionPage));
			setWifiConnectionToONOFF("On");
			click(TermsAndConditionPage.objOkBtn, "Button Okay, Got It!");
			
			verifyElementPresentAndClick(RingLoginPage.objIAcceptCheckBox,"I accept the Ring’s Terms & Conditions & IT Act 2000 checkbox");
			setLocationConnectionToONOFF("off");
			verifyElementPresentAndClick(TermsAndConditionPage.objAcceptAndPayBtn,getText(TermsAndConditionPage.objAcceptText));
			verifyElementPresent(PromoCodeOfferPage.objLocationPopup, getText(PromoCodeOfferPage.objLocationPopup));
			verifyElementPresent(PromoCodeOfferPage.objLocationAllowBtn, getText(PromoCodeOfferPage.objLocationAllowBtn));
			verifyElementPresent(PromoCodeOfferPage.objLocationNotNowBtn, getText(PromoCodeOfferPage.objLocationNotNowBtn));

			verifyElementPresentAndClick(PromoCodeOfferPage.objLocationAllowBtn,getText(PromoCodeOfferPage.objLocationAllowBtn));
			verifyElementPresentAndClick(PromoCodeOfferPage.objLocationOkBtn, "OK Button");
			verifyElementPresent(PromoCodeOfferPage.objMerchantOfferHeader,getText(PromoCodeOfferPage.objMerchantOfferHeader));
			softAssertion.assertEquals(getText(PromoCodeOfferPage.objMerchantOfferHeader), "Woohoo, Sunil!");

			setLocationConnectionToONOFF("off");
			verifyElementPresentAndClick(TermsAndConditionPage.objAcceptAndPayBtn,getText(TermsAndConditionPage.objAcceptText));
			verifyElementPresent(PromoCodeOfferPage.objLocationPopup, getText(PromoCodeOfferPage.objLocationPopup));
			verifyElementPresentAndClick(PromoCodeOfferPage.objLocationNotNowBtn,getText(PromoCodeOfferPage.objLocationNotNowBtn));

			setLocationConnectionToONOFF("on");
			waitTime(5000);
			getDriver().runAppInBackground(Duration.ofSeconds(3));
			waitTime(5000);
			setLocationConnectionToONOFF("Off");
			verifyElementPresentAndClick(TermsAndConditionPage.objAcceptAndPayBtn,getText(TermsAndConditionPage.objAcceptText));
			verifyElementPresentAndClick(PromoCodeOfferPage.objLocationNotNowBtn,getText(PromoCodeOfferPage.objLocationNotNowBtn));
			verifyElementPresentAndClick(PromoCodeOfferPage.objEnableLocBtn, getText(PromoCodeOfferPage.objEnableLocation));
			setLocationConnectionToONOFF("on");
			

			getDriver().resetApp();
			if (verifyElementPresent(RingLoginPage.objCamPermPopUp, "Enable permissions button")) {
				enablePermissions();
			}
			verifyElementPresent(RingLoginPage.objScanQRPage, getText(RingLoginPage.objScanQRPage));

			scannQRSwitch();
			
			click(RingLoginPage.objMakePaymentLetsRingItBtn, getText(RingLoginPage.objMakePaymentLetsRingItBtn));
			click(RingPayMerchantFlowPage.objAmountTextField, "Enter Amount Field");
			type1(RingPayMerchantFlowPage.objAmountTextField, "101", "Amount Field");
			verifyElementPresentAndClick(RingLoginPage.objMakePaymentPageProceedBtn,getText(RingLoginPage.objMakePaymentPageProceedBtn));
			
			loginMobile();

			mobileNoValidation1("9" + RandomIntegerGenerator(9));
			enterOtp(prop.getproperty("OTP"));
			readAndAccept();
			waitTime(3000);
			userDetails();
			dateOfBirth("Feb", "10", "1994");
			verifyElementPresentAndClick(RingUserDetailPage.objRegisterBtn, "Proceed Button");
			instaNewCommunicationAddress();
			waitTime(5000);
			verifyElementPresent(PromoCodeOfferPage.objMerchantOfferHeader,getText(PromoCodeOfferPage.objMerchantOfferHeader));
			

			verifyElementPresentAndClick(RingLoginPage.objIAcceptCheckBox,"I accept the Ring’s Terms & Conditions & IT Act 2000 checkbox");
			verifyElementPresentAndClick(TermsAndConditionPage.objAcceptAndPayBtn,getText(TermsAndConditionPage.objAcceptText));
			waitTime(5000);
			verifyElementPresent(TermsAndConditionPage.objSetPin, getText(TermsAndConditionPage.objSetPin));
			softAssertion.assertEquals(getText(TermsAndConditionPage.objSetPin), "Set Pin");
			instaLoanSetPin(prop.getproperty("InstaLoanMPIN"), prop.getproperty("InstaLoanMPIN"));
			explicitWaitVisibilityNonDefault(PromoCodeOfferPage.objGoToHomePage,30);
			verifyElementPresentAndClick(PromoCodeOfferPage.objGoToHomePage, getText(PromoCodeOfferPage.objGoToHomePage));

			explicitWaitVisibility(RingUserDetailPage.objCrossBtn,30);
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			if (verifyElementPresent2(UserRegistrationPage.objNoBtn, "No Button")) {
				verifyElementPresentAndClick(UserRegistrationPage.objNoBtn, getText(UserRegistrationPage.objNoBtn));
				String sHome = getText(HomePage.objHome);
				softAssertion.assertEquals(sHome, "Home");
				ringPayLogout();
			}
			else {
				logger.info("Transaction has failed and that is why are you enjoying popup is not displayed");
				String sHome = getText(HomePage.objHome);
				softAssertion.assertEquals(sHome, "Home");
				ringPayLogout();
			}

		}

	//===============================================Play Store Offer====================================================================	
		public void playStorePromocodeOfferPageValidation() throws Exception {

			getDriver().resetApp();

			enablePermissions();

			verifyElementPresentAndClick(RingLoginPage.objSignUpLoginBt, getText(RingLoginPage.objSignUpLoginBt));
			softAssertion.assertEquals(getText(RingPayMerchantFlowPage.objLoginPageHeader), "Sign up/Login");
			logger.info("User redirected to Signup/Login Screen");
			extent.extentLogger("login", "User redirected to Signup/Login Screen");
		
			loginMobile();

			mobileNoValidation1("9" + RandomIntegerGenerator(9));
			enterOtp(prop.getproperty("OTP"));
			readAndAccept();
			userDetails();
			dateOfBirth("Feb", "10", "1996");
			verifyElementPresentAndClick(RingUserDetailPage.objRegisterBtn, "Proceed Button");
			instaNewCommunicationAddress();
			explicitWaitVisibility(RingLoginPage.objCongratsPage,30);

			verifyElementPresent(RingLoginPage.objCongratsPage, getText(RingLoginPage.objCongratsPage));
			verifyElementPresent(RingLoginPage.objApprovedRingLimit,getText(RingLoginPage.objApprovedRingLimit) + getText(RingLoginPage.objApprovedRinglimitAmt));
			verifyElementPresent(RingLoginPage.objIAcceptCheckBox,"I accept the Ring’s Terms & Conditions & IT Act 2000 checkbox");
			verifyElementPresent(RingLoginPage.objAcceptOfferBtn, getText(RingLoginPage.objAcceptOfferBtn));
			verifyElementPresentAndClick(RingLoginPage.objIAcceptCheckBox,"I accept the Ring’s Terms & Conditions & IT Act 2000 checkbox");

			setLocationConnectionToONOFF("off");
			explicitWaitVisibility(RingLoginPage.objAcceptOfferBtn,30);
			verifyElementPresentAndClick(RingLoginPage.objAcceptOfferBtn, getText(RingLoginPage.objAcceptOfferBtn));

			explicitWaitVisibility(UserRegistrationPage.objSetPin,30);
			if (verifyElementPresent(UserRegistrationPage.objSetPin, "Set Pin Page")) {
				setLocationConnectionToONOFF("on");
				logger.info("Navigated to MPIN Page");
				extent.extentLoggerPass("MPIN Page", "Navigated to MPIN Page");
				verifyElementPresent(TermsAndConditionPage.objSetPin, getText(TermsAndConditionPage.objSetPin));
				softAssertion.assertEquals(getText(TermsAndConditionPage.objSetPin), "Set Pin");
				instaLoanSetPin(prop.getproperty("InstaLoanMPIN"), prop.getproperty("InstaLoanMPIN"));
			}

			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			String sHome = getText(HomePage.objHome);
			softAssertion.assertEquals(sHome, "Home");
			ringPayLogout();

		}

	//===================================================Offer Module End==============================================================================
//====================================To verify Age criteria failed checks (22 to 60) Start========================================================
	public void ageCriteriaFailedCheck() throws Exception {
		extent.HeaderChildNode("Age Criteria Check");
		getDriver().resetApp();
		if (verifyElementPresent(RingLoginPage.objCamPermPopUp, "Enable permissions button")) {
			enablePermissions();
		}

		verifyElementPresentAndClick(RingLoginPage.objSignUpLoginBt, getText(RingLoginPage.objSignUpLoginBt));
		softAssertion.assertEquals(getText(RingPayMerchantFlowPage.objLoginPageHeader), "Sign up/Login");
		logger.info("User redirected to Signup/Login Screen");
		extent.extentLogger("login", "User redirected to Signup/Login Screen");
		verifyElementPresent(RingPayMerchantFlowPage.objContinueWithMobileCTA,getTextVal(RingPayMerchantFlowPage.objContinueWithMobileCTA, "CTA"));
		verifyElementPresent(RingPayMerchantFlowPage.objContinueWithGoogleCTA,getTextVal(RingPayMerchantFlowPage.objContinueWithGoogleCTA, "CTA"));
		verifyElementPresent(RingPayMerchantFlowPage.objContinueWithFacebookCTA,getTextVal(RingPayMerchantFlowPage.objContinueWithFacebookCTA, "CTA"));
		loginMobile();

		mobileNoValidation1("9" + RandomIntegerGenerator(9));
		enterOtp(prop.getproperty("OTP"));
		readAndAccept();
		userDetails();
		ageSelect("lessthan18", "Oct", "12", "2010", 2010, 12);
		ageSelect("greaterthan55", "Oct", "12", "1966", 1966, 12);

		verifyElementPresentAndClick(RingLoginPage.objSignUpLoginBt, getText(RingLoginPage.objSignUpLoginBt));
		softAssertion.assertEquals(getText(RingPayMerchantFlowPage.objLoginPageHeader), "Sign up/Login");
		logger.info("User redirected to Signup/Login Screen");
		loginMobile();
		mobileNoValidation1("9" + RandomIntegerGenerator(9));
		enterOtp(prop.getproperty("OTP"));
		userDetails();
		ageSelect("greaterthanequalto18 || lessthanequalto55", "Oct", "12", "1995", 1995, 12);

		logger.info("TC_Ring_Customer_Seg_57 - To verify users provided age <22 years");
		extent.extentLoggerPass("TC_Ring_Customer_Seg_57", "To verify users provided age <22 years");

		logger.info("TC_Ring_Customer_Seg_58 - To verify users provided age >60 years");
		extent.extentLoggerPass("TC_Ring_Customer_Seg_58", "To verify users provided age >60 years");

		logger.info("TC_Ring_Customer_Seg_59 - To verify users provided age exact 22 years");
		extent.extentLoggerPass("TC_Ring_Customer_Seg_59", "To verify users provided age exact 22 years");

		logger.info("TC_Ring_Customer_Seg_60 - To verify users provided age exact 60 years");
		extent.extentLoggerPass("TC_Ring_Customer_Seg_60", "To verify users provided age exact 60 years");

		logger.info("TC_Ring_Customer_Seg_61 - To verify users age between 22 to 60 years");
		extent.extentLoggerPass("TC_Ring_Customer_Seg_61", "To verify users age between 22 to 60 years");
	}

	public void userDetails() throws Exception {
		//extent.HeaderChildNode("Age Criteria");

		explicitWaitVisibility(RingUserDetailPage.objFirstName,30);
		click(RingUserDetailPage.objFirstName, "First Name Field");
		type(RingUserDetailPage.objFirstName, "Sunil", "First Name field");

		explicitWaitVisibilityNonDefault(RingUserDetailPage.objLastName, 10);
		click(RingUserDetailPage.objLastName, "Last Name Field");
		type(RingUserDetailPage.objLastName, "Chatla", "Last Name field");
		waitTime(2000);
		explicitWaitVisibilityNonDefault(RingUserDetailPage.objMothersName, 10);
		click(RingUserDetailPage.objMothersName, "Mother's Name Field");
		type(RingUserDetailPage.objMothersName, "Mom", "Mother's Name field");
		hideKeyboard();

		explicitWaitVisibilityNonDefault(RingUserDetailPage.objEmail, 10);
		click(RingUserDetailPage.objEmail, "Email Field");
		click(RingUserDetailPage.objNone, "None of the Above Button");
		String email = generateRandomString(8) + "@gmail.com";
		type(RingUserDetailPage.objEmail, email, "Email Filed");
		hideKeyboard();
		explicitWaitVisibility(RingLoginPage.objGender,30);
		click(RingLoginPage.objGender, "Gender male is selected");
		click(RingLoginPage.objGender, "Gender male is selected");
	}

	public void ageSelect(String key, String Month, String Date, String Year, int year, int date) throws Exception {
		//extent.HeaderChildNode("Age Criteria");

		switch (key) {
		case "lessthan18":
			dateOfBirth(Month, Date, Year);

			age = ageCal(year, date);
			break;

		case "greaterthan55":
			dateOfBirth(Month, Date, Year);

			age = ageCal(year, date);
			break;

		case "greaterthanequalto18 || lessthanequalto55":
			dateOfBirth(Month, Date, Year);

			age = ageCal(year, date);
			break;

		default:
			logger.info("invalid age!!");
			break;
		}

		explicitWaitVisibility(RingUserDetailPage.objRegisterBtn,30);
		verifyElementPresentAndClick(RingUserDetailPage.objRegisterBtn, "Proceed Button");

		if (age < 18) {
			logger.warn("The present age is <18 therefore the age is: " + age);
			extent.extentLoggerPass("age", "The present age is <18 therefore the age is: " + age);
			explicitWaitVisibility(RingUserDetailPage.objAgeLessThan18Notification,30);
			verifyElementPresent(RingUserDetailPage.objAgeLessThan18Notification,getText(RingUserDetailPage.objAgeLessThan18Notification));
			logger.info("Age Criteria Failed");
			extent.extentLogger("Age Verification", "Age Criteria Failed");

		} else if (age > 55) {

			logger.warn("The present age is >55 therefore the age is: " + age);
			extent.extentLoggerPass("age", "The present age is >55 therefore the age is: " + age);
			explicitWaitVisibility(RingUserDetailPage.objSorryMsg,30);
			verifyElementPresent(RingUserDetailPage.objSorryMsg, getText(RingUserDetailPage.objSorryMsg));
			logger.info("Age Criteria Failed");
			extent.extentLogger("Age Verification", "Age Criteria Failed");
			verifyElementPresentAndClick(RingUserDetailPage.objHamburgerTab, "Hamburger Tab");
			verifyElementPresentAndClick(RingUserDetailPage.objProfileTabCompletedPercentage,"Profile Completed Percentage tab");
			verifyElementPresentAndClick(RingUserDetailPage.objLogoutBtn, "Logout Button");

			click(RingLoginPage.objYesBtn, "Yes confirmation button");
			logger.info("User is successfully logged out");
			extent.extentLoggerPass("Logout confirmation", "User is successfully logged out");

		} else if (age >= 18 || age <= 55) {
			instaNewCommunicationAddress();
			logger.info("The present age is >=18 & <=55 and therefor the age is: " + age);
			extent.extentLogger("age", "The present age is >=18 & <=55 and therefor the age is: " + age);
			logger.info("Age Criteria Passed");
			extent.extentLogger("Age Verification", "Age Criteria Passed");

			instaLoancongratsScreen();
			instaLoanSetPin(prop.getproperty("InstaLoanMPIN"), prop.getproperty("InstaLoanMPIN"));
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			String sHome = getText(HomePage.objHome);
			softAssertion.assertEquals(sHome, "Home");
			ringPayLogout();
		}
	}

	public void dateOfBirth(String month, String date, String year) throws Exception {
		if(verifyElementPresent2(UserRegistrationPage.objUserDOB, "Date Of Birth Element Present")) {
		explicitWaitClickable(UserRegistrationPage.objUserDOB, 10);
		click(UserRegistrationPage.objUserDOB, "DOB field");
		explicitWaitVisibilityNonDefault(UserRegistrationPage.objDatePickerDate, 10);
		
		Aclick(UserRegistrationPage.objDatePickerYear, "Year field");
		clearField(UserRegistrationPage.objDatePickerYear, "Year field");
		type(UserRegistrationPage.objDatePickerYear, year, "Year field");

		click(UserRegistrationPage.objDatePickerMonth, "Month field");
		click(UserRegistrationPage.objDatePickerMonth, "Month field");
		clearField(UserRegistrationPage.objDatePickerMonth, "Month field");
		type(UserRegistrationPage.objDatePickerMonth, month, "Month field");

		click(UserRegistrationPage.objDatePickerDate, "Date field");
		clearField(UserRegistrationPage.objDatePickerDate, "Date field");
		type(UserRegistrationPage.objDatePickerDate, date, "Date field");

		Aclick(UserRegistrationPage.objOK, "OK button");
		
	}
	}

//============================================age Criteria check End====================================================================	
//===========================Pan bottom sheet case if pan received from bureau and KYC get skipped Start================================	
	public HashMap mockUserAPI(String url, String gender, String encrypted_name) {
		HashMap map = new HashMap();
		ValidatableResponse response = Utilities.MockuserAPI(url, gender, encrypted_name);
		String firstName = response.extract().jsonPath().getString("data.data.first_name");
		System.out.println("First Name= " + firstName);
		String middleName = response.extract().jsonPath().getString("data.data.middle_name");
		System.out.println("Middle Name= " + middleName);
		String lastName = response.extract().jsonPath().getString("data.data.last_name");
		System.out.println("Last Name =" + lastName);
		String panCard = response.extract().jsonPath().getString("data.data.pan");
		System.out.println("Pan Card= " + panCard);
		String mobileNumber = response.extract().jsonPath().getString("data.data.mobile_number");
		System.out.println("Mobile Number= " + mobileNumber);
		String imei = response.extract().jsonPath().getString("data.data.imei");
		System.out.println("IMEI= " + imei);
		String motherName = response.extract().jsonPath().getString("data.data.mother_name");
		System.out.println("Mother Name= " + motherName);
		String email = response.extract().jsonPath().getString("data.data.email");
		System.out.println("Email= " + email);
		String genders = response.extract().jsonPath().getString("data.data.gender");
		System.out.println("Gender= " + genders);
		String otp = response.extract().jsonPath().getString("data.data.otp");
		System.out.println(otp);
		String dob = response.extract().jsonPath().getString("data.data.dob");
		System.out.println(dob);
		String[] dateSplit = dob.split("-");
		String year = dateSplit[0];
		System.out.println("years---" + year);
		String month = dateSplit[1];
		System.out.println("month---" + month);
		String date = dateSplit[2];
		System.out.println("date---" + date);
		int monthNumber = Integer.parseInt(month);
		String monthByName = Month.of(monthNumber).getDisplayName(TextStyle.SHORT, Locale.US);
		System.out.println(monthByName);
		String roomno = response.extract().jsonPath().getString("data.data.present_address.room_number");
		System.out.println(roomno);
		String address1 = response.extract().jsonPath().getString("data.data.present_address.line_1");
		System.out.println(address1);
		String address2 = response.extract().jsonPath().getString("data.data.present_address.line_2");
		System.out.println(address2);
		String landmark = response.extract().jsonPath().getString("data.data.present_address.landmark");
		System.out.println(landmark);
		String pin = response.extract().jsonPath().getString("data.data.present_address.pincode");
		System.out.println(pin);
		
		String front = response.extract().jsonPath().getString("data.data.aadhaar_front_image.asset_path");
		System.out.println(front);
		String back = response.extract().jsonPath().getString("data.data.aadhaar_back_image.asset_path");
		System.out.println(back);
		// User Details

		map.put("firstName", firstName);
		map.put("middleName", middleName);
		map.put("lastName", lastName);
		map.put("panCard", panCard);
		map.put("mobileNumber", mobileNumber);
		map.put("motherName", motherName);
		map.put("email", email);
		map.put("genders", genders);
		map.put("otp", otp);
		map.put("year", year);
		map.put("month", monthByName);
		map.put("date", date);
		map.put("imei", imei);
		// New Communication Address
		map.put("roomNo", roomno);
		map.put("address1", address1);
		map.put("address2", address2);
		map.put("landmark", landmark);
		map.put("pin", pin);
		map.put("dob", dob);
		map.put("front", front);
		map.put("back", back);

		return map;
	}

	public void userDetailsFromAPI(String firstName, String lastName, String email, String motherName, String gender)throws Exception {
//		extent.HeaderChildNode("Age Criteria");

		explicitWaitVisibility(RingUserDetailPage.objFirstName, 10);
		click(RingUserDetailPage.objFirstName, "First Name Field");
		type(RingUserDetailPage.objFirstName, firstName, "First Name field");

		explicitWaitVisibility(RingUserDetailPage.objLastName, 10);
		click(RingUserDetailPage.objLastName, "Last Name Field");
		type(RingUserDetailPage.objLastName, lastName, "Last Name field");
		hideKeyboard();

		Aclick(UserRegistrationPage.objMothersName, "Mother's Name Text Field");
		type(UserRegistrationPage.objMothersName, motherName, "Mother's Name Text Field");
		hideKeyboard();
		explicitWaitVisibility(RingUserDetailPage.objEmail, 10);
		click(RingUserDetailPage.objEmail, "Email Field");
		if (verifyElementPresent(RingLoginPage.objNoneBtn, "None of the above popup")) {
			Aclick(RingLoginPage.objNoneBtn, "None of the above");
		}

		type(RingUserDetailPage.objEmail, email, "Email Filed");
		hideKeyboard();
		click(RingLoginPage.objGender, "Gender male is selected");
		click(RingLoginPage.objGender, "Gender male is selected");
	}

	public void dateOfBirthForAPI(String month, String date, String year) throws Exception {
		explicitWaitClickable(UserRegistrationPage.objUserDOB, 10);
		Aclick(UserRegistrationPage.objUserDOB, "DOB field");
		explicitWaitVisibility(UserRegistrationPage.objOK,30);

		Aclick(UserRegistrationPage.objDatePickerYear, "Year field");
		clearField(UserRegistrationPage.objDatePickerYear, "Year field");
		type(UserRegistrationPage.objDatePickerYear, year, "Year field");

		explicitWaitVisibilityNonDefault(UserRegistrationPage.objDatePickerMonth, 10);
		Aclick(UserRegistrationPage.objDatePickerMonth, "Month field");
		clearField(UserRegistrationPage.objDatePickerMonth, "Month field");
		type(UserRegistrationPage.objDatePickerMonth, month, "Month field");

		Aclick(UserRegistrationPage.objDatePickerDate, "Date field");
		clearField(UserRegistrationPage.objDatePickerDate, "Date field");
		type(UserRegistrationPage.objDatePickerDate, date, "Date field");

		Aclick(UserRegistrationPage.objOK, "OK button");

	}

	public void addAddress(String room, String address1, String address2, String landmark, String pin)
			throws Exception {
		extent.HeaderChildNode("RingPay App Merchant Flow");
		explicitWaitVisibilityNonDefault(AddAddressPage.objAddCurrentAddressHeader, 30);
		Aclick(AddAddressPage.objRoomNoTextField, "Room");
		type(AddAddressPage.objRoomNoTextField, room, "Room");
		Aclick(AddAddressPage.objAddressLine_1, "AddressLine1");
		type(AddAddressPage.objAddressLine_1, address1, "AddressLine1");
		Aclick(AddAddressPage.objAddressLine_2, "AddressLine2");
		type(AddAddressPage.objAddressLine_2, address2, "AddressLine2");
		waitTime(2000);
//		   Aclick(AddAddressPage.objLandmarkTextFields,"Landmark");
//		   type(AddAddressPage.objLandmarkTextFieldspulkit,landmark,"Landmark");
		hideKeyboard();
		waitTime(2000);
		Aclick(AddAddressPage.objPinCodeFieldpulkit, "PinCode");
		type(AddAddressPage.objPinCodeFieldpulkit, pin, "PinCode");
		hideKeyboard();
//		   click(AddAddressPage.rdioResidanceOwned, "Owned");
//		   click(AddAddressPage.rdioResidanceOwned, "Owned");
//		   Swipe("up", 1);
//		   click(AddAddressPage.cbkResidingSince, "");
//		   explicitWaitVisibilityNonDefault(AddAddressPage.oneTwoYear, 20);
//		   click(AddAddressPage.oneTwoYear, "");
		waitTime(5000);
		explicitWaitVisibilityNonDefault(AddAddressPage.objSubmitButton, 30);
		click(AddAddressPage.objSubmitButton, "Submit Button");
	}

	public void kycSkipped() throws Exception {
		extent.HeaderChildNode("RingPay KYC_Skip");
		getDriver().resetApp();
		HashMap mockUserDetails = mockUserAPI("https://testing-gateway.test.paywithring.com/api/v1/mock-data/user", "male",prop.getproperty("encryptedName"));

		String firstName = (String) mockUserDetails.get("firstName");
		String middleName = (String) mockUserDetails.get("middleName");
		String lastName = (String) mockUserDetails.get("lastName");
		String mobileNumber = (String) mockUserDetails.get("mobileNumber");
		String otp = (String) mockUserDetails.get("otp");
		String panCard = (String) mockUserDetails.get("panCard");
		String email = (String) mockUserDetails.get("email");
		String motherName = (String) mockUserDetails.get("motherName");
		String genders = (String) mockUserDetails.get("genders");
		String year = (String) mockUserDetails.get("year");
		String monthByName = (String) mockUserDetails.get("month");
		String date = (String) mockUserDetails.get("date");
		String dob = (String)mockUserDetails.get("dob");
		
		String roomno = (String) mockUserDetails.get("roomNo");
		String address1 = (String) mockUserDetails.get("address1");
		String address2 = (String) mockUserDetails.get("address2");
		String landmark = (String) mockUserDetails.get("landmark");
		String pin = (String) mockUserDetails.get("pin");

		if (verifyElementPresent(RingLoginPage.objCamPermPopUp, "Enable permissions button")) {
			enablePermissions();
		}

		click(RingLoginPage.objLoginLink, "Signup/Login link");
		loginMobile();
		mobileNoValidation1(mobileNumber);
		enterOtp(otp);
		readAndAccept();

		userDetailsFromAPI(firstName, lastName, email, motherName, genders);
		dateOfBirthForAPI(monthByName, date, year);

		setPlatform("Web");
		System.out.println("platform changed to web");
		String pf = getPlatform();
		System.out.println(pf);
		new RingPayBusinessLogic("ring");
		getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobileNumber+"&");
		type(InstaLoanPage.objEmail,"shakir.muchumarri@collabera.com","Email text field");
		click(InstaLoanPage.objContinue,"Continue Button");
		explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
		String userRefNo = getText(InstaLoanPage.objUserRefNo);
		verifyPan(panCard, dob,firstName,lastName,middleName);
		ValidatableResponse responseSkipKyc = Utilities.skip_kyc(
				"https://testing-gateway.test.paywithring.com/api/v1/update-upload-equifax/updateUploadEquifax",
				userRefNo, panCard, firstName, middleName, lastName, prop.getproperty("encryptedName"));
		String message = responseSkipKyc.extract().jsonPath().getString("message");
		System.out.println("Skip_Kyc-------------" + message);

		BrowsertearDown();

		setPlatform("Android");
		initDriver();
		explicitWaitClickable(RingUserDetailPage.objRegisterBtn,30);
		verifyElementPresentAndClick(RingUserDetailPage.objRegisterBtn, "Proceed Button");

		explicitWaitVisibility(AddAddressPage.objAddCurrentAddressHeader,30);

		if (verifyElementPresent(AddAddressPage.objAddCurrentAddressHeader, "address Page")) {
			addAddress(roomno, address1, address2, landmark, pin);
			click(AddAddressPage.objSubmitButton, "Submit");
		}
		explicitWaitVisibilityNonDefault(PAN_DetailsPage.btnyesThisIsMyPlan, 20);
		System.out.println(getText(PAN_DetailsPage.btnyesThisIsMyPlan));

		softAssertion.assertEquals("Confirm this is your PAN & name", getText(PAN_DetailsPage.txtTittle));
		softAssertion.assertEquals(panCard, getText(PAN_DetailsPage.PanNo));
		softAssertion.assertEquals(firstName + " " + middleName + " " + lastName,
				getText(PAN_DetailsPage.UserNameOfPAN));

		extent.extentLoggerPass("Popup Validation",
				"Hand Icon,Pan Number,Name of Pan Holder is visible_TC_Ring_Core_167");

		Back(2);
		logger.info("Back Button Pressed 2 Times");
		extent.extentLoggerPass("Back Button click", "Validated by doing back popup is not disappear_TC_Ring_Core_168");
		setWifiConnectionToONOFF("Off");
		explicitWaitVisibility(PAN_DetailsPage.btnnoThisIsNotMine,30);
		Aclick(PAN_DetailsPage.btnnoThisIsNotMine, "No, This Is Not Mine");
		explicitWaitVisibilityNonDefault(PAN_DetailsPage.txtNoInternet, 20);
		softAssertion.assertEquals(" Check your connection & try again ",
				getText(PAN_DetailsPage.txtCheckyourInternet));
		logger.info("Internet Connection Validation");
		extent.extentLoggerPass("Validate Internet Connection Off", "Check Your Internet Connection_TC_Ring_Core_171");
		setWifiConnectionToONOFF("On");
		explicitWaitVisibility(PAN_DetailsPage.btnOkaygotIt,30);
		Aclick(PAN_DetailsPage.btnOkaygotIt, "Okay Got It");
		explicitWaitVisibilityNonDefault(PAN_DetailsPage.txtTittle, 20);
		setWifiConnectionToONOFF("Off");
		explicitWaitVisibility(PAN_DetailsPage.btnyesThisIsMyPlan,30);
		Aclick(PAN_DetailsPage.btnyesThisIsMyPlan, "Yes, This is My Pan");
		explicitWaitVisibilityNonDefault(PAN_DetailsPage.txtNoInternet, 20);
		softAssertion.assertEquals(" Check your connection & try again ",
				getText(PAN_DetailsPage.txtCheckyourInternet));
		logger.info("Internet Connection Validation");
		extent.extentLoggerPass("Validate Internet Connection Off", "Check Your Internet Connection_TC_Ring_Core_173");
		setWifiConnectionToONOFF("On");
		explicitWaitVisibility(PAN_DetailsPage.btnOkaygotIt,30);
		Aclick(PAN_DetailsPage.btnOkaygotIt, "Okay Got It");
		explicitWaitVisibilityNonDefault(PAN_DetailsPage.txtTittle, 20);
		Aclick(PAN_DetailsPage.btnnoThisIsNotMine, "No This is not Mine Button");
		explicitWaitVisibilityNonDefault(OfferPage.offerTitle, 20);
		logger.info("Offer Page Open When we Click on 'No this is not mine' Button");
		softAssertion.assertEquals("Accept Offer", getText(OfferPage.btnAcceptOffer));
		extent.extentLoggerPass("Offer Page Visible", "Offer Page visible when network up then press_TC_Ring_Core_172");
		extent.extentLoggerPass("Offer Page Visible",
				"Offer Page visible when Press we press on 'No this is not mine' Button_TC_Ring_Core_169");
		explicitWaitVisibilityNonDefault(OfferPage.btnAcceptOffer, 10);
		Aclick(OfferPage.ckbAcceptTermandcondition, "check box for term and conditions");
		Aclick(OfferPage.btnAcceptOffer, "Accept Offer");
		explicitWaitVisibilityNonDefault(TransactionPIN.txtTitleOfSetPin, 20);
		Aclick(TransactionPIN.objEnterPin, "Enter Pin");
		type(TransactionPIN.objEnterPin, "1111", "Enter Pin");
		Aclick(TransactionPIN.objReEnterPin, "Re-Enter Pin");
		type(TransactionPIN.objReEnterPin, "1111", "Re-Enter Pin");
		Aclick(TransactionPIN.objSubmit, "Submit Button");
		if (checkElementExist(HomePage.objAdCloseBtn, "Check")) {
			explicitWaitVisibilityNonDefault(HomePage.objAdCloseBtn, 20);
			Aclick(HomePage.objAdCloseBtn, "Ad Cross Button");
		}

		explicitWaitVisibilityNonDefault(HomePage.homeTab, 20);
		ringPayLogout();

		HashMap mockUserDetailsForYesBtn = mockUserAPI("https://testing-gateway.test.paywithring.com/api/v1/mock-data/user", "male",prop.getproperty("encryptedName"));

		String firstNameForYesBtn = (String) mockUserDetailsForYesBtn.get("firstName");
		String middleNameForYesBtn = (String) mockUserDetailsForYesBtn.get("middleName");
		String lastNameForYesBtn = (String) mockUserDetailsForYesBtn.get("lastName");
		String mobileNumberForYesBtn = (String) mockUserDetailsForYesBtn.get("mobileNumber");
		String otpForYesBtn = (String) mockUserDetailsForYesBtn.get("otp");
		String panCardForYesBtn = (String) mockUserDetailsForYesBtn.get("panCard");
		String emailForYesBtn = (String) mockUserDetailsForYesBtn.get("email");
		String motherNameForYesBtn = (String) mockUserDetailsForYesBtn.get("motherName");
		String gendersForYesBtn = (String) mockUserDetailsForYesBtn.get("genders");
		String yearForYesBtn = (String) mockUserDetailsForYesBtn.get("year");
		String monthByNameForYesBtn = (String) mockUserDetailsForYesBtn.get("month");
		String dateForYesBtn = (String) mockUserDetailsForYesBtn.get("date");
		String dobForYesBtn  =(String)mockUserDetailsForYesBtn.get("dob");
		
		String roomnoForYesBtn = (String) mockUserDetailsForYesBtn.get("roomNo");
		String address1ForYesBtn = (String) mockUserDetailsForYesBtn.get("address1");
		String address2ForYesBtn = (String) mockUserDetailsForYesBtn.get("address2");
		String landmarkForYesBtn = (String) mockUserDetailsForYesBtn.get("landmark");
		String pinForYesBtn = (String) mockUserDetailsForYesBtn.get("pin");

		click(RingLoginPage.objLoginLink, "Signup/Login link");
		loginMobile();
		mobileNoValidation1(mobileNumberForYesBtn);
		enterOtp(otp);

		userDetailsFromAPI(firstNameForYesBtn, lastNameForYesBtn, emailForYesBtn, motherNameForYesBtn,
				gendersForYesBtn);
		dateOfBirthForAPI(monthByNameForYesBtn, dateForYesBtn, yearForYesBtn);

		setPlatform("Web");
		new RingPayBusinessLogic("ring");
		getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobileNumberForYesBtn+"&");
		type(InstaLoanPage.objEmail,"shakir.muchumarri@collabera.com","Email text field");
		click(InstaLoanPage.objContinue,"Continue Button");
		explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
		String userRefNo1 = getText(InstaLoanPage.objUserRefNo);
		BrowsertearDown();
		verifyPan(panCard, dobForYesBtn,firstName,lastName,middleName);
		ValidatableResponse responseSkipKyc1 = Utilities.skip_kyc(
				"https://testing-gateway.test.paywithring.com/api/v1/update-upload-equifax/updateUploadEquifax",
				userRefNo1, panCardForYesBtn, firstNameForYesBtn, middleNameForYesBtn, lastNameForYesBtn,
				prop.getproperty("encryptedName"));
		String message1 = responseSkipKyc1.extract().jsonPath().getString("message");
		System.out.println("Skip_Kyc-------------" + message1);

		setPlatform("Android");
		initDriver();

		verifyElementPresentAndClick(RingUserDetailPage.objRegisterBtn, "Proceed Button");

		explicitWaitVisibility(AddAddressPage.objAddCurrentAddressHeader,30);
		if (verifyElementPresent(AddAddressPage.objAddCurrentAddressHeader, "address Page")) {
			addAddress(roomnoForYesBtn, address1ForYesBtn, address2ForYesBtn, landmarkForYesBtn, pinForYesBtn);
			click(AddAddressPage.objSubmitButton, "Submit");
		}

		explicitWaitVisibilityNonDefault(OfferPage.offerTitle, 20);
		logger.info("Offer Page Open When we Click on 'Yes, This is my PAN");
		softAssertion.assertEquals("Accept Offer", getText(OfferPage.btnAcceptOffer));
		extent.extentLoggerPass("Offer Page Visible", "Offer Page visible when network up then press_TC_Ring_Core_170");
		extent.extentLoggerPass("Offer Page Visible",
				"Offer Page visible when Press we press on 'Yes, This is my PAN' Button_TC_Ring_Core_174");

		instaLoancongratsScreen();
		instaLoanSetPin(prop.getproperty("InstaLoanMPIN"), prop.getproperty("InstaLoanMPIN"));
		if (checkElementExist(HomePage.objAdCloseBtn, "Check")) {
			explicitWaitVisibilityNonDefault(HomePage.objAdCloseBtn, 20);
			Aclick(HomePage.objAdCloseBtn, "Ad Cross Button");
		}
		ringPayLogout();
	}

//=====================Pan bottom sheet case if pan received from bureau and KYC get skipped End=========================================================	
//========================================Banner logic to HomePage Start=============================================================================
	public void bannerLogicOnHomePage() throws Exception {

		extent.HeaderChildNode("Banner logic on Home screen");
		getDriver().resetApp();
		/*------------------------------Data From API----------------------------*/

		HashMap mockUserDetails = mockUserAPI("https://testing-gateway.test.paywithring.com/api/v1/mock-data/user", "male",prop.getproperty("encryptedName"));
		String firstName = (String) mockUserDetails.get("firstName");
		String middleName = (String) mockUserDetails.get("middleName");
		String lastName = (String) mockUserDetails.get("lastName");
		String mobileNumber = (String) mockUserDetails.get("mobileNumber");
		String otp = (String) mockUserDetails.get("otp");
		String panCard = (String) mockUserDetails.get("panCard");
		String email = (String) mockUserDetails.get("email");
		String motherName = (String) mockUserDetails.get("motherName");
		String genders = (String) mockUserDetails.get("genders");
		String year = (String) mockUserDetails.get("year");
		String monthByName = (String) mockUserDetails.get("month");
		String date = (String) mockUserDetails.get("date");

		String roomno = (String) mockUserDetails.get("roomNo");
		String address1 = (String) mockUserDetails.get("address1");
		String address2 = (String) mockUserDetails.get("address2");
		String landmark = (String) mockUserDetails.get("landmark");
		String pin = (String) mockUserDetails.get("pin");

		/*------------------------------Front End----------------------------*/

		if (verifyElementPresent(RingLoginPage.objCamPermPopUp, "Enable permissions button")) {
			enablePermissions();
		}

		click(RingLoginPage.objLoginLink, "Signup/Login link");
		loginMobile();
		mobileNoValidation1(mobileNumber);
		enterOtp(otp);
		readAndAccept();


		userDetailsFromAPI("sunil", "chatla",
				"sunil" + "chatla" + new Random().nextInt(10000) + "@example.com", "yashoda", "male");
		dateOfBirthForAPI("Oct", "26", "1996");
		verifyElementPresentAndClick(RingUserDetailPage.objRegisterBtn, "Proceed Button");
		explicitWaitVisibility(AddAddressPage.objAddCurrentAddressHeader,30);
		if (verifyElementPresent(AddAddressPage.objAddCurrentAddressHeader, "address Page")) {
			addAddress(roomno, address1, address2, landmark, pin);
			click(AddAddressPage.objSubmitButton, "Submit");
		}
		explicitWaitVisibilityNonDefault(OfferPage.btnAcceptOffer, 10);
		Aclick(OfferPage.ckbAcceptTermandcondition, "check box for term and conditions");
		Aclick(OfferPage.btnAcceptOffer, "Accept Offer");
		explicitWaitVisibilityNonDefault(TransactionPIN.txtTitleOfSetPin, 20);
		Aclick(TransactionPIN.objEnterPin, "Enter Pin");
		type(TransactionPIN.objEnterPin, prop.getproperty("InstaLoanMPIN"), "Enter Pin");
		Aclick(TransactionPIN.objReEnterPin, "Re-Enter Pin");
		type(TransactionPIN.objReEnterPin, prop.getproperty("InstaLoanMPIN"), "Re-Enter Pin");
		Aclick(TransactionPIN.objSubmit, "Submit Button");
		if (verifyElementPresent(HomePage.objAdCloseBtn, "")) {

			explicitWaitVisibilityNonDefault(HomePage.objAdCloseBtn, 20);
			Aclick(HomePage.objAdCloseBtn, "Ad Cross Button");
			explicitWaitVisibilityNonDefault(HomePage.homeTab, 20);
		}

		if (verifyElementPresent(HomePage.purpleBannerVerifyNow, "Searching Purple Banner")) {
			boolean purpleBanner = swipeElementAndroid(HomePage.bannerTray, HomePage.purpleBannerVerifyNow);
			logger.info("Purple Banner--Verify KYC Doucment");
			softAssertion.assertEquals("Verify your document to get", getText(HomePage.purpleBannerText1));
			extent.extentLoggerPass("Verify your document to get",
					"Purple Banner is visible with text 'Verify your document to get higher limit & cashback up to ₹100 Verify Now' ----TC_Ring_Core_260");
			Aclick(HomePage.purpleBannerVerifyNow, "KYC Documents Page");
			explicitWaitVisibilityNonDefault(KycDocument.kycHeader, 30);
			logger.info("KYC Documents Page Open");
			softAssertion.assertEquals("KYC Documents", getText(KycDocument.kycHeader));
			extent.extentLoggerPass("KYC Document Page",
					"After click on verify now of purple banner,Kyc Document page Open_TC_Ring_Core_261");
			Back(1);
			explicitWaitVisibilityNonDefault(KycDocument.txtPopupWhileBackBtn, 20);
			Aclick(KycDocument.yesBtn, "Yes Button");
			explicitWaitVisibilityNonDefault(HomePage.homeTab, 20);
		}

		swipeElementAndroid(HomePage.bannerTray,HomePage.lightBlueBannerTransferNow);
		explicitWaitVisibilityNonDefault(HomePage.lightBlueBannerTransferNow, 20);
		logger.info("Ligh Blue Banner----Transfer Now");
		softAssertion.assertEquals("Transfer your RING", getText(HomePage.lightBluetext2));
		extent.extentLoggerPass("Cash Crunch? Transfer your RING limit to your bank account",
				"Light Blue Banner is visible with text 'Cash Crunch? Transfer your RING limit to your bank account Transfer Now'");
		boolean green = swipeElementAndroid(HomePage.lightBlueBannerTransferNow, HomePage.greenBannerText1);
		System.out.println(green);

		if (green == true) {
			logger.info("micro payment flag is enabled for user from Configuration");
			System.out.println(getText(HomePage.greenBannerPayNow));
			extent.extentLoggerPass("micro payment flag",
					"micro payment flag is enabled for user from Configuration-----------TC_Ring_Core_265");
			extent.extentLoggerPass("RINGGGG! Transactions below",
					"Green Banner is visible with text 'RINGGGG! Transactions below 200 are absolutely FREE! Pay Now'--------------TC_Ring_Core_266");
			explicitWaitClickable(HomePage.greenBannerPayNow,30);
			Aclick(HomePage.greenBannerPayNow, "Pay Now");

			explicitWaitVisibilityNonDefault(HomePage.objScanAnyQRToPay, 10);
			String sScanAnyQRCode = getText(HomePage.objScanAnyQRToPay);
			softAssertion.assertEquals("Scan any QR code to pay", sScanAnyQRCode);
			verifyElementPresent(HomePage.objScanAnyQRToPay, getTextVal(HomePage.objScanAnyQRToPay, "Text"));
			Back(1);
			// Scan here
			/*------------------------------WEB----------------------------*/
			/*------------------------------WEB----------------------------*/
			Utilities.setPlatform = "Web";
			new CommandBase("Chrome");
			waitTime(4000);
			getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobileNumber+"&");
			type(InstaLoanPage.objEmail,"shakir.muchumarri@collabera.com","Email text field");
			click(InstaLoanPage.objContinue,"Continue Button");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo = getText(InstaLoanPage.objUserRefNo);
			BrowsertearDown();
			txnApi(userRefNo,mobileNumber,"100");
			
			
			

		/*------------------------------WEB----------------------------*/
		Utilities.setPlatform = "Web";
		new CommandBase("Chrome");
		waitTime(2000);
		getWebDriver().get("https://new-admin-panel.test.ideopay.in/sign-in");
		explicitWaitVisibilityNonDefault(TestingPortalWebPage.emailFieldNewPortal, 20);
		type(TestingPortalWebPage.emailFieldNewPortal, prop.getproperty("RingAdminEmail"), "Email Entered");
		JSClick(TestingPortalWebPage.continueNewButton, "Continue Button");
		explicitWaitVisibilityNonDefault(TestingPortalWebPage.tabBNPL, 20);
		JSClick(TestingPortalWebPage.tabBNPL, "BNPL Link");
		explicitWaitVisibilityNonDefault(TestingPortalWebPage.tabBNPL_Line, 20);
		JSClick(TestingPortalWebPage.tabBNPL_Line, "BNPL Line Link");
		waitTime(3000);
		explicitWaitVisibilityNonDefault(TestingPortalWebPage.iconBulkUpload, 20);
		JSClick(TestingPortalWebPage.iconBulkUpload, "Bulk Upload Icon");
		explicitWaitVisibilityNonDefault(TestingPortalWebPage.menuBulkuploadcashbackWhitelisting, 20);
		JSClick(TestingPortalWebPage.menuBulkuploadcashbackWhitelisting, "Bulk Upload Cashback WhiteListing");
		clickElementWithWebLocator(TestingPortalWebPage.uploadExcelFile);
		waitTime(3000);
		System.out.println(System.getProperty("user.dir"));
		String projectPath = System.getProperty("user.dir");
		// Enter Data into Excel
		Date dt = new Date();
		DateTime dtOrg = new DateTime(dt);
		DateTime dtPlusOne = dtOrg.plusDays(1);
		System.out.println("tomorrow date"+dtPlusOne);
		/*------------------------------Write In Excel----------------------------*/

		ExcelFunctions ex = new ExcelFunctions();
		ex.writeXLSXFile(projectPath + "\\Mock_Files\\BULK_CASHBACK_WHITELISTING.xlsx", 1, 0, userRefNo);
		ex.writeXLSXFile(projectPath + "\\Mock_Files\\BULK_CASHBACK_WHITELISTING.xlsx", 1, 1,prop.getproperty("cashbackPercent"));
		ex.writeXLSXFile(projectPath + "\\Mock_Files\\BULK_CASHBACK_WHITELISTING.xlsx", 1, 2,prop.getproperty("billingMaxCap"));
		ex.writeXLSXFile(projectPath + "\\Mock_Files\\BULK_CASHBACK_WHITELISTING.xlsx", 1, 4,prop.getproperty("expireDate"));
		type(TestingPortalWebPage.uploadExcelFile,projectPath + "\\Mock_Files\\BULK_CASHBACK_WHITELISTING.xlsx","Uploading bulk cashback excel file");
		System.out.println("completed");
		JSClick(TestingPortalWebPage.submitBtn, "Submit Button");
		BrowsertearDown();

		/*------------------------------Android----------------------------*/

		setPlatform("Android");
		initDriver();
		click(RingLoginPage.objTopMenu,"Top left menu");
		explicitWaitVisibilityNonDefault(RingLoginPage.objProfileCrossBtn,30);
		click(RingLoginPage.objProfileCrossBtn,"Profile Cross Button");
		explicitWaitVisibility(HomePage.txtAreYouEnjoyingApp,30);
		explicitWaitVisibility(HomePage.noBtn,30);
		click(HomePage.noBtn,"No button");
		Swipe("DOWN", 1);
		click(HomePage.objScanAndPay,"Scan & Pay button");
		waitTime(3000);
		Back(1);
		boolean yellow = swipeElementAndroid(HomePage.bannerTray,HomePage.txt1yellowBanner(prop.getproperty("cashbackPercent")));
		if (yellow == true) {
			logger.info("Yellow Banner--CashBack Banner is visible");
			softAssertion.assertEquals("Up to " + prop.getproperty("cashbackPercent") + "%* cashback on all transactions",getText(HomePage.txt1yellowBanner(prop.getproperty("cashbackPercent"))));
			extent.extentLoggerPass("CashBack Banner", "CashBack Banner is Visible--------------TC_Ring_Core_263");
			//click(HomePage.objScanAndPay, "Scan Button");
			txnApi(userRefNo,mobileNumber,"90");
			click(RingLoginPage.objTopMenu,"Top left menu");
			explicitWaitVisibilityNonDefault(RingLoginPage.objProfileCrossBtn,30);
			click(RingLoginPage.objProfileCrossBtn,"Profile Cross Button");
			explicitWaitVisibility(HomePage.txtAreYouEnjoyingApp,30);
			explicitWaitVisibility(HomePage.noBtn,30);
			click(HomePage.noBtn,"No button");
		} else {
			logger.info("Yellow Banner not visible");
			extent.extentLoggerFail("Yellow Banner", "Yellow Banner not visible");
		
	}}}
//===========================================Banner logic to HomePage end=========================================================	

	//===============================================Transaction Set pin Start========================================================

		public void transactionSetPin() throws Exception {
			extent.HeaderChildNode("Transaction Set Pin Page Validation");
			getDriver().resetApp();
			promoCodeFlowSetPin();
			setPinMerchantFlowWithLocationOff();
			SetPinMerchantFlowWithLocationOn();
		}

	//=========================================Promocode transaction Set pin=============================================================
		public void promoCodeFlowSetPin() throws Exception {
			getDriver().resetApp();
			if (verifyElementPresent(RingLoginPage.objCamPermPopUp, "Enable permissions button")) {
				enablePermissions();
			}
			verifyElementPresentAndClick(RingLoginPage.objSignUpLoginBt, getText(RingLoginPage.objSignUpLoginBt));
			loginMobile();

			mobileNoValidation1("9" + RandomIntegerGenerator(9));
			enterOtp(prop.getproperty("OTP"));
			readAndAccept();
			userDetails();
			dateOfBirth("Feb", "10", "1996");
			verifyElementPresentAndClick(RingUserDetailPage.objRegisterBtn, "Proceed Button");
			instaNewCommunicationAddress();
			
			explicitWaitVisibility(RingLoginPage.objCongratsPage,30);
			verifyElementPresent(RingLoginPage.objCongratsPage, getText(RingLoginPage.objCongratsPage));
			verifyElementPresent(RingLoginPage.objApprovedRingLimit,getText(RingLoginPage.objApprovedRingLimit) + getText(RingLoginPage.objApprovedRinglimitAmt));
			verifyElementPresent(RingLoginPage.objIAcceptCheckBox,"I accept the Ring’s Terms & Conditions & IT Act 2000 checkbox");
			verifyElementPresent(RingLoginPage.objAcceptOfferBtn, getText(RingLoginPage.objAcceptOfferBtn));
			verifyElementPresentAndClick(RingLoginPage.objIAcceptCheckBox,"I accept the Ring’s Terms & Conditions & IT Act 2000 checkbox");
			verifyElementPresentAndClick(RingLoginPage.objAcceptOfferBtn, getText(RingLoginPage.objAcceptOfferBtn));

			if (verifyElementPresent(UserRegistrationPage.objSetPin, getTextVal(UserRegistrationPage.objSetPin, "Test"))) {
				String sSetPin = getText(UserRegistrationPage.objSetPin);
				softAssertion.assertEquals(sSetPin, "Set Pin");
				logger.info("TC_Ring_Core_190,Navigated to SET PIN screen,after clicking on Accept Offer");
				extent.extentLoggerPass("TC_Ring_Core_190", "TC_Ring_Core_190 - Navigated to SET PIN screen,after clicking on Accept Offer");
				System.out.println("-----------------------------------------------------------");
			}
			transactionSetPinForPromoCodeFlow();
		}

		public void transactionSetPinForPromoCodeFlow() throws Exception {
		
			for(int i=1;i<=4;i++) {
				
				switch (i) {
				case 1:	
					click(UserRegistrationPage.objEnterPin(i), "Enter pin first Element");
					type(UserRegistrationPage.objEnterPin(i), "1", "Enter pin first Element");
					click(UserRegistrationPage.objSubmit, "Submit Button");
					hideKeyboard();
					verifyElementPresent(UserRegistrationPage.objErrorMessage,getTextVal(UserRegistrationPage.objErrorMessage, "Error Message"));
					logger.info("TC_Ring_Core_192,Please enter new Pin Error Message is displayed");
					extent.extentLoggerPass("TC_Ring_Core_192","TC_Ring_Core_192,Please enter new Pin Error Message is displayed");
					System.out.println("-----------------------------------------------------------");	
					break;
				
				case 2 :
					click(UserRegistrationPage.objEnterPin(i), "Enter pin second Element");
					type(UserRegistrationPage.objEnterPin(i), "1", "Enter pin second Element");
					hideKeyboard();
					verifyElementPresent(UserRegistrationPage.objErrorMessage,getTextVal(UserRegistrationPage.objErrorMessage, "Error Message"));
					logger.info("TC_Ring_Core_193,Please enter new Pin Error Message is displayed");
					extent.extentLoggerPass("TC_Ring_Core_193","TC_Ring_Core_193,Please enter new Pin Error Message is displayed");
					System.out.println("-----------------------------------------------------------");
					break;
					
				case 3 :
					click(UserRegistrationPage.objEnterPin(i), "Enter pin third Element");
					type(UserRegistrationPage.objEnterPin(i), "1", "Enter pin third Element");
					hideKeyboard();
					verifyElementPresent(UserRegistrationPage.objErrorMessage,getTextVal(UserRegistrationPage.objErrorMessage, "Error Message"));
					logger.info("TC_Ring_Core_193,Please enter new Pin Error Message is displayed");
					extent.extentLoggerPass("TC_Ring_Core_193","TC_Ring_Core_193,Please enter new Pin Error Message is displayed");
					System.out.println("-----------------------------------------------------------");
					break;
					
				case 4 :
					click(UserRegistrationPage.objEnterPin(i), "Enter pin fourth Element");
					type(UserRegistrationPage.objEnterPin(i), "1", "Enter pin fourth Element");
					if (verifyElementPresent(UserRegistrationPage.objHide, "asterisk")) {
						for (int nPin = 1; nPin >= 4; nPin++) {
							String sAsterisk = getText(UserRegistrationPage.objHide(nPin));
							softAssertion.assertEquals(sAsterisk, "*");
						}
						logger.info("TC_Ring_Core_195,Pin should displayed as asterisk mark");
						extent.extentLoggerPass("TC_Ring_Core_195", "TC_Ring_Core_195,Pin should displayed as asterisk mark");
					}
						System.out.println("-----------------------------------------------------------");
					break;
					

				default: logger.info("Mpin is not entered in Any Field!!");
						 extent.extentLoggerFail("Mpin Page", "Mpin is not entered in Any Field!!");
					break;
				}
			}
			

			verifyElementPresent(UserRegistrationPage.objEnterPin, "Enter pin Data");
			String sEnteredPin = getText(UserRegistrationPage.objEnterPin);
			int nEnterPin = Integer.parseInt(sEnteredPin);
			softAssertion.assertEquals(sEnteredPin, 1111, "Return True When Both are Integers");
			logger.info("TC_Ring_Core_191, Entered Pin " + nEnterPin + " is an Integer");
			extent.extentLoggerPass("TC_Ring_Core_191", "TC_Ring_Core_191,Entered Pin " + nEnterPin + " is an Integer");
			System.out.println("-----------------------------------------------------------");


			if (verifyElementPresent(UserRegistrationPage.objReEnterPin, "Re-Enter Pin")) {
				Aclick(UserRegistrationPage.objReEnterPin, "Re-Enter Pin");
				type(UserRegistrationPage.objReEnterPin, prop.getproperty("InValidPin"), "Re-Enter Pin");
				Aclick(UserRegistrationPage.objSubmit, "Submit Button");
				verifyElementPresent(UserRegistrationPage.objSorryErrorMessage,getTextVal(UserRegistrationPage.objSorryErrorMessage, "Error Message"));
				softAssertion.assertEquals(UserRegistrationPage.objSorryErrorMessage,"Sorry! Pin does not match, please enter again");
				logger.info("TC_Ring_Core_196,Sorry! Pin does not match, please enter again Error Message is displayed");
				extent.extentLoggerPass("TC_Ring_Core_196","TC_Ring_Core_196,Sorry! Pin does not match, please enter again Error Message is displayed");
				System.out.println("-----------------------------------------------------------");
			}

			if (verifyElementPresent(UserRegistrationPage.objReEnterPin, "Re-Enter Pin")) {
				Aclick(UserRegistrationPage.objReEnterPin, "Re-Enter Pin");
				clearField(UserRegistrationPage.objReEnterPin, "Re-Enter Pin");
				type(UserRegistrationPage.objReEnterPin, prop.getproperty("ValidPin"), "Re-Enter Pin");
				setLocationConnectionToONOFF("Off");
				explicitWaitVisibilityNonDefault(UserRegistrationPage.objSubmit, 10);
				Aclick(UserRegistrationPage.objSubmit, "Submit Button");
			}
			verifyElementPresent(UserRegistrationPage.objTransactionPinSet, "Transaction Set pin");
			verifyElementPresent(UserRegistrationPage.objTransactionPinSet,getTextVal(UserRegistrationPage.objTransactionPinSet, "Text"));
			logger.info("TC_Ring_Core_197, Transaction pin is set successfully");
			extent.extentLoggerPass("TC_Ring_Core_197", "TC_Ring_Core_197,Transaction pin is set successfully");
			System.out.println("-----------------------------------------------------------");
		
			setLocationConnectionToONOFF("on");
			explicitWaitVisibilityNonDefault(HomePage.objCloseButton, 10);
			Aclick(HomePage.objCloseButton, "Close Button");
			
			logger.info("TC_Ring_Core_199, Transaction pin is set successfully with location off");
			extent.extentLoggerPass("TC_Ring_Core_199","TC_Ring_Core_199,Transaction pin is set successfully with location off");
			System.out.println("-----------------------------------------------------------");
			
			ringPayLogout();
		}
	//=========================================Promocode transaction Set pin End===========================================================	
	//==========================================Merchantflow transaction set pin===========================================================

		public void setPinMerchantFlowWithLocationOff() throws Exception {
			extent.HeaderChildNode("SetPin Merchant Flow Without Location");
			merchantPage();
			setLocationConnectionToONOFF("Off");
			Aclick(UserRegistrationPage.objSubmit, "Submit Button");
			verifyElementPresent(UserRegistrationPage.objAllowLocationPopup,getTextVal(UserRegistrationPage.objAllowLocationPopup, "Text"));
			verifyElementPresent(UserRegistrationPage.objAllowLocationBtn,getTextVal(UserRegistrationPage.objAllowLocationBtn, "Text"));
			verifyElementPresent(UserRegistrationPage.objNotNowLocationBtn,getTextVal(UserRegistrationPage.objNotNowLocationBtn, "Text"));
			click(UserRegistrationPage.objNotNowLocationBtn, "Not Now Button");
			Back(1);
			explicitWaitVisibilityNonDefault(HomePage.objCloseButton, 10);
			Aclick(HomePage.objCloseButton, "Close Button");
			String sHome = getText(HomePage.objHome);
			softAssertion.assertEquals(sHome, "Home");
			logger.info("TC_Ring_Core_201, Transaction pin is not set and Navigated to Home Page");
			extent.extentLoggerPass("TC_Ring_Core_201","TC_Ring_Core_201,Transaction pin is not set and Navigated to Home Page");
			System.out.println("-----------------------------------------------------------");
			ringPayLogout();
			setLocationConnectionToONOFF("On");
		}

		public void SetPinMerchantFlowWithLocationOn() throws Exception {
			extent.HeaderChildNode("SetPin Merchant Flow Without Location");
			merchantPage();
			setLocationConnectionToONOFF("Off");
			Aclick(UserRegistrationPage.objSubmit, "Submit Button");
			verifyElementPresent(UserRegistrationPage.objAllowLocationPopup, "Location Permission Required Text");
			verifyElementPresent(UserRegistrationPage.objAllowLocationBtn,getTextVal(UserRegistrationPage.objAllowLocationBtn, "Text"));
			verifyElementPresent(UserRegistrationPage.objNotNowLocationBtn,getTextVal(UserRegistrationPage.objNotNowLocationBtn, "Text"));
			logger.info("TC_Ring_Core_198 , Location permission Required Popup is displayed");
			extent.extentLoggerPass("TC_Ring_Core_198","TC_Ring_Core_198 , Location permission Required Popup is displayed");
			System.out.println("-----------------------------------------------------------");
			Aclick(UserRegistrationPage.objAllowLocationBtn, "Allow Button on Location Permission Required");
			Aclick(UserRegistrationPage.objOkBtn, "Ok Button");
			Aclick(UserRegistrationPage.objSubmit, "Submit");
			logger.info("TC_Ring_Core_200, Transaction pin is set successful also user completes the first transaction");
			extent.extentLoggerPass("TC_Ring_Core_200","TC_Ring_Core_200, Transaction pin is set successful also user completes the first transaction");
			System.out.println("-----------------------------------------------------------");
			explicitWaitVisibilityNonDefault(PromoCodeOfferPage.objGoToHomePage,30);
			verifyElementPresentAndClick(PromoCodeOfferPage.objGoToHomePage,getText(PromoCodeOfferPage.objGoToHomePage));
			explicitWaitVisibility(RingUserDetailPage.objCrossBtn,30);
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			verifyElementPresentAndClick(UserRegistrationPage.objNoBtn, "No Button");
			String sHome = getText(HomePage.objHome);
			softAssertion.assertEquals(sHome, "Home");
			ringPayLogout();
		}

		public void merchantPage() throws Exception {
			
			verifyElementPresent(RingLoginPage.objScanQRPage, getText(RingLoginPage.objScanQRPage));
			scannQRSwitch();

			click(RingLoginPage.objMakePaymentLetsRingItBtn, getText(RingLoginPage.objMakePaymentLetsRingItBtn));
			click(RingPayMerchantFlowPage.objAmountTextField, "Enter Amount Field");
			type(RingPayMerchantFlowPage.objAmountTextField, prop.getproperty("Amount_Merchant"), "Amount Field");
			verifyElementPresentAndClick(RingLoginPage.objMakePaymentPageProceedBtn,getText(RingLoginPage.objMakePaymentPageProceedBtn));
			verifyIsElementDisplayed(RingPayMerchantFlowPage.objLoginPageHeader,getTextVal(RingPayMerchantFlowPage.objLoginPageHeader, "Page"));
			softAssertion.assertEquals(getText(RingPayMerchantFlowPage.objLoginPageHeader), "Sign up/Login");
			logger.info("User redirected to Signup/Login Screen");
			extent.extentLogger("login", "User redirected to Signup/Login Screen");
			
			loginMobile();

			mobileNoValidation1("9" + RandomIntegerGenerator(9));
			enterOtp(prop.getproperty("OTP"));
			userDetails();
			dateOfBirth("Feb", "10", "1996");
			verifyElementPresentAndClick(RingUserDetailPage.objRegisterBtn, "Proceed Button");
			
			instaNewCommunicationAddress();
			explicitWaitVisibility(PromoCodeOfferPage.objMerchantOfferHeader,30);
			verifyElementPresent(PromoCodeOfferPage.objMerchantOfferHeader,getText(PromoCodeOfferPage.objMerchantOfferHeader));
			softAssertion.assertEquals(getText(PromoCodeOfferPage.objMerchantOfferHeader), "Woohoo, Sunil!");
			verifyElementPresent(PromoCodeOfferPage.objUnlockRingLimit, getText(PromoCodeOfferPage.objUnlockRingLimit)+ " of " + getText(PromoCodeOfferPage.objApprovedRinglimitAmt));
			verifyElementPresent(PromoCodeOfferPage.objPayingType, getText(PromoCodeOfferPage.objPayingType));
			verifyElementPresent(PromoCodeOfferPage.objUPIId, getText(PromoCodeOfferPage.objUPIId));
			verifyElementPresent(PromoCodeOfferPage.objMerchantPayAmt, getText(PromoCodeOfferPage.objMerchantPayAmt));
			verifyElementPresent(PromoCodeOfferPage.objRBIMsg,getText(PromoCodeOfferPage.objRBIMsg) + getText(PromoCodeOfferPage.objRBIMsg));
			verifyElementPresent(RingLoginPage.objIAcceptCheckBox,"I accept the Ring’s Terms & Conditions & IT Act 2000 checkbox");
			verifyElementPresentAndClick(RingLoginPage.objIAcceptCheckBox,"I accept the Ring’s Terms & Conditions & IT Act 2000 checkbox");
			verifyElementPresentAndClick(TermsAndConditionPage.objAcceptAndPayBtn,getText(TermsAndConditionPage.objAcceptText));

			if (verifyElementPresent(UserRegistrationPage.objSetPin, getTextVal(UserRegistrationPage.objSetPin, "Test"))) {
				String sSetPin = getText(UserRegistrationPage.objSetPin);
				softAssertion.assertEquals(sSetPin, "Set Pin");
				logger.info("TC_Ring_Core_189,Navigated to SET PIN screen,after clicking on Accept and Pay");
				extent.extentLoggerPass("TC_Ring_Core_189","TC_Ring_Core_189,Navigated to SET PIN screen,after clicking on Accept and Pay");
				Aclick(UserRegistrationPage.objEnterPin, "Enter Pin");
				type(UserRegistrationPage.objEnterPin, "1111", "Enter Pin");
				Aclick(UserRegistrationPage.objReEnterPin, "Re-Enter Pin");
				type(UserRegistrationPage.objReEnterPin, "1111", "Re-Enter Pin");
			}
		}

	//=============================================Transaction Set Pin End ==========================================================	
		//=========================================Check Payment multiple Cases Start====================================================
		public void repaymentMultipleCases() throws Exception {
			extent.HeaderChildNode("Check payment page multiple cases");
			getDriver().resetApp();
			enablePermissions();
			
			verifyElementPresentAndClick(RingLoginPage.objSignUpLoginBt, getText(RingLoginPage.objSignUpLoginBt));
			softAssertion.assertEquals(getText(RingPayMerchantFlowPage.objLoginPageHeader), "Sign up/Login");
			loginMobile();
			String mobNo = "6" + RandomIntegerGenerator(9);
			mobileNoValidation1(mobNo);
			enterOtp(prop.getproperty("OTP"));
			readAndAccept();
			userDetails();
			dateOfBirth("Feb", "10", "1996");
			verifyElementPresentAndClick(RingUserDetailPage.objRegisterBtn, "Proceed Button");
			
			Utilities.setPlatform = "Web";
	        new CommandBase("Chrome");
	        waitTime(4000);
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
			type(InstaLoanPage.objEmail,"shakir.muchumarri@collabera.com","Email text field");
			click(InstaLoanPage.objContinue,"Continue Button");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo = getText(InstaLoanPage.objUserRefNo);
			BrowsertearDown();
			setPlatform("Android");
			initDriver();
			instaNewCommunicationAddress();
			
			instaLoancongratsScreen("Congrats");
			instaLoanSetPin(prop.getproperty("NewMPIN"), prop.getproperty("NewMPIN"));
			
			explicitWaitVisibilityNonDefault(HomPageNew.objAdHeader, 10);
			click(HomPageNew.objAdCloseBtn, "AD Close button");
			txnApi(userRefNo,mobNo,"100");
			click(RingLoginPage.objTopMenu,"Top left menu");
			explicitWaitVisibilityNonDefault(RingLoginPage.objProfileCrossBtn,30);
			waitTime(5000);
			click(RingLoginPage.objProfileCrossBtn,"Profile Cross Button");
			explicitWaitVisibility(HomePage.noBtn,30);
			click(HomePage.noBtn,"No button");
			
			explicitWaitVisibility(HomPageNew.objPayEarlyBtn,30);
			
			click(HomPageNew.objPayEarlyBtn, "Pay Now");
			explicitWaitVisibilityNonDefault(HomPageNew.objRepaymentHeader, 10);
			Swipe("UP",1);
			click(HomPageNew.objAmtToBeRadio, "Amount to be paid radio button");
			hideKeyboard();
			hideKeyboard();
			click(HomPageNew.objNetBankingOption, "Net Banking/Debit card option");
			
				logger.info("TC_Ring_Payment_243 - Verify payment page by keeping other amount field empty");
				extent.extentLoggerPass("TC_Ring_Payment_243","TC_Ring_Payment_243 - Verify payment page by keeping other amount field empty");


			click(HomPageNew.objAmtRepayText, "Amount repay text field");
			type(HomPageNew.objAmtRepayText, "0", "Amount repay text field");
			hideKeyboard();
			click(HomPageNew.objNetBankingOption, "Net Banking/Debit card option");
			
				logger.info("TC_Ring_Payment_244 - Verify payment page by entering amount as 0 in other amount field");
				extent.extentLoggerPass("TC_Ring_Payment_244","TC_Ring_Payment_244 - Verify payment page by entering amount as 0 in other amount field");
			

			clearField(HomPageNew.objAmtRepayText, "Amount repay text field");

			click(HomPageNew.objAmtRepayText, "Amount repay text field");
			type(HomPageNew.objAmtRepayText, "7777", "Amount repay text field");
			hideKeyboard();
			click(HomPageNew.objNetBankingOption, "Net Banking/Debit card option");
			
				logger.info("TC_Ring_Payment_245 - Verify payment page by entering amount greater than Total Payable Amount");
				extent.extentLoggerPass("TC_Ring_Payment_245","TC_Ring_Payment_245 - Verify payment page by entering amount greater than Total Payable Amount");
			

			clearField(HomPageNew.objAmtRepayText, "Amount repay text field");
			click(HomPageNew.objAmtRepayText, "Amount repay text field");
			type(HomPageNew.objAmtRepayText, "10,20,", "Amount repay text field");
			hideKeyboard();
			click(HomPageNew.objNetBankingOption, "Net Banking/Debit card option");
			
				logger.info("TC_Ring_Payment_246 - Verify payment page by entering invalid amount as \"10,20,\" in other amount field");
				extent.extentLoggerPass("TC_Ring_Payment_246","TC_Ring_Payment_246 - Verify payment page by entering invalid amount as \"10,20,\" in other amount field");
			

			clearField(HomPageNew.objAmtRepayText, "Amount repay text field");
			click(HomPageNew.objAmtRepayText, "Amount repay text field");
			type(HomPageNew.objAmtRepayText, "8", "Amount repay text field");
			hideKeyboard();
			click(HomPageNew.objNetBankingOption, "Net Banking/Debit card option");
			waitTime(3000);
			click(HomPageNew.objNetBankingOption, "Net Banking/Debit card option");
			explicitWaitVisibilityNonDefault(HomePage.objNetbanking, 30);
			click(HomPageNew.objNetbanking, "Netbanking");
			explicitWaitVisibilityNonDefault(HomPageNew.objSelectBankHeader, 10);
			click(HomPageNew.objSBIBank, "SBI bank");
			click(HomPageNew.objPayNowBtn, "Pay Now Button");
			explicitWaitVisibility(HomPageNew.objFailureBtn,30);
			click(HomPageNew.objFailureBtn,"Failure Button");
			explicitWaitVisibilityNonDefault(HomPageNew.objPayFail, 10);
			logger.info("TC_Ring_Payment_247 - Verify payment page by entering amount less than 20rs if Partner is RAZORPAY or PAYNIMO");
			extent.extentLoggerPass("TC_Ring_Payment_247","TC_Ring_Payment_247 - Verify payment page by entering amount less than 20rs if Partner is RAZORPAY or PAYNIMO");
			click(HomPageNew.objTryAgain, "Try Again button");
			Back(3);

			explicitWaitVisibilityNonDefault(HomPageNew.objRepaymentHeader, 10);
			click(HomPageNew.objAmtRepayText, "Amount to be paid text field");
			clearField(HomPageNew.objAmtRepayText, "Amount to be paid text field");
			type(HomPageNew.objAmtRepayText, "20", "Amount to be paid text field");
			hideKeyboard();
			click(HomPageNew.objNetBankingOption, "Net Banking/Debit card option");
			waitTime(3000);
			click(HomPageNew.objNetBankingOption, "Net Banking/Debit card option");
			explicitWaitVisibilityNonDefault(HomPageNew.objNetbanking, 10);
			logger.info("TC_Ring_Payment_248 - Verify payment page by entering amount equal to 20rs");
			extent.extentLoggerPass("TC_Ring_Payment_248","TC_Ring_Payment_248 - Verify payment page by entering amount equal to 20rs");
			click(HomPageNew.objNetbanking, "Netbanking");
			explicitWaitVisibilityNonDefault(HomPageNew.objSelectBankHeader, 10);
			click(HomPageNew.objSBIBank, "SBI bank");
			click(HomPageNew.objPayNowBtn, "Pay Now Button");
			explicitWaitVisibilityNonDefault(HomPageNew.objSuccessBtn, 10);
			click(HomPageNew.objSuccessBtn, "Success Button");
			explicitWaitVisibilityNonDefault(HomPageNew.objRepaySuccess, 10);
			click(HomPageNew.objHomePage, "Go to homepage button");
			explicitWaitClickable(HomPageNew.objPayEarlyBtn, 10);
			click(HomPageNew.objPayEarlyBtn, "Pay Early Button");
			explicitWaitVisibilityNonDefault(HomPageNew.objRepaymentHeader, 10);
			waitTime(3000);
			click(HomPageNew.objViewDetails, "View Details");
			explicitWaitVisibilityNonDefault(HomPageNew.objPayDate, 10);
			explicitWaitVisibilityNonDefault(HomPageNew.objTotSpends, 10);
			explicitWaitVisibilityNonDefault(HomPageNew.objTransacFee, 10);
			explicitWaitVisibilityNonDefault(HomPageNew.objTotAmt, 10);
			logger.info("TC_Ring_Payment_249 - To Verify user clicks on View Details on Payment screen");
			extent.extentLoggerPass("TC_Ring_Payment_249","TC_Ring_Payment_249 - To Verify user clicks on View Details on Payment screen");
			Back(2);
			explicitWaitVisibilityNonDefault(RingLoginPage.objAvailLimitHeader, 10);
			ringPayLogout();
		}

	//=============================================Check Payment multiple Cases End===================================================	

		//================================================Bank transfer Flow Start========================================================
		public void BankTransferModule(String accountNo, String name) throws Exception {
			extent.HeaderChildNode("RingPay Bank Transfer Flow");
			getDriver().resetApp();
			enablePermissions();
			verifyElementPresentAndClick(RingLoginPage.objSignUpLoginBt, getText(RingLoginPage.objSignUpLoginBt));
			
			loginMobile();
			mobileNoValidation1("7" + RandomIntegerGenerator(9));
			enterOtp(prop.getproperty("OTP"));
			readAndAccept();
			userDetails();
			dateOfBirth("Feb", "10", "1996");
			click(UserRegistrationNew.objProceed, "Proceed Button");
			instaNewCommunicationAddress();
			instaLoancongratsScreen("Congrats");
			instaLoanSetPin(prop.getproperty("NewMPIN"), prop.getproperty("NewMPIN"));
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			String sHome = getText(HomePage.objHome);
			softAssertion.assertEquals(sHome, "Home");

			click(BankTransferModule.objMoretTab, "Clicked On More Button");
			verifyElementPresent(BankTransferModule.objBankTransfer,getTextVal(BankTransferModule.objBankTransfer, "Module"));
			click(BankTransferModule.objBankTransfer, "Bank Transfer Module");
			logger.info("Navigated to Add Bank Account Page");
			extent.extentLogger("Account Page", "Navigated to Add Bank Account Page");
			logger.info("TC_Ring_Core_250, To Verify when user clicks on bank transfer ");
			extent.extentLogger("TC_Ring_Core_250", "TC_Ring_Core_250, To Verify when user clicks on bank transfer ");
			logger.info("'TC_Ring_Core_257' To verify user enter valid account No and invalid account holder name as per user name");
			extent.extentLogger("TC_Ring_Core_257","'TC_Ring_Core_257' To verify user enter valid account No and invalid account holder name as per user name");
			addbankAccount(accountNo, name);

			String buttonName = getText(BankTransferModule.objOkayBtn);
			if (buttonName.equalsIgnoreCase("okay") || buttonName.equalsIgnoreCase("Re-Enter Bank Details") || buttonName.equalsIgnoreCase("Add another Bank Account")) {
				click(BankTransferModule.objOkayBtn, getTextVal(BankTransferModule.objOkayBtn, "Button"));
				explicitWaitVisibilityNonDefault(BankTransferModule.objAddNewBankAccountBtn, 10);
				click(BankTransferModule.objAddNewBankAccountBtn,getTextVal(BankTransferModule.objAddNewBankAccountBtn, "Button"));
				addbankAccount("1", "Sunil");
				logger.info("'TC_Ring_Core_258'To verify user enter invalid account No and Valid account holder name as per user name");
				extent.extentLogger("TC_Ring_Core_258","'TC_Ring_Core_258'To verify user enter invalid account No and Valid account holder name as per user name");
				click(BankTransferModule.objOkayBtn, getTextVal(BankTransferModule.objOkayBtn, "Button"));
				logger.info("'TC_Ring_Core_259' To Verify user clicks on Okay got button error validation screen ");
				extent.extentLogger("TC_Ring_Core_259","'TC_Ring_Core_259' To Verify user clicks on Okay got button error validation screen ");
			}
			Back(2);
			explicitWaitVisibilityNonDefault(BankTransferModule.objMoretTab, 10);
			click(BankTransferModule.objMoretTab, "Clicked On More Button");
			explicitWaitVisibilityNonDefault(BankTransferModule.objBankTransfer, 10);
			click(BankTransferModule.objBankTransfer, "Bank Transfer Module");
			click(BankTransferModule.objAddNewBankAccountBtn,getTextVal(BankTransferModule.objAddNewBankAccountBtn, "Button"));
			logger.info("'TC_Ring_Core_253' To verify user enter valid account No and Valid account holder name as per user name");
			extent.extentLogger("TC_Ring_Core_253","'TC_Ring_Core_253' To verify user enter valid account No and Valid account holder name as per user name");
			addbankAccount("5", "Sunil");
			logger.info("'TC_Ring_Core_257' To verify user enter valid account No and invalid account holder name as per user name");
			extent.extentLogger("TC_Ring_Core_257","'TC_Ring_Core_257' To verify user enter valid account No and invalid account holder name as per user name");
			
			explicitWaitVisibilityNonDefault(BankTransferModule.objMakePaymentPage, 10);
			verifyElementPresent(BankTransferModule.objMakePaymentPage, "Make Payment Page Displayed");

			click(BankTransferModule.objEnterAmountTextField, "Amount Text Field");
			type(BankTransferModule.objEnterAmountTextField, "1", "Amount Text Field");
			logger.info(getTextVal(BankTransferModule.objTransactionFeeMsg, "Fee Message is Displayed"));
			extent.extentLogger("Fee Message",getTextVal(BankTransferModule.objTransactionFeeMsg, "Fee Message is Displayed"));
			click(BankTransferModule.objPayNowBtn, "Pay Now Button");
			logger.info("TC_Ring_Core_254,TC_Ring_Core_255 - To verify user continue on make payment page");
			extent.extentLogger("TC_Ring_Core_254,TC_Ring_Core_255", "TC_Ring_Core_254,TC_Ring_Core_255 - To verify user continue on make payment page");

			// Confirm payment Page
			verifyElementPresent(BankTransferModule.objEnterPinPage, "Confirm Payment Page");
			logger.info("User Navigaed to Confirm Payment Page");
			extent.extentLogger("Enter Pin", "User Navigaed to Confirm Payment Page");
			explicitWaitVisibilityNonDefault(BankTransferModule.objEnterPinTextField, 10);
			click(BankTransferModule.objEnterPinTextField, "Enter Pin Field");
			type(BankTransferModule.objEnterPinTextField, "1234", "Enter Pin Field");
			click(BankTransferModule.objProceedBtn, "Proceed Button");
			logger.info("TC_Ring_Core_256 To Verify user should able to continue with valid Pin");
			extent.extentLogger("TC_Ring_Core_256","TC_Ring_Core_256 To Verify user should able to continue with valid Pin");

			verifyElementPresent2(BankTransferModule.objStatusPage, "Payment Status Page");
			click(BankTransferModule.objHomePageBtn, "Go to Homepage");
			waitTime(2000);
			if (checkElementExist1(BankTransferModule.objEnjoyingPopup, "Are you Enjoying pop up")) {
				click(BankTransferModule.objYesBtn, "Yes Button");
				click(BankTransferModule.objRateExperience, "Rate Your Experience pop up");
			}
			ringPayLogout();
		}

		public String addbankAccount(String accountNo, String name) throws Exception {
			explicitWaitVisibilityNonDefault(BankTransferModule.objIfscTextField, 20);
			click(BankTransferModule.objIfscTextField, "IFSC Code Text Field");
			type(BankTransferModule.objIfscTextField, "SBIN4566777", "IFSC Code Text Field");
			logger.warn(getTextVal(BankTransferModule.objIFSCErrorMsg, "Error Message"));
			extent.extentLoggerPass("Error Message", getTextVal(BankTransferModule.objIFSCErrorMsg, "Error Message"));
			clearField(BankTransferModule.objIfscTextField, "IFSC Code Text Field");
			type(BankTransferModule.objIfscTextField, "SBIN0004064", "IFSC Code Text Field");
			logger.info("Branch Name AutoPopulated " + getText(BankTransferModule.objBranchName));
			extent.extentLogger("BranchName", "Branch Name AutoPopulated " + getText(BankTransferModule.objBranchName));
			logger.info("TC_Ring_Core_251,TC_Ring_Core_252 To verify User enter valid and invalid IFSC code ");
			extent.extentLogger("TC_Ring_Core_251,TC_Ring_Core_252","TC_Ring_Core_251,TC_Ring_Core_252 To verify User enter valid and invalid IFSC code ");

			explicitWaitVisibilityNonDefault(BankTransferModule.objAccountNumberField, 10);
			click(BankTransferModule.objAccountNumberField, "Account Number Field");
			accountNo = accountNo + RandomIntegerGenerator(5);
			type(BankTransferModule.objAccountNumberField, accountNo, "Account Number Text Field");

			explicitWaitVisibilityNonDefault(BankTransferModule.ObjConfirmAccountNumberField, 10);
			click(BankTransferModule.ObjConfirmAccountNumberField, "Confirm Account Number Field");
			type(BankTransferModule.ObjConfirmAccountNumberField, accountNo, "Account Number Text Field");
			hideKeyboard();
			waitTime(2000);
			Swipe("Up", 2);
			waitTime(2000);
			click(BankTransferModule.objAccountHolderName, "Account Holder Name");
			type(BankTransferModule.objAccountHolderName, name, "Account Number Text Field");
			hideKeyboard();
			doubleTap(BankTransferModule.objAccountTypeField, 2, "Account Type Field");
			explicitWaitVisibilityNonDefault(BankTransferModule.objSavings, 10);
			click(BankTransferModule.objSavings, "Savings Type");
//	        if(!checkElementExist(BankTransferModule.objSavings))
//	        {
//	            click(BankTransferModule.objAccountTypeField, "Account Type");
//	            click(BankTransferModule.objSavings, "Savings Type");
//	        }    
			click(BankTransferModule.objContinueButton, "Continue Button");
			explicitWaitVisibilityNonDefault(BankTransferModule.objBottomSheetPopup, 10);
			verifyElementExist(BankTransferModule.objBottomSheetPopup, "Verify Bank Details Pop Up");
			click(BankTransferModule.objConfirmBtn, "Confirm Button");
			return accountNo;
		}

	//===============================================Bank transfer flow ends==========================================================	
				
	//===============================================Add Address Flow Start===============================================================
		public void addAddressFlow() throws Exception {
			extent.HeaderChildNode("RingPay Add Address Flow");
			getDriver().resetApp();
			enablePermissions();
			verifyElementPresentAndClick(RingLoginPage.objSignUpLoginBt, getText(RingLoginPage.objSignUpLoginBt));
			loginMobile();
			mobileNoValidation1("9"+RandomIntegerGenerator(9));
			enterOtp(prop.getproperty("OTP"));
			readAndAccept();
			
			userDetails();
			dateOfBirth("Feb", "10", "1996");
			click(UserRegistrationNew.objProceed, "Proceed Button");
			

			verifyElementPresent2(AddAddresPage.objAddressHeader, "Add Current Address Page Header");
			logger.info("TC_Ring_Core_116 - To verfiy Address page");
			extent.extentLoggerPass("TC_Ring_Core_116", "TC_Ring_Core_116 - To verfiy Address page");
				
			verifyElementExist(AddAddresPage.objRoomNoTextField,getTextVal(AddAddresPage.objRoomNoTextField, "Text Field"));
			click(AddAddresPage.objRoomNoTextField, getTextVal(AddAddresPage.objRoomNoTextField, "Text Field"));
			type(AddAddresPage.objRoomNoTextField, "", getTextVal(AddAddresPage.objRoomNoTextField, "Text Field"));
			click(AddAddresPage.objSubmitButton, "Submit Button");
			logger.info(getTextVal(AddAddresPage.objMandatoryWarnMessage, "Warning Message Displayed"));
			extent.extentLogger("Rooom No", getTextVal(AddAddresPage.objMandatoryWarnMessage, "Warning Message"));
			
			type(AddAddresPage.objRoomNoTextField, "@#%^&#$",getTextVal(AddAddresPage.objRoomNoTextField, "Text Field"));
			logger.info(getTextVal(AddAddresPage.objRoomNoTextField, "Special Character Warning Message"));
			extent.extentLogger("Rooom No",getTextVal(AddAddresPage.objRoomNoTextField, "Special Character Warning Message"));
			clearField(AddAddresPage.objRoomNoField, getTextVal(AddAddresPage.objRoomNoTextField, "Text Field"));
			type(AddAddresPage.objRoomNoTextField, "86", getTextVal(AddAddresPage.objRoomNoTextField, "Text Field"));
			logger.info("TC_Ring_Core_117 - To verify room no and building name field with valid data");
			extent.extentLoggerPass("TC_Ring_Core_117","TC_Ring_Core_117 - To verify room no and building name field with valid data");

			verifyElementExist(AddAddresPage.objAddressLineOneTextField,getTextVal(AddAddresPage.objAddressLineOneTextField, "Text Field"));
			click(AddAddresPage.objAddressLineOneTextField,getTextVal(AddAddresPage.objAddressLineOneTextField, "Text Field"));
			type(AddAddresPage.objAddressLineOneTextField, "",getTextVal(AddAddresPage.objAddressLineOneTextField, "Text Field"));
			type(AddAddresPage.objAddressLineOneTextField, "@#%^&#$",getTextVal(AddAddresPage.objAddressLineOneTextField, "Text Field"));
			logger.info(getTextVal(AddAddresPage.objMinThreeCharWarnMessage, "Warning Message"));
			extent.extentLogger("Address Line 1",getTextVal(AddAddresPage.objMinThreeCharWarnMessage, "Warning Message"));
			clearField(AddAddresPage.objAddressLineOneField,getTextVal(AddAddresPage.objAddressLineOneField, "Text Field"));
			type(AddAddresPage.objAddressLineOneTextField, "44, Borivali",getTextVal(AddAddresPage.objAddressLineOneTextField, "Text Field"));
			logger.info("TC_Ring_Core_118 - To verify address 1 field with valid data");
			extent.extentLoggerPass("TC_Ring_Core_118", "TC_Ring_Core_118 - To verify address 1 field with valid data");

			verifyElementExist(AddAddresPage.objAddressLineTwoTextField,getTextVal(AddAddresPage.objAddressLineTwoTextField, "Field"));
			click(AddAddresPage.objAddressLineTwoTextField,getTextVal(AddAddresPage.objAddressLineTwoTextField, "Text Field"));
			type(AddAddresPage.objAddressLineTwoTextField, "",getTextVal(AddAddresPage.objAddressLineTwoTextField, "Text Field"));
			type(AddAddresPage.objAddressLineTwoTextField, "@#%^&#$",getTextVal(AddAddresPage.objAddressLineTwoTextField, "Text Field"));
			logger.info(getTextVal(AddAddresPage.objSpclCharWarnMessage, "Warning Message"));
			extent.extentLogger("Address Line 2", getTextVal(AddAddresPage.objSpclCharWarnMessage, "Warning Message"));
			clearField(AddAddresPage.objAddressLineTwoField,getTextVal(AddAddresPage.objAddressLineTwoField, "Text Field"));
			type(AddAddresPage.objAddressLineTwoField, "Vasa Street",getTextVal(AddAddresPage.objAddressLineTwoField, "Text Field"));
			logger.info("TC_Ring_Core_119 - To verify address 2 field with valid data");
			extent.extentLoggerPass("TC_Ring_Core_119", "TC_Ring_Core_119 - To verify address 2 field with valid data");

			verifyElementExist(AddAddresPage.objPincodeTextField,getTextVal(AddAddresPage.objPincodeTextField, "Text Field"));
			click(AddAddresPage.objPincodeTextField, getTextVal(AddAddresPage.objPincodeTextField, "Text Field"));
			type(AddAddresPage.objPincodeTextField, "", getTextVal(AddAddresPage.objPincodeTextField, "Text Field"));
			type(AddAddresPage.objPincodeTextField, "255552",getTextVal(AddAddresPage.objPincodeTextField, "Text Field"));
			logger.info(getTextVal(AddAddresPage.objInvalidPinCode, "Warning Message"));
			extent.extentLogger("Pincode Field", getTextVal(AddAddresPage.objInvalidPinCode, "Warning Message"));
			clearField(AddAddresPage.objpincodeClr, getTextVal(AddAddresPage.objPincodeTextField, "Text Field"));
			type(AddAddresPage.objpincodeClr, "252", getTextVal(AddAddresPage.objpincodeClr, "Text Field"));
			logger.info(getTextVal(AddAddresPage.objSixDigitWarnMessage, "Warning Message"));
			extent.extentLogger("Pincode Field", getTextVal(AddAddresPage.objSixDigitWarnMessage, "Warning Message"));
			clearField(AddAddresPage.objpincodeClr, getTextVal(AddAddresPage.objPincodeTextField, "Text Field"));
			type(AddAddresPage.objPincodeTextField, "560076",getTextVal(AddAddresPage.objPincodeTextField, "Text Field"));
			hideKeyboard();

			verifyElementExist(AddAddresPage.objCityTextField, getTextVal(AddAddresPage.objCityTextField, "Field"));
			explicitWaitVisibilityNonDefault(AddAddresPage.objPopulatedCity, 10);
			logger.info(getTextVal(AddAddresPage.objPopulatedCity, "City Name autoPopulated after entering pincode"));
			extent.extentLogger("CityName",getTextVal(AddAddresPage.objPopulatedCity, "City Name autoPopulated after entering pincode"));

			verifyElementExist(AddAddresPage.objStateTextField, getTextVal(AddAddresPage.objStateTextField, "Field"));
			explicitWaitVisibilityNonDefault(AddAddresPage.objPopulatedState, 10);
			logger.info(getTextVal(AddAddresPage.objPopulatedState, "State Name autoPopulated after entering pincode"));
			extent.extentLogger("StateName",getTextVal(AddAddresPage.objPopulatedState, "State Name autoPopulated after entering pincode"));
			logger.info("TC_Ring_Core_121,TC_Ring_Core_122 - To verify pincode field with invalid and valid pincode");
			extent.extentLoggerPass("TC_Ring_Core_121,TC_Ring_Core_122,","TC_Ring_Core_121,TC_Ring_Core_122 - To verify pincode field with invalid and valid pincode");

			explicitWaitVisibilityNonDefault(AddAddresPage.objSubmitButton, 10);
			click(AddAddresPage.objSubmitButton, "Submit Button");
			logger.info("TC_Ring_Core_123 - To verfiy user clicks on submit button after entering valid address details");
			extent.extentLoggerPass("TC_Ring_Core_123","TC_Ring_Core_123 - To verfiy user clicks on submit button after entering valid address details");
			
			
			instaLoancongratsScreen("Congrats");
			instaLoanSetPin(prop.getproperty("NewMPIN"), prop.getproperty("NewMPIN"));
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			String sHome = getText(HomePage.objHome);
			softAssertion.assertEquals(sHome, "Home");
			ringPayLogout();
		}

	//================================================Add Address Flow End=================================================================================================
	
	
		//===============================================User Scan and Pay Start===============================================================================================	
		public void userScanAndPayTransactions() throws Exception {
			extent.HeaderChildNode("SetPin Merchant Flow Without Location");
			getDriver().resetApp();
			onBoarding();
		
			verifyElementPresent(HomePage.objScanAndPay, "Scan And Pay Button");
			String scanQrEnabled = getAttributValue("enabled", HomePage.objScanAndPay);
			softAssertion.assertEquals("true", scanQrEnabled);
			click(HomePage.objScanAndPay, "Scan And Pay");
			logger.info("TC_Ring_Core_203 , To Verify Scan and Pay button available on bottom of the  HomeScreen is enable and clickable");
			extent.extentLoggerPass("TC_Ring_Core_203","TC_Ring_Core_203, To Verify Scan and Pay button available on bottom of the  HomeScreen is enable and clickable");
			System.out.println("-----------------------------------------------------------");

			explicitWaitVisibilityNonDefault(HomePage.objScanAnyQRToPay, 10);
			String sScanAnyQRCode = getText(HomePage.objScanAnyQRToPay);
			softAssertion.assertEquals("Scan any QR code to pay", sScanAnyQRCode);
			verifyElementPresent(HomePage.objScanAnyQRToPay, getTextVal(HomePage.objScanAnyQRToPay, "Text"));
			logger.info("TC_Ring_Core_204, Camera Scanner should get opened to Scan and Pay");
			extent.extentLoggerPass("TC_Ring_Core_204","TC_Ring_Core_204,Camera Scanner should get opened to Scan and Pay");
			System.out.println("-----------------------------------------------------------");

			scannQRSwitch();
			if (verifyElementPresent2(RingPayMerchantFlowPage.objCreditAtZeroPopUp,"Credit at zero Charges popup")) {
				logger.info("TC_Ring_Core_191, Credit at zero Charges popup is Displayed");
				extent.extentLoggerPass("TC_Ring_Core_191","TC_Ring_Core_191,Credit at zero Charges popup is Displayed");
				System.out.println("-----------------------------------------------------------");
			} else {
				logger.info("TC_Ring_Core_191, Credit at zero Charges popup is not Displayed");
				extent.extentLoggerPass("TC_Ring_Core_191","TC_Ring_Core_191,Credit at zero Charges popup is not Displayed");
				System.out.println("-----------------------------------------------------------");
			}

				explicitWaitVisibilityNonDefault(PaymentPage.objUseCreditLimitText, 30);
				if (verifyElementPresent(PaymentPage.objUseCreditLimitText,getTextVal(RingPayMerchantFlowPage.objUseCreditLimitText, "Text"))) {
					verifyElementPresent(PaymentPage.objMerchantName,getTextVal(RingPayMerchantFlowPage.objPayTypeMethod, "is Merchant Name"));
					verifyElementPresent(PaymentPage.objUpiID, getTextVal(RingPayMerchantFlowPage.objUpiID, "is UPI Id"));
					Back(1);
					verifyElementPresent(PaymentPage.objRupeesLogo, getTextVal(PaymentPage.objRupeesLogo, "Rupees Logo"));
					verifyElementPresent(PaymentPage.objTransactionFeeMsg,getTextVal(PaymentPage.objTransactionFeeMsg, "Text"));
					verifyElementPresent(PaymentPage.objDisabledPayNowButton, "Disabled pay Now Button");
					hideKeyboard();
					logger.info("TC_Ring_Core_207, Payment Screen Validated");
					extent.extentLoggerPass("TC_Ring_Core_207", "TC_Ring_Core_207,Payment Screen Validated");
					System.out.println("-----------------------------------------------------------");

				}
				if (verifyElementPresent(PaymentPage.objAmountTextField, "Amount Text Field")) {
					Aclick(PaymentPage.objAmountTextField, "Amount Field");
					type(PaymentPage.objAmountTextField, prop.getproperty("HigherAmount_Merchant"), "Amount Field");
					verifyElementPresent(RingPayMerchantFlowPage.objTransactionMsg,getTextVal(RingPayMerchantFlowPage.objTransactionMsg, "Error Message"));
					String sErrorMessage1 = getText(RingPayMerchantFlowPage.objTransactionMsg);
					softAssertion.assertEquals(sErrorMessage1,"You have entered a higher amount than your available limit. Re-enter amount.");
					hideKeyboard();
					logger.info("TC_Ring_Core_208, You have entered a higher amount than your available limit. Re-enter amount. Error message should be displayed");
					extent.extentLoggerPass("TC_Ring_Core_208","TC_Ring_Core_208,You have entered a higher amount than your available limit. Re-enter amount. Error message should be displayed");
					System.out.println("-----------------------------------------------------------");
				}
				if (verifyElementPresent(PaymentPage.objAmountTextField, "Amount Text Field")) {
					Aclick(PaymentPage.objAmountTextField, "Amount Field");
					clearField(PaymentPage.objAmountTextField, "Amount Field");
					type(PaymentPage.objAmountTextField, prop.getproperty("ZeroAmount_Merchant"), "Amount Field");
					verifyElementPresent(RingPayMerchantFlowPage.objTransactionMsg,getTextVal(RingPayMerchantFlowPage.objTransactionMsg, "Error Message"));
					String sErrorMessage2 = getText(RingPayMerchantFlowPage.objTransactionMsg);
					softAssertion.assertEquals(sErrorMessage2, "Please enter amount greater than 0");
					hideKeyboard();
					logger.info("TC_Ring_Core_209, Please enter amount greater than 0 Error Message is Displayed");
					extent.extentLoggerPass("TC_Ring_Core_209","TC_Ring_Core_209, Please enter amount greater than 0 Error Message is Displayed");
					System.out.println("-----------------------------------------------------------");
				}
				if (verifyElementPresent(PaymentPage.objAmountTextField, "Amount Text Field")) {
					Aclick(PaymentPage.objAmountTextField, "Amount Field");
					clearField(PaymentPage.objAmountTextField, "Amount Field");
					type(PaymentPage.objAmountTextField, prop.getproperty("Amount_Merchant"), "Amount Field");
					verifyElementPresent(PaymentPage.objTransactionMsg,getTextVal(PaymentPage.objTransactionMsg, "Message"));
					String sPayNow = getAttributValue("enabled", PaymentPage.objTransactionMsg);
					softAssertion.assertEquals("true", sPayNow);
					logger.info("TC_Ring_Core_210, Pay Now button should be enabled after entering the valid amount");
					extent.extentLoggerPass("TC_Ring_Core_210","TC_Ring_Core_210, Pay Now button should be enabled after entering the valid amount");
					System.out.println("-----------------------------------------------------------");
					hideKeyboard();
				}
				
				doubleTap(PaymentPage.objPayNowBtn2, 2, "Pay Now Button");
				
				logger.info("TC_Ring_Core_211, Navigated to Confirm Payment Screen to enter the Security PIN for transaction");
				extent.extentLoggerPass("TC_Ring_Core_211","TC_Ring_Core_211, Navigated to Confirm Payment Screen to enter the Security PIN for transaction");
				System.out.println("-----------------------------------------------------------");
				
				if (verifyElementPresent(PaymentPage.objConfirmPayment,getTextVal(PaymentPage.objConfirmPayment, "Text"))) {
					click(PaymentPage.objEditTransactionPin, "Edit Pin Field");
					type(PaymentPage.objEditTransactionPin, "1234", "Edit Pin Field");
					click(PaymentPage.objContinue, "Continue Button");
					verifyElementPresent(PaymentPage.objIncorrectPin,getTextVal(PaymentPage.objIncorrectPin, "Error Message"));
					String sIncorrectPin = getText(PaymentPage.objIncorrectPin);
					softAssertion.assertEquals("Incorrect PIN", sIncorrectPin);
					logger.info("TC_Ring_Core_212, Incorrect PIN Error message should be Displayed");
					extent.extentLoggerPass("TC_Ring_Core_212","TC_Ring_Core_212, Incorrect PIN Error message should be Displayed");
					System.out.println("-----------------------------------------------------------");
				}
				if (verifyElementPresent(PaymentPage.objForgotPin, getTextVal(PaymentPage.objForgotPin, "Text"))) {
					click(PaymentPage.objForgotPin, "Forget PIN?");
					verifyElementPresent(PaymentPage.objChangePin, getTextVal(PaymentPage.objChangePin, "text"));
					String sChangePin = getText(PaymentPage.objChangePin);
					softAssertion.assertEquals("Change Pin", sChangePin);
					logger.info("TC_Ring_Core_213, Navigated to Change Pin Screen");
					extent.extentLoggerPass("TC_Ring_Core_213", "TC_Ring_Core_213, Navigated to Change Pin Screen");
					System.out.println("-----------------------------------------------------------");
				}
				if (verifyElementPresent(PaymentPage.objChangePin, getTextVal(PaymentPage.objChangePin, "Text"))) {
					click(PaymentPage.objEnterNewPin, "Enter New Pin");
					type(PaymentPage.objEnterNewPin, "1111", "Enter New Pin Field");
					click(PaymentPage.objReEnterNewPin, "Re-Enter New Pin");
					type(PaymentPage.objReEnterNewPin, "1111", "Re-Enter New Pin Field");
					click(PaymentPage.objContinue, "Continue Button");
					if (verifyElementPresent(PaymentPage.objPleaseEnterOtp,getTextVal(PaymentPage.objPleaseEnterOtp, "Text"))) {
						String sErrorMessage = getText(PaymentPage.objPleaseEnterOtp);
						softAssertion.assertEquals("Please enter OTP", sErrorMessage);
						logger.info("TC_Ring_Core_214, Please enter OTP error message is displayed");
						extent.extentLoggerPass("TC_Ring_Core_214","TC_Ring_Core_199, Please enter OTP error message is displayed");
						System.out.println("-----------------------------------------------------------");
					}
					Swipe("DOWN", 1);
					click(PaymentPage.objEnterOtp, "Enter OTP Field");
					type(PaymentPage.objEnterOtp, "123456", "Enter OTP Field");
					hideKeyboard();
					click(PaymentPage.objEnterNewPin, "Enter New Pin");
					type(PaymentPage.objEnterNewPin, "1111", "Enter New Pin Field");
					hideKeyboard();
					click(PaymentPage.objContinue, "Continue Button");
					verifyElementPresent(PaymentPage.objInvalidOtpMsg,getTextVal(PaymentPage.objInvalidOtpMsg, "Text"));
					String sInvalidOtpErrorMessage = getText(PaymentPage.objInvalidOtpMsg);
					softAssertion.assertEquals("Invalid Otp, Please enter correct otp.", sInvalidOtpErrorMessage);
					logger.info("TC_Ring_Core_215, Invalid Otp, Please enter correct otp. Error Message is displayed");
					extent.extentLoggerPass("TC_Ring_Core_215","TC_Ring_Core_215, Invalid Otp, Please enter correct otp. Error Message is displayed");
					System.out.println("-----------------------------------------------------------");
						
					click(PaymentPage.objEnterOtp, "Enter OTP Field");
					clearField(PaymentPage.objEnterOtp, "Enter OTP Field");
					type(PaymentPage.objEnterOtp, "888888", "Enter OTP Field");
					hideKeyboard();
					click(PaymentPage.objEnterNewPin, "Enter New Pin");
					type(PaymentPage.objEnterNewPin, "1111", "Enter New Pin Field");
					hideKeyboard();
					explicitWaitVisibilityNonDefault(PaymentPage.objReEnterNewPin, 10);
					click(PaymentPage.objReEnterNewPin, "Re-Enter New Pin");
					clearField(PaymentPage.objReEnterNewPin, "Re-Enter New Pin");
					type(PaymentPage.objReEnterNewPin, "1234", "Re-Enter New Pin Field");
					hideKeyboard();
					click(PaymentPage.objContinue, "Continue Button");
					String sEnterSamePinMessage = getText(PaymentPage.objEnterSamePin);
					softAssertion.assertEquals("Please enter same PIN in both fields", sEnterSamePinMessage);
					logger.info("TC_Ring_Core_216, Please enter same PIN in both fields Error Message is displayed");
					extent.extentLoggerPass("TC_Ring_Core_216","TC_Ring_Core_216, Please enter same PIN in both fields Error Message is displayed");
					System.out.println("-----------------------------------------------------------");
					click(PaymentPage.objReEnterNewPin, "Re-Enter New Pin");
					clearField(PaymentPage.objReEnterNewPin, "Re-Enter New Pin");
					type(PaymentPage.objReEnterNewPin, "1111", "Re-Enter New Pin Field");
					click(PaymentPage.objContinue, "Continue Button");
					explicitWaitVisibilityNonDefault(PaymentPage.objConfirmPayment, 10);
					logger.info("TC_Ring_Core_218, Navigated back to Confirm Payment Screen from Change Pin Screen");
					extent.extentLoggerPass("TC_Ring_Core_218","TC_Ring_Core_218, Navigated back to Confirm Payment Screen from Change Pin Screen");
					System.out.println("-----------------------------------------------------------");
				}
				if (verifyElementPresent(PaymentPage.objConfirmPayment,getTextVal(PaymentPage.objConfirmPayment, "Text"))) {
					click(PaymentPage.objEditTransactionPin, "Edit Pin Field");
					type(PaymentPage.objEditTransactionPin, "1111", "Edit Pin Field");
					click(PaymentPage.objContinue, "Continue Button");
					if (verifyElementPresent(PaymentPage.objPaymentDone,getTextVal(PaymentPage.objPaymentDone, "Text"))) {
						String sPaymentDone = getText(PaymentPage.objPaymentDone);
						softAssertion.assertEquals("Payment Done!", sPaymentDone);
						verifyElementPresent(PaymentPage.objGreenTickMark, "Payment Done green tick mark");
						verifyElementPresent(PaymentPage.objRupeesLogo,getTextVal(PaymentPage.objRupeesLogo, "is Rupees Logo"));
						verifyElementPresent(PaymentPage.objMerchantNameReceipt,getTextVal(PaymentPage.objMerchantNameReceipt, "Text"));
						verifyElementPresent(PaymentPage.objTaxDateAndTime,getTextVal(PaymentPage.objTaxDateAndTime, "is Tax date and time"));
						verifyElementPresent(PaymentPage.objSeekBar, "Seek Bar");
						verifyElementPresent(PaymentPage.objAvailableLimit,getTextVal(PaymentPage.objAvailableLimit, "Text"));
						verifyElementPresent(PaymentPage.ObjAvailableLimitAmount,getTextVal(PaymentPage.ObjAvailableLimitAmount, "is Available Limit Amount"));
						verifyElementPresent(PaymentPage.objGoToHomePage,getTextVal(PaymentPage.objGoToHomePage, "Text"));
						logger.info("TC_Ring_Core_219, Validation of Payment Done Screen");
						extent.extentLoggerPass("TC_Ring_Core_219","TC_Ring_Core_219, Validation of Payment Done Screen");
						System.out.println("-----------------------------------------------------------");
					}
					if (verifyElementPresent(PaymentPage.objGoToHomePage,getTextVal(PaymentPage.objGoToHomePage, "Text"))) {
						click(PaymentPage.objGoToHomePage, "Go To HomePage");
						explicitWaitVisibilityNonDefault(UserRegistrationPage.objNoBtn, 20);
						verifyElementPresentAndClick(UserRegistrationPage.objNoBtn,getText(UserRegistrationPage.objNoBtn));
						logger.info("TC_Ring_Core_221, Navigation to HomeScreen");
						extent.extentLoggerPass("TC_Ring_Core_221", "TC_Ring_Core_221, Navigation to HomeScreen");
						System.out.println("-----------------------------------------------------------");
					}
				}
				paymentDeclinedFlow();
				homeScreenValidation();
			}
			

		public void homeScreenValidation() throws Exception {
			extent.HeaderChildNode("Home Screen Validation");
			explicitWaitVisibilityNonDefault(HomePage.objHome, 10);
			String sHome = getText(HomePage.objHome);
			softAssertion.assertEquals(sHome, "Home");
			if (verifyElementPresent(HomePage.objHome, "Home")) {
				verifyElementPresent(HomePage.objAvailableLimit, getTextVal(HomePage.objAvailableLimit, "Text"));
				verifyElementPresent(HomePage.ObjAvailableLimitAmount,getTextVal(HomePage.ObjAvailableLimitAmount, "is Available Limit Amount"));
				verifyElementPresent(HomePage.objCurrentSpends, getTextVal(HomePage.objCurrentSpends, "Text"));
				verifyElementPresent(HomePage.ObjCurrentSpendsAmount,getTextVal(HomePage.ObjCurrentSpendsAmount, "is Current Spends Amount"));
				verifyElementPresent(HomePage.objRecentTransactions, getTextVal(HomePage.objRecentTransactions, "Text"));
				verifyElementPresent(HomePage.objViewAll, getTextVal(HomePage.objViewAll, "Text"));
				verifyElementPresent(HomePage.objHome, getTextVal(HomePage.objHome, "Text"));
				verifyElementPresent(HomePage.objTransactions, getTextVal(HomePage.objTransactions, "Text"));
				verifyElementPresent(HomePage.objScanAndPay, "Scan And Pay Button");
				verifyElementPresent(HomePage.objRewards, getTextVal(HomePage.objRewards, "Text"));
				verifyElementPresent(HomePage.objMore, getTextVal(HomePage.objMore, "Text"));
				logger.info("TC_Ring_Core_202, Home Screen Validation");
				extent.extentLoggerPass("TC_Ring_Core_202", "TC_Ring_Core_202,Home Screen Validation");
				System.out.println("-----------------------------------------------------------");
			}
			Swipe("DOWN", 1);
			if (verifyElementPresent(HomePage.objPayEarly, getTextVal(HomePage.objPayEarly, "Text"))) {
				String sPayEarly = getText(HomePage.objPayEarly);
				softAssertion.assertEquals("Pay Early", sPayEarly);
				Aclick(HomePage.objPayEarly, "Pay Early");
				if (verifyElementPresent(PayEarlyPaymentPage.objPayEarlyPayment,getTextVal(PayEarlyPaymentPage.objPayEarlyPayment, "Text"))) {
					String sPayment = getText(PayEarlyPaymentPage.objPayEarlyPayment);
					softAssertion.assertEquals("Pay Early", sPayment);
					logger.info("TC_Ring_Core_224, Navigated to Payment Page to Pay Early");
					extent.extentLoggerPass("TC_Ring_Core_224", "TC_Ring_Core_224, Navigated to Payment Page to Pay Early");
					System.out.println("-----------------------------------------------------------");

					verifyElementPresent(PayEarlyPaymentPage.objAmountDue,getTextVal(PayEarlyPaymentPage.objAmountDue, "Text"));
					verifyElementPresent(PayEarlyPaymentPage.objAmountToBePaid,getTextVal(PayEarlyPaymentPage.objAmountToBePaid, "Text"));
					verifyElementPresent(PayEarlyPaymentPage.objChoosePaymentMethod,getTextVal(PayEarlyPaymentPage.objChoosePaymentMethod, "Text"));
					verifyElementPresent(PayEarlyPaymentPage.objPayViaUPIApps,getTextVal(PayEarlyPaymentPage.objPayViaUPIApps, "Text"));
					verifyElementPresent(PayEarlyPaymentPage.objUPIAppsConvenienceFee,getTextVal(PayEarlyPaymentPage.objUPIAppsConvenienceFee, ""));
					verifyElementPresent(PayEarlyPaymentPage.objAddUPIId,getTextVal(PayEarlyPaymentPage.objAddUPIId, "Text"));
					verifyElementPresent(PayEarlyPaymentPage.objAddUPIIdConvenienceFee,getTextVal(PayEarlyPaymentPage.objAddUPIIdConvenienceFee, ""));
					verifyElementPresent(PayEarlyPaymentPage.objNetBankingAndDebitCard,getTextVal(PayEarlyPaymentPage.objNetBankingAndDebitCard, "Text"));
					verifyElementPresent(PayEarlyPaymentPage.objNetBankingAndDebitCardConvenienceFee,getTextVal(PayEarlyPaymentPage.objNetBankingAndDebitCardConvenienceFee, ""));
					Swipe("UP", 1);
					verifyElementPresent(PayEarlyPaymentPage.objBankTransfer,getTextVal(PayEarlyPaymentPage.objBankTransfer, "Text"));
					verifyElementPresent(PayEarlyPaymentPage.objPaymentViaUPI,getTextVal(PayEarlyPaymentPage.objPaymentViaUPI, "Text"));
					logger.info("TC_Ring_Core_225, Validation of Pay Early Payment Screen");
					extent.extentLoggerPass("TC_Ring_Core_225", "TC_Ring_Core_225, Validation of Pay Early Payment Screen");
					System.out.println("-----------------------------------------------------------");
				}
			}
			Back(1);
			explicitWaitVisibilityNonDefault(HomePage.objCurrentSpends, 10);
			if (verifyElementPresent(HomePage.objCurrentSpends, getTextVal(HomePage.objCurrentSpends, "Text"))) {
				String sCurrentSpends = getText(HomePage.objCurrentSpends);
				softAssertion.assertEquals("Current Spends", sCurrentSpends);
				verifyElementPresent(HomePage.ObjCurrentSpendsAmount,getTextVal(HomePage.ObjCurrentSpendsAmount, "is Current Spends"));
				verifyElementPresent(HomePage.objPayEarly, getTextVal(HomePage.objPayEarly, "Text"));
				logger.info("TC_Ring_Core_223, Validation of Current Spends Banner");
				extent.extentLoggerPass("TC_Ring_Core_223", "TC_Ring_Core_223, Validation of Current Spends Banner");
				System.out.println("-----------------------------------------------------------");
			}
			if (verifyElementPresent(HomePage.objViewAll, "Scan And Pay Button")) {
				Aclick(HomePage.objViewAll, "View All Button");
				explicitWaitVisibilityNonDefault(HomePage.objTransactionHistory, 10);
				if (verifyElementPresent(HomePage.objTransactionHistory,getTextVal(HomePage.objTransactionHistory, "Text"))) {
					String sTransactionHistory = getText(HomePage.objTransactionHistory);
					softAssertion.assertEquals("Transaction History", sTransactionHistory);
					logger.info("TC_Ring_Core_222, Navigated to Transaction History List Screen");
					extent.extentLoggerPass("TC_Ring_Core_222","TC_Ring_Core_222, Navigated to Transaction History List Screen");
					System.out.println("-----------------------------------------------------------");
				}
			}
			Back(1);
		}

		public void paymentDeclinedFlow() throws Exception {
			extent.HeaderChildNode("Payment declined Flow");
			if (verifyElementPresent(HomePage.objScanAndPay, "Scan And Pay Button")) {
				Aclick(HomePage.objScanAndPay, "Scan And pay Button");
				explicitWaitVisibilityNonDefault(HomePage.objScanAnyQRToPay, 10);
				String sScanAnyQRCode = getText(HomePage.objScanAnyQRToPay);
				softAssertion.assertEquals("Scan any QR code to pay", sScanAnyQRCode);

				// Scan here
				/*------------------------------WEB----------------------------*/
				Utilities.setPlatform = "Web";
				new CommandBase("Chrome");
				String projectPath = System.getProperty("user.dir");
				waitTime(2000);
				getWebDriver().get(projectPath + "\\Mock_Files\\blockedqr.png");
				
				BrowsertearDown();
				/*------------------------------Android----------------------------*/
				setPlatform("Android");
				initDriver();

				explicitWaitVisibilityNonDefault(PaymentPage.objUseCreditLimitText, 10);
				if (verifyElementPresent(PaymentPage.objUseCreditLimitText,getTextVal(RingPayMerchantFlowPage.objUseCreditLimitText, "Text"))) {
					click(PaymentPage.objAmountTextField, "Amount Field");
					clearField(PaymentPage.objAmountTextField, "Amount Field");
					type(PaymentPage.objAmountTextField, amount[2], "Amount Field");
					click(PaymentPage.objPayNowBtn, "Pay Now Button");
					waitTime(20000);
					String sConfirmPayment = getText(PaymentPage.objConfirmPayment);
					softAssertion.assertEquals("Confirm Payment", sConfirmPayment);
					click(PaymentPage.objEditTransactionPin, "Edit Pin Field");
					waitTime(10000);
					type(PaymentPage.objEditTransactionPin, "1111", "Edit Pin Field");
					click(PaymentPage.objContinue, "Continue Button");
				}
				explicitWaitVisibilityNonDefault(PaymentPage.objPaymentFailed, 10);
				if (verifyElementPresent(PaymentPage.objPaymentFailed, getTextVal(PaymentPage.objPaymentFailed, "Text"))) {
					String sPaymentDeclined = getText(PaymentPage.objPaymentFailed);
					softAssertion.assertEquals("Payment failed", sPaymentDeclined);
					logger.info("TC_Ring_Core_220, Payment failed Screen is displayed");
					extent.extentLoggerPass("TC_Ring_Core_220", "TC_Ring_Core_220, Payment failed Screen is displayed");
					System.out.println("-----------------------------------------------------------");
					Back(1);
				}
			}
		}

		public void onBoarding() throws Exception {
			extent.HeaderChildNode("User Play store Flow Module");		
			enablePermissions();
			
			explicitWaitVisibilityNonDefault(RingLoginPage.objLoginLink, 10);
			click(RingLoginPage.objLoginLink, "Signup/Login link");
			loginMobile();
			String nMobileNumber = "9" + RandomIntegerGenerator(9);
			click(RingLoginPage.objMobTextField, "Mobile Number Field");
			type(RingLoginPage.objMobTextField, nMobileNumber, "Mobile Number Field");
//			type(RingLoginPage.objMobTextField, "9811286711", "Mobile Number Field");
			enterOtp("888888");
			readAndAccept();
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthMonth"), prop.getproperty("dateOfBirthDate"),prop.getproperty("dateOfBirthYear"));
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		
			instaNewCommunicationAddress();
		
			instaLoancongratsScreen("Congrats");
			instaLoanSetPin(prop.getproperty("ValidPin"), prop.getproperty("ValidPin"));
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");

		}

	//===============================================User Scan and Pay end===========================================================	
	
		//===========================================================BBPS flow===========================================================
		public void bbpsModule() throws Exception {
			extent.HeaderChildNode("BBPS Flow");
			getDriver().resetApp();
			enablePermissions();
			// while running separately
			explicitWaitVisibilityNonDefault(RingLoginPage.objLoginLink, 10);
			click(RingLoginPage.objLoginLink, "Signup/Login");
			loginMobile();
			mobileNoValidation1("9"+RandomIntegerGenerator(9));
			enterOtp(prop.getproperty("OTP"));
			readAndAccept();
			waitTime(40000);
			userDetails();
			dateOfBirth("Feb", "10", "1996");
			click(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(10000);
			instaNewCommunicationAddress();
			waitTime(5000);
			instaLoancongratsScreen("Congrats");
			instaLoanSetPin(prop.getproperty("ValidPin"), prop.getproperty("ValidPin"));
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objMoreBtn, 10);
			click(UtilitiesBillPage.objMoreBtn, "More Button");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objPayBillsHeader, 10);
			click(UtilitiesBillPage.objElectricityBtn, "Electricity Button Option");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objSelectBiller, 10);
			waitTime(5000);
			click(UtilitiesBillPage.objBillerSearch, "Biller search text field");
			type(UtilitiesBillPage.objBillerSearch, "Besc", "Biller search text field");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objBescomBiller, 10);
			click(UtilitiesBillPage.objBescomBiller, "Bescom Biller");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objCustId, 10);
			click(UtilitiesBillPage.objCustId, "Customer ID text field");
			type(UtilitiesBillPage.objCustId, "10001", "Customer ID text field");
			click(UtilitiesBillPage.objFetchBtn, "Fetch Bill Button");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objPayBtn, 10);
			click(UtilitiesBillPage.objPayBtn, "Pay button");
			waitTime(5000);
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objPayRingBtn, 10);
			click(UtilitiesBillPage.objPayRingBtn, "Pay with ring limit button");
			explicitWaitVisibilityNonDefault(InstaLoanOptionalJourney.objEnterSecurityPinforElectricityBill, 20);
			click(InstaLoanOptionalJourney.objPINFieldElectricityBill, "PIN Field");
			type(InstaLoanOptionalJourney.objPINFieldElectricityBill, prop.getproperty("ValidPin"), "PIN Field");
			verifyElementPresentAndClick(InstaLoanOptionalJourney.objContinueBtn, getText(InstaLoanOptionalJourney.objContinueBtn));
			waitTime(15000);
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objTransacSuccess, 30);
			Back(1);
			verifyElementPresentAndClick(UserRegistrationPage.objNoBtn, getText(UserRegistrationPage.objNoBtn));
			waitTime(5000);
			Swipe("up", 1);
			click(UtilitiesBillPage.objPaidTransac,"Recent paid transaction");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objBnplTransacNo,10);
			String transacNo = getText(UtilitiesBillPage.objBnplTransacNo);
			String PaidtranscNo = executeQuery3("SELECT * FROM db_disbursal_service.settlements where transaction_reference_number='"+transacNo+"';",39);
			if(transacNo.equals(transacNo)) {
				logger.info("TC_Ring_Core_288, To verify BBPS txn entry created in settlements table");
				extent.extentLoggerPass("TC_Ring_Core_288", "TC_Ring_Core_288, To verify BBPS txn entry created in settlements table");	
			}else {
				logger.error("TC_Ring_Core_288, To verify BBPS txn entry created in settlements table");
				extent.extentLoggerFail("TC_Ring_Core_288", "TC_Ring_Core_288, To verify BBPS txn entry created in settlements table");	
			}
			logger.info("TC_Ring_Core_281, To validate positive flow in specific biller ");
			extent.extentLoggerPass("TC_Ring_Core_281", "TC_Ring_Core_281, To validate positive flow in specific biller ");
			logger.info("TC_Ring_Core_289, To verify once global BBPS flag set to Active, only then BBPS section should be displayed under more tab of the app  [BBPS_ENABLED=1] ");
			extent.extentLoggerPass("TC_Ring_Core_289", "TC_Ring_Core_289, To verify once global BBPS flag set to Active, only then BBPS section should be displayed under more tab of the app  [BBPS_ENABLED=1] ");
			
			Back(1);
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objMoreBtn, 10);
			click(UtilitiesBillPage.objMoreBtn, "More Button");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objPayBillsHeader, 10);
			click(UtilitiesBillPage.objElectricityBtn, "Electricity Button Option");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objSelectBiller, 10);
			waitTime(5000);
			click(UtilitiesBillPage.objBillerSearch, "Biller search text field");
			type(UtilitiesBillPage.objBillerSearch, "Besc", "Biller search text field");
			hideKeyboard();
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objBescomBiller, 10);
			click(UtilitiesBillPage.objBescomBiller, "Bescom Biller");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objCustId, 10);
			click(UtilitiesBillPage.objCustId, "Customer ID text field");
			type(UtilitiesBillPage.objCustId, "100501", "Customer ID text field");
			click(UtilitiesBillPage.objFetchBtn, "Fetch Bill Button");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objPayBtn, 10);
			click(UtilitiesBillPage.objPayBtn, "Pay button");
			waitTime(5000);
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objPayRingBtn, 10);
			click(UtilitiesBillPage.objPayRingBtn, "Pay with ring limit button");
			explicitWaitVisibilityNonDefault(InstaLoanOptionalJourney.objEnterSecurityPinforElectricityBill, 20);
			click(InstaLoanOptionalJourney.objPINFieldElectricityBill, "PIN Field");
			type(InstaLoanOptionalJourney.objPINFieldElectricityBill, prop.getproperty("ValidPin"), "PIN Field");
			verifyElementPresentAndClick(InstaLoanOptionalJourney.objContinueBtn, getText(InstaLoanOptionalJourney.objContinueBtn));
			waitTime(20000);
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objPayFailed, 30);
			verifyElementPresent(UtilitiesBillPage.objPayFailed, getText(UtilitiesBillPage.objPayFailed));
			waitTime(10000);
			Back(1);
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objRefundTransac, 20);
			verifyElementPresent(UtilitiesBillPage.objRefundTransac, getText(UtilitiesBillPage.objRefundTransac));
			logger.info("TC_Ring_Core_282, To validate failed flow in the specific biller  ");
			extent.extentLoggerPass("TC_Ring_Core_282","TC_Ring_Core_282, To validate failed flow in the specific biller  ");
			logger.info("TC_Ring_Core_283, To validate auto refund is applied during failure  ");
			extent.extentLoggerPass("TC_Ring_Core_283","TC_Ring_Core_283, To validate auto refund is applied during failure  ");
			ringPayLogout();
			
			logger.info("Logging in with Suspended user type");
			explicitWaitVisibilityNonDefault(RingLoginPage.objLoginLink, 10);
			click(RingLoginPage.objLoginLink, "Signup/Login");
			loginMobile();
			mobileNoValidation1("9930244553");
			enterOtp("888888");
			waitTime(20000);
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objMoreBtn, 10);
			click(UtilitiesBillPage.objMoreBtn, "More Button");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objPayBillsHeader, 10);
			click(UtilitiesBillPage.objElectricityBtn, "Electricity Button Option");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objOverdueMsg,10);
			verifyElementPresent(UtilitiesBillPage.objOverdueMsg, getText(UtilitiesBillPage.objOverdueMsg));
			click(UtilitiesBillPage.objOkBtn,"Okay Got it Button!");
			logger.info("TC_Ring_Core_284, To verify on click of any Biller during suspended state, bottom sheet saying User is having overdue bills should be displayed");
			extent.extentLoggerPass("TC_Ring_Core_284","TC_Ring_Core_284, To verify on click of any Biller during suspended state, bottom sheet saying User is having overdue bills should be displayed");
			
			getDriver().resetApp();
			logger.info("Logging in with terminated user who has dues/emi");
			enablePermissions();
			explicitWaitVisibilityNonDefault(RingLoginPage.objLoginLink, 10);
			click(RingLoginPage.objLoginLink, "Signup/Login");
			loginMobile();
			mobileNoValidation1("8994512988");
			enterOtp("888888");
			readAndAccept();
			waitTime(20000);
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objMoreBtn, 10);
			click(UtilitiesBillPage.objMoreBtn, "More Button");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objPayBillsHeader, 10);
			click(UtilitiesBillPage.objElectricityBtn, "Electricity Button Option");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objOopsMsg,10);
			verifyElementPresent(UtilitiesBillPage.objOopsMsg, getText(UtilitiesBillPage.objOopsMsg));
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objPayNowBtn,10);
			verifyElementPresent(UtilitiesBillPage.objPayNowBtn, getText(UtilitiesBillPage.objPayNowBtn));
			logger.info("TC_Ring_Core_285, To verify on click of any Biller during Terminated state with emi/ dues pending , Must redirect to a page having Pay Now link");
			extent.extentLoggerPass("TC_Ring_Core_285","TC_Ring_Core_285, To verify on click of any Biller during Terminated state with emi/ dues pending , Must redirect to a page having Pay Now link");
			click(UtilitiesBillPage.objPayNowBtn,"Pay Now Button");
			explicitWaitVisibilityNonDefault(HomePage.objRepaymentHeader,10);
			Back(1);
			ringPayLogout();
			
			logger.info("Logging in with terminated user without dues/emi");
			explicitWaitVisibilityNonDefault(RingLoginPage.objLoginLink, 10);
			click(RingLoginPage.objLoginLink, "Signup/Login");
			loginMobile();
			mobileNoValidation1("9464345848");
			enterOtp("888888");
			waitTime(20000);
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objMoreBtn, 10);
			click(UtilitiesBillPage.objMoreBtn, "More Button");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objPayBillsHeader, 10);
			click(UtilitiesBillPage.objElectricityBtn, "Electricity Button Option");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objPayNotAvail,10);
			verifyElementPresent(UtilitiesBillPage.objPayNotAvail, getText(UtilitiesBillPage.objPayNotAvail));
			logger.info("TC_Ring_Core_286, To verify on click of any Biller during Terminated state with out emi/ dues pending , Must redirect to a page having text \"This mode of payment is not available\"");
			extent.extentLoggerPass("TC_Ring_Core_286","TC_Ring_Core_286, To verify on click of any Biller during Terminated state with out emi/ dues pending , Must redirect to a page having text \"This mode of payment is not available\"");
			click(UtilitiesBillPage.objOkBtn,"Okay got it button");
			ringPayLogout();
			
			logger.info("Logging in with user who has exhausted their ring limit");
			explicitWaitVisibilityNonDefault(RingLoginPage.objLoginLink, 10);
			click(RingLoginPage.objLoginLink, "Signup/Login");
			loginMobile();
			mobileNoValidation1("9"+RandomIntegerGenerator(9));
			enterOtp(prop.getproperty("OTP"));
			readAndAccept();
			waitTime(40000);
			userDetails();
			dateOfBirth("Feb", "10", "1996");
			click(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(10000);
			instaNewCommunicationAddress();
			waitTime(5000);
			instaLoancongratsScreen("Congrats");
			instaLoanSetPin(prop.getproperty("ValidPin"), prop.getproperty("ValidPin"));
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objMoreBtn, 10);
			click(UtilitiesBillPage.objMoreBtn, "More Button");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objPayBillsHeader, 10);
			click(UtilitiesBillPage.objElectricityBtn, "Electricity Button Option");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objSelectBiller, 10);
			waitTime(5000);
			click(UtilitiesBillPage.objBillerSearch, "Biller search text field");
			type(UtilitiesBillPage.objBillerSearch, "Besc", "Biller search text field");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objBescomBiller, 10);
			click(UtilitiesBillPage.objBescomBiller, "Bescom Biller");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objCustId, 10);
			click(UtilitiesBillPage.objCustId, "Customer ID text field");
			type(UtilitiesBillPage.objCustId, "10001", "Customer ID text field");
			click(UtilitiesBillPage.objFetchBtn, "Fetch Bill Button");
			clearField(UtilitiesBillPage.objamountField, "Amount Field");
			click(UtilitiesBillPage.objamountField, "Amount Field");
			type(UtilitiesBillPage.objamountField, "35789", "Amount Entered");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objPayBtn, 10);
			click(UtilitiesBillPage.objPayBtn, "Pay button");	
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objRingInsuffMsg,10);
			verifyElementPresent(UtilitiesBillPage.objRingInsuffMsg, getText(UtilitiesBillPage.objRingInsuffMsg));
			logger.info("TC_Ring_Core_287, To verify on click of any biller user will be redirected to Page having \"Pay with ring limit\", then throwing bottom sheet having \"Insufficient Rings\"");
			extent.extentLoggerPass("TC_Ring_Core_287","TC_Ring_Core_287, To verify on click of any biller user will be redirected to Page having \"Pay with ring limit\", then throwing bottom sheet having \"Insufficient Rings\"");
			click(UtilitiesBillPage.objOkBtn,"Okay got it button");
			Back(2);
			
			/*------Platform switch to web to handle configuration flags------*/
			
			Utilities.setPlatform = "Web";
	        new CommandBase("Chrome");
	        waitTime(4000);
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/configuration?search=bb&");
	        logger.info("Switched to Web platform");
	        click(UtilitiesBillPage.objEmail,"Admin panel email field");
	        type(UtilitiesBillPage.objEmail,"shakir.muchumarri@collabera.com","Email text field");
	        click(UtilitiesBillPage.objContinue,"Continue Button");
	        waitTime(5000);
	        click(UtilitiesBillPage.objEditConfBtn,"Edit Config button");
	        waitTime(4000);
	        click(UtilitiesBillPage.objInactiveRadioBtn,"Inactive radio button");
	        waitTime(2000);
	        click(UtilitiesBillPage.objSubmitBtn,"Submit Button");
	        waitTime(15000);
	        BrowsertearDown();
	        /*------------------------------Android----------------------------*/
	        setPlatform("Android");
	        initDriver();
	        
	        verifyElementPresentAndClick(InstaLoanOptionalJourney.objUPITab, "UPI Tab");
			Back(1);
	        explicitWaitVisibilityNonDefault(UtilitiesBillPage.objMoreBtn, 10);
			click(UtilitiesBillPage.objMoreBtn, "More Button");
			if(verifyElementPresent2(UtilitiesBillPage.objPayBillsHeader,"Pay Biller Header")) {
				logger.error("TC_Ring_Core_290, To verify once global BBPS flag set to InActive, only then BBPS section should disappear under more tab of the app  [BBPS_ENABLED=0 ]");
				extent.extentLoggerFail("TC_Ring_Core_290","TC_Ring_Core_290, To verify once global BBPS flag set to InActive, only then BBPS section should disappear under more tab of the app  [BBPS_ENABLED=0 ]");
			}
			else {
				logger.info("TC_Ring_Core_290, To verify once global BBPS flag set to InActive, only then BBPS section should disappear under more tab of the app  [BBPS_ENABLED=0 ]");
				extent.extentLoggerPass("TC_Ring_Core_290","TC_Ring_Core_290, To verify once global BBPS flag set to InActive, only then BBPS section should disappear under more tab of the app  [BBPS_ENABLED=0 ]");
			}
	        ringPayLogout();
		}
		
		
	//=========================================================BBPS Flow Ends======================================================
//=================================================Merchant Flow========================================//
	public void merchantFlow() throws Exception {
		extent.HeaderChildNode("RingPay App Merchant Flow");
		getDriver().resetApp();
		if (verifyElementPresent(RingLoginPage.objCamPermPopUp, "Enable permissions button")) {
			enablePermissions();
		}

		explicitWaitVisibilityNonDefault(RingPayMerchantFlowPage_New.objScanQRCodeText, 10);
		verifyIsElementDisplayed(RingPayMerchantFlowPage_New.objScanQRCodeText,getTextVal(RingPayMerchantFlowPage.objScanQRCodeText, "Text"));
		softAssertion.assertEquals(getText(RingPayMerchantFlowPage_New.objScanQRCodeText),"Scan any QR to get started");
		verifyElementPresent(RingPayMerchantFlowPage_New.obDontHaveQRCodeText,getTextVal(RingPayMerchantFlowPage_New.obDontHaveQRCodeText, "Text"));
		verifyElementPresent(RingPayMerchantFlowPage_New.objSignUpORLoginLink,getTextVal(RingPayMerchantFlowPage_New.objSignUpORLoginLink, "Text"));
		logger.info("Scanning the QR Code");
		extent.extentLogger("QR Code", "Scanning the QR Code");
		
		scannQRSwitch();
		
		if (checkElementExist(RingPayMerchantFlowPage_New.objFreedomPopup, "Freedom Pop Up")) {
			click(RingPayMerchantFlowPage_New.objLetsRingItBtn, getText(RingPayMerchantFlowPage_New.objLetsRingItBtn));
			//click(RingPayMerchantFlowPage_New.objLetsRingItBtn, getText(RingPayMerchantFlowPage_New.objLetsRingItBtn));
		}
		verifyElementPresent(RingPayMerchantFlowPage_New.objMakePaymentPage, "Navigated to Make Payment Page");
		verifyElementPresent(RingPayMerchantFlowPage_New.objPayTypeMethod,getTextVal(RingPayMerchantFlowPage_New.objPayTypeMethod, "Payment Method"));
		verifyElementPresent(RingPayMerchantFlowPage_New.objUpiId,getTextVal(RingPayMerchantFlowPage_New.objUpiId, "UPI Id"));
		click(RingPayMerchantFlowPage_New.objAmountTextField, "Amount Text Field");
		waitTime(2000);
		iskeyboardShown("Numeric");
		for (int i = 0; i <= amount.length; i++) {
			type1(RingPayMerchantFlowPage_New.objAmountTextField, amount[i], "Amount Field");
			String validationText = getTextVal(RingPayMerchantFlowPage_New.objTransactionMsg, "Text is Displayed");
			if (validationText.contains("You can pay up to")) {
				break;
			}
			logger.warn(validationText);
			extent.extentLoggerWarning("validation", validationText);
			clearField(RingPayMerchantFlowPage_New.objAmountTextField, "Amount text field");
		}
		click(RingPayMerchantFlowPage_New.objProceedBtn, "Proceed Button");

		verifyIsElementDisplayed(RingPayMerchantFlowPage_New.objLoginPageHeader,getTextVal(RingPayMerchantFlowPage.objLoginPageHeader, "Page"));
		softAssertion.assertEquals(getText(RingPayMerchantFlowPage_New.objLoginPageHeader), "Sign up/Login");
		logger.info("User redirected to Signup/Login Screen");
		extent.extentLogger("login", "User redirected to Signup/Login Screen");
		verifyElementPresent(RingPayMerchantFlowPage_New.objContinueWithMobileCTA,getTextVal(RingPayMerchantFlowPage_New.objContinueWithMobileCTA, "CTA"));
		verifyElementPresent(RingPayMerchantFlowPage_New.objContinueWithGoogleCTA,getTextVal(RingPayMerchantFlowPage_New.objContinueWithGoogleCTA, "CTA"));
		verifyElementPresent(RingPayMerchantFlowPage_New.objContinueWithFacebookCTA,getTextVal(RingPayMerchantFlowPage_New.objContinueWithFacebookCTA, "CTA"));

		extent.extentLoggerPass("TC_Ring_Core_60", "TC_Ring_Core_60-To Verify the Login screen when user open the app");
		extent.extentLoggerPass("TC_Ring_Core_61", "TC_Ring_Core_61-To verify When User selects Enable Permission option");
		extent.extentLoggerPass("TC_Ring_Core_62", "TC_Ring_Core_62-To verify when user Scans the QR  code");
		extent.extentLoggerPass("TC_Ring_Core_63", "TC_Ring_Core_63-To verify the Screen when the Pop up disappers");
		extent.extentLoggerPass("TC_Ring_Core_64", "TC_Ring_Core_64-To verify the merchant details ");
		extent.extentLoggerPass("TC_Ring_Core_65", "TC_Ring_Core_65-To verify the First Transcation  fee message ");
		extent.extentLoggerPass("TC_Ring_Core_66", "TC_Ring_Core_66-To Verify  when User cliks on  enter transaction amount on screen ");
		extent.extentLoggerPass("TC_Ring_Core_67", "TC_Ring_Core_67-To verify when user  tries to enter amount more than first transaction limit");
		extent.extentLoggerPass("TC_Ring_Core_68", "TC_Ring_Core_68-To Verify when user tries to enter 0 amount");
		extent.extentLoggerPass("TC_Ring_Core_69", "TC_Ring_Core_69-To Verify user enters valid amt. and clicks on the Pay now button on merchant detail page");

		userPlayStoreLoginFlow(prop.getproperty("lessThanTenMob"), prop.getproperty("moreThanTenMob"),
			prop.getproperty("specialCharMob"), prop.getproperty("spaceMob"), prop.getproperty("validMob"),
			prop.getproperty("invalidOtp"), prop.getproperty("lessOtp"));
		waitTime(40000);
		userDetails();
		dateOfBirth("Feb", "10", "1996");
		click(UserRegistrationNew.objProceed, "Proceed Button");
		waitTime(10000);
		instaNewCommunicationAddress();

		verifyElementPresent(PromoCodeOfferPage.objMerchantOfferHeader,getText(PromoCodeOfferPage.objMerchantOfferHeader));
		softAssertion.assertEquals(getText(PromoCodeOfferPage.objMerchantOfferHeader), "Woohoo, Sunil!");
		verifyElementPresent(PromoCodeOfferPage.objUnlockRingLimit, getText(PromoCodeOfferPage.objUnlockRingLimit)+ " of " + getText(PromoCodeOfferPage.objApprovedRinglimitAmt));
		verifyElementPresent(PromoCodeOfferPage.objPayingType, getText(PromoCodeOfferPage.objPayingType));
		verifyElementPresent(PromoCodeOfferPage.objUPIId, getText(PromoCodeOfferPage.objUPIId));
		verifyElementPresent(PromoCodeOfferPage.objMerchantPayAmt, getText(PromoCodeOfferPage.objMerchantPayAmt));
		verifyElementPresent(PromoCodeOfferPage.objRBIMsg,getText(PromoCodeOfferPage.objRBIMsg) + getText(PromoCodeOfferPage.objRBIMsg));
		verifyElementPresentAndClick(RingLoginPage.objIAcceptCheckBox,"I accept the Ring’s Terms & Conditions & IT Act 2000 checkbox");
		verifyElementPresentAndClick(TermsAndConditionPage.objAcceptAndPayBtn,getText(TermsAndConditionPage.objAcceptText));

		instaLoanSetPin(prop.getproperty("InstaLoanMPIN"), prop.getproperty("InstaLoanMPIN"));
		waitTime(25000);
		verifyElementPresentAndClick(PromoCodeOfferPage.objGoToHomePage, getText(PromoCodeOfferPage.objGoToHomePage));
		waitTime(15000);
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		//explicitWaitVisibilityNonDefault(UserRegistrationPage.objNoBtn, 20);
		verifyElementPresentAndClick(UserRegistrationPage.objNoBtn, getText(UserRegistrationPage.objNoBtn));
		String sHome = getText(HomePage.objHome);
		softAssertion.assertEquals(sHome, "Home");
		ringPayLogout();
	}
//================================================Merchant Flow End=================================================================

	public void cameraPermission() throws Exception {
		explicitWaitVisibilityNonDefault(RingLoginPage.objCamPermHeader, 30);
		verifyElementPresent(RingLoginPage.objCamPermHeader, "Camera Permission required");
		String camPermHeaderTxt = getText(RingLoginPage.objCamPermHeader);
		softAssertion.assertEquals(camPermHeaderTxt, "Camera Permission required");
		logger.info("Camera Permission required popup");
	}

	public void enablePermissions() throws Exception {
		explicitWaitVisibilityNonDefault(RingLoginPage.objCamPermPopUp, 10);
		Aclick(RingLoginPage.objCamPermPopUp, "Enable permissions button");
		logger.info("Foreground allow camera permissions");
		extent.extentLoggerPass("Foreground allow camera permissions", "Foreground allow camera permissions options");
		explicitWaitVisibilityNonDefault(RingLoginPage.objAllowCamera, 10);
		Aclick(RingLoginPage.objAllowCamera, "While using the app foreground camera permission option");

		explicitWaitVisibilityNonDefault(RingLoginPage.objScanQRPage, 10);
		logger.info("Scan any QR to get started");
		String qrCodeHeader = getText(RingLoginPage.objScanQRPage);
		softAssertion.assertEquals(qrCodeHeader, "Scan any QR to get started");
	}

	public void loginMobile() throws Exception {
		explicitWaitVisibilityNonDefault(RingLoginPage.objLoginMobile, 10);
		Aclick(RingLoginPage.objLoginMobile, "Continue with Mobile option");
		waitTime(3000);
//		trueCallerPopup();
//		if (verifyElementPresent(RingLoginPage.objNoneBtn, "None of the above button")) {
//			String noneOfAboveTxt = getText(RingLoginPage.objNoneBtn);
//			softAssertion.assertEquals(noneOfAboveTxt, "NONE OF THE ABOVE");
//			Aclick(RingLoginPage.objNoneBtn, "None of the above");
//
//			explicitWaitVisibilityNonDefault(RingLoginPage.objVerifyMobHeader, 10);
//			logger.info("Verify Mobile Header");
//			String verifyMobHeaderTxt = getText(RingLoginPage.objVerifyMobHeader);
//			softAssertion.assertEquals(verifyMobHeaderTxt, "Verify Mobile");
//			explicitWaitVisibilityNonDefault(RingLoginPage.objMobTextField, 10);
//			explicitWaitVisibilityNonDefault(RingLoginPage.objVerifyMobHeader, 10);
//			String verifyMobHeaderTxt1 = getText(RingLoginPage.objVerifyMobHeader);
//			softAssertion.assertEquals(verifyMobHeaderTxt1, "Verify Mobile");
//			explicitWaitVisibilityNonDefault(RingLoginPage.objMobTextField, 10);
//		}else {
//		if(verifyElementPresent(RingLoginPage.objTruSkipBtn,"TruCaller skip button")) {
//			click(RingLoginPage.objTruSkipBtn,"TruCaller skip button");
//			if(verifyElementPresent(RingLoginPage.objNoneBtn,"None of the above")) {
//				click(RingLoginPage.objNoneBtn,"None of the above");
//				explicitWaitVisibilityNonDefault(RingLoginPage.objVerifyMobHeader, 10);
//				String verifyMobHeaderTxt = getText(RingLoginPage.objVerifyMobHeader);
//				softAssertion.assertEquals(verifyMobHeaderTxt, "Verify Mobile");
//				explicitWaitVisibilityNonDefault(RingLoginPage.objMobTextField, 10);
//				explicitWaitVisibilityNonDefault(RingLoginPage.objVerifyMobHeader, 10);
//				logger.info("Verify Mobile Header");
//				String verifyMobHeaderTxt1 = getText(RingLoginPage.objVerifyMobHeader);
//				softAssertion.assertEquals(verifyMobHeaderTxt1, "Verify Mobile");
//				explicitWaitVisibilityNonDefault(RingLoginPage.objMobTextField, 10);
//			}
//		}
		if(verifyElementPresent(RingLoginPage.objNoneBtn,"None of the above")) {
			click(RingLoginPage.objNoneBtn,"None of the above");
			explicitWaitVisibilityNonDefault(RingLoginPage.objVerifyMobHeader, 10);
			String verifyMobHeaderTxt = getText(RingLoginPage.objVerifyMobHeader);
			softAssertion.assertEquals(verifyMobHeaderTxt, "Verify Mobile");
			explicitWaitVisibilityNonDefault(RingLoginPage.objMobTextField, 10);
			explicitWaitVisibilityNonDefault(RingLoginPage.objVerifyMobHeader, 10);
			logger.info("Verify Mobile Header");
			String verifyMobHeaderTxt1 = getText(RingLoginPage.objVerifyMobHeader);
			softAssertion.assertEquals(verifyMobHeaderTxt1, "Verify Mobile");
			explicitWaitVisibilityNonDefault(RingLoginPage.objMobTextField, 10);
			
		}
		else {
			explicitWaitVisibilityNonDefault(RingLoginPage.objVerifyMobHeader, 10);
			String verifyMobHeaderTxt = getText(RingLoginPage.objVerifyMobHeader);
			softAssertion.assertEquals(verifyMobHeaderTxt, "Verify Mobile");
			explicitWaitVisibilityNonDefault(RingLoginPage.objMobTextField, 10);
			explicitWaitVisibilityNonDefault(RingLoginPage.objVerifyMobHeader, 10);
			logger.info("Verify Mobile Header");
			String verifyMobHeaderTxt1 = getText(RingLoginPage.objVerifyMobHeader);
			softAssertion.assertEquals(verifyMobHeaderTxt1, "Verify Mobile");
			explicitWaitVisibilityNonDefault(RingLoginPage.objMobTextField, 10);
		}
//		}

	}

	public void trueCallerPopup() throws Exception {
		if (verifyElementPresent(RingLoginPage.objTruSkipBtn, "True caller popup")) {
			Aclick(RingLoginPage.objTruSkipBtn, "True caller skip button");
		}
		if (verifyElementPresent(RingLoginPage.objNoneBtn, "None of the above")) {
			Aclick(RingLoginPage.objNoneBtn, "None of the above");
		}
	}

	public String mobileNoValidation(String mobNo) throws Exception {
		// public String noTxt;
		waitTime(4000);
		if (verifyElementPresent(RingLoginPage.objTruSkipBtn, "True caller popup")) {
			Aclick(RingLoginPage.objTruSkipBtn, "True caller skip button");
		}
		if (verifyElementPresent(RingLoginPage.objNoneBtn, "None of the above button")) {
			String noneOfAboveTxt = getText(RingLoginPage.objNoneBtn);
			softAssertion.assertEquals(noneOfAboveTxt, "NONE OF THE ABOVE");
			Aclick(RingLoginPage.objNoneBtn, "None of the above");
		}
		explicitWaitVisibilityNonDefault(RingLoginPage.objVerifyMobHeader, 10);
		logger.info("Verify Mobile Header");
		String verifyMobHeaderTxt = getText(RingLoginPage.objVerifyMobHeader);
		softAssertion.assertEquals(verifyMobHeaderTxt, "Verify Mobile");
		waitTime(4000);
		Aclick(RingLoginPage.objMobTextField, "Mobile text field");
		type(RingLoginPage.objMobTextField, mobNo, "Mobile text field");
		System.out.println(mobNo);
		waitTime(5000);
		String noTxt = getText(RingLoginPage.objMobTextField);
		if (!verifyIsElementDisplayed(RingUserDetailPage.objRegisterBtn)) {
			logger.info("Navigated to OTP Page");
		} else {
			Aclick(RingUserDetailPage.objRegisterBtn, "Proceed Button");
		}
		return noTxt;
	}

	public void mobileNoValidation1(String mobNo) throws Exception {
		click(RingLoginPage.objMobTextField, "Mobile text field");
		type(RingLoginPage.objMobTextField, mobNo, "Mobile text field");
//		String noTxt = getText(RingLoginPage.objMobTextField);
//		waitTime(5000);
//		if (!verifyElementExist(RingUserDetailPage.objRegisterBtn, "Proceed Button")) {
//			logger.info("Navigated to OTP Page");
//		} else {
//			click(RingUserDetailPage.objRegisterBtn, "Proceed Button");
//		}
//		return noTxt;
	}

	public String mobileNoValidation2(String mobNo) throws Exception {
		Aclick(RingLoginPage.objMobTextField, "Mobile text field");
		type(RingLoginPage.objMobTextField, mobNo, "Mobile text field");
		System.out.println(mobNo);
		String noTxt = getText(RingLoginPage.objMobTextField);
		if (mobNo.length() >= 10) {
			logger.info("Navigated to OTP Page");
		} else {
			click(RingLoginPage.objProceedBtn, "Proceed Button");
		}
		return noTxt;
	}

	public void enterOtp(String otp) throws Exception {
		explicitWaitClickable(RingLoginPage.resendOtpTxt, 20);
		Aclick(RingLoginPage.objOtpTxtField1, "Otp text field");
		type(RingLoginPage.objOtpTxtField1, otp, "Enter OTP");
	}

	/**
	 * @throws Exception
	 * 
	 */
	public void gmailLogin(String userId, String password) throws Exception {
		extent.HeaderChildNode("User Play store Flow Module");
		cameraPermission();
		enablePermissions();
		click(RingLoginPage.objLoginLink, "Signup/Login link");
		explicitWaitVisibilityNonDefault(SignUP_LoginPage.continueWithGmail, 20);
		Aclick(SignUP_LoginPage.continueWithGmail, "Continue With Gmail Account");
		if (verifyElementPresent(GmailLoginPage.addAnotherAccount, "Add Another Account")) {
			Aclick(GmailLoginPage.addAnotherAccount, "Add Another Account");
			
		}

		explicitWaitVisibilityNonDefault(GmailLoginPage.enterEmailID, 20);
		Aclick(GmailLoginPage.enterEmailID, "Enter Email Field");
		type(GmailLoginPage.enterEmailID, userId, "Entered Email ID");
		Aclick(GmailLoginPage.nextButton, "Next Button");
		explicitWaitVisibilityNonDefault(GmailLoginPage.enterPassword, 20);
		// Aclick(GmailLoginPage.enterPassword, "Enter Password Field");
		type(GmailLoginPage.enterPassword, password, "Password");
		Aclick(GmailLoginPage.nextButton, "Next Button");

		if (verifyElementPresent(GmailLoginPage.txtKeepYourAccountUpdate,"Keep Your account updated with phone number")) {
			swipeUp();
			click(GmailLoginPage.btnYesImIn, "Yes I'm in");
		}
		
		if (verifyElementPresent(GmailLoginPage.btnDontTurnOn, "Never Lose Your Contacts")) {
			click(GmailLoginPage.btnDontTurnOn, "Don't Turn On Button");
		}

		explicitWaitVisibilityNonDefault(GmailLoginPage.btnIAgree, 20);
		Aclick(GmailLoginPage.btnIAgree, "I Agree Button");
		explicitWaitVisibilityNonDefault(GmailLoginPage.txtGoogleServices, 20);

		Aclick(GmailLoginPage.btnMore, "More Button");
		explicitWaitVisibilityNonDefault(GmailLoginPage.btnAccept, 20);
		Aclick(GmailLoginPage.btnAccept, "Accept Button");
		waitTime(10000);
	}

	/**
	 * Business method for RingPay Application Logout
	 * 
	 */

	public void ringPayLogout() throws Exception {
		//extent.HeaderChildNode("RingPay Logout");

		explicitWaitVisibilityNonDefault(RingLoginPage.objTopMenu, 15);
		click(RingLoginPage.objTopMenu, "Top left menu button");

		explicitWaitVisibilityNonDefault(RingLoginPage.objProfileSelect, 10);
		click(RingLoginPage.objProfileSelect, "Profile Select Button");

		explicitWaitVisibilityNonDefault(RingLoginPage.objLogoutBtn, 20);
		click(RingLoginPage.objLogoutBtn, "Logout Button");

		logger.info("Logout popup comes up");
		extent.extentLoggerPass("Logout popup", "Logout popup comes up");

		explicitWaitVisibilityNonDefault(RingLoginPage.objYesBtn, 10);
		click(RingLoginPage.objYesBtn, "Yes confirmation button");

		logger.info("User is successfully logged out");
		extent.extentLoggerPass("Logout confirmation", "User is successfully logged out");

	}

	public void hamburgerTab() throws Exception {
		verifyElementPresentAndClick(RingUserDetailPage.objHamburgerTab, "Hamburger Tab");
		verifyElementPresentAndClick(RingUserDetailPage.objProfileTabCompletedPercentage,
				"Profile Completed Percentage tab");
		verifyElementPresentAndClick(RingUserDetailPage.objLogoutBtn, "Logout Button");
		logger.info("Are you sure you want to Logout?");
		explicitWaitVisibilityNonDefault(RingUserDetailPage.objLogOutYesBtn, 10);
		click(RingUserDetailPage.objLogOutYesBtn, "Yes Button");
	}

//==============================================InstaLoan Onboarding==========================================================================
	public void mockUserAPI() throws ClassNotFoundException, SQLException {
		extent.HeaderChildNode("DB");
		ValidatableResponse response = Utilities.MockuserAPI(
				"https://testing-gateway.test.paywithring.com/api/v1/mock-data/user", "male",
				prop.getproperty("encryptedName"));
		firstName = response.extract().jsonPath().getString("data.data.first_name");
		System.out.println("First Name= " + firstName);
		middleName = response.extract().jsonPath().getString("data.data.middle_name");
		System.out.println("Middle Name= " + middleName);
		lastName = response.extract().jsonPath().getString("data.data.last_name");
		System.out.println("Last Name =" + lastName);
		panCard = response.extract().jsonPath().getString("data.data.pan");
		System.out.println("Pan Card= " + panCard);
		mobileNumber = response.extract().jsonPath().getString("data.data.mobile_number");
		System.out.println("Mobile Number= " + mobileNumber);
		mothersName = response.extract().jsonPath().getString("data.data.mother_name");
		System.out.println("Mother Name= " + mothersName);
		email = response.extract().jsonPath().getString("data.data.email");
		System.out.println("Email= " + email);
		gender = response.extract().jsonPath().getString("data.data.gender");
		System.out.println("Gender= " + gender);
		String otp = response.extract().jsonPath().getString("data.data.otp");
		System.out.println(otp);
		dob = response.extract().jsonPath().getString("data.data.dob");
		System.out.println(dob);
		String[] dateSplit = dob.split("-");
		year = dateSplit[0];
		System.out.println("years---" + year);
		String month = dateSplit[1];
		System.out.println("month---" + month);
		date = dateSplit[2];
		System.out.println("date---" + date);
		int monthNumber = Integer.parseInt(month);
		monthByName = Month.of(monthNumber).getDisplayName(TextStyle.SHORT, Locale.US);
		System.out.println(monthByName);
		aadharFrontImage = response.extract().jsonPath().getString("data.data.aadhaar_front_image.asset_path");
		System.out.println("Front Aadhar Image---->" + aadharFrontImage);
		aadharBackImage = response.extract().jsonPath().getString("data.data.aadhaar_back_image.asset_path");
		System.out.println("Back Aadhar Image---->" + aadharBackImage);
		panImage = response.extract().jsonPath().getString("data.data.pan_image.asset_path");
		System.out.println("Pan Card Image---->" + panImage);
		System.out.println("--------------------------------------------------------------------------------------");
		System.out.println("--------------------------Bank Account Details----------------------");

		bankAccountNumber = response.extract().jsonPath().getString("data.data.bank_account.bank_account_no");
		System.out.println("Bank Account Number----" + bankAccountNumber);
		bankifsc = response.extract().jsonPath().getString("data.data.bank_account.bank_branch_ifsc");
		System.out.println("Bank IFSC-----" + bankifsc);
		bankAccountType = response.extract().jsonPath().getString("data.data.bank_account.bank_account_type");
		System.out.println("Bank Account Type-----" + bankAccountType);
		bankAccountHolder = response.extract().jsonPath().getString("data.data.bank_account.bank_account_holder_name");
		System.out.println("Account Holder----" + bankAccountHolder);
//		executeQuery("SELECT * FROM db_txn_service.instaloan_whitelisted_users;");
//		executeUpdate("Update db_txn_service.instaloan_whitelisted_users set approved_amount='000' where id=48","SELECT * FROM db_txn_service.instaloan_whitelisted_users;");
//		executeInsert();
	}

	public void instaLoanWhitelistLogic() throws Exception {
		extent.HeaderChildNode("WhiteList Logic");
		mockUserAPI();
		loginOnboarding("1");
		mobileNoValidation1(mobileNumber);
		enterOtp(prop.getproperty("OTP"));
		readAndAccept();
		waitTime(60000);
		instaUserDetails(firstName, lastName, mothersName, email, gender);
//		instaNewCommunicationAddress();
		if (verifyElementPresent(AddAddressPage.objAddCurrentAddressHeader, "address Page")) {
			addAddress("86", "44, Borivali", "Vasa Street", "Das Gupta Street", "400080");
			
			click(AddAddressPage.objSubmitBtn, "Submit");
		}
		waitTime(10000);
		//instaKycDocument();
		
		instaPanCardDetails(prop.getproperty("RingAdminEmail"), prop.getproperty("RingAdminPassword"),
				prop.getproperty("RingAdminOTP"), "OPTIONAL", "13000", "2311", "2319");
		panCardDetails();
		instaLoancongratsScreen();
		instaLoanSetPin(prop.getproperty("InstaLoanMPIN"), prop.getproperty("InstaLoanMPIN"));
	}

	public void loginOnboarding(String amount) throws Exception {
		if (verifyElementPresent2(RingLoginPage.objCamPermPopUp, "Enable permissions button")) {
			enablePermissions();
		}
		verifyElementPresent(RingLoginPage.objScanQRPage, getText(RingLoginPage.objScanQRPage));
		if (verifyElementPresent2(RingLoginPage.objMakePayment, "Make Payment Page")) {
			keyLogin = getText(RingLoginPage.objMakePayment);
			System.out.println(keyLogin);
		} else {
			keyLogin = getText(RingLoginPage.objScanQRPage);
			System.out.println(keyLogin);
		}

		switch (keyLogin) {
		case "Say hello to freedom!!":
			click(RingLoginPage.objMakePaymentLetsRingItBtn, getText(RingLoginPage.objMakePaymentLetsRingItBtn));
			click(RingLoginPage.objMakePaymentLetsRingItBtn, getText(RingLoginPage.objMakePaymentLetsRingItBtn));
			click(RingPayMerchantFlowPage.objAmountTextField, "Enter Amount Field");
			type1(RingPayMerchantFlowPage.objAmountTextField, amount, "Amount Field");
			verifyElementPresentAndClick(RingLoginPage.objMakePaymentPageProceedBtn,
					getText(RingLoginPage.objMakePaymentPageProceedBtn));
			verifyIsElementDisplayed(RingPayMerchantFlowPage.objLoginPageHeader,
					getTextVal(RingPayMerchantFlowPage.objLoginPageHeader, "Page"));
			softAssertion.assertEquals(getText(RingPayMerchantFlowPage.objLoginPageHeader), "Sign up/Login");
			logger.info("User redirected to Signup/Login Screen");
			extent.extentLogger("login", "User redirected to Signup/Login Screen");
			verifyElementPresent(RingPayMerchantFlowPage.objContinueWithMobileCTA,
					getTextVal(RingPayMerchantFlowPage.objContinueWithMobileCTA, "CTA"));
			verifyElementPresent(RingPayMerchantFlowPage.objContinueWithGoogleCTA,
					getTextVal(RingPayMerchantFlowPage.objContinueWithGoogleCTA, "CTA"));
			verifyElementPresent(RingPayMerchantFlowPage.objContinueWithFacebookCTA,
					getTextVal(RingPayMerchantFlowPage.objContinueWithFacebookCTA, "CTA"));
			loginMobile();
			break;

		case "Scan any QR to get started":
			verifyElementPresentAndClick(RingLoginPage.objSignUpLoginBt, getText(RingLoginPage.objSignUpLoginBt));
			softAssertion.assertEquals(getText(RingPayMerchantFlowPage.objLoginPageHeader), "Sign up/Login");
			logger.info("User redirected to Signup/Login Screen");
			extent.extentLogger("login", "User redirected to Signup/Login Screen");
			verifyElementPresent(RingPayMerchantFlowPage.objContinueWithMobileCTA,
					getTextVal(RingPayMerchantFlowPage.objContinueWithMobileCTA, "CTA"));
			verifyElementPresent(RingPayMerchantFlowPage.objContinueWithGoogleCTA,
					getTextVal(RingPayMerchantFlowPage.objContinueWithGoogleCTA, "CTA"));
			verifyElementPresent(RingPayMerchantFlowPage.objContinueWithFacebookCTA,
					getTextVal(RingPayMerchantFlowPage.objContinueWithFacebookCTA, "CTA"));
			loginMobile();
			break;
		default:
			logger.info("Application crashed!!");
			extent.extentLoggerFail("App Crash", "Application crashed!!");
			break;
		}
	}

public void readAndAccept() throws Exception {
		
		click(RingLoginPage.objReadAcceptBtn, "Read & Accept button");
		if (verifyElementPresent(RingLoginPage.objlocationPopup, "RingPay permissions popup")) {

			logger.info("Ring Pay Permissions page (SMS, LOCATION & PHONE)");
			String ringPermissionTxt = getText(RingLoginPage.objlocationPopup);
			Assert.assertEquals(ringPermissionTxt, "Allow Ring Debug to access this device’s location?");

			explicitWaitVisibilityNonDefault(RingLoginPage.objLocAccess, 10);

			click(RingLoginPage.objLocAccess, "Location Access option");
			click(RingLoginPage.objPhoneAccess, "Phone access option");
			click(RingLoginPage.objSMSAccess, "SMS access option");
		}
	}

	public void instaUserDetails(String firstName, String lastName, String mothersName, String email, String gender)
			throws Exception {
		// explicitWaitVisibilityNonDefault(RingUserDetailPage.objFirstName, 10);
		
		click(RingUserDetailPage.objFirstName, "First Name Field");
		type(RingUserDetailPage.objFirstName, firstName, "First Name field");
		
		explicitWaitVisibilityNonDefault(RingUserDetailPage.objLastName, 10);
		click(RingUserDetailPage.objLastName, "Last Name Field");
		type(RingUserDetailPage.objLastName, lastName, "Last Name field");
		
		hideKeyboard();
		explicitWaitVisibilityNonDefault(RingUserDetailPage.objMothersName, 10);
		click(RingUserDetailPage.objMothersName, "Mother's Name Field");
		type(RingUserDetailPage.objMothersName, mothersName, "Mother's Name field");

		hideKeyboard();
		
		explicitWaitVisibilityNonDefault(RingUserDetailPage.objEmail, 10);
		click(RingUserDetailPage.objEmail, "Email Field");
		click(RingUserDetailPage.objNone, "None of the Above Button");
		type(RingUserDetailPage.objEmail, email, "Email Filed");
		hideKeyboard();
		
		dateOfBirth(monthByName, date, year);
		click(RingLoginPage.objGender, "Gender male is selected");
		verifyElementPresentAndClick(RingUserDetailPage.objRegisterBtn, "Proceed Button");
	}

	public void instaLoancongratsScreen() throws Exception {
		verifyElementPresent(RingLoginPage.objCongratsPage, getText(RingLoginPage.objCongratsPage));
		verifyElementPresent(RingLoginPage.objApprovedRingLimit,
				getText(RingLoginPage.objApprovedRingLimit) + getText(RingLoginPage.objApprovedRinglimitAmt));
		verifyElementPresentAndClick(RingLoginPage.objIAcceptCheckBox,
				"I accept the Ring’s Terms & Conditions & IT Act 2000 and Key Fact Statement");
		verifyElementPresentAndClick(RingLoginPage.objAcceptOfferBtn, getText(RingLoginPage.objAcceptOfferBtn));
	}

	public void instaLoanSetPin(String enterMPIN, String ReEnterMPIN) throws Exception {
		verifyElementPresent(RingLoginPage.objSetPinPage, getText(RingLoginPage.objSetPinPage));
		
		click(RingLoginPage.objEnterPin, "Mpin Field");
		type(RingLoginPage.objEnterPin, enterMPIN, "MPIN Field");
		hideKeyboard();
		click(RingUserDetailPage.objReEnterPin, "Re-Enter Pin Field");
		type(RingUserDetailPage.objReEnterPin, ReEnterMPIN, "Re-Enter pin Field");
		verifyElementPresentAndClick(RingLoginPage.objSetinSubmitBtn, getText(RingLoginPage.objSetinSubmitBtn));
	}

	public void instaNewCommunicationAddress() throws Exception {
		explicitWaitVisibility(AddAddressPage.objAddressHeader,30);
		if (verifyElementPresent2(AddAddressPage.objAddressHeader, "New Communication Address Page Header")) {
			click(AddAddressPage.objRoomNoTextField, "Room No Field");
			type(AddAddressPage.objRoomNoTextField, "86", "Room No. Field");

			click(AddAddressPage.objAddressLine_1, "Address Line 1 Field");
			type(AddAddressPage.objAddressLine_1, "44, Borivali", "Address Line 1");

			click(AddAddressPage.objAddressLine_2, "Address Line 2 Field");
			type(AddAddressPage.objAddressLine_2, "Vasa Street",getTextVal(AddAddressPage.objAddressLine_2, "Text Field"));

			hideKeyboard();
			Swipe("up", 2);
			click(AddAddressPage.objPinCodeFieldpulkit, "Pincode Field");
			type(AddAddressPage.objPinCodeFieldpulkit, "400080",getTextVal(AddAddressPage.objPinCodeFieldpulkit, "Text Field"));

			hideKeyboard();
			explicitWaitVisibilityNonDefault(AddAddressPage.objCityTextField, 10);
			logger.info(getTextVal(AddAddressPage.objCityTextField, "City Name autoPopulated after entering pincode"));
			extent.extentLogger("CityName",	getTextVal(AddAddressPage.objCityTextField, "City Name autoPopulated after entering pincode"));

			explicitWaitVisibilityNonDefault(AddAddressPage.objStateTextField, 10);
			logger.info(getTextVal(AddAddressPage.objStateTextField, "State Name autoPopulated after entering pincode"));
			extent.extentLogger("CityName",	getTextVal(AddAddressPage.objStateTextField, "State Name autoPopulated after entering pincode"));

			explicitWaitVisibilityNonDefault(AddAddressPage.objSubmitButton, 10);
			click(AddAddressPage.objSubmitButton, "Submit Button");
		} else {
			logger.warn("Address Page is not displyed");
			extent.extentLoggerWarning("Address Page", "Address Page is not displyed");
		}
	}

	public void instaKycDocument() throws Exception {
		verifyElementPresent(InstaLoanPage.objKYCHeader, getText(InstaLoanPage.objKYCHeader));
		verifyElementPresentAndClick(InstaLoanPage.objKYCFrontAadhar, "Upload Front Aadhar");
		if (verifyElementPresent(InstaLoanPage.objCaptureAadharFrontImage, "Capture Aadhar Front Page")) {

			setPlatform("Web");
			System.out.println("platform changed to web");
			String pf = getPlatform();
			System.out.println(pf);
			waitTime(5000);
			new RingPayBusinessLogic(null);
			waitTime(10000);
			getWebDriver().get(aadharFrontImage);
			waitTime(10000);

			setPlatform("Android");
			initDriver();
			waitTime(5000);
			click(InstaLoanPage.objCaptureBtn, "Clicked on Capture Button");
			click(InstaLoanPage.objKYCCaptureContinueBtn, getText(InstaLoanPage.objKYCCaptureContinueBtn));
			setPlatform("Web");
			String pft = getPlatform();
			System.out.println(pft);
			initDriver();
			waitTime(10000);

			getWebDriver().get(aadharBackImage);
			waitTime(10000);

			setPlatform("Android");
			initDriver();
			waitTime(5000);
			click(InstaLoanPage.objBackAadharCaptureBtn, "Clicked on Capture Button");
			click(InstaLoanPage.objKYCCaptureContinueBtn, getText(InstaLoanPage.objKYCCaptureContinueBtn));

			setPlatform("Web");
			String pf2 = getPlatform();
			System.out.println(pf2);
			initDriver();
			waitTime(10000);
			// BrowsertearDown();
			waitTime(10000);

			setPlatform("Android");
			initDriver();
			waitTime(5000);
			verifyElementPresent(InstaLoanPage.objKYCCAptureSelfie, getText(InstaLoanPage.objKYCCAptureSelfie));
			waitTime(5000);
			verifyElementPresentAndClick(InstaLoanPage.objSelficeCaptureBtn, "Camera Capture Button");
			verifyElementPresentAndClick(InstaLoanPage.objKYCCaptureContinueBtn,
					getText(InstaLoanPage.objKYCCaptureContinueBtn));
			verifyElementPresentAndClick(RingUserDetailPage.objRegisterBtn, "Proceed Button");
		}
	}

	public String instaPanCardDetails(String ringAdminEmail, String ringAdminPassword, String ringAdminOTP,
			String eligible_type, String approved_amount, String offer_id, String repeat_offer_id) throws Exception {

		waitTime(5000);
		setPlatform("Web");
		System.out.println("platform changed to web");
		String pf = getPlatform();
		System.out.println(pf);
		waitTime(5000);
		new RingPayBusinessLogic("ring");
		waitTime(10000);

		getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobileNumber+"&");
		type(InstaLoanPage.objEmail,"shakir.muchumarri@collabera.com","Email text field");
		click(InstaLoanPage.objContinue,"Continue Button");
		waitTime(10000);
		explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
		String userRefNo = getText(InstaLoanPage.objUserRefNo);
		
		executeInsert(userRefNo, eligible_type, approved_amount, offer_id, repeat_offer_id);
		executeQuery("SELECT * FROM db_txn_service.instaloan_whitelisted_users Where user_reference_number = '"
				+ userRefNo + "';", "user_reference_number");

		waitTime(5000);

		BrowsertearDown();
		verifyPan(panCard,dob,firstName,lastName,middleName);
		waitTime(10000);
		setPlatform("Android");
		initDriver();
		waitTime(5000);
		return userReferenceNo;
	}

	public void panCardDetails() throws Exception {
		verifyElementPresent(InstaLoanPage.objPanNoHeader, getText(InstaLoanPage.objPanNoHeader));
		verifyElementPresentAndClick(InstaLoanPage.objPanNoEnter, "Pan Card Number Field");
		type(InstaLoanPage.objPanNoEnter, panCard, "Pan Card Number Field");
		try {
			verifyElementPresentAndClick(RingUserDetailPage.objRegisterBtn, "Proceed Button");
		} catch (Exception e) {

		}
	}

	public String getRefrenceNumber(String mobileNumber) throws Exception {
		waitTime(5000);
		setPlatform("Web");
		System.out.println("platform changed to web");
		String pf = getPlatform();
		System.out.println(pf);
		waitTime(5000);
		new RingPayBusinessLogic("ring");
		waitTime(10000);

		click(InstaLoanPage.objEmail, "Email Field");
		type(InstaLoanPage.objEmail, prop.getproperty("RingAdminEmail"), "Email Field");
		verifyElementPresentAndClick(InstaLoanPage.objContinueBtn, getText(InstaLoanPage.objContinueBtn));
		click(InstaLoanPage.objPasswordField, "Password Field");
		clearField(InstaLoanPage.objPasswordField, "Password Field");
		type(InstaLoanPage.objPasswordField, prop.getproperty("RingAdminPassword"), "Password Field");
		click(InstaLoanPage.objOTPField, "OTP Field");
		clearField(InstaLoanPage.objOTPField, "OTP Field");
		type(InstaLoanPage.objOTPField, prop.getproperty("RingAdminOTP"), "OTP Field");
		verifyElementPresentAndClick(InstaLoanPage.objLoginBtn, getText(InstaLoanPage.objLoginBtn));
		verifyElementPresentAndClick(InstaLoanPage.objUserTab, getText(InstaLoanPage.objUserTab));
		selectByVisibleTextFromDD(InstaLoanPage.objSearchUser, "Mobile Number");
		click(InstaLoanPage.objSearchTerm, "Search Item Field");
		type(InstaLoanPage.objSearchTerm, mobileNumber, "Search Item Field");
		click(InstaLoanPage.objSearchBtn, "Search Button");
		waitTime(10000);
		String userReferenceNo = getText(InstaLoanPage.objUserReferenceNo);
		System.out.println(userReferenceNo);
		BrowsertearDown();
		waitTime(10000);
		setPlatform("Android");
		initDriver();
		waitTime(5000);
		return userReferenceNo;
	}

	public void checkBoxisSelected(By locator) {
		List<WebElement> checkbox = getDriver().findElements(By.className("android.widget.CheckBox"));
		String a = checkbox.get(0).getAttribute("checked");
		logger.info("The checkbox is selection state is - " + a);
		extent.extentLoggerPass("checkBox", "The checkbox is selection state is - " + a);
	}

	public void afterClickOnApplyNowForLoanOfferBanner() throws Exception {
		extent.HeaderChildNode("After click on apply now for loan offer banner");
		getDriver().resetApp();
		//instalonAPIKycVerification();
		loginOnboarding("1");
		mobileNoValidation1(mobileNumber);
		enterOtp(prop.getproperty("OTP"));
		user_reference = instaPanCardDetails(prop.getproperty("RingAdminEmail"), prop.getproperty("RingAdminPassword"),prop.getproperty("RingAdminOTP"), "OPTIONAL", "5000", "2135", "2319");
		readAndAccept();
		waitTime(30000);
		instaUserDetails(firstName, lastName, mothersName, email, gender);
		instalonAPIKycVerification(mobileNumber);
		waitTime(30000);
		explicitWaitVisibilityNonDefault(AddAddressPage.objAddCurrentAddressHeader, 30);
		if (verifyElementPresent2(AddAddressPage.objAddCurrentAddressHeader, "address Page")) {
			addAddress("86", "44, Borivali", "Vasa Street", "Das Gupta Street", "400080");
			click(AddAddressPage.objSubmitBtn, "Submit");
		}
//		if (verifyElementPresent2(KycDocument.kycHeader, "KYC Documents")) {
//			instaKycDocument();
//			waitTime(20000);
//			panCardDetails();
//			logger.info("Pan Card Validate Successfully");
//			extent.extentLoggerPass("Pan Card Details", "Pan Card Details Validatation Successfull");
//		}

		waitTime(10000);
		click(RingLoginPage.objIAcceptCheckBox, "Term and Condition Check Box");
		click(RingLoginPage.objAcceptOfferBtn, "Accept Button");

		instaLoanSetPin(prop.getproperty("InstaLoanMPIN"), prop.getproperty("InstaLoanMPIN"));
		waitTime(13000);
		if (verifyElementPresent2(HomePage.objAdCloseBtn, "Ad Banner")) {
			explicitWaitVisibilityNonDefault(HomePage.objAdCloseBtn, 20);
			click(HomePage.objAdCloseBtn, "Ad Cross Button");
		}
		if (verifyElementPresent2(InstaLoanPage.bannerTitle, "Check")) {
			click(InstaLoanPage.closeBanner, "Close Instalone Banner");
		}
		waitTime(3000);
		click(InstaLoanPage.btnOkaygotIt, "Okay Got It");

		explicitWaitVisibilityNonDefault(HomePage.moreTab, 20);
		click(HomePage.moreTab, "More Tab");
		explicitWaitVisibilityNonDefault(HomePage.tabBankTransfer, 20);
		click(HomePage.tabBankTransfer, "Bank Transfer Button");
		waitTime(20000);

		addbankAccount("5", bankAccountHolder);
		hideKeyboard();
		waitTime(10000);
		hideKeyboard();
		Back(3);

		waitTime(6000);
		click(InstaLoanPage.btnApplyNow, "Apply Now Button");
		waitTime(10000);

		if (verifyElementPresent2(KycDocument.kycHeader, "KYC Documents")) {
			explicitWaitVisibilityNonDefault(KycDocument.kycHeader, 20);
			softAssertion.assertEquals("KYC Documents", getText(KycDocument.kycHeader));
			logger.info("KYC Documents Page Validate Successfully");
			extent.extentLoggerPass("KYC Document","KYC Document Page Validatation Successfull------------TC_Insta_loan_108");
			instaKycDocument();

			waitTime(20000);
			panCardDetails();
			logger.info("Pan Card Validate Successfully");
			extent.extentLoggerPass("Pan Card Details","Pan Card Details Validatation Successfull-------TC_Insta_loan_48");
			extent.extentLoggerPass("Pan Card Details","Pan Card Details Validatation Successfull-------TC_Insta_loan_82");
			extent.extentLoggerPass("Pan Card Details","Pan Card Details Validatation Successfull-------TC_Insta_loan_110");
		}

		explicitWaitVisibilityNonDefault(InstaLoanPage.txtBasicDetails, 20);
		softAssertion.assertEquals("Basic Details", getText(InstaLoanPage.txtBasicDetails));
		logger.info("Basic Details Page Validate");
		extent.extentLoggerPass("Basic Details", "Basic Details Validatation Successfull------TC_Insta_loan_112");
		Aclick(InstaLoanPage.fieldFatherName, "");
		type(InstaLoanPage.fieldFatherName, "Dinesh", "Fathers Name Field");
		try {
			click(RingUserDetailPage.objRegisterBtn, "Proceed Button");
		} catch (Exception e) {
		}
		explicitWaitVisibilityNonDefault(InstaLoanPage.ckbCKYCTerm, 20);
		extent.extentLoggerPass("Consent Popup", "Consent Popup Display-------------TC_Insta_loan_114");
		checkBoxisSelected(InstaLoanPage.objIallowCheckBox);
		checkBoxisSelected(InstaLoanPage.objReadmoreCheckBox);
		extent.extentLoggerPass("Consent should be prechecked", "Consent is prechecked------------TC_Insta_loan_48");

		tapUsingCoordinates(585, 1632);
		softAssertion.assertEquals("Credit Bureau Consent", InstaLoanPage.txtCreditBureauConsent);
		logger.info("Credit Bureau Consent");
		extent.extentLoggerPass("Credit Bureau Consent ","Credit Bureau Consent Display-------------TC_Insta_loan_115");
		extent.extentLoggerPass("Term and Condition", "Term and Condition--------------TC_Insta_loan_126");
		click(InstaLoanPage.btnOkGotIt, "");
		explicitWaitVisibilityNonDefault(RingUserDetailPage.objRegisterBtn, 20);
		tapUsingCoordinates(324, 1688);
		softAssertion.assertEquals("CKYC Consent", InstaLoanPage.txtCKYC_Consent);
		logger.info("CKYC Consent");
		extent.extentLoggerPass("CKYC Consent", "CKYC Consent Display-------------TC_Insta_loan_116");
		extent.extentLoggerPass("Term and Condition", "Term and Condition--------------TC_Insta_loan_126");
		click(InstaLoanPage.btnOkGotIt, "");
		explicitWaitVisibilityNonDefault(RingUserDetailPage.objRegisterBtn, 20);
		tapUsingCoordinates(475, 1948);
		softAssertion.assertEquals("Ok, Got It!", InstaLoanPage.btnOkGotIt);
		logger.info("Read More");
		click(InstaLoanPage.btnOkGotIt, "Okay Got It Button");
		click(InstaLoanPage.ckbCKYCTerm, "Check Box CKYC Term");
		click(RingUserDetailPage.objRegisterBtn, "Proceed Button");
		String toast = getText(UserRegistrationNew.objToast);
		System.out.println(toast);
		softAssertion.assertEquals("Please select the checkbox for relevant authorizations", toast);
		extent.extentLoggerPass("Toast Message Display","Toast Message Display if checkbox unchecked --------------TC_Insta_loan_83");
		extent.extentLoggerPass("Toast Message Display","Toast Message Display if checkbox unchecked --------------TC_Insta_loan_117");
		logger.info(toast + " Validation Successfully");
		extent.extentLogger("Toast Message Validation", toast);
		click(InstaLoanPage.ckbCKYCTerm, "Check Box CKYC Term");
		click(RingUserDetailPage.objRegisterBtn, "Proceed Button");
		if (verifyElementPresent2(AddAddressPage.objAddCurrentAddressHeader, "address Page")) {
			softAssertion.assertEquals("New Current Address", AddAddressPage.objAddCurrentAddressHeader);
			logger.info("New Current Address Validate Successfully");
			extent.extentLoggerPass("New Current Address","New Current Address Validate Successfully--------------TC_Insta_loan_118");
			addAddress("86", "44, Borivali", "Vasa Street", "Das Gupta Street", "400080");
			click(AddAddressPage.objSubmitBtn, "Submit");
		}
		if (verifyElementPresent2(InstaLoanPage.txtPermanentAddress, "Address Page")) {
			click(InstaLoanPage.dropSelectAddress, "Select Address Drop Down");
			explicitWaitVisibilityNonDefault(InstaLoanPage.dropAddressSelection, 20);
			click(InstaLoanPage.dropAddressSelection, "Select Address First Option");
			extent.extentLoggerPass("New Current Address","New Current Address Validate Successfully--------------TC_Insta_loan_118");
			click(InstaLoanPage.ckbDeclarationAddress, "Declaration CheckBox");
			click(InstaLoanPage.linkAddCommunicationAddress, "Add Address Link");
			extent.extentLoggerPass("New Communication Link", "New Communication Link--------------TC_Insta_loan_119");
			explicitWaitVisibilityNonDefault(AddAddressPage.objAddCurrentAddressHeader, 20);
			addAddress("85", "45, Borivali", "Vasa Street", "Das Gupta Street", "400080");
			extent.extentLoggerPass("New Communication Address","New Communication Successfully--------------TC_Insta_loan_120");
			click(AddAddressPage.objSubmitBtn, "Submit");

		}
		explicitWaitVisibilityNonDefault(InstaLoanPage.txtCongratulations, 20);
		softAssertion.assertEquals("Offer", InstaLoanPage.txtOffer);
		softAssertion.assertEquals("Congratulations", InstaLoanPage.txtCongratulations);
		softAssertion.assertEquals("Add Bank Details", InstaLoanPage.btnAddABankDetails);
		explicitWaitVisibilityNonDefault(InstaLoanPage.btnAddABankDetails, 10);
		click(InstaLoanPage.btnAddABankDetails, "Add Bank Account Button");

		waitTime(15000);
		if (verifyElementPresent2(InstaLoanPage.txtExistingBankAcc, "Address Page")) {
			softAssertion.assertEquals("Existing Bank Account", getText(InstaLoanPage.txtExistingBankAcc));
			logger.info("Existing Bank Account Validation Successfull");
			extent.extentLoggerPass("Existing Bank Account","Existing Bank Validation Pass-----------------TC_Insta_loan_121");
			click(InstaLoanPage.selectExistingAccount, "Select Existing Bank Account");
			extent.extentLoggerPass("Select Existing Bank", "Select Existing Bank------------TC_Insta_loan_122");

		} else {
			explicitWaitVisibilityNonDefault(InstaLoanPage.txtAddBankAccount, 20);
			addbankAccount("5", bankAccountHolder);
			explicitWaitVisibilityNonDefault(InstaLoanPage.txtVerifyBankDetails, 20);
			click(InstaLoanPage.btnConfirm, "Confirm Button");
		}

		explicitWaitVisibilityNonDefault(InstaLoanPage.txtCongratulations, 20);
		softAssertion.assertEquals("Offer", InstaLoanPage.txtOffer);
		softAssertion.assertEquals("Congratulations", InstaLoanPage.txtCongratulations);
		click(InstaLoanPage.ckbAcceptOfferPermission, "Permission Accept Offer");
		click(InstaLoanPage.btnAcceptOffer, "Accept Offer");
		explicitWaitVisibilityNonDefault(InstaLoanPage.txtImportant, 20);
		click(InstaLoanPage.btnConfirmOffer, "Confirm Order Button");
		waitTime(12000);
		explicitWaitVisibilityNonDefault(InstaLoanPage.btnHome, 20);
		click(InstaLoanPage.btnHome, "Home Button");
		waitTime(80000);
		Swipe("Down", 2);
		explicitWaitVisibilityNonDefault(HomePage.homeTab, 30);
		logger.info("Home Page");
		explicitWaitVisibilityNonDefault(InstaLoanPage.titleDisbursed, 30);
		softAssertion.assertEquals("Disbursed", InstaLoanPage.titleDisbursed);
		softAssertion.assertEquals("₹5,000", getText(InstaLoanPage.approvedLoanAmount));
		logger.info(getText(InstaLoanPage.titleDisbursed) + " Successful");
		extent.extentLoggerPass("Disbursed Successfull", "Disbursed Successfully Done-------------TC_Insta_loan_123");
		softAssertion.assertEquals("Your Ring limit is Paused while you have an ongoing Insta Loan.",InstaLoanPage.txtRingLimit);
		logger.info("Ring Limit Paused");
		extent.extentLoggerPass("Ring Limit Paused","Ring Limit Paused after accepting Instaloan------TC_Insta_loan_124");
		click(InstaLoanPage.linkViewDetails, "View Details Link");
		explicitWaitVisibilityNonDefault(InstaLoanPage.titleInstalone, 20);
		softAssertion.assertEquals("First EMI due in 15 Days", getText(InstaLoanPage.firstemiduein) + " "+ getText(InstaLoanPage.fifteen) + " " + getText(InstaLoanPage.txtdays));
		System.out.println(getText(InstaLoanPage.firstemiduein) + " " + getText(InstaLoanPage.fifteen) + " "+ getText(InstaLoanPage.txtdays));
		softAssertion.assertEquals("100% waiver", getText(InstaLoanPage.secoundWaiver));
		extent.extentLoggerPass("Secount EMI Waiver", "Secount EMI Waiver Validate--------------TC_Insta_loan_125");
		Back(1);
		ringPayLogout();
		newAdminPanel_appScore("0.107666832000000");
		instaloneOnboardingKYCValidation("2251");
		instaloneOnboardingFatherNameValidation("2251");
	}

	public void instaloneOnboardingWithOfferId(String offerID) throws Exception {
		extent.HeaderChildNode("On_Boarding With Offer ID");
		//instalonAPIKycVerification();
		loginOnboarding("1");
		mobileNoValidation1(mobileNumber);
		enterOtp(prop.getproperty("OTP"));
		user_reference = instaPanCardDetails(prop.getproperty("RingAdminEmail"), prop.getproperty("RingAdminPassword"),prop.getproperty("RingAdminOTP"), "OPTIONAL", "5000", offerID, "2319");
		// waitTime(10000);
		if (verifyElementPresent2(RingLoginPage.objReadAcceptBtn, "Permission Page")) {
			readAndAccept();
		}
		waitTime(50000);
		instaUserDetails(firstName, lastName, mothersName, email, gender);
		instalonAPIKycVerification(mobileNumber);
		waitTime(50000);
		if (verifyElementPresent2(AddAddressPage.objAddCurrentAddressHeader, "address Page")) {
			addAddress("86", "44, Borivali", "Vasa Street", "Das Gupta Street", "400080");
			click(AddAddressPage.objSubmitBtn, "Submit");
		}
		newAdminPanel_appScore("0.107666832000000");
//		if (verifyElementPresent2(KycDocument.kycHeader, "KYC Documents")) {
//			instaKycDocument();
//
//			waitTime(20000);
//			panCardDetails();
//			logger.info("Pan Card Validate Successfully");
//			extent.extentLoggerPass("Pan Card Details", "Pan Card Details Validatation Successfull");
//		}

		waitTime(20000);
		click(RingLoginPage.objIAcceptCheckBox, "Term and Condition Check Box");
		click(RingLoginPage.objAcceptOfferBtn, "Accept Button");

		instaLoanSetPin(prop.getproperty("InstaLoanMPIN"), prop.getproperty("InstaLoanMPIN"));
		waitTime(13000);
		if (verifyElementPresent2(HomePage.objAdCloseBtn, "Ad Banner")) {
			explicitWaitVisibilityNonDefault(HomePage.objAdCloseBtn, 20);
			click(HomePage.objAdCloseBtn, "Ad Cross Button");
		}
		if (verifyElementPresent2(InstaLoanPage.bannerTitle, "Check")) {
			click(InstaLoanPage.closeBanner, "Close Instalone Banner");
		}
		waitTime(3000);
		click(InstaLoanPage.btnOkaygotIt, "Okay Got It");

		waitTime(6000);
		click(InstaLoanPage.btnApplyNow, "Apply Now Button");
		waitTime(10000);

		if (verifyElementPresent2(KycDocument.kycHeader, "KYC Documents")) {
			explicitWaitVisibilityNonDefault(KycDocument.kycHeader, 20);
			logger.info("KYC Documents Page Validate Successfully");
			instaKycDocument();
			waitTime(20000);
			panCardDetails();
			logger.info("Pan Card Validate Successfully");
		}
		waitTime(5000);
		if (verifyElementPresent2(InstaLoanPage.txtBasicDetails, "Basic Details")) {
			explicitWaitVisibilityNonDefault(InstaLoanPage.txtBasicDetails, 20);

			click(InstaLoanPage.fieldFatherName, "");
			type(InstaLoanPage.fieldFatherName, "Dinesh", "Fathers Name Field");
			try {
				click(RingUserDetailPage.objRegisterBtn, "Proceed Button");
			} catch (Exception e) {
			}
		} else {
			extent.extentLoggerPass("Father Name ", "Father Name is Already Available----------TC_Insta_loan_111");
			logger.info("Father Name Already Available");
		}

		explicitWaitVisibilityNonDefault(InstaLoanPage.ckbCKYCTerm, 20);

		click(RingUserDetailPage.objRegisterBtn, "Proceed Button");
		if (verifyElementPresent2(AddAddressPage.objAddCurrentAddressHeader, "address Page")) {
			softAssertion.assertEquals("New Current Address", AddAddressPage.objAddCurrentAddressHeader);
			logger.info("New Current Address Validate Successfully");
			addAddress("86", "44, Borivali", "Vasa Street", "Das Gupta Street", "400080");
			click(AddAddressPage.objSubmitBtn, "Submit");
		}
		if (verifyElementPresent2(InstaLoanPage.txtPermanentAddress, "Address Page")) {
			click(InstaLoanPage.dropSelectAddress, "Select Address Drop Down");
			explicitWaitVisibilityNonDefault(InstaLoanPage.dropAddressSelection, 20);
			click(InstaLoanPage.dropAddressSelection, "Select Address First Option");
			click(RingUserDetailPage.objRegisterBtn, "Proceed Button");
		}
		waitTime(5000);
		explicitWaitVisibilityNonDefault(InstaLoanPage.txtCongratulations, 30);
		click(InstaLoanPage.btnAddABankDetails, "Add Bank Account Button");
		waitTime(15000);
		if (verifyElementPresent2(InstaLoanPage.txtExistingBankAcc, "Existing Bank")) {
			softAssertion.assertEquals("Existing Bank Account", getText(InstaLoanPage.txtExistingBankAcc));
			logger.info("Existing Bank Account Validation Successfull");
			extent.extentLoggerPass("Existing Bank Account","Existing Bank Validation Pass-----------------TC_Insta_loan_121");
			click(InstaLoanPage.selectExistingAccount, "Select Existing Bank Account");
			extent.extentLoggerPass("Select Existing Bank", "Select Existing Bank------------TC_Insta_loan_122");
		} else {
			explicitWaitVisibilityNonDefault(InstaLoanPage.txtAddBankAccount, 20);
			BankAccountRandom = addbankAccount("5", bankAccountHolder);
			// explicitWaitVisibilityNonDefault(InstaLoanPage.txtVerifyBankDetails, 20);
			click(InstaLoanPage.btnConfirm, "Confirm Button");
		}
		explicitWaitVisibilityNonDefault(InstaLoanPage.txtCongratulations, 30);
		softAssertion.assertEquals("Offer", InstaLoanPage.txtOffer);
		softAssertion.assertEquals("Congratulations", InstaLoanPage.txtCongratulations);
		click(InstaLoanPage.ckbAcceptOfferPermission, "Permission Accept Offer");
		click(InstaLoanPage.btnAcceptOffer, "Accept Offer");
		explicitWaitVisibilityNonDefault(InstaLoanPage.txtImportant, 20);
		click(InstaLoanPage.btnConfirmOffer, "Confirm Order Button");
		waitTime(12000);
		explicitWaitVisibilityNonDefault(InstaLoanPage.btnHome, 20);
		click(InstaLoanPage.btnHome, "Home Button");
		waitTime(24000);
		Swipe("Down", 2);
		explicitWaitVisibilityNonDefault(HomePage.homeTab, 30);
		logger.info("Home Page");
	}

	public void instaloneOnboardingKYCValidation(String offerID) throws Exception {

		extent.HeaderChildNode("Instaloan KYC Validation");
		mockUserAPI();

		loginOnboarding("1");
		mobileNoValidation1(mobileNumber);
		enterOtp(prop.getproperty("OTP"));
		user_reference = instaPanCardDetails(prop.getproperty("RingAdminEmail"), prop.getproperty("RingAdminPassword"),prop.getproperty("RingAdminOTP"), "OPTIONAL", "5000", offerID, "2319");
		readAndAccept();
		waitTime(50000);

		instaUserDetails(firstName, lastName, mothersName, email, gender);

		waitTime(50000);

		if (verifyElementPresent2(AddAddressPage.objAddCurrentAddressHeader, "address Page")) {
			addAddress("86", "44, Borivali", "Vasa Street", "Das Gupta Street", "400080");
			click(AddAddressPage.objSubmitBtn, "Submit");
		}
		if (verifyElementPresent2(KycDocument.kycHeader, "KYC Documents")) {
			explicitWaitVisibilityNonDefault(KycDocument.kycHeader, 20);
			logger.info("KYC Documents Page Validate Successfully");

			instaKycDocument();
			waitTime(12000);
			if (verifyElementPresent2(InstaLoanPage.txtSorry, "sorry Page")) {
				explicitWaitVisibilityNonDefault(InstaLoanPage.txtSorry, 30);
				softAssertion.assertEquals("Sorry!", InstaLoanPage.txtSorry);
				// softAssertion.assertEquals("Your application cannot be processed right now.",
				// InstaLoanPage.txtSorryDetails);
				logger.info("Sorry Page Display");
				extent.extentLoggerPass("Sorry Page Display","Sorry Page Display due to KYC Document not upload properly------------TC_Insta_loan_109");
				waitTime(20000);
				explicitWaitVisibilityNonDefault(InstaLoanPage.sorryPageTopHamBurgger, 15);
				click(InstaLoanPage.sorryPageTopHamBurgger, "Top left menu button");
				explicitWaitVisibilityNonDefault(RingLoginPage.objProfileSelect, 10);
				click(RingLoginPage.objProfileSelect, "Profile Select Button");
				explicitWaitVisibilityNonDefault(RingLoginPage.objLogoutBtn, 10);
				click(RingLoginPage.objLogoutBtn, "Logout Button");
				logger.info("Logout popup comes up");
				extent.extentLoggerPass("Logout popup", "Logout popup comes up");
				explicitWaitVisibilityNonDefault(RingLoginPage.objYesBtn, 10);
				click(RingLoginPage.objYesBtn, "Yes confirmation button");
			} else {
				waitTime(20000);
				panCardDetails();
				waitTime(20000);
				click(RingLoginPage.objIAcceptCheckBox, "Term and Condition Check Box");
				click(RingLoginPage.objAcceptOfferBtn, "Accept Button");
				instaLoanSetPin(prop.getproperty("InstaLoanMPIN"), prop.getproperty("InstaLoanMPIN"));
				waitTime(13000);

				if (verifyElementPresent2(HomePage.objAdCloseBtn, "Ad Banner")) {
					explicitWaitVisibilityNonDefault(HomePage.objAdCloseBtn, 20);
					click(HomePage.objAdCloseBtn, "Ad Cross Button");
				}
				if (verifyElementPresent2(InstaLoanPage.bannerTitle, "Check")) {
					click(InstaLoanPage.closeBanner, "Close Instalone Banner");
					waitTime(3000);
					click(InstaLoanPage.btnOkaygotIt, "Okay Got It");
				}
			}

			waitTime(6000);
			click(InstaLoanPage.btnApplyNow, "Apply Now Button");
			waitTime(10000);
			if (verifyElementPresent2(KycDocument.kycHeader, "KYC Documents")) {
				explicitWaitVisibilityNonDefault(KycDocument.kycHeader, 20);
				logger.info("KYC Documents Page Validate Successfully");

				instaKycDocument();
				waitTime(12000);
				explicitWaitVisibilityNonDefault(InstaLoanPage.txtSorry, 30);
				softAssertion.assertEquals("Sorry!", InstaLoanPage.txtSorry);
				logger.info("Sorry Page Display");
				extent.extentLoggerPass("Sorry Page Display","Sorry Page Display due to KYC Document not upload properly------------TC_Insta_loan_109");
				waitTime(20000);
				explicitWaitVisibilityNonDefault(InstaLoanPage.sorryPageTopHamBurgger, 15);
				click(InstaLoanPage.sorryPageTopHamBurgger, "Top left menu button");

				explicitWaitVisibilityNonDefault(RingLoginPage.objProfileSelect, 10);
				click(RingLoginPage.objProfileSelect, "Profile Select Button");

				explicitWaitVisibilityNonDefault(RingLoginPage.objLogoutBtn, 10);

				click(RingLoginPage.objLogoutBtn, "Logout Button");

				logger.info("Logout popup comes up");
				extent.extentLoggerPass("Logout popup", "Logout popup comes up");

				explicitWaitVisibilityNonDefault(RingLoginPage.objYesBtn, 10);
				click(RingLoginPage.objYesBtn, "Yes confirmation button");
			}
		}
	}
	public void instaloneOnboardingFatherNameValidation(String offerID) throws Exception {

		extent.HeaderChildNode("Instaloan Father Details Validation");
		mockUserAPI();

		loginOnboarding("1");
		mobileNoValidation1(mobileNumber);
		enterOtp(prop.getproperty("OTP"));
		user_reference = instaPanCardDetails(prop.getproperty("RingAdminEmail"), prop.getproperty("RingAdminPassword"),
				prop.getproperty("RingAdminOTP"), "OPTIONAL", "5000", offerID, "2319");
		// waitTime(10000);
		if (verifyElementPresent2(RingLoginPage.objReadAcceptBtn, "Permission Page")) {
			readAndAccept();
		}
		waitTime(50000);
		String query = "update users set father_name='Suresh' where user_reference_number='" + user_reference + "';";
		DBUpdate(query);
		logger.info("Father Name Update In DataBase");

		instaUserDetails(firstName, lastName, mothersName, email, gender);

		waitTime(50000);
		if (verifyElementPresent2(AddAddressPage.objAddCurrentAddressHeader, "address Page")) {
			addAddress("86", "44, Borivali", "Vasa Street", "Das Gupta Street", "400080");
			click(AddAddressPage.objSubmitBtn, "Submit");
		}
		newAdminPanel_appScore("0.107666832000000");
		if (verifyElementPresent2(KycDocument.kycHeader, "KYC Documents")) {
			instaKycDocument();

			waitTime(20000);
			panCardDetails();
			logger.info("Pan Card Validate Successfully");
			extent.extentLoggerPass("Pan Card Details", "Pan Card Details Validatation Successfull");
		}

		waitTime(20000);
		click(RingLoginPage.objIAcceptCheckBox, "Term and Condition Check Box");
		click(RingLoginPage.objAcceptOfferBtn, "Accept Button");

		instaLoanSetPin(prop.getproperty("InstaLoanMPIN"), prop.getproperty("InstaLoanMPIN"));
		waitTime(13000);
		if (verifyElementPresent2(HomePage.objAdCloseBtn, "Ad Banner")) {
			explicitWaitVisibilityNonDefault(HomePage.objAdCloseBtn, 20);
			click(HomePage.objAdCloseBtn, "Ad Cross Button");
		}
		if (verifyElementPresent2(InstaLoanPage.bannerTitle, "Check")) {
			click(InstaLoanPage.closeBanner, "Close Instalone Banner");
		}
		waitTime(3000);
		click(InstaLoanPage.btnOkaygotIt, "Okay Got It");

		waitTime(6000);
		click(InstaLoanPage.btnApplyNow, "Apply Now Button");
		waitTime(10000);
		if (verifyElementPresent2(KycDocument.kycHeader, "KYC Documents")) {
			explicitWaitVisibilityNonDefault(KycDocument.kycHeader, 20);
			logger.info("KYC Documents Page Validate Successfully");

			instaKycDocument();

			waitTime(20000);
			panCardDetails();
			logger.info("Pan Card Validate Successfully");
		}
		waitTime(5000);
		if (verifyElementPresent2(InstaLoanPage.txtBasicDetails, "Basic Details")) {
			explicitWaitVisibilityNonDefault(InstaLoanPage.txtBasicDetails, 20);

			click(InstaLoanPage.fieldFatherName, "");
			type(InstaLoanPage.fieldFatherName, "Dinesh", "Fathers Name Field");
			try {
				click(RingUserDetailPage.objRegisterBtn, "Proceed Button");
			} catch (Exception e) {
			}
		} else {
			new TouchAction(getDriver()).tap(PointOption.point(324, 1688)).release().perform();
			softAssertion.assertEquals("CKYC Consent", InstaLoanPage.txtCKYC_Consent);
			logger.info("CKYC Consent");
			extent.extentLoggerPass("Father Name ", "Father Name is Already Available-------TC_Insta_loan_111");
			click(InstaLoanPage.btnOkGotIt, "Okay Got It Button");
			logger.info("Father Name Already Available");
		}

	}

	public void instaLoanRepayment() throws Exception {
		extent.HeaderChildNode("Insta Loan Repayment");
		getDriver().resetApp();

		// waitTime(30000);
		instaloneOnboardingWithOfferId("2135");
		// Back(1);
		explicitWaitVisibilityNonDefault(InstaLoanPage.objMenuTab, 30);
		click(InstaLoanPage.objMenuTab, "Top left menu button");
		explicitWaitVisibilityNonDefault(HomPageNew.transacTab, 20);
		click(HomPageNew.transacTab, "Transaction Tab");
		explicitWaitVisibilityNonDefault(HomPageNew.objHistory, 20);
		click(HomPageNew.objLoanHistory, "Loan History");
		waitTime(4000);
		click(HomPageNew.linkViewDetails, "View Details");
		explicitWaitVisibilityNonDefault(HomPageNew.txtLoanDetails, 20);
		click(InstaLoanPage.btnPayNow, "Pay Now");
		explicitWaitVisibilityNonDefault(InstaLoanPage.txtPayment, 20);
		softAssertion.assertEquals("Payment", getText(InstaLoanPage.txtPayment));
		extent.extentLoggerPass("Redirect Payment", "Redirect To Payment Mode-------TC_Insta_loan_165");
		Back(4);
		click(InstaLoanPage.linkViewDetails, "View Details Link");
		explicitWaitVisibilityNonDefault(InstaLoanPage.titleInstalone, 20);

		click(InstaLoanPage.rbnFullPayment, "Full Payment Radio Button");
		click(InstaLoanPage.btnPayNow, "Pay Now");
		explicitWaitVisibilityNonDefault(InstaLoanPage.txtPayment, 20);
		softAssertion.assertEquals("Payment", getText(InstaLoanPage.txtPayment));
		extent.extentLoggerPass("Make Payment", "Make Payment from Loan Details-------TC_Insta_loan_166");
		netBankingPayment();
		verifyElementPresentAndClick(HomPageNew.objHomePage, "Go To Homepage Button");
		logger.info("Home Screen");
		extent.extentLoggerPass("Home Screen", "After Click on Go TO Home Button-------TC_Insta_loan_168");
		waitTime(13000);
		String dbref = executeQueryWithReferanceNumber(user_reference);

		System.out.println("Database data------------" + dbref);
		softAssertion.assertEquals(user_reference, dbref);
		updateEmail(email, user_reference);
		if (verifyElementPresent2(HomePage.objAdCloseBtn, "Ad Banner")) {
			explicitWaitVisibilityNonDefault(HomePage.objAdCloseBtn, 20);
			Aclick(HomePage.objAdCloseBtn, "Ad Cross Button");
		}

	}

	public void netBankingPayment() throws Exception {
		verifyElementPresentAndClick(HomPageNew.objNetBankingOption, getText(HomPageNew.objNetBankingOption));
		waitTime(10000);
		explicitWaitVisibilityNonDefault(HomPageNew.objNetbanking, 20);
		// waitTime(5000);
		click(HomPageNew.objNetbanking, getText(HomPageNew.objNetbanking));
		click(HomPageNew.selectDifferentBankDropDown, "Select Different Bank From Dropdown");
		explicitWaitVisibilityNonDefault(HomPageNew.txtAllbanks, 20);
		click(HomPageNew.searchBank, "Search Bank");
		type(HomPageNew.searchBank, "State Bank Of Hyderabad", "State Bank of Hyderabad");
		click(HomPageNew.selectBank, "Select Bank");

		waitTime(4000);
		click(HomPageNew.objPayNowBtn, "Pay Now Button");
		verifyElementPresentAndClick(HomPageNew.objSuccessBtn, "Success Button");
		waitTime(15000);
		explicitWaitVisibilityNonDefault(InstaLoanPage.txtPayment, 30);
		verifyElementPresentAndClick(HomPageNew.objHomePage, "Go To Homepage Button");

	}

	public void insta_loanAgain(String status) throws Exception {
		explicitWaitVisibilityNonDefault(InstaLoanPage.btnApplyNow, 30);
		click(InstaLoanPage.btnApplyNow, "Apply Now Button");
		waitTime(8000);
		if (verifyElementPresent2(InstaLoanPage.txtPermanentAddress, "Address Page")) {
			click(InstaLoanPage.dropSelectAddress, "Select Address Drop Down");
			explicitWaitVisibilityNonDefault(InstaLoanPage.dropAddressSelection, 20);
			click(InstaLoanPage.dropAddressSelection, "Select Address First Option");
			click(RingUserDetailPage.objRegisterBtn, "Proceed Button");
		}
		explicitWaitVisibilityNonDefault(InstaLoanPage.txtCongratulations, 30);
		click(InstaLoanPage.btnAddABankDetails, "Add Bank Account Button");
		waitTime(15000);
		if (verifyElementPresent2(InstaLoanPage.txtExistingBankAcc, "Existing Bank")) {
			softAssertion.assertEquals("Existing Bank Account", getText(InstaLoanPage.txtExistingBankAcc));
			logger.info("Existing Bank Account Validation Successfull");
			click(InstaLoanPage.selectExistingAccount, "Select Existing Bank Account");
		} else {
			explicitWaitVisibilityNonDefault(InstaLoanPage.txtAddBankAccount, 20);
			addbankAccount("5", bankAccountHolder);
			explicitWaitVisibilityNonDefault(InstaLoanPage.txtVerifyBankDetails, 20);
			click(InstaLoanPage.btnConfirm, "Confirm Button");
		}

		explicitWaitVisibilityNonDefault(InstaLoanPage.txtCongratulations, 30);
		softAssertion.assertEquals("Offer", InstaLoanPage.txtOffer);
		softAssertion.assertEquals("Congratulations", InstaLoanPage.txtCongratulations);
		BankAccountRandom = updateBankAccStatus(status, BankAccountRandom, user_reference);
		click(InstaLoanPage.ckbAcceptOfferPermission, "Permission Accept Offer");
		click(InstaLoanPage.btnAcceptOffer, "Accept Offer");
		explicitWaitVisibilityNonDefault(InstaLoanPage.txtImportant, 20);
		click(InstaLoanPage.btnConfirmOffer, "Confirm Order Button");
		waitTime(12000);
		explicitWaitVisibilityNonDefault(InstaLoanPage.btnHome, 20);

		click(InstaLoanPage.btnHome, "Home Button");
		waitTime(8000);
		explicitWaitVisibilityNonDefault(HomePage.homeTab, 30);
		logger.info("Home Page");
	}

	public void InstaLoanDetailScreen() throws Exception {

		extent.HeaderChildNode("Instaloan Details Screen");
		getDriver().resetApp();
		instaloneOnboardingWithOfferId("2135");

		click(InstaLoanPage.linkViewDetails, "View Details Link");
		explicitWaitVisibilityNonDefault(InstaLoanPage.titleInstalone, 20);
		softAssertion.assertEquals("Insta Loan", getText(InstaLoanPage.titleInstalone));
		logger.info("Insta Loan Screen Display");
		extent.extentLoggerPass("Insta Loan details screen ", "Insta Loan details screen -----TC_Insta_loan_170");
		softAssertion.assertEquals("First EMI due in 15 Days", getText(InstaLoanPage.firstemiduein) + " "+ getText(InstaLoanPage.fifteen) + " " + getText(InstaLoanPage.txtdays));
		System.out.println(getText(InstaLoanPage.firstemiduein) + " " + getText(InstaLoanPage.fifteen) + " "+ getText(InstaLoanPage.txtdays));
		softAssertion.assertEquals("Pay Full Amount", getText(InstaLoanPage.txtPayFullAmount));
		softAssertion.assertEquals("1st installment", getText(InstaLoanPage.txtfirstInstall));
		softAssertion.assertEquals("2nd installment", getText(InstaLoanPage.txtsecoundInstall));
		logger.info("Validate offer of 15 days");
		extent.extentLoggerPass("verified for insta loan 15 days offer loan details screen","Verified for insta loan 15 days offer loan details screen with pay full amount, 1st installment and 2nd installment-----TC_Insta_loan_173");
		Back(1);
		explicitWaitVisibilityNonDefault(InstaLoanPage.objMenuTab, 30);
		click(InstaLoanPage.objMenuTab, "Top left menu button");
		explicitWaitVisibilityNonDefault(HomPageNew.transacTab, 20);
		click(HomPageNew.transacTab, "Transaction Tab");
		explicitWaitVisibilityNonDefault(HomPageNew.objHistory, 20);
		click(HomPageNew.objLoanHistory, "Loan History");
		waitTime(4000);
		click(InstaLoanPage.btnPayEMI, "Pay EMI Button");
		click(InstaLoanPage.btnPayEMI, "Pay EMI Button");

		explicitWaitVisibilityNonDefault(InstaLoanPage.txtPayment, 20);
		netBankingPayment();
		waitTime(10000);
		System.out.println(BankAccountRandom);
		insta_loanAgain("onhold");
		waitTime(5000);
		softAssertion.assertEquals("On Hold", getText(InstaLoanPage.txtOnHold));
		logger.info("Loan On hold");
		extent.extentLoggerPass("Instaloan Is in On Hold", "Instaloan is in On_Hold-----TC_Insta_loan_172");
		explicitWaitVisibilityNonDefault(InstaLoanPage.objMenuTab, 30);
		click(InstaLoanPage.objMenuTab, "Top left menu button");
		explicitWaitVisibilityNonDefault(HomPageNew.transacTab, 20);
		click(HomPageNew.transacTab, "Transaction Tab");
		explicitWaitVisibilityNonDefault(HomPageNew.objHistory, 20);
		click(HomPageNew.objLoanHistory, "Loan History");
		waitTime(4000);
		click(InstaLoanPage.btnPayEMI, "Pay EMI");
		click(InstaLoanPage.btnPayEMI, "Pay EMI Button");

		explicitWaitVisibilityNonDefault(InstaLoanPage.txtPayment, 20);
		netBankingPayment();
		waitTime(10000);
		System.out.println(BankAccountRandom);
		insta_loanAgain("inprogress");
		waitTime(5000);
		softAssertion.assertEquals("In Process", getText(InstaLoanPage.txtInProgress));
		logger.info("Loan In-Progress");
		extent.extentLoggerPass("Instaloan Is In-Progress", "Instaloan is In-Progress-----TC_Insta_loan_171");
		explicitWaitVisibilityNonDefault(InstaLoanPage.objMenuTab, 30);
		click(InstaLoanPage.objMenuTab, "Top left menu button");
		explicitWaitVisibilityNonDefault(HomPageNew.transacTab, 20);
		click(HomPageNew.transacTab, "Transaction Tab");
		explicitWaitVisibilityNonDefault(HomPageNew.objHistory, 20);
		click(HomPageNew.objLoanHistory, "Loan History");
		waitTime(4000);
		click(InstaLoanPage.btnPayEMI, "Pay EMI");
		click(InstaLoanPage.btnPayEMI, "Pay EMI Button");

		explicitWaitVisibilityNonDefault(InstaLoanPage.txtPayment, 20);
		netBankingPayment();
		waitTime(10000);
		ringPayLogout();

// 62 Days Offer ID Validation and Execution

		instaloneOnboardingWithOfferId("2251");
		click(InstaLoanPage.linkViewDetails, "View Details Link");
		explicitWaitVisibilityNonDefault(InstaLoanPage.titleInstalone, 20);
		click(InstaLoanPage.firstInstallRadioBtn, "1st Installment Radio Button");
		click(InstaLoanPage.firtstInsallmentPay, "Pay");
		explicitWaitVisibilityNonDefault(InstaLoanPage.txtPayment, 20);
		netBankingPayment();
		waitTime(10000);
		click(InstaLoanPage.linkViewDetails, "View Details Link");
		explicitWaitVisibilityNonDefault(InstaLoanPage.titleInstalone, 20);
		softAssertion.assertEquals("Second EMI due in 62 Days", getText(InstaLoanPage.Secoundemiduein) + " "+ getText(InstaLoanPage.sixtytwo) + " " + getText(InstaLoanPage.txtdays));
		System.out.println(getText(InstaLoanPage.Secoundemiduein) + " " + getText(InstaLoanPage.sixtytwo) + " "+ getText(InstaLoanPage.txtdays));
		logger.info("Validate offer of 62 days");
		extent.extentLoggerPass("verified for insta loan 62 days offer loan details screen","Verified for insta loan 62 days offer loan details screen with pay full amount, 2 Month Installment and 2 Month Installment-----TC_Insta_loan_174");

		explicitWaitVisibilityNonDefault(InstaLoanPage.titleInstalone, 20);
		click(InstaLoanPage.secoundInstallRadioBtn, "2nd Installment Radio Button");
		click(InstaLoanPage.secoundInsallmentPay, "Pay");
		explicitWaitVisibilityNonDefault(InstaLoanPage.txtPayment, 20);
		netBankingPayment();
		waitTime(10000);

		ringPayLogout();

// 3 Months Offer ID Validation and Execution		

		instaloneOnboardingWithOfferId("2413");
		click(InstaLoanPage.linkViewDetails, "View Details Link");
		explicitWaitVisibilityNonDefault(InstaLoanPage.titleInstalone, 20);
		click(InstaLoanPage.firstInstallRadioBtn, "1st Installment Radio Button");
		click(InstaLoanPage.firtstInsallmentPay, "Pay");
		explicitWaitVisibilityNonDefault(InstaLoanPage.txtPayment, 20);
		netBankingPayment();
		waitTime(10000);

		click(InstaLoanPage.linkViewDetails, "View Details Link");
		explicitWaitVisibilityNonDefault(InstaLoanPage.titleInstalone, 20);
		click(InstaLoanPage.secoundInstallRadioBtn, "2nd Installment Radio Button");
		click(InstaLoanPage.secoundInsallmentPay, "Pay");
		explicitWaitVisibilityNonDefault(InstaLoanPage.txtPayment, 20);
		netBankingPayment();
		click(InstaLoanPage.linkViewDetails, "View Details Link");
		softAssertion.assertEquals("Third EMI due in 90 Days", getText(InstaLoanPage.thirdemiduein) + " "+ getText(InstaLoanPage.ninty) + " " + getText(InstaLoanPage.txtdays));
		System.out.println(getText(InstaLoanPage.thirdemiduein) + " " + getText(InstaLoanPage.ninty) + " "+ getText(InstaLoanPage.txtdays));
		logger.info("Validate offer of 90 days");
		extent.extentLoggerPass("verified for insta loan 3 Months offer loan details screen","Verified for insta loan 3 Months offer loan details screen with pay full amount, 3 Month Installment and 3rd Month Installment-----TC_Insta_loan_175");

		explicitWaitVisibilityNonDefault(InstaLoanPage.titleInstalone, 20);
		click(InstaLoanPage.thirdInstallRadioBtn, "3rd Installment Radio Button");
		click(InstaLoanPage.thirdInsallmentPay, "Pay");
		explicitWaitVisibilityNonDefault(InstaLoanPage.txtPayment, 20);
		netBankingPayment();
		waitTime(10000);

		ringPayLogout();

// 	6 Months Offer ID Validation and Execution		

		instaloneOnboardingWithOfferId("2311");
		click(InstaLoanPage.linkViewDetails, "View Details Link");
		explicitWaitVisibilityNonDefault(InstaLoanPage.titleInstalone, 20);
		click(InstaLoanPage.firstInstallRadioBtn, "1st Installment Radio Button");
		click(InstaLoanPage.firtstInsallmentPay, "Pay");
		explicitWaitVisibilityNonDefault(InstaLoanPage.txtPayment, 20);
		netBankingPayment();
		waitTime(10000);

		click(InstaLoanPage.linkViewDetails, "View Details Link");
		explicitWaitVisibilityNonDefault(InstaLoanPage.titleInstalone, 20);
		click(InstaLoanPage.secoundInstallRadioBtn, "2nd Installment Radio Button");
		click(InstaLoanPage.secoundInsallmentPay, "Pay");
		explicitWaitVisibilityNonDefault(InstaLoanPage.txtPayment, 20);
		netBankingPayment();
		waitTime(10000);

		click(InstaLoanPage.linkViewDetails, "View Details Link");
		explicitWaitVisibilityNonDefault(InstaLoanPage.titleInstalone, 20);
		click(InstaLoanPage.thirdInstallRadioBtn, "3rd Installment Radio Button");
		click(InstaLoanPage.thirdInsallmentPay, "Pay");
		explicitWaitVisibilityNonDefault(InstaLoanPage.txtPayment, 20);
		netBankingPayment();
		waitTime(10000);

		click(InstaLoanPage.linkViewDetails, "View Details Link");
		explicitWaitVisibilityNonDefault(InstaLoanPage.titleInstalone, 20);
		click(InstaLoanPage.fourthInstallRadioBtn, "4th Installment Radio Button");
		click(InstaLoanPage.fourthInsallmentPay, "Pay");
		explicitWaitVisibilityNonDefault(InstaLoanPage.txtPayment, 20);
		netBankingPayment();
		waitTime(10000);
		click(InstaLoanPage.linkViewDetails, "View Details Link");
		Swipe("up", 2);

		explicitWaitVisibilityNonDefault(InstaLoanPage.titleInstalone, 20);
		click(InstaLoanPage.fifthInstallRadioBtn, "5th Installment Radio Button");
		click(InstaLoanPage.fifthInsallmentPay, "Pay");
		explicitWaitVisibilityNonDefault(InstaLoanPage.txtPayment, 20);
		netBankingPayment();
		waitTime(10000);

		click(InstaLoanPage.linkViewDetails, "View Details Link");
		softAssertion.assertEquals("Sixth EMI due in 181 Days", getText(InstaLoanPage.sixthemiduein) + " "+ getText(InstaLoanPage.oneHunderedeightyOne) + " " + getText(InstaLoanPage.txtdays));
		System.out.println(getText(InstaLoanPage.sixthemiduein) + " " + getText(InstaLoanPage.oneHunderedeightyOne)+ " " + getText(InstaLoanPage.txtdays));
		logger.info("Validate offer of 181 days");
		extent.extentLoggerPass("verified for insta loan 6 Months offer loan details screen","Verified for insta loan 6 Months offer loan details screen with pay full amount, 6 Month Installment and 6rd Month Installment-----TC_Insta_loan_176");

		// click(InstaLoanPage.linkViewDetails, "View Details Link");
		Swipe("up", 2);

		explicitWaitVisibilityNonDefault(InstaLoanPage.titleInstalone, 20);
		click(InstaLoanPage.sixthInstallRadioBtn, "6th Installment Radio Button");
		click(InstaLoanPage.sixthInsallmentPay, "Pay");
		explicitWaitVisibilityNonDefault(InstaLoanPage.txtPayment, 20);
		netBankingPayment();
		waitTime(10000);

		ringPayLogout();

	}

	public void PanValidation() throws Exception {
		extent.HeaderChildNode("Validation of PAN Module");
		HashMap mockUserDetails = mockUserAPI(prop.getproperty("MockAPIurl"), prop.getproperty("gender"),prop.getproperty("encrypted"));

		String otherspanCard = (String) mockUserDetails.get("panCard");
		mockUserAPI();
		loginOnboarding("1");
		mobileNoValidation1(mobileNumber);
		enterOtp(prop.getproperty("OTP"));
		readAndAccept();
		waitTime(60000);
		instaUserDetails(firstName, lastName, mothersName, email, gender);
		if (verifyElementPresent(AddAddressPage.objAddCurrentAddressHeader, "address Page")) {
			addAddress("86", "44, Borivali", "Vasa Street", "Das Gupta street", "400080");
			click(AddAddressPage.objSubmitBtn, "Submit");
		}
		waitTime(10000);
		user_reference = instaPanCardDetails(prop.getproperty("RingAdminEmail"), prop.getproperty("RingAdminPassword"),
				prop.getproperty("RingAdminOTP"), "OPTIONAL", "5000", "2311", "2319");
		instaKycDocument();
		waitTime(30000);
		click(PAN_DetailsPage.panCardNumberField, "Field");
		type(PAN_DetailsPage.panCardNumberField, prop.getproperty("onlyNumber"), "Only Number Formate");
		click(PAN_DetailsPage.btnProceed, "Procced");
		softAssertion.assertEquals("Please enter a valid pan number", getText(PAN_DetailsPage.errorMessage));
		logger.info("To validate the pan with only numbers-----TC_RCC_18");
		extent.extentLoggerPass("To validate the pan with only numbers", "Validate With Pan with Only Number formate");
		click(PAN_DetailsPage.panCardNumberField, "Field");
		clearField(PAN_DetailsPage.panCardNumberField, "Pan Card Field");
		waitTime(3000);
		type(PAN_DetailsPage.panCardNumberField, prop.getproperty("onlyAlphabets"), "Only Alphabets Formate");
		click(PAN_DetailsPage.btnProceed, "Procced");
		softAssertion.assertEquals("Please enter a valid pan number", getText(PAN_DetailsPage.errorMessage));
		logger.info("To validate the pan with only alphabets----------TC_RCC_19");
		extent.extentLoggerPass("To validate the pan with only alphabets","Validate With Pan with Only alphabets formate");
		click(PAN_DetailsPage.panCardNumberField, "Field");
		clearField(PAN_DetailsPage.panCardNumberField, "Pan Card Field");
		waitTime(3000);
		type(PAN_DetailsPage.panCardNumberField, prop.getproperty("onlySpecialChar"), "Only Special Character");
		click(PAN_DetailsPage.btnProceed, "Procced");
		softAssertion.assertEquals("Please enter a valid pan number", getText(PAN_DetailsPage.errorMessage));
		logger.info("To validate the pan with only special Characters------TC_RCC_20");
		extent.extentLoggerPass("To validate the pan with only special Characters","Validate With Pan with Only special Characters formate");
		click(PAN_DetailsPage.panCardNumberField, "Field");
		clearField(PAN_DetailsPage.panCardNumberField, "Pan Card Field");
		waitTime(3000);
		type(PAN_DetailsPage.panCardNumberField, prop.getproperty("mixCharacter"), "Mix Character");
		click(PAN_DetailsPage.btnProceed, "Procced");
		softAssertion.assertEquals("Please enter a valid pan number", getText(PAN_DetailsPage.errorMessage));
		logger.info("To validate the pan with combination of alphabets, numbers and special character----TC_RCC_21");
		extent.extentLoggerPass("To validate the pan with combination of alphabets, numbers and special character","Validate With Pan with combination of alphabets,number,special character formate");
		click(PAN_DetailsPage.panCardNumberField, "Field");
		clearField(PAN_DetailsPage.panCardNumberField, "Pan Card Field");
		waitTime(3000);
		type(PAN_DetailsPage.panCardNumberField, prop.getproperty("wrongFormat"), "Wrong Formate");
		click(PAN_DetailsPage.btnProceed, "Procced");
		softAssertion.assertEquals("Please enter a valid pan number", getText(PAN_DetailsPage.errorMessage));
		logger.info("To validate the pan with wrong format----TC_RCC_22");
		extent.extentLoggerPass("To validate the pan with wrong format", "Validate With Pan with wrong formate");
		click(PAN_DetailsPage.panCardNumberField, "Field");
		clearField(PAN_DetailsPage.panCardNumberField, "Pan Card Field");
		waitTime(3000);
		type(PAN_DetailsPage.panCardNumberField, otherspanCard, "With Other Pan Card Details");
		click(PAN_DetailsPage.btnProceed, "Procced");
		softAssertion.assertEquals("Please enter a valid pan number", getText(PAN_DetailsPage.errorMessage));
		logger.info("To validate the pan which is already mapped with other Ring User-----TC_RCC_23");
		extent.extentLoggerPass("To validate the pan which is already mapped with other Ring User","Validate with already mapped with other ring user");

		click(PAN_DetailsPage.panCardNumberField, "Field");
		clearField(PAN_DetailsPage.panCardNumberField, "");
		waitTime(3000);
		type(PAN_DetailsPage.panCardNumberField, prop.getproperty("notExistPanCard"), "Not Exist Pan Card");
		click(PAN_DetailsPage.btnProceed, "Procced");
		softAssertion.assertEquals("Please enter a valid pan number", getText(PAN_DetailsPage.errorMessage));
		logger.info("To validate the pan which is not mapped with other Ring user and also not exist---TC_RCC_24");
		extent.extentLoggerPass("To validate the pan which is not mapped with other Ring user and also not exist","Validate with not exist user");

		click(PAN_DetailsPage.panCardNumberField, "Field");
		clearField(PAN_DetailsPage.panCardNumberField, "Pan Card Field");
		waitTime(3000);
		type(PAN_DetailsPage.panCardNumberField, panCard, "Valid Pan Card");
		click(PAN_DetailsPage.btnProceed, "Proced");
		waitTime(8000);
		if (verifyElementPresent2(AddAddressPage.objAddCurrentAddressHeader, "address Page")) {

			softAssertion.assertEquals("New Current Address", getText(AddAddressPage.objAddCurrentAddressHeader));
			logger.info("To Verify With Valid Pan Card Details---TC_RCC_17");
			extent.extentLoggerPass("To Verify With Valid Pan Card Details",
					"To Verify With Valid Pan Card Details---TC_RCC_17");
		} else {
			softAssertion.assertEquals("Accept Offer", getText(RingLoginPage.objAcceptOfferBtn));
			logger.info("To Verify With Valid Pan Card Details---TC_RCC_17");
			extent.extentLoggerPass("To Verify With Valid Pan Card Details",
					"To Verify With Valid Pan Card Details---TC_RCC_17");
		}
	}

	public void dataPointApi() throws Exception {
		extent.HeaderChildNode("DB");
		ValidatableResponse response = Utilities.customDataPoints_policy("8000", "8000", "IDEP741689288872YTRK");
		String message = response.extract().jsonPath().getString("message");
		System.out.println("Data Point-------------" + message);
		String responseCode = response.extract().jsonPath().getString("response_code");
		System.out.println("Data Point-------------" + responseCode);
		String success = response.extract().jsonPath().getString("success");
		System.out.println("Data Point-------------" + success);
	}

	public void onBoardingHomePage() throws Exception {
		extent.HeaderChildNode("On Boarding");
		mockUserAPI();

		loginOnboarding("1");
		mobileNoValidation1(mobileNumber);
		enterOtp(prop.getproperty("OTP"));
		user_reference = getRefrenceNumber(mobileNumber);
		if (verifyElementPresent2(RingLoginPage.objReadAcceptBtn, "Permission Page")) {
			readAndAccept();
		}
		waitTime(10000);

		instaUserDetails("sunil", "chatla", mothersName, email, gender);

		waitTime(20000);

		if (verifyElementPresent2(AddAddressPage.objAddCurrentAddressHeader, "address Page")) {
			addAddress("86", "44, Borivali", "Vasa Street", "Das Gupta Street", "400080");
			click(AddAddressPage.objSubmitBtn, "Submit");
		}
		newAdminPanel_appScore("0.107666832000000");
		waitTime(20000);
		click(RingLoginPage.objIAcceptCheckBox, "Term and Condition Check Box");
		click(RingLoginPage.objAcceptOfferBtn, "Accept Button");

		instaLoanSetPin(prop.getproperty("InstaLoanMPIN"), prop.getproperty("InstaLoanMPIN"));
		waitTime(13000);
		if (verifyElementPresent2(HomePage.objAdCloseBtn, "Ad Banner")) {
			explicitWaitVisibilityNonDefault(HomePage.objAdCloseBtn, 20);
			Aclick(HomePage.objAdCloseBtn, "Ad Cross Button");
		}
	}
	public void onBoardingHomePage1(String number) throws Exception {
		extent.HeaderChildNode("On Boarding");
		
		loginOnboarding("1");
		mobileNoValidation1(number);
		enterOtp(prop.getproperty("OTP"));
		user_reference = getRefrenceNumber(mobileNumber);
		if (verifyElementPresent2(RingLoginPage.objReadAcceptBtn, "Permission Page")) {
			readAndAccept();
		}
		waitTime(10000);

		instaUserDetails("sunil", "chatla", mothersName, email, gender);

		waitTime(20000);

		if (verifyElementPresent2(AddAddressPage.objAddCurrentAddressHeader, "address Page")) {
			addAddress("86", "44, Borivali", "Vasa Street", "Das Gupta Street", "400080");
			click(AddAddressPage.objSubmitBtn, "Submit");
		}
		newAdminPanel_appScore("0.107666832000000");
		waitTime(20000);
		click(RingLoginPage.objIAcceptCheckBox, "Term and Condition Check Box");
		click(RingLoginPage.objAcceptOfferBtn, "Accept Button");

		instaLoanSetPin(prop.getproperty("InstaLoanMPIN"), prop.getproperty("InstaLoanMPIN"));
		waitTime(13000);
		if (verifyElementPresent2(HomePage.objAdCloseBtn, "Ad Banner")) {
			explicitWaitVisibilityNonDefault(HomePage.objAdCloseBtn, 20);
			click(HomePage.objAdCloseBtn, "Ad Cross Button");
		}
	}

	public void coble_Count() throws Exception {
		extent.HeaderChildNode("Cable_Count");
		onBoardingHomePage();
		waitTime(10000);
		explicitWaitVisibilityNonDefault(HomePage.moreTab, 20);
		click(HomePage.moreTab, "More Tab");
		explicitWaitVisibilityNonDefault(HomePage.tabBankTransfer, 20);
		click(HomePage.tabBankTransfer, "Bank Transfer Button");
		waitTime(5000);

		addbankAccount("5", "Sunil Chatla");
		ValidatableResponse response = Utilities.customDataPoints_policy("8000", "8005", user_reference);
		type(PaymentPage.enterAmount, "50", "Entered Amount");
		click(PaymentPage.objPayNowBtn, "Pay Now");
		explicitWaitVisibilityNonDefault(PaymentPage.objConfirmPayment, 20);
		click(PaymentPage.objEditTransactionPin, "Transaction Pin Box");
		type(PaymentPage.objEditTransactionPin, prop.getproperty("InstaLoanMPIN"), "Ented MPin");
		waitTime(2000);
		click(PaymentPage.arrowBtn, "Arrow Button");
		waitTime(8000);
		if (verifyElementPresent2(HomePage.okaygotit, "Okay Got It")) {

			click(HomePage.okaygotit, "Ok Got It");
		} else {
			click(PaymentPage.objGoToHomePage, "Go to Homepage");

		}

		waitTime(30000);
		if (verifyElementPresent2(HomePage.txtAreYouEnjoyingApp, "Are You Enjoying The App")) {

			explicitWaitVisibilityNonDefault(HomePage.txtAreYouEnjoyingApp, 20);
			click(HomePage.noBtn, "No Button");
		}

		click(HomePage.selectRecentTransaction, "Select Recent Transaction");
		explicitWaitVisibilityNonDefault(HomePage.txtTransactionDetails, 20);
		String transactionNumebr = getText(HomePage.getTransactionNumber);
		System.out.println(transactionNumebr);
		Back(2);
		String result = executeQuery(
				"SELECT  * FROM `bnpl_transactions` WHERE bnpl_txn_reference_number=\"" + transactionNumebr + "\";",
				"rejection_reason");

		System.out.println("result=============" + result);
		logger.info("Reason For Rejection----" + result);
		softAssertion.assertEquals("Cabal count exceeded", result);
		extent.extentLoggerPass("Verify Cable Count Result Through Bank Transfer",
				"To verify user doing transaction through Bank Transfer when its linked with >2 cabal count ------TC_Ring_Customer_Seg_123");

		waitTime(10000);

		explicitWaitVisibilityNonDefault(HomePage.moreTab, 20);
		Aclick(HomePage.objScanAndPay, "Scan Button");
		String projectPath = System.getProperty("user.dir");

		scannQRSwitch();

		waitTime(30000);
		ValidatableResponse response1 = Utilities.customDataPoints_policy("8000", "8005", user_reference);
		explicitWaitVisibilityNonDefault(PaymentPage.objAmountTextField, 30);
		if (verifyElementPresent(PaymentPage.objAmountTextField, "Amount Text Field")) {
			Aclick(PaymentPage.objAmountTextField, "Amount Field");
			type(PaymentPage.objAmountTextField, "80", "Amount Field");
		}
		Aclick(PaymentPage.objPayNowBtn, "Pay Now Button");
		waitTime(20000);
		if (verifyElementPresent(PaymentPage.objConfirmPayment, getTextVal(PaymentPage.objConfirmPayment, "Text"))) {
			click(PaymentPage.objEditTransactionPin, "Edit Pin Field");
			type(PaymentPage.objEditTransactionPin, "1111", "Edit Pin Field");
			click(PaymentPage.objContinue, "Continue Button");
		}

		waitTime(8000);
		if (verifyElementPresent2(HomePage.okaygotit, "Okay Got It")) {

			click(HomePage.okaygotit, "Ok Got It");
		} else {
			click(PaymentPage.objGoToHomePage, "Go to Homepage");

		}

		waitTime(10000);
		if (verifyElementPresent2(HomePage.txtAreYouEnjoyingApp, "Are You Enjoying The App")) {

			explicitWaitVisibilityNonDefault(HomePage.txtAreYouEnjoyingApp, 20);
			click(HomePage.noBtn, "No Button");
		}

		click(HomePage.selectRecentTransaction, "Select Recent Transaction");
		explicitWaitVisibilityNonDefault(HomePage.txtTransactionDetails, 20);
		String transactionNumebrbarcode = getText(HomePage.getTransactionNumberqrscan);
		System.out.println(transactionNumebrbarcode);
		Back(2);
		String resultqrscan = executeQuery("SELECT  * FROM `bnpl_transactions` WHERE bnpl_txn_reference_number=\""+ transactionNumebrbarcode + "\";", "rejection_reason");

		System.out.println("result=============" + resultqrscan);
		logger.info("Reason For Rejection----" + resultqrscan);
		softAssertion.assertEquals("Cabal count exceeded", resultqrscan);
		extent.extentLoggerPass("Verify Cable Count Result Through Scan and Pay","To verify user doing transaction through Scan and Pay when its linked with >2 cabal count ------TC_Ring_Customer_Seg_122");

		explicitWaitVisibilityNonDefault(HomePage.moreTab, 20);
		click(HomePage.moreTab, "More Tab");
		explicitWaitVisibilityNonDefault(HomePage.tabBankTransfer, 20);
		click(HomePage.tabBankTransfer, "Bank Transfer Button");
		waitTime(5000);
		click(PaymentPage.selectExistingAcc, "Select Existing Account");
		ValidatableResponse response2 = Utilities.customDataPoints_policy("8000", "8000", user_reference);
		click(PaymentPage.enterAmount, "Entered Amount");
		type(PaymentPage.enterAmount, "30", "Entered Amount");
		// type(PaymentPage.enterAmount, "30", "Entered Amount");
		click(PaymentPage.objPayNowBtn, "Pay Now");
		explicitWaitVisibilityNonDefault(PaymentPage.objConfirmPayment, 20);
		click(PaymentPage.objEditTransactionPin, "Transaction Pin Box");
		type(PaymentPage.objEditTransactionPin, prop.getproperty("InstaLoanMPIN"), "Ented MPin");
		waitTime(2000);
		click(PaymentPage.arrowBtn, "Arrow Button");
		waitTime(8000);
		if (verifyElementPresent2(HomePage.okaygotit, "Okay Got It")) {

			click(HomePage.okaygotit, "Ok Got It");
		} else {
			click(PaymentPage.objGoToHomePage, "Go to Homepage");

		}
		waitTime(30000);
		if (verifyElementPresent2(HomePage.txtAreYouEnjoyingApp, "Are You Enjoying The App")) {

			explicitWaitVisibilityNonDefault(HomePage.txtAreYouEnjoyingApp, 20);
			click(HomePage.noBtn, "No Button");
		}

		click(HomePage.selectRecentTransaction, "Select Recent Transaction");
		explicitWaitVisibilityNonDefault(HomePage.txtTransactionDetails, 20);
		String transactionNumebr1 = getText(HomePage.getTransactionNumber);
		System.out.println(transactionNumebr1);
		Back(2);
		String result1 = executeQuery("SELECT  * FROM `bnpl_transactions` WHERE bnpl_txn_reference_number=\"" + transactionNumebr1 + "\";","rejection_reason");

		System.out.println("result=============" + result1);
		logger.info("Reason For Rejection----" + result1);
		softAssertion.assertEquals("null", result1);
		extent.extentLoggerPass("Verify Cable Count","To verify user doing Scan n Pay and Bank transfer transaction when user whitelisted for cabal check ------TC_Ring_Customer_Seg_124");
		waitTime(10000);

		explicitWaitVisibilityNonDefault(HomePage.moreTab, 20);
		click(HomePage.objScanAndPay, "Scan Button");
		String projectPath1 = System.getProperty("user.dir");

		scannQRSwitch();

		waitTime(30000);
		ValidatableResponse response3 = Utilities.customDataPoints_policy("8000", "8000", user_reference);
		explicitWaitVisibilityNonDefault(PaymentPage.objAmountTextField, 30);
		if (verifyElementPresent(PaymentPage.objAmountTextField, "Amount Text Field")) {
			click(PaymentPage.objAmountTextField, "Amount Field");
			type(PaymentPage.objAmountTextField, "60", "Amount Field");
		}
		Aclick(PaymentPage.objPayNowBtn, "Pay Now Button");
		waitTime(20000);
		if (verifyElementPresent(PaymentPage.objConfirmPayment, getTextVal(PaymentPage.objConfirmPayment, "Text"))) {
			click(PaymentPage.objEditTransactionPin, "Edit Pin Field");
			type(PaymentPage.objEditTransactionPin, "1111", "Edit Pin Field");
			click(PaymentPage.objContinue, "Continue Button");
		}

		waitTime(8000);
		if (verifyElementPresent2(HomePage.okaygotit, "Okay Got It")) {
			click(HomePage.okaygotit, "Ok Got It");
		} else {
			click(PaymentPage.objGoToHomePage, "Go to Homepage");

		}
		waitTime(30000);
		if (verifyElementPresent2(HomePage.txtAreYouEnjoyingApp, "Are You Enjoying The App")) {
			explicitWaitVisibilityNonDefault(HomePage.txtAreYouEnjoyingApp, 20);
			click(HomePage.noBtn, "No Button");
		}

		click(HomePage.selectRecentTransaction, "Select Recent Transaction");
		explicitWaitVisibilityNonDefault(HomePage.txtTransactionDetails, 20);
		String transactionNumebrbarcode1 = getText(HomePage.getTransactionNumberqrscan);
		System.out.println(transactionNumebrbarcode1);
		Back(2);
		String resultqrscan1 = executeQuery("SELECT  * FROM `bnpl_transactions` WHERE bnpl_txn_reference_number=\""+ transactionNumebrbarcode1 + "\";", "rejection_reason");

		System.out.println("result=============" + resultqrscan1);
		logger.info("Reason For Rejection----" + resultqrscan1);
		softAssertion.assertEquals("null", resultqrscan1);
		extent.extentLoggerPass("Verify Cable Count","To verify user doing Scan n Pay and Bank transfer transaction when user whitelisted for cabal check ------TC_Ring_Customer_Seg_124");
		ringPayLogout();

	}

// ------------------------------------Pulkit Code Start------------------------------------//

//==========================================================WhitleList Logic Start==================================================================//

	public void instaLoanWhitelistLogic(String OTP, String RingAdminEmail, String RingAdminPassword,String RingAdminOTP, String InstaLoanMPIN, String ReEnterMPIN, String firstName, String lastName,String mothersName, String gender, String delinquentNo, String terminatedNo) throws Exception {
		extent.HeaderChildNode("WhiteList Logic");
		instaLoanBanner(OTP, RingAdminEmail, RingAdminPassword, RingAdminOTP, InstaLoanMPIN, ReEnterMPIN);
		outstandingAMTGreaterThanZero(OTP, RingAdminEmail, RingAdminPassword, RingAdminOTP, InstaLoanMPIN, ReEnterMPIN);
		bannerNotDisplayedWhenUserIsNotWhitelisted(OTP, firstName, lastName, mothersName, gender, InstaLoanMPIN,ReEnterMPIN);
		offerNotAvailedIfUserBecomesDelinquent(delinquentNo, OTP);
		userBecomesBlocked(OTP, RingAdminEmail, RingAdminPassword, RingAdminOTP, InstaLoanMPIN, ReEnterMPIN);
		userBecomesTerminated(terminatedNo, OTP);
		amountSetNull(OTP, RingAdminEmail, RingAdminPassword, RingAdminOTP, InstaLoanMPIN, ReEnterMPIN);
		amountSetAsZero(OTP, RingAdminEmail, RingAdminPassword, RingAdminOTP, InstaLoanMPIN, ReEnterMPIN);
		offerIDNULL(OTP, RingAdminEmail, RingAdminPassword, RingAdminOTP, InstaLoanMPIN, ReEnterMPIN);
		offerIDZero(OTP, RingAdminEmail, RingAdminPassword, RingAdminOTP, InstaLoanMPIN, ReEnterMPIN);
		User_reference_numberNULL(OTP, RingAdminEmail, RingAdminPassword, RingAdminOTP, InstaLoanMPIN, ReEnterMPIN);
		userRemovedFromWhiteListTable(OTP, RingAdminEmail, RingAdminPassword, RingAdminOTP, InstaLoanMPIN, ReEnterMPIN);
	}

	public void instaLoanBanner(String OTP, String RingAdminEmail, String RingAdminPassword, String RingAdminOTP,String InstaLoanMPIN, String ReEnterMPIN) throws Exception {
		extent.HeaderChildNode("InstaLoan BannerWhiteList Logic");
		mockUserAPI();
		getDriver().resetApp();
		//loginOnboarding("1");
		enablePermissions();
		verifyElementPresentAndClick(RingLoginPage.objSignUpLoginBt, getText(RingLoginPage.objSignUpLoginBt));
		loginMobile();
		mobileNoValidation1(mobileNumber);
		enterOtp(OTP);
		readAndAccept();
		
		instaUserDetails(firstName, lastName, mothersName, email, gender);
		
		instalonAPIKycVerification(mobileNumber);
		instaNewCommunicationAddress();
		
		instaPanCardDetails(RingAdminEmail, RingAdminPassword, RingAdminOTP, "OPTIONAL", "13000", "2311", "2319");
		panCardDetails();
		
		instaLoancongratsScreen("Congrats");
		instaLoanSetPin(InstaLoanMPIN, ReEnterMPIN);
		
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		instaLoanCoachPopup();
		Swipe("up", 1);
		instaLoanHomePageBanner();
		instaLoanOutstandingBalance();
		basicDetails();
		click(InstaLoanPage.objAcceptCheckBox, getText(InstaLoanPage.objAcceptCheckBox));
		verifyElementPresentAndClick(InstaLoanPage.objAcceptOffer, getText(InstaLoanPage.objAcceptOffer));
		verifyElementPresentAndClick(InstaLoanPage.objConfirmOfferBtn, getText(InstaLoanPage.objConfirmOfferBtn));
		
		verifyElementPresentAndClick(InstaLoanPage.objHomeBtn, getText(InstaLoanPage.objHomeBtn) + " Button");
		
		if (!verifyElementPresent2(InstaLoanOptionalJourney.objPayNowBtnAtHomePage, "Pay Now Button")) {
			waitTime(12000);
			verifyElementPresentAndClick(InstaLoanOptionalJourney.objUPITab, "UPI Tab");
			waitTime(9000);
			Back(1);
		}
		Swipe("down", 1);
		payNowInstaLoan();

	}

//=====================================To verify if whitelisted user clicks on apply now button when Outstanding amount > 0=======================================//
	public void outstandingAMTGreaterThanZero(String OTP, String RingAdminEmail, String RingAdminPassword,String RingAdminOTP, String InstaLoanMPIN, String ReEnterMPIN) throws Exception {
		extent.HeaderChildNode("whitelisted user clicks on apply now button when Outstanding amount > 0");
		mockUserAPI();
		getDriver().resetApp();
		if (verifyElementPresent(RingLoginPage.objCamPermPopUp, "Enable permissions button")) {
			enablePermissions();
		}
		scannQRSwitch();

		click(RingLoginPage.objMakePaymentLetsRingItBtn, getText(RingLoginPage.objMakePaymentLetsRingItBtn));
		click(RingPayMerchantFlowPage.objAmountTextField, "Enter Amount Field");
		type1(RingPayMerchantFlowPage.objAmountTextField, "1", "Amount Field");
		verifyElementPresentAndClick(RingLoginPage.objMakePaymentPageProceedBtn,getText(RingLoginPage.objMakePaymentPageProceedBtn));
		verifyIsElementDisplayed(RingPayMerchantFlowPage.objLoginPageHeader,getTextVal(RingPayMerchantFlowPage.objLoginPageHeader, "Page"));
		softAssertion.assertEquals(getText(RingPayMerchantFlowPage.objLoginPageHeader), "Sign up/Login");
		logger.info("User redirected to Signup/Login Screen");
		extent.extentLogger("login", "User redirected to Signup/Login Screen");
		loginMobile();
		mobileNoValidation1(mobileNumber);
		enterOtp(OTP);
		readAndAccept();
		waitTime(5000);
		instaUserDetails(firstName, lastName, mothersName, email, gender);
		instalonAPIKycVerification(mobileNumber);
		instaNewCommunicationAddress();
		waitTime(5000);
		instaPanCardDetails(RingAdminEmail, RingAdminPassword, RingAdminOTP, "OPTIONAL", "13000", "2311", "2319");
		panCardDetails();
		waitTime(5000);
		instaLoancongratsScreen("Woohoo");
		instaLoanSetPin(InstaLoanMPIN, ReEnterMPIN);
		waitTime(5000);
		click(BankTransferModule.objHomePageBtn, "Go to Homepage");
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		verifyElementPresentAndClick(InstaLoanPage.objNoBtn, "No Button");
		instaLoanCoachPopup();
		Swipe("up", 1);
		instaLoanHomePageBanner();
		instaLoanOutstandingBalance();
	}

//========================================Insta banner Not displayed when user is not whitelisted========================================================================
	public void bannerNotDisplayedWhenUserIsNotWhitelisted(String OTP, String firstName, String lastName,String mothersName, String gender, String InstaLoanMPIN, String ReEnterMPIN) throws Exception {
		extent.HeaderChildNode("Insta banner Not displayed when user is not whitelisted");
		getDriver().resetApp();
		mockUserAPI();
		loginOnboarding("1");
		mobileNoValidation1("9" + RandomIntegerGenerator(9));
		waitTime(5000);
		enterOtp(OTP);
		readAndAccept();
		waitTime(40000);
		instaUserDetails(firstName, lastName, mothersName, RandomStringGenerator(4) + "@example.com", gender);
		instaNewCommunicationAddress();
		waitTime(10000);
		instaLoancongratsScreen("Congrats");
		instaLoanSetPin(InstaLoanMPIN, ReEnterMPIN);
		waitTime(5000);
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		instaLoanCoachPopup();
		instaLoanHomePageBanner();
	}

//============================To verify Insta loan offer is not availed if user becomes delinquent even if customer is present in whitelist==========================================
	public void offerNotAvailedIfUserBecomesDelinquent(String delinquentNo, String OTP) throws Exception {
		extent.HeaderChildNode("Insta loan offer is not availed if user becomes delinquent even if customer is present in whitelist");
		getDriver().resetApp();
		loginOnboarding("1");
		mobileNoValidation1(delinquentNo);
		waitTime(5000);
		enterOtp(OTP);
		readAndAccept();

		if (verifyElementPresent2(UserRegistrationPage.objNoBtn, "No Button")) {
			verifyElementPresentAndClick(UserRegistrationPage.objNoBtn, getText(UserRegistrationPage.objNoBtn));
		}
		waitTime(20000);

		setPlatform("Web");
		System.out.println("platform changed to web");
		String pf = getPlatform();
		System.out.println(pf);
		waitTime(5000);
		new RingPayBusinessLogic("ring");
		waitTime(10000);

		getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+delinquentNo+"&");
		type(InstaLoanPage.objEmail,"shakir.muchumarri@collabera.com","Email text field");
		click(InstaLoanPage.objContinue,"Continue Button");
		waitTime(10000);
		explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
		String userRefNo = getText(InstaLoanPage.objUserRefNo);
		BrowsertearDown();

		setPlatform("Android");
		initDriver();
		waitTime(5000);

		String instaLoanWhitelistNo = executeQuery3("SELECT * FROM db_txn_service.instaloan_whitelisted_users Where user_reference_number = '" + userRefNo+ "';",2);

		if (instaLoanWhitelistNo.equals(userRefNo)) {
			softAssertion.assertEquals(instaLoanWhitelistNo, userRefNo);
			Swipe("down", 1);
			boolean instaLoanBanner = verifyElementPresent2(InstaLoanPage.objApplyBtn, "InstaLoan Banner");
			logger.info("InstaLoan Banner present : " + instaLoanBanner);
			extent.extentLogger("InstaLoan Banner", "InstaLoan Banner present : " + instaLoanBanner);

			if (instaLoanBanner == true) {
				logger.info("InstaLoan Banner is present even if user is deliquent");
				extent.extentLoggerFail("InstaLoan Banner", "InstaLoan Banner is present even if user is deliquent");
			} else {
				logger.info("TC_Insta_loan_10 - To verify Insta loan offer is not availed if user becomes delinquent even if customer is present in whitelist");
				extent.extentLoggerPass("TC_Insta_loan_10","TC_Insta_loan_10 - To verify Insta loan offer is not availed if user becomes delinquent even if customer is present in whitelist");
			}
		}
	}

//============================To verify Insta loan offer is not avail if user becomes blocked even if customer is present in whitelist===============================================
	public void userBecomesBlocked(String OTP, String RingAdminEmail, String RingAdminPassword, String RingAdminOTP,String InstaLoanMPIN, String ReEnterMPIN) throws Exception {
		extent.HeaderChildNode("Insta loan offer is not avail if user becomes blocked even if customer is present in whitelist");
		getDriver().resetApp();
		mockUserAPI();
		//instalonAPIKycVerification();
		loginOnboarding("1");
		mobileNoValidation1(mobileNumber);
		enterOtp(OTP);
		readAndAccept();
		waitTime(30000);
		instaUserDetails(firstName, lastName, mothersName, email, gender);
		instalonAPIKycVerification(mobileNumber);
		instaNewCommunicationAddress();
		waitTime(10000);
		instaPanCardDetails(RingAdminEmail, RingAdminPassword, RingAdminOTP, "OPTIONAL", "13000", "2311", "2319");
		panCardDetails();
		waitTime(5000);
		instaLoancongratsScreen("Congrats");
		instaLoanSetPin(InstaLoanMPIN, ReEnterMPIN);
		waitTime(5000);
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		instaLoanCoachPopup();
		Swipe("up", 1);
		instaLoanHomePageBanner();
		blockUser();
	}

//=========================================To verify Insta loan offer is not avail if user becomes terminated even if customer is present in whitelist=========================
	public void userBecomesTerminated(String terminatedNo, String OTP) throws Exception {
		extent.HeaderChildNode("Insta loan offer is not avail if user becomes Terminated even if customer is present in whitelist");
		getDriver().resetApp();
		loginOnboarding("1");
		mobileNoValidation1(terminatedNo);
		waitTime(5000);
		enterOtp(OTP);
		readAndAccept();
		waitTime(20000);

		setPlatform("Web");
		System.out.println("platform changed to web");
		waitTime(5000);
		new RingPayBusinessLogic("ring");
		waitTime(10000);

		getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+terminatedNo+"&");
		type(InstaLoanPage.objEmail,"shakir.muchumarri@collabera.com","Email text field");
		click(InstaLoanPage.objContinue,"Continue Button");
		waitTime(10000);
		explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
		String userRefNo = getText(InstaLoanPage.objUserRefNo);
		BrowsertearDown();

		setPlatform("Android");
		initDriver();
		waitTime(5000);

		String instaLoanWhitelistNo1 = executeQuery3("SELECT * FROM db_txn_service.instaloan_whitelisted_users Where user_reference_number = '" + userRefNo+ "';",2);
		System.out.println("User is present in WhiteListTable : " + instaLoanWhitelistNo1);
		logger.info("User is present in WhiteListTable : " + instaLoanWhitelistNo1);
		extent.extentLogger("WhiteList Table", "User is present in WhiteListTable : " + instaLoanWhitelistNo1);
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		if (instaLoanWhitelistNo1.equals(userRefNo)) {
			softAssertion.assertEquals(instaLoanWhitelistNo1, userRefNo);

			boolean instaLoanBanner = verifyElementPresent2(InstaLoanPage.objApplyBtn, "InstaLoan Banner");
			logger.info("InstaLoan Banner present : " + instaLoanBanner);
			extent.extentLogger("InstaLoan Banner", "InstaLoan Banner present : " + instaLoanBanner);

			if (instaLoanBanner == false) {
				logger.info("TC_Insta_loan_12 - To verify Insta loan offer is not avail if user becomes terminated even if customer is present in whitelist");
				extent.extentLoggerFail("InstaLoan Banner","TC_Insta_loan_12 - To verify Insta loan offer is not avail if user becomes terminated even if customer is present in whitelist");
			}
		}
	}

//=============================================To verify if approved amount is set as NULL in whitelisting table=============================================================
	public void amountSetNull(String OTP, String RingAdminEmail, String RingAdminPassword, String RingAdminOTP,String InstaLoanMPIN, String ReEnterMPIN) throws Exception {
		extent.HeaderChildNode("Approved amount is set as NULL in whitelisting table");
		getDriver().resetApp();
		mockUserAPI();
		//instalonAPIKycVerification();
		loginOnboarding("1");
		mobileNoValidation1(mobileNumber);
		enterOtp(OTP);
		readAndAccept();
		waitTime(30000);
		instaUserDetails(firstName, lastName, mothersName, email, gender);
		instalonAPIKycVerification(mobileNumber);
		instaNewCommunicationAddress();
		waitTime(5000);
		instaApprovedAmountNull(RingAdminEmail, RingAdminPassword, RingAdminOTP, "OPTIONAL", "NULL", "2311", "2319");
		panCardDetails();
		waitTime(5000);
		instaLoancongratsScreen("Congrats");
		instaLoanSetPin(InstaLoanMPIN, ReEnterMPIN);
		waitTime(5000);
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		logger.info("TC_Insta_loan_13 - To verify if approved amount is set as NULL in whitelisting table");
		extent.extentLoggerPass("TC_Insta_loan_13","TC_Insta_loan_04 - To verify if approved amount is set as NULL in whitelisting table");
	}

//====================================To verify if approved amount is set as 0 in whitelisting table===================================================================
	public void amountSetAsZero(String OTP, String RingAdminEmail, String RingAdminPassword, String RingAdminOTP,String InstaLoanMPIN, String ReEnterMPIN) throws Exception {
		extent.HeaderChildNode("if approved amount is set as 0 in whitelisting table");
		getDriver().resetApp();
		//instalonAPIKycVerification();
		mockUserAPI();
		loginOnboarding("1");
		mobileNoValidation1(mobileNumber);
		enterOtp(OTP);
		readAndAccept();
		waitTime(30000);
		instaUserDetails(firstName, lastName, mothersName, email, gender);
		instalonAPIKycVerification(mobileNumber);
		instaNewCommunicationAddress();
		waitTime(5000);
		instaPanCardDetails(RingAdminEmail, RingAdminPassword, RingAdminOTP, "OPTIONAL", "0", "2311", "2319");
		panCardDetails();
		waitTime(5000);
		instaLoancongratsScreen("Congrats");
		instaLoanSetPin(InstaLoanMPIN, ReEnterMPIN);
		waitTime(5000);
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		instaLoanCoachPopup();
		Swipe("up", 1);
		instaLoanHomePageBanner();
		instaLoanOutstandingBalance();
		waitTime(10000);
		basicDetails();

		boolean msg = verifyElementPresent2(InstaLoanPage.objDisbursementSuccessfulMsg,"Insta Loan Disbursement Successful message");
		if (msg == true) {
			logger.info(getText(InstaLoanPage.objDisbursementSuccessfulMsg) + " is displayed");
			extent.extentLogger("DisbursementMsg",getText(InstaLoanPage.objDisbursementSuccessfulMsg) + " is displayed");
		} else {
			logger.info("TC_Insta_loan_14 - To verify if approved amount is set as 0 in whitelisting table");
			extent.extentLoggerPass("TC_Insta_loan_14","TC_Insta_loan_14 - To verify if approved amount is set as 0 in whitelisting table");
			verifyElementPresentAndClick(InstaLoanPage.objOKGotItBtn, getText(InstaLoanPage.objOKGotItBtn));
		}
	}

//===================================To verify if Offer ID is set as NULL in whitelisting table=================================================================
	public void offerIDNULL(String OTP, String RingAdminEmail, String RingAdminPassword, String RingAdminOTP,String InstaLoanMPIN, String ReEnterMPIN) throws Exception {
		extent.HeaderChildNode("Offer ID is set as NULL in whitelisting table");
		getDriver().resetApp();
		//instalonAPIKycVerification();
		mockUserAPI();
		loginOnboarding("1");
		mobileNoValidation1(mobileNumber);
		enterOtp(OTP);
		readAndAccept();
		waitTime(30000);
		instaUserDetails(firstName, lastName, mothersName, email, gender);
		instalonAPIKycVerification(mobileNumber);
		instaNewCommunicationAddress();
		waitTime(5000);
		instaApprovedAmountNull(RingAdminEmail, RingAdminPassword, RingAdminOTP, "OPTIONAL", "13000", "NULL", "2319");
		panCardDetails();
		waitTime(5000);
		instaLoancongratsScreen("Congrats");
		instaLoanSetPin(InstaLoanMPIN, ReEnterMPIN);
		waitTime(5000);
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		logger.info("TC_Insta_loan_15 - To verify if Offer ID is set as NULL in whitelisting table");
		extent.extentLoggerPass("TC_Insta_loan_15","TC_Insta_loan_04 - To verify if Offer ID is set as NULL in whitelisting table");
	}

//=======================================To verify if Offer ID is set as 0 in whitelisting table================================================================================
	public void offerIDZero(String OTP, String RingAdminEmail, String RingAdminPassword, String RingAdminOTP,String InstaLoanMPIN, String ReEnterMPIN) throws Exception {
		extent.HeaderChildNode("Offer ID is set as NULL in whitelisting table");
		getDriver().resetApp();
		//instalonAPIKycVerification();
		mockUserAPI();
		loginOnboarding("1");
		mobileNoValidation1(mobileNumber);
		enterOtp(OTP);
		readAndAccept();
		waitTime(30000);
		instaUserDetails(firstName, lastName, mothersName, email, gender);
		instalonAPIKycVerification(mobileNumber);
		instaNewCommunicationAddress();
		waitTime(5000);
		instaPanCardDetails(RingAdminEmail, RingAdminPassword, RingAdminOTP, "OPTIONAL", "13000", "0", "2135");
		panCardDetails();
		waitTime(5000);
		instaLoancongratsScreen("Congrats");
		instaLoanSetPin(InstaLoanMPIN, ReEnterMPIN);
		waitTime(5000);
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		instaLoanCoachPopup();
		Swipe("up", 1);
		instaLoanHomePageBanner();
		instaLoanOutstandingBalance();
		waitTime(10000);
		basicDetails();
		boolean msg1 = verifyElementPresent2(InstaLoanPage.objSorryPage,"Sorry! your application cannot be processed right now.");
		if (msg1 == true) {
			logger.info("TC_Insta_loan_16 - To verify if Offer ID is set as 0 in whitelisting table");
			extent.extentLoggerPass("TC_Insta_loan_16","TC_Insta_loan_04 - To verify if Offer ID is set as 0 in whitelisting table");
		}
	}

//=================================To verify if User_reference_number column is set as NULL in whitelisting table===========================================
	public void User_reference_numberNULL(String OTP, String RingAdminEmail, String RingAdminPassword,String RingAdminOTP, String InstaLoanMPIN, String ReEnterMPIN) throws Exception {
		extent.HeaderChildNode("User_reference_number column is set as NULL in whitelisting table");
		getDriver().resetApp();
		//instalonAPIKycVerification();
		mockUserAPI();
		loginOnboarding("1");
		mobileNoValidation1(mobileNumber);
		enterOtp(OTP);
		readAndAccept();
		waitTime(30000);
		instaUserDetails(firstName, lastName, mothersName, email, gender);
		instalonAPIKycVerification(mobileNumber);
		instaNewCommunicationAddress();
		waitTime(5000);
		instaReferenceNoNull(RingAdminEmail, RingAdminPassword, RingAdminOTP, "NULL", "OPTIONAL", "13000", "2311","2319");
		panCardDetails();
		waitTime(5000);
		instaLoancongratsScreen("Congrats");
		instaLoanSetPin(InstaLoanMPIN, ReEnterMPIN);
		waitTime(5000);
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");

		boolean instaBanner = verifyElementPresent2(InstaLoanPage.objApplyBtn, "Insta Banner");
		if (instaBanner == false) {
			logger.info("TC_Insta_loan_17 - To verify if User_reference_number column is set as NULL in whitelisting table");
			extent.extentLoggerPass("TC_Insta_loan_17","TC_Insta_loan_17 - To verify if User_reference_number column is set as NULL in whitelisting table");
		}
	}

//==============================To verify if User is removed from Whitelist Loan after taking InstaLoan=======================================================================
	public void userRemovedFromWhiteListTable(String OTP, String RingAdminEmail, String RingAdminPassword,String RingAdminOTP, String InstaLoanMPIN, String ReEnterMPIN) throws Exception {
		extent.HeaderChildNode("User is removed from Whitelist Loan after taking InstaLoan");
		//instalonAPIKycVerification();
		mockUserAPI();
		getDriver().resetApp();
		loginOnboarding("1");
		mobileNoValidation1(mobileNumber);
		enterOtp(OTP);
		readAndAccept();
		waitTime(30000);
		instaUserDetails(firstName, lastName, mothersName, email, gender);
		instalonAPIKycVerification(mobileNumber);
		instaNewCommunicationAddress();
		waitTime(5000);
		instaPanCardDetails(RingAdminEmail, RingAdminPassword, RingAdminOTP, "OPTIONAL", "13000", "2311", "2319");
		panCardDetails();
		waitTime(5000);
		instaLoancongratsScreen("Congrats");
		instaLoanSetPin(InstaLoanMPIN, ReEnterMPIN);
		waitTime(5000);
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		instaLoanCoachPopup();
		Swipe("up", 1);
		instaLoanHomePageBanner();
		instaLoanOutstandingBalance();
		waitTime(10000);
		basicDetails();
		click(InstaLoanPage.objAcceptCheckBox, getText(InstaLoanPage.objAcceptCheckBox));
		verifyElementPresentAndClick(InstaLoanPage.objAcceptOffer, getText(InstaLoanPage.objAcceptOffer));
		verifyElementPresentAndClick(InstaLoanPage.objConfirmOfferBtn, getText(InstaLoanPage.objConfirmOfferBtn));
		waitTime(9000);
		verifyElementPresentAndClick(InstaLoanPage.objHomeBtn, getText(InstaLoanPage.objHomeBtn) + " Button");
		waitTime(12000);
		deleteRowFromInstaLoanWhitelistTable("DELETE FROM db_txn_service.instaloan_whitelisted_users WHERE user_reference_number = '" + userReferenceNo+ "';");
		verifyElementPresentAndClick(InstaLoanPage.objViewDetails, getText(InstaLoanPage.objViewDetails) + " Button");
		click(InstaLoanPage.objPayFullAmountRadioBtn, "Pay Full Amount RadioButton");
		verifyElementPresentAndClick(InstaLoanPage.objPayNowBtn, getText(InstaLoanPage.objPayNowBtn) + " Button");
		explicitWaitVisibilityNonDefault(InstaLoanPage.objNetBankingDebitCard, 20);
		verifyElementPresentAndClick(InstaLoanPage.objNetBankingDebitCard,getText(InstaLoanPage.objNetBankingDebitCard) + " Chip");
		waitTime(20000);
		verifyElementPresentAndClick(InstaLoanPage.objCard, "Card chip");
		addNewCard();
		String refAfterRepayment = executeQuery2("SELECT * FROM db_txn_service.instaloan_whitelisted_users Where user_reference_number = '"+ userReferenceNo + "';");
		System.out.println(refAfterRepayment);

		boolean reApplyForInstaLoanBanner = verifyElementPresent2(InstaLoanPage.objReApplyForInstaLoan,getText(InstaLoanPage.objReApplyForInstaLoan));
		if (reApplyForInstaLoanBanner == false) {
			logger.info("TC_Insta_loan_18 - To verify if User is removed from Whitelist Loan after taking InstaLoan");
			extent.extentLoggerPass("TC_Insta_loan_18","TC_Insta_loan_18 - To verify if User is removed from Whitelist Loan after taking InstaLoan");
		}
	}

	public void instaLoanHomePageBanner() throws Exception {

		boolean instaBanner = verifyElementPresent2(InstaLoanPage.objinstaLoanBannerImage,"InstaLoan Banner is displayed");
		logger.info("InstaLoan Banner present is : " + instaBanner);
		extent.extentLogger("InstaLoan Banner", "InstaLoan Banner present is : " + instaBanner);

		if (instaBanner == true) {
			logger.info("TC_Insta_loan_01 - To verify if insta loan banner is visible when user is whitelisted");
			extent.extentLoggerPass("TC_Insta_loan_01","TC_Insta_loan_01 - To verify if insta loan banner is visible when user is whitelisted");
		} else {
			logger.info("TC_Insta_loan_02 - To verify if insta loan Banner is not visible when user is not whitelisted");
			extent.extentLoggerPass("TC_Insta_loan_02","TC_Insta_loan_02 -To verify if insta loan Banner is not visible when user is not whitelisted");
		}
	}

	public void instaLoanCoachPopup() throws Exception {
		boolean instaCoach = verifyElementPresent2(InstaLoanPage.objInstaLoanCoachEnjoyText,"InstaLoan Coach Popup is displayed");
		logger.info("InstaLoan Coach Popup present : " + instaCoach);
		extent.extentLogger("InstaLoan Coach Popup", "InstaLoan Coach Popup present : " + instaCoach);

		if (instaCoach == true) {
			logger.info("TC_Insta_loan_05 - To verify the Display Coach mark and popup banner for 1st time whitelisted user.");
			extent.extentLogger("TC_Insta_loan_05","TC_Insta_loan_05 -To verify the Display Coach mark and popup banner for 1st time whitelisted user");
			verifyElementPresentAndClick(InstaLoanPage.instaPopupBannerCrossBtn, "Cross Button");
		} else {
			logger.info("TC_Insta_loan_06 - To verify that Display Coach mark and popup banner should not be displayed if user is not whitelisted");
			extent.extentLoggerPass("C_Insta_loan_06","TC_Insta_loan_06 - To verify that Display Coach mark and popup banner should not be displayed if user is not whitelisted");
		}

		if (verifyElementPresent2(InstaLoanPage.objOkayGotIt, "Okay, Got It!")) {
			click(InstaLoanPage.objOkayGotIt, "Okay, Got It!");
		}
	}

	public void instaLoanOutstandingBalance() throws Exception {
		String outstandingAmt = getText(InstaLoanPage.objOutstatndingAmount);
		outstandingAmt = outstandingAmt.substring(1);
		double number = Double.parseDouble(outstandingAmt);
		if ((number == 0) || (number <= 1)) {
			logger.info("Outstanding Amount is : " + outstandingAmt);
			extent.extentLoggerPass("Outstanding Amount", "Outstanding Amount is : " + outstandingAmt);
		}
		if (number == 0) {
			click(InstaLoanPage.objApplyBtn, getText(InstaLoanPage.objApplyBtn) + " Button is displayed");
			logger.info("TC_Insta_loan_03 - To verify if whitelisted user clicks on apply now button when Outstanding amount = 0");
			extent.extentLoggerPass("TC_Insta_loan_03","TC_Insta_loan_03 -To verify if whitelisted user clicks on apply now button when Outstanding amount = 0");
		} else if ((number <= 1)) {
			click(InstaLoanPage.objApplyBtn, getText(InstaLoanPage.objApplyBtn) + " Button is displayed");
			verifyElementPresent(InstaLoanPage.objRepayDues,getText(InstaLoanPage.objRepayDues) + getText(InstaLoanPage.objRepayDuesDescription));
			click(InstaLoanPage.objRepayPopupCrossBtn, "Cross Button");
			logger.info("TC_Insta_loan_04 - To verify if whitelisted user clicks on apply now button when Outstanding amount > 0");
			extent.extentLoggerPass("TC_Insta_loan_04","TC_Insta_loan_04 - To verify if whitelisted user clicks on apply now button when Outstanding amount > 0");
		}
	}

	public void basicDetails() throws Exception {
		click(InstaLoanPage.objFatherName, "Father Name Field");
		type(InstaLoanPage.objFatherName, "Arun", "Father Name Field");

		waitTime(20000);
		verifyElementPresentAndClick(InstaLoanPage.objProceedBtn, "Proceed Button");
		waitTime(5000);
		verifyElementPresentAndClick(InstaLoanPage.objProceedBtn, "Proceed Button");
		click(InstaLoanPage.objProceedBtn, "Proceed Button");
		waitTime(5000);
		verifyElementPresentAndClick(InstaLoanPage.objSelectAddress, "Select Address Dropdown");
		verifyElementPresentAndClick(InstaLoanPage.objAddressSelected, getText(InstaLoanPage.objAddressSelected));
		verifyElementPresentAndClick(RingUserDetailPage.objRegisterBtn, "Proceed Button");
		offerPage();
	}

	public void offerPage() throws Exception {
		if (verifyElementPresent2(InstaLoanPage.objOfferHeader, "Offer Page")) {
			verifyElementPresent(InstaLoanPage.objOfferHeader, getText(InstaLoanPage.objOfferHeader) + "Header");
			verifyElementPresent(InstaLoanPage.objEligible,getText(InstaLoanPage.objEligible) + getText(InstaLoanPage.objEligibleInstaLoanAmt));
			verifyElementPresentAndClick(InstaLoanPage.objAddBankButton, getText(InstaLoanPage.objAddBankButton));
			addbankAccount("5" + RandomIntegerGenerator(4), firstName + " " + lastName);
		}
	}

	public void payNowInstaLoan() throws Exception {

		explicitWaitVisibilityNonDefault(InstaLoanPage.objInstaLoanAmountApproved, 10);
		String instaLoanAmountApproved = getText(InstaLoanPage.objInstaLoanAmountApproved);
		instaLoanAmountApproved = instaLoanAmountApproved.substring(1);
		String[] split1 = instaLoanAmountApproved.split(",");
		String values = split1[0] + split1[1];
		System.out.println(values);
		softAssertion.assertEquals(value, values);
		if (value == values) {
			logger.info("TC_Insta_loan_08 - To verify if user gets the same amount of loan if user is present in whitelist ");
			extent.extentLoggerPass("TC_Insta_loan_08","TC_Insta_loan_08 - To verify if user gets the same amount of loan if user is present in whitelist ");
		}

		verifyElementPresentAndClick(InstaLoanPage.objViewDetails, getText(InstaLoanPage.objViewDetails) + " Button");
		click(InstaLoanPage.objPayFullAmountRadioBtn, "Pay Full Amount RadioButton");
		verifyElementPresentAndClick(InstaLoanPage.objPayNowBtn, getText(InstaLoanPage.objPayNowBtn) + " Button");
		explicitWaitVisibilityNonDefault(InstaLoanPage.objNetBankingDebitCard, 20);
		verifyElementPresentAndClick(InstaLoanPage.objNetBankingDebitCard,getText(InstaLoanPage.objNetBankingDebitCard) + " Chip");
		waitTime(20000);
		verifyElementPresentAndClick(InstaLoanPage.objCard, "Card chip");
		addNewCard();
		String refAfterRepayment = executeQuery2("SELECT * FROM db_txn_service.instaloan_whitelisted_users Where user_reference_number = '"+ userReferenceNo + "';");
		System.out.println(refAfterRepayment);
		softAssertion.assertEquals(refBeforeRepayment, refAfterRepayment);

		boolean reApplyForInstaLoanBanner = verifyElementPresent2(InstaLoanPage.objReApplyForInstaLoan,getText(InstaLoanPage.objReApplyForInstaLoan));
		if (reApplyForInstaLoanBanner == true) {
			logger.info("TC_Insta_loan_07 - To verify if user remains in whitelist table even if user has repaid the previous loan");
			extent.extentLoggerPass("TC_Insta_loan_07","TC_Insta_loan_07 - To verify if user remains in whitelist table even if user has repaid the previous loan");
		}
		deleteRowFromInstaLoanWhitelistTable("DELETE FROM db_txn_service.instaloan_whitelisted_users WHERE user_reference_number = '" + userReferenceNo+ "';");
	}

	public void addNewCard() throws Exception {
		click(InstaLoanPage.objCardNumber, "Card Number Field");
		type(InstaLoanPage.objCardNumber, prop.getproperty("cardNumber"), "Card Number Field");

		click(InstaLoanPage.objExpiryDate, "Expiry Date Field");
		type(InstaLoanPage.objExpiryDate, prop.getproperty("expiryDate"), "Expiry Date Field");

		click(InstaLoanPage.objCardHolderName, "Card Holder's Field");
		type(InstaLoanPage.objCardHolderName, prop.getproperty("userName"), "Card Holder's Field");

		click(InstaLoanPage.objCVV, "CVV Field");
		type(InstaLoanPage.objCVV, prop.getproperty("cvv"), "CVV Field");

		verifyElementPresentAndClick(InstaLoanPage.objPayNowBtn, getText(InstaLoanPage.objPayNowBtn) + " Button");
		verifyElementPresentAndClick(InstaLoanPage.objPayWithoutSavingCardBtn,getText(InstaLoanPage.objPayWithoutSavingCardBtn) + " Button");

		if (verifyElementPresent2(InstaLoanPage.objSuccessBtn, "Success Button")) {
			verifyElementPresentAndClick(InstaLoanPage.objSuccessBtn, getText(InstaLoanPage.objSuccessBtn));
		} else {
			click(InstaLoanPage.objCardOTPField, "OTP field");
			type(InstaLoanPage.objCardOTPField, prop.getproperty("OTP"), "OTP Field");
			verifyElementPresentAndClick(InstaLoanPage.objSubmitButton,getText(InstaLoanPage.objSubmitButton) + " Button");
		}
		waitTime(20000);
		verifyElementPresentAndClick(HomPageNew.objHomePage, "Go to homepage button");
	}

	public void deleteRowFromInstaLoanWhitelistTable(String deleteQuery) throws Exception {
		deleteRow(deleteQuery);
		Swipe("down", 1);
		waitTime(10000);
		boolean reApplyForInstaLoanBanner = verifyElementPresent2(InstaLoanPage.objApplyBtn, "InstaLoan Banner");
		logger.info("InstaLoan Banner present : " + reApplyForInstaLoanBanner);
		extent.extentLogger("InstaLoan Banner", "InstaLoan Banner present : " + reApplyForInstaLoanBanner);

		if (reApplyForInstaLoanBanner == true) {
			logger.info("InstaLoan Banner is present even if it isremoved from whitelist table");
			extent.extentLoggerFail("InstaLoan Banner","InstaLoan Banner is present even if it isremoved from whitelist table");
		} else {
			logger.info("TC_Insta_loan_09 - To verify if user has repaid the previous Insta loan and removed from whitelist");
			extent.extentLoggerPass("TC_Insta_loan_09","TC_Insta_loan_09 - To verify if user has repaid the previous Insta loan and removed from whitelist");
		}
	}

	public void blockUser() throws Exception {

		setPlatform("Web");
		System.out.println("platform changed to web");
		String pf = getPlatform();
		System.out.println(pf);
		waitTime(5000);
		new RingPayBusinessLogic("ring");
		waitTime(10000);

		getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobileNumber+"&");
		type(InstaLoanPage.objEmail,"shakir.muchumarri@collabera.com","Email text field");
		click(InstaLoanPage.objContinue,"Continue Button");
		waitTime(10000);
		explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
		String userRefNo = getText(InstaLoanPage.objUserRefNo);
		BrowsertearDown();

		setPlatform("Android");
		initDriver();
		waitTime(5000);

		executeInsertBlockUser(prop.getproperty("blocked_reason"), mobileNumber, prop.getproperty("entryType"));
		String blockUser = executeQuery3("SELECT * FROM db_txn_service.blocked_users Where entity_value = '" + mobileNumber + "';", 5);
		System.out.println(blockUser);

		verifyElementPresentAndClick(InstaLoanOptionalJourney.objUPITab, "UPI Tab");
		waitTime(20000);
		Back(1);

		String instaLoanWhitelistNo = executeQuery3("SELECT * FROM db_txn_service.instaloan_whitelisted_users Where user_reference_number = '" + userRefNo+ "';",2);
		System.out.println("User is present in WhiteListTable : " + instaLoanWhitelistNo);

		if (instaLoanWhitelistNo.equals(userRefNo)) {
			softAssertion.assertEquals(instaLoanWhitelistNo, userRefNo);
			Swipe("down", 1);
			boolean instaLoanBanner = verifyElementPresent2(InstaLoanPage.objApplyBtn, "InstaLoan Banner");
			logger.info("InstaLoan Banner present : " + instaLoanBanner);
			extent.extentLogger("InstaLoan Banner", "InstaLoan Banner present : " + instaLoanBanner);
			System.out.println(instaLoanBanner);

			if (instaLoanBanner == true) {
				logger.info("InstaLoan Banner is present even if it isremoved from whitelist table");
				extent.extentLoggerFail("InstaLoan Banner","InstaLoan Banner is present even if it is removed from whitelist table");
			} else {
				logger.info("TC_Insta_loan_11 - To verify Insta loan offer is not avail if user becomes blocked even if customer is present in whitelist");
				extent.extentLoggerPass("TC_Insta_loan_11","TC_Insta_loan_11 - To verify Insta loan offer is not avail if user becomes blocked even if customer is present in whitelist");
			}
		}
	}

	public void instaApprovedAmountNull(String ringAdminEmail, String ringAdminPassword, String ringAdminOTP,String eligible_type, String approved_amount, String offer_id, String repeat_offer_id) throws Exception {
		waitTime(5000);
		setPlatform("Web");
		System.out.println("platform changed to web");
		String pf = getPlatform();
		System.out.println(pf);
		waitTime(5000);
		new RingPayBusinessLogic("ring");
		waitTime(10000);

		getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobileNumber+"&");
		type(InstaLoanPage.objEmail,"shakir.muchumarri@collabera.com","Email text field");
		click(InstaLoanPage.objContinue,"Continue Button");
		waitTime(10000);
		explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
		String userRefNo = getText(InstaLoanPage.objUserRefNo);
		verifyPan(mobileNumber,dob,firstName,lastName,middleName);

		BrowsertearDown();
		waitTime(10000);
		setPlatform("Android");
		initDriver();
		waitTime(5000);
		try {
			executeInsert(userRefNo, eligible_type, approved_amount, offer_id, repeat_offer_id);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Approved Amount,offer_id, reference_number cannot be null");
			extent.extentLogger("approved amount", "Approved Amount,offer_id, reference_number cannot be null");
		}
	}

	public void instaReferenceNoNull(String ringAdminEmail, String ringAdminPassword, String ringAdminOTP,String ref_No, String eligible_type, String approved_amount, String offer_id, String repeat_offer_id)throws Exception {
		waitTime(5000);
		setPlatform("Web");
		System.out.println("platform changed to web");
		String pf = getPlatform();
		System.out.println(pf);
		waitTime(5000);
		new RingPayBusinessLogic("ring");
		waitTime(10000);

		/*click(InstaLoanPage.objEmail, "Email Field");
		type(InstaLoanPage.objEmail, ringAdminEmail, "Email Field");
		verifyElementPresentAndClick(InstaLoanPage.objContinueBtn, getText(InstaLoanPage.objContinueBtn));
		click(InstaLoanPage.objPasswordField, "Password Field");
		clearField(InstaLoanPage.objPasswordField, "Password Field");
		type(InstaLoanPage.objPasswordField, ringAdminPassword, "Password Field");
		click(InstaLoanPage.objOTPField, "OTP Field");
		clearField(InstaLoanPage.objOTPField, "OTP Field");
		type(InstaLoanPage.objOTPField, ringAdminOTP, "OTP Field");
		verifyElementPresentAndClick(InstaLoanPage.objLoginBtn, getText(InstaLoanPage.objLoginBtn));
		verifyElementPresentAndClick(InstaLoanPage.objUserTab, getText(InstaLoanPage.objUserTab));
		selectByVisibleTextFromDD(InstaLoanPage.objSearchUser, "Mobile Number");
		click(InstaLoanPage.objSearchTerm, "Search Item Field");
		type(InstaLoanPage.objSearchTerm, mobileNumber, "Search Item Field");
		click(InstaLoanPage.objSearchBtn, "Search Button");*/
		waitTime(10000);

		try {
			executeInsert(ref_No, eligible_type, approved_amount, offer_id, repeat_offer_id);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Approved Amount,offer_id, reference_number cannot be null");
			extent.extentLogger("approved amount", "Approved Amount,offer_id, reference_number cannot be null");
		}

		/*ScrollToTheElement(InstaLoanPage.dragDown);
		waitTime(5000);
		JSClick(InstaLoanPage.objOthersTab, "Other Tab");
		ScrollToTheElement(InstaLoanPage.dragDown);
		JSClick(InstaLoanPage.objPanNSDLData, "PanNSDL Tab");
		JSClick(InstaLoanPage.objAddPanCard, "Add PanCard");

		click(InstaLoanPage.objNameField, "First Name Field");
		type(InstaLoanPage.objNameField, firstName, "First Name Field");
		click(InstaLoanPage.objMiddleNameField, "Middle Name Field");
		type(InstaLoanPage.objMiddleNameField, middleName, "Middle Name Field");
		click(InstaLoanPage.objLastNameField, "Last Name Field");
		type(InstaLoanPage.objLastNameField, lastName, "Last Name Field");
		click(InstaLoanPage.objPanNoField, "PanNo. Field");
		type(InstaLoanPage.objPanNoField, panCard, "PanNo. Field");
		selectByVisibleTextFromDD(InstaLoanPage.objPanStatus, "Valid");
		verifyElementPresentAndClick(InstaLoanPage.objSubmitBtn, "Submit Button");*/
		verifyPan(mobileNumber,dob,firstName,lastName,middleName);
		BrowsertearDown();
		waitTime(10000);
		setPlatform("Android");
		initDriver();
		waitTime(5000);
	}

	public void payPreviousLoan() throws Exception {
		verifyElementPresentAndClick(InstaLoanPage.objViewDetails, getText(InstaLoanPage.objViewDetails) + " Button");
		click(InstaLoanPage.objPayFullAmountRadioBtn, "Pay Full Amount RadioButton");
		verifyElementPresentAndClick(InstaLoanPage.objPayNowBtn, getText(InstaLoanPage.objPayNowBtn) + " Button");
		explicitWaitVisibilityNonDefault(InstaLoanPage.objNetBankingDebitCard, 20);
		verifyElementPresentAndClick(InstaLoanPage.objNetBankingDebitCard,getText(InstaLoanPage.objNetBankingDebitCard) + " Chip");
		waitTime(20000);
		verifyElementPresentAndClick(InstaLoanPage.objCard, "Card chip");
		addNewCard();

		boolean reApplyForInstaLoanBanner = verifyElementPresent2(InstaLoanPage.objReApplyForInstaLoan,"ReApply for Insta Loan! Banner");
		if (reApplyForInstaLoanBanner == false) {
			logger.info("TC_Insta_loan_18 - To verify if User is removed from Whitelist Loan after taking InstaLoan");
			extent.extentLoggerPass("TC_Insta_loan_18","TC_Insta_loan_18 - To verify if User is removed from Whitelist Loan after taking InstaLoan");
		}
	}

//=====================================================InstaLoan WhiteList Logic Ends============================================================================	
//========================================================Transaction History Start==========================================================================================

		public void instaTransactionHistory(String InstatransactionHistoryNo, String OTP) throws Exception {
			extent.HeaderChildNode("Transaction History");
			getDriver().resetApp();
			loginOnboarding("1");
			mobileNoValidation1(InstatransactionHistoryNo);
			enterOtp(OTP);
			readAndAccept();
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			if(verifyElementPresent2(InstaLoanOptionalJourney.objNoBtn, "No Button")) {
				click(InstaLoanOptionalJourney.objNoBtn, getText(InstaLoanOptionalJourney.objNoBtn) + " Button");
			}
			explicitWaitVisibilityNonDefault(InstaLoanTransactionHistoryPage.objHamburgerTab, 10);
			verifyElementPresentAndClick(InstaLoanTransactionHistoryPage.objHamburgerTab, "Hamburger Tab");
			verifyElementPresentAndClick(InstaLoanTransactionHistoryPage.objTransactionChip,getText(InstaLoanTransactionHistoryPage.objTransactionChip) + " Chip");
			explicitWaitVisibilityNonDefault(InstaLoanTransactionHistoryPage.objHistoryHeader, 10);
			verifyElementPresent(InstaLoanTransactionHistoryPage.objHistoryHeader,getText(InstaLoanTransactionHistoryPage.objHistoryHeader));
			softAssertion.assertEquals(InstaLoanTransactionHistoryPage.objHistoryHeader, "History");
			verifyElementPresent(InstaLoanTransactionHistoryPage.objTransactionHistoryTab,getText(InstaLoanTransactionHistoryPage.objTransactionHistoryTab));

			List<WebElement> transactionList = getDriver().findElements(InstaLoanTransactionHistoryPage.objTransactionList);
			for (WebElement wb : transactionList) {
				String list = wb.getText();
				logger.info(list);
				extent.extentLogger("Transaction_list", list);
			}

			softAssertion.assertEquals(InstaLoanTransactionHistoryPage.objTransactionHistoryTab, " Transaction History");
			verifyElementPresent(InstaLoanTransactionHistoryPage.objLoanHistoryTab,getText(InstaLoanTransactionHistoryPage.objLoanHistoryTab));
			softAssertion.assertEquals(InstaLoanTransactionHistoryPage.objLoanHistoryTab, "Loan History");
			verifyElementPresent(InstaLoanTransactionHistoryPage.objFilterOption, "Filter Option");

			verifyElementPresentAndClick(InstaLoanTransactionHistoryPage.objLoanHistoryTab, "Loan History");
			verifyElementPresentAndClick(InstaLoanTransactionHistoryPage.objFilterOption, "Filter Option");

			if (verifyElementPresent2(InstaLoanTransactionHistoryPage.objTimePeriod,getText(InstaLoanTransactionHistoryPage.objTimePeriod))) {
				verifyElementPresent(InstaLoanTransactionHistoryPage.objThisMonthChip,getText(InstaLoanTransactionHistoryPage.objThisMonthChip));
				verifyElementPresent(InstaLoanTransactionHistoryPage.objLastMonth,getText(InstaLoanTransactionHistoryPage.objLastMonth));
				verifyElementPresent(InstaLoanTransactionHistoryPage.objCustomDate,getText(InstaLoanTransactionHistoryPage.objCustomDate));
				verifyElementPresent(InstaLoanTransactionHistoryPage.objLoanType,getText(InstaLoanTransactionHistoryPage.objLoanType));
				verifyElementPresent(InstaLoanTransactionHistoryPage.objAll,getText(InstaLoanTransactionHistoryPage.objAll));
				verifyElementPresent(InstaLoanTransactionHistoryPage.objActiveLoan,getText(InstaLoanTransactionHistoryPage.objActiveLoan));
				verifyElementPresent(InstaLoanTransactionHistoryPage.objClosedLoan,getText(InstaLoanTransactionHistoryPage.objClosedLoan));
				verifyElementPresent(InstaLoanTransactionHistoryPage.objClearBtn,getText(InstaLoanTransactionHistoryPage.objClearBtn));
				verifyElementPresent(InstaLoanTransactionHistoryPage.objApplyBtn,getText(InstaLoanTransactionHistoryPage.objApplyBtn));

				for (int i = 1; i <= 3; i++) {
					click(InstaLoanTransactionHistoryPage.objTimePeriodSelect(i), "Element " + i + " " + " of TimePeriod");
					if (verifyElementPresent2(InstaLoanTransactionHistoryPage.objDatePicker, "Date-picker")) {
						verifyElementPresentAndClick(InstaLoanTransactionHistoryPage.objOkBtn,
								getText(InstaLoanTransactionHistoryPage.objOkBtn));
					}
					for (int j = 1; j <= 3; j++) {
						verifyElementPresentAndClick(InstaLoanTransactionHistoryPage.objLoanTypeSelect(j),"Element " + j + " " + " of LoanTypes");
						waitTime(5000);
						click(InstaLoanTransactionHistoryPage.objApplyBtn, "Apply Button");
						waitTime(3000);
						if (!verifyElementPresent2(InstaLoanTransactionHistoryPage.objTimePeriod, "Time Period")) {
							verifyElementPresentAndClick(InstaLoanTransactionHistoryPage.objFilterOption, "Filter Option");
						}
					}
				}
				click(InstaLoanTransactionHistoryPage.objApplyBtn, "Apply Button");
			}

			verifyElementPresentAndClick(InstaLoanTransactionHistoryPage.objFilterOption, "Filter Option");
			verifyElementPresentAndClick(InstaLoanTransactionHistoryPage.objClearBtn,getText(InstaLoanTransactionHistoryPage.objClearBtn));

			if (verifyElementPresent(InstaLoanTransactionHistoryPage.objLoanHistoryActiveStatus,getText(InstaLoanTransactionHistoryPage.objLoanHistoryActiveStatus))) {
				verifyElementPresentAndClick(InstaLoanTransactionHistoryPage.objLoanHistoryActiveStatusViewDetails,getText(InstaLoanTransactionHistoryPage.objLoanHistoryActiveStatusViewDetails));
				waitTime(5000);
				List<WebElement> viewList = getDriver().findElements(InstaLoanTransactionHistoryPage.objAllLoanDetail);
				for (WebElement wb : viewList) {
					String vlist = wb.getText();
					logger.info(vlist);
					extent.extentLogger("Loan_Details", vlist);
				}
				Swipe("up", 1);
				verifyElementPresentAndClick(InstaLoanTransactionHistoryPage.objPayNoBtn,getText(InstaLoanTransactionHistoryPage.objPayNoBtn));
				if (verifyElementPresent(InstaLoanTransactionHistoryPage.objLoanHistoryActiveStatusPayEMIPaymentPage,getText(InstaLoanTransactionHistoryPage.objLoanHistoryActiveStatusPayEMIPaymentPage))) {
					Back(2);
				}
				verifyElementPresentAndClick(InstaLoanTransactionHistoryPage.objLoanHistoryActiveStatusViewDetails,getText(InstaLoanTransactionHistoryPage.objLoanHistoryActiveStatusViewDetails));
				if (verifyElementPresent(InstaLoanTransactionHistoryPage.objLoanHistoryActiveStatusViewDetailsLoanDetailsPage,getText(InstaLoanTransactionHistoryPage.objLoanHistoryActiveStatusViewDetailsLoanDetailsPage))) {
					Back(1);
				}
				verifyElementPresentAndClick(InstaLoanTransactionHistoryPage.objLoanHistoryActiveStatusPayEMI,getText(InstaLoanTransactionHistoryPage.objLoanHistoryActiveStatusPayEMI));
				if (verifyElementPresent(InstaLoanTransactionHistoryPage.objLoanHistoryActiveStatusPayEMIPaymentPage,getText(InstaLoanTransactionHistoryPage.objLoanHistoryActiveStatusPayEMIPaymentPage))) {
					Back(1);
				}
			}

			if (verifyElementPresent(InstaLoanTransactionHistoryPage.objLoanHistoryClosedDownloadNocBtn,getText(InstaLoanTransactionHistoryPage.objLoanHistoryClosedDownloadNocBtn))) {
				verifyElementPresent(InstaLoanTransactionHistoryPage.objLoanHistoryClosedStatus,getText(InstaLoanTransactionHistoryPage.objLoanHistoryClosedStatus));
				verifyElementPresentAndClick(InstaLoanTransactionHistoryPage.objLoanHistoryActiveStatusViewDetails,getText(InstaLoanTransactionHistoryPage.objLoanHistoryActiveStatusViewDetails));
				if (verifyElementPresent(InstaLoanTransactionHistoryPage.objLoanHistoryActiveStatusViewDetailsLoanDetailsPage,getText(InstaLoanTransactionHistoryPage.objLoanHistoryActiveStatusViewDetailsLoanDetailsPage))) {
					Back(1);
				}
				verifyElementPresentAndClick(InstaLoanTransactionHistoryPage.objLoanHistoryClosedDownloadNocBtn,getText(InstaLoanTransactionHistoryPage.objLoanHistoryClosedDownloadNocBtn));

				Set<String> windows = getDriver().getContextHandles();
				for (String window : windows) {
					System.out.println("Active Applications " + window);
					getDriver().context("NATIVE_APP");
				}
				verifyElementPresentAndClick(InstaLoanTransactionHistoryPage.objNOCPdf,getText(InstaLoanTransactionHistoryPage.objNOCPdf));
				verifyElementPresentAndClick(InstaLoanTransactionHistoryPage.objThreeDotForDownloadPDF,"Three Dot to DownloadPDF file");
				verifyElementPresentAndClick(InstaLoanTransactionHistoryPage.objDownloadBtn,getText(InstaLoanTransactionHistoryPage.objDownloadBtn));

				String toastPDFDownloadMsg = getText(InstaLoanTransactionHistoryPage.objDownloadPDFToastMsg);
				System.out.println(toastPDFDownloadMsg);
				if (toastPDFDownloadMsg.contains("Downloaded Noc_Letter")) {
					logger.info("TC_Insta_loan_136 - To Verify for Loan details page for Closed loans from Loan History View Details option ");
					extent.extentLoggerPass("TC_Insta_loan_136","TC_Insta_loan_136 - To Verify for Loan details page for Closed loans from Loan History View Details option ");
				}
				Back(1);
			}

			verifyElementPresentAndClick(InstaLoanTransactionHistoryPage.objLoanHistoryActiveStatusViewDetails,getText(InstaLoanTransactionHistoryPage.objLoanHistoryActiveStatusViewDetails));
			verifyElementPresentAndClick(InstaLoanTransactionHistoryPage.objLoanDetailViewFeeDetailsBtn,getText(InstaLoanTransactionHistoryPage.objLoanDetailViewFeeDetailsBtn));
			verifyElementPresent(InstaLoanTransactionHistoryPage.objFeeDetailsHeader,getText(InstaLoanTransactionHistoryPage.objFeeDetailsHeader));
			verifyElementPresentAndClick(InstaLoanTransactionHistoryPage.objLoanAmountText,getText(InstaLoanTransactionHistoryPage.objLoanAmountText) + " is : "+ getText(InstaLoanTransactionHistoryPage.objLoanAmount));
			verifyElementPresentAndClick(InstaLoanTransactionHistoryPage.objTotalFeesChargesText,getText(InstaLoanTransactionHistoryPage.objTotalFeesChargesText) + " is : "+ getText(InstaLoanTransactionHistoryPage.objTotalFeesCharges));
			verifyElementPresentAndClick(InstaLoanTransactionHistoryPage.objProcessingFeesText,getText(InstaLoanTransactionHistoryPage.objProcessingFeesText) + " is : "+ getText(InstaLoanTransactionHistoryPage.objProcessingFees));
			verifyElementPresentAndClick(InstaLoanTransactionHistoryPage.objGSTText,getText(InstaLoanTransactionHistoryPage.objGSTText) + " is : "+ getText(InstaLoanTransactionHistoryPage.objGST));
			verifyElementPresentAndClick(InstaLoanTransactionHistoryPage.objDisbursalAmountText,getText(InstaLoanTransactionHistoryPage.objDisbursalAmountText) + " is : "+ getText(InstaLoanTransactionHistoryPage.objDisbursalAmount));

			logger.info("TC_Insta_loan_128 - To Verify After Click on History Tab from Home page, User should  Redirect on History Page & It should display  All  Transaction History on page");
			extent.extentLoggerPass("TC_Insta_loan_128","TC_Insta_loan_128 - To Verify After Click on History Tab from Home page, User should  Redirect on History Page & It should display  All  Transaction History on page");

			logger.info("TC_Insta_loan_129 - User should able to see Filter option in Loan History tab");
			extent.extentLoggerPass("TC_Insta_loan_129", "TC_Insta_loan_129 - User should able to see Filter option in Loan History tab");

			logger.info("TC_Insta_loan_130 - User should able to see & select all option to filter out loans");
			extent.extentLoggerPass("TC_Insta_loan_130", "TC_Insta_loan_130 - User should able to see & select all option to filter out loans");

			logger.info("TC_Insta_loan_131 - To Verify On Transaction History Page should display all Transaction History of User");
			extent.extentLoggerPass("TC_Insta_loan_131","TC_Insta_loan_131 - To Verify On Transaction History Page should display all Transaction History of User");

			logger.info("TC_Insta_loan_133 - To Verify On Loan history page for Active loan  \"View Details\"link  & \"Pay EMI\" button should be display.");
			extent.extentLoggerPass("TC_Insta_loan_133","TC_Insta_loan_133 - To Verify On Loan history page for Active loan  \"View Details\"link  & \"Pay EMI\" button should be display.");

			logger.info("TC_Insta_loan_134 - To Verify On Loan History page for Closed Loan \"View Details\" link & \"Download NOC\" button should  be display ");
			extent.extentLoggerPass("TC_Insta_loan_134","TC_Insta_loan_134 - To Verify On Loan History page for Closed Loan \"View Details\" link & \"Download NOC\" button should  be display ");

			logger.info("TC_Insta_loan_135 - To Verify for Loan details page for active loans from Loan History View Details option ");
			extent.extentLoggerPass("TC_Insta_loan_135","TC_Insta_loan_135 - To Verify for Loan details page for active loans from Loan History View Details option ");

			logger.info("TC_Insta_loan_137 - To verify View Fee Details from Loan Details page");
			extent.extentLoggerPass("TC_Insta_loan_137", "TC_Insta_loan_137 - To verify View Fee Details from Loan Details page");
		}

//==============================================Transaction History Start===================================================================================================================
		//============================================Insta Loan Optional Journey start=============================================================================================================
		public void instaLoanOptioanlJourney() throws Exception {
			extent.HeaderChildNode("InstaLoan Optional Journey");
			getDriver().resetApp();
			//instalonAPIKycVerification();	
			loginOnboarding("1");
			mobileNoValidation1(mobileNumber);
			enterOtp(prop.getproperty("OTP"));
			readAndAccept();
			waitTime(30000);
			instaUserDetails(firstName, lastName, mothersName, email, gender);
			instalonAPIKycVerification(mobileNumber);
			instaNewCommunicationAddress();
			waitTime(10000);
			instaKycDocument();
			instaPanCardDetails(prop.getproperty("RingAdminEmail"), prop.getproperty("RingAdminPassword"),prop.getproperty("RingAdminOTP"), "OPTIONAL", "13000", "2311", "2319");
			panCardDetails();
			waitTime(5000);
			instaLoancongratsScreen("Congrats");
			instaLoanSetPin(prop.getproperty("InstaLoanMPIN"), prop.getproperty("InstaLoanMPIN"));
			waitTime(5000);
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			verifyElementPresentAndClick(InstaLoanPage.instaPopupBannerCrossBtn, "Cross Button");
			click(InstaLoanPage.objOkayGotIt, "Okay, Got It!");
			
			Swipe("up", 2);
			String referenceNo = switchAdminPanelForRefNo();
			String refNoFromDB = executeQuery3("SELECT * FROM db_txn_service.instaloan_whitelisted_users Where user_reference_number = '" + referenceNo+ "';",4);
			logger.info("Eligible type : " + refNoFromDB);
			extent.extentLogger("Eligible type : ", "Eligible type : " + refNoFromDB);

			verifyElementPresent(InstaLoanOptionalJourney.objInstaOfferBanner,getText(InstaLoanOptionalJourney.objInstaOfferBanner));
			logger.info("TC_Insta_loan_40 - To verify when the user eligible type is optional, user should choose either Ring limit or an insta loan offer");
			extent.extentLoggerPass("TC_Insta_loan_40","To verify when the user eligible type is optional, user should choose either Ring limit or an insta loan offer");
			
			verifyElementPresentAndClick(InstaLoanOptionalJourney.objQRScanner, "QR Code Scann Button");
			String scanAndPayToastMsg = getText(InstaLoanOptionalJourney.objScanAndPayToastMsg);
			logger.info(scanAndPayToastMsg);
			extent.extentLogger("Toast Message", scanAndPayToastMsg);
			verifyElementPresent(InstaLoanOptionalJourney.objQRScannPage,getText(InstaLoanOptionalJourney.objQRScannPage));
			logger.info("TC_Insta_loan_42 - To verify optional eligible users, all transaction instruments like Scan & pay will be enabled until avail(FA) insta loan offer ");
			extent.extentLoggerPass("TC_Insta_loan_42","To verify optional eligible users, all transaction instruments like Scan & pay will be enabled until avail(FA) insta loan offer ");

			scannQRSwitch();
			waitTime(10000);
			type(InstaLoanOptionalJourney.objAmountEnter, "100", "Amount Field");
			doubleTap(InstaLoanOptionalJourney.objPayNowBtn, 1, "Pay Now Button");
			waitTime(40000);
			click(InstaLoanOptionalJourney.objPINField, "PIN Field");
			type(InstaLoanOptionalJourney.objPINField, prop.getproperty("ValidPin"), "PIN Field");
			verifyElementPresentAndClick(InstaLoanOptionalJourney.objContinueBtn,getText(InstaLoanOptionalJourney.objContinueBtn));
			waitTime(20000);
			verifyElementPresentAndClick(HomPageNew.objHomePage, "Go to homepage button");
			click(InstaLoanOptionalJourney.objNoBtn, getText(InstaLoanOptionalJourney.objNoBtn) + " Button");
			
			explicitWaitVisibilityNonDefault(InstaLoanOptionalJourney.objPayEarly, 20);
			verifyElementPresentAndClick(InstaLoanOptionalJourney.objPayEarly,getText(InstaLoanOptionalJourney.objPayEarly));
			verifyElementPresent(InstaLoanOptionalJourney.objUPIPaymentMethod,getText(InstaLoanOptionalJourney.objUPIPaymentMethod));
			verifyElementPresent(InstaLoanOptionalJourney.objNetBankingAndDebitCardPaymentMethod,getText(InstaLoanOptionalJourney.objNetBankingAndDebitCardPaymentMethod));
			verifyElementPresent(InstaLoanOptionalJourney.objBankTransferPaymentMethod,getText(InstaLoanOptionalJourney.objBankTransferPaymentMethod));
			Back(1);
			verifyElementPresentAndClick(InstaLoanOptionalJourney.objMoreBottomTab,getText(InstaLoanOptionalJourney.objMoreBottomTab));
			verifyElementPresentAndClick(InstaLoanOptionalJourney.objBankTransfer,getText(InstaLoanOptionalJourney.objBankTransfer));
			explicitWaitVisibilityNonDefault(InstaLoanOptionalJourney.objAddBankAccount, 20);
			boolean addBankHeader = verifyElementPresent(InstaLoanOptionalJourney.objAddBankAccount,getText(InstaLoanOptionalJourney.objAddBankAccount));
			if (addBankHeader == true) {
				logger.info("TC_Insta_loan_41 - To verify optional eligible users, all transaction instruments like Card will be enabled until avail(FA) insta loan offer ");
				extent.extentLoggerPass("TC_Insta_loan_41","To verify optional eligible users, all transaction instruments like Card will be enabled until avail(FA) insta loan offer ");

				addbankAccount("5" + RandomIntegerGenerator(4), firstName + " " + lastName);
				explicitWaitVisibilityNonDefault(InstaLoanOptionalJourney.objMakePaymentPage, 30);
				type(InstaLoanOptionalJourney.objMakePaymentAmount, "100", "Amount Field");
				click(InstaLoanOptionalJourney.objPayNowBtn, "Pay Now Button");
				doubleTap(InstaLoanOptionalJourney.objPayNowBtn, 1, "Pay Now Button");
				click(InstaLoanOptionalJourney.objPINField, "PIN Field");
				type(InstaLoanOptionalJourney.objPINField, prop.getproperty("ValidPin"), "PIN Field");
				click(InstaLoanOptionalJourney.objCircularBtn, "Circular Button");
				verifyElementPresentAndClick(HomPageNew.objHomePage, "Go to homepage button");

				logger.info("TC_Insta_loan_44 - To verify optional eligible users, all transaction instruments like bank transfer will be enabled until avail insta loan offer ");
				extent.extentLoggerPass("TC_Insta_loan_44","To verify optional eligible users, all transaction instruments like bank transfer will be enabled until avail insta loan offer ");
			}
			verifyElementPresentAndClick(InstaLoanOptionalJourney.objMoreBottomTab,getText(InstaLoanOptionalJourney.objMoreBottomTab));
			verifyElementPresentAndClick(InstaLoanOptionalJourney.objBBPSFlowElectricity,getText(InstaLoanOptionalJourney.objBBPSFlowElectricity));
			explicitWaitVisibilityNonDefault(InstaLoanOptionalJourney.objSelectBiller, 20);
			scrollToElement("text", "Bangalore Electricity Supply Co. Ltd (BESCOM)").click();
			click(InstaLoanOptionalJourney.objEnterCustomerID, "Customer ID Field");
			waitTime(5000);
			type(InstaLoanOptionalJourney.objEnterCustomerID, "10001", "Customer Number Field");

			verifyElementPresentAndClick(InstaLoanOptionalJourney.objFetchBill,getText(InstaLoanOptionalJourney.objFetchBill));
			waitTime(20000);
			verifyElementPresentAndClick(InstaLoanOptionalJourney.objPayBtn,getText(InstaLoanOptionalJourney.objPayBtn) + " Button");
			explicitWaitVisibilityNonDefault(InstaLoanOptionalJourney.objPaymentInProgress, 20);
			verifyElementPresentAndClick(InstaLoanOptionalJourney.objPayWithRingLimit,getText(InstaLoanOptionalJourney.objPayWithRingLimit) + " Button");
			explicitWaitVisibilityNonDefault(InstaLoanOptionalJourney.objEnterSecurityPinforElectricityBill, 20);
			click(InstaLoanOptionalJourney.objPINFieldElectricityBill, "PIN Field");
			type(InstaLoanOptionalJourney.objPINFieldElectricityBill, prop.getproperty("ValidPin"), "PIN Field");
			verifyElementPresentAndClick(InstaLoanOptionalJourney.objContinueBtn,getText(InstaLoanOptionalJourney.objContinueBtn));
			verifyElementPresent2(InstaLoanOptionalJourney.objProcessing,"Processing your Payment");
			waitTime(30000);
			Back(2);
			logger.info("TC_Insta_loan_43 - To verify optional eligible users, all transaction instruments like BBPS  will be enabled until avail(FA) insta loan offer ");
			extent.extentLoggerPass("TC_Insta_loan_43","To verify optional eligible users, all transaction instruments like BBPS  will be enabled until avail(FA) insta loan offer ");
			
			verifyElementPresent(InstaLoanOptionalJourney.objPayEarly,getText(InstaLoanOptionalJourney.objPayEarly));
			verifyElementPresent(InstaLoanOptionalJourney.objCurrentSpending,getText(InstaLoanOptionalJourney.objCurrentSpending));
			verifyElementPresentAndClick(InstaLoanOptionalJourney.objPayEarly,getText(InstaLoanOptionalJourney.objPayEarly));
			logger.info("TC_Insta_loan_46 - To verify ring limit outstanding non 0 and entry in the whitelisting table");
			extent.extentLoggerPass("TC_Insta_loan_46","To verify ring limit outstanding non 0 and entry in the whitelisting table");
			
			verifyElementPresentAndClick(InstaLoanOptionalJourney.objNetBankingAndDebitCardPaymentMethod,getText(InstaLoanOptionalJourney.objNetBankingAndDebitCardPaymentMethod));
			waitTime(20000);
			verifyElementPresentAndClick(InstaLoanPage.objCard, "Card chip");
			addNewCard();
			verifyElementPresentAndClick(HomPageNew.objHomePage, "Go to homepage button");
			Swipe("up", 2);
			executeUpdateFatherName("Update db_user_service.users set father_name='Arun' where mobile_number='" + mobileNumber + "';","Select * from db_user_service.users where mobile_number='" + mobileNumber + "';");
			click(InstaLoanPage.objApplyBtn, getText(InstaLoanPage.objApplyBtn) + " Button is displayed");

			verifyElementPresent2(InstaLoanOptionalJourney.objFathersNameBasicDetails,"Fathers Name Basic Details Page");
			logger.info("TC_Insta_loan_50 - To verifiy Basic deatils page should be skipped if father name is present");
			extent.extentLoggerPass("TC_Insta_loan_50","To verifiy Basic deatils page should be skipped if father name is present");
			
			verifyElementPresent2(InstaLoanPage.objKYCHeader, "KYC Document Page");
			Back(1);
			waitTime(3000);
			click(InstaLoanOptionalJourney.objYesBtn, "yes Button");
			logger.info("TC_Insta_loan_47 - To verify if KYC is completed, then skip KYC, KYC can be in the onboarding/profile-page");
			extent.extentLoggerPass("TC_Insta_loan_47","To verify if KYC is completed, then skip KYC, KYC can be in the onboarding/profile-page");
			
			waitTime(5000);
			verifyElementPresentAndClick(InstaLoanOptionalJourney.objMoreBottomTab,getText(InstaLoanOptionalJourney.objMoreBottomTab));
			verifyElementPresentAndClick(InstaLoanOptionalJourney.objBankTransfer,getText(InstaLoanOptionalJourney.objBankTransfer));
			explicitWaitVisibilityNonDefault(InstaLoanOptionalJourney.objExistingBankAccount, 20);
			boolean addBankHeader1 = verifyElementPresent(InstaLoanOptionalJourney.objExistingBankAccount,getText(InstaLoanOptionalJourney.objExistingBankAccount));
			Back(1);
			verifyElementPresentAndClick(InstaLoanOptionalJourney.objBBPSFlowElectricity,getText(InstaLoanOptionalJourney.objBBPSFlowElectricity));
			boolean selectBillerBBPS = verifyElementPresent(InstaLoanOptionalJourney.objSelectBiller,getText(InstaLoanOptionalJourney.objSelectBiller));
			Back(2);
			boolean qrScanBtn = verifyElementPresent(InstaLoanOptionalJourney.objQRScanner, "QR Code Scann Button");
			if (addBankHeader1 && selectBillerBBPS && qrScanBtn == true) {
				logger.info("TC_Insta_loan_45 - To verify when an optional user applies for an offer but has not completed documentation, all transaction instruments like Scan & pay, Card, BBPS and Bank transfer will be enabled until the insta loan journey is completed.");
				extent.extentLoggerPass("TC_Insta_loan_45","To verify when an optional user applies for an offer but has not completed documentation, all transaction instruments like Scan & pay, Card, BBPS and Bank transfer will be enabled until the insta loan journey is completed.");
			}

			getDriver().resetApp();
			//instalonAPIKycVerification();
			loginOnboarding("1");
			mobileNoValidation1(mobileNumber);
			enterOtp(prop.getproperty("OTP"));
			readAndAccept();
			waitTime(30000);
			instaUserDetails(firstName, lastName, mothersName, email, gender);
			instalonAPIKycVerification(mobileNumber);
			instaNewCommunicationAddress();
			waitTime(5000);
			instaPanCardDetails(prop.getproperty("RingAdminEmail"), prop.getproperty("RingAdminPassword"),prop.getproperty("RingAdminOTP"), "OPTIONAL", "13000", "2311", "2319");
			panCardDetails();
			waitTime(5000);
			instaLoancongratsScreen("Congrats");
			instaLoanSetPin(prop.getproperty("InstaLoanMPIN"), prop.getproperty("InstaLoanMPIN"));
			waitTime(5000);
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			verifyElementPresentAndClick(InstaLoanPage.instaPopupBannerCrossBtn, "Cross Button");
			click(InstaLoanPage.objOkayGotIt, "Okay, Got It!");
			Swipe("up", 2);
			click(InstaLoanPage.objApplyBtn, getText(InstaLoanPage.objApplyBtn) + " Button is displayed");
			boolean basicDetailsPageSecondTime = verifyElementPresent2(InstaLoanOptionalJourney.objBasicDetailsPage,"Basic Details Page");
			List<WebElement> toatalField = getDriver().findElements(InstaLoanOptionalJourney.objOnlyFathersNameField);
			for (WebElement web : toatalField) {
				String name = web.getText();
				logger.info(name);
				extent.extentLoggerPass("Name", name);
				if (web.getText().equals("Father Name") && basicDetailsPageSecondTime == true) {
					logger.info("TC_Insta_loan_51 - To verify basic detail page");
					extent.extentLoggerPass("TC_Insta_loan_51", "To verify basic detail page");
				}
			}
			waitTime(10000);
			click(InstaLoanPage.objFatherName, "Father Name Field");
			type(InstaLoanPage.objFatherName, "Arun", "Father Name Field");
			verifyElementPresentAndClick(InstaLoanPage.objProceedBtn, "Proceed Button");
			waitTime(10000);
			boolean checkBureauHyperlink = verifyElementPresent(InstaLoanOptionalJourney.objIallowCreditBureauChecksText,getText(InstaLoanOptionalJourney.objIallowCreditBureauChecksText));
			boolean ihearByConfirm = verifyElementPresent(InstaLoanOptionalJourney.objIhearbyConfirmText,getText(InstaLoanOptionalJourney.objIhearbyConfirmText));
			if (checkBureauHyperlink && ihearByConfirm == true) {
				logger.info("TC_Insta_loan_53 - To verify personal detail page should be skipped ");
				extent.extentLoggerPass("TC_Insta_loan_53", "To verify personal detail page should be skipped");
			}

			tapUsingCoordinates(385, 1061);
			verifyElementPresent(InstaLoanOptionalJourney.objCreditBureauConsentPopup,getText(InstaLoanOptionalJourney.objCreditBureauConsentPopup));
			logger.info("TC_Insta_loan_54 - To Verify If the customer clicks on “Credit bureau checks” hyperlink");
			extent.extentLoggerPass("TC_Insta_loan_54","TC_Insta_loan_54 - To Verify If the customer clicks on Credit bureau checks hyperlink");
			verifyElementPresentAndClick(InstaLoanOptionalJourney.objOkGotItCreditBureaupopup,getText(InstaLoanOptionalJourney.objOkGotItCreditBureaupopup));
			
			waitTime(5000);
			tapUsingCoordinates(178, 1102);
			verifyElementPresent(InstaLoanOptionalJourney.objCKYCConsentHyperlink,getText(InstaLoanOptionalJourney.objCKYCConsentHyperlink));
			logger.info("TC_Insta_loan_55 - To Verify If the customer clicks on “Credit bureau checks” hyperlink");
			extent.extentLoggerPass("TC_Insta_loan_55","TC_Insta_loan_55 - To Verify If the customer clicks on “Credit bureau checks” hyperlink");
			verifyElementPresentAndClick(InstaLoanOptionalJourney.objOkGotItCreditBureaupopup,getText(InstaLoanOptionalJourney.objOkGotItCreditBureaupopup));

			click(InstaLoanOptionalJourney.objIallowCheckBox, "I allow Ring to run checkBox");
			click(InstaLoanOptionalJourney.objReadmoreCheckBox,"I, do hearby confirm that my annual household income checkBox");
			verifyElementPresentAndClick(InstaLoanPage.objProceedBtn, "Proceed Button");
			String text = getText(InstaLoanOptionalJourney.objScanAndPayToastMsg);
			if (text.equals("Please select the checkbox for relevant authorizations")) {
				logger.info("TC_Insta_loan_56 - To Verify If the customer unchecks any one or both check boxes for consent and clicks on proceed");
				extent.extentLoggerPass("TC_Insta_loan_56","To Verify If the customer unchecks any one or both check boxes for consent and clicks on proceed");
			}
			click(InstaLoanOptionalJourney.objIallowCheckBox, "I allow Ring to run checkBox");
			click(InstaLoanOptionalJourney.objReadmoreCheckBox,"I, do hearby confirm that my annual household income checkBox");
			verifyElementPresentAndClick(InstaLoanPage.objProceedBtn, "Proceed Button");

			verifyElementPresent(InstaLoanOptionalJourney.objSelectAddressDropDown,getText(InstaLoanOptionalJourney.objSelectAddressDropDown));
			verifyElementPresent(InstaLoanOptionalJourney.objIdeclaretheAboveAddressCheckBox,"I declare the above address is same as my communication address");

			click(InstaLoanOptionalJourney.objIdeclaretheAboveAddressCheckBox,"I declare the above address is same as my communication address checkBox");
			waitTime(5000);
			isClickable(InstaLoanOptionalJourney.objAddCommunicationAddress);

			logger.info("TC_Insta_loan_58 - To Verify user if check box is not ticked user should able to  click on add communication address link");
			extent.extentLoggerPass("TC_Insta_loan_58","To Verify user if check box is not ticked user should able to  click on add communication address link");

			click(InstaLoanOptionalJourney.objAddCommunicationAddress,getText(InstaLoanOptionalJourney.objAddCommunicationAddress));
			instaNewCommunicationAddress();
			verifyElementPresent2(InstaLoanPage.objOfferHeader, "Offer Page");
			logger.info("TC_Insta_loan_59 - User should able to add communication address");
			extent.extentLoggerPass("TC_Insta_loan_59", "User should able to add communication address");

			offerPage();
			click(InstaLoanPage.objAcceptCheckBox, getText(InstaLoanPage.objAcceptCheckBox));
			verifyElementPresentAndClick(InstaLoanPage.objAcceptOffer, getText(InstaLoanPage.objAcceptOffer));
			verifyElementPresentAndClick(InstaLoanPage.objConfirmOfferBtn, getText(InstaLoanPage.objConfirmOfferBtn));
			waitTime(100000);
			verifyElementPresentAndClick(InstaLoanPage.objHomeBtn, getText(InstaLoanPage.objHomeBtn) + " Button");
			waitTime(40000);

			explicitWaitVisibilityNonDefault(InstaLoanPage.objInstaLoanAmountApproved, 10);
			verifyElementPresentAndClick(InstaLoanPage.objViewDetails, getText(InstaLoanPage.objViewDetails) + " Button");
			click(InstaLoanPage.objPayFullAmountRadioBtn, "Pay Full Amount RadioButton");
			verifyElementPresentAndClick(InstaLoanPage.objPayNowBtn, getText(InstaLoanPage.objPayNowBtn) + " Button");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objNetBankingDebitCard, 20);
			verifyElementPresentAndClick(InstaLoanPage.objNetBankingDebitCard,getText(InstaLoanPage.objNetBankingDebitCard) + " Chip");
			waitTime(20000);
			verifyElementPresentAndClick(InstaLoanPage.objCard, "Card chip");
			addNewCard();

			Swipe("up", 2);
			click(InstaLoanPage.objApplyNowBtn, getText(InstaLoanPage.objApplyNowBtn) + " Button is displayed");
			verifyElementPresentAndClick(InstaLoanPage.objSelectAddress, "Select Address Dropdown");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objAddressSelected, 30);
			verifyElementPresentAndClick(InstaLoanPage.objAddressSelected, getText(InstaLoanPage.objAddressSelected));
			verifyElementPresentAndClick(RingUserDetailPage.objRegisterBtn, "Proceed Button");
			verifyElementPresent2(InstaLoanPage.objOfferHeader, "Offer Page");
			logger.info("TC_Insta_loan_57 - To Verify Address page");
			extent.extentLoggerPass("TC_Insta_loan_57", "To Verify Address page");

			System.out.println("--------------------------15 days loans show a second EMI as waived off EMI----------------------");
			mockUserAPI();
			getDriver().resetApp();
			loginOnboarding("1");
			mobileNoValidation1(mobileNumber);
			enterOtp(prop.getproperty("OTP"));
			readAndAccept();
			waitTime(30000);
			instaUserDetails(firstName, lastName, mothersName, email, gender);
			instaNewCommunicationAddress();
			waitTime(5000);
			instaPanCardDetails(prop.getproperty("RingAdminEmail"), prop.getproperty("RingAdminPassword"),prop.getproperty("RingAdminOTP"), "OPTIONAL", "13000", "2135", "2319");
			panCardDetails();
			waitTime(5000);
			instaLoancongratsScreen("Congrats");
			instaLoanSetPin(prop.getproperty("InstaLoanMPIN"), prop.getproperty("InstaLoanMPIN"));
			waitTime(5000);
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			verifyElementPresentAndClick(InstaLoanPage.instaPopupBannerCrossBtn, "Cross Button");
			click(InstaLoanPage.objOkayGotIt, "Okay, Got It!");
			
			Swipe("up", 2);
			click(InstaLoanPage.objApplyBtn, getText(InstaLoanPage.objApplyBtn) + " Button is displayed");
			waitTime(10000);

			basicDetails();
			click(InstaLoanPage.objAcceptCheckBox, getText(InstaLoanPage.objAcceptCheckBox));
			verifyElementPresentAndClick(InstaLoanPage.objAcceptOffer, getText(InstaLoanPage.objAcceptOffer));
			verifyElementPresentAndClick(InstaLoanPage.objConfirmOfferBtn, getText(InstaLoanPage.objConfirmOfferBtn));
			waitTime(90000);
			verifyElementPresentAndClick(InstaLoanPage.objHomeBtn, getText(InstaLoanPage.objHomeBtn) + " Button");
			waitTime(30000);
			verifyElementPresentAndClick(InstaLoanPage.objViewDetails, getText(InstaLoanPage.objViewDetails) + " Button");

			boolean FirstEMIWaiver15Days = verifyElementPresent(InstaLoanOptionalJourney.objFirstEMIDueIN15Days,getText(InstaLoanOptionalJourney.objFirstEMIDueIN15Days) + " 15 Days");
			boolean SecondInstallmentWaiver = verifyElementPresent(InstaLoanOptionalJourney.objSecondInstallmentWaiver,getText(InstaLoanOptionalJourney.objSecondInstallmentWaiver));
			boolean WaiverMsg = verifyElementPresent(InstaLoanOptionalJourney.objWaiverMsg,getText(InstaLoanOptionalJourney.objWaiverMsg));
			if (FirstEMIWaiver15Days && SecondInstallmentWaiver && WaiverMsg == true) {
				logger.info("TC_Insta_loan_60 - To verify  for 15 days loans show a second EMI as waived off EMI");
				extent.extentLoggerPass("TC_Insta_loan_60","TC_Insta_loan_60 - To verify  for 15 days loans show a second EMI as waived off EMI");

				logger.info("TC_Insta_loan_93 - To verify  for 15 days loans show a second EMI as waived off EMI");
				extent.extentLoggerPass("TC_Insta_loan_93","TC_Insta_loan_93 - To verify  for 15 days loans show a second EMI as waived off EMI");

				logger.info("TC_Insta_loan_160 - To verify  for 15 days loans show a second EMI as waived off EMI");
				extent.extentLoggerPass("TC_Insta_loan_160","TC_Insta_loan_160 -To verify  for 15 days loans show a second EMI as waived off EMI");

				logger.info("TC_Insta_loan_164 - To verify for 15 days loans show a second EMI as waived off EMI");
				extent.extentLoggerPass("TC_Insta_loan_164","TC_Insta_loan_164 - To verify for 15 days loans show a second EMI as waived off EMI");
			}

			System.out.println("------------------------------62 days loan EMI--------------------------");
			getDriver().resetApp();
			//instalonAPIKycVerification();
			loginOnboarding("1");
			mobileNoValidation1(mobileNumber);
			enterOtp(prop.getproperty("OTP"));
			readAndAccept();
			waitTime(30000);
			instaUserDetails(firstName, lastName, mothersName, email, gender);
			instalonAPIKycVerification(mobileNumber);
			instaNewCommunicationAddress();
			waitTime(5000);
			instaPanCardDetails(prop.getproperty("RingAdminEmail"), prop.getproperty("RingAdminPassword"),prop.getproperty("RingAdminOTP"), "OPTIONAL", "13000", "2251", "2319");
			panCardDetails();
			waitTime(5000);
			instaLoancongratsScreen("Congrats");
			instaLoanSetPin(prop.getproperty("InstaLoanMPIN"), prop.getproperty("InstaLoanMPIN"));
			waitTime(5000);
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			verifyElementPresentAndClick(InstaLoanPage.instaPopupBannerCrossBtn, "Cross Button");
			click(InstaLoanPage.objOkayGotIt, "Okay, Got It!");
			
			Swipe("up", 2);
			click(InstaLoanPage.objApplyBtn, getText(InstaLoanPage.objApplyBtn) + " Button is displayed");
			waitTime(10000);

			basicDetails();
			click(InstaLoanPage.objAcceptCheckBox, getText(InstaLoanPage.objAcceptCheckBox));
			verifyElementPresentAndClick(InstaLoanPage.objAcceptOffer, getText(InstaLoanPage.objAcceptOffer));
			verifyElementPresentAndClick(InstaLoanPage.objConfirmOfferBtn, getText(InstaLoanPage.objConfirmOfferBtn));
			waitTime(90000);
			verifyElementPresentAndClick(InstaLoanPage.objHomeBtn, getText(InstaLoanPage.objHomeBtn) + " Button");
			if (!verifyElementPresent2(InstaLoanOptionalJourney.objPayNowBtnAtHomePage, "Pay Now Button")) {
				waitTime(12000);
				verifyElementPresentAndClick(InstaLoanOptionalJourney.objUPITab, "UPI Tab");
				waitTime(9000);
				Back(1);
			}
			int EMIDueDatesInInteger = 0;
			int count = 1;
			for (int i = 0; i < count; i++) {
				if (verifyElementPresent2(InstaLoanPage.objViewDetails, "View Details")) {
					verifyElementPresentAndClick(InstaLoanPage.objViewDetails,getText(InstaLoanPage.objViewDetails) + " Button");
					String EMIDueDates = getText(InstaLoanOptionalJourney.objEMIDueDays);
					EMIDueDatesInInteger = convertToInt(EMIDueDates);
					if (EMIDueDatesInInteger == 62) {
						List<WebElement> installmentDisbursed = getDriver().findElements(InstaLoanOptionalJourney.objInstallment);
						for (WebElement web : installmentDisbursed) {
							String installment = web.getText();
							if (installment.equalsIgnoreCase("2nd installment")) {
								verifyElementPresent(InstaLoanOptionalJourney.objEMIDueDays,getText(InstaLoanOptionalJourney.objEMIDueDays));
								verifyElementPresent(InstaLoanOptionalJourney.objTenureDates,getText(InstaLoanOptionalJourney.objTenureDates));
								verifyElementPresent(InstaLoanOptionalJourney.objAmtToBePaid,getText(InstaLoanOptionalJourney.objAmtToBePaid));
								logger.info("TC_Insta_loan_62 - To verify  for 62 days loan EMI");
								extent.extentLoggerPass("TC_Insta_loan_62","TC_Insta_loan_62 - To verify  for 62 days loan EMI");

								logger.info("TC_Insta_loan_95 - To verify  for 62 days loan EMI");
								extent.extentLoggerPass("TC_Insta_loan_95","TC_Insta_loan_95 - To verify  for 62 days loan EMI");

								logger.info("TC_Insta_loan_161 - To verify  for 62 days loan EMI");
								extent.extentLoggerPass("TC_Insta_loan_161","TC_Insta_loan_161 - To verify  for 62 days loan EMI");
							}
						}
					}
				}
				verifyElementPresentAndClick(InstaLoanOptionalJourney.objDayEMI,getText(InstaLoanOptionalJourney.objDayEMI));
				explicitWaitVisibilityNonDefault(InstaLoanPage.objNetBankingDebitCard, 20);
				verifyElementPresentAndClick(InstaLoanPage.objNetBankingDebitCard,getText(InstaLoanPage.objNetBankingDebitCard) + " Chip");
				waitTime(20000);
				verifyElementPresentAndClick(InstaLoanPage.objCard, "Card chip");
				addNewCard();

				if (verifyElementPresent2(InstaLoanPage.objViewDetails, "View Details")) {
					count++;
				} else {
					break;
				}
			}

			System.out.println("------------------------------3 months loan EMI--------------------------");
			getDriver().resetApp();
			//instalonAPIKycVerification();
			loginOnboarding("1");
			mobileNoValidation1(mobileNumber);
			enterOtp(prop.getproperty("OTP"));
			readAndAccept();
			waitTime(30000);
			instaUserDetails(firstName, lastName, mothersName, email, gender);
			instalonAPIKycVerification(mobileNumber);
			instaNewCommunicationAddress();
			waitTime(5000);
			instaPanCardDetails(prop.getproperty("RingAdminEmail"), prop.getproperty("RingAdminPassword"),prop.getproperty("RingAdminOTP"), "OPTIONAL", "13000", "2412", "2319");
			panCardDetails();
			waitTime(5000);
			instaLoancongratsScreen("Congrats");
			instaLoanSetPin(prop.getproperty("InstaLoanMPIN"), prop.getproperty("InstaLoanMPIN"));
			waitTime(5000);
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			verifyElementPresentAndClick(InstaLoanPage.instaPopupBannerCrossBtn, "Cross Button");
			click(InstaLoanPage.objOkayGotIt, "Okay, Got It!");
			Swipe("up", 2);
			click(InstaLoanPage.objApplyBtn, getText(InstaLoanPage.objApplyBtn) + " Button is displayed");
			waitTime(10000);

			basicDetails();
			click(InstaLoanPage.objAcceptCheckBox, getText(InstaLoanPage.objAcceptCheckBox));
			verifyElementPresentAndClick(InstaLoanPage.objAcceptOffer, getText(InstaLoanPage.objAcceptOffer));
			verifyElementPresentAndClick(InstaLoanPage.objConfirmOfferBtn, getText(InstaLoanPage.objConfirmOfferBtn));
			waitTime(90000);
			verifyElementPresentAndClick(InstaLoanPage.objHomeBtn, getText(InstaLoanPage.objHomeBtn) + " Button");
			waitTime(90000);
			int EMIDueDatesInInteger1 = 0;
			int count3MonthEMI = 1;
			for (int i = 0; i < count3MonthEMI; i++) {
				if (verifyElementPresent2(InstaLoanPage.objViewDetails, "View Details")) {
					verifyElementPresentAndClick(InstaLoanPage.objViewDetails,getText(InstaLoanPage.objViewDetails) + " Button");
					String EMIDueDates = getText(InstaLoanOptionalJourney.objEMIDueDays);
					EMIDueDatesInInteger1 = convertToInt(EMIDueDates);
					if (EMIDueDatesInInteger1 == 90) {
						List<WebElement> installmentDisbursed = getDriver().findElements(InstaLoanOptionalJourney.objInstallment);
						for (WebElement web : installmentDisbursed) {
							String installment = web.getText();
							if (installment.equalsIgnoreCase("3rd installment")) {
								verifyElementPresent(InstaLoanOptionalJourney.objEMIDueDays,getText(InstaLoanOptionalJourney.objEMIDueDays));
								verifyElementPresent(InstaLoanOptionalJourney.objTenureDates,getText(InstaLoanOptionalJourney.objTenureDates));
								verifyElementPresent(InstaLoanOptionalJourney.objAmtToBePaid,getText(InstaLoanOptionalJourney.objAmtToBePaid));
								logger.info("TC_Insta_loan_63 - To verify  for 3 months loan EMI");
								extent.extentLoggerPass("TC_Insta_loan_63","TC_Insta_loan_63 - To verify  for 3 months loan EMI");

								logger.info("TC_Insta_loan_96 - To verify  for 3 months loan EMI");
								extent.extentLoggerPass("TC_Insta_loan_96","TC_Insta_loan_96 - To verify  for 3 months loan EMI");

								logger.info("TC_Insta_loan_162 - To verify  for 3 months loan EMI");
								extent.extentLoggerPass("TC_Insta_loan_162","TC_Insta_loan_162 - To verify  for 3 months loan EMI");
							}
						}
					}
				}
				verifyElementPresentAndClick(InstaLoanOptionalJourney.objDayEMI,getText(InstaLoanOptionalJourney.objDayEMI));
				explicitWaitVisibilityNonDefault(InstaLoanPage.objNetBankingDebitCard, 20);
				verifyElementPresentAndClick(InstaLoanPage.objNetBankingDebitCard,getText(InstaLoanPage.objNetBankingDebitCard) + " Chip");
				waitTime(20000);
				verifyElementPresentAndClick(InstaLoanPage.objCard, "Card chip");
				addNewCard();
				if (verifyElementPresent2(InstaLoanPage.objViewDetails, "View Details")) {
					count3MonthEMI++;
				} else {
					break;
				}
			}
			executeUpdateFatherName("Update db_txn_service.instaloan_whitelisted_users set eligible_type='MANDATORY' where user_reference_number='"+ userReferenceNo + "';","Select * from db_txn_service.instaloan_whitelisted_users where user_reference_number='" + userReferenceNo+ "';");
			waitTime(5000);
			Swipe("DOWN", 2);

			verifyElementPresent(InstaLoanOptionalJourney.objReApplyInstaLoan,getText(InstaLoanOptionalJourney.objReApplyInstaLoan));
			verifyElementPresentAndClick(InstaLoanPage.objApplyNowBtn, "ReApply Button");

			verifyElementPresentAndClick(InstaLoanPage.objSelectAddress, "Select Address Dropdown");
			waitTime(2000);
			verifyElementPresentAndClick(InstaLoanPage.objAddressSelected, getText(InstaLoanPage.objAddressSelected));
			verifyElementPresentAndClick(RingUserDetailPage.objRegisterBtn, "Proceed Button");

			verifyElementPresent2(InstaLoanPage.objOfferHeader, "Offer Page");
			click(InstaLoanOptionalJourney.objAddBankDetails,getTextVal(InstaLoanOptionalJourney.objAddBankDetails, "Text"));
			verifyElementPresentAndClick(InstaLoanOptionalJourney.objExistingBankContinue,"Existing Bank Account Continue Button");

			click(InstaLoanPage.objAcceptCheckBox, getText(InstaLoanPage.objAcceptCheckBox));
			verifyElementPresentAndClick(InstaLoanPage.objAcceptOffer, getText(InstaLoanPage.objAcceptOffer));
			verifyElementPresentAndClick(InstaLoanPage.objConfirmOfferBtn, getText(InstaLoanPage.objConfirmOfferBtn));
			waitTime(90000);
			verifyElementPresentAndClick(InstaLoanPage.objHomeBtn, getText(InstaLoanPage.objHomeBtn) + " Button");
			waitTime(30000);

			explicitWaitVisibilityNonDefault(InstaLoanPage.objInstaLoanAmountApproved, 10);
			verifyElementPresentAndClick(InstaLoanPage.objViewDetails, getText(InstaLoanPage.objViewDetails) + " Button");
			click(InstaLoanPage.objPayFullAmountRadioBtn, "Pay Full Amount RadioButton");
			verifyElementPresentAndClick(InstaLoanPage.objPayNowBtn, getText(InstaLoanPage.objPayNowBtn) + " Button");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objNetBankingDebitCard, 20);
			verifyElementPresentAndClick(InstaLoanPage.objNetBankingDebitCard,getText(InstaLoanPage.objNetBankingDebitCard) + " Chip");
			waitTime(20000);
			verifyElementPresentAndClick(InstaLoanPage.objCard, "Card chip");
			addNewCard();
			verifyElementPresent(InstaLoanOptionalJourney.objLimitPausedForMandatoryEligiblityType,getText(InstaLoanOptionalJourney.objLimitPausedForMandatoryEligiblityType));
			verifyElementPresentAndClick(InstaLoanOptionalJourney.objQRScanner, "QR Code Scann Button");
			verifyElementPresent(InstaLoanOptionalJourney.objSorryMsgInstaLoanApproved,getText(InstaLoanOptionalJourney.objSorryMsgInstaLoanApproved)+ getText(InstaLoanOptionalJourney.objSorryMsgInstaLoanApprovedCurrentlyUnavailable));
			logger.info("TC_Insta_loan_75 - To verify when the user loan is closed and change the eligible type to mandatory for repeat journey");
			extent.extentLoggerPass("TC_Insta_loan_75","TC_Insta_loan_75 - To verify when the user loan is closed and change the eligible type to mandatory for repeat journey");

			System.out.println("------------------------------6 months loan EMI--------------------------");
			getDriver().resetApp();
			//instalonAPIKycVerification();
			loginOnboarding("1");
			mobileNoValidation1(mobileNumber);
			enterOtp(prop.getproperty("OTP"));
			readAndAccept();
			waitTime(30000);
			instaUserDetails(firstName, lastName, mothersName, email, gender);
			instalonAPIKycVerification(mobileNumber);
			instaNewCommunicationAddress();
			waitTime(5000);
			instaPanCardDetails(prop.getproperty("RingAdminEmail"), prop.getproperty("RingAdminPassword"),prop.getproperty("RingAdminOTP"), "OPTIONAL", "13000", "2319", "2319");
			panCardDetails();
			waitTime(5000);
			instaLoancongratsScreen("Congrats");
			instaLoanSetPin(prop.getproperty("InstaLoanMPIN"), prop.getproperty("InstaLoanMPIN"));
			waitTime(5000);
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			verifyElementPresentAndClick(InstaLoanPage.instaPopupBannerCrossBtn, "Cross Button");
			click(InstaLoanPage.objOkayGotIt, "Okay, Got It!");
			transactionValidation();

			Swipe("up", 2);
			click(InstaLoanPage.objApplyBtn, getText(InstaLoanPage.objApplyBtn) + " Button is displayed");
			waitTime(10000);

			basicDetails();
			waitTime(10000);
			boolean IacceptCheckBoxNotClicked = getDriver().findElement(InstaLoanPage.objAcceptCheckBox).isSelected();
			verifyElementPresentAndClick(InstaLoanPage.objAcceptOffer, getText(InstaLoanPage.objAcceptOffer));
			String toast = getText(InstaLoanOptionalJourney.objScanAndPayToastMsg);
			logger.info(toast);
			extent.extentLogger("Toast Msg", toast);

			click(InstaLoanPage.objAcceptCheckBox, "CheckBox");
			verifyElementPresentAndClick(InstaLoanPage.objAcceptOffer, getText(InstaLoanPage.objAcceptOffer));
			boolean importantPopupMsg = verifyElementPresent(InstaLoanOptionalJourney.objRingLimitpausedPopup,getText(InstaLoanOptionalJourney.objRingLimitpausedPopup));
			boolean ringLimitPausedpopup = verifyElementPresent(InstaLoanOptionalJourney.objRingLimitPausedTextPopup,getText(InstaLoanOptionalJourney.objRingLimitPausedTextPopup));
			boolean confirmOfferBtn = verifyElementPresent(InstaLoanPage.objConfirmOfferBtn,getText(InstaLoanPage.objConfirmOfferBtn));
			verifyElementPresentAndClick(InstaLoanPage.objConfirmOfferBtn, getText(InstaLoanPage.objConfirmOfferBtn));
			if (IacceptCheckBoxNotClicked == false && confirmOfferBtn == true && importantPopupMsg == true && ringLimitPausedpopup == true) {
				logger.info("TC_Insta_loan_65 - To verify terms and conditions data to check");
				extent.extentLoggerPass("TC_Insta_loan_65","TC_Insta_loan_65 - To verify terms and conditions data to check");

				logger.info("TC_Insta_loan_71 - To verify, Pop -up of deactivating Ring limit is shown once user click on 'Accept offer'");
				extent.extentLoggerPass("TC_Insta_loan_71","To verify, Pop -up of deactivating Ring limit is shown once user click on 'Accept offer'");
			}
			waitTime(12000);
			verifyElementPresentAndClick(InstaLoanPage.objHomeBtn, getText(InstaLoanPage.objHomeBtn) + " Button");

			verifyElementPresent(InstaLoanOptionalJourney.objRingLimitPaused,getText(InstaLoanOptionalJourney.objRingLimitPaused));
			String FAStatus = executeQuery3("SELECT * FROM db_txn_service.line_application where user_reference_number='" + userReferenceNo + "';",7);
			System.out.println(FAStatus);
			softAssertion.assertEquals(FAStatus, "FINAL_APPROVED");
			logger.info("TC_Insta_loan_72 - To verify status should be changed to FA when user accept offer");
			extent.extentLoggerPass("TC_Insta_loan_72","TC_Insta_loan_72 - To verify status should be changed to FA when user accept offer");

			boolean inProcessStatus = verifyElementPresent(InstaLoanOptionalJourney.objInProcessStatus,getText(InstaLoanOptionalJourney.objInProcessStatus));
			if (!verifyElementPresent2(InstaLoanOptionalJourney.objPayNowBtnAtHomePage, "Pay Now Button")) {
				waitTime(12000);
				verifyElementPresentAndClick(InstaLoanOptionalJourney.objUPITab, "UPI Tab");
				waitTime(9000);
				Back(1);
			}
			boolean disbursedStatus = verifyElementPresent(InstaLoanOptionalJourney.objDisbursedStatus,getText(InstaLoanOptionalJourney.objDisbursedStatus));
			if (inProcessStatus == true && disbursedStatus == true) {
				logger.info("TC_Insta_loan_73 - To verify different disbursement status are working");
				extent.extentLoggerPass("TC_Insta_loan_73","TC_Insta_loan_73 - To verify different disbursement status are working");
			}

			int EMIDueDatesInInteger2 = 0;
			int count6MonthEMI = 1;
			for (int i = 0; i < count6MonthEMI; i++) {
				if (verifyElementPresent2(InstaLoanPage.objViewDetails, "View Details")) {
					verifyElementPresentAndClick(InstaLoanPage.objViewDetails,getText(InstaLoanPage.objViewDetails) + " Button");
					String EMIDueDates = getText(InstaLoanOptionalJourney.objEMIDueDays);
					EMIDueDatesInInteger2 = convertToInt(EMIDueDates);
					waitTime(3000);
					Swipe("up", 1);
					if (EMIDueDatesInInteger2 == 181) {
						List<WebElement> installmentDisbursed = getDriver().findElements(InstaLoanOptionalJourney.objInstallment);
						for (WebElement web : installmentDisbursed) {
							String installment = web.getText();

							if (installment.equalsIgnoreCase("6th installment")) {
								verifyElementPresent(InstaLoanOptionalJourney.objEMIDueDays,getText(InstaLoanOptionalJourney.objEMIDueDays));
								verifyElementPresent(InstaLoanOptionalJourney.objTenureDates,getText(InstaLoanOptionalJourney.objTenureDates));
								verifyElementPresent(InstaLoanOptionalJourney.objAmtToBePaid,getText(InstaLoanOptionalJourney.objAmtToBePaid));
								logger.info("TC_Insta_loan_64 - To verify  for 6 months loan EMI");
								extent.extentLoggerPass("TC_Insta_loan_64","TC_Insta_loan_64 - To verify  for 6 months loan EMI");

								logger.info("TC_Insta_loan_97 - To verify  for 6 months loan EMI");
								extent.extentLoggerPass("TC_Insta_loan_97","TC_Insta_loan_97 - To verify  for 6 months loan EMI");

								logger.info("TC_Insta_loan_163 - To verify  for 6 months loan EMI");
								extent.extentLoggerPass("TC_Insta_loan_163","TC_Insta_loan_163 - To verify  for 6 months loan EMI");
							}
						}
					}
				}
				verifyElementPresentAndClick(InstaLoanOptionalJourney.objDayEMI,getText(InstaLoanOptionalJourney.objDayEMI));
				explicitWaitVisibilityNonDefault(InstaLoanPage.objNetBankingDebitCard, 20);
				verifyElementPresentAndClick(InstaLoanPage.objNetBankingDebitCard,getText(InstaLoanPage.objNetBankingDebitCard) + " Chip");
				waitTime(20000);
				verifyElementPresentAndClick(InstaLoanPage.objCard, "Card chip");
				addNewCard();
				if (verifyElementPresent2(InstaLoanPage.objViewDetails, "View Details")) {
					count6MonthEMI++;
				} else {
					break;
				}
			}

			verifyElementPresent(InstaLoanOptionalJourney.objAvailableLimit,getText(InstaLoanOptionalJourney.objAvailableLimit));
			logger.info("TC_Insta_loan_74 - After repayment of previous insta loan ring limit should get active");
			extent.extentLoggerPass("TC_Insta_loan_74","TC_Insta_loan_74 - After repayment of previous insta loan ring limit should get active");

			explicitWaitVisibilityNonDefault(HomePage.objHome, 10);
			String sHomeTxt = getText(HomePage.objHome);
			softAssertion.assertEquals(sHomeTxt, "Home");
			logger.info("Navigated to Home Page");
			extent.extentLoggerPass("Home Page", "Navigated to Home Page");
			waitTime(2000);
			Swipe("DOWN", 2);

			verifyElementPresent(InstaLoanOptionalJourney.objReApplyInstaLoan,getText(InstaLoanOptionalJourney.objReApplyInstaLoan));
			logger.info("TC_Insta_loan_76 - To verify when the user repays the loan amount or closed the Loan and Reapply for  insta loan then banner should be visible on the homepage ");
			extent.extentLoggerPass("TC_Insta_loan_76","TC_Insta_loan_76 - To verify when the user repays the loan amount or closed the Loan and Reapply for  insta loan then banner should be visible on the homepage ");

			verifyElementPresentAndClick(InstaLoanPage.objApplyNowBtn, "ReApply Button");

			verifyElementPresentAndClick(InstaLoanPage.objSelectAddress, "Select Address Dropdown");
			verifyElementPresentAndClick(InstaLoanPage.objAddressSelected, getText(InstaLoanPage.objAddressSelected));
			verifyElementPresentAndClick(RingUserDetailPage.objRegisterBtn, "Proceed Button");

			verifyElementPresent2(InstaLoanPage.objOfferHeader, "Offer Page");
			if (verifyElementPresent(InstaLoanPage.objAddBankButton, getText(InstaLoanPage.objAddBankButton))) {
				click(InstaLoanOptionalJourney.objAddBankDetails,getTextVal(InstaLoanOptionalJourney.objAddBankDetails, "Text"));
				if (verifyElementPresent(InstaLoanOptionalJourney.objExistingBankAcc,getTextVal(InstaLoanOptionalJourney.objExistingBankAcc, "Text"))) {
					logger.info("TC_Insta_loan_68 - Existing Bank Accounts are displayed After clicking on Add Bank Details");
					extent.extentLoggerPass("TC_Insta_loan_68","TC_Insta_loan_68 -  Existing Bank Accounts are displayed After clicking on Add Bank Details");
					System.out.println("-----------------------------------------------------------");

					verifyElementPresent(InstaLoanOptionalJourney.objApproved,getTextVal(InstaLoanOptionalJourney.objApproved, "Text"));
					logger.info("TC_Insta_loan_69 - Bank should be added on bank name match percent");
					extent.extentLoggerPass("TC_Insta_loan_69","TC_Insta_loan_69, Bank should be added on bank name match percent");
					System.out.println("-----------------------------------------------------------");
				}
			}
			verifyElementPresentAndClick(InstaLoanOptionalJourney.objAddNewBankAccount,getText(InstaLoanOptionalJourney.objAddNewBankAccount) + " Button");
			addbankAccount("6" + RandomIntegerGenerator(4), firstName + " " + lastName);
			explicitWaitVisibilityNonDefault(BankTransferModule.objBottomSheetPopup, 10);
			verifyElementExist(BankTransferModule.objBottomSheetPopup, "Verify Bank Details Pop Up");
			click(BankTransferModule.objConfirmBtn, "Confirm Button");
			if (verifyElementPresent(InstaLoanOptionalJourney.objUnableToAddAcc,getTextVal(InstaLoanOptionalJourney.objUnableToAddAcc, "Text"))) {
				click(InstaLoanOptionalJourney.objOkGotIt2, getTextVal(InstaLoanOptionalJourney.objOkGotIt2, "Text"));
			}
			Swipe("up", 2);
			verifyElementPresentAndClick(InstaLoanOptionalJourney.objContinue1, "Continue Button");
			verifyElementPresentAndClick(InstaLoanOptionalJourney.objAddBankDetails,getTextVal(InstaLoanOptionalJourney.objAddBankDetails, "Text"));

			verifyElementPresent(InstaLoanOptionalJourney.objExistingBankAcc,getTextVal(InstaLoanOptionalJourney.objExistingBankAcc, "Text"));
			verifyElementPresent(InstaLoanOptionalJourney.objRejected,getTextVal(InstaLoanOptionalJourney.objRejected, "Status of Accunt"));
			logger.info("TC_Insta_loan_70 - Bank should be reject on bank name match percent");
			extent.extentLoggerPass("TC_Insta_loan_70","TC_Insta_loan_70, Bank should be reject on bank name match percent");
			System.out.println("-----------------------------------------------------------");

		}

		public void instaLoanOnHold() throws Exception {
			extent.HeaderChildNode("Insta Loan OnHold Status");
			//instalonAPIKycVerification();
			loginOnboarding("1");
			mobileNoValidation1(mobileNumber);
			enterOtp(prop.getproperty("OTP"));
			readAndAccept();
			waitTime(30000);
			instaUserDetails(firstName, lastName, mothersName, email, gender);
			instalonAPIKycVerification(mobileNumber);
			instaNewCommunicationAddress();
			waitTime(5000);
			instaPanCardDetails(prop.getproperty("RingAdminEmail"), prop.getproperty("RingAdminPassword"),prop.getproperty("RingAdminOTP"), "MANDATORY", "13000", "2311", "2319");
			panCardDetails();
			waitTime(5000);
			instaLoancongratsScreen("Congrats");
			instaLoanSetPin(prop.getproperty("InstaLoanMPIN"), prop.getproperty("InstaLoanMPIN"));
			waitTime(5000);
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			waitTime(2000);
			instaLoanCoachPopup();
			Swipe("up", 2);
			click(InstaLoanPage.objApplyBtn, getText(InstaLoanPage.objApplyBtn) + " Button is displayed");

			waitTime(5000);
			click(InstaLoanPage.objFatherName, "Father Name Field");
			type(InstaLoanPage.objFatherName, "Arun", "Father Name Field");
			waitTime(5000);
			verifyElementPresentAndClick(InstaLoanPage.objProceedBtn, "Proceed Button");
			click(InstaLoanPage.objProceedBtn, "Proceed Button");
			verifyElementPresentAndClick(InstaLoanPage.objProceedBtn, "Proceed Button");
			verifyElementPresentAndClick(InstaLoanPage.objProceedBtn, "Proceed Button");
			waitTime(10000);
			verifyElementPresentAndClick(InstaLoanPage.objSelectAddress, "Select Address Dropdown");
			verifyElementPresentAndClick(InstaLoanPage.objAddressSelected, getText(InstaLoanPage.objAddressSelected));
			verifyElementPresentAndClick(RingUserDetailPage.objRegisterBtn, "Proceed Button");
			verifyElementPresent(InstaLoanPage.objOfferHeader, getText(InstaLoanPage.objOfferHeader) + "Header");
			verifyElementPresent(InstaLoanPage.objEligible,getText(InstaLoanPage.objEligible) + getText(InstaLoanPage.objEligibleInstaLoanAmt));
			verifyElementPresentAndClick(InstaLoanPage.objAddBankButton, getText(InstaLoanPage.objAddBankButton));
			String BankAccount = addbankAccount("5" + RandomIntegerGenerator(4), firstName + " " + lastName);
			updateBankAccStatus("ONHOLD", BankAccount, userReferenceNo);

			String cond_ApprovedStatus = executeQuery3("SELECT * FROM db_tradofina.user_attributes where user_reference_number='" + userReferenceNo + "';",51);
			System.out.println(cond_ApprovedStatus);
			softAssertion.assertEquals(cond_ApprovedStatus, "COND_APPROVED");
			logger.info("TC_Insta_loan_67 - To verify transaction will be CA before loading offer page");
			extent.extentLoggerPass("TC_Insta_loan_67","TC_Insta_loan_67 - To verify transaction will be CA before loading offer page");

			verifyElementPresentAndClick(InstaLoanPage.objAcceptOffer, getText(InstaLoanPage.objAcceptOffer));
			click(InstaLoanPage.objAcceptCheckBox, "CheckBox");
			verifyElementPresentAndClick(InstaLoanPage.objAcceptOffer, getText(InstaLoanPage.objAcceptOffer));
			waitTime(12000);
			verifyElementPresentAndClick(InstaLoanPage.objHomeBtn, getText(InstaLoanPage.objHomeBtn) + " Button");
			if (!verifyElementPresent2(InstaLoanOptionalJourney.objPayNowBtnAtHomePage, "Pay Now Button")) {
				waitTime(12000);
				verifyElementPresentAndClick(InstaLoanOptionalJourney.objUPITab, "UPI Tab");
				waitTime(9000);
				Back(1);
			}
			if (verifyElementPresent(InstaLoanHomePage.objOnHold, "On Hold Status")) {
				verifyElementPresentAndClick(InstaLoanHomePage.objViewDetails,getTextVal(InstaLoanHomePage.objViewDetails, "Link"));
				explicitWaitVisibilityNonDefault(InstaLoanHomePage.objDisbursementOnHold, 10);
				if (verifyElementPresent(InstaLoanHomePage.objDisbursementOnHold,getTextVal(InstaLoanHomePage.objDisbursementOnHold, "Text"))) {
					verifyElementPresent(InstaLoanViewDetailsPage.objDisbursementStructure,getTextVal(InstaLoanViewDetailsPage.objDisbursementStructure, "Text"));
					verifyElementPresent(InstaLoanViewDetailsPage.objApprovedInstaLoan,getTextVal(InstaLoanViewDetailsPage.objApprovedInstaLoan, "Text"));
					verifyElementPresent(InstaLoanViewDetailsPage.objApprovedInstaLoanAmount,getTextVal(InstaLoanViewDetailsPage.objApprovedInstaLoanAmount, "Text"));
					verifyElementPresent(InstaLoanViewDetailsPage.objProcessingFee,getTextVal(InstaLoanViewDetailsPage.objProcessingFee, "Text"));
					verifyElementPresent(InstaLoanViewDetailsPage.objProcessingFeeAmount,getTextVal(InstaLoanViewDetailsPage.objProcessingFeeAmount, "Amount"));
					verifyElementPresent(InstaLoanViewDetailsPage.objGST,getTextVal(InstaLoanViewDetailsPage.objGST, "Text"));
					verifyElementPresent(InstaLoanViewDetailsPage.objGSTAmount,getTextVal(InstaLoanViewDetailsPage.objGSTAmount, "Amount"));
					verifyElementPresent(InstaLoanViewDetailsPage.objDisbursementAmountTxt,getTextVal(InstaLoanViewDetailsPage.objDisbursementAmountTxt, "Text"));
					verifyElementPresent(InstaLoanViewDetailsPage.objDisbursementAmount,getTextVal(InstaLoanViewDetailsPage.objDisbursementAmount, "Amount"));
					logger.info("TC_Insta_loan_73, Disbursement On Hold screen is validated");
					extent.extentLoggerPass("TC_Insta_loan_73","TC_Insta_loan_73, Disbursement On Hold screen is validated");
					System.out.println("-----------------------------------------------------------");
					Back(1);
				}
			}
		}

		public void transactionValidation() throws Exception {
			extent.HeaderChildNode("Transaction Validation");
			if (verifyElementPresent(HomePage.objScanAndPay, "Scan and Pay Button")) {
				click(HomePage.objScanAndPay, "Scan and Pay Button");
				verifyElementPresent(HomePage.objScanAnyQRToPay, "Scan Any QR To Pay");

				scannQRSwitch();
				verifyElementPresent(InstaLoanOptionalJourney.objScanAndPayPage,getTextVal(InstaLoanOptionalJourney.objScanAndPayPage, "Text"));
				logger.info("TC_Insta_loan_66 - To verify transaction will be initiated after KYC is completed");
				extent.extentLoggerPass("TC_Insta_loan_66","TC_Insta_loan_66 - To verify transaction will be initiated after KYC is completed");
				System.out.println("-----------------------------------------------------------");

				Back(1);
				waitTime(3000);
				Back(1);
				verifyElementPresentAndClick(InstaLoanOptionalJourney.objYesButton,getTextVal(InstaLoanOptionalJourney.objYesButton, "Text"));
				explicitWaitVisibilityNonDefault(HomePage.objHome, 10);
				String sHome = getText(HomePage.objHome);
				softAssertion.assertEquals(sHome, "Home");
				logger.info("Navigated to Home Page");
			}
		}
//============================================InstaLoan Optional journey End==========================================================================

	public void scannQRSwitch() {
		// Scan here
		/*------------------------------WEB----------------------------*/
		Utilities.setPlatform = "Web";
		new CommandBase("Chrome");
		String projectPath = System.getProperty("user.dir");
		getWebDriver().get(projectPath + "\\Mock_Files\\qrcode.png");
		waitTime(4000);
		BrowsertearDown();

		/*------------------------------Android----------------------------*/
		setPlatform("Android");
		initDriver();
	}

// ---------------------------------Pulkit Code End------------------------------------//
	public void instaLoancongratsScreen(String offer) throws Exception {
		switch (offer) {
		case "Congrats":
			explicitWaitVisibilityNonDefault(RingLoginPage.objCongratsPage, 30);
			verifyElementPresent(RingLoginPage.objCongratsPage, getText(RingLoginPage.objCongratsPage));
			verifyElementPresent(RingLoginPage.objApprovedRingLimit,
					getText(RingLoginPage.objApprovedRingLimit) + getText(RingLoginPage.objApprovedRinglimitAmt));
			verifyElementPresentAndClick(RingLoginPage.objIAcceptCheckBox,
					"I accept the Ring’s Terms & Conditions & IT Act 2000 checkbox");
			verifyElementPresentAndClick(RingLoginPage.objAcceptOfferBtn, getText(RingLoginPage.objAcceptOfferBtn));
			break;
		case "Woohoo":
			verifyElementPresent(PromoCodeOfferPage.objMerchantOfferHeader,
					getText(PromoCodeOfferPage.objMerchantOfferHeader));
			verifyElementPresent(PromoCodeOfferPage.objUnlockRingLimit, getText(PromoCodeOfferPage.objUnlockRingLimit)
					+ " of " + getText(PromoCodeOfferPage.objApprovedRinglimitAmt));
			verifyElementPresent(PromoCodeOfferPage.objPayingType, getText(PromoCodeOfferPage.objPayingType));
			verifyElementPresent(PromoCodeOfferPage.objUPIId, getText(PromoCodeOfferPage.objUPIId));
			verifyElementPresent(PromoCodeOfferPage.objMerchantPayAmt, getText(PromoCodeOfferPage.objMerchantPayAmt));
			verifyElementPresent(PromoCodeOfferPage.objRBIMsg,
					getText(PromoCodeOfferPage.objRBIMsg) + getText(PromoCodeOfferPage.objRBIMg2));
			verifyElementPresentAndClick(RingLoginPage.objIAcceptCheckBox,
					"I accept the Ring’s Terms & Conditions & IT Act 2000 checkbox");
			verifyElementPresentAndClick(TermsAndConditionPage.objAcceptAndPayBtn,
					getText(TermsAndConditionPage.objAcceptText));
			break;
		}
	}

	public String switchAdminPanelForRefNo() throws Exception {
		setPlatform("Web");
		System.out.println("platform changed to web");
		waitTime(5000);
		new RingPayBusinessLogic("ring");
		waitTime(10000);

		getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobileNumber+"&");
		type(InstaLoanPage.objEmail,"shakir.muchumarri@collabera.com","Email text field");
		click(InstaLoanPage.objContinue,"Continue Button");
		waitTime(10000);
		explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
		String userRefNo = getText(InstaLoanPage.objUserRefNo);
		BrowsertearDown();

		setPlatform("Android");
		initDriver();
		waitTime(5000);
		return userRefNo;
	}
//--------------------------------------------Pulkit Code End------------------------------------//
	
//--------------------------------------------TShashi Code Start----------------------------------//
//======================================================HomeScrean Scenario Start==================================================================//

	public void instaLoanWhitelistOnBoarding() throws Exception {
		//instalonAPIKycVerification();		
		loginOnboarding("1");
		mobileNoValidation1(mobileNumber);
		enterOtp(prop.getproperty("OTP"));
		readAndAccept();
		waitTime(30000);
		instaUserDetails(firstName, lastName, mothersName, email, gender);
		instalonAPIKycVerification(mobileNumber);
		instaNewCommunicationAddress();
		waitTime(10000);
		//instaKycDocument();
		instaPanCardDetails(prop.getproperty("RingAdminEmail"), prop.getproperty("RingAdminPassword"), prop.getproperty("RingAdminOTP"), "OPTIONAL", "13000", "2311", "2319");
		panCardDetails();
		waitTime(5000);
		instaLoancongratsScreen("Congrats");
		instaLoanSetPin(prop.getproperty("InstaLoanMPIN"), prop.getproperty("InstaLoanMPIN"));
		waitTime(5000);
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		waitTime(2000);
		instaLoanCoachPopup();
	}

	
	
	public void instaLoanHomeScreenScenarios() throws Exception {
		extent.HeaderChildNode("Insta Loan Approval Journey");
		getDriver().resetApp();
		instaLoanWhitelistOnBoarding();
		Swipe("UP", 2);
		verifyElementPresent(InstaLoanPage.objApplyBtn, getTextVal(InstaLoanPage.objApplyBtn, "Text"));
		click(InstaLoanPage.objApplyBtn, getTextVal(InstaLoanPage.objApplyBtn, "Text"));
		click(InstaLoanPage.objSpouseFatherName, "Spouse / Father Name Field");
		type(InstaLoanPage.objSpouseFatherName, "Arun", "Spouse / Father Name Field");
		click(InstaLoanHomePage.objProceedBtn, getTextVal(InstaLoanHomePage.objProceedBtn, "Text"));
		waitTime(5000);
		click(InstaLoanHomePage.objProceedBtn, getTextVal(InstaLoanHomePage.objProceedBtn, "Text"));
		click(InstaLoanHomePage.objCommunicationAddressBackArrow, "Back Arrow");
		click(InstaLoanPage.objYesBtn, "Yes Button");

		Swipe("UP", 2);
		if (verifyElementPresent(InstaLoanHomePage.objTwoSteps, getTextVal(InstaLoanHomePage.objTwoSteps, "Text"))) {
			verifyElementPresent(InstaLoanHomePage.objMsg1, getTextVal(InstaLoanHomePage.objMsg1, "Text"));
			verifyElementPresent(InstaLoanHomePage.objMsg2, getTextVal(InstaLoanHomePage.objMsg2, "Text"));
			verifyElementPresent(InstaLoanHomePage.objContinue, getTextVal(InstaLoanHomePage.objContinue, "Text"));
			logger.info("TC_Insta_loan_35, Just 2 steps ahead status of Insta Loan is Validated");
			extent.extentLoggerPass("TC_Insta_loan_35", "TC_Insta_loan_35, Just 2 steps ahead status of Insta Loan is Validated");
			System.out.println("-----------------------------------------------------------");
		}
		click(InstaLoanHomePage.objContinue, "Continue Button");
		click(InstaLoanHomePage.objSelectAddress, getTextVal(InstaLoanHomePage.objSelectAddress, "Text"));
		click(InstaLoanHomePage.objAddress, getTextVal(InstaLoanHomePage.objAddress, "Text"));
		click(InstaLoanHomePage.objProceedBtn, getTextVal(InstaLoanHomePage.objProceedBtn, "Text"));
		explicitWaitVisibilityNonDefault(InstaLoanHomePage.objOffer, 10);
		click(InstaLoanHomePage.objOfferBackArrow, "Back Arrow Button");
		click(InstaLoanPage.objYesBtn, getTextVal(InstaLoanPage.objYesBtn, "Text"));
		Swipe("UP", 2);

		if (verifyElementPresent(InstaLoanHomePage.objOneStep, getTextVal(InstaLoanHomePage.objOneStep, "Text"))) {
			verifyElementPresent(InstaLoanHomePage.objMsgg1, getTextVal(InstaLoanHomePage.objMsgg1, "Text"));
			verifyElementPresent(InstaLoanHomePage.objMsgg2, getTextVal(InstaLoanHomePage.objMsgg2, "Text"));
			verifyElementPresent(InstaLoanHomePage.objContinue1, getTextVal(InstaLoanHomePage.objContinue1, "Text"));
			logger.info("TC_Insta_loan_36, Just 1 step ahead status of Insta Loan is Validated");
			extent.extentLoggerPass("TC_Insta_loan_36", "TC_Insta_loan_36, Just 1 step ahead status of Insta Loan is Validated");
			System.out.println("-----------------------------------------------------------");
		}
		click(InstaLoanHomePage.objContinue1, "Continue Button");
		explicitWaitVisibilityNonDefault(InstaLoanHomePage.objOffer, 10);
		if (verifyElementPresent(InstaLoanHomePage.objAddBankDetails, getTextVal(InstaLoanHomePage.objAddBankDetails, "Text"))) {
			click(InstaLoanHomePage.objAddBankDetails, getTextVal(InstaLoanHomePage.objAddBankDetails, "Text"));
		}
		addbankAccount("5" + RandomIntegerGenerator(4), firstName + " " + lastName);

		if (verifyElementPresent(InstaLoanHomePage.objOffer, getTextVal(InstaLoanHomePage.objOffer, "Text"))) {
			verifyElementPresentAndClick(InstaLoanHomePage.objCheckBox, "Check Box");
			verifyElementPresentAndClick(InstaLoanHomePage.objAcceptOffer, getTextVal(InstaLoanHomePage.objAcceptOffer, "Text"));
			verifyElementPresentAndClick(InstaLoanHomePage.objConfirmOffer, getTextVal(InstaLoanHomePage.objConfirmOffer, "Text"));
			waitTime(90000);
		}

		if (verifyElementPresent(InstaLoanHomePage.objInstaLoanDisbursement, "Disbursement Text")){
			verifyElementPresent(InstaLoanHomePage.objInstaLoanAmount, getTextVal(InstaLoanHomePage.objInstaLoanAmount, "Amount"));
			verifyElementPresentAndClick(InstaLoanHomePage.objHome, getTextVal(InstaLoanHomePage.objHome, "Button"));
			waitTime(10000);
		} else {
			logger.info("Insta Loan Disbursement screen is not displayed");
		}
		verifyElementPresentAndClick(InstaLoanPage.objHomeBtn, getText(InstaLoanPage.objHomeBtn) + " Button");
		waitTime(10000);
//
		instaLoanInProcessStatus();
		waitTime(120000);

		if(!verifyElementPresent2(HomePage.objPayNowBtnAtHomePage, "Pay Now Button")) {
			waitTime(120000);
			verifyElementPresentAndClick(HomePage.objUPI, "UPI Tab");
			waitTime(120000);
			Back(1);
		}
		instaLoanDisbursedHomePage();
		payNowInstaLoan();
//		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		waitTime(2000);
		Swipe("DOWN", 2);
		if (verifyElementPresent(InstaLoanHomePage.objReApplyInstaLoan, getTextVal(InstaLoanHomePage.objReApplyInstaLoan, "Text"))) {
			logger.info("TC_Insta_loan_37, Reapply for insta loan banner is Validated");
			extent.extentLoggerPass("TC_Insta_loan_37", "TC_Insta_loan_37, Reapply for insta loan banner is Validated");
			System.out.println("-----------------------------------------------------------");
		}
		boolean sLimitPaused = verifyElementPresent(InstaLoanHomePage.objLimitPaused, getTextVal(InstaLoanHomePage.objLimitPaused, "Text"));
		if (sLimitPaused == true) {
			logger.info("TC_Insta_loan_39, Ring limit doesn't get activated even post full repayment of Instaloan");
			extent.extentLoggerPass("TC_Insta_loan_39", "TC_Insta_loan_39, Ring limit doesn't get activated even post full repayment of Instaloan");
			System.out.println("-----------------------------------------------------------");
		}
		ringPayLogout();

	}
//===============================================instaLoan Disbursed HomePage=================================================================//
	
	public void instaLoanDisbursedHomePage() throws Exception {
		extent.HeaderChildNode("Insta Loan Disbursed Home Page Validation");

		String sDisbursed = getText(InstaLoanHomePage.objDisbursed);
		softAssertion.assertEquals(sDisbursed, "Disbursed");
		if (verifyElementPresent(InstaLoanHomePage.objDisbursed, getTextVal(InstaLoanHomePage.objDisbursed, "Text"))) {
			verifyElementPresent(InstaLoanHomePage.objLimitPaused, getTextVal(InstaLoanHomePage.objLimitPaused, "Text"));
			verifyElementPresent(InstaLoanHomePage.objInstaLoanAmountTxt, getTextVal(InstaLoanHomePage.objInstaLoanAmountTxt, "Text"));
			verifyElementPresent(InstaLoanHomePage.objInstaLoanAmountValue, getTextVal(InstaLoanHomePage.objInstaLoanAmountValue, "Amount"));
			verifyElementPresent(InstaLoanHomePage.objDueDateTxt, getTextVal(InstaLoanHomePage.objDueDateTxt, "Text"));
			verifyElementPresent(InstaLoanHomePage.objDueDate, getTextVal(InstaLoanHomePage.objDueDate, "Date"));
			verifyElementPresent(InstaLoanHomePage.objEmiAmountTxt, getTextVal(InstaLoanHomePage.objEmiAmountTxt, "Text"));
			verifyElementPresent(InstaLoanHomePage.objEmiAmount, getTextVal(InstaLoanHomePage.objEmiAmount, "Amount"));
			isClickable(InstaLoanHomePage.objViewDetails);
			verifyElementPresent(InstaLoanHomePage.objPayNow, getTextVal(InstaLoanHomePage.objPayNow, "Text"));
			logger.info("TC_Insta_loan_19, Loan Banner on Homepage after loan disbursed is Validated");
			extent.extentLoggerPass("TC_Insta_loan_19", "TC_Insta_loan_19, Loan Banner on Homepage after loan disbursed is Validated");
			System.out.println("-----------------------------------------------------------");

			verifyElementPresent(InstaLoanHomePage.objDisbursedLoanBanner, "Disbursed Loan Banner");
			verifyElementPresent(InstaLoanHomePage.objLimitPausedBanner, "Limit Paused Banner");
			verifyElementPresent(InstaLoanHomePage.objLimitPausedMsg, getTextVal(InstaLoanHomePage.objLimitPausedMsg, "Text"));
			verifyElementPresentAndClick(HomePage.objScanAndPay, "Scan button");
			if (verifyElementPresent(InstaLoanHomePage.objLimitPausedPopup, getTextVal(InstaLoanHomePage.objLimitPausedPopup, "Text"))) {
				verifyElementPresentAndClick(InstaLoanHomePage.objOkGotIt, getTextVal(InstaLoanHomePage.objOkGotIt, "Text"));
				explicitWaitVisibilityNonDefault(HomePage.objHome, 10);
				verifyElementPresent(HomePage.objHome, getTextVal(HomePage.objHome, "Text"));
				String sHome = getText(HomePage.objHome);
				softAssertion.assertEquals(sHome, "Home");
				logger.info("Navigated to Home Page");
			}
			logger.info("TC_Insta_loan_20, Homepage validation in Loan Disbursed State");
			extent.extentLoggerPass("TC_Insta_loan_20", "TC_Insta_loan_20, Homepage validation in Loan Disbursed State");
			System.out.println("-----------------------------------------------------------");
		} else {
			logger.info("Loan is not Disbursed");
			extent.extentLoggerPass("Loan Status", "Loan is not Disbursed");
		}

		if (verifyElementPresent(InstaLoanHomePage.objViewDetails, getTextVal(InstaLoanHomePage.objViewDetails, "Text"))) {
			click(InstaLoanHomePage.objViewDetails, "View Details");
			if (verifyElementPresent(InstaLoanViewDetailsPage.objInstaLoan, getTextVal(InstaLoanViewDetailsPage.objInstaLoan, "Text"))) {
				String sInstaLoan = getText(InstaLoanViewDetailsPage.objInstaLoan);
				softAssertion.assertEquals(sInstaLoan, "Insta Loan");
				verifyElementPresent(InstaLoanViewDetailsPage.objFirstEMI, getTextVal(InstaLoanViewDetailsPage.objFirstEMI, "Text"));
				verifyElementPresent(InstaLoanViewDetailsPage.objFirstEMIDueInDays, getTextVal(InstaLoanViewDetailsPage.objFirstEMIDueInDays, "Days"));
				verifyElementPresent(InstaLoanViewDetailsPage.objPayFullAmountTxt, getTextVal(InstaLoanViewDetailsPage.objPayFullAmountTxt, "Text"));
				verifyElementPresent(InstaLoanViewDetailsPage.objPayFullAmountValue, getTextVal(InstaLoanViewDetailsPage.objPayFullAmountValue, "Amount"));
				verifyElementPresent(InstaLoanViewDetailsPage.objFirstInstallment, getTextVal(InstaLoanViewDetailsPage.objFirstInstallment, "Text"));
				verifyElementPresent(InstaLoanViewDetailsPage.objPayBeforeDate, getTextVal(InstaLoanViewDetailsPage.objPayBeforeDate, "date"));
				verifyElementPresent(InstaLoanViewDetailsPage.objFirstEMIAmount, getTextVal(InstaLoanViewDetailsPage.objFirstEMIAmount, "Amount"));
				verifyElementPresent(InstaLoanViewDetailsPage.objPayAmount, getTextVal(InstaLoanViewDetailsPage.objPayAmount, "Text"));
				logger.info("TC_Insta_loan_21, View details page validation");
				extent.extentLoggerPass("TC_Insta_loan_21", "TC_Insta_loan_21, View details page validation");
				System.out.println("-----------------------------------------------------------");
				Back(1);
			}
		}
		if (verifyElementPresent(InstaLoanHomePage.objPayNow, getTextVal(InstaLoanHomePage.objPayNow, "Text"))) {
			click(InstaLoanHomePage.objPayNow, "Pay Now Button");
			if (verifyElementPresent(InstaLoanHomePage.objPayment, getTextVal(InstaLoanHomePage.objPayment, "Text"))) {
				verifyElementPresent(PayEarlyPaymentPage.objAmountDue, getTextVal(PayEarlyPaymentPage.objAmountDue, "Text"));
				verifyElementPresent(PayEarlyPaymentPage.objAmountToBePaid, getTextVal(PayEarlyPaymentPage.objAmountToBePaid, "Text"));
				verifyElementPresent(PayEarlyPaymentPage.objChoosePaymentMethod, getTextVal(PayEarlyPaymentPage.objChoosePaymentMethod, "Text"));
				verifyElementPresent(PayEarlyPaymentPage.objPayViaUPIApps, getTextVal(PayEarlyPaymentPage.objPayViaUPIApps, "Text"));
				verifyElementPresent(PayEarlyPaymentPage.objUPIAppsConvenienceFee, getTextVal(PayEarlyPaymentPage.objUPIAppsConvenienceFee, ""));
				verifyElementPresent(PayEarlyPaymentPage.objAddUPIId, getTextVal(PayEarlyPaymentPage.objAddUPIId, "Text"));
				waitTime(2000);
				Swipe("DOWN", 2);
				verifyElementPresent(PayEarlyPaymentPage.objAddUPIIdConvenienceFee, getTextVal(PayEarlyPaymentPage.objAddUPIIdConvenienceFee, ""));
				verifyElementPresent(PayEarlyPaymentPage.objNetBankingAndDebitCard, getTextVal(PayEarlyPaymentPage.objNetBankingAndDebitCard, "Text"));
				Swipe("UP", 1);
				verifyElementPresent(PayEarlyPaymentPage.objNetBankingAndDebitCardConvenienceFee, getTextVal(PayEarlyPaymentPage.objNetBankingAndDebitCardConvenienceFee, ""));
				logger.info("TC_Insta_loan_22, Pay Now Payment Page validation");
				extent.extentLoggerPass("TC_Insta_loan_22", "TC_Insta_loan_22, Pay Now Payment Page validation");
				System.out.println("-----------------------------------------------------------");
				verifyElementPresentAndClick(PayEarlyPaymentPage.objPaymentBackArrow, "Back Arrow Button");
			}
		}
		if (verifyElementPresent(HomePage.objScanAndPay, "Scan And Pay Button")) {
			click(HomePage.objScanAndPay, "Scan and Pay Button");
			if (verifyElementPresent(InstaLoanHomePage.objLimitPausedPopup, getTextVal(InstaLoanHomePage.objLimitPausedPopup, "Text"))) {
				verifyElementPresentAndClick(InstaLoanHomePage.objOkGotIt, getTextVal(InstaLoanHomePage.objOkGotIt, "Text"));
				explicitWaitVisibilityNonDefault(HomePage.objHome, 10);
				verifyElementPresent(HomePage.objHome, getTextVal(HomePage.objHome, "Text"));
				String sHome = getText(HomePage.objHome);
				softAssertion.assertEquals(sHome, "Home");
				logger.info("Navigated to Home Page");
			}
			logger.info("TC_Insta_loan_23, Scan and Pay Button Validation");
			extent.extentLoggerPass("TC_Insta_loan_23", "TC_Insta_loan_23, Scan and Pay Button Validation");
			System.out.println("-----------------------------------------------------------");
		}
		if (verifyElementPresent(HomePage.objMore, getTextVal(HomePage.objMore, "Text"))) {
			click(HomePage.objMore, "More Button");
			verifyElementPresentAndClick(InstaLoanHomePage.objBankTransfer, getTextVal(InstaLoanHomePage.objBankTransfer, "Text"));
			if (verifyElementPresent(InstaLoanHomePage.objLimitPausedPopup, getTextVal(InstaLoanHomePage.objLimitPausedPopup, "Text"))) {
				verifyElementPresentAndClick(InstaLoanHomePage.objOkGotIt, getTextVal(InstaLoanHomePage.objOkGotIt, "Text"));
			}
			verifyElementPresentAndClick(InstaLoanHomePage.objElectricity, getTextVal(InstaLoanHomePage.objElectricity, "Text"));
			if (verifyElementPresent(InstaLoanHomePage.objLimitPausedPopup, getTextVal(InstaLoanHomePage.objLimitPausedPopup, "Text"))) {
				verifyElementPresentAndClick(InstaLoanHomePage.objOkGotIt, getTextVal(InstaLoanHomePage.objOkGotIt, "Text"));
			}

			logger.info("TC_Insta_loan_24, Limit Paused Banner displayed when click on Bank transfer");
			extent.extentLoggerPass("TC_Insta_loan_24", "TC_Insta_loan_24, Limit Paused Banner displayed when click on Bank transfer");
			System.out.println("-----------------------------------------------------------");
		}
		waitTime(1);
		Back(2);

	}


//==================================================== instaLoan InProcess Status==============================================================//
//========================================================== Backdated =======================================================================//
	public void backDatedScenario() throws Exception {
		extent.HeaderChildNode("Insta Loan Approval Journey");
		getDriver().resetApp();
		waitTime(5000);
		Utilities.setPlatform = "Web";
		new CommandBase("Chrome");
		waitTime(4000);
		getWebDriver().get("https://new-admin-panel.test.ideopay.in/sign-in");

		click(InstaLoanPage.objEmail, "Email Field");
		type(InstaLoanPage.objEmail,(prop.getproperty("RingAdminEmail")),"Email Field");
		verifyElementPresentAndClick(InstaLoanPage.objContinue, "Continue Button");

		verifyElementPresentAndClick(InstaLoanPage.objLoanBtn,"Loan Button");
		selectByValueFromDD(InstaLoanPage.objSelectStatus,"ONGOING");
//		selectByValueFromDD(InstaLoanPage.objSelectDelinquencyStatus,"DELINQUENT");
		selectByValueFromDD(InstaLoanPage.objSearchBy,"mobile_number");
		type(InstaLoanPage.objMobile,(prop.getproperty("InstaLoan_BackDate")),"Mobile Number");
		JSClick(InstaLoanPage.objSearchMobileNo,getTextVal(InstaLoanPage.objSearchMobileNo,"Button"));

		verifyElementPresent(InstaLoanPage.objLoanRefNumber,getTextVal(InstaLoanPage.objLoanRefNumber,"Loan Ref Number"));
		String LoanReferenceNumber = getText(InstaLoanPage.objLoanRefNumber);
		System.out.println("LoanReferenceNumber = " +LoanReferenceNumber);

		String TodayDate = String.valueOf(java.time.LocalDate.now());

		executeUpdateFatherName("UPDATE db_tradofina.repayments SET scheduled_payment_date='" + TodayDate + "'WHERE loan_reference_number='" + LoanReferenceNumber + "' AND month='1';","Select * from db_tradofina.repayments where loan_reference_number='" + LoanReferenceNumber + "' AND month='1';");

		verifyElementPresentAndClick(InstaLoanPage.objLoanRefNumber,getTextVal(InstaLoanPage.objLoanRefNumber,"Loan Ref Number"));

		waitTime(5000);
		verifyElementPresentAndClick(InstaLoanPage.objSyncLoan,"Sync Now Button");
		BrowsertearDown();

		setPlatform("Android");
		initDriver();
		waitTime(5000);
		onBoardingHomePage1(prop.getproperty("InstaLoan_BackDate"));
		explicitWaitVisibilityNonDefault(HomePage.objHome, 20);

		if(verifyElementPresent(InstaLoanHomePage.objDueToday,getTextVal(InstaLoanHomePage.objDueToday,"Status"))){verifyElementPresent(InstaLoanHomePage.objDueDate,getTextVal(InstaLoanHomePage.objDueDate,"Date"));
			verifyElementPresent(InstaLoanHomePage.objEmiAmountTxt,getTextVal(InstaLoanHomePage.objEmiAmountTxt,"Text"));
			verifyElementPresent(InstaLoanHomePage.objEmiAmount,getTextVal(InstaLoanHomePage.objEmiAmount,"Amount"));
			isClickable(InstaLoanHomePage.objViewDetails);
			verifyElementPresentAndClick(InstaLoanHomePage.objViewDetails,getTextVal(InstaLoanHomePage.objViewDetails,"Link"));
			if(verifyElementPresent(InstaLoanHomePage.objRepaymentSchedule,getTextVal(InstaLoanHomePage.objRepaymentSchedule,"Text"))){
				verifyElementPresent(InstaLoanViewDetailsPage.objPayFullAmountTxt,getTextVal(InstaLoanViewDetailsPage.objPayFullAmountTxt,"Text"));
				verifyElementPresent(InstaLoanViewDetailsPage.objPayFullAmountValue,getTextVal(InstaLoanViewDetailsPage.objPayFullAmountValue,"Amount"));
				verifyElementPresent(InstaLoanViewDetailsPage.objFirstInstallment, getTextVal(InstaLoanViewDetailsPage.objFirstInstallment, "Text"));
				verifyElementPresent(InstaLoanViewDetailsPage.objSecondInstallment, getTextVal(InstaLoanViewDetailsPage.objSecondInstallment, "Text"));
				verifyElementPresent(InstaLoanViewDetailsPage.objThirdInstallment, getTextVal(InstaLoanViewDetailsPage.objThirdInstallment, "Text"));
				Swipe("UP", 1);
				waitTime(3000);
				verifyElementPresent(InstaLoanViewDetailsPage.objFourthInstallment, getTextVal(InstaLoanViewDetailsPage.objFourthInstallment, "Text"));
				verifyElementPresent(InstaLoanViewDetailsPage.objFifthInstallment, getTextVal(InstaLoanViewDetailsPage.objFifthInstallment, "Text"));
				verifyElementPresent(InstaLoanViewDetailsPage.objSixthInstallment, getTextVal(InstaLoanViewDetailsPage.objSixthInstallment, "Text"));
				Back(1);
			}
			waitTime(3000);
			isClickable(InstaLoanHomePage.objPayNow);
			if(verifyElementPresent(InstaLoanHomePage.objPayNow,getTextVal(InstaLoanHomePage.objPayNow,"Button"))){
				click(InstaLoanHomePage.objPayNow,getTextVal(InstaLoanHomePage.objPayNow,"Button"));
				verifyElementPresent(InstaLoanHomePage.objPayment,getTextVal(InstaLoanHomePage.objPayment,"Page"));
				Back(1);
			}
			logger.info("TC_Insta_loan_31, Due today Status Validated");
			extent.extentLoggerPass("TC_Insta_loan_31", "TC_Insta_loan_31, Due Today Status Validated");
			System.out.println("-----------------------------------------------------------");

			if (verifyElementPresent(InstaLoanViewDetailsPage.objLimitPaused, getTextVal(InstaLoanViewDetailsPage.objLimitPaused, "Text"))) {
				verifyElementPresent(InstaLoanViewDetailsPage.objLimitPausedMsg, getTextVal(InstaLoanViewDetailsPage.objLimitPausedMsg, "Message"));
				logger.info("TC_Insta_loan_32, Limit Paused text Message is validated");
				extent.extentLoggerPass("TC_Insta_loan_32", "TC_Insta_loan_32, Limit Paused text Message is validated");
				System.out.println("-----------------------------------------------------------");
			}
		}

		waitTime(5000);
		Utilities.setPlatform = "Web";
		new CommandBase("Chrome");
		waitTime(4000);
		getWebDriver().get("https://new-admin-panel.test.ideopay.in/sign-in");

		click(InstaLoanPage.objEmail, "Email Field");
		type(InstaLoanPage.objEmail,(prop.getproperty("RingAdminEmail")),"Email Field");
		verifyElementPresentAndClick(InstaLoanPage.objContinue, "Continue Button");

		verifyElementPresentAndClick(InstaLoanPage.objLoanBtn,"Loan Button");
		selectByValueFromDD(InstaLoanPage.objSelectStatus,"ONGOING");
//		selectByValueFromDD(InstaLoanPage.objSelectDelinquencyStatus,"DELINQUENT");
		selectByValueFromDD(InstaLoanPage.objSearchBy,"mobile_number");
		type(InstaLoanPage.objMobile,(prop.getproperty("InstaLoan_BackDate")),"Mobile Number");
		JSClick(InstaLoanPage.objSearchMobileNo,getTextVal(InstaLoanPage.objSearchMobileNo,"Button"));

		verifyElementPresent(InstaLoanPage.objLoanRefNumber,getTextVal(InstaLoanPage.objLoanRefNumber,"Loan Ref Number"));

		String Date = backDate(4);

		executeUpdateFatherName("UPDATE db_tradofina.repayments SET scheduled_payment_date='" + Date + "'WHERE loan_reference_number='" + LoanReferenceNumber + "' AND month='1';","Select * from db_tradofina.repayments where loan_reference_number='" + LoanReferenceNumber + "' AND month='1';");

		verifyElementPresentAndClick(InstaLoanPage.objLoanRefNumber,getTextVal(InstaLoanPage.objLoanRefNumber,"Loan Ref Number"));

		waitTime(5000);
		verifyElementPresentAndClick(InstaLoanPage.objSyncLoan,"Sync Now Button");
		BrowsertearDown();

		setPlatform("Android");
		initDriver();
		waitTime(5000);

		verifyElementPresentAndClick(HomePage.objUPI,getTextVal(HomePage.objUPI,"Button"));
		verifyElementPresentAndClick(HomePage.objGoToHomePage,getTextVal(HomePage.objGoToHomePage,"Button"));
		verifyElementPresent(InstaLoanHomePage.objOverDueSinceFourDays,getTextVal(InstaLoanHomePage.objOverDueSinceFourDays,"Text"));
		explicitWaitVisibilityNonDefault(HomePage.objHome, 20);

		if(verifyElementPresent(InstaLoanHomePage.objOverDueSinceFourDays,getTextVal(InstaLoanHomePage.objOverDueSinceFourDays,"Status"))){
			isClickable(InstaLoanHomePage.objViewDetails);
			verifyElementPresentAndClick(InstaLoanHomePage.objViewDetails,getTextVal(InstaLoanHomePage.objViewDetails,"Link"));
			if(verifyElementPresent(InstaLoanHomePage.objRepaymentSchedule,getTextVal(InstaLoanHomePage.objRepaymentSchedule,"Text"))){
				verifyElementPresent(InstaLoanViewDetailsPage.objPayFullAmountTxt,getTextVal(InstaLoanViewDetailsPage.objPayFullAmountTxt,"Text"));
				verifyElementPresent(InstaLoanViewDetailsPage.objPayFullAmountValue,getTextVal(InstaLoanViewDetailsPage.objPayFullAmountValue,"Amount"));
				verifyElementPresent(InstaLoanViewDetailsPage.objFirstInstallment, getTextVal(InstaLoanViewDetailsPage.objFirstInstallment, "Text"));
				verifyElementPresent(InstaLoanViewDetailsPage.objSecondInstallment, getTextVal(InstaLoanViewDetailsPage.objSecondInstallment, "Text"));
				verifyElementPresent(InstaLoanViewDetailsPage.objThirdInstallment, getTextVal(InstaLoanViewDetailsPage.objThirdInstallment, "Text"));
				Swipe("UP", 1);
				waitTime(3000);
				verifyElementPresent(InstaLoanViewDetailsPage.objFourthInstallment, getTextVal(InstaLoanViewDetailsPage.objFourthInstallment, "Text"));
				verifyElementPresent(InstaLoanViewDetailsPage.objFifthInstallment, getTextVal(InstaLoanViewDetailsPage.objFifthInstallment, "Text"));
				verifyElementPresent(InstaLoanViewDetailsPage.objSixthInstallment, getTextVal(InstaLoanViewDetailsPage.objSixthInstallment, "Text"));
				Back(1);
			}
			waitTime(3000);
			isClickable(InstaLoanHomePage.objPayNow);
			if(verifyElementPresent(InstaLoanHomePage.objPayNow,getTextVal(InstaLoanHomePage.objPayNow,"Button"))){
				click(InstaLoanHomePage.objPayNow,getTextVal(InstaLoanHomePage.objPayNow,"Button"));
				verifyElementPresent(InstaLoanHomePage.objPayment,getTextVal(InstaLoanHomePage.objPayment,"Page"));
				Back(1);
			}
			logger.info("TC_Insta_loan_33, Overdue since 4 Days Status Validated");
			extent.extentLoggerPass("TC_Insta_loan_33", "TC_Insta_loan_33, Overdue since 4 Days Status Validated");
			System.out.println("-----------------------------------------------------------");

			if (verifyElementPresent(InstaLoanViewDetailsPage.objLimitPaused, getTextVal(InstaLoanViewDetailsPage.objLimitPaused, "Text"))) {
				verifyElementPresent(InstaLoanViewDetailsPage.objLimitPausedMsg, getTextVal(InstaLoanViewDetailsPage.objLimitPausedMsg, "Message"));
				logger.info("TC_Insta_loan_34, Limit Paused text Message is validated");
				extent.extentLoggerPass("TC_Insta_loan_34", "TC_Insta_loan_34, Limit Paused text Message is validated");
				System.out.println("-----------------------------------------------------------");
			}
		}


		
		waitTime(5000);
		Utilities.setPlatform = "Web";
		new CommandBase("Chrome");
		waitTime(4000);
		getWebDriver().get("https://new-admin-panel.test.ideopay.in/sign-in");

		click(InstaLoanPage.objEmail, "Email Field");
		type(InstaLoanPage.objEmail,(prop.getproperty("RingAdminEmail")),"Email Field");
		verifyElementPresentAndClick(InstaLoanPage.objContinue, "Continue Button");

		verifyElementPresentAndClick(InstaLoanPage.objLoanBtn,"Loan Button");
		selectByValueFromDD(InstaLoanPage.objSelectStatus,"ONGOING");
//		selectByValueFromDD(InstaLoanPage.objSelectDelinquencyStatus,"DELINQUENT");
		selectByValueFromDD(InstaLoanPage.objSearchBy,"mobile_number");
		type(InstaLoanPage.objMobile,(prop.getproperty("InstaLoan_BackDate")),"Mobile Number");
		JSClick(InstaLoanPage.objSearchMobileNo,getTextVal(InstaLoanPage.objSearchMobileNo,"Button"));

		verifyElementPresent(InstaLoanPage.objLoanRefNumber,getTextVal(InstaLoanPage.objLoanRefNumber,"Loan Ref Number"));

		String Date2 = backDate(-30);

		executeUpdateFatherName("UPDATE db_tradofina.repayments SET scheduled_payment_date='" + Date2 + "'WHERE loan_reference_number='" + LoanReferenceNumber + "' AND month='1';","Select * from db_tradofina.repayments where loan_reference_number='" + LoanReferenceNumber + "' AND month='1';");

		verifyElementPresentAndClick(InstaLoanPage.objLoanRefNumber,getTextVal(InstaLoanPage.objLoanRefNumber,"Loan Ref Number"));

		waitTime(5000);
		verifyElementPresentAndClick(InstaLoanPage.objSyncLoan,"Sync Now Button");
		BrowsertearDown();

		setPlatform("Android");
		initDriver();
		waitTime(5000);
		verifyElementPresentAndClick(HomePage.objUPI,getTextVal(HomePage.objUPI,"Button"));
		verifyElementPresentAndClick(HomePage.objGoToHomePage,getTextVal(HomePage.objGoToHomePage,"Button"));
		verifyElementPresent(InstaLoanHomePage.objDueIn30Days,getTextVal(InstaLoanHomePage.objDueIn30Days,"Text"));
		explicitWaitVisibilityNonDefault(HomePage.objHome, 20);

		if(verifyElementPresent(InstaLoanHomePage.objDueIn30Days,getTextVal(InstaLoanHomePage.objDueIn30Days,"Status"))){
			isClickable(InstaLoanHomePage.objViewDetails);
			verifyElementPresentAndClick(InstaLoanHomePage.objViewDetails,getTextVal(InstaLoanHomePage.objViewDetails,"Link"));
			if(verifyElementPresent(InstaLoanHomePage.objRepaymentSchedule,getTextVal(InstaLoanHomePage.objRepaymentSchedule,"Text"))){
				verifyElementPresent(InstaLoanViewDetailsPage.objPayFullAmountTxt,getTextVal(InstaLoanViewDetailsPage.objPayFullAmountTxt,"Text"));
				verifyElementPresent(InstaLoanViewDetailsPage.objPayFullAmountValue,getTextVal(InstaLoanViewDetailsPage.objPayFullAmountValue,"Amount"));
				verifyElementPresent(InstaLoanViewDetailsPage.objFirstInstallment, getTextVal(InstaLoanViewDetailsPage.objFirstInstallment, "Text"));
				verifyElementPresent(InstaLoanViewDetailsPage.objSecondInstallment, getTextVal(InstaLoanViewDetailsPage.objSecondInstallment, "Text"));
				verifyElementPresent(InstaLoanViewDetailsPage.objThirdInstallment, getTextVal(InstaLoanViewDetailsPage.objThirdInstallment, "Text"));
				Swipe("UP", 1);
				waitTime(3000);
				verifyElementPresent(InstaLoanViewDetailsPage.objFourthInstallment, getTextVal(InstaLoanViewDetailsPage.objFourthInstallment, "Text"));
				verifyElementPresent(InstaLoanViewDetailsPage.objFifthInstallment, getTextVal(InstaLoanViewDetailsPage.objFifthInstallment, "Text"));
				verifyElementPresent(InstaLoanViewDetailsPage.objSixthInstallment, getTextVal(InstaLoanViewDetailsPage.objSixthInstallment, "Text"));
				Back(1);
			}
			waitTime(3000);
			isClickable(InstaLoanHomePage.objPayNow);
			if(verifyElementPresent(InstaLoanHomePage.objPayNow,getTextVal(InstaLoanHomePage.objPayNow,"Button"))){
				click(InstaLoanHomePage.objPayNow,getTextVal(InstaLoanHomePage.objPayNow,"Button"));
				verifyElementPresent(InstaLoanHomePage.objPayment,getTextVal(InstaLoanHomePage.objPayment,"Page"));
				Back(1);
			}
			logger.info("TC_Insta_loan_29, Due i 30 Days Status Validated");
			extent.extentLoggerPass("TC_Insta_loan_29", "TC_Insta_loan_29, Overdue since 4 Days Status Validated");
			System.out.println("-----------------------------------------------------------");

			if (verifyElementPresent(InstaLoanViewDetailsPage.objLimitPaused, getTextVal(InstaLoanViewDetailsPage.objLimitPaused, "Text"))) {
				verifyElementPresent(InstaLoanViewDetailsPage.objLimitPausedMsg, getTextVal(InstaLoanViewDetailsPage.objLimitPausedMsg, "Message"));
				logger.info("TC_Insta_loan_30, Limit Paused text Message is validated");
				extent.extentLoggerPass("TC_Insta_loan_30", "TC_Insta_loan_30, Limit Paused text Message is validated");
				System.out.println("-----------------------------------------------------------");
			}
		}
	}
	public String backDate(int x){
		Calendar cal = Calendar.getInstance();
		LocalDate TodayDate = LocalDate.now();
		System.out.println(TodayDate);
		cal.add(Calendar.DAY_OF_YEAR, -x);
		Date dateDays = cal.getTime();
		System.out.println("Date before 30 days: " + dateDays);

		DateFormat dateFormat = new SimpleDateFormat("yyyy-M-dd");
		String strDate = dateFormat.format(dateDays);
		System.out.println("Converted String: " + strDate);
		return strDate;
	}
//======================================================HomeScrean Scenario End==================================================================//
// ==============================================================FeedBack Validation Start==================================//

	public void feedBackValidationWithGlobalFlag() throws Exception {
		extent.HeaderChildNode("FeedBack Validation With Global Flag");
		getDriver().resetApp();
		onBoarding();
		/*------------------------------WEB----------------------------*/
		waitTime(5000);
		Utilities.setPlatform = "Web";
        new CommandBase("Chrome");
        waitTime(4000);
		getWebDriver().get("https://new-admin-panel.test.ideopay.in/sign-in");
		waitTime(10000);
		if (verifyElementPresent(AdminPage.objEmailField, "Email Text Field")) {
			explicitWaitVisibilityNonDefault(AdminPage.objEmailField, 10);
			type(AdminPage.objEmailField, prop.getproperty("RingAdminEmail"), "Email");
			verifyElementPresent(TestingPortalWebPage.continueBtn, "Continue Button");
			JSClick(TestingPortalWebPage.continueBtn, "Continue Button");
			waitTime(10000);
			JSClick(AdminPage.objDashBoard, "DashBoard");
			ScrollToTheElement(AdminPage.objConfiguration);
			JSClick(AdminPage.objConfiguration, "Configuration");
			waitTime(5000);
			type(AdminPage.objSearchField, "Feedback", "FeedBack");
			waitTime(5000);
			JSClick(AdminPage.objEditBtn, "Edit Button");
			click(AdminPage.objMetaValueField, "Meta Field");
			waitTime(5000);
			clearFieldUsingRobot(AdminPage.objMetaValueField, "Meta Field");
			waitTime(2000);
			type(AdminPage.objMetaValueField, "0", "Meta Field");
			waitTime(2000);
			JSClick(AdminPage.objSubmit, "Submit Button");
			logOutAdminPanel();
		}

		waitTime(10000);
		BrowsertearDown();

		/*------------------------------Android----------------------------*/
		setPlatform("Android");
		initDriver();
		waitTime(5000);

		singleTransactionFromHomePage();
		explicitWaitVisibilityNonDefault(HomePage.objHome, 10);
		Swipe("DOWN", 1);
		verifyElementNotPresent(FeedbackPage.objFeedbackPopup, 5);
		logger.info("TC_Ring_Core_294, FeedBack popup is not displayed when global Flag is set as 0 or disabled");
		extent.extentLoggerPass("TC_Ring_Core_294","TC_Ring_Core_294, FeedBack popup is not displayed when global Flag is set as 0 or disabled");
		System.out.println("-----------------------------------------------------------");

		/*------------------------------WEB----------------------------*/
		waitTime(5000);
		Utilities.setPlatform = "Web";
        new CommandBase("Chrome");
        waitTime(4000);
		getWebDriver().get("https://new-admin-panel.test.ideopay.in/sign-in");
		waitTime(10000);
		if (verifyElementPresent(AdminPage.objEmailField, "Email Text Field")) {
			explicitWaitVisibilityNonDefault(AdminPage.objEmailField, 10);
			type(AdminPage.objEmailField, prop.getproperty("RingAdminEmail"), "Email");
			verifyElementPresent(AdminPage.objContinueBtn, "Continue Button");
			JSClick(TestingPortalWebPage.continueBtn, "Continue Button");
			waitTime(10000);
			JSClick(AdminPage.objDashBoard, "DashBoard");
			ScrollToTheElement(AdminPage.objConfiguration);
			JSClick(AdminPage.objConfiguration, "Configuration");
			waitTime(5000);
			type(AdminPage.objSearchField, "Feedback", "FeedBack");
			waitTime(5000);
			JSClick(AdminPage.objEditBtn, "Edit Button");
			click(AdminPage.objMetaValueField, "Meta Field");
			waitTime(5000);
			clearFieldUsingRobot(AdminPage.objMetaValueField, "Meta Field");
			waitTime(2000);
			type(AdminPage.objMetaValueField, "1", "Meta Field");
			JSClick(AdminPage.objSubmit, "Submit Button");
			logOutAdminPanel();
		}

		waitTime(10000);
		BrowsertearDown();

		/*------------------------------Android----------------------------*/
		setPlatform("Android");
		initDriver();
		waitTime(5000);
		Swipe("DOWN", 2);
		click(HomePage.objUPI, getTextVal(HomePage.objUPI, "Tab"));
		click(HomePage.objGoToHomePage, getTextVal(HomePage.objGoToHomePage, "Button"));
		if (verifyElementPresent(FeedbackPage.objFeedbackPopup, getTextVal(FeedbackPage.objFeedbackPopup, "Text"))) {
			logger.info("TC_Ring_Core_295, FeedBack popup is displayed when global Flag is set as 1 or enabled");
			extent.extentLoggerPass("TC_Ring_Core_295","TC_Ring_Core_295, FeedBack popup is displayed when global Flag is set as 1 or enabled");
			System.out.println("-----------------------------------------------------------");
		}
		click(FeedbackPage.objNoBtn, "No Button");

	}

	public void logOutAdminPanel() throws Exception {
		extent.HeaderChildNode(("LogOut from Admin Panel"));
		JSClick(AdminPage.objProfileBtn, "Profile Button");
		JSClick(AdminPage.objLogOutBtn, "LogOut Button");
		if (verifyElementPresent(AdminPage.objAdminPage, "Admin Login Page")) {
			logger.info("User Successfully LogOut from Admin Panel");
		} else {
			logger.info("User has not been Logout");
		}
	}

	public void feedBackMechanism() throws Exception {
		extent.HeaderChildNode("Validation of Feedback");
		getDriver().resetApp();
		onBoarding();
		explicitWaitVisibilityNonDefault(HomePage.objHome, 10);
		String sHome = getText(HomePage.objHome);
		softAssertion.assertEquals("Home", sHome);
		if (verifyElementPresent(HomePage.objScanAndPay, "Scan And Pay Button")) {
			String scanQrEnabled = getAttributValue("enabled", HomePage.objScanAndPay);
			softAssertion.assertEquals("true", scanQrEnabled);
			Aclick(HomePage.objScanAndPay, "Scan And Pay");

			explicitWaitVisibilityNonDefault(HomePage.objScanAnyQRToPay, 10);
			String sScanAnyQRCode = getText(HomePage.objScanAnyQRToPay);
			softAssertion.assertEquals("Scan any QR code to pay", sScanAnyQRCode);
			verifyElementPresent(HomePage.objScanAnyQRToPay, getTextVal(HomePage.objScanAnyQRToPay, "Text"));
			
			scannQRSwitch();
			
			if (verifyElementPresent(PaymentPage.objAmountTextField, "Amount Text Field")) {
				Aclick(PaymentPage.objAmountTextField, "Amount Field");
				clearField(PaymentPage.objAmountTextField, "Amount Field");
				type(PaymentPage.objAmountTextField, prop.getproperty("Amount_Merchant"), "Amount Field");
				click(PaymentPage.BtnPayNowBtn, "Pay Now Button");
				
			}
			waitTime(25000);
			explicitWaitVisibilityNonDefault(PaymentPage.objConfirmPayment, 30);
			if (verifyElementPresent2(PaymentPage.objConfirmPayment,getTextVal(PaymentPage.objConfirmPayment, "Text"))) {
				waitTime(30000);
				click(PaymentPage.objEditTransactionPin, "Edit Pin Field");
				type(PaymentPage.objEditTransactionPin, "1111", "Edit Pin Field");
				click(PaymentPage.objContinue, "Continue Button");
			}
			waitTime(30000);
			if (verifyElementPresent(PaymentPage.objPaymentDone, getTextVal(PaymentPage.objPaymentDone, "Text"))) {
				String sPaymentDone = getText(PaymentPage.objPaymentDone);
				softAssertion.assertEquals("Payment Done!", sPaymentDone);
				verifyElementPresent(PaymentPage.objGoToHomePage, getTextVal(PaymentPage.objGoToHomePage, "Text"));
				click(PaymentPage.objGoToHomePage, "Go To HomePage");
			}
			Swipe("DOWN", 2);
			waitTime(30000);
			if (verifyElementPresent(FeedbackPage.objFeedbackPopup,getTextVal(FeedbackPage.objFeedbackPopup, "Text"))) {
				String sFeedBackMsg = getText(FeedbackPage.objFeedbackPopup);
				softAssertion.assertEquals(sFeedBackMsg, "Are you enjoying the app?");
				verifyElementPresent(FeedbackPage.objYesBtn, getTextVal(FeedbackPage.objYesBtn, "Text"));
				verifyElementPresent(FeedbackPage.objNoBtn, getTextVal(FeedbackPage.objNoBtn, "Text"));
				logger.info("TC_Ring_Core_291, FeedBack popup displayed with yes & No option");
				extent.extentLoggerPass("TC_Ring_Core_291","TC_Ring_Core_291, FeedBack popup displayed with yes & No option");
				System.out.println("-----------------------------------------------------------");
				getDriver().runAppInBackground(Duration.ofSeconds(5));
			}
			if (verifyElementPresent(FeedbackPage.objFeedbackPopup,getTextVal(FeedbackPage.objFeedbackPopup, "Text"))) {
				logger.info("TC_Ring_Core_299, Reappeared FeedBack popup after dismissing and re-initiating");
				extent.extentLoggerPass("TC_Ring_Core_299","TC_Ring_Core_299, Reappeared FeedBack popup after dismissing and re-initiating");
				System.out.println("-----------------------------------------------------------");
			}
			click(FeedbackPage.objYesBtn, "Yes Button");
			if (verifyElementPresent(FeedbackPage.objRateUrExp, getTextVal(FeedbackPage.objRateUrExp, "Text"))) {
				String sRateUrExp = getText(FeedbackPage.objRateUrExp);
				softAssertion.assertEquals(sRateUrExp, "Rate your experience");
				logger.info("TC_Ring_Core_292, Rate ur exp bottom sheet got displayed");
				extent.extentLoggerPass("TC_Ring_Core_292", "TC_Ring_Core_292, Rate ur exp bottom sheet got displayed");
				System.out.println("-----------------------------------------------------------");
			}
			click(FeedbackPage.objCloseBtn, "Close Button");
			singleTransactionFromHomePage();
			Swipe("DOWN", 2);
			verifyElementNotPresent(FeedbackPage.objFeedbackPopup, 5);
			logger.info("TC_Ring_Core_293, FeedBack popup is not displayed on Second Transaction");
			extent.extentLoggerPass("TC_Ring_Core_293","TC_Ring_Core_293, FeedBack popup is not displayed on Second Transaction");
			System.out.println("-----------------------------------------------------------");
			ringPayLogout();

		}
		int nNumber = 4;
		for (int i = 1; i < nNumber; i++) {

			explicitWaitVisibilityNonDefault(RingLoginPage.objLoginLink, 10);
			Aclick(RingLoginPage.objLoginLink, "Signup/Login link");
			explicitWaitVisibilityNonDefault(RingLoginPage.objLoginMobile, 10);
			Aclick(RingLoginPage.objLoginMobile, "Continue with Mobile option");
			String nMobileNumber = "9" + RandomIntegerGenerator(9);
			click(RingLoginPage.objMobTextField, "Mobile Number Field");
			type(RingLoginPage.objMobTextField, nMobileNumber, "Mobile Number Field");

			enterOtp("888888");

			waitTime(30000);
			Aclick(UserRegistrationPage.objFirstName, "First Name text field");
			type(UserRegistrationPage.objFirstName, prop.getproperty("userName"), "First Name text field");
			Aclick(UserRegistrationPage.objLastName, "Last Name text field");
			type(UserRegistrationPage.objLastName, prop.getproperty("lastName"), "Last Name text field");
			hideKeyboard();
			Aclick(UserRegistrationPage.objMothersName, "Mother's Name Text Field");

			type(UserRegistrationPage.objMothersName, prop.getproperty("motherName"), "Mother's Name Text Field");
			hideKeyboard();
			String email = generateRandomEmail(15);
			Aclick(UserRegistrationPage.objUserEmail, "Email Field");
			Aclick(UserRegistrationPage.objNoneOfTheAbove, "None of the above");

			type(UserRegistrationPage.objUserEmail, email, "Email Field");
			hideKeyboard();
			dateOfBirth(prop.getproperty("dateOfBirthMonth"), prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthYear"));
			Aclick(UserRegistrationPage.objGenderBtn, "Gender Radio Button");
			Aclick(UserRegistrationPage.objGenderBtn, "Gender Radio Button");
			Aclick(UserRegistrationPage.objProceed, "Proceed Button");
			waitTime(20000);
			addAddress("86", "44, Borivali", "Vasa Street", "Das Gupta Street", "400080");
			waitTime(10000);
			instaLoancongratsScreen("Congrats");
			instaLoanSetPin(prop.getproperty("ValidPin"), prop.getproperty("ValidPin"));
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			softAssertion.assertEquals("Home", sHome);
			if (verifyElementPresent(HomePage.objScanAndPay, "Scan And Pay Button")) {
				String scanQrEnabled = getAttributValue("enabled", HomePage.objScanAndPay);
				softAssertion.assertEquals("true", scanQrEnabled);
				Aclick(HomePage.objScanAndPay, "Scan And Pay");

				explicitWaitVisibilityNonDefault(HomePage.objScanAnyQRToPay, 10);
				String sScanAnyQRCode = getText(HomePage.objScanAnyQRToPay);
				softAssertion.assertEquals("Scan any QR code to pay", sScanAnyQRCode);
				verifyElementPresent(HomePage.objScanAnyQRToPay, getTextVal(HomePage.objScanAnyQRToPay, "Text"));

				scannQRSwitch();

				if (verifyElementPresent(PaymentPage.objAmountTextField, "Amount Text Field")) {
					Aclick(PaymentPage.objAmountTextField, "Amount Field");
					clearField(PaymentPage.objAmountTextField, "Amount Field");
					type(PaymentPage.objAmountTextField, prop.getproperty("Amount_Merchant"), "Amount Field");
					Aclick(PaymentPage.objPayNowBtn, "Pay Now Button");
				}
				waitTime(30000);
				if (verifyElementPresent(PaymentPage.objConfirmPayment,
						getTextVal(PaymentPage.objConfirmPayment, "Text"))) {
					Aclick(PaymentPage.objEditTransactionPin, "Edit Pin Field");
					type(PaymentPage.objEditTransactionPin, "1111", "Edit Pin Field");
					Aclick(PaymentPage.objContinue, "Continue Button");
				}
				if (verifyElementPresent(PaymentPage.objPaymentDone, getTextVal(PaymentPage.objPaymentDone, "Text"))) {
					String sPaymentDone = getText(PaymentPage.objPaymentDone);
					softAssertion.assertEquals("Payment Done!", sPaymentDone);
					verifyElementPresent(PaymentPage.objGoToHomePage, getTextVal(PaymentPage.objGoToHomePage, "Text"));
					Aclick(PaymentPage.objGoToHomePage, "Go To HomePage");
				}
				Swipe("DOWN", 2);

				if (verifyElementPresent(FeedbackPage.objFeedbackPopup,
						getTextVal(FeedbackPage.objFeedbackPopup, "Text"))) {
					String sFeedBackMsg = getText(FeedbackPage.objFeedbackPopup);
					softAssertion.assertEquals(sFeedBackMsg, "Are you enjoying the app?");
					verifyElementPresent(FeedbackPage.objYesBtn, getTextVal(FeedbackPage.objYesBtn, "Text"));
					verifyElementPresent(FeedbackPage.objNoBtn, getTextVal(FeedbackPage.objNoBtn, "Text"));
					Aclick(FeedbackPage.objYesBtn, "Yes Button");

					if (verifyElementPresent(FeedbackPage.objRateUrExp,
							getTextVal(FeedbackPage.objRateUrExp, "Text"))) {
						String sRateUrExp = getText(FeedbackPage.objRateUrExp);
						softAssertion.assertEquals(sRateUrExp, "Rate your experience");
						click(FeedbackPage.objRating(i), "Rating Button");
//					click(FeedbackPage.objChips(nNumber-i),"Rating Button");
						int nChips = nNumber - i;
						for (int j = nChips; j > 0; j--) {
							click(FeedbackPage.objChips(j), +j + "Element");
						}
						click(FeedbackPage.objSubmitBtn, "Submit Button");
						logger.info("All chips data should be selected and submitted for rating " + i);
						extent.extentLoggerPass("Rate your experience",
								"All chips data should be selected and submitted for rating" + i);
						System.out.println("-----------------------------------------------------------");
					}
				} else {
					logger.info("FeedBack Popup is not displayed");
					extent.extentLoggerPass("FeedBack Popup", "FeedBack Popup is not displayed");
					System.out.println("-----------------------------------------------------------");
				}
			}
			ringPayLogout();
		}
		for (int i = nNumber; i < 6; i++) {

			explicitWaitVisibilityNonDefault(RingLoginPage.objLoginLink, 10);
			Aclick(RingLoginPage.objLoginLink, "Signup/Login link");
			explicitWaitVisibilityNonDefault(RingLoginPage.objLoginMobile, 10);
			Aclick(RingLoginPage.objLoginMobile, "Continue with Mobile option");
			String nMobileNumber = "9" + RandomIntegerGenerator(9);
			click(RingLoginPage.objMobTextField, "Mobile Number Field");
			type(RingLoginPage.objMobTextField, nMobileNumber, "Mobile Number Field");

			enterOtp("888888");

			waitTime(10000);
			Aclick(UserRegistrationPage.objFirstName, "First Name text field");
			type(UserRegistrationPage.objFirstName, prop.getproperty("userName"), "First Name text field");
			Aclick(UserRegistrationPage.objLastName, "Last Name text field");
			type(UserRegistrationPage.objLastName, prop.getproperty("lastName"), "Last Name text field");
			hideKeyboard();
			waitTime(2000);
			Aclick(UserRegistrationPage.objMothersName, "Mother's Name Text Field");

			type(UserRegistrationPage.objMothersName, prop.getproperty("motherName"), "Mother's Name Text Field");
			hideKeyboard();
			String email = generateRandomEmail(15);
			waitTime(2000);
			Aclick(UserRegistrationPage.objUserEmail, "Email Field");
			waitTime(3000);
			Aclick(UserRegistrationPage.objNoneOfTheAbove, "None of the above");

			type(UserRegistrationPage.objUserEmail, email, "Email Field");
			waitTime(2000);
			hideKeyboard();
			dateOfBirth(prop.getproperty("dateOfBirthMonth"), prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthYear"));
			Aclick(UserRegistrationPage.objGenderBtn, "Gender Radio Button");
			Aclick(UserRegistrationPage.objGenderBtn, "Gender Radio Button");
			Aclick(UserRegistrationPage.objProceed, "Proceed Button");
			waitTime(10000);
			waitTime(10000);
			addAddress("86", "44, Borivali", "Vasa Street", "Das Gupta Street", "400080");
			waitTime(10000);
			instaLoancongratsScreen("Congrats");
			instaLoanSetPin(prop.getproperty("ValidPin"), prop.getproperty("ValidPin"));
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			softAssertion.assertEquals("Home", sHome);
			if (verifyElementPresent(HomePage.objScanAndPay, "Scan And Pay Button")) {
				String scanQrEnabled = getAttributValue("enabled", HomePage.objScanAndPay);
				softAssertion.assertEquals("true", scanQrEnabled);
				Aclick(HomePage.objScanAndPay, "Scan And Pay");

				explicitWaitVisibilityNonDefault(HomePage.objScanAnyQRToPay, 10);
				String sScanAnyQRCode = getText(HomePage.objScanAnyQRToPay);
				softAssertion.assertEquals("Scan any QR code to pay", sScanAnyQRCode);
				verifyElementPresent(HomePage.objScanAnyQRToPay, getTextVal(HomePage.objScanAnyQRToPay, "Text"));

				scannQRSwitch();

				if (verifyElementPresent(PaymentPage.objAmountTextField, "Amount Text Field")) {
					Aclick(PaymentPage.objAmountTextField, "Amount Field");
					clearField(PaymentPage.objAmountTextField, "Amount Field");
					type(PaymentPage.objAmountTextField, prop.getproperty("Amount_Merchant"), "Amount Field");
					Aclick(PaymentPage.objPayNowBtn, "Pay Now Button");
				}
				waitTime(30000);
				if (verifyElementPresent(PaymentPage.objConfirmPayment,
						getTextVal(PaymentPage.objConfirmPayment, "Text"))) {
					Aclick(PaymentPage.objEditTransactionPin, "Edit Pin Field");
					type(PaymentPage.objEditTransactionPin, "1111", "Edit Pin Field");
					Aclick(PaymentPage.objContinue, "Continue Button");
				}
				if (verifyElementPresent(PaymentPage.objPaymentDone, getTextVal(PaymentPage.objPaymentDone, "Text"))) {
					String sPaymentDone = getText(PaymentPage.objPaymentDone);
					softAssertion.assertEquals("Payment Done!", sPaymentDone);
					verifyElementPresent(PaymentPage.objGoToHomePage, getTextVal(PaymentPage.objGoToHomePage, "Text"));
					Aclick(PaymentPage.objGoToHomePage, "Go To HomePage");
				}
				Swipe("DOWN", 2);

				if (verifyElementPresent(FeedbackPage.objFeedbackPopup,
						getTextVal(FeedbackPage.objFeedbackPopup, "Text"))) {
					String sFeedBackMsg = getText(FeedbackPage.objFeedbackPopup);
					softAssertion.assertEquals(sFeedBackMsg, "Are you enjoying the app?");
					verifyElementPresent(FeedbackPage.objYesBtn, getTextVal(FeedbackPage.objYesBtn, "Text"));
					verifyElementPresent(FeedbackPage.objNoBtn, getTextVal(FeedbackPage.objNoBtn, "Text"));
					Aclick(FeedbackPage.objYesBtn, "Yes Button");

					if (verifyElementPresent(FeedbackPage.objRateUrExp,
							getTextVal(FeedbackPage.objRateUrExp, "Text"))) {
						String sRateUrExp = getText(FeedbackPage.objRateUrExp);
						softAssertion.assertEquals(sRateUrExp, "Rate your experience");

						click(FeedbackPage.objRating(i), "Rating Button");

//					click(FeedbackPage.objChips(nNumber-i),"Rating Button");
//						int nChips = nNumber - i;
//						for (int j = nChips; j > 0; j--) {
//							click(FeedbackPage.objChips(j), +j + "Element");
//						}
						click(FeedbackPage.objRateUs, "Rate us on PlayStore Button");
						if (verifyElementPresent(FeedbackPage.objPlayStore,
								getTextVal(FeedbackPage.objPlayStore, "Text"))) {
							String sPayWithRing = getText(FeedbackPage.objPlayStore);
							softAssertion.assertEquals(sPayWithRing, "//*[@text='Pay with Ring']");
							logger.info("TC_Ring_Core_300, Redirected to PlayStore");
							extent.extentLoggerPass("TC_Ring_Core_300", "TC_Ring_Core_300, Redirected to PlayStore");
							System.out.println("-----------------------------------------------------------");
							Back(1);
						}
					}
				} else {
					logger.info("FeedBack Popup is not displayed");
					extent.extentLoggerPass("FeedBack Popup", "FeedBack Popup is not displayed");
					System.out.println("-----------------------------------------------------------");
				}
			}
			ringPayLogout();
		}
	}

	public void singleTransactionFromHomePage() throws Exception {
		extent.HeaderChildNode(" Single Transaction");
		waitTime(5000);
		if (verifyElementPresent(HomePage.objScanAndPay, "Scan And Pay Button")) {
			String scanQrEnabled = getAttributValue("enabled", HomePage.objScanAndPay);
			softAssertion.assertEquals("true", scanQrEnabled);
			Aclick(HomePage.objScanAndPay, "Scan And Pay");
			
			scannQRSwitch();

			if (verifyElementPresent(PaymentPage.objAmountTextField, "Amount Text Field")) {
				Aclick(PaymentPage.objAmountTextField, "Amount Field");
				clearField(PaymentPage.objAmountTextField, "Amount Field");
				type(PaymentPage.objAmountTextField, "200", "Amount Field");

				verifyElementPresentAndClick(PaymentPage.objPayNowBtn, "Pay Now Button");
//				Aclick(PaymentPage.objPayNowBtn, "Pay Now Button");
			}
			waitTime(30000);
			explicitWaitVisibilityNonDefault(PaymentPage.objConfirmPayment, 30);
			if (verifyElementPresent(PaymentPage.objConfirmPayment,
					getTextVal(PaymentPage.objConfirmPayment, "Text"))) {
				Aclick(PaymentPage.objEditTransactionPin, "Edit Pin Field");
				type(PaymentPage.objEditTransactionPin, "1111", "Edit Pin Field");
				Aclick(PaymentPage.objContinue, "Continue Button");
			}
			if (verifyElementPresent(PaymentPage.objPaymentDone, getTextVal(PaymentPage.objPaymentDone, "Text"))) {
				String sPaymentDone = getText(PaymentPage.objPaymentDone);
				softAssertion.assertEquals("Payment Done!", sPaymentDone);
				verifyElementPresent(PaymentPage.objGoToHomePage, getTextVal(PaymentPage.objGoToHomePage, "Text"));
				Aclick(PaymentPage.objGoToHomePage, "Go To HomePage");
			}
		}
	}

	public static String generateRandomEmail(int length) {
		logger.info("Generating a Random email String");
		String allowedChars = "abcdefghijklmnopqrstuvwxyz" + "1234567890";
		String email = "";
		String temp = RandomStringUtils.random(length, allowedChars);
		email = temp.substring(0, temp.length() - 9) + "@gmail.com";
		return email;
	}

	// =========================FeedBack Validation End==================================//

	// ==========================Mandatory Journey Start================================//

	public void instaLoanInProcessStatus() throws Exception {
		waitTime(10000);
		if (verifyElementPresent(InstaLoanViewDetailsPage.objInProcess, "InProcess")) {
			String sInProcess = getText(InstaLoanViewDetailsPage.objInProcess);
			softAssertion.assertEquals(sInProcess, "In Process");
			verifyElementPresentAndClick(InstaLoanHomePage.objViewDetails,getTextVal(InstaLoanHomePage.objViewDetails, "Link"));
			if (verifyElementPresent(InstaLoanViewDetailsPage.objDisbursementInProcess,getTextVal(InstaLoanViewDetailsPage.objDisbursementInProcess, "Text"))) {
				verifyElementPresent(InstaLoanViewDetailsPage.objInProcessAmount,getTextVal(InstaLoanViewDetailsPage.objInProcessAmount, "Amount"));
				verifyElementPresent(InstaLoanViewDetailsPage.objDisbursementStructure,getTextVal(InstaLoanViewDetailsPage.objDisbursementStructure, "Text"));
				verifyElementPresent(InstaLoanViewDetailsPage.objApprovedInstaLoan,getTextVal(InstaLoanViewDetailsPage.objApprovedInstaLoan, "Text"));
				verifyElementPresent(InstaLoanViewDetailsPage.objApprovedInstaLoanAmount,getTextVal(InstaLoanViewDetailsPage.objApprovedInstaLoanAmount, "Text"));
				verifyElementPresent(InstaLoanViewDetailsPage.objProcessingFee,getTextVal(InstaLoanViewDetailsPage.objProcessingFee, "Text"));
				verifyElementPresent(InstaLoanViewDetailsPage.objProcessingFeeAmount,getTextVal(InstaLoanViewDetailsPage.objProcessingFeeAmount, "Amount"));
				verifyElementPresent(InstaLoanViewDetailsPage.objActivationFee,getTextVal(InstaLoanViewDetailsPage.objActivationFee, "Text"));
				verifyElementPresent(InstaLoanViewDetailsPage.objActivationFeeAmount,getTextVal(InstaLoanViewDetailsPage.objActivationFeeAmount, "Amount"));
				verifyElementPresent(InstaLoanViewDetailsPage.objGST,getTextVal(InstaLoanViewDetailsPage.objGST, "Text"));
				verifyElementPresent(InstaLoanViewDetailsPage.objGSTAmount,getTextVal(InstaLoanViewDetailsPage.objGSTAmount, "Amount"));
				verifyElementPresent(InstaLoanViewDetailsPage.objDisbursementAmountTxt,getTextVal(InstaLoanViewDetailsPage.objDisbursementAmountTxt, "Text"));
				verifyElementPresent(InstaLoanViewDetailsPage.objDisbursementAmount,getTextVal(InstaLoanViewDetailsPage.objDisbursementAmount, "Amount"));
				logger.info("TC_Insta_loan_25, Disbursement in process screen is validated");
				extent.extentLoggerPass("TC_Insta_loan_25","TC_Insta_loan_25, Disbursement in process screen is validated");
				System.out.println("-----------------------------------------------------------");
				Back(1);
			}
			if (verifyElementPresent(InstaLoanViewDetailsPage.objLimitPaused,getTextVal(InstaLoanViewDetailsPage.objLimitPaused, "Text"))) {
				verifyElementPresent(InstaLoanViewDetailsPage.objLimitPausedMsg,getTextVal(InstaLoanViewDetailsPage.objLimitPausedMsg, "Message"));
				logger.info("TC_Insta_loan_26, Limit Paused text Message is validated");
				extent.extentLoggerPass("TC_Insta_loan_26", "TC_Insta_loan_26, Limit Paused text Message is validated");
				System.out.println("-----------------------------------------------------------");
			}
		} else {
			logger.info("In Process status is not displayed");
		}
	}

	public void instaLoanMandatoryJourney() throws Exception {
		extent.HeaderChildNode("Insta Loan Mandatory Journey");
		getDriver().resetApp();
		String userReferenceNo = instaLoanWhitelistOnBoardingMandatory();
		transactionValidation();
		Swipe("UP", 2);

		executeUpdateFatherName("Update db_user_service.users set father_name='Arun' where mobile_number='" + mobileNumber + "';","Select * from db_user_service.users where mobile_number='" + mobileNumber + "';");
		click(InstaLoanPage.objApplyBtn, getText(InstaLoanPage.objApplyBtn) + " Button is displayed");
		boolean basicDetailsPage = verifyElementPresent2(InstaLoanMandatoryJourney.objFathersNameBasicDetails,"Fathers Name Basic Details Page");
		if (basicDetailsPage == false) {
			logger.info("TC_Insta_loan_84 - To verify Basic details page should be skipped if father name is present");
			extent.extentLoggerPass("TC_Insta_loan_84","TC_Insta_loan_84, To verify Basic details page should be skipped if father name is present");
		}

		tapUsingCoordinates(540, 1540);
		boolean creditbureaupopup = verifyElementPresent(InstaLoanOptionalJourney.objCreditBureauConsentPopup,getText(InstaLoanOptionalJourney.objCreditBureauConsentPopup));
		if (creditbureaupopup == true) {
			logger.info("TC_Insta_loan_87 - To Verify If the customer clicks on “Credit bureau checks” hyperlink");
			extent.extentLoggerPass("TC_Insta_loan_87","TC_Insta_loan_87 - To Verify If the customer clicks on Credit bureau checks hyperlink");
			verifyElementPresentAndClick(InstaLoanOptionalJourney.objOkGotItCreditBureaupopup,getText(InstaLoanOptionalJourney.objOkGotItCreditBureaupopup));
		}
		waitTime(5000);
		tapUsingCoordinates(268, 1599);
		boolean ckycConsent = verifyElementPresent(InstaLoanOptionalJourney.objCKYCConsentHyperlink,getText(InstaLoanOptionalJourney.objCKYCConsentHyperlink));
		if (creditbureaupopup == true) {
			logger.info("TC_Insta_loan_88 - To Verify If the customer clicks on “Credit bureau checks” hyperlink");
			extent.extentLoggerPass("TC_Insta_loan_88","TC_Insta_loan_88 - To Verify If the customer clicks on “Credit bureau checks” hyperlink");
			verifyElementPresentAndClick(InstaLoanOptionalJourney.objOkGotItCreditBureaupopup,getText(InstaLoanOptionalJourney.objOkGotItCreditBureaupopup));
		}

		click(InstaLoanHomePage.objProceedBtn, "Proceed Button");
		click(InstaLoanHomePage.objSelectAddress, getTextVal(InstaLoanHomePage.objSelectAddress, "Text"));
		verifyElementPresentAndClick(InstaLoanPage.objAddressSelected, getText(InstaLoanPage.objAddressSelected));

//		click(InstaLoanHomePage.objAddress,getTextVal(InstaLoanHomePage.objAddress,"Text"));
		click(InstaLoanHomePage.objProceedBtn, getTextVal(InstaLoanHomePage.objProceedBtn, "Text"));

		explicitWaitVisibilityNonDefault(InstaLoanHomePage.objOffer, 10);

		addbankAccount("5" + RandomIntegerGenerator(4), firstName + " " + lastName);

		String cond_ApprovedStatus = executeQuery3("SELECT * FROM db_tradofina.user_attributes where user_reference_number='" + userReferenceNo + "';",51);
		System.out.println(cond_ApprovedStatus);
		softAssertion.assertEquals(cond_ApprovedStatus, "COND_APPROVED");
		logger.info("TC_Insta_loan_99 - To verify transaction will be CA before loading offer page");
		extent.extentLoggerPass("TC_Insta_loan_99","TC_Insta_loan_99 - To verify transaction will be CA before loading offer page");

		if (verifyElementPresent(InstaLoanHomePage.objOffer, getTextVal(InstaLoanHomePage.objOffer, "Text"))) {
			verifyElementPresentAndClick(InstaLoanHomePage.objCheckBox, "Check Box");
			verifyElementPresentAndClick(InstaLoanHomePage.objAcceptOffer,getTextVal(InstaLoanHomePage.objAcceptOffer, "Text"));
//			verifyElementPresentAndClick(InstaLoanHomePage.objConfirmOffer, getTextVal(InstaLoanHomePage.objConfirmOffer, "Text"));
			waitTime(90000);
		}
		if (verifyElementPresent(InstaLoanHomePage.objInstaLoanDisbursement, "Disbursement Text")) {
			verifyElementPresent(InstaLoanHomePage.objInstaLoanAmount,getTextVal(InstaLoanHomePage.objInstaLoanAmount, "Amount"));
			verifyElementPresentAndClick(InstaLoanHomePage.objHome, getTextVal(InstaLoanHomePage.objHome, "Button"));
			waitTime(10000);
		} else {
			logger.info("Insta Loan Disbursement screen is not displayed");
		}
		verifyElementPresentAndClick(InstaLoanPage.objHomeBtn, getText(InstaLoanPage.objHomeBtn) + " Button");
		waitTime(10000);
		instaLoanInProcessStatus();
		waitTime(120000);

		if (!verifyElementPresent2(HomePage.objPayNowBtnAtHomePage, "Pay Now Button")) {
			waitTime(120000);
			verifyElementPresentAndClick(HomePage.objUPI, "UPI Tab");
			waitTime(120000);
			Back(1);
		}

		if (verifyElementPresent(InstaLoanViewDetailsPage.objLimitPaused,getTextVal(InstaLoanViewDetailsPage.objLimitPaused, "Text"))) {
			verifyElementPresent(InstaLoanViewDetailsPage.objLimitPausedMsg,getTextVal(InstaLoanViewDetailsPage.objLimitPausedMsg, "Message"));
			logger.info("TC_Insta_loan_103, Loan status should be change to FA after accepting te offer");
			extent.extentLoggerPass("TC_Insta_loan_103","TC_Insta_loan_103, Loan status should be change to FA after accepting te offer");
			System.out.println("-----------------------------------------------------------");
		}

		payNowInstaLoan();

		explicitWaitVisibilityNonDefault(HomePage.objHome, 10);
		String sHomeTxt = getText(HomePage.objHome);
		softAssertion.assertEquals(sHomeTxt, "Home");
		logger.info("Navigated to Home Page");
		extent.extentLoggerPass("Home Page", "Navigated to Home Page");
		waitTime(2000);
		Swipe("DOWN", 2);

		String sLimitPaused = getText(InstaLoanHomePage.objLimitPaused);
		softAssertion.assertEquals(sLimitPaused, "Limit Paused");
		if (verifyElementPresent(InstaLoanViewDetailsPage.objLimitPaused,getTextVal(InstaLoanViewDetailsPage.objLimitPaused, "Text"))) {
			verifyElementPresent(InstaLoanViewDetailsPage.objLimitPausedMsg,getTextVal(InstaLoanViewDetailsPage.objLimitPausedMsg, "Message"));
			logger.info("TC_Insta_loan_104, Ring limit should be inactive state after repayment of insta loan for mandatory user");
			extent.extentLoggerPass("TC_Insta_loan_104","TC_Insta_loan_104, Ring limit should be inactive state after repayment of insta loan for mandatory user");
			System.out.println("-----------------------------------------------------------");

		}
		if (verifyElementPresent(InstaLoanViewDetailsPage.objLimitPaused,getTextVal(InstaLoanViewDetailsPage.objLimitPaused, "Text"))) {
			verifyElementPresent(InstaLoanViewDetailsPage.objLimitPausedMsg,getTextVal(InstaLoanViewDetailsPage.objLimitPausedMsg, "Message"));
			logger.info("TC_Insta_loan_107, Ring limit should not get activated post full repayment of instaloan");
			extent.extentLoggerPass("TC_Insta_loan_107","TC_Insta_loan_107, Ring limit should not get activated post full repayment of instaloan");
			System.out.println("-----------------------------------------------------------");
		}
		if (verifyElementPresent(InstaLoanHomePage.objReApplyInstaLoan,getTextVal(InstaLoanHomePage.objReApplyInstaLoan, "Text"))) {
			logger.info("TC_Insta_loan_106, Insta loan avail banner should be visible again for that user");
			extent.extentLoggerPass("TC_Insta_loan_106","TC_Insta_loan_106, Insta loan avail banner should be visible again for that user");
			System.out.println("-----------------------------------------------------------");
		}
		click(InstaLoanPage.objApplyBtn, getText(InstaLoanPage.objApplyBtn) + " Button is displayed");
		explicitWaitVisibilityNonDefault(InstaLoanHomePage.objPermanentAddress, 10);
		if (verifyElementPresent(InstaLoanHomePage.objPermanentAddress,getTextVal(InstaLoanHomePage.objPermanentAddress, "Text"))) {
			logger.info("TC_Insta_loan_77, user should able to continue the insta loan mandatory journey");
			extent.extentLoggerPass("TC_Insta_loan_77","TC_Insta_loan_77, user should able to continue the insta loan mandatory journey");
			System.out.println("-----------------------------------------------------------");
		}

		click(InstaLoanHomePage.objCommunicationAddressBackArrow, "Back Arrow");
		click(InstaLoanPage.objYesBtn, "Yes Button");

		verifyElementPresentAndClick(HomePage.objScanAndPay, "Scan and Pay Button");
		if (verifyElementPresent(InstaLoanHomePage.objSorryMsg1, getTextVal(InstaLoanHomePage.objSorryMsg1, "Text"))) {
			verifyElementPresent(InstaLoanHomePage.objSorryMsg2, getTextVal(InstaLoanHomePage.objSorryMsg2, "Text"));
			logger.info("TC_Insta_loan_78, Error Message is displayed after clicking on Scan and Pay");
			extent.extentLoggerPass("TC_Insta_loan_78","TC_Insta_loan_78, Error Message is displayed after clicking on Scan and Pay");
			System.out.println("-----------------------------------------------------------");
		}
		Back(1);

		verifyElementPresentAndClick(HomePage.objMore, getTextVal(HomePage.objMore, "Text"));
		verifyElementPresentAndClick(InstaLoanHomePage.objBankTransfer,getTextVal(InstaLoanHomePage.objBankTransfer, "Text"));
		if (verifyElementPresent(InstaLoanHomePage.objSorryMsg1, getTextVal(InstaLoanHomePage.objSorryMsg1, "Text"))) {
			verifyElementPresent(InstaLoanHomePage.objSorryMsg2, getTextVal(InstaLoanHomePage.objSorryMsg2, "Text"));
			logger.info("TC_Insta_loan_79, Error Message is displayed after clicking on Bank Transfer");
			extent.extentLoggerPass("TC_Insta_loan_79","TC_Insta_loan_79, Error Message is displayed after clicking on Bank Transfer");
			System.out.println("-----------------------------------------------------------");
		}
		Back(1);

		verifyElementPresentAndClick(InstaLoanHomePage.objElectricity,getTextVal(InstaLoanHomePage.objElectricity, "Text"));
		if (verifyElementPresent(InstaLoanHomePage.objSorryMsg1, getTextVal(InstaLoanHomePage.objSorryMsg1, "Text"))) {
			verifyElementPresent(InstaLoanHomePage.objSorryMsg2, getTextVal(InstaLoanHomePage.objSorryMsg2, "Text"));
			logger.info("TC_Insta_loan_80, Error Message is displayed after clicking on BBPS");
			extent.extentLoggerPass("TC_Insta_loan_80","TC_Insta_loan_80, Error Message is displayed after clicking on BBPS");
			System.out.println("-----------------------------------------------------------");
			logger.info("TC_Insta_loan_81, If Ring bill is paid then line should get inactive");
			extent.extentLoggerPass("TC_Insta_loan_81","TC_Insta_loan_81, If Ring bill is paid then line should get inactive");
			System.out.println("-----------------------------------------------------------");
		}
		Back(1);
		Back(1);

		click(InstaLoanHomePage.objContinue, "Continue Button");
		click(InstaLoanHomePage.objSelectAddress, getTextVal(InstaLoanHomePage.objSelectAddress, "Text"));

		click(InstaLoanHomePage.objAddress1, "Address");

//		click(InstaLoanHomePage.objAddress,getTextVal(InstaLoanHomePage.objAddress,"Text"));
		click(InstaLoanHomePage.objProceedBtn, getTextVal(InstaLoanHomePage.objProceedBtn, "Text"));

		explicitWaitVisibilityNonDefault(InstaLoanHomePage.objOffer, 10);
		if (verifyElementPresent(InstaLoanHomePage.objAddBankDetails,getTextVal(InstaLoanHomePage.objAddBankDetails, "Text"))) {
			click(InstaLoanHomePage.objAddBankDetails, getTextVal(InstaLoanHomePage.objAddBankDetails, "Text"));
			if (verifyElementPresent(InstaLoanHomePage.objExistingBankAcc,getTextVal(InstaLoanHomePage.objExistingBankAcc, "Text"))) {
				logger.info("TC_Insta_loan_100 - Existing Bank Accounts are displayed After clicking on Add Bank Details");
				extent.extentLoggerPass("TC_Insta_loan_100","TC_Insta_loan_100, Existing Bank Accounts are displayed After clicking on Add Bank Details");
				System.out.println("-----------------------------------------------------------");

				if (verifyElementPresent(InstaLoanHomePage.objApproved,getTextVal(InstaLoanHomePage.objApproved, "Text"))) {
					logger.info("TC_Insta_loan_101 - Bank should be added on bank name match percent");
					extent.extentLoggerPass("TC_Insta_loan_101","TC_Insta_loan_101, Bank should be added on bank name match percent");
					System.out.println("-----------------------------------------------------------");
				}
			}
		}
		verifyElementPresentAndClick(BankTransferModule.objAddNewBankAccountBtn, "Add New Bank Account");
		explicitWaitVisibilityNonDefault(BankTransferModule.objIfscTextField, 20);
		click(BankTransferModule.objIfscTextField, "IFSC Code Text Field");
		type(BankTransferModule.objIfscTextField, "SBIN4566777", "IFSC Code Text Field");
		logger.warn(getTextVal(BankTransferModule.objIFSCErrorMsg, "Error Message"));
		extent.extentLoggerWarning("Error Message", getTextVal(BankTransferModule.objIFSCErrorMsg, "Error Message"));
		clearField(BankTransferModule.objIfscTextField, "IFSC Code Text Field");
		type(BankTransferModule.objIfscTextField, "SBIN0003456", "IFSC Code Text Field");

		explicitWaitVisibilityNonDefault(BankTransferModule.objAccountNumberField, 10);
		click(BankTransferModule.objAccountNumberField, "Account Number Field");
		String accountNo = "6" + RandomIntegerGenerator(4);
		accountNo = accountNo + RandomIntegerGenerator(5);
		type(BankTransferModule.objAccountNumberField, accountNo, "Account Number Text Field");

		explicitWaitVisibilityNonDefault(BankTransferModule.ObjConfirmAccountNumberField, 10);
		click(BankTransferModule.ObjConfirmAccountNumberField, "Confirm Account Number Field");
		type(BankTransferModule.ObjConfirmAccountNumberField, accountNo, "Account Number Text Field");
		hideKeyboard();
		waitTime(2000);
		Swipe("Up", 2);
		waitTime(2000);
		String name = firstName + " " + lastName;
		click(BankTransferModule.objAccountHolderName, "Account Holder Name");
		type(BankTransferModule.objAccountHolderName, name, "Account Number Text Field");
		hideKeyboard();
		doubleTap(BankTransferModule.objAccountTypeField, 2, "Account Type Field");
		explicitWaitVisibilityNonDefault(BankTransferModule.objSavings, 10);
		click(BankTransferModule.objSavings, "Savings Type");
		click(BankTransferModule.objContinueButton, "Continue Button");
		explicitWaitVisibilityNonDefault(BankTransferModule.objBottomSheetPopup, 10);
		verifyElementExist(BankTransferModule.objBottomSheetPopup, "Verify Bank Details Pop Up");
		click(BankTransferModule.objConfirmBtn, "Confirm Button");
		explicitWaitVisibilityNonDefault(BankTransferModule.objUnableToAddAcc, 10);
		if (verifyElementPresent(BankTransferModule.objUnableToAddAcc,getTextVal(BankTransferModule.objUnableToAddAcc, "Text"))) {
			click(InstaLoanHomePage.objOkGotIt2, getTextVal(InstaLoanHomePage.objOkGotIt2, "Text"));
		}
		Swipe("up", 2);

		verifyElementPresentAndClick(InstaLoanHomePage.objContinue1, "Continue Button");
		verifyElementPresentAndClick(InstaLoanHomePage.objAddBankDetails,getTextVal(InstaLoanHomePage.objAddBankDetails, "Text"));

		if (verifyElementPresent(InstaLoanHomePage.objExistingBankAcc,getTextVal(InstaLoanHomePage.objExistingBankAcc, "Text"))) {
			verifyElementPresent(InstaLoanHomePage.objRejected,getTextVal(InstaLoanHomePage.objRejected, "Status of Accunt"));
			logger.info("TC_Insta_loan_102 - Bank should be reject on bank name match percent");
			extent.extentLoggerPass("TC_Insta_loan_102","TC_Insta_loan_102, Bank should be reject on bank name match percent");
			System.out.println("-----------------------------------------------------------");
		}

		getDriver().resetApp();

		instaLoanWhitelistOnBoardingMandatory();

		Swipe("up", 2);
		click(InstaLoanPage.objApplyBtn, getText(InstaLoanPage.objApplyBtn) + " Button is displayed");
		boolean basicDetailsPageSecondTime = verifyElementPresent2(InstaLoanMandatoryJourney.objBasicDetailsPage,"Basic Details Page");
		List<WebElement> toatalField = getDriver().findElements(InstaLoanMandatoryJourney.objOnlyFathersNameField);
		for (WebElement web : toatalField) {
			String name1 = web.getText();
			logger.info(name1);
			extent.extentLoggerPass("Name", name1);
			if (web.getText().equals("Father Name") && basicDetailsPageSecondTime == true) {
				logger.info("TC_Insta_loan_85 - To verify basic detail page");
				extent.extentLoggerPass("TC_Insta_loan_85", "TC_Insta_loan_85, To verify basic detail page");
				System.out.println("-----------------------------------------------------------");
			}
		}
		waitTime(10000);
		click(InstaLoanPage.objFatherName, "Father Name Field");
		type(InstaLoanPage.objFatherName, "Arun", "Father Name Field");
		verifyElementPresentAndClick(InstaLoanPage.objProceedBtn, "Proceed Button");
		waitTime(10000);
		boolean checkBureauHyperlink = verifyElementPresent(InstaLoanMandatoryJourney.objIallowCreditBureauChecksText,getText(InstaLoanMandatoryJourney.objIallowCreditBureauChecksText));
		boolean ihearByConfirm = verifyElementPresent(InstaLoanMandatoryJourney.objIhearbyConfirmText,getText(InstaLoanMandatoryJourney.objIhearbyConfirmText));
		if (checkBureauHyperlink && ihearByConfirm == true) {
			logger.info("TC_Insta_loan_86 - To verify personal detail page should be skipped ");
			extent.extentLoggerPass("TC_Insta_loan_86","TC_Insta_loan_86, To verify personal detail page should be skipped");
			System.out.println("-----------------------------------------------------------");
		}

		click(InstaLoanMandatoryJourney.objIallowCheckBox, "I allow Ring to run checkBox");
		click(InstaLoanMandatoryJourney.objReadmoreCheckBox,"I, do hearby confirm that my annual household income checkBox");
		verifyElementPresentAndClick(InstaLoanPage.objProceedBtn, "Proceed Button");
		String text = getText(InstaLoanMandatoryJourney.objScanAndPayToastMsg);
		if (text.equals("Please select the checkbox for relevant authorizations")) {
			logger.info("TC_Insta_loan_89 - To Verify If the customer unchecks any one or both check boxes for consent and clicks on proceed");
			extent.extentLoggerPass("TC_Insta_loan_89","TC_Insta_loan_89, To Verify If the customer unchecks any one or both check boxes for consent and clicks on proceed");
			System.out.println("-----------------------------------------------------------");
		}
		click(InstaLoanMandatoryJourney.objIallowCheckBox, "I allow Ring to run checkBox");
		click(InstaLoanMandatoryJourney.objReadmoreCheckBox,"I, do hearby confirm that my annual household income checkBox");
		verifyElementPresentAndClick(InstaLoanPage.objProceedBtn, "Proceed Button");
		verifyElementPresent(InstaLoanMandatoryJourney.objSelectAddressDropDown,getText(InstaLoanMandatoryJourney.objSelectAddressDropDown));
		verifyElementPresent(InstaLoanMandatoryJourney.objIdeclaretheAboveAddressCheckBox,"I declare the above address is same as my communication address");
		click(InstaLoanMandatoryJourney.objIdeclaretheAboveAddressCheckBox,"I declare the above address is same as my communication address checkBox");
		waitTime(5000);
		isClickable(InstaLoanMandatoryJourney.objAddCommunicationAddress);
		logger.info("TC_Insta_loan_91 - To Verify user if check box is not ticked user should able to click on add communication address link");
		extent.extentLoggerPass("TC_Insta_loan_91","TC_Insta_loan_91, To Verify user if check box is not ticked user should able to click on add communication address link");
		System.out.println("-----------------------------------------------------------");
		click(InstaLoanMandatoryJourney.objAddCommunicationAddress,getText(InstaLoanMandatoryJourney.objAddCommunicationAddress));
		instaNewCommunicationAddress();
		verifyElementPresent2(InstaLoanPage.objOfferHeader, "Offer Page");
		logger.info("TC_Insta_loan_92 - User should able to add communication address");
		extent.extentLoggerPass("TC_Insta_loan_92", "TC_Insta_loan_92, User should able to add communication address");
		System.out.println("-----------------------------------------------------------");
		offerPage();
		click(InstaLoanPage.objAcceptCheckBox, getText(InstaLoanPage.objAcceptCheckBox));
		verifyElementPresentAndClick(InstaLoanPage.objAcceptOffer, getText(InstaLoanPage.objAcceptOffer));
//		verifyElementPresentAndClick(InstaLoanPage.objConfirmOfferBtn, getText(InstaLoanPage.objConfirmOfferBtn));
		waitTime(70000);
		verifyElementPresentAndClick(InstaLoanPage.objHomeBtn, getText(InstaLoanPage.objHomeBtn) + " Button");
		waitTime(4000);

		payNowInstaLoan();

		Swipe("up", 2);
		click(InstaLoanPage.objApplyNowBtn, getText(InstaLoanPage.objApplyNowBtn) + " Button is displayed");
		verifyElementPresentAndClick(InstaLoanPage.objSelectAddress, "Select Address Dropdown");
		explicitWaitVisibilityNonDefault(InstaLoanPage.objAddressSelected, 30);
		verifyElementPresentAndClick(InstaLoanPage.objAddressSelected, getText(InstaLoanPage.objAddressSelected));
		verifyElementPresentAndClick(RingUserDetailPage.objRegisterBtn, "Proceed Button");
		verifyElementPresent2(InstaLoanPage.objOfferHeader, "Offer Page");
		logger.info("TC_Insta_loan_90 - To Verify Address page");
		extent.extentLoggerPass("TC_Insta_loan_90", "TC_Insta_loan_90, To Verify Address page");
		System.out.println("-----------------------------------------------------------");

	}
	public void instaLoanMandatoryAndOptional() throws Exception {
		extent.HeaderChildNode("Both Mandatory and Optional");
		getDriver().resetApp();
		String userReferenceNo = instaLoanWhitelistOnBoardingMandatory();
		waitTime(10000);

		Swipe("up", 2);
		click(InstaLoanPage.objApplyBtn, getText(InstaLoanPage.objApplyBtn) + " Button is displayed");

		click(InstaLoanPage.objFatherName, "Father Name Field");
		type(InstaLoanPage.objFatherName, "Arun", "Father Name Field");
		verifyElementPresentAndClick(InstaLoanPage.objProceedBtn, "Proceed Button");
		waitTime(5000);
		verifyElementPresentAndClick(InstaLoanPage.objProceedBtn, "Proceed Button");
		click(InstaLoanHomePage.objSelectAddress, getTextVal(InstaLoanHomePage.objSelectAddress, "Text"));

		click(InstaLoanHomePage.objAddress1, "Address");

//		click(InstaLoanHomePage.objAddress,getTextVal(InstaLoanHomePage.objAddress,"Text"));
		click(InstaLoanHomePage.objProceedBtn, getTextVal(InstaLoanHomePage.objProceedBtn, "Text"));
		addbankAccount("5" + RandomIntegerGenerator(4), firstName + " " + lastName);

		if (verifyElementPresent(InstaLoanHomePage.objOffer, getTextVal(InstaLoanHomePage.objOffer, "Text"))) {
			verifyElementPresentAndClick(InstaLoanHomePage.objCheckBox, "Check Box");
			verifyElementPresentAndClick(InstaLoanHomePage.objAcceptOffer,getTextVal(InstaLoanHomePage.objAcceptOffer, "Text"));
//			verifyElementPresentAndClick(InstaLoanHomePage.objConfirmOffer, getTextVal(InstaLoanHomePage.objConfirmOffer, "Text"));
			waitTime(90000);
		}
		verifyElementPresentAndClick(InstaLoanPage.objHomeBtn, getText(InstaLoanPage.objHomeBtn) + " Button");
		if (!verifyElementPresent2(HomePage.objPayNowBtnAtHomePage, "Pay Now Button")) {
			waitTime(120000);
			verifyElementPresentAndClick(HomePage.objUPI, "UPI Tab");
			waitTime(90000);
			Back(1);
		}

		waitTime(5000);
		Swipe("DOWN", 2);
		payNowInstaLoan();
		executeUpdateOptional("Update db_txn_service.instaloan_whitelisted_users set eligible_type='OPTIONAL' where user_reference_number='"+ userReferenceNo + "';","Select * from db_txn_service.instaloan_whitelisted_users where user_reference_number='" + userReferenceNo+ "';");
//		executeQuery("SELECT * FROM db_txn_service.instaloan_whitelisted_users Where user_reference_number = '" + userReferenceNo + "';");

		click(HomePage.objScanAndPay, "Scan And Pay");

		if (verifyElementPresent(HomePage.objScanAndPay, "Scan and Pay Button")) {
			click(HomePage.objScanAndPay, "Scan and Pay Button");
			verifyElementPresent(HomePage.objScanAnyQRToPay, "Scan Any QR To Pay");
			Back(1);
		}
		verifyElementPresentAndClick(InstaLoanPage.objApplyNowBtn, "");
		click(InstaLoanHomePage.objSelectAddress, getTextVal(InstaLoanHomePage.objSelectAddress, "Text"));
		click(InstaLoanHomePage.objAddress1, "Address");
		// click(InstaLoanHomePage.objAddress,getTextVal(InstaLoanHomePage.objAddress,"Text"));
		click(InstaLoanHomePage.objProceedBtn, getTextVal(InstaLoanHomePage.objProceedBtn, "Text"));

		explicitWaitVisibilityNonDefault(InstaLoanHomePage.objOffer, 10);
		if (verifyElementPresent(InstaLoanHomePage.objAddBankDetails,getTextVal(InstaLoanHomePage.objAddBankDetails, "Text"))) {
			click(InstaLoanHomePage.objAddBankDetails, getTextVal(InstaLoanHomePage.objAddBankDetails, "Text"));
			if (verifyElementPresent(InstaLoanHomePage.objExistingBankAcc,getTextVal(InstaLoanHomePage.objExistingBankAcc, "Text"))) {
				click(InstaLoanHomePage.objExistingBank, "Existing Bank Account");
			}
		}
		if (verifyElementPresent(InstaLoanHomePage.objOffer, getTextVal(InstaLoanHomePage.objOffer, "Text"))) {
			verifyElementPresentAndClick(InstaLoanHomePage.objCheckBox, "Check Box");
			verifyElementPresentAndClick(InstaLoanHomePage.objAcceptOffer,getTextVal(InstaLoanHomePage.objAcceptOffer, "Text"));
			verifyElementPresentAndClick(InstaLoanHomePage.objConfirmOffer,getTextVal(InstaLoanHomePage.objConfirmOffer, "Text"));
			waitTime(90000);
		}
		verifyElementPresentAndClick(InstaLoanPage.objHomeBtn, getText(InstaLoanPage.objHomeBtn) + " Button");
		verifyElementPresentAndClick(HomePage.objScanAndPay, "Scan and Pay Button");
		if (verifyElementPresent(InstaLoanHomePage.objSorryMsg1, getTextVal(InstaLoanHomePage.objSorryMsg1, "Text"))) {
			verifyElementPresent(InstaLoanHomePage.objSorryMsg2, getTextVal(InstaLoanHomePage.objSorryMsg2, "Text"));
			logger.info("TC_Insta_loan_105,  Ring should be active for optional journey till loan get CA");
			extent.extentLoggerPass("TC_Insta_loan_105","TC_Insta_loan_105,  Ring should be active for optional journey till loan get CA");
			System.out.println("-----------------------------------------------------------");
		}
	}

	public String instaLoanWhitelistOnBoardingMandatory() throws Exception {
		extent.HeaderChildNode("WhiteList Logic for Mandatory Journey");
		//instalonAPIKycVerification();
		loginOnboarding("1");
		click(RingUserDetailPage.objNone, "None of the Above Button");
		mobileNoValidation1(mobileNumber);
		enterOtp(prop.getproperty("OTP"));
		readAndAccept();
		waitTime(30000);
		instaUserDetails(firstName, lastName, mothersName, email, gender);
		instalonAPIKycVerification(mobileNumber);
		instaNewCommunicationAddress();
		waitTime(10000);
		String ref = instaPanCardDetails(prop.getproperty("RingAdminEmail"), prop.getproperty("RingAdminPassword"),prop.getproperty("RingAdminOTP"), "MANDATORY", "13000", "2311", "2319");
		panCardDetails();
		waitTime(5000);
		instaLoancongratsScreen("Congrats");
		instaLoanSetPin(prop.getproperty("InstaLoanMPIN"), prop.getproperty("InstaLoanMPIN"));
		waitTime(5000);
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		waitTime(2000);
		instaLoanCoachPopup();
		return ref;
	}
// ===========================================================Mandatory Journey End===============================================================//
	
	//---------------------------------------------------------TShashi Code End---------------------------------------------------------//
	
	
	// ========================================================Kiran Code Start=========================================================//

	public void suspendedDependantMethods(String MobileNO) throws Exception {
		getDriver().resetApp();
		loginOnboarding("1");
		mobileNoValidation1(MobileNO);
		enterOtp(prop.getproperty("OTP"));
		readAndAccept();
	}

	public void sqlValidation(String userReferenceNumber, String status) throws Exception {
		ResultSet s = Utilities.executeQuery11("SELECT * FROM db_txn_service.lines where user_reference_number= '" + userReferenceNumber + "';");
		String values = null;
		while (s.next()) {
			values = (s.getInt(1) + " " + s.getString(2) + " " + s.getString(3) + " " + s.getString(4) + " "
					+ s.getString(5) + " " + s.getString(6) + " " + s.getString(7) + " " + s.getString(8));
		}
		logger.info("Before Update " + values);
		String subString = values.substring(78, 84);
		con.close();
		if (subString.equalsIgnoreCase("ACTIVE")) {
			logger.info("Changing the status ACTIVE to TERMINATED");
			executeUpdate(
					"UPDATE db_txn_service.lines SET status = '" + status + "' WHERE user_reference_number = '"
							+ userReferenceNumber + "';",
					"SELECT * FROM db_txn_service.lines where user_reference_number= '" + userReferenceNumber + "';");
		}
	}

	public void scanAndPayuserSuspendedTerminateCase() throws Exception {
		extent.HeaderChildNode("Scan & Pay User Suspended Terminate Cases");
		getDriver().resetApp();
		suspendedDependantMethods("7085400548");
		explicitWaitVisibilityNonDefault(RingUserDetailPage.objCrossBtn, 20);
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		waitTime(2000);
		click(UserSuspendedCases.objScanAndPayBtn, "Scan And Pay Button");
		
		Boolean repayNowMsg = verifyElementPresent(UserSuspendedCases.objRepayNowToastMsg, "Repay message");
		if (repayNowMsg) {
			logger.info(getTextVal(UserSuspendedCases.objRepayNowToastMsg, "Toast Msg displayed"));
			extent.extentLogger("ToastMsg", getTextVal(UserSuspendedCases.objRepayNowToastMsg, "Toast Msg displayed"));
			softAssertion.assertEquals("Complete your profile to unlock Scan & Pay",getTextVal(UserSuspendedCases.objRepayNowToastMsg, "Toast Msg displayed"));
			logger.info("Scan & Pay Button is Disabled");
			extent.extentLogger("Disabled", "Scan & Pay Button is Disabled");
			extent.extentLogger("TC_Ring_Core_267","'TC_Ring_Core_267' To Verify when user is suspended  and trying clicking on scan & pay");
		}

		waitTime(2000);
		suspendedDependantMethods("9930244553");
		explicitWaitVisibilityNonDefault(RingUserDetailPage.objCrossBtn, 20);
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		click(UserSuspendedCases.objScanAndPayBtn, "Scan And Pay Button");
		String oOpsMsg = getText(UserSuspendedCases.objOopsMsg);
		String technicalIssueMsg = getText(UserSuspendedCases.objTecnicalIssueMsg);
		String unlockMsg = getText(UserSuspendedCases.objunlockMsg);
		logger.info(oOpsMsg + " " + technicalIssueMsg + " " + unlockMsg);
		extent.extentLogger("Messages", oOpsMsg + " " + technicalIssueMsg + " " + unlockMsg);
		verifyElementPresent(UserSuspendedCases.objpayNowBtn, getTextVal(UserSuspendedCases.objpayNowBtn, "Button"));
		extent.extentLogger("TC_Ring_Core_268","'TC_Ring_Core_268' To Verify when user is terminated by DPD but Dues and outstandings are pending tries scan & pay");

		waitTime(2000);
		suspendedDependantMethods("3405927918");
		explicitWaitVisibilityNonDefault(RingUserDetailPage.objCrossBtn, 20);
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		click(UserSuspendedCases.objScanAndPayBtn, "Scan And Pay Button");
		String modeofpaymentMsg = getText(UserSuspendedCases.objRepayNowToastMsg);
		logger.info(oOpsMsg + " " + modeofpaymentMsg);
		extent.extentLogger("Message", oOpsMsg + " " + modeofpaymentMsg);
		//verifyElementPresent(UserSuspendedCases.objGotItBtn, getTextVal(UserSuspendedCases.objGotItBtn, "Button"));
		extent.extentLogger("TC_Ring_Core_269","'TC_Ring_Core_269' To Verify when user is terminated by DPD but no Dues and outstandings are pending tries scan & pay");

		waitTime(2000);
		suspendedDependantMethods("6364546466");
		explicitWaitVisibilityNonDefault(RingUserDetailPage.objCrossBtn, 20);
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		waitTime(2000);
		sqlValidation("IDEP4116896212382OBS", "TERMINATED");
		click(UserSuspendedCases.objScanAndPayBtn, "Scan And Pay Button");
		logger.info(oOpsMsg + " " + technicalIssueMsg + " " + unlockMsg);
		extent.extentLogger("Messages", oOpsMsg + " " + technicalIssueMsg + " " + unlockMsg);
		explicitWaitVisibilityNonDefault(UserSuspendedCases.objpayNowBtnDif, 10);
		verifyElementPresent(UserSuspendedCases.objpayNowBtnDif,getTextVal(UserSuspendedCases.objpayNowBtnDif, "Button"));
		extent.extentLogger("TC_Ring_Core_270","'TC_Ring_Core_270' To Verify when user is terminated by Manually but Dues and outstandings are pending tries scan & pay");

		waitTime(2000);
		suspendedDependantMethods("6079464664");
		explicitWaitVisibilityNonDefault(RingUserDetailPage.objCrossBtn, 20);
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		waitTime(2000);
		sqlValidation("IDEP4116896212382OBS", "TERMINATED");
		click(UserSuspendedCases.objScanAndPayBtn, "Scan And Pay Button");
		logger.info(oOpsMsg + " " + modeofpaymentMsg);
		extent.extentLogger("Message", oOpsMsg + " " + modeofpaymentMsg);
		verifyElementPresent(UserSuspendedCases.objGotItBtn, getTextVal(UserSuspendedCases.objGotItBtn, "Button"));
		extent.extentLogger("TC_Ring_Core_271","'TC_Ring_Core_271' To Verify when user is terminated by manually DPD but no Dues and outstandings are pending tries scan & pay");

		waitTime(2000);
		suspendedDependantMethods("9764695535");
		explicitWaitVisibilityNonDefault(RingUserDetailPage.objCrossBtn, 20);
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		waitTime(2000);
		click(UserSuspendedCases.objScanAndPayBtn, "Scan And Pay Button");
		logger.info(oOpsMsg + " " + technicalIssueMsg + " " + unlockMsg);
		extent.extentLogger("Messages", oOpsMsg + " " + technicalIssueMsg + " " + unlockMsg);
		verifyElementPresent(UserSuspendedCases.objpayNowBtnDif,getTextVal(UserSuspendedCases.objpayNowBtnDif, "Button"));
		extent.extentLogger("TC_Ring_Core_272","'TC_Ring_Core_272' To Verify when user is terminated by Fraud checks but Dues and outstandings are pending tries scan & pay");

		waitTime(2000);
		suspendedDependantMethods("9466491349");
		explicitWaitVisibilityNonDefault(RingUserDetailPage.objCrossBtn, 20);
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		waitTime(2000);
		click(UserSuspendedCases.objScanAndPayBtn, "Scan And Pay Button");
		logger.info(oOpsMsg + " " + modeofpaymentMsg);
		extent.extentLogger("Message", oOpsMsg + " " + modeofpaymentMsg);
		verifyElementPresent(UserSuspendedCases.objGotItBtn, getTextVal(UserSuspendedCases.objGotItBtn, "Button"));
		extent.extentLogger("TC_Ring_Core_273","'TC_Ring_Core_273' To Verify when user is terminated by fraud checks but no Dues and outstandings are pending tries scan & pay");
		
		bankTransferuserSuspendedTerminateCase();
	}

	public void bankTransferuserSuspendedTerminateCase() throws Exception {
		extent.HeaderChildNode("Bank Transfer User Suspended Terminate Cases");

		suspendedDependantMethods("9930244553");
		verifyElementPresent2(RingUserDetailPage.objCrossBtn, "Cross Button");
		click(RingUserDetailPage.objCrossBtn, "Cross Button");
		verifyElementPresent2(BankTransferModule.objMoretTab, "Clicked On More Button");
		click(BankTransferModule.objMoretTab, "Clicked On More Button");
		verifyElementPresent2(BankTransferModule.objBankTransfer,getTextVal(BankTransferModule.objBankTransfer, "Module"));
		click(BankTransferModule.objBankTransfer, "Bank Transfer Module");
		String unpaidBillsErrMsg = getText(UserSuspendedCases.objUhOhErrorMsg);
		String repayMsg = getText(UserSuspendedCases.objKindlyRepayErrMsg);
		logger.info(unpaidBillsErrMsg + " " + repayMsg);
		extent.extentLogger("Msg", unpaidBillsErrMsg + " " + repayMsg);
		extent.extentLogger("TC_Ring_Core_274","'TC_Ring_Core_274' To Verify when user is suspended Tries bank transfer");

		suspendedDependantMethods("7085400548");
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		click(BankTransferModule.objMoretTab, "Clicked On More Button");
		verifyElementPresent(BankTransferModule.objBankTransfer,getTextVal(BankTransferModule.objBankTransfer, "Module"));
		click(BankTransferModule.objBankTransfer, "Bank Transfer Module");
		String oOpsMsg = getText(UserSuspendedCases.objOopsMsg);
		String technicalIssueMsg = getText(UserSuspendedCases.objTecnicalIssueMsg);
		String unlockMsg = getText(UserSuspendedCases.objunlockMsg);
		logger.info(oOpsMsg + " " + technicalIssueMsg + " " + unlockMsg);
		extent.extentLogger("Messages", oOpsMsg + " " + technicalIssueMsg + " " + unlockMsg);
		verifyElementPresent(UserSuspendedCases.objpayNowBtn, getTextVal(UserSuspendedCases.objpayNowBtn, "Button"));
		extent.extentLogger("TC_Ring_Core_275","'TC_Ring_Core_275' To Verify when user is terminated by DPD but Dues and outstandings are pending tries Bank transfer");

		suspendedDependantMethods("3405927918");
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		click(BankTransferModule.objMoretTab, "More Button");
		verifyElementPresent(BankTransferModule.objBankTransfer,getTextVal(BankTransferModule.objBankTransfer, "Module"));
		click(BankTransferModule.objBankTransfer, "Bank Transfer Module");
		String modeofpaymentMsg = getText(UserSuspendedCases.objmodeOfPaymentMsg);
		logger.info(oOpsMsg + " " + modeofpaymentMsg);
		extent.extentLogger("Message", oOpsMsg + " " + modeofpaymentMsg);
		verifyElementPresent(UserSuspendedCases.objGotItBtn, getTextVal(UserSuspendedCases.objGotItBtn, "Button"));
		extent.extentLogger("TC_Ring_Core_276","'TC_Ring_Core_276' To Verify when user is terminated by DPD but no Dues and outstandings are pending tries bank transfer");

		suspendedDependantMethods("6364546466");
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		click(BankTransferModule.objMoretTab, "Clicked On More Button");
		verifyElementPresent(BankTransferModule.objBankTransfer,getTextVal(BankTransferModule.objBankTransfer, "Module"));
		waitTime(2000);
		sqlValidation("IDEP4116896212382OBS", "TERMINATED");
		click(BankTransferModule.objBankTransfer, "Bank Transfer Module");
		logger.info(oOpsMsg + " " + technicalIssueMsg + " " + unlockMsg);
		extent.extentLogger("Messages", oOpsMsg + " " + technicalIssueMsg + " " + unlockMsg);
		explicitWaitVisibilityNonDefault(UserSuspendedCases.objpayNowBtnDif, 10);
		verifyElementPresent(UserSuspendedCases.objpayNowBtnDif,getTextVal(UserSuspendedCases.objpayNowBtnDif, "Button"));
		extent.extentLogger("TC_Ring_Core_277","'TC_Ring_Core_277' To Verify when user is terminated by Manually but Dues and outstandings are pending tries bank transfer");

		suspendedDependantMethods("6079464664");
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		click(BankTransferModule.objMoretTab, "Clicked On More Button");
		verifyElementPresent(BankTransferModule.objBankTransfer,getTextVal(BankTransferModule.objBankTransfer, "Module"));
		waitTime(2000);
		sqlValidation("IDEP4116896212382OBS", "TERMINATED");
		click(BankTransferModule.objBankTransfer, "Bank Transfer Module");
		logger.info(oOpsMsg + " " + modeofpaymentMsg);
		extent.extentLogger("Message", oOpsMsg + " " + modeofpaymentMsg);
		verifyElementPresent(UserSuspendedCases.objGotItBtn, getTextVal(UserSuspendedCases.objGotItBtn, "Button"));
		extent.extentLogger("TC_Ring_Core_278","'TC_Ring_Core_278' To Verify when user is terminated by manually DPD but no Dues and outstandings are pending tries bank transfer");

		suspendedDependantMethods("9764695535");
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		click(BankTransferModule.objMoretTab, "Clicked On More Button");
		verifyElementPresent(BankTransferModule.objBankTransfer,
				getTextVal(BankTransferModule.objBankTransfer, "Module"));
		click(BankTransferModule.objBankTransfer, "Bank Transfer Module");
		logger.info(oOpsMsg + " " + technicalIssueMsg + " " + unlockMsg);
		extent.extentLogger("Messages", oOpsMsg + " " + technicalIssueMsg + " " + unlockMsg);
		verifyElementPresent(UserSuspendedCases.objpayNowBtnDif,getTextVal(UserSuspendedCases.objpayNowBtnDif, "Button"));
		extent.extentLogger("TC_Ring_Core_279","'TC_Ring_Core_279' To Verify when user is terminated by Fraud checks but Dues and outstandings are pending tries bank transfer");

		suspendedDependantMethods("9466491349");
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		click(BankTransferModule.objMoretTab, "Clicked On More Button");
		verifyElementPresent(BankTransferModule.objBankTransfer,getTextVal(BankTransferModule.objBankTransfer, "Module"));
		click(BankTransferModule.objBankTransfer, "Bank Transfer Module");
		logger.info(oOpsMsg + " " + modeofpaymentMsg);
		extent.extentLogger("Message", oOpsMsg + " " + modeofpaymentMsg);
		verifyElementPresent(UserSuspendedCases.objGotItBtn, getTextVal(UserSuspendedCases.objGotItBtn, "Button"));
		extent.extentLogger("TC_Ring_Core_280","'TC_Ring_Core_280' To Verify when user is terminated by fraud checks but no Dues and outstandings are pending tries bank transfer");

	}

	public void instaLoanOfferPage() throws Exception {
		extent.HeaderChildNode("Insta Loan Offer Page");
		getDriver().resetApp();
		instaLoanWhitelistLogic();
//		loginOnboarding("1");
//		mobileNoValidation1("6242739082"); // 6242739082//8522170918//9516125975//7952394810
//		enterOtp(prop.getproperty("OTP"));
//		readAndAccept();

		waitTime(3000);
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		waitTime(2000);
		verifyElementPresentAndClick(InstaOfferPage.instaPopupBannerCrossBtn, "Cross Button");
		if (verifyElementPresent2(InstaOfferPage.objOkayGotIt, "Okay, Got It!")) {
			click(InstaOfferPage.objOkayGotIt, "Okay, Got It!");
		}
		waitTime(2000);
		Swipe("up", 2);
		click(InstaOfferPage.objApplyBtn, getText(InstaOfferPage.objApplyBtn) + " Button is displayed");
		waitTime(10000);

		click(InstaOfferPage.objContinueBtn, "Continue Button");
		verifyElementPresentAndClick(InstaOfferPage.objFatherName, "Father Name Field");
		type(InstaOfferPage.objFatherName, "Arun", "Father Name Field");
		click(InstaOfferPage.objProceedBtn, "Proceed Button");
		waitTime(2000);
		// click(InstaOfferPage.objProceedBtn, "Proceed Button");

		int count = 0;
		for (int i = 0; i < count; i++) {
			verifyElementPresentAndClick(InstaOfferPage.objProceedBtn, "Proceed Button");
			if (verifyElementPresent(InstaOfferPage.objselectAddress, "Select Address Dropdown")) {
				break;
			}
			count++;
		}

		waitTime(3000);
		click(InstaOfferPage.objselectAddress, "Select Address DropDown");
		click(InstaOfferPage.objAddress, getTextVal(InstaOfferPage.objAddress, "Addresss"));
		click(InstaOfferPage.objProceedBtn, "Proceed Button");

		verifyElementPresent(InstaOfferPage.objOfferPageHeader, "Offer Page");
		verifyElementPresent(InstaOfferPage.objInstallment, "Installment Tab");
		getTextVal1(InstaOfferPage.objDisbursedAmountValue, "Disbursal Amount");
		getTextVal1(InstaOfferPage.objEmiAmountValue, "EMI Amount");
		getTextVal1(InstaOfferPage.objTenure, "Tenure Period");
		extent.extentLogger("TC_Insta_loan_138", "'TC_Insta_loan_138' To verify the Offer page of the insta loan");

		getTextVal1(InstaOfferPage.objTotalInterestRate, "Total Interest");
		getTextVal1(InstaOfferPage.ObjAprRate, "APR Rate");
		verifyElementPresentAndClick(InstaOfferPage.objFeeStructure, "Detailed Fee Structure");
		extent.extentLogger("TC_Insta_loan_139", "'TC_Insta_loan_139' To verify the Offer page for KFS and APR");

		waitTime(2000);
		Swipe("UP", 2);
		tapUsingCoordinates(595, 1260);
		extent.extentLogger("TC_Insta_loan_148",
				"'TC_Insta_loan_148' To verify on the offer page, know more link content only visible for the Optional eligible type user ");
		verifyElementPresentAndClick(InstaOfferPage.objBottomSheetCrossBtn, "Important bottom sheet popup");
		extent.extentLogger("TC_Insta_loan_147",
				"'TC_Insta_loan_147', To verify when the user click on the know more link from offer page,  the bottom sheet popup should be displayed (Important popup without button)");

		click(InstaOfferPage.objAddBankDetailsBtn, "Add Bank Details");
		if (verifyElementPresent(InstaOfferPage.objExsistingAccPage, "Existing Bank Account")) {
			click(InstaOfferPage.objAddNewAbnkAccBtn, "Add New Bank Account");
		}

		addbankAccount("5" + RandomIntegerGenerator(5), "Ramaswamy");
		extent.extentLogger("TC_Insta_loan_144",
				"'TC_Insta_loan_144' To verify If users have existing Banks with less than 60% name match don’t show them in the Bank list. Name match % for Bank should be => 60%");
		extent.extentLogger("TC_Insta_loan_153",
				"'TC_Insta_loan_153' To verify users Invalid Bank account by Penny Drop");

		click(InstaOfferPage.objReEnterBankDetailsBtn, "Re-Enter Bank Details");

		addbankAccount("5" + RandomIntegerGenerator(5), firstName); // Make generic here as firstName
		waitTime(2000);
		tapUsingCoordinates(314, 1340);
		verifyElementPresent(InstaOfferPage.objTermsAndCondition, "Terms & Condition's Page");
		click(InstaOfferPage.objBackBtn, "Back Button");
		extent.extentLogger("TC_Insta_loan_140",
				"'TC_Insta_loan_140' To verify the KFS link should present with terms & condition Link");
		extent.extentLogger("TC_Insta_loan_145",
				"'TC_Insta_loan_145' To verify the penny drop verification type cases while the time of add bank");
		extent.extentLogger("TC_Insta_loan_142",
				"'TC_Insta_loan_142' To verify user able to Add bank on the offer page");

		tapUsingCoordinates(506, 1340);
		if (verifyElementPresent(InstaOfferPage.objOpenWithTab, "Saction Letter download Open With Tab")) {
			Back(1);
		}
		extent.extentLogger("TC_Insta_loan_141",
				"To Verify saction letter and KFS should download when click on Key fact statement link");

		getTextVal1(InstaOfferPage.objConditionLinks, "Link");
		getTextVal1(InstaOfferPage.objBankName, "Bank Name");
		extent.extentLogger("TC_Insta_loan_143",
				"TC_Insta_loan_143, To verify is user selects bank the selected bank should be visible on offer page");

		click(InstaOfferPage.objChangeBank, "Change Bank Link");
		verifyElementPresent(InstaOfferPage.objExsistingAccPage, "Select Bank Account Page");
		extent.extentLogger("TC_Insta_loan_150",
				"'TC_Insta_loan_150' To verify when the user click on the change bank link redirected to select bank account page");
		click(InstaOfferPage.objAddBankDetailsBtn, "Add Bank Details");
		verifyElementPresent(InstaOfferPage.objExistingBankAccSelect,
				"User redirected to add bank details page to add another bank");
		click(InstaOfferPage.objExistingBankAccSelect, "Select Existing Bank Account Button");

		click(InstaOfferPage.objExistingAccountNameSel, "Already Existing User");

		extent.extentLogger("TC_Insta_loan_151",
				"'TC_Insta_loan_151' To verify when user add another bank then it redirected to Add bank account");
		extent.extentLogger("TC_Insta_loan_152",
				"'TC_Insta_loan_152', To verify users Valid Bank account by Penny Drop");

		click(InstaOfferPage.objCheckBox, "Terms and Condition Check Box");
		verifyElementPresentAndClick(InstaOfferPage.objAcceptOfferBtn, "Accept Offer Button");

		verifyElementPresent(InstaOfferPage.objBottomSheetPopup, "Important Bottom Sheet Pop up");
		click(InstaOfferPage.objConfirmOfferBtn, "Confirm Offer Button");
		extent.extentLogger("TC_Insta_loan_146",
				"'TC_Insta_loan_146' To verify after adding bank details, when the user clicks on an accept term and condition check box and accepts offer button then the bottom sheet popup(Important ) should be displayed with confirm offer button.");

		verifyElementPresent(InstaOfferPage.objLoader, "Transferring money loader screen");
		extent.extentLogger("TC_Insta_loan_154",
				"'TC_Insta_loan_154' To verify when the user accept an offer then transferring money loader screen should be displayed");

		explicitWaitVisibilityNonDefault(InstaOfferPage.objDisbursementSuccessful, 60);
		getTextVal1(InstaOfferPage.objDisbursementSuccessful, "Message");
		getTextVal1(InstaOfferPage.objLoanAmount, "Disbursed Loan Amount");
		extent.extentLogger("TC_Insta_loan_155",
				"'TC_Insta_loan_155'To verify user Insta Loan Disbursement Successful ");

		verifyElementPresentAndClick(InstaOfferPage.objHomeButton, "Home Button");
		extent.extentLogger("TC_Insta_loan_156",
				"'TC_Insta_loan_156' To verify payment successful page with home button");

		// Repayment journey //7952394810//6512703939
		click(InstaOfferPage.objViewDetails, "View Details");
		click(InstaOfferPage.objFullPaymentBtn, "Pay Full Amount Radio Button");
		click(InstaOfferPage.objPayNowBtn, "Pay Now");

		waitTime(2000);
		String netBankingPayment = getText(InstaOfferPage.objNetBankingOrDebitCard);
		logger.info(netBankingPayment);
		click(InstaOfferPage.objNetBankingOrDebitCard, getText(InstaOfferPage.objNetBankingOrDebitCard));
		// explicitWaitVisibilityNonDefault(InstaOfferPage.objCardTypePayment, 60);
		waitTime(10000);
		String paymentype = getText(InstaOfferPage.objCardTypePayment);
		logger.info(paymentype);
		click(InstaOfferPage.objCardTypePayCrossBtn, "Cross Button in Payment Page");

		choosePaymentMethod(netBankingPayment, paymentype);

		click(InstaOfferPage.objPaywithoutSavingCardBtn, "Pay without Saving Card Button");
		click(InstaOfferPage.objSuccessBtn, " Success Button");
		logger.info(getTextVal1(InstaOfferPage.objPaymentSuccessfulMsg, "Message"));
		click(InstaOfferPage.objGotoHomePageBtn, "Go to Home Page");
		if (verifyElementPresent(RingUserDetailPage.objCrossBtn, "Cross Button")) {
			click(RingUserDetailPage.objCrossBtn, "Cross Button");
		}
		verifyElementPresent(InstaOfferPage.objReapplyInstaLoanBanner, "ReApply For Insta Loan Banner");
		click(InstaOfferPage.objApplyNowBtn, "Apply Now Button");
		extent.extentLogger("TC_Insta_loan_159",
				"'TC_Insta_loan_159', To verify when user completes the repayment of the ring limit, Repayment done screen should be visible with insta loan banner");

		// Add Pulkit code

	}

	public void choosePaymentMethod(String paymentMethod, String typeOfMethod) throws Exception {
		switch (paymentMethod) {
		case "Net Banking & Debit Card":
			click(InstaOfferPage.objNetBankingOrDebitCard, getText(InstaOfferPage.objNetBankingOrDebitCard));
			waitTime(5000);
			List<WebElement> values = getDriver().findElements(InstaOfferPage.objPaymentTypes);
			for (int i = 0; i <= values.size(); i++) {
				String paymentMethods = values.get(i).getText();
				System.out.println("Payment Types : " + paymentMethods);
				if (paymentMethods.equalsIgnoreCase(typeOfMethod)) {
					waitTime(2000);
					getDriver().findElements(InstaOfferPage.objPaymentTypes).get(i).click();
					break;
				}
			}
			explicitWaitVisibilityNonDefault(InstaOfferPage.objAddNewCard, 30);
			click(InstaOfferPage.objCardNumberField, "Card Number Field");
			type(InstaOfferPage.objCardNumberField, "4111111111111111", "Card Number Field");
			click(InstaOfferPage.objExpiryDateField, "ExpiryDate Field");
			type(InstaOfferPage.objExpiryDateField, "1025", "ExpiryDate Field");
			click(InstaOfferPage.objCardHolderName, "Card Holder Name Field");
			type(InstaOfferPage.objCardHolderName, "anuj", "Card Holder Name Field");
			click(InstaOfferPage.objCvvField, "CVV Field");
			type(InstaOfferPage.objCvvField, "123", "CVV Field");
			click(InstaOfferPage.objPayNowBtn, "Pay Now Button");
			break;

		default:
			logger.info("Invalid Payment Methods");
		}
	}
//=====================================Kiran Code End===============================================//
	
//=====================================Shakir Hussain Ring Policy Start===============================================//

	
	public String oldAdminPanelGBGrid() throws Exception{
		Utilities.setPlatform = "Web";
        new CommandBase("Chrome");
        waitTime(4000);
        getWebDriver().get("https://ci-api-gateway.test.ideopay.in/admin/login");
        
        click(InstaLoanPage.objEmail, "Email Field");
		type(InstaLoanPage.objEmail, "shakir.muchumarri@collabera.com", "Email Field");
		verifyElementPresentAndClick(InstaLoanPage.objContinueBtn, getText(InstaLoanPage.objContinueBtn));
		click(InstaLoanPage.objPasswordField, "Password Field");
		clearField(InstaLoanPage.objPasswordField, "Password Field");
		type(InstaLoanPage.objPasswordField, "123456", "Password Field");
		click(InstaLoanPage.objOTPField, "OTP Field");
		clearField(InstaLoanPage.objOTPField, "OTP Field");
		type(InstaLoanPage.objOTPField, "888888", "OTP Field");
		verifyElementPresentAndClick(InstaLoanPage.objLoginBtn, getText(InstaLoanPage.objLoginBtn));
		verifyElementPresentAndClick(InstaLoanPage.objPurple,getText(InstaLoanPage.objPurple));
		verifyElementPresentAndClick(InstaLoanPage.objGBGrid,getText(InstaLoanPage.objGBGrid));
		selectByVisibleTextFromDD(InstaLoanPage.objModelVersion,"FRESH-V17");
		click(InstaLoanPage.objSubmitFilter,"Submit filter");
		verifyElementPresentAndClick(InstaLoanPage.objEditGrid,"Edit Grid");
		ScrollToTheElement(InstaLoanPage.objBC1RejectBand);
		click(InstaLoanPage.objBC1RejectBand,getText(InstaLoanPage.objBC1RejectBand));
		waitTime(2000);
		click(InstaLoanPage.objUpdateBtn,"Update reject template");
		explicitWaitVisibilityNonDefault(InstaLoanPage.objPassword,10);
		type(InstaLoanPage.objPassword,"123456","password field");
		String reject_score = getText(InstaLoanPage.objRejectAppScore);
		click(InstaLoanPage.objUpdateSubmit,"Update the reject band submit");
		
		return reject_score;
        
	}
	public void newAdminPanel_appScore(String app_score) throws Exception{
		extent.HeaderChildNode("Updating app score");
		Utilities.setPlatform = "Web";
        new CommandBase("Chrome");
        waitTime(4000);
		getWebDriver().get("https://new-admin-panel.test.ideopay.in/config/configuration?search=app_score&");
		type(InstaLoanPage.objEmail,"shakir.muchumarri@collabera.com","Email text field");
		click(InstaLoanPage.objContinue,"Continue Button");
		waitTime(3000);
		click(InstaLoanPage.objEditConfBtn,"Edit Configuration Buton");
		waitTime(2000);
		waitTime(2000);
		clearWebField(InstaLoanPage.objMetaValue);
		waitTime(2000);
		type(InstaLoanPage.objMetaValue,app_score,"app score meta value field");
		waitTime(2000);
		click(InstaLoanPage.objSubmit,"Submit button");
		waitTime(2000);
	}
	
	public String regularOfferReusable(String cibil_score,String app_score,boolean age_flag) throws Exception{
		extent.HeaderChildNode("Ring Policy - Reusable Method for regular offer test cases");
		
		enablePermissions();
		Aclick(RingLoginPage.objLoginLink,"Sign up login link");
		loginMobile();
		String mobNo = "7" + RandomIntegerGenerator(9);
		mobileNoValidation2(mobNo);
		System.out.println(mobNo);
		enterOtp("888888");
		readAndAccept();
		waitTime(3000);
		Utilities.setPlatform = "Web";
        new CommandBase("Chrome");
        waitTime(4000);
        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
		type(InstaLoanPage.objEmail,"shakir.muchumarri@collabera.com","Email text field");
		click(InstaLoanPage.objContinue,"Continue Button");
		waitTime(10000);
		explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
		String userRefNo = getText(InstaLoanPage.objUserRefNo);
		BrowsertearDown();
		newAdminPanel_appScore(app_score);
		BrowsertearDown();
		cibilDummy(cibil_score,userRefNo,"no","Sunil Chatla");
		setPlatform("Android");
		initDriver();
		waitTime(5000);
		userDetails();
		if(age_flag) {
			dateOfBirth("MAY","04","2002");
		}
		else {
			dateOfBirth("MAY","09","1994");
		}
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		waitTime(20000);
		return userRefNo;
		
	}
	public String RingPolicy_BlackBox(String appScore, boolean flag) throws Exception{
		extent.HeaderChildNode("Ring Policy - Reusable Method for BlackBox pool test cases");
		 String pool_pref="";
		newAdminPanel_appScore(appScore);
		BrowsertearDown();
		waitTime(5000);
		setPlatform("Android");
		initDriver();
		waitTime(5000);
		enablePermissions();
		Aclick(RingLoginPage.objLoginLink,"Sign up login link");
		loginMobile();
		String mobNo = "6" + RandomIntegerGenerator(9);
		mobileNoValidation2(mobNo);
		System.out.println(mobNo);
		enterOtp("888888");
		readAndAccept();
		waitTime(3000);
		Utilities.setPlatform = "Web";
        new CommandBase("Chrome");
        waitTime(4000);
        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
		type(InstaLoanPage.objEmail,"shakir.muchumarri@collabera.com","Email text field");
		click(InstaLoanPage.objContinue,"Continue Button");
		explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
		String userRefNo = getText(InstaLoanPage.objUserRefNo);
		waitTime(30000);
		setPlatform("Android");
		initDriver();
		waitTime(5000);
		userDetails();
		if(flag) {
			dateOfBirth("MAY","04","2002");
		}
		else {
			dateOfBirth("MAY","04","1997");
		}
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		waitTime(10000);
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		JSONObject obj = new JSONObject(json_data);
		if(obj.has("offer_preference")) {
			pool_pref = obj.getString("offer_preference");
			System.out.println(obj.getString("offer_preference"));
		}
		
		BrowsertearDown();
	    return pool_pref;
	}
	
	public String txnFeeCashFee(String appScore) throws Exception{
		extent.HeaderChildNode("Ring Policy - Txn fee & Cash fee test cases");
		
		enablePermissions();
		Aclick(RingLoginPage.objLoginLink,"Sign up login link");
		loginMobile();
		String mobNo = "7" + RandomIntegerGenerator(9);
		mobileNoValidation2(mobNo);
		System.out.println(mobNo);
		enterOtp("888888");
		readAndAccept();
		waitTime(3000);
		Utilities.setPlatform = "Web";
        new CommandBase("Chrome");
        waitTime(4000);
        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
		type(InstaLoanPage.objEmail,"shakir.muchumarri@collabera.com","Email text field");
		click(InstaLoanPage.objContinue,"Continue Button");
		waitTime(10000);
		explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
		String userRefNo = getText(InstaLoanPage.objUserRefNo);
		newAdminPanel_appScore(appScore);
		setPlatform("Android");
		initDriver();
		waitTime(5000);
		userDetails();
		
		dateOfBirth("MAY","09","1994");
		
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		waitTime(30000);
		
		return userRefNo;
	}
	public String RingPolicy_Digi(String appScore, boolean age_pref_flag,String ccAmt, Boolean digi_reliability) throws Exception{
		extent.HeaderChildNode("Ring Policy - Reusable Method for BlackBox pool test cases for digi users");
		 String pool_pref="";
		newAdminPanel_appScore(appScore);
		BrowsertearDown();
		waitTime(5000);
		setPlatform("Android");
		initDriver();
		waitTime(5000);
		enablePermissions();
		Aclick(RingLoginPage.objLoginLink,"Sign up login link");
		loginMobile();
		String mobNo = "6" + RandomIntegerGenerator(9);
		mobileNoValidation2(mobNo);
		System.out.println(mobNo);
		enterOtp("888888");
		readAndAccept();
		waitTime(3000);
		Utilities.setPlatform = "Web";
        new CommandBase("Chrome");
        waitTime(4000);
        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
		type(InstaLoanPage.objEmail,"shakir.muchumarri@collabera.com","Email text field");
		click(InstaLoanPage.objContinue,"Continue Button");
		waitTime(10000);
		explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
		String userRefNo = getText(InstaLoanPage.objUserRefNo);
		digiReliability(userRefNo,ccAmt,digi_reliability);
		BrowsertearDown();
		waitTime(5000);
		setPlatform("Android");
		initDriver();
		waitTime(5000);
		userDetails();
		if(age_pref_flag) {
			dateOfBirth("DEC","05","2002");
		}
		else {
			dateOfBirth("MAY","07","1996");
		}
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		waitTime(20000);
		return userRefNo;
	}
	
	public void digiReliability(String userRefNo,String ccAmount, Boolean digi_reliability) throws Exception{
		getWebDriver().get("https://data.test.ideopay.in/admin#!/test/update-digiscore");
		explicitWaitVisibilityNonDefault(DigiWeb.userName,10);
		type(DigiWeb.userName,prop.getproperty("digiUserName"),"User name field");
		click(DigiWeb.loginBtn,"Login button");
		waitTime(2000);
		type(DigiWeb.password,prop.getproperty("digiPassword"),"Password field");
		type(DigiWeb.otp,prop.getproperty("OTP"),"Otp field");
		click(DigiWeb.loginBtn1,"Login button");
		waitTime(3000);
		getWebDriver().get("https://data.test.ideopay.in/admin#!/test/update-digiscore");
		explicitWaitVisibilityNonDefault(DigiWeb.userRefNo,10);
		type(DigiWeb.userRefNo,userRefNo,"User Ref No");
		if(digi_reliability) {
			selectByVisibleTextFromDD(DigiWeb.digiSelect,prop.getproperty("digi_reliable"));
		}
		else {
			selectByVisibleTextFromDD(DigiWeb.digiSelect,"No");
		}
		type(DigiWeb.salaryV1,prop.getproperty("salary_v1"),"Salary V1");
		type(DigiWeb.incomeV1,prop.getproperty("income_v1"),"Income V1");
		type(DigiWeb.incomeV2,prop.getproperty("digi_income_v2"),"Income V2");
		if(ccAmount!=null) {
			ScrollToTheElement(DigiWeb.ccWithdraw);
			waitTime(2000);
			type(DigiWeb.ccWithdraw,ccAmount,"CC Withdraw Amount");
		}
		
		
		type(DigiWeb.socialScore,prop.getproperty("social_score"),"Social Score");
		type(DigiWeb.timeStamp,prop.getproperty("timeStamp"),"Time Stamp");
		ScrollToTheElement(DigiWeb.custNameMatch);
		waitTime(2000);
		selectByVisibleTextFromDD(DigiWeb.custNameMatch,prop.getproperty("customer_name_match"));
		selectByVisibleTextFromDD(DigiWeb.dupNoSelect,prop.getproperty("duplicate_imei"));
		ScrollToTheElement(DigiWeb.updateDigiScoreBtn);
		waitTime(2000);
		click(DigiWeb.updateDigiScoreBtn,"Update digi score button");
		waitTime(15000);
		
	}
	
	public String blockUsers(String url, String gender, String encryptedName,String flag) throws Exception{
		HashMap map = mockUserAPI(url, gender,encryptedName);
		String mobNo = map.get("mobileNumber").toString();
		String imei = map.get("imei").toString();
		String pan_card = map.get("panCard").toString();
		enablePermissions();
		Aclick(RingLoginPage.objLoginLink,"Sign up login link");
		loginMobile();
		mobileNoValidation2(mobNo);
		System.out.println(mobNo);
		enterOtp("888888");
		readAndAccept();
		waitTime(3000);
		Utilities.setPlatform = "Web";
        new CommandBase("Chrome");
        waitTime(4000);
        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
		type(InstaLoanPage.objEmail,"shakir.muchumarri@collabera.com","Email text field");
		click(InstaLoanPage.objContinue,"Continue Button");
		waitTime(10000);
		explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
		String userRefNo = getText(InstaLoanPage.objUserRefNo);
		BrowsertearDown();
		waitTime(5000);
		setPlatform("Android");
		initDriver();
		waitTime(10000);
		userDetails();
		dateOfBirth("MAY","07","1996");
		if(flag.equals("mobile_block")) {
			executeInsert_block(mobNo,"mobile","test");
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
		}
		else if(flag.equals("imei_block")) {
			executeInsert_block(imei,"imei","test");
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
		}
		else if(flag.equals("pan_block")) {
			executeInsert_block(pan_card,"pan","test");
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
		}
		
		else if(flag.equals("after_block")) {
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
			executeInsert_block(mobNo,"mobile","test");
		}
		
		return userRefNo;
		
	}
	
	public String kla_cc_offer(String app_score,String cc_flag,String kla_flag) throws Exception{
		extent.HeaderChildNode("Ring Policy - Reusable Method for kla_cc offer test cases");
		
		enablePermissions();
		Aclick(RingLoginPage.objLoginLink,"Sign up login link");
		loginMobile();
		String mobNo = "7" + RandomIntegerGenerator(9);
		mobileNoValidation2(mobNo);
		System.out.println(mobNo);
		enterOtp("888888");
		readAndAccept();
		waitTime(3000);
		Utilities.setPlatform = "Web";
        new CommandBase("Chrome");
        waitTime(4000);
        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
		type(InstaLoanPage.objEmail,"shakir.muchumarri@collabera.com","Email text field");
		click(InstaLoanPage.objContinue,"Continue Button");
		waitTime(10000);
		explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
		String userRefNo = getText(InstaLoanPage.objUserRefNo);
		waitTime(3000);
		
		newAdminPanel_appScore(app_score);
		customDataPoints(cc_flag,kla_flag,userRefNo);
		setPlatform("Android");
		initDriver();
		waitTime(5000);
		userDetails();
		dateOfBirth("MAY","09","1994");
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		waitTime(20000);
		return userRefNo;
		
	}
	
	public String lowSegmentationOffer(String appScore,String cibil_score,String cc_flag,String kla_flag) throws Exception{
		extent.HeaderChildNode("Ring Policy - Reusable Method for low_segmentation offer test cases");
		
		enablePermissions();
		Aclick(RingLoginPage.objLoginLink,"Sign up login link");
		loginMobile();
		String mobNo = "7" + RandomIntegerGenerator(9);
		mobileNoValidation2(mobNo);
		System.out.println(mobNo);
		enterOtp("888888");
		readAndAccept();
		waitTime(3000);
		Utilities.setPlatform = "Web";
        new CommandBase("Chrome");
        waitTime(4000);
        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
		type(InstaLoanPage.objEmail,"shakir.muchumarri@collabera.com","Email text field");
		click(InstaLoanPage.objContinue,"Continue Button");
		waitTime(10000);
		explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
		String userRefNo = getText(InstaLoanPage.objUserRefNo);
		waitTime(3000);
		newAdminPanel_appScore(appScore);
		customDataPoints_policy("8000","8000",userRefNo);
		cibilDummy(cibil_score,userRefNo,"no","Sunil Chatla");
		setPlatform("Android");
		initDriver();
		waitTime(5000);
		userDetails();
		dateOfBirth("MAY","09","1994");
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		waitTime(20000);
		return userRefNo;
	}
	
	public List<Object> repeatGrid(String amt) throws Exception{
		extent.HeaderChildNode("Ring Policy - Reusable Method for Repeat offer test cases");
		System.out.println(calendarDate(15));
		enablePermissions();
		Aclick(RingLoginPage.objLoginLink,"Sign up login link");
		loginMobile();
		String mobNo = "7" + RandomIntegerGenerator(9);
		mobileNoValidation2(mobNo);
		System.out.println(mobNo);
		enterOtp("888888");
		readAndAccept();
		waitTime(3000);
		newAdminPanel_appScore("0.007666832");
        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
		explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
		String userRefNo = getText(InstaLoanPage.objUserRefNo);
		waitTime(3000);
		cibilDummy(prop.getproperty("ltbc1"), userRefNo,"no","Sunil Chatla");
		setPlatform("Android");
		initDriver();
		waitTime(5000);
		userDetails();
		dateOfBirth(prop.getproperty("dateOfBirthMonth"), prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthYear"));
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		waitTime(10000);
		addAddress("asasa","asaas","asasa","asq","560102");
		waitTime(5000);
		instaLoancongratsScreen();
		instaLoanSetPin(prop.getproperty("ValidPin"),prop.getproperty("ValidPin"));
		waitTime(5000);
		verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
		String lineRefNo = executeQuery1("SELECT line_reference_number FROM db_txn_service.lines where user_reference_number='"+userRefNo+"';");
		txnApi(userRefNo,mobNo,"300");
		click(RingLoginPage.objTopMenu,"Top left menu");
		explicitWaitVisibilityNonDefault(RingLoginPage.objProfileCrossBtn,30);
		click(RingLoginPage.objProfileCrossBtn,"Profile Cross Button");
		waitTime(3000);
		explicitWaitVisibility(HomePage.txtAreYouEnjoyingApp,30);
		explicitWaitVisibility(HomePage.noBtn,30);
		click(HomePage.noBtn,"No button");
		generateForceBill(userRefNo,lineRefNo,calendarDate(14));
		explicitWaitClickable(HomPageNew.objPayEarlyBtn, 30);
		Aclick(HomPageNew.objPayEarlyBtn, "Pay Early Button");
		explicitWaitVisibilityNonDefault(HomPageNew.objRepaymentHeader, 10);
		waitTime(3000);
		hideKeyboard();
		explicitWaitVisibilityNonDefault(HomPageNew.objNetBankingOption, 10);
		Aclick(HomPageNew.objNetBankingOption, "Net Banking/Debit card option");
		explicitWaitVisibilityNonDefault(HomPageNew.objNetbanking,20);
		Aclick(HomPageNew.objNetbanking, "Netbanking");
		explicitWaitVisibilityNonDefault(HomPageNew.objSelectBankHeader, 10);
		Aclick(HomPageNew.objSBIBank, "SBI bank");
		Aclick(HomPageNew.objPayNowBtn, "Pay Now Button");
		explicitWaitVisibilityNonDefault(HomPageNew.objSuccessBtn, 10);
		Aclick(HomPageNew.objSuccessBtn, "Success Button");
		waitTime(4000);
		explicitWaitVisibilityNonDefault(PaymentPage.objGoToHomePage,30);
		click(PaymentPage.objGoToHomePage,"Go to homepage button");
		if(Integer.parseInt(amt)>=50) {
			System.out.println("Repeat set");
			explicitWaitVisibilityNonDefault(RingLoginPage.objIAcceptCheckBox,30);
			click(RingLoginPage.objIAcceptCheckBox,"Terms & Conditions check box");
			click(UserRegistrationNew.objOkGotIt, "OK Got it Button");
		}
		else if(Integer.parseInt(amt)<50) {
			System.out.println("Repeat not set");
			verifyElementNotPresent(RingLoginPage.objIAcceptCheckBox,30);
		}
		click(RingLoginPage.objTopMenu, "Top left menu button");
		explicitWaitVisibilityNonDefault(HomePage.objBillHistory,10);
		click(HomePage.objBillHistory,"Bill history tab");
		explicitWaitVisibilityNonDefault(HomePage.objBillRefNo,10);
		String billRefNo = getText(HomePage.objBillRefNo);
		Back(2);
		//return billRefNo;
		return Arrays.asList(userRefNo,mobNo,billRefNo);
	}
	
	public String noOffer(String appScore, String cibil_score) throws Exception{
		extent.HeaderChildNode("Ring Policy - Reusable Method for no offer test cases");
		
		enablePermissions();
		Aclick(RingLoginPage.objLoginLink,"Sign up login link");
		loginMobile();
		String mobNo = "7" + RandomIntegerGenerator(9);
		mobileNoValidation2(mobNo);
		System.out.println(mobNo);
		enterOtp("888888");
		readAndAccept();
		waitTime(3000);
		Utilities.setPlatform = "Web";
        new CommandBase("Chrome");
        waitTime(4000);
        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
		type(InstaLoanPage.objEmail,"shakir.muchumarri@collabera.com","Email text field");
		click(InstaLoanPage.objContinue,"Continue Button");
		waitTime(10000);
		explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
		String userRefNo = getText(InstaLoanPage.objUserRefNo);
		waitTime(3000);
		newAdminPanel_appScore(appScore);
		cibilDummy(cibil_score,userRefNo,"no","Sunil Chatla");
		setPlatform("Android");
		initDriver();
		waitTime(5000);
		userDetails();
		dateOfBirth("MAY","09","1994");
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		waitTime(20000);
		addAddress("aas","adsa","asads","askjaskjks","560102");
		return userRefNo;
		
	}
	public String pool3Reusable(String appScore, String cibil_score, String cc_flag, String kla_flag) throws Exception{
		extent.HeaderChildNode("Ring Policy - Reusable Method for pool 3 offer test cases");
		
		enablePermissions();
		Aclick(RingLoginPage.objLoginLink,"Sign up login link");
		loginMobile();
		String mobNo = "7" + RandomIntegerGenerator(9);
		mobileNoValidation2(mobNo);
		System.out.println(mobNo);
		enterOtp("888888");
		readAndAccept();
		waitTime(3000);
		Utilities.setPlatform = "Web";
        new CommandBase("Chrome");
        waitTime(4000);
        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
		type(InstaLoanPage.objEmail,"shakir.muchumarri@collabera.com","Email text field");
		click(InstaLoanPage.objContinue,"Continue Button");
		waitTime(10000);
		explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
		String userRefNo = getText(InstaLoanPage.objUserRefNo);
		waitTime(3000);
		newAdminPanel_appScore(appScore);
		customDataPoints(cc_flag,kla_flag,userRefNo);
		cibilDummy(cibil_score,userRefNo,"no","Sunil Chatla");
		setPlatform("Android");
		initDriver();
		waitTime(5000);
		userDetails();
		dateOfBirth("MAY","09","1994");
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		waitTime(20000);
		return userRefNo;	
		
	}
	
	public String gbReject(String cibil_score) throws Exception{
		enablePermissions();
		Aclick(RingLoginPage.objLoginLink,"Sign up login link");
		loginMobile();
		String mobNo = "7" + RandomIntegerGenerator(9);
		mobileNoValidation2(mobNo);
		System.out.println(mobNo);
		enterOtp("888888");
		readAndAccept();
		waitTime(3000);
		Utilities.setPlatform = "Web";
        new CommandBase("Chrome");
        waitTime(4000);
        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
		type(InstaLoanPage.objEmail,"shakir.muchumarri@collabera.com","Email text field");
		click(InstaLoanPage.objContinue,"Continue Button");
		waitTime(10000);
		explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
		String userRefNo = getText(InstaLoanPage.objUserRefNo);
		waitTime(3000);
		
		cibilDummy(cibil_score,userRefNo,"no","Sunil Chatla");
		newAdminPanel_appScore("0.259277582000000");
		setPlatform("Android");
		initDriver();
		waitTime(5000);
		userDetails();
		dateOfBirth("MAY","09","1994");
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		waitTime(20000);
		return userRefNo;	
	}
	
	public String scanPay(String same_amt,String greater_amt) throws Exception{
		extent.HeaderChildNode("Reusable method for scan & pay with registered mobile");
		enablePermissions();
		Aclick(RingLoginPage.objLoginLink,"Sign up login link");
		loginMobile();
		String mobNo = "7" + RandomIntegerGenerator(9);
		mobileNoValidation2(mobNo);
		System.out.println(mobNo);
		enterOtp("888888");
		readAndAccept();
		waitTime(3000);
		Utilities.setPlatform = "Web";
        new CommandBase("Chrome");
        waitTime(4000);
        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
		type(InstaLoanPage.objEmail,"shakir.muchumarri@collabera.com","Email text field");
		click(InstaLoanPage.objContinue,"Continue Button");
		waitTime(10000);
		explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
		String userRefNo = getText(InstaLoanPage.objUserRefNo);
		waitTime(3000);
		setPlatform("Android");
		initDriver();
		waitTime(5000);
		userDetails();
		dateOfBirth("MAY","09","1994");
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		waitTime(20000);
		addAddress("aas","adsa","asads","askjaskjks","560102");
		waitTime(3000);
		instaLoancongratsScreen();
		instaLoanSetPin(prop.getproperty("ValidPin"),prop.getproperty("ValidPin"));
		waitTime(8000);
		explicitWaitVisibilityNonDefault(HomePage.objCloseButton, 10);
		Aclick(HomePage.objCloseButton, "Close Button");
		
		if(same_amt!=null && greater_amt==null) {
			txnApi(userRefNo,mobNo,same_amt);
			waitTime(3000);
			txnApi(userRefNo,mobNo,same_amt);
			click(RingLoginPage.objTopMenu,"Top left menu");
			explicitWaitVisibilityNonDefault(RingLoginPage.objProfileCrossBtn,30);
			click(RingLoginPage.objProfileCrossBtn,"Profile Cross Button");
			waitTime(3000);
			explicitWaitVisibility(HomePage.txtAreYouEnjoyingApp,30);
			explicitWaitVisibility(HomePage.noBtn,30);
			click(HomePage.noBtn,"No button");
			Swipe("Up",1);
			explicitWaitVisibilityNonDefault(HomePage.objFirstTransaction,10);
			click(HomePage.objFirstTransaction,"Most recent transaction");
			
			
		}
		else if(greater_amt!=null && same_amt==null) {
			
		}
		explicitWaitVisibilityNonDefault(HomePage.transacNumber,10);
		String txnNumber = getText(HomePage.transacNumber);
		
		return txnNumber;
		
	}
	
	public String bankPay(String same_amt) throws Exception{
		extent.HeaderChildNode("Reusable method for bank transfer with registered mobile");
		enablePermissions();
		Aclick(RingLoginPage.objLoginLink,"Sign up login link");
		loginMobile();
		String mobNo = "7" + RandomIntegerGenerator(9);
		mobileNoValidation2(mobNo);
		System.out.println(mobNo);
		enterOtp("888888");
		readAndAccept();
		waitTime(3000);
		userDetails();
		dateOfBirth("MAY","09","1994");
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		waitTime(20000);
		addAddress("aas","adsa","asads","askjaskjks","560102");
		waitTime(3000);
		instaLoancongratsScreen();
		instaLoanSetPin(prop.getproperty("ValidPin"),prop.getproperty("ValidPin"));
		waitTime(8000);
		explicitWaitVisibilityNonDefault(HomePage.objCloseButton, 10);
		Aclick(HomePage.objCloseButton, "Close Button");
		
		click(HomePage.moreTab,"More button");
		explicitWaitVisibilityNonDefault(BankTransferModule.objBankTransfer,10);
		click(BankTransferModule.objBankTransfer,"Bank transfer option");
		explicitWaitVisibilityNonDefault(BankTransferModule.objexistingBankAcc,10);
		click(BankTransferModule.objexistingBankAcc,"Existing bank account");
		waitTime(7000);
		click(BankTransferModule.objEnterAmountTextField,"Amt text field");
		type(BankTransferModule.objEnterAmountTextField,same_amt,"Amt text field");
		click(BankTransferModule.objPayNowBtn,"Pay now button");
		waitTime(5000);
		click(BankTransferModule.objEnterPinTextField,"PIN Text field");
		type(BankTransferModule.objEnterPinTextField,"1111","PIN Text field");
		click(BankTransferModule.objNextNavigation,"Next navigation button");
		explicitWaitVisibilityNonDefault(BankTransferModule.objStatusPage,30);
		click(BankTransferModule.objHomePageBtn,"Go to home page button");
		
		click(HomePage.moreTab,"More button");
		explicitWaitVisibilityNonDefault(BankTransferModule.objBankTransfer,10);
		click(BankTransferModule.objBankTransfer,"Bank transfer option");
		explicitWaitVisibilityNonDefault(BankTransferModule.objexistingBankAcc,10);
		click(BankTransferModule.objexistingBankAcc,"Existing bank account");
		waitTime(7000);
		click(BankTransferModule.objEnterAmountTextField,"Amt text field");
		type(BankTransferModule.objEnterAmountTextField,same_amt,"Amt text field");
		click(BankTransferModule.objPayNowBtn,"Pay now button");
		waitTime(5000);
		click(BankTransferModule.objEnterPinTextField,"PIN Text field");
		type(BankTransferModule.objEnterPinTextField,"1111","PIN Text field");
		click(BankTransferModule.objNextNavigation,"Next navigation button");
		click(BankTransferModule.objHomePageBtn,"Go to home page button");
		explicitWaitVisibility(HomePage.txtAreYouEnjoyingApp,30);
		explicitWaitVisibility(HomePage.noBtn,30);
		click(HomePage.noBtn,"No button");
		Swipe("Up",1);
		explicitWaitVisibilityNonDefault(HomePage.objFirstTransaction,10);
		click(HomePage.objFirstTransaction,"Most recent transaction");
		
		explicitWaitVisibilityNonDefault(HomePage.transacNumber,10);
		String txnNumber = getText(HomePage.transacNumber);
		
		return txnNumber;
		
	}
	
	public String ntcOffer(String appScore) throws Exception {
		extent.HeaderChildNode("Reusable method for NTC Offer test cases");
		HashMap mockUserDetails = mockUserAPI("https://testing-gateway.test.paywithring.com/api/v1/mock-data/user", "male",prop.getproperty("encryptedName"));

		String firstName = (String) mockUserDetails.get("firstName");
		String middleName = (String) mockUserDetails.get("middleName");
		String lastName = (String) mockUserDetails.get("lastName");
		String mobileNumber = (String) mockUserDetails.get("mobileNumber");
		
		String panCard = (String) mockUserDetails.get("panCard");
		String email = (String) mockUserDetails.get("email");
		String motherName = (String) mockUserDetails.get("motherName");
		String gender = (String) mockUserDetails.get("genders");
		String dob = (String) mockUserDetails.get("dob");
		String year = (String) mockUserDetails.get("year");
		String monthByName = (String) mockUserDetails.get("month");
		String date = (String) mockUserDetails.get("date");

		String roomno = (String) mockUserDetails.get("roomNo");
		String address1 = (String) mockUserDetails.get("address1");
		String address2 = (String) mockUserDetails.get("address2");
		String landmark = (String) mockUserDetails.get("landmark");
		String pin = (String) mockUserDetails.get("pin");
		
		String front = (String)mockUserDetails.get("front");
		System.out.println(front + "   gimme");
		
		String back = (String)mockUserDetails.get("back");
		
		enablePermissions();
		Aclick(RingLoginPage.objLoginLink,"Sign up login link");
		loginMobile();
		mobileNoValidation2(mobileNumber);
		enterOtp("888888");
		readAndAccept();
		waitTime(3000);
		newAdminPanel_appScore(appScore);
		getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobileNumber+"&");
		explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
		String userRefNo = getText(InstaLoanPage.objUserRefNo);
		waitTime(4000);
		setPlatform("Android");
		initDriver();
		waitTime(5000);
		
		userDetailsFromAPI(firstName, lastName, email, motherName, gender);
		dateOfBirthForAPI(monthByName, date, year);
		cibilDummy("1",userRefNo,"no",firstName + " " + lastName);
		
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		waitTime(20000);
		if(verifyElementPresent(RingUserDetailPage.objSorryMsg,"Sorry message")) {
			System.out.println("No offer");
		}
		else {
		autoDocUpload(userRefNo,mobileNumber);
		addAddress(roomno, address1, address2, landmark, pin);
		verifyPan(panCard, dob,firstName,lastName,middleName);
		click(AddAddressPage.objSubmitButton, "Submit");
		waitTime(5000);
		explicitWaitVisibility(PAN_DetailsPage.panCardNumberField,30);
		click(PAN_DetailsPage.panCardNumberField,"Pan card text field");
		type(PAN_DetailsPage.panCardNumberField,panCard,"Pan card text field");
		click(PAN_DetailsPage.btnProceed,"Proceed button");
		waitTime(5000);
		explicitWaitVisibility(OfferPage.btnAcceptOffer,30);}
		
		return userRefNo;
	}
	
	public List<Object> hex13(String thresholdValue,String journeyFlag) throws Exception {
		extent.HeaderChildNode("Reusable method for HEX 13 test cases");
		enablePermissions();
		if(journeyFlag.equals("PlayStore")) {
		Aclick(RingLoginPage.objLoginLink,"Sign up login link");
		loginMobile();}
		else if(journeyFlag.equals("Merchant") || journeyFlag.equals("Promocode")) {
			scannQRSwitch();
			User_Play_Store_Flow(prop.getproperty("PaymentPage"), prop.getproperty("Permission"));
		}
		String mobNo = "7" + RandomIntegerGenerator(9);
		mobileNoValidation2(mobNo);
		System.out.println(mobNo);
		enterOtp("888888");
		readAndAccept();
		waitTime(3000);
		Utilities.setPlatform = "Web";
        new CommandBase("Chrome");
        waitTime(4000);
        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
		type(InstaLoanPage.objEmail,"shakir.muchumarri@collabera.com","Email text field");
		click(InstaLoanPage.objContinue,"Continue Button");
		waitTime(10000);
		explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
		String userRefNo = getText(InstaLoanPage.objUserRefNo);
		waitTime(3000);
		getWebDriver().get("https://new-admin-panel.test.ideopay.in/config/configuration?search=hex_fa_count&");
		explicitWaitVisibility(AdminPage.objEditBtn,20);
		click(AdminPage.objEditBtn,"Hex_FA_threshold edit button");
		waitTime(2000);
		clearWebField(InstaLoanPage.objMetaValue);
		type(InstaLoanPage.objMetaValue,thresholdValue,"Hex FA threshold value");
		click(AdminPage.objActiveRadioBtn,"Active radio button");
		click(AdminPage.objSubmit,"Submit button");
		waitTime(4000);
		setPlatform("Android");
		initDriver();
		waitTime(5000);
		userDetails();
		dateOfBirth("MAY","09","1994");
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		waitTime(25000);
		addAddress("aas","adsa","asads","askjaskjks","560102");
		waitTime(3000);
		return Arrays.asList(userRefNo,mobNo);
	}
   //==================================DIGI SURROGATE POOL OFFER TEST CASES================================================= 
	public void TC_Ring_Customer_Seg_01(String appScore) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_01");
		getDriver().resetApp();
		String userRefNo = RingPolicy_Digi(appScore,false,null,true);
		waitTime(3000);
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		JSONObject obj = new JSONObject(json_data);
		String pool_pref="";
		
		if(obj.has("offer_preference")) {
			pool_pref = obj.getString("offer_preference");
			System.out.println(obj.getString("offer_preference"));
		}
		String eligibility = obj.getString("eligible_source");
		System.out.println(eligibility);
		if((pool_pref!="") && eligibility.equals("digi_surrogate")) {
			Assert.assertEquals("POOL_1", pool_pref);
			extent.extentLoggerPass("TC_Ring_Customer_Seg_01","TC_Ring_Customer_Seg_01-To verify user receives offer from Pool 1 for Fresh v-16 when app score is less then 0.107666832");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_34","TC_Ring_Customer_Seg_34-To verify if user is eligible for Pool 1 does not get offer from Regular offer");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_148","TC_Ring_Customer_Seg_148-To verify user with segments L1, L2, L3 & DIGI who matches POOL 1 or POOL 2 definition");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_104","TC_Ring_Customer_Seg_104-To verify user's output value is in Accept band for Fresh v16 & Fresh v17");
			
		}
		
		else {
			extent.extentLoggerPass("TC_Ring_Customer_Seg_37","TC_Ring_Customer_Seg_37-To verify if user is not eligible for Pool 1, get offer from Regular offer");
		}
	}
	
	public void TC_Ring_Customer_Seg_02(String appScore) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_02");
		getDriver().resetApp();
		String userRefNo = RingPolicy_Digi(appScore,false,null,true);
		waitTime(3000);
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		JSONObject obj = new JSONObject(json_data);
		String pool_pref="";
		
		if(obj.has("offer_preference")) {
			pool_pref = obj.getString("offer_preference");
			System.out.println(obj.getString("offer_preference"));
		}
		String eligibility = obj.getString("eligible_source");
		System.out.println(eligibility);
		if((pool_pref!="") && eligibility.equals("digi_surrogate")) {
			Assert.assertNotEquals("POOL_1", pool_pref);
			extent.extentLoggerPass("TC_Ring_Customer_Seg_02","TC_Ring_Customer_Seg_02-To verify user does not receives offer from Pool 1 for Fresh v-16 when app score is greater then 0.107666833");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_35","TC_Ring_Customer_Seg_35-To verify if user is eligible for Pool 2 does not get offer from Regular offer");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_135","TC_Ring_Customer_Seg_135-To verify user with income v2 > 4k");
		}
		
		else{
			extent.extentLoggerPass("TC_Ring_Customer_Seg_38","TC_Ring_Customer_Seg_38-To verify if user is not eligible for Pool 2, get offer from Regular offer");
		}
	}
	
	public void TC_Ring_Customer_Seg_05(String appScore) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_05");
		getDriver().resetApp();
		String userRefNo = RingPolicy_Digi(appScore,false,null,true);
		waitTime(3000);
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		JSONObject obj = new JSONObject(json_data);
		String pool_pref="";
		
		if(obj.has("offer_preference")) {
			pool_pref = obj.getString("offer_preference");
			System.out.println(obj.getString("offer_preference"));
		}
		String eligibility = obj.getString("eligible_source");
		System.out.println(eligibility);
		if((pool_pref!="") && eligibility.equals("digi_surrogate")) {
			Assert.assertEquals("POOL_2", pool_pref);
			extent.extentLoggerPass("TC_Ring_Customer_Seg_05","TC_Ring_Customer_Seg_05-To verify user receives offer from Pool 2 for Fresh v-16 when app score is greater then 0.107666832");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_35","TC_Ring_Customer_Seg_35-To verify if user is eligible for Pool 2 does not get offer from Regular offer");
		}
		
		else {
			extent.extentLoggerPass("TC_Ring_Customer_Seg_38","TC_Ring_Customer_Seg_38-To verify if user is not eligible for Pool 2, get offer from Regular offer");
		}
	}
	
	public void TC_Ring_Customer_Seg_06(String appScore) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_06");
		getDriver().resetApp();
		String userRefNo = RingPolicy_Digi(appScore,false,null,true);
		waitTime(3000);
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		JSONObject obj = new JSONObject(json_data);
		String pool_pref="";
		
		if(obj.has("offer_preference")) {
			pool_pref = obj.getString("offer_preference");
			System.out.println(obj.getString("offer_preference"));
		}
		String eligibility = obj.getString("eligible_source");
		System.out.println(eligibility);
		if((pool_pref!="") && eligibility.equals("digi_surrogate")) {
			Assert.assertEquals("POOL_2", pool_pref);
			extent.extentLoggerPass("TC_Ring_Customer_Seg_06","TC_Ring_Customer_Seg_06-To verify user receives offer from Pool 2 for Fresh v-16 when app score is equal to 0.107666833");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_35","TC_Ring_Customer_Seg_35-To verify if user is eligible for Pool 2 does not get offer from Regular offer");
		}
		
		else {
			extent.extentLoggerPass("TC_Ring_Customer_Seg_38","TC_Ring_Customer_Seg_38-To verify if user is not eligible for Pool 2, get offer from Regular offer");
		}
	}
	
	public void TC_Ring_Customer_Seg_07(String appScore) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_07");
		getDriver().resetApp();
		String userRefNo = RingPolicy_Digi(appScore,false,null,true);
		waitTime(3000);
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		JSONObject obj = new JSONObject(json_data);
		String pool_pref="";
		
		if(obj.has("offer_preference")) {
			pool_pref = obj.getString("offer_preference");
			System.out.println(obj.getString("offer_preference"));
		}
		String eligibility = obj.getString("eligible_source");
		System.out.println(eligibility);
		if((pool_pref!="") && eligibility.equals("digi_surrogate")) {
			Assert.assertEquals("POOL_2", pool_pref);
			extent.extentLoggerPass("TC_Ring_Customer_Seg_07","TC_Ring_Customer_Seg_07-To verify user receives offer from Pool 2 for Fresh v-16 when app score is less then 0.16426588");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_35","TC_Ring_Customer_Seg_35-To verify if user is eligible for Pool 2 does not get offer from Regular offer");
			
		}
		
		else {
			extent.extentLoggerPass("TC_Ring_Customer_Seg_38","TC_Ring_Customer_Seg_38-To verify if user is not eligible for Pool 2, get offer from Regular offer");
		}
	}
	
	public void TC_Ring_Customer_Seg_08(String appScore) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_08");
		getDriver().resetApp();
		String userRefNo = RingPolicy_Digi(appScore,false,null,true);
		waitTime(3000);
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		JSONObject obj = new JSONObject(json_data);
		String pool_pref="";
		
		if(obj.has("offer_preference")) {
			pool_pref = obj.getString("offer_preference");
			System.out.println(obj.getString("offer_preference"));
		}
		String eligibility = obj.getString("eligible_source");
		System.out.println(eligibility);
		if((pool_pref!="") && eligibility.equals("digi_surrogate")) {
			Assert.assertNotEquals("POOL_2", pool_pref);
			extent.extentLoggerPass("TC_Ring_Customer_Seg_08","TC_Ring_Customer_Seg_08-To verify user does not receives offer from Pool 2 for Fresh v-16 when app score is greater then 0.16426588");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_35","TC_Ring_Customer_Seg_35-To verify if user is eligible for Pool 2 does not get offer from Regular offer");
		}
		
		else {
			extent.extentLoggerPass("TC_Ring_Customer_Seg_38","TC_Ring_Customer_Seg_38-To verify if user is not eligible for Pool 2, get offer from Regular offer");
		}
	}
	
	
	
	//================================================CIBIL surrogate POOL Offer test cases =======================================
	public void TC_Ring_Customer_Seg_03(String appScore) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_03");
		getDriver().resetApp();
		String pool_pref = RingPolicy_BlackBox(appScore,false);
		if(pool_pref!="") {
			Assert.assertEquals("POOL_1", pool_pref);
			extent.extentLoggerPass("TC_Ring_Customer_Seg_03","TC_Ring_Customer_Seg_03-To verify user receives offer from Pool 1 for Fresh v-17 when app score is less then 0.13826741");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_34","TC_Ring_Customer_Seg_34-To verify if user is eligible for Pool 1 does not get offer from Regular offer");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_136","TC_Ring_Customer_Seg_136-To verify user with income v2 < 4k");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_104","TC_Ring_Customer_Seg_104-To verify user's output value is in Accept band for Fresh v16 & Fresh v17");
		}
		
		else {
			extent.extentLoggerPass("TC_Ring_Customer_Seg_37","TC_Ring_Customer_Seg_37-To verify if user is not eligible for Pool 1, get offer from Regular offer");
		}
	}
	
	public void TC_Ring_Customer_Seg_04(String appScore) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_04");
		getDriver().resetApp();
		String pool_pref = RingPolicy_BlackBox(appScore,false);
		if(pool_pref!="") {
			Assert.assertNotEquals("POOL_1", pool_pref);
			extent.extentLoggerPass("TC_Ring_Customer_Seg_04","TC_Ring_Customer_Seg_04-To verify user does not receives offer from Pool 1 for Fresh v-17 when app score is greater then 0.13826741");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_35","TC_Ring_Customer_Seg_35-To verify if user is eligible for Pool 2 does not get offer from Regular offer");
		}
		
		else {
			extent.extentLoggerPass("TC_Ring_Customer_Seg_38","TC_Ring_Customer_Seg_38-To verify if user is not eligible for Pool 2, get offer from Regular offer");
		}
	}
	
	public void TC_Ring_Customer_Seg_09(String appScore) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_09");
		getDriver().resetApp();
		String pool_pref = RingPolicy_BlackBox(appScore,false);
		if(pool_pref!="") {
			Assert.assertEquals("POOL_2", pool_pref);
			extent.extentLoggerPass("TC_Ring_Customer_Seg_09","TC_Ring_Customer_Seg_09-To verify user receives offer from Pool 2 for Fresh v-17 when app score is greater then 0.13826741");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_35","TC_Ring_Customer_Seg_35-To verify if user is eligible for Pool 2 does not get offer from Regular offer");
		}
		
		else {
			extent.extentLoggerPass("TC_Ring_Customer_Seg_38","TC_Ring_Customer_Seg_38-To verify if user is not eligible for Pool 2, get offer from Regular offer");
		}
	}
	
	public void TC_Ring_Customer_Seg_10(String appScore) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_10");
		getDriver().resetApp();
		String pool_pref = RingPolicy_BlackBox(appScore,false);
		if(pool_pref!="") {
			Assert.assertEquals("POOL_2", pool_pref);
			extent.extentLoggerPass("TC_Ring_Customer_Seg_10","TC_Ring_Customer_Seg_10-To verify user receives offer from Pool 2 for Fresh v-17 when app score is equal to 0.13826741");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_35","TC_Ring_Customer_Seg_35-To verify if user is eligible for Pool 2 does not get offer from Regular offer");
		}
		
		else {
			extent.extentLoggerPass("TC_Ring_Customer_Seg_38","TC_Ring_Customer_Seg_38-To verify if user is not eligible for Pool 2, get offer from Regular offer");
		}
	}
	public void TC_Ring_Customer_Seg_11(String appScore) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_11");
		getDriver().resetApp();
		String pool_pref = RingPolicy_BlackBox(appScore,false);
		if(pool_pref!="") {
			Assert.assertEquals("POOL_2", pool_pref);
			extent.extentLoggerPass("TC_Ring_Customer_Seg_11","TC_Ring_Customer_Seg_11-To verify user receives offer from Pool 2 for Fresh v-17 when app score is less then 0.184381501");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_35","TC_Ring_Customer_Seg_35-To verify if user is eligible for Pool 2 does not get offer from Regular offer");
		}
		
		else {
			extent.extentLoggerPass("TC_Ring_Customer_Seg_38","TC_Ring_Customer_Seg_38-To verify if user is not eligible for Pool 2, get offer from Regular offer");
		}
	}
	
	public void TC_Ring_Customer_Seg_12(String appScore) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_12");
		getDriver().resetApp();
		String pool_pref = RingPolicy_BlackBox(appScore,false);
		if(pool_pref!="") {
			Assert.assertNotEquals("POOL_2", pool_pref);
			extent.extentLoggerPass("TC_Ring_Customer_Seg_12","To verify user does not receives offer from Pool 2 for Fresh v-17 when app score is greater then 0.184381501");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_36","TC_Ring_Customer_Seg_36-To verify if user is eligible for Pool 3 does not get offer from Regular offer");
		}
		
		else {
			extent.extentLoggerPass("TC_Ring_Customer_Seg_39","TC_Ring_Customer_Seg_39-To verify if user is not eligible for Pool 3 ,get offer from Regular offer");
		}
	}
	
	public void TC_Ring_Customer_Seg_28(String cibil_score,String app_score) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_28");
		getDriver().resetApp();
		String pool_pref="";
		String refNo = regularOfferReusable(cibil_score,app_score,false);
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+refNo+"';");
		String loan_amount_DB = executeQuery1("SELECT limit_amount from db_txn_service.bnpl_line_offers where segment=\"LTBC1\" and surrogate_type=\"EQUIFAX\" and merchant_type=\"type2\" and lower_band=3;");
		JSONObject obj = new JSONObject(json_data);
		String segment = obj.getString("gb_segment");
		String loan_amount = obj.getString("allowed_loan_amount");
		System.out.println(loan_amount);
		
		if(obj.has("offer_preference")) {
			pool_pref=obj.getString("offer_preference");
		}
		
		//for band 1 to 9 users
		if((segment.equals("LTBC1") && pool_pref=="" && loan_amount.equals(loan_amount_DB))){
			Assert.assertEquals("LTBC1", segment);
			extent.extentLoggerPass("TC_Ring_Customer_Seg_28","TC_Ring_Customer_Seg_28-To verify if user receive offer according to LTBC1");
			
		}
		
	}
	
	public void TC_Ring_Customer_Seg_29(String cibil_score,String app_score) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_29");
		getDriver().resetApp();
		String pool_pref="";
		String refNo = regularOfferReusable(cibil_score,app_score,false);
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+refNo+"';");
		JSONObject obj = new JSONObject(json_data);
		String loan_amount_DB = executeQuery1("SELECT limit_amount from db_txn_service.bnpl_line_offers where segment=\"BC1\" and surrogate_type=\"EQUIFAX\" and merchant_type=\"type2\" and lower_band=3;");
		String segment = obj.getString("gb_segment");
		String loan_amount = obj.getString("allowed_loan_amount");
		System.out.println(loan_amount);
		
		if(obj.has("offer_preference")) {
			pool_pref=obj.getString("offer_preference");
		}
		
		if((segment.equals("BC1") && pool_pref=="" && loan_amount.equals(loan_amount_DB))){
			Assert.assertEquals("BC1", segment);
			extent.extentLoggerPass("TC_Ring_Customer_Seg_29","TC_Ring_Customer_Seg_29-To verify if user receive offer according to BC1");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_147","TC_Ring_Customer_Seg_147-To verify user with segments BC1, BC2 & THIN who matches POOL 1 or POOL 2 definition");
		}
		
	}
	
	public void TC_Ring_Customer_Seg_30(String cibil_score,String app_score) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_30");
		getDriver().resetApp();
		String pool_pref="";
		String refNo = regularOfferReusable(cibil_score,app_score,false);
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+refNo+"';");
		JSONObject obj = new JSONObject(json_data);
		String loan_amount_DB = executeQuery1("SELECT limit_amount from db_txn_service.bnpl_line_offers where segment=\"BC2\" and surrogate_type=\"EQUIFAX\" and merchant_type=\"type2\" and lower_band=3;");

		String segment = obj.getString("gb_segment");
		String loan_amount = obj.getString("allowed_loan_amount");
		System.out.println(loan_amount);
		
		if(obj.has("offer_preference")) {
			pool_pref=obj.getString("offer_preference");
		}
		
		if(segment.equals("BC2") && pool_pref=="" && loan_amount.equals(loan_amount_DB)){
			Assert.assertEquals("BC2", segment);
			extent.extentLoggerPass("TC_Ring_Customer_Seg_30","TC_Ring_Customer_Seg_30-To verify if user receive offer according to BC2");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_147","TC_Ring_Customer_Seg_147-To verify user with segments BC1, BC2 & THIN who matches POOL 1 or POOL 2 definition");
		}
		
	}
	
	public void TC_Ring_Customer_Seg_31(String cibil_score,String app_score) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_31");
		getDriver().resetApp();
		String pool_pref="";
		String refNo = regularOfferReusable(cibil_score,app_score,false);
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+refNo+"';");
		JSONObject obj = new JSONObject(json_data);
		String loan_amount_DB = executeQuery1("SELECT limit_amount from db_txn_service.bnpl_line_offers where segment=\"L1\" and surrogate_type=\"EQUIFAX\" and merchant_type=\"type2\" and lower_band=3;");

		String segment = obj.getString("gb_segment");
		String loan_amount = obj.getString("allowed_loan_amount");
		System.out.println(loan_amount);
		
		if(obj.has("offer_preference")) {
			pool_pref=obj.getString("offer_preference");
		}
		
		if((segment.equals("L1") && pool_pref=="" && loan_amount.equals(loan_amount_DB))){
			Assert.assertEquals("L1", segment);
			extent.extentLoggerPass("TC_Ring_Customer_Seg_31","TC_Ring_Customer_Seg_31-To verify if user receive offer according to L1");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_148","TC_Ring_Customer_Seg_148-To verify user with segments L1, L2, L3 & DIGI who matches POOL 1 or POOL 2 definition");
		}
		
	}
	
	public void TC_Ring_Customer_Seg_32(String cibil_score,String app_score) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_32");
		getDriver().resetApp();
		String pool_pref="";
		String refNo = regularOfferReusable(cibil_score,app_score,false);
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+refNo+"';");
		JSONObject obj = new JSONObject(json_data);
		String loan_amount_DB = executeQuery1("SELECT limit_amount from db_txn_service.bnpl_line_offers where segment=\"L2\" and surrogate_type=\"EQUIFAX\" and merchant_type=\"type2\" and lower_band=3;");

		String segment = obj.getString("gb_segment");
		String loan_amount = obj.getString("allowed_loan_amount");
		System.out.println(loan_amount);
		
		if(obj.has("offer_preference")) {
			pool_pref=obj.getString("offer_preference");
		}
		
		if((segment.equals("L2") && pool_pref=="" && loan_amount.equals(loan_amount_DB))){
			Assert.assertEquals("L2", segment);
			extent.extentLoggerPass("TC_Ring_Customer_Seg_32","TC_Ring_Customer_Seg_32-To verify if user receive offer according to L2");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_148","TC_Ring_Customer_Seg_148-To verify user with segments L1, L2, L3 & DIGI who matches POOL 1 or POOL 2 definition");
		}
		
	}
	
	public void TC_Ring_Customer_Seg_33(String cibil_score,String app_score) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_33");
		getDriver().resetApp();
		String pool_pref="";
		String refNo = regularOfferReusable(cibil_score,app_score,false);
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+refNo+"';");
		JSONObject obj = new JSONObject(json_data);
		String loan_amount_DB = executeQuery1("SELECT limit_amount from db_txn_service.bnpl_line_offers where segment=\"L3\" and surrogate_type=\"EQUIFAX\" and merchant_type=\"type2\" and lower_band=3;");

		String segment = obj.getString("gb_segment");
		String loan_amount = obj.getString("allowed_loan_amount");
		System.out.println(loan_amount);
		
		if(obj.has("offer_preference")) {
			pool_pref=obj.getString("offer_preference");
		}
		
		if((segment.equals("L3") && pool_pref=="" && loan_amount.equals(loan_amount_DB))){
			Assert.assertEquals("L3", segment);
			extent.extentLoggerPass("TC_Ring_Customer_Seg_33","TC_Ring_Customer_Seg_33-To verify if user receive offer according to L3");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_148","TC_Ring_Customer_Seg_148-To verify user with segments L1, L2, L3 & DIGI who matches POOL 1 or POOL 2 definition");
		}
		
	}
	
	//================================================AGE Preference test cases =======================================

	public void TC_Ring_Customer_Seg_158_digi(String appScore) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_158");
		getDriver().resetApp();
		String userRefNo = RingPolicy_Digi(appScore,true,null,true);
		waitTime(3000);
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		JSONObject obj = new JSONObject(json_data);
		String pool_pref="";
		
		if(obj.has("offer_preference")) {
			pool_pref = obj.getString("offer_preference");
			System.out.println(obj.getString("offer_preference"));
		}
		String eligibility = obj.getString("eligible_source");
		System.out.println(eligibility);
		if((pool_pref!="") && eligibility.equals("digi_surrogate")) {
			Assert.assertEquals("AGE", pool_pref);
			extent.extentLoggerPass("TC_Ring_Customer_Seg_158","TC_Ring_Customer_Seg_158-To verify user getting offer from Age preference between Age 18 to 22 for Digi, L1, L2 & L3 Segments");

		}

	}
	
	public void TC_Ring_Customer_Seg_158_L1(String appScore,String cibil_score) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_158");
		getDriver().resetApp();
		String userRefNo = regularOfferReusable(cibil_score,appScore,true);
		waitTime(3000);
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		JSONObject obj = new JSONObject(json_data);
		String pool_pref="";
		String segment = obj.getString("gb_segment");
		if(obj.has("offer_preference")) {
			pool_pref = obj.getString("offer_preference");
			System.out.println(obj.getString("offer_preference"));
		}
		String eligibility = obj.getString("eligible_source");
		System.out.println(eligibility);
		if((pool_pref!="") && (segment.equals("L1") || segment.equals("L2") || segment.equals("L3"))) {
			Assert.assertEquals("AGE", pool_pref);
			extent.extentLoggerPass("TC_Ring_Customer_Seg_158","TC_Ring_Customer_Seg_158-To verify user getting offer from Age preference between Age 18 to 22 for Digi, L1, L2 & L3 Segments");

		}
	}
	
	public void TC_Ring_Customer_Seg_159_BC1(String appScore,String cibil_score) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_159");
		getDriver().resetApp();
		String userRefNo = regularOfferReusable(cibil_score,appScore,true);
		waitTime(3000);
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		JSONObject obj = new JSONObject(json_data);
		String pool_pref="";
		String segment = obj.getString("gb_segment");
		if(obj.has("offer_preference")) {
			pool_pref = obj.getString("offer_preference");
			System.out.println(obj.getString("offer_preference"));
		}
		String eligibility = obj.getString("eligible_source");
		System.out.println(eligibility);
		if((pool_pref=="") && segment.equals("BC1")) {
			Assert.assertNotEquals("AGE", pool_pref);
			extent.extentLoggerPass("TC_Ring_Customer_Seg_159","TC_Ring_Customer_Seg_159-To verify user getting offer from Age preference between Age 18 to 22 for BC1, BC2, LTBC1, NTC & THIN Segments");

		}
	}
	
	public void TC_Ring_Customer_Seg_160(String appScore) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_160");
		getDriver().resetApp();
		String pool_pref = RingPolicy_BlackBox(appScore,true);
		if(pool_pref!="" && (pool_pref.equals("POOL_1") || pool_pref.equals("POOL_2"))) {
			//Assert.assertEquals("POOL_2", pool_pref);
			extent.extentLoggerPass("TC_Ring_Customer_Seg_160","TC_Ring_Customer_Seg_160-To verify if users Age is in between 18 to 22 & user falls under the app score of POOL");
		}
		
		else if(pool_pref==""){
			extent.extentLoggerPass("TC_Ring_Customer_Seg_161","TC_Ring_Customer_Seg_161-To verify if users Age is not in between 18 to 22 & user does not fall under the app score of POOL");
		}
	}
	
	//================================================CC Cash withdrawal in last 3m test cases =======================================
   
	public void TC_Ring_Customer_Seg_62(String appScore) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_62");
		getDriver().resetApp();
		String userRefNo = RingPolicy_Digi(appScore,false,prop.getproperty("ccAmount_positive"),true);
		waitTime(3000);
		String json_data = executeQuery1("SELECT rejection_reason FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		if(json_data==null) {
			extent.extentLoggerPass("TC_Ring_Customer_Seg_62","TC_Ring_Customer_Seg_62-To verify user is Digi Reliable + CC Cash withdrawal <15k in last 3 months");
		}
	}
	
	public void TC_Ring_Customer_Seg_63(String appScore) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_63");
		getDriver().resetApp();
		String userRefNo = RingPolicy_Digi(appScore,false,prop.getproperty("ccAmount_equals"),true);
		waitTime(3000);
		String json_data = executeQuery1("SELECT rejection_reason FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		if(json_data==null) {
			extent.extentLoggerPass("TC_Ring_Customer_Seg_63","TC_Ring_Customer_Seg_63-To verify user is Digi Reliable + CC Cash withdrawal equal to 15k in last 3 months");
		}
	}

	public void TC_Ring_Customer_Seg_64(String appScore) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_64");
		getDriver().resetApp();
		String userRefNo = RingPolicy_Digi(appScore,false,prop.getproperty("ccAmount_negative"),true);
		waitTime(3000);
		String json_data = executeQuery1("SELECT rejection_reason FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		if(json_data.equalsIgnoreCase("Limit exceed for CC cash withdrawal")) {
			extent.extentLoggerPass("TC_Ring_Customer_Seg_64","TC_Ring_Customer_Seg_64-To verify user is Digi Reliable + CC Cash withdrawal >15k in last 3 months");
		}
	}
	
	public void TC_Ring_Customer_Seg_65(String appScore) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_65");
		getDriver().resetApp();
		String userRefNo = RingPolicy_Digi(appScore,false,prop.getproperty("ccAmount_negative"),false);
		waitTime(3000);
		String json_data = executeQuery1("SELECT rejection_reason FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		if(json_data==null) {
			extent.extentLoggerPass("TC_Ring_Customer_Seg_65","TC_Ring_Customer_Seg_65-To verify user is Digi Not Reliable + CC Cash withdrawal >15k in last 3 months");
		}
	}
	
	//================================================Blocking user test cases =======================================

	public void TC_Ring_Customer_Seg_70(String url, String gender, String encryptedName) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_70");
		getDriver().resetApp();
		String userRefNo = blockUsers(url,gender,encryptedName,"mobile_block");
		explicitWaitVisibilityNonDefault(RingUserDetailPage.objSorryMsg,10);
		if(verifyElementExist(RingUserDetailPage.objSorryMsg,"Sorry message")) {
			extent.extentLoggerPass("TC_Ring_Customer_Seg_70","TC_Ring_Customer_Seg_70-To verify adding users in block table through mobile number, pan hash & IMEI");
		}
		
		String json_data = executeQuery1("SELECT rejection_reason FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		if(json_data.equals("Customer rejected from block list")) {
			extent.extentLoggerPass("TC_Ring_Customer_Seg_71","TC_Ring_Customer_Seg_71-To verify adding users in block table through mobile number");
		}
		
		
	}
	
	
	public void TC_Ring_Customer_Seg_74(String url,String gender, String encryptedName) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_74");
		getDriver().resetApp();
		String userRefNo = blockUsers(url,gender,encryptedName,"after_block");
		
		String json_data = executeQuery1("SELECT rejection_reason FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		System.out.println(json_data);
		if((json_data==null) && verifyElementNotPresent(RingUserDetailPage.objSorryMsg,30)) {
			extent.extentLoggerPass("TC_Ring_Customer_Seg_74","TC_Ring_Customer_Seg_74-To verify homepage after adding users in block table once limit activated");
		}
	}
	
	//================================================KLA offer test cases =======================================
	
	public void TC_Ring_Customer_Seg_170(String appScore,String cc_flag,String kla_flag) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_170");
		getDriver().resetApp();
		String userRefNo = kla_cc_offer(appScore,cc_flag,kla_flag);
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		JSONObject obj = new JSONObject(json_data);
		String pool_pref="";
		
		if(obj.has("offer_preference")) {
			pool_pref = obj.getString("offer_preference");
			System.out.println(obj.getString("offer_preference"));
		}
		
		if(pool_pref.equals("KOINO_LOOKALIKE")) {
			extent.extentLoggerPass("TC_Ring_Customer_Seg_170","TC_Ring_Customer_Seg_170-To verify user receives offer from KLA if 'is_true_comp=1' and GB band does not fall for POOL criteria");
		}
	}
	
	public void TC_Ring_Customer_Seg_171(String appScore,String cc_flag,String kla_flag) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_171");
		getDriver().resetApp();
		String userRefNo = kla_cc_offer(appScore,cc_flag,kla_flag);
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		JSONObject obj = new JSONObject(json_data);
		String pool_pref="";
		
		if(obj.has("offer_preference")) {
			pool_pref = obj.getString("offer_preference");
			System.out.println(obj.getString("offer_preference"));
		}
		
		if((pool_pref!="KOINO_LOOKALIKE") && (pool_pref.equals("POOL_1"))) {
			extent.extentLoggerPass("TC_Ring_Customer_Seg_171","TC_Ring_Customer_Seg_171-To verify user with 'is_true_comp=1' and GB band falls under POOL criteria");
		}
	}
	
	//================================================CC offer test cases =======================================

	public void TC_Ring_Customer_Seg_172(String appScore,String cc_flag,String kla_flag) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_172");
		getDriver().resetApp();
		String userRefNo = kla_cc_offer(appScore,cc_flag,kla_flag);
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		JSONObject obj = new JSONObject(json_data);
		String pool_pref="";
		
		if(obj.has("offer_preference")) {
			pool_pref = obj.getString("offer_preference");
			System.out.println(obj.getString("offer_preference"));
		}
		
		if(pool_pref.equals("CC_PRESENCE")) {
			extent.extentLoggerPass("TC_Ring_Customer_Seg_172","TC_Ring_Customer_Seg_172-To verify user receives offer from CC Presence if 'unique_cc_count_12m = 1' and GB band does not fall for POOL criteria");
		}
	}
	
	public void TC_Ring_Customer_Seg_173(String appScore,String cc_flag,String kla_flag) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_173");
		getDriver().resetApp();
		String userRefNo = kla_cc_offer(appScore,cc_flag,kla_flag);
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		JSONObject obj = new JSONObject(json_data);
		String pool_pref="";
		
		if(obj.has("offer_preference")) {
			pool_pref = obj.getString("offer_preference");
			System.out.println(obj.getString("offer_preference"));
		}
		
		if((pool_pref!="CC_PRESENCE") && (pool_pref.equals("POOL_1"))) {
			extent.extentLoggerPass("TC_Ring_Customer_Seg_173","TC_Ring_Customer_Seg_173-To verify user with 'unique_cc_count_12m = 1' and GB band falls under POOL criteria");
		}
	}
	
	public void TC_Ring_Customer_Seg_174(String appScore,String cc_flag,String kla_flag) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_174");
		getDriver().resetApp();
		String userRefNo = kla_cc_offer(appScore,cc_flag,kla_flag);
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		JSONObject obj = new JSONObject(json_data);
		String pool_pref="";
		
		if(obj.has("offer_preference")) {
			pool_pref = obj.getString("offer_preference");
			System.out.println(obj.getString("offer_preference"));
		}
		
		if((pool_pref=="KOINO_LOOKALIKE") ) {
			extent.extentLoggerPass("TC_Ring_Customer_Seg_174","TC_Ring_Customer_Seg_174-To verify user with 'is_true_comp=1' and 'unique_cc_count_12m = 1'");
		}
	}
	
	
	//================================================Low Segmentation offer test cases =======================================
	
	public void TC_Ring_Customer_Seg_131(String appScore,String cibil_score,String cc_flag,String kla_flag) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_131");
		getDriver().resetApp();
		executeUpdateOnly("update db_txn_service.bnpl_line_offers set limit_amount='0' where merchant_type='type2' and segment='LTBC1' and surrogate_type='EQUIFAX' and upper_band='15';","db_txn_service.bnpl_line_offers");
		String userRefNo = lowSegmentationOffer(appScore,cibil_score,cc_flag,kla_flag);
		addAddress("as","assa","afaa","asdafd","560102");
		explicitWaitVisibilityNonDefault(PromoCodeOfferPage.objCheckBox,20);
		click(PromoCodeOfferPage.objCheckBox,"Terms & Conditions check box");
		click(PromoCodeOfferPage.objAcceptOffer,"Accept offer button");
		
		waitTime(4000);
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		JSONObject obj = new JSONObject(json_data);
		String pool_pref="";
		
		if(obj.has("offer_preference")) {
			pool_pref = obj.getString("offer_preference");
			System.out.println(obj.getString("offer_preference"));
		}
		
		if(pool_pref.equals("LOW_SEGMENTATION")) {
			System.out.println("yep");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_131","TC_Ring_Customer_Seg_131-To verify user receives offer from Low_Segmentation for Segments BC1 / BC2 / LTBC1 / THIN for band 9-20 only when 'ratio_parsed_total_sms>0.095' and 'latest_loan_history_months_count>25.5' and 'no_of_loan_entries>9.5'");
		}
		executeUpdateOnly("update db_txn_service.bnpl_line_offers set limit_amount='600' where merchant_type='type2' and segment='LTBC1' and surrogate_type='EQUIFAX' and upper_band='15';","db_txn_service.bnpl_line_offers");
	}
	
	public void TC_Ring_Customer_Seg_132(String appScore,String cibil_score,String cc_flag,String kla_flag) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_132");
		getDriver().resetApp();
		String userRefNo = lowSegmentationOffer(appScore,cibil_score,cc_flag,kla_flag);
		addAddress("as","assa","afaa","asdafd","560102");
		explicitWaitVisibilityNonDefault(PromoCodeOfferPage.objCheckBox,20);
		click(PromoCodeOfferPage.objCheckBox,"Terms & Conditions check box");
		click(PromoCodeOfferPage.objAcceptOffer,"Accept offer button");
		
		waitTime(4000);
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		JSONObject obj = new JSONObject(json_data);
		String pool_pref="";
		
		if(obj.has("offer_preference")) {
			pool_pref = obj.getString("offer_preference");
			System.out.println(obj.getString("offer_preference"));
		}
		
		if(pool_pref!="LOW_SEGMENTATION") {
			System.out.println("yep");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_132","TC_Ring_Customer_Seg_132-To verify user matched all the criteria i.e. 'ratio_parsed_total_sms>0.095' and 'latest_loan_history_months_count>25.5' and 'no_of_loan_entries>9.5'\r\n"
					+ "and standard amount is defined as well as Low_Segmentation amount is defined");
		}
		
		
	}
	
	public void TC_Ring_Customer_Seg_133(String appScore,String cibil_score,String cc_flag,String kla_flag) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_133");
		getDriver().resetApp();
		String userRefNo = lowSegmentationOffer(appScore,cibil_score,cc_flag,kla_flag);
		addAddress("as","assa","afaa","asdafd","560102");
		explicitWaitVisibilityNonDefault(PromoCodeOfferPage.objCheckBox,20);
		click(PromoCodeOfferPage.objCheckBox,"Terms & Conditions check box");
		click(PromoCodeOfferPage.objAcceptOffer,"Accept offer button");
		
		waitTime(4000);
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		JSONObject obj = new JSONObject(json_data);
		String pool_pref="";
		
		if(obj.has("offer_preference")) {
			pool_pref = obj.getString("offer_preference");
			System.out.println(obj.getString("offer_preference"));
		}
		
		if(pool_pref!="LOW_SEGMENTATION") {
			System.out.println("yep");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_133","TC_Ring_Customer_Seg_133-To verify user receives offer from Standard section for Segments BC1 / BC2 / LTBC1 / THIN for band 1-9 even when criteria not matched i.e. 'ratio_parsed_total_sms>0.095' and 'latest_loan_history_months_count>25.5' and 'no_of_loan_entries>9.5'");
		}
		
		
	}
	
	public void TC_Ring_Customer_Seg_134(String appScore,String cibil_score,String cc_flag,String kla_flag) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_134");
		getDriver().resetApp();
		executeUpdateOnly("update db_txn_service.bnpl_line_offers set limit_amount='0' where merchant_type='type2' and segment='LTBC1' and surrogate_type='EQUIFAX' and upper_band='15';","db_txn_service.bnpl_line_offers");
		String userRefNo = lowSegmentationOffer(appScore,cibil_score,cc_flag,kla_flag);
		addAddress("as","assa","afaa","asdafd","560102");
		
		waitTime(5000);
		String reason = executeQuery1("SELECT rejection_reason FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");

		if(reason.equals("No amount from matrix")) {
			System.out.println("yep");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_134","TC_Ring_Customer_Seg_134-To verify user did not passed criteria for band 9 - 20 i.e. 'ratio_parsed_total_sms>0.095' and 'latest_loan_history_months_count>25.5' and 'no_of_loan_entries>9.5' for BC1 / BC2 / LTBC1 / THIN");
		}
		executeUpdateOnly("update db_txn_service.bnpl_line_offers set limit_amount='600' where merchant_type='type2' and segment='LTBC1' and surrogate_type='EQUIFAX' and upper_band='15';","db_txn_service.bnpl_line_offers");
	}
	
	public void TC_Ring_Customer_Seg_135(String appScore,String cibil_score,String cc_flag,String kla_flag) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_135");
		getDriver().resetApp();
		executeUpdateOnly("update db_txn_service.bnpl_line_offers set limit_amount='0' where merchant_type='type2' and segment='L2' and surrogate_type='EQUIFAX' and upper_band='15';","db_txn_service.bnpl_line_offers");
		String userRefNo = lowSegmentationOffer(appScore,cibil_score,cc_flag,kla_flag);
		addAddress("as","assa","afaa","asdafd","560102");
		explicitWaitVisibilityNonDefault(PromoCodeOfferPage.objCheckBox,20);
		click(PromoCodeOfferPage.objCheckBox,"Terms & Conditions check box");
		click(PromoCodeOfferPage.objAcceptOffer,"Accept offer button");
		
		waitTime(4000);
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		JSONObject obj = new JSONObject(json_data);
		String pool_pref="";
		
		if(obj.has("offer_preference")) {
			pool_pref = obj.getString("offer_preference");
			System.out.println(obj.getString("offer_preference"));
		}
		
		if(pool_pref!="LOW_SEGMENTATION") {
			System.out.println("yep");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_135","TC_Ring_Customer_Seg_135-To verify user did not passed criteria for band 9 - 20 i.e. 'ratio_parsed_total_sms>0.095' and 'latest_loan_history_months_count>25.5' and 'no_of_loan_entries>9.5' for segments other than  BC1 / BC2 / LTBC1 / THIN");
		}
		executeUpdateOnly("update db_txn_service.bnpl_line_offers set limit_amount='600' where merchant_type='type2' and segment='L2' and surrogate_type='EQUIFAX' and upper_band='15';","db_txn_service.bnpl_line_offers");
	}

	//================================================No loan offer test cases =======================================
	
	public void TC_Ring_Customer_Seg_99(String appScore,String cibil_score) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_99");
		getDriver().resetApp();
		String userRefNo = noOffer(appScore,cibil_score);
		waitTime(3000);
		String json_data = executeQuery1("SELECT rejection_reason FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		if(json_data.equals("No amount from matrix")) {
			extent.extentLoggerPass("TC_Ring_Customer_Seg_99","TC_Ring_Customer_Seg_99-To verify user cibil score where the same is not defined in the offer grid");
		}
	}
	
	//================================================GB Reject/Accept test cases =======================================
	
	public void TC_Ring_Customer_Seg_103(String cibil_score) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_103");
		getDriver().resetApp();
		String userRefNo = gbReject(cibil_score);
		waitTime(3000);
		String json_data = executeQuery1("SELECT rejection_reason FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		if(json_data.equals("GB Reject FRESH-V17")) {
			extent.extentLoggerPass("TC_Ring_Customer_Seg_103","TC_Ring_Customer_Seg_103-To verify user's output value is in Reject band for Fresh v17");
		}
	}
	
	public void TC_Ring_Customer_Seg_105(String appScore,String cibil_score) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_105");
		getDriver().resetApp();
		regularOfferReusable(cibil_score,appScore,false);
		waitTime(3000);
		explicitWaitVisibilityNonDefault(UserRegistrationPage.objSomethingWrong,15);
		if(verifyElementPresent(UserRegistrationPage.objSomethingWrong,"Something went wrong message")) {
			extent.extentLoggerPass("TC_Ring_Customer_Seg_105","TC_Ring_Customer_Seg_105-To verify user's output value is 0.0000000 in Accept band");
		}
	}
	
	public void TC_Ring_Customer_Seg_106(String appScore,String cibil_score) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_106");
		getDriver().resetApp();
		String userRefNo = regularOfferReusable(cibil_score,appScore,false);
		waitTime(3000);
		String json_data = executeQuery1("SELECT rejection_reason FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		if(json_data.equals("GB Reject FRESH-V17")) {
			extent.extentLoggerPass("TC_Ring_Customer_Seg_106","TC_Ring_Customer_Seg_106-To verify user's output value is 1.0000000 in Accept band");
		}
	}
	
	
	public void TC_Ring_Customer_Seg_107(String appScore) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_107");
		getDriver().resetApp();
		String userRefNo = RingPolicy_Digi(appScore, false,null, true);
		
		String json_data = executeQuery1("SELECT isnull(rejection_reason) FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		if(json_data.equals("1")) {
			extent.extentLoggerPass("TC_Ring_Customer_Seg_107","TC_Ring_Customer_Seg_107-To verify Digi surrogate users output value is in Reject band");
		}
	}
	
	
	//================================================txn fee & cash fee test cases =======================================
	
	public void TC_Ring_Customer_Seg_40(String appScore) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_40");
		getDriver().resetApp();
		String userRefNo = txnFeeCashFee(appScore);
		
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		JSONObject obj = new JSONObject(json_data);
		String txn_fee_user = obj.getString("txn_fees_percentage_with_gst");
		System.out.println(txn_fee_user);
		
		String txn_fee_bnpl_table = executeQuery1("SELECT txn_fees_percentage_with_gst FROM db_txn_service.bnpl_line_offers where surrogate_type='POOL_CIBIL_1' and merchant_type='type2';");
		System.out.println(txn_fee_bnpl_table);
		if(txn_fee_user.equals(txn_fee_bnpl_table)) {
			extent.extentLoggerPass("TC_Ring_Customer_Seg_40","TC_Ring_Customer_Seg_40-To verify if user receives transaction fee according to the offer assigned");
		}
	}
	
	public void TC_Ring_Customer_Seg_41(String appScore) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_41");
		getDriver().resetApp();
		String userRefNo = txnFeeCashFee(appScore);
		
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		JSONObject obj = new JSONObject(json_data);
		String cash_fee_user = obj.getString("cash_fee_percentage_with_gst");
		System.out.println(cash_fee_user);
		
		String cash_fee_bnpl_table = executeQuery1("SELECT cash_fee_percentage_with_gst FROM db_txn_service.bnpl_line_offers where surrogate_type='POOL_CIBIL_1' and merchant_type='type2';");
		System.out.println(cash_fee_bnpl_table);
		if(cash_fee_user.equals(cash_fee_bnpl_table)) {
			extent.extentLoggerPass("TC_Ring_Customer_Seg_41","TC_Ring_Customer_Seg_41-To verify if user receives Cash fee according to the offer assigned");
		}
	}
	
	public void TC_Ring_Customer_Seg_42(String appScore) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_42");
		getDriver().resetApp();
		String userRefNo = txnFeeCashFee(appScore);
		
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		JSONObject obj = new JSONObject(json_data);
		double cash_fee_user = Double.parseDouble(obj.getString("cash_fee_percentage_with_gst"));
		double txn_fee_user = Double.parseDouble(obj.getString("txn_fees_percentage_with_gst"));
		double offer_amt = Double.parseDouble(obj.getString("allowed_loan_amount"));
		
		/*String cash_fee_bnpl_table = executeQuery1("SELECT cash_fee_percentage_with_gst FROM db_txn_service.bnpl_line_offers where surrogate_type='POOL_CIBIL_1' and merchant_type='type2';");
		System.out.println(cash_fee_bnpl_table);*/
		
		double tenPercentOffer = 0.1*offer_amt;
		if((((cash_fee_user+txn_fee_user)*offer_amt))/100<tenPercentOffer) {
			System.out.println("yep");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_42","TC_Ring_Customer_Seg_42-To verify if user receives transaction fee and Cash fee total less than 10 percent");
		}
	}
	
	public void TC_Ring_Customer_Seg_43(String appScore) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_43");
		getDriver().resetApp();
		executeUpdateOnly("update db_txn_service.bnpl_line_offers set txn_fees_percentage_with_gst='5.00',cash_fee_percentage_with_gst='5.00' where surrogate_type='POOL_CIBIL_1' and merchant_type='type2';","db_txn_service.bnpl_line_offers");
		
		String userRefNo = txnFeeCashFee(appScore);
		
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		JSONObject obj = new JSONObject(json_data);
		double cash_fee_user = Double.parseDouble(obj.getString("cash_fee_percentage_with_gst"));
		double txn_fee_user = Double.parseDouble(obj.getString("txn_fees_percentage_with_gst"));
		double offer_amt = Double.parseDouble(obj.getString("allowed_loan_amount"));
		
		double tenPercentOffer = 0.1*offer_amt;
		if((((cash_fee_user+txn_fee_user)*offer_amt))/100==tenPercentOffer) {
			System.out.println("yep");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_43","TC_Ring_Customer_Seg_43-To verify if user receives transaction fee and Cash fee total equal to 10 percent");
		}
		
		executeUpdateOnly("update db_txn_service.bnpl_line_offers set txn_fees_percentage_with_gst='2.27',cash_fee_percentage_with_gst='3.49' where surrogate_type='POOL_CIBIL_1' and merchant_type='type2';","db_txn_service.bnpl_line_offers");
	}
	
	public void TC_Ring_Customer_Seg_44(String appScore) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_44");
		getDriver().resetApp();
		executeUpdateOnly("update db_txn_service.bnpl_line_offers set txn_fees_percentage_with_gst='7.00',cash_fee_percentage_with_gst='7.00' where surrogate_type='POOL_CIBIL_1' and merchant_type='type2';","db_txn_service.bnpl_line_offers");
		
		String userRefNo = txnFeeCashFee(appScore);
		addAddress("as","assa","afaa","asdafd","560102");
		waitTime(10000);
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		JSONObject obj = new JSONObject(json_data);
		double cash_fee_user = Double.parseDouble(obj.getString("cash_fee_percentage_with_gst"));
		double txn_fee_user = Double.parseDouble(obj.getString("txn_fees_percentage_with_gst"));
		double offer_amt = Double.parseDouble(obj.getString("allowed_loan_amount"));
		
		double tenPercentOffer = 0.1*offer_amt;
		if((((cash_fee_user+txn_fee_user)*offer_amt))/100<=tenPercentOffer) {
			System.out.println("yep");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_43","TC_Ring_Customer_Seg_43-To verify if user receives transaction fee and Cash fee total equal to 10 percent");
		}
		
		executeUpdateOnly("update db_txn_service.bnpl_line_offers set txn_fees_percentage_with_gst='2.27',cash_fee_percentage_with_gst='3.49' where surrogate_type='POOL_CIBIL_1' and merchant_type='type2';","db_txn_service.bnpl_line_offers");
	}
	
	//================================================Max limit amt fresh test cases =======================================
	
	public void TC_Ring_Customer_Seg_45(String appScore) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_45");
		getDriver().resetApp();
		executeUpdateOnly("update db_txn_service.bnpl_line_offers set limit_amount='26000.00' where surrogate_type='POOL_CIBIL_1' and merchant_type='type2';","db_txn_service.bnpl_line_offers");
		
		String userRefNo = txnFeeCashFee(appScore);
		addAddress("as","assa","afaa","asdafd","560102");
		waitTime(10000);
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		JSONObject obj = new JSONObject(json_data);
		double limit_amt = Double.parseDouble(obj.getString("allowed_loan_amount"));
		
		if(limit_amt<=26000.00) {
			System.out.println("yep");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_45","TC_Ring_Customer_Seg_45-To verify if user receives offer (limit amount) max upto 26000 when limit amount set as 26000");
		}
		
		executeUpdateOnly("update db_txn_service.bnpl_line_offers set limit_amount='8100.00' where surrogate_type='POOL_CIBIL_1' and merchant_type='type2';","db_txn_service.bnpl_line_offers");
	}

	
	public void TC_Ring_Customer_Seg_46(String appScore) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_46");
		getDriver().resetApp();
		executeUpdateOnly("update db_txn_service.bnpl_line_offers set limit_amount='29000.00' where surrogate_type='POOL_CIBIL_1' and merchant_type='type2';","db_txn_service.bnpl_line_offers");
		
		String userRefNo = txnFeeCashFee(appScore);
		addAddress("as","assa","afaa","asdafd","560102");
		waitTime(10000);
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		JSONObject obj = new JSONObject(json_data);
		double limit_amt = Double.parseDouble(obj.getString("allowed_loan_amount"));
		
		if(limit_amt<=26000.00) {
			System.out.println("yep");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_46","TC_Ring_Customer_Seg_46-To verify if user receives offer (limit amount) max upto 26000 when limit amount set as greater than 26000");
		}
		
		executeUpdateOnly("update db_txn_service.bnpl_line_offers set limit_amount='8100.00' where surrogate_type='POOL_CIBIL_1' and merchant_type='type2';","db_txn_service.bnpl_line_offers");
	}
	
	//================================================KYC Re verfication test cases =======================================
	public void TC_Ring_Customer_Seg_167(String cibil_score, String appScore) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_167");
		getDriver().resetApp();
		String userRefNo = regularOfferReusable(cibil_score,appScore,false);
		addAddress("as","assa","afaa","asdafd","560102");
		explicitWaitVisibilityNonDefault(PromoCodeOfferPage.objCheckBox,20);
		click(PromoCodeOfferPage.objCheckBox,"Terms & Conditions check box");
		click(PromoCodeOfferPage.objAcceptOffer,"Accept offer button");
		waitTime(4000);
		
		String kyc_status = executeQuery1("SELECT kyc_re_verification_band FROM db_user_service.users where user_reference_number='"+userRefNo+"';");
		if(kyc_status.equals("HIGH")) {
			System.out.println("yep");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_167","TC_Ring_Customer_Seg_167-To verify kyc reverification band status for GB band =>18 & GB segment DIGI & LTBC1");
		}
	}
	
	public void TC_Ring_Customer_Seg_168(String cibil_score, String appScore) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_168");
		getDriver().resetApp();
		String userRefNo = regularOfferReusable(cibil_score,appScore,false);
		addAddress("as","assa","afaa","asdafd","560102");
		explicitWaitVisibilityNonDefault(PromoCodeOfferPage.objCheckBox,20);
		click(PromoCodeOfferPage.objCheckBox,"Terms & Conditions check box");
		click(PromoCodeOfferPage.objAcceptOffer,"Accept offer button");
		
		waitTime(4000);
		
		String kyc_status = executeQuery1("SELECT kyc_re_verification_band FROM db_user_service.users where user_reference_number='"+userRefNo+"';");
		if(kyc_status.equals("MEDIUM")) {
			System.out.println("yep");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_168","TC_Ring_Customer_Seg_168-To verify kyc reverification band status for GB band =>18 & GB segment BC1, BC2, L1, L2, L3, NTC & THIN");
		}
	}
	
	public void TC_Ring_Customer_Seg_169(String cibil_score, String appScore) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_169");
		getDriver().resetApp();
		String userRefNo = regularOfferReusable(cibil_score,appScore,false);
		addAddress("as","assa","afaa","asdafd","560102");
		explicitWaitVisibilityNonDefault(PromoCodeOfferPage.objCheckBox,20);
		click(PromoCodeOfferPage.objCheckBox,"Terms & Conditions check box");
		click(PromoCodeOfferPage.objAcceptOffer,"Accept offer button");
		
		waitTime(4000);
		
		String kyc_status = executeQuery1("SELECT kyc_re_verification_band FROM db_user_service.users where user_reference_number='"+userRefNo+"';");
		if(kyc_status.equals("LOW")) {
			System.out.println("yep");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_169","TC_Ring_Customer_Seg_169-To verify kyc reverification band status for GB band <18 & all GB segment");
		}
		
	}
	
	//================================================bnpl txn level checks test cases =======================================

	public void TC_Ring_Customer_Seg_120(String same_amt,String greater_amt) throws Exception{
		
		extent.HeaderChildNode("TC_Ring_Customer_Seg_120");
		getDriver().resetApp();
		String txnNumber = scanPay(same_amt,greater_amt);
		String reason = executeQuery1("SELECT rejection_reason FROM db_txn_service.bnpl_transactions where bnpl_txn_reference_number='"+txnNumber+"';");
		
		if(reason.equals("Dedupe FA transaction")) {
			System.out.println("yep");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_120","TC_Ring_Customer_Seg_120-To verify user doing transactions of same amount to same merchant within 5 minutes through Scan n Pay");
		}
	}
	
	public void TC_Ring_Customer_Seg_121(String same_amt) throws Exception{
		
		extent.HeaderChildNode("TC_Ring_Customer_Seg_121");
		getDriver().resetApp();
		String txnNumber = bankPay(same_amt);
		String reason = executeQuery1("SELECT rejection_reason FROM db_txn_service.bnpl_transactions where bnpl_txn_reference_number='"+txnNumber+"';");
		
		if(reason.equals("Dedupe FA transaction")) {
			System.out.println("yep");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_121","TC_Ring_Customer_Seg_121-To verify user doing transactions of same amount to same merchant within 5 minutes through Bank Transfer");
		}
	}
	
	public void TC_Ring_Customer_Seg_125() throws Exception{
		//USE GPS EMULATOR to emulate a location outside RingPay's reach (eg: Andhra Pradesh)
		extent.HeaderChildNode("TC_Ring_Customer_Seg_125");
		getDriver().resetApp();
		enablePermissions();
		Aclick(RingLoginPage.objLoginLink,"Sign up login link");
		loginMobile();
		String mobNo = "7" + RandomIntegerGenerator(9);
		mobileNoValidation2(mobNo);
		System.out.println(mobNo);
		enterOtp("888888");
		readAndAccept();
		waitTime(3000);
		Utilities.setPlatform = "Web";
        new CommandBase("Chrome");
        waitTime(4000);
        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
		type(InstaLoanPage.objEmail,"shakir.muchumarri@collabera.com","Email text field");
		click(InstaLoanPage.objContinue,"Continue Button");
		waitTime(10000);
		explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
		String userRefNo = getText(InstaLoanPage.objUserRefNo);
		waitTime(3000);
		setPlatform("Android");
		initDriver();
		waitTime(5000);
		userDetails();
		dateOfBirth("MAY","09","1994");
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		waitTime(20000);
		addAddress("aas","adsa","asads","askjaskjks","560102");
		waitTime(3000);
		instaLoancongratsScreen();
		instaLoanSetPin(prop.getproperty("ValidPin"),prop.getproperty("ValidPin"));
		waitTime(8000);
		explicitWaitVisibilityNonDefault(HomePage.objCloseButton, 10);
		Aclick(HomePage.objCloseButton, "Close Button");
		txnApi(userRefNo,mobNo,"101");
		click(RingLoginPage.objTopMenu,"Top left menu");
		explicitWaitVisibilityNonDefault(RingLoginPage.objProfileCrossBtn,30);
		click(RingLoginPage.objProfileCrossBtn,"Profile Cross Button");
		waitTime(3000);
		explicitWaitVisibility(HomePage.txtAreYouEnjoyingApp,30);
		explicitWaitVisibility(HomePage.noBtn,30);
		click(HomePage.noBtn,"No button");
		Swipe("Up",1);
		explicitWaitVisibilityNonDefault(HomePage.objFirstTransaction,10);
		click(HomePage.objFirstTransaction,"Most recent transaction");
		explicitWaitVisibilityNonDefault(HomePage.transacNumber,10);
		String txnNumber = getText(HomePage.transacNumber);
		
		String reason = executeQuery1("SELECT rejection_reason FROM db_txn_service.bnpl_transactions where bnpl_txn_reference_number='"+txnNumber+"';");
		
		if(reason.equals("Area Blocked for Transaction")) {
			System.out.println("yep");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_125","TC_Ring_Customer_Seg_125-To verify user doing Scan n Pay transaction from the location which is disabled for doing transaction ");
		}
		
		
	}
	
	public void TC_Ring_Customer_Seg_126() throws Exception{
		//USE GPS EMULATOR to emulate a location outside RingPay's reach (eg: Andhra Pradesh)
		extent.HeaderChildNode("TC_Ring_Customer_Seg_126");
		getDriver().resetApp();
		enablePermissions();
		Aclick(RingLoginPage.objLoginLink,"Sign up login link");
		loginMobile();
		String mobNo = "7" + RandomIntegerGenerator(9);
		mobileNoValidation2(mobNo);
		System.out.println(mobNo);
		enterOtp("888888");
		readAndAccept();
		waitTime(3000);
		userDetails();
		dateOfBirth("MAY","09","1994");
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		waitTime(20000);
		addAddress("aas","adsa","asads","askjaskjks","560102");
		waitTime(3000);
		instaLoancongratsScreen();
		instaLoanSetPin(prop.getproperty("ValidPin"),prop.getproperty("ValidPin"));
		waitTime(8000);
		explicitWaitVisibilityNonDefault(HomePage.objCloseButton, 10);
		Aclick(HomePage.objCloseButton, "Close Button");
		click(HomePage.moreTab,"More button");
		explicitWaitVisibilityNonDefault(BankTransferModule.objBankTransfer,10);
		click(BankTransferModule.objBankTransfer,"Bank transfer option");
		explicitWaitVisibilityNonDefault(BankTransferModule.objexistingBankAcc,10);
		click(BankTransferModule.objexistingBankAcc,"Existing bank account");
		waitTime(7000);
		click(BankTransferModule.objEnterAmountTextField,"Amt text field");
		type(BankTransferModule.objEnterAmountTextField,"2","Amt text field");
		click(BankTransferModule.objPayNowBtn,"Pay now button");
		waitTime(5000);
		click(BankTransferModule.objEnterPinTextField,"PIN Text field");
		type(BankTransferModule.objEnterPinTextField,"1111","PIN Text field");
		click(BankTransferModule.objNextNavigation,"Next navigation button");
		
		
		explicitWaitVisibilityNonDefault(HomePage.okGotIt,10);
		click(HomePage.okGotIt,"Ok got it button");
		
		explicitWaitVisibilityNonDefault(HomePage.objFirstTransaction,10);
		click(HomePage.objFirstTransaction,"Most recent transaction");
		
		explicitWaitVisibilityNonDefault(HomePage.transacNumber,10);
		String txnNumber = getText(HomePage.transacNumber);
		
		String reason = executeQuery1("SELECT rejection_reason FROM db_txn_service.bnpl_transactions where bnpl_txn_reference_number='"+txnNumber+"';");
		
		if(reason.equals("Area Blocked for Transaction")) {
			System.out.println("yep");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_126","TC_Ring_Customer_Seg_126-To verify user doing Bank Transfer transaction from the location which is disabled for doing transaction ");
		}
		
		
	}
	
	//================================================Terminate user on full repayment test cases =======================================
	
	public void TC_Ring_Customer_Seg_151() throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_151");
		getDriver().resetApp();
		executeUpdateOnly("update db_master_service.configuration set meta_value='LTBC1', status='ACTIVE' where meta_key='DELINQUENT_EVERT_SEGMENTS';","db_txn_service.bnpl_line_offers");
		String userRefNo = regularOfferReusable(prop.getproperty("ltbc1"),prop.getproperty("pool_2_equals"),false);
		addAddress("as","assa","afaa","asdafd","560102");
		explicitWaitVisibilityNonDefault(PromoCodeOfferPage.objCheckBox,20);
		click(PromoCodeOfferPage.objCheckBox,"Terms & Conditions check box");
		click(PromoCodeOfferPage.objAcceptOffer,"Accept offer button");
		
		waitTime(4000);
		String reason = executeQuery1("SELECT status FROM db_txn_service.lines where user_reference_number='"+userRefNo+"';");
		
		if(reason.equals("TERMINATED")) {
			System.out.println("yep");
			
			extent.extentLoggerPass("TC_Ring_Customer_Seg_151","TC_Ring_Customer_Seg_151-To Verify that check should get performed on segments defined in configuration (DELINQUENT_EVERT_SEGMENTS)");
		}
		
		executeUpdateOnly("update db_master_service.configuration set meta_value='LTBC1', status='INACTIVE' where meta_key='DELINQUENT_EVERT_SEGMENTS';","db_txn_service.bnpl_line_offers");
	}
	
	//================================================Cabal check at onboarding test cases =======================================
	
	public void TC_Ring_Customer_Seg_141() throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_141");
		getDriver().resetApp();
		enablePermissions();
		Aclick(RingLoginPage.objLoginLink,"Sign up login link");
		loginMobile();
		String mobNo = "7" + RandomIntegerGenerator(9);
		mobileNoValidation2(mobNo);
		System.out.println(mobNo);
		enterOtp("888888");
		readAndAccept();
		waitTime(3000);
		
		String userRefNo = executeQuery1("SELECT user_reference_number FROM db_user_service.users where mobile_number='"+mobNo+"';");
		waitTime(15000);
		userDetails();
		customDataPoints_policy(prop.getproperty("onboarding_cabal_count_greater"),prop.getproperty("transaction_cabal_count"),userRefNo);
		dateOfBirth("MAY","09","1994");
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		waitTime(20000);
		String json_data = executeQuery1("SELECT rejection_reason FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		if(json_data.equals("Cabal Linked User")) {
			System.out.println("yep");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_141","TC_Ring_Customer_Seg_141-To verify onboarding new user whose cabal exceeds the cut off limit during Init to CA");
		}
		
	}
	
	public void TC_Ring_Customer_Seg_142() throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_142");
		getDriver().resetApp();
		enablePermissions();
		Aclick(RingLoginPage.objLoginLink,"Sign up login link");
		loginMobile();
		String mobNo = "7" + RandomIntegerGenerator(9);
		mobileNoValidation2(mobNo);
		System.out.println(mobNo);
		enterOtp("888888");
		readAndAccept();
		waitTime(3000);
		
		String userRefNo = executeQuery1("SELECT user_reference_number FROM db_user_service.users where mobile_number='"+mobNo+"';");
		waitTime(15000);
		userDetails();
		dateOfBirth("MAY","09","1994");
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		waitTime(20000);
		addAddress("as","assa","afaa","asdafd","560102");
		explicitWaitVisibilityNonDefault(PromoCodeOfferPage.objCheckBox,20);
		customDataPoints_policy(prop.getproperty("onboarding_cabal_count_greater"),prop.getproperty("transaction_cabal_count"),userRefNo);
		click(PromoCodeOfferPage.objCheckBox,"Terms & Conditions check box");
		click(PromoCodeOfferPage.objAcceptOffer,"Accept offer button");
		waitTime(4000);
		String json_data = executeQuery1("SELECT rejection_reason FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		if(json_data.equals("Cabal Linked User")) {
			System.out.println("yep");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_142","TC_Ring_Customer_Seg_142-To verify user whose cabal exceeds the cut off limit during CA to FA");
		}
		
	}

	public void TC_Ring_Customer_Seg_143() throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_143");
		getDriver().resetApp();
		enablePermissions();
		Aclick(RingLoginPage.objLoginLink,"Sign up login link");
		loginMobile();
		String mobNo = "7" + RandomIntegerGenerator(9);
		mobileNoValidation2(mobNo);
		System.out.println(mobNo);
		enterOtp("888888");
		readAndAccept();
		waitTime(3000);
		
		String userRefNo = executeQuery1("SELECT user_reference_number FROM db_user_service.users where mobile_number='"+mobNo+"';");
		waitTime(15000);
		userDetails();
		customDataPoints_policy(prop.getproperty("onboarding_cabal_count_lesser"),prop.getproperty("transaction_cabal_count"),userRefNo);
		dateOfBirth("MAY","09","1994");
		Aclick(UserRegistrationNew.objProceed, "Proceed Button");
		waitTime(20000);
		String json_data = executeQuery1("SELECT isnull(rejection_reason) FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
		if(json_data.equals("1")) {
			System.out.println("yep");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_143","TC_Ring_Customer_Seg_143-To verify onboarding new user whose cabal is less or equal to the cut off limit");
		}
		
	}
	
	//================================================To verify if user is set as repeat test cases =======================================
	
	public void TC_Ring_Customer_Seg_47(String amt) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_47");
		getDriver().resetApp();
		List<Object> s = repeatGrid(amt);
		String userRefNo = s.get(0).toString();
		String mobNo = s.get(1).toString();
		System.out.println(userRefNo);
		txnApi(userRefNo,mobNo,prop.getproperty("stepup_greater_amount_next"));
		click(RingLoginPage.objTopMenu,"Top left menu");
		explicitWaitVisibilityNonDefault(RingLoginPage.objProfileCrossBtn,30);
		click(RingLoginPage.objProfileCrossBtn,"Profile Cross Button");
		waitTime(3000);
		explicitWaitVisibility(HomePage.txtAreYouEnjoyingApp,30);
		explicitWaitVisibility(HomePage.noBtn,30);
		click(HomePage.noBtn,"No button");
		Swipe("UP",1);
		explicitWaitVisibilityNonDefault(HomePage.objFirstTransaction,10);
		click(HomePage.objFirstTransaction,"Most recent transaction");
		
		explicitWaitVisibilityNonDefault(HomePage.transacNumber,10);
		String txnNumber = getText(HomePage.transacNumber);
		
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.bnpl_transactions where bnpl_txn_reference_number='"+txnNumber+"';");
		JSONObject obj = new JSONObject(json_data);
		Boolean repeatFlag = obj.getBoolean("is_repeat");
		
		if(repeatFlag==true) {
			System.out.println("yes");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_47","TC_Ring_Customer_Seg_47-To verify if user sets as repeat if user has utilized minimum amount of rs.50 from previous limit and does repayment");
		}
		
		
	}

	public void TC_Ring_Customer_Seg_48(String amt) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_48");
		getDriver().resetApp();
		List<Object> s = repeatGrid(amt);
		String userRefNo = s.get(0).toString();
		String mobNo = s.get(1).toString();
		System.out.println(userRefNo);
		txnApi(userRefNo,mobNo,prop.getproperty("stepup_lesser_amount_next"));
		click(RingLoginPage.objTopMenu,"Top left menu");
		explicitWaitVisibilityNonDefault(RingLoginPage.objProfileCrossBtn,30);
		click(RingLoginPage.objProfileCrossBtn,"Profile Cross Button");
		waitTime(3000);
		explicitWaitVisibility(HomePage.txtAreYouEnjoyingApp,30);
		explicitWaitVisibility(HomePage.noBtn,30);
		click(HomePage.noBtn,"No button");
		Swipe("UP",1);
		explicitWaitVisibilityNonDefault(HomePage.objFirstTransaction,10);
		click(HomePage.objFirstTransaction,"Most recent transaction");
		
		explicitWaitVisibilityNonDefault(HomePage.transacNumber,10);
		String txnNumber = getText(HomePage.transacNumber);
		
		String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.bnpl_transactions where bnpl_txn_reference_number='"+txnNumber+"';");
		JSONObject obj = new JSONObject(json_data);
		Boolean repeatFlag = obj.getBoolean("is_repeat");
		
		if(repeatFlag==false) {
			System.out.println("yes");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_48","TC_Ring_Customer_Seg_48-To verify if user does not get set as repeat if user has not utilized minimum amount of rs.50");
		}
	}
	
	//================================================To verify if user gets step up when set as repeat test cases =======================================//
	public void TC_Ring_Customer_Seg_49(String amt) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_49");
		getDriver().resetApp();
		List<Object> s = repeatGrid(amt);
		String userRefNo = s.get(0).toString();
		String mobNo = s.get(1).toString();
		String billRefNo = s.get(2).toString();
		System.out.println(userRefNo);
		txnApi(userRefNo,mobNo,prop.getproperty("stepup_greater_amount_next"));
		click(RingLoginPage.objTopMenu,"Top left menu");
		explicitWaitVisibilityNonDefault(RingLoginPage.objProfileCrossBtn,30);
		click(RingLoginPage.objProfileCrossBtn,"Profile Cross Button");
		waitTime(3000);
		explicitWaitVisibility(HomePage.txtAreYouEnjoyingApp,30);
		explicitWaitVisibility(HomePage.noBtn,30);
		click(HomePage.noBtn,"No button");
		String stepupPerc = executeQuery1("SELECT dpd_prepay FROM db_txn_service.bnpl_repeat_limit_amount_grid where repeat_type='EARLY' and max_line_amount='2000';");
		String new_limit = executeQuery1("SELECT new_limit FROM db_loan_service.line_step_up_data where repeat_source_reference_number='"+billRefNo+"';");
		String old_limit = executeQuery1("SELECT old_limit FROM db_loan_service.line_step_up_data where repeat_source_reference_number='"+billRefNo+"';");
		
		if(((Double.parseDouble(old_limit)*(100.00 + Double.parseDouble(stepupPerc))/100.00) == Double.parseDouble(new_limit))){
			System.out.println("yep");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_49","TC_Ring_Customer_Seg_49-To verify if user gets step up according to percentage (10) mentioned according to previous loan repayment behaviour");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_50","TC_Ring_Customer_Seg_50-To verify if user gets step up only if user has utilized \"x\" percentage of amount from previous limit which is mentioned in \"STEP UP CREDIT USAGE PERCENT\" configuration");
		}	
		
	}
	
	//================================================To verify if user gets step down when set as repeat test cases =======================================//
	public void TC_Ring_Customer_Seg_52(String amt) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_52");
		getDriver().resetApp();
		executeUpdateOnly("update db_txn_service.bnpl_repeat_limit_amount_grid set dpd_prepay='-20' where max_line_amount='2000' and repeat_type='EARLY';","db_txn_service.bnpl_repeat_limit_amount_grid");
		List<Object> s = repeatGrid(amt);
		String userRefNo = s.get(0).toString();
		String mobNo = s.get(1).toString();
		String billRefNo = s.get(2).toString();
		System.out.println(userRefNo);
		txnApi(userRefNo,mobNo,prop.getproperty("stepup_greater_amount_next"));
		click(RingLoginPage.objTopMenu,"Top left menu");
		explicitWaitVisibilityNonDefault(RingLoginPage.objProfileCrossBtn,30);
		click(RingLoginPage.objProfileCrossBtn,"Profile Cross Button");
		waitTime(3000);
		explicitWaitVisibility(HomePage.txtAreYouEnjoyingApp,30);
		explicitWaitVisibility(HomePage.noBtn,30);
		click(HomePage.noBtn,"No button");
		String stepdownPerc = executeQuery1("SELECT dpd_prepay FROM db_txn_service.bnpl_repeat_limit_amount_grid where repeat_type='EARLY' and max_line_amount='2000';");
		String new_limit = executeQuery1("SELECT new_limit FROM db_loan_service.line_step_up_data where repeat_source_reference_number='"+billRefNo+"';");
		String old_limit = executeQuery1("SELECT old_limit FROM db_loan_service.line_step_up_data where repeat_source_reference_number='"+billRefNo+"';");
		
		if(((Double.parseDouble(old_limit)*(100.00 + Double.parseDouble(stepdownPerc))/100.00) == Double.parseDouble(new_limit))){
			System.out.println("yep");
			extent.extentLoggerPass("TC_Ring_Customer_Seg_52","TC_Ring_Customer_Seg_52-To verify if user gets step down according to percentage (-10) mentioned according to previous loan repayment behaviour");
			
		}	
		executeUpdateOnly("update db_txn_service.bnpl_repeat_limit_amount_grid set dpd_prepay='45' where max_line_amount='2000' and repeat_type='EARLY';","db_txn_service.bnpl_repeat_limit_amount_grid");
	}
	
	//================================================To verify max and min value for repeat test cases =======================================
	
	public void TC_Ring_Customer_Seg_53(String amt) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_53");
		getDriver().resetApp();
		executeUpdateOnly("update db_txn_service.bnpl_line_offers set limit_amount = '10000.00' where merchant_type='type2' and segment = 'LTBC1' and surrogate_type='EQUIFAX' and limit_amount='2000.00';","db_txn_service.bnpl_line_offers");
		executeUpdateOnly("update db_txn_service.bnpl_repeat_limit_amount_grid set dpd_prepay='260.00' where max_line_amount='10000.00' and repeat_type='EARLY';","db_txn_service.bnpl_repeat_limit_amount_grid");
		List<Object> s = repeatGrid(amt);
		String userRefNo = s.get(0).toString();
		String mobNo = s.get(1).toString();
		String billRefNo = s.get(2).toString();
		System.out.println(userRefNo);
		txnApi(userRefNo,mobNo,prop.getproperty("stepup_greater_amount_next"));
		click(RingLoginPage.objTopMenu,"Top left menu");
		explicitWaitVisibilityNonDefault(RingLoginPage.objProfileCrossBtn,30);
		click(RingLoginPage.objProfileCrossBtn,"Profile Cross Button");
		waitTime(3000);
		explicitWaitVisibility(HomePage.txtAreYouEnjoyingApp,30);
		explicitWaitVisibility(HomePage.noBtn,30);
		click(HomePage.noBtn,"No button");
		String stepdownPerc = executeQuery1("SELECT dpd_prepay FROM db_txn_service.bnpl_repeat_limit_amount_grid where repeat_type='EARLY' and max_line_amount='2000';");
		String new_limit = executeQuery1("SELECT new_limit FROM db_loan_service.line_step_up_data where repeat_source_reference_number='"+billRefNo+"';");
		String old_limit = executeQuery1("SELECT old_limit FROM db_loan_service.line_step_up_data where repeat_source_reference_number='"+billRefNo+"';");
		
		if(new_limit.equals("26000.00")){
			System.out.println(new_limit);
			extent.extentLoggerPass("TC_Ring_Customer_Seg_53","TC_Ring_Customer_Seg_53-To verify if user gets repeat limit maximum amount upto 26000 by setting value = 26000");
			
		}	
		executeUpdateOnly("update db_txn_service.bnpl_repeat_limit_amount_grid set dpd_prepay='20.00' where max_line_amount='2000' and repeat_type='EARLY';","db_txn_service.bnpl_repeat_limit_amount_grid");
		executeUpdateOnly("update db_txn_service.bnpl_line_offers set limit_amount = '2000.00' where merchant_type='type2' and segment = 'LTBC1' and surrogate_type='EQUIFAX' and limit_amount='10000.00';","db_txn_service.bnpl_line_offers");	
		
	}
	
	public void TC_Ring_Customer_Seg_54(String amt) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_54");
		getDriver().resetApp();
		executeUpdateOnly("update db_txn_service.bnpl_line_offers set limit_amount = '10000.00' where merchant_type='type2' and segment = 'LTBC1' and surrogate_type='EQUIFAX' and limit_amount='2000.00';","db_txn_service.bnpl_line_offers");
		executeUpdateOnly("update db_txn_service.bnpl_repeat_limit_amount_grid set dpd_prepay='280.00' where max_line_amount='10000.00' and repeat_type='EARLY';","db_txn_service.bnpl_repeat_limit_amount_grid");
		List<Object> s = repeatGrid(amt);
		String userRefNo = s.get(0).toString();
		String mobNo = s.get(1).toString();
		String billRefNo = s.get(2).toString();
		System.out.println(userRefNo);
		txnApi(userRefNo,mobNo,prop.getproperty("stepup_greater_amount_next"));
		click(RingLoginPage.objTopMenu,"Top left menu");
		explicitWaitVisibilityNonDefault(RingLoginPage.objProfileCrossBtn,30);
		click(RingLoginPage.objProfileCrossBtn,"Profile Cross Button");
		waitTime(3000);
		explicitWaitVisibility(HomePage.txtAreYouEnjoyingApp,30);
		explicitWaitVisibility(HomePage.noBtn,30);
		click(HomePage.noBtn,"No button");
		String stepdownPerc = executeQuery1("SELECT dpd_prepay FROM db_txn_service.bnpl_repeat_limit_amount_grid where repeat_type='EARLY' and max_line_amount='2000';");
		String new_limit = executeQuery1("SELECT new_limit FROM db_loan_service.line_step_up_data where repeat_source_reference_number='"+billRefNo+"';");
		String old_limit = executeQuery1("SELECT old_limit FROM db_loan_service.line_step_up_data where repeat_source_reference_number='"+billRefNo+"';");
		
		if(new_limit.equals("26000.00")){
			System.out.println(new_limit);
			extent.extentLoggerPass("TC_Ring_Customer_Seg_54","TC_Ring_Customer_Seg_54-To verify if user gets repeat limit maximum amount upto 26000 by setting value > 26000");
			
		}	
		executeUpdateOnly("update db_txn_service.bnpl_repeat_limit_amount_grid set dpd_prepay='20.00' where max_line_amount='2000' and repeat_type='EARLY';","db_txn_service.bnpl_repeat_limit_amount_grid");
		executeUpdateOnly("update db_txn_service.bnpl_line_offers set limit_amount = '2000.00' where merchant_type='type2' and segment = 'LTBC1' and surrogate_type='EQUIFAX' and limit_amount='10000.00';","db_txn_service.bnpl_line_offers");	
	
	}
	
	public void TC_Ring_Customer_Seg_55(String amt) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_55");
		getDriver().resetApp();
		executeUpdateOnly("update db_txn_service.bnpl_line_offers set limit_amount = '10000.00' where merchant_type='type2' and segment = 'LTBC1' and surrogate_type='EQUIFAX' and limit_amount='2000.00';","db_txn_service.bnpl_line_offers");
		executeUpdateOnly("update db_txn_service.bnpl_repeat_limit_amount_grid set dpd_prepay='-99.00' where max_line_amount='10000.00' and repeat_type='EARLY';","db_txn_service.bnpl_repeat_limit_amount_grid");
		List<Object> s = repeatGrid(amt);
		String userRefNo = s.get(0).toString();
		String mobNo = s.get(1).toString();
		String billRefNo = s.get(2).toString();
		System.out.println(userRefNo);
		txnApi(userRefNo,mobNo,prop.getproperty("stepdown_amount"));
		click(RingLoginPage.objTopMenu,"Top left menu");
		explicitWaitVisibilityNonDefault(RingLoginPage.objProfileCrossBtn,30);
		click(RingLoginPage.objProfileCrossBtn,"Profile Cross Button");
		waitTime(3000);
		explicitWaitVisibility(HomePage.txtAreYouEnjoyingApp,30);
		explicitWaitVisibility(HomePage.noBtn,30);
		click(HomePage.noBtn,"No button");
		
		String new_limit = executeQuery1("SELECT new_limit FROM db_loan_service.line_step_up_data where repeat_source_reference_number='"+billRefNo+"';");
		
		
		if(new_limit.equals("100.00")){
			System.out.println(new_limit);
			extent.extentLoggerPass("TC_Ring_Customer_Seg_55","TC_Ring_Customer_Seg_55-To verify if user gets repeat limit minimum amount upto 100 by setting value = 100");
			
		}	
		executeUpdateOnly("update db_txn_service.bnpl_repeat_limit_amount_grid set dpd_prepay='20.00' where max_line_amount='2000' and repeat_type='EARLY';","db_txn_service.bnpl_repeat_limit_amount_grid");
		executeUpdateOnly("update db_txn_service.bnpl_line_offers set limit_amount = '2000.00' where merchant_type='type2' and segment = 'LTBC1' and surrogate_type='EQUIFAX' and limit_amount='10000.00';","db_txn_service.bnpl_line_offers");	
		
	}
	
	public void TC_Ring_Customer_Seg_56(String amt) throws Exception{
		extent.HeaderChildNode("TC_Ring_Customer_Seg_56");
		getDriver().resetApp();
		executeUpdateOnly("update db_txn_service.bnpl_line_offers set limit_amount = '10000.00' where merchant_type='type2' and segment = 'LTBC1' and surrogate_type='EQUIFAX' and limit_amount='2000.00';","db_txn_service.bnpl_line_offers");
		executeUpdateOnly("update db_txn_service.bnpl_repeat_limit_amount_grid set dpd_prepay='-99.8' where max_line_amount='10000.00' and repeat_type='EARLY';","db_txn_service.bnpl_repeat_limit_amount_grid");
		List<Object> s = repeatGrid(amt);
		String userRefNo = s.get(0).toString();
		String mobNo = s.get(1).toString();
		String billRefNo = s.get(2).toString();
		System.out.println(userRefNo);
		txnApi(userRefNo,mobNo,prop.getproperty("stepdown_amount"));
		click(RingLoginPage.objTopMenu,"Top left menu");
		explicitWaitVisibilityNonDefault(RingLoginPage.objProfileCrossBtn,30);
		click(RingLoginPage.objProfileCrossBtn,"Profile Cross Button");
		waitTime(3000);
		explicitWaitVisibility(HomePage.txtAreYouEnjoyingApp,30);
		explicitWaitVisibility(HomePage.noBtn,30);
		click(HomePage.noBtn,"No button");
		
		String new_limit = executeQuery1("SELECT new_limit FROM db_loan_service.line_step_up_data where repeat_source_reference_number='"+billRefNo+"';");
		
		if(new_limit.equals("100.00")){
			System.out.println(new_limit);
			extent.extentLoggerPass("TC_Ring_Customer_Seg_56","TC_Ring_Customer_Seg_56-To verify if user gets repeat limit minimum amount upto 100 by setting value < 100");
			
		}	
		executeUpdateOnly("update db_txn_service.bnpl_repeat_limit_amount_grid set dpd_prepay='20.00' where max_line_amount='2000' and repeat_type='EARLY';","db_txn_service.bnpl_repeat_limit_amount_grid");
		executeUpdateOnly("update db_txn_service.bnpl_line_offers set limit_amount = '2000.00' where merchant_type='type2' and segment = 'LTBC1' and surrogate_type='EQUIFAX' and limit_amount='10000.00';","db_txn_service.bnpl_line_offers");	
		
	}
	
	//================================================To verify user receives offer from THIN columns from Offer Grid test cases =======================================
		public void TC_Ring_Customer_Seg_144() throws Exception{
			extent.HeaderChildNode("TC_Ring_Customer_Seg_144");
			getDriver().resetApp();
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo = "7" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo);
			System.out.println(mobNo);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			cibilDummy(prop.getproperty("l1"), userRefNo,"yes","Sunil Chatla");
			setPlatform("Android");
			initDriver();
			waitTime(5000);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthMonth"), prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthYear"));
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(10000);
			addAddress("asasa","asaas","asasa","asq","560102");
			waitTime(5000);
			instaLoancongratsScreen();
			instaLoanSetPin(prop.getproperty("ValidPin"),prop.getproperty("ValidPin"));
			waitTime(5000);
			String thin_limit_amount = executeQuery1("SELECT thin_limit_amount FROM db_txn_service.bnpl_line_offers where segment=\"L1\" and merchant_type=\"type2\" and surrogate_type=\"CIBIL\" and lower_band=\"1\";");
			String thin_cashback_percentage = executeQuery1("SELECT thin_cashback_percentage FROM db_txn_service.bnpl_line_offers where segment=\"L1\" and merchant_type=\"type2\" and surrogate_type=\"CIBIL\" and lower_band=\"1\";");
			String thin_cash_fee_percentage_with_gst = executeQuery1("SELECT thin_cash_fee_percentage_with_gst FROM db_txn_service.bnpl_line_offers where segment=\"L1\" and merchant_type=\"type2\" and surrogate_type=\"CIBIL\" and lower_band=\"1\";");
			
			String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
			JSONObject obj = new JSONObject(json_data);
			
			String allowed_loan_amount = obj.getString("allowed_loan_amount");
			String allowed_cashback = obj.getString("cashback_percentage");
			String cash_fee_percentage_with_gst = obj.getString("cash_fee_percentage_with_gst");
			
			if((thin_limit_amount.equals(allowed_loan_amount)) && (thin_cashback_percentage.equals(allowed_cashback)) && (thin_cash_fee_percentage_with_gst.equals(cash_fee_percentage_with_gst))) {
				System.out.println("yes");
				extent.extentLoggerPass("TC_Ring_Customer_Seg_144","TC_Ring_Customer_Seg_144-To verify user eligible for THIN and receives offer from THIN columns i.e. Activation fee, Txn Fee, Cash Fee, Cashback");
			}
		}

		public void TC_Ring_Customer_Seg_145() throws Exception{
			extent.HeaderChildNode("TC_Ring_Customer_Seg_145");
			getDriver().resetApp();
			executeUpdateOnly("update db_txn_service.bnpl_line_offers set thin_line_activation_fees=\"0.00\",thin_txn_fees_percentage_with_gst=\"0.00\",thin_cashback_percentage=\"0.00\",thin_cash_fee_percentage_with_gst=\"0.00\" where segment=\"L1\" and merchant_type=\"type2\" and surrogate_type=\"CIBIL\" and lower_band=\"1\";","db_txn_service.bnpl_line_offers");
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo = "7" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo);
			System.out.println(mobNo);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			cibilDummy(prop.getproperty("l1"), userRefNo,"yes","Sunil Chatla");
			setPlatform("Android");
			initDriver();
			waitTime(5000);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthMonth"), prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthYear"));
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(10000);
			addAddress("asasa","asaas","asasa","asq","560102");
			waitTime(5000);
			instaLoancongratsScreen();
			instaLoanSetPin(prop.getproperty("ValidPin"),prop.getproperty("ValidPin"));
			waitTime(5000);
			String thin_limit_amount = executeQuery1("SELECT thin_limit_amount FROM db_txn_service.bnpl_line_offers where segment=\"L1\" and merchant_type=\"type2\" and surrogate_type=\"CIBIL\" and lower_band=\"1\";");
			System.out.println(thin_limit_amount);
			String cashback_percentage_offer = executeQuery1("SELECT cashback_percentage FROM db_txn_service.bnpl_line_offers where segment=\"L1\" and merchant_type=\"type2\" and surrogate_type=\"CIBIL\" and lower_band=\"1\";");
			System.out.println(cashback_percentage_offer );
			String cash_fee_percentage_with_gst_offer = executeQuery1("SELECT cash_fee_percentage_with_gst FROM db_txn_service.bnpl_line_offers where segment=\"L1\" and merchant_type=\"type2\" and surrogate_type=\"CIBIL\" and lower_band=\"1\";");
			System.out.println(cash_fee_percentage_with_gst_offer);
			
			String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
			JSONObject obj = new JSONObject(json_data);
			String allowed_loan_amount = obj.getString("allowed_loan_amount");
			System.out.println(allowed_loan_amount);
			String allowed_cashback = obj.getString("cashback_percentage");
			System.out.println(allowed_cashback);
			String cash_fee_percentage_with_gst = obj.getString("cash_fee_percentage_with_gst");
			System.out.println(cash_fee_percentage_with_gst);
			
			if((thin_limit_amount.equals(allowed_loan_amount)) && (cashback_percentage_offer.equals(allowed_cashback)) && (cash_fee_percentage_with_gst_offer.equals(cash_fee_percentage_with_gst))) {
				System.out.println("yes");
				extent.extentLoggerPass("TC_Ring_Customer_Seg_145","TC_Ring_Customer_Seg_145-To verify user eligible for THIN and THIN columns i.e. Activation fee, Txn Fee, Cash Fee, Cashback as 0 or Null amount");
			}
		}
		
		public void TC_Ring_Customer_Seg_146() throws Exception{
			extent.HeaderChildNode("TC_Ring_Customer_Seg_146");
			getDriver().resetApp();
			executeUpdateOnly("update db_txn_service.bnpl_line_offers set thin_line_activation_fees=\"0.00\",thin_limit_amount=\"0.00\",thin_txn_fees_percentage_with_gst=\"0.00\",thin_cashback_percentage=\"0.00\",thin_cash_fee_percentage_with_gst=\"0.00\" where segment=\"L1\" and merchant_type=\"type2\" and surrogate_type=\"EQUIFAX\" and lower_band=\"1\";","db_txn_service.bnpl_line_offers");
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo = "7" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo);
			System.out.println(mobNo);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			cibilDummy(prop.getproperty("l1"), userRefNo,"yes","Sunil Chatla");
			setPlatform("Android");
			initDriver();
			waitTime(5000);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthMonth"), prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthYear"));
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
			addAddress("asasa","asaas","asasa","asq","560102");
			waitTime(5000);
			instaLoancongratsScreen();
			instaLoanSetPin(prop.getproperty("ValidPin"),prop.getproperty("ValidPin"));
			waitTime(5000);
			String limit_amount = executeQuery1("SELECT limit_amount FROM db_txn_service.bnpl_line_offers where segment=\"L1\" and merchant_type=\"type2\" and surrogate_type=\"EQUIFAX\" and lower_band=\"1\";");
			System.out.println(limit_amount);
			String cashback_percentage_offer = executeQuery1("SELECT cashback_percentage FROM db_txn_service.bnpl_line_offers where segment=\"L1\" and merchant_type=\"type2\" and surrogate_type=\"EQUIFAX\" and lower_band=\"1\";");
			System.out.println(cashback_percentage_offer );
			String cash_fee_percentage_with_gst_offer = executeQuery1("SELECT cash_fee_percentage_with_gst FROM db_txn_service.bnpl_line_offers where segment=\"L1\" and merchant_type=\"type2\" and surrogate_type=\"EQUIFAX\" and lower_band=\"1\";");
			System.out.println(cash_fee_percentage_with_gst_offer);
			
			String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
			JSONObject obj = new JSONObject(json_data);
			String allowed_loan_amount = obj.getString("allowed_loan_amount");
			System.out.println(allowed_loan_amount);
			String allowed_cashback = obj.getString("cashback_percentage");
			System.out.println(allowed_cashback);
			String cash_fee_percentage_with_gst = obj.getString("cash_fee_percentage_with_gst");
			System.out.println(cash_fee_percentage_with_gst);
			
			if((limit_amount.equals(allowed_loan_amount)) && (cashback_percentage_offer.equals(allowed_cashback)) && (cash_fee_percentage_with_gst_offer.equals(cash_fee_percentage_with_gst))) {
				System.out.println("yes");
				extent.extentLoggerPass("TC_Ring_Customer_Seg_146","TC_Ring_Customer_Seg_146-To verfiy user eligible for THIN and Thin Limit as 0 or Null amount");
			}
		}
		
		//================================================To verify user receives Gamified Cashback based on Txn Count=3 =======================================

		public void TC_Ring_Engagememt_01() throws Exception{
			getDriver().resetApp();
			extent.HeaderChildNode("TC_Ring_Engagememt_01");
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo = "7" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo);
			System.out.println(mobNo);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			getWebDriver().get("https://new-admin-panel.test.ideopay.in/customer_engagement/cashback");
			explicitWaitVisibilityNonDefault(CashBackPage.addEngagementOfferBtn,20);
			click(CashBackPage.addEngagementOfferBtn,"Add engagement offer button");
			String offerCode = generateRandomString(8) + "_offer";
			click(CashBackPage.offerCode,"Offer code text field");
			type(CashBackPage.offerCode,offerCode,"Offer code text field");
			click(CashBackPage.cashBackValue,"Offer value text field");
			String cashBackValue = RandomIntegerGenerator(2);
			type(CashBackPage.cashBackValue,cashBackValue,"Offer value text field");
			click(CashBackPage.txnCountLowerLimit,"Txn count lower limit text field");
			type(CashBackPage.txnCountLowerLimit,"3","Txn count lower limit field");
			click(CashBackPage.txnCountUpperLimit,"Txn count upper limit text field");
			type(CashBackPage.txnCountUpperLimit,"3","Txn count upper limit field");
			/*click(CashBackPage.minTxnAmt,"Txn amount text field");
			type(CashBackPage.minTxnAmt,prop.getproperty("minTxnAmount"),"Txn amount field");*/
			scrollDownWEB();
			click(CashBackPage.validityEndDate,"Validiy end date field");
			type(CashBackPage.validityEndDate,calendarDateCashBack(10),"Validiy end date field");
			click(CashBackPage.addEngagementSubmitButton,"Submit button");
			System.out.println(userRefNo);
			waitTime(5000);
			scrollToTopOfPageWEB();
			click(CashBackPage.editConfigBtn,"Edit configuration button");
			click(CashBackPage.editConfigBtn,"Edit configuration button");
			ScrollToTheElement(CashBackPage.activeRadioBtn);
			click(CashBackPage.activeRadioBtn,"Active radio button");
			click(CashBackPage.addEngagementSubmitButton,"Submit config button");
			engagementWhiteList(userRefNo,offerCode);
			setPlatform("Android");
			initDriver();
			waitTime(5000);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthMonth"), prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthYear"));
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
			addAddress("asasa","asaas","asasa","asq","560102");
			waitTime(5000);
			instaLoancongratsScreen();
			instaLoanSetPin(prop.getproperty("ValidPin"),prop.getproperty("ValidPin"));
			waitTime(8000);
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			txnApi(userRefNo,mobNo,"46");
			waitTime(3000);
			txnApi(userRefNo,mobNo,"25");
			waitTime(3000);
			txnApi(userRefNo,mobNo,"21");
			click(RingLoginPage.objTopMenu,"Top left menu");
			explicitWaitVisibilityNonDefault(RingLoginPage.objProfileCrossBtn,30);
			click(RingLoginPage.objProfileCrossBtn,"Profile Cross Button");
			
			explicitWaitVisibilityNonDefault(CashBackPage.cashBackHeader,30);
			
			extent.extentLoggerPass("TC_Ring_Engagememt_01","TC_Ring_Engagememt_01-To verify CB is generated for having minimum Scan & Pay txn count = 3 & appropriate CB amt is created");
			Back(1);
			waitTime(3000);
			verifyElementPresentAndClick(UserRegistrationPage.objNoBtn, getText(UserRegistrationPage.objNoBtn));
			Swipe("UP",1);
			explicitWaitVisibilityNonDefault(CashBackPage.cashBackRecentTxn,30);
			click(CashBackPage.rewardsTab,"Rewards Tab");
			explicitWaitVisibilityNonDefault(CashBackPage.cashBackTxn,30);
			
			extent.extentLoggerPass("TC_Ring_Engagememt_07","TC_Ring_Engagememt_07-To verify Cashback entry is created under Recent Txn history and Cashback tab of app");

		}	

		public void TC_Ring_Engagememt_02() throws Exception{
			getDriver().resetApp();
			extent.HeaderChildNode("TC_Ring_Engagememt_02");
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo = "7" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo);
			System.out.println(mobNo);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			getWebDriver().get("https://new-admin-panel.test.ideopay.in/customer_engagement/cashback");
			explicitWaitVisibilityNonDefault(CashBackPage.addEngagementOfferBtn,20);
			click(CashBackPage.addEngagementOfferBtn,"Add engagement offer button");
			String offerCode = generateRandomString(8) + "_offer";
			click(CashBackPage.offerCode,"Offer code text field");
			type(CashBackPage.offerCode,offerCode,"Offer code text field");
			click(CashBackPage.cashBackValue,"Offer value text field");
			String cashBackValue = RandomIntegerGenerator(2);
			type(CashBackPage.cashBackValue,cashBackValue,"Offer value text field");
			click(CashBackPage.txnCountLowerLimit,"Txn count lower limit text field");
			type(CashBackPage.txnCountLowerLimit,"3","Txn count lower limit field");
			click(CashBackPage.txnCountUpperLimit,"Txn count upper limit text field");
			type(CashBackPage.txnCountUpperLimit,"3","Txn count upper limit field");
			/*click(CashBackPage.minTxnAmt,"Txn amount text field");
			type(CashBackPage.minTxnAmt,prop.getproperty("minTxnAmount"),"Txn amount field");*/
			scrollDownWEB();
			click(CashBackPage.validityEndDate,"Validiy end date field");
			type(CashBackPage.validityEndDate,calendarDateCashBack(10),"Validiy end date field");
			click(CashBackPage.addEngagementSubmitButton,"Submit button");
			System.out.println(userRefNo);
			waitTime(5000);
			scrollToTopOfPageWEB();
			click(CashBackPage.editConfigBtn,"Edit configuration button");
			click(CashBackPage.editConfigBtn,"Edit configuration button");
			ScrollToTheElement(CashBackPage.activeRadioBtn);
			click(CashBackPage.activeRadioBtn,"Active radio button");
			click(CashBackPage.addEngagementSubmitButton,"Submit config button");
			engagementWhiteList(userRefNo,offerCode);
			setPlatform("Android");
			initDriver();
			waitTime(5000);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthMonth"), prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthYear"));
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
			addAddress("asasa","asaas","asasa","asq","560102");
			waitTime(5000);
			instaLoancongratsScreen();
			instaLoanSetPin(prop.getproperty("ValidPin"),prop.getproperty("ValidPin"));
			waitTime(8000);
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			txnApi(userRefNo,mobNo,"39");
			waitTime(3000);
			txnApi(userRefNo,mobNo,"31");
			click(RingLoginPage.objTopMenu,"Top left menu");
			explicitWaitVisibilityNonDefault(RingLoginPage.objProfileCrossBtn,30);
			click(RingLoginPage.objProfileCrossBtn,"Profile Cross Button");
			waitTime(8000);
			explicitWaitVisibility(HomePage.txtAreYouEnjoyingApp,30);
			explicitWaitVisibility(HomePage.noBtn,30);
			click(HomePage.noBtn,"No button");
			if(verifyElementNotPresent(CashBackPage.cashBackHeader,30)) {
				extent.extentLoggerPass("TC_Ring_Engagememt_02","TC_Ring_Engagememt_02-To verify CB is not generated for having minimum Scan & Pay txn count <3 when Txn count set as 3");
			}
			
			
			
		}
		
		public void TC_Ring_Engagememt_03() throws Exception{
			getDriver().resetApp();
			extent.HeaderChildNode("TC_Ring_Engagememt_03");
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo = "7" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo);
			System.out.println(mobNo);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo = getText(InstaLoanPage.objUserRefNo);
			engagementWhiteList(userRefNo,prop.getproperty("expiredOfferCode_txnCount"));
			setPlatform("Android");
			initDriver();
			waitTime(5000);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthMonth"), prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthYear"));
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
			addAddress("asasa","asaas","asasa","asq","560102");
			waitTime(5000);
			instaLoancongratsScreen();
			instaLoanSetPin(prop.getproperty("ValidPin"),prop.getproperty("ValidPin"));
			waitTime(8000);
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			txnApi(userRefNo,mobNo,"105");
			waitTime(3000);
			txnApi(userRefNo,mobNo,"110");
			click(RingLoginPage.objTopMenu,"Top left menu");
			explicitWaitVisibilityNonDefault(RingLoginPage.objProfileCrossBtn,30);
			click(RingLoginPage.objProfileCrossBtn,"Profile Cross Button");
			waitTime(8000);
			explicitWaitVisibility(HomePage.txtAreYouEnjoyingApp,30);
			explicitWaitVisibility(HomePage.noBtn,30);
			click(HomePage.noBtn,"No button");
			if(verifyElementNotPresent(CashBackPage.cashBackHeader,30)) {
				extent.extentLoggerPass("TC_Ring_Engagememt_03","TC_Ring_Engagememt_03-To verify CB is not generated for having minimum Scan & Pay txn count  of 3 & the valditiy is expired.");
			}
			
			
			
		}
		
		public void TC_Ring_Engagememt_04() throws Exception{
			getDriver().resetApp();
			extent.HeaderChildNode("TC_Ring_Engagememt_04");
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo = "7" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo);
			System.out.println(mobNo);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			getWebDriver().get("https://new-admin-panel.test.ideopay.in/customer_engagement/cashback");
			explicitWaitVisibilityNonDefault(CashBackPage.addEngagementOfferBtn,20);
			click(CashBackPage.addEngagementOfferBtn,"Add engagement offer button");
			String offerCode = generateRandomString(8) + "_offer";
			click(CashBackPage.offerCode,"Offer code text field");
			type(CashBackPage.offerCode,offerCode,"Offer code text field");
			click(CashBackPage.cashBackValue,"Offer value text field");
			String cashBackValue = RandomIntegerGenerator(2);
			type(CashBackPage.cashBackValue,cashBackValue,"Offer value text field");
			click(CashBackPage.txnCountLowerLimit,"Txn count lower limit text field");
			type(CashBackPage.txnCountLowerLimit,"1","Txn count lower limit field");
			click(CashBackPage.txnCountUpperLimit,"Txn count upper limit text field");
			type(CashBackPage.txnCountUpperLimit,"1","Txn count upper limit field");
			click(CashBackPage.minTxnAmt,"Txn amount text field");
			type(CashBackPage.minTxnAmt,prop.getproperty("minTxnAmount"),"Txn amount field");
			scrollDownWEB();
			click(CashBackPage.validityEndDate,"Validiy end date field");
			type(CashBackPage.validityEndDate,calendarDateCashBack(10),"Validiy end date field");
			click(CashBackPage.addEngagementSubmitButton,"Submit button");
			System.out.println(userRefNo);
			waitTime(5000);
			scrollToTopOfPageWEB();
			click(CashBackPage.editConfigBtn,"Edit configuration button");
			click(CashBackPage.editConfigBtn,"Edit configuration button");
			ScrollToTheElement(CashBackPage.activeRadioBtn);
			click(CashBackPage.activeRadioBtn,"Active radio button");
			click(CashBackPage.addEngagementSubmitButton,"Submit config button");
			engagementWhiteList(userRefNo,offerCode);
			setPlatform("Android");
			initDriver();
			waitTime(5000);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthMonth"), prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthYear"));
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
			addAddress("asasa","asaas","asasa","asq","560102");
			waitTime(5000);
			instaLoancongratsScreen();
			instaLoanSetPin(prop.getproperty("ValidPin"),prop.getproperty("ValidPin"));
			waitTime(8000);
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objMoreBtn, 10);
			click(UtilitiesBillPage.objMoreBtn, "More Button");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objPayBillsHeader, 10);
			click(UtilitiesBillPage.objElectricityBtn, "Electricity Button Option");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objSelectBiller, 10);
			waitTime(5000);
			click(UtilitiesBillPage.objBillerSearch, "Biller search text field");
			type(UtilitiesBillPage.objBillerSearch, "Besc", "Biller search text field");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objBescomBiller, 10);
			click(UtilitiesBillPage.objBescomBiller, "Bescom Biller");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objCustId, 10);
			click(UtilitiesBillPage.objCustId, "Customer ID text field");
			type(UtilitiesBillPage.objCustId, "10001", "Customer ID text field");
			click(UtilitiesBillPage.objFetchBtn, "Fetch Bill Button");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objPayBtn, 10);
			click(UtilitiesBillPage.objPayBtn, "Pay button");
			waitTime(5000);
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objPayRingBtn, 10);
			click(UtilitiesBillPage.objPayRingBtn, "Pay with ring limit button");
			explicitWaitVisibilityNonDefault(InstaLoanOptionalJourney.objEnterSecurityPinforElectricityBill, 20);
			click(InstaLoanOptionalJourney.objPINFieldElectricityBill, "PIN Field");
			type(InstaLoanOptionalJourney.objPINFieldElectricityBill, prop.getproperty("ValidPin"), "PIN Field");
			verifyElementPresentAndClick(InstaLoanOptionalJourney.objContinueBtn, getText(InstaLoanOptionalJourney.objContinueBtn));
			waitTime(15000);
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objTransacSuccess, 30);
			Back(1);
			explicitWaitVisibilityNonDefault(CashBackPage.cashBackHeader,30);
			explicitWaitVisibility(HomePage.txtAreYouEnjoyingApp,30);
			explicitWaitVisibility(HomePage.noBtn,30);
			click(HomePage.noBtn,"No button");
			extent.extentLoggerPass("TC_Ring_Engagememt_04","TC_Ring_Engagememt_04-To verify Txn count based CB is  applied to both  Bank Transfer  and BBPS txn types along with scan & pay");
		
			
		
		}
		
		public void TC_Ring_Engagememt_05() throws Exception{
			getDriver().resetApp();
			extent.HeaderChildNode("TC_Ring_Engagememt_05");
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo = "7" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo);
			System.out.println(mobNo);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			getWebDriver().get("https://new-admin-panel.test.ideopay.in/customer_engagement/cashback");
			explicitWaitVisibilityNonDefault(CashBackPage.addEngagementOfferBtn,20);
			click(CashBackPage.addEngagementOfferBtn,"Add engagement offer button");
			String offerCode = generateRandomString(8) + "_offer";
			click(CashBackPage.offerCode,"Offer code text field");
			type(CashBackPage.offerCode,offerCode,"Offer code text field");
			click(CashBackPage.cashBackValue,"Offer value text field");
			String cashBackValue = RandomIntegerGenerator(2);
			type(CashBackPage.cashBackValue,cashBackValue,"Offer value text field");
			click(CashBackPage.txnCountLowerLimit,"Txn count lower limit text field");
			type(CashBackPage.txnCountLowerLimit,"2","Txn count lower limit field");
			click(CashBackPage.txnCountUpperLimit,"Txn count upper limit text field");
			type(CashBackPage.txnCountUpperLimit,"2","Txn count upper limit field");
			/*click(CashBackPage.minTxnAmt,"Txn amount text field");
			type(CashBackPage.minTxnAmt,prop.getproperty("minTxnAmount"),"Txn amount field");*/
			scrollDownWEB();
			click(CashBackPage.validityEndDate,"Validiy end date field");
			type(CashBackPage.validityEndDate,calendarDateCashBack(10),"Validiy end date field");
			click(CashBackPage.addEngagementSubmitButton,"Submit button");
			System.out.println(userRefNo);
			waitTime(5000);
			engagementWhiteList(userRefNo,offerCode);
			setPlatform("Android");
			initDriver();
			waitTime(5000);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthMonth"), prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthYear"));
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
			addAddress("asasa","asaas","asasa","asq","560102");
			waitTime(5000);
			instaLoancongratsScreen();
			instaLoanSetPin(prop.getproperty("ValidPin"),prop.getproperty("ValidPin"));
			waitTime(8000);
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			txnApi(userRefNo,mobNo,"105");
			waitTime(3000);
			txnApi(userRefNo,mobNo,"110");
			click(RingLoginPage.objTopMenu,"Top left menu");
			explicitWaitVisibilityNonDefault(RingLoginPage.objProfileCrossBtn,30);
			click(RingLoginPage.objProfileCrossBtn,"Profile Cross Button");
			waitTime(8000);
			explicitWaitVisibility(HomePage.txtAreYouEnjoyingApp,30);
			explicitWaitVisibility(HomePage.noBtn,30);
			click(HomePage.noBtn,"No button");
			if(verifyElementNotPresent(CashBackPage.cashBackHeader,30)) {
				extent.extentLoggerPass("TC_Ring_Engagememt_05","TC_Ring_Engagememt_05-To verify cashback ui is not visible when offer is marked as INACTIVE");
			}
		
		}
		
		public void TC_Ring_Engagememt_06() throws Exception{
			getDriver().resetApp();
			extent.HeaderChildNode("TC_Ring_Engagememt_06");
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo = "7" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo);
			System.out.println(mobNo);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			getWebDriver().get("https://new-admin-panel.test.ideopay.in/customer_engagement/cashback");
			explicitWaitVisibilityNonDefault(CashBackPage.addEngagementOfferBtn,20);
			click(CashBackPage.addEngagementOfferBtn,"Add engagement offer button");
			String offerCode = generateRandomString(8) + "_offer";
			click(CashBackPage.offerCode,"Offer code text field");
			type(CashBackPage.offerCode,offerCode,"Offer code text field");
			click(CashBackPage.cashBackValue,"Offer value text field");
			String cashBackValue = RandomIntegerGenerator(2);
			type(CashBackPage.cashBackValue,cashBackValue,"Offer value text field");
			click(CashBackPage.txnCountLowerLimit,"Txn count lower limit text field");
			type(CashBackPage.txnCountLowerLimit,"3","Txn count lower limit field");
			click(CashBackPage.txnCountUpperLimit,"Txn count upper limit text field");
			type(CashBackPage.txnCountUpperLimit,"3","Txn count upper limit field");
			/*click(CashBackPage.minTxnAmt,"Txn amount text field");
			type(CashBackPage.minTxnAmt,prop.getproperty("minTxnAmount"),"Txn amount field");*/
			scrollDownWEB();
			click(CashBackPage.validityEndDate,"Validiy end date field");
			type(CashBackPage.validityEndDate,calendarDateCashBack(10),"Validiy end date field");
			click(CashBackPage.addEngagementSubmitButton,"Submit button");
			System.out.println(userRefNo);
			waitTime(5000);
			engagementWhiteList(userRefNo,offerCode);
			setPlatform("Android");
			initDriver();
			waitTime(5000);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthMonth"), prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthYear"));
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
			addAddress("asasa","asaas","asasa","asq","560102");
			waitTime(5000);
			instaLoancongratsScreen();
			instaLoanSetPin(prop.getproperty("ValidPin"),prop.getproperty("ValidPin"));
			waitTime(8000);
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			txnApi(userRefNo,mobNo,"30");
			waitTime(3000);
			txnApi(userRefNo,mobNo,"40");
			waitTime(3000);
			txnApi(userRefNo,mobNo,"40");
			click(RingLoginPage.objTopMenu,"Top left menu");
			explicitWaitVisibilityNonDefault(RingLoginPage.objProfileCrossBtn,30);
			click(RingLoginPage.objProfileCrossBtn,"Profile Cross Button");
			waitTime(8000);
			explicitWaitVisibility(HomePage.txtAreYouEnjoyingApp,30);
			explicitWaitVisibility(HomePage.noBtn,30);
			click(HomePage.noBtn,"No button");
			if(verifyElementNotPresent(CashBackPage.cashBackHeader,30)) {
				extent.extentLoggerPass("TC_Ring_Engagememt_06","TC_Ring_Engagememt_06-If user history has txn status cancelled/refunded, pending/in_process, then those txn count should not be considered during offer period");
			}
			
		}
		public void TC_Ring_Engagememt_08() throws Exception{
			getDriver().resetApp();
			extent.HeaderChildNode("TC_Ring_Engagememt_08");
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo = "7" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo);
			System.out.println(mobNo);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			getWebDriver().get("https://new-admin-panel.test.ideopay.in/customer_engagement/cashback");
			explicitWaitVisibilityNonDefault(CashBackPage.addEngagementOfferBtn,20);
			click(CashBackPage.addEngagementOfferBtn,"Add engagement offer button");
			String offerCode = generateRandomString(8) + "_offer";
			click(CashBackPage.offerCode,"Offer code text field");
			type(CashBackPage.offerCode,offerCode,"Offer code text field");
			click(CashBackPage.cashBackValue,"Offer value text field");
			String cashBackValue = RandomIntegerGenerator(2);
			type(CashBackPage.cashBackValue,cashBackValue,"Offer value text field");
			click(CashBackPage.txnCountLowerLimit,"Txn count lower limit text field");
			type(CashBackPage.txnCountLowerLimit,"3","Txn count lower limit field");
			click(CashBackPage.txnCountUpperLimit,"Txn count upper limit text field");
			type(CashBackPage.txnCountUpperLimit,"3","Txn count upper limit field");
			click(CashBackPage.minTxnAmt,"Txn amount text field");
			type(CashBackPage.minTxnAmt,prop.getproperty("minTxnAmount"),"Txn amount field");
			scrollDownWEB();
			click(CashBackPage.validityEndDate,"Validiy end date field");
			type(CashBackPage.validityEndDate,calendarDateCashBack(10),"Validiy end date field");
			click(CashBackPage.addEngagementSubmitButton,"Submit button");
			System.out.println(userRefNo);
			waitTime(5000);
			scrollToTopOfPageWEB();
			click(CashBackPage.editConfigBtn,"Edit configuration button");
			click(CashBackPage.editConfigBtn,"Edit configuration button");
			ScrollToTheElement(CashBackPage.activeRadioBtn);
			click(CashBackPage.activeRadioBtn,"Active radio button");
			click(CashBackPage.addEngagementSubmitButton,"Submit config button");
			engagementWhiteList(userRefNo,offerCode);
			setPlatform("Android");
			initDriver();
			waitTime(5000);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthMonth"), prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthYear"));
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
			addAddress("asasa","asaas","asasa","asq","560102");
			waitTime(5000);
			instaLoancongratsScreen();
			instaLoanSetPin(prop.getproperty("ValidPin"),prop.getproperty("ValidPin"));
			waitTime(8000);
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			txnApi(userRefNo,mobNo,"102");
			waitTime(3000);
			txnApi(userRefNo,mobNo,"104");
			waitTime(3000);
			txnApi(userRefNo,mobNo,"109");
			click(RingLoginPage.objTopMenu,"Top left menu");
			explicitWaitVisibilityNonDefault(RingLoginPage.objProfileCrossBtn,30);
			click(RingLoginPage.objProfileCrossBtn,"Profile Cross Button");
			explicitWaitVisibilityNonDefault(CashBackPage.cashBackHeader,30);
			explicitWaitVisibility(HomePage.txtAreYouEnjoyingApp,30);
			explicitWaitVisibility(HomePage.noBtn,30);
			click(HomePage.noBtn,"No button");
			extent.extentLoggerPass("TC_Ring_Engagememt_08","TC_Ring_Engagememt_08-To verify CB is generated for having minimum txn Amt =100 & appropriate CB amt is created for fresh txn");
			
		}	
		
		public void TC_Ring_Engagememt_09() throws Exception{
			extent.HeaderChildNode("TC_Ring_Engagememt_09");
			getDriver().resetApp();
			System.out.println(calendarDate(15));
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo = "7" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo);
			System.out.println(mobNo);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			getWebDriver().get("https://new-admin-panel.test.ideopay.in/customer_engagement/cashback");
			explicitWaitVisibilityNonDefault(CashBackPage.addEngagementOfferBtn,20);
			click(CashBackPage.addEngagementOfferBtn,"Add engagement offer button");
			String offerCode = generateRandomString(8) + "_offer";
			click(CashBackPage.offerCode,"Offer code text field");
			type(CashBackPage.offerCode,offerCode,"Offer code text field");
			click(CashBackPage.cashBackValue,"Offer value text field");
			String cashBackValue = RandomIntegerGenerator(2);
			type(CashBackPage.cashBackValue,cashBackValue,"Offer value text field");
			click(CashBackPage.txnCountLowerLimit,"Txn count lower limit text field");
			type(CashBackPage.txnCountLowerLimit,"3","Txn count lower limit field");
			click(CashBackPage.txnCountUpperLimit,"Txn count upper limit text field");
			type(CashBackPage.txnCountUpperLimit,"3","Txn count upper limit field");
			click(CashBackPage.minTxnAmt,"Txn amount text field");
			type(CashBackPage.minTxnAmt,prop.getproperty("minTxnAmount"),"Txn amount field");
			scrollDownWEB();
			click(CashBackPage.validityEndDate,"Validiy end date field");
			type(CashBackPage.validityEndDate,calendarDateCashBack(10),"Validiy end date field");
			click(CashBackPage.addEngagementSubmitButton,"Submit button");
			System.out.println(userRefNo);
			waitTime(5000);
			scrollToTopOfPageWEB();
			click(CashBackPage.editConfigBtn,"Edit configuration button");
			click(CashBackPage.editConfigBtn,"Edit configuration button");
			ScrollToTheElement(CashBackPage.activeRadioBtn);
			click(CashBackPage.activeRadioBtn,"Active radio button");
			click(CashBackPage.addEngagementSubmitButton,"Submit config button");
			engagementWhiteList(userRefNo,offerCode);
			cibilDummy(prop.getproperty("ltbc1"), userRefNo,"no","Sunil Chatla");
			setPlatform("Android");
			initDriver();
			waitTime(5000);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthMonth"), prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthYear"));
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(10000);
			addAddress("asasa","asaas","asasa","asq","560102");
			waitTime(5000);
			instaLoancongratsScreen();
			instaLoanSetPin(prop.getproperty("ValidPin"),prop.getproperty("ValidPin"));
			waitTime(5000);
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			String lineRefNo = executeQuery1("SELECT line_reference_number FROM db_txn_service.lines where user_reference_number='"+userRefNo+"';");
			txnApi(userRefNo,mobNo,"300");
			click(RingLoginPage.objTopMenu,"Top left menu");
			explicitWaitVisibilityNonDefault(RingLoginPage.objProfileCrossBtn,30);
			click(RingLoginPage.objProfileCrossBtn,"Profile Cross Button");
			generateForceBill(userRefNo,lineRefNo,calendarDate(14));
			explicitWaitVisibilityNonDefault(BankTransferModule.objNoBtn,30);
			click(BankTransferModule.objNoBtn,"No button");
			explicitWaitClickable(HomPageNew.objPayEarlyBtn, 30);
			Aclick(HomPageNew.objPayEarlyBtn, "Pay Early Button");
			explicitWaitVisibilityNonDefault(HomPageNew.objRepaymentHeader, 10);
			waitTime(3000);
			hideKeyboard();
			explicitWaitVisibilityNonDefault(HomPageNew.objNetBankingOption, 10);
			Aclick(HomPageNew.objNetBankingOption, "Net Banking/Debit card option");
			explicitWaitVisibilityNonDefault(HomPageNew.objNetbanking,20);
			Aclick(HomPageNew.objNetbanking, "Netbanking");
			explicitWaitVisibilityNonDefault(HomPageNew.objSelectBankHeader, 10);
			Aclick(HomPageNew.objSBIBank, "SBI bank");
			Aclick(HomPageNew.objPayNowBtn, "Pay Now Button");
			explicitWaitVisibilityNonDefault(HomPageNew.objSuccessBtn, 10);
			Aclick(HomPageNew.objSuccessBtn, "Success Button");
			waitTime(4000);
			explicitWaitVisibilityNonDefault(PaymentPage.objGoToHomePage,30);
			click(PaymentPage.objGoToHomePage,"Go to homepage button");
			System.out.println("Repeat set");
			explicitWaitVisibilityNonDefault(RingLoginPage.objIAcceptCheckBox,30);
			click(RingLoginPage.objIAcceptCheckBox,"Terms & Conditions check box");
			click(UserRegistrationNew.objOkGotIt, "OK Got it Button");
			
			txnApi(userRefNo,mobNo,"102");
			waitTime(3000);
			txnApi(userRefNo,mobNo,"106");
			waitTime(3000);
			txnApi(userRefNo,mobNo,"108");
			click(RingLoginPage.objTopMenu,"Top left menu");
			explicitWaitVisibilityNonDefault(RingLoginPage.objProfileCrossBtn,30);
			click(RingLoginPage.objProfileCrossBtn,"Profile Cross Button");
			
			explicitWaitVisibilityNonDefault(CashBackPage.cashBackHeader,30);
			
			extent.extentLoggerPass("TC_Ring_Engagememt_09","TC_Ring_Engagememt_09-To verify CB is generated for having minimum txn Amt =100 & appropriate CB amt is created for repeat txn");
			
		}
		
		public void TC_Ring_Engagememt_10() throws Exception{
			getDriver().resetApp();
				extent.HeaderChildNode("TC_Ring_Engagememt_10");
				enablePermissions();
				Aclick(RingLoginPage.objLoginLink,"Sign up login link");
				loginMobile();
				String mobNo = "7" + RandomIntegerGenerator(9);
				mobileNoValidation2(mobNo);
				System.out.println(mobNo);
				enterOtp("888888");
				readAndAccept();
				waitTime(3000);
				newAdminPanel_appScore("0.007666832");
		        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
				explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
				String userRefNo = getText(InstaLoanPage.objUserRefNo);
				engagementWhiteList(userRefNo,prop.getproperty("expiredOfferCode_txnAmt"));
				setPlatform("Android");
				initDriver();
				waitTime(5000);
				userDetails();
				dateOfBirth(prop.getproperty("dateOfBirthMonth"), prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthYear"));
				Aclick(UserRegistrationNew.objProceed, "Proceed Button");
				waitTime(15000);
				addAddress("asasa","asaas","asasa","asq","560102");
				waitTime(5000);
				instaLoancongratsScreen();
				instaLoanSetPin(prop.getproperty("ValidPin"),prop.getproperty("ValidPin"));
				waitTime(8000);
				verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
				txnApi(userRefNo,mobNo,"105");
				waitTime(3000);
				txnApi(userRefNo,mobNo,"110");
				waitTime(3000);
				txnApi(userRefNo,mobNo,"119");
				click(RingLoginPage.objTopMenu,"Top left menu");
				explicitWaitVisibilityNonDefault(RingLoginPage.objProfileCrossBtn,30);
				click(RingLoginPage.objProfileCrossBtn,"Profile Cross Button");
				waitTime(8000);
				explicitWaitVisibility(HomePage.txtAreYouEnjoyingApp,30);
				explicitWaitVisibility(HomePage.noBtn,30);
				click(HomePage.noBtn,"No button");
				if(verifyElementNotPresent(CashBackPage.cashBackHeader,30)) {
					extent.extentLoggerPass("TC_Ring_Engagememt_10","TC_Ring_Engagememt_10-To verify CB is not generated for having minimum Scan & Pay txn count  of 3 & the valditiy is expired.");
				}
				
		}
		
		public void TC_Ring_Engagememt_11() throws Exception{
			getDriver().resetApp();
			extent.HeaderChildNode("TC_Ring_Engagememt_11");
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo = "7" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo);
			System.out.println(mobNo);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			getWebDriver().get("https://new-admin-panel.test.ideopay.in/customer_engagement/cashback");
			explicitWaitVisibilityNonDefault(CashBackPage.addEngagementOfferBtn,20);
			click(CashBackPage.addEngagementOfferBtn,"Add engagement offer button");
			String offerCode = generateRandomString(8) + "_offer";
			click(CashBackPage.offerCode,"Offer code text field");
			type(CashBackPage.offerCode,offerCode,"Offer code text field");
			click(CashBackPage.cashBackValue,"Offer value text field");
			String cashBackValue = RandomIntegerGenerator(2);
			type(CashBackPage.cashBackValue,cashBackValue,"Offer value text field");
			click(CashBackPage.txnCountLowerLimit,"Txn count lower limit text field");
			type(CashBackPage.txnCountLowerLimit,"1","Txn count lower limit field");
			click(CashBackPage.txnCountUpperLimit,"Txn count upper limit text field");
			type(CashBackPage.txnCountUpperLimit,"1","Txn count upper limit field");
			click(CashBackPage.minTxnAmt,"Txn amount text field");
			type(CashBackPage.minTxnAmt,prop.getproperty("minTxnAmount"),"Txn amount field");
			scrollDownWEB();
			click(CashBackPage.validityEndDate,"Validiy end date field");
			type(CashBackPage.validityEndDate,calendarDateCashBack(10),"Validiy end date field");
			click(CashBackPage.addEngagementSubmitButton,"Submit button");
			System.out.println(userRefNo);
			waitTime(5000);
			scrollToTopOfPageWEB();
			click(CashBackPage.editConfigBtn,"Edit configuration button");
			click(CashBackPage.editConfigBtn,"Edit configuration button");
			ScrollToTheElement(CashBackPage.activeRadioBtn);
			click(CashBackPage.activeRadioBtn,"Active radio button");
			click(CashBackPage.addEngagementSubmitButton,"Submit config button");
			engagementWhiteList(userRefNo,offerCode);
			setPlatform("Android");
			initDriver();
			waitTime(5000);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthMonth"), prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthYear"));
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
			addAddress("asasa","asaas","asasa","asq","560102");
			waitTime(5000);
			instaLoancongratsScreen();
			instaLoanSetPin(prop.getproperty("ValidPin"),prop.getproperty("ValidPin"));
			waitTime(8000);
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			
			click(BankTransferModule.objMoretTab, "Clicked On More Button");
			verifyElementPresent(BankTransferModule.objBankTransfer,getTextVal(BankTransferModule.objBankTransfer, "Module"));
			click(BankTransferModule.objBankTransfer, "Bank Transfer Module");
			addbankAccount("5", "Sunil");
			
			explicitWaitVisibilityNonDefault(BankTransferModule.objMakePaymentPage, 10);
			verifyElementPresent(BankTransferModule.objMakePaymentPage, "Make Payment Page Displayed");

			click(BankTransferModule.objEnterAmountTextField, "Amount Text Field");
			type(BankTransferModule.objEnterAmountTextField, "100", "Amount Text Field");
			click(BankTransferModule.objPayNowBtn, "Pay Now Button");

			verifyElementPresent(BankTransferModule.objEnterPinPage, "Confirm Payment Page");
			logger.info("User Navigaed to Confirm Payment Page");
			extent.extentLogger("Enter Pin", "User Navigaed to Confirm Payment Page");
			explicitWaitVisibilityNonDefault(BankTransferModule.objEnterPinTextField, 10);
			click(BankTransferModule.objEnterPinTextField, "Enter Pin Field");
			type(BankTransferModule.objEnterPinTextField, "1111", "Enter Pin Field");
			click(BankTransferModule.objProceedBtn, "Proceed Button");
			explicitWaitVisibilityNonDefault(BankTransferModule.objStatusPage,30);
			verifyElementPresent2(BankTransferModule.objStatusPage, "Payment Status Page");
			click(BankTransferModule.objHomePageBtn, "Go to Homepage");
			waitTime(4000);
			explicitWaitVisibility(HomePage.txtAreYouEnjoyingApp,30);
			explicitWaitVisibility(HomePage.noBtn,30);
			click(HomePage.noBtn,"No button");
			if(verifyElementNotPresent(CashBackPage.cashBackHeader,30)) {
				extent.extentLoggerPass("TC_Ring_Engagememt_11","TC_Ring_Engagememt_11-To verify Txn Amt based CB's is not applicable to Bank Transfer txn type");
			}
			
	}

		public void TC_Ring_Engagememt_12() throws Exception{
			getDriver().resetApp();
			extent.HeaderChildNode("TC_Ring_Engagememt_12");
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo = "7" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo);
			System.out.println(mobNo);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			getWebDriver().get("https://new-admin-panel.test.ideopay.in/customer_engagement/cashback");
			explicitWaitVisibilityNonDefault(CashBackPage.addEngagementOfferBtn,20);
			click(CashBackPage.addEngagementOfferBtn,"Add engagement offer button");
			String offerCode = generateRandomString(8) + "_offer";
			click(CashBackPage.offerCode,"Offer code text field");
			type(CashBackPage.offerCode,offerCode,"Offer code text field");
			click(CashBackPage.cashBackValue,"Offer value text field");
			String cashBackValue = RandomIntegerGenerator(2);
			type(CashBackPage.cashBackValue,cashBackValue,"Offer value text field");
			click(CashBackPage.txnCountLowerLimit,"Txn count lower limit text field");
			type(CashBackPage.txnCountLowerLimit,"3","Txn count lower limit field");
			click(CashBackPage.txnCountUpperLimit,"Txn count upper limit text field");
			type(CashBackPage.txnCountUpperLimit,"3","Txn count upper limit field");
			click(CashBackPage.minTxnAmt,"Txn amount text field");
			type(CashBackPage.minTxnAmt,prop.getproperty("minTxnAmount"),"Txn amount field");
			scrollDownWEB();
			click(CashBackPage.validityEndDate,"Validiy end date field");
			type(CashBackPage.validityEndDate,calendarDateCashBack(10),"Validiy end date field");
			click(CashBackPage.addEngagementSubmitButton,"Submit button");
			System.out.println(userRefNo);
			waitTime(5000);
			scrollToTopOfPageWEB();
			click(CashBackPage.editConfigBtn,"Edit configuration button");
			click(CashBackPage.editConfigBtn,"Edit configuration button");
			ScrollToTheElement(CashBackPage.activeRadioBtn);
			click(CashBackPage.activeRadioBtn,"Active radio button");
			click(CashBackPage.addEngagementSubmitButton,"Submit config button");
			engagementWhiteList(userRefNo,offerCode);
			setPlatform("Android");
			initDriver();
			waitTime(5000);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthMonth"), prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthYear"));
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
			addAddress("asasa","asaas","asasa","asq","560102");
			waitTime(5000);
			instaLoancongratsScreen();
			instaLoanSetPin(prop.getproperty("ValidPin"),prop.getproperty("ValidPin"));
			waitTime(8000);
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			txnApi(userRefNo,mobNo,"146");
			waitTime(3000);
			txnApi(userRefNo,mobNo,"125");
			waitTime(3000);
			txnApi(userRefNo,mobNo,"121");
			click(RingLoginPage.objTopMenu,"Top left menu");
			explicitWaitVisibilityNonDefault(RingLoginPage.objProfileCrossBtn,30);
			click(RingLoginPage.objProfileCrossBtn,"Profile Cross Button");
			explicitWaitVisibilityNonDefault(CashBackPage.cashBackHeader,30);
			explicitWaitVisibility(HomePage.txtAreYouEnjoyingApp,30);
			explicitWaitVisibility(HomePage.noBtn,30);
			click(HomePage.noBtn,"No button");
			extent.extentLoggerPass("TC_Ring_Engagememt_12","TC_Ring_Engagememt_12-To verify CB is generated for having both min txn amt = 100 and Txn count = 3 & appropriate CB amt is created [scan & pay txn]");
			
		}	
		
		public void TC_Ring_Engagememt_13() throws Exception{
			getDriver().resetApp();
			extent.HeaderChildNode("TC_Ring_Engagememt_13");
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo = "7" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo);
			System.out.println(mobNo);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			cibilDummy(prop.getproperty("ltbc1"), userRefNo,"no","Sunil Chatla");
			getWebDriver().get("https://new-admin-panel.test.ideopay.in/customer_engagement/cashback");
			explicitWaitVisibilityNonDefault(CashBackPage.addEngagementOfferBtn,20);
			click(CashBackPage.addEngagementOfferBtn,"Add engagement offer button");
			String offerCode = generateRandomString(8) + "_offer";
			click(CashBackPage.offerCode,"Offer code text field");
			type(CashBackPage.offerCode,offerCode,"Offer code text field");
			click(CashBackPage.cashBackValue,"Offer value text field");
			String cashBackValue = RandomIntegerGenerator(2);
			type(CashBackPage.cashBackValue,cashBackValue,"Offer value text field");
			click(CashBackPage.txnCountLowerLimit,"Txn count lower limit text field");
			type(CashBackPage.txnCountLowerLimit,"3","Txn count lower limit field");
			click(CashBackPage.txnCountUpperLimit,"Txn count upper limit text field");
			type(CashBackPage.txnCountUpperLimit,"3","Txn count upper limit field");
			click(CashBackPage.minTxnAmt,"Txn amount text field");
			type(CashBackPage.minTxnAmt,prop.getproperty("minTxnAmount"),"Txn amount field");
			scrollDownWEB();
			click(CashBackPage.validityEndDate,"Validiy end date field");
			type(CashBackPage.validityEndDate,calendarDateCashBack(10),"Validiy end date field");
			click(CashBackPage.addEngagementSubmitButton,"Submit button");
			System.out.println(userRefNo);
			waitTime(5000);
			scrollToTopOfPageWEB();
			click(CashBackPage.editConfigBtn,"Edit configuration button");
			click(CashBackPage.editConfigBtn,"Edit configuration button");
			ScrollToTheElement(CashBackPage.activeRadioBtn);
			click(CashBackPage.activeRadioBtn,"Active radio button");
			click(CashBackPage.addEngagementSubmitButton,"Submit config button");
			engagementWhiteList(userRefNo,offerCode);			
			setPlatform("Android");
			initDriver();
			waitTime(5000);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthMonth"), prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthYear"));
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
			addAddress("asasa","asaas","asasa","asq","560102");
			waitTime(5000);
			instaLoancongratsScreen();
			instaLoanSetPin(prop.getproperty("ValidPin"),prop.getproperty("ValidPin"));
			waitTime(8000);
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			txnApi(userRefNo,mobNo,"200");
			click(RingLoginPage.objTopMenu,"Top left menu");
			waitTime(3000);
			explicitWaitVisibilityNonDefault(RingLoginPage.objProfileCrossBtn,30);
			click(RingLoginPage.objProfileCrossBtn,"Profile Cross Button");
			explicitWaitVisibilityNonDefault(BankTransferModule.objNoBtn,30);
			click(BankTransferModule.objNoBtn,"No button");
			txnApi(userRefNo,mobNo,"101");
			waitTime(3000);
			txnApi(userRefNo,mobNo,"102");
			click(RingLoginPage.objTopMenu,"Top left menu");
			explicitWaitVisibilityNonDefault(RingLoginPage.objProfileCrossBtn,30);
			click(RingLoginPage.objProfileCrossBtn,"Profile Cross Button");
			explicitWaitVisibilityNonDefault(CashBackPage.cashBackHeader,30);
			Back(1);
			Swipe("UP",1);
			explicitWaitVisibilityNonDefault(CashBackPage.viewAllTab,10);
			click(CashBackPage.viewAllTab,"View All Transactions tab");
			explicitWaitVisibilityNonDefault(CashBackPage.engagementCashBack,20);
			if(verifyElementPresent(CashBackPage.engagementCashBack,"Engagement Cashback") && verifyElementPresent(CashBackPage.normalCashBack,"Normal cashback")) {
				extent.extentLoggerPass("TC_Ring_Engagememt_13","TC_Ring_Engagememt_13-To verify existing cashback applied to fresh txn [ bnpl_offergrid] along with Gamified cashback Active offer");
			}
			
		}
		
		public void TC_Ring_Engagememt_14() throws Exception{
			getDriver().resetApp();
			extent.HeaderChildNode("TC_Ring_Engagememt_14");
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo = "7" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo);
			System.out.println(mobNo);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			cibilDummy(prop.getproperty("ltbc1"), userRefNo,"no","Sunil Chatla");
			getWebDriver().get("https://new-admin-panel.test.ideopay.in/customer_engagement/cashback");
			explicitWaitVisibilityNonDefault(CashBackPage.addEngagementOfferBtn,20);
			click(CashBackPage.addEngagementOfferBtn,"Add engagement offer button");
			String offerCode = generateRandomString(8) + "_offer";
			click(CashBackPage.offerCode,"Offer code text field");
			type(CashBackPage.offerCode,offerCode,"Offer code text field");
			click(CashBackPage.cashBackValue,"Offer value text field");
			String cashBackValue = RandomIntegerGenerator(2);
			type(CashBackPage.cashBackValue,cashBackValue,"Offer value text field");
			click(CashBackPage.txnCountLowerLimit,"Txn count lower limit text field");
			type(CashBackPage.txnCountLowerLimit,"3","Txn count lower limit field");
			click(CashBackPage.txnCountUpperLimit,"Txn count upper limit text field");
			type(CashBackPage.txnCountUpperLimit,"3","Txn count upper limit field");
			click(CashBackPage.minTxnAmt,"Txn amount text field");
			type(CashBackPage.minTxnAmt,prop.getproperty("minTxnAmount"),"Txn amount field");
			scrollDownWEB();
			click(CashBackPage.validityEndDate,"Validiy end date field");
			type(CashBackPage.validityEndDate,calendarDateCashBack(10),"Validiy end date field");
			click(CashBackPage.addEngagementSubmitButton,"Submit button");
			System.out.println(userRefNo);
			waitTime(5000);
			scrollToTopOfPageWEB();
			click(CashBackPage.editConfigBtn,"Edit configuration button");
			click(CashBackPage.editConfigBtn,"Edit configuration button");
			ScrollToTheElement(CashBackPage.activeRadioBtn);
			click(CashBackPage.activeRadioBtn,"Active radio button");
			click(CashBackPage.addEngagementSubmitButton,"Submit config button");
			engagementWhiteList(userRefNo,offerCode);
			executeInsertCashback(userRefNo,"7.00","100.00","1",calendarDate(0),calendarDate(15));
			setPlatform("Android");
			initDriver();
			waitTime(5000);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthMonth"), prop.getproperty("dateOfBirthYear"));
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
			addAddress("asasa","asaas","asasa","asq","560102");
			waitTime(5000);
			instaLoancongratsScreen();
			instaLoanSetPin(prop.getproperty("ValidPin"),prop.getproperty("ValidPin"));
			waitTime(8000);
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			txnApi(userRefNo,mobNo,"200");
			click(RingLoginPage.objTopMenu,"Top left menu");
			waitTime(3000);
			explicitWaitVisibilityNonDefault(RingLoginPage.objProfileCrossBtn,30);
			click(RingLoginPage.objProfileCrossBtn,"Profile Cross Button");
			explicitWaitVisibility(HomePage.txtAreYouEnjoyingApp,30);
			explicitWaitVisibility(HomePage.noBtn,30);
			click(HomePage.noBtn,"No button");
			//explicitWaitVisibilityNonDefault(BankTransferModule.objNoBtn,30);
			//click(BankTransferModule.objNoBtn,"No button");
			txnApi(userRefNo,mobNo,"101");
			waitTime(3000);
			txnApi(userRefNo,mobNo,"102");
			click(RingLoginPage.objTopMenu,"Top left menu");
			explicitWaitVisibilityNonDefault(RingLoginPage.objProfileCrossBtn,30);
			click(RingLoginPage.objProfileCrossBtn,"Profile Cross Button");
			explicitWaitVisibilityNonDefault(CashBackPage.cashBackHeader,30);
			Back(1);
			Swipe("UP",1);
			explicitWaitVisibilityNonDefault(CashBackPage.viewAllTab,10);
			click(CashBackPage.viewAllTab,"View All Transactions tab");
			explicitWaitVisibilityNonDefault(CashBackPage.engagementCashBack,20);
			if(verifyElementPresent(CashBackPage.engagementCashBack,"Engagement Cashback") && verifyElementPresent(CashBackPage.normalCashBack,"Normal cashback")) {
				extent.extentLoggerPass("TC_Ring_Engagememt_13","TC_Ring_Engagememt_13-To verify existing cashback applied to fresh txn [ bnpl_offergrid] along with Gamified cashback Active offer");
			}	
			
		}
		
		public void TC_Ring_Engagememt_15() throws Exception{
			getDriver().resetApp();
			extent.HeaderChildNode("TC_Ring_Engagememt_15");
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo = "6" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo);
			System.out.println(mobNo);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			cibilDummy(prop.getproperty("ltbc1"), userRefNo,"no","Sunil Chatla");
			getWebDriver().get("https://new-admin-panel.test.ideopay.in/customer_engagement/cashback");
			explicitWaitVisibilityNonDefault(CashBackPage.addEngagementOfferBtn,20);
			click(CashBackPage.addEngagementOfferBtn,"Add engagement offer button");
			String offerCode = generateRandomString(8) + "_offer";
			click(CashBackPage.offerCode,"Offer code text field");
			type(CashBackPage.offerCode,offerCode,"Offer code text field");
			click(CashBackPage.cashBackValue,"Offer value text field");
			String cashBackValue = RandomIntegerGenerator(2);
			type(CashBackPage.cashBackValue,cashBackValue,"Offer value text field");
			click(CashBackPage.txnCountLowerLimit,"Txn count lower limit text field");
			type(CashBackPage.txnCountLowerLimit,"3","Txn count lower limit field");
			click(CashBackPage.txnCountUpperLimit,"Txn count upper limit text field");
			type(CashBackPage.txnCountUpperLimit,"3","Txn count upper limit field");
			click(CashBackPage.minTxnAmt,"Txn amount text field");
			type(CashBackPage.minTxnAmt,prop.getproperty("minTxnAmount"),"Txn amount field");
			scrollDownWEB();
			click(CashBackPage.validityEndDate,"Validiy end date field");
			type(CashBackPage.validityEndDate,calendarDateCashBack(10),"Validiy end date field");
			click(CashBackPage.addEngagementSubmitButton,"Submit button");
			System.out.println(userRefNo);
			waitTime(5000);
			scrollToTopOfPageWEB();
			click(CashBackPage.editConfigBtn,"Edit configuration button");
			click(CashBackPage.editConfigBtn,"Edit configuration button");
			ScrollToTheElement(CashBackPage.activeRadioBtn);
			click(CashBackPage.activeRadioBtn,"Active radio button");
			click(CashBackPage.addEngagementSubmitButton,"Submit config button");
			engagementWhiteList(userRefNo,offerCode);
			setPlatform("Android");
			initDriver();
			waitTime(5000);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthMonth"), prop.getproperty("dateOfBirthYear"));
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
			addAddress("asasa","asaas","asasa","asq","560102");
			waitTime(5000);
			instaLoancongratsScreen();
			instaLoanSetPin(prop.getproperty("ValidPin"),prop.getproperty("ValidPin"));
			waitTime(8000);
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			txnApi(userRefNo,mobNo,"105");
			
			click(BankTransferModule.objMoretTab, "Clicked On More Button");
			verifyElementPresent(BankTransferModule.objBankTransfer,getTextVal(BankTransferModule.objBankTransfer, "Module"));
			click(BankTransferModule.objBankTransfer, "Bank Transfer Module");
			addbankAccount("5", "Sunil");
			explicitWaitVisibilityNonDefault(BankTransferModule.objMakePaymentPage, 10);
			verifyElementPresent(BankTransferModule.objMakePaymentPage, "Make Payment Page Displayed");
			click(BankTransferModule.objEnterAmountTextField, "Amount Text Field");
			type(BankTransferModule.objEnterAmountTextField, "100", "Amount Text Field");
			click(BankTransferModule.objPayNowBtn, "Pay Now Button");
			verifyElementPresent(BankTransferModule.objEnterPinPage, "Confirm Payment Page");
			logger.info("User Navigaed to Confirm Payment Page");
			extent.extentLogger("Enter Pin", "User Navigaed to Confirm Payment Page");
			explicitWaitVisibilityNonDefault(BankTransferModule.objEnterPinTextField, 10);
			click(BankTransferModule.objEnterPinTextField, "Enter Pin Field");
			type(BankTransferModule.objEnterPinTextField, "1111", "Enter Pin Field");
			click(BankTransferModule.objProceedBtn, "Proceed Button");
			waitTime(15000);
			explicitWaitVisibilityNonDefault(BankTransferModule.objStatusPage,30);
			verifyElementPresent2(BankTransferModule.objStatusPage, "Payment Status Page");
			click(BankTransferModule.objHomePageBtn, "Go to Homepage");
			waitTime(4000);
			explicitWaitVisibility(HomePage.txtAreYouEnjoyingApp,30);
			explicitWaitVisibility(HomePage.noBtn,30);
			click(HomePage.noBtn,"No button");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objMoreBtn, 10);
			click(UtilitiesBillPage.objMoreBtn, "More Button");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objPayBillsHeader, 10);
			click(UtilitiesBillPage.objElectricityBtn, "Electricity Button Option");
			waitTime(3000);
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objSelectBiller, 30);
			waitTime(5000);
			click(UtilitiesBillPage.objBillerSearch, "Biller search text field");
			type(UtilitiesBillPage.objBillerSearch, "Besc", "Biller search text field");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objBescomBiller, 30);
			click(UtilitiesBillPage.objBescomBiller, "Bescom Biller");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objCustId, 30);
			click(UtilitiesBillPage.objCustId, "Customer ID text field");
			type(UtilitiesBillPage.objCustId, "10001", "Customer ID text field");
			click(UtilitiesBillPage.objFetchBtn, "Fetch Bill Button");
			waitTime(10000);
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objPayBtn, 30);
			click(UtilitiesBillPage.objPayBtn, "Pay button");
			waitTime(5000);
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objPayRingBtn, 30);
			click(UtilitiesBillPage.objPayRingBtn, "Pay with ring limit button");
			explicitWaitVisibilityNonDefault(InstaLoanOptionalJourney.objEnterSecurityPinforElectricityBill, 30);
			click(InstaLoanOptionalJourney.objPINFieldElectricityBill, "PIN Field");
			type(InstaLoanOptionalJourney.objPINFieldElectricityBill, prop.getproperty("ValidPin"), "PIN Field");
			verifyElementPresentAndClick(InstaLoanOptionalJourney.objContinueBtn, getText(InstaLoanOptionalJourney.objContinueBtn));
			waitTime(15000);
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objTransacSuccess, 30);
			Back(1);
			explicitWaitVisibilityNonDefault(CashBackPage.cashBackHeader,30);
			extent.extentLoggerPass("TC_Ring_Engagememt_15","TC_Ring_Engagememt_15-Run 3 consecutive txn such that 1st txn is scan Merchant QR, 2nd is self Transfer & last is card, on last txn Cashback of 10rs should be applied");
			
		}
		
		public void TC_Ring_Engagememt_16() throws Exception{
			getDriver().resetApp();
			extent.HeaderChildNode("TC_Ring_Engagememt_16");
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo = "6" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo);
			System.out.println(mobNo);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			cibilDummy(prop.getproperty("ltbc1"), userRefNo,"no","Sunil Chatla");
			getWebDriver().get("https://new-admin-panel.test.ideopay.in/customer_engagement/cashback");
			explicitWaitVisibilityNonDefault(CashBackPage.addEngagementOfferBtn,20);
			click(CashBackPage.addEngagementOfferBtn,"Add engagement offer button");
			String offerCode = generateRandomString(8) + "_offer";
			click(CashBackPage.offerCode,"Offer code text field");
			type(CashBackPage.offerCode,offerCode,"Offer code text field");
			click(CashBackPage.cashBackValue,"Offer value text field");
			String cashBackValue = RandomIntegerGenerator(2);
			type(CashBackPage.cashBackValue,cashBackValue,"Offer value text field");
			click(CashBackPage.txnCountLowerLimit,"Txn count lower limit text field");
			type(CashBackPage.txnCountLowerLimit,"3","Txn count lower limit field");
			click(CashBackPage.txnCountUpperLimit,"Txn count upper limit text field");
			type(CashBackPage.txnCountUpperLimit,"3","Txn count upper limit field");
			click(CashBackPage.minTxnAmt,"Txn amount text field");
			type(CashBackPage.minTxnAmt,prop.getproperty("minTxnAmount"),"Txn amount field");
			scrollDownWEB();
			click(CashBackPage.validityEndDate,"Validiy end date field");
			type(CashBackPage.validityEndDate,calendarDateCashBack(10),"Validiy end date field");
			click(CashBackPage.addEngagementSubmitButton,"Submit button");
			System.out.println(userRefNo);
			waitTime(5000);
			scrollToTopOfPageWEB();
			click(CashBackPage.editConfigBtn,"Edit configuration button");
			click(CashBackPage.editConfigBtn,"Edit configuration button");
			ScrollToTheElement(CashBackPage.activeRadioBtn);
			click(CashBackPage.activeRadioBtn,"Active radio button");
			click(CashBackPage.addEngagementSubmitButton,"Submit config button");
			engagementWhiteList(userRefNo,offerCode);
			setPlatform("Android");
			initDriver();
			waitTime(5000);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthMonth"), prop.getproperty("dateOfBirthYear"));
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
			addAddress("asasa","asaas","asasa","asq","560102");
			waitTime(5000);
			instaLoancongratsScreen();
			instaLoanSetPin(prop.getproperty("ValidPin"),prop.getproperty("ValidPin"));
			waitTime(8000);
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			txnApi(userRefNo,mobNo,"105");
			
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objMoreBtn, 10);
			click(UtilitiesBillPage.objMoreBtn, "More Button");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objPayBillsHeader, 10);
			click(UtilitiesBillPage.objElectricityBtn, "Electricity Button Option");
			waitTime(3000);
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objSelectBiller, 30);
			waitTime(5000);
			click(UtilitiesBillPage.objBillerSearch, "Biller search text field");
			type(UtilitiesBillPage.objBillerSearch, "Besc", "Biller search text field");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objBescomBiller, 30);
			click(UtilitiesBillPage.objBescomBiller, "Bescom Biller");
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objCustId, 30);
			click(UtilitiesBillPage.objCustId, "Customer ID text field");
			type(UtilitiesBillPage.objCustId, "10001", "Customer ID text field");
			click(UtilitiesBillPage.objFetchBtn, "Fetch Bill Button");
			waitTime(10000);
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objPayBtn, 30);
			click(UtilitiesBillPage.objPayBtn, "Pay button");
			waitTime(5000);
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objPayRingBtn, 30);
			click(UtilitiesBillPage.objPayRingBtn, "Pay with ring limit button");
			explicitWaitVisibilityNonDefault(InstaLoanOptionalJourney.objEnterSecurityPinforElectricityBill, 30);
			click(InstaLoanOptionalJourney.objPINFieldElectricityBill, "PIN Field");
			type(InstaLoanOptionalJourney.objPINFieldElectricityBill, prop.getproperty("ValidPin"), "PIN Field");
			verifyElementPresentAndClick(InstaLoanOptionalJourney.objContinueBtn, getText(InstaLoanOptionalJourney.objContinueBtn));
			waitTime(15000);
			explicitWaitVisibilityNonDefault(UtilitiesBillPage.objTransacSuccess, 30);
			Back(1);
			explicitWaitVisibility(HomePage.txtAreYouEnjoyingApp,30);
			explicitWaitVisibility(HomePage.noBtn,30);
			click(HomePage.noBtn,"No button");
			click(BankTransferModule.objMoretTab, "Clicked On More Button");
			verifyElementPresent(BankTransferModule.objBankTransfer,getTextVal(BankTransferModule.objBankTransfer, "Module"));
			click(BankTransferModule.objBankTransfer, "Bank Transfer Module");
			addbankAccount("5", "Sunil");
			explicitWaitVisibilityNonDefault(BankTransferModule.objMakePaymentPage, 10);
			verifyElementPresent(BankTransferModule.objMakePaymentPage, "Make Payment Page Displayed");
			click(BankTransferModule.objEnterAmountTextField, "Amount Text Field");
			type(BankTransferModule.objEnterAmountTextField, "100", "Amount Text Field");
			click(BankTransferModule.objPayNowBtn, "Pay Now Button");
			verifyElementPresent(BankTransferModule.objEnterPinPage, "Confirm Payment Page");
			logger.info("User Navigaed to Confirm Payment Page");
			extent.extentLogger("Enter Pin", "User Navigaed to Confirm Payment Page");
			explicitWaitVisibilityNonDefault(BankTransferModule.objEnterPinTextField, 10);
			click(BankTransferModule.objEnterPinTextField, "Enter Pin Field");
			type(BankTransferModule.objEnterPinTextField, "1111", "Enter Pin Field");
			click(BankTransferModule.objProceedBtn, "Proceed Button");
			waitTime(15000);
			explicitWaitVisibilityNonDefault(BankTransferModule.objStatusPage,30);
			verifyElementPresent2(BankTransferModule.objStatusPage, "Payment Status Page");
			click(BankTransferModule.objHomePageBtn, "Go to Homepage");
			waitTime(4000);
			
			explicitWaitVisibilityNonDefault(CashBackPage.cashBackHeader,30);
			extent.extentLoggerPass("TC_Ring_Engagememt_16","TC_Ring_Engagememt_16-Run 3 consecutive txn such that 1st txn is Scan MerchantQR, 2nd is card & 3rd is of Bank Transfer,  Cashback should  be applied");
			
		}
		
		//======================================================To verify GDI Active Transaction (Same GDI > 1)============================================================//	
		
		public void TC_Ring_Customer_Seg_81() throws Exception{
			getDriver().resetApp();
			extent.HeaderChildNode("TC_Ring_Customer_Seg_81");
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo = "6" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo);
			System.out.println(mobNo);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			cibilDummy(prop.getproperty("ltbc1"), userRefNo,"no","Sunil Chatla");
			setPlatform("Android");
			initDriver();
			waitTime(5000);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthMonth"), prop.getproperty("dateOfBirthYear"));
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
			Boolean flag = verifyElementNotPresent(RingUserDetailPage.objSorryMsg,10);
			
			if(flag) {
				System.out.println("yep");
				extent.extentLoggerPass("TC_Ring_Customer_Seg_81","TC_Ring_Customer_Seg_81-To verify user onboarding having same GDI with 1 user");
				extent.extentLoggerPass("TC_Ring_Customer_Seg_86","TC_Ring_Customer_Seg_86-To verify user onboarding having same GDI with 1 user");
			}
			
		}
		
		public void TC_Ring_Customer_Seg_82() throws Exception{
			getDriver().resetApp();
			extent.HeaderChildNode("TC_Ring_Customer_Seg_82");
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo_first = "6" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo_first);
			System.out.println(mobNo_first);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo_first+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo_first = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			cibilDummy(prop.getproperty("ltbc1"), userRefNo_first,"no","Sunil Chatla");
			setPlatform("Android");
			initDriver();
			waitTime(5000);
			customDigiScore(userRefNo_first,null,null);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthMonth"), prop.getproperty("dateOfBirthYear"));
			
			getDriver().resetApp();
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo_second = "6" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo_second);
			System.out.println(mobNo_second);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo_second+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo_second = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			cibilDummy(prop.getproperty("ltbc1"), userRefNo_second,"no","Sunil Chatla");
			setPlatform("Android");
			initDriver();
			waitTime(5000);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthMonth"), prop.getproperty("dateOfBirthYear"));
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
			Boolean flag = verifyElementPresent(RingUserDetailPage.objSorryMsg,"Sorry Message");
			//fetch rejection reason from line_application table as well
			if(flag) {
				System.out.println("yep");
				extent.extentLoggerPass("TC_Ring_Customer_Seg_82","TC_Ring_Customer_Seg_82-To verify user onboarding having same GDI with >1 user and earlier user application with same GDI is in INITIATE state");
			}
			
		}
		
		public void TC_Ring_Customer_Seg_83() throws Exception{
			getDriver().resetApp();
			extent.HeaderChildNode("TC_Ring_Customer_Seg_83");
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo_first = "6" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo_first);
			System.out.println(mobNo_first);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo_first+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo_first = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			cibilDummy(prop.getproperty("ltbc1"), userRefNo_first,"no","Sunil Chatla");
			setPlatform("Android");
			initDriver();
			waitTime(5000);
			customDigiScore(userRefNo_first,null,null);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthMonth"), prop.getproperty("dateOfBirthYear"));
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
			addAddress("asasa","asaas","asasa","asq","560102");
			waitTime(5000);
			
			getDriver().resetApp();
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo_second = "6" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo_second);
			System.out.println(mobNo_second);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo_second+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo_second = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			cibilDummy(prop.getproperty("ltbc1"), userRefNo_second,"no","Sunil Chatla");
			setPlatform("Android");
			initDriver();
			waitTime(5000);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthMonth"), prop.getproperty("dateOfBirthYear"));
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
			Boolean flag = verifyElementPresent(RingUserDetailPage.objSorryMsg,"Sorry Message");
			//fetch rejection reason from line_application table as well
			if(flag) {
				System.out.println("yep");
				extent.extentLoggerPass("TC_Ring_Customer_Seg_83","TC_Ring_Customer_Seg_83-To verify user onboarding having same GDI with >1 user and earlier user application with same GDI is in COND_APROVED state");
			}
		}
		
		public void TC_Ring_Customer_Seg_84() throws Exception{
			getDriver().resetApp();
			extent.HeaderChildNode("TC_Ring_Customer_Seg_84");
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo_first = "6" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo_first);
			System.out.println(mobNo_first);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo_first+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo_first = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			cibilDummy(prop.getproperty("ltbc1"), userRefNo_first,"no","Sunil Chatla");
			setPlatform("Android");
			initDriver();
			waitTime(5000);
			customDigiScore(userRefNo_first,null,null);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthMonth"), "1956");
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
			verifyElementPresent(RingUserDetailPage.objSorryMsg,"Sorry Message");
			
			getDriver().resetApp();
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo_second = "6" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo_second);
			System.out.println(mobNo_second);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo_second+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo_second = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			cibilDummy(prop.getproperty("ltbc1"), userRefNo_second,"no","Sunil Chatla");
			setPlatform("Android");
			initDriver();
			waitTime(5000);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthMonth"), prop.getproperty("dateOfBirthYear"));
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
			Boolean flag = verifyElementNotPresent(RingUserDetailPage.objSorryMsg,20);
			//fetch rejection reason from line_application table as well
			if(flag) {
				System.out.println("yep");
				extent.extentLoggerPass("TC_Ring_Customer_Seg_84","TC_Ring_Customer_Seg_84-To verify user onboarding having same GDI with >1 user and earlier user application with same GDI is in DENIED state");
			}
		}
		
		public void TC_Ring_Customer_Seg_85() throws Exception{
			getDriver().resetApp();
			extent.HeaderChildNode("TC_Ring_Customer_Seg_85");
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo_first = "6" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo_first);
			System.out.println(mobNo_first);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo_first+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo_first = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			cibilDummy(prop.getproperty("ltbc1"), userRefNo_first,"no","Sunil Chatla");
			setPlatform("Android");
			initDriver();
			waitTime(5000);
			customDigiScore(userRefNo_first,null,null);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthMonth"),prop.getproperty("dateOfBirthYear") );
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
			addAddress("asasa","asaas","asasa","asq","560102");
			waitTime(5000);
			instaLoancongratsScreen();
			
			getDriver().resetApp();
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo_second = "6" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo_second);
			System.out.println(mobNo_second);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo_second+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo_second = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			cibilDummy(prop.getproperty("ltbc1"), userRefNo_second,"no","Sunil Chatla");
			setPlatform("Android");
			initDriver();
			waitTime(5000);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthMonth"), prop.getproperty("dateOfBirthYear"));
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
			Boolean flag = verifyElementNotPresent(RingUserDetailPage.objSorryMsg,20);
			//fetch rejection reason from line_application table as well
			if(flag) {
				System.out.println("yep");
				extent.extentLoggerPass("TC_Ring_Customer_Seg_85","TC_Ring_Customer_Seg_85-To verify user onboarding having same GDI with >1 user and earlier user application with same GDI is in FINAL_APPROVED state");
			}			
		}
		
//======================================================To verify GDI Active Loan (Same GDI > 1)============================================================//	
		public void TC_Ring_Customer_Seg_87() throws Exception{
			getDriver().resetApp();
			extent.HeaderChildNode("TC_Ring_Customer_Seg_87");
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo_first = "6" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo_first);
			System.out.println(mobNo_first);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo_first+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo_first = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			cibilDummy(prop.getproperty("ltbc1"), userRefNo_first,"no","Sunil Chatla");
			setPlatform("Android");
			initDriver();
			waitTime(5000);
			customDigiScore(userRefNo_first,null,null);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthMonth"),prop.getproperty("dateOfBirthYear") );
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
			addAddress("asasa","asaas","asasa","asq","560102");
			waitTime(5000);
			instaLoancongratsScreen();
			instaLoanSetPin(prop.getproperty("ValidPin"),prop.getproperty("ValidPin"));
			waitTime(8000);
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			verifyElementNotPresent(HomePage.objPayEarlyBtn,20);
			
			getDriver().resetApp();
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo_second = "6" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo_second);
			System.out.println(mobNo_second);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo_second+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo_second = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			cibilDummy(prop.getproperty("ltbc1"), userRefNo_second,"no","Sunil Chatla");
			setPlatform("Android");
			initDriver();
			waitTime(5000);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthMonth"),prop.getproperty("dateOfBirthYear") );
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
			addAddress("asasa","asaas","asasa","asq","560102");
			waitTime(5000);
			instaLoancongratsScreen();
			instaLoanSetPin(prop.getproperty("ValidPin"),prop.getproperty("ValidPin"));
			waitTime(8000);
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			if(verifyElementPresent(HomePage.objAvailableLimit,"Available Limit header")) {
				System.out.println("yep");
				extent.extentLoggerPass("TC_Ring_Customer_Seg_87","TC_Ring_Customer_Seg_87-To verify user onboarding having same GDI with >1 user and earlier user application does not have loan");
				
			}
			
		}		
		public void TC_Ring_Customer_Seg_88() throws Exception{
			getDriver().resetApp();
			extent.HeaderChildNode("TC_Ring_Customer_Seg_88");
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo_first = "6" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo_first);
			System.out.println(mobNo_first);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo_first+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo_first = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			cibilDummy(prop.getproperty("ltbc1"), userRefNo_first,"no","Sunil Chatla");
			setPlatform("Android");
			initDriver();
			waitTime(5000);
			customDigiScore(userRefNo_first,null,null);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthMonth"),prop.getproperty("dateOfBirthYear") );
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
			addAddress("asasa","asaas","asasa","asq","560102");
			waitTime(5000);
			instaLoancongratsScreen();
			instaLoanSetPin(prop.getproperty("ValidPin"),prop.getproperty("ValidPin"));
			waitTime(8000);
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			txnApi(userRefNo_first,mobNo_first,"200");
			click(RingLoginPage.objTopMenu,"Top left menu");
			waitTime(3000);
			explicitWaitVisibilityNonDefault(RingLoginPage.objProfileCrossBtn,30);
			click(RingLoginPage.objProfileCrossBtn,"Profile Cross Button");
			verifyElementPresent(HomePage.objPayEarlyBtn,"Pay Early button");
			
			getDriver().resetApp();
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo_second = "6" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo_second);
			System.out.println(mobNo_second);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo_second+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo_second = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			cibilDummy(prop.getproperty("ltbc1"), userRefNo_second,"no","Sunil Chatla");
			setPlatform("Android");
			initDriver();
			waitTime(5000);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthMonth"),prop.getproperty("dateOfBirthYear") );
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
			Boolean flag = verifyElementPresent(RingUserDetailPage.objSorryMsg,"Sorry Message");
			//fetch rejection reason from line_application table as well
			if(flag) {
				System.out.println("yep");
				extent.extentLoggerPass("TC_Ring_Customer_Seg_88","TC_Ring_Customer_Seg_88-To verify user onboarding having same GDI with >1 user and earlier user application have loan");
			}
			
		}		
		
		public void TC_Ring_Customer_Seg_89() throws Exception{
			getDriver().resetApp();
			extent.HeaderChildNode("TC_Ring_Customer_Seg_89");
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo_first = "6" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo_first);
			System.out.println(mobNo_first);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo_first+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo_first = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			cibilDummy(prop.getproperty("ltbc1"), userRefNo_first,"no","Sunil Chatla");
			setPlatform("Android");
			initDriver();
			customDigiScore(userRefNo_first,null,null);
			waitTime(5000);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthMonth"),prop.getproperty("dateOfBirthYear") );
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
			addAddress("asasa","asaas","asasa","asq","560102");
			waitTime(5000);
			instaLoancongratsScreen();
			instaLoanSetPin(prop.getproperty("ValidPin"),prop.getproperty("ValidPin"));
			waitTime(8000);
			verifyElementPresentAndClick(RingUserDetailPage.objCrossBtn, "Cross Button");
			txnApi(userRefNo_first,mobNo_first,"200");
			click(RingLoginPage.objTopMenu,"Top left menu");
			waitTime(3000);
			explicitWaitVisibilityNonDefault(RingLoginPage.objProfileCrossBtn,30);
			click(RingLoginPage.objProfileCrossBtn,"Profile Cross Button");
			verifyElementPresent(HomePage.objPayEarlyBtn,"Pay Early button");
			click(HomePage.objPayEarlyBtn, "Pay Early");
			explicitWaitVisibilityNonDefault(HomPageNew.objRepaymentHeader, 10);
			Swipe("UP",1);
			waitTime(3000);
			click(HomPageNew.objNetBankingOption, "Net Banking/Debit card option");
			waitTime(3000);
			click(HomPageNew.objNetBankingOption, "Net Banking/Debit card option");
			explicitWaitVisibilityNonDefault(HomPageNew.objNetbanking, 10);
			click(HomPageNew.objNetbanking, "Netbanking");
			explicitWaitVisibilityNonDefault(HomPageNew.objSelectBankHeader, 10);
			click(HomPageNew.objSBIBank, "SBI bank");
			click(HomPageNew.objPayNowBtn, "Pay Now Button");
			explicitWaitVisibilityNonDefault(HomPageNew.objSuccessBtn, 10);
			click(HomPageNew.objSuccessBtn, "Success Button");
			explicitWaitVisibilityNonDefault(HomPageNew.objRepaySuccess, 10);
			click(HomPageNew.objHomePage, "Go to homepage button");
			
			getDriver().resetApp();
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo_second = "6" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo_second);
			System.out.println(mobNo_second);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo_second+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo_second = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			cibilDummy(prop.getproperty("ltbc1"), userRefNo_second,"no","Sunil Chatla");
			setPlatform("Android");
			initDriver();
			waitTime(5000);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthMonth"),prop.getproperty("dateOfBirthYear") );
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
			Boolean flag = verifyElementNotPresent(RingUserDetailPage.objSorryMsg,20);
			//fetch rejection reason from line_application table as well
			if(flag) {
				System.out.println("yep");
				extent.extentLoggerPass("TC_Ring_Customer_Seg_89","TC_Ring_Customer_Seg_89-To verify user onboarding having same GDI with >1 user and earlier user application have closed the loan");
			}
			
		}		
		//======================================================To verify Same user with multiple GDI (Different GDI > 4)============================================================//	
		public void TC_Ring_Customer_Seg_90() throws Exception{
			getDriver().resetApp();
			extent.HeaderChildNode("TC_Ring_Customer_Seg_90");
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo_first = "6" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo_first);
			System.out.println(mobNo_first);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo_first+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo_first = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			cibilDummy(prop.getproperty("ltbc1"), userRefNo_first,"no","Sunil Chatla");
			setPlatform("Android");
			initDriver();
			customDigiScore(userRefNo_first,"5",null);
			waitTime(5000);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthMonth"),prop.getproperty("dateOfBirthYear") );
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
			
			Boolean flag = verifyElementNotPresent(RingUserDetailPage.objSorryMsg,20);
			//fetch rejection reason from line_application table as well
			if(flag) {
				System.out.println("yep");
				extent.extentLoggerPass("TC_Ring_Customer_Seg_90","TC_Ring_Customer_Seg_90-To verify same user with multiple GDI >4");
			}
			
		}		
		
		//======================================================To verify Same user with multiple GDI within 2 weeks (Different GDI > 2)============================================================//	
		public void TC_Ring_Customer_Seg_91() throws Exception{
			getDriver().resetApp();
			extent.HeaderChildNode("TC_Ring_Customer_Seg_91");
			enablePermissions();
			Aclick(RingLoginPage.objLoginLink,"Sign up login link");
			loginMobile();
			String mobNo_first = "6" + RandomIntegerGenerator(9);
			mobileNoValidation2(mobNo_first);
			System.out.println(mobNo_first);
			enterOtp("888888");
			readAndAccept();
			waitTime(3000);
			newAdminPanel_appScore("0.007666832");
	        getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobNo_first+"&");
			explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
			String userRefNo_first = getText(InstaLoanPage.objUserRefNo);
			waitTime(3000);
			cibilDummy(prop.getproperty("ltbc1"), userRefNo_first,"no","Sunil Chatla");
			setPlatform("Android");
			initDriver();
			customDigiScore(userRefNo_first,null,"3");
			waitTime(5000);
			userDetails();
			dateOfBirth(prop.getproperty("dateOfBirthDate"), prop.getproperty("dateOfBirthMonth"),prop.getproperty("dateOfBirthYear") );
			Aclick(UserRegistrationNew.objProceed, "Proceed Button");
			waitTime(15000);
			
			Boolean flag = verifyElementNotPresent(RingUserDetailPage.objSorryMsg,20);
			//fetch rejection reason from line_application table as well
			if(flag) {
				System.out.println("yep");
				extent.extentLoggerPass("TC_Ring_Customer_Seg_90","TC_Ring_Customer_Seg_90-To verify same user with multiple GDI >4");
			}
			
		}		
		
	//======================================================To verify User receives offer from NTC (Band 1 to 7, Band 8 to 9)============================================================//	
				public void TC_Ring_Customer_Seg_162() throws Exception{
					getDriver().resetApp();
					extent.HeaderChildNode("TC_Ring_Customer_Seg_162");
					String userRefNo = ntcOffer("0.007666832");
					String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
					JSONObject obj = new JSONObject(json_data);
					String gb_segment = obj.getString("gb_segment");
					boolean ntc_customer = obj.getBoolean("is_ntc_customer");
					String offer_amount = obj.getString("allowed_loan_amount");
					
					System.out.println(gb_segment + ntc_customer + offer_amount);
					if(gb_segment.equals("NTC") && ntc_customer) {
						System.out.println("yep");
						extent.extentLoggerPass("TC_Ring_Customer_Seg_162","TC_Ring_Customer_Seg_162-To verify user is of NTC and app score falls between band 1 to 7");
					}
				}
				
				public void TC_Ring_Customer_Seg_163() throws Exception{
					getDriver().resetApp();
					extent.HeaderChildNode("TC_Ring_Customer_Seg_163");
					String userRefNo = ntcOffer("0.235737101000000");
					String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
					JSONObject obj = new JSONObject(json_data);
					String gb_segment = obj.getString("gb_segment");
					boolean ntc_customer = obj.getBoolean("is_ntc_customer");
					String offer_amount = obj.getString("allowed_loan_amount");
					
					System.out.println(gb_segment + ntc_customer + offer_amount);
					if(gb_segment.equals("NTC") && ntc_customer) {
						System.out.println("yep");
						extent.extentLoggerPass("TC_Ring_Customer_Seg_163","TC_Ring_Customer_Seg_163-To verify user is of NTC and app score falls between band 8 to 9");
					}
				}
				
				public void TC_Ring_Customer_Seg_164() throws Exception{
					getDriver().resetApp();
					extent.HeaderChildNode("TC_Ring_Customer_Seg_164");
					executeUpdate("update db_txn_service.bnpl_line_offers set limit_amount=\"0.00\" where segment=\"NTC\" and merchant_type=\"type2\" and surrogate_type=\"EQUIFAX\" and upper_band=\"21\";"," ");
					
					String userRefNo = ntcOffer("0.360352000000000");
					String json_data = executeQuery1("SELECT digi_data FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
					String reason = executeQuery1("SELECT rejection_reason FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");

					JSONObject obj = new JSONObject(json_data);
					String gb_segment = obj.getString("gb_segment");
					boolean ntc_customer = obj.getBoolean("is_ntc_customer");
					
					
					if(reason.equals("No Amount from Matrix") && gb_segment.equals("NTC") && ntc_customer) {
						System.out.println("yep");
						extent.extentLoggerPass("TC_Ring_Customer_Seg_164","TC_Ring_Customer_Seg_164-To verify user is of NTC and app score falls between band 10 to 20");
					}
				}
				
		//======================================================To verify Hex 13 checks============================================================//	
				public void TC_Ring_Customer_Seg_108() throws Exception{
					extent.HeaderChildNode("TC_Ring_Customer_Seg_108");
					List<Object> s = hex13("1","PlayStore");
					String userRefNo = s.get(0).toString();
					
					
					String reason = executeQuery1("SELECT isnull(rejection_reason) FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
					if(reason.equals("1")) {
						System.out.println("yep");
						
					}
				}
				
				public void TC_Ring_Customer_Seg_109() throws Exception{
					extent.HeaderChildNode("TC_Ring_Customer_Seg_109");
					List<Object> s = hex13("1","PlayStore");
					String userRefNo = s.get(0).toString();
					
					
					String reason = executeQuery1("SELECT rejection_reason FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
					if(reason.equals("Same hex and FA limit exceeds")) {
						System.out.println("yep");
						
					}
				}
				
				public void TC_Ring_Customer_Seg_110() throws Exception{
					extent.HeaderChildNode("TC_Ring_Customer_Seg_110");
					List<Object> s = hex13("1","Merchant");
					String userRefNo = s.get(0).toString();
					
					
					String reason = executeQuery1("SELECT isnull(rejection_reason) FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
					if(reason.equals("1")) {
						System.out.println("yep");
						
					}
				}
				
				public void TC_Ring_Customer_Seg_111() throws Exception{
					extent.HeaderChildNode("TC_Ring_Customer_Seg_111");
					List<Object> s = hex13("1","Promocode");
					String userRefNo = s.get(0).toString();
				
					
					String reason = executeQuery1("SELECT isnull(rejection_reason) FROM db_txn_service.line_application where user_reference_number='"+userRefNo+"';");
					if(reason.equals("1")) {
						System.out.println("yep");
						
					}
				}
				
//======================================================Shakir Hussain Ring Policy End============================================================//	
//========================================================KYC Bypass through API===================================================================//
	public void onloadapi(String mobNo) throws Exception {
		//mockUserAPI();
		/*setPlatform("Web");
		System.out.println("platform changed to web");
		String pf = getPlatform();
		System.out.println(pf);
		waitTime(5000);
		new RingPayBusinessLogic(null);
		waitTime(10000);
		getWebDriver().get(aadharFrontImage);
		waitTime(5000);
		Save_Image();
		//mockUserAPI();
		ValidatableResponse response1 = onload();
		ValidatableResponse response2 = sendOTP();
		ValidatableResponse response3 = userAuthentication();
		waitTime(5000);
		String userToken = response3.extract().jsonPath().getString("data.user_token");
		
		System.out.println(userToken);
		
		ValidatableResponse responsefront = frontAdharCard(userToken);
		waitTime(6000);
		getWebDriver().get(aadharBackImage);
		waitTime(10000);

		Save_Image();
		ValidatableResponse responseback = backAdhar(userToken);
		ValidatableResponse responseselfie = selfie(userToken);
		BrowsertearDown();
		setPlatform("Android");
		initDriver();*/
		setPlatform("Web");
		System.out.println("platform changed to web");
		String pf = getPlatform();
		System.out.println(pf);
		
		new RingPayBusinessLogic("ring");
		
		getWebDriver().get("https://new-admin-panel.test.ideopay.in/user/users?selected=mobile_number&search="+mobileNumber+"&");
		type(InstaLoanPage.objEmail,"shakir.muchumarri@collabera.com","Email text field");
		click(InstaLoanPage.objContinue,"Continue Button");
		
		explicitWaitVisibilityNonDefault(InstaLoanPage.objUserRefNo,10);
		String userRefNo = getText(InstaLoanPage.objUserRefNo);
		autoDocUpload(userRefNo,mobNo);
		BrowsertearDown();
		setPlatform("Android");
		initDriver();
	}

	public void Save_Image() throws IOException, InterruptedException, AWTException {
		deleteFile();
		Thread.sleep(3000);
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_S);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_S);
		waitTime(5000);
		StringSelection str = new StringSelection(System.getProperty("user.dir") + "\\Mock_Files\\KYC_Validation\\"+mobileNumber+"-frontandback.png");
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(str, null);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_V);
		waitTime(5000);
		robot.keyPress(KeyEvent.VK_ENTER);  
		waitTime(6000);
		  
	}
		public void deleteFile() {
			 File path = new File(System.getProperty("user.dir") + "\\Mock_Files\\KYC_Validation\\"); 
			 File[] files = path.listFiles();
			    for (File file : files) {
			        System.out.println("Deleted filename :"+ file.getName());
			        file.delete();
			    }
		}
		
		public static ValidatableResponse frontAdharCard(String token) throws IOException, URISyntaxException {
			String projectPath=System.getProperty("user.dir");
			Random rand = new Random();
			String randomnumber=String.valueOf(rand.nextInt(1000));
			String url="https://user-gateway.test.ideopay.in/api/v1/documents/upload-document";
			HashMap<String, Object> headers = new HashMap<>();
			
			headers.put("Token",token);
		
			File uri=new File(System.getProperty("user.dir")+"\\Mock_Files\\KYC_Validation\\"+mobileNumber+"-frontandback.png"); 
			ValidatableResponse response = RestAssured.given().auth().oauth2(token).baseUri(url)
					.param("source", "IDEOPAY_APP")
					.param("document_type_id", "3")
					.param("face", "front")
					.param("source_reference_type", "user")
					.param("document_source", "IDEOPAY")
					.multiPart("file", uri).when()
					.post().then();

			System.out.println("Request Url -->" + url);
			String Resp = response.extract().body().asString();
			System.out.println("Response Body= " + Resp);
			return response;
		}
		
		public static ValidatableResponse backAdhar(String token) throws IOException, URISyntaxException {
			String projectPath=System.getProperty("user.dir");
			Random rand = new Random();
			String randomnumber=String.valueOf(rand.nextInt(1000));
			String url="https://user-gateway.test.ideopay.in/api/v1/documents/upload-document";
			HashMap<String, Object> headers = new HashMap<>();
			
			headers.put("Token",token);
	
			File uri=new File(System.getProperty("user.dir") + "\\Mock_Files\\KYC_Validation\\"+mobileNumber+"-frontandback.png"); 
			ValidatableResponse response = RestAssured.given().auth().oauth2(token).baseUri(url)
					.param("source", "IDEOPAY_APP")
					.param("document_type_id", "3")
					.param("face", "back")
					.param("source_reference_type", "user")
					.param("document_source", "IDEOPAY")
					.multiPart("file", uri).when()
					.post().then();

			System.out.println("Request Url -->" + url);
			String Resp = response.extract().body().asString();
			System.out.println("Response Body= " + Resp);
			return response;
		}
		
		public static ValidatableResponse selfie(String token) throws IOException, URISyntaxException {
			String projectPath=System.getProperty("user.dir");
			Random rand = new Random();
			String randomnumber=String.valueOf(rand.nextInt(1000));
			String url="https://user-gateway.test.ideopay.in/api/v1/documents/upload-document";
			HashMap<String, Object> headers = new HashMap<>();
			headers.put("Token",token);
			File uri=new File(System.getProperty("user.dir") + "\\Mock_Files\\Shashi_Photo.JPG"); 
			ValidatableResponse response = RestAssured.given().auth().oauth2(token).baseUri(url)
					.param("source", "IDEOPAY_APP")
					.param("document_type_id", "92")
					.param("face", "front")
					.param("source_reference_type", "user")
					.param("document_source", "IDEOPAY")
					.multiPart("file", uri).when()
					.post().then();

			System.out.println("Request Url -->" + url);
			String Resp = response.extract().body().asString();
			System.out.println("Response Body= " + Resp);
			return response;
		}
	
		public void instalonAPIKycVerification(String mobNo) throws Exception {
				onloadapi(mobNo);	
		}
		
		public static ValidatableResponse sendOTP() {
			Random rand = new Random();
			String randomnumber=String.valueOf(rand.nextInt(1000));
			String url="https://user-gateway.test.ideopay.in/api/v1/users/android/send-otp";

			HashMap<String, String> req_body = new HashMap<>();

			req_body.put("mobile_number", mobileNumber);
			req_body.put("source_app", "IDEOPAY");

			org.json.simple.JSONObject Myrequestbody = new org.json.simple.JSONObject();

			Myrequestbody.put("mobile_number", req_body.get("mobile_number"));
			Myrequestbody.put("source_app", req_body.get("source_app"));

			HashMap<String, Object> headers = new HashMap<>();
			headers.put("x-request-id", randomnumber);
			headers.put("x-login-token", "eyJhbGciOiJSUzI1NiIsIng1YyI6WyJNSUlGWVRDQ0JFbWdBd0lCQWdJUkFQaEtkUXdrSUFNRENRQUFBQUM4QzZvd0RRWUpLb1pJaHZjTkFRRUxCUUF3UmpFTE1Ba0dBMVVFQmhNQ1ZWTXhJakFnQmdOVkJBb1RHVWR2YjJkc1pTQlVjblZ6ZENCVFpYSjJhV05sY3lCTVRFTXhFekFSQmdOVkJBTVRDa2RVVXlCRFFTQXhSRFF3SGhjTk1qRXhNREUzTVRjd05qQTNXaGNOTWpJd01URTFNVGN3TmpBMldqQWRNUnN3R1FZRFZRUURFeEpoZEhSbGMzUXVZVzVrY205cFpDNWpiMjB3Z2dFaU1BMEdDU3FHU0liM0RRRUJBUVVBQTRJQkR3QXdnZ0VLQW9JQkFRQ3ZnU2VHM3JTVlcwSVBpWkJGVmJoMktjYjNoTnl3R2VJOUZmaVgyUXZRQnBmUkIvT0xiUUFwZGdDWTZJL1dqNEw0aHVNQzRMVHA3OFZXbmhtZGJ3Y1NxbXJzNkpDM3kwWnVmVm4ydzhsV0NYODNsYytFUmdRVHhmaGUwTVNIakhlWk9mWGROQ3dqejZrTXJkZEVPUlJ5T3V3SWdjcXcrNGoycS9mSktHbkUyNXQ5NndOTDgrUDg1V294ZXhaZEROR1pzMmkzNmRvZkdVTGR1YTZaWFI1YjFlODJkd0dra0Rkd3RFMjZCeDRhTTl4VDEwK3A0S3FKNXZ0MWpvY1N0K2tTWHFRaEowQlJjS082OWhGUTRDSUdKYk5EYlRIMENGYlMvanJsNThGWnhVTUVwaUNHbG9JdmJyZ20xSlFzRDE2UmtIZlQ0NVM5UERNc3k5WFI4bjVqQWdNQkFBR2pnZ0p4TUlJQ2JUQU9CZ05WSFE4QkFmOEVCQU1DQmFBd0V3WURWUjBsQkF3d0NnWUlLd1lCQlFVSEF3RXdEQVlEVlIwVEFRSC9CQUl3QURBZEJnTlZIUTRFRmdRVUJ0M1lUWkFYZ3pGYXdpV2FXN3hmaStYRDhnZ3dId1lEVlIwakJCZ3dGb0FVSmVJWURySlhrWlFxNWRSZGhwQ0QzbE96dUpJd2JRWUlLd1lCQlFVSEFRRUVZVEJmTUNvR0NDc0dBUVVGQnpBQmhoNW9kSFJ3T2k4dmIyTnpjQzV3YTJrdVoyOXZaeTluZEhNeFpEUnBiblF3TVFZSUt3WUJCUVVITUFLR0pXaDBkSEE2THk5d2Eya3VaMjl2Wnk5eVpYQnZMMk5sY25SekwyZDBjekZrTkM1a1pYSXdIUVlEVlIwUkJCWXdGSUlTWVhSMFpYTjBMbUZ1WkhKdmFXUXVZMjl0TUNFR0ExVWRJQVFhTUJnd0NBWUdaNEVNQVFJQk1Bd0dDaXNHQVFRQjFua0NCUU13UHdZRFZSMGZCRGd3TmpBMG9ES2dNSVl1YUhSMGNEb3ZMMk55YkhNdWNHdHBMbWR2YjJjdlozUnpNV1EwYVc1MEwxZ3lTakpJY2w4M1VHbE5MbU55YkRDQ0FRUUdDaXNHQVFRQjFua0NCQUlFZ2ZVRWdmSUE4QUIxQUZHanNQWDlBWG1jVm0yNE4zaVBES1I2ekJzbnkvZWVpRUthRGY3VWl3WGxBQUFCZkk5dXVqSUFBQVFEQUVZd1JBSWdYd3JxbEEvV21IRFVySVpSWDIrS24raldjRVlsQjliVCtsRk9HT3RaTEtNQ0lGUzRXYU14Q09GaVAxTnhVN3hMcVBQVGlwR2dlaFgwS0IwTFgrTXhkdEl0QUhjQUtYbSs4SjQ1T1NId1ZuT2ZZNlYzNWI1WGZaeGdDdmo1VFYwbVhDVmR4NFFBQUFGOGoyNjZLUUFBQkFNQVNEQkdBaUVBNDdRNldJYmVnQUZuL0liUUM5OEFoR0dlY0xGVWowcjRCMnlrSkFlN2tzd0NJUURiQ2RNNFdzQ2JVUHJsSDhIV3M1ZGpqQWluKy9jWDZPNHpDTldMbzJxakhEQU5CZ2txaGtpRzl3MEJBUXNGQUFPQ0FRRUFMWHlhOUhVVm5rZURkUFgyd0tzQ2QybDhNcGpTeW5iVWVKWGI5Um04dXRsczRjRzkvdXEzRzZ3clRGWkNhdldJMnE5SmxlUnA1Q21DeCtrcElPVVh3T0dPQUZ3SVFrUFhCRnFrOGJscmE1MmhGTTluMUROYzY1bmNVRHkybXFYbjNXaVByN0crZEdSNlkzRnFKMjQ3K0VySlllbTZnM28rR3ZVcERxbWpkZ01SdHFFTXlmTVZIa0xoN3ZucWlXdnYzQ2VlU1ViRjkvMFdxUklNdTdPSFZyTkVET1ZUUEZuWENVczgyUk1OVVd0dVJTS1Njelh3QXFNN0JFWGR4TjNYcXE1Z1dOUDdUeFowczZzRTZGOHovWmN0OFVLdHRkNVBidGhrdGdFMmVvUmFaYTB1alNWVmtUeTVGb1pvMWJ1ZXhjbnM5WjlEWDFCUy9RU1JXbjNBUHc9PSIsIk1JSUZqRENDQTNTZ0F3SUJBZ0lOQWdDT3NnSXpObVdMWk0zYm16QU5CZ2txaGtpRzl3MEJBUXNGQURCSE1Rc3dDUVlEVlFRR0V3SlZVekVpTUNBR0ExVUVDaE1aUjI5dloyeGxJRlJ5ZFhOMElGTmxjblpwWTJWeklFeE1RekVVTUJJR0ExVUVBeE1MUjFSVElGSnZiM1FnVWpFd0hoY05NakF3T0RFek1EQXdNRFF5V2hjTk1qY3dPVE13TURBd01EUXlXakJHTVFzd0NRWURWUVFHRXdKVlV6RWlNQ0FHQTFVRUNoTVpSMjl2WjJ4bElGUnlkWE4wSUZObGNuWnBZMlZ6SUV4TVF6RVRNQkVHQTFVRUF4TUtSMVJUSUVOQklERkVORENDQVNJd0RRWUpLb1pJaHZjTkFRRUJCUUFEZ2dFUEFEQ0NBUW9DZ2dFQkFLdkFxcVBDRTI3bDB3OXpDOGRUUElFODliQSt4VG1EYUc3eTdWZlE0YyttT1dobFVlYlVRcEsweXYycjY3OFJKRXhLMEhXRGplcStuTElITjFFbTVqNnJBUlppeG15UlNqaElSMEtPUVBHQk1VbGRzYXp0SUlKN08wZy84MnFqL3ZHRGwvLzN0NHRUcXhpUmhMUW5UTFhKZGVCKzJEaGtkVTZJSWd4NndON0U1TmNVSDNSY3NlamNxajhwNVNqMTl2Qm02aTFGaHFMR3ltaE1Gcm9XVlVHTzN4dElIOTFkc2d5NGVGS2NmS1ZMV0szbzIxOTBRMExtL1NpS21MYlJKNUF1NHkxZXVGSm0ySk05ZUI4NEZrcWEzaXZyWFdVZVZ0eWUwQ1FkS3ZzWTJGa2F6dnh0eHZ1c0xKekxXWUhrNTV6Y1JBYWNEQTJTZUV0QmJRZkQxcXNDQXdFQUFhT0NBWFl3Z2dGeU1BNEdBMVVkRHdFQi93UUVBd0lCaGpBZEJnTlZIU1VFRmpBVUJnZ3JCZ0VGQlFjREFRWUlLd1lCQlFVSEF3SXdFZ1lEVlIwVEFRSC9CQWd3QmdFQi93SUJBREFkQmdOVkhRNEVGZ1FVSmVJWURySlhrWlFxNWRSZGhwQ0QzbE96dUpJd0h3WURWUjBqQkJnd0ZvQVU1SzhySm5FYUswZ25oUzlTWml6djhJa1RjVDR3YUFZSUt3WUJCUVVIQVFFRVhEQmFNQ1lHQ0NzR0FRVUZCekFCaGhwb2RIUndPaTh2YjJOemNDNXdhMmt1WjI5dlp5OW5kSE55TVRBd0JnZ3JCZ0VGQlFjd0FvWWthSFIwY0RvdkwzQnJhUzVuYjI5bkwzSmxjRzh2WTJWeWRITXZaM1J6Y2pFdVpHVnlNRFFHQTFVZEh3UXRNQ3N3S2FBbm9DV0dJMmgwZEhBNkx5OWpjbXd1Y0d0cExtZHZiMmN2WjNSemNqRXZaM1J6Y2pFdVkzSnNNRTBHQTFVZElBUkdNRVF3Q0FZR1o0RU1BUUlCTURnR0Npc0dBUVFCMW5rQ0JRTXdLakFvQmdnckJnRUZCUWNDQVJZY2FIUjBjSE02THk5d2Eya3VaMjl2Wnk5eVpYQnZjMmwwYjNKNUx6QU5CZ2txaGtpRzl3MEJBUXNGQUFPQ0FnRUFJVlRveTI0andYVXIwckFQYzkyNHZ1U1ZiS1F1WXczbkxmbExmTGg1QVlXRWVWbC9EdTE4UUFXVU1kY0o2by9xRlpiaFhrQkgwUE5jdzk3dGhhZjJCZW9EWVk5Q2svYitVR2x1aHgwNnpkNEVCZjdIOVA4NG5ucndwUis0R0JEWksrWGgzSTB0cUp5MnJnT3FORGZscjVJTVE4WlRXQTN5bHRha3pTQktaNlhwRjBQcHF5Q1J2cC9OQ0d2MktYMlR1UENKdnNjcDEvbTJwVlR0eUJqWVBSUStRdUNRR0FKS2p0TjdSNURGcmZUcU1XdllnVmxwQ0pCa3dsdTcrN0tZM2NUSWZ6RTdjbUFMc2tNS05MdUR6K1J6Q2NzWVRzVmFVN1ZwM3hMNjBPWWhxRmt1QU9PeERaNnBIT2o5K09KbVlnUG1PVDRYMys3TDUxZlhKeVJIOUtmTFJQNm5UMzFENW5tc0dBT2daMjYvOFQ5aHNCVzF1bzlqdTVmWkxaWFZWUzVIMEh5SUJNRUt5R01JUGhGV3JsdC9oRlMyOE4xemFLSTBaQkdEM2dZZ0RMYmlEVDlmR1hzdHBrK0ZtYzRvbFZsV1B6WGU4MXZkb0VuRmJyNU0yNzJIZGdKV28rV2hUOUJZTTBKaSt3ZFZtblJmZlhnbG9Fb2x1VE5jV3pjNDFkRnBnSnU4ZkYzTEcwZ2wyaWJTWWlDaTlhNmh2VTBUcHBqSnlJV1hoa0pUY01KbFByV3gxVnl0RVVHclgybDBKRHdSalcvNjU2cjBLVkIwMnhIUkt2bTJaS0kwM1RnbExJcG1WQ0sza0JLa0tOcEJOa0Z0OHJoYWZjQ0tPYjlKeC85dHBORmxRVGw3QjM5ckpsSldrUjE3UW5acVZwdEZlUEZPUm9abUZ6TT0iLCJNSUlGWWpDQ0JFcWdBd0lCQWdJUWQ3ME5iTnMyK1JycUlRL0U4RmpURFRBTkJna3Foa2lHOXcwQkFRc0ZBREJYTVFzd0NRWURWUVFHRXdKQ1JURVpNQmNHQTFVRUNoTVFSMnh2WW1Gc1UybG5iaUJ1ZGkxellURVFNQTRHQTFVRUN4TUhVbTl2ZENCRFFURWJNQmtHQTFVRUF4TVNSMnh2WW1Gc1UybG5iaUJTYjI5MElFTkJNQjRYRFRJd01EWXhPVEF3TURBME1sb1hEVEk0TURFeU9EQXdNREEwTWxvd1J6RUxNQWtHQTFVRUJoTUNWVk14SWpBZ0JnTlZCQW9UR1VkdmIyZHNaU0JVY25WemRDQlRaWEoyYVdObGN5Qk1URU14RkRBU0JnTlZCQU1UQzBkVVV5QlNiMjkwSUZJeE1JSUNJakFOQmdrcWhraUc5dzBCQVFFRkFBT0NBZzhBTUlJQ0NnS0NBZ0VBdGhFQ2l4N2pvWGViTzl5L2xENjNsYWRBUEtIOWd2bDlNZ2FDY2ZiMmpILzc2TnU4YWk2WGw2T01TL2tyOXJINXpvUWRzZm5GbDk3dnVmS2o2YndTaVY2bnFsS3IrQ01ueTZTeG5HUGIxNWwrOEFwZTYyaW05TVphUncxTkVEUGpUckVUbzhnWWJFdnMvQW1RMzUxa0tTVWpCNkcwMGowdVlPRFAwZ21IdTgxSThFM0N3bnFJaXJ1Nnoxa1oxcStQc0Fld25qSHhnc0hBM3k2bWJXd1pEclhZZmlZYVJRTTlzSG1rbENpdEQzOG01YWdJL3Bib1BHaVVVKzZET29nckZaWUpzdUI2akM1MTFwenJwMVprajVaUGFLNDlsOEtFajhDOFFNQUxYTDMyaDdNMWJLd1lVSCtFNEV6Tmt0TWc2VE84VXBtdk1yVXBzeVVxdEVqNWN1SEtaUGZtZ2hDTjZKM0Npb2o2T0dhSy9HUDVBZmw0L1h0Y2QvcDJoL3JzMzdFT2VaVlh0TDBtNzlZQjBlc1dDcnVPQzdYRnhZcFZxOU9zNnBGTEtjd1pwRElsVGlyeFpVVFFBczZxemttMDZwOThnN0JBZStkRHE2ZHNvNDk5aVlINlRLWC8xWTdEemt2Z3RkaXpqa1hQZHNEdFFDdjlVdyt3cDlVN0RiR0tvZ1BlTWEzTWQrcHZlejdXMzVFaUV1YSsrdGd5L0JCakZGRnkzbDNXRnBPOUtXZ3o3enBtN0FlS0p0OFQxMWRsZUNmZVhra1VBS0lBZjVxb0liYXBzWld3cGJrTkZoSGF4MnhJUEVEZ2ZnMWF6Vlk4MFpjRnVjdEw3VGxMbk1RLzBsVVRiaVN3MW5INjlNRzZ6TzBiOWY2QlFkZ0FtRDA2eUs1Nm1EY1lCWlVDQXdFQUFhT0NBVGd3Z2dFME1BNEdBMVVkRHdFQi93UUVBd0lCaGpBUEJnTlZIUk1CQWY4RUJUQURBUUgvTUIwR0ExVWREZ1FXQkJUa3J5c21jUm9yU0NlRkwxSm1MTy93aVJOeFBqQWZCZ05WSFNNRUdEQVdnQlJnZTJZYVJRMlh5b2xRTDMwRXpUU28vL3o5U3pCZ0JnZ3JCZ0VGQlFjQkFRUlVNRkl3SlFZSUt3WUJCUVVITUFHR0dXaDBkSEE2THk5dlkzTndMbkJyYVM1bmIyOW5MMmR6Y2pFd0tRWUlLd1lCQlFVSE1BS0dIV2gwZEhBNkx5OXdhMmt1WjI5dlp5OW5jM0l4TDJkemNqRXVZM0owTURJR0ExVWRId1FyTUNrd0o2QWxvQ09HSVdoMGRIQTZMeTlqY213dWNHdHBMbWR2YjJjdlozTnlNUzluYzNJeExtTnliREE3QmdOVkhTQUVOREF5TUFnR0JtZUJEQUVDQVRBSUJnWm5nUXdCQWdJd0RRWUxLd1lCQkFIV2VRSUZBd0l3RFFZTEt3WUJCQUhXZVFJRkF3TXdEUVlKS29aSWh2Y05BUUVMQlFBRGdnRUJBRFNrSHJFb285QzBkaGVtTVhvaDZkRlNQc2piZEJaQmlMZzlOUjN0NVArVDRWeGZxN3ZxZk0vYjVBM1JpMWZ5Sm05YnZoZEdhSlEzYjJ0NnlNQVlOL29sVWF6c2FMK3l5RW45V3ByS0FTT3NoSUFyQW95WmwrdEphb3gxMThmZXNzbVhuMWhJVnc0MW9lUWExdjF2ZzRGdjc0elBsNi9BaFNydzlVNXBDWkV0NFdpNHdTdHo2ZFRaL0NMQU54OExaaDFKN1FKVmoyZmhNdGZUSnI5dzR6MzBaMjA5Zk9VMGlPTXkrcWR1Qm1wdnZZdVI3aFpMNkR1cHN6Zm53MFNrZnRoczE4ZEc5WktiNTlVaHZtYVNHWlJWYk5RcHNnM0JabHZpZDBsSUtPMmQxeG96Y2xPemdqWFBZb3ZKSkl1bHR6a011MzRxUWI5U3oveWlscmJDZ2o4PSJdfQ.eyJub25jZSI6IlBvSEJNR1FXVTZMTHZuQ21tQUlqUkt4dTJ4ND0iLCJ0aW1lc3RhbXBNcyI6MTYzNzc1MTY1NTE2OSwiYXBrUGFja2FnZU5hbWUiOiJjb20uZmFzdGJhbmtpbmcuZGVidWciLCJhcGtEaWdlc3RTaGEyNTYiOiJsRHF1bDJxejdyd2owRDFJSzBkcTZwTnNaUmR0QW9BbUNNOVh5MGg2bkNjPSIsImN0c1Byb2ZpbGVNYXRjaCI6dHJ1ZSwiYXBrQ2VydGlmaWNhdGVEaWdlc3RTaGEyNTYiOlsiR3k3N1doNFRkR0ZXd3NoaS9VVXdDdUJIL0NBZ2V4VFFLdmJzbW5pWHFpTT0iXSwiYmFzaWNJbnRlZ3JpdHkiOnRydWUsImV2YWx1YXRpb25UeXBlIjoiQkFTSUMsSEFSRFdBUkVfQkFDS0VEIn0.ShOvWqQ_5i-T1ixx59sbk0-6LMo8oKiC5PfZCt9dVJrnfeap8JMQ9x8v19-Yh-M07y54BjQPXFGU-Y602uFc_V7TKHonDqjaEOsx6VfRwiQeZmtaO-Hhmlr2g-xRHFoDOnXy2wHYGfDkMbir50EraIyny3xfs-guIDMwg5qAzQaN999KRsrbHXX-a6wwoQ0qyUSVKGN57T_qOcXaq9X5bI1B3nD1m5Inu7TW0xrCb0sfUn8GDimAtnXELKf048S4iaXBObbgtiNyVQtTEfqHA8WdfhANIZWcV4XQDHbv69wcvrmUTDeZJienIfkmesfYnFDngW2NfR9A9m_Q5sorig");
			headers.put("x-login-nonce", "B6B667EB514890789F56F9B78BFA509AB41B673B");
			headers.put("x-login-timestamp","1636960116339");
			headers.put("X-Client-App", "android");
			headers.put("X-Client-Version", "1.1.5");
			headers.put("X-Client-OS-Type", "android");
			headers.put("X-Client-OS-Version", "10");
			
			ValidatableResponse response = RestAssured.given().baseUri(url).contentType(ContentType.JSON).headers(headers)
					.body(Myrequestbody.toJSONString()).when().post().then();
			System.out.println("Request Url -->" + url);
			System.out.println("Request :" + Myrequestbody);
			String Resp = response.extract().body().asString();
			System.out.println("Response Body= " + Resp);
			return response;
		}
		
		public static ValidatableResponse userAuthentication() {
			Random rand = new Random();
			String randomnumber=String.valueOf(rand.nextInt(1000));
			String url="https://user-gateway.test.ideopay.in/api/v1/users/android/authentication";

			HashMap<String, String> req_body = new HashMap<>();
			
			req_body.put("otp", "888888");
			req_body.put("mobile_number", mobileNumber);
			req_body.put("client_id", "MCUMMlBD7gs98HlHL3Py9Syk3xRsvvU5");
			req_body.put("source_app", "IDEOPAY");

			org.json.simple.JSONObject Myrequestbody = new org.json.simple.JSONObject();
			Myrequestbody.put("otp", req_body.get("otp"));
			Myrequestbody.put("mobile_number", req_body.get("mobile_number"));
			Myrequestbody.put("client_id", req_body.get("client_id"));
			Myrequestbody.put("source_app", req_body.get("source_app"));


			HashMap<String, Object> headers = new HashMap<>();
			headers.put("x-request-id", randomnumber);
			headers.put("x-login-token", "eyJhbGciOiJSUzI1NiIsIng1YyI6WyJNSUlGWVRDQ0JFbWdBd0lCQWdJUkFQaEtkUXdrSUFNRENRQUFBQUM4QzZvd0RRWUpLb1pJaHZjTkFRRUxCUUF3UmpFTE1Ba0dBMVVFQmhNQ1ZWTXhJakFnQmdOVkJBb1RHVWR2YjJkc1pTQlVjblZ6ZENCVFpYSjJhV05sY3lCTVRFTXhFekFSQmdOVkJBTVRDa2RVVXlCRFFTQXhSRFF3SGhjTk1qRXhNREUzTVRjd05qQTNXaGNOTWpJd01URTFNVGN3TmpBMldqQWRNUnN3R1FZRFZRUURFeEpoZEhSbGMzUXVZVzVrY205cFpDNWpiMjB3Z2dFaU1BMEdDU3FHU0liM0RRRUJBUVVBQTRJQkR3QXdnZ0VLQW9JQkFRQ3ZnU2VHM3JTVlcwSVBpWkJGVmJoMktjYjNoTnl3R2VJOUZmaVgyUXZRQnBmUkIvT0xiUUFwZGdDWTZJL1dqNEw0aHVNQzRMVHA3OFZXbmhtZGJ3Y1NxbXJzNkpDM3kwWnVmVm4ydzhsV0NYODNsYytFUmdRVHhmaGUwTVNIakhlWk9mWGROQ3dqejZrTXJkZEVPUlJ5T3V3SWdjcXcrNGoycS9mSktHbkUyNXQ5NndOTDgrUDg1V294ZXhaZEROR1pzMmkzNmRvZkdVTGR1YTZaWFI1YjFlODJkd0dra0Rkd3RFMjZCeDRhTTl4VDEwK3A0S3FKNXZ0MWpvY1N0K2tTWHFRaEowQlJjS082OWhGUTRDSUdKYk5EYlRIMENGYlMvanJsNThGWnhVTUVwaUNHbG9JdmJyZ20xSlFzRDE2UmtIZlQ0NVM5UERNc3k5WFI4bjVqQWdNQkFBR2pnZ0p4TUlJQ2JUQU9CZ05WSFE4QkFmOEVCQU1DQmFBd0V3WURWUjBsQkF3d0NnWUlLd1lCQlFVSEF3RXdEQVlEVlIwVEFRSC9CQUl3QURBZEJnTlZIUTRFRmdRVUJ0M1lUWkFYZ3pGYXdpV2FXN3hmaStYRDhnZ3dId1lEVlIwakJCZ3dGb0FVSmVJWURySlhrWlFxNWRSZGhwQ0QzbE96dUpJd2JRWUlLd1lCQlFVSEFRRUVZVEJmTUNvR0NDc0dBUVVGQnpBQmhoNW9kSFJ3T2k4dmIyTnpjQzV3YTJrdVoyOXZaeTluZEhNeFpEUnBiblF3TVFZSUt3WUJCUVVITUFLR0pXaDBkSEE2THk5d2Eya3VaMjl2Wnk5eVpYQnZMMk5sY25SekwyZDBjekZrTkM1a1pYSXdIUVlEVlIwUkJCWXdGSUlTWVhSMFpYTjBMbUZ1WkhKdmFXUXVZMjl0TUNFR0ExVWRJQVFhTUJnd0NBWUdaNEVNQVFJQk1Bd0dDaXNHQVFRQjFua0NCUU13UHdZRFZSMGZCRGd3TmpBMG9ES2dNSVl1YUhSMGNEb3ZMMk55YkhNdWNHdHBMbWR2YjJjdlozUnpNV1EwYVc1MEwxZ3lTakpJY2w4M1VHbE5MbU55YkRDQ0FRUUdDaXNHQVFRQjFua0NCQUlFZ2ZVRWdmSUE4QUIxQUZHanNQWDlBWG1jVm0yNE4zaVBES1I2ekJzbnkvZWVpRUthRGY3VWl3WGxBQUFCZkk5dXVqSUFBQVFEQUVZd1JBSWdYd3JxbEEvV21IRFVySVpSWDIrS24raldjRVlsQjliVCtsRk9HT3RaTEtNQ0lGUzRXYU14Q09GaVAxTnhVN3hMcVBQVGlwR2dlaFgwS0IwTFgrTXhkdEl0QUhjQUtYbSs4SjQ1T1NId1ZuT2ZZNlYzNWI1WGZaeGdDdmo1VFYwbVhDVmR4NFFBQUFGOGoyNjZLUUFBQkFNQVNEQkdBaUVBNDdRNldJYmVnQUZuL0liUUM5OEFoR0dlY0xGVWowcjRCMnlrSkFlN2tzd0NJUURiQ2RNNFdzQ2JVUHJsSDhIV3M1ZGpqQWluKy9jWDZPNHpDTldMbzJxakhEQU5CZ2txaGtpRzl3MEJBUXNGQUFPQ0FRRUFMWHlhOUhVVm5rZURkUFgyd0tzQ2QybDhNcGpTeW5iVWVKWGI5Um04dXRsczRjRzkvdXEzRzZ3clRGWkNhdldJMnE5SmxlUnA1Q21DeCtrcElPVVh3T0dPQUZ3SVFrUFhCRnFrOGJscmE1MmhGTTluMUROYzY1bmNVRHkybXFYbjNXaVByN0crZEdSNlkzRnFKMjQ3K0VySlllbTZnM28rR3ZVcERxbWpkZ01SdHFFTXlmTVZIa0xoN3ZucWlXdnYzQ2VlU1ViRjkvMFdxUklNdTdPSFZyTkVET1ZUUEZuWENVczgyUk1OVVd0dVJTS1Njelh3QXFNN0JFWGR4TjNYcXE1Z1dOUDdUeFowczZzRTZGOHovWmN0OFVLdHRkNVBidGhrdGdFMmVvUmFaYTB1alNWVmtUeTVGb1pvMWJ1ZXhjbnM5WjlEWDFCUy9RU1JXbjNBUHc9PSIsIk1JSUZqRENDQTNTZ0F3SUJBZ0lOQWdDT3NnSXpObVdMWk0zYm16QU5CZ2txaGtpRzl3MEJBUXNGQURCSE1Rc3dDUVlEVlFRR0V3SlZVekVpTUNBR0ExVUVDaE1aUjI5dloyeGxJRlJ5ZFhOMElGTmxjblpwWTJWeklFeE1RekVVTUJJR0ExVUVBeE1MUjFSVElGSnZiM1FnVWpFd0hoY05NakF3T0RFek1EQXdNRFF5V2hjTk1qY3dPVE13TURBd01EUXlXakJHTVFzd0NRWURWUVFHRXdKVlV6RWlNQ0FHQTFVRUNoTVpSMjl2WjJ4bElGUnlkWE4wSUZObGNuWnBZMlZ6SUV4TVF6RVRNQkVHQTFVRUF4TUtSMVJUSUVOQklERkVORENDQVNJd0RRWUpLb1pJaHZjTkFRRUJCUUFEZ2dFUEFEQ0NBUW9DZ2dFQkFLdkFxcVBDRTI3bDB3OXpDOGRUUElFODliQSt4VG1EYUc3eTdWZlE0YyttT1dobFVlYlVRcEsweXYycjY3OFJKRXhLMEhXRGplcStuTElITjFFbTVqNnJBUlppeG15UlNqaElSMEtPUVBHQk1VbGRzYXp0SUlKN08wZy84MnFqL3ZHRGwvLzN0NHRUcXhpUmhMUW5UTFhKZGVCKzJEaGtkVTZJSWd4NndON0U1TmNVSDNSY3NlamNxajhwNVNqMTl2Qm02aTFGaHFMR3ltaE1Gcm9XVlVHTzN4dElIOTFkc2d5NGVGS2NmS1ZMV0szbzIxOTBRMExtL1NpS21MYlJKNUF1NHkxZXVGSm0ySk05ZUI4NEZrcWEzaXZyWFdVZVZ0eWUwQ1FkS3ZzWTJGa2F6dnh0eHZ1c0xKekxXWUhrNTV6Y1JBYWNEQTJTZUV0QmJRZkQxcXNDQXdFQUFhT0NBWFl3Z2dGeU1BNEdBMVVkRHdFQi93UUVBd0lCaGpBZEJnTlZIU1VFRmpBVUJnZ3JCZ0VGQlFjREFRWUlLd1lCQlFVSEF3SXdFZ1lEVlIwVEFRSC9CQWd3QmdFQi93SUJBREFkQmdOVkhRNEVGZ1FVSmVJWURySlhrWlFxNWRSZGhwQ0QzbE96dUpJd0h3WURWUjBqQkJnd0ZvQVU1SzhySm5FYUswZ25oUzlTWml6djhJa1RjVDR3YUFZSUt3WUJCUVVIQVFFRVhEQmFNQ1lHQ0NzR0FRVUZCekFCaGhwb2RIUndPaTh2YjJOemNDNXdhMmt1WjI5dlp5OW5kSE55TVRBd0JnZ3JCZ0VGQlFjd0FvWWthSFIwY0RvdkwzQnJhUzVuYjI5bkwzSmxjRzh2WTJWeWRITXZaM1J6Y2pFdVpHVnlNRFFHQTFVZEh3UXRNQ3N3S2FBbm9DV0dJMmgwZEhBNkx5OWpjbXd1Y0d0cExtZHZiMmN2WjNSemNqRXZaM1J6Y2pFdVkzSnNNRTBHQTFVZElBUkdNRVF3Q0FZR1o0RU1BUUlCTURnR0Npc0dBUVFCMW5rQ0JRTXdLakFvQmdnckJnRUZCUWNDQVJZY2FIUjBjSE02THk5d2Eya3VaMjl2Wnk5eVpYQnZjMmwwYjNKNUx6QU5CZ2txaGtpRzl3MEJBUXNGQUFPQ0FnRUFJVlRveTI0andYVXIwckFQYzkyNHZ1U1ZiS1F1WXczbkxmbExmTGg1QVlXRWVWbC9EdTE4UUFXVU1kY0o2by9xRlpiaFhrQkgwUE5jdzk3dGhhZjJCZW9EWVk5Q2svYitVR2x1aHgwNnpkNEVCZjdIOVA4NG5ucndwUis0R0JEWksrWGgzSTB0cUp5MnJnT3FORGZscjVJTVE4WlRXQTN5bHRha3pTQktaNlhwRjBQcHF5Q1J2cC9OQ0d2MktYMlR1UENKdnNjcDEvbTJwVlR0eUJqWVBSUStRdUNRR0FKS2p0TjdSNURGcmZUcU1XdllnVmxwQ0pCa3dsdTcrN0tZM2NUSWZ6RTdjbUFMc2tNS05MdUR6K1J6Q2NzWVRzVmFVN1ZwM3hMNjBPWWhxRmt1QU9PeERaNnBIT2o5K09KbVlnUG1PVDRYMys3TDUxZlhKeVJIOUtmTFJQNm5UMzFENW5tc0dBT2daMjYvOFQ5aHNCVzF1bzlqdTVmWkxaWFZWUzVIMEh5SUJNRUt5R01JUGhGV3JsdC9oRlMyOE4xemFLSTBaQkdEM2dZZ0RMYmlEVDlmR1hzdHBrK0ZtYzRvbFZsV1B6WGU4MXZkb0VuRmJyNU0yNzJIZGdKV28rV2hUOUJZTTBKaSt3ZFZtblJmZlhnbG9Fb2x1VE5jV3pjNDFkRnBnSnU4ZkYzTEcwZ2wyaWJTWWlDaTlhNmh2VTBUcHBqSnlJV1hoa0pUY01KbFByV3gxVnl0RVVHclgybDBKRHdSalcvNjU2cjBLVkIwMnhIUkt2bTJaS0kwM1RnbExJcG1WQ0sza0JLa0tOcEJOa0Z0OHJoYWZjQ0tPYjlKeC85dHBORmxRVGw3QjM5ckpsSldrUjE3UW5acVZwdEZlUEZPUm9abUZ6TT0iLCJNSUlGWWpDQ0JFcWdBd0lCQWdJUWQ3ME5iTnMyK1JycUlRL0U4RmpURFRBTkJna3Foa2lHOXcwQkFRc0ZBREJYTVFzd0NRWURWUVFHRXdKQ1JURVpNQmNHQTFVRUNoTVFSMnh2WW1Gc1UybG5iaUJ1ZGkxellURVFNQTRHQTFVRUN4TUhVbTl2ZENCRFFURWJNQmtHQTFVRUF4TVNSMnh2WW1Gc1UybG5iaUJTYjI5MElFTkJNQjRYRFRJd01EWXhPVEF3TURBME1sb1hEVEk0TURFeU9EQXdNREEwTWxvd1J6RUxNQWtHQTFVRUJoTUNWVk14SWpBZ0JnTlZCQW9UR1VkdmIyZHNaU0JVY25WemRDQlRaWEoyYVdObGN5Qk1URU14RkRBU0JnTlZCQU1UQzBkVVV5QlNiMjkwSUZJeE1JSUNJakFOQmdrcWhraUc5dzBCQVFFRkFBT0NBZzhBTUlJQ0NnS0NBZ0VBdGhFQ2l4N2pvWGViTzl5L2xENjNsYWRBUEtIOWd2bDlNZ2FDY2ZiMmpILzc2TnU4YWk2WGw2T01TL2tyOXJINXpvUWRzZm5GbDk3dnVmS2o2YndTaVY2bnFsS3IrQ01ueTZTeG5HUGIxNWwrOEFwZTYyaW05TVphUncxTkVEUGpUckVUbzhnWWJFdnMvQW1RMzUxa0tTVWpCNkcwMGowdVlPRFAwZ21IdTgxSThFM0N3bnFJaXJ1Nnoxa1oxcStQc0Fld25qSHhnc0hBM3k2bWJXd1pEclhZZmlZYVJRTTlzSG1rbENpdEQzOG01YWdJL3Bib1BHaVVVKzZET29nckZaWUpzdUI2akM1MTFwenJwMVprajVaUGFLNDlsOEtFajhDOFFNQUxYTDMyaDdNMWJLd1lVSCtFNEV6Tmt0TWc2VE84VXBtdk1yVXBzeVVxdEVqNWN1SEtaUGZtZ2hDTjZKM0Npb2o2T0dhSy9HUDVBZmw0L1h0Y2QvcDJoL3JzMzdFT2VaVlh0TDBtNzlZQjBlc1dDcnVPQzdYRnhZcFZxOU9zNnBGTEtjd1pwRElsVGlyeFpVVFFBczZxemttMDZwOThnN0JBZStkRHE2ZHNvNDk5aVlINlRLWC8xWTdEemt2Z3RkaXpqa1hQZHNEdFFDdjlVdyt3cDlVN0RiR0tvZ1BlTWEzTWQrcHZlejdXMzVFaUV1YSsrdGd5L0JCakZGRnkzbDNXRnBPOUtXZ3o3enBtN0FlS0p0OFQxMWRsZUNmZVhra1VBS0lBZjVxb0liYXBzWld3cGJrTkZoSGF4MnhJUEVEZ2ZnMWF6Vlk4MFpjRnVjdEw3VGxMbk1RLzBsVVRiaVN3MW5INjlNRzZ6TzBiOWY2QlFkZ0FtRDA2eUs1Nm1EY1lCWlVDQXdFQUFhT0NBVGd3Z2dFME1BNEdBMVVkRHdFQi93UUVBd0lCaGpBUEJnTlZIUk1CQWY4RUJUQURBUUgvTUIwR0ExVWREZ1FXQkJUa3J5c21jUm9yU0NlRkwxSm1MTy93aVJOeFBqQWZCZ05WSFNNRUdEQVdnQlJnZTJZYVJRMlh5b2xRTDMwRXpUU28vL3o5U3pCZ0JnZ3JCZ0VGQlFjQkFRUlVNRkl3SlFZSUt3WUJCUVVITUFHR0dXaDBkSEE2THk5dlkzTndMbkJyYVM1bmIyOW5MMmR6Y2pFd0tRWUlLd1lCQlFVSE1BS0dIV2gwZEhBNkx5OXdhMmt1WjI5dlp5OW5jM0l4TDJkemNqRXVZM0owTURJR0ExVWRId1FyTUNrd0o2QWxvQ09HSVdoMGRIQTZMeTlqY213dWNHdHBMbWR2YjJjdlozTnlNUzluYzNJeExtTnliREE3QmdOVkhTQUVOREF5TUFnR0JtZUJEQUVDQVRBSUJnWm5nUXdCQWdJd0RRWUxLd1lCQkFIV2VRSUZBd0l3RFFZTEt3WUJCQUhXZVFJRkF3TXdEUVlKS29aSWh2Y05BUUVMQlFBRGdnRUJBRFNrSHJFb285QzBkaGVtTVhvaDZkRlNQc2piZEJaQmlMZzlOUjN0NVArVDRWeGZxN3ZxZk0vYjVBM1JpMWZ5Sm05YnZoZEdhSlEzYjJ0NnlNQVlOL29sVWF6c2FMK3l5RW45V3ByS0FTT3NoSUFyQW95WmwrdEphb3gxMThmZXNzbVhuMWhJVnc0MW9lUWExdjF2ZzRGdjc0elBsNi9BaFNydzlVNXBDWkV0NFdpNHdTdHo2ZFRaL0NMQU54OExaaDFKN1FKVmoyZmhNdGZUSnI5dzR6MzBaMjA5Zk9VMGlPTXkrcWR1Qm1wdnZZdVI3aFpMNkR1cHN6Zm53MFNrZnRoczE4ZEc5WktiNTlVaHZtYVNHWlJWYk5RcHNnM0JabHZpZDBsSUtPMmQxeG96Y2xPemdqWFBZb3ZKSkl1bHR6a011MzRxUWI5U3oveWlscmJDZ2o4PSJdfQ.eyJub25jZSI6IlBvSEJNR1FXVTZMTHZuQ21tQUlqUkt4dTJ4ND0iLCJ0aW1lc3RhbXBNcyI6MTYzNzc1MTY1NTE2OSwiYXBrUGFja2FnZU5hbWUiOiJjb20uZmFzdGJhbmtpbmcuZGVidWciLCJhcGtEaWdlc3RTaGEyNTYiOiJsRHF1bDJxejdyd2owRDFJSzBkcTZwTnNaUmR0QW9BbUNNOVh5MGg2bkNjPSIsImN0c1Byb2ZpbGVNYXRjaCI6dHJ1ZSwiYXBrQ2VydGlmaWNhdGVEaWdlc3RTaGEyNTYiOlsiR3k3N1doNFRkR0ZXd3NoaS9VVXdDdUJIL0NBZ2V4VFFLdmJzbW5pWHFpTT0iXSwiYmFzaWNJbnRlZ3JpdHkiOnRydWUsImV2YWx1YXRpb25UeXBlIjoiQkFTSUMsSEFSRFdBUkVfQkFDS0VEIn0.ShOvWqQ_5i-T1ixx59sbk0-6LMo8oKiC5PfZCt9dVJrnfeap8JMQ9x8v19-Yh-M07y54BjQPXFGU-Y602uFc_V7TKHonDqjaEOsx6VfRwiQeZmtaO-Hhmlr2g-xRHFoDOnXy2wHYGfDkMbir50EraIyny3xfs-guIDMwg5qAzQaN999KRsrbHXX-a6wwoQ0qyUSVKGN57T_qOcXaq9X5bI1B3nD1m5Inu7TW0xrCb0sfUn8GDimAtnXELKf048S4iaXBObbgtiNyVQtTEfqHA8WdfhANIZWcV4XQDHbv69wcvrmUTDeZJienIfkmesfYnFDngW2NfR9A9m_Q5sorig");
			headers.put("x-login-nonce", "B6B667EB514890789F56F9B78BFA509AB41B673B");
			headers.put("x-login-timestamp","1636960116339");
			headers.put("X-Client-App", "android");
			headers.put("X-Client-Version", "1.1.5");
			headers.put("X-Client-OS-Type", "android");
			headers.put("X-Client-OS-Version", "10");
			
			ValidatableResponse response = RestAssured.given().baseUri(url).contentType(ContentType.JSON).headers(headers)
					.body(Myrequestbody.toJSONString()).when().post().then();
			System.out.println("Request Url -->" + url);
			System.out.println("Request :" + Myrequestbody);
			String Resp = response.extract().body().asString();
			System.out.println("Response Body= " + Resp);
			return response;
		}
		public static ValidatableResponse onload() {
			Random rand = new Random();
			HashMap<String, String> req_body = new HashMap<>();
			String url="https://user-gateway.test.ideopay.in/api/v1/onload/data";
			ValidatableResponse response = RestAssured.given().baseUri(url).contentType(ContentType.ANY)
				.when().get().then();
			System.out.println("Request Url -->" + url);
			String Resp = response.extract().body().asString();
			System.out.println("Response Body= " + Resp);
			return response;
		}
		//========================================================KYC Bypass through API End===================================================================//
}
