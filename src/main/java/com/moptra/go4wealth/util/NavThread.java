package com.moptra.go4wealth.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class NavThread implements Runnable{

	List<Double> unitsList = new ArrayList<Double>();
	double monthlyAmount = 100.0;
	
	static SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
	
	@Override
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
		double unit = monthlyAmount / nav;
		unitsList.add(unit);
	}

	public  String getDate(Calendar cal) throws ParseException {
		String returndate = "" + cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/"+ cal.get(Calendar.YEAR);
		Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(returndate);
		String returndateFormate = dateFormat.format(date1);
		return returndateFormate;
	}

	public static void main(String[] args){
		for(int i=1; i<=50; i++){
			Thread threadObj = new Thread(new NavThread());
			threadObj.start();
		}
	}


}


/**
 import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class NavThread implements Runnable{

	static List<Double> unitsList = new ArrayList<Double>();
	double monthlyAmount = 100.0;
	
	static SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
	
	@Override
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
		double unit = monthlyAmount / nav;
		System.out.println("Unit List in Double : " + unit);
		unitsList.add(unit);
		System.out.println("Thread End");
	}
	
	public  String getDate(Calendar cal) throws ParseException {
		String returndate = "" + cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/"+ cal.get(Calendar.YEAR);
		Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(returndate);
		String returndateFormate = dateFormat.format(date1);
		return returndateFormate;
	}
	
	public static void main(String[] args){
		System.out.println("Main() start");
		for(int i=0; i<50; i++){
			System.out.println("Thread Start : " + i);
			Thread threadObj = new Thread(new NavThread());
			threadObj.start();
		}
		System.out.println("Unit List Size : " + unitsList.size());
		for(int i=0; i<unitsList.size();i++){
			System.out.println("Units List : " + i + " = " + unitsList.get(i));
		}
		System.out.println("Main() end");
	}


}
 
 
 
**/