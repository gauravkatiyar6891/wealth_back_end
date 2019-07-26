package com.moptra.go4wealth.prs.navapi;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.moptra.go4wealth.prs.model.NavCalculateResponse;

@Component
public class CurrentWorthCalculatorUtil  implements Runnable{

	private NavService navService;
	
	static SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
	
	String errorResponse = "No data found against your query.";
	
	ArrayList<Double> unitsLists = new ArrayList<Double>();
	double monthlyAmounts = 0.0;
	
	public CurrentWorthCalculatorUtil(){
	}
	
	public CurrentWorthCalculatorUtil(NavService navService) {
		this.navService=navService;
	}

	public void run() {
		String date = "";
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("GMT"));
		cal.add(Calendar.MONTH,0);
		double nav = 0.0;
		try {
			date = getDate(cal);
			//String navInputData = navService.getNavData(amfiCode, date);
			//String navInputData = navService.getNavData("113544", "30 Nov 2017");
			String navInputData = "83.77";
			nav=Double.parseDouble(navInputData);
		}catch (ParseException e) {
			e.printStackTrace();
		}
		double unit = monthlyAmounts / nav;
		unitsLists.add(unit);
	}
	
	public  NavCalculateResponse calculateSipData(double monthlyAmmount, int numberOfYears, String amfiCode) {
		//ArrayList<Double> unitsList = new ArrayList<Double>();
		double currentValue = 0.0;
		int numberOfMonths = numberOfYears * 12;
		//double nav = 0.0;
		//double currentNav = 0.0;
		double currentNav = 83.77;
		monthlyAmounts = monthlyAmmount;
		for(int i = 0; i < numberOfMonths; i++) {
			Thread threadObj = new Thread(new CurrentWorthCalculatorUtil());
			threadObj.start();
		}
		System.out.println("unitsList === " + unitsLists.toString());
		double totalUnits = unitsLists.stream().mapToDouble(Double::doubleValue).sum();
		totalUnits = Math.round(totalUnits * 100D) / 100D;
		currentValue = totalUnits * currentNav;
		double rate = 0.0;
		try {
			rate = calculateRate(numberOfMonths,monthlyAmmount , 0, -currentValue, 0, 0.1);
			rate = rate * 100;
			rate*=rate*12;
			checkValue(rate);
		} catch (EvaluationException e) {
			e.printStackTrace();
		}
		DecimalFormat df = new DecimalFormat("##.##");
		String newCurrentValue = df.format((Math.round(currentValue * 100.0) / 100.0));
		currentValue= Double.parseDouble(newCurrentValue);
		
		String newRate = df.format((Math.round(rate * 100.0) / 100.0));
		rate= Double.parseDouble(newRate);
		
		NavCalculateResponse navCalculateResponse= new NavCalculateResponse();
		navCalculateResponse.setCurrentNav(currentNav);
		navCalculateResponse.setInterest(rate);
		navCalculateResponse.setWorthAmount(currentValue);
		navCalculateResponse.setMessage("Success");
		return navCalculateResponse;
	}

	public  NavCalculateResponse calculateLumpsumData(double monthlyAmmount, int numberOfYears, String amfiCode) {
		double currentUnits = 0.0;
		double currentValue = 0.0;
		int numberOfMonths = numberOfYears * 12;
		double currentNav = 0.0;
		double pastNav = 0.0;
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("GMT"));
		try {
			System.out.println("current date: " + getDate(cal));
			//String navInputData = navService.getNavData(amfiCode, getDate(cal));
			String navInputData = /*navService.getNavData("113544", "30 Nov 2017");*/"87.15";
			if(navInputData.equals(errorResponse)){
				NavCalculateResponse navCalculateResponse= new NavCalculateResponse();
				navCalculateResponse.setCurrentNav(0d);
				navCalculateResponse.setInterest(0d);
				navCalculateResponse.setWorthAmount(0d);
				navCalculateResponse.setMessage("Nav Failure");
				return navCalculateResponse;
			}
			currentNav=Double.parseDouble(navInputData);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cal.add(Calendar.YEAR, -numberOfYears);
		try {
			System.out.println("past year Date: " + getDate(cal));
			//String pastnavInputData = navService.getNavData(amfiCode, getDate(cal));
			String pastnavInputData = "42.43";
			if(pastnavInputData.equals(errorResponse)){
				NavCalculateResponse navCalculateResponse= new NavCalculateResponse();
				navCalculateResponse.setCurrentNav(0d);
				navCalculateResponse.setInterest(0d);
				navCalculateResponse.setWorthAmount(0d);
				navCalculateResponse.setMessage("Nav Failure");
				return navCalculateResponse;
			}
			pastNav=Double.parseDouble(pastnavInputData);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		currentUnits = monthlyAmmount / pastNav;
		currentUnits = Math.round(currentUnits * 100D) / 100D;
		currentValue = currentUnits * currentNav;
		System.out.println("currentUnits === " + currentUnits);
		System.out.println("currentValue === " + currentValue);
		double rate = 0.0;
		try {
			rate = calculateRate(numberOfMonths, 0, monthlyAmmount, -currentValue, 0, 0.1);
			rate = rate * 100;
			rate*=12;
			checkValue(rate);
		} catch (EvaluationException e) {
			e.printStackTrace();
		}
		DecimalFormat df = new DecimalFormat("##.##");
		String newCurrentValue = df.format((Math.round(currentValue * 100.0) / 100.0));
		currentValue= Double.parseDouble(newCurrentValue);
		
		String newRate = df.format((Math.round(rate * 100.0) / 100.0));
		rate = Double.parseDouble(newRate);
		NavCalculateResponse navCalculateResponse= new NavCalculateResponse();
		navCalculateResponse.setCurrentNav(currentNav);
		navCalculateResponse.setInterest(rate);
		navCalculateResponse.setWorthAmount(currentValue);
		navCalculateResponse.setMessage("Success");
		return navCalculateResponse;
	}

	public  double calculateRate(double nper, double pmt, double pv, double fv, double type, double guess) {
		// FROM MS
		// http://office.microsoft.com/en-us/excel-help/rate-HP005209232.aspx
		int FINANCIAL_MAX_ITERATIONS = 20;// Bet accuracy with 128
		double FINANCIAL_PRECISION = 0.0000001;// 1.0e-8

		double y, y0, y1, x0, x1 = 0, f = 0, i = 0;
		double rate = guess;
		if (Math.abs(rate) < FINANCIAL_PRECISION) {
			y = pv * (1 + nper * rate) + pmt * (1 + rate * type) * nper + fv;
		} else {
			f = Math.exp(nper * Math.log(1 + rate));
			y = pv * f + pmt * (1 / rate + type) * (f - 1) + fv;
		}
		y0 = pv + pmt * nper + fv;
		y1 = pv * f + pmt * (1 / rate + type) * (f - 1) + fv;

		// find root by Newton secant method
		i = x0 = 0.0;
		x1 = rate;
		while ((Math.abs(y0 - y1) > FINANCIAL_PRECISION) && (i < FINANCIAL_MAX_ITERATIONS)) {
			rate = (y1 * x0 - y0 * x1) / (y1 - y0);
			x0 = x1;
			x1 = rate;

			if (Math.abs(rate) < FINANCIAL_PRECISION) {
				y = pv * (1 + nper * rate) + pmt * (1 + rate * type) * nper + fv;
			} else {
				f = Math.exp(nper * Math.log(1 + rate));
				y = pv * f + pmt * (1 / rate + type) * (f - 1) + fv;
			}

			y0 = y1;
			y1 = y;
			++i;
		}
		return rate;
	}

	public  final void checkValue(double result) throws EvaluationException {
		if (Double.isNaN(result) || Double.isInfinite(result)) {
			throw new EvaluationException(ErrorEval.NUM_ERROR);
		}
	}

	public  String getDate(Calendar cal) throws ParseException {
		String returndate = "" + cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/"
				+ cal.get(Calendar.YEAR);
		Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(returndate);
		String returndateFormate = dateFormat.format(date1);

		return returndateFormate;
	}
	
	
	public NavCalculateResponse calculateSipDataOriginal(double monthlyAmmount, int numberOfYears, String amfiCode) {
		ArrayList<Double> unitsList = new ArrayList<Double>();
		double currentValue = 0.0;
		int numberOfMonths = numberOfYears * 12;
		double nav = 0.0;
		double currentNav = 0.0;
		for(int i = 0; i < numberOfMonths; i++) {
			String date = "";
			Calendar cal = Calendar.getInstance();
			cal.setTimeZone(TimeZone.getTimeZone("GMT"));
			cal.add(Calendar.MONTH, -i);
			try {
				date = getDate(cal);
				//String navInputData = navService.getNavData(amfiCode, date);
				String navInputData = /*navService.getNavData("113544", "30 Nov 2017");*/"83.77";
				if(navInputData.equals(errorResponse)){
					NavCalculateResponse navCalculateResponse= new NavCalculateResponse();
					navCalculateResponse.setCurrentNav(0d);
					navCalculateResponse.setInterest(0d);
					navCalculateResponse.setWorthAmount(0d);
					navCalculateResponse.setMessage("Nav Failure");
					return navCalculateResponse;			
				}
				nav=Double.parseDouble(navInputData);
			}catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (i == 0)
				currentNav = nav;
			double unit = monthlyAmmount / nav;
			unitsList.add(unit);
		}
		System.out.println("unitsList === " + unitsList.toString());
		double totalUnits = unitsList.stream().mapToDouble(Double::doubleValue).sum();
		totalUnits = Math.round(totalUnits * 100D) / 100D;
		currentValue = totalUnits * currentNav;
		double rate = 0.0;
		try {
			rate = calculateRate(numberOfMonths,monthlyAmmount , 0, -currentValue, 0, 0.1);
			rate = rate * 100;
			rate*=rate*12;
			checkValue(rate);
		} catch (EvaluationException e) {
			e.printStackTrace();
		}
		DecimalFormat df = new DecimalFormat("##.##");
		String newCurrentValue = df.format((Math.round(currentValue * 100.0) / 100.0));
		currentValue= Double.parseDouble(newCurrentValue);
		
		String newRate = df.format((Math.round(rate * 100.0) / 100.0));
		rate= Double.parseDouble(newRate);
		
		NavCalculateResponse navCalculateResponse= new NavCalculateResponse();
		navCalculateResponse.setCurrentNav(currentNav);
		navCalculateResponse.setInterest(rate);
		navCalculateResponse.setWorthAmount(currentValue);
		navCalculateResponse.setMessage("Success");
		return navCalculateResponse;
	}
	


}
