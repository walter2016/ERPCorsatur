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

import sv.gob.corsatur.model.EstadoVehiculo;

@Repository
public interface IEstadoVehiculoRepository extends JpaRepository<EstadoVehiculo, Integer>{
	
	Optional<EstadoVehiculo> findByEstVehiculo(String estVehiculo);
	 boolean existsByEstVehiculo(String estVehiculo);
	
	 @Query(value = "SELECT * FROM estado_vehiculo WHERE estado='A';", nativeQuery=true)
	    List<EstadoVehiculo> obtenerActivos();
	    
	    @Modifying
	    @Query(value = "UPDATE  estado_vehiculo SET estado='N', update_date=:updateDate,user_update=:userUpdate  WHERE estado_vehiculo_id=:estadoVehiculoId", nativeQuery=true)
	    void eliminarEstadoVehiculo(@Param("estadoVehiculoId") int estadoVehiculoId, @Param("updateDate") Date updateDate, @Param("userUpdate") String userUpdate);
	    
	    
	    @Query(value = "SELECT * FROM estado_vehiculo WHERE estado='A'",
	    	    countQuery = "SELECT count(*) FROM estado_vehiculo WHERE estado='A'",
	    	    nativeQuery = true)
	    	  Page<EstadoVehiculo> findEstadoVehiculo(Pageable pageable);


}
