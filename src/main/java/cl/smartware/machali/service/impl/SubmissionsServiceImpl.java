package cl.smartware.machali.service.impl;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.smartware.machali.CSVItem;
import cl.smartware.machali.repository.crud.SubmissionsCrudRepository;
import cl.smartware.machali.repository.model.Submissions;
import cl.smartware.machali.service.SubmissionsService;
import cl.smartware.machali.utils.DateUtils;

@Service
public class SubmissionsServiceImpl implements SubmissionsService
{
	@Autowired
	private SubmissionsCrudRepository submissionsCrudRepository;
	
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
			LOGGER.warn(MessageFormat.format("No se ha podido convertir {0} a fecha", item.getDate()), e);
			return null;
		}
		
		submission.setUserId(String.valueOf("0"));
		submission.setUserIp(String.valueOf("0"));
		submission.setUsername(item.getUsuario());
		
		return submission;
	}

	@Override
	public Submissions save(Submissions submission)
	{
		return submissionsCrudRepository.save(submission);
	}

	@Override
	public void delete(Submissions submission) {
		submissionsCrudRepository.delete(submission);
	}

	@Override
	public Optional<Submissions> findById(Integer id) {
		return submissionsCrudRepository.findById(id);
	}

}
