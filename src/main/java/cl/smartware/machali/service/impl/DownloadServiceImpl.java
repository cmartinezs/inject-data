package cl.smartware.machali.service.impl;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import cl.smartware.machali.service.DownloadService;

@Service
public class DownloadServiceImpl implements DownloadService {

	@Override
	public File download(String url) {
		//Login a la web
		Connection.Response loginForm = null;
		
		try 
		{
			loginForm = Jsoup.connect("https://app.machali.cl/manage/cms/form/")
			.timeout(2000)
			.method(Connection.Method.GET)
			.userAgent("http://scrapingauthority.com")
			.execute();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Map<String, String> loginCookies = loginForm.cookies();
		
		Connection.Response login = null;
		
		try 
		{
			login = Jsoup.connect("https://app.machali.cl/manage/cms/form/")
			.userAgent("http://scrapingauthority.com")
			.data("soporte@dnet.cl", "soporte@dnet.cl")
			.data("P0o9i8u7","P0o9i8u7")
			.cookies(loginCookies)
			.method(Connection.Method.POST)
			.execute();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		loginCookies = login.cookies();
		
//Aca no logro entender como le paso el login a la Url
		
	    //Descarga
		URL urlDownload = null;
		
		try 
		{
			urlDownload = new URL("https://app.machali.cl/manage/cms/form/21766351/allReply?csv=1");
		}
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		}
		
		URLConnection urlCon = null;
		
		try 
		{
			urlCon = urlDownload.openConnection();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		// acceso al contenido web
		InputStream is = null;
		try 
		{
			is = urlCon.getInputStream();
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Fichero en el que queremos guardar el contenido
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("./files/");
			// buffer para ir leyendo.
			byte [] array = new byte[1000];

			// Primera lectura y bucle hasta el final
			int leido = is.read(array);
			while (leido > 0) {
			fos.write(array,0,leido);
			leido=is.read(array);
			}	
			// Cierre de conexion y fichero.
			is.close();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return null;
	}

}
