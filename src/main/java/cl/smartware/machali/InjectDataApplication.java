package cl.smartware.machali;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

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
			long initTime = 0;
			long lastExecute = 0;
			long waitTime = 60000;
			long nextExecutionIn = 180000;
			
			LastExecutionCSV lastExecutionCSV = null;

			do 
			{
				initTime = Calendar.getInstance().getTimeInMillis();
				long timeElapsed = initTime - lastExecute;
				
				if(lastExecute > 0 && (timeElapsed <= nextExecutionIn)) 
				{
					LOGGER.info(MessageFormat.format("Esperando para próxima ejecución... {0}ms", nextExecutionIn - timeElapsed));
					
					if(nextExecutionIn - timeElapsed <= 10000) 
					{
						waitTime = 1000;
					}
					else if(nextExecutionIn - timeElapsed <= 30000) 
					{
						waitTime = 10000;
					}
					
					Thread.sleep(waitTime);
					
					continue;
				}
				
				LOGGER.info(MessageFormat.format("Iniciando proceso... {0}", DateUtils.toString(initTime)));
				
				ExecutorService executorService = Executors.newSingleThreadExecutor();
				
				if(lastExecutionCSV != null) 
				{
					LOGGER.info("Se eliminará la inserción del proceso anterior...");
					
					DeleteLastCSVInDataBase deleteLastCSVInDataBase = new DeleteLastCSVInDataBase();
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
				saveCSVinDataBase.setFilePath("./files/export_allReply_iphone_22656189.csv");
				saveCSVinDataBase.setSubmissionsService(submissionsService);
				saveCSVinDataBase.setSubmissionsValueService(submissionsValueService);
				
				executorService.execute(saveCSVinDataBase);
				executorService.shutdown();
				
				int i = 0;
				
				do
				{
					LOGGER.info(MessageFormat.format("Ejecutando pool de threads... {0}", i++));

					Thread.sleep(1000);
				}
				while(!executorService.isTerminated());
				
				lastExecutionCSV = new LastExecutionCSV();
				lastExecutionCSV.setSubmissions(saveCSVinDataBase.getSubmissions());

				lastExecute = Calendar.getInstance().getTimeInMillis();
				
				LOGGER.info(MessageFormat.format("Pool de threads finalizado... {0}", DateUtils.toString(lastExecute)));
				
				waitTime = 60000;
			}
			while(true);
		};
	}

}
