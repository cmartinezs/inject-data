package cl.smartware.machali.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cl.smartware.machali.CSVItem;
import cl.smartware.machali.repository.crud.SubmissionsValueCrudRepository;
import cl.smartware.machali.repository.model.Submissions;
import cl.smartware.machali.repository.model.SubmissionsValue;
import cl.smartware.machali.repository.model.SubmissionsValue.SubmissionsValueFieldNames;
import cl.smartware.machali.service.SubmissionsValueService;

public class SubsmissionsValueServiceimpl implements SubmissionsValueService
{
	@Autowired
	private SubmissionsValueCrudRepository submissionsValueCrudRepository;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SubsmissionsValueServiceimpl.class);
	
	@Override
	public List<SubmissionsValue> buildFromCSVItem(CSVItem item, Submissions submission)
	{
		List<SubmissionsValue> list = new ArrayList<>();
		
		for(SubmissionsValueFieldNames field: SubmissionsValueFieldNames.values())
		{
			SubmissionsValue value = new SubmissionsValue();
			value.setFieldName(field.getCsvField());
			value.setFieldValue(mapFieldValue(field, item));
			value.setFormId(6);
			value.setSubmission(submission);
			
			list.add(value);
		}
		
		return list;
	}

	private String mapFieldValue(SubmissionsValueFieldNames field, CSVItem item)
	{
		String value = "";
		
		Class<?> clazz = item.getClass();
		
		try
		{
			Method method = clazz.getMethod(getSetMethodNameFromField(field), String.class);
			Object returnedObject = method.invoke(item, new Object[] {});
			value = String.valueOf(returnedObject);
		}
		catch (NoSuchMethodException | SecurityException e)
		{
			LOGGER.warn(MessageFormat.format("Campo no encontrado {0}", field.getCsvField()), e);
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			LOGGER.warn(MessageFormat.format("Error al ejecutar el método {0}", getSetMethodNameFromField(field)), e);
		}
		
		return value;
	}

	private String getSetMethodNameFromField(SubmissionsValueFieldNames field)
	{
		return "set" + field.getCsvField().substring(0, 1).toUpperCase() + field.getCsvField().substring(1);
	}

	@Override
	public SubmissionsValue save(SubmissionsValue entity)
	{
		return submissionsValueCrudRepository.save(entity);
	}

	@Override
	public Iterable<SubmissionsValue> saveAll(Iterable<SubmissionsValue> entities)
	{
		return submissionsValueCrudRepository.saveAll(entities);
	}

}
