package cl.smartware.machali.utils;

public class CSVItemUtils {
	
	public static String removeSurroundQuote(String value)
	{
		return value.substring(1).substring(0, value.length() - 2);
	}

}
