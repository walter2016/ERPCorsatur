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

import sv.gob.corsatur.model.Gerencia;

@Repository
public interface IGerenciaRepository extends JpaRepository<Gerencia, Integer>  {
	
	Optional<Gerencia> findByNombre(String nombre);
	 boolean existsByNombre(String nombre);
	
	 @Query(value = "SELECT * FROM gerencia WHERE estado='A';", nativeQuery=true)
	    List<Gerencia> obtenerActivos();
	    
	    @Modifying
	    @Query(value = "UPDATE  gerencia SET estado='N', update_date=:updateDate,user_update=:userUpdate  WHERE gerencia_id=:gerenciaId", nativeQuery=true)
	    void eliminarGerencia(@Param("gerenciaId") int gerenciaId, @Param("updateDate") Date updateDate, @Param("userUpdate") String userUpdate);
	    
	    
	    @Query(value = "SELECT * FROM gerencia WHERE estado='A'",
	    	    countQuery = "SELECT count(*) FROM gerencia WHERE estado='A'",
	    	    nativeQuery = true)
	    	  Page<Gerencia> findGerencia(Pageable pageable);

}
