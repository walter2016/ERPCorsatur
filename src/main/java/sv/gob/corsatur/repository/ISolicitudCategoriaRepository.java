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

import sv.gob.corsatur.model.SolicitudCategoria;

@Repository
public interface ISolicitudCategoriaRepository extends JpaRepository<SolicitudCategoria, Integer> {
	
	Optional<SolicitudCategoria> findByCategoria(String categoria);
    boolean existsByCategoria(String categoria);

    @Query(value = "SELECT * FROM solicitud_caregoria WHERE estado='A';", nativeQuery=true)
    List<SolicitudCategoria> obtenerActivos();
    
    @Modifying
    @Query(value = "UPDATE  solicitud_caregoria SET estado='N', update_date=:updateDate,user_update=:userUpdate  WHERE solic_cate_id=:solicCateId", nativeQuery=true)
    void eliminarSolicutudCategoria(@Param("solicCateId") int solicCateId, @Param("updateDate") Date updateDate, @Param("userUpdate") String userUpdate);
    
    
    @Query(value = "SELECT * FROM solicitud_caregoria WHERE estado='A'",
    	    countQuery = "SELECT count(*) FROM solicitud_caregoria WHERE estado='A'",
    	    nativeQuery = true)
    	  Page<SolicitudCategoria> findEstado(Pageable pageable);


}
