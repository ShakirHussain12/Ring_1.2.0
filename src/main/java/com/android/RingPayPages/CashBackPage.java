package com.android.RingPayPages;

import org.openqa.selenium.By;

public class CashBackPage {
	
	public static By addEngagementOfferBtn = By.xpath("//span[text()=\"Add Engagement Offer\"]");
	
	public static By offerCode = By.id("offer_code");
	
	public static By cashBackValue = By.id("policy_value");
	
	public static By txnCountLowerLimit = By.id("txn_count_lower_limit");
	
	public static By txnCountUpperLimit = By.id("txn_count_upper_limit");
	
	public static By minTxnAmt = By.id("txn_amount");
	
	public static By validityEndDate = By.id("validity_end_date");
	
	public static By editConfigBtn = By.xpath("(//*[text()=\" EDIT\"])[1]");
	
	public static By activeRadioBtn = By.xpath("//input[@name=\"is_active\" and @value=\"1\"]");
	
	public static By addEngagementSubmitButton = By.xpath("//span[text()=\"Submit\" and @class='MuiButton-label']");
	
	public static By addWhiteListBtn = By.xpath("//span[text()=\"Add whiteList User\"]");
	
	public static By uploadUser = By.xpath("//input[@type=\"file\"]");
	
	public static By cashBackHeader = By.xpath("//*[@text='Yipeee!']");
	
	public static By cashBackDescription = By.xpath("//*[contains(@text,'unlocked')]");
	
	public static By cashBackRecentTxn = By.xpath("//*[@text='Ring Cashback']");
	
	public static By rewardsTab = By.xpath("//*[@text='Rewards' and (./preceding-sibling::* | ./following-sibling::*)[@class='android.view.ViewGroup']]");
	
	public static By cashBackTxn = By.xpath("//*[@text='Cashback Earned ']");
			
	public static By viewAllTab = By.xpath("//*[@text='View All']");
	
	public static By engagementCashBack = By.xpath("(//*[@text='Ring Cashback'])[1]");
	
	public static By normalCashBack = By.xpath("(//*[@text='Ring Cashback'])[2]");

}
