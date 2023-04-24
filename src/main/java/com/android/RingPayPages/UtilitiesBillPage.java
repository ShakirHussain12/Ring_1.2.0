package com.android.RingPayPages;

import org.openqa.selenium.By;

public class UtilitiesBillPage {
	
	//More button on home page
		public static By objMoreBtn = By.xpath("//*[@class='android.view.ViewGroup' and ./*[@class='android.view.ViewGroup' and ./*[@text='More'] and ./*[@class='android.view.ViewGroup']]]");
		
		//Pay bills header
		public static By objPayBillsHeader = By.xpath("//*[@text='Pay Bills']");
		
		//Electricity button option
		public static By objElectricityBtn = By.xpath("//*[@class='android.view.ViewGroup' and ./*[@text='Electricity'] and ./*[@class='android.view.ViewGroup' and ./*[@class='android.view.ViewGroup']]]");
		
		//Select your biller header
		public static By objSelectBiller = By.xpath("//*[@text='Select your biller']");
		
		//Search biller
		public static By objBillerSearch = By.xpath("//*[@resource-id='biller-name-search']");
		
		//Bescom option
		public static By objBescomBiller = By.xpath("//*[@class='android.view.View' and ./*[@class='android.widget.Image'] and ./*[@text='Bangalore Electricity Supply Co. Ltd (BESCOM)']]");
		
		//Customer id text
		public static By objCustId = By.xpath("//*[@resource-id='param1']");
		
		//Fetch bill button
		public static By objFetchBtn = By.xpath("//*[@text='FETCH BILL']");
		
		//Pay button
		public static By objPayBtn = By.xpath("//*[@text='Pay']");
		
		//Bill Details header
		public static By objBillHeader = By.xpath("//*[@text='Bill Details']");
		
		//Amount paid
		public static By objAmtPaid = By.xpath("(//*[@class='android.view.View' and ./parent::*[@id='bill-details-box']]/*[@class='android.widget.TextView'])[2]");
		
		//Biller
		public static By objBiller = By.xpath("(//*[@class='android.view.View' and ./parent::*[@id='bill-details-box']]/*[@class='android.widget.TextView'])[4]");
		
		//Bill date
		public static By objBillDate = By.xpath("(//*[@class='android.view.View' and ./parent::*[@id='bill-details-box']]/*[@class='android.widget.TextView'])[10]");
		
		//Due date
		public static By objDueDate = By.xpath("(//*[@class='android.view.View' and ./parent::*[@id='bill-details-box']]/*[@class='android.widget.TextView'])[12]");
		
		//Bill number
		public static By objBillNo = By.xpath("(//*[@class='android.view.View' and ./parent::*[@id='bill-details-box']]/*[@class='android.widget.TextView'])[14]");
		
		//pay with ring limit button
		public static By objPayRingBtn = By.xpath("//*[@class='android.widget.Button']");
		
		//Payment in progress
		public static By objPayProgressHeader = By.xpath("//*[@text='Payment in Progress']");
		
		//Transaction success message
		public static By objTransacSuccess = By.xpath("//*[@text='Transaction successful!']");
		
		
		//Utility bill payment transaction
		public static By objBillPayment = By.xpath("(//*[@class='android.view.ViewGroup' and ./parent::*[@class='android.widget.ScrollView' and ./parent::*[./parent::*[./parent::*[./parent::*[@class='android.widget.ScrollView']]]]]]/*/*/*[@class='android.view.ViewGroup' and ./parent::*[@class='android.view.ViewGroup' and (./preceding-sibling::* | ./following-sibling::*)[@class='android.view.ViewGroup']] and (./preceding-sibling::* | ./following-sibling::*)[@text]])[1]");
		
		//Payment Success
		public static By objPaySuccess = By.xpath("//*[@text='Payment successful!']");
		
		//Payment failed
		public static By objPayFailed = By.xpath("//*[@text='Payment failed!']");
		
		//Refund Initiated
		public static By objRefundMsg = By.xpath("//*[@text='Refund initiated!']");
		
		//Refund transaction text
		public static By objRefundTransac = By.xpath("//*[@text='Refund']");
		
		//Paid transaction
		public static By objPaidTransac = By.xpath("//*[@text='Paid']");
		
		//BNPL transaction number
		public static By objBnplTransacNo = By.xpath("(//*[@text='Transaction Number']//following-sibling::*)[2]");
		
		//Overdue bills
		public static By objOverdueMsg = By.xpath("//*[@text='Looks like you have overdue bills!']");
		
		//Okay got it button
		public static By objOkBtn = By.xpath("//*[@class='android.view.ViewGroup' and ./*[@text='Ok, Got It']]");
		
		//Oops message
		public static By objOopsMsg = By.xpath("//*[@text='Oops']");
		
		//Pay No link
		public static By objPayNowBtn = By.xpath("//*[@class='android.view.ViewGroup' and ./*[@text='Pay Now']]");
		
		//Payment not available
		public static By objPayNotAvail = By.xpath("//*[@text='This mode of payment is not available at the moment']");
		
		//Ring limit not sufficient
		public static By objRingInsuffMsg = By.xpath("//*[@text='RING limit not sufficient to complete this transaction.']");
		
		//Admin panel email
		public static By objEmail = By.xpath("//*[@id='email']");
		
		//Continue button
		public static By objContinue = By.xpath("//*[@type='submit']");
		
		//Admin Panel BBPS edit button
		public static By objEditConfBtn = By.xpath("//*[@class=\"MuiButtonBase-root MuiButton-root MuiButton-contained MuiButton-containedPrimary MuiButton-containedSizeSmall MuiButton-sizeSmall\"]");

		//INACTIVE radio button
		public static By objInactiveRadioBtn = By.xpath("//*[@value=\"INACTIVE\"]");
		
		//Submit button 
		public static By objSubmitBtn = By.xpath("(//button[@type=\"submit\"])[2]");
		//pay mount field
		public static By objamountField = By.xpath("//*[@resource-id='amount']");
}
