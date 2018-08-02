/**
 * 
 */
package cl.smartware.machali;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cl.smartware.machali.repository.model.Submissions;
import cl.smartware.machali.repository.model.SubmissionsValue;
import cl.smartware.machali.service.SubmissionsService;
import cl.smartware.machali.service.SubmissionsValueService;
import cl.smartware.machali.utils.CSVItemUtils;

/**
 * @author carlo
 *
 */
class SaveCSVInDataBase extends Thread 
{
	private SubmissionsService submissionsService;
	
	private SubmissionsValueService submissionsValueService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SaveCSVInDataBase.class);
	
	private String filePath;
	
	private String csvSplit;
	
	private List<Submissions> submissions;

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() 
	{	
		final Function<String, CSVItem> mapToItem = (line) -> 
		{
			String[] p = line.split(csvSplit);
			CSVItem item = new CSVItem();
			item.setDate(CSVItemUtils.removeSurroundQuote(p[0]));
			item.setMotivo(CSVItemUtils.removeSurroundQuote(p[1]));
			item.setLugar(CSVItemUtils.removeSurroundQuote(p[2]));
			item.setArchivo(CSVItemUtils.removeSurroundQuote(p[3]));
			item.setUsuario(CSVItemUtils.removeSurroundQuote(p[4]));
			item.setEmail(CSVItemUtils.removeSurroundQuote(p[5]));
			item.setTelefonoContacto(CSVItemUtils.removeSurroundQuote(p[6]));
			item.setFecha(CSVItemUtils.removeSurroundQuote(p[7]));
			item.setHora(CSVItemUtils.removeSurroundQuote(p[8]));
			return item;
		};
		
		LOGGER.info("Lectura de archivo");
		
		File inputF = new File(filePath);
		
		InputStream inputFS = null;
		
		try 
		{
			inputFS = new FileInputStream(inputF);
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
		
		List<CSVItem> items = br.lines().skip(1).map(mapToItem).collect(Collectors.toList());
		
		try 
		{
			br.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		items.forEach((item) -> System.out.println(item));
		
		LOGGER.info("Guardado de archivo en base de datos...");

		submissions = new ArrayList<>();
		
		items.forEach(
			(item) -> {
				Submissions submission = submissionsService.buildFromCSVItem(item);
				
				if(submission != null)
				{
					submission = submissionsService.save(submission);
				
					LOGGER.info(MessageFormat.format("Insertando Submission id {0}", submission.getId()));
				
					List<SubmissionsValue> submissionValues = submissionsValueService.buildFromCSVItem(item, submission);
					submissionsValueService.saveAll(submissionValues);
					
					LOGGER.info(MessageFormat.format("A insertar {0} registros de SubmissionsValues", submissionValues.size()));
					
					submissionValues.forEach(submissionsValue -> {
						LOGGER.info(MessageFormat.format("Insertando SubmissionValues id: {0}", submissionsValue.getId()));
					});
					
					
					submission.addAllValues(submissionValues);
					
					submissions.add(submission);
				}
				else
				{
					LOGGER.warn(MessageFormat.format("No ha sido posible transformar el item CSV a modelo de Submission: {0}", item.getMotivo()));
				}
			}
		);
	}

	public void setSubmissionsService(SubmissionsService submissionsService) {
		this.submissionsService = submissionsService;
	}

	public void setSubmissionsValueService(SubmissionsValueService submissionsValueService) {
		this.submissionsValueService = submissionsValueService;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public List<Submissions> getSubmissions() {
		return submissions;
	}

	public String getCsvSplit() {
		return csvSplit;
	}

	public void setCsvSplit(String csvSplit) {
		this.csvSplit = csvSplit;
	}
}
