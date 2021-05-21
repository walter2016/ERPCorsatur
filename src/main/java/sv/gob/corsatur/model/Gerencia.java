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
public class Gerencia{

	@OneToMany(mappedBy = "gerenciaId", cascade = CascadeType.ALL)
	private List<AsignacionTransporte> asignaciones;
	
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gerencia_id", unique = true, nullable = false)
	private int gerenciaId;
	

	@NotNull
	@Column(name = "nombre", length = 50)
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
		updateDate = new Date();
	}

	@Column(name = "user_update", length = 100)
	private String userUpdate;

	@Column(name = "estado", length = 1)
	private String estado;

}
