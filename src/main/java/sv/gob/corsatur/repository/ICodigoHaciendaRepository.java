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

import sv.gob.corsatur.model.CodigoHacienda;

@Repository
public interface ICodigoHaciendaRepository extends JpaRepository<CodigoHacienda, Integer> {
	
	Optional<CodigoHacienda> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);

    @Query(value = "SELECT * FROM cod_hacienda WHERE estado='A';", nativeQuery=true)
    List<CodigoHacienda> obtenerActivos();
    
    @Modifying
    @Query(value = "UPDATE cod_hacienda SET estado='N', update_date=:updateDate,user_update=:userUpdate  WHERE hacienda_id=:haciendaId", nativeQuery=true)
    void eliminarCodigoHacienda(@Param("haciendaId") int haciendaId, @Param("updateDate") Date updateDate, @Param("userUpdate") String userUpdate);
    
    
    @Query(value = "SELECT * FROM cod_hacienda WHERE estado='A'",
    	    countQuery = "SELECT count(*) FROM cod_hacienda WHERE estado='A'",
    	    nativeQuery = true)
    	  Page<CodigoHacienda> findEstado(Pageable pageable);
	

}
