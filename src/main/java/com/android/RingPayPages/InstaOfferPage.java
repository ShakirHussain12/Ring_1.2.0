package com.android.RingPayPages;

import org.openqa.selenium.By;

import com.business.RingPay.RingPayBusinessLogic;

public class InstaOfferPage {

	public static By objOfferPageHeader=By.xpath("//*[@text='Offer']");
	public static By objdisbursedAmount=By.xpath("//*[@text='Disbursal Amount']");
	public static By objDisbursedAmountValue=By.xpath("((//*[android.view.ViewGroup/descendant::android.view.ViewGroup])[20]/child::android.view.ViewGroup/descendant::android.widget.TextView)[2]");
	public static By objEmiAmount=By.xpath("//*[@text='EMI Amount']");
	public static By objEmiAmountValue=By.xpath("((//*[android.view.ViewGroup/descendant::android.view.ViewGroup])[20]/child::android.view.ViewGroup/descendant::android.widget.TextView)[5]");
	public static By objTenure=By.xpath("((//*[android.view.ViewGroup/descendant::android.view.ViewGroup])[20]/child::android.view.ViewGroup/descendant::android.widget.TextView)[6]");

	
	public static By instaPopupBannerCrossBtn = By.xpath("//*[@text='Insta Loan']/parent::android.view.ViewGroup/preceding-sibling::android.view.ViewGroup/(descendant::android.view.ViewGroup)[1]");
    public static By objOkayGotIt = By.xpath("//*[@text='Okay, Got It!']");
    public static By objApplyBtn = By.xpath("//*[contains(@text,'Apply')]");
	public static By objFatherName=By.xpath("//*[@text='Father Name']/preceding-sibling::android.widget.EditText");
	public static By objProceedBtn=By.xpath("//*[@text='Proceed']");
	public static By objselectAddress=By.xpath("//*[@text='Select Address']/following-sibling::android.widget.ImageView");
	public static By objAddress=By.xpath("(//*[android.view.ViewGroup]/child::android.view.ViewGroup/child::android.widget.TextView)[3]");
	public static By objContinueBtn=By.xpath("//*[@text='Continue']");
	public static By objInstallment=By.xpath("//*[@text='Installment']");
	
	public static By objTotalInterest=By.xpath("//*[@text='Total Interest']");
	public static By objTotalInterestRate=By.xpath("//*[@text='Total Interest']/following-sibling::android.widget.TextView");
	
	public static By ObjAPR=By.xpath("//*[@text='Annualised Percentage Rate']");
	public static By ObjAprRate=By.xpath("//*[@text='Annualised Percentage Rate']/following-sibling::android.widget.TextView");
	
	public static By objFeeStructure=By.xpath("//*[@text='Fee Structure']");
	
	public static By objAddBankDetailsBtn=By.xpath("//*[@text='Add Bank Details' or @text='Add New Bank Account']");
	public static By objExistingBankAccSelect=By.xpath("//*[@text='Select Existing Bank Account ']/preceding-sibling::android.widget.ImageView");
	
//	public static By objExistingAccountNameSel=By.xpath("//*[android.view.ViewGroup]/descendant::*[@text='Narayan']");
	
	public static By objExistingAccountNameSel=By.xpath("//*[android.view.ViewGroup]/descendant::*[@text='"+RingPayBusinessLogic.firstName+"']");
	
	
	public static By objExsistingAccPage=By.xpath("//*[@text='Existing Bank Account']");
	public static By objAddNewAbnkAccBtn=By.xpath("//*[@text='Add New Bank Account']");
	public static By objConditionLinks=By.xpath("//*[android.widget.CheckBox]/child::android.widget.TextView");
	public static By objCheckBox=By.xpath("//*[android.widget.TextView]/preceding-sibling::android.widget.CheckBox");
	
	public static By objBankName=By.xpath("(//*[@text='Change Bank']/preceding-sibling::android.widget.TextView)[1]");
	public static By objAcceptOfferBtn=By.xpath("//*[@text='Accept Offer']");
	
	public static By objReEnterBankDetailsBtn=By.xpath("//*[@text='Re-Enter Bank Details']");
	
	public static By objBottomSheetPopup=By.xpath("//*[@text='IMPORTANT']");
	
	public static By objBottomSheetCrossBtn=By.xpath("(//*[@text='IMPORTANT']/ancestor::android.view.ViewGroup/descendant::android.view.ViewGroup)[5]");
	
	public static By objConfirmOfferBtn=By.xpath("//*[@text='Confirm Offer']");
	
	public static By objChangeBank=By.xpath("//*[@text='Change Bank']");
	
	public static By objLoader=By.xpath("//*[@text='All good things take time.']");
	
	public static By objDisbursementSuccessful=By.xpath("//*[@text='Insta Loan Disbursement Successful']");
	
	public static By objLoanAmount=By.xpath("(//*[android.view.ViewGroup]/child::android.widget.TextView)[2]");
	
	public static By objHomeButton=By.xpath("//*[@text='Home']");
	
	public static By objTermsAndCondition = By.xpath("//*[@text='//*[@text='Borrower Agreement']']");
	
	public static By objBackBtn=By.xpath("(//*[android.widget.TextView]/parent::android.view.ViewGroup/descendant::android.view.ViewGroup)[3]");
	
	public static By objOpenWithTab=By.xpath("//*[@text='Open with']");
	
	public static By objViewDetails=By.xpath("//*[@text='View Details']");
	
	public static By objFullPaymentBtn=By.xpath("//*[@text='Pay Full Amount']/preceding-sibling::android.widget.RadioButton/child::android.view.ViewGroup");
	
	public static By objPayNowBtn=By.xpath("//*[@text='Pay Now']");
	
	//Repayment journey
	
	public static By objNetBankingOrDebitCard=By.xpath("//*[@text='Net Banking & Debit Card']");
	
	public static By objPaymentTypes=By.xpath("//*[@text='Cards, UPI & More']/parent::android.view.View/child::android.widget.ListView/child::android.view.View");
	
	//public static By objCardTypePayment=By.xpath("//*[@text='Card Visa, MasterCard, RuPay, and Maestro']");
	
	public static By objCardTypePayment=By.xpath("(//*[@text='Cards, UPI & More']/parent::android.view.View/child::android.widget.ListView/child::android.view.View)[1]");
	
	public static By objCardTypePayCrossBtn=By.xpath("(//*[android.view.View]/child::android.widget.Button)[1]");
	
	public static By objAddNewCard=By.xpath("//*[@text='Add New Card']");
	
	public static By objCardNumberField=By.xpath("//*[@text='Card Number']/preceding-sibling::android.widget.EditText");
	
	public static By objExpiryDateField=By.xpath("//*[@text='Expiry']");
	
	public static By objCardHolderName=By.xpath("//*[@resource-id='card_name']");
	
	public static By objCvvField=By.xpath("//*[@text='CVV']");
	
	public static By objPaynwBtn=By.xpath("//*[@text='Pay Now");
	
	public static By objPaywithoutSavingCardBtn=By.xpath("//*[@text='Pay without Saving Card']");
	
	public static By objSuccessBtn=By.xpath("//*[@text='Success']");
	
	public static By objPaymentSuccessfulMsg=By.xpath("//*[@text='Your payment was successful']");
	
	public static By objGotoHomePageBtn=By.xpath("//*[@text='Go to Homepage']");
	
	public static By objReapplyInstaLoanBanner=By.xpath("//*[@text='ReApply for Insta Loan!']");
	
	public static By objApplyNowBtn=By.xpath("//*[@text='Apply Now']");

	
	
	
	
}
