package cl.smartware.machali.service;

import java.util.List;

import cl.smartware.machali.CSVItem;
import cl.smartware.machali.repository.model.Submissions;
import cl.smartware.machali.repository.model.SubmissionsValue;

public interface SubmissionsValueService
{
	public List<SubmissionsValue> buildFromCSVItem(CSVItem item, Submissions submission);
	
	public SubmissionsValue save(SubmissionsValue entity);
	
	public Iterable<SubmissionsValue> saveAll(Iterable<SubmissionsValue> entities);
	
	public void delete(SubmissionsValue entity);
	
	public void deleteAll(Iterable<SubmissionsValue> entities);
}
