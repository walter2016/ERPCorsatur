package sv.gob.corsatur.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sv.gob.corsatur.model.AsignacionTransporte;

public interface IAsignacionVehiculorepository extends JpaRepository<AsignacionTransporte, Integer>{
	
		    
	    @Modifying
	    @Query(value = "UPDATE  asignacion_transporte SET estado_solicitud='DENEGADA', update_date=:updateDate,user_update=:userUpdate  WHERE asigna_transporte_id=:asignacionTransporteId", nativeQuery=true)
	    void denegar(@Param("asignacionTransporteId") int asignacionTransporteId, @Param("updateDate") Date updateDate, @Param("userUpdate") String userUpdate);
	    
	    
	    @Query(value = "SELECT * FROM asignacion_transporte WHERE estado='A' and estado_solicitud in ('SOLICITADA', 'ACEPTADA','DENEGADA')",
	    	    countQuery = "SELECT count(*) FROM asignacion_transporte WHERE estado='A' and estado_solicitud in ('SOLICITADA', 'ACEPTADA','DENEGADA')",
	    	    nativeQuery = true)
	    	  Page<AsignacionTransporte> buscarSolicitudes(Pageable pageable);

	    
	    @Query(value = "SELECT * FROM asignacion_transporte WHERE estado='A' and user_create=:userCreate",
	    	    countQuery = "SELECT count(*) FROM asignacion_transporte WHERE estado='A' and user_create=:userCreate",
	    	    nativeQuery = true)
	    	  Page<AsignacionTransporte> buscarSolicitudesUsuario(Pageable pageable, @Param("userCreate") String userCreate);
	    
	    @Modifying
	    @Query(value = "UPDATE  asignacion_transporte SET estado_solicitud='FINALIZADA', update_date=:updateDate,user_update=:userUpdate  WHERE asigna_transporte_id=:asignacionTransporteId", nativeQuery=true)
	    void finalizar(@Param("asignacionTransporteId") int asignacionTransporteId, @Param("updateDate") Date updateDate, @Param("userUpdate") String userUpdate);
}
