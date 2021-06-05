package sv.gob.corsatur.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.gob.corsatur.model.Motoristas;
import sv.gob.corsatur.repository.IMotoristaRepository;
@Service
@Transactional
public class MotoristaService {
	
	@Autowired
	private IMotoristaRepository motoristaRepository;
	
	
	public Optional<Motoristas> getOne(int id) {
		return motoristaRepository.findById(id);
	}
	
	public void save(Motoristas motoristas) {
		motoristaRepository.save(motoristas);
	}
	
	public boolean existsByNombre(String motoristas) {
		return motoristaRepository.existsByNombre(motoristas);
	}

	public Optional<Motoristas> getByNombre(String nombre) {
		return motoristaRepository.findByNombre(nombre);
	}
	
	public boolean existsById(int id) {
		return motoristaRepository.existsById(id);
	}
	
	public List<Motoristas> obtenerActivos(){
		return motoristaRepository.obtenerActivos();
	}
	
	public List<Motoristas> obtenerSinAsignacion(){
		return motoristaRepository.obtenerSinAsignar();
	}
	
	
	public void eliminar(int motoristaId, Date updateDate, String userUpdate) {
		motoristaRepository.eliminarMotorista(motoristaId, updateDate, userUpdate);
	}
	
	public Page<Motoristas> obtenerActivos(Pageable pageable){
		return motoristaRepository.findGerencia(pageable);
	}


}
