package sv.gob.corsatur.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import sv.gob.corsatur.model.Configuracion;

@Repository
public interface IConfiguracionRepository extends JpaRepository<Configuracion, Integer> {
	
	Optional<Configuracion> findByCodigoClasificacion(String codigo);
    boolean existsByCodigoClasificacion(String codigo);
    
    
    @Query(value = "SELECT * FROM configuracion WHERE estado='A'",
    	    countQuery = "SELECT count(*) FROM configuracion WHERE estado='A'",
    	    nativeQuery = true)
    	  Page<Configuracion> findEstado(Pageable pageable);
	
	

}
