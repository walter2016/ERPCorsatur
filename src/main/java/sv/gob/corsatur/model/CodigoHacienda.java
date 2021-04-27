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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "cod_hacienda")
public class CodigoHacienda {
	
	@OneToMany(mappedBy = "haciendaId", cascade = CascadeType.ALL)
    private List<Inventario> inventarios;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "hacienda_id", unique = true, nullable = false)
	private int haciendaId;
	
	@NotNull
    @Column(unique=true,length = 10)
    private String codigo;

	@NotNull
    @Column(unique=true,length = 100)
    private String nombre;
	
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
	
	
	public CodigoHacienda(@NotNull String codigo, @NotNull String nombre, Date createDate, String userCreate,
			String estado) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.createDate = createDate;
		this.userCreate = userCreate;
		this.estado = estado;
	}
	public CodigoHacienda() {

	}
	
	
	
	public CodigoHacienda(@NotNull String codigo, @NotNull String nombre, String userCreate, String estado) {
	
		this.codigo = codigo;
		this.nombre = nombre;
		this.userCreate = userCreate;
		this.estado = estado;
	}
	
	
	public int getHaciendaId() {
		return haciendaId;
	}
	public void setHaciendaId(int haciendaId) {
		this.haciendaId = haciendaId;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
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
