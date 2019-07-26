package com.moptra.go4wealth.admin.common.util;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.moptra.go4wealth.admin.common.constant.GoForWealthAdminConstants;

public class GoForWealthAdminStringUtil {
	/**
	 * Check for null or blank for a string. Returns
	 * <blockquote>false</blockquote> if String consists of value.
	 * <blockquote>true</blockquote> otherwise.
	 * 
	 * @param dataStr
	 * @return boolean
	 */
	public static boolean isNullOrEmpty(String dataStr) {
		boolean isEmpty = false;
		if (dataStr == null
				|| (dataStr != null && (dataStr.length() == 0 || dataStr.trim()
						.length() == 0)))
			isEmpty = true;
		return isEmpty;
	}
	
	/**
	 * Check if obj is null or not
	 * 
	 * @param obj
	 * @return boolean
	 */
	public static boolean isNull(Object obj) {
		boolean isNull = false;
		if (obj == null)
			isNull = true;
		return isNull;
	}
	
	/**
	 * Check if obj is empty or not
	 * 
	 * @param obj
	 * @return boolean
	 */
	public static boolean isEmpty(Object obj) {
		boolean isEmpty = false;
		if (obj == GoForWealthAdminConstants.BLANK)
			isEmpty = true;
		return isEmpty;
	}

	/**
	 * Converts the object to String. If obj is null, blank string is returned
	 * 
	 * @param obj
	 * @return {@link String}
	 */
	public static String obj2Str(Object obj) {
		String str = "";
		if (obj != null) {
			str = obj.toString();
		}
		return str;
	}
	/**
	 * convert all elements of list into string. Example : 1,3 elements of list
	 * would be converted into '1','3' in String. This String is used in
	 * <blockquote>in</blockquote> query in database
	 * 
	 * @param list
	 *            {@link List}
	 * @return {@link String}
	 */
	public static String parseListForQuery(List<String> list) {
		String parsedString = "";
		String str = null;
		if (list == null || (list != null && list.size() == 0)) {
			return null;
		}
		Iterator<String> itr = list.iterator();
		while (itr.hasNext()) {
			str = itr.next();
			parsedString = parsedString + "'" + str + "'" + ",";
		}
		if (parsedString.indexOf(",") != -1) {
			parsedString = parsedString.substring(0, parsedString.length() - 1);
		}
		return parsedString;
	}
	
	public static double generateRandomNumber(){
		Random randomno = new Random();
		return randomno.nextDouble();
	}
	
	public static boolean compareListData(List currentList, List availableList)
	{
		if ((currentList != null && currentList.size() > 0) && (availableList != null && availableList.size() > 0) && currentList.size()==availableList.size())
		{
			for (Object current : currentList) {
				if (!availableList.contains(current)) {
					return false;
				}
			}
			return true;
		}
		else if(GoForWealthAdminUtil.isEmptyCollection(currentList) && GoForWealthAdminUtil.isEmptyCollection(availableList)){
			return true;
		}
		return false;
	}
}
