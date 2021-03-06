package test_jsoup.test_jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ND0Calculator {
				String tickerSymbol;
				ND11Insider webScraper;
				
				public ND0Calculator(String tickerSymbol) throws IOException, InterruptedException{
					this.tickerSymbol = tickerSymbol;
					webScraper = new ND11Insider(tickerSymbol);
				}
				
				/** **************** **/
				/** Start: 1 Revenue **/
				/** **************** **/
				
				//Uses function that divides latest period by second latest period to get the percent increase/decrease. 
				Double revenuePercentIncrease(Period period) throws IOException, InterruptedException {
					Map<String, String> revenueByPeriod = null;
					Double percentIncrease = null;
					
					switch (period) { 
						case YEAR: 		revenueByPeriod = webScraper.getRevenueByYears();	
										break;
						case QUARTER: 	revenueByPeriod = webScraper.getRevenueByQuarters();
										break;
						default: 		System.out.println("Invalid period entered: " + period);
										break;
					}

					Set<String> keys = revenueByPeriod.keySet();
					ArrayList<String> allPeriods = new ArrayList<>();
					
					if (keys.size() < 2) {
						System.out.println("Not Enough " + period + " Data. ");
						return null;
					} else {
						for (String k : keys) {
							allPeriods.add(k);
						}
						
						String unParsedLatestPeriodRevenue = revenueByPeriod.get(allPeriods.get(allPeriods.size() - 1));
						String unParsedSecondLatestPeriodRevenue = revenueByPeriod.get(allPeriods.get(allPeriods.size() - 2));
						//Need to parse the alphanumeric values like 999M and 1.01B
						Long latestPeriodRevenue = webScraper.getParsedAlphaNumericMoney(unParsedLatestPeriodRevenue);	
						Long secondLatestPeriodRevenue = webScraper.getParsedAlphaNumericMoney(unParsedSecondLatestPeriodRevenue);
						percentIncrease = webScraper.convertDifferenceToPercent(latestPeriodRevenue, secondLatestPeriodRevenue);
					}
					return percentIncrease;
				}
				
				//For YEAR, compares only the latest complete fiscal year and the year before that. 
				//For QUARTER, compares only latest complete quarter and the quarter before that. 
				public Boolean hasIncreasingRevenue(Period period) throws IOException, InterruptedException {
					Map<String, String> revenueByPeriod = null;
					
					switch (period) { 
						case YEAR: 		revenueByPeriod = webScraper.getRevenueByYears();
										break;
						case QUARTER: 	revenueByPeriod = webScraper.getRevenueByQuarters();
										break;
						default: 		System.out.println("Invalid period entered: " + period);
										break;
					}

					Set<String> keys = revenueByPeriod.keySet();
					ArrayList<String> allPeriods = new ArrayList<>();
					
					if (keys.size() < 2) {
						System.out.println("Not Enough " + period + " Data. ");
						return null;
					} else {
						for (String k : keys) {
							allPeriods.add(k);
						}
						
						String unParsedLatestPeriodRevenue = revenueByPeriod.get(allPeriods.get(allPeriods.size() - 1));
						String unParsedSecondLatestPeriodRevenue = revenueByPeriod.get(allPeriods.get(allPeriods.size() - 2));
						Long latestPeriodRevenue = webScraper.getParsedAlphaNumericMoney(unParsedLatestPeriodRevenue);
						Long secondLatestPeriodRevenue = webScraper.getParsedAlphaNumericMoney(unParsedSecondLatestPeriodRevenue);
						
						if (latestPeriodRevenue > secondLatestPeriodRevenue) {
							return true;
						} else if (latestPeriodRevenue < secondLatestPeriodRevenue) {
							return false;
						}
					}
					return null;
				}
				
				/** ************** **/
				/** End: 1 Revenue **/
				/** ************** **/
				
				/** ************ **/
				/** Start: 2 EPS **/
				/** ************ **/
				
				//Uses function that divides latest period by second latest period to get the percent increase/decrease. 
				Double epsPercentIncrease(Period period) throws IOException, InterruptedException {
					Map<String, String> epsByPeriod = null;
					Double percentIncrease = null;
					
					switch (period) { 
						case YEAR: 		epsByPeriod = webScraper.getEPSByYears();	
										break;
						case QUARTER: 	epsByPeriod = webScraper.getEPSByQuarters();
										break;
						default: 		System.out.println("Invalid period entered: " + period);
										break;
					}

					Set<String> keys = epsByPeriod.keySet();
					ArrayList<String> allPeriods = new ArrayList<>();
					
					if (keys.size() < 2) {
						System.out.println("Not Enough " + period + " Data. ");
						return null;
					} else {
						for (String k : keys) {
							allPeriods.add(k);
						}
						
						String unParsedLatestPeriodEPS = epsByPeriod.get(allPeriods.get(allPeriods.size() - 1));
						String unParsedSecondLatestPeriodEPS = epsByPeriod.get(allPeriods.get(allPeriods.size() - 2));
						//Need to parse the alphanumeric values like 999M and 1.01B
						Double latestPeriodEPS = Double.parseDouble(unParsedLatestPeriodEPS);	
						Double secondLatestPeriodEPS = Double.parseDouble(unParsedSecondLatestPeriodEPS);
						percentIncrease = webScraper.convertDifferenceToPercent(latestPeriodEPS, secondLatestPeriodEPS);
					}
					return percentIncrease;
				}
				
				//For YEAR, compares only the latest complete fiscal year and the year before that. 
				//For QUARTER, compares only latest complete quarter and the quarter before that. 
				public Boolean hasIncreasingEPS(Period period) throws IOException, InterruptedException {
					Map<String, String> epsByPeriod = null;
					
					switch (period) { 
						case YEAR: 		epsByPeriod = webScraper.getEPSByYears();
										//System.out.println(epsByPeriod);
										break;
						case QUARTER: 	epsByPeriod = webScraper.getEPSByQuarters();
										break;
						default: 		System.out.println("Invalid period entered: " + period);
										break;
					}

					Set<String> keys = epsByPeriod.keySet();
					ArrayList<String> allPeriods = new ArrayList<>();
					
					if (keys.size() < 2) {
						System.out.println("Not Enough " + period + " Data. ");
						return null;
					} else {
						for (String k : keys) {
							allPeriods.add(k);
						}
						
						String unParsedLatestPeriodEPS = epsByPeriod.get(allPeriods.get(allPeriods.size() - 1));
						String unParsedSecondLatestPeriodEPS = epsByPeriod.get(allPeriods.get(allPeriods.size() - 2));
						
						Double latestPeriodEPS = Double.parseDouble(unParsedLatestPeriodEPS);
						Double secondLatestPeriodEPS = Double.parseDouble(unParsedSecondLatestPeriodEPS);
						
						if (latestPeriodEPS > secondLatestPeriodEPS) {
							return true;
						} else if (latestPeriodEPS < secondLatestPeriodEPS) {
							return false;
						}
					}
					return null;
				}
				
				/** ********** **/
				/** End: 2 EPS **/
				/** ********** **/
				
				/** ************ **/
				/** Start: 3 ROE **/
				/** ************ **/
				
				//Uses function that divides latest period by second latest period to get the percent increase/decrease. 
				Double roePercentIncrease(Period period) throws IOException, InterruptedException {
					Map<String, String> roeByPeriod = null;
					Double percentIncrease = null;
					
					switch (period) { 
						case YEAR: 		roeByPeriod = webScraper.getROEByYears();	
										break;
						case QUARTER: 	roeByPeriod = webScraper.getROEByQuarters();
										break;
						default: 		System.out.println("Invalid period entered: " + period);
										break;
					}

					Set<String> keys = roeByPeriod.keySet();
					ArrayList<String> allPeriods = new ArrayList<>();
					
					if (keys.size() < 2) {
						System.out.println("Not Enough " + period + " Data. ");
						return null;
					} else {
						for (String k : keys) {
							allPeriods.add(k);
						}
						
						String unParsedLatestPeriodROE = roeByPeriod.get(allPeriods.get(allPeriods.size() - 1));
						String unParsedSecondLatestPeriodROE = roeByPeriod.get(allPeriods.get(allPeriods.size() - 2));
						//Need to parse the alphanumeric values like 999M and 1.01B
						Double latestPeriodROE = Double.parseDouble(unParsedLatestPeriodROE);	
						Double secondLatestPeriodROE = Double.parseDouble(unParsedSecondLatestPeriodROE);
						percentIncrease = webScraper.convertDifferenceToPercent(latestPeriodROE, secondLatestPeriodROE);
					}
					return percentIncrease;
				}
				
				public Boolean hasIncreasingROE(Period period) throws IOException, InterruptedException {
					Double percentIncrease = roePercentIncrease(period);
					if (percentIncrease > 0) {
						return true;
					} else if (percentIncrease <= 0) {
						return false;
					}
					return null;
				}
				
				/** ********** **/
				/** End: 3 ROE **/
				/** ********** **/
				
				/** ******************************* **/
				/** Start: 4 Analyst Recommendation **/
				/** ******************************* **/
				
				public String getAnalystRecommendation() {
					return webScraper.getAnalystRecommendation();
				}
				
				public Boolean analystRecommendationIsPositive() {
					String analystRecommendation = getAnalystRecommendation();
					if (analystRecommendation.equals("BUY") || analystRecommendation.equals("OVER")) {
						return true;
					} else if (analystRecommendation.equals("SELL") || analystRecommendation.equals("UNDER") || analystRecommendation.equals("HOLD")) {
						return false;
					}
					return null;
				}
				
				/** ***************************** **/
				/** End: 4 Analyst Recommendation **/
				/** ***************************** **/
				
				/** ***************** **/
				/** Start: 9 Industry **/
				/** ***************** **/
				
				public String getIndustry() throws InterruptedException {
					return webScraper.getIndustry();
				}
				
				public String getSector() throws InterruptedException {
					return webScraper.getSector();
				}
				
				/** *************** **/
				/** End: 9 Industry **/
				/** *************** **/
				
				/** ************************** **/
				/** Start: 11 Insider Activity **/
				/** ************************** **/
				
				public Boolean hasMoreInsiderBuysThanSells() {
					int sharesPurchased = webScraper.getSharesPurchasedInLastThreeMonths();
					int sharesSold = webScraper.getSharesSoldInLastThreeMonths();
					if (sharesPurchased > sharesSold) {
						return true;
					} else if (sharesSold > sharesPurchased) {
						return false; 
					}
					return null;
				}
				
				/** ************************ **/
				/** End: 11 Insider Activity **/
				/** ************************ **/
}
