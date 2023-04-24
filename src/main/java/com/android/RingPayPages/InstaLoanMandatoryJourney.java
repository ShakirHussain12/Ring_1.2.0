package com.android.RingPayPages;

import org.openqa.selenium.By;

public class InstaLoanMandatoryJourney {

//    public static By objFathersNameBasicDetails = By.xpath("//*[@text='Father Name']");
//    public static By objBasicDetailsPage = By.xpath("//*[@text='Basic Details']");
//

    public static By objQRScanner = By.xpath("(//*[@text='UPI']/parent::android.view.ViewGroup/parent::android.view.ViewGroup/parent::android.view.ViewGroup/following-sibling::android.view.ViewGroup/descendant::android.view.ViewGroup)[3]");
    public static By objScanAndPayToastMsg = By.xpath("//android.widget.Toast");
    public static By objQRScannPage = By.xpath("//*[@text='Scan any QR code to pay']");
    public static By objInstaOfferBanner = By.xpath("//*[@text='New Offer Alert!' or @text='Insta Loan']");
    public static By objAmountEnter = By.xpath("//*[@text='₹']/following-sibling::android.widget.EditText");
    public static By objPayNowBtn = By.xpath("(//*[@text='Pay Now'])[1]");
    public static By objEnterSecurityPin = By.xpath("//*[@text='Enter Transaction Security PIN']");
    public static By objPINField = By.xpath("(//*[@text='Enter Transaction Security PIN']/following-sibling::android.widget.TextView)[1]");
    public static By objContinueBtn = By.xpath("//*[@text='Continue']");
    public static By objPaymentDone = By.xpath("//*[@text='Payment Done!']");
    public static By objPayEarly = By.xpath("//*[@text='Pay Early']");
    public static By objUPIPaymentMethod = By.xpath("//*[@text='Add UPI ID']");
    public static By objNetBankingAndDebitCardPaymentMethod = By.xpath("//*[@text='Net Banking & Debit Card']");
    public static By objBankTransferPaymentMethod = By.xpath("//*[@text='Bank Transfer']");
    public static By objPaymentViaUPIPaymentMethod = By.xpath("//*[@text='Payment via UPI']");
    public static By objMoreBottomTab = By.xpath("//*[@text='More']");
    public static By objBankTransfer = By.xpath("//*[@text='Bank Transfer']");
    public static By objAddBankAccount = By.xpath("//*[@text='Add Bank Account']");
    public static By objBBPSFlowElectricity = By.xpath("//*[@text='Electricity']");
    public static By objSelectBiller = By.xpath("//*[@text='Select your biller']");
    public static By objElectricsConsumerNo = By.xpath("//*[@text='Enter your details']");
    public static By objEnterCustomerID = By.xpath("//*[@resource-id='param1']");
    public static By objFetchBill = By.xpath("//*[@resource-id='fetch-bill-button']");
    public static By objPayAmount = By.xpath("//*[@text='Pay Amount']");
    public static By objPayBtn = By.xpath("//*[@text='Pay']");
    public static By objPaymentInProgress = By.xpath("//*[@text='Payment in Progress']");
    public static By objPayWithRingLimit = By.xpath("//*[@text='Pay With Ring Limit']");
    public static By objProcessing = By.xpath("//*[@text='Processing your Payment']");
    public static By objPaymentSuccessfull = By.xpath("//*[@text='Payment successful!']");
    public static By objNoBtn = By.xpath("//*[@text='No']");
    public static By objMakePaymentPage = By.xpath("//*[@text='Make Payment']");
    public static By objMakePaymentAmount = By.xpath("//*[@text='₹']/following-sibling::android.widget.EditText");
    public static By objCircularBtn = By.xpath("//*[@resource-id='circularButtonFull']");
    public static By objExistingBankAccount = By.xpath("//*[@text='Existing Bank Account']");
    public static By objApprovedStatusProceed = By.xpath("//*[@text='Approved']/preceding-sibling::android.view.ViewGroup");
 public static By objEnterSecurityPinforElectricityBill = By.xpath("//*[@text='Enter Security Pin']");
            public static By objPINFieldElectricityBill = By.xpath("//*[@resource-id='cursor']");
 public static By objYesBtn = By.xpath("//*[@text='YES']");
 public static By objCurrentSpending = By.xpath("//*[@resource-id='bannerDetails']/following-sibling::android.widget.TextView");
public static By objFathersNameBasicDetails = By.xpath("//*[@text='Father Name']");
 public static By objBasicDetailsPage = By.xpath("//*[@text='Basic Details']");
 public static By objOnlyFathersNameField = By.xpath("//*[@text='Basic Details']/preceding-sibling::android.view.ViewGroup/parent::android.view.ViewGroup/following-sibling::android.view.ViewGroup/descendant::android.widget.TextView");
             public static By objContinueStepBtn = By.xpath("//*[@text='Continue']");
             public static By objIallowCreditBureauChecksText = By.xpath("//*[contains(@text,'I allow')]");
             public static By objIhearbyConfirmText = By.xpath("//*[contains(@text,'Read')]");
             public static By objCreditBureauChecksHyperlinks = By.xpath("//*[@text='I allow Ring to run credit bureau checks and provide \r\n"
             + " consent for CKYC']");
 public static By objCreditBureauConsentPopup = By.xpath("//*[@text='Credit Bureau Consent']");
 public static By objOkGotItCreditBureaupopup = By.xpath("//*[@text='Ok, Got It!']");
 public static By objIallowCheckBox = By.xpath("//*[contains(@text,'I allow')]/preceding-sibling::android.widget.CheckBox");
 public static By objReadmoreCheckBox = By.xpath("//*[contains(@text,'Read more')]/(preceding-sibling::android.widget.CheckBox)[2]");
 public static By objSelectAddressDropDown = By.xpath("//*[@text='Select Address']");
 public static By objIdeclaretheAboveAddressCheckBox = By.xpath("//*[contains(@text,'I declare')]/preceding-sibling::android.widget.CheckBox");
 public static By objAddCommunicationAddress = By.xpath("//*[@text='Add Communication Address']");
 //First EMI due in 15 days
 public static By objFirstEMIDueIN15Days = By.xpath("//*[@text='First EMI due in']");
 public static By objSecondInstallmentWaiver = By.xpath("//*[@text='2nd installment']");
 public static By objWaiverMsg = By.xpath("//*[@text='2nd installment']/following-sibling::android.widget.TextView[contains(@text,'waiver')]");
}





