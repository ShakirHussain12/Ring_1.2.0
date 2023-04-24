package com.android.RingPayPages;

import org.openqa.selenium.By;

public class UserSuspendedCases {
	
	public static By objHomepAge = By.xpath("//*[@text='Pay now to use RING Limit']");
	public static By objScanAndPayBtn = By.xpath("(//*[@text='Rewards']/ancestor::android.view.ViewGroup/preceding-sibling::android.view.ViewGroup/descendant::android.view.ViewGroup)[20]");
	public static By objRepayNowToastMsg = By.xpath("//*[@text='Complete your profile to unlock Scan & Pay']");
	public static By objOopsMsg = By.xpath("//*[@text='Oops']");
	public static By objTecnicalIssueMsg = By.xpath("//*[@text='You may have faced technical issues, as we are updating the billing cycle to 30 DAYS']");
	public static By objunlockMsg = By.xpath("//*[@text='You can unlock a higher limit by paying your outstanding now']");
	public static By objpayNowBtn = By.xpath("//*[@text='Pay Now']");
	public static By objpayNowBtnDif=By.xpath("//*[@text='Oops']/following-sibling::android.view.ViewGroup/descendant::android.widget.TextView");
	public static By objEnabledScanAndPayBtn = By.xpath("(//*[@text='Rewards']/ancestor::android.view.ViewGroup/preceding-sibling::android.view.ViewGroup)[3]");
	public static By objmodeOfPaymentMsg = By.xpath("//*[@text='This mode of payment is not available at the moment']");
	public static By objGotItBtn = By.xpath("//*[@text='Ok, Got It']");
	
	//BankTransfer
	public static By objUhOhErrorMsg = By.xpath("//*[@text='Uh-oh! It seems you have unpaid bills.']");
	public static By objKindlyRepayErrMsg = By.xpath("//*[@text='Kindly repay your existing dues to unlock higher limits.']");
//	public static By objOkayBtn=By.xpath("//*[@text='Okay']");
	
	
	
	
	


	
	}

