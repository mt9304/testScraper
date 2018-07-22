package test_jsoup.test_jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebScraper {
	//Remember hash table is synchronized and hash map is not. 
	//Add /quarter to the income, balance, and cash urls to get quarterly data, otherwise it is annual by default. 
	String tickerSymbol;
	String mainUrl;
	String incomeUrl; //Financials. 
	String balanceUrl;
	String cashUrl;
	
	int scrapeDelay = 500; //Delay in ms between each get action. 
	
	Document document;

	public WebScraper(String tickerSymbol) throws IOException {
		this.tickerSymbol = tickerSymbol;
		mainUrl = "https://www.marketwatch.com/investing/stock/" + tickerSymbol;
		incomeUrl = mainUrl + "/financials";
		balanceUrl = mainUrl + "/balance-sheet";
		cashUrl = mainUrl + "/cash-flow";
		
		//document = Jsoup.connect(mainUrl).get();
		//System.out.println(mainUrl);
	}
	
	//Gets current price at the main page. 
	public String getCurrentPrice() throws IOException {
		document = Jsoup.connect(mainUrl).get();
		Elements stockPriceElement = document.select("h3.intraday__price > bg-quote");
		String elementValue = stockPriceElement.text();
		return elementValue;
	}
	
	//The period can be Years or Quarters. Years would be "2013", Quarters would be "30-Sep-2016"
	public String getRevenuePeriodHeader(Document document, int index) {
		Element yearNode = document.getElementsByClass("crDataTable").get(0).select("th[scope]").get(index);
		String revenueYear = yearNode.text();
		return revenueYear;
	}
	
	//Gets the revenue values by index, 0 would be oldest period (2013 for years) and 4 would be latest period (2017 for years) at the current year of 2018. 
	public String getRevenuePeriodValue(Document document, int index) {
		Element revenueNode = document.getElementsByClass("partialSum").get(0).select("td.valueCell").get(index);
		String revenueValue = revenueNode.text();
		return revenueValue;
	}
	
	/** This section is for getting ND12 values 
	 * @throws InterruptedException **/
	//Returns a hash map of data like so: { 2015=1.2b, 2016="2.4b", 2017=2.2b }.
	//1. Revenue
	public Map<String, String> getRevenueByYears() throws IOException, InterruptedException {
		//https://www.marketwatch.com/investing/stock/${ticker}/financials
		document = Jsoup.connect(incomeUrl).get();
		Map<String, String> revenueByYears = new LinkedHashMap<String, String>();

		for (int i = 0; i < 5; i++) {
			String yearValue = getRevenuePeriodHeader(document, i);
			String revenueValue = getRevenuePeriodValue(document, i);
			if (!yearValue.isEmpty()) {
				revenueByYears.put(yearValue, revenueValue);
			}
			Thread.sleep(scrapeDelay);
		}
		System.out.println(revenueByYears);
		return revenueByYears;
	}
	
	public Map<String, String> getRevenueByQuarters() throws IOException, InterruptedException {
		//https://www.marketwatch.com/investing/stock/${ticker}/financials
		document = Jsoup.connect(incomeUrl+"/income/quarter").get();
		Map<String, String> revenueByQuarters = new LinkedHashMap<String, String>();
		
		for (int i = 0; i < 5; i++) {
			String quarterValue = getRevenuePeriodHeader(document, i);
			String revenueValue = getRevenuePeriodValue(document, i);
			
			//Converts the quarter end date from "30-Sep-2016" to "2016-Q3"
			if (!quarterValue.isEmpty()) {
				String month = quarterValue.split("-")[1];
				String year = quarterValue.split("-")[2];
				StringBuilder parsedQuarterValueBuilder = new StringBuilder();
				parsedQuarterValueBuilder.append(year+"-Q");
				
				switch (month) {
					case "Mar": parsedQuarterValueBuilder.append("1");
								break;
					case "Jun": parsedQuarterValueBuilder.append("2");
								break;
					case "Sep": parsedQuarterValueBuilder.append("3");
								break;
					case "Dec": parsedQuarterValueBuilder.append("4");
								break;
					default: 	parsedQuarterValueBuilder.setLength(0);
								parsedQuarterValueBuilder.append(quarterValue);
								//Log unexpected month. 
								break;
				}
				String parsedQuarterValue = parsedQuarterValueBuilder.toString();
				revenueByQuarters.put(parsedQuarterValue, revenueValue);
			}
			Thread.sleep(scrapeDelay);
		}
		System.out.println(revenueByQuarters);
		return revenueByQuarters;
	}
}
