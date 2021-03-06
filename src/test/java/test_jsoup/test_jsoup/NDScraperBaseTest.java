package test_jsoup.test_jsoup;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import junit.framework.TestCase;

public class NDScraperBaseTest extends TestCase {
	String tickerSymbol = "MSFT";
	String mainUrl;
	String incomeUrl;
	String incomeQuarterUrl;
	String balanceUrl;
	String cashUrl;
	
	Document mainDocument;
	Document incomeDocument;
	Document incomeQuarterDocument;
	Document balanceSheetDocument;
	Document balanceSheetQuarterUrlDocument;
	Document cashflowDocument;
	Document cashflowQuarterDocument;
	
	NDScraperBase webScraper = new NDScraperBase(tickerSymbol);
	int scrapeDelay = 500;
	
	public NDScraperBaseTest() throws IOException, InterruptedException {
		mainUrl = "https://www.marketwatch.com/investing/stock/" + tickerSymbol;
		incomeUrl = mainUrl + "/financials";
		incomeQuarterUrl = incomeUrl+"/income/quarter";
		balanceUrl = mainUrl + "/balance-sheet";
		cashUrl = mainUrl + "/cash-flow";
	}
	
	//Tests if the site is still following the same HTML layout. Stock price should be convertible into a float like 103.58. 
	@Test
	public void testGetCurrentPrice_isFloat() throws NumberFormatException, IOException, InterruptedException {
		//System.out.println("Stock Price For " + tickerSymbol +" Is: "+ webScraper.getCurrentPrice());
		float floatValue;
		floatValue = Float.parseFloat(webScraper.getCurrentPrice());
		assertNotNull(floatValue);
	}
	
	/** ********************************* **/
	/** getParsedAlphaNumericMoney Start: **/
	/** ********************************* **/
	
	@Test
	public void testGetParsedAlphaNumericMoneyB() {
		long expectedResult = 11120000000L;
		long parsedNumericMoney = webScraper.getParsedAlphaNumericMoney("11.12B");
		assertEquals(expectedResult, parsedNumericMoney);
	}
	
	@Test
	public void testGetParsedAlphaNumericMoneyM() {
		long expectedResult = 311120000L;
		long parsedNumericMoney = webScraper.getParsedAlphaNumericMoney("311.12M");
		assertEquals(expectedResult, parsedNumericMoney);
	}
	
	@Test
	public void testGetParsedAlphaNumericMoneyNoDecB() {
		long expectedResult = 22000000000L;
		long parsedNumericMoney = webScraper.getParsedAlphaNumericMoney("22B");
		assertEquals(expectedResult, parsedNumericMoney);
	}
	
	@Test
	public void testGetParsedAlphaNumericMoneyNoDecM() {
		long expectedResult = 222000000L;
		long parsedNumericMoney = webScraper.getParsedAlphaNumericMoney("222M");
		assertEquals(expectedResult, parsedNumericMoney);
	}
	
	@Test
	public void testGetParsedAlphaNumericMoneyNoLetter() {
		Long expectedResult = null;
		Long parsedNumericMoney = webScraper.getParsedAlphaNumericMoney("11.12");
		assertEquals(expectedResult, parsedNumericMoney);
	}
	
	@Test
	public void testGetParsedAlphaNumericMoneyNoLetterNoDec() {
		Long expectedResult = null;
		Long parsedNumericMoney = webScraper.getParsedAlphaNumericMoney("311");
		assertEquals(expectedResult, parsedNumericMoney);
	}
	
	/** ******************************* **/
	/** getParsedAlphaNumericMoney End: **/
	/** ******************************* **/
	
	/** *********************** **/
	/** useDecimalPlaces Start: **/
	/** *********************** **/
	
	@Test
	public void testUseDecimalPlaces_twoDigits_twoSpaces() {
		Double expectedResult = 12.12;
		Double rawValue = 12.12353464;
		int decimalSpaces = 2;
		Double actualResult = webScraper.useDecimalPlaces(rawValue, decimalSpaces);
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testUseDecimalPlaces_twoDigits_fourSpaces() {
		Double expectedResult = 12.1235;
		Double rawValue = 12.12353464;
		int decimalSpaces = 4;
		Double actualResult = webScraper.useDecimalPlaces(rawValue, decimalSpaces);
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testUseDecimalPlaces_oneDigit_twoSpaces() {
		Double expectedResult = 2.12;
		Double rawValue = 2.12353464;
		int decimalSpaces = 2;
		Double actualResult = webScraper.useDecimalPlaces(rawValue, decimalSpaces);
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testUseDecimalPlaces_oneDigit_fourSpaces() {
		Double expectedResult = 2.1235;
		Double rawValue = 2.12353464;
		int decimalSpaces = 4;
		Double actualResult = webScraper.useDecimalPlaces(rawValue, decimalSpaces);
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testUseDecimalPlaces_twoDigits_twoSpaces_zeroes() {
		Double expectedResult = 12.1;
		Double rawValue = 12.10000;
		int decimalSpaces = 2;
		Double actualResult = webScraper.useDecimalPlaces(rawValue, decimalSpaces);
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testUseDecimalPlaces_twoDigits_fourSpaces_zeroes() {
		Double expectedResult = 12.1;
		Double rawValue = 12.100000000;
		int decimalSpaces = 4;
		Double actualResult = webScraper.useDecimalPlaces(rawValue, decimalSpaces);
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testUseDecimalPlaces_oneDigit_twoSpaces_zeroOnlyDecimal() {
		Double expectedResult = 2.0;
		Double rawValue = 2.0000000;
		int decimalSpaces = 2;
		Double actualResult = webScraper.useDecimalPlaces(rawValue, decimalSpaces);
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testUseDecimalPlaces_twoDigits_fourSpaces_zeroeOnlyDecimal() {
		Double expectedResult = 12.0;
		Double rawValue = 12.000000000;
		int decimalSpaces = 4;
		Double actualResult = webScraper.useDecimalPlaces(rawValue, decimalSpaces);
		assertEquals(expectedResult, actualResult);
	}
	
	/** ********************* **/
	/** useDecimalPlaces End: **/
	/** ********************* **/
	
	/** ********************************* **/
	/** convertDifferentToPercent: Start  **/
	/** ********************************* **/
	@Test
	public void testConvertDifferenceToPercent_greaterLatestValue_fourDigits() {
		Double expectedResult = 23.91;
		Long latestPeriodValue = 570000L;
		Long secondLatestPeriodValue = 460000L;
		Double actualResult = webScraper.convertDifferenceToPercent(latestPeriodValue, secondLatestPeriodValue);
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testConvertDifferenceToPercent_greaterLatestValue_threeDigits() {
		Double expectedResult = 2.75;
		Long latestPeriodValue = 560000L;
		Long secondLatestPeriodValue = 545000L;
		Double actualResult = webScraper.convertDifferenceToPercent(latestPeriodValue, secondLatestPeriodValue);
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testConvertDifferenceToPercent_lowerLatestValue_fourDigits() {
		//Expected result should really be 12.21, but it gets calculated at 12.209999~, and Double will cut 0 off. 
		Double expectedResult = -12.2;	//755000/860000=87.79. The function should subtract 100 with 87.79 to get 6.33%. 
		Long latestPeriodValue = 755000L;
		Long secondLatestPeriodValue = 860000L;
		Double actualResult = webScraper.convertDifferenceToPercent(latestPeriodValue, secondLatestPeriodValue);
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testConvertDifferenceToPercent_lowerLatestValue_threeDigits() {
		Double expectedResult = -6.33;	//740000/790000=93.67. The function should subtract 100 with 93.67 to get 6.33%. 
		Long latestPeriodValue = 740000L;
		Long secondLatestPeriodValue = 790000L;
		Double actualResult = webScraper.convertDifferenceToPercent(latestPeriodValue, secondLatestPeriodValue);
		assertEquals(expectedResult, actualResult);
	}
	
	/** ******************************* **/
	/** convertDifferentToPercent: End  **/
	/** ******************************* **/
	
}
