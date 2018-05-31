package com.generic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GenericFunctions {
	 public static String generateRandomNumber(Integer iContainerLength, String sInitials)
	 {
		String sMin = "1";			// * Variable used to store a string of length 'n'
		String sMax = "9";			// * Variable used to store a string of length 'n'
		Long lMin;					// * Variable used to store the minimum value 
		Long lMax;					// * Variable used to store the maxxximum value
		String sContainerNumber;	// * Variable used to store final Credit card number
		Long lRandomNumber;			// * Variable used to store the random generate value
//		String sInitials;			// * Variable used to store the initial value as string
		Integer iInitialLength;		// * Variable used to store the initial value length
		Integer iRandomLength; 		// * Variable used to store the length of random number generation
		try
		{
//			sInitials = iInitials.toString();
			iInitialLength = sInitials.length();
			iRandomLength = iContainerLength - iInitialLength;
				
			for (int iCount=2; iCount <= iRandomLength; iCount++)
			{
				sMin = sMin + "0";
				sMax = sMax + "9";
			}
			lMin = Long.parseLong(sMin);
			lMax = Long.parseLong(sMax);
			lRandomNumber = lMin + (long)(Math.random() * ((lMax - lMin) + 1));
			sContainerNumber = sInitials  + lRandomNumber ;
			}
			catch (Exception e)
			{
				sContainerNumber = e.getMessage();
			}
					
			return sContainerNumber;
	 }
	 
	 public static String generateDateString()
	 {
		 DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	    	Date date = new Date();
//	    	System.out.println(dateFormat.format(date));
	    	String sString = "At" +  dateFormat.format(date);
	    	
	    	return sString;
	 }
}
