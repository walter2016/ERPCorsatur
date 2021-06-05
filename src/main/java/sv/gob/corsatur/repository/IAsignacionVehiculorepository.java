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
	    @Query(value = "UPDATE  motoristas SET estado='N', disponible='N', update_date=:updateDate,user_update=:userUpdate  WHERE motorista_id=:motoristaId", nativeQuery=true)
	    void eliminarMotorista(@Param("motoristaId") int gerenciaId, @Param("updateDate") Date updateDate, @Param("userUpdate") String userUpdate);
	    
	    
	    @Query(value = "SELECT * FROM asignacion_transporte WHERE estado='A' and estado_solicitud in ('SOLICITADA', 'ACEPTADA')",
	    	    countQuery = "SELECT count(*) FROM asignacion_transporte WHERE estado='A' and estado_solicitud in ('SOLICITADA', 'ACEPTADA')",
	    	    nativeQuery = true)
	    	  Page<AsignacionTransporte> buscarSolicitudes(Pageable pageable);

}
