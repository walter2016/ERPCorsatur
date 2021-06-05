package sv.gob.corsatur.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
public class Motoristas {
	
	@OneToMany(mappedBy = "motoristaId", cascade = CascadeType.ALL)
	private List<AsignacionTransporte> asignaciones;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "motorista_id", unique = true, nullable = false)
	private int motoristaId;

	@NotNull
	@Column(name = "nombre", length = 50)
	private String nombre;
	
	@NotNull
	@Column(name = "disponible", length = 1)
	private String disponible;
	
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

	public List<AsignacionTransporte> getAsignaciones() {
		return asignaciones;
	}

	public void setAsignaciones(List<AsignacionTransporte> asignaciones) {
		this.asignaciones = asignaciones;
	}

	public int getMotoristaId() {
		return motoristaId;
	}

	public void setMotoristaId(int motoristaId) {
		this.motoristaId = motoristaId;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDisponible() {
		return disponible;
	}

	public void setDisponible(String disponible) {
		this.disponible = disponible;
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
