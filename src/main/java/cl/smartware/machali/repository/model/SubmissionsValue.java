package cl.smartware.machali.repository.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "m4x4l1_rsform_submission_values")
public class SubmissionsValue
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SubmissionValueId", length = 11, nullable = false)
	private Integer id;

	@Column(name = "FormId", length = 11, nullable = false)
	private Integer formId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="SubmissionId")
	private Submissions submission;

	@Column(name = "FieldName", columnDefinition = "MEDIUMTEXT")
	private String fieldName;

	@Column(name = "FieldValue", columnDefinition = "MEDIUMTEXT")
	private String fieldValue;

	/**
	 * @return the id
	 */
	public Integer getId()
	{
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
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
	 * @param formId
	 *            the formId to set
	 */
	public void setFormId(Integer formId)
	{
		this.formId = formId;
	}

	/**
	 * @return the submission
	 */
	public Submissions getSubmission()
	{
		return this.submission;
	}

	/**
	 * @param submission
	 *            the submission to set
	 */
	public void setSubmission(Submissions submission)
	{
		this.submission = submission;
	}

	/**
	 * @return the fieldName
	 */
	public String getFieldName()
	{
		return this.fieldName;
	}

	/**
	 * @param fieldName
	 *            the fieldName to set
	 */
	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}

	/**
	 * @return the fieldValue
	 */
	public String getFieldValue()
	{
		return this.fieldValue;
	}

	/**
	 * @param fieldValue
	 *            the fieldValue to set
	 */
	public void setFieldValue(String fieldValue)
	{
		this.fieldValue = fieldValue;
	}

	public enum SubmissionsValueFieldNames
	{
		ASUNTO("asunto", "motivo")
		, LUGAR("lugar", "lugar")
		, COMENTARIOS("comentarios", null)
		, GEOMAPS("geomaps", null)
		, NOMBRE("nombre", "usuario")
		, EMAIL("email", "email")
		, TELEFONO("telefono", "telefonoContacto")
		, EDAD("edad", null)
		, RESULT("result", "archivo")
		, TICKET("ticket", null)
		, FORMID("formId", null)
		, DATESUBMITTED("DateSubmitted", "date");
		
		private String submissionsValueField;
		private String csvField;

		private SubmissionsValueFieldNames(String submissionaValueField, String csvField)
		{
			this.submissionsValueField = submissionaValueField;
			this.csvField = csvField;
		}

		/**
		 * @return the submissionsValueField
		 */
		public String getSubmissionsValueField()
		{
			return this.submissionsValueField;
		}

		/**
		 * @param submissionsValueField the submissionsValueField to set
		 */
		public void setSubmissionsValueField(String value)
		{
			this.submissionsValueField = value;
		}

		/**
		 * @return the csvField
		 */
		public String getCsvField()
		{
			return this.csvField;
		}

		/**
		 * @param csvField the csvField to set
		 */
		public void setCsvField(String submissionaValueField)
		{
			this.csvField = submissionaValueField;
		}
	}
}
