package sv.gob.corsatur.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sv.gob.corsatur.model.InventarioVehiculo;

@Repository
public interface IInventarioVehiculoRepository extends JpaRepository<InventarioVehiculo, Integer> {

	Optional<InventarioVehiculo> findByPlaca(String placa);

	boolean existsByPlaca(String placa);

	boolean existsByNumeroMotor(String numeroMotor);

	boolean existsByNumeroChasis(String numeroChasis);

	@Query(value = "SELECT * FROM inventario_vehiculo WHERE estado='A';", nativeQuery = true)
	List<InventarioVehiculo> obtenerActivos();

	@Query(value = "SELECT i.* FROM inventario_vehiculo i inner join estado_vehiculo e on i.estado_vehiculo_id = e.estado_vehiculo_id WHERE i.estado='A' and i.asignado='N' and i.aplica=1 and e.estado_vehiculo_id in (select estado_vehiculo_id from estado_vehiculo where est_vehiculo='DISPONIBLE') and i.clase_vehiculo_id=:claseVehiculoId", nativeQuery = true)
	List<InventarioVehiculo> obtenerVehiculoParaAsignar(@Param("claseVehiculoId") int claseVehiculoId);

	@Modifying
	@Query(value = "UPDATE  inventario_vehiculo SET estado='N', update_date=:updateDate,user_update=:userUpdate  WHERE inventario_vehiculo_id=:inventarioVehiculoId", nativeQuery = true)
	void eliminarInventarioVehiculo(@Param("inventarioVehiculoId") int inventarioVehiculoId,
			@Param("updateDate") Date updateDate, @Param("userUpdate") String userUpdate);

	@Query(value = "SELECT * FROM inventario_vehiculo WHERE estado='A'", countQuery = "SELECT count(*) FROM inventario_vehiculo WHERE estado='A'", nativeQuery = true)
	Page<InventarioVehiculo> findInventarioVehiculo(Pageable pageable);

	@Modifying
	@Query(value = "UPDATE  inventario_vehiculo SET asignado='N', update_date=:updateDate,user_update=:userUpdate  WHERE inventario_vehiculo_id=:inventarioVehiculoId", nativeQuery = true)
	void dejarDisponible(@Param("inventarioVehiculoId") int inventarioVehiculoId, @Param("updateDate") Date updateDate,
			@Param("userUpdate") String userUpdate);

}
