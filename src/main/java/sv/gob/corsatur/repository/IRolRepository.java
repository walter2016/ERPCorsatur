package sv.gob.corsatur.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import sv.gob.corsatur.enums.RolNombre;
import sv.gob.corsatur.model.Rol;

@Repository
public interface IRolRepository extends JpaRepository<Rol, Integer>{
	Optional<Rol> findByRolNombre(RolNombre rolNombre);
    boolean existsByRolNombre(RolNombre rolNombre);
}
