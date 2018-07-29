package cl.smartware.machali;

import java.util.List;

import cl.smartware.machali.repository.model.Submissions;

public class LastExecutionCSV {
	private List<Submissions> submissions;

	public List<Submissions> getSubmissions() {
		return submissions;
	}

	public void setSubmissions(List<Submissions> submissionValues) {
		this.submissions = submissionValues;
	}

}
