package com.moptra.go4wealth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.moptra.go4wealth.prs.common.util.GoForWealthPRSUtil;

public class Test122 {

	public static void main(String...sf) throws ParseException{
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		String prevDate = GoForWealthPRSUtil.getPreviousDate(new Date());
		Date date = dateFormat1.parse(prevDate);
		String sipRegnDate = dateFormat.format(date);
	}
}
