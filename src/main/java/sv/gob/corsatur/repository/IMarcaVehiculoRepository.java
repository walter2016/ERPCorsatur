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

import sv.gob.corsatur.model.MarcaVehiculo;

@Repository
public interface IMarcaVehiculoRepository extends JpaRepository<MarcaVehiculo, Integer> {
	
	Optional<MarcaVehiculo> findByMarca(String marca);
	 boolean existsByMarca(String marca);
	
	 @Query(value = "SELECT * FROM marca_vehiculo WHERE estado='A';", nativeQuery=true)
	    List<MarcaVehiculo> obtenerActivos();
	    
	    @Modifying
	    @Query(value = "UPDATE  marca_vehiculo SET estado='N', update_date=:updateDate,user_update=:userUpdate  WHERE marca_vehiculo_id=:marcaVehiculoId", nativeQuery=true)
	    void eliminarArea(@Param("marcaVehiculoId") int marcaVehiculoId, @Param("updateDate") Date updateDate, @Param("userUpdate") String userUpdate);
	    
	    
	    @Query(value = "SELECT * FROM marca_vehiculo WHERE estado='A'",
	    	    countQuery = "SELECT count(*) FROM marca_vehiculo WHERE estado='A'",
	    	    nativeQuery = true)
	    	  Page<MarcaVehiculo> findMarcaVehiculo(Pageable pageable);


}
