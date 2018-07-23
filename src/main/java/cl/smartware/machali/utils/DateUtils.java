package cl.smartware.machali.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtils
{
	public static final String DATE_FORMAT = "dd/MM/yyyy HH:mm";
	
	public static SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
	
	public static java.util.Date toDate(String date, String format) throws ParseException
	{
		return toDate(date, new SimpleDateFormat(format));
	}
	
	public static java.util.Date toDate(String date) throws ParseException
	{
		return toDate(date, sdf);
	}
	
	public static java.util.Date toDate(String date, SimpleDateFormat format) throws ParseException
	{
		return format.parse(date);
	}
	
	public static java.sql.Date utilToSql(java.util.Date date)
	{
		return new java.sql.Date(date.getTime()); 
	}
}
