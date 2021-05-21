package sv.gob.corsatur.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import sv.gob.corsatur.model.Condicion;

@Repository
public interface ICondicionRepository extends JpaRepository<Condicion, Integer>{
	
	@Query(value = "SELECT * FROM condicion WHERE estado='A';", nativeQuery=true)
    List<Condicion> obtenerActivos();

}
