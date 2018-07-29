package cl.smartware.machali.repository.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "m4x4l1_rsform_submissions")
public class Submissions
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SubmissionId", length = 11, nullable = false)
	private Integer id;

	@Column(name = "FormId", length = 11, nullable = false)
	private Integer formId;

	@Column(name = "DateSubmitted")
	@Temporal(TemporalType.TIMESTAMP)
	private Date submitted;

	@Column(name = "UserIp", length = 255)
	private String userIp;

	@Column(name = "Username", length = 255)
	private String username;

	@Column(name = "UserId", columnDefinition = "MEDIUMTEXT")
	private String userId;

	@Column(name = "Lang", length = 255)
	private String lang;

	@Column(name = "confirmed", columnDefinition = "BIT")
	private boolean confirmed;

	@Column(name = "SubmissionHash", length = 32)
	private String submissionHash;

	@OneToMany(mappedBy = "submission")
	private List<SubmissionsValue> submissions = new ArrayList<>();

	public Submissions addValue(SubmissionsValue value)
	{
		submissions.add(value);
		return this;
	}
	
	public void addAllValues(List<SubmissionsValue> submissions)
	{
		submissions.addAll(submissions);
	}

	/**
	 * @return the id
	 */
	public Integer getId()
	{
		return this.id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id)
	{
		this.id = id;
	}

	/**
	 * @return the formId
	 */
	public Integer getFormId()
	{
		return this.formId;
	}

	/**
	 * @param formId the formId to set
	 */
	public void setFormId(Integer formId)
	{
		this.formId = formId;
	}

	/**
	 * @return the submitted
	 */
	public Date getSubmitted()
	{
		return this.submitted;
	}

	/**
	 * @param submitted the submitted to set
	 */
	public void setSubmitted(Date submitted)
	{
		this.submitted = submitted;
	}

	/**
	 * @return the userIp
	 */
	public String getUserIp()
	{
		return this.userIp;
	}

	/**
	 * @param userIp the userIp to set
	 */
	public void setUserIp(String userIp)
	{
		this.userIp = userIp;
	}

	/**
	 * @return the username
	 */
	public String getUsername()
	{
		return this.username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}

	/**
	 * @return the userId
	 */
	public String getUserId()
	{
		return this.userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	/**
	 * @return the lang
	 */
	public String getLang()
	{
		return this.lang;
	}

	/**
	 * @param lang the lang to set
	 */
	public void setLang(String lang)
	{
		this.lang = lang;
	}

	/**
	 * @return the confirmed
	 */
	public boolean isConfirmed()
	{
		return this.confirmed;
	}

	/**
	 * @param confirmed the confirmed to set
	 */
	public void setConfirmed(boolean confirmed)
	{
		this.confirmed = confirmed;
	}

	/**
	 * @return the submissionHash
	 */
	public String getSubmissionHash()
	{
		return this.submissionHash;
	}

	/**
	 * @param submissionHash the submissionHash to set
	 */
	public void setSubmissionHash(String submissionHash)
	{
		this.submissionHash = submissionHash;
	}

	/**
	 * @return the submissions
	 */
	public List<SubmissionsValue> getSubmissions()
	{
		return this.submissions;
	}

	/**
	 * @param submissions the submissions to set
	 */
	public void setSubmissions(List<SubmissionsValue> values)
	{
		this.submissions = values;
	}
}
