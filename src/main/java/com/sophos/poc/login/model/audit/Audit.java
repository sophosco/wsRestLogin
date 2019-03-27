package com.sophos.poc.login.model.audit;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Audit {
	
	@JsonProperty(value ="FechaCreacion")
	private Date FechaCreacion;
	@JsonProperty(value ="IdSesion")
	private String IdSesion;
	@JsonProperty(value ="IdUsuario")
	private String IdUsuario;
	@JsonProperty(value ="TipoAccion")
	private String TipoAccion;
	@JsonProperty(value ="DescripcionAccion")
	private String DescripcionAccion;
	@JsonProperty(value ="ModuloAplicacion")
	private String ModuloAplicacion;
	@JsonProperty(value ="IdProducto")
	private String IdProducto;
	@JsonProperty(value ="IdCategoria")
	private String IdCategoria;
	@JsonProperty(value ="MessageData")
	private String MessageData;
	
	
	public Audit() {}
	
	public Date getFechaCreacion() {
		return FechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		FechaCreacion = fechaCreacion;
	}
	public String getIdSesion() {
		return IdSesion;
	}
	public void setIdSesion(String idSesion) {
		IdSesion = idSesion;
	}
	public String getIdUsuario() {
		return IdUsuario;
	}
	public void setIdUsuario(String idUsuario) {
		IdUsuario = idUsuario;
	}
	public String getTipoAccion() {
		return TipoAccion;
	}
	public void setTipoAccion(String tipoAccion) {
		TipoAccion = tipoAccion;
	}
	public String getDescripcionAccion() {
		return DescripcionAccion;
	}
	public void setDescripcionAccion(String descripcionAccion) {
		DescripcionAccion = descripcionAccion;
	}
	public String getModuloAplicacion() {
		return ModuloAplicacion;
	}
	public void setModuloAplicacion(String moduloAplicacion) {
		ModuloAplicacion = moduloAplicacion;
	}
		public String getMessageData() {
		return MessageData;
	}
	public void setMessageData(String messageData) {
		MessageData = messageData;
	}

	public String getIdProducto() {
		return IdProducto;
	}

	public void setIdProducto(String idProducto) {
		IdProducto = idProducto;
	}

	public String getIdCategoria() {
		return IdCategoria;
	}

	public void setIdCategoria(String idCategoria) {
		IdCategoria = idCategoria;
	}

	public Audit(Date fechaCreacion, String idSesion, String idUsuario, String tipoAccion, String descripcionAccion,
			String moduloAplicacion, String idProducto, String idCategoria, String messageData) {
		super();
		FechaCreacion = fechaCreacion;
		IdSesion = idSesion;
		IdUsuario = idUsuario;
		TipoAccion = tipoAccion;
		DescripcionAccion = descripcionAccion;
		ModuloAplicacion = moduloAplicacion;
		IdProducto = idProducto;
		IdCategoria = idCategoria;
		MessageData = messageData;
	}

	
}
