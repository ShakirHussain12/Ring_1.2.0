package com.android.RingPayPages;

import org.openqa.selenium.By;

public class UserRegistrationPage {

	// User Details Header
	public static By objUserDetailsHeader = By.xpath("//*[@text='User Details']");

	// First Name text field
	public static By objFirstName = By.xpath("//*[@text='First Name']");

	// Last Name text field
	public static By objLastName = By.xpath("//*[@text='Last Name']");

	// Email Address text Field
	public static By objUserEmail = By.xpath("//*[@text='Email Address']]");
	
	//Mother's Name
	public static By objMothersName = By.xpath("(//*[@text='Last Name']/parent::android.view.ViewGroup/parent::android.view.ViewGroup/parent::android.view.ViewGroup/following-sibling::android.view.ViewGroup/descendant::android.widget.EditText)[1]");

	// DOB
	public static By objUserDOB = By.xpath("//*[@resource-id='uddobbtn']");

	// ok button
	public static By objOK = By.xpath("//*[@text='OK']");

	// DOB - date
	public static By objDatePickerMonth = By.xpath("(//*[@class='android.widget.NumberPicker'])[1]");

	// DOB - month
	public static By objDatePickerDate = By.xpath("(//*[@class='android.widget.NumberPicker'])[2]");
	
	// DOB - year
	public static By objDatePickerYear = By.xpath("(//*[@class='android.widget.NumberPicker'])[3]");
	
	//Gender
	public static By objGenderText = By.xpath("//*[@text='Gender']");

	// Gender select
	public static By objGenderSelect = By.xpath(
			"(//*[@class='android.view.ViewGroup' and ./parent::*[@class='android.widget.ScrollView']]/*[@class='android.view.ViewGroup' and ./*[@class='android.widget.ImageView'] and ./*[@class='android.widget.TextView']])[2]");

	// Male gender
	public static By objMale = By.xpath("//*[@text='Male']");

	// Female gender
	public static By objFemale = By.xpath("//*[@text='Female']");

	// Register
	public static By objRegister = By.xpath("//*[@class='android.view.ViewGroup' and ./*[@text='Register']]");

	// First name error
	public static By objFirstNameError = By.xpath("//*[@text='Please enter valid first name']");

	// Last name error
	public static By objLastNameError = By.xpath("//*[@text='Please enter valid last name']");

	// DOB error
	public static By objDOBError = By.xpath("//*[@text='Please select Date of Birth']");

	// Email error
	public static By objEmailError = By.xpath("//*[@text='Please enter valid email id']");

	// Gender error
	public static By objGender = By.xpath("//*[@text='Please Select Gender.']");

	// Toast message
	public static By objToast = By.xpath("//android.widget.Toast");

	// None of the above
	public static By objNoneOfAbove = By.xpath("//*[@text='NONE OF THE ABOVE']");

	// Internet error
	public static By objInternetError = By.xpath("//*[@text=' Check your connection & try again ']");

	// Okay got it button
	public static By objGotItBtn = By.xpath("//*[@text='Okay Got it']");
	
	// Okay got it button
	public static By objGotIt= By.xpath("//*[@text='Ok Got It']");

	// Your application can't process
	public static By objProcessError = By.xpath("//*[@text='Your application cannot be processed\nright now.']");

	// Offer page
	public static By objOfferHeader = By.xpath("//*[@text='Offer']");

	// Male option
	public static By objMaleOption = By.xpath("//*[@text='Male']");

	// Gender Cancel Button
	public static By objGenderCancelBtn = By.xpath("//*[@text='CANCEL']");

	// ================================================================================

	public static By objSetPin = By.xpath("//*[@text='Set Pin']");
	public static By objEnterPin = By.xpath("//*[@class='android.widget.EditText']");
	public static By objReEnterPin = By.xpath("(//*[@class='android.widget.EditText'])[2]");
	public static By objSubmit = By.xpath("//*[@text='Submit']");
	public static By objHide = By.xpath("//*[@text='*']");
	public static By objErrorMessage = By.xpath("//*[@text='Please enter numbers only']");
	public static By objSorryErrorMessage = By.xpath("//*[@text='Sorry! Pin does not match, please enter again']");

	public static By objHide(int nNumber) {
		return By.xpath("//*[@text='*'][" + nNumber + "]");
	}

	public static By objTransactionPinSet = By.xpath("//*[@text='Transaction Security Pin Set Successfully']");
	public static By objAllowLocationPopup = By.xpath("//*[@text='Location Permission Required']");
	public static By objAllowLocationBtn = By.xpath("//*[@text='Allow']");
	public static By objOkBtn = By.xpath("//*[@resource-id='android:id/button1']");
	public static By objNotNowLocationBtn = By.xpath("//*[@text='Not Now']");

	
	public static By objPaymentDoneMsg = By.xpath("//*[@text='Payment Done!']");
	public static By objSuccessMsg = By.xpath("(//*[@text='Payment Done!']/following-sibling::android.widget.TextView)[2]");
	public static By objGoTOHomePageBtn = By.xpath("//*[@text='Go to Homepage']");
	public static By objNoBtn = By.xpath("//*[@text='No']");
	
	
	//===================================API DOB=======================//
			
			//DOB - month
			public static By objDatePickerMonthSH = By.xpath("(//*[@resource-id='android:id/numberpicker_input'])[2]");
					
			//DOB - date
			public static By objDatePickerDateSH = By.xpath("(//*[@class='android.widget.NumberPicker'])[1]");
//=======================================API DOB End=============================//
			
			public static By objGenderBtn = By.xpath("(//*[@class='android.widget.RadioButton'])[1]");			

			public static By objAcceptOffer = By.xpath("//*[@text='Accept Offer']");

			public static By objNoneOfTheAbove = By.xpath("//*[@text='NONE OF THE ABOVE']");
			
			public static By objProceed = By.xpath("//*[@class='android.view.ViewGroup' and ./*[@text='Proceed']]");

			public static By objTermsAndCondition = By.xpath("//*[@class='android.widget.CheckBox']");
			
			public static By objSomethingWrong = By.xpath("//*[@text='Something Went Wrong!']");
			
			public static By objEnterPin(int n) {
				return By.xpath("(//*[@text='Enter Pin']/following-sibling::android.view.ViewGroup/descendant::android.widget.TextView)["+n+"]");
			}

}



















