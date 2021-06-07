package sv.gob.corsatur.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sv.gob.corsatur.enums.RolNombre;
import sv.gob.corsatur.model.Rol;

@Repository
public interface IRolRepository extends JpaRepository<Rol, Integer>{
	Optional<Rol> findByRolNombre(RolNombre rolNombre);
    boolean existsByRolNombre(RolNombre rolNombre);
    
    @Query(value = "select r.* from rol r INNER join usuario_rol u on r.id = u.rol_id where u.usuario_id =:usuarioId", nativeQuery=true)
    List<Rol> obtenerRoles(@Param("usuarioId") int usuarioId);
    
   
}
