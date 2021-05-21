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
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Solicitud {
	
	@ManyToOne
	@JoinColumn(name = "empleado_id", nullable = false )
	private Empleado empleadoId;
	
	@ManyToOne
	@JoinColumn(name = "solic_cate_id", nullable = false )
	private SolicitudCategoria solicCateId;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "solicitud_id", unique = true, nullable = false)
	private int solicitudId;
	
	@Column(name = "numero_solicitud", unique = true, nullable = false)
	private int numeroSolicitud;
	
	@Column(name = "estado_solicitud", nullable = false, length = 50)
	private String estadoSolicitud;
	
	@Column(name = "descripcion", nullable = false, length = 500)
	private String descripcion;
	
	@Column(name = "respuesta_solicitud", nullable = false, length = 500)
	private String respuestaSolicitud;
	
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
		updateDate = new Date();
	}

	@Column(name = "user_update", length = 100)
	private String userUpdate;

	@Column(name = "estado", length = 1)
	private String estado;

	public Empleado getEmpleadoId() {
		return empleadoId;
	}

	public void setEmpleadoId(Empleado empleadoId) {
		this.empleadoId = empleadoId;
	}

	public SolicitudCategoria getSolicCateId() {
		return solicCateId;
	}

	public void setSolicCateId(SolicitudCategoria solicCateId) {
		this.solicCateId = solicCateId;
	}

	public int getSolicitudId() {
		return solicitudId;
	}

	public void setSolicitudId(int solicitudId) {
		this.solicitudId = solicitudId;
	}

	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}

	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	public String getEstadoSolicitud() {
		return estadoSolicitud;
	}

	public void setEstadoSolicitud(String estadoSolicitud) {
		this.estadoSolicitud = estadoSolicitud;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getRespuestaSolicitud() {
		return respuestaSolicitud;
	}

	public void setRespuestaSolicitud(String respuestaSolicitud) {
		this.respuestaSolicitud = respuestaSolicitud;
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

	public Solicitud() {
		super();
	}
	
	
	

}
