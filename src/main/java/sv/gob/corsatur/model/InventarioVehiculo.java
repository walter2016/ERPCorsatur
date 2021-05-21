package sv.gob.corsatur.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
public class InventarioVehiculo {
	
	@OneToMany(mappedBy = "inventarioVehiculoId", cascade = CascadeType.ALL)
	private List<AsignacionTransporte> asignaciones;
	
	@ManyToOne
	@JoinColumn(name = "claseVehiculo_id", nullable = false )
	private ClaseVehiculo claseVehiculoId;
	
	@ManyToOne
	@JoinColumn(name = "marcaVehiculo_id", nullable = false )
	private MarcaVehiculo marcaVehiculoId;
	
	@ManyToOne
	@JoinColumn(name = "estadoVehiculo_id", nullable = false )
	private EstadoVehiculo estadoVehiculoId;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "inventarioVehiculo_id", unique = true, nullable = false)
	private int inventarioVehiculoId;
	
	@NotNull
    @Column(unique=true,length = 100)
    private String placa;
	
	@NotNull
    @Column(length = 250)
    private String asignacion;

	
	@NotNull
    @Column(length = 100)
    private String modelo;

	@NotNull
    @Column(length = 100)
    private String anho;

	@NotNull
    @Column(unique=true,length = 100)
    private String numeroMotor;
	
	@NotNull
    @Column(unique=true,length = 100)
    private String numeroChasis;
	
	@NotNull
    @Column(length = 100)
    private String numeroVin;
	
	
	@NotNull
    @Column(length = 100)
    private String tipoAsientos;
	
	@NotNull
    @Column(length = 100)
    private String color;
	
	
	@NotNull
    @Column(length = 100)
    private String descripcion;
	

    @Column(length = 100)
    private String traccion;
    @NotNull
    @Column(length = 100)
    private String capacidad;
    @NotNull
    @Column(length = 100)
    private String combustible;
    
    @NotNull
	@Column(unique = false)
	private boolean aplica;
    
    @Column(name = "asignado", length = 1)
	private String asignado;
	

    @Column(length = 100)
    private String utilizadoPor;
    
    @Column(name = "create_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@PrePersist
	public void prePersist() {
		createDate = new Date();
	}
	
	
	public boolean isAplica() {
		return aplica;
	}


	public void setAplica(boolean aplica) {
		this.aplica = aplica;
	}


	public String getAsignado() {
		return asignado;
	}


	public void setAsignado(String asignado) {
		this.asignado = asignado;
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


	public ClaseVehiculo getClaseVehiculoId() {
		return claseVehiculoId;
	}


	public void setClaseVehiculoId(ClaseVehiculo claseVehiculoId) {
		this.claseVehiculoId = claseVehiculoId;
	}


	public MarcaVehiculo getMarcaVehiculoId() {
		return marcaVehiculoId;
	}


	public void setMarcaVehiculoId(MarcaVehiculo marcaVehiculoId) {
		this.marcaVehiculoId = marcaVehiculoId;
	}


	public EstadoVehiculo getEstadoVehiculoId() {
		return estadoVehiculoId;
	}


	public void setEstadoVehiculoId(EstadoVehiculo estadoVehiculoId) {
		this.estadoVehiculoId = estadoVehiculoId;
	}


	public int getInventarioVehiculoId() {
		return inventarioVehiculoId;
	}


	public void setInventarioVehiculoId(int inventarioVehiculoId) {
		this.inventarioVehiculoId = inventarioVehiculoId;
	}


	public String getPlaca() {
		return placa;
	}


	public void setPlaca(String placa) {
		this.placa = placa;
	}


	public String getAsignacion() {
		return asignacion;
	}


	public void setAsignacion(String asignacion) {
		this.asignacion = asignacion;
	}


	public String getModelo() {
		return modelo;
	}


	public void setModelo(String modelo) {
		this.modelo = modelo;
	}


	public String getAnho() {
		return anho;
	}


	public void setAnho(String anho) {
		this.anho = anho;
	}


	public String getNumeroMotor() {
		return numeroMotor;
	}


	public void setNumeroMotor(String numeroMotor) {
		this.numeroMotor = numeroMotor;
	}


	public String getNumeroChasis() {
		return numeroChasis;
	}


	public void setNumeroChasis(String numeroChasis) {
		this.numeroChasis = numeroChasis;
	}


	public String getNumeroVin() {
		return numeroVin;
	}


	public void setNumeroVin(String numeroVin) {
		this.numeroVin = numeroVin;
	}


	public String getTipoAsientos() {
		return tipoAsientos;
	}


	public void setTipoAsientos(String tipoAsientos) {
		this.tipoAsientos = tipoAsientos;
	}


	public String getColor() {
		return color;
	}


	public void setColor(String color) {
		this.color = color;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public String getTraccion() {
		return traccion;
	}


	public void setTraccion(String traccion) {
		this.traccion = traccion;
	}


	public String getCapacidad() {
		return capacidad;
	}


	public void setCapacidad(String capacidad) {
		this.capacidad = capacidad;
	}


	public String getCombustible() {
		return combustible;
	}


	public void setCombustible(String combustible) {
		this.combustible = combustible;
	}


	public String getUtilizadoPor() {
		return utilizadoPor;
	}


	public void setUtilizadoPor(String utilizadoPor) {
		this.utilizadoPor = utilizadoPor;
	}
    
    
}
