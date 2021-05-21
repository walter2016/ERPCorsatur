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

import sv.gob.corsatur.model.ClaseVehiculo;

@Repository
public interface IClaseVehiculoRepository extends JpaRepository<ClaseVehiculo, Integer> {
	Optional<ClaseVehiculo> findByClase(String clase);
	 boolean existsByClase(String clase);
	
	 @Query(value = "SELECT * FROM clase_vehiculo WHERE estado='A';", nativeQuery=true)
	    List<ClaseVehiculo> obtenerActivos();
	    
	    @Modifying
	    @Query(value = "UPDATE  clase_vehiculo SET estado='N', update_date=:updateDate,user_update=:userUpdate  WHERE clase_vehiculo_id=:claseVehiculoId", nativeQuery=true)
	    void eliminarArea(@Param("claseVehiculoId") int claseVehiculoId, @Param("updateDate") Date updateDate, @Param("userUpdate") String userUpdate);
	    
	    
	    @Query(value = "SELECT * FROM clase_vehiculo WHERE estado='A'",
	    	    countQuery = "SELECT count(*) FROM clase_vehiculo WHERE estado='A'",
	    	    nativeQuery = true)
	    	  Page<ClaseVehiculo> findEstado(Pageable pageable);

}
