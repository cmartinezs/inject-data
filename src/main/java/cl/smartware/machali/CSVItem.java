package cl.smartware.machali;

import java.lang.reflect.Field;

public class CSVItem
{
	private String date;
	private String motivo;
	private String lugar;
	private String archivo;
	private String usuario;
	private String email;
	private String telefonoContacto;
	private String fecha;
	private String hora;

	/**
	 * @return the date
	 */
	public String getDate()
	{
		return this.date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(String date)
	{
		this.date = date;
	}

	/**
	 * @return the motivo
	 */
	public String getMotivo()
	{
		return this.motivo;
	}

	/**
	 * @param motivo
	 *            the motivo to set
	 */
	public void setMotivo(String motivo)
	{
		this.motivo = motivo;
	}

	/**
	 * @return the lugar
	 */
	public String getLugar()
	{
		return this.lugar;
	}

	/**
	 * @param lugar
	 *            the lugar to set
	 */
	public void setLugar(String lugar)
	{
		this.lugar = lugar;
	}

	/**
	 * @return the archivo
	 */
	public String getArchivo()
	{
		return this.archivo;
	}

	/**
	 * @param archivo
	 *            the archivo to set
	 */
	public void setArchivo(String archivo)
	{
		this.archivo = archivo;
	}

	/**
	 * @return the usuario
	 */
	public String getUsuario()
	{
		return this.usuario;
	}

	/**
	 * @param usuario
	 *            the usuario to set
	 */
	public void setUsuario(String usuario)
	{
		this.usuario = usuario;
	}

	/**
	 * @return the email
	 */
	public String getEmail()
	{
		return this.email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email)
	{
		this.email = email;
	}

	/**
	 * @return the telefonoContacto
	 */
	public String getTelefonoContacto()
	{
		return this.telefonoContacto;
	}

	/**
	 * @param telefonoContacto
	 *            the telefonoContacto to set
	 */
	public void setTelefonoContacto(String telefonoContacto)
	{
		this.telefonoContacto = telefonoContacto;
	}

	/**
	 * @return the fecha
	 */
	public String getFecha()
	{
		return this.fecha;
	}

	/**
	 * @param fecha
	 *            the fecha to set
	 */
	public void setFecha(String fecha)
	{
		this.fecha = fecha;
	}

	/**
	 * @return the hora
	 */
	public String getHora()
	{
		return this.hora;
	}

	/**
	 * @param hora
	 *            the hora to set
	 */
	public void setHora(String hora)
	{
		this.hora = hora;
	}

	@Override
	public String toString()
	{
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		for (Field field : this.getClass().getDeclaredFields())
		{
			sb.append("\t");
			
			if(!first) sb.append(", ");
			
			try
			{
				String name = field.getName();
				Object obj = field.get(this);
				
				sb.append(name).append(" = ").append(obj).append("\n");
				
				if(first) first = false;
			}
			catch (IllegalArgumentException | IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
		sb.append("}");
		return sb.toString();
	}
}
