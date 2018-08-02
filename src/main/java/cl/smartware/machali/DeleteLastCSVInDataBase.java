package cl.smartware.machali;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cl.smartware.machali.service.SubmissionsService;
import cl.smartware.machali.service.SubmissionsValueService;

public class DeleteLastCSVInDataBase extends Thread {
	private SubmissionsService submissionsService;

	private SubmissionsValueService submissionsValueService;

	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteLastCSVInDataBase.class);

	private LastExecutionCSV lastExecutionCSV;

	@Override
	public void run() {
		LOGGER.info("Eliminando ejecución anterior...");
		
		lastExecutionCSV.getSubmissions().forEach(submission -> {
			
			submission.getSubmissionsValues().forEach(submissionsValue -> {
				LOGGER.info(MessageFormat.format("Eliminando SubmissionValue id = {0}", submissionsValue.getId()));
				submissionsValueService.delete(submissionsValue);
			});
			
			submission.setSubmissionsValues(null);
			
			LOGGER.info(MessageFormat.format("Eliminando submission id = {0}", submission.getId()));
			submissionsService.delete(submission);
		});
		
		
		LOGGER.info("Ejecución anterior eliminada...");
	}

	public LastExecutionCSV getLastExecutionCSV() {
		return lastExecutionCSV;
	}

	public void setLastExecutionCSV(LastExecutionCSV lastexecutionCSV) {
		this.lastExecutionCSV = lastexecutionCSV;
	}

	public SubmissionsService getSubmissionsService() {
		return submissionsService;
	}

	public void setSubmissionsService(SubmissionsService submissionsService) {
		this.submissionsService = submissionsService;
	}

	public SubmissionsValueService getSubmissionsValueService() {
		return submissionsValueService;
	}

	public void setSubmissionsValueService(SubmissionsValueService submissionsValueService) {
		this.submissionsValueService = submissionsValueService;
	}
}
