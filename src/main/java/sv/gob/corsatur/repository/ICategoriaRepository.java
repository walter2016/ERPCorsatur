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

import sv.gob.corsatur.model.Categoria;

@Repository
public interface ICategoriaRepository extends JpaRepository<Categoria, Integer>{
	
	Optional<Categoria> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);

    @Query(value = "SELECT * FROM categoria WHERE estado='A';", nativeQuery=true)
    List<Categoria> obtenerActivos();
    
    @Modifying
    @Query(value = "UPDATE  categoria SET estado='N', update_date=:updateDate,user_update=:userUpdate  WHERE categoria_id=:categoriaId", nativeQuery=true)
    void eliminarCategoria(@Param("categoriaId") int categoriaId, @Param("updateDate") Date updateDate, @Param("userUpdate") String userUpdate);
    
    
    @Query(value = "SELECT * FROM categoria WHERE estado='A'",
    	    countQuery = "SELECT count(*) FROM categoria WHERE estado='A'",
    	    nativeQuery = true)
    	  Page<Categoria> findEstado(Pageable pageable);

}
