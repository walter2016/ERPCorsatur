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

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Inventario {
	
	@OneToMany(mappedBy = "inventarioId", cascade = CascadeType.ALL)
    private List<Asignacion> asignaciones;
	
	@ManyToOne
	@JoinColumn(name = "tipo_id", nullable = false )
	private Tipo tipoId;
	
	@ManyToOne
	@JoinColumn(name = "hacienda_id", nullable = false )
	private CodigoHacienda haciendaId;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "inventario_id", unique = true, nullable = false)
	private int inventarioId;

	@NotNull
	@Column(unique = false)
	private int correlativo;

	@NotNull
	@Column(name = "codigo_individual",unique = true, length = 30)
	private String codigoIndividual;

	@NotNull
	@Column( length = 100)
	private String marca;
	
	@NotNull
	@Column( length = 100)
	private String modelo;

	@NotNull
	@Column( length = 100)
	private String serie;
	
	@Column(name = "fecha_adquisicion", nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fechaAdquisicion;
	
	@NotNull
	@Column(unique = false)
	private float costo;
	
	@NotNull
	@Column(unique = false)
	private boolean depreciable;
	
	@NotNull
	@Column(name = "valor_residual",unique = false)
	private float valorResidual;
	
	@NotNull
	@Column(name = "valor_depreciar",unique = false)
	private float valorDepreciar;
	
	@NotNull
	@Column(name = "depresiacion_mensual",unique = false)
	private float depreciacionMensual;
	
	@NotNull
	@Column(name = "depresiacion_anaul",unique = false)
	private float depreciacionAnual;
	
	@NotNull
	@Column(name = "depresiacion_acumulada",unique = false)
	private float depreciacionAcumulada;
	
	@NotNull
	@Column(name = "valor_libros",unique = false)
	private float valorLibros;
	
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
	
	@Column(name = "asignado", length = 1)
	private String asignado;
	
	/*  Contructor con todos los parametros  */
	
	public Inventario(Tipo tipoId, CodigoHacienda haciendaId, @NotNull int correlativo, @NotNull String codigoIndividual,
			@NotNull String marca, @NotNull String modelo, @NotNull String serie, Date fechaAdquisicion,
			@NotNull float costo, @NotNull boolean depreciable, @NotNull float valorResidual,
			@NotNull float valorDepreciar, @NotNull float depreciacionMensual, @NotNull float depreciacionAnual,
			@NotNull float depreciacionAcumulada, @NotNull float valorLibros, Date createDate, String userCreate,
			Date updateDate, String userUpdate, String estado, String asignado) {
		super();
		this.tipoId = tipoId;
		this.haciendaId = haciendaId;
		this.correlativo = correlativo;
		this.codigoIndividual = codigoIndividual;
		this.marca = marca;
		this.modelo = modelo;
		this.serie = serie;
		this.fechaAdquisicion = fechaAdquisicion;
		this.costo = costo;
		this.depreciable = depreciable;
		this.valorResidual = valorResidual;
		this.valorDepreciar = valorDepreciar;
		this.depreciacionMensual = depreciacionMensual;
		this.depreciacionAnual = depreciacionAnual;
		this.depreciacionAcumulada = depreciacionAcumulada;
		this.valorLibros = valorLibros;
		this.createDate = createDate;
		this.userCreate = userCreate;
		this.updateDate = updateDate;
		this.userUpdate = userUpdate;
		this.estado = estado;
		this.asignado=asignado;
	}
	
	
	/////////////////////////////////////
	

	
	
	public Inventario() {
		super();
	}


	public Inventario(Tipo tipoId, CodigoHacienda haciendaId, @NotNull int correlativo,
			@NotNull String codigoIndividual, @NotNull String marca, @NotNull String modelo, @NotNull String serie,
			Date fechaAdquisicion, @NotNull float costo, @NotNull boolean depreciable, @NotNull float valorResidual,
			@NotNull float valorDepreciar, @NotNull float depreciacionMensual, @NotNull float depreciacionAnual,
			@NotNull float depreciacionAcumulada, @NotNull float valorLibros, Date createDate, String userCreate, String estado, String asignado) {
		super();
		this.tipoId = tipoId;
		this.haciendaId = haciendaId;
		this.correlativo = correlativo;
		this.codigoIndividual = codigoIndividual;
		this.marca = marca;
		this.modelo = modelo;
		this.serie = serie;
		this.fechaAdquisicion = fechaAdquisicion;
		this.costo = costo;
		this.depreciable = depreciable;
		this.valorResidual = valorResidual;
		this.valorDepreciar = valorDepreciar;
		this.depreciacionMensual = depreciacionMensual;
		this.depreciacionAnual = depreciacionAnual;
		this.depreciacionAcumulada = depreciacionAcumulada;
		this.valorLibros = valorLibros;
		this.createDate =createDate;
		this.userCreate = userCreate;
		this.estado = estado;
		this.asignado=asignado;
	}
	
	


	public String getAsignado() {
		return asignado;
	}


	public void setAsignado(String asignado) {
		this.asignado = asignado;
	}


	public int getInventarioId() {
		return inventarioId;
	}

	
	public void setInventarioId(int inventarioId) {
		this.inventarioId = inventarioId;
	}

	public int getCorrelativo() {
		return correlativo;
	}

	public void setCorrelativo(int correlativo) {
		this.correlativo = correlativo;
	}

	public String getCodigoIndividual() {
		return codigoIndividual;
	}

	public void setCodigoIndividual(String codigoIndividual) {
		this.codigoIndividual = codigoIndividual;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public Date getFechaAdquisicion() {
		return fechaAdquisicion;
	}

	public void setFechaAdquisicion(Date fechaAdquisicion) {
		this.fechaAdquisicion = fechaAdquisicion;
	}

	public float getCosto() {
		return costo;
	}

	public void setCosto(float costo) {
		this.costo = costo;
	}

	public boolean isDepreciable() {
		return depreciable;
	}

	public void setDepreciable(boolean depreciable) {
		this.depreciable = depreciable;
	}

	public float getValorResidual() {
		return valorResidual;
	}

	public void setValorResidual(float valorResidual) {
		this.valorResidual = valorResidual;
	}

	public float getValorDepreciar() {
		return valorDepreciar;
	}

	public void setValorDepreciar(float valorDepreciar) {
		this.valorDepreciar = valorDepreciar;
	}

	public float getDepreciacionMensual() {
		return depreciacionMensual;
	}

	public void setDepreciacionMensual(float depreciacionMensual) {
		this.depreciacionMensual = depreciacionMensual;
	}

	public float getDepreciacionAnual() {
		return depreciacionAnual;
	}

	public void setDepreciacionAnual(float depreciacionAnual) {
		this.depreciacionAnual = depreciacionAnual;
	}

	public float getDepreciacionAcumulada() {
		return depreciacionAcumulada;
	}

	public void setDepreciacionAcumulada(float depreciacionAcumulada) {
		this.depreciacionAcumulada = depreciacionAcumulada;
	}

	public float getValorLibros() {
		return valorLibros;
	}

	public void setValorLibros(float valorLibros) {
		this.valorLibros = valorLibros;
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

	public Tipo getTipoId() {
		return tipoId;
	}

	public void setTipoId(Tipo tipoId) {
		this.tipoId = tipoId;
	}

	public CodigoHacienda getHaciendaId() {
		return haciendaId;
	}

	public void setHaciendaId(CodigoHacienda haciendaId) {
		this.haciendaId = haciendaId;
	}
	
	
	

}
