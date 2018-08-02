package cl.smartware.machali.service;

import java.util.Optional;

import cl.smartware.machali.CSVItem;
import cl.smartware.machali.repository.model.Submissions;

public interface SubmissionsService
{
	public Submissions buildFromCSVItem(CSVItem item);
	
	public Submissions save(Submissions submission);
	
	public void delete(Submissions submission);
	
	public Optional<Submissions> findById(Integer id);
}
