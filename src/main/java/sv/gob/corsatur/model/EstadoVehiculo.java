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
public class EstadoVehiculo {
	
	@OneToMany(mappedBy = "estadoVehiculoId", cascade = CascadeType.ALL)
    private List<InventarioVehiculo> inventariosVehiculos ;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "estadoVehiculo_id", unique = true, nullable = false)
	private int estadoVehiculoId;
	
	@NotNull
    @Column(unique=true,length = 100)
    private String estVehiculo;
	
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
	public int getEstadoVehiculoId() {
		return estadoVehiculoId;
	}

	public void setEstadoVehiculoId(int estadoVehiculoId) {
		this.estadoVehiculoId = estadoVehiculoId;
	}

	public String getEstVehiculo() {
		return estVehiculo;
	}

	public void setEstVehiculo(String estVehiculo) {
		this.estVehiculo = estVehiculo;
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
