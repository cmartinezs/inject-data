package cl.smartware.machali.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.smartware.machali.CSVItem;
import cl.smartware.machali.repository.crud.SubmissionsValueCrudRepository;
import cl.smartware.machali.repository.model.Submissions;
import cl.smartware.machali.repository.model.SubmissionsValue;
import cl.smartware.machali.repository.model.SubmissionsValue.SubmissionsValueFieldNames;
import cl.smartware.machali.service.SubmissionsValueService;

@Service
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
			if(field.getCsvField() != null)
			{
				SubmissionsValue value = new SubmissionsValue();
				value.setFieldName(field.getSubmissionsValueField());
				value.setFieldValue(mapFieldValue(field, item));
				value.setFormId(6);
				value.setSubmission(submission);
				
				list.add(value);
			}
			else
			{
				LOGGER.warn(MessageFormat.format("La columna {0} no tiene par en archivo CSV", field.getSubmissionsValueField()));
			}
			
		}
		
		return list;
	}

	private String mapFieldValue(SubmissionsValueFieldNames field, CSVItem item)
	{
		String value = "";
		
		Class<?> clazz = item.getClass();
		
		try
		{
			Method method = clazz.getDeclaredMethod(getGetMethodNameFromField(field));
			Object returnedObject = method.invoke(item, new Object[] {});
			value = String.valueOf(returnedObject);
		}
		catch (NoSuchMethodException | SecurityException e)
		{
			LOGGER.warn(MessageFormat.format("Método {0} no encontrado", getGetMethodNameFromField(field)), e);
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			LOGGER.warn(MessageFormat.format("Error al ejecutar el método {0}", getGetMethodNameFromField(field)), e);
		}
		
		if(field.equals(SubmissionsValueFieldNames.GEOMAPAS))
		{
			//String iframe = "<iframe src=\"https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d6656.066581992951!2d-70.55392342452782!3d-33.474484023068044!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x9662ce209ff85e67%3A0x81e08693f444cfb0!2s"+value+"!5e0!3m2!1ses-419!2scl!4v1524636026559";
			value = "Frame de GMaps para ["+value+"] próximamente.";
		}
		
		return value;
	}

	private String getGetMethodNameFromField(SubmissionsValueFieldNames field)
	{
		return "get" + field.getCsvField().substring(0, 1).toUpperCase() + field.getCsvField().substring(1);
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

	@Override
	public void delete(SubmissionsValue entity) {
		submissionsValueCrudRepository.delete(entity);
	}

	@Override
	public void deleteAll(Iterable<SubmissionsValue> entities) {
		submissionsValueCrudRepository.deleteAll(entities);
	}
}
