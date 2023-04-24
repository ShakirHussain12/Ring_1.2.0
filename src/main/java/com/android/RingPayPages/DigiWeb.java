package com.android.RingPayPages;

import org.openqa.selenium.By;

public class DigiWeb {


	//username
	public static By userName = By.xpath("(//input[@type=\"text\"])[1]");
	
	//log in button
	public static By loginBtn = By.xpath("(//button[@type=\"submit\"])[1]");
	
	//password 
	public static By password = By.xpath("//input[@type=\"password\"]");
	
	//OTP
	public static By otp = By.xpath("//input[@placeholder=\"OTP\"]");
	
	//login near OTP button
	public static By loginBtn1 = By.xpath("(//button[@class=\"btn btn-default btn-sm\"])[3]");
	
	//user ref no field
	public static By userRefNo = By.id("user_reference_number");
	
	//digi reliability select
	public static By digiSelect = By.xpath("(//*[@id=\"is_digiscore_reliable\"])[1]");
	
	//v1 salary
	public static By salaryV1 = By.id("estimated_salary_v1");
	
	//v1 income
	public static By incomeV1 = By.id("estimated_income_v1");
	
	//v2 income
	public static By incomeV2 = By.id("estimated_income_v2");
	
	//social score
	public static By socialScore = By.id("social_score");
	
	//time stamp
	public static By timeStamp = By.id("app_last_data_ts");
	
	//customer name match select
	public static By custNameMatch = By.xpath("(//*[@id=\"is_digiscore_reliable\"])[3]");
	
	//duplicate imei select
	public static By dupNoSelect = By.xpath("(//*[@id=\"is_digiscore_reliable\"])[4]");
	
	//CC 3m
	public static By ccWithdraw = By.id("cc_cash_withdrawal_3m");
	
	//Loan overdues last 1m
	public static By loanOverDue_1m = By.id("loan_overdues_1m");
	
	//update digiscore button
	public static By updateDigiScoreBtn = By.xpath("//*[@class=\"btn btn-sm btn-default\" and @type=\"submit\"]");


}
