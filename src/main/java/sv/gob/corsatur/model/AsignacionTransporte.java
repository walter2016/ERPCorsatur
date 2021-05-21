package sv.gob.corsatur.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	@JoinColumn(name = "motorista_id", nullable = false )
	private Motoristas motoristaId;
	
	@ManyToOne
	@JoinColumn(name = "inventarioVehiculo_id", nullable = false )
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
	private String persona_viajaran;
	
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
	
	@NotNull
	@Column(name = "observacion", length = 255)
	private String observacion;
	
	

}
