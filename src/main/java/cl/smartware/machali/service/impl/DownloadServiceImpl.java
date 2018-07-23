package cl.smartware.machali.service.impl;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.util.Map;
import cl.smartware.machali.service.DownloadService;

@Service
public class DownloadServiceImpl implements DownloadService {

	@Override
	public File download(String url) {
		//Login a la web
		Connection.Response loginForm = Jsoup.connect("https://app.machali.cl/manage/cms/form/")
	    .timeout(2000)
		.method(Connection.Method.GET)
		.userAgent("http://scrapingauthority.com")
		.execute();
		Map<String, String> loginCookies = loginForm.cookies();
		Connection.Response login = Jsoup.connect("https://app.machali.cl/manage/cms/form/")
		.userAgent("http://scrapingauthority.com")
	    .data("soporte@dnet.cl", "soporte@dnet.cl")
		.data("P0o9i8u7","P0o9i8u7")
		.cookies(loginCookies)
		.method(Connection.Method.POST)
		.execute();
		loginCookies = login.cookies();
		
//Aca no logro entender como le paso el login a la Url
		
	    //Descarga
		URL url = new URL("https://app.machali.cl/manage/cms/form/21766351/allReply");
		URLConnection urlCon = url.openConnection();
		// acceso al contenido web
		InputStream is = urlCon.getInputStream();

		// Fichero en el que queremos guardar el contenido
		FileOutputStream fos = new FileOutputStream("./files/");

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
		return null;
	}

}
