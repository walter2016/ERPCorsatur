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

import sv.gob.corsatur.model.Empleado;
import sv.gob.corsatur.model.Usuario;
@Repository
public interface IEmpleadoRepository extends JpaRepository<Empleado, Integer>{
	
	@Query(value = "SELECT * FROM empleado WHERE estado='A';", nativeQuery=true)
    List<Empleado> obtenerActivos();
	
	 @Modifying
	    @Query(value = "UPDATE  empleado SET estado='N', update_date=:updateDate,user_update=:userUpdate  WHERE empleado_id=:empleadoId", nativeQuery=true)
	    void eliminarArea(@Param("empleadoId") int empleadoId, @Param("updateDate") Date updateDate, @Param("userUpdate") String userUpdate);
	 
	  @Query(value = "SELECT * FROM empleado WHERE estado='A' order by empleado_id asc",
	    	    countQuery = "SELECT count(*) FROM empleado WHERE estado='A'",
	    	    nativeQuery = true)
	    	  Page<Empleado> findEstado(Pageable pageable);
	  
	  
	  @Query(value = "SELECT * FROM empleado WHERE estado='A' and usuario_id=:usuario", nativeQuery=true)
	  Empleado obtenerEmpleadoPorUsuario(@Param("usuario") Usuario Usuario);
	 
}
