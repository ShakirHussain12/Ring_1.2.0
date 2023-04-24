package com.RingPay.TestScripts;

import java.io.IOException;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.utility.Utilities;
import com.android.RingPayPages.RingLoginPage;
import com.extent.ExtentReporter;
import com.propertyfilereader.PropertyFileReader;
import com.utility.Utilities;

public class RingPayTestScripts extends Utilities{

	private com.business.RingPay.RingPayBusinessLogic ringPayBusiness;
	/** Test data from property file. */
	public static PropertyFileReader prop = new PropertyFileReader(System.getProperty("user.dir")+"//properties//testData.properties");
	
	@BeforeTest
	public void Before() throws InterruptedException {
		Utilities.relaunch = true;
		ringPayBusiness = new com.business.RingPay.RingPayBusinessLogic("ring");
	}
	
	
//==========================================================Ring App Flow Start=====================================================================//@Test(priority = 0)
	/*@Test(priority=0)
	public void simBindingFlow() throws Exception {
		ringPayBusiness.simBindingFlow();
	}*/
	
	@Test(priority = 1)
	@Parameters({ "SignUpBtn", "permission" })
	public void User_Playstore_Flow(String SignUpBtn, String permission) throws Exception {
		ringPayBusiness.User_Play_Store_Flow(SignUpBtn, permission);
	}

	/*@Test(priority = 2)
	public void userRegistrationFlow() throws Exception {
		ringPayBusiness.User_Registration_Flow();
	}

	/*@Test(priority = 3)
	public void promocodeFlow() throws Exception {
		ringPayBusiness.promoCodeFlowModule();
	}

	@Test(priority = 4)
	public void offerScreen() throws Exception {
		ringPayBusiness.offerScreenModule();
	}
	
	@Test(priority = 5)
	public void ageCheck() throws Exception {
		ringPayBusiness.ageCriteriaFailedCheck();
	}

	@Test(priority = 6)
	public void kycSkipped() throws Exception {
		ringPayBusiness.kycSkipped();
	}
	@Test(priority = 7)
	public void bannerLogicOnHomepage() throws Exception {
		ringPayBusiness.bannerLogicOnHomePage();
	}

	@Test(priority = 8)
	public void transactionSetPin() throws Exception {
		ringPayBusiness.transactionSetPin();
	}

	@Test(priority = 9)
	public void repaymentmultipleCase() throws Exception {
		ringPayBusiness.repaymentMultipleCases();
	} 

	@Test(priority = 10)
	public void bankTransferFlow() throws Exception {
		ringPayBusiness.BankTransferModule("5", "Akash");
	}

	@Test(priority = 11)
	public void addAddresFlow() throws Exception {
		ringPayBusiness.addAddressFlow();
	}

	@Test(priority = 12)
	public void userScanAndPayFlow() throws Exception {
		ringPayBusiness.userScanAndPayTransactions();
	}*/

	@Test(priority = 13)
	/*public void merchantFlow() throws Exception {
		ringPayBusiness.merchantFlow();
	}

	@Test(priority = 14)
	public void userSuspendTerminateCase() throws Exception {
		ringPayBusiness.scanAndPayuserSuspendedTerminateCase();
	} 

	@Test(priority = 15)
	public void feedBackMechanism() throws Exception {
		ringPayBusiness.feedBackValidationWithGlobalFlag();
		ringPayBusiness.feedBackMechanism();
	}

	@Test(priority = 16)
	public void bbpsModule() throws Exception {
		ringPayBusiness.bbpsModule();
	}*/
//===========================================================Ring App Flow End=====================================================================//
//===========================================================Instaloan Start======================================================================//
	/*@Test(priority = 17)
	 public void ringInstaWhitelist() throws Exception{
	 ringPayBusiness.instaLoanWhitelistLogic(prop.getproperty("OTP"),prop.getproperty("RingAdminEmail"), prop.getproperty("RingAdminPassword"),prop.getproperty("RingAdminOTP"), prop.getproperty("InstaLoanMPIN"), prop.getproperty("InstaLoanMPIN"),prop.getproperty("firstName"), prop.getproperty("lastName"), prop.getproperty("mothersName"),prop.getproperty("gender"), prop.getproperty("delinquentNo"), prop.getproperty("terminatedNo"));
	} 
	@Test(priority = 18)
	public void instaLoanHomeScreenScenario() throws Exception {
		ringPayBusiness.instaLoanHomeScreenScenarios();
		ringPayBusiness.instaLoanOnHold();
		ringPayBusiness.backDatedScenario();
	}
	@Test(priority = 19)
	public void ringInstaLoanOptionalJourney() throws Exception {
		ringPayBusiness.instaLoanOptioanlJourney();
		ringPayBusiness.instaLoanOnHold();
	}
	
	@Test(priority = 20)
	public void InstaLoanMandatory() throws Exception {
		ringPayBusiness.instaLoanMandatoryJourney();
		ringPayBusiness.instaLoanMandatoryAndOptional();
	}
	@Test(priority = 21)
	public void afterClickOnApplyNowForLoanOfferBanner() throws Exception {
		ringPayBusiness.afterClickOnApplyNowForLoanOfferBanner();
	 } 
	@Test(priority = 22)
	public void ringInstaTransactionHistory() throws Exception {
		ringPayBusiness.instaTransactionHistory(prop.getproperty("InstatransactionHistoryNo"), prop.getproperty("OTP"));
	}

	@Test(priority = 23)
	public void instaLoanOfferPage() throws Exception {
		ringPayBusiness.instaLoanOfferPage();
	 } 
	@Test(priority = 24)
	public void instaLoanRepayment() throws Exception {
		ringPayBusiness.instaLoanRepayment();
	 } 
	@Test(priority = 25)
	public void InstaLoanDetailScreen() throws Exception {
		ringPayBusiness.InstaLoanDetailScreen();
	 } 
// ===================================================Instaloan End======================================================================//	
//===========================================Ring Policy Start=====================================//
//================================================Digi Surrogate Related POOL Offer test cases =======================================
		@Test(priority=26)
		public void ringPolicy_TC_1() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_01(prop.getproperty("pool_1_v16"));
		}
		
		@Test(priority=27)
		public void ringPolicy_TC_2() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_02(prop.getproperty("pool_2_v16"));
		}
		
		@Test(priority=28)
		public void ringPolicy_TC_5() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_05(prop.getproperty("pool_2_v16"));
		}
		
		@Test(priority=29)
		public void ringPolicy_TC_6() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_06(prop.getproperty("pool_2_equals"));
		}
		
		@Test(priority=30)
		public void ringPolicy_TC_7() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_07(prop.getproperty("pool_2_v16"));
		}
		
		@Test(priority=31)
		public void ringPolicy_TC_8() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_08(prop.getproperty("pool_3_v16"));
		}
		
		
		//================================================cibil surrogate related POOL Offer test cases =======================================
		@Test(priority = 32)
		public void ringPolicy_TC_3() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_03(prop.getproperty("pool_1_v17"));
		}
		
		@Test(priority = 33)
		public void ringPolicy_TC_4() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_04(prop.getproperty("pool_2_v17"));
		}
		
		@Test(priority = 34)
		public void ringPolicy_TC_9() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_09(prop.getproperty("pool_2_v17"));
		}
		
		@Test(priority = 35)
		public void ringPolicy_TC_10() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_10(prop.getproperty("pool_2_v17_equals"));
		}
		
		@Test(priority = 36)
		public void ringPolicy_TC_11() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_11(prop.getproperty("pool_2_v17_equals"));
		}
		
		@Test(priority = 37)
		public void ringPolicy_TC_12() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_12(prop.getproperty("pool_2_v17_greater"));
		}
		
		@Test(priority = 38)
		public void ringPolicy_TC_28() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_28(prop.getproperty("ltbc1"),prop.getproperty("pool_2_v17_greater"));
		}
		
		@Test(priority = 39)
		public void ringPolicy_TC_29() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_29(prop.getproperty("bc1"),prop.getproperty("pool_2_v17_greater"));
		}
		
		@Test(priority = 40)
		public void ringPolicy_TC_30() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_30(prop.getproperty("bc2"),prop.getproperty("pool_2_v17_greater"));
		}
		
		@Test(priority = 41)
		public void ringPolicy_TC_31() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_31(prop.getproperty("l1"),prop.getproperty("pool_2_v17_greater"));
		}
		
		@Test(priority = 42)
		public void ringPolicy_TC_32() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_32(prop.getproperty("l2"),prop.getproperty("pool_2_v17_greater"));
		}
		
		@Test(priority = 43)
		public void ringPolicy_TC_33() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_33(prop.getproperty("l3"),prop.getproperty("pool_2_v17_greater"));
		}
		
		//================================================age preference related POOL Offer test cases =======================================
		@Test(priority = 44)
		public void ringPolicy_TC_158_digi() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_158_digi(prop.getproperty("age_digi"));
		}
		
		@Test(priority = 45)
		public void ringPolicy_TC_158_L1() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_158_L1(prop.getproperty("age_digi"),prop.getproperty("l1"));
		}
		
		@Test(priority = 46)
		public void ringPolicy_TC_159_BC1() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_159_BC1(prop.getproperty("age_digi"),prop.getproperty("bc1"));
		}
		
		@Test(priority=47)
		public void ringPolicy_TC_160() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_160(prop.getproperty("pool_1_v17"));
		}
		//================================================To verify Limit exceed for CC cash withdrawal (Digi Reliable + CC cash withdrawal >15k) test cases =======================================
		@Test(priority=48)
		public void ringPolicy_TC_62() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_62(prop.getproperty("pool_1_v17"));
		}
		
		@Test(priority=49)
		public void ringPolicy_TC_63() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_63(prop.getproperty("pool_1_v17"));
		}
		@Test(priority=50)
		public void ringPolicy_TC_64() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_64(prop.getproperty("pool_1_v17"));
		}
		
		@Test(priority=51)
		public void ringPolicy_TC_65() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_65(prop.getproperty("pool_1_v17"));
		}
		//================================================To verify Blocking users (mobile_number, pan_hash & IMEI) test cases =======================================
		@Test(priority=52)
		public void ringPolicy_TC_70() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_70(prop.getproperty("MockAPIurl"), "male", prop.getproperty("encrypted"));
		}
		
		@Test(priority=53)
		public void ringPolicy_TC_74() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_74(prop.getproperty("MockAPIurl"), "male", prop.getproperty("encrypted"));
		}
		//================================================To verify user receives offer from KLA preference (is_true_comp = 1) test cases =======================================
		@Test(priority=54)
		public void ringPolicy_TC_170() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_170(prop.getproperty("kla"),prop.getproperty("cc_flag_negative"),prop.getproperty("kla_flag_positive"));
		}
		
		@Test(priority=55)
		public void ringPolicy_TC_171() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_171(prop.getproperty("pool_1_v17"),prop.getproperty("cc_flag_negative"),prop.getproperty("kla_flag_positive"));
		}
		
		//================================================To verify user receives offer from CC preference (unique_cc_count_12m = 1) test cases =======================================
		@Test(priority=56)
		public void ringPolicy_TC_172() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_172(prop.getproperty("kla"),prop.getproperty("cc_flag_positive"),prop.getproperty("kla_flag_negative"));
		}
		
		@Test(priority=57)
		public void ringPolicy_TC_173() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_173(prop.getproperty("pool_1_v17"),prop.getproperty("cc_flag_positive"),prop.getproperty("kla_flag_negative"));
		}
		
		@Test(priority=58)
		public void ringPolicy_TC_174() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_174(prop.getproperty("kla"),prop.getproperty("cc_flag_positive"),prop.getproperty("kla_flag_positive"));
		}
				
		//================================================To verify offer receives from Low_Segmentation (BC1 / BC2 / LTBC1 / THIN) test cases =======================================
		@Test(priority=61)
		public void ringPolicy_TC_131() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_131(prop.getproperty("band_9_20"), prop.getproperty("ltbc1"), prop.getproperty("cc_flag_negative"),prop.getproperty("kla_flag_negative"));
		}
		@Test(priority=62)
		public void ringPolicy_TC_132() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_132(prop.getproperty("band_9_20"), prop.getproperty("ltbc1"), prop.getproperty("cc_flag_negative"),prop.getproperty("kla_flag_negative"));
		}
		
		@Test(priority=63)
		public void ringPolicy_TC_133() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_133(prop.getproperty("band_6_7"), prop.getproperty("ltbc1"), prop.getproperty("cc_flag_negative"),prop.getproperty("kla_flag_negative"));
		}
		
		@Test(priority=64)
		public void ringPolicy_TC_134() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_134(prop.getproperty("band_6_7"), prop.getproperty("ltbc1"), prop.getproperty("cc_flag_negative"),prop.getproperty("kla_flag_negative"));
		}
		
		@Test(priority=65)
		public void ringPolicy_TC_135() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_135(prop.getproperty("band_9_20"), prop.getproperty("l1"), prop.getproperty("cc_flag_negative"),prop.getproperty("kla_flag_negative"));
		}
		
		//================================================To verify No Loan from Matrix, Cibil Response Failed, No Digi Response test cases =======================================
		@Test(priority=66)
		public void ringPolicy_TC_99() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_99(prop.getproperty("l3_band_no_offer"), prop.getproperty("no_offer"));
		}
		
		//================================================To verify GB Rejection / GB Acceptance test cases =======================================
		@Test(priority=67)
		public void ringPolicy_TC_103() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_103(prop.getproperty("bc1"));
		}
		
		@Test(priority=68)
		public void ringPolicy_TC_105() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_105(prop.getproperty("firstBand"), prop.getproperty("l2"));
		}
		@Test(priority=69)
		public void ringPolicy_TC_106() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_106(prop.getproperty("lastBand"), prop.getproperty("bc1"));
		}
		
		@Test(priority=70)
		public void ringPolicy_TC_107() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_107(prop.getproperty("lastBand"));
		}
		
		//================================================To verify user receives transaction fee and cashfee according to grid test cases =======================================
		@Test(priority=71)
		public void ringPolicy_TC_40() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_40(prop.getproperty("pool_1_v17"));
		}
		
		@Test(priority=72)
		public void ringPolicy_TC_41() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_41(prop.getproperty("pool_1_v17"));
		}
		
		@Test(priority=73)
		public void ringPolicy_TC_42() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_42(prop.getproperty("pool_1_v17"));
		}
		
		@Test(priority=74)
		public void ringPolicy_TC_43() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_43(prop.getproperty("pool_1_v17"));
		}
		
		@Test(priority=75)
		public void ringPolicy_TC_44() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_44(prop.getproperty("pool_1_v17"));
		}
		
		//================================================To verify user receives Maximum limit amount for Fresh test cases =======================================
		@Test(priority=76)
		public void ringPolicy_TC_45() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_45(prop.getproperty("pool_1_v17"));
		}
		
		@Test(priority=77)
		public void ringPolicy_TC_46() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_46(prop.getproperty("pool_1_v17"));
		}
		
		//================================================To verify KYC Reverification band status (=>18 as High & Medium, <18 as Low) test cases =======================================
		@Test(priority=78)
		public void ringPolicy_TC_167() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_167(prop.getproperty("ltbc1"),prop.getproperty("band_18_greater"));
		}
		
		@Test(priority=79)
		public void ringPolicy_TC_168() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_168(prop.getproperty("l1"), prop.getproperty("band_18_greater"));
		}
		
		@Test(priority=80)
		public void ringPolicy_TC_169() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_169(prop.getproperty("l3"), prop.getproperty("pool_1_v17"));
		}
		
		//================================================To verify bnpl transaction level checks test cases =======================================
		@Test(priority=81)
		public void ringPolicy_TC_120() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_120(prop.getproperty("payAmount"),null);
		}
		
		@Test(priority=82)
		public void ringPolicy_TC_121() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_121(prop.getproperty("payAmount"));
		}
		
		@Test(priority=83)
		public void ringPolicy_TC_122_123_124() throws Exception{
			ringPayBusiness.coble_Count();
		}
		@Test(priority=84)
		public void ringPolicy_TC_125() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_125();
		}
		
		@Test(priority=85)
		public void ringPolicy_TC_126() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_126();
		}
		
		//================================================To verify 'Terminate user on full repayment if ever delinquent based on segment' test cases =======================================
		@Test(priority=86)
		public void ringPolicy_TC_151() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_151();
		}*/
		
		//================================================To verify Cabal check at the time of Onboarding test cases =======================================
		/*@Test(priority=87)
		public void ringPolicy_TC_141() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_141();
		}
		
		@Test(priority=88)
		public void ringPolicy_TC_142() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_142();
		}
		
		@Test(priority=89)
		public void ringPolicy_TC_143() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_143();
		}
		
		//================================================To verify if user is set as repeat test cases =======================================
		@Test(priority = 90)
		public void ringPolicy_TC_47() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_47(prop.getproperty("stepup_greater_amount"));
		}
		
		@Test(priority = 91)
		public void ringPolicy_TC_48() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_48(prop.getproperty("stepup_lesser_amount"));
		}
		
		//================================================To verify if user gets step up when set as repeat test cases =======================================
		@Test(priority = 92)
		public void ringPolicy_TC_49() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_49(prop.getproperty("stepup_greater_amount"));
		}
		
		//================================================To verify if user gets step down when set as repeat test cases =======================================
		@Test(priority = 93)
		public void ringPolicy_TC_52() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_52(prop.getproperty("stepup_greater_amount"));
		}
		
		//================================================To verify max and min value for repeat  test cases =======================================
		@Test(priority=94)
		public void ringPolicy_TC_53() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_53(prop.getproperty("stepup_greater_amount"));
		}
		
		@Test(priority=95)
		public void ringPolicy_TC_54() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_54(prop.getproperty("stepup_greater_amount"));
		}
		
		@Test(priority=96)
		public void ringPolicy_TC_55() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_55(prop.getproperty("stepup_greater_amount"));
		}
		
		@Test(priority=97)
		public void ringPolicy_TC_56() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_56(prop.getproperty("stepup_greater_amount"));
		}
		
		//================================================To verify 'Cabal linked with Delinquency'  test cases =======================================
		/*@Test(priority=98)
		public void ringPolicy_TC_149() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_149();
		}
		@Test(priority=99)
		public void ringPolicy_TC_150() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_150();
		}*/
		
		//================================================To verify user receives offer from THIN columns from Offer Grid  test cases =======================================
		/*@Test(priority=100)
		public void ringPolicy_TC_144() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_144();
		}
		@Test(priority=101)
		public void ringPolicy_TC_145() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_145();
		}		
		@Test(priority=102)
		public void ringPolicy_TC_146() throws Exception{
			ringPayBusiness.TC_Ring_Customer_Seg_146();
		}*/
	
		//================================================To verify user receives Gamified Cashback Test Cases =======================================//
		/*@Test(priority=103)
		public void ringEngagement_TC_01() throws Exception{
			ringPayBusiness.TC_Ring_Engagememt_01();
		}
	
		@Test(priority=104)
		public void ringEngagement_TC_02() throws Exception{
			ringPayBusiness.TC_Ring_Engagememt_02();
		}
	
		@Test(priority=105)
		public void ringEngagement_TC_03() throws Exception{
			ringPayBusiness.TC_Ring_Engagememt_03();
		}
	
		@Test(priority=106)
		public void ringEngagement_TC_04() throws Exception{
			ringPayBusiness.TC_Ring_Engagememt_04();
		}
		
		@Test(priority=107)
		public void ringEngagement_TC_05() throws Exception{
			ringPayBusiness.TC_Ring_Engagememt_05();
		}
		
		@Test(priority=108)
		public void ringEngagement_TC_06() throws Exception{
			ringPayBusiness.TC_Ring_Engagememt_06();
		}
	
		@Test(priority=109)
		public void ringEngagement_TC_08() throws Exception{
			ringPayBusiness.TC_Ring_Engagememt_08();
		}
	
		@Test(priority=110)
		public void ringEngagement_TC_09() throws Exception{
			ringPayBusiness.TC_Ring_Engagememt_09();
		}
	
		@Test(priority=111)
		public void ringEngagement_TC_10() throws Exception{
			ringPayBusiness.TC_Ring_Engagememt_10();
		}
	
		@Test(priority=112)
		public void ringEngagement_TC_11() throws Exception{
			ringPayBusiness.TC_Ring_Engagememt_11();
		}
	
		@Test(priority=112)
		public void ringEngagement_TC_12() throws Exception{
			ringPayBusiness.TC_Ring_Engagememt_12();
		}
		
		@Test(priority=113)
		public void ringEngagement_TC_13() throws Exception{
			ringPayBusiness.TC_Ring_Engagememt_13();
		}
		
		@Test(priority=114)
		public void ringEngagement_TC_14() throws Exception{
			ringPayBusiness.TC_Ring_Engagememt_14();
		}
	
		@Test(priority=115)
		public void ringEngagement_TC_15() throws Exception{
			ringPayBusiness.TC_Ring_Engagememt_15();
		}
		
		@Test(priority=116)
		public void ringEngagement_TC_16() throws Exception{
			ringPayBusiness.TC_Ring_Engagememt_16();
		}
	
	//================================================To verify User receives offer from NTC (Band 1 to 7, Band 8 to 9) Test Cases =======================================//
	@Test(priority=117)
	public void ringPolicy_TC_162()throws Exception{
		ringPayBusiness.TC_Ring_Customer_Seg_162();
	}
	@Test(priority=118)
	public void ringPolicy_TC_163()throws Exception{
		ringPayBusiness.TC_Ring_Customer_Seg_163();
	}
	
	@Test(priority=119)
	public void ringPolicy_TC_164()throws Exception{
		ringPayBusiness.TC_Ring_Customer_Seg_164();
	}
	
	//================================================To verify Hex 13 checks Test Cases =======================================//
	/*@Test(priority=120)
	public void ringPolicy_TC_108()throws Exception{
		ringPayBusiness.TC_Ring_Customer_Seg_108();
	}
	@Test(priority=121)
	public void ringPolicy_TC_109()throws Exception{
		ringPayBusiness.TC_Ring_Customer_Seg_109();
	}
	
	@Test(priority=122)
	public void ringPolicy_TC_110()throws Exception{
		ringPayBusiness.TC_Ring_Customer_Seg_110();
	}
	
	@Test(priority=123)
	public void ringPolicy_TC_111()throws Exception{
		ringPayBusiness.TC_Ring_Customer_Seg_111();
	}*/
	
//===========================================Ring Policy End=====================================//
		@AfterTest
		public void ringAppQuit() throws Exception {
			 //BrowsertearDown();
			ringPayBusiness.TearDown();
		} 

}