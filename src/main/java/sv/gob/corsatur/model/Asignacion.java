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

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Asignacion {
	
	@ManyToOne
	@JoinColumn(name = "empleado_id", nullable = false )
	private Empleado empleadoId;
	
	@ManyToOne
	@JoinColumn(name = "area_id", nullable = false )
	private Area areaId;
	
	@ManyToOne
	@JoinColumn(name = "inventario_id", nullable = false )
	private Inventario inventarioId;
	
	
	
	
	public Inventario getInventarioId() {
		return inventarioId;
	}

	public void setInventarioId(Inventario inventarioId) {
		this.inventarioId = inventarioId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "asignacion_id", unique = true, nullable = false)
	private int asignacionId;
	
	@Column(name="tipo_asignacion", nullable = false, length = 50)
	private String tipoAsignacion;
	
	@Column(name = "create_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@PrePersist
	public void prePersist() {
		createDate = new Date();
	}
	
	@Column(name = "fecha_asignacion", nullable = false)
	private String fechaAsignacion;

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
	public Empleado getEmpleadoId() {
		return empleadoId;
	}

	public void setEmpleadoId(Empleado empleadoId) {
		this.empleadoId = empleadoId;
	}

	public Area getAreaId() {
		return areaId;
	}

	public void setAreaId(Area areaId) {
		this.areaId = areaId;
	}

	public int getAsignacionId() {
		return asignacionId;
	}

	public void setAsignacionId(int asignacionId) {
		this.asignacionId = asignacionId;
	}

	public String getTipoAsignacion() {
		return tipoAsignacion;
	}

	public void setTipoAsignacion(String tipoAsignacion) {
		this.tipoAsignacion = tipoAsignacion;
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

	public Asignacion(Empleado empleadoId, Inventario inventarioId,Area areaId, String tipoAsignacion, Date createDate, String userCreate,
			String estado, String fechaAsignacion) {
		super();
		this.empleadoId = empleadoId;
		this.areaId = areaId;
		this.tipoAsignacion = tipoAsignacion;
		this.createDate = createDate;
		this.userCreate = userCreate;
		this.estado = estado;
		this.inventarioId=inventarioId;
		this.fechaAsignacion=fechaAsignacion;
	}

	public Asignacion() {
		super();
	}

	public String getFechaAsignacion() {
		return fechaAsignacion;
	}

	public void setFechaAsignacion(String fechaAsignacion) {
		this.fechaAsignacion = fechaAsignacion;
	}


	
	
}
