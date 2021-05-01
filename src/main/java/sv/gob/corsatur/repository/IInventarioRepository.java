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

import sv.gob.corsatur.model.Inventario;

@Repository
public interface IInventarioRepository extends JpaRepository<Inventario, Integer> {

	Optional<Inventario> findByCodigoIndividual(String codigo);

	boolean existsByCodigoIndividual(String codigo);

	@Query(value = "SELECT * FROM inventario WHERE estado='A';", nativeQuery = true)
	List<Inventario> obtenerActivos();

	@Modifying
	@Query(value = "UPDATE  inventario SET estado='N', update_date=:updateDate,user_update=:userUpdate  WHERE inventario_id=:inventarioId", nativeQuery = true)
	void eliminarArea(@Param("inventarioId") int inventarioId, @Param("updateDate") Date updateDate,
			@Param("userUpdate") String userUpdate);

	@Query(value = "SELECT * FROM inventario WHERE estado='A'", countQuery = "SELECT count(*) FROM inventario WHERE estado='A'", nativeQuery = true)
	Page<Inventario> findEstado(Pageable pageable);

	@Query(value = "select max(correlativo) from inventario where tipo_id=:tipoId and hacienda_id=:haciendaId", nativeQuery = true)
	int buscarCorrelativo(int tipoId, int haciendaId);
	
	@Query(value = "SELECT * FROM inventario WHERE estado='A' and asignado='N'", nativeQuery = true)
	List<Inventario> obtenerNoAsignado();
	
	@Modifying
	@Query(value="UPDATE  inventario SET asignado='S', update_date=:updateDate,user_update=:userUpdate  WHERE inventario_id=:inventarioId" , nativeQuery = true)
	void actualizarAsignacion(@Param("inventarioId") int inventarioId, @Param("updateDate") Date updateDate,
			@Param("userUpdate") String userUpdate);
	
	
	@Modifying
	@Query(value="UPDATE  inventario SET asignado='N', update_date=:updateDate,user_update=:userUpdate  WHERE inventario_id=:inventarioId" , nativeQuery = true)
	void actualizarDescargo(@Param("inventarioId") int inventarioId, @Param("updateDate") Date updateDate,
			@Param("userUpdate") String userUpdate);
	
	@Query(value = "SELECT asignado FROM inventario WHERE inventario_id=:inventarioId", nativeQuery = true)
	String estasAsignado(@Param("inventarioId") int inventarioId);

}
