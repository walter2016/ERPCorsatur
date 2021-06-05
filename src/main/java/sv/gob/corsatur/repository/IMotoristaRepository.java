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

import sv.gob.corsatur.model.Motoristas;

public interface IMotoristaRepository extends JpaRepository<Motoristas, Integer>  {
	
	Optional<Motoristas> findByNombre(String nombre);
	 boolean existsByNombre(String nombre);
	
	 @Query(value = "SELECT * FROM motoristas WHERE estado='A';", nativeQuery=true)
	    List<Motoristas> obtenerActivos();
	    
	    @Modifying
	    @Query(value = "UPDATE  motoristas SET estado='N', disponible='N', update_date=:updateDate,user_update=:userUpdate  WHERE motorista_id=:motoristaId", nativeQuery=true)
	    void eliminarMotorista(@Param("motoristaId") int gerenciaId, @Param("updateDate") Date updateDate, @Param("userUpdate") String userUpdate);
	    
	    
	    @Query(value = "SELECT * FROM motoristas WHERE estado='A'",
	    	    countQuery = "SELECT count(*) FROM motoristas WHERE estado='A'",
	    	    nativeQuery = true)
	    	  Page<Motoristas> findGerencia(Pageable pageable);
	    
	    
	    @Query(value = "SELECT * FROM motoristas WHERE estado='A' and disponible='S';", nativeQuery=true)
	    List<Motoristas> obtenerSinAsignar();
	    
	    

	

}
