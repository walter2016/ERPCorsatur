package sv.gob.corsatur.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.gob.corsatur.enums.RolNombre;
import sv.gob.corsatur.model.Gerencia;
import sv.gob.corsatur.model.Rol;
import sv.gob.corsatur.repository.IRolRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RolService {
	
	@Autowired
	IRolRepository rolRepository;
	
	public void save(Rol rol){
        rolRepository.save(rol);
    }

    public Optional<Rol> getByRolNombre(RolNombre rolNombre){
        return rolRepository.findByRolNombre(rolNombre);
    }

    public boolean existsByRolNombre(RolNombre rolNombre){
        return rolRepository.existsByRolNombre(rolNombre);
    }
    
    public List<Rol> obtenerRoles(int usuarioId){
		return rolRepository.obtenerRoles(usuarioId);
	}
    
    public List<Rol> obtenerTodosRoles(){
		return rolRepository.findAll();
	}

}
