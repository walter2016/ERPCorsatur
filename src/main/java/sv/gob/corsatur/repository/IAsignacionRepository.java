package sv.gob.corsatur.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sv.gob.corsatur.model.Asignacion;

@Repository
public interface IAsignacionRepository extends JpaRepository<Asignacion, Integer> {

	@Query(value = "SELECT * FROM asignacion WHERE estado='A';", nativeQuery = true)
	List<Asignacion> obtenerActivos();

	@Modifying
	@Query(value = "UPDATE  asignacion SET estado='N', update_date=:updateDate,user_update=:userUpdate  WHERE asignacion_id=:asignacionId", nativeQuery = true)
	void eliminarAsignacion(@Param("asignacionId") int asignacionId, @Param("updateDate") Date updateDate,
			@Param("userUpdate") String userUpdate);

	@Query(value = "SELECT a.* FROM asignacion a inner join inventario i on a.inventario_id=i.inventario_id WHERE a.estado='A' and a.tipo_asignacion='ASIGNACION' and i.asignado='S'", countQuery = "SELECT count(*) FROM asigancion WHERE estado='A'", nativeQuery = true)
	Page<Asignacion> obtenerAsignacionPaginados(Pageable pageable);

}
