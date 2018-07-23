package cl.smartware.machali.service.impl;

import java.text.MessageFormat;
import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cl.smartware.machali.CSVItem;
import cl.smartware.machali.repository.model.Submissions;
import cl.smartware.machali.service.SubmissionsService;
import cl.smartware.machali.utils.DateUtils;

public class SubmissionsServiceImpl implements SubmissionsService
{
	@Autowired
	private SubmissionsService submissionsService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SubmissionsServiceImpl.class);
	
	@Override
	public Submissions buildFromCSVItem(CSVItem item)
	{
		Submissions submission = new Submissions();
		
		submission.setConfirmed(true);
		submission.setFormId(6);
		submission.setLang("es-ES");
		submission.setSubmissionHash("unHashAlAzar");
		
		try
		{
			submission.setSubmitted(DateUtils.toDate(item.getDate()));
		}
		catch (ParseException e)
		{
			LOGGER.warn(MessageFormat.format("No se ha podido convertir {0} a fecha", item.getDate()));
		}
		
		submission.setUserId(String.valueOf("0"));
		submission.setUserIp(String.valueOf("0"));
		submission.setUsername(item.getUsuario());
		
		return submission;
	}

	@Override
	public Submissions save(Submissions submission)
	{
		return submissionsService.save(submission);
	}

}
