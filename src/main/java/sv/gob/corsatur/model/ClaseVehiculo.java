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
public class ClaseVehiculo {
	
	@OneToMany(mappedBy = "claseVehiculoId", cascade = CascadeType.ALL)
	private List<AsignacionTransporte> asignaciones;
	
	@OneToMany(mappedBy = "claseVehiculoId", cascade = CascadeType.ALL)
    private List<InventarioVehiculo> inventariosVehiculos ;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "claseVehiculo_id", unique = true, nullable = false)
	public int claseVehiculoId;
	
	@NotNull
    @Column(unique=true,length = 100)
    public String clase;
	
	@Column(name = "create_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date createDate;
	@PrePersist
	public void prePersist() {
		createDate = new Date();
	}

	@Column(name = "user_create", nullable = false, length = 100)
	public String userCreate;

	@Column(name = "update_date")
	public Date updateDate;
	@PreUpdate
	public void preUpdate() {
		updateDate=new Date();
	}

	
	@Column(name = "user_update", length = 100)
	public String userUpdate;
	
	@Column(name = "estado", length = 1)
	public String estado;
	public int getClaseVehiculoId() {
		return claseVehiculoId;
	}

	public void setClaseVehiculoId(int claseVehiculoId) {
		this.claseVehiculoId = claseVehiculoId;
	}

	public String getClase() {
		return clase;
	}

	public void setClase(String clase) {
		this.clase = clase;
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
