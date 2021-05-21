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

import sv.gob.corsatur.model.Solicitud;

@Repository
public interface ISolicitudRepository extends JpaRepository<Solicitud, Integer>{
	


    @Query(value = "SELECT * FROM solicitud WHERE estado='A';", nativeQuery=true)
    List<Solicitud> obtenerActivos();
    
    @Modifying
    @Query(value = "UPDATE  solicitud SET estado='N', update_date=:updateDate,user_update=:userUpdate  WHERE solicitud_id=:solicitudId", nativeQuery=true)
    void eliminarSolicutud(@Param("solicitudId") int solicitudId, @Param("updateDate") Date updateDate, @Param("userUpdate") String userUpdate);
    
    
    @Query(value = "SELECT * FROM solicitud WHERE estado='A'",
    	    countQuery = "SELECT count(*) FROM solicitud WHERE estado='A'",
    	    nativeQuery = true)
    	  Page<Solicitud> findSolicitudes(Pageable pageable);
    
    @Query(value = "select max(numero_solicitud) from solicitud", nativeQuery = true)
	int buscarNumeroSolicitud();

}
