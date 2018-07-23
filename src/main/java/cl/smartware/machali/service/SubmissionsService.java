package cl.smartware.machali.service;

import cl.smartware.machali.CSVItem;
import cl.smartware.machali.repository.model.Submissions;

public interface SubmissionsService
{
	public Submissions buildFromCSVItem(CSVItem item);
	
	public Submissions save(Submissions submission);
}
