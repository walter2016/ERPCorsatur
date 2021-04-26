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

import sv.gob.corsatur.model.Area;

@Repository
public interface IAreaRepository extends JpaRepository<Area, Integer>{
	Optional<Area> findByNombre(String nombre);
    boolean existsByNombre(String nombre);

    @Query(value = "SELECT * FROM area WHERE estado='A';", nativeQuery=true)
    List<Area> obtenerActivos();
    
    @Modifying
    @Query(value = "UPDATE  area SET estado='N', update_date=:updateDate,user_update=:userUpdate  WHERE area_id=:areaId", nativeQuery=true)
    void eliminarArea(@Param("areaId") int areaId, @Param("updateDate") Date updateDate, @Param("userUpdate") String userUpdate);
    
    
    @Query(value = "SELECT * FROM area WHERE estado='A'",
    	    countQuery = "SELECT count(*) FROM area WHERE estado='A'",
    	    nativeQuery = true)
    	  Page<Area> findEstado(Pageable pageable);
    
    
}
