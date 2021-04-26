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

import sv.gob.corsatur.model.Tipo;

@Repository
public interface ITipoRepository extends JpaRepository<Tipo, Integer> {
	
	Optional<Tipo> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);

    @Query(value = "SELECT * FROM tipo WHERE estado='A';", nativeQuery=true)
    List<Tipo> obtenerActivos();
    
    @Modifying
    @Query(value = "UPDATE  tipo SET estado='N', update_date=:updateDate,user_update=:userUpdate  WHERE tipo_id=:tipoId", nativeQuery=true)
    void eliminarTipo(@Param("tipoId") int tipoId, @Param("updateDate") Date updateDate, @Param("userUpdate") String userUpdate);
    
    
    @Query(value = "SELECT * FROM tipo WHERE estado='A'",
    	    countQuery = "SELECT count(*) FROM tipo WHERE estado='A'",
    	    nativeQuery = true)
    	  Page<Tipo> findEstado(Pageable pageable);

}
