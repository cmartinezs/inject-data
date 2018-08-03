package cl.smartware.machali;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import cl.smartware.machali.repository.model.Submissions;
import cl.smartware.machali.service.DownloadService;
import cl.smartware.machali.service.SubmissionsService;
import cl.smartware.machali.service.SubmissionsValueService;
import cl.smartware.machali.utils.DateUtils;

@SpringBootApplication
public class InjectDataApplication
{

	@Autowired
	private DownloadService downloadService;
	
	@Autowired
	private SubmissionsService submissionsService;
	
	@Autowired
	private SubmissionsValueService submissionsValueService;
	
	@Value("${app.csv.path}")
	private String csvPath;
	
	@Value("${app.csv.split}")
	private String csvSplit;
	
	@Value("${app.csv.file.last.execution}")
	private String fileLastExecution;
	
	@Value("${app.csv.file.last.execution.split}")
	private String fileLastExecutionSplit;
	
	@Value("${app.csv.next.execution}")
	private long nextExecutionIn;
	
	@Value("${app.csv.number.of.executions}")
	private long numberOfExecutions;
	
	private final long WAIT_TIME = 60000;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InjectDataApplication.class);

	public static void main(String[] args)
	{
		SpringApplication.run(InjectDataApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx)
	{
		return args -> 
		{
			//Eliminacion ejecucion anterior del proceso, si existe
			
			File inputFile = new File(fileLastExecution);
			
			if(inputFile.exists())
			{
				LOGGER.info(MessageFormat.format("Se eliminará la inserción del proceso anterior guardada en el archivo {0}", fileLastExecution));
				
				InputStream inputStream = new FileInputStream(inputFile);
				
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				
				final Function<String, String[]> mapToItem = (line) -> 
				{
					return line.split(fileLastExecutionSplit);
				};
				
				List<String[]> items = bufferedReader
						.lines()
						.map(mapToItem)
						.collect(Collectors.toList());
				
				bufferedReader.close();
				
				String[] submissionsIds = items.get(0);
				
				for(String id: submissionsIds) 
				{
					if(id == null || id.isEmpty()) continue;
					
					Optional<Submissions> optionalSubmissions = submissionsService.findById(Integer.valueOf(id));
					
					if(optionalSubmissions.isPresent())
					{
						Submissions submissions = optionalSubmissions.get();
						submissions.getSubmissionsValues().forEach(submissionsValue -> {
							LOGGER.info(MessageFormat.format("Eliminando SubmissionValue id = {0}", submissionsValue.getId()));
							submissionsValueService.delete(submissionsValue);
						});
						
						submissions.setSubmissionsValues(null);
						
						LOGGER.info(MessageFormat.format("Eliminando submission id = {0}", submissions.getId()));
						submissionsService.delete(submissions);
					}
				}
				
				inputStream.close();
				
				if(inputFile.delete())
				{
					LOGGER.info(MessageFormat.format("Archvio {0} eliminado", inputFile.getName()));
				}
			}
			
			long initTime = 0;
			long lastExecute = 0;
			
			LastExecutionCSV lastExecutionCSV = null;
			
			boolean executeAgain = true;
			
			long executions = 0;

			do 
			{
				initTime = Calendar.getInstance().getTimeInMillis();
				long timeElapsed = initTime - lastExecute;
				
				if(lastExecute > 0 && (timeElapsed <= nextExecutionIn)) 
				{
					LOGGER.info(MessageFormat.format("Esperando para próxima ejecución... {0}ms", nextExecutionIn - timeElapsed));
					
					Thread.sleep(WAIT_TIME);
					
					continue;
				}
				
				LOGGER.info(MessageFormat.format("Iniciando proceso... {0}", DateUtils.toString(initTime)));
				
				ExecutorService executorService = Executors.newSingleThreadExecutor();
				
				if(lastExecutionCSV != null) 
				{
					LOGGER.info("Se eliminará la inserción del proceso anterior...");
					
					DeleteLastCSVInDataBase deleteLastCSVInDataBase = new DeleteLastCSVInDataBase();
					deleteLastCSVInDataBase.setName("Eliminacion de CSV");
					deleteLastCSVInDataBase.setLastExecutionCSV(lastExecutionCSV);
					deleteLastCSVInDataBase.setSubmissionsService(submissionsService);
					deleteLastCSVInDataBase.setSubmissionsValueService(submissionsValueService);
					executorService.execute(deleteLastCSVInDataBase);
				}
				else
				{
					LOGGER.info("Primera ejecución del proceso...");
				}
				
				SaveCSVInDataBase saveCSVinDataBase = new SaveCSVInDataBase();
				saveCSVinDataBase.setName("Guardado de CSV");
				saveCSVinDataBase.setFilePath(csvPath);
				saveCSVinDataBase.setCsvSplit(csvSplit);
				saveCSVinDataBase.setSubmissionsService(submissionsService);
				saveCSVinDataBase.setSubmissionsValueService(submissionsValueService);
				
				executorService.execute(saveCSVinDataBase);
				executorService.shutdown();
				
				int i = 0;
				
				do
				{
					LOGGER.info(MessageFormat.format("Esperando la finalización del proceso... Cuenta en {0}", i++));

					Thread.sleep(5000);
				}
				while(!executorService.isTerminated());
				
				lastExecutionCSV = new LastExecutionCSV();
				lastExecutionCSV.setSubmissions(saveCSVinDataBase.getSubmissions());

				lastExecute = Calendar.getInstance().getTimeInMillis();
				
				LOGGER.info(MessageFormat.format("Ejecución finalizada... {0}", DateUtils.toString(lastExecute)));
				
				if(numberOfExecutions > 0)
				{
					executions++;
					
					executeAgain = numberOfExecutions != executions;
				}
				
				if(executeAgain)
				{
					LOGGER.info("Se realizará una nueva ejecucción de forma consecutiva");
				}
				else
				{
					LOGGER.info("Guardando archivo con registros de ultima ejecución procesada....");
					
					File outputFile = new File(fileLastExecution);
					OutputStream outputStream = new FileOutputStream(outputFile);
					BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
					
					StringBuffer sb = new StringBuffer();
					
					lastExecutionCSV.getSubmissions().forEach(submission -> {
						sb.append(submission.getId()).append(fileLastExecutionSplit);
					});
					
					bufferedWriter.write(sb.toString());
					bufferedWriter.flush();
					bufferedWriter.close();
					
					LOGGER.info("No hay más ejecucciones, fin del proceso.");
				}
			}
			while(executeAgain);
		};
	}

}
