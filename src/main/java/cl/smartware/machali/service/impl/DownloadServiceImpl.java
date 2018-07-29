package cl.smartware.machali.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import cl.smartware.machali.service.DownloadService;

@Service
public class DownloadServiceImpl implements DownloadService 
{
	@Override
	public File download(String url) 
	{
		//grab login form page first
		Response loginPageResponse = null;
				
        try 
        {
			loginPageResponse = 
			        Jsoup.connect("https://app.machali.cl/manage")
			        .referrer("https://app.machali.cl/")
			        .userAgent("Mozilla/5.0")
			        .timeout(10 * 1000)
			        .followRedirects(true)
			        .execute();
		} 
        catch (IOException e) 
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        System.out.println("Fetched login page");
        
        //get the cookies from the response, which we will post to the action URL
        Map<String, String> mapLoginPageCookies = loginPageResponse.cookies();
        
        //lets make data map containing all the parameters and its values found in the form
        Map<String, String> mapParams = new HashMap<String, String>();
        mapParams.put("identification", "true");
        mapParams.put("login", "soporte@dnet.cl");
        mapParams.put("password", "P0o9i8u7");
        
        //URL found in form's action attribute
        String strActionURL = "https://app.machali.cl/manage";
        
        try 
        {
			Response responsePostLogin = Jsoup.connect(strActionURL) 
			        //referrer will be the login page's URL
			        .referrer("https://app.machali.cl/manage")
			        //user agent
			        .userAgent("Mozilla/5.0")
			        //connect and read time out
			        .timeout(10 * 1000)
			        //post parameters
			        .data(mapParams)
			        //cookies received from login page
			        .cookies(mapLoginPageCookies)
			        //many websites redirects the user after login, so follow them
			        .followRedirects(true)
			        .execute();
		} 
        catch (IOException e) 
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return null;
	}

}
