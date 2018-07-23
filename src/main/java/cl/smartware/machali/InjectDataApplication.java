package cl.smartware.machali;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import cl.smartware.machali.repository.model.Submissions;
import cl.smartware.machali.repository.model.SubmissionsValue;
import cl.smartware.machali.service.SubmissionsService;
import cl.smartware.machali.service.SubmissionsValueService;

@SpringBootApplication
public class InjectDataApplication
{

	// @Autowired
	// private DownloadService downloadService;
	
	@Autowired
	private SubmissionsService submissionsService;
	
	@Autowired
	private SubmissionsValueService submissionsValueService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InjectDataApplication.class);

	public static void main(String[] args)
	{
		SpringApplication.run(InjectDataApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx)
	{
		return args -> {

			final Function<String, CSVItem> mapToItem = (line) -> {
				String[] p = line.split(";");// a CSV has comma separated lines
				CSVItem item = new CSVItem();
				item.setDate(p[0]);
				item.setMotivo(p[1]);
				item.setLugar(p[2]);
				item.setArchivo(p[3]);
				item.setUsuario(p[4]);
				item.setEmail(p[5]);
				item.setTelefonoContacto(p[6]);
				item.setFecha(p[7]);
				item.setHora(p[8]);
				return item;
			};
			
			// File file = downloadService.download("");
			
			LOGGER.info("Lectura de archivo");
			
			List<CSVItem> items = new ArrayList<>();
			File inputF = new File("./files/export_allReply_iphone_22656189.csv");
			InputStream inputFS = new FileInputStream(inputF);
			BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
			// skip the header of the csv
			items = br.lines().skip(1).map(mapToItem).collect(Collectors.toList());
			br.close();
			
			items.forEach((item) -> System.out.println(item));
			
			LOGGER.info("Guardado de archivo en base de datos");
			
			items.forEach(
				(item) -> {
					Submissions submission = submissionsService.buildFromCSVItem(item);
					submission = submissionsService.save(submission);
					
					LOGGER.info(MessageFormat.format("Submission id {0}", submission.getId()));
					
					List<SubmissionsValue> values = submissionsValueService.buildFromCSVItem(item, submission);
					submissionsValueService.saveAll(values);
				}
			);

		};
	}

}
