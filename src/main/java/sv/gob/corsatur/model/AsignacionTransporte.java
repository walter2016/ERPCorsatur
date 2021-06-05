package sv.gob.corsatur.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class AsignacionTransporte {
	
	@ManyToOne
	@JoinColumn(name = "gerencia_id", nullable = false )
	private Gerencia gerenciaId;
	
	@ManyToOne
	@JoinColumn(name = "claseVehiculo_id", nullable = false )
	private ClaseVehiculo claseVehiculoId;
	
	@ManyToOne
	@JoinColumn(name = "motorista_id", nullable = true )
	private Motoristas motoristaId;
	
	@ManyToOne
	@JoinColumn(name = "inventarioVehiculo_id", nullable = true )
	private InventarioVehiculo inventarioVehiculoId;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "asigna_transpporte_id", unique = true, nullable = false)
	private int asignacionTransporteId;
	
	
	@NotNull
	@Column(name = "encargada_mision", length = 150)
	private String encargadaMision;
	
	@NotNull
	@Column(name = "persona_viajaran", length = 5000)
	private String personaViajaran;
	
	@NotNull
	@Column(name = "destino", length = 255)
	private String destino;
	
	
	@NotNull
	@Column(name = "objetivo_mision", length = 255)
	private String objetivoMision;
	
	@Column(name = "fecha_utilizacion", nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fechaUtilizacion;
	
	@NotNull
	@Column(name = "hora_salida", length = 100)
	private String horaSalida;
	
	@NotNull
	@Column(name = "hora_regreso", length = 100)
	private String horaRegreso;
	
	@Column(name = "estado_solicitud", length = 100)
	private String estadoSolicitud;

	
	@NotNull
	@Column(name = "observacion", length = 255)
	private String observacion;
	
	@Column(name = "create_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@PrePersist
	public void prePersist() {
		createDate = new Date();
	}

	@Column(name = "user_create", nullable = false, length = 100)
	private String userCreate;

	@Column(name = "update_date")
	private Date updateDate;
	@PreUpdate
	public void preUpdate() {
		updateDate=new Date();
	}

	
	@Column(name = "user_update", length = 100)
	private String userUpdate;
	
	@Column(name = "estado", length = 1)
	private String estado;
	public Gerencia getGerenciaId() {
		return gerenciaId;
	}

	public void setGerenciaId(Gerencia gerenciaId) {
		this.gerenciaId = gerenciaId;
	}

	public ClaseVehiculo getClaseVehiculoId() {
		return claseVehiculoId;
	}

	public void setClaseVehiculoId(ClaseVehiculo claseVehiculoId) {
		this.claseVehiculoId = claseVehiculoId;
	}

	public Motoristas getMotoristaId() {
		return motoristaId;
	}

	public void setMotoristaId(Motoristas motoristaId) {
		this.motoristaId = motoristaId;
	}

	public InventarioVehiculo getInventarioVehiculoId() {
		return inventarioVehiculoId;
	}

	public void setInventarioVehiculoId(InventarioVehiculo inventarioVehiculoId) {
		this.inventarioVehiculoId = inventarioVehiculoId;
	}

	public int getAsignacionTransporteId() {
		return asignacionTransporteId;
	}

	public void setAsignacionTransporteId(int asignacionTransporteId) {
		this.asignacionTransporteId = asignacionTransporteId;
	}

	public String getEncargadaMision() {
		return encargadaMision;
	}

	public void setEncargadaMision(String encargadaMision) {
		this.encargadaMision = encargadaMision;
	}



	public String getPersonaViajaran() {
		return personaViajaran;
	}

	public void setPersonaViajaran(String personaViajaran) {
		this.personaViajaran = personaViajaran;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public String getObjetivoMision() {
		return objetivoMision;
	}

	public void setObjetivoMision(String objetivoMision) {
		this.objetivoMision = objetivoMision;
	}

	public Date getFechaUtilizacion() {
		return fechaUtilizacion;
	}

	public void setFechaUtilizacion(Date fechaUtilizacion) {
		this.fechaUtilizacion = fechaUtilizacion;
	}

	public String getHoraSalida() {
		return horaSalida;
	}

	public void setHoraSalida(String horaSalida) {
		this.horaSalida = horaSalida;
	}

	public String getHoraRegreso() {
		return horaRegreso;
	}

	public void setHoraRegreso(String horaRegreso) {
		this.horaRegreso = horaRegreso;
	}

	public String getEstadoSolicitud() {
		return estadoSolicitud;
	}

	public void setEstadoSolicitud(String estadoSolicitud) {
		this.estadoSolicitud = estadoSolicitud;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUserCreate() {
		return userCreate;
	}

	public void setUserCreate(String userCreate) {
		this.userCreate = userCreate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUserUpdate() {
		return userUpdate;
	}

	public void setUserUpdate(String userUpdate) {
		this.userUpdate = userUpdate;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	


}
